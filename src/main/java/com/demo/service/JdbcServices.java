package com.demo.service;

import com.demo.entity.Book;
import java.util.List;

public interface JdbcServices {
    /**
     * 新增一本书
     * @param name
     * @param descrip
     */
    int create(Integer id, String name, String descrip);

    /**
     * 根据name查询书籍
     * @param name
     * @return
     */
    List<Book> getByName(String name);

    /**
     * 根据name删除书籍
     * @param name
     */
    int deleteByName(String name);

    /**
     * 获取书籍总量
     */
    int getAllBooks();

    /**
     * 清空书籍
     */
    int deleteAllBooks();

}
