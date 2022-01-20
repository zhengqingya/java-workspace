package com.zhengqing.demo.canal.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 测试实体类
 */
@Data
@Table(name = "t_user")
public class User implements Serializable {

    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer userId;

    /**
     * 用户名
     */
    @Column(name = "username")
    private String username;

    /**
     * 密码
     */
    @Column(name = "password")
    private String password;

    /**
     * 性别
     */
    @Column(name = "sex")
    private Integer sex;

    /**
     * 备注
     */
    private String remark;

    /**
     * 时间
     */
    private Date date;

}
