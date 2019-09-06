package org.zj.interfaceinvoke.base.util.data;

import com.alibaba.druid.pool.DruidDataSource;
import org.zj.interfaceinvoke.base.util.ReflectionUtil;
import org.zj.interfaceinvoke.base.util.StringUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ProjectName: interface-invoke
 * @ClassName: DBUtil
 * @Description: 数据库的相关工具
 * @Author: ZhangJun
 * @Date: 2019/9/6 17:54
 */
public class DBUtil {
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * 驱动名
     */
    private String driverName;
    /**
     * 链接
     */
    private String url;
    /**
     * 这里使用德鲁伊的数据源
     */
    private static DruidDataSource dataSource;

    public String getUsername() {
        return username;
    }

    public DBUtil setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public DBUtil setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getDriverName() {
        return driverName;
    }

    public DBUtil setDriverName(String driverName) {
        this.driverName = driverName;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public DBUtil setUrl(String url) {
        this.url = url;
        return this;
    }

    public DBUtil generateDataSource(){
        dataSource=new DruidDataSource();
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setUrl(url);
        dataSource.setDriverClassName(driverName);
        return this;
    }
    /**
     * 这里直接查询结果
     * @param sql
     * @return
     */
    public static List<Map> getResult(String sql){
        List<Map> result=new ArrayList<>();
        Statement statement = getStatement();
        if(statement==null){
            return null;
        }
        try {
            ResultSet resultSet = statement.executeQuery(sql);
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            while(resultSet.next()){
                Map<String,Object> map=new HashMap<>();
                for(int i=1;i<=columnCount;i++){
                    map.put(metaData.getColumnName(i),resultSet.getObject(i));
                }
                result.add(map);
            }
            resultSet.close();
            Connection connection = statement.getConnection();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    private static Statement getStatement(){
        Connection connection = getConnection();
        if(connection!=null){
            try {
                return connection.createStatement();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private static Connection getConnection(){
        if(dataSource!=null){
            try {
                return dataSource.getConnection();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 把返回的map 转换为List<T>
     * @param result
     * @param c
     * @param <T>
     * @return
     */
    public  static <T> List<T>convert(List<Map> result,Class c){
        List<T> r=new ArrayList<>();
        for(Map<String,Object> m:result){
            r.add(convert(m,c));
        }
        return r;
    }
    private static <T> T convert(Map<String,Object> m,Class c){
        Object o = null;
        try {
            o = c.newInstance();
            for(Map.Entry<String,Object>entry:m.entrySet()){
                ReflectionUtil.setValue(o, StringUtil.underlineToHump(entry.getKey()).toString(),entry.getValue());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return (T) o;
    }
}
