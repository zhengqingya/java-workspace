/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 50724
 Source Host           : localhost:3306
 Source Schema         : demo

 Target Server Type    : MySQL
 Target Server Version : 50724
 File Encoding         : 65001

 Date: 14/10/2019 12:52:48
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_sys_user
-- ----------------------------
DROP TABLE IF EXISTS `t_sys_user`;
CREATE TABLE `t_sys_user`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `username` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '账号',
  `password` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '登录密码',
  `nick_name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '昵称',
  `salt` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '盐值',
  `token` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT 'token',
  `gmt_create` datetime(0) DEFAULT NULL COMMENT '创建时间',
  `gmt_modified` datetime(0) DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 22 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '系统管理-用户基础信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_sys_user
-- ----------------------------
INSERT INTO `t_sys_user` VALUES (1, 'admin', '97ba1ef7f148b2aec1c61303a7d88d0967825495', '郑清', 'zhengqing', 'dca764e5f803aa64e3fd7bf1322bdbbcac5f18ae', '2019-05-05 16:09:06', '2019-09-19 00:59:47');
INSERT INTO `t_sys_user` VALUES (2, 'test', '97ba1ef7f148b2aec1c61303a7d88d0967825495', '测试号', 'zhengqing', '1fe7aa7c2e7a37803f787dbc94c9297d4a812d73', '2019-05-05 16:15:06', '2019-09-19 01:47:19');
INSERT INTO `t_sys_user` VALUES (3, '10086', '97ba1ef7f148b2aec1c61303a7d88d0967825495', '中国移动', 'zhengqing', '', '2019-09-05 12:55:48', '2019-09-18 23:28:23');

SET FOREIGN_KEY_CHECKS = 1;
