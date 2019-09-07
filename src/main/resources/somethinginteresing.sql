/*
 Navicat Premium Data Transfer

 Source Server         : local
 Source Server Type    : MySQL
 Source Server Version : 50559
 Source Host           : localhost:3306
 Source Schema         : somethinginteresing

 Target Server Type    : MySQL
 Target Server Version : 50559
 File Encoding         : 65001

 Date: 07/09/2019 21:31:53
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for zj_business_customer
-- ----------------------------
DROP TABLE IF EXISTS `zj_business_customer`;
CREATE TABLE `zj_business_customer`  (
  `id` int(50) NOT NULL COMMENT '业务唯一标识',
  `customer_id` int(50) NULL DEFAULT NULL COMMENT '客户的id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of zj_business_customer
-- ----------------------------
INSERT INTO `zj_business_customer` VALUES (1, 1);

-- ----------------------------
-- Table structure for zj_customer_info
-- ----------------------------
DROP TABLE IF EXISTS `zj_customer_info`;
CREATE TABLE `zj_customer_info`  (
  `id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '客户ID',
  `customer_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '客户名',
  `customer_phone` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '客户电话号码',
  `customer_idcardnumber` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '客户身份证',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of zj_customer_info
-- ----------------------------
INSERT INTO `zj_customer_info` VALUES ('1', '古全星', '15898999180', '371121198711160413');

-- ----------------------------
-- Table structure for zj_interface_invoke_param
-- ----------------------------
DROP TABLE IF EXISTS `zj_interface_invoke_param`;
CREATE TABLE `zj_interface_invoke_param`  (
  `id` int(50) NOT NULL COMMENT '参数的唯一标识',
  `param_field_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '参数的字段名',
  `data_value` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '具体的值',
  `data_ref` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '表中的字段',
  `data_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '唯一标识',
  `param_type` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据最终需要的 form json head',
  `current_data_type` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '当前表中存放的数据类型 ',
  `need_data_type` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '需要的数据类型',
  `class_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据的类全名'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of zj_interface_invoke_param
-- ----------------------------
INSERT INTO `zj_interface_invoke_param` VALUES (0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'org.zj.interfaceinvoke.business.test.bean.RequestHlzApply');
INSERT INTO `zj_interface_invoke_param` VALUES (1, 'reqBodyBean.baseUser.idCard', '', 'zj_invoke_hlz_apply.customer_idcardnumber', 'business_id', 'json', 'string', 'string', 'org.zj.interfaceinvoke.business.test.bean.RequestHlzApply');
INSERT INTO `zj_interface_invoke_param` VALUES (1, 'reqBodyBean.baseUser.idName', NULL, 'zj_invoke_hlz_apply.customer_name', 'business_id', 'json', 'string', 'string', 'org.zj.interfaceinvoke.business.test.bean.RequestHlzApply');
INSERT INTO `zj_interface_invoke_param` VALUES (1, 'reqBodyBean.baseUser.phone', NULL, 'zj_invoke_hlz_apply.customer_phone', 'business_id', 'json', 'string', 'string', 'org.zj.interfaceinvoke.business.test.bean.RequestHlzApply');
INSERT INTO `zj_interface_invoke_param` VALUES (1, 'version', '1.0.0', NULL, '', 'json', 'string', 'string', 'org.zj.interfaceinvoke.business.test.bean.RequestHlzApply');
INSERT INTO `zj_interface_invoke_param` VALUES (1, 'reqBodyBean.memberId', 'xxxxx', NULL, NULL, 'json', 'string', 'string', 'org.zj.interfaceinvoke.business.test.bean.RequestHlzApply');
INSERT INTO `zj_interface_invoke_param` VALUES (1, 'reqBodyBean.workId', 'xxxxx', NULL, NULL, 'json', 'string', 'string', 'org.zj.interfaceinvoke.business.test.bean.RequestHlzApply');

-- ----------------------------
-- Table structure for zj_interface_invoke_task
-- ----------------------------
DROP TABLE IF EXISTS `zj_interface_invoke_task`;
CREATE TABLE `zj_interface_invoke_task`  (
  `id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '接口调用的唯一标识',
  `invoke_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT ' 要调用的接口的url',
  `interface_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '接口名',
  `invoke_method` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '调用方法 get post',
  `param_handler_ref` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '参数处理的类全名.方法名',
  `response_handler_ref` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '接口响应结果的处理类全名',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of zj_interface_invoke_task
-- ----------------------------
INSERT INTO `zj_interface_invoke_task` VALUES ('1', 'xxx', 'xxx', 'post', 'org.zj.interfaceinvoke.base.operate.TestParamHandler', NULL);

-- ----------------------------
-- View structure for zj_invoke_hlz_apply
-- ----------------------------
DROP VIEW IF EXISTS `zj_invoke_hlz_apply`;
CREATE ALGORITHM = UNDEFINED SQL SECURITY DEFINER VIEW `zj_invoke_hlz_apply` AS select `zbc`.`id` AS `business_id`,`zci`.`id` AS `customer_id`,`zci`.`customer_name` AS `customer_name`,`zci`.`customer_phone` AS `customer_phone`,`zci`.`customer_idcardnumber` AS `customer_idcardnumber` from (`zj_business_customer` `zbc` left join `zj_customer_info` `zci` on((`zbc`.`customer_id` = `zci`.`id`)));

SET FOREIGN_KEY_CHECKS = 1;
