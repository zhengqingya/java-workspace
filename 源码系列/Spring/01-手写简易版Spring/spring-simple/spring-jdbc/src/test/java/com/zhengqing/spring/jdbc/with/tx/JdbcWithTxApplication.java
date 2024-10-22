package com.zhengqing.spring.jdbc.with.tx;

import com.zhengqing.spring.annotation.ComponentScan;
import com.zhengqing.spring.annotation.Configuration;
import com.zhengqing.spring.annotation.Import;
import com.zhengqing.spring.jdbc.JdbcConfiguration;

@ComponentScan
@Configuration
@Import(JdbcConfiguration.class)
public class JdbcWithTxApplication {

}
