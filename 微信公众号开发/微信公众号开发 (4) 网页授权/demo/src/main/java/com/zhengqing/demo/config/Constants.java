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

    // ==================================================================================================================================

    /**
     * 用户授权页面 -> 获取code   【scope：应用授权作用域
     *                                      值： snsapi_base （不弹出授权页面，直接跳转，只能获取用户openid）
     *                                          snsapi_userinfo （弹出授权页面，可通过openid拿到昵称、性别、所在地。并且， 即使在未关注的情况下，只要用户授权，也能获取其信息 ）
     * 】
     */
    public static final String AUTH_URL = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=SCOPE&state=STATE#wechat_redirect";
    /**
     * 授权回调地址 TODO 这里需修改为自己的哦！
     */
    public static final String AUTH_REDIRECT_URI = "http://域名/api/weixin/basic/getOpenId";

    /**
     * 通过`code`换取网页授权`access_token`和`openid` - GET请求
     */
    public static final String AUTH_GET_ACCESS_TOKEN_AND_OPENID = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";


    /**
     * 获取用户信息(需scope为 `snsapi_userinfo`) - GET请求
     */
    public static final String AUTH_GET_USER_INFO = "https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";

    /**
     * 检验授权凭证（access_token）是否有效 - GET请求
     */
    public static final String AUTH_CHECK_ACCESS_TOKEN = "https://api.weixin.qq.com/sns/auth?access_token=ACCESS_TOKEN&openid=OPENID";

    /**
     * 刷新access_token（如果需要） - GET请求
     */
    public static final String AUTH_REFRESH_ACCESS_TOKEN = "https://api.weixin.qq.com/sns/oauth2/refresh_token?appid=APPID&grant_type=refresh_token&refresh_token=REFRESH_TOKEN";

}
