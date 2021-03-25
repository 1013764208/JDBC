package Test02;

import Jutil.JDBCUtils;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * @author HXS
 * @create 2021-03-23 20:53
 */

/*
    使用preparedStatement 实现批量数据的操作

    update,delete 本身就具有批量操作的效果
    此时的批量操作，主要指的是批量插入，使用preparedStatement如果四化建更高效的批量插入？

    题目：向goods表中插入20000条数据
    CREATE TABLE goods(
	id INT PRIMARY KEY AUTO_INCREMENT,
	NAME VARCHAR(25)

    方式1：使用statement // 不太行
    Connection connection = JDBCUtils.getConnection();
    Statement st = connection.createStatement();
    for (int i = 0; i <= 20000; i++) {
        String sql = "insert into goods(name)values('name_" + i + "')";
        st.execute(sql);
    }


 */
public class InsertTest {

    // 批量插入的方式2
    @Test
    public void testInsert() throws Exception {
        Connection connection = JDBCUtils.getConnection();
        String sql = "insert into goods(name)values(?)";
        PreparedStatement ps = connection.prepareStatement(sql);

        for (int i = 0; i <= 20000; i++) {
            ps.setObject(1,"name_" + i);
            ps.execute();
        }
        JDBCUtils.closeResource(connection,ps);
    }

    /*
        1. addBatch(),executeBatch(),clearBatch()
        2.mysql服务器默认是关闭批处理的，我们需要通过一个参数，让mysql开启批处理的支持。
		  ?rewriteBatchedStatements=true 写在配置文件的url后面
        3.使用更新的mysql 驱动：mysql-connector-java-5.1.37-bin.jar
     */

    // 确实很快
    @Test
    public void testInsert2() throws Exception {
        Connection connection = JDBCUtils.getConnection();
        String sql = "insert into goods(name)values(?)";
        PreparedStatement ps = connection.prepareStatement(sql);

        for (int i = 0; i <= 20000; i++) {
            ps.setObject(1,"name_" + i);

            // 1. "攒sql"
            ps.addBatch();
            if (i % 500 == 0) {
                // 2. 执行batch
                ps.executeBatch();
                // 3. 清空batch
                ps.clearBatch();
            }
        }
        JDBCUtils.closeResource(connection,ps);
    }

    // 批量插入的方式4：设置连接不允许自动提交
    // 比上次快一点点
    @Test
    public void testInsert3() throws Exception {
        Connection connection = JDBCUtils.getConnection();
        String sql = "insert into goods(name)values(?)";
        PreparedStatement ps = connection.prepareStatement(sql);

        // 设置不允许自动提交数据
        connection.setAutoCommit(false);

        for (int i = 0; i <= 20000; i++) {
            ps.setObject(1,"name_" + i);

            // 1. "攒sql"
            ps.addBatch();
            if (i % 500 == 0) {
                // 2. 执行batch
                ps.executeBatch();
                // 3. 清空batch
                ps.clearBatch();
            }
        }
        // 提交数据
        connection.commit();
        JDBCUtils.closeResource(connection,ps);
    }
}
