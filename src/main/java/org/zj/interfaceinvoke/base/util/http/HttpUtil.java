package org.zj.interfaceinvoke.base.util.http;


import com.alibaba.fastjson.JSONObject;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.util.Map;

/**
 * @BelongsProject: appService
 * @BelongsPackage: com.zhongjin.utils.http.util
 * @Author: ZhangJun
 * @CreateTime: 2019/2/28
 * @Description: 网络请求的工具类
 */

public class HttpUtil {
    public static final okhttp3.MediaType JSON_FORMATE = okhttp3.MediaType.parse("application/json; charset=utf-8");

    private METHOD method;

    private String url;

    private Map<String, String> headMap;

    private Map<String, String> formParamMap;
    private String formKeyVal;

    private Map<String, String> jsonParamMap;
    private String jsonParamString;
    private Object obj;

    private HttpExceptionCallback exceptionCallback;
    private HttpObjectResponceCallback objectResponceCallback;

    public HttpUtil addFormParam(Map<String, String> paramMap) {
        this.formParamMap = paramMap;
        return this;
    }

    public HttpUtil addFormParamString(String keyval) {
        this.formKeyVal = keyval;
        return this;
    }

    public HttpUtil addJsonParamMap(Map<String, String> jsonParamMap) {
        this.jsonParamMap = jsonParamMap;
        return this;
    }

    public HttpUtil addJsonParamString(String jsonParamString) {
        this.jsonParamString = jsonParamString;
        return this;
    }

    public HttpUtil addJsonParamObject(Object obj) {
        this.obj = obj;
        return this;
    }

    public HttpUtil setHeaderMap(Map<String, String> headMap) {
        this.headMap = headMap;
        return this;
    }

    public HttpUtil setExceptionCallback(HttpExceptionCallback callback) {
        this.exceptionCallback = callback;
        return this;
    }

    public <T> HttpUtil setResponseObjectCallback(HttpObjectResponceCallback<T> callback) {
        this.objectResponceCallback = callback;
        return this;
    }

    public String handle() {
        if (url == null || "".equals(url) || !url.startsWith("http")) {
            System.out.println("url有问题:->" + url);
            return null;
        }
        String formParamStr = generateFormParamKeyval();
        String jsonParamStr = generateJsonParam();
        System.out.println("json参数:--->"+jsonParamStr);
        Request request = generateRequest(formParamStr, jsonParamStr);
        String method = request.method();
        System.out.println("请求方法:--->"+method);
        System.out.println("url:--->"+request.url());
        String respStr = null;
        try {
            Response execute = ClientGenerater.getInstance().newCall(request).execute();
            respStr = execute.body().string();
            System.out.println("获得响应:---->"+respStr);
            if (objectResponceCallback != null) {
                objectResponceCallback.handleResponse(JSONObject.parseObject(respStr, InterfaceUtil.getGenericity(objectResponceCallback, 0)));
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (exceptionCallback != null) {
                exceptionCallback.handleException(e);
                return null;
            }
        }
        return respStr;
    }

    private Request generateRequest(String formParamStr, String jsonParamStr) {
        Request.Builder builder = new Request.Builder();
        if(this.method!=null){
            if(method== METHOD.GET) {
                builder.method("GET",null);
            }
            if(method== METHOD.POST){
                builder.method("POST",null);
            }
        }
        //头放进去
        if (headMap != null && headMap.size() != 0) {
            for (Map.Entry<String, String> entry : headMap.entrySet()) {
                builder.header(entry.getKey(), entry.getValue());
            }
        }
        //json参数放进去
        if (jsonParamStr != null && !"".equals(jsonParamStr)&&!"{}".equals(jsonParamStr)) {
            RequestBody requestBody = FormBody.create(JSON_FORMATE
                    , jsonParamStr);
            builder.post(requestBody);
        }
        //form参数放进去
        if (formParamStr != null && !"".equals(formParamStr)) {
            if (formParamStr.startsWith("?")) {
                builder.url(this.url + formParamStr);
                return builder.build();
            }
            builder.url(this.url + "?" + formParamStr);
            return builder.build();
        }else{
            builder.url(this.url);
        }

        return builder.build();
    }

    /**
     * 这里遍历所有传过来的东西，拼接
     *
     * @return
     */
    private String generateJsonParam() {
        StringBuilder sb = new StringBuilder();
        //obj
        if (obj != null) {
            appendJson(sb, JSONObject.toJSONString(obj));
        }
        //jsonStr
        if (jsonParamString != null && !"".equals(jsonParamString)) {
            appendJson(sb, jsonParamString);
        }
        //jsonMap
        if (jsonParamMap != null && jsonParamMap.size() != 0) {
            String s = JSONObject.toJSONString(jsonParamMap);
            appendJson(sb, s);
        }
        return "{"+sb.toString()+"}";
    }

    /**
     * 添加进去
     *
     * @param sb
     * @param toJSONString
     */
    private void appendJson(StringBuilder sb, String toJSONString) {
        if(toJSONString==null||"".equals(toJSONString)){
            return;
        }
        String substring = toJSONString.substring(0, 1);
        if(substring!=null&&substring.equals("{")){
            toJSONString=toJSONString.substring(1);
        }
        if(toJSONString.endsWith("}")){
            toJSONString=toJSONString.substring(0,toJSONString.length()-1);
        }
        if(sb!=null&&sb.length()!=0) {
            sb.append(",").append(toJSONString);
            return;
        }
        sb.append(toJSONString);
    }

    /**
     * 生成表单传值的键值对
     *
     * @return
     */
    private String generateFormParamKeyval() {
        StringBuilder sb = new StringBuilder("?");
        if (formParamMap != null && formParamMap.size() != 0) {
            for (Map.Entry<String, String> entry : formParamMap.entrySet()) {
                sb.append( entry.getKey() + "=" + entry.getValue()+"&");
            }
        }
        if (formKeyVal != null && !"".equals(formKeyVal)) {
            String prefix = "";
            if (!formKeyVal.startsWith("&")) {
                prefix = "&";
            }
            sb.append(prefix + formKeyVal);
        }
        if (sb.toString() != null && sb.toString().equals("?")) {
            return "";
        }
        if(sb.toString().endsWith("&")){
            sb=sb.deleteCharAt(sb.length()-1);
        }
        return sb.toString();
    }

    public HttpUtil url(String url) {
        this.url = url;
        return this;
    }
    public HttpUtil method(METHOD method){
        this.method=method;
        return this;
    }

    public enum METHOD{
        GET,POST
    }
}
