import com.bdqn.ssm.dao.UserDao;
import com.bdqn.ssm.util.MybatisUtil;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

public class test {

    SqlSession sqlSession=null;
    @Test
    public void aaa() {
        sqlSession= MybatisUtil.getSqlSession();
        int count=sqlSession.getMapper(UserDao.class).getUserCount(null,0);
        System.out.println(count);
    }
}
