import com.whitley.io.Resources;
import com.whitley.pojo.User;
import com.whitley.sqlSession.SqlSession;
import com.whitley.sqlSession.SqlSessionFactory;
import com.whitley.sqlSession.SqlSessionFactoryBuilder;
import org.dom4j.DocumentException;

import java.beans.PropertyVetoException;
import java.io.InputStream;

public class Test {
    public void test() throws PropertyVetoException, DocumentException {
        InputStream resourceAsStream = Resources.getResourceAsStream("SqlMapConfig.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(resourceAsStream);
        SqlSession sqlSession = sqlSessionFactory.openSession();
        User user = new User();
        user.setId(1);
        user.setUsername("zhangsan");
        User user1 = sqlSession.selectOne("user.selectOne", user);
    }
}
