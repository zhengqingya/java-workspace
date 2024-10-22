package com.zhengqing.spring.hello.web;

import com.zhengqing.spring.annotation.Autowired;
import com.zhengqing.spring.annotation.GetMapping;
import com.zhengqing.spring.annotation.PathVariable;
import com.zhengqing.spring.annotation.RestController;
import com.zhengqing.spring.exception.DataAccessException;
import com.zhengqing.spring.hello.User;
import com.zhengqing.spring.hello.service.UserService;

import java.util.List;
import java.util.Map;

@RestController
public class ApiController {

    @Autowired
    UserService userService;

    @GetMapping("/api/user/{email}")
    Map<String, Boolean> userExist(@PathVariable("email") String email) {
        if (!email.contains("@")) {
            throw new IllegalArgumentException("Invalid email");
        }
        try {
            userService.getUser(email);
            return Map.of("result", Boolean.TRUE);
        } catch (DataAccessException e) {
            return Map.of("result", Boolean.FALSE);
        }
    }

    @GetMapping("/api/users")
    List<User> users() {
        return userService.getUsers();
    }
}
