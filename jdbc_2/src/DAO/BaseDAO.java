package DAO;

import Jutil.JDBCUtils;
import transaction.User;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author HXS
 * @create 2021-03-24 11:57
 */

/*
    DAO：date（base） access object
    封装了针对数据的表的通用操作，
 */
public abstract class BaseDAO {

    // 通用的增删改操作 - version 2.0（考虑上事务）
    public User update(Connection connection, String sql, Object ... args) throws Exception {

        // 1. 预编译sql语句，返回preparedStatement的实例
        PreparedStatement ps = connection.prepareStatement(sql);

        // 2. 填充占位符
        for (int i = 0; i < args.length; i++) {
            ps.setObject(i+1,args[i]);
        }

        // 3. 执行
        ps.execute();

        // 4. 资源的关闭
        JDBCUtils.closeResource(null,ps);
        return null;
    }


    // ----------------------------------------------------------------------------------
    // 通用的查询操作，用于返回数据表中的一条记录（version 2.0 : 考虑上事务）
    public <T> T getInstance(Connection connection, Class<T> clazz, String sql, Object ... args) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            connection = JDBCUtils.getConnection();

            ps = connection.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i+1,args[i]);
            }
            // 执行，获取结果集
            rs = ps.executeQuery();
            // 获取结果集的元数据
            ResultSetMetaData rsmd = rs.getMetaData();
            // 获取列数
            int columnCount = rsmd.getColumnCount();
            if (rs.next()) {
                T t = clazz.newInstance();
                for (int i = 0; i < columnCount; i++) {
                    // 获取每个列的列值: 通过ResultSet
                    Object columnValue = rs.getObject(i + 1);

                    // 获取每个列的列名：通过ResultSetMetaData
                    // 获取列的列名: getColumnName  / 不推荐使用
                    // 获取列的别名:
//                    String columnName = rsmd.getColumnName(i + 1);
                    String columnLabel = rsmd.getColumnLabel(i + 1);

                    // 通过反射，将对象指定名columnName的属性赋值columnValue为指定的值

                    Field field = clazz.getDeclaredField(columnLabel);
                    field.setAccessible(true);
                    field.set(t,columnValue);
                }
                return t;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(null,ps,rs);
        }
        return null;
    }



    // 针对不同表，通用的查询操作，返回表中的多条记录
    // 通用的查询操作，用于返回数据表中的一条记录（version 2.0 : 考虑上事务）
    public <T> List<T> getForList(Connection connection, Class<T> clazz, String sql, Object ... args){
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            connection = JDBCUtils.getConnection();

            ps = connection.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i+1,args[i]);
            }
            // 执行，获取结果集
            rs = ps.executeQuery();
            // 获取结果集的元数据
            ResultSetMetaData rsmd = rs.getMetaData();
            // 获取列数
            int columnCount = rsmd.getColumnCount();
            // 创建集合对象                         //
            ArrayList<T> arrayList = new ArrayList<>();
            while (rs.next()) {                   //
                T t = clazz.newInstance();
                // 处理结果集一行数据的每一个列：给t对象指定的属性赋值
                for (int i = 0; i < columnCount; i++) {
                    // 获取每个列的列值: 通过ResultSet
                    Object columnValue = rs.getObject(i + 1);

                    // 获取每个列的列名：通过ResultSetMetaData
                    // 获取列的列名: getColumnName  / 不推荐使用
                    // 获取列的别名:
//                    String columnName = rsmd.getColumnName(i + 1);
                    String columnLabel = rsmd.getColumnLabel(i + 1);

                    // 通过反射，将对象指定名columnName的属性赋值columnValue为指定的值

                    Field field = clazz.getDeclaredField(columnLabel);
                    field.setAccessible(true);
                    field.set(t,columnValue);
                }
                arrayList.add(t);
            }
            return arrayList;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(null,ps,rs);
        }
        return null;
    }

    // 查询特殊值的通用方法
    public <E> E getValue(Connection connection, String sql, Object ... args) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = connection.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i+1,args[i]);
            }

            rs = ps.executeQuery();
            if (rs.next()) {
                return (E)rs.getObject(1);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            JDBCUtils.closeResource(null,ps,rs);
        }
        return null;
    }
}
