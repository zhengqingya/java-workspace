package com.zhengqing.spring.webmvc.controller;

import com.zhengqing.spring.annotation.Configuration;
import com.zhengqing.spring.annotation.Import;
import com.zhengqing.spring.webmvc.WebMvcConfiguration;

@Configuration
@Import(WebMvcConfiguration.class)
public class ControllerConfiguration {

}
