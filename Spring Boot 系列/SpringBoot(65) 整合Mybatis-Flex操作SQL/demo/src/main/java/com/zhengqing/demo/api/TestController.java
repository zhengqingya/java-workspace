package com.zhengqing.demo.api;


import cn.hutool.core.date.DateUtil;
import com.mybatisflex.core.query.QueryWrapper;
import com.zhengqing.demo.mapper.AccountMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.zhengqing.demo.entity.table.Tables.ACCOUNT;

/**
 * <p> 测试api </p>
 *
 * @author zhengqingya
 * @description
 * @date 2021/10/5 2:36 下午
 */
@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("")
@Api(tags = "测试api")
public class TestController {

    private AccountMapper accountMapper;

    @GetMapping("time")
    @ApiOperation("time")
    public String time() {
        log.info("time: {}", DateUtil.date());
        return DateUtil.date().toString();
    }

    @GetMapping("sql")
    @ApiOperation("sql")
    public Object sql() {
        // 查询数据
        QueryWrapper queryWrapper = QueryWrapper.create()
                .select(
                        ACCOUNT.ID,
                        ACCOUNT.USER_NAME
                )
                .from(ACCOUNT)
                .where(ACCOUNT.ID.eq(1))
                .limit(1);
        return this.accountMapper.selectListByQuery(queryWrapper);
    }

}
