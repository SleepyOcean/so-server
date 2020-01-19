package com.sleepy.blog;

import com.sleepy.blog.entity.TagEntity;
import com.sleepy.jpql.JpqlParser;
import com.sleepy.jpql.ParserParameter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author gehoubao
 * @create 2020-01-18 13:10
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class BlogServiceTest {

    @Autowired
    JpqlParser jpqlParser;
    @Autowired
    private EntityManagerFactory entityManagerFactory;

    public Session getSession() {
        return entityManagerFactory.unwrap(SessionFactory.class).openSession();
    }

    @Test
    public void test1() {
        Map<String, Object> parameters = new HashMap<>(4);
//        parameters.put("title", "%原则%");
//        parameters.put("readCount", 400);
        parameters.put("tagNames", Arrays.asList("平台", "后台"));
        String sql = jpqlParser.parse(new ParserParameter("testJpql.customSQL", parameters, "mysql")).getExecutableSql();
        Query query = getSession().createNativeQuery(sql).addEntity(TagEntity.class);
        List<TagEntity> result = query.getResultList();
        System.out.println(result.size());
    }
}