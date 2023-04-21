package com.zhengqing.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fhs.core.trans.anno.Trans;
import com.fhs.core.trans.constant.TransType;
import com.fhs.core.trans.vo.TransPojo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

/**
 * <p> 用户表 </p>
 *
 * @author zhengqing
 * @description
 * @date 2019/10/10 11:52
 */
@Data
@TableName("t_user")
public class User extends Model<User> implements TransPojo {

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
    private String nickname;

    @Trans(type = TransType.DICTIONARY, key = "sex")
    private String sex;

    @Trans(type = TransType.ENUM, key = "desc") // 理想的数据是映射value，但这个框架好像只能根据枚举属性做处理...
    private SexEnum sex2;

    @Getter
    @AllArgsConstructor
    public static enum SexEnum {
        男(0, "男"),
        女(1, "女");

        private Integer value;
        private String desc;
    }

}
