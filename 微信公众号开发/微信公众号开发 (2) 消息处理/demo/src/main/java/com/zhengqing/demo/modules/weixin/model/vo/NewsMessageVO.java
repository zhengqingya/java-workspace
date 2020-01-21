package com.zhengqing.demo.modules.weixin.model.vo;

import com.google.common.collect.Lists;
import com.zhengqing.demo.modules.weixin.enumeration.EnumResponseMessageType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;


/**
 * <p> 图文消息 </p>
 *
 * @author : zhengqing
 * @description :
 * @date : 2020/1/15 14:04
 */
@Data
@ApiModel(description = "图文消息")
public class NewsMessageVO extends BaseMessageVO {

    @ApiModelProperty(value = "消息类型", hidden = true)
    private String MsgType = EnumResponseMessageType.NEWS.getType();

    @ApiModelProperty(value = "图文消息个数；当用户发送文本、图片、视频、图文、地理位置这五种消息时，开发者只能回复1条图文消息；其余场景最多可回复8条图文消息")
    private int ArticleCount = 0;

    @ApiModelProperty(value = "图文消息信息，注意，如果图文数超过限制，则将只发限制内的条数")
    private List<Article> Articles;

    @Data
    @ApiModel(description = "图文消息中Article类的定义")
    public static class Article {

        @ApiModelProperty(value = "图文消息标题")
        private String Title;

        @ApiModelProperty(value = "图文消息描述")
        private String Description = "";

        @ApiModelProperty(value = "图片链接，支持JPG、PNG格式，较好的效果为大图360*200，小图200*200")
        private String PicUrl = "";

        @ApiModelProperty(value = "点击图文消息跳转链接")
        private String Url = "";

    }

    public void addArticle(Article article) {
        if (Articles == null) {
            Articles = Lists.newLinkedList();
        }
        Articles.add(article);
        ArticleCount++;
    }

}
