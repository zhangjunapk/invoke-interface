package org.zj.interfaceinvoke.base.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ProjectName: interface-invoke
 * @ClassName: RequestParam
 * @Description: 请求的通用参数
 * @Author: ZhangJun
 * @Date: 2019/9/6 17:20
 */
public class RequestParam<T> {
    /**
     * 保存请求头的信息
     */
    private Map<String,String> headMap=new HashMap<>();
    /**
     * 保存表单参数的信息
     */
    private List<Map<String,String>> formMapList=new ArrayList<>();
    /**
     * json参数
     */
    private  List<T> jsonParam;
    /**
     * 请求的方法
     */
    private String method;
    /**
     * 你要请求哪个url
     */
    private String url;
    /**
     * 处理之后的json参数
     */
    private String handledJsonParam;




    public List<Map<String, String>> getFormMapList() {
        return formMapList;
    }

    public RequestParam setFormMapList(List<Map<String, String>> formMapList) {
        this.formMapList = formMapList;
        return this;
    }

    public String getHandledJsonParam() {
        return handledJsonParam;
    }

    public RequestParam setHandledJsonParam(String handledJsonParam) {
        this.handledJsonParam = handledJsonParam;
        return this;
    }

    /**
     * 添加头信息
     * @param map
     * @return
     */
    public RequestParam addHead(Map<String,String> map){
        headMap.putAll(map);
        return this;
    }


    /**
     * 设置头
     * @param headMap
     * @return
     */
    public RequestParam setHead(Map<String,String> headMap){
        this.headMap=headMap;
        return this;
    }

    /**
     * 添加表单信息
     * @param map
     * @return
     */
    public RequestParam addForm(Map<String,String> map){
        if(map!=null&&!map.isEmpty()){
            formMapList.add(map);
        }
        return this;
    }

    /**
     * 添加表单集合
     * @param formMapList
     * @return
     */
    public RequestParam addForm(List<Map<String,String>> formMapList){
        this.formMapList.addAll(formMapList);
        return this;
    }

    /**
     * 设置表单
     * @param formMapList
     * @return
     */
    public RequestParam setForm(List<Map<String,String>> formMapList){
        this.formMapList=formMapList;
        return this;
    }



    public Map<String, String> getHeadMap() {
        return headMap;
    }

    public RequestParam setHeadMap(Map<String, String> headMap) {
        this.headMap = headMap;
        return this;
    }

    public List<T> getJsonParam() {
        return jsonParam;
    }

    public RequestParam setJsonParam(List<T> jsonParam) {
        this.jsonParam = jsonParam;
        return this;
    }

    public String getMethod() {
        return method;
    }

    public RequestParam setMethod(String method) {
        this.method = method;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public RequestParam setUrl(String url) {
        this.url = url;
        return this;
    }
}
