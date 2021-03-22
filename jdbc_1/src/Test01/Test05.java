package Test01;

import Jutil.JDBCUtils;
import bean.Customer;
import org.junit.Test;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

/**
 * @author HXS
 * @create 2021-03-21 20:22
 */
// 针对不同表，通用的查询操作，返回表中的一条记录
public class Test05 {

    private Object Customer;

    public <T> T getInstance(Class<T> clazz, String sql, Object ... args) {
        Connection connection = null;
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
            JDBCUtils.closeResource(connection,ps,rs);
        }
        return null;
    }

    @Test
    public void Test1(){
        String sql  = "select id,name,email from customers where id = ?";

        Customer customer = getInstance(Customer.class, sql, 1);
        System.out.println(customer);
    }


    // 针对不同表，通用的查询操作，返回表中的多条记录
    public <T> List<T> getForList(Class<T> clazz,String sql, Object ... args){
        Connection connection = null;
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
            JDBCUtils.closeResource(connection,ps,rs);
        }
        return null;
    }

    @Test
    public void Test2(){
        String sql = "select id,name,email from customers where id < ?";
        List<bean.Customer> forList = getForList(Customer.class, sql, 5);
        forList.forEach(System.out :: println);
    }
}
