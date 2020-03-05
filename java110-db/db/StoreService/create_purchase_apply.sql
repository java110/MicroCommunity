/*
 Navicat Premium Data Transfer

 Source Server         : dev.db.java110.com_3306
 Source Server Type    : MySQL
 Source Server Version : 50645
 Source Host           : dev.db.java110.com:3306
 Source Schema         : tt

 Target Server Type    : MySQL
 Target Server Version : 50645
 File Encoding         : 65001

 Date: 05/03/2020 16:56:24
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for business_purchase_apply
-- ----------------------------
DROP TABLE IF EXISTS `business_purchase_apply`;
CREATE TABLE `business_purchase_apply`  (
  `apply_order_id` varchar(30) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '订单号',
  `b_id` varchar(30) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '业务Id',
  `store_id` varchar(30) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '商户ID',
  `user_id` varchar(30) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '使用人ID',
  `entry_person` varchar(30) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '录入人ID',
  `description` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '申请说明',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `res_order_type` varchar(8) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '出库类型 10000 入库 20000 出库 在t_dict表查看',
  `state` varchar(12) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '申请状态',
  `status_cd` varchar(2) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考t_dict表，0, 在用 1失效',
  `operate` varchar(4) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '数据状态，添加ADD，修改MOD 删除DEL'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for business_purchase_apply_detail
-- ----------------------------
DROP TABLE IF EXISTS `business_purchase_apply_detail`;
CREATE TABLE `business_purchase_apply_detail`  (
  `apply_order_id` varchar(30) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '订单号',
  `b_id` varchar(30) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '业务ID',
  `res_id` varchar(30) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '资源ID',
  `quantity` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '数量',
  `remark` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '备注',
  `operate` varchar(4) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '数据状态，添加ADD，修改MOD 删除DEL',
  INDEX `idx_apply_detail_id`(`apply_order_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for purchase_apply
-- ----------------------------
DROP TABLE IF EXISTS `purchase_apply`;
CREATE TABLE `purchase_apply`  (
  `apply_order_id` varchar(30) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '订单号',
  `b_id` varchar(30) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '业务Id',
  `store_id` varchar(30) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '商户ID',
  `user_id` varchar(30) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '使用人ID',
  `entry_person` varchar(30) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '录入人ID',
  `description` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '申请说明',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `res_order_type` varchar(8) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '出库类型 10000 入库 20000 出库 在t_dict表查看',
  `state` varchar(12) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '申请状态',
  `status_cd` varchar(2) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考t_dict表，0, 在用 1失效',
  INDEX `idx_apply_id`(`apply_order_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for purchase_apply_detail
-- ----------------------------
DROP TABLE IF EXISTS `purchase_apply_detail`;
CREATE TABLE `purchase_apply_detail`  (
  `apply_order_id` varchar(30) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '订单号',
  `b_id` varchar(30) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '业务id',
  `res_id` varchar(30) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '资源ID',
  `quantity` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '数量',
  `remark` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '备注',
  `operate` varchar(4) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '数据状态，添加ADD，修改MOD 删除DEL',
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_apply_detail_id`(`apply_order_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Compact;

SET FOREIGN_KEY_CHECKS = 1;
