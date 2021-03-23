package Test02;

import Jutil.JDBCUtils;
import bean.Customer;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.*;

/**
 * @author HXS
 * @create 2021-03-23 20:02
 */

// 测试使用preparedStatement 操作Blob类型的数据
public class BlobTest {

    // 向数据表customers中插入Blob类型的字段
    @Test
    public void testInsert() throws Exception {
        Connection connection = JDBCUtils.getConnection();

        String sql = "insert into customers(name,email,birth,photo)values(?,?,?,?)";

        PreparedStatement ps = connection.prepareStatement(sql);

        ps.setObject(1, "张宇");
        ps.setObject(2,"213@qq.com");
        ps.setObject(3,"2000-01-01");

        FileInputStream is = new FileInputStream(new File("101.png"));
        ps.setBlob(4,is);

        ps.execute();


        JDBCUtils.closeResource(connection,ps);
    }

    // 查询数据表customers中Blob类型的字段 / 没写try-catch-finally
    @Test
    public void testQuery() throws Exception {

        Connection connection = JDBCUtils.getConnection();

        String sql = "select id,name,email,birth, photo from customers where id = ?";

        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setObject(1,23);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {

//            方式1： 参数 索引
//            int id = rs.getInt(1);
//            String name = rs.getString(2);
//            String birth = rs.getString(3);
//            Date date = rs.getDate(4);

            // 方式2： 参数 别名
            int id = rs.getInt("id");
            String name = rs.getString("name");
            String birth = rs.getString("email");
            Date date = rs.getDate("birth");

            // 将Blob类型的字段下载下类，以文件的方式保存到本地
            Blob photo = rs.getBlob("photo");
            InputStream is = photo.getBinaryStream();
            FileOutputStream fos = new FileOutputStream("102.png");
            byte[] buffer = new byte[1024];
            int len;
            while ((len = is.read(buffer)) != -1) {
                fos.write(buffer,0,len);
            }

            Customer customer = new Customer(id,name,birth,date);
            System.out.println(customer);

            is.close();
            fos.close();
        }
        JDBCUtils.closeResource(connection,ps,rs);
    }
}
