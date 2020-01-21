package com.zhengqing.demo.modules.weixin.service.impl;

import com.alibaba.fastjson.JSON;
import com.zhengqing.demo.config.Constants;
import com.zhengqing.demo.modules.weixin.model.menu.Menu;
import com.zhengqing.demo.modules.weixin.model.WeixinResponseResult;
import com.zhengqing.demo.modules.weixin.service.IMenuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * <p> 菜单 - 服务实现类 </p>
 *
 * @author : zhengqing
 * @description :
 * @date : 2020/1/16 16:15
 */
@Slf4j
@Service
public class MenuServiceImpl implements IMenuService {

    @Autowired
    private RestTemplate restTemplate;


    @Override
    public Object getMenu(String accessToken) {
        Object menu = restTemplate.getForObject(Constants.GET_MENU_URL.replace("ACCESS_TOKEN", accessToken), Object.class);
        return menu;
    }

    @Override
    public WeixinResponseResult deleteMenu(String accessToken) {
        WeixinResponseResult result = restTemplate.getForObject(Constants.DELETE_MENU_URL.replace("ACCESS_TOKEN", accessToken), WeixinResponseResult.class);
        return result;
    }

    @Override
    public WeixinResponseResult createMenu(Menu menu, String accessToken) {
        // 将菜单对象转换成json字符串
        String jsonMenu = JSON.toJSONString(menu);
        WeixinResponseResult result = restTemplate.postForObject(Constants.CREATE_MENU_URL.replace("ACCESS_TOKEN", accessToken), jsonMenu, WeixinResponseResult.class);
        return result;
    }

}
