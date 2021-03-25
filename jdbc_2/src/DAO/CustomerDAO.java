package DAO;

import bean.Customer;

import java.sql.Connection;
import java.sql.Date;
import java.util.List;

/**
 * @author HXS
 * @create 2021-03-24 14:52
 */

/*
    此接口用于规范针对于customers表的常用操作
 */
public interface CustomerDAO {

    // 将cust对象添加到数据库对象中
    public abstract void insert (Connection connection, Customer customer) throws Exception;

    // 根据指定的id删除表中的记录
    void deleteById(Connection connection, int id) throws Exception;

    // 针对内存中的customer对象，去修改数据表中的记录
    void update(Connection connection, Customer customer) throws Exception;

    // 针对指定的id查询得到对应的customer对象
    Customer getCustomerById(Connection connection, int id);

    // 查询表中的所有记录构成的集合
    List<Customer> getAll(Connection connection);

    // 返回数据表中的数据的条目数
    Long getCount(Connection connection);

    // 返回数据表中最大的生日
    Date getMaxBirth(Connection connection);
}
