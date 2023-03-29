package com.zhengqing.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

/**
 * <p> 用户表 </p>
 *
 * @author zhengqing
 * @description
 * @date 2019/10/10 11:52
 */
@Data
@TableName("t_user")
public class User extends Model<User> {

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
     * 账号
     */
    private String username;
    /**
     * 昵称
     */
//    private String nickname;

}
