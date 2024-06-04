package com.zhengqing.demo.model.bo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p> 用户 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2024/6/4 20:51
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private Long id;
    private String name;
    private Integer age;
    private String sex;

}
