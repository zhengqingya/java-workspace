DROP DATABASE IF EXISTS `demo`;
CREATE DATABASE `demo`;
USE `demo`;
DROP TABLE IF EXISTS `cron`;
CREATE TABLE `cron`  (
  `cron_id` varchar(30),
  `cron` varchar(30)
);
INSERT INTO `cron` VALUES ('1', '0/5 * * * * ?');
