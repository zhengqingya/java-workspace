package com.zhengqing.common.base.context;

import com.zhengqing.common.base.constant.BaseConstant;

import java.util.HashMap;
import java.util.Map;

/**
 * <p> B端系统用户上下文 </p>
 *
 * @author zhengqingya
 * @description 请务必在请求结束时, 调用 @Method remove()
 * @date 2020/8/1 19:07
 */
public class SysUserContext {

    public static final ThreadLocal<Map<String, Object>> THREAD_LOCAL = new ThreadLocal<>();

    public static Object get(String key) {
        Map<String, Object> map = THREAD_LOCAL.get();
        if (map == null) {
            return null;
        }
        return map.get(key);
    }

    public static Integer getUserId() {
        Object value = get(BaseConstant.CONTEXT_KEY_SYS_USER_ID);
        return value == null ? Integer.valueOf(BaseConstant.DEFAULT_CONTEXT_KEY_USER_ID) : (Integer) value;
    }

    public static String getUsername() {
        Object value = get(BaseConstant.CONTEXT_KEY_USERNAME);
        return value == null ? BaseConstant.DEFAULT_CONTEXT_KEY_USERNAME : (String) value;
    }

    public static void setUserId(Integer userId) {
        set(BaseConstant.CONTEXT_KEY_SYS_USER_ID, userId);
    }

    public static void setUsername(String username) {
        set(BaseConstant.CONTEXT_KEY_USERNAME, username);
    }

    private static void set(String key, Object value) {
        Map<String, Object> map = THREAD_LOCAL.get();
        if (map == null) {
            map = new HashMap<>(1);
            THREAD_LOCAL.set(map);
        }
        map.put(key, value);
    }

    public static void remove() {
        THREAD_LOCAL.remove();
    }

}
