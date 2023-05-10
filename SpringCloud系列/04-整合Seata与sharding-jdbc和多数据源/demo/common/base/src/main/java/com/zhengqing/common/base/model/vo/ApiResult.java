package com.zhengqing.common.base.model.vo;

import com.zhengqing.common.base.enums.ApiResultCodeEnum;
import com.zhengqing.common.base.exception.MyException;
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

    public ApiResult(T data) {
        this.code = ApiResultCodeEnum.SUCCESS.getCode();
        this.msg = "OK";
        this.data = data;
    }

    public ApiResult(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static ApiResult ok() {
        return new ApiResult(ApiResultCodeEnum.SUCCESS.getCode(), "OK", null);
    }

    public static <E> ApiResult<E> ok(E o) {
        // 支持Controller层直接返回ApiResult
        ApiResult result = new ApiResult(ApiResultCodeEnum.SUCCESS);
        if (o instanceof ApiResult) {
            result = ((ApiResult) o);
        } else {
            // 其他obj封装进data,保持返回格式统一
            result.setData(o);
        }
        return result;
    }

    public static ApiResult ok(String data) {
        return new ApiResult(ApiResultCodeEnum.SUCCESS.getCode(), "OK", data);
    }

    public static ApiResult ok(Object data, String msg) {
        return new ApiResult(ApiResultCodeEnum.SUCCESS.getCode(), msg, data);
    }

    /**
     * 自定义返回码
     */
    public static ApiResult ok(Integer code, String msg) {
        return new ApiResult(code, msg);
    }

    /**
     * 自定义
     *
     * @param code 验证码
     * @param msg  返回消息内容
     * @param data 返回数据
     * @return 响应体
     */
    public static ApiResult ok(Integer code, String msg, Object data) {
        return new ApiResult(code, msg, data);
    }

    /**
     * 过期
     *
     * @param msg 消息内容
     * @return 响应体
     */
    public static ApiResult expired(String msg) {
        return new ApiResult(ApiResultCodeEnum.TOKEN_EXPIRED.getCode(), msg, null);
    }

    public static ApiResult fail(String msg) {
        return new ApiResult(ApiResultCodeEnum.FAILURE.getCode(), msg, null);
    }

    public static ApiResult busy() {
        return new ApiResult(ApiResultCodeEnum.FAILURE.getCode(), "服务繁忙", null);
    }

    /***
     * 自定义错误返回码
     *
     * @param code 验证码
     * @param msg 消息内容
     * @return 响应体
     */
    public static ApiResult fail(Integer code, String msg) {
        return new ApiResult(code, msg, null);
    }

    /**
     * 是否成功
     *
     * @return true->成功，false->失败
     * @author zhengqingya
     * @date 2022/6/1 12:45
     */
    public boolean checkIsSuccess() {
        return ApiResultCodeEnum.SUCCESS.getCode() == this.code;
    }

    /**
     * 是否失败
     *
     * @return true->失败，false->成功
     * @author zhengqingya
     * @date 2022/6/1 12:45
     */
    public boolean checkIsFail() {
        return ApiResultCodeEnum.SUCCESS.getCode() != this.code;
    }

    /**
     * rpc校验结果是否异常
     *
     * @return void
     * @author zhengqingya
     * @date 2021/10/14 16:21
     */
    public void checkForRpc() {
        if (this.checkIsFail()) {
            throw new MyException(this.msg);
        }
    }

}
