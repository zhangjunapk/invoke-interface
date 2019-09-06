package org.zj.interfaceinvoke.base.util;

/**
 * @ProjectName: interface-invoke
 * @ClassName: StringUtil
 * @Description: 字符串的处理
 * @Author: ZhangJun
 * @Date: 2019/9/6 21:14
 */
public class StringUtil {
    public static StringBuilder underlineToHump(String str){
        StringBuilder result=new StringBuilder();
        if(str==null||"".equals(str)){
            return result;
        }
        String[] strs = str.split("_");
        if(strs==null||strs.length<=0){
            return result;
        }
        result.append(strs[0]);
        for(int i=1;i<strs.length;i++){
            String str1 = strs[i];
            String substring = str1.substring(0, 1);
            result.append(substring.toUpperCase()).append(str1.substring(1));
        }
        return result;
    }

    public static void main(String[] args) {
        String str="user_name";
        System.out.println(StringUtil.underlineToHump(str));
    }
}
