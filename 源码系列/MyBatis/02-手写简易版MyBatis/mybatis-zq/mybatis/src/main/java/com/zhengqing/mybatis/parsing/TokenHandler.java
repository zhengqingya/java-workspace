package com.zhengqing.mybatis.parsing;

/**
 * <p> 标记处理器 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2024/4/22 01:41
 */
public interface TokenHandler {

    /**
     * 处理标记
     *
     * @param content 参数内容
     * @return 标记解析后的内容 eg: 参数名称 -> ?
     */
    String handleToken(String content);

}
