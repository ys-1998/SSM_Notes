package work.yspan.utils;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

public class Mybatis_utils {


    private static SqlSessionFactory sqlSessionFactory;

   static  {
        try {
            String resource="config/mybatis_config.xml";
            InputStream in = Resources.getResourceAsStream(resource);
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(in);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static SqlSession getsqlSession(){

       return sqlSessionFactory.openSession();
    }

}
