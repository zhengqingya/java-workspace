package com.zhengqing.mybatis.entity;


import lombok.Data;

/**
 * <p> 用户实体类 </p>
 *
 * @author : zhengqing
 * @description :
 * @date : 2020/4/29 14:00
 */
@Data
public class User {

  private Integer id;
  private String username;
  private String password;

}
