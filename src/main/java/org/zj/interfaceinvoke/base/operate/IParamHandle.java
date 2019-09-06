package org.zj.interfaceinvoke.base.operate;

import org.zj.interfaceinvoke.base.bean.RequestParam;

/**
 * @ProjectName: interface-invoke
 * @ClassName: IParamHandle
 * @Description: 参数的处理接口
 * @Author: ZhangJun
 * @Date: 2019/9/6 17:29
 */
public interface IParamHandle {
    /**
     * 对参数进行处理
     * @param requestParam
     */
    void handleParam(RequestParam requestParam);
}
