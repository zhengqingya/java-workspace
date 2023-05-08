package com.zhengqing.demo.model.vo;

import com.zhengqing.demo.enums.ApiResultCodeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * <p>
 * API返回参数
 * </p>
 *
 * @author zhengqingya
 * @description
 * @date 2019/7/20 11:09
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "API返回参数")
public class ApiResult<T> {

    @ApiModelProperty(value = "响应码", required = true)
    private Integer code;

    @ApiModelProperty(value = "消息内容", required = false)
    private String msg;

    @ApiModelProperty(value = "响应数据", required = false)
    private T data;


    public static ApiResult ok() {
        return new ApiResult(ApiResultCodeEnum.SUCCESS.getCode(), "OK", null);
    }

    public static <E> ApiResult<E> ok(E o) {
        // 支持Controller层直接返回ApiResult
        ApiResult result = new ApiResult();
        result.setCode(ApiResultCodeEnum.SUCCESS.getCode());
        result.setMsg("OK");
        if (o instanceof ApiResult) {
            result = ((ApiResult) o);
        } else {
            // 其他obj封装进data,保持返回格式统一
            result.setData(o);
        }
        return result;
    }


    public static ApiResult fail(String msg) {
        return new ApiResult(ApiResultCodeEnum.FAIL.getCode(), msg, null);
    }

    /**
     * 自定义错误返回码
     *
     * @param code 验证码
     * @param msg  消息内容
     * @return 响应体
     */
    public static ApiResult fail(Integer code, String msg) {
        return new ApiResult(code, msg, null);
    }


}
