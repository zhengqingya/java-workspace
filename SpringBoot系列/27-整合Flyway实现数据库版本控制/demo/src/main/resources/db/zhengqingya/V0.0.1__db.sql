-- 创建表t_user
CREATE TABLE `t_user`
(
    `id`       int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `username` varchar(20) DEFAULT NULL COMMENT '用户名',
    `nickname` varchar(20) DEFAULT NULL COMMENT '昵称',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8 COMMENT='测试表';

-- 插入数据
insert into t_user
values (1, 'admin', '管理员'),
       (2, 'test', '测试员');
