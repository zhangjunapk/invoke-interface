package org.zj.interfaceinvoke.base.util.http;

import java.util.HashMap;

/**
 * @BelongsProject: appService
 * @BelongsPackage: com.zhongjin.utils.http.util
 * @Author: ZhangJun
 * @CreateTime: 2019/2/28
 * @Description: 工具类测试
 */

public class Content {
    public static void main(String[] args) {
        HashMap<String, String> objectObjectHashMap = new HashMap<>();
        objectObjectHashMap.put("ffasdf","ffffffasdfff");
        objectObjectHashMap.put("aaaaaaaaaa","ffffffff");

        new HttpUtil()
                .url("http://baidu.com")

                //添加表单参数的map
                .addFormParam(objectObjectHashMap)
                //添加表单参数的key=val
                .addFormParamString("name=zhangsan&age=14")
                //添加json参数的str

                .addJsonParamString("{'key':'value'}")
                //添加一个对象作为json参数
                .addJsonParamObject(new Student("zhangsan","a1000216"))
                //添加一个map 作为参数
                .addJsonParamMap(objectObjectHashMap)

                //设置头
                .setHeaderMap(new HashMap<>())
                //处理异常
                .setExceptionCallback(e -> {
                })
                //设置处理返回对象的回调
                .setResponseObjectCallback(new HttpObjectResponceCallback<Student>() {
                    @Override
                    public void handleResponse(Student student) {

                    }
                })
                //处理
                .handle();
    }
}
