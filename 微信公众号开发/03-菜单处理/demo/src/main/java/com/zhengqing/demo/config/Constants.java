package com.zhengqing.demo.config;

/**
 * <p> 全局常用变量 </p>
 *
 * @author : zhengqing
 * @description :
 * @date : 2019/11/7 9:51
 */
public class Constants {

    /**
     * 解决返回json字符串中文乱码问题
     */
    public static final String CONTENT_TYPE = "application/json;charset=utf-8";

    /**
     * TODO 这里的token是微信公众平台上自己所配的！
     */
    public static final String TOKEN = "xxx";

    /**
     * TODO 填写自己的 `appID` 和 `appsecret`
     */
    public static final String APP_ID = "xxx";
    public static final String APP_SECRET = "xxx";

    /**
     * 通过 `GET请求方式` 获取 `access_token`
     */
    public static final String GET_ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
    /**
     * TODO 只做临时方便测试使用
     */
    public static final String ACCESS_TOKEN = "xxx";

    /**
     * 查询菜单接口 - GET请求
     */
    public static final String GET_MENU_URL = "https://api.weixin.qq.com/cgi-bin/menu/get?access_token=ACCESS_TOKEN";
    /**
     * 删除菜单接口 - GET请求 （注意，在个性化菜单时，调用此接口会删除默认菜单及全部个性化菜单）
     */
    public static final String DELETE_MENU_URL = "https://api.weixin.qq.com/cgi-bin/menu/delete?access_token=ACCESS_TOKEN";
    /**
     * 创建菜单接口 - POST请求
     */
    public static final String CREATE_MENU_URL = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";

}
