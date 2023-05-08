package com.zhengqing.demo.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * <p> sha1加密工具类 </p>
 *
 * @description:
 * @author: zhengqing
 * @date: 2019/6/10 14:17
 */
public class SecurityUtil {

    public static String sha1(String str) {
        try {
            StringBuilder sb = new StringBuilder();
            MessageDigest digest = MessageDigest.getInstance("sha1");
            // 放入加密字符串
            digest.update(str.getBytes());
            // 进行加密
            byte[] digestMsg = digest.digest();
            // byte转换16进制
            for (byte b : digestMsg) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return str;
    }

}
