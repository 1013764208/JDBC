package Test01;
import org.junit.Test;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @author HXS
 * @create 2021-03-20 11:24
 */
public class Test01 {

    // 方式1
    @Test
    public void Test1() throws SQLException {


        Driver driver = new com.mysql.jdbc.Driver();


        // jdbc: mysql: 协议
        // localhost: 地址
        // localhost: ip协议
        // 3306: 默认mysql的端口号
        // test: test数据库
        String url = "jdbc:mysql://localhost:3306/test";
        // 将用户名和密码封装在properties中
        Properties info = new Properties();
        info.setProperty("user","root");
        info.setProperty("password","root");

        Connection connect = driver.connect(url, info);
        System.out.println(connect);

    }

    // 方式5
    @Test
    public void test2() throws Exception {
        // 1.读取配置文件
        InputStream inputStream = Test.class.getClassLoader().getResourceAsStream("jdbc.properties");

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

        System.out.println(connection);
    }
}
