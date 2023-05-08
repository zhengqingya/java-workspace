package com.zhengqing.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table("t_user")
public class User {
    @Column
    private long id;

    @Column
    private String username;

    @Column
    private String nickname;
}