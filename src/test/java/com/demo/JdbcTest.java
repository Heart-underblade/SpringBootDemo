package com.demo;

import com.demo.entity.Book;
import com.demo.service.JdbcServices;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.boot.test.context.SpringBootTest;


import java.util.List;
@RunWith(SpringRunner.class)
@SpringBootTest
public class JdbcTest {
    @Autowired
    private JdbcServices Serivce;

    @Before
    public void setUp() {
        // 清空表
        Serivce.deleteAllBooks();
    }

    @Test
    public void test() throws Exception {
        // 插入5本书
        Serivce.create(1,"T", "1");
        Serivce.create(2,"M", "11");
        Serivce.create(3,"D", "30");
        Serivce.create(4,"O", "21");
        Serivce.create(5,"L", "17");

        // 查询名为O的书
        List<Book> bookList = Serivce.getByName("O");
        Assert.assertEquals("21", bookList.get(0).getDescription());

        // 查数据库
        Assert.assertEquals(5, Serivce.getAllBooks());

        // 删除两本书
        Serivce.deleteByName("T");
        Serivce.deleteByName("M");


        Assert.assertEquals(3, Serivce.getAllBooks());

    }
}
