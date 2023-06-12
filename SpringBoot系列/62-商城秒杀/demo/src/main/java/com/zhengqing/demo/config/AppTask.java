package com.zhengqing.demo.config;

import com.zhengqing.demo.enums.SeckillModeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;


/**
 * <p>
 * 应用任务
 * </p>
 *
 * @author zhengqingya
 * @description
 * @date 2021/01/13 10:11
 */
@Slf4j
@Component
public class AppTask {

    @PostConstruct
    public void init() {
        System.err.println("秒杀模式: \n" + SeckillModeEnum.MODE_VALUES);
    }

}
