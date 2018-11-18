CREATE USER 'TT'@'%' IDENTIFIED BY 'TT@12345678';

GRANT ALL ON *.* TO 'TT'@'%';

-- create database `TT` default character set utf8 collate utf8_general_ci;
/*
SQLyog Professional v12.09 (64 bit)
MySQL - 5.6.42 : Database - TT
*********************************************************************
*/


/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`TT` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `TT`;

/*Table structure for table `business_shop` */

DROP TABLE IF EXISTS `business_shop`;

CREATE TABLE `business_shop` (
  `shop_id` varchar(30) NOT NULL COMMENT '商品ID',
  `b_id` varchar(30) NOT NULL COMMENT '业务Id',
  `catalog_id` varchar(30) NOT NULL COMMENT '目录ID',
  `store_id` varchar(30) NOT NULL COMMENT '商店ID',
  `name` varchar(100) NOT NULL COMMENT '商品名称',
  `hot_buy` varchar(2) NOT NULL DEFAULT 'N' COMMENT '是否热卖 Y是 N否',
  `sale_price` decimal(10,2) NOT NULL COMMENT '商品销售价,再没有打折情况下显示',
  `open_shop_count` varchar(2) NOT NULL DEFAULT 'N' COMMENT '是否开启库存管理，默认N Y开启，N关闭，开启后界面显示数量，如果为0 则下架',
  `shop_count` decimal(10,0) NOT NULL DEFAULT '10000' COMMENT '商品库存',
  `start_date` date NOT NULL COMMENT '商品开始时间',
  `end_date` date NOT NULL COMMENT '商品结束时间',
  `month` int(11) NOT NULL COMMENT '月份',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `operate` varchar(3) NOT NULL COMMENT '数据状态，添加ADD，修改MOD 删除DEL',
  KEY `idx_business_shop_shop_id` (`shop_id`),
  KEY `idx_business_shop_b_id` (`b_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8
/*!50100 PARTITION BY RANGE (`month`)
(PARTITION business_shop_1 VALUES LESS THAN (2) ENGINE = InnoDB,
 PARTITION business_shop_2 VALUES LESS THAN (3) ENGINE = InnoDB,
 PARTITION business_shop_3 VALUES LESS THAN (4) ENGINE = InnoDB,
 PARTITION business_shop_4 VALUES LESS THAN (5) ENGINE = InnoDB,
 PARTITION business_shop_5 VALUES LESS THAN (6) ENGINE = InnoDB,
 PARTITION business_shop_6 VALUES LESS THAN (7) ENGINE = InnoDB,
 PARTITION business_shop_7 VALUES LESS THAN (8) ENGINE = InnoDB,
 PARTITION business_shop_8 VALUES LESS THAN (9) ENGINE = InnoDB,
 PARTITION business_shop_9 VALUES LESS THAN (10) ENGINE = InnoDB,
 PARTITION business_shop_10 VALUES LESS THAN (11) ENGINE = InnoDB,
 PARTITION business_shop_11 VALUES LESS THAN (12) ENGINE = InnoDB,
 PARTITION business_shop_12 VALUES LESS THAN (13) ENGINE = InnoDB) */;

/*Data for the table `business_shop` */

/*Table structure for table `business_shop_attr` */

DROP TABLE IF EXISTS `business_shop_attr`;

CREATE TABLE `business_shop_attr` (
  `b_id` varchar(30) NOT NULL COMMENT '订单ID',
  `attr_id` varchar(30) NOT NULL COMMENT '属性id',
  `shop_id` varchar(30) NOT NULL COMMENT '商品ID',
  `spec_cd` varchar(12) NOT NULL COMMENT '规格id,参考spec表',
  `value` varchar(50) NOT NULL COMMENT '属性值',
  `month` int(11) NOT NULL COMMENT '月份',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `operate` varchar(3) NOT NULL COMMENT '数据状态，添加ADD，修改MOD 删除DEL',
  KEY `idx_business_shop_attr_shop_id` (`shop_id`),
  KEY `idx_business_shop_attr_b_id` (`b_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8
/*!50100 PARTITION BY RANGE (`month`)
(PARTITION business_shop_attr_1 VALUES LESS THAN (2) ENGINE = InnoDB,
 PARTITION business_shop_attr_2 VALUES LESS THAN (3) ENGINE = InnoDB,
 PARTITION business_shop_attr_3 VALUES LESS THAN (4) ENGINE = InnoDB,
 PARTITION business_shop_attr_4 VALUES LESS THAN (5) ENGINE = InnoDB,
 PARTITION business_shop_attr_5 VALUES LESS THAN (6) ENGINE = InnoDB,
 PARTITION business_shop_attr_6 VALUES LESS THAN (7) ENGINE = InnoDB,
 PARTITION business_shop_attr_7 VALUES LESS THAN (8) ENGINE = InnoDB,
 PARTITION business_shop_attr_8 VALUES LESS THAN (9) ENGINE = InnoDB,
 PARTITION business_shop_attr_9 VALUES LESS THAN (10) ENGINE = InnoDB,
 PARTITION business_shop_attr_10 VALUES LESS THAN (11) ENGINE = InnoDB,
 PARTITION business_shop_attr_11 VALUES LESS THAN (12) ENGINE = InnoDB,
 PARTITION business_shop_attr_12 VALUES LESS THAN (13) ENGINE = InnoDB) */;

/*Data for the table `business_shop_attr` */

/*Table structure for table `business_shop_attr_param` */

DROP TABLE IF EXISTS `business_shop_attr_param`;

CREATE TABLE `business_shop_attr_param` (
  `attr_param_id` varchar(30) NOT NULL COMMENT '商品属性参数ID',
  `b_id` varchar(30) NOT NULL COMMENT '订单ID',
  `shop_id` varchar(30) NOT NULL COMMENT '商品ID',
  `spec_cd` varchar(12) NOT NULL COMMENT '规格id,参考spec表',
  `param` varchar(50) NOT NULL COMMENT '参数值',
  `month` int(11) NOT NULL COMMENT '月份',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `operate` varchar(3) NOT NULL COMMENT '数据状态，添加ADD，修改MOD 删除DEL',
  KEY `idx_business_shop_attr_param_shop_id` (`shop_id`),
  KEY `idx_business_shop_attr_param_b_id` (`b_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8
/*!50100 PARTITION BY RANGE (`month`)
(PARTITION business_shop_attr_param_1 VALUES LESS THAN (2) ENGINE = InnoDB,
 PARTITION business_shop_attr_param_2 VALUES LESS THAN (3) ENGINE = InnoDB,
 PARTITION business_shop_attr_param_3 VALUES LESS THAN (4) ENGINE = InnoDB,
 PARTITION business_shop_attr_param_4 VALUES LESS THAN (5) ENGINE = InnoDB,
 PARTITION business_shop_attr_param_5 VALUES LESS THAN (6) ENGINE = InnoDB,
 PARTITION business_shop_attr_param_6 VALUES LESS THAN (7) ENGINE = InnoDB,
 PARTITION business_shop_attr_param_7 VALUES LESS THAN (8) ENGINE = InnoDB,
 PARTITION business_shop_attr_param_8 VALUES LESS THAN (9) ENGINE = InnoDB,
 PARTITION business_shop_attr_param_9 VALUES LESS THAN (10) ENGINE = InnoDB,
 PARTITION business_shop_attr_param_10 VALUES LESS THAN (11) ENGINE = InnoDB,
 PARTITION business_shop_attr_param_11 VALUES LESS THAN (12) ENGINE = InnoDB,
 PARTITION business_shop_attr_param_12 VALUES LESS THAN (13) ENGINE = InnoDB) */;

/*Data for the table `business_shop_attr_param` */

/*Table structure for table `business_shop_catalog` */

DROP TABLE IF EXISTS `business_shop_catalog`;

CREATE TABLE `business_shop_catalog` (
  `catalog_id` varchar(30) NOT NULL COMMENT '目录ID',
  `b_id` varchar(30) NOT NULL COMMENT '业务Id',
  `store_id` varchar(30) NOT NULL COMMENT '商店ID',
  `name` varchar(100) NOT NULL COMMENT '目录名称',
  `level` varchar(2) NOT NULL COMMENT '目录等级',
  `parent_catalog_id` varchar(30) NOT NULL DEFAULT '-1' COMMENT '父目录ID，一级目录则写-1',
  `month` int(11) NOT NULL COMMENT '月份',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `operate` varchar(3) NOT NULL COMMENT '数据状态，添加ADD，修改MOD 删除DEL',
  KEY `idx_business_shop_catalog_store_id` (`store_id`),
  KEY `idx_business_shop_catalog_b_id` (`b_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8
/*!50100 PARTITION BY RANGE (`month`)
(PARTITION business_shop_catalog_1 VALUES LESS THAN (2) ENGINE = InnoDB,
 PARTITION business_shop_catalog_2 VALUES LESS THAN (3) ENGINE = InnoDB,
 PARTITION business_shop_catalog_3 VALUES LESS THAN (4) ENGINE = InnoDB,
 PARTITION business_shop_catalog_4 VALUES LESS THAN (5) ENGINE = InnoDB,
 PARTITION business_shop_catalog_5 VALUES LESS THAN (6) ENGINE = InnoDB,
 PARTITION business_shop_catalog_6 VALUES LESS THAN (7) ENGINE = InnoDB,
 PARTITION business_shop_catalog_7 VALUES LESS THAN (8) ENGINE = InnoDB,
 PARTITION business_shop_catalog_8 VALUES LESS THAN (9) ENGINE = InnoDB,
 PARTITION business_shop_catalog_9 VALUES LESS THAN (10) ENGINE = InnoDB,
 PARTITION business_shop_catalog_10 VALUES LESS THAN (11) ENGINE = InnoDB,
 PARTITION business_shop_catalog_11 VALUES LESS THAN (12) ENGINE = InnoDB,
 PARTITION business_shop_catalog_12 VALUES LESS THAN (13) ENGINE = InnoDB) */;

/*Data for the table `business_shop_catalog` */

/*Table structure for table `business_shop_desc` */

DROP TABLE IF EXISTS `business_shop_desc`;

CREATE TABLE `business_shop_desc` (
  `shop_desc_id` varchar(30) NOT NULL COMMENT '商品描述ID',
  `shop_id` varchar(30) NOT NULL COMMENT '商品ID',
  `b_id` varchar(30) NOT NULL COMMENT '业务Id',
  `shop_describe` longtext NOT NULL COMMENT '商品描述',
  `month` int(11) NOT NULL COMMENT '月份',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `operate` varchar(3) NOT NULL COMMENT '数据状态，添加ADD，修改MOD 删除DEL',
  KEY `idx_business_shop_desc_shop_id` (`shop_id`),
  KEY `idx_business_shop_desc_b_id` (`b_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8
/*!50100 PARTITION BY RANGE (`month`)
(PARTITION business_shop_desc_1 VALUES LESS THAN (2) ENGINE = InnoDB,
 PARTITION business_shop_desc_2 VALUES LESS THAN (3) ENGINE = InnoDB,
 PARTITION business_shop_desc_3 VALUES LESS THAN (4) ENGINE = InnoDB,
 PARTITION business_shop_desc_4 VALUES LESS THAN (5) ENGINE = InnoDB,
 PARTITION business_shop_desc_5 VALUES LESS THAN (6) ENGINE = InnoDB,
 PARTITION business_shop_desc_6 VALUES LESS THAN (7) ENGINE = InnoDB,
 PARTITION business_shop_desc_7 VALUES LESS THAN (8) ENGINE = InnoDB,
 PARTITION business_shop_desc_8 VALUES LESS THAN (9) ENGINE = InnoDB,
 PARTITION business_shop_desc_9 VALUES LESS THAN (10) ENGINE = InnoDB,
 PARTITION business_shop_desc_10 VALUES LESS THAN (11) ENGINE = InnoDB,
 PARTITION business_shop_desc_11 VALUES LESS THAN (12) ENGINE = InnoDB,
 PARTITION business_shop_desc_12 VALUES LESS THAN (13) ENGINE = InnoDB) */;

/*Data for the table `business_shop_desc` */

/*Table structure for table `business_shop_photo` */

DROP TABLE IF EXISTS `business_shop_photo`;

CREATE TABLE `business_shop_photo` (
  `shop_photo_id` varchar(30) NOT NULL COMMENT '商品照片ID',
  `b_id` varchar(30) NOT NULL COMMENT '业务Id',
  `shop_id` varchar(30) NOT NULL COMMENT '商店ID',
  `shop_photo_type_cd` varchar(12) NOT NULL COMMENT '商品照片类型,L logo O 其他照片',
  `photo` varchar(100) NOT NULL COMMENT '照片',
  `month` int(11) NOT NULL COMMENT '月份',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `operate` varchar(3) NOT NULL COMMENT '数据状态，添加ADD，修改MOD 删除DEL',
  KEY `idx_business_shop_photo_shop_id` (`shop_id`),
  KEY `idx_business_shop_photo_b_id` (`b_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8
/*!50100 PARTITION BY RANGE (`month`)
(PARTITION business_shop_photo_1 VALUES LESS THAN (2) ENGINE = InnoDB,
 PARTITION business_shop_photo_2 VALUES LESS THAN (3) ENGINE = InnoDB,
 PARTITION business_shop_photo_3 VALUES LESS THAN (4) ENGINE = InnoDB,
 PARTITION business_shop_photo_4 VALUES LESS THAN (5) ENGINE = InnoDB,
 PARTITION business_shop_photo_5 VALUES LESS THAN (6) ENGINE = InnoDB,
 PARTITION business_shop_photo_6 VALUES LESS THAN (7) ENGINE = InnoDB,
 PARTITION business_shop_photo_7 VALUES LESS THAN (8) ENGINE = InnoDB,
 PARTITION business_shop_photo_8 VALUES LESS THAN (9) ENGINE = InnoDB,
 PARTITION business_shop_photo_9 VALUES LESS THAN (10) ENGINE = InnoDB,
 PARTITION business_shop_photo_10 VALUES LESS THAN (11) ENGINE = InnoDB,
 PARTITION business_shop_photo_11 VALUES LESS THAN (12) ENGINE = InnoDB,
 PARTITION business_shop_photo_12 VALUES LESS THAN (13) ENGINE = InnoDB) */;

/*Data for the table `business_shop_photo` */

/*Table structure for table `business_shop_preferential` */

DROP TABLE IF EXISTS `business_shop_preferential`;

CREATE TABLE `business_shop_preferential` (
  `shop_preferential_id` varchar(30) NOT NULL COMMENT '商品ID',
  `shop_id` varchar(30) NOT NULL COMMENT '商品ID',
  `b_id` varchar(30) NOT NULL COMMENT '业务Id',
  `original_price` decimal(10,2) NOT NULL COMMENT '商品销售价，再没有优惠的情况下和售价是一致的',
  `discount_rate` decimal(3,2) NOT NULL DEFAULT '1.00' COMMENT '商品打折率',
  `show_original_price` varchar(2) NOT NULL DEFAULT 'N' COMMENT '是否显示原价，Y显示，N 不显示',
  `preferential_start_date` date NOT NULL COMMENT '商品优惠开始时间',
  `preferential_end_date` date NOT NULL COMMENT '商品优惠结束时间',
  `month` int(11) NOT NULL COMMENT '月份',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `operate` varchar(3) NOT NULL COMMENT '数据状态，添加ADD，修改MOD 删除DEL',
  KEY `idx_business_shop_preferential_shop_id` (`shop_id`),
  KEY `idx_business_shop_preferential_b_id` (`b_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8
/*!50100 PARTITION BY RANGE (`month`)
(PARTITION business_shop_preferential_1 VALUES LESS THAN (2) ENGINE = InnoDB,
 PARTITION business_shop_preferential_2 VALUES LESS THAN (3) ENGINE = InnoDB,
 PARTITION business_shop_preferential_3 VALUES LESS THAN (4) ENGINE = InnoDB,
 PARTITION business_shop_preferential_4 VALUES LESS THAN (5) ENGINE = InnoDB,
 PARTITION business_shop_preferential_5 VALUES LESS THAN (6) ENGINE = InnoDB,
 PARTITION business_shop_preferential_6 VALUES LESS THAN (7) ENGINE = InnoDB,
 PARTITION business_shop_preferential_7 VALUES LESS THAN (8) ENGINE = InnoDB,
 PARTITION business_shop_preferential_8 VALUES LESS THAN (9) ENGINE = InnoDB,
 PARTITION business_shop_preferential_9 VALUES LESS THAN (10) ENGINE = InnoDB,
 PARTITION business_shop_preferential_10 VALUES LESS THAN (11) ENGINE = InnoDB,
 PARTITION business_shop_preferential_11 VALUES LESS THAN (12) ENGINE = InnoDB,
 PARTITION business_shop_preferential_12 VALUES LESS THAN (13) ENGINE = InnoDB) */;

/*Data for the table `business_shop_preferential` */

/*Table structure for table `business_store` */

DROP TABLE IF EXISTS `business_store`;

CREATE TABLE `business_store` (
  `store_id` varchar(30) NOT NULL COMMENT '商店ID',
  `b_id` varchar(30) NOT NULL COMMENT '业务Id',
  `user_id` varchar(30) NOT NULL COMMENT '用户ID',
  `name` varchar(100) NOT NULL COMMENT '店铺名称',
  `address` varchar(200) NOT NULL COMMENT '店铺地址',
  `tel` varchar(11) NOT NULL COMMENT '电话',
  `store_type_cd` varchar(10) NOT NULL COMMENT '店铺种类，对应表 store_type',
  `nearby_landmarks` varchar(200) DEFAULT NULL COMMENT '地标，如王府井北60米',
  `map_x` varchar(20) NOT NULL COMMENT '地区 x坐标',
  `map_y` varchar(20) NOT NULL COMMENT '地区 y坐标',
  `month` int(11) NOT NULL COMMENT '月份',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `operate` varchar(3) NOT NULL COMMENT '数据状态，添加ADD，修改MOD 删除DEL',
  KEY `idx_business_store_store_id` (`store_id`),
  KEY `idx_business_store_b_id` (`b_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8
/*!50100 PARTITION BY RANGE (`month`)
(PARTITION business_store_1 VALUES LESS THAN (2) ENGINE = InnoDB,
 PARTITION business_store_2 VALUES LESS THAN (3) ENGINE = InnoDB,
 PARTITION business_store_3 VALUES LESS THAN (4) ENGINE = InnoDB,
 PARTITION business_store_4 VALUES LESS THAN (5) ENGINE = InnoDB,
 PARTITION business_store_5 VALUES LESS THAN (6) ENGINE = InnoDB,
 PARTITION business_store_6 VALUES LESS THAN (7) ENGINE = InnoDB,
 PARTITION business_store_7 VALUES LESS THAN (8) ENGINE = InnoDB,
 PARTITION business_store_8 VALUES LESS THAN (9) ENGINE = InnoDB,
 PARTITION business_store_9 VALUES LESS THAN (10) ENGINE = InnoDB,
 PARTITION business_store_10 VALUES LESS THAN (11) ENGINE = InnoDB,
 PARTITION business_store_11 VALUES LESS THAN (12) ENGINE = InnoDB,
 PARTITION business_store_12 VALUES LESS THAN (13) ENGINE = InnoDB) */;

/*Data for the table `business_store` */

/*Table structure for table `business_store_attr` */

DROP TABLE IF EXISTS `business_store_attr`;

CREATE TABLE `business_store_attr` (
  `b_id` varchar(30) NOT NULL COMMENT '订单ID',
  `attr_id` varchar(30) NOT NULL COMMENT '属性id',
  `store_id` varchar(30) NOT NULL COMMENT '用户ID',
  `spec_cd` varchar(12) NOT NULL COMMENT '规格id,参考spec表',
  `value` varchar(50) NOT NULL COMMENT '属性值',
  `month` int(11) NOT NULL COMMENT '月份',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `operate` varchar(3) NOT NULL COMMENT '数据状态，添加ADD，修改MOD 删除DEL',
  KEY `idx_business_store_attr_store_id` (`store_id`),
  KEY `idx_business_store_attr_b_id` (`b_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8
/*!50100 PARTITION BY RANGE (`month`)
(PARTITION business_store_attr_1 VALUES LESS THAN (2) ENGINE = InnoDB,
 PARTITION business_store_attr_2 VALUES LESS THAN (3) ENGINE = InnoDB,
 PARTITION business_store_attr_3 VALUES LESS THAN (4) ENGINE = InnoDB,
 PARTITION business_store_attr_4 VALUES LESS THAN (5) ENGINE = InnoDB,
 PARTITION business_store_attr_5 VALUES LESS THAN (6) ENGINE = InnoDB,
 PARTITION business_store_attr_6 VALUES LESS THAN (7) ENGINE = InnoDB,
 PARTITION business_store_attr_7 VALUES LESS THAN (8) ENGINE = InnoDB,
 PARTITION business_store_attr_8 VALUES LESS THAN (9) ENGINE = InnoDB,
 PARTITION business_store_attr_9 VALUES LESS THAN (10) ENGINE = InnoDB,
 PARTITION business_store_attr_10 VALUES LESS THAN (11) ENGINE = InnoDB,
 PARTITION business_store_attr_11 VALUES LESS THAN (12) ENGINE = InnoDB,
 PARTITION business_store_attr_12 VALUES LESS THAN (13) ENGINE = InnoDB) */;

/*Data for the table `business_store_attr` */

/*Table structure for table `business_store_cerdentials` */

DROP TABLE IF EXISTS `business_store_cerdentials`;

CREATE TABLE `business_store_cerdentials` (
  `store_cerdentials_id` varchar(30) NOT NULL COMMENT '商户证件ID',
  `b_id` varchar(30) NOT NULL COMMENT '业务Id',
  `store_id` varchar(30) NOT NULL COMMENT '商店ID',
  `credentials_cd` varchar(12) NOT NULL COMMENT '证件类型，对应于 credentials表',
  `value` varchar(50) NOT NULL COMMENT '证件号码',
  `validity_period` date NOT NULL COMMENT '有效期，如果是长期有效 写成 3000/1/1',
  `positive_photo` varchar(100) DEFAULT NULL COMMENT '正面照片',
  `negative_photo` varchar(100) DEFAULT NULL COMMENT '反面照片',
  `month` int(11) NOT NULL COMMENT '月份',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `operate` varchar(3) NOT NULL COMMENT '数据状态，添加ADD，修改MOD 删除DEL',
  KEY `idx_business_store_cerdentials_store_id` (`store_id`),
  KEY `idx_business_store_cerdentials_b_id` (`b_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8
/*!50100 PARTITION BY RANGE (`month`)
(PARTITION business_store_cerdentials_1 VALUES LESS THAN (2) ENGINE = InnoDB,
 PARTITION business_store_cerdentials_2 VALUES LESS THAN (3) ENGINE = InnoDB,
 PARTITION business_store_cerdentials_3 VALUES LESS THAN (4) ENGINE = InnoDB,
 PARTITION business_store_cerdentials_4 VALUES LESS THAN (5) ENGINE = InnoDB,
 PARTITION business_store_cerdentials_5 VALUES LESS THAN (6) ENGINE = InnoDB,
 PARTITION business_store_cerdentials_6 VALUES LESS THAN (7) ENGINE = InnoDB,
 PARTITION business_store_cerdentials_7 VALUES LESS THAN (8) ENGINE = InnoDB,
 PARTITION business_store_cerdentials_8 VALUES LESS THAN (9) ENGINE = InnoDB,
 PARTITION business_store_cerdentials_9 VALUES LESS THAN (10) ENGINE = InnoDB,
 PARTITION business_store_cerdentials_10 VALUES LESS THAN (11) ENGINE = InnoDB,
 PARTITION business_store_cerdentials_11 VALUES LESS THAN (12) ENGINE = InnoDB,
 PARTITION business_store_cerdentials_12 VALUES LESS THAN (13) ENGINE = InnoDB) */;

/*Data for the table `business_store_cerdentials` */

/*Table structure for table `business_store_photo` */

DROP TABLE IF EXISTS `business_store_photo`;

CREATE TABLE `business_store_photo` (
  `store_photo_id` varchar(30) NOT NULL COMMENT '商户照片ID',
  `b_id` varchar(30) NOT NULL COMMENT '业务Id',
  `store_id` varchar(30) NOT NULL COMMENT '商店ID',
  `store_photo_type_cd` varchar(12) NOT NULL COMMENT '商户照片类型,T 门头照 I 内景照',
  `photo` varchar(100) NOT NULL COMMENT '照片',
  `month` int(11) NOT NULL COMMENT '月份',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `operate` varchar(3) NOT NULL COMMENT '数据状态，添加ADD，修改MOD 删除DEL',
  KEY `idx_business_store_photo_store_id` (`store_id`),
  KEY `idx_business_store_photo_b_id` (`b_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8
/*!50100 PARTITION BY RANGE (`month`)
(PARTITION business_store_photo_1 VALUES LESS THAN (2) ENGINE = InnoDB,
 PARTITION business_store_photo_2 VALUES LESS THAN (3) ENGINE = InnoDB,
 PARTITION business_store_photo_3 VALUES LESS THAN (4) ENGINE = InnoDB,
 PARTITION business_store_photo_4 VALUES LESS THAN (5) ENGINE = InnoDB,
 PARTITION business_store_photo_5 VALUES LESS THAN (6) ENGINE = InnoDB,
 PARTITION business_store_photo_6 VALUES LESS THAN (7) ENGINE = InnoDB,
 PARTITION business_store_photo_7 VALUES LESS THAN (8) ENGINE = InnoDB,
 PARTITION business_store_photo_8 VALUES LESS THAN (9) ENGINE = InnoDB,
 PARTITION business_store_photo_9 VALUES LESS THAN (10) ENGINE = InnoDB,
 PARTITION business_store_photo_10 VALUES LESS THAN (11) ENGINE = InnoDB,
 PARTITION business_store_photo_11 VALUES LESS THAN (12) ENGINE = InnoDB,
 PARTITION business_store_photo_12 VALUES LESS THAN (13) ENGINE = InnoDB) */;

/*Data for the table `business_store_photo` */

/*Table structure for table `business_user` */

DROP TABLE IF EXISTS `business_user`;

CREATE TABLE `business_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `b_id` varchar(30) NOT NULL COMMENT '业务Id',
  `user_id` varchar(30) NOT NULL COMMENT '用户ID',
  `name` varchar(50) NOT NULL COMMENT '名称',
  `email` varchar(30) DEFAULT NULL COMMENT '邮箱地址',
  `address` varchar(200) DEFAULT NULL COMMENT '现居住地址',
  `password` varchar(128) DEFAULT NULL COMMENT '用户密码，加密过后',
  `location_cd` varchar(8) DEFAULT NULL COMMENT '用户地区，编码详见 u_location',
  `age` int(11) DEFAULT NULL COMMENT '用户年龄',
  `sex` varchar(1) DEFAULT NULL COMMENT '性别，0表示男孩 1表示女孩',
  `tel` varchar(11) DEFAULT NULL COMMENT '用户手机',
  `level_cd` varchar(2) NOT NULL DEFAULT '0' COMMENT '用户级别,关联user_level',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `operate` varchar(3) NOT NULL COMMENT '数据状态，添加ADD，修改MOD 删除DEL',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `business_user` */

/*Table structure for table `business_user_address` */

DROP TABLE IF EXISTS `business_user_address`;

CREATE TABLE `business_user_address` (
  `address_id` varchar(30) NOT NULL COMMENT '地址ID',
  `b_id` varchar(30) NOT NULL COMMENT '业务Id',
  `user_id` varchar(30) NOT NULL COMMENT '用户ID',
  `tel` varchar(11) NOT NULL COMMENT '电话',
  `postal_code` varchar(10) NOT NULL COMMENT '邮政编码',
  `address` varchar(200) NOT NULL COMMENT '地址',
  `is_default` varchar(1) NOT NULL COMMENT '是否为默认地址 1，表示默认地址 0 或空不是默认地址',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `operate` varchar(3) NOT NULL COMMENT '数据状态，添加ADD，修改MOD 删除DEL'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `business_user_address` */

/*Table structure for table `business_user_attr` */

DROP TABLE IF EXISTS `business_user_attr`;

CREATE TABLE `business_user_attr` (
  `b_id` varchar(30) NOT NULL COMMENT '订单ID',
  `attr_id` varchar(30) NOT NULL COMMENT '属性id',
  `user_id` varchar(30) NOT NULL COMMENT '用户ID',
  `spec_cd` varchar(12) NOT NULL COMMENT '规格id,参考spec表',
  `value` varchar(50) NOT NULL COMMENT '属性值',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `operate` varchar(3) NOT NULL COMMENT '数据状态，添加ADD，修改MOD 删除DEL'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `business_user_attr` */

/*Table structure for table `business_user_credentials` */

DROP TABLE IF EXISTS `business_user_credentials`;

CREATE TABLE `business_user_credentials` (
  `credentials_id` varchar(30) NOT NULL COMMENT '证件ID',
  `b_id` varchar(30) NOT NULL COMMENT '业务Id',
  `user_id` varchar(30) NOT NULL COMMENT '用户ID',
  `credentials_cd` varchar(12) NOT NULL COMMENT '证件类型',
  `value` varchar(50) NOT NULL COMMENT '证件号码',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `operate` varchar(3) NOT NULL COMMENT '数据状态，添加ADD，修改MOD 删除DEL'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `business_user_credentials` */

/*Table structure for table `business_user_tag` */

DROP TABLE IF EXISTS `business_user_tag`;

CREATE TABLE `business_user_tag` (
  `tag_id` varchar(30) NOT NULL COMMENT '打标ID',
  `b_id` varchar(30) NOT NULL COMMENT '业务Id',
  `user_id` varchar(30) NOT NULL COMMENT '用户ID',
  `tag_cd` varchar(12) NOT NULL COMMENT '标签编码,参考tag表',
  `remark` varchar(200) DEFAULT NULL COMMENT '备注',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `operate` varchar(3) NOT NULL COMMENT '数据状态，添加ADD，修改MOD 删除DEL'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `business_user_tag` */

/*Table structure for table `c_app` */

DROP TABLE IF EXISTS `c_app`;

CREATE TABLE `c_app` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `app_id` varchar(30) NOT NULL COMMENT '应用ID',
  `name` varchar(50) NOT NULL COMMENT '名称 对应系统名称',
  `security_code` varchar(64) NOT NULL COMMENT '签名码 sign签名时用',
  `while_list_ip` varchar(200) DEFAULT NULL COMMENT '白名单ip 多个之间用;隔开',
  `black_list_ip` varchar(200) DEFAULT NULL COMMENT '黑名单ip 多个之间用;隔开',
  `remark` varchar(200) DEFAULT NULL COMMENT '描述',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `status_cd` varchar(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考c_status表，0在用，1失效',
  PRIMARY KEY (`id`),
  UNIQUE KEY `app_id` (`app_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

/*Data for the table `c_app` */

insert  into `c_app`(`id`,`app_id`,`name`,`security_code`,`while_list_ip`,`black_list_ip`,`remark`,`create_time`,`status_cd`) values (1,'8000418001','内部测试应用','',NULL,NULL,'记得删除','2018-11-14 13:28:44','0'),(2,'8000418002','控制中心应用','',NULL,NULL,'控制中心应用','2018-11-14 13:28:44','0'),(3,'8000418003','用户管理应用','WEBURFPKIFJUHNCJUEIKMKJUJHULSMNCHDY89KMC',NULL,NULL,'用户管理应用','2018-11-14 13:28:44','0');

/*Table structure for table `c_business` */

DROP TABLE IF EXISTS `c_business`;

CREATE TABLE `c_business` (
  `b_id` varchar(30) NOT NULL COMMENT '业务Id',
  `o_id` varchar(30) NOT NULL COMMENT '订单ID',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `business_type_cd` varchar(4) NOT NULL COMMENT '业务项类型，参考c_business_type表',
  `finish_time` date DEFAULT NULL COMMENT '完成时间',
  `remark` varchar(200) DEFAULT NULL COMMENT '备注',
  `status_cd` varchar(2) NOT NULL COMMENT '数据状态，详细参考c_status表',
  UNIQUE KEY `b_id` (`b_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `c_business` */

insert  into `c_business`(`b_id`,`o_id`,`create_time`,`business_type_cd`,`finish_time`,`remark`,`status_cd`) values ('20512771454920556544','10512771452089401344','2018-11-15 15:30:42','D',NULL,'保存用户录入费用信息','S'),('20512773563694972928','10512773560800903168','2018-11-15 15:39:05','D',NULL,'保存用户录入费用信息','E'),('20512774199522099200','10512774197236203520','2018-11-15 15:41:36','D',NULL,'保存用户录入费用信息','E'),('20512774704356917248','10512774702192656384','2018-11-15 15:43:37','D',NULL,'保存用户录入费用信息','E'),('20512774784430374912','10512774782215782400','2018-11-15 15:43:56','D',NULL,'保存用户录入费用信息','E'),('20512778369964703744','10512778366974164992','2018-11-15 15:58:11','D',NULL,'保存用户录入费用信息','E'),('20512781063739670528','10512781061441191936','2018-11-15 16:08:53','D',NULL,'保存用户录入费用信息','E'),('20512781924033363968','10512781921827160064','2018-11-15 16:12:18','D',NULL,'保存用户录入费用信息','E'),('20512782243995844608','10512782241781252096','2018-11-15 16:13:34','D',NULL,'保存用户录入费用信息','S'),('20512782362006781952','10512782357623734272','2018-11-15 16:14:03','D',NULL,'保存用户录入费用信息','S'),('20513135740868100096','10513135737009340416','2018-11-16 15:38:14','D',NULL,'保存用户录入费用信息','S'),('20513136394307108864','10513136391413039104','2018-11-16 15:40:50','D',NULL,'保存用户录入费用信息','E'),('20513138496731348992','10513138492071477248','2018-11-16 15:49:11','D',NULL,'保存用户录入费用信息','E'),('20513138960449404928','10513138958218035200','2018-11-16 15:51:02','D',NULL,'保存用户录入费用信息','E'),('20513139668225622016','10513139665960697856','2018-11-16 15:53:51','D',NULL,'保存用户录入费用信息','E'),('20513140469312520192','10513140466590416896','2018-11-16 15:57:02','D',NULL,'保存用户录入费用信息','E'),('20513141047354720256','10513141044963966976','2018-11-16 15:59:20','D',NULL,'保存商户录入用户应缴费用信息','E'),('20513143019977834496','10513143017092153344','2018-11-16 16:07:10','D',NULL,'保存用户录入费用信息','S'),('20513145624225382400','10513145621712994304','2018-11-16 16:17:31','D',NULL,'保存商户录入用户应缴费用信息','S'),('20513145829515591680','10513145827309387776','2018-11-16 16:18:20','D',NULL,'保存商户录入用户应缴费用信息','S'),('20513146865416404992','10513146861406650368','2018-11-16 16:22:27','D',NULL,'保存商户录入用户应缴费用信息','S');

/*Table structure for table `c_business_attrs` */

DROP TABLE IF EXISTS `c_business_attrs`;

CREATE TABLE `c_business_attrs` (
  `b_id` varchar(30) NOT NULL COMMENT '订单ID',
  `attr_id` varchar(30) NOT NULL COMMENT '属性id',
  `spec_cd` varchar(12) NOT NULL COMMENT '规格id,参考spec表',
  `value` varchar(50) NOT NULL COMMENT '属性值'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `c_business_attrs` */

/*Table structure for table `c_business_type` */

DROP TABLE IF EXISTS `c_business_type`;

CREATE TABLE `c_business_type` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `business_type_cd` varchar(4) NOT NULL COMMENT '业务项类型',
  `name` varchar(50) NOT NULL COMMENT '名称',
  `description` varchar(200) DEFAULT NULL COMMENT '描述',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `business_type_cd` (`business_type_cd`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

/*Data for the table `c_business_type` */

insert  into `c_business_type`(`id`,`business_type_cd`,`name`,`description`,`create_time`) values (1,'DO','撤单','作废订单','2018-11-14 13:28:44');

/*Table structure for table `c_cache` */

DROP TABLE IF EXISTS `c_cache`;

CREATE TABLE `c_cache` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '缓存ID',
  `cache_code` varchar(10) NOT NULL COMMENT '缓存编码 开始于1001',
  `service_code` varchar(50) NOT NULL COMMENT '调用服务编码 对应 c_service',
  `name` varchar(50) NOT NULL COMMENT '前台显示名称',
  `param` longtext NOT NULL COMMENT '请求缓存系统时的参数',
  `seq` int(11) NOT NULL COMMENT '列顺序',
  `group` varchar(10) NOT NULL DEFAULT 'COMMON' COMMENT '组，缓存属于哪个组',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `status_cd` varchar(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考c_status表，0在用，1失效',
  PRIMARY KEY (`id`),
  UNIQUE KEY `cache_code` (`cache_code`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

/*Data for the table `c_cache` */

insert  into `c_cache`(`id`,`cache_code`,`service_code`,`name`,`param`,`seq`,`group`,`create_time`,`status_cd`) values (1,'1001','flush.center.cache','映射缓存（c_mapping表）','{\"cacheName\":\"MAPPING\"}',1,'COMMON','2018-11-14 13:28:51','0'),(2,'1002','flush.center.cache','业务配置缓存（c_app,c_service,c_route表）','{\"cacheName\":\"APP_ROUTE_SERVICE\"}',2,'COMMON','2018-11-14 13:28:51','0'),(3,'1003','flush.center.cache','公用服务缓存（c_service_sql表）','{\"cacheName\":\"SERVICE_SQL\"}',3,'COMMON','2018-11-14 13:28:51','0');

/*Table structure for table `c_cache_2_user` */

DROP TABLE IF EXISTS `c_cache_2_user`;

CREATE TABLE `c_cache_2_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '缓存用户ID',
  `cache_code` int(11) NOT NULL COMMENT '缓存编码',
  `user_id` varchar(30) NOT NULL COMMENT '用户id',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `status_cd` varchar(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考c_status表，0在用，1失效',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

/*Data for the table `c_cache_2_user` */

insert  into `c_cache_2_user`(`id`,`cache_code`,`user_id`,`create_time`,`status_cd`) values (1,1001,'10001','2018-11-14 13:28:51','0'),(2,1002,'10001','2018-11-14 13:28:51','0'),(3,1003,'10001','2018-11-14 13:28:51','0');

/*Table structure for table `c_comment` */

DROP TABLE IF EXISTS `c_comment`;

CREATE TABLE `c_comment` (
  `comment_id` varchar(30) NOT NULL COMMENT '评论ID',
  `user_id` varchar(30) NOT NULL COMMENT '评论者用户ID',
  `b_id` varchar(30) NOT NULL COMMENT '订单ID',
  `comment_type_cd` varchar(2) NOT NULL DEFAULT 'S' COMMENT '评论类型 S表示 商品 M表示 商户 T 物流',
  `out_id` varchar(30) NOT NULL COMMENT '外部ID，如商品ID 商户ID等',
  `month` int(11) NOT NULL COMMENT '月份',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `status_cd` varchar(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考c_status表，S 保存，0, 在用 1失效',
  KEY `idx_comment_b_id` (`b_id`),
  KEY `idx_comment_out_id` (`out_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8
/*!50100 PARTITION BY RANGE (`month`)
(PARTITION comment_1 VALUES LESS THAN (2) ENGINE = InnoDB,
 PARTITION comment_2 VALUES LESS THAN (3) ENGINE = InnoDB,
 PARTITION comment_3 VALUES LESS THAN (4) ENGINE = InnoDB,
 PARTITION comment_4 VALUES LESS THAN (5) ENGINE = InnoDB,
 PARTITION comment_5 VALUES LESS THAN (6) ENGINE = InnoDB,
 PARTITION comment_6 VALUES LESS THAN (7) ENGINE = InnoDB,
 PARTITION comment_7 VALUES LESS THAN (8) ENGINE = InnoDB,
 PARTITION comment_8 VALUES LESS THAN (9) ENGINE = InnoDB,
 PARTITION comment_9 VALUES LESS THAN (10) ENGINE = InnoDB,
 PARTITION comment_10 VALUES LESS THAN (11) ENGINE = InnoDB,
 PARTITION comment_11 VALUES LESS THAN (12) ENGINE = InnoDB,
 PARTITION comment_12 VALUES LESS THAN (13) ENGINE = InnoDB) */;

/*Data for the table `c_comment` */

/*Table structure for table `c_comment_score` */

DROP TABLE IF EXISTS `c_comment_score`;

CREATE TABLE `c_comment_score` (
  `comment_score_id` varchar(30) NOT NULL COMMENT '评论分数ID',
  `comment_id` varchar(30) NOT NULL COMMENT '评论ID',
  `b_id` varchar(30) NOT NULL COMMENT '业务Id',
  `score_type_cd` varchar(30) NOT NULL COMMENT '打分类别，S 商品相符，U 卖家打分，T 物流打分',
  `value` int(11) NOT NULL COMMENT '分数',
  `month` int(11) NOT NULL COMMENT '月份',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `status_cd` varchar(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考c_status表，S 保存，0, 在用 1失效',
  KEY `idx_comment_score_b_id` (`b_id`),
  KEY `idx_comment_score_comment_id` (`comment_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8
/*!50100 PARTITION BY RANGE (`month`)
(PARTITION comment_score_1 VALUES LESS THAN (2) ENGINE = InnoDB,
 PARTITION comment_score_2 VALUES LESS THAN (3) ENGINE = InnoDB,
 PARTITION comment_score_3 VALUES LESS THAN (4) ENGINE = InnoDB,
 PARTITION comment_score_4 VALUES LESS THAN (5) ENGINE = InnoDB,
 PARTITION comment_score_5 VALUES LESS THAN (6) ENGINE = InnoDB,
 PARTITION comment_score_6 VALUES LESS THAN (7) ENGINE = InnoDB,
 PARTITION comment_score_7 VALUES LESS THAN (8) ENGINE = InnoDB,
 PARTITION comment_score_8 VALUES LESS THAN (9) ENGINE = InnoDB,
 PARTITION comment_score_9 VALUES LESS THAN (10) ENGINE = InnoDB,
 PARTITION comment_score_10 VALUES LESS THAN (11) ENGINE = InnoDB,
 PARTITION comment_score_11 VALUES LESS THAN (12) ENGINE = InnoDB,
 PARTITION comment_score_12 VALUES LESS THAN (13) ENGINE = InnoDB) */;

/*Data for the table `c_comment_score` */

/*Table structure for table `c_mapping` */

DROP TABLE IF EXISTS `c_mapping`;

CREATE TABLE `c_mapping` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `domain` varchar(50) NOT NULL COMMENT '域',
  `name` varchar(50) NOT NULL COMMENT '名称',
  `key` varchar(100) NOT NULL COMMENT 'key',
  `value` varchar(1000) NOT NULL COMMENT 'value',
  `remark` varchar(200) DEFAULT NULL COMMENT '描述',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `status_cd` varchar(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考c_status表，0在用，1失效',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8;

/*Data for the table `c_mapping` */

insert  into `c_mapping`(`id`,`domain`,`name`,`key`,`value`,`remark`,`create_time`,`status_cd`) values (1,'DOMAIN.COMMON','日志开关','LOG_ON_OFF','OFF','日志开关','2018-11-14 13:28:44','0'),(2,'DOMAIN.COMMON','耗时开关','COST_TIME_ON_OFF','OFF','耗时开关','2018-11-14 13:28:44','0'),(3,'DOMAIN.COMMON','规则开关','RULE_ON_OFF','OFF','规则开关','2018-11-14 13:28:44','0'),(4,'DOMAIN.COMMON','不调规则服务的订单类型','NO_NEED_RULE_VALDATE_ORDER','Q','不调规则服务的订单类型','2018-11-14 13:28:44','0'),(5,'DOMAIN.COMMON','不保存订单信息','NO_SAVE_ORDER','Q,T','不保存订单信息','2018-11-14 13:28:44','0'),(6,'DOMAIN.COMMON','不用调用 下游系统的配置','NO_INVOKE_BUSINESS_SYSTEM','Q','不用调用 下游系统的配置(一般不存在这种情况，这里主要是在没有下游系统的情况下测试中心服务用)','2018-11-14 13:28:44','0'),(7,'DOMAIN.COMMON','不用调用 作废下游系统的配置','NO_INVALID_BUSINESS_SYSTEM','Q','不用调用 作废下游系统的配置 (一般不存在这种情况，这里主要是在没有下游系统的情况下测试中心服务用)','2018-11-14 13:28:44','0'),(8,'DOMAIN.COMMON','需要调用服务生成各个ID','NEED_INVOKE_SERVICE_GENERATE_ID','ON','需要调用服务生成各个ID','2018-11-14 13:28:44','0'),(9,'DOMAIN.COMMON','公钥','PUBLIC_STRING','MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDloKXSBA5+tP39uS8yi5RZOs6Jdrt0znRQetyXX2l/IUCi1x1QAMgoZbnEavmdZ5jOZN/T1WYFbt/VomXEHaTdStAiYm3DCnxxy5CMMyRKai0+6n4lLJQpUmnAQPFENrOV8b70gBSBVjUXksImgui5qYaNqX90pjEzcyq+6CugBwIDAQAB','公钥','2018-11-14 13:28:44','0'),(10,'DOMAIN.COMMON','私钥','PRIVATE_STRING','MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAJbtQYV+VpWZvifoc0R11MyAfIyMGoJKHDrWQau7oxLHotDDJM80o7ea7oL2onaHWOXaybpUp5FpZgjuixYMhlQOA/VXosrJOGJhgNv0dAL6VVXxmjlg2UWqIEoyTS7IzF3BuQCqy2k9aT7mGiC0RYRpndTuwe/0DKwFx70dIIIrAgMBAAECgYBMNMHnqLIJWZa1Sd6hy6lGFP5ObROZg9gbMUH5d4XQnrKsHEyCvz6HH5ic0fGYTaDqdn1zMvllJ8XYbrIV0P8lvHr9/LCnoXessmf61hKZyTKi5ycNkiPHTjmJZCoVTQFprcNgYeVX4cvNsqB2zWwzoAk8bbdWY6X5jB6YEpfBmQJBANiO9GiBtw+T9h60MpyV1xhJKsx0eCvxRZcsDB1OLZvQ7dHnsHmh0xUBL2MraHKnQyxOlrIzOtyttxSTrQzwcM0CQQCyajkzxpF6EjrXWHYVHb3AXFSoz5krjOkLEHegYlGhx0gtytBNVwftCn6hqtaxCxKMp00ZJoXIxo8d9tQy4N7XAkBljnTT5bEBnzPWpk7t298pRnbJtvz8LoOiJ0fvHlCJN+uvemXqRJeGzC165kpvKj14M8q7+wZpoxWukqqe3MspAkAuFYHw/blV7p+EQDU//w6kQTUc5YKK3TrUwMwlgT/UqcTbDyf+0hwZ/jv3RkluMY35Br3DYU/tLFyLQNZOzgbBAkEApWARXVlleEYbv8dPUL+56S0ky1hZSuPfVOBda4V3p0q18LjcHIyYcVhKGqkpii5JgblaYyjUriNDisFalCp8jQ==','私钥','2018-11-14 13:28:44','0'),(11,'DOMAIN.COMMON','外部应用公钥','PUBLIC_STRING','MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCW7UGFflaVmb4n6HNEddTMgHyMjBqCShw61kGru6MSx6LQwyTPNKO3mu6C9qJ2h1jl2sm6VKeRaWYI7osWDIZUDgP1V6LKyThiYYDb9HQC+lVV8Zo5YNlFqiBKMk0uyMxdwbkAqstpPWk+5hogtEWEaZ3U7sHv9AysBce9HSCCKwIDAQAB','外部应用公钥','2018-11-14 13:28:44','0'),(12,'DOMAIN.COMMON','外部应用私钥','PRIVATE_STRING','MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAOWgpdIEDn60/f25LzKLlFk6zol2u3TOdFB63JdfaX8hQKLXHVAAyChlucRq+Z1nmM5k39PVZgVu39WiZcQdpN1K0CJibcMKfHHLkIwzJEpqLT7qfiUslClSacBA8UQ2s5XxvvSAFIFWNReSwiaC6Lmpho2pf3SmMTNzKr7oK6AHAgMBAAECgYEAlfR5FVNM2/X6QC0k408/i53Zru94r2j7kGsLj1bhoAHpIe502AAKtkboL5rkc6Rpp688dCvRug6T4gFxj8cEF7rOOU4CHqVCHUHa4sWSDL2Rg7pMbQOVB7PGmM4C/hEgXcwM6tmLiU3xkkQDrpgT1bPpAm7iwDx4HkZBv1naYnECQQDyk40+KDvyUgp/r1tKbkMLoQOAfTZPXy+HgeAkU3PCUTFQlvn2OU6Fsl3Yjlp6utxPVnd00DoPZ8qvx1falaeLAkEA8lWoIDgyYwnibv2fr3A715PkieJ0exKfLb5lSD9UIfGJ9s7oTcltl7pprykfSP46heWjScS7YJRZHPfqb1/Y9QJAJNUQqjJzv7yDSZX3t5p8ZaSiIn1gpLagQeQPg5SETCoF4eW6uI9FA/nsU/hxdpcu4oEPjFYdqr8owH31MgRtNwJAaE+6qPPHrJ3qnAAMJoZXG/qLG1cg8IEZh6U3D5xC6MGBs31ovWMBC5iwOTeoQdE8+7nXSb+nMHFq0m9cuEg3qQJAH4caPSQ9RjVOP9on+niy9b1mATbvurepIB95KUtaHLz1hpihCLR7dTwrz8JOitgFE75Wzt4a00GZYxnaq3jsjA==','外部应用私钥','2018-11-14 13:28:44','0'),(13,'DOMAIN.COMMON','默认KEY_SIZE','DEFAULT_DECRYPT_KEY_SIZE','2048','默认KEY_SIZE','2018-11-14 13:28:44','0'),(14,'DOMAIN.COMMON','中心服务地址','CENTER_SERVICE_URL','http://center-service/httpApi/service','中心服务地址','2018-11-14 13:28:44','0'),(15,'DOMAIN.COMMON','控制中心APP_ID','CONSOLE_SERVICE_APP_ID','8000418002','控制中心APP_ID','2018-11-14 13:28:44','0'),(16,'DOMAIN.COMMON','控制服务加密开关','KEY_CONSOLE_SERVICE_SECURITY_ON_OFF','ON','控制服务加密开关','2018-11-14 13:28:44','0'),(17,'DOMAIN.COMMON','控制服务鉴权秘钥','CONSOLE_SECURITY_CODE','WEBURFPKIFJUHNCJUEIKMKJUJHULSMNCHDY89KMC','控制服务鉴权秘钥','2018-11-14 13:28:44','0'),(18,'DOMAIN.COMMON','编码生成服务地址','CODE_PATH','http://code-service/codeApi/generate','编码生成服务地址','2018-11-14 13:28:44','0');

/*Table structure for table `c_order_type` */

DROP TABLE IF EXISTS `c_order_type`;

CREATE TABLE `c_order_type` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `order_type_cd` varchar(4) NOT NULL COMMENT '订单类型',
  `name` varchar(50) NOT NULL COMMENT '名称',
  `description` varchar(200) DEFAULT NULL COMMENT '描述',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `order_type_cd` (`order_type_cd`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

/*Data for the table `c_order_type` */

insert  into `c_order_type`(`id`,`order_type_cd`,`name`,`description`,`create_time`) values (1,'Q','查询单','查询单','2018-11-14 13:28:44'),(2,'D','受理单','受理单','2018-11-14 13:28:44');

/*Table structure for table `c_orders` */

DROP TABLE IF EXISTS `c_orders`;

CREATE TABLE `c_orders` (
  `o_id` varchar(30) NOT NULL COMMENT '订单ID',
  `app_id` varchar(30) NOT NULL COMMENT '应用ID',
  `ext_transaction_id` varchar(30) NOT NULL COMMENT '外部交易流水',
  `user_id` varchar(30) NOT NULL COMMENT '用户ID',
  `request_time` varchar(16) NOT NULL COMMENT '外部系统请求时间',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `order_type_cd` varchar(4) NOT NULL COMMENT '订单类型，参考c_order_type表',
  `finish_time` date DEFAULT NULL COMMENT '订单完成时间',
  `remark` varchar(200) DEFAULT NULL COMMENT '备注',
  `status_cd` varchar(2) NOT NULL COMMENT '数据状态，详细参考c_status表',
  UNIQUE KEY `o_id` (`o_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `c_orders` */

insert  into `c_orders`(`o_id`,`app_id`,`ext_transaction_id`,`user_id`,`request_time`,`create_time`,`order_type_cd`,`finish_time`,`remark`,`status_cd`) values ('102018111500000001','8000418001','100000000020180409224736000001','1101','20181115215136','2018-11-15 15:04:21','D',NULL,'保存用户录入费用信息','S'),('102018111500000002','8000418001','100000000020180409224736000001','1101','20181115215136','2018-11-15 15:16:48','D',NULL,'保存用户录入费用信息','S'),('102018111500000003','8000418001','100000000020180409224736000001','1101','20181115215136','2018-11-15 15:17:19','D',NULL,'保存用户录入费用信息','S'),('102018111500000004','8000418001','100000000020180409224736000001','1101','20181115215136','2018-11-15 15:18:04','D',NULL,'保存用户录入费用信息','S'),('102018111500000005','8000418001','100000000020180409224736000001','1101','20181115215136','2018-11-15 15:19:03','D',NULL,'保存用户录入费用信息','S'),('10512770937368608768','8000418001','100000000020180409224736000001','1101','20181115215136','2018-11-15 15:28:39','D',NULL,'保存用户录入费用信息','S'),('10512771452089401344','8000418001','100000000020180409224736000001','1101','20181115215136','2018-11-15 15:30:42','D',NULL,'保存用户录入费用信息','S'),('10512773560800903168','8000418001','100000000020180409224736000001','1101','20181115215136','2018-11-15 15:39:04','D',NULL,'保存用户录入费用信息','E'),('10512774197236203520','8000418001','100000000020180409224736000001','1101','20181115215136','2018-11-15 15:41:36','D',NULL,'保存用户录入费用信息','E'),('10512774702192656384','8000418001','100000000020180409224736000001','1101','20181115215136','2018-11-15 15:43:36','D',NULL,'保存用户录入费用信息','E'),('10512774782215782400','8000418001','100000000020180409224736000001','1101','20181115215136','2018-11-15 15:43:55','D',NULL,'保存用户录入费用信息','E'),('10512778366974164992','8000418001','100000000020180409224736000001','1101','20181115215136','2018-11-15 15:58:10','D',NULL,'保存用户录入费用信息','E'),('10512781061441191936','8000418001','100000000020180409224736000001','1101','20181115215136','2018-11-15 16:08:53','D',NULL,'保存用户录入费用信息','E'),('10512781921827160064','8000418001','100000000020180409224736000001','1101','20181115215136','2018-11-15 16:12:18','D',NULL,'保存用户录入费用信息','E'),('10512782241781252096','8000418001','100000000020180409224736000001','1101','20181115215136','2018-11-15 16:13:34','D',NULL,'保存用户录入费用信息','S'),('10512782357623734272','8000418001','100000000020180409224736000001','1101','20181115215136','2018-11-15 16:14:02','D',NULL,'保存用户录入费用信息','S'),('10513135737009340416','8000418001','100000000020180409224736000002','1101','20181115215136','2018-11-16 15:38:14','D',NULL,'保存用户录入费用信息','S'),('10513136391413039104','8000418001','100000000020180409224736000002','1101','20181115215136','2018-11-16 15:40:50','D',NULL,'保存用户录入费用信息','E'),('10513138492071477248','8000418001','100000000020180409224736000002','1101','20181115215136','2018-11-16 15:49:10','D',NULL,'保存用户录入费用信息','E'),('10513138958218035200','8000418001','100000000020180409224736000002','1101','20181115215136','2018-11-16 15:51:01','D',NULL,'保存用户录入费用信息','E'),('10513139665960697856','8000418001','100000000020180409224736000002','1101','20181115215136','2018-11-16 15:53:50','D',NULL,'保存用户录入费用信息','E'),('10513140466590416896','8000418001','100000000020180409224736000002','1101','20181115215136','2018-11-16 15:57:01','D',NULL,'保存用户录入费用信息','E'),('10513141044963966976','8000418001','100000000020180409224736000002','800625487','20181115215136','2018-11-16 15:59:19','D',NULL,'保存用户录入费用信息','E'),('10513143017092153344','8000418001','100000000020180409224736000002','1101','20181115215136','2018-11-16 16:07:09','D',NULL,'保存用户录入费用信息','S'),('10513145621712994304','8000418001','100000000020180409224736000002','800625487','20181115215136','2018-11-16 16:17:30','D',NULL,'保存用户录入费用信息','S'),('10513145827309387776','8000418001','100000000020180409224736000002','800625487','20181115215136','2018-11-16 16:18:19','D',NULL,'保存用户录入费用信息','S'),('10513146861406650368','8000418001','100000000020180409224736000002','800625487','20181115215136','2018-11-16 16:22:26','D',NULL,'保存用户录入费用信息','S');

/*Table structure for table `c_orders_attrs` */

DROP TABLE IF EXISTS `c_orders_attrs`;

CREATE TABLE `c_orders_attrs` (
  `o_id` varchar(30) NOT NULL COMMENT '订单ID',
  `attr_id` varchar(30) NOT NULL COMMENT '属性id',
  `spec_cd` varchar(12) NOT NULL COMMENT '规格id,参考spec表',
  `value` varchar(50) NOT NULL COMMENT '属性值'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `c_orders_attrs` */

insert  into `c_orders_attrs`(`o_id`,`attr_id`,`spec_cd`,`value`) values ('10512771452089401344','11512771453565796352','1001','测试案例'),('10512773560800903168','11512773562461847552','1001','测试案例'),('10512774197236203520','11512774198477717504','1001','测试案例'),('10512774702192656384','11512774703383838720','1001','测试案例'),('10512774782215782400','11512774783264358400','1001','测试案例'),('10512778366974164992','11512778368580583424','1001','测试案例'),('10512781061441191936','11512781062695288832','1001','测试案例'),('10512781921827160064','11512781923018342400','1001','测试案例'),('10512782241781252096','11512782243047931904','1001','测试案例'),('10512782357623734272','11512782358638755840','1001','测试案例'),('10513135737009340416','11513135739198767104','1001','测试案例'),('10513136391413039104','11513136392964931584','1001','测试案例'),('10513138492071477248','11513138493426237440','1001','测试案例'),('10513138958218035200','11513138959384051712','1001','测试案例'),('10513139665960697856','11513139667198017536','1001','测试案例'),('10513140466590416896','11513140468163280896','1001','测试案例'),('10513141044963966976','11513141046289367040','1001','测试案例'),('10513143017092153344','11513143018937647104','1001','测试案例'),('10513145621712994304','11513145623130669056','1001','测试案例'),('10513145827309387776','11513145828433461248','1001','测试案例'),('10513146861406650368','11513146864455909376','1001','测试案例');

/*Table structure for table `c_route` */

DROP TABLE IF EXISTS `c_route`;

CREATE TABLE `c_route` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `app_id` varchar(30) NOT NULL COMMENT '应用ID',
  `service_id` int(11) NOT NULL COMMENT '下游接口配置ID',
  `order_type_cd` varchar(4) NOT NULL COMMENT '订单类型，参考c_order_type表',
  `invoke_limit_times` int(11) DEFAULT NULL COMMENT '接口调用一分钟调用次数',
  `invoke_model` varchar(1) NOT NULL COMMENT '1-同步方式 2-异步方式',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `status_cd` varchar(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考c_status表，0在用，1失效，2 表示下线（当组件调用服务超过限制时自动下线）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8;

/*Data for the table `c_route` */

insert  into `c_route`(`id`,`app_id`,`service_id`,`order_type_cd`,`invoke_limit_times`,`invoke_model`,`create_time`,`status_cd`) values (1,'8000418001',1,'Q',NULL,'S','2018-11-14 13:28:44','0'),(2,'8000418001',3,'Q',NULL,'S','2018-11-14 13:28:44','0'),(3,'8000418002',3,'Q',NULL,'S','2018-11-14 13:28:44','0'),(4,'8000418002',4,'Q',NULL,'S','2018-11-14 13:28:44','0'),(5,'8000418002',5,'Q',NULL,'S','2018-11-14 13:28:44','0'),(6,'8000418002',6,'Q',NULL,'S','2018-11-14 13:28:44','0'),(7,'8000418002',7,'Q',NULL,'S','2018-11-14 13:28:44','0'),(8,'8000418002',8,'Q',NULL,'S','2018-11-14 13:28:44','0'),(9,'8000418002',9,'Q',NULL,'S','2018-11-14 13:28:44','0'),(10,'8000418002',10,'Q',NULL,'S','2018-11-14 13:28:44','0'),(11,'8000418002',11,'Q',NULL,'S','2018-11-14 13:28:44','0'),(12,'8000418002',12,'Q',NULL,'S','2018-11-14 13:28:44','0'),(13,'8000418002',13,'Q',NULL,'S','2018-11-14 13:28:45','0'),(14,'8000418002',14,'Q',NULL,'S','2018-11-14 13:28:45','0'),(15,'8000418002',15,'Q',NULL,'S','2018-11-14 13:28:45','0'),(16,'8000418002',16,'Q',NULL,'S','2018-11-14 13:28:45','0'),(17,'8000418002',17,'Q',NULL,'S','2018-11-14 13:28:45','0'),(18,'8000418001',21,'Q',NULL,'S','2018-11-14 13:28:45','0'),(19,'8000418001',22,'Q',NULL,'S','2018-11-14 13:28:45','0'),(20,'8000418001',23,'Q',NULL,'S','2018-11-14 13:28:45','0'),(21,'8000418001',24,'Q',NULL,'S','2018-11-14 13:28:45','0'),(22,'8000418001',25,'Q',NULL,'S','2018-11-14 13:28:45','0'),(23,'8000418001',26,'Q',NULL,'S','2018-11-14 13:28:45','0'),(24,'8000418001',27,'Q',NULL,'S','2018-11-14 13:28:45','0'),(25,'8000418001',33,'Q',NULL,'S','2018-11-15 13:46:45','0'),(26,'8000418001',34,'Q',NULL,'S','2018-11-16 15:31:35','0');

/*Table structure for table `c_service` */

DROP TABLE IF EXISTS `c_service`;

CREATE TABLE `c_service` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `service_id` varchar(12) NOT NULL COMMENT '服务ID',
  `service_code` varchar(50) NOT NULL COMMENT '自定义，命名方式查询类query.+目标系统+.+业务名称 保存类 save.+目标系统+.+业务名称 修改类 modify.+目标系统+.+业务名称 删除类 remove.+目标系统+.+业务名称 例如：query.user.userinfo save.user.adduserinfo',
  `business_type_cd` varchar(4) NOT NULL COMMENT '业务项类型，参考c_business_type表',
  `name` varchar(50) NOT NULL COMMENT '服务名称',
  `seq` int(11) NOT NULL COMMENT '顺序 只有同步方式下根据seq从小到大调用接口',
  `messageQueueName` varchar(50) DEFAULT NULL COMMENT '消息队里名称 只有异步时有用',
  `is_instance` varchar(2) NOT NULL DEFAULT 'N' COMMENT '是否Instance Y 需要，N不需要，NT透传类',
  `url` varchar(200) DEFAULT NULL COMMENT '目标地址',
  `method` varchar(50) DEFAULT NULL COMMENT '方法 空 为http post LOCAL_SERVICE 为调用本地服务 其他为webservice方式调用',
  `timeout` int(11) NOT NULL DEFAULT '60' COMMENT '超时时间',
  `retry_count` int(11) NOT NULL DEFAULT '3' COMMENT '重试次数',
  `provide_app_id` varchar(30) NOT NULL COMMENT '应用ID',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `status_cd` varchar(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考c_status表，0在用，1失效',
  PRIMARY KEY (`id`),
  UNIQUE KEY `service_code` (`service_code`),
  UNIQUE KEY `service_id` (`service_id`)
) ENGINE=InnoDB AUTO_INCREMENT=37 DEFAULT CHARSET=utf8;

/*Data for the table `c_service` */

insert  into `c_service`(`id`,`service_id`,`service_code`,`business_type_cd`,`name`,`seq`,`messageQueueName`,`is_instance`,`url`,`method`,`timeout`,`retry_count`,`provide_app_id`,`create_time`,`status_cd`) values (1,'1','query.user.userInfo','Q','用户信息查询',1,NULL,'N','http://...',NULL,60,3,'8000418001','2018-11-18 05:44:00','0'),(2,'2','query.order.orderInfo','Q','订单信息',1,NULL,'N','http://center-service/businessApi/query',NULL,60,3,'8000418001','2018-11-18 05:44:00','0'),(3,'3','query.console.menu','Q','查询菜单',1,NULL,'N','http://center-service/businessApi/query',NULL,60,3,'8000418002','2018-11-18 05:44:00','0'),(4,'4','query.user.loginInfo','Q','查询用户登录信息',1,NULL,'N','http://center-service/businessApi/query',NULL,60,3,'8000418002','2018-11-18 05:44:01','0'),(5,'5','query.console.template','Q','查询模板信息',1,NULL,'N','http://center-service/businessApi/query',NULL,60,3,'8000418002','2018-11-18 05:44:01','0'),(6,'6','query.console.templateCol','Q','查询列模板信息',1,NULL,'N','http://center-service/businessApi/query',NULL,60,3,'8000418002','2018-11-18 05:44:01','0'),(7,'7','query.center.mapping','Q','查询映射数据',1,NULL,'N','http://center-service/businessApi/query',NULL,60,3,'8000418002','2018-11-18 05:44:01','0'),(8,'8','query.center.apps','Q','查询外部应用',1,NULL,'N','http://center-service/businessApi/query',NULL,60,3,'8000418002','2018-11-18 05:44:01','0'),(9,'9','query.center.services','Q','查询服务',1,NULL,'N','http://center-service/businessApi/query',NULL,60,3,'8000418002','2018-11-18 05:44:01','0'),(10,'10','query.center.routes','Q','查询路由',1,NULL,'N','http://center-service/businessApi/query',NULL,60,3,'8000418002','2018-11-18 05:44:01','0'),(11,'11','flush.center.cache','Q','CenterService缓存',1,NULL,'N','http://center-service/cacheApi/flush',NULL,60,3,'8000418002','2018-11-18 05:44:01','0'),(12,'12','query.console.caches','Q','查询所有缓存',1,NULL,'N','http://center-service/businessApi/query',NULL,60,3,'8000418002','2018-11-18 05:44:01','0'),(13,'13','query.console.cache','Q','查询所有缓存',1,NULL,'N','http://center-service/businessApi/query',NULL,60,3,'8000418002','2018-11-18 05:44:01','0'),(14,'14','save.center.mapping','Q','保存映射信息',1,NULL,'N','http://center-service/businessApi/do',NULL,60,3,'8000418002','2018-11-18 05:44:01','0'),(15,'15','delete.center.mapping','Q','删除映射信息',1,NULL,'N','http://center-service/businessApi/do',NULL,60,3,'8000418002','2018-11-18 05:44:01','0'),(16,'16','update.center.mapping','Q','修改映射信息',1,NULL,'N','http://center-service/businessApi/do',NULL,60,3,'8000418002','2018-11-18 05:44:01','0'),(17,'17','save.user.userInfo','D','保存用户信息',1,NULL,'N','http://user-service/userApi/service',NULL,60,3,'8000418003','2018-11-18 05:44:01','0'),(18,'18','save.store.info','D','保存商户信息',1,NULL,'N','http://store-service/storeApi/service',NULL,60,3,'8000418003','2018-11-18 05:44:01','0'),(19,'19','update.store.info','D','修改商户信息',1,NULL,'N','http://store-service/storeApi/service',NULL,60,3,'8000418003','2018-11-18 05:44:01','0'),(20,'20','delete.store.info','D','删除商户信息',1,NULL,'N','http://store-service/storeApi/service',NULL,60,3,'8000418003','2018-11-18 05:44:01','0'),(21,'21','transfer.console.menu','T','透传菜单查询',1,NULL,'N','http://192.168.31.199:8001/userApi/service',NULL,60,3,'8000418001','2018-11-18 05:44:01','0'),(22,'22','save.shop.info','D','保存商品信息',1,NULL,'N','http://shop-service/shopApi/service',NULL,60,3,'8000418003','2018-11-18 05:44:01','0'),(23,'23','update.shop.info','D','修改商品信息',1,NULL,'N','http://shop-service/shopApi/service',NULL,60,3,'8000418003','2018-11-18 05:44:01','0'),(24,'24','delete.shop.info','D','删除商品信息',1,NULL,'N','http://shop-service/shopApi/service',NULL,60,3,'8000418003','2018-11-18 05:44:01','0'),(25,'25','buy.shop.info','D','购买商品信息',1,NULL,'N','http://shop-service/shopApi/service',NULL,60,3,'8000418003','2018-11-18 05:44:01','0'),(26,'26','save.shop.catalog','D','保存商品目录信息',1,NULL,'N','http://shop-service/shopApi/service',NULL,60,3,'8000418003','2018-11-18 05:44:01','0'),(27,'27','update.shop.catalog','D','修改商品目录信息',1,NULL,'N','http://shop-service/shopApi/service',NULL,60,3,'8000418003','2018-11-18 05:44:01','0'),(28,'28','delete.shop.catalog','D','删除商品目录信息',1,NULL,'N','http://shop-service/shopApi/service',NULL,60,3,'8000418003','2018-11-18 05:44:01','0'),(29,'29','save.comment.info','D','保存评论',1,NULL,'N','http://comment-service/commentApi/service',NULL,60,3,'8000418003','2018-11-18 05:44:01','0'),(30,'30','delete.comment.info','D','删除评论',1,NULL,'N','http://comment-service/commentApi/service',NULL,60,3,'8000418003','2018-11-18 05:44:01','0'),(31,'31','member.joined.store','D','商户成员加入',1,NULL,'N','http://store-service/storeApi/service',NULL,60,3,'8000418003','2018-11-18 05:44:01','0'),(32,'32','member.quit.store','D','商户成员退出',1,NULL,'N','http://store-service/storeApi/service',NULL,60,3,'8000418003','2018-11-18 05:44:02','0'),(33,'33','save.storefee.info','D','保存用户费用信息',1,NULL,'N','http://store-service/storeApi/service',NULL,60,3,'8000418001','2018-11-18 05:45:46','0'),(34,'34','save.storehouse.info','D','保存商户录入用户应缴费用信息',1,NULL,'N','http://store-service/storeApi/service',NULL,60,3,'8000418001','2018-11-18 05:45:49','0'),(36,'35','do.service.order','API','订单服务受理',1,NULL,'Y','http://center-service/centerApi/service',NULL,60,3,'8000418003','2018-11-18 05:47:11','0');

/*Table structure for table `c_service_sql` */

DROP TABLE IF EXISTS `c_service_sql`;

CREATE TABLE `c_service_sql` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `service_code` varchar(50) NOT NULL COMMENT '对应c_service表',
  `name` varchar(50) NOT NULL COMMENT '名称',
  `params` varchar(500) NOT NULL COMMENT '参数',
  `query_model` varchar(1) NOT NULL COMMENT '查询方式 1、sql,2、存储过程',
  `sql` longtext COMMENT '执行sql',
  `proc` varchar(200) DEFAULT NULL COMMENT '存储过程名称',
  `java_script` longtext COMMENT '执行java脚本代码',
  `template` longtext COMMENT '输出模板',
  `remark` varchar(200) DEFAULT NULL COMMENT '描述',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `status_cd` varchar(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考c_status表，0在用，1失效',
  PRIMARY KEY (`id`),
  UNIQUE KEY `service_code` (`service_code`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8;

/*Data for the table `c_service_sql` */

insert  into `c_service_sql`(`id`,`service_code`,`name`,`params`,`query_model`,`sql`,`proc`,`java_script`,`template`,`remark`,`create_time`,`status_cd`) values (1,'query.order.orderInfo','订单信息','oId','1','{\n                                                 	\"param1\":\"SELECT co.app_id appId,co.create_time createTime,co.ext_transaction_id extTransactionId,co.finish_time finishTime\n                                                 ,co.order_type_cd orderTypeCd,co.o_id oId ,co.remark remark ,co.request_time requestTime ,co.user_id userId,co.status_cd statusCd\n                                                  FROM c_orders co WHERE co.o_id = #oId#\",\n                                                  \"param2\":\"SELECT cb.b_id bId, cb.business_type_cd businessTypeCd,cb.create_time createTime,cb.finish_time finishTime ,cb.o_id oId,\n                                                 cb.remark remark,cb.status_cd statusCd FROM c_business cb WHERE cb.o_id = #oId#\"\n                                                 }','',NULL,'{\"PARAM:\"{\n                                                            \"param1\": \"$.#order#Object\",\n                                                            \"param2\": \"$.#business#Array\"\n                                                            },\"TEMPLATE\":\"{\n                                                         \"response\": {\n                                                           \"code\": \"0000\",\n                                                           \"message\": \"成功\"\n                                                         }\n                                                       }\"}','','2018-11-14 13:28:45','0'),(2,'query.console.menu','查询菜单','manageId,menuGroup','1','{\n                                                 	\"param1\":\"select mm.m_id mId,mm.name name,mm.level level,mm.parent_id parentId,mm.menu_group menuGroup,mm.user_id userId,mm.create_time createTime,\n                                                              mm.remark remark,mmc.url url,mmc.template template\n                                                              from m_menu_2_user mm2u,m_menu mm, m_menu_ctg mmc\n                                                              where mm2u.user_id = #manageId#\n                                                              and mm2u.m_id = mm.m_id\n                                                              AND mm.menu_group = #menuGroup#\n                                                              and mm2u.status_cd = \'0\'\n                                                              and mm.status_cd = \'0\'\n                                                              and mmc.m_id = mm.m_id\n                                                              and mmc.status_cd = \'0\'\n                                                              order by mm.seq asc\"\n                                                 }','',NULL,'{\"PARAM\":{\n                                                            \"param1\": \"$.#menus#Array\"\n                                                            },\"TEMPLATE\":{\n                                                         \"response\": {\n                                                           \"code\": \"0000\",\n                                                           \"message\": \"成功\"\n                                                         }\n                                                       }}','','2018-11-14 13:28:45','0'),(3,'query.user.loginInfo','查询用户登录信息','userCode','1','{\n                                                 	\"param1\":\"SELECT \'10001\' userId,\'admin\' userName,\'d57167e07915c9428b1c3aae57003807\' userPwd FROM DUAL WHERE #userCode#=\'admin\'\"\n                                                 }','',NULL,'{\"PARAM\":{\n                                                            \"param1\": \"$.#user#Object\"\n                                                            },\"TEMPLATE\":{\n                                                         \"response\": {\n                                                           \"code\": \"0000\",\n                                                           \"message\": \"成功\"\n                                                         }\n                                                       }}','','2018-11-14 13:28:45','0'),(4,'query.console.template','查询模板信息','templateCode','1','{\n                                                 	\"param1\":\"SELECT t.template_code templateCode,t.name templateName,t.`html_name` htmlName,t.`url` templateUrl\n                                                              FROM c_template t WHERE t.status_cd = \'0\' AND t.template_code = #templateCode#\"\n                                                 }','',NULL,'{\"PARAM\":{\n                                                            \"param1\": \"$.#template#Object\"\n                                                            },\"TEMPLATE\":{\n                                                         \"response\": {\n                                                           \"code\": \"0000\",\n                                                           \"message\": \"成功\"\n                                                         }\n                                                       }}','','2018-11-14 13:28:45','0'),(5,'query.console.templateCol','查询模板列信息','templateCode','1','{\n                                                 	\"param1\":\"SELECT t.template_code templateCode,t.name templateName,tc.col_name colName,tc.col_model colModel FROM c_template t,c_template_col tc WHERE t.status_cd = \'0\' AND t.template_code = tc.template_code\n                                                              AND tc.status_cd = \'0\'\n                                                              AND t.template_code = #templateCode#\"\n                                                 }','',NULL,'{\"PARAM\":{\n                                                            \"param1\": \"$.#template#Array\"\n                                                            },\"TEMPLATE\":{\n                                                         \"response\": {\n                                                           \"code\": \"0000\",\n                                                           \"message\": \"成功\"\n                                                         }\n                                                       }}','','2018-11-14 13:28:45','0'),(6,'query.center.mapping','查询映射数据','page,rows,sord','1','{\"param1\":\"select count(1) records,ceil(#page#/#rows#)+1 page,ceil(count(1)/#rows#) total from c_mapping m where m.status_cd = \'0\'\",\n                                                             \"param2\":\"SELECT m.`id` id,m.`domain` domain,m.name name,m.`key`  `key` ,m.`value` `value`,m.`remark` remark FROM c_mapping m WHERE m.`status_cd` = \'0\' LIMIT #page#, #rows#\"\n                                                             }','',NULL,'{\n                                                        	\"PARAM\": {\n                                                        		\"param1\": \"$.##Object\",\n                                                        		\"param2\": \"$.#rows#Array\"\n                                                        	},\n                                                        	\"TEMPLATE\": {\n                                                        		\"response\": {\n                                                        			\"code\": \"0000\",\n                                                        			\"message\": \"成功\"\n                                                        		}\n                                                        	}\n                                                        }','','2018-11-14 13:28:45','0'),(7,'query.center.apps','查询外部应用','page,rows,sord','1','{\"param1\":\"select count(1) records,ceil(#page#/#rows#)+1 page,ceil(count(1)/#rows#) total from c_app a where a.status_cd = \'0\'\",\n                                                             \"param2\":\"SELECT m.`id` id,m.`app_id` appId,m.name `name`,m.`security_code`  securityCode ,m.`while_list_ip` whileListIp,m.`black_list_ip` blackListIp,m.`remark` remark FROM c_app m WHERE m.`status_cd` = \'0\' LIMIT #page#, #rows#\"\n                                                             }','',NULL,'{\n                                                        	\"PARAM\": {\n                                                        		\"param1\": \"$.##Object\",\n                                                        		\"param2\": \"$.#rows#Array\"\n                                                        	},\n                                                        	\"TEMPLATE\": {\n                                                        		\"response\": {\n                                                        			\"code\": \"0000\",\n                                                        			\"message\": \"成功\"\n                                                        		}\n                                                        	}\n                                                        }','','2018-11-14 13:28:45','0'),(8,'query.center.services','查询服务数据','page,rows,sord','1','{\"param1\":\"select count(1) records,ceil(#page#/#rows#)+1 page,ceil(count(1)/#rows#) total from c_service a where a.status_cd = \'0\'\",\n                                                             \"param2\":\"SELECT s.`service_id` serviceId,s.`service_code` serviceCode,s.`business_type_cd`  businessTypeCd,s.name `name`,s.is_instance isInstance,\n                                                                       s.`messageQueueName` messageQueueName,s.url url,s.`provide_app_id` provideAppId FROM c_service s WHERE s.`status_cd` = \'0\' LIMIT #page#, #rows#\"\n                                                             }','',NULL,'{\n                                                        	\"PARAM\": {\n                                                        		\"param1\": \"$.##Object\",\n                                                        		\"param2\": \"$.#rows#Array\"\n                                                        	},\n                                                        	\"TEMPLATE\": {\n                                                        		\"response\": {\n                                                        			\"code\": \"0000\",\n                                                        			\"message\": \"成功\"\n                                                        		}\n                                                        	}\n                                                        }','','2018-11-14 13:28:45','0'),(9,'query.center.route','查询路由数据','page,rows,sord','1','{\"param1\":\"select count(1) records,ceil(#page#/#rows#)+1 page,ceil(count(1)/#rows#) total from c_route a,c_service cs WHERE a.`service_id` = cs.`service_id` AND cs.`status_cd` = \'0\' and a.status_cd = \'0\'\",\n                                                             \"param2\":\"SELECT s.id id,s.`app_id` appId,s.`service_id` serviceId,s.`invoke_model` invokeModel,cs.`name` serviceName,cs.`service_code` serviceCode,s.`order_type_cd` orderTypeCd,s.`invoke_limit_times` invokelimitTimes FROM c_route s,c_service cs WHERE s.`service_id` = cs.`service_id` AND cs.`status_cd` = \'0\' AND s.`status_cd` = \'0\' LIMIT #page#, #rows#\"\n                                                             }','',NULL,'{\n                                                        	\"PARAM\": {\n                                                        		\"param1\": \"$.##Object\",\n                                                        		\"param2\": \"$.#rows#Array\"\n                                                        	},\n                                                        	\"TEMPLATE\": {\n                                                        		\"response\": {\n                                                        			\"code\": \"0000\",\n                                                        			\"message\": \"成功\"\n                                                        		}\n                                                        	}\n                                                        }','','2018-11-14 13:28:45','0'),(10,'query.console.caches','查询缓存数据','userId','1','{\n                                                             \"param1\":\"SELECT c.id id,c.`cache_code` cacheCode, c.`name` cacheName FROM c_cache c, c_cache_2_user c2u WHERE c.`cache_code` = c2u.`cache_code` AND c.`status_cd` = \'0\'\n                                                                       AND c2u.`status_cd` = \'0\' AND c2u.`user_id` = #userId# AND c.`group` = \'COMMON\' ORDER BY c.`seq` ASC\"\n                                                             }','',NULL,'{\n                                                        	\"PARAM\": {\n                                                        		\"param1\": \"$.#rows#Array\"\n                                                        	},\n                                                        	\"TEMPLATE\": {\n                                                        		\"response\": {\n                                                        			\"code\": \"0000\",\n                                                        			\"message\": \"成功\"\n                                                        		}\n                                                        	}\n                                                        }','','2018-11-14 13:28:45','0'),(11,'query.console.cache','查询单条缓存信息','cacheCode','1','{\n                                                             \"param1\":\"SELECT c.id id,c.`cache_code` cacheCode, c.`name` cacheName,c.`param` param,c.`service_code` serviceCode FROM c_cache c WHERE  c.`status_cd` = \'0\' AND c.`cache_code` = #cacheCode#\"\n                                                             }','',NULL,'{\n                                                        	\"PARAM\": {\n                                                        		\"param1\": \"$.#cache#Object\"\n                                                        	},\n                                                        	\"TEMPLATE\": {\n                                                        		\"response\": {\n                                                        			\"code\": \"0000\",\n                                                        			\"message\": \"成功\"\n                                                        		}\n                                                        	}\n                                                        }','','2018-11-14 13:28:45','0'),(12,'save.center.mapping','保存映射信息','domain,name,key,value,remark','1','{\n                                                             \"param1\":\"INSERT c_mapping(domain,`name`,`key`,`value`,remark) VALUES(#domain#,#name#,#key#,#value#,#remark#)\"\n                                                             }','',NULL,'{\n                                                        	\"TEMPLATE\": {\n                                                        		\"response\": {\n                                                        			\"code\": \"0000\",\n                                                        			\"message\": \"成功\"\n                                                        		}\n                                                        	}\n                                                        }','','2018-11-14 13:28:45','0'),(13,'delete.center.mapping','删除映射信息','id','1','{\n                                                             \"param1\":\"UPDATE c_mapping m SET m.status_cd = \'1\' WHERE m.status_cd = \'0\' AND m.id = #id#\"\n                                                             }','',NULL,'{\n                                                        	\"TEMPLATE\": {\n                                                        		\"response\": {\n                                                        			\"code\": \"0000\",\n                                                        			\"message\": \"成功\"\n                                                        		}\n                                                        	}\n                                                        }','','2018-11-14 13:28:45','0'),(14,'update.center.mapping','修改映射信息','id,domain,name,key,value,remark','1','{\n                                                             \"param1\":\"UPDATE c_mapping m SET m.domain=#domain#,m.name = #name#,m.key=#key#,m.value=#value#,m.remark=#remark# WHERE m.status_cd = \'0\' AND m.id = #id#\"\n                                                             }','',NULL,'{\n                                                        	\"TEMPLATE\": {\n                                                        		\"response\": {\n                                                        			\"code\": \"0000\",\n                                                        			\"message\": \"成功\"\n                                                        		}\n                                                        	}\n                                                        }','','2018-11-14 13:28:45','0');

/*Table structure for table `c_status` */

DROP TABLE IF EXISTS `c_status`;

CREATE TABLE `c_status` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `status_cd` varchar(4) NOT NULL COMMENT '状态',
  `name` varchar(50) NOT NULL COMMENT '名称',
  `description` varchar(200) DEFAULT NULL COMMENT '描述',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `status_cd` (`status_cd`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;

/*Data for the table `c_status` */

insert  into `c_status`(`id`,`status_cd`,`name`,`description`,`create_time`) values (1,'1','无效的，不在用的','无效的，不在用的','2018-11-14 13:28:44'),(2,'0','有效的，在用的','有效的，在用的','2018-11-14 13:28:44'),(3,'S','保存成功','保存成功','2018-11-14 13:28:44'),(4,'D','作废订单','作废订单','2018-11-14 13:28:44'),(5,'E','错误订单','错误订单','2018-11-14 13:28:44'),(6,'NE','通知错误订单','通知错误订单','2018-11-14 13:28:44'),(7,'C','订单完成','订单完成','2018-11-14 13:28:44'),(8,'B','Business过程','Business过程','2018-11-14 13:28:44'),(9,'I','Instance过程','Instance过程','2018-11-14 13:28:44');

/*Table structure for table `c_sub_comment` */

DROP TABLE IF EXISTS `c_sub_comment`;

CREATE TABLE `c_sub_comment` (
  `sub_comment_id` varchar(30) NOT NULL COMMENT '子评论ID',
  `comment_id` varchar(30) NOT NULL COMMENT '评论ID ',
  `b_id` varchar(30) NOT NULL COMMENT '订单ID',
  `parent_sub_comment_id` varchar(30) NOT NULL DEFAULT '-1' COMMENT '父 子评论ID 如果不是回复 写成-1',
  `sub_comment_type_cd` varchar(2) NOT NULL DEFAULT 'C' COMMENT '评论类型 C 评论 R 回复 A 追加',
  `comment_context` longtext NOT NULL COMMENT '评论内容',
  `month` int(11) NOT NULL COMMENT '月份',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `status_cd` varchar(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考c_status表，S 保存，0, 在用 1失效',
  KEY `idx_sub_comment_b_id` (`b_id`),
  KEY `idx_sub_comment_comment_id` (`comment_id`),
  KEY `idx_sub_comment_parent_sub_comment_id` (`parent_sub_comment_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8
/*!50100 PARTITION BY RANGE (`month`)
(PARTITION sub_comment_1 VALUES LESS THAN (2) ENGINE = InnoDB,
 PARTITION sub_comment_2 VALUES LESS THAN (3) ENGINE = InnoDB,
 PARTITION sub_comment_3 VALUES LESS THAN (4) ENGINE = InnoDB,
 PARTITION sub_comment_4 VALUES LESS THAN (5) ENGINE = InnoDB,
 PARTITION sub_comment_5 VALUES LESS THAN (6) ENGINE = InnoDB,
 PARTITION sub_comment_6 VALUES LESS THAN (7) ENGINE = InnoDB,
 PARTITION sub_comment_7 VALUES LESS THAN (8) ENGINE = InnoDB,
 PARTITION sub_comment_8 VALUES LESS THAN (9) ENGINE = InnoDB,
 PARTITION sub_comment_9 VALUES LESS THAN (10) ENGINE = InnoDB,
 PARTITION sub_comment_10 VALUES LESS THAN (11) ENGINE = InnoDB,
 PARTITION sub_comment_11 VALUES LESS THAN (12) ENGINE = InnoDB,
 PARTITION sub_comment_12 VALUES LESS THAN (13) ENGINE = InnoDB) */;

/*Data for the table `c_sub_comment` */

/*Table structure for table `c_sub_comment_attr` */

DROP TABLE IF EXISTS `c_sub_comment_attr`;

CREATE TABLE `c_sub_comment_attr` (
  `attr_id` varchar(30) NOT NULL COMMENT '属性id',
  `sub_comment_id` varchar(30) NOT NULL COMMENT '子评论ID',
  `b_id` varchar(30) NOT NULL COMMENT '订单ID',
  `spec_cd` varchar(12) NOT NULL COMMENT '规格id,参考spec表',
  `value` varchar(50) NOT NULL COMMENT '属性值',
  `month` int(11) NOT NULL COMMENT '月份',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `status_cd` varchar(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考c_status表，S 保存，0, 在用 1失效',
  KEY `idx_sub_comment_attr_b_id` (`b_id`),
  KEY `idx_sub_comment_attr_sub_comment_id` (`sub_comment_id`),
  KEY `idx_sub_comment_attr_spec_cd` (`spec_cd`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8
/*!50100 PARTITION BY RANGE (`month`)
(PARTITION sub_comment_attr_1 VALUES LESS THAN (2) ENGINE = InnoDB,
 PARTITION sub_comment_attr_2 VALUES LESS THAN (3) ENGINE = InnoDB,
 PARTITION sub_comment_attr_3 VALUES LESS THAN (4) ENGINE = InnoDB,
 PARTITION sub_comment_attr_4 VALUES LESS THAN (5) ENGINE = InnoDB,
 PARTITION sub_comment_attr_5 VALUES LESS THAN (6) ENGINE = InnoDB,
 PARTITION sub_comment_attr_6 VALUES LESS THAN (7) ENGINE = InnoDB,
 PARTITION sub_comment_attr_7 VALUES LESS THAN (8) ENGINE = InnoDB,
 PARTITION sub_comment_attr_8 VALUES LESS THAN (9) ENGINE = InnoDB,
 PARTITION sub_comment_attr_9 VALUES LESS THAN (10) ENGINE = InnoDB,
 PARTITION sub_comment_attr_10 VALUES LESS THAN (11) ENGINE = InnoDB,
 PARTITION sub_comment_attr_11 VALUES LESS THAN (12) ENGINE = InnoDB,
 PARTITION sub_comment_attr_12 VALUES LESS THAN (13) ENGINE = InnoDB) */;

/*Data for the table `c_sub_comment_attr` */

/*Table structure for table `c_sub_comment_photo` */

DROP TABLE IF EXISTS `c_sub_comment_photo`;

CREATE TABLE `c_sub_comment_photo` (
  `comment_photo_id` varchar(30) NOT NULL COMMENT '评论照片ID',
  `b_id` varchar(30) NOT NULL COMMENT '业务Id',
  `sub_comment_id` varchar(30) NOT NULL COMMENT '商店ID',
  `comment_photo_type_cd` varchar(12) NOT NULL DEFAULT 'S' COMMENT '评论照片类型,S 商品照片 M 商户ID',
  `photo` varchar(100) NOT NULL COMMENT '照片',
  `month` int(11) NOT NULL COMMENT '月份',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `status_cd` varchar(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考c_status表，S 保存，0, 在用 1失效',
  KEY `idx_sub_comment_photo_b_id` (`b_id`),
  KEY `idx_sub_comment_photo_sub_comment_id` (`sub_comment_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8
/*!50100 PARTITION BY RANGE (`month`)
(PARTITION sub_comment_photo_1 VALUES LESS THAN (2) ENGINE = InnoDB,
 PARTITION sub_comment_photo_2 VALUES LESS THAN (3) ENGINE = InnoDB,
 PARTITION sub_comment_photo_3 VALUES LESS THAN (4) ENGINE = InnoDB,
 PARTITION sub_comment_photo_4 VALUES LESS THAN (5) ENGINE = InnoDB,
 PARTITION sub_comment_photo_5 VALUES LESS THAN (6) ENGINE = InnoDB,
 PARTITION sub_comment_photo_6 VALUES LESS THAN (7) ENGINE = InnoDB,
 PARTITION sub_comment_photo_7 VALUES LESS THAN (8) ENGINE = InnoDB,
 PARTITION sub_comment_photo_8 VALUES LESS THAN (9) ENGINE = InnoDB,
 PARTITION sub_comment_photo_9 VALUES LESS THAN (10) ENGINE = InnoDB,
 PARTITION sub_comment_photo_10 VALUES LESS THAN (11) ENGINE = InnoDB,
 PARTITION sub_comment_photo_11 VALUES LESS THAN (12) ENGINE = InnoDB,
 PARTITION sub_comment_photo_12 VALUES LESS THAN (13) ENGINE = InnoDB) */;

/*Data for the table `c_sub_comment_photo` */

/*Table structure for table `c_template` */

DROP TABLE IF EXISTS `c_template`;

CREATE TABLE `c_template` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '模板ID',
  `template_code` varchar(20) NOT NULL COMMENT '模板编码 模板英文名',
  `name` varchar(50) NOT NULL COMMENT '模板名称',
  `html_name` varchar(20) NOT NULL COMMENT '对应HTML文件名称',
  `url` varchar(200) NOT NULL COMMENT '查询数据，修改数据url 其真实地址对应于mapping表中 LIST->key 对应 查询多条数据 QUERY->key 对应单条数据 UPDATE-> 对应修改数据 DELETE->key 对应删除数据 多条之间用 ; 分隔',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `status_cd` varchar(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考c_status表，0在用，1失效',
  PRIMARY KEY (`id`),
  UNIQUE KEY `template_code` (`template_code`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

/*Data for the table `c_template` */

insert  into `c_template`(`id`,`template_code`,`name`,`html_name`,`url`,`create_time`,`status_cd`) values (1,'mapping','映射管理','list_template','LIST->query.center.mapping;QUERY->mapping_query_url;INSERT->save.center.mapping;UPDATE->update.center.mapping;DELETE->delete.center.mapping','2018-11-14 13:28:51','0'),(2,'app','外部应用','list_template','LIST->query.center.apps;QUERY->query.center.app','2018-11-14 13:28:51','0'),(3,'service','服务管理','list_template','LIST->query.center.services;QUERY->query.center.service','2018-11-14 13:28:51','0'),(4,'route','路由管理','list_template','LIST->query.center.routes;QUERY->query.center.route','2018-11-14 13:28:51','0'),(5,'cache','刷新缓存','list_template_cache','LIST->query.center.caches;QUERY->query.center.cacheOne','2018-11-14 13:28:51','0');

/*Table structure for table `c_template_col` */

DROP TABLE IF EXISTS `c_template_col`;

CREATE TABLE `c_template_col` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '模板ID',
  `template_code` varchar(20) NOT NULL COMMENT '模板编码 模板英文名',
  `col_name` varchar(50) NOT NULL COMMENT '前台显示名称',
  `col_code` varchar(20) NOT NULL COMMENT '字段的编码',
  `col_model` longtext NOT NULL COMMENT 'jqgrid的colmodel',
  `seq` int(11) NOT NULL COMMENT '列顺序',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `status_cd` varchar(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考c_status表，0在用，1失效',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=38 DEFAULT CHARSET=utf8;

/*Data for the table `c_template_col` */

insert  into `c_template_col`(`id`,`template_code`,`col_name`,`col_code`,`col_model`,`seq`,`create_time`,`status_cd`) values (1,'mapping','列ID','id','{ \"name\": \"id\",\"index\": \"id\",\"width\": \"90\",\n                                                                                                             \"editable\": true,\n                                                                                                             \"sorttype\": \"int\" }',1,'2018-11-14 13:28:51','0'),(2,'mapping','域','domain','{ \"name\": \"domain\",\"index\": \"domain\",\"width\": \"90\",\n                                                                                                             \"editable\": true,\n                                                                                                             \"formatoptions\": { \"defaultValue\": \"DOMAIN.COMMON\" }\n                                                                                                           }',2,'2018-11-14 13:28:51','0'),(3,'mapping','名称','name','{ \"name\": \"name\",\"index\": \"name\",\"width\": \"90\",\n                                                                                                             \"editable\": true }',3,'2018-11-14 13:28:51','0'),(4,'mapping','键','key','{ \"name\": \"key\",\"index\": \"key\",\"width\": \"90\",\n                                                                                                             \"editable\": true }',4,'2018-11-14 13:28:51','0'),(5,'mapping','值','value','{ \"name\": \"value\",\"index\": \"value\",\"width\": \"90\",\n                                                                                                             \"editable\": true }',5,'2018-11-14 13:28:51','0'),(6,'mapping','备注','value','{ \"name\": \"remark\",\"index\": \"remark\",\"width\": \"90\",\n                                                                                                             \"editable\": true }',6,'2018-11-14 13:28:51','0'),(7,'mapping','BUTTON','BUTTON','{\n                                                                                                            \"name\": \"detail\",\n                                                                                                            \"index\": \"\",\n                                                                                                            \"width\": \"40\",\n                                                                                                            \"fixed\": \"true\",\n                                                                                                            \"sortable\": \"false\",\n                                                                                                            \"resize\": \"false\",\n                                                                                                            \"formatter\": \"function(cellvalue, options, rowObject){\n var temp =\"<div style=\'margin-left:8px;\'><div title=\'详情记录\' style=\'float:left;cursor:pointer;\' class=\'ui-pg-div\' id=\'jEditButton_3\' onclick=\'detail(\"+rowObject+\")\' onmouseover=\'jQuery(this).addClass(\'ui-state-hover\');\' onmouseout=\'jQuery(this).removeClass(\'ui-state-hover\');\'><span class=\'ui-icon fa-search-plus\'/></div></div>\";\n return temp; \n}\"\n                                                                                                          }',7,'2018-11-14 13:28:51','0'),(8,'app','列ID','id','{ \"name\": \"id\",\"index\": \"id\",\"width\": \"20\",\n                                                                                                             \"editable\": true,\n                                                                                                             \"sorttype\": \"int\" }',1,'2018-11-14 13:28:51','0'),(9,'app','AppId','domain','{ \"name\": \"appId\",\"index\": \"appId\",\"width\": \"40\",\n                                                                                                             \"editable\": true\n                                                                                                           }',2,'2018-11-14 13:28:51','0'),(10,'app','名称','name','{ \"name\": \"name\",\"index\": \"name\",\"width\": \"50\",\n                                                                                                             \"editable\": true }',3,'2018-11-14 13:28:51','0'),(11,'app','秘钥','securityCode','{ \"name\": \"securityCode\",\"index\": \"securityCode\",\"width\": \"50\",\n                                                                                                             \"editable\": true }',4,'2018-11-14 13:28:51','0'),(12,'app','白名单','whileListIp','{ \"name\": \"whileListIp\",\"index\": \"whileListIp\",\"width\": \"90\",\n                                                                                                             \"editable\": true }',5,'2018-11-14 13:28:51','0'),(13,'app','黑名单','blackListIp','{ \"name\": \"blackListIp\",\"index\": \"blackListIp\",\"width\": \"40\",\n                                                                                                             \"editable\": true }',6,'2018-11-14 13:28:51','0'),(14,'app','备注','value','{ \"name\": \"remark\",\"index\": \"remark\",\"width\": \"90\",\n                                                                                                             \"editable\": true }',7,'2018-11-14 13:28:51','0'),(15,'app','BUTTON','BUTTON','{\n                                                                                                            \"name\": \"detail\",\n                                                                                                            \"index\": \"\",\n                                                                                                            \"width\": \"40\",\n                                                                                                            \"fixed\": \"true\",\n                                                                                                            \"sortable\": \"false\",\n                                                                                                            \"resize\": \"false\",\n                                                                                                            \"formatter\": \"function(cellvalue, options, rowObject){\n var temp =\"<div style=\'margin-left:8px;\'><div title=\'详情记录\' style=\'float:left;cursor:pointer;\' class=\'ui-pg-div\' id=\'jEditButton_3\' onclick=\'detail(\"+rowObject+\")\' onmouseover=\'jQuery(this).addClass(\'ui-state-hover\');\' onmouseout=\'jQuery(this).removeClass(\'ui-state-hover\');\'><span class=\'ui-icon fa-search-plus\'/></div></div>\";\n return temp; \n}\"\n                                                                                                          }',8,'2018-11-14 13:28:51','0'),(16,'service','服务ID','serviceId','{ \"name\": \"serviceId\",\"index\": \"serviceId\",\"width\": \"20\",\n                                                                                                             \"editable\": true,\n                                                                                                             \"sorttype\": \"int\" }',1,'2018-11-14 13:28:51','0'),(17,'service','服务编码','serviceCode','{ \"name\": \"serviceCode\",\"index\": \"serviceCode\",\"width\": \"40\",\n                                                                                                             \"editable\": true\n                                                                                                           }',2,'2018-11-14 13:28:51','0'),(18,'service','业务类型','businessTypeCd','{ \"name\": \"businessTypeCd\",\"index\": \"businessTypeCd\",\"width\": \"50\",\n                                                                                                             \"editable\": true }',3,'2018-11-14 13:28:51','0'),(19,'service','名称','name','{ \"name\": \"name\",\"index\": \"name\",\"width\": \"40\",\n                                                                                                             \"editable\": true }',4,'2018-11-14 13:28:51','0'),(20,'service','消息队列','messageQueueName','{ \"name\": \"messageQueueName\",\"index\": \"messageQueueName\",\"width\": \"10\",\n                                                                                                             \"editable\": true }',5,'2018-11-14 13:28:51','0'),(21,'service','需要Instance','isInstance','{ \"name\": \"isInstance\",\"index\": \"isInstance\",\"width\": \"10\",\n                                                                                                             \"editable\": true }',6,'2018-11-14 13:28:51','0'),(22,'service','URL','url','{ \"name\": \"url\",\"index\": \"url\",\"width\": \"60\",\n                                                                                                             \"editable\": true }',7,'2018-11-14 13:28:51','0'),(23,'service','提供者AppId','provideAppId','{ \"name\": \"provideAppId\",\"index\": \"provideAppId\",\"width\": \"10\",\n                                                                                                             \"editable\": true }',8,'2018-11-14 13:28:51','0'),(24,'service','BUTTON','BUTTON','{\n                                                                                                            \"name\": \"detail\",\n                                                                                                            \"index\": \"\",\n                                                                                                            \"width\": \"40\",\n                                                                                                            \"fixed\": \"true\",\n                                                                                                            \"sortable\": \"false\",\n                                                                                                            \"resize\": \"false\",\n                                                                                                            \"formatter\": \"function(cellvalue, options, rowObject){\n var temp =\"<div style=\'margin-left:8px;\'><div title=\'详情记录\' style=\'float:left;cursor:pointer;\' class=\'ui-pg-div\' id=\'jEditButton_3\' onclick=\'detail(\"+rowObject+\")\' onmouseover=\'jQuery(this).addClass(\'ui-state-hover\');\' onmouseout=\'jQuery(this).removeClass(\'ui-state-hover\');\'><span class=\'ui-icon fa-search-plus\'/></div></div>\";\n return temp; \n}\"\n                                                                                                          }',9,'2018-11-14 13:28:51','0'),(25,'route','路由ID','id','{ \"name\": \"id\",\"index\": \"id\",\"width\": \"10\",\n                                                                                                             \"editable\": true,\n                                                                                                             \"sorttype\": \"int\" }',1,'2018-11-14 13:28:51','0'),(26,'route','AppId','appId','{ \"name\": \"appId\",\"index\": \"appId\",\"width\": \"30\",\n                                                                                                             \"editable\": true\n                                                                                                           }',2,'2018-11-14 13:28:51','0'),(27,'route','服务ID','serviceId','{ \"name\": \"serviceId\",\"index\": \"serviceId\",\"width\": \"30\",\n                                                                                                             \"editable\": true }',3,'2018-11-14 13:28:51','0'),(28,'route','调用方式','invokeModel','{ \"name\": \"invokeModel\",\"index\": \"invokeModel\",\"width\": \"50\",\n                                                                                                              \"editable\": true }',4,'2018-11-14 13:28:51','0'),(29,'route','服务名称','serviceName','{ \"name\": \"serviceName\",\"index\": \"serviceName\",\"width\": \"30\",\n                                                                                                             \"editable\": true }',5,'2018-11-14 13:28:51','0'),(30,'route','服务编码','serviceCode','{ \"name\": \"serviceCode\",\"index\": \"serviceCode\",\"width\": \"30\",\n                                                                                                             \"editable\": true }',6,'2018-11-14 13:28:51','0'),(31,'route','订单类型','orderTypeCd','{ \"name\": \"orderTypeCd\",\"index\": \"orderTypeCd\",\"width\": \"30\",\n                                                                                                             \"editable\": true }',7,'2018-11-14 13:28:51','0'),(32,'route','调用次数限制','invokelimitTimes','{ \"name\": \"invokelimitTimes\",\"index\": \"invokelimitTimes\",\"width\": \"40\",\n                                                                                                             \"editable\": true }',8,'2018-11-14 13:28:51','0'),(33,'route','BUTTON','BUTTON','{\n                                                                                                            \"name\": \"detail\",\n                                                                                                            \"index\": \"\",\n                                                                                                            \"width\": \"40\",\n                                                                                                            \"fixed\": \"true\",\n                                                                                                            \"sortable\": \"false\",\n                                                                                                            \"resize\": \"false\",\n                                                                                                            \"formatter\": \"function(cellvalue, options, rowObject){\n var temp =\"<div style=\'margin-left:8px;\'><div title=\'详情记录\' style=\'float:left;cursor:pointer;\' class=\'ui-pg-div\' id=\'jEditButton_3\' onclick=\'detail(\"+rowObject+\")\' onmouseover=\'jQuery(this).addClass(\'ui-state-hover\');\' onmouseout=\'jQuery(this).removeClass(\'ui-state-hover\');\'><span class=\'ui-icon fa-search-plus\'/></div></div>\";\n return temp; \n}\"}',9,'2018-11-14 13:28:51','0'),(34,'cache','缓存ID','id','{ \"name\": \"id\",\"index\": \"id\",\"width\": \"10\",\n                                                                                                             \"editable\": true,\n                                                                                                             \"sorttype\": \"int\" }',1,'2018-11-14 13:28:51','0'),(35,'cache','缓存编码','cacheCode','{ \"name\": \"cacheCode\",\"index\": \"cacheCode\",\"width\": \"30\",\n                                                                                                             \"editable\": true }',2,'2018-11-14 13:28:51','0'),(36,'cache','缓存名称','cacheName','{ \"name\": \"cacheName\",\"index\": \"cacheName\",\"width\": \"30\",\n                                                                                                             \"editable\": true }',3,'2018-11-14 13:28:51','0'),(37,'cache','BUTTON','BUTTON','{\n                                                                                                            \"name\": \"detail\",\n                                                                                                            \"index\": \"\",\n                                                                                                            \"width\": \"40\",\n                                                                                                            \"fixed\": \"true\",\n                                                                                                            \"sortable\": \"false\",\n                                                                                                            \"resize\": \"false\",\n                                                                                                            \"formatter\": \"\"function(cellvalue, options, rowObject){ var temp =\"<div style=\'margin-left:8px;\'><button type=\'button\' class=\'btn btn-warning\' style=\'border-radius: .25rem;\' onclick=\'flush(this,\"+rowObject.cacheCode+\")\'>刷新缓存</button></div>\";return temp; }\"\n                                                                                                          }',4,'2018-11-14 13:28:51','0');

/*Table structure for table `credentials` */

DROP TABLE IF EXISTS `credentials`;

CREATE TABLE `credentials` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `credentials_cd` varchar(12) NOT NULL COMMENT '证件编码',
  `name` varchar(50) NOT NULL COMMENT '证件名称',
  `description` varchar(200) DEFAULT NULL COMMENT '描述',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `credentials` */

/*Table structure for table `l_transaction_log` */

DROP TABLE IF EXISTS `l_transaction_log`;

CREATE TABLE `l_transaction_log` (
  `log_id` varchar(30) NOT NULL COMMENT 'id',
  `transaction_id` varchar(30) NOT NULL COMMENT '外部交易流水',
  `contract_id` varchar(64) NOT NULL COMMENT '上下文ID',
  `ip` varchar(20) NOT NULL COMMENT '日志产生主机IP',
  `port` varchar(10) NOT NULL COMMENT '日志产生端口',
  `src_ip` varchar(20) DEFAULT NULL COMMENT '调用方IP',
  `src_port` varchar(10) DEFAULT NULL COMMENT '调用方端口',
  `app_id` varchar(30) NOT NULL COMMENT '调用方应用ID',
  `user_id` varchar(30) DEFAULT NULL COMMENT '用户ID',
  `service_code` varchar(50) DEFAULT NULL COMMENT '服务编码',
  `service_name` varchar(50) DEFAULT NULL COMMENT '服务名称',
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '日志交互时间，时间戳',
  `cost_time` int(11) NOT NULL DEFAULT '0' COMMENT '耗时',
  `status_cd` varchar(2) NOT NULL COMMENT '交互状态 S 成功 F 失败',
  `month` int(11) NOT NULL COMMENT '月份',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  UNIQUE KEY `log_id` (`log_id`,`month`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8
/*!50100 PARTITION BY RANGE (month)
(PARTITION transaction_log_1 VALUES LESS THAN (2) ENGINE = InnoDB,
 PARTITION transaction_log_2 VALUES LESS THAN (3) ENGINE = InnoDB,
 PARTITION transaction_log_3 VALUES LESS THAN (4) ENGINE = InnoDB,
 PARTITION transaction_log_4 VALUES LESS THAN (5) ENGINE = InnoDB,
 PARTITION transaction_log_5 VALUES LESS THAN (6) ENGINE = InnoDB,
 PARTITION transaction_log_6 VALUES LESS THAN (7) ENGINE = InnoDB,
 PARTITION transaction_log_7 VALUES LESS THAN (8) ENGINE = InnoDB,
 PARTITION transaction_log_8 VALUES LESS THAN (9) ENGINE = InnoDB,
 PARTITION transaction_log_9 VALUES LESS THAN (10) ENGINE = InnoDB,
 PARTITION transaction_log_10 VALUES LESS THAN (11) ENGINE = InnoDB,
 PARTITION transaction_log_11 VALUES LESS THAN (12) ENGINE = InnoDB,
 PARTITION transaction_log_12 VALUES LESS THAN (13) ENGINE = InnoDB) */;

/*Data for the table `l_transaction_log` */

/*Table structure for table `l_transaction_log_message` */

DROP TABLE IF EXISTS `l_transaction_log_message`;

CREATE TABLE `l_transaction_log_message` (
  `log_id` varchar(30) NOT NULL COMMENT 'id',
  `request_header` longtext COMMENT '请求头信息',
  `response_header` longtext COMMENT '返回头信息',
  `request_message` longtext COMMENT '请求报文',
  `response_message` longtext COMMENT '返回报文',
  `remark` varchar(200) DEFAULT NULL COMMENT '备注',
  `month` int(11) NOT NULL COMMENT '月份',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  UNIQUE KEY `log_id` (`log_id`,`month`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8
/*!50100 PARTITION BY RANGE (month)
(PARTITION transaction_log_message_1 VALUES LESS THAN (2) ENGINE = InnoDB,
 PARTITION transaction_log_message_2 VALUES LESS THAN (3) ENGINE = InnoDB,
 PARTITION transaction_log_message_3 VALUES LESS THAN (4) ENGINE = InnoDB,
 PARTITION transaction_log_message_4 VALUES LESS THAN (5) ENGINE = InnoDB,
 PARTITION transaction_log_message_5 VALUES LESS THAN (6) ENGINE = InnoDB,
 PARTITION transaction_log_message_6 VALUES LESS THAN (7) ENGINE = InnoDB,
 PARTITION transaction_log_message_7 VALUES LESS THAN (8) ENGINE = InnoDB,
 PARTITION transaction_log_message_8 VALUES LESS THAN (9) ENGINE = InnoDB,
 PARTITION transaction_log_message_9 VALUES LESS THAN (10) ENGINE = InnoDB,
 PARTITION transaction_log_message_10 VALUES LESS THAN (11) ENGINE = InnoDB,
 PARTITION transaction_log_message_11 VALUES LESS THAN (12) ENGINE = InnoDB,
 PARTITION transaction_log_message_12 VALUES LESS THAN (13) ENGINE = InnoDB) */;

/*Data for the table `l_transaction_log_message` */

/*Table structure for table `m_menu` */

DROP TABLE IF EXISTS `m_menu`;

CREATE TABLE `m_menu` (
  `m_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '菜单ID',
  `name` varchar(10) NOT NULL COMMENT '菜单名称',
  `level` varchar(2) NOT NULL COMMENT '菜单级别 一级菜单 为 1 二级菜单为2',
  `parent_id` int(11) NOT NULL COMMENT '父类菜单id 如果是一类菜单则写为-1 如果是二类菜单则写父类的菜单id',
  `menu_group` varchar(10) NOT NULL COMMENT '菜单组 left 显示在页面左边的菜单',
  `user_id` varchar(12) NOT NULL COMMENT '创建菜单的用户id',
  `remark` varchar(200) DEFAULT NULL COMMENT '描述',
  `seq` int(11) NOT NULL COMMENT '列顺序',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `status_cd` varchar(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考c_status表，0在用，1失效',
  PRIMARY KEY (`m_id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

/*Data for the table `m_menu` */

insert  into `m_menu`(`m_id`,`name`,`level`,`parent_id`,`menu_group`,`user_id`,`remark`,`seq`,`create_time`,`status_cd`) values (1,'系统配置','1',-1,'LEFT','10001','',1,'2018-11-14 13:28:51','0'),(2,'映射管理','2',1,'LEFT','10001','',2,'2018-11-14 13:28:51','0'),(3,'外部应用','2',1,'LEFT','10001','',3,'2018-11-14 13:28:51','0'),(4,'路由管理','2',1,'LEFT','10001','',4,'2018-11-14 13:28:51','0'),(5,'服务管理','2',1,'LEFT','10001','',5,'2018-11-14 13:28:51','0'),(6,'缓存管理','1',-1,'LEFT','10001','',1,'2018-11-14 13:28:51','0'),(7,'刷新缓存','2',1,'LEFT','10001','',2,'2018-11-14 13:28:51','0');

/*Table structure for table `m_menu_2_user` */

DROP TABLE IF EXISTS `m_menu_2_user`;

CREATE TABLE `m_menu_2_user` (
  `m_user_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '菜单用户ID',
  `m_id` int(11) NOT NULL COMMENT '菜单id',
  `user_id` varchar(30) NOT NULL COMMENT '用户id',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `status_cd` varchar(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考c_status表，0在用，1失效',
  PRIMARY KEY (`m_user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

/*Data for the table `m_menu_2_user` */

insert  into `m_menu_2_user`(`m_user_id`,`m_id`,`user_id`,`create_time`,`status_cd`) values (1,1,'10001','2018-11-14 13:28:51','0'),(2,2,'10001','2018-11-14 13:28:51','0'),(3,3,'10001','2018-11-14 13:28:51','0'),(4,4,'10001','2018-11-14 13:28:51','0'),(5,5,'10001','2018-11-14 13:28:51','0'),(6,6,'10001','2018-11-14 13:28:51','0'),(7,7,'10001','2018-11-14 13:28:51','0');

/*Table structure for table `m_menu_ctg` */

DROP TABLE IF EXISTS `m_menu_ctg`;

CREATE TABLE `m_menu_ctg` (
  `m_ctg_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '菜单配置ID',
  `m_id` int(11) NOT NULL COMMENT '菜单ID',
  `url` varchar(100) NOT NULL COMMENT '菜单打开地址',
  `template` varchar(50) DEFAULT NULL COMMENT '页面模板 模板名称',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `status_cd` varchar(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考c_status表，0在用，1失效',
  PRIMARY KEY (`m_ctg_id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

/*Data for the table `m_menu_ctg` */

insert  into `m_menu_ctg`(`m_ctg_id`,`m_id`,`url`,`template`,`create_time`,`status_cd`) values (1,1,'#','','2018-11-14 13:28:51','0'),(2,2,'/console/list?templateCode=mapping','','2018-11-14 13:28:51','0'),(3,3,'/console/list?templateCode=app','','2018-11-14 13:28:51','0'),(4,4,'/console/list?templateCode=service','','2018-11-14 13:28:51','0'),(5,5,'/console/list?templateCode=route','','2018-11-14 13:28:51','0'),(6,6,'#','','2018-11-14 13:28:51','0'),(7,7,'/','','2018-11-14 13:28:51','0');

/*Table structure for table `s_buy_shop` */

DROP TABLE IF EXISTS `s_buy_shop`;

CREATE TABLE `s_buy_shop` (
  `buy_id` varchar(30) NOT NULL COMMENT '购买ID',
  `b_id` varchar(30) NOT NULL COMMENT '业务Id',
  `shop_id` varchar(30) NOT NULL COMMENT '商品ID',
  `buy_count` decimal(10,0) NOT NULL DEFAULT '1' COMMENT '购买商品数',
  `month` int(11) NOT NULL COMMENT '月份',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `status_cd` varchar(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考c_status表，S 保存，0, 在用 1失效',
  UNIQUE KEY `buy_id` (`buy_id`,`month`),
  KEY `idx_buy_shop_b_id` (`b_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8
/*!50100 PARTITION BY RANGE (`month`)
(PARTITION buy_shop_1 VALUES LESS THAN (2) ENGINE = InnoDB,
 PARTITION buy_shop_2 VALUES LESS THAN (3) ENGINE = InnoDB,
 PARTITION buy_shop_3 VALUES LESS THAN (4) ENGINE = InnoDB,
 PARTITION buy_shop_4 VALUES LESS THAN (5) ENGINE = InnoDB,
 PARTITION buy_shop_5 VALUES LESS THAN (6) ENGINE = InnoDB,
 PARTITION buy_shop_6 VALUES LESS THAN (7) ENGINE = InnoDB,
 PARTITION buy_shop_7 VALUES LESS THAN (8) ENGINE = InnoDB,
 PARTITION buy_shop_8 VALUES LESS THAN (9) ENGINE = InnoDB,
 PARTITION buy_shop_9 VALUES LESS THAN (10) ENGINE = InnoDB,
 PARTITION buy_shop_10 VALUES LESS THAN (11) ENGINE = InnoDB,
 PARTITION buy_shop_11 VALUES LESS THAN (12) ENGINE = InnoDB,
 PARTITION buy_shop_12 VALUES LESS THAN (13) ENGINE = InnoDB) */;

/*Data for the table `s_buy_shop` */

/*Table structure for table `s_buy_shop_attr` */

DROP TABLE IF EXISTS `s_buy_shop_attr`;

CREATE TABLE `s_buy_shop_attr` (
  `b_id` varchar(30) NOT NULL COMMENT '订单ID',
  `attr_id` varchar(30) NOT NULL COMMENT '属性id',
  `buy_id` varchar(30) NOT NULL COMMENT '购买ID buy_id',
  `spec_cd` varchar(12) NOT NULL COMMENT '规格id,参考spec表',
  `value` varchar(50) NOT NULL COMMENT '属性值',
  `month` int(11) NOT NULL COMMENT '月份',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `status_cd` varchar(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考c_status表，S 保存，0, 在用 1失效',
  UNIQUE KEY `attr_id` (`attr_id`,`month`),
  KEY `idx_buy_shop_attr_b_id` (`b_id`),
  KEY `idx_buy_shop_attr_buy_id` (`buy_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8
/*!50100 PARTITION BY RANGE (`month`)
(PARTITION buy_shop_attr_1 VALUES LESS THAN (2) ENGINE = InnoDB,
 PARTITION buy_shop_attr_2 VALUES LESS THAN (3) ENGINE = InnoDB,
 PARTITION buy_shop_attr_3 VALUES LESS THAN (4) ENGINE = InnoDB,
 PARTITION buy_shop_attr_4 VALUES LESS THAN (5) ENGINE = InnoDB,
 PARTITION buy_shop_attr_5 VALUES LESS THAN (6) ENGINE = InnoDB,
 PARTITION buy_shop_attr_6 VALUES LESS THAN (7) ENGINE = InnoDB,
 PARTITION buy_shop_attr_7 VALUES LESS THAN (8) ENGINE = InnoDB,
 PARTITION buy_shop_attr_8 VALUES LESS THAN (9) ENGINE = InnoDB,
 PARTITION buy_shop_attr_9 VALUES LESS THAN (10) ENGINE = InnoDB,
 PARTITION buy_shop_attr_10 VALUES LESS THAN (11) ENGINE = InnoDB,
 PARTITION buy_shop_attr_11 VALUES LESS THAN (12) ENGINE = InnoDB,
 PARTITION buy_shop_attr_12 VALUES LESS THAN (13) ENGINE = InnoDB) */;

/*Data for the table `s_buy_shop_attr` */

/*Table structure for table `s_shop` */

DROP TABLE IF EXISTS `s_shop`;

CREATE TABLE `s_shop` (
  `shop_id` varchar(30) NOT NULL COMMENT '商品ID',
  `b_id` varchar(30) NOT NULL COMMENT '业务Id',
  `store_id` varchar(30) NOT NULL COMMENT '商店ID',
  `catalog_id` varchar(30) NOT NULL COMMENT '目录ID',
  `name` varchar(100) NOT NULL COMMENT '商品名称',
  `hot_buy` varchar(2) NOT NULL DEFAULT 'N' COMMENT '是否热卖 Y是 N否',
  `sale_price` decimal(10,2) NOT NULL COMMENT '商品销售价,再没有打折情况下显示',
  `open_shop_count` varchar(2) NOT NULL DEFAULT 'N' COMMENT '是否开启库存管理，默认N Y开启，N关闭，开启后界面显示数量，如果为0 则下架',
  `shop_count` decimal(10,0) NOT NULL DEFAULT '10000' COMMENT '商品库存',
  `start_date` date NOT NULL COMMENT '商品开始时间',
  `end_date` date NOT NULL COMMENT '商品结束时间',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `status_cd` varchar(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考c_status表，S 保存，0, 在用 1失效',
  UNIQUE KEY `shop_id` (`shop_id`),
  UNIQUE KEY `idx_shop_shop_id` (`shop_id`),
  KEY `idx_store_b_id` (`b_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `s_shop` */

/*Table structure for table `s_shop_attr` */

DROP TABLE IF EXISTS `s_shop_attr`;

CREATE TABLE `s_shop_attr` (
  `b_id` varchar(30) NOT NULL COMMENT '订单ID',
  `attr_id` varchar(30) NOT NULL COMMENT '属性id',
  `shop_id` varchar(30) NOT NULL COMMENT '商品ID',
  `spec_cd` varchar(12) NOT NULL COMMENT '规格id,参考spec表',
  `value` varchar(50) NOT NULL COMMENT '属性值',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `status_cd` varchar(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考c_status表，S 保存，0, 在用 1失效',
  UNIQUE KEY `attr_id` (`attr_id`),
  KEY `idx_store_attr_b_id` (`b_id`),
  KEY `idx_shop_attr_shop_id` (`shop_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `s_shop_attr` */

/*Table structure for table `s_shop_attr_param` */

DROP TABLE IF EXISTS `s_shop_attr_param`;

CREATE TABLE `s_shop_attr_param` (
  `attr_param_id` varchar(30) NOT NULL COMMENT '商品属性参数ID',
  `b_id` varchar(30) NOT NULL COMMENT '订单ID',
  `shop_id` varchar(30) NOT NULL COMMENT '商品ID',
  `spec_cd` varchar(12) NOT NULL COMMENT '规格id,参考spec表',
  `param` varchar(50) NOT NULL COMMENT '参数值',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `status_cd` varchar(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考c_status表，S 保存，0, 在用 1失效',
  UNIQUE KEY `attr_param_id` (`attr_param_id`),
  KEY `idx_shop_attr_param_b_id` (`b_id`),
  KEY `idx_shop_attr_param_shop_id` (`shop_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `s_shop_attr_param` */

/*Table structure for table `s_shop_catalog` */

DROP TABLE IF EXISTS `s_shop_catalog`;

CREATE TABLE `s_shop_catalog` (
  `catalog_id` varchar(30) NOT NULL COMMENT '目录ID',
  `b_id` varchar(30) NOT NULL COMMENT '业务Id',
  `store_id` varchar(30) NOT NULL COMMENT '商店ID',
  `name` varchar(100) NOT NULL COMMENT '目录名称',
  `level` varchar(2) NOT NULL COMMENT '目录等级',
  `parent_catalog_id` varchar(30) NOT NULL DEFAULT '-1' COMMENT '父目录ID，一级目录则写-1',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `status_cd` varchar(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考c_status表，S 保存，0, 在用 1失效',
  UNIQUE KEY `catalog_id` (`catalog_id`),
  KEY `idx_shop_catalog_b_id` (`b_id`),
  KEY `idx_shop_catalog_store_id` (`store_id`),
  KEY `idx_shop_catalog_catalog_id` (`catalog_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `s_shop_catalog` */

/*Table structure for table `s_shop_desc` */

DROP TABLE IF EXISTS `s_shop_desc`;

CREATE TABLE `s_shop_desc` (
  `shop_desc_id` varchar(30) NOT NULL COMMENT '商品描述ID',
  `shop_id` varchar(30) NOT NULL COMMENT '商品ID',
  `b_id` varchar(30) NOT NULL COMMENT '业务Id',
  `shop_describe` longtext NOT NULL COMMENT '商品描述',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `status_cd` varchar(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考c_status表，S 保存，0, 在用 1失效',
  UNIQUE KEY `shop_desc_id` (`shop_desc_id`),
  KEY `idx_shop_desc_b_id` (`b_id`),
  KEY `idx_shop_desc_shop_id` (`shop_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `s_shop_desc` */

/*Table structure for table `s_shop_photo` */

DROP TABLE IF EXISTS `s_shop_photo`;

CREATE TABLE `s_shop_photo` (
  `shop_photo_id` varchar(30) NOT NULL COMMENT '商品照片ID',
  `b_id` varchar(30) NOT NULL COMMENT '业务Id',
  `shop_id` varchar(30) NOT NULL COMMENT '商品ID',
  `shop_photo_type_cd` varchar(12) NOT NULL COMMENT '商品照片类型,L logo O 其他照片',
  `photo` varchar(100) NOT NULL COMMENT '照片',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `status_cd` varchar(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考c_status表，S 保存，0, 在用 1失效',
  UNIQUE KEY `shop_photo_id` (`shop_photo_id`),
  KEY `idx_shop_photo_b_id` (`b_id`),
  KEY `idx_shop_photo_shop_id` (`shop_id`),
  KEY `idx_shop_photo_shop_photo_id` (`shop_photo_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `s_shop_photo` */

/*Table structure for table `s_shop_preferential` */

DROP TABLE IF EXISTS `s_shop_preferential`;

CREATE TABLE `s_shop_preferential` (
  `shop_preferential_id` varchar(30) NOT NULL COMMENT '商品ID',
  `shop_id` varchar(30) NOT NULL COMMENT '商品ID',
  `b_id` varchar(30) NOT NULL COMMENT '业务Id',
  `original_price` decimal(10,2) NOT NULL COMMENT '商品销售价，再没有优惠的情况下和售价是一致的',
  `discount_rate` decimal(3,2) NOT NULL DEFAULT '1.00' COMMENT '商品打折率',
  `show_original_price` varchar(2) NOT NULL DEFAULT 'N' COMMENT '是否显示原价，Y显示，N 不显示',
  `preferential_start_date` date NOT NULL COMMENT '商品优惠开始时间',
  `preferential_end_date` date NOT NULL COMMENT '商品优惠结束时间',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `status_cd` varchar(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考c_status表，S 保存，0, 在用 1失效',
  UNIQUE KEY `shop_preferential_id` (`shop_preferential_id`),
  KEY `idx_shop_preferential_b_id` (`b_id`),
  KEY `idx_shop_preferential_shop_id` (`shop_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `s_shop_preferential` */

/*Table structure for table `s_store` */

DROP TABLE IF EXISTS `s_store`;

CREATE TABLE `s_store` (
  `store_id` varchar(30) NOT NULL COMMENT '商店ID',
  `b_id` varchar(30) NOT NULL COMMENT '业务Id',
  `user_id` varchar(30) NOT NULL COMMENT '用户ID',
  `name` varchar(100) NOT NULL COMMENT '店铺名称',
  `address` varchar(200) NOT NULL COMMENT '店铺地址',
  `tel` varchar(11) NOT NULL COMMENT '电话',
  `store_type_cd` varchar(10) NOT NULL COMMENT '店铺种类',
  `nearby_landmarks` varchar(200) DEFAULT NULL COMMENT '地标，如王府井北60米',
  `map_x` varchar(20) NOT NULL COMMENT '地区 x坐标',
  `map_y` varchar(20) NOT NULL COMMENT '地区 y坐标',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `status_cd` varchar(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考c_status表，S 保存，0, 在用 1失效',
  UNIQUE KEY `store_id` (`store_id`),
  UNIQUE KEY `idx_store_store_id` (`store_id`),
  KEY `idx_store_b_id` (`b_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `s_store` */

/*Table structure for table `s_store_attr` */

DROP TABLE IF EXISTS `s_store_attr`;

CREATE TABLE `s_store_attr` (
  `b_id` varchar(30) NOT NULL COMMENT '订单ID',
  `attr_id` varchar(30) NOT NULL COMMENT '属性id',
  `store_id` varchar(30) NOT NULL COMMENT '用户ID',
  `spec_cd` varchar(12) NOT NULL COMMENT '规格id,参考spec表',
  `VALUE` varchar(50) NOT NULL COMMENT '属性值',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `status_cd` varchar(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考c_status表，S 保存，0, 在用 1失效',
  UNIQUE KEY `attr_id` (`attr_id`),
  KEY `idx_store_attr_b_id` (`b_id`),
  KEY `idx_store_attr_store_id` (`store_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `s_store_attr` */

/*Table structure for table `s_store_cerdentials` */

DROP TABLE IF EXISTS `s_store_cerdentials`;

CREATE TABLE `s_store_cerdentials` (
  `store_cerdentials_id` varchar(30) NOT NULL COMMENT '商户证件ID',
  `b_id` varchar(30) NOT NULL COMMENT '业务Id',
  `store_id` varchar(30) NOT NULL COMMENT '商店ID',
  `credentials_cd` varchar(12) NOT NULL COMMENT '证件类型，对应于 credentials表',
  `value` varchar(50) NOT NULL COMMENT '证件号码',
  `validity_period` date NOT NULL COMMENT '有效期，如果是长期有效 写成 3000/1/1',
  `positive_photo` varchar(100) DEFAULT NULL COMMENT '正面照片',
  `negative_photo` varchar(100) DEFAULT NULL COMMENT '反面照片',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `status_cd` varchar(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考c_status表，S 保存，0, 在用 1失效',
  UNIQUE KEY `store_cerdentials_id` (`store_cerdentials_id`),
  KEY `idx_store_cerdentials_b_id` (`b_id`),
  KEY `idx_store_cerdentials_store_id` (`store_id`),
  KEY `idx_store_cerdentials_store_cerdentials_id` (`store_cerdentials_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `s_store_cerdentials` */

/*Table structure for table `s_store_fee` */

DROP TABLE IF EXISTS `s_store_fee`;

CREATE TABLE `s_store_fee` (
  `fee_id` varchar(30) NOT NULL COMMENT 'ID',
  `b_id` varchar(30) NOT NULL COMMENT '业务Id',
  `store_id` varchar(30) NOT NULL COMMENT '商店ID',
  `user_id` varchar(30) NOT NULL COMMENT '用户ID',
  `fee_type_cd` varchar(10) NOT NULL COMMENT '费用类型,物业费，停车费 请查看store_fee_type表',
  `fee_money` varchar(20) NOT NULL COMMENT '费用金额',
  `fee_time` varchar(10) NOT NULL COMMENT '费用周期，一个月，半年，或一年 请查看store_fee_time表',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `start_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '开始时间',
  `end_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '结束时间',
  `status_cd` varchar(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考c_status表，S 保存，0, 在用 1失效',
  UNIQUE KEY `fee_id` (`fee_id`),
  KEY `idx_store_fee_fee_id` (`fee_id`),
  KEY `idx_store_fee_b_id` (`b_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `s_store_fee` */

insert  into `s_store_fee`(`fee_id`,`b_id`,`store_id`,`user_id`,`fee_type_cd`,`fee_money`,`fee_time`,`create_time`,`start_time`,`end_time`,`status_cd`) values ('43512782246290128896','20512782243995844608','201811150000001','201811150000235','8001','200','7002','2018-11-15 16:13:35','2018-11-16 00:00:00','2018-12-16 00:00:00','0'),('43512782363944550400','20512782362006781952','201811150000001','201811150000235','8001','200','7002','2018-11-15 16:14:03','2018-11-16 00:00:00','2018-12-16 00:00:00','0');

/*Table structure for table `s_store_house` */

DROP TABLE IF EXISTS `s_store_house`;

CREATE TABLE `s_store_house` (
  `house_id` varchar(30) NOT NULL COMMENT 'ID',
  `house_num` varchar(30) NOT NULL COMMENT '门牌号',
  `house_name` varchar(50) NOT NULL COMMENT '住户名称',
  `house_phone` varchar(11) DEFAULT NULL COMMENT '住户联系号码',
  `house_area` varchar(30) NOT NULL COMMENT '房屋面积',
  `fee_type_cd` varchar(10) NOT NULL COMMENT '费用类型 store_fee_type表',
  `fee_price` varchar(30) NOT NULL COMMENT '费用单价',
  `user_id` varchar(10) NOT NULL COMMENT '录入人',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `status_cd` varchar(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考c_status表，S 保存，0, 在用 1失效',
  UNIQUE KEY `house_id` (`house_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `s_store_house` */

insert  into `s_store_house`(`house_id`,`house_num`,`house_name`,`house_phone`,`house_area`,`fee_type_cd`,`fee_price`,`user_id`,`create_time`,`update_time`,`status_cd`) values ('44513145747198181376','10-1253','测试你大爷','10000','200','7002','54','800625487','2018-11-16 16:18:02','2018-11-16 00:00:00','C'),('44513145904337780736','10-1253','测试你大爷','10000','200','7002','54','800625487','2018-11-16 16:20:07','2018-11-16 00:00:00','C'),('44513146888191475712','10-1253','测试你大爷','10000','200','7002','54','800625487','2018-11-16 16:22:33','2018-11-16 00:00:00','C');

/*Table structure for table `s_store_house_attr` */

DROP TABLE IF EXISTS `s_store_house_attr`;

CREATE TABLE `s_store_house_attr` (
  `attr_id` varchar(30) NOT NULL COMMENT '属性id',
  `house_id` varchar(30) NOT NULL COMMENT '用户ID',
  `spec_cd` varchar(12) NOT NULL COMMENT '规格id,参考spec表',
  `VALUE` varchar(50) NOT NULL COMMENT '属性值',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `status_cd` varchar(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考c_status表，S 保存，0, 在用 1失效',
  UNIQUE KEY `attr_id` (`attr_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `s_store_house_attr` */

/*Table structure for table `s_store_photo` */

DROP TABLE IF EXISTS `s_store_photo`;

CREATE TABLE `s_store_photo` (
  `store_photo_id` varchar(30) NOT NULL COMMENT '商户照片ID',
  `b_id` varchar(30) NOT NULL COMMENT '业务Id',
  `store_id` varchar(30) NOT NULL COMMENT '商店ID',
  `store_photo_type_cd` varchar(12) NOT NULL COMMENT '商户照片类型,T 门头照 I 内景照',
  `photo` varchar(100) NOT NULL COMMENT '照片',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `status_cd` varchar(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考c_status表，S 保存，0, 在用 1失效',
  UNIQUE KEY `store_photo_id` (`store_photo_id`),
  KEY `idx_store_photo_b_id` (`b_id`),
  KEY `idx_store_photo_store_id` (`store_id`),
  KEY `idx_store_photo_store_photo_id` (`store_photo_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `s_store_photo` */

/*Table structure for table `spec` */

DROP TABLE IF EXISTS `spec`;

CREATE TABLE `spec` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `domain` varchar(20) NOT NULL COMMENT '属性域',
  `spec_cd` varchar(4) NOT NULL COMMENT '业务项类型规格编码，从x00020001开始每次加一就可以(约定，x=10表示c_orders_attrs 中属性，x=11表示c_business_attrs 中的属性)',
  `name` varchar(50) NOT NULL COMMENT '名称',
  `description` varchar(200) DEFAULT NULL COMMENT '描述',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `spec_cd` (`spec_cd`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

/*Data for the table `spec` */

insert  into `spec`(`id`,`domain`,`spec_cd`,`name`,`description`,`create_time`) values (1,'ORDERS','1000','订单来源','订单来源','2018-11-14 13:28:44'),(2,'BUSINESS','2000','推荐UserID','推荐UserID','2018-11-14 13:28:44');

/*Table structure for table `store_fee_time` */

DROP TABLE IF EXISTS `store_fee_time`;

CREATE TABLE `store_fee_time` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `domain` varchar(20) NOT NULL COMMENT '域',
  `fee_time_cd` varchar(12) NOT NULL COMMENT '费用周期编码 一年，半年等',
  `name` varchar(50) NOT NULL COMMENT '收费类型编码',
  `description` varchar(200) DEFAULT NULL COMMENT '描述',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `fee_time_cd` (`fee_time_cd`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

/*Data for the table `store_fee_time` */

insert  into `store_fee_time`(`id`,`domain`,`fee_time_cd`,`name`,`description`,`create_time`) values (1,'feetype_domain','7001','一个月','按月缴费','2018-11-15 14:15:28'),(5,'feetype_domain','7002','季度','按季度缴费','2018-11-15 14:16:10'),(6,'feetype_domain','7003','年','按年缴费','2018-11-15 14:16:13');

/*Table structure for table `store_fee_type` */

DROP TABLE IF EXISTS `store_fee_type`;

CREATE TABLE `store_fee_type` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `domain` varchar(20) NOT NULL COMMENT '域',
  `fee_type_cd` varchar(12) NOT NULL COMMENT '收费类型 物业费 停车费等',
  `name` varchar(50) NOT NULL COMMENT '收费类型编码',
  `description` varchar(200) DEFAULT NULL COMMENT '描述',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `fee_type_cd` (`fee_type_cd`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

/*Data for the table `store_fee_type` */

insert  into `store_fee_type`(`id`,`domain`,`fee_type_cd`,`name`,`description`,`create_time`) values (1,'feetype_domain','8001','物业费','物业费按年收取','2018-11-15 14:08:11'),(2,'feetype_domain','8002','停车费','物业费按月收取','2018-11-15 14:08:28'),(3,'feetype_domain','8003','水费','物业费按需收取','2018-11-15 14:08:28'),(4,'feetype_domain','8004','电费','物业费按需收取','2018-11-15 14:08:28'),(5,'feetype_domain','8005','暖气费','物业费按年收取','2018-11-15 14:08:28'),(6,'feetype_domain','8006','气费','物业费按需收取','2018-11-15 14:08:28');

/*Table structure for table `store_type` */

DROP TABLE IF EXISTS `store_type`;

CREATE TABLE `store_type` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `store_type_cd` varchar(12) NOT NULL COMMENT '店铺编码',
  `name` varchar(50) NOT NULL COMMENT '店铺种类编码',
  `description` varchar(200) DEFAULT NULL COMMENT '描述',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `store_type_cd` (`store_type_cd`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `store_type` */

/*Table structure for table `tag` */

DROP TABLE IF EXISTS `tag`;

CREATE TABLE `tag` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `domain` varchar(20) NOT NULL COMMENT '标签域',
  `tag_cd` varchar(12) NOT NULL COMMENT '标签编码',
  `name` varchar(50) NOT NULL COMMENT '标签名称',
  `description` varchar(200) DEFAULT NULL COMMENT '描述',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `tag` */

/*Table structure for table `u_location` */

DROP TABLE IF EXISTS `u_location`;

CREATE TABLE `u_location` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `location_cd` varchar(4) NOT NULL COMMENT '区域编码',
  `level` varchar(4) NOT NULL COMMENT '区域级别，1 表示一级地区',
  `name` varchar(50) NOT NULL COMMENT '区域名称',
  `description` varchar(200) DEFAULT NULL COMMENT '描述',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `location_cd` (`location_cd`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `u_location` */

/*Table structure for table `u_user` */

DROP TABLE IF EXISTS `u_user`;

CREATE TABLE `u_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `user_id` varchar(30) NOT NULL COMMENT '用户ID',
  `name` varchar(50) NOT NULL COMMENT '名称',
  `email` varchar(30) DEFAULT NULL COMMENT '邮箱地址',
  `address` varchar(200) DEFAULT NULL COMMENT '现居住地址',
  `password` varchar(128) DEFAULT NULL COMMENT '用户密码，加密过后',
  `location_cd` varchar(8) DEFAULT NULL COMMENT '用户地区，编码详见 u_location',
  `age` int(11) DEFAULT NULL COMMENT '用户年龄',
  `sex` varchar(1) DEFAULT NULL COMMENT '性别，0表示男孩 1表示女孩',
  `tel` varchar(11) DEFAULT NULL COMMENT '用户手机',
  `level_cd` varchar(2) NOT NULL DEFAULT '0' COMMENT '用户级别,关联user_level',
  `b_id` varchar(30) NOT NULL COMMENT '业务Id',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `status_cd` varchar(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考c_status表，S 保存，0, 在用 1失效',
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `u_user` */

/*Table structure for table `u_user_address` */

DROP TABLE IF EXISTS `u_user_address`;

CREATE TABLE `u_user_address` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `address_id` varchar(30) NOT NULL COMMENT '地址ID',
  `b_id` varchar(30) NOT NULL COMMENT '业务Id',
  `user_id` varchar(30) NOT NULL COMMENT '用户ID',
  `tel` varchar(11) NOT NULL COMMENT '电话',
  `postal_code` varchar(10) NOT NULL COMMENT '邮政编码',
  `address` varchar(200) NOT NULL COMMENT '地址',
  `is_default` varchar(1) NOT NULL COMMENT '是否为默认地址 1，表示默认地址 0 或空不是默认地址',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `status_cd` varchar(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考c_status表，S 保存，0在用，1失效',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `u_user_address` */

/*Table structure for table `u_user_attr` */

DROP TABLE IF EXISTS `u_user_attr`;

CREATE TABLE `u_user_attr` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `attr_id` varchar(30) NOT NULL COMMENT '属性id',
  `user_id` varchar(30) NOT NULL COMMENT '用户ID',
  `spec_cd` varchar(12) NOT NULL COMMENT '规格id,参考spec表',
  `value` varchar(50) NOT NULL COMMENT '属性值',
  `b_id` varchar(30) NOT NULL COMMENT '业务ID',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `status_cd` varchar(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考c_status表，S 保存，0在用，1失效',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `u_user_attr` */

/*Table structure for table `u_user_credentials` */

DROP TABLE IF EXISTS `u_user_credentials`;

CREATE TABLE `u_user_credentials` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `credentials_id` varchar(30) NOT NULL COMMENT '证件ID',
  `b_id` varchar(30) NOT NULL COMMENT '业务Id',
  `user_id` varchar(30) NOT NULL COMMENT '用户ID',
  `credentials_cd` varchar(12) NOT NULL COMMENT '证件类型',
  `value` varchar(50) NOT NULL COMMENT '证件号码',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `status_cd` varchar(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考c_status表，S 保存，0在用，1失效',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `u_user_credentials` */

/*Table structure for table `u_user_tag` */

DROP TABLE IF EXISTS `u_user_tag`;

CREATE TABLE `u_user_tag` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `tag_id` varchar(30) NOT NULL COMMENT '打标ID',
  `user_id` varchar(30) NOT NULL COMMENT '用户ID',
  `tag_cd` varchar(12) NOT NULL COMMENT '标签编码,参考tag表',
  `b_id` varchar(30) NOT NULL COMMENT '业务Id',
  `remark` varchar(200) DEFAULT NULL COMMENT '备注',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `status_cd` varchar(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考c_status表，S 保存，0在用，1失效',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `u_user_tag` */

/*Table structure for table `user_level` */

DROP TABLE IF EXISTS `user_level`;

CREATE TABLE `user_level` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `level_cd` varchar(4) NOT NULL COMMENT '用户级别',
  `name` varchar(50) NOT NULL COMMENT '名称',
  `description` varchar(200) DEFAULT NULL COMMENT '描述',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `level_cd` (`level_cd`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

/*Data for the table `user_level` */

insert  into `user_level`(`id`,`level_cd`,`name`,`description`,`create_time`) values (1,'0','普通用户','普通用户','2018-11-14 13:28:59');

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
