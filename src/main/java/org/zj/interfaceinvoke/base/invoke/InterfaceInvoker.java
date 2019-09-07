package org.zj.interfaceinvoke.base.invoke;

import com.alibaba.fastjson.JSONObject;
import org.zj.interfaceinvoke.base.Constants;
import org.zj.interfaceinvoke.base.bean.InterfaceInvoke;
import org.zj.interfaceinvoke.base.bean.InterfaceInvokeParam;
import org.zj.interfaceinvoke.base.bean.RequestParam;
import org.zj.interfaceinvoke.base.operate.IParamHandle;
import org.zj.interfaceinvoke.base.operate.IResultHandle;
import org.zj.interfaceinvoke.base.util.ReflectionUtil;
import org.zj.interfaceinvoke.base.util.data.DBUtil;
import org.zj.interfaceinvoke.base.util.http.HttpUtil;
import org.zj.interfaceinvoke.business.test.bean.RequestHlzApply;

import javax.management.relation.Relation;
import javax.swing.*;
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

    public String getInterfaceId() {
        return interfaceId;
    }

    public InterfaceInvoker setInterfaceId(String interfaceId) {
        this.interfaceId = interfaceId;
        return this;
    }

    public String getBusinessId() {
        return businessId;
    }

    public List<RequestParam> getRequestParam() {
        return requestParam;
    }

    public InterfaceInvoker setRequestParam(List<RequestParam> requestParam) {
        this.requestParam = requestParam;
        return this;
    }

    public IParamHandle getParamHandle() {
        return paramHandle;
    }

    public InterfaceInvoker setParamHandle(IParamHandle paramHandle) {
        this.paramHandle = paramHandle;
        return this;
    }

    public IResultHandle getResultHandle() {
        return resultHandle;
    }

    public InterfaceInvoker setResultHandle(IResultHandle resultHandle) {
        this.resultHandle = resultHandle;
        return this;
    }

    public List<String> getJsonResponse() {
        return jsonResponse;
    }

    public InterfaceInvoker setJsonResponse(List<String> jsonResponse) {
        this.jsonResponse = jsonResponse;
        return this;
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
            System.out.println(JSONObject.toJSONString(b));

            String handle = new HttpUtil()
                    .url(b.getUrl())
                    .addJsonParamString(b.getHandledJsonParam())
                    .handle();
            System.out.println(handle);
            jsonResponse.add(handle);
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
            checkAndInflateParamHandler(bean);
            parseAndInflateRequestParam(bean, convert1);
            inflateResultHandler(bean);
        }
    }

    private void inflateResultHandler(InterfaceInvoke bean) {
        String responseHandlerRef = bean.getResponseHandlerRef();
        if(responseHandlerRef==null||"".equals(responseHandlerRef)){
            return;
        }
        Object o = ReflectionUtil.newInstance(responseHandlerRef);
        if(o!=null && o instanceof IResultHandle){
            setResultHandler((IResultHandle) o);
        }
    }

    private void checkAndInflateParamHandler(InterfaceInvoke bean) {
        String paramHandlerRef = bean.getParamHandlerRef();
        if(paramHandlerRef!=null){
            Object o = ReflectionUtil.newInstance(paramHandlerRef);
            if(o!=null&&o instanceof IParamHandle){
                setParamHandler((IParamHandle) o);
            }
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

        Map<String,Object> fieldInstanceMap=new HashMap<>();
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
                        inflateToJson(fieldInstanceMap,jsonParamMap, b, tableData);
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
    private void inflateToJson(Map<String,Object> fieldInstanceMap,Map<String, Object> jsonParamMap, InterfaceInvokeParam b, Map<String, Map> tableData) {
        String dataRef = b.getDataRef();
        String dataValue = b.getDataValue();
        String paramFieldName = b.getParamFieldName();
        //如果定义的参数没有表示，那也返回
        if (paramFieldName == null || "".equals(paramFieldName)) {
            System.out.println("没有定义字段");
            return;
        }
        //要把这个值塞进去
        Object o = null;
        //先看表地址是否为空
        if(dataRef!=null){
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
            o = tableData.get(split[0]).get(split[1]);
        }else if(dataValue!=null){
            //如果具体的值不为空，那就塞进去
            o=dataValue;
        }
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
        /**
         * 里面存放字段的实例，因为可能会出现多重赋值的情况
         */

        setVal(new StringBuilder(),fieldInstanceMap,b.getParamFieldName(),jsonParamMap.get(className),o);

        //recursiveInflateData(new HashMap<>(),jsonParamMap.get(className),b.getParamFieldName(),o);

        System.out.println(jsonParamMap.get(className)+" 操作结束 "+b.getParamFieldName());
    }

    /**
     * 递归填充数据
     * @param obj
     * @param fileNameMultilayer
     * @param val
     */
    private void recursiveInflateData(Map<String,Object> member,Object obj,String fileNameMultilayer, Object val) {
        StringBuilder sb=new StringBuilder();
        String[] split = fileNameMultilayer.split("\\.");
        System.out.println("字段名->"+split[0]);
        if(split.length==1){
            ReflectionUtil.setValue(obj,split[0],val);
            return;
        }else{
            //试着剥开一层
            String s = split[0];
            Field field;
            System.out.println(obj);
            field = ReflectionUtil.getField(obj.getClass(), s);
            if(member.get(sb.toString())==null){
                Object o = ReflectionUtil.instanceField(field);
                member.put(sb.toString(),o);
            }
            //先初始化那个对象，然后再设置进去
            System.out.println("剥开一层:-->"+sb.toString());
            String substring = fileNameMultilayer.substring(fileNameMultilayer.indexOf(".") + 1);
            sb.append(substring);
            System.out.println(substring);
            recursiveInflateData(member,member.get(sb.toString()),substring,val);
            //内层走完，到外面直接设置进去
            ReflectionUtil.setValue(obj,field,member.get(sb.toString()));
        }
    }
    /**
     * 多层赋值
     * @param fieldNameMultilayer
     * @param obj
     * @param value
     */
    public static void setVal(StringBuilder sbCurrentFieldName,Map<String,Object> fieldInstanceMap,String fieldNameMultilayer,Object obj,Object value){
        //设置值
        String[] split = fieldNameMultilayer.split("\\.");
        Object o=null;
        if(split.length==1){
            //到头了，我需要
            ReflectionUtil.setValue(obj,split[0],value);
        }else{
            sbCurrentFieldName.append(split[0]).append(".");
            o=fieldInstanceMap.get(sbCurrentFieldName.toString());
            if(o==null){
                o = ReflectionUtil.instanceField(ReflectionUtil.getField(obj.getClass(), split[0]));
                fieldInstanceMap.put(sbCurrentFieldName.toString(),o);
            }
            ReflectionUtil.setValue(obj,split[0],o);
            //剥开一层
            System.out.println("--->"+sbCurrentFieldName);
            setVal(sbCurrentFieldName,fieldInstanceMap,fieldNameMultilayer.substring(fieldNameMultilayer.indexOf(".")+1),o,value);
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
        new DBUtil().setUrl("jdbc:mysql://localhost:3306/somethinginteresing?useUnicode=true&characterEncoding=utf-8&allowMultiQueries=true&useSSL=false&serverTimezone=Asia/Shanghai")
                .setUsername("root")
                .setDriverName("com.mysql.jdbc.Driver")
                .generateDataSource();
        new InterfaceInvoker("1").setBusinessId("1").handle();
/*
        Field field = ReflectionUtil.getField(RequestHlzApply.class, "userInfo");

        //先初始化那个对象，然后再设置进去
        Object o = ReflectionUtil.instanceField(field);
        System.out.println(o);*/
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
