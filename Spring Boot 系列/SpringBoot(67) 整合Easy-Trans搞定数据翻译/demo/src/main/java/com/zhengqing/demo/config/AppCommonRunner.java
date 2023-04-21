package com.zhengqing.demo.config;

import com.fhs.trans.service.impl.DictionaryTransService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 服务初始化之后，执行方法
 * </p>
 *
 * @author zhengqingya
 * @description
 * @date 2020/5/22 19:29
 */
@Slf4j
@Component
@AllArgsConstructor
public class AppCommonRunner implements CommandLineRunner {

    private DictionaryTransService dictionaryTransService;

    @Override
    public void run(String... args) throws Exception {

        Map<String, String> transMap = new HashMap<>();
        transMap.put("0", "男");
        transMap.put("1", "女");
        this.dictionaryTransService.refreshCache("sex", transMap);

        //如果字典修改了 需要刷新所有微服务的字典缓存 请调用：
//        this.dictionaryTransService.refreshCacheAndNoticeOtherService("sex", transMap);

        log.info("初始化Easy-Trans数据...");
    }

}
