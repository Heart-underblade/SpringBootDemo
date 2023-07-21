package com.demo.service;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.demo.dao.MybatisMapper;
import com.demo.entity.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional
public class MybatisImpl extends ServiceImpl<MybatisMapper, Book> implements MybatisService{

    @Override
    public Book findbyid(Integer id){
        return baseMapper.findbyid(id);
    }

}
