package com.zhengqing.demo.api;


import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zhengqing.demo.entity.QUser;
import com.zhengqing.demo.entity.User;
import com.zhengqing.demo.repository.UserRepositoryDls;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

/**
 * <p> 测试api </p>
 *
 * @author zhengqingya
 * @description
 * @date 2021/10/5 2:36 下午
 */
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("")
@Api(tags = "测试api")
public class TestController {

    private JPAQueryFactory jpaQueryFactory;
    private UserRepositoryDls userRepositoryDls;


    @GetMapping("time")
    @ApiOperation("time")
    public String time() {
        log.info("time: {}", DateUtil.date());
        return DateUtil.date().toString();
    }

    @GetMapping("sql")
    @ApiOperation("sql")
    public Object sql() {

        // 保存数据
        this.userRepositoryDls.save(new User(1, RandomUtil.randomString(10), RandomUtil.randomString(10)));

        // 查询数据
        User result = this.jpaQueryFactory
                .selectFrom(QUser.user)
                .where(QUser.user.id.eq(1))
                .fetchOne();

        Optional<User> one = this.userRepositoryDls.findOne(QUser.user.id.eq(1));

        return one;
    }

}
