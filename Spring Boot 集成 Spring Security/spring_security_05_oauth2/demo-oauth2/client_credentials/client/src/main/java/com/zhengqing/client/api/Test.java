package com.zhengqing.client.api;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * <p> 测试api </p>
 *
 * @author zhengqingya
 * @description
 * @date 2022/4/1 16:02
 */
@Slf4j
public class Test {

    public static void main(String[] args) {
        RestTemplate restTemplate = new RestTemplate();
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("client_id", "zq_app_id");
        map.add("client_secret", "zq_app_secret");
        map.add("grant_type", "client_credentials");
        Map<String, String> authResponseMap = restTemplate.postForObject("http://127.0.0.1:10010/oauth/token", map, Map.class);
        log.info("authResponse: {}", JSON.toJSONString(authResponseMap));
        String access_token = authResponseMap.get("access_token");

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + access_token);
        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<String> entity = restTemplate.exchange("http://127.0.0.1:10030/hello", HttpMethod.GET, httpEntity, String.class);
        log.info("result: {}", entity.getBody());
    }

}
