package dbutils;

import DPutil.JDBCUtils;
import bean.Customer;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.*;
import org.junit.Test;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;



/*
    commons-dbutils 是apche提供的一个开源的JDBC工具类库，分装了了针对于数据库的增删改查的操作
 */
public class QueryTest {

    // 测试插入
    @Test
    public void testInsert() throws Exception {
        QueryRunner runner = new QueryRunner();
        Connection conn = JDBCUtils.getConnection3();
        String sql = "insert into customers(name,email,birth)vlaues(?,?,?)";

        int insertCount = runner.update(conn, sql, "张小博", "23100@qq.com", "1998-03-13");
        System.out.println("打印了几条记录："+insertCount);
        JDBCUtils.closeResource1(conn,null);
    }

    // 测试查询
    /*
        beanHandler:是ResultSetHandler接口的封装类，用于封装表中的一条记录
     */
    @Test
    public void testQuery() throws Exception {
        QueryRunner runner = new QueryRunner();
        Connection conn = JDBCUtils.getConnection3();
        String sql = "select id,name,email,birth from customers where id = ?";
        BeanHandler<Customer> beanHandler = new BeanHandler<>(Customer.class);
        Customer customer = runner.query(conn, sql, beanHandler, 13);
        System.out.println(customer);
        JDBCUtils.closeResource1(conn,null);
    }

    // beanHandler:是ResultSetHandler接口的封装类，用于封装表中的多条记录
    @Test
    public void testListQuery() throws Exception {
        QueryRunner runner = new QueryRunner();
        Connection conn = JDBCUtils.getConnection3();
        String sql = "select id,name,email,birth from customers where id < ?";
        BeanListHandler<Customer> beanListHandler = new BeanListHandler<>(Customer.class);
        List<Customer> customerList = runner.query(conn, sql, beanListHandler, 13);
        customerList.forEach(System.out::println);
        JDBCUtils.closeResource1(conn,null);
    }


    //  MapHandler:是ResultSetHandler接口的封装类，用于封装表中的一条记录
    // 将字段及相应字段的值作为map在的key和values
    @Test
    public void mapQuery() throws Exception {
        QueryRunner runner = new QueryRunner();
        Connection conn = JDBCUtils.getConnection3();
        String sql = "select id,name,email,birth from customers where id = ?";
        MapHandler mapHandler = new MapHandler();
        Map<String, Object> map = runner.query(conn, sql, mapHandler, 13);
        System.out.println(map);
        JDBCUtils.closeResource1(conn,null);
    }


    // MapListHandler:是ResultSetHandler接口的封装类，用于封装表中的多条记录
    // 将字段及相应字段的值作为map在的key和values，将这些map添加到list中
    @Test
    public void mapListQuery() throws Exception {
        QueryRunner runner = new QueryRunner();
        Connection conn = JDBCUtils.getConnection3();
        String sql = "select id,name,email,birth from customers where id < ?";
        MapListHandler mapListHandler = new MapListHandler();
        List<Map<String, Object>> mapList = runner.query(conn, sql, mapListHandler, 13);
        mapList.forEach(System.out::println);
        JDBCUtils.closeResource(conn,null);
    }

    // 特殊查询/ 查count
    @Test
    public void scalarHandler() throws Exception {
        QueryRunner runner = new QueryRunner();
        Connection conn = JDBCUtils.getConnection3();
        String sql = "select count(*) from customers";
        ScalarHandler scalarHandler = new ScalarHandler();
        Long query = (Long)runner.query(conn, sql, scalarHandler);
        System.out.println(query);
        JDBCUtils.closeResource1(conn,null);
    }

    // 特殊查询/ 查max
    @Test
    public void scalarHandler1() throws Exception {
        QueryRunner runner = new QueryRunner();
        Connection conn = JDBCUtils.getConnection3();
        String sql = "select max(birth) from customers";
        ScalarHandler scalarHandler = new ScalarHandler();
        Date query = (Date) runner.query(conn, sql, scalarHandler);
        System.out.println(query);
        JDBCUtils.closeResource1(conn,null);
    }

    // 自定义 ResultSetHandler 的实现类
    @Test
    public void scalarHandler2() throws Exception {
        QueryRunner runner = new QueryRunner();
        Connection conn = JDBCUtils.getConnection3();
        String sql = "select id ,name,email,birth from customers where id = ?";
        ResultSetHandler<Customer> handler = new ResultSetHandler<Customer>() {
            @Override
            public Customer handle(ResultSet resultSet) throws SQLException {
                if (resultSet.next()){
                    int id = resultSet.getInt("id");
                    String name = resultSet.getString("name");
                    String email = resultSet.getString("email");
                    Date birth = resultSet.getDate("birth");
                    Customer customer = new Customer(id, name, email, birth);
                    return customer;
                }
                return null;
            }
        };
        Customer query = runner.query(conn, sql, handler, 13);
        System.out.println(query);
        JDBCUtils.closeResource1(conn,null);
    }
}


