package Datapool;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.junit.Test;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author HXS
 * @create 2021-03-25 11:26
 */
public class C3P0Test {
    // 方式1
    @Test
    public void testGetConnection() throws Exception {
        // 获取c3p0数据库连接池
        ComboPooledDataSource cpds = new ComboPooledDataSource();
        cpds.setDriverClass( "com.mysql.jdbc.Driver" ); //loads the jdbc driver
        cpds.setJdbcUrl( "jdbc:mysql://localhost:3306/test" );
        cpds.setUser("root");
        cpds.setPassword("root");
        // 通过设置相关的参数，对数据库连接池进行管理
        // 设置初始时数据库连接池的连接数
        cpds.setInitialPoolSize(10);

        Connection connection = cpds.getConnection();
        System.out.println(connection);


    }

    // 方式2：配置文件
    @Test
    public void testGetConnection1() throws SQLException {
        ComboPooledDataSource cpds = new ComboPooledDataSource("intergalactoApp");
        Connection connection = cpds.getConnection();
    }
}
