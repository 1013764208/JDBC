package Test01;

import Jutil.JDBCUtils;
import com.sun.org.apache.bcel.internal.util.ClassLoader;
import org.junit.Test;

import java.io.InputStream;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Properties;

/**
 * @author HXS
 * @create 2021-03-21 14:13
 */
public class Test02 {

    // 向customer 表中添加一条记录
    @Test
    public void Test1() {
        Connection connection = null;
        PreparedStatement ps = null;

        try {
            // 1.读取配置文件
            InputStream inputStream = new ClassLoader().getSystemClassLoader().getResourceAsStream("jdbc.properties");

            Properties pros = new Properties();
            pros.load(inputStream);

            String user = pros.getProperty("user");
            String password = pros.getProperty("password");
            String url = pros.getProperty("url");
            String driverClass = pros.getProperty("driverClass");

            // 2. 加载驱动
            Class.forName(driverClass);

            // 3. 获取连接
            connection = DriverManager.getConnection(url, user, password);

            // 4. 预编译sql语句，返回 PreparedStatement的实例
            String sql = "insert into customers(name,email,birth) values (?,?,?)"; // ? 占位符
            ps = connection.prepareStatement(sql);

            // 5. 填充占位符  / 索引从1开始
            ps.setString(1,"蔡徐坤");
            ps.setString(2,"cxk@gmail.com");

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date date = sdf.parse("2000-11-01");
            ps.setDate(3,new Date(date.getTime()));

            // 6. 执行操作
            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 7. 资源关闭
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    // 修改customers表的一条记录
    @Test
    public void Test2() {
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            // 1. 获取数据库的连接
            connection = JDBCUtils.getConnection();

            // 2. 预编译sql语句，返回PreparedStatement的一个实例
            String sql = "update customers set name = ? where id = ? ";
            ps = connection.prepareStatement(sql);

            // 3. 填充占位符
            ps.setObject(1,"莫扎特");
            ps.setObject(2,18);

            // 4. 执行
            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 5. 资源关闭
            JDBCUtils.closeResource(connection,ps);
        }
    }
    // 删除customers表的一条记录
    @Test
    public void Test3() {
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            // 1. 获取数据库的连接
            connection = JDBCUtils.getConnection();

            // 2. 预编译sql语句，返回PreparedStatement的一个实例
            // 语法：DELETE FROM 表名 WHERE 筛选条件；
            String sql = "delete from customers where id = ?";
            ps = connection.prepareStatement(sql);

            // 3. 填充占位符
            ps.setObject(1,"20");

            // 4. 执行
            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 5. 资源关闭
            JDBCUtils.closeResource(connection,ps);
        }
    }

    // 通用的增删改操作
    public void Test4(String sql,Object ... args) {  // sql中占位符的个数与可变形参的长度一致
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            // 1. 获取数据库的连接
            connection = JDBCUtils.getConnection();

            // 2. 预编译sql语句，返回PreparedStatement的一个实例
            ps = connection.prepareStatement(sql);

            // 3. 填充占位符
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i+1,args[i]);  // 小心参数声明错误，sql从1开始，数组从0开始
            }

            // 4. 执行
            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 5. 关闭资源
            JDBCUtils.closeResource(connection,ps);
        }
    }

    @Test
    public void Test5(){
//        String sql = "delete from customers where id = ?";
//        update(sql,3);
        String sql = "update `order` set order_name = ? where order_id = ?";
        Test4(sql,"HXS", 4);

    }
}
