package DPutil;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.apache.commons.dbcp.BasicDataSourceFactory;
import org.apache.commons.dbutils.DbUtils;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @author HXS
 * @create 2021-03-25 14:14
 */
public class JDBCUtils {

    // 使用C3P0数据库连接池技术
    // 数据库连接池只需提供一个即可
    private static ComboPooledDataSource cpds = new ComboPooledDataSource("intergalactoApp");
    public static Connection getConnection1() throws SQLException {
        ComboPooledDataSource cpds = new ComboPooledDataSource("intergalactoApp");
        Connection connection = cpds.getConnection();
        return connection;
    }


    // 使用DBCP数据库连接池技术
    private static DataSource source;
    static {
        Properties properties = null;
        try {
            properties = new Properties();
            FileInputStream fileInputStream = new FileInputStream(new File("src/dbcp.properties"));
            properties.load(fileInputStream);
            // 创建一个DBCP数据库连接池
            source = BasicDataSourceFactory.createDataSource(properties);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static Connection getConnection2() throws Exception {
        Connection connection = source.getConnection();
        return connection;
    }


    // 使用Druid数据库连接池技术
    private static DataSource source1;
    static {
        try {
            Properties properties = new Properties();
            InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("druid.properties");
            properties.load(is);
            source1 = DruidDataSourceFactory.createDataSource(properties);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static Connection getConnection3() throws Exception {
        Connection connection = source1.getConnection();
        return connection;
    }

    // 关闭连接和statement的操作
    public static void closeResource1(Connection connection, PreparedStatement ps){
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
    // 关闭连接和statement的操作
    // 使用dbutils.jar中的提供的DbUtils工具类，实现资源的关闭
    public static void closeResource2(Connection connection, PreparedStatement ps){
        // 太low了
//        try {
//            DbUtils.close(connection);
//        } catch (SQLException throwables) {
//            throwables.printStackTrace();
//        }
//        try {
//            DbUtils.close(ps);
//        } catch (SQLException throwables) {
//            throwables.printStackTrace();
//        }

        // 这个外面不用写 try-catch
        DbUtils.closeQuietly(connection);
        DbUtils.closeQuietly(ps);

    }
}
