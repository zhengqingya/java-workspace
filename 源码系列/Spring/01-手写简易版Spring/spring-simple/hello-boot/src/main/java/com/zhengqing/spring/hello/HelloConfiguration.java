package com.zhengqing.spring.hello;


import com.zhengqing.spring.annotation.ComponentScan;
import com.zhengqing.spring.annotation.Configuration;
import com.zhengqing.spring.annotation.Import;
import com.zhengqing.spring.jdbc.JdbcConfiguration;
import com.zhengqing.spring.webmvc.WebMvcConfiguration;

@ComponentScan
@Configuration
@Import({JdbcConfiguration.class, WebMvcConfiguration.class})
public class HelloConfiguration {

}
