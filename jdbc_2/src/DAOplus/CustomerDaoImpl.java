package DAOplus;

import bean.Customer;

import java.sql.Connection;
import java.sql.Date;
import java.util.List;

/**
 * @author HXS
 * @create 2021-03-24 15:06
 */
public class CustomerDaoImpl extends BaseDAO<Customer> implements CustomerDAO {

    @Override
    public void insert(Connection connection, Customer customer) throws Exception {
        String sql = "insert into customers(name,email,birth)values(?,?,?)";
        update(connection, sql, customer.getName(), customer.getEmail(), customer.getBirth());
    }

    @Override
    public void deleteById(Connection connection, int id) throws Exception {
        String sql = "delete from customers where id = ?";
        update(connection, sql, id);
    }

    @Override
    public void update(Connection connection, Customer customer) throws Exception {
        String sql = "update customers set name = ?,email = ?,birth = ? where id = ?";
        update(connection,sql,customer.getName(),customer.getEmail(),customer.getBirth(),customer.getId());
    }

    @Override
    public Customer getCustomerById(Connection connection, int id) {
        String sql = "select id,name,email,birth from customers where id = ?";
        Customer customer = getInstance(connection, sql, id);
        return customer;
    }

    @Override
    public List<Customer> getAll(Connection connection) {
        String sql = "select id,name,email,birth from customers";
        List<Customer> customers = getForList(connection, sql);
        return customers;
    }

    @Override
    public Long getCount(Connection connection) {
        String sql = "select count(*) from customers";
        return getValue(connection, sql);
    }

    @Override
    public Date getMaxBirth(Connection connection) {
        String sql = "select max(birth) from customers";
        return getValue(connection, sql);
    }
}
