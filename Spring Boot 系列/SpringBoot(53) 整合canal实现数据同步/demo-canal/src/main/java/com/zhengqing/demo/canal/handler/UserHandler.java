package com.zhengqing.demo.canal.handler;

import com.zhengqing.demo.canal.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import top.javatool.canal.client.annotation.CanalTable;
import top.javatool.canal.client.handler.EntryHandler;


/**
 * <p> 订阅数据库的增删改操作 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2022/1/20 19:05
 */
@Slf4j
@Component
@CanalTable(value = "t_user")
public class UserHandler implements EntryHandler<User> {

    @Override
    public void insert(User user) {
        log.info("insert message  {}", user);
    }

    @Override
    public void update(User before, User after) {
        log.info("update before {} ", before);
        log.info("update after {}", after);
    }

    @Override
    public void delete(User user) {
        log.info("delete  {}", user);
    }

}
