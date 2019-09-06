package org.zj.interfaceinvoke.base.invoke;

import org.zj.interfaceinvoke.base.Constants;
import org.zj.interfaceinvoke.base.bean.InterfaceInvoke;
import org.zj.interfaceinvoke.base.bean.InterfaceInvokeParam;
import org.zj.interfaceinvoke.base.bean.RequestParam;
import org.zj.interfaceinvoke.base.operate.IParamHandle;
import org.zj.interfaceinvoke.base.operate.IResultHandle;
import org.zj.interfaceinvoke.base.util.ReflectionUtil;
import org.zj.interfaceinvoke.base.util.data.DBUtil;
import org.zj.interfaceinvoke.base.util.http.HttpUtil;

import javax.management.relation.Relation;
import java.lang.reflect.Field;
import java.sql.Ref;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ProjectName: interface-invoke
 * @ClassName: InterfaceInvoker
 * @Description: 接口调用器
 * @Author: ZhangJun
 * @Date: 2019/9/6 17:32
 */
public class InterfaceInvoker {
    /**
     * 要调用的接口的id
     */
    private String interfaceId;
    /**
     * 我们这边的业务号
     */
    private String businessId;
    /**
     * 请求的信息封装
     */
    private List<RequestParam> requestParam=new ArrayList<>();
    /**
     * 参数处理器
     */
    private IParamHandle paramHandle;
    /**
     * 对结果进行处理
     */
    private IResultHandle resultHandle;
    /**
     * json返回结果
     */
    private List<String> jsonResponse;

    public InterfaceInvoker(String interfaceId) {
        this.interfaceId = interfaceId;
    }

    /**
     * 设置业务号
     *
     * @param businessId
     * @return
     */
    public InterfaceInvoker setBusinessId(String businessId) {
        this.businessId = businessId;
        return this;
    }

    /**
     * 设置参数处理器
     *
     * @param paramHandler
     * @return
     */
    public InterfaceInvoker setParamHandler(IParamHandle paramHandler) {
        this.paramHandle = paramHandler;
        return this;
    }

    /**
     * 设置结果处理器
     *
     * @param resultHandler
     * @return
     */
    public InterfaceInvoker setResultHandler(IResultHandle resultHandler) {
        this.requestParam = requestParam;
        return this;
    }

    public void handle() {
        //请求参数处理，然后发送http请求，解析响应
        getReadyForRequestParam();
        //请求接口来获得数据
        System.out.println(requestParam.size()+"几个需要发送");
        requestData();
        //处理响应
        handleResponse();
    }

    private void handleResponse() {
        //处理响应的数据
        if (resultHandle != null) {
            for (String resp : jsonResponse) {
                resultHandle.handleResult(resp);
            }

        }
    }

    private void requestData() {
        //直接在这里请求数据
        for (RequestParam b : requestParam) {
            //参数准备完毕，接下来就是发送请求;
            System.out.println(b);
        }
    }


    /**
     * 准备数据
     */
    private void getReadyForRequestParam() {
        inflateRequestParam();
        checkHandleRequestParam();
    }

    /**
     * 检测数据是否需要进行加密，对数据进行接下来的操作
     */
    private void checkHandleRequestParam() {
        if (paramHandle != null) {
            for (RequestParam b : requestParam) {
                paramHandle.handleParam(b);
            }

        }
    }

    /**
     * 我要去表里面准备数据,这里会比较麻烦
     */
    private void inflateRequestParam() {
        RequestParam<Object> requestParam = new RequestParam<>();
        //从主表获得数据，塞进去
        String sql = "select * from zj_interface_invoke_task where id='" + this.interfaceId + "'";
        System.out.println(sql);
        //从详情表获得参数，塞进去
        List<Map> result = DBUtil.getResult(sql);
        if(result==null){
            System.out.println("未获得结果，未找到invoke_task 中 id 为"+interfaceId);
            return;
        }
        //我需要把这个进行类型转换
        List<InterfaceInvoke> convert = DBUtil.convert(result, InterfaceInvoke.class);
        //接下来获得详细数据
        Map<InterfaceInvoke, List<InterfaceInvokeParam>> needParseMap = new HashMap<>();
        for (InterfaceInvoke bean : convert) {
            String id = bean.getId();
            String queryParam = "select * from zj_interface_invoke_param where id='" + id + "'";
            List<Map> result1 = DBUtil.getResult(queryParam);
            List<InterfaceInvokeParam> convert1 = DBUtil.convert(result1, InterfaceInvokeParam.class);
            parseAndInflateRequestParam(bean, convert1);
        }
    }

