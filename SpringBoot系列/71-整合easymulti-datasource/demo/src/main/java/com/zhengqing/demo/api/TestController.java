//package com.zhengqing.demo.api;
//
//import cn.hutool.core.date.DateUtil;
//import cn.hutool.core.util.StrUtil;
//import io.swagger.annotations.Api;
//import lombok.RequiredArgsConstructor;
//import lombok.SneakyThrows;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@Slf4j
//@RestController
//@RequiredArgsConstructor
//@RequestMapping("/api/test")
//@Api(tags = "测试api")
//public class TestController {
//
//    private final JdbcTemplate jdbcTemplate;
//
//    @GetMapping("/testJdbc")
//    @SneakyThrows(Exception.class)
//    @Transactional(rollbackFor = Exception.class)
//    public Object testJdbc() {
//        jdbcTemplate.execute("TRUNCATE TABLE t_test;");
//        jdbcTemplate.execute(StrUtil.format("INSERT INTO `t_test` (`name`) VALUES ('time:{}');", DateUtil.now()));
//        return "ok:" + DateUtil.now();
//    }
//
//
//}
