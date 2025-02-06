package com.zhengqing.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * <p> 用户实体类 </p>
 *
 * @author zhengqingya
 * @description
 * @date 2024/4/20 19:07
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private Integer id;
    private String name;
    private Integer age;

}
