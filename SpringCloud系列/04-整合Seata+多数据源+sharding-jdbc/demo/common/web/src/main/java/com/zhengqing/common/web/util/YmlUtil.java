package com.zhengqing.common.web.util;

import cn.hutool.core.lang.Assert;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;

/**
 * <p> 读取yml文件工具类 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2021/8/24 10:28 下午
 */
@Slf4j
public class YmlUtil {

    /**
     * 读取yml内容转对象
     *
     * @param ymlResourceLocation yml文件内容
     * @param clz                 目标对象class
     * @return T
     * @author zhengqingya
     * @date 2021/8/24 10:52 下午
     */
    @SneakyThrows(Exception.class)
    public static <T> T getYml(String ymlResourceLocation, Class<T> clz) {
        Assert.notBlank(ymlResourceLocation, "yml文件不存在!");
        Yaml yaml = new Yaml();
        T t = yaml.loadAs(new FileInputStream(ymlResourceLocation), clz);
        log.info("[读取yml] 文件内容：{}", t);
        return t;
    }

}
