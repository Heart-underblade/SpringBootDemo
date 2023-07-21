package com.demo.service;

import com.demo.entity.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JdbcImpl implements JdbcServices {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public int create(Integer id, String name, String descript) {
        return jdbcTemplate.update("insert into Book(id, name, description) values(?, ?, ?)",id, name, descript);
    }

    @Override
    public List<Book> getByName(String name) {
        return jdbcTemplate.query("select name, description from Book where name = ?", (resultSet, i) -> {
            Book book = new Book();
            book.setName(resultSet.getString("name"));
            book.setDescription(resultSet.getString("description"));
            return book;
        }, name);
    }

    @Override
    public int deleteByName(String name) {
        return jdbcTemplate.update("delete from Book where name = ?", name);
    }


    @Override
    public int getAllBooks() {
        return jdbcTemplate.queryForObject("select count(1) from Book", Integer.class);
    }

    @Override
    public int deleteAllBooks(){
        return jdbcTemplate.update("delete from Book");
    }
}
