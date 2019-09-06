package org.zj.interfaceinvoke.base.util.http;

/**
 * @BelongsProject: appService
 * @BelongsPackage: com.zhongjin.utils.http.util
 * @Author: ZhangJun
 * @CreateTime: 2019/2/28
 * @Description: ${Description}
 */
public interface HttpObjectResponceCallback<T> {
    void handleResponse(T t) throws Exception;
}
