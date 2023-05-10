package com.zhengqing.common.base.model.bo;

import com.zhengqing.common.base.enums.AuthSourceEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * <p> jwt中自定义的用户信息 </p>
 *
 * @author zhengqingya
 * @description {@link com.zhengqing.auth.security.config.CustomAdditionalInformation}
 * @date 2022/6/15 10:34
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class JwtCustomUserBO {
    /**
     * {@link AuthSourceEnum}
     */
    @ApiModelProperty(value = "认证来源")
    private String authSource;

    @ApiModelProperty(value = "jwt")
    private String token;

    @ApiModelProperty(value = "jwt的唯一身份标识")
    private String jti;

    @ApiModelProperty(value = "过期时间(2022-06-01 23:06:53)")
    private String expireTime;

    @ApiModelProperty(value = "租户ID")
    private String tenantId;

    @ApiModelProperty(value = "B端系统用户ID")
    private String sysUserId;

    @ApiModelProperty(value = "C端用户ID")
    private String umsUserId;

    @ApiModelProperty(value = "用户名")
    private String username;
}
