package com.zhengqing.demo.modules.weixin.service;


import com.zhengqing.demo.modules.weixin.model.menu.Menu;
import com.zhengqing.demo.modules.weixin.model.WeixinResponseResult;

/**
 * <p> 菜单 - 服务类 </p>
 *
 * @author : zhengqing
 * @description :
 * @date : 2020/1/16 16:13
 */
public interface IMenuService {

    /**
     * 查询菜单
     *
     * @param accessToken:访问凭据
     * @return: java.lang.Object
     */
    Object getMenu(String accessToken);

    /**
     * 删除菜单
     *
     * @param accessToken:访问凭据
     * @return: com.zhengqing.demo.modules.weixin.model.WeixinResponseResult
     */
    WeixinResponseResult deleteMenu(String accessToken);

    /**
     * 创建菜单
     *
     * @param menu        : 创建的菜单数据
     * @param accessToken : 访问凭据
     * @return: com.zhengqing.demo.modules.weixin.model.WeixinResponseResult
     */
    WeixinResponseResult createMenu(Menu menu, String accessToken);

}
