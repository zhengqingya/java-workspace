package com.zhengqing.client.api;

import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
@Controller
@RequestMapping("")
@Api(tags = {"测试api"})
public class TestController {

    @GetMapping("/index.html")
    public String hello(String code, Model model) {
        if (StringUtils.isNotBlank(code)) {
            RestTemplate restTemplate = new RestTemplate();
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
            // 刷新令牌
//            map.add("grant_type", "refresh_token");
//            map.add("refresh_token", "07a12e6d-73f4-43c0-9533-dd28d17449a5");
            Map<String, String> authResponseMap = restTemplate.postForObject("http://127.0.0.1:10010/oauth/token", map, Map.class);
            log.info("authResponse: {}", JSON.toJSONString(authResponseMap));
            String access_token = authResponseMap.get("access_token");


            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "Bearer " + access_token);
            HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
            ResponseEntity<String> entity = restTemplate.exchange("http://127.0.0.1:10030/admin/hello", HttpMethod.GET, httpEntity, String.class);
            model.addAttribute("msg", entity.getBody());
        }
        return "index";
    }

}
