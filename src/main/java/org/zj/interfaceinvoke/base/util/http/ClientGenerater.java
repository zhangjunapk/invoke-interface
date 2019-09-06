package org.zj.interfaceinvoke.base.util.http;

import okhttp3.OkHttpClient;

import java.util.concurrent.TimeUnit;


/**
 * @BelongsProject: appService
 * @BelongsPackage: com.zhongjin.utils.http.util
 * @Author: ZhangJun
 * @CreateTime: 2019/2/28
 * @Description: ${Description}
 */


public class ClientGenerater {
    private static volatile OkHttpClient client;
    public static OkHttpClient getInstance(){
        if(client==null){
            synchronized (ClientGenerater.class){
                if(client==null){
                    client=new OkHttpClient.Builder().connectTimeout(60, TimeUnit.SECONDS)
                            .readTimeout(60, TimeUnit.SECONDS)
                            .writeTimeout(60, TimeUnit.SECONDS).build();
                }
            }
        }
        return client;
    }
}

