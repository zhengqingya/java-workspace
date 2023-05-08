/*
 Navicat Premium Data Transfer

 Source Server         : mysql_3306
 Source Server Type    : MySQL
 Source Server Version : 50726
 Source Host           : www.zhengqingya.com:3306
 Source Schema         : oauth2

 Target Server Type    : MySQL
 Target Server Version : 50726
 File Encoding         : 65001

 Date: 01/04/2022 21:32:23
*/


SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;


-- 建库
create database if not exists oauth2 default charset = utf8mb4;
use oauth2;


-- ----------------------------
-- Table structure for oauth_client_details
-- ----------------------------
DROP TABLE IF EXISTS `oauth_client_details`;
CREATE TABLE `oauth_client_details`  (
     `client_id` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '客户端ID，唯一标识',
     `client_secret` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '客户端访问秘钥，BCryptPasswordEncoder加密算法加密',
     `resource_ids` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '可访问资源id(英文逗号分隔)',
     `scope` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '授权范围(英文逗号分隔)',
     `authorized_grant_types` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '授权类型(英文逗号分隔)',
     `web_server_redirect_uri` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '重定向uri',
     `authorities` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '@PreAuthorize(\"hasAuthority(\'admin\')\")可以在方法上标志 用户或者说client 需要说明样的权限\r\n\n\n指定客户端所拥有的Spring Security的权限值\r\n(英文逗号分隔)',
     `access_token_validity` int(11) NOT NULL COMMENT '令牌有效期(单位:秒)',
     `refresh_token_validity` int(11) NOT NULL COMMENT '刷新令牌有效期(单位:秒)',
     `additional_information` varchar(4096) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '预留字段,在Oauth的流程中没有实际的使用(JSON格式数据)',
     `autoapprove` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '设置用户是否自动Approval操作, 默认值为 \'false\'\r\n可选值包括 \'true\',\'false\', \'read\',\'write\'.\r\n该字段只适用于grant_type=\"authorization_code\"的情况,当用户登录成功后,若该值为\'true\'或支持的scope值,则会跳过用户Approve的页面, 直接授权',
     `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
     `update_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
     PRIMARY KEY (`client_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of oauth_client_details
-- ----------------------------
INSERT INTO `oauth_client_details` VALUES ('zq_app_id', '$2a$10$kdh16TzZoILQmaC4VkKdq.Ah2/u4LBbNJ7lpTqC8NXZVXKiu2LnVa', 'res1', 'all', 'authorization_code,refresh_token', 'http://127.0.0.1:10020/index.html', NULL, 3600, 259200, NULL, NULL, '2022-04-02 09:31:10', '2022-04-02 09:34:22');

SET FOREIGN_KEY_CHECKS = 1;
