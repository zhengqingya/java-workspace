package com.zhengqing.demo.repository;


import com.zhengqing.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface UserRepositoryDls extends JpaRepository<User, Integer>, QuerydslPredicateExecutor<User> {

}