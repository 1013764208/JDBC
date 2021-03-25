package Datapool;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.dbcp.BasicDataSourceFactory;
import org.junit.Test;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @author HXS
 * @create 2021-03-25 14:31
 */
public class DBCPTest {

    // 测试DBCP的数据库连接池技术
    // 方式1
    @Test
    public void testGetConnection() throws SQLException {
        // 创建了DBCP的数据库连接池
        BasicDataSource source = new BasicDataSource();

        // 设置基本信息
        source.setDriverClassName("com.mysql.jdbc.Driver");
        source.setUrl("jdbc:mysql://localhost:3306/test");
        source.setPassword("root");
        source.setUsername("root");

        // 还可以设置其他涉及数据库池管理的相关属性
        source.setInitialSize(10);
        source.setMaxActive(10);

        Connection connection = source.getConnection();
        System.out.println(connection);
    }

    // 方式2：使用配置文件
    public void testGetConnection2() throws Exception {
        Properties properties = new Properties();
        // 方式1： 加载配置文件
        InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("dbcp.properties");
        // 方式2：
        FileInputStream fileInputStream = new FileInputStream(new File("src/dbcp.properties"));

        properties.load(is);
        DataSource source = BasicDataSourceFactory.createDataSource(properties);
        Connection connection = source.getConnection();
        System.out.println(connection);
    }
}
