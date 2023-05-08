package com.zhengqing.demo.rpc.client;

import java.util.Map;

import com.dtflys.forest.annotation.DataVariable;
import com.dtflys.forest.annotation.Get;

/**
 * <p>
 * 远程调用三方API
 * </p>
 *
 * @author : zhengqing
 * @description :
 * @date : 2020/8/26 14:44
 */
public interface MyClient {

    /**
     * 本地测试接口
     */
    @Get(url = "http://127.0.0.1:80/demo/index")
    String index();

    @Get(url = "http://127.0.0.1:80/demo/hello?msg=${msg}")
    String hello(@DataVariable("msg") String msg);

    /**
     * 高德地图API
     */
    @Get(url = "http://ditu.amap.com/service/regeo?longitude=${longitude}&latitude=${latitude}")
    Map getLocation(@DataVariable("longitude") String longitude, @DataVariable("latitude") String latitude);

}
