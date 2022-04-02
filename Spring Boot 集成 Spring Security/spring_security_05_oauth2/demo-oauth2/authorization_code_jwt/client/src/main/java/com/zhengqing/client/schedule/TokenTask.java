package com.zhengqing.client.schedule;

import cn.hutool.core.date.DateTime;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.annotation.SessionScope;

import java.util.Map;

/**
 * <p> 解决 Token 管理问题 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2022/4/1 10:09 下午
 */
@Slf4j
@Component
@SessionScope
@EnableScheduling
public class TokenTask {

    public String access_token = "";
    public String refresh_token = "";

    public String getData(String code) {
        if (StringUtils.isBlank(code)) {
            return "未认证";
        }
        if (StringUtils.isBlank(this.access_token)) {
            /**
             * 如果 code 不为 null，标识是通过授权服务器重定向到这个地址来的
             * 根据拿到的 code 去获取 Token
             */
            MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
            map.add("code", code);
            map.add("client_id", "zq_app_id");
            map.add("client_secret", "zq_app_secret");
            map.add("redirect_uri", "http://127.0.0.1:10020/index.html");
            map.add("grant_type", "authorization_code");
            Map<String, String> authResponseMap = new RestTemplate().postForObject("http://127.0.0.1:10010/oauth/token", map, Map.class);
            log.info("authResponse: {}", JSON.toJSONString(authResponseMap));

            this.access_token = authResponseMap.get("access_token");
            this.refresh_token = authResponseMap.get("refresh_token");

            return this.loadDataFromResServer();
        } else {
            return this.loadDataFromResServer();
        }
    }

    /**
     * 加载资源服务器数据
     */
    private String loadDataFromResServer() {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "Bearer " + this.access_token);
            HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
            ResponseEntity<String> entity = new RestTemplate().exchange("http://127.0.0.1:10030/admin/hello", HttpMethod.GET, httpEntity, String.class);
            return entity.getBody();
        } catch (RestClientException e) {
            log.error("加载资源服务器数据:", e);
            return "未加载";
        }
    }

    /**
     * 刷新令牌定时任务
     * 每隔 55 分钟去刷新一下 access_token（access_token 有效期是 60 分钟）。
     */
//    @Scheduled(cron = "0 */55 * * * ?")
    @Scheduled(cron = "*/30 * * * * ?") // 每隔30秒
    public void tokenTask() {
        log.info("<<<<<< 刷新令牌定时任务 Start: 【{}】 >>>>>>", DateTime.now());
        if (StringUtils.isBlank(this.refresh_token)) {
            return;
        }
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("client_id", "zq_app_id");
        map.add("client_secret", "zq_app_secret");
        map.add("grant_type", "refresh_token");
        map.add("refresh_token", this.refresh_token);
        Map<String, String> authResponseMap = new RestTemplate().postForObject("http://127.0.0.1:10010/oauth/token", map, Map.class);
        log.info("刷新令牌定时任务 authResponse: {}", JSON.toJSONString(authResponseMap));

        this.access_token = authResponseMap.get("access_token");
        this.refresh_token = authResponseMap.get("refresh_token");

        // 测试解析用户信息
        this.checkToken();
    }

    /**
     * 解析jwt中的用户信息
     */
    private void checkToken() {
        if (StringUtils.isBlank(this.access_token)) {
            return;
        }
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("token", this.access_token);
        Map<String, String> authResponseMap = new RestTemplate().postForObject("http://127.0.0.1:10010/oauth/check_token", map, Map.class);
        log.info("解析jwt中的用户信息 authResponse: {}", JSON.toJSONString(authResponseMap));
    }

}
