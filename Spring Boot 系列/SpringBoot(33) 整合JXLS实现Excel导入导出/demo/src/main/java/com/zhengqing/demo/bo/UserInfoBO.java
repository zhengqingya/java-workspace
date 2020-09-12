package com.zhengqing.demo.bo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 * 用户信息
 * </p>
 *
 * @author : zhengqing
 * @description :
 * @date : 2020/9/12 11:25
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoBO {

    private String id;

    private String name;

    private String age;

}
