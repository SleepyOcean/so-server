package com.sleepy.blog;

import com.sleepy.blog.entity.TagEntity;
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

    @Test
    public void test1() {
    }
}