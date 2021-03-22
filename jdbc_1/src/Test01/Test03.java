package Test01;

import Jutil.JDBCUtils;
import bean.Customer;
import org.junit.Test;

import java.lang.reflect.Field;
import java.sql.*;

/**
 * @author HXS
 * @create 2021-03-21 15:33
 */

// 针对于Customers表的查询操作
public class Test03 {

    @Test
    public void Test1() {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        try {
            // 1. 获取数据库的连接
            connection = JDBCUtils.getConnection();

            // 2. 预编译sql语句，返回PreparedStatement的一个实例
            String sql = "select id,name,email,birth  from customers where id = ?";
            ps = connection.prepareStatement(sql);

            // 3. 填充
            ps.setObject(1,1);

            // 4. 执行并返回结果集
            resultSet = ps.executeQuery();

            // 5. 处理结果集
            if (resultSet.next()) {  // next方法作用：判断结果集的下一条是否有数据，如果有数据返回true
                                     // 并指针下移，若返回false，指针不会下移
                // 获取当前这条数据的各个字段值
                int id = resultSet.getInt(1);
                String name = resultSet.getString(2);
                String email = resultSet.getString(3);
                Date birth = resultSet.getDate(4);

                // 方式1：
                // System.out.println("id=" + id + " name=" + name + " email" + email + " birth" + birth);

                // 方式2：
                // Object[] date = new Object[]{id, name, email, birth};

                // 方式3：将数据封装为一个对象（推荐）
                Customer customer = new Customer(id, name, email, birth);
                System.out.println(customer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 6. 关闭资源
            JDBCUtils.closeResource(connection,ps,resultSet);
        }
    }


    // 针对于customer 表的通用的查询操作
    public Customer Test2(String sql, Object ... args) {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            // 1. 获取数据库的连接
            connection = JDBCUtils.getConnection();

            // 2. 预编译sql语句，返回PreparedStatement的一个实例
            ps = connection.prepareStatement(sql);

            // 3. 填充占位符
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i+1,args[i]);
            }

            // 4. 执行，获取结果集
            rs = ps.executeQuery();
            // 获取结果集的元数据：ResultSetMetaData
            ResultSetMetaData rsmd = rs.getMetaData();
            // 通过ResultSetMetaData获取结果集的列数
            int columnCount = rsmd.getColumnCount();

            // 5. 处理结果集
            if (rs.next()){
                Customer cust = new Customer();

                // 处理结果集一行数据中的每一个列
                for (int i = 0; i < columnCount; i++) {
                    // 获取列值
                    Object columnValue = rs.getObject(i + 1);

                    // 获取每个列的列名
//                    String columnName = rsmd.getColumnName(i + 1);
                    String columnLabel = rsmd.getColumnLabel(i + 1);

                    // 给cust对象指定的某个属性，赋值为columnValue：通过反射
                    Field field = Customer.class.getDeclaredField(columnLabel);
                    field.setAccessible(true);
                    field.set(cust,columnValue);

                }
                return cust;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 6. 资源关闭
            JDBCUtils.closeResource(connection,ps,rs);
        }

        return null;
    }

    @Test
    public void Test3(){
        String sql1 = "select id,name,birth,email from customers where id = ?";
        String sql2 = "select id,name from customers where id = ?";
        Customer customer = Test2(sql2, 13);
        System.out.println(customer);

    }
}
