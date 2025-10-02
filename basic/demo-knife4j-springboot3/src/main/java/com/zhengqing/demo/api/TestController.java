package com.zhengqing.demo.api;


import cn.hutool.core.date.DateUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p> 测试api </p>
 *
 * @author zhengqingya
 * @description
 * @date 2021/10/5 2:36 下午
 */
@Slf4j
@RestController
@RequestMapping("")
@Tag(name = "测试api")
public class TestController {

    @GetMapping("time")
    @Operation(summary = "获取当前时间")
    public String time() {
        log.info("time: {}", DateUtil.date());
        return DateUtil.date().toString();
    }

    @GetMapping("/users/{userId}")
    @Operation(summary = "获取用户详情", description = "根据ID查询用户详细信息")
    @Parameter(name = "userId", description = "用户ID", required = true, in = ParameterIn.PATH)
//    @Parameters({
//            @Parameter(name = "id", description = "文件id", in = ParameterIn.PATH),
//            @Parameter(name = "token", description = "请求token", required = true, in = ParameterIn.HEADER),
//            @Parameter(name = "name", description = "文件名称", required = true, in = ParameterIn.QUERY)
//    })
    public UserVO getUserById(@PathVariable Long userId) {
        return UserVO.builder().id(userId).username("admin").password("<PASSWORD>").build();
    }

    @Data
    @Builder
    @Schema(description = "用户信息实体")
    public static class UserVO {

        @Schema(description = "用户ID", example = "1001")
        private Long id;

        @Schema(description = "用户名", example = "admin")
        private String username;

        @Schema(description = "密码", example = "123456")
        private String password;
    }

}
