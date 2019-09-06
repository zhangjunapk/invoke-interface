package org.zj.interfaceinvoke.base.util.http;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @BelongsProject: appService
 * @BelongsPackage: com.zhongjin.utils.util
 * @Author: ZhangJun
 * @CreateTime: 2019/2/28
 * @Description: ${Description}
 */
public class InterfaceUtil {
    /**
     * 获得一个接口的 泛型 类
     * @param interfaceObj
     * @return
     */
    public static Class getGenericity(Object interfaceObj,int index){
        Type[] types = interfaceObj.getClass().getGenericInterfaces();

        for (Type type : types) {
            if (type instanceof ParameterizedType) {
                ParameterizedType parameterizedType = (ParameterizedType) type;
                Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
                for(int i=0;i<actualTypeArguments.length;i++) {
                    String typeName = actualTypeArguments[i].getTypeName();
                    try {
                        if(i==index) {
                            return Class.forName(typeName);
                        }
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                        return null;
                    }
                }
            }
        }
        return null;
    }

    public static void main(String[] args) {
        Interfacellll objectStudentStringIntegerInterfacellll = new Interfacellll<Object, Student, String, Integer>() {
            @Override
            public void ss() {

            }
        };
        Class genericity = InterfaceUtil.getGenericity(objectStudentStringIntegerInterfacellll, 1);
        System.out.println(genericity);
    }
}
