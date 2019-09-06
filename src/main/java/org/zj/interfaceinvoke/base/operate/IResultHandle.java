package org.zj.interfaceinvoke.base.operate;

/**
 * @ProjectName: interface-invoke
 * @ClassName: IResultHandle
 * @Description: 结果处理
 * @Author: ZhangJun
 * @Date: 2019/9/6 17:39
 */
public interface IResultHandle {
    /**
     * 对结果进行处理
     * @param response
     */
    void handleResult(String response);
}
