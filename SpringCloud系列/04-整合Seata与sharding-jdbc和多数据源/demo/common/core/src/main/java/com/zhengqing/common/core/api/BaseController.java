package com.zhengqing.common.core.api;

import com.zhengqing.common.base.context.SysUserContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * Controller基类
 * </p>
 *
 * @author zhengqingya
 * @description
 * @date 2019/8/17 0017 19:53
 */
@Slf4j
@RestController
//@Api(tags = "base接口")
public class BaseController {

    /**
     * 获取当前登录人ID
     *
     * @return 当前登录人ID
     * @author zhengqingya
     * @date 2020/8/30 15:41
     */
    protected Integer appGetCurrentUserId() {
        return SysUserContext.getUserId();
    }

}
