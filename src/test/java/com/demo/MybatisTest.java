package com.demo;

import com.demo.dao.BookMapper;
import com.demo.entity.Book;
import com.demo.service.MybatisService;
import org.junit.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.junit.runner.RunWith;

@RunWith(SpringRunner.class)
@SpringBootTest
@MapperScan("com.demo.dao")
public class MybatisTest {
    @Autowired
    private MybatisService mybatisService;
    private BookMapper bookMapper;

    @Test
    public void findbyid(){
        Book book = mybatisService.findbyid(5);
        System.out.println(book);
    }

}
