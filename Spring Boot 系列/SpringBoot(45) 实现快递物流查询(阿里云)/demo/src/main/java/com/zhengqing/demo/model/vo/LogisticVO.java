package com.zhengqing.demo.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * <p> 物流-响应参数 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2021/10/23 9:19 下午
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("物流-响应参数")
public class LogisticVO {

    @ApiModelProperty("运单编号")
    private String number;

    @ApiModelProperty("快递公司编码[见附表]")
    private String type;

    @ApiModelProperty("投递状态 0快递收件(揽件)1在途中 2正在派件 3已签收 4派送失败 5.疑难件 6.退件签收")
    private String deliverystatus;

    @ApiModelProperty("是否本人签收")
    private String issign;

    @ApiModelProperty("快递公司名字")
    private String expName;

    @ApiModelProperty("快递公司官网")
    private String expSite;

    @ApiModelProperty("快递公司电话")
    private String expPhone;

    @ApiModelProperty("快递员")
    private String courier;

    @ApiModelProperty("快递员电话")
    private String courierPhone;

    @ApiModelProperty("最新轨迹的时间")
    private String updateTime;

    @ApiModelProperty("发货到收货耗时(截止最新轨迹)")
    private String takeTime;

    @ApiModelProperty("快递公司logo")
    private String logo;

    @ApiModelProperty("事件轨迹集")
    private List<LogisticItem> list;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @ApiModel("事件轨迹集")
    public static class LogisticItem {
        @ApiModelProperty("时间点")
        private String time;

        @ApiModelProperty("事件详情")
        private String status;
    }

}
