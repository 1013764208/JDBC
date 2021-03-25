package transaction;

import Jutil.JDBCUtils;
import com.sun.tracing.dtrace.Attributes;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @author HXS
 * @create 2021-03-24 9:39
 */

public class ConnectionTest {

    @Test
    public void test01() throws Exception {
        Connection connection = JDBCUtils.getConnection();

        System.out.println(connection);
    }

}
