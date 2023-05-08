package com.zhengqing.demo.modules.weixin.model.message.dto;

import com.zhengqing.demo.modules.weixin.enumeration.EnumRequestMessageType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p> 链接消息 </p>
 *
 * @author : zhengqing
 * @description :
 * @date : 2020/1/15 18:12
 */
@Data
@ApiModel(description = "链接消息")
public class LinkMessageDTO extends BaseMessageDTO {

	@ApiModelProperty(value = "消息类型", hidden = true)
	private String MsgType = EnumRequestMessageType.LINK.getType();

	@ApiModelProperty(value = "消息标题")
	private String Title;

	@ApiModelProperty(value = "消息描述")
	private String Description;

	@ApiModelProperty(value = "消息链接")
	private String Url;

}
