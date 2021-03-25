package DAOplus;

import DAO.CustomerDaoImpl;
import Jutil.JDBCUtils;
import bean.Customer;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.apache.commons.dbcp.BasicDataSourceFactory;
import org.junit.Test;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;


/**
 * @author HXS
 * @create 2021-03-24 15:28
 */
public class CustomerDaoImplTest {

    DAO.CustomerDaoImpl dao = new CustomerDaoImpl();

    @Test
    public void insert() throws Exception {
        Connection connection = JDBCUtils.getConnection();
        Customer customer = new Customer(1,"小飞","213@qe.com", new Date(231231L));
        dao.insert(connection,customer);
        System.out.println("添加成功");
        JDBCUtils.closeResource(connection,null,null);
    }

    @Test
    public void deleteById() throws Exception {
        Connection connection = JDBCUtils.getConnection();
        dao.deleteById(connection,11);
        System.out.println("删除成功");
        JDBCUtils.closeResource(connection,null,null);
    }

    @Test
    public void update() throws Exception {
        Connection connection = JDBCUtils.getConnection();
        Customer customer = new Customer(5,"张飞","32133@qe.com", new Date(221331231L));
        dao.update(connection,customer);
        System.out.println("修改成功");
        JDBCUtils.closeResource(connection,null,null);

    }

    @Test
    public void getCustomerById() throws Exception {
        Connection connections1 = getConnection3();
//        Connection connection = JDBCUtils.getConnection();
        Customer customerById = dao.getCustomerById(connections1, 19);
        System.out.println(customerById);
    }

    @Test
    public void getAll() throws Exception {
        Connection connection = JDBCUtils.getConnection();
        List<Customer> daoAll = dao.getAll(connection);
        daoAll.forEach(System.out::println);
    }

    @Test
    public void getCount() throws Exception {
        Connection connection = JDBCUtils.getConnection();
        Long count = dao.getCount(connection);
        System.out.println("表中的记录数为" + count);
    }

    @Test
    public void getMaxBirth() throws Exception {
        Connection connection = JDBCUtils.getConnection();
        Date maxBirth = dao.getMaxBirth(connection);
        System.out.println("最大的生日为"+maxBirth);
    }


    // 使用C3P0数据库连接池技术
    public static Connection getConnections1() throws SQLException {
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
    public static Connection getConnection3() throws Exception {
        Properties properties = new Properties();
        InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("druid.properties");
        properties.load(is);
        DataSource source = DruidDataSourceFactory.createDataSource(properties);
        Connection connection = source.getConnection();
        return connection;
    }
}