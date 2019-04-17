package com.bdqn.ssm.util;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;


public class MybatisUtil {
    private static SqlSessionFactory sqlSessionFactory=null;
    private static SqlSession sqlSession=null;
    private static String resource="mybatis-config.xml";
    static{
        try {
            InputStream inputStream=Resources.getResourceAsStream(resource);
            sqlSessionFactory=new SqlSessionFactoryBuilder().build(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //获得SqlSession
    public static SqlSession getSqlSession(){
        return sqlSessionFactory.openSession(false);//false 关闭自动提交事务 true打开自动提交事件
    }
    //关闭SqlSession
    public static void closeSqlSession(SqlSession sqlSession){
        if(sqlSession!=null){
            sqlSession.close();
        }
    }
}