    /**
     * 解析并且把数据填入到里面
     *
     * @param bean
     * @param convert1
     */
    private void parseAndInflateRequestParam(InterfaceInvoke bean, List<InterfaceInvokeParam> convert1) {
        RequestParam<Object> requestParam = new RequestParam<>();
        requestParam.setUrl(bean.getInvokeUrl());
        requestParam.setMethod(bean.getInvokeMethod());
        Map<String, Object> jsonParamMap = new HashMap<>();
        Map<String, String> formMap = new HashMap<>();
        Map<String, String> headMap = new HashMap<>();
        Map<String, Map> tableData = new HashMap<>();
        //接下来就是参数的填充了
        for (InterfaceInvokeParam b : convert1) {
            String currentDataType = b.getCurrentDataType();
            String needDataType = b.getNeedDataType();
            if (currentDataType != null && needDataType != null) {
                if (Constants.STRING.equals(currentDataType) && Constants.STRING.equals(needDataType)) {
                    //接下来就判断放到哪里去
                    String paramType = b.getParamType();
                    if (paramType == null) {
                        continue;
                    }
                    if (Constants.JSON.equals(paramType)) {
                        //这个参数要放到json里面
                        inflateToJson(jsonParamMap, b, tableData);
                    }
                    if (Constants.FORM.equals(paramType)) {
                        formMap.put(b.getParamFieldName(), b.getDataValue());
                    }
                    if (Constants.HEAD.equals(paramType)) {
                        headMap.put(b.getParamFieldName(), b.getDataValue());
                    }
                }
            }
        }
        requestParam.addHead(headMap);
        requestParam.addForm(formMap);
        requestParam.setJsonParam(new ArrayList<>(jsonParamMap.values()));
        this.requestParam.add(requestParam);
    }

    /**
     * 这里要根据类全名反射得到类，然后塞数据进去
     *
     * @param jsonParamMap
     * @param b
     */
    private void inflateToJson(Map<String, Object> jsonParamMap, InterfaceInvokeParam b, Map<String, Map> tableData) {
        String dataRef = b.getDataRef();
        String paramFieldName = b.getParamFieldName();
        //如果定义的参数没有表示，那也返回
        if (paramFieldName == null || "".equals(paramFieldName)) {
            System.out.println("没有定义字段");
            return;
        }
        //如果定义的表中字段没有就返回
        if (dataRef == null || "".equals(dataRef)) {
            System.out.println("没有定义表字段");
            return;
        }
        //表中的表示地址如果没有按照规则，也返回
        String[] split = dataRef.split("\\.");
        if (split == null || split.length <= 1) {
            System.out.println("表地址没按规则");
            return;
        }
        //获得表名
        String tableName = split[0];
        //看能不能得到缓存的表数据
        Map tableDataMap = tableData.get(tableName);
        //如果获得数据了，那我就直接从这里面得到
        if (tableDataMap == null) {
            //如果没有得到缓存的表数据，说明是第一次从这个表里获得数据，我要先查一遍
            List<Map> maps = queryTableDataWithBusinessId(tableName);
            if (maps == null || maps.size() <= 0) {
                return;
            }
            tableData.put(tableName, maps.get(0));
        }

        //然后我需要把数据塞进去，这个塞进去还要是递归的
        //直接获得这个字段的值
        Object o = tableData.get(split[0]).get(split[1]);
        //获得数据了，接下来我需要把数据填进去
        String className = b.getClassName();
        if (className == null || "".equals(className)) {
            return;
        }
        Object o1 = jsonParamMap.get(className);
        if (o1 == null) {
            Object o2 = ReflectionUtil.newInstance(className);
            jsonParamMap.put(className,o2);
        }
        //接下来都有对象了，我需要递归把数据放进去
        recursiveInflateData(jsonParamMap.get(className),b.getParamFieldName(),o);
    }

    /**
     * 递归填充数据
     * @param obj
     * @param fileNameMultilayer
     * @param val
     */
    private void recursiveInflateData(Object obj,String fileNameMultilayer, Object val) {
        StringBuilder sb=new StringBuilder();
        String[] split = fileNameMultilayer.split("\\.");
        if(split.length==1){
            ReflectionUtil.setValue(obj,split[0],val);
            return;
        }else{
            //试着剥开一层
            String s = split[0];
            Field field;
            System.out.println(obj);
            field = ReflectionUtil.getField(obj.getClass(), s);

            //先初始化那个对象，然后再设置进去
            Object o = ReflectionUtil.instanceField(field);
            String substring = fileNameMultilayer.substring(fileNameMultilayer.indexOf(".") + 1);
            sb.append(substring);
            System.out.println(substring);
            recursiveInflateData(o,substring,val);
            //内层走完，到外面直接设置进去
            ReflectionUtil.setValue(obj,field,o);
        }
    }

    private List<Map> queryTableDataWithBusinessId(String tableName) {
        String sql = "select * from " + tableName + " where business_id='" + businessId + "'";
        List<Map> result = DBUtil.getResult(sql);
        return result;
    }

    public static void main(String[] args) {
      /*  String str="ajsdfa";
        System.out.println(str.split("\\.").length);

        List<Map> result = DBUtil.getResult("select * from zj_interface_invoke_task");
        List<InterfaceInvoke> convert = DBUtil.convert(result, InterfaceInvoke.class);
        for (InterfaceInvoke b : convert) {

        }*/
      /*  Student student = new Student();
        new InterfaceInvoker("").recursiveInflateData(student,"stu.name","zhangsan");
        System.out.println(student.getStu().getName());*/
        new DBUtil().setUrl(" jdbc:mysql://localhost:3306/somethinginteresing?useUnicode=true&characterEncoding=utf-8&allowMultiQueries=true&useSSL=false&serverTimezone=Asia/Shanghai")
                .setUsername("root")
                .setDriverName("com.mysql.jdbc.Driver")
                .generateDataSource();
        new InterfaceInvoker("1").setBusinessId("1").handle();

       /* Student student=new Student();
        Class<? extends Student> aClass = student.getClass();
        System.out.println(aClass);
        try {
            Field stu = student.getClass().getDeclaredField("stu");
            Object obj = stu.getDeclaringClass().newInstance();
            Field stu1 = obj.getClass().getDeclaredField("stu");

        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }
}
