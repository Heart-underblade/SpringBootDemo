package com.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.demo.entity.Book;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

public interface MybatisService extends IService<Book> {

    Book findbyid(Integer id);

}
