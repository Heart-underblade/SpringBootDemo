package com.demo.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.demo.dao.BookMapper;
import com.demo.entity.Book;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class MybatisImpl extends ServiceImpl<BookMapper, Book> implements MybatisService{

    @Override
    public Book findbyid(Integer id){
        return baseMapper.findbyid(id);
    }

}
