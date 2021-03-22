package Test01;

import Jutil.JDBCUtils;
import bean.Order;
import org.junit.Test;

import java.lang.reflect.Field;
import java.sql.*;

/**
 * @author HXS
 * @create 2021-03-21 19:17
 */

// 针对于Order表的通用的查询操作
public class Test04 {
        /*
        针对于表的字段名和类的属性名不相同的情况
        1. 必须声明sql时，使用类的属性名来命名字段的别名
        2. 使用ResultSetMetaData时，需要使用getColumnLabel()来替换getColumnName()
           获取列的类名

        补充说明：如果sql中没有给字段起别名，getColumnLabel()获取的就是列名
     */

    @Test
    public void Test1() {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            connection = JDBCUtils.getConnection();

            String sql = "select order_id,order_name,order_date from`order` where order_id = ?";
            ps = connection.prepareStatement(sql);

            ps.setObject(1,1);

            rs = ps.executeQuery();
            if (rs.next()){
                int id = (int) rs.getObject(1);
                String name = (String) rs.getString(2);
                Date date = rs.getDate(3);

                Order order = new Order(id, name, date);
                System.out.println(order);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(connection,ps,rs);
        }
    }


    // 通用的针对于order表的查询操作
    public Order Test2(String sql, Object...args) {
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
                Order order = new Order();
                for (int i = 0; i < columnCount; i++) {
                    // 获取每个列的列值: 通过ResultSet
                    Object columnValue = rs.getObject(i + 1);

                    // 获取每个列的列名：通过ResultSetMetaData
                    // 获取列的列名: getColumnName  / 不推荐使用
                    // 获取列的别名:
//                    String columnName = rsmd.getColumnName(i + 1);
                    String columnLabel = rsmd.getColumnLabel(i + 1);

                    // 通过反射，将对象指定名columnName的属性赋值columnValue为指定的值
                    Field field = Order.class.getDeclaredField(columnLabel);
                    field.setAccessible(true);
                    field.set(order,columnValue);
                }
                return order;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(connection,ps,rs);
        }
        return null;
    }

    @Test
    public void Test3(){
        String sql = "select order_id orderId ,order_name orderName ,order_date orderDate from`order` where order_id = ?";
        Order order = Test2(sql, 1);
        System.out.println(order);

    }
}
