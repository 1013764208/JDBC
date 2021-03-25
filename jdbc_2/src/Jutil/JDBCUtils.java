package Jutil;

import com.sun.org.apache.bcel.internal.util.ClassLoader;

import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

/**
 * @author HXS
 * @create 2021-03-21 14:35
 */

// 操作数据库的工具类
public class JDBCUtils {

    // 获取数据库的连接
    public static Connection getConnection() throws Exception {
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
        Connection connection = DriverManager.getConnection(url, user, password);

        // 返回连接
        return connection;
    }

    // 关闭连接和statement的操作
    public static void closeResource(Connection connection, PreparedStatement ps){
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

    // 关闭资源的操作
    public static void closeResource(Connection connection, PreparedStatement ps, ResultSet rs){
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
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
