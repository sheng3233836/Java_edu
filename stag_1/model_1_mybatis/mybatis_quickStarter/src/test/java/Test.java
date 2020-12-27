import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import com.whitley.dao.ProductDao;
import com.whitley.dao.UserDao;
import com.whitley.pojo.Product;
import com.whitley.pojo.User;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

/**
 * @author yuanxin
 * @date 2020-08-16
 */
public class Test {
    @org.junit.Test
    public void test1() throws IOException {
        InputStream resourceAsStream = Resources.getResourceAsStream("sqlMapConfig.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(resourceAsStream);
        SqlSession sqlSession = sqlSessionFactory.openSession();
            ProductDao productDao = sqlSession.getMapper(ProductDao.class);
            List<Product> products = productDao.findProductAndUser();
        for (Product product : products) {
            System.out.println(product);
        }
        sqlSession.close();
    }

    @org.junit.Test
    public void test2() throws IOException {
        InputStream resourceAsStream = Resources.getResourceAsStream("sqlMapConfig.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(resourceAsStream);
        SqlSession sqlSession = sqlSessionFactory.openSession();
        UserDao userDao = sqlSession.getMapper(UserDao.class);
        List<User> users = userDao.findUserAndProduct();
        for (User product : users) {
            System.out.println(product);
        }
        sqlSession.close();
        userDao = sqlSession.getMapper(UserDao.class);
        users = userDao.findUserAndProduct();
        for (User product : users) {
            System.out.println(product);
        }
    }
}
