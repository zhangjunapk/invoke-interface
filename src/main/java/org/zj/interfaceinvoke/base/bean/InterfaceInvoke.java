package org.zj.interfaceinvoke.base.bean;

/**
 * @ProjectName: interface-invoke
 * @ClassName: InterfaceInvoke
 * @Description: 接口调用的参数
 * @Author: ZhangJun
 * @Date: 2019/9/6 20:53
 */
public class InterfaceInvoke {
    private String id;
    private String invokeUrl;
    private String interfaceName;
    private String invokeMethod;
    private String paramHandlerRef;
    private String responseHandlerRef;

    public InterfaceInvoke setParamHandlerRef(String paramHandlerRef) {
        this.paramHandlerRef = paramHandlerRef;
        return this;
    }

    public String getResponseHandlerRef() {
        return responseHandlerRef;
    }

    public InterfaceInvoke setResponseHandlerRef(String responseHandlerRef) {
        this.responseHandlerRef = responseHandlerRef;
        return this;
    }

    public String getId() {
        return id;
    }

    public String getParamHandlerRef() {
        return paramHandlerRef;
    }

    public InterfaceInvoke setParamHandleRef(String paramHandleRef) {
        this.paramHandlerRef = paramHandlerRef;
        return this;
    }

    public InterfaceInvoke setId(String id) {
        this.id = id;
        return this;
    }

    public String getInvokeUrl() {
        return invokeUrl;
    }

    public InterfaceInvoke setInvokeUrl(String invokeUrl) {
        this.invokeUrl = invokeUrl;
        return this;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public InterfaceInvoke setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
        return this;
    }

    public String getInvokeMethod() {
        return invokeMethod;
    }

    public InterfaceInvoke setInvokeMethod(String invokeMethod) {
        this.invokeMethod = invokeMethod;
        return this;
    }
}
