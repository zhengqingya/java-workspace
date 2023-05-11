package com.zhengqing.common.web.util;

import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.SneakyThrows;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.Map;

/**
 * <p>
 * FreeMarker工具类
 * </p>
 *
 * @author zhengqingya
 * @description
 * @date 2021/2/25 11:16
 */
public class FreeMarkerUtil {

    /**
     * 将模板和数据模型合并 --> 输出合并模板内容
     *
     * @param templateDataMap 数据模型
     * @param templateContent 模板内容
     * @return 合并模板结果
     * @author zhengqingya
     * @date 2021/2/25 11:23
     */
    @SneakyThrows(Exception.class)
    public static String generateTemplateData(Map<String, Object> templateDataMap, String templateContent) {
        String templateData = "";
        try {
            StringWriter stringWriter = new StringWriter();
            Template template = new Template("template", new StringReader(templateContent),
                    new Configuration(Configuration.VERSION_2_3_28));
            template.process(templateDataMap, stringWriter);
            templateData = stringWriter.toString();
            stringWriter.close();
        } catch (Exception e) {
            throw new Exception("《FreeMarker合并模板》 异常：" + e.getMessage());
        }
        return templateData;
    }

}
