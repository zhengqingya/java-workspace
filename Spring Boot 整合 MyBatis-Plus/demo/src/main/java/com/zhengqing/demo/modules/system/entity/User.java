package com.zhengqing.demo.modules.system.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;

/**
 *  <p> 系统管理-用户基础信息表 </p>
 *
 * @description :
 * @author : zhengqing
 * @date : 2019/10/10 11:52
 */
@Data
@TableName("t_sys_user")
public class User extends Model<User> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 账号
     */
	@TableField("username")
	private String username;
    /**
     * 登录密码
     */
	@TableField("password")
	private String password;
    /**
     * 昵称
     */
	@TableField("nick_name")
	private String nickName;

	@Override
	protected Serializable pkVal() {
		return this.id;
	}
}
