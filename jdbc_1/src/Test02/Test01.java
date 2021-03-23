package Test02;

import Jutil.JDBCUtils;
import org.junit.Test;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Scanner;

/**
 * @author HXS
 * @create 2021-03-22 15:51
 */

// 课后练习1
public class Test01 {

    // 通用增删改操作
    public int update (String sql, Object ... args) throws Exception {
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            // 1. 获取数据库的连接
            connection = JDBCUtils.getConnection();

            ps = connection.prepareStatement(sql);

            // 3. 填充占位符
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i+1,args[i]);
            }

            // 4. 执行sql语句
            /*
                ps.execute():
                如果执行的时查询操作，有返回结果，则方法返回true
                如果执行的是增删改操作，没有返回结果，则此方法返回false
             */
            // 方式1
//            ps.execute();
            // 方式2
            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 5. 资源关闭
            JDBCUtils.closeResource(connection,ps);
        }
        return 0;
    }

    @Test
    // @Test 不能进入输入操作
    public void testInsert() throws Exception {
        Scanner scanner = new Scanner(System.in);
        System.out.print("请输入用户名：");
        String name = scanner.next();

        System.out.print("请输入邮箱：");
        String email = scanner.next();
        System.out.print("请输入生日：");
        String birthday = scanner.next(); // 内部含有隐式转换

        String sql = "insert into customers(name,email,birth)values(?,?,?)";
        int insetCount = update(sql, name, email, birthday);
        if (insetCount > 0) {
            System.out.println("添加成功");
        } else {
            System.out.println("添加失败");
        }
    }
}
