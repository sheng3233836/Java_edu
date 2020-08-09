import com.whitley.dao.UserDao;
import com.whitley.io.Resources;
import com.whitley.pojo.User;
import com.whitley.sqlSession.SqlSession;
import com.whitley.sqlSession.SqlSessionFactory;
import com.whitley.sqlSession.SqlSessionFactoryBuilder;
import org.dom4j.DocumentException;

import java.beans.PropertyVetoException;
import java.io.InputStream;
import java.util.List;


public class Test {

    @org.junit.Test
    public void test() throws Exception {
        InputStream resourceAsStream = Resources.getResourceAsStream("SqlMapConfig.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(resourceAsStream);
        SqlSession sqlSession = sqlSessionFactory.openSession();
        UserDao mapper = sqlSession.getMapper(UserDao.class);
        List<User> all = mapper.findAll();
        for (User user : all) {
            System.out.println(user);
        }
        User find = new User();
        find.setUsername("李四");
        System.out.println(mapper.findByCondition(find));
    }
}
