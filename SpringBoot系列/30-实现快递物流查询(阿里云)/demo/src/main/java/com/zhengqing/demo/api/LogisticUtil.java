package com.zhengqing.demo.api;

import cn.hutool.core.lang.Assert;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.zhengqing.demo.model.dto.LogisticDTO;
import com.zhengqing.demo.model.vo.LogisticApiResult;
import com.zhengqing.demo.model.vo.LogisticVO;
import com.zhengqing.demo.util.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;


/**
 * <p> 快递物流信息查询 </p>
 *
 * @author zhengqingya
 * @description 可参考 https://market.aliyun.com/products/57124001/cmapi022273.html?spm=5176.730005.productlist.d_cmapi022273.e8357d36FVX3Eu&innerSource=search#sku=yuncode1627300000
 * @date 2021/10/23 7:51 下午
 */
@Slf4j
public class LogisticUtil {

    /**
     * 查询物流信息
     *
     * @param params 提交参数
     * @return 物流信息
     * @author zhengqingya
     * @date 2021/10/23 10:48 下午
     */
    public static LogisticVO getLogisticInfo(LogisticDTO params) {
        String no = params.getNo();
        String type = params.getType();
        String appCode = params.getAppCode();

        // 请求地址
        String requestUrl = String.format("https://kdwlcxf.market.alicloudapi.com/kdwlcx?no=%s&type=%s",
                no, StringUtils.isBlank(type) ? "" : type);
        // 发起请求
        Map<String, String> headerMap = Maps.newHashMap();
        headerMap.put("Authorization", String.format("APPCODE %s", appCode));
        String resultJson = HttpUtil.getUrl(requestUrl, headerMap);
//        String resultJson = "{\"status\":\"0\",\"msg\":\"ok\",\"result\":{\"number\":\"780098068058\",\"type\":\"zto\",\"list\":[{\"time\":\"2018-03-09 11:59:26\",\"status\":\"【石家庄市】 快件已在 【长安三部】 签收,签收人: 本人, 感谢使用中通快递,期待再次为您服务!\"},{\"time\":\"2018-03-09 09:03:10\",\"status\":\"【石家庄市】快件已到达【长安三部】（0311-85344265）,业务员 容晓光（13081105270）正在第1次派件\"},{\"time\":\"2018-03-08 23:43:44\",\"status\":\"【石家庄市】 快件离开 【石家庄】 发往 【长安三部】\"},{\"time\":\"2018-03-08 21:00:44\",\"status\":\"【石家庄市】 快件到达 【石家庄】\"},{\"time\":\"2018-03-07 01:38:45\",\"status\":\"【广州市】 快件离开 【广州中心】 发往 【石家庄】\"},{\"time\":\"2018-03-07 01:36:53\",\"status\":\"【广州市】 快件到达 【广州中心】\"},{\"time\":\"2018-03-07 00:40:57\",\"status\":\"【广州市】 快件离开 【广州花都】 发往 【石家庄中转】\"},{\"time\":\"2018-03-07 00:01:55\",\"status\":\"【广州市】 【广州花都】（020-37738523） 的 马溪 （18998345739） 已揽收\"}],\"deliverystatus\":\"3\",\"issign\":\"1\",\"expName\":\"中通快递\",\"expSite\":\"www.zto.com\",\"expPhone\":\"95311\",\"courier\":\"容晓光\",\"courierPhone\":\"13081105270\",\"updateTime\":\"2019-08-27 13:56:19\",\"takeTime\":\"2天20小时14分\",\"logo\":\"https://img3.fegine.com/express/zto.jpg\"}}";
        LogisticApiResult logisticApiResult = JSON.parseObject(resultJson, LogisticApiResult.class);
        Assert.notNull(logisticApiResult, "参数异常");
        Assert.isTrue(logisticApiResult.getStatus() == 0, logisticApiResult.getMsg());
        return logisticApiResult.getResult();
    }

    public static void main(String[] args) {
        LogisticVO logisticInfo = getLogisticInfo(LogisticDTO.builder()
                .no("780098068058")
                .type("zto")
                .appCode("xxx")
                .build());
        log.info("物流信息: {}", JSON.toJSONString(logisticInfo));
    }

}
