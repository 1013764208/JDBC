package DAO;

import Jutil.JDBCUtils;
import bean.Customer;
import org.junit.Test;

import java.sql.Connection;
import java.sql.Date;
import java.util.List;


/**
 * @author HXS
 * @create 2021-03-24 15:28
 */
public class CustomerDaoImplTest {

    CustomerDaoImpl dao = new CustomerDaoImpl();

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
        Connection connection = JDBCUtils.getConnection();
        Customer customerById = dao.getCustomerById(connection, 19);
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
}