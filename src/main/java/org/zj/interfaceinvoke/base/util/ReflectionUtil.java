package org.zj.interfaceinvoke.base.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

/**
 * @ProjectName: interface-invoke
 * @ClassName: ReflectionUtil
 * @Description: 反射工具
 * @Author: ZhangJun
 * @Date: 2019/9/6 21:11
 */
public class ReflectionUtil {
    public static Field getField(Class c,String fieldName){
        try {
            System.out.println(c);
            System.out.println(fieldName);
            Field declaredField = c.getDeclaredField(fieldName);
            System.out.println(c);
            System.out.println(fieldName);
            return declaredField;
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Object instanceField(Field field){
        try {
            Object o = field.getDeclaringClass().newInstance();
            return o;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 设置值
     * @param obj
     * @param fieldName
     * @param value
     */
    public static void setValue(Object obj,String fieldName,Object value){
        Field field = getField(obj.getClass(), fieldName);
        if(field!=null){
            field.setAccessible(true);
            try {
                field.set(obj,value);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
    public static void setValue(Object obj,Field field,Object value){
        field.setAccessible(true);
        try {
            field.set(obj,value);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据类名来查找类，然后实例化一个对象返回过去
     * @param className
     * @return 自己给自己挖坑哦```
     */
    public static Object newInstance(String className){
        Object result = null;
        try {
            result = Class.forName(className).newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}
