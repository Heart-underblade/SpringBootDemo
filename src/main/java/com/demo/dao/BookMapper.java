package com.demo.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.demo.entity.Book;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BookMapper extends BaseMapper<Book> {
    void add(Book book);


    void update(Integer id, String name, String description);


    void delete(Integer id);


    List<Book> findall();


    Book findbyid(@Param("id") Integer id);
}
