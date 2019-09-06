package org.zj.interfaceinvoke.base.bean;

/**
 * @ProjectName: interface-invoke
 * @ClassName: InterfaceInvokeParam
 * @Description: 接口调用的参数
 * @Author: ZhangJun
 * @Date: 2019/9/6 20:57
 */
public class InterfaceInvokeParam {
    private Integer id;
    private String paramFieldName;
    private String dataRef;
    private String dataId;
    private String paramType;
    private String currentDataType;
    private String needDataType;
    private String dataValue;
    private String className;

    public String getClassName() {
        return className;
    }

    public InterfaceInvokeParam setClassName(String className) {
        this.className = className;
        return this;
    }

    public String getDataValue() {
        return dataValue;
    }

    public InterfaceInvokeParam setDataValue(String dataValue) {
        this.dataValue = dataValue;
        return this;
    }

    public Integer getId() {
        return id;
    }

    public InterfaceInvokeParam setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getParamFieldName() {
        return paramFieldName;
    }

    public InterfaceInvokeParam setParamFieldName(String paramFieldName) {
        this.paramFieldName = paramFieldName;
        return this;
    }

    public String getDataRef() {
        return dataRef;
    }

    public InterfaceInvokeParam setDataRef(String dataRef) {
        this.dataRef = dataRef;
        return this;
    }

    public String getDataId() {
        return dataId;
    }

    public InterfaceInvokeParam setDataId(String dataId) {
        this.dataId = dataId;
        return this;
    }

    public String getParamType() {
        return paramType;
    }

    public InterfaceInvokeParam setParamType(String paramType) {
        this.paramType = paramType;
        return this;
    }

    public String getCurrentDataType() {
        return currentDataType;
    }

    public InterfaceInvokeParam setCurrentDataType(String currentDataType) {
        this.currentDataType = currentDataType;
        return this;
    }


    public String getNeedDataType() {
        return needDataType;
    }

    public InterfaceInvokeParam setNeedDataType(String needDataType) {
        this.needDataType = needDataType;
        return this;
    }
}
