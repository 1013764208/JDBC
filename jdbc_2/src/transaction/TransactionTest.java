package transaction;

import Jutil.JDBCUtils;
import org.junit.Test;

import java.lang.reflect.Field;
import java.sql.*;

/**
 * @author HXS
 * @create 2021-03-24 9:48
 */

/*
    1. 什么叫数据库事务
    事务：一组逻辑操作单元,使数据从一种状态变换到另一种状态
         一组逻辑操作单元：一个或多个DML操作

    2. 事务处理（事务操作）的原则：保证所有事务都作为一个工作单元来执行，即使出现了故障，
      都不能改变这种执行方式。当在一个事务中执行多个操作时，要么所有的事务都被提交(commit)，
      那么这些修改就永久地保存下来；要么数据库管理系统将放弃所作的所有修改，整个事务回滚(rollback)到最初状态

    3. 数据一旦提交，就不可回滚

    4. 哪些操作会导致数据的自动提交
        DDL操作一旦执行，都会自动提交，set autocommit = false的方式 对DDL失效
        DML默认情况下，一旦执行，就会自动提交
            我们可以通过set autocommit = false的方式，取消DML操作的自动提交

        默认在关闭连接时，会自动提交
 */
public class TransactionTest {

    // 通用的增删改操作 - version 1.0
    public void update(String sql, Object ... args) throws Exception {
        // 1. 获取数据库的连接
        Connection connection = JDBCUtils.getConnection();

        // 2. 预编译sql语句，返回preparedStatement的实例
        PreparedStatement ps = connection.prepareStatement(sql);

        // 3. 填充占位符
        for (int i = 0; i < args.length; i++) {
            ps.setObject(i+1,args[i]);
        }

        // 4. 执行
        ps.execute();

        // 5. 资源的关闭
        JDBCUtils.closeResource(connection,ps);
    }

    /*
        针对于数据表user_table 来说
        AA用户给BB用户转账100

        update user_table set balance = balance - 100 where user = 'AA'
        update user_table set balance = balance + 100 where user = 'BB'
     */
    // —————————————————— 未考虑数据库事务的转账操作----------------
    @Test
    public void testUpdate() throws Exception {
        String sql1 = " update user_table set balance = balance - 100 where user = ? ";
        update(sql1, "AA");

        // 模拟网络异常
        System.out.println(10 / 0);

        String sql2 = " update user_table set balance = balance + 100 where user = ? ";

        update(sql2, "BB");

        System.out.println("转账成功");
    }



    // --------------- 考虑数据库事务的转账操作---------------------------
    // 通用的增删改操作 - version 2.0（考虑上事务）
    public User update2(Connection connection, String sql, Object ... args) throws Exception {

        // 1. 预编译sql语句，返回preparedStatement的实例
        PreparedStatement ps = connection.prepareStatement(sql);

        // 2. 填充占位符
        for (int i = 0; i < args.length; i++) {
            ps.setObject(i+1,args[i]);
        }

        // 3. 执行
        ps.execute();

        // 4. 资源的关闭
        JDBCUtils.closeResource(null,ps);
        return null;
    }

    @Test
    public void testUpdateWithTx() {
        Connection connection = null;
        try {
            connection = JDBCUtils.getConnection();

            // 1. 取消数据的自动提交功能
            connection.setAutoCommit(false);

            String sql1 = " update user_table set balance = balance - 100 where user = ? ";
            update2(connection,sql1, "AA");

            // 模拟网络异常
            System.out.println(10 / 0);

            String sql2 = " update user_table set balance = balance + 100 where user = ? ";

            update2(connection,sql2, "BB");

            System.out.println("转账成功");

            // 2. 提交数据
            connection.commit();
        } catch (Exception e) {
            e.printStackTrace();
            // 3. 归滚数据
            try {
                connection.rollback();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        } finally {
            JDBCUtils.closeResource(connection,null);
        }
    }


    // ----------------------------------------------------------------------------------
    // 通用的查询操作，用于返回数据表中的一条记录（version 2.0 : 考虑上事务）
    public <T> T getInstance(Connection connection,Class<T> clazz, String sql, Object ... args) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            connection = JDBCUtils.getConnection();

            ps = connection.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i+1,args[i]);
            }
            // 执行，获取结果集
            rs = ps.executeQuery();
            // 获取结果集的元数据
            ResultSetMetaData rsmd = rs.getMetaData();
            // 获取列数
            int columnCount = rsmd.getColumnCount();
            if (rs.next()) {
                T t = clazz.newInstance();
                for (int i = 0; i < columnCount; i++) {
                    // 获取每个列的列值: 通过ResultSet
                    Object columnValue = rs.getObject(i + 1);

                    // 获取每个列的列名：通过ResultSetMetaData
                    // 获取列的列名: getColumnName  / 不推荐使用
                    // 获取列的别名:
//                    String columnName = rsmd.getColumnName(i + 1);
                    String columnLabel = rsmd.getColumnLabel(i + 1);

                    // 通过反射，将对象指定名columnName的属性赋值columnValue为指定的值

                    Field field = clazz.getDeclaredField(columnLabel);
                    field.setAccessible(true);
                    field.set(t,columnValue);
                }
                return t;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(null,ps,rs);
        }
        return null;
    }


    @Test
    public void testTransactionSelect() throws Exception {
        Connection connection = JDBCUtils.getConnection();

        // 获取当前连接的隔离级别
        System.out.println(connection.getTransactionIsolation());

        // 设置数据库的隔离级别
        connection.setTransactionIsolation(connection.TRANSACTION_READ_COMMITTED);

        // 设置自动提交数据
        connection.setAutoCommit(false);



        String sql = "select user, password,balance from user_table where user = ?";

        User user = getInstance(connection, User.class, sql, "CC");

        System.out.println(user);


    }

    @Test
    public void testTransactionUpdate() throws Exception {
        Connection connection = JDBCUtils.getConnection();

        // 设置自动提交数据
        connection.setAutoCommit(false);

        String sql = "update user_table set balance = ? where user =  ?";

        User user = update2(connection,sql,5000,"CC");

        Thread.sleep(15000);

        System.out.println("修改结束");
    }
}
