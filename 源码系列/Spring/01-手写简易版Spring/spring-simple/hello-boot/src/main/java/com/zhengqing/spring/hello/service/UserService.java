package com.zhengqing.spring.hello.service;

import com.zhengqing.spring.annotation.Autowired;
import com.zhengqing.spring.annotation.Component;
import com.zhengqing.spring.annotation.Transactional;
import com.zhengqing.spring.hello.User;
import com.zhengqing.spring.jdbc.JdbcTemplate;

import java.util.List;

@Component
@Transactional
public class UserService {

    @Autowired
    JdbcTemplate jdbcTemplate;

    public void initDb() {
        String sql = "CREATE TABLE IF NOT EXISTS users (\n" +
                "                    email VARCHAR(50) PRIMARY KEY,\n" +
                "                    name VARCHAR(50) NOT NULL,\n" +
                "                    password VARCHAR(50) NOT NULL\n" +
                "                )";
        jdbcTemplate.update(sql);
    }

    public User getUser(String email) {
        return jdbcTemplate.queryForObject("SELECT * FROM users WHERE email = ?", User.class, email);
    }

    public List<User> getUsers() {
        return jdbcTemplate.queryForList("SELECT email, name FROM users", User.class);
    }

    public User createUser(String email, String name, String password) {
        User user = new User();
        user.email = email.strip().toLowerCase();
        user.name = name.strip();
        user.password = password;
        jdbcTemplate.update("INSERT INTO users (email, name, password) VALUES (?, ?, ?)", user.email, user.name, user.password);
        return user;
    }

}
