package com.zhengqing.user.api;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.zhengqing.common.core.util.IdGeneratorUtil;
import com.zhengqing.common.db.config.dynamic.DataSourceConfig;
import com.zhengqing.user.entity.User;
import com.zhengqing.user.service.IUserService;
import io.seata.spring.annotation.GlobalTransactional;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p> 分布式事务 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2021/01/13 10:11
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/seata")
@Api(tags = {"分布式事务"})
@DS(DataSourceConfig.SHARDING_DATA_SOURCE_NAME)
public class SeataController {

    private final IUserService userService;

    @PostMapping("test1")
    @GlobalTransactional
    public User test1() {
        long id = IdGeneratorUtil.snowflakeId();
        if (id % 2 == 0) {
            id += 1;
        }
        User user = User.builder()
                .userId(id)
                .username("test")
                .password("123456")
                .sex((byte) 1)
                .remark("test data")
                .build();
        this.userService.save(user);
        int a = 1 / 0;
        return user;
    }

    @PostMapping("test2")
    public String test2(@RequestBody User user) {
        this.userService.testSeata(user);
        return "OK";
    }


}
