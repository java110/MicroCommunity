/*
SQLyog Professional v12.09 (64 bit)
MySQL - 5.6.40 : Database - TT
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

/*Table structure for table `c_app` */

DROP TABLE IF EXISTS `c_app`;

CREATE TABLE `c_app` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `app_id` varchar(10) NOT NULL COMMENT 'åº”ç”¨ID',
  `name` varchar(50) NOT NULL COMMENT 'åç§° å¯¹åº”ç³»ç»Ÿåç§°',
  `security_code` varchar(64) NOT NULL COMMENT 'ç­¾åç  signç­¾åæ—¶ç”¨',
  `while_list_ip` varchar(200) DEFAULT NULL COMMENT 'ç™½åå•ip å¤šä¸ªä¹‹é—´ç”¨;éš”å¼€',
  `black_list_ip` varchar(200) DEFAULT NULL COMMENT 'é»‘åå•ip å¤šä¸ªä¹‹é—´ç”¨;éš”å¼€',
  `remark` varchar(200) DEFAULT NULL COMMENT 'æè¿°',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'åˆ›å»ºæ—¶é—´',
  `status_cd` varchar(2) NOT NULL COMMENT 'æ•°æ®çŠ¶æ€ï¼Œè¯¦ç»†å‚è€ƒc_statusè¡¨ï¼Œ0åœ¨ç”¨ï¼Œ1å¤±æ•ˆ',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

/*Data for the table `c_app` */

insert  into `c_app`(`id`,`app_id`,`name`,`security_code`,`while_list_ip`,`black_list_ip`,`remark`,`create_time`,`status_cd`) values (1,'8000418001','å†…éƒ¨æµ‹è¯•åº”ç”¨','WEBURFPKIFJUHNCJUEIKMKJUJHULSMNCHDY89KMC',NULL,NULL,'è®°å¾—åˆ é™¤','2018-04-24 15:52:23','0'),(2,'8000418002','控制中心应用','',NULL,NULL,'控制中心应用','2018-05-07 12:14:44','0');

/*Table structure for table `c_business` */

DROP TABLE IF EXISTS `c_business`;

CREATE TABLE `c_business` (
  `b_id` varchar(18) NOT NULL COMMENT 'ä¸šåŠ¡Id',
  `o_id` varchar(18) NOT NULL COMMENT 'è®¢å•ID',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'åˆ›å»ºæ—¶é—´',
  `business_type_cd` varchar(4) NOT NULL COMMENT 'ä¸šåŠ¡é¡¹ç±»åž‹ï¼Œå‚è€ƒc_business_typeè¡¨',
  `finish_time` date DEFAULT NULL COMMENT 'å®Œæˆæ—¶é—´',
  `remark` varchar(200) DEFAULT NULL COMMENT 'å¤‡æ³¨',
  `status_cd` varchar(2) NOT NULL COMMENT 'æ•°æ®çŠ¶æ€ï¼Œè¯¦ç»†å‚è€ƒc_statusè¡¨'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `c_business` */

/*Table structure for table `c_business_attrs` */

DROP TABLE IF EXISTS `c_business_attrs`;

CREATE TABLE `c_business_attrs` (
  `b_id` varchar(18) NOT NULL COMMENT 'è®¢å•ID',
  `attr_id` varchar(18) NOT NULL COMMENT 'å±žæ€§id',
  `spec_cd` varchar(12) NOT NULL COMMENT 'è§„æ ¼id,å‚è€ƒspecè¡¨',
  `VALUE` varchar(50) NOT NULL COMMENT 'å±žæ€§å€¼'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `c_business_attrs` */

/*Table structure for table `c_business_type` */

DROP TABLE IF EXISTS `c_business_type`;

CREATE TABLE `c_business_type` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `business_type_cd` varchar(4) NOT NULL COMMENT 'ä¸šåŠ¡é¡¹ç±»åž‹',
  `name` varchar(50) NOT NULL COMMENT 'åç§°',
  `description` varchar(200) DEFAULT NULL COMMENT 'æè¿°',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'åˆ›å»ºæ—¶é—´',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `c_business_type` */

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
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

/*Data for the table `c_cache` */

insert  into `c_cache`(`id`,`cache_code`,`service_code`,`name`,`param`,`seq`,`group`,`create_time`,`status_cd`) values (1,'1001','flush.center.cache','映射缓存（c_mapping表）','{\"cacheName\":\"MAPPING\"}',1,'COMMON','2018-05-10 13:50:51','0'),(5,'1002','flush.center.cache','业务配置缓存（c_app,c_service,c_route表）','{\"cacheName\":\"APP_ROUTE_SERVICE\"}',2,'COMMON','2018-05-10 13:51:06','0'),(6,'1003','flush.center.cache','公用服务缓存（c_service_sql表）','{\"cacheName\":\"SERVICE_SQL\"}',3,'COMMON','2018-05-10 13:51:06','0');

/*Table structure for table `c_cache_2_user` */

DROP TABLE IF EXISTS `c_cache_2_user`;

CREATE TABLE `c_cache_2_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '缓存用户ID',
  `cache_code` int(11) NOT NULL COMMENT '缓存编码',
  `user_id` varchar(12) NOT NULL COMMENT '用户id',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `status_cd` varchar(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考c_status表，0在用，1失效',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

/*Data for the table `c_cache_2_user` */

insert  into `c_cache_2_user`(`id`,`cache_code`,`user_id`,`create_time`,`status_cd`) values (1,1001,'10001','2018-05-10 14:10:57','0'),(2,1002,'10001','2018-05-10 14:10:57','0'),(3,1003,'10001','2018-05-10 14:10:57','0');

/*Table structure for table `c_mapping` */

DROP TABLE IF EXISTS `c_mapping`;

CREATE TABLE `c_mapping` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `domain` varchar(50) NOT NULL COMMENT 'åŸŸ',
  `name` varchar(50) NOT NULL COMMENT 'åç§°',
  `key` varchar(100) NOT NULL COMMENT 'key',
  `value` varchar(1000) NOT NULL COMMENT 'value',
  `remark` varchar(200) DEFAULT NULL COMMENT 'æè¿°',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'åˆ›å»ºæ—¶é—´',
  `status_cd` varchar(2) NOT NULL DEFAULT '0' COMMENT 'æ•°æ®çŠ¶æ€ï¼Œè¯¦ç»†å‚è€ƒc_statusè¡¨ï¼Œ0åœ¨ç”¨ï¼Œ1å¤±æ•ˆ',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8;

/*Data for the table `c_mapping` */

insert  into `c_mapping`(`id`,`domain`,`name`,`key`,`value`,`remark`,`create_time`,`status_cd`) values (12,'DOMAIN.COMMON','日志开关','LOG_ON_OFF','ON','日志开关','2018-05-07 12:13:59','0'),(13,'DOMAIN.COMMON','耗时开关','COST_TIME_ON_OFF','ON','耗时开关','2018-05-07 12:13:59','0'),(14,'DOMAIN.COMMON','规则开关','RULE_ON_OFF','OFF','规则开关','2018-05-07 12:13:59','0'),(15,'DOMAIN.COMMON','不调规则服务的订单类型','NO_NEED_RULE_VALDATE_ORDER','Q','不调规则服务的订单类型','2018-05-07 12:13:59','0'),(16,'DOMAIN.COMMON','不保存订单信息','NO_SAVE_ORDER','Q','不保存订单信息','2018-05-07 12:13:59','0'),(17,'DOMAIN.COMMON','不用调用 下游系统的配置','NO_INVOKE_BUSINESS_SYSTEM','JAVA110','不用调用 下游系统的配置(一般不存在这种情况，这里主要是在没有下游系统的情况下测试中心服务用)','2018-05-07 12:13:59','0'),(18,'DOMAIN.COMMON','不用调用 作废下游系统的配置','NO_INVALID_BUSINESS_SYSTEM','Q','不用调用 作废下游系统的配置 (一般不存在这种情况，这里主要是在没有下游系统的情况下测试中心服务用)','2018-05-07 12:14:00','0'),(19,'DOMAIN.COMMON','需要调用服务生成各个ID','NEED_INVOKE_SERVICE_GENERATE_ID','OFF','需要调用服务生成各个ID','2018-05-07 12:14:00','0'),(20,'DOMAIN.COMMON','公钥','PUBLIC_STRING','MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDloKXSBA5+tP39uS8yi5RZOs6Jdrt0znRQetyXX2l/IUCi1x1QAMgoZbnEavmdZ5jOZN/T1WYFbt/VomXEHaTdStAiYm3DCnxxy5CMMyRKai0+6n4lLJQpUmnAQPFENrOV8b70gBSBVjUXksImgui5qYaNqX90pjEzcyq+6CugBwIDAQAB','公钥','2018-05-07 12:14:00','0'),(21,'DOMAIN.COMMON','私钥','PRIVATE_STRING','MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAJbtQYV+VpWZvifoc0R11MyAfIyMGoJKHDrWQau7oxLHotDDJM80o7ea7oL2onaHWOXaybpUp5FpZgjuixYMhlQOA/VXosrJOGJhgNv0dAL6VVXxmjlg2UWqIEoyTS7IzF3BuQCqy2k9aT7mGiC0RYRpndTuwe/0DKwFx70dIIIrAgMBAAECgYBMNMHnqLIJWZa1Sd6hy6lGFP5ObROZg9gbMUH5d4XQnrKsHEyCvz6HH5ic0fGYTaDqdn1zMvllJ8XYbrIV0P8lvHr9/LCnoXessmf61hKZyTKi5ycNkiPHTjmJZCoVTQFprcNgYeVX4cvNsqB2zWwzoAk8bbdWY6X5jB6YEpfBmQJBANiO9GiBtw+T9h60MpyV1xhJKsx0eCvxRZcsDB1OLZvQ7dHnsHmh0xUBL2MraHKnQyxOlrIzOtyttxSTrQzwcM0CQQCyajkzxpF6EjrXWHYVHb3AXFSoz5krjOkLEHegYlGhx0gtytBNVwftCn6hqtaxCxKMp00ZJoXIxo8d9tQy4N7XAkBljnTT5bEBnzPWpk7t298pRnbJtvz8LoOiJ0fvHlCJN+uvemXqRJeGzC165kpvKj14M8q7+wZpoxWukqqe3MspAkAuFYHw/blV7p+EQDU//w6kQTUc5YKK3TrUwMwlgT/UqcTbDyf+0hwZ/jv3RkluMY35Br3DYU/tLFyLQNZOzgbBAkEApWARXVlleEYbv8dPUL+56S0ky1hZSuPfVOBda4V3p0q18LjcHIyYcVhKGqkpii5JgblaYyjUriNDisFalCp8jQ==','私钥','2018-05-07 12:14:00','0'),(22,'DOMAIN.COMMON','外部应用公钥','PUBLIC_STRING','MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCW7UGFflaVmb4n6HNEddTMgHyMjBqCShw61kGru6MSx6LQwyTPNKO3mu6C9qJ2h1jl2sm6VKeRaWYI7osWDIZUDgP1V6LKyThiYYDb9HQC+lVV8Zo5YNlFqiBKMk0uyMxdwbkAqstpPWk+5hogtEWEaZ3U7sHv9AysBce9HSCCKwIDAQAB','外部应用公钥','2018-05-07 12:14:00','0'),(23,'DOMAIN.COMMON','外部应用私钥','PRIVATE_STRING','MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAOWgpdIEDn60/f25LzKLlFk6zol2u3TOdFB63JdfaX8hQKLXHVAAyChlucRq+Z1nmM5k39PVZgVu39WiZcQdpN1K0CJibcMKfHHLkIwzJEpqLT7qfiUslClSacBA8UQ2s5XxvvSAFIFWNReSwiaC6Lmpho2pf3SmMTNzKr7oK6AHAgMBAAECgYEAlfR5FVNM2/X6QC0k408/i53Zru94r2j7kGsLj1bhoAHpIe502AAKtkboL5rkc6Rpp688dCvRug6T4gFxj8cEF7rOOU4CHqVCHUHa4sWSDL2Rg7pMbQOVB7PGmM4C/hEgXcwM6tmLiU3xkkQDrpgT1bPpAm7iwDx4HkZBv1naYnECQQDyk40+KDvyUgp/r1tKbkMLoQOAfTZPXy+HgeAkU3PCUTFQlvn2OU6Fsl3Yjlp6utxPVnd00DoPZ8qvx1falaeLAkEA8lWoIDgyYwnibv2fr3A715PkieJ0exKfLb5lSD9UIfGJ9s7oTcltl7pprykfSP46heWjScS7YJRZHPfqb1/Y9QJAJNUQqjJzv7yDSZX3t5p8ZaSiIn1gpLagQeQPg5SETCoF4eW6uI9FA/nsU/hxdpcu4oEPjFYdqr8owH31MgRtNwJAaE+6qPPHrJ3qnAAMJoZXG/qLG1cg8IEZh6U3D5xC6MGBs31ovWMBC5iwOTeoQdE8+7nXSb+nMHFq0m9cuEg3qQJAH4caPSQ9RjVOP9on+niy9b1mATbvurepIB95KUtaHLz1hpihCLR7dTwrz8JOitgFE75Wzt4a00GZYxnaq3jsjA==','外部应用私钥','2018-05-07 12:14:00','0'),(24,'DOMAIN.COMMON','默认KEY_SIZE','DEFAULT_DECRYPT_KEY_SIZE','2048','默认KEY_SIZE','2018-05-07 12:14:00','0'),(25,'DOMAIN.COMMON','中心服务地址','CENTER_SERVICE_URL','http://center-service/httpApi/service','中心服务地址','2018-05-07 12:14:00','0'),(26,'DOMAIN.COMMON','控制中心APP_ID','CONSOLE_SERVICE_APP_ID','8000418002','控制中心APP_ID','2018-05-07 12:14:00','0'),(27,'DOMAIN.COMMON','控制服务加密开关','KEY_CONSOLE_SERVICE_SECURITY_ON_OFF','ON','控制服务加密开关','2018-05-07 12:14:00','0'),(28,'DOMAIN.COMMON','控制服务鉴权秘钥','CONSOLE_SECURITY_CODE','WEBURFPKIFJUHNCJUEIKMKJUJHULSMNCHDY89KMC','控制服务鉴权秘钥','2018-05-07 12:14:00','0'),(29,'DOMAIN.COMMON','update java110','java110','1','java110','2018-05-13 15:27:55','1'),(30,'DOMAIN.COMMON','java110??','java110','ON','','2018-05-13 15:44:49','1');

/*Table structure for table `c_order_type` */

DROP TABLE IF EXISTS `c_order_type`;

CREATE TABLE `c_order_type` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `order_type_cd` varchar(4) NOT NULL COMMENT 'è®¢å•ç±»åž‹',
  `name` varchar(50) NOT NULL COMMENT 'åç§°',
  `description` varchar(200) DEFAULT NULL COMMENT 'æè¿°',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'åˆ›å»ºæ—¶é—´',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

/*Data for the table `c_order_type` */

insert  into `c_order_type`(`id`,`order_type_cd`,`name`,`description`,`create_time`) values (1,'Q','æŸ¥è¯¢å•','æŸ¥è¯¢å•','2018-04-24 15:52:23');

/*Table structure for table `c_orders` */

DROP TABLE IF EXISTS `c_orders`;

CREATE TABLE `c_orders` (
  `o_id` varchar(18) NOT NULL COMMENT 'è®¢å•ID',
  `app_id` varchar(10) NOT NULL COMMENT 'åº”ç”¨ID',
  `ext_transaction_id` varchar(30) NOT NULL COMMENT 'å¤–éƒ¨äº¤æ˜“æµæ°´',
  `user_id` varchar(12) NOT NULL COMMENT 'ç”¨æˆ·ID',
  `request_time` varchar(16) NOT NULL COMMENT 'å¤–éƒ¨ç³»ç»Ÿè¯·æ±‚æ—¶é—´',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'åˆ›å»ºæ—¶é—´',
  `order_type_cd` varchar(4) NOT NULL COMMENT 'è®¢å•ç±»åž‹ï¼Œå‚è€ƒc_order_typeè¡¨',
  `finish_time` date DEFAULT NULL COMMENT 'è®¢å•å®Œæˆæ—¶é—´',
  `remark` varchar(200) DEFAULT NULL COMMENT 'å¤‡æ³¨',
  `status_cd` varchar(2) NOT NULL COMMENT 'æ•°æ®çŠ¶æ€ï¼Œè¯¦ç»†å‚è€ƒc_statusè¡¨'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `c_orders` */

/*Table structure for table `c_orders_attrs` */

DROP TABLE IF EXISTS `c_orders_attrs`;

CREATE TABLE `c_orders_attrs` (
  `o_id` varchar(18) NOT NULL COMMENT 'è®¢å•ID',
  `attr_id` varchar(18) NOT NULL COMMENT 'å±žæ€§id',
  `spec_cd` varchar(12) NOT NULL COMMENT 'è§„æ ¼id,å‚è€ƒspecè¡¨',
  `VALUE` varchar(50) NOT NULL COMMENT 'å±žæ€§å€¼'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `c_orders_attrs` */

/*Table structure for table `c_route` */

DROP TABLE IF EXISTS `c_route`;

CREATE TABLE `c_route` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `app_id` varchar(10) NOT NULL COMMENT 'åº”ç”¨ID',
  `service_id` int(11) NOT NULL COMMENT 'ä¸‹æ¸¸æŽ¥å£é…ç½®ID',
  `order_type_cd` varchar(4) NOT NULL COMMENT 'è®¢å•ç±»åž‹ï¼Œå‚è€ƒc_order_typeè¡¨',
  `invoke_limit_times` int(11) DEFAULT NULL COMMENT 'æŽ¥å£è°ƒç”¨ä¸€åˆ†é’Ÿè°ƒç”¨æ¬¡æ•°',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'åˆ›å»ºæ—¶é—´',
  `status_cd` varchar(2) NOT NULL COMMENT 'æ•°æ®çŠ¶æ€ï¼Œè¯¦ç»†å‚è€ƒc_statusè¡¨ï¼Œ0åœ¨ç”¨ï¼Œ1å¤±æ•ˆï¼Œ2 è¡¨ç¤ºä¸‹çº¿ï¼ˆå½“ç»„ä»¶è°ƒç”¨æœåŠ¡è¶…è¿‡é™åˆ¶æ—¶è‡ªåŠ¨ä¸‹çº¿ï¼‰',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8;

/*Data for the table `c_route` */

insert  into `c_route`(`id`,`app_id`,`service_id`,`order_type_cd`,`invoke_limit_times`,`create_time`,`status_cd`) values (1,'8000418001',1,'Q',NULL,'2018-04-24 15:52:23','0'),(2,'8000418001',3,'Q',NULL,'2018-04-24 15:52:23','0'),(3,'8000418001',3,'Q',NULL,'2018-05-07 12:16:38','0'),(4,'8000418002',3,'Q',NULL,'2018-05-07 12:16:38','0'),(5,'8000418002',4,'Q',NULL,'2018-05-07 12:16:38','0'),(6,'8000418002',5,'Q',NULL,'2018-05-09 12:18:26','0'),(7,'8000418002',6,'Q',NULL,'2018-05-09 12:18:26','0'),(8,'8000418002',7,'Q',NULL,'2018-05-09 12:18:27','0'),(9,'8000418002',8,'Q',NULL,'2018-05-10 12:24:10','0'),(10,'8000418002',9,'Q',NULL,'2018-05-10 12:24:10','0'),(11,'8000418002',10,'Q',NULL,'2018-05-10 12:24:10','0'),(12,'8000418002',11,'Q',NULL,'2018-05-10 13:04:51','0'),(13,'8000418002',12,'Q',NULL,'2018-05-10 14:13:34','0'),(14,'8000418002',13,'Q',NULL,'2018-05-10 16:21:42','0'),(15,'8000418002',14,'Q',NULL,'2018-05-13 15:16:20','0'),(16,'8000418002',15,'Q',NULL,'2018-05-13 15:38:00','0'),(17,'8000418002',16,'Q',NULL,'2018-05-13 15:46:53','0');

/*Table structure for table `c_service` */

DROP TABLE IF EXISTS `c_service`;

CREATE TABLE `c_service` (
  `service_id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `service_code` varchar(50) NOT NULL COMMENT 'è‡ªå®šä¹‰ï¼Œå‘½åæ–¹å¼æŸ¥è¯¢ç±»query.+ç›®æ ‡ç³»ç»Ÿ+.+ä¸šåŠ¡åç§° ä¿å­˜ç±» save.+ç›®æ ‡ç³»ç»Ÿ+.+ä¸šåŠ¡åç§° ä¿®æ”¹ç±» modify.+ç›®æ ‡ç³»ç»Ÿ+.+ä¸šåŠ¡åç§° åˆ é™¤ç±» remove.+ç›®æ ‡ç³»ç»Ÿ+.+ä¸šåŠ¡åç§° ä¾‹å¦‚ï¼šquery.user.userinfo save.user.adduserinfo',
  `invoke_model` varchar(1) NOT NULL COMMENT '1-åŒæ­¥æ–¹å¼ 2-å¼‚æ­¥æ–¹å¼',
  `business_type_cd` varchar(4) NOT NULL COMMENT 'ä¸šåŠ¡é¡¹ç±»åž‹ï¼Œå‚è€ƒc_business_typeè¡¨',
  `name` varchar(50) NOT NULL COMMENT 'æœåŠ¡åç§°',
  `seq` int(11) NOT NULL COMMENT 'é¡ºåº åªæœ‰åŒæ­¥æ–¹å¼ä¸‹æ ¹æ®seqä»Žå°åˆ°å¤§è°ƒç”¨æŽ¥å£',
  `messageQueueName` varchar(50) DEFAULT NULL COMMENT 'æ¶ˆæ¯é˜Ÿé‡Œåç§° åªæœ‰å¼‚æ­¥æ—¶æœ‰ç”¨',
  `url` varchar(200) DEFAULT NULL COMMENT 'ç›®æ ‡åœ°å€',
  `method` varchar(50) DEFAULT NULL COMMENT 'æ–¹æ³• ç©º ä¸ºhttp post LOCAL_SERVICE ä¸ºè°ƒç”¨æœ¬åœ°æœåŠ¡ å…¶ä»–ä¸ºwebserviceæ–¹å¼è°ƒç”¨',
  `timeout` int(11) NOT NULL DEFAULT '60' COMMENT 'è¶…æ—¶æ—¶é—´',
  `retry_count` int(11) NOT NULL DEFAULT '3' COMMENT 'é‡è¯•æ¬¡æ•°',
  `provide_app_id` varchar(10) NOT NULL COMMENT 'åº”ç”¨ID',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'åˆ›å»ºæ—¶é—´',
  `status_cd` varchar(2) NOT NULL DEFAULT '0' COMMENT 'æ•°æ®çŠ¶æ€ï¼Œè¯¦ç»†å‚è€ƒc_statusè¡¨ï¼Œ0åœ¨ç”¨ï¼Œ1å¤±æ•ˆ',
  PRIMARY KEY (`service_id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8;

/*Data for the table `c_service` */

insert  into `c_service`(`service_id`,`service_code`,`invoke_model`,`business_type_cd`,`name`,`seq`,`messageQueueName`,`url`,`method`,`timeout`,`retry_count`,`provide_app_id`,`create_time`,`status_cd`) values (1,'query.user.userInfo','S','Q','ç”¨æˆ·ä¿¡æ¯æŸ¥è¯¢',1,NULL,'http://...',NULL,60,3,'8000418001','2018-04-24 15:52:23','0'),(2,'query.order.orderInfo','S','Q','è®¢å•ä¿¡æ¯',1,NULL,'http://center-service/businessApi/query',NULL,60,3,'8000418001','2018-04-24 15:52:23','0'),(3,'query.console.menu','S','Q','查询菜单',1,NULL,'http://center-service/businessApi/query',NULL,60,3,'8000418002','2018-05-07 12:16:18','0'),(4,'query.user.loginInfo','S','Q','查询用户登录信息',1,NULL,'http://center-service/businessApi/query',NULL,60,3,'8000418002','2018-05-07 12:16:18','0'),(5,'query.console.template','S','Q','查询模板信息',1,NULL,'http://center-service/businessApi/query',NULL,60,3,'8000418002','2018-05-09 12:17:45','0'),(6,'query.console.templateCol','S','Q','查询列模板信息',1,NULL,'http://center-service/businessApi/query',NULL,60,3,'8000418002','2018-05-09 12:17:45','0'),(7,'query.center.mapping','S','Q','查询映射数据',1,NULL,'http://center-service/businessApi/query',NULL,60,3,'8000418002','2018-05-09 12:17:45','0'),(8,'query.center.apps','S','Q','查询外部应用',1,NULL,'http://center-service/businessApi/query',NULL,60,3,'8000418002','2018-05-10 12:23:19','0'),(9,'query.center.services','S','Q','查询服务',1,NULL,'http://center-service/businessApi/query',NULL,60,3,'8000418002','2018-05-10 12:23:19','0'),(10,'query.center.routes','S','Q','查询路由',1,NULL,'http://center-service/businessApi/query',NULL,60,3,'8000418002','2018-05-10 12:23:19','0'),(11,'flush.center.cache','S','Q','CenterService缓存',1,NULL,'http://center-service/cacheApi/flush',NULL,60,3,'8000418002','2018-05-10 13:03:56','0'),(12,'query.console.caches','S','Q','查询所有缓存',1,NULL,'http://center-service/businessApi/query',NULL,60,3,'8000418002','2018-05-10 14:13:12','0'),(13,'query.console.cache','S','Q','查询所有缓存',1,NULL,'http://center-service/businessApi/query',NULL,60,3,'8000418002','2018-05-10 16:21:26','0'),(14,'save.center.mapping','S','Q','保存映射信息',1,NULL,'http://center-service/businessApi/do',NULL,60,3,'8000418002','2018-05-13 15:15:26','0'),(15,'delete.center.mapping','S','Q','删除映射信息',1,NULL,'http://center-service/businessApi/do',NULL,60,3,'8000418002','2018-05-13 15:37:41','0'),(16,'update.center.mapping','S','Q','保存映射信息',1,NULL,'http://center-service/businessApi/do',NULL,60,3,'8000418002','2018-05-13 15:46:44','0');

/*Table structure for table `c_service_sql` */

DROP TABLE IF EXISTS `c_service_sql`;

CREATE TABLE `c_service_sql` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `service_code` varchar(50) NOT NULL COMMENT 'å¯¹åº”c_serviceè¡¨',
  `name` varchar(50) NOT NULL COMMENT 'åç§°',
  `params` varchar(500) NOT NULL COMMENT 'å‚æ•°',
  `query_model` varchar(1) NOT NULL COMMENT 'æŸ¥è¯¢æ–¹å¼ 1ã€sql,2ã€å­˜å‚¨è¿‡ç¨‹',
  `sql` longtext COMMENT 'æ‰§è¡Œsql',
  `proc` varchar(200) DEFAULT NULL COMMENT 'å­˜å‚¨è¿‡ç¨‹åç§°',
  `java_script` longtext COMMENT 'æ‰§è¡Œjavaè„šæœ¬ä»£ç ',
  `template` longtext COMMENT 'è¾“å‡ºæ¨¡æ¿',
  `remark` varchar(200) DEFAULT NULL COMMENT 'æè¿°',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'åˆ›å»ºæ—¶é—´',
  `status_cd` varchar(2) NOT NULL COMMENT 'æ•°æ®çŠ¶æ€ï¼Œè¯¦ç»†å‚è€ƒc_statusè¡¨ï¼Œ0åœ¨ç”¨ï¼Œ1å¤±æ•ˆ',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8;

/*Data for the table `c_service_sql` */

insert  into `c_service_sql`(`id`,`service_code`,`name`,`params`,`query_model`,`sql`,`proc`,`java_script`,`template`,`remark`,`create_time`,`status_cd`) values (1,'query.order.orderInfo','è®¢å•ä¿¡æ¯','oId','1','{\n                                                 	\"param1\":\"SELECT co.app_id appId,co.create_time createTime,co.ext_transaction_id extTransactionId,co.finish_time finishTime\n                                                 ,co.order_type_cd orderTypeCd,co.o_id oId ,co.remark remark ,co.request_time requestTime ,co.user_id userId,co.status_cd statusCd\n                                                  FROM c_orders co WHERE co.o_id = #oId#\",\n                                                  \"param2\":\"SELECT cb.b_id bId, cb.business_type_cd businessTypeCd,cb.create_time createTime,cb.finish_time finishTime ,cb.o_id oId,\n                                                 cb.remark remark,cb.status_cd statusCd FROM c_business cb WHERE cb.o_id = #oId#\"\n                                                 }','',NULL,'{\"PARAM:\"{\n                                                            \"param1\": \"$.#order#Object\",\n                                                            \"param2\": \"$.#business#Array\"\n                                                            },\"TEMPLATE\":\"{\n                                                         \"response\": {\n                                                           \"code\": \"0000\",\n                                                           \"message\": \"æˆåŠŸ\"\n                                                         }\n                                                       }\"}','','2018-04-24 15:52:23','0'),(2,'query.order.orderInfo','订单信息','oId','1','{\n                                                 	\"param1\":\"SELECT co.app_id appId,co.create_time createTime,co.ext_transaction_id extTransactionId,co.finish_time finishTime\n                                                 ,co.order_type_cd orderTypeCd,co.o_id oId ,co.remark remark ,co.request_time requestTime ,co.user_id userId,co.status_cd statusCd\n                                                  FROM c_orders co WHERE co.o_id = #oId#\",\n                                                  \"param2\":\"SELECT cb.b_id bId, cb.business_type_cd businessTypeCd,cb.create_time createTime,cb.finish_time finishTime ,cb.o_id oId,\n                                                 cb.remark remark,cb.status_cd statusCd FROM c_business cb WHERE cb.o_id = #oId#\"\n                                                 }','',NULL,'{\"PARAM:\"{\n                                                            \"param1\": \"$.#order#Object\",\n                                                            \"param2\": \"$.#business#Array\"\n                                                            },\"TEMPLATE\":\"{\n                                                         \"response\": {\n                                                           \"code\": \"0000\",\n                                                           \"message\": \"成功\"\n                                                         }\n                                                       }\"}','','2018-05-07 12:16:18','0'),(3,'query.console.menu','查询菜单','manageId,menuGroup','1','{\n                                                 	\"param1\":\"select mm.m_id mId,mm.name name,mm.level level,mm.parent_id parentId,mm.menu_group menuGroup,mm.user_id userId,mm.create_time createTime,\n                                                              mm.remark remark,mmc.url url,mmc.template template\n                                                              from m_menu_2_user mm2u,m_menu mm, m_menu_ctg mmc\n                                                              where mm2u.user_id = #manageId#\n                                                              and mm2u.m_id = mm.m_id\n                                                              AND mm.menu_group = #menuGroup#\n                                                              and mm2u.status_cd = \'0\'\n                                                              and mm.status_cd = \'0\'\n                                                              and mmc.m_id = mm.m_id\n                                                              and mmc.status_cd = \'0\'\r\norder by mm.seq asc\"\n                                                 }','',NULL,'{\"PARAM\":{\n                                                            \"param1\": \"$.#menus#Array\"\n                                                            },\"TEMPLATE\":{\n                                                         \"response\": {\n                                                           \"code\": \"0000\",\n                                                           \"message\": \"成功\"\n                                                         }\n                                                       }}','','2018-05-07 12:16:18','0'),(4,'query.user.loginInfo','查询用户登录信息','userCode','1','{\n\"param1\":\"SELECT \'10001\' userId, \'admin\' userName,\'d57167e07915c9428b1c3aae57003807\' userPwd FROM DUAL WHERE #userCode#=\'admin\'\"\n                                                 }','',NULL,'{\"PARAM\":{\n                                                            \"param1\": \"$.#user#Object\"\n                                                            },\"TEMPLATE\":{\n                                                         \"response\": {\n                                                           \"code\": \"0000\",\n                                                           \"message\": \"成功\"\n                                                         }\n                                                       }}','','2018-05-07 12:16:18','0'),(5,'query.console.template','查询模板信息','templateCode','1','{\n                                                 	\"param1\":\"SELECT t.template_code templateCode,t.name templateName,t.`html_name` htmlName,t.`url` templateUrl\n                                                              FROM c_template t WHERE t.status_cd = \'0\' AND t.template_code = #templateCode#\"\n                                                 }','',NULL,'{\"PARAM\":{\n                                                            \"param1\": \"$.#template#Object\"\n                                                            },\"TEMPLATE\":{\n                                                         \"response\": {\n                                                           \"code\": \"0000\",\n                                                           \"message\": \"成功\"\n                                                         }\n                                                       }}','','2018-05-09 12:18:54','0'),(6,'query.console.templateCol','查询模板列信息','templateCode','1','{\n                                                 	\"param1\":\"SELECT t.template_code templateCode,t.name templateName,tc.col_name colName,tc.col_model colModel FROM c_template t,c_template_col tc WHERE t.status_cd = \'0\' AND t.template_code = tc.template_code\n                                                              AND tc.status_cd = \'0\'\n                                                              AND t.template_code = #templateCode# order by tc.seq asc\"\n                                                 }','',NULL,'{\"PARAM\":{\n                                                            \"param1\": \"$.#template#Array\"\n                                                            },\"TEMPLATE\":{\n                                                         \"response\": {\n                                                           \"code\": \"0000\",\n                                                           \"message\": \"成功\"\n                                                         }\n                                                       }}','','2018-05-09 12:18:54','0'),(7,'query.center.mapping','查询映射数据','page,rows,sord','1','{\r\n	\"param1\": \"select count(1) records,ceil(#page#/#rows#)+1 page,ceil(count(1)/#rows#) total from c_mapping m where m.status_cd = \'0\'\",\r\n	\"param2\": \"SELECT m.`id` id,m.`domain` domain,m.name name,m.`key` `key` ,m.`value` `value`,m.`remark` remark FROM c_mapping m WHERE m.`status_cd` = \'0\' LIMIT #page#,#rows#\"\r\n}','',NULL,'{\n                                                        	\"PARAM\": {\n                                                        		\"param1\": \"$.##Object\",\n                                                        		\"param2\": \"$.#rows#Array\"\n                                                        	},\n                                                        	\"TEMPLATE\": {\n                                                        		\"response\": {\n                                                        			\"code\": \"0000\",\n                                                        			\"message\": \"成功\"\n                                                        		}\n                                                        	}\n                                                        }','','2018-05-09 12:37:28','0'),(8,'query.center.apps','查询外部应用','page,rows,sord','1','{\"param1\":\"select count(1) records,ceil(#page#/#rows#)+1 page,ceil(count(1)/#rows#) total from c_app a where a.status_cd = \'0\'\",\n                                                             \"param2\":\"SELECT m.`id` id,m.`app_id` appId,m.name `name`,m.`security_code`  securityCode ,m.`while_list_ip` whileListIp,m.`black_list_ip` blackListIp,m.`remark` remark FROM c_app m WHERE m.`status_cd` = \'0\' LIMIT #page#, #rows#\"\n                                                             }','',NULL,'{\n                                                        	\"PARAM\": {\n                                                        		\"param1\": \"$.##Object\",\n                                                        		\"param2\": \"$.#rows#Array\"\n                                                        	},\n                                                        	\"TEMPLATE\": {\n                                                        		\"response\": {\n                                                        			\"code\": \"0000\",\n                                                        			\"message\": \"成功\"\n                                                        		}\n                                                        	}\n                                                        }','','2018-05-10 12:24:38','0'),(9,'query.center.services','查询服务数据','page,rows,sord','1','{\"param1\":\"select count(1) records,ceil(#page#/#rows#)+1 page,ceil(count(1)/#rows#) total from c_service a where a.status_cd = \'0\'\",\n                                                             \"param2\":\"SELECT s.`service_id` serviceId,s.`service_code` serviceCode,s.`invoke_model` invokeModel,s.`business_type_cd`  businessTypeCd,s.name `name`,\n                                                                       s.`messageQueueName` messageQueueName,s.url url,s.`provide_app_id` provideAppId FROM c_service s WHERE s.`status_cd` = \'0\' LIMIT #page#, #rows#\"\n                                                             }','',NULL,'{\n                                                        	\"PARAM\": {\n                                                        		\"param1\": \"$.##Object\",\n                                                        		\"param2\": \"$.#rows#Array\"\n                                                        	},\n                                                        	\"TEMPLATE\": {\n                                                        		\"response\": {\n                                                        			\"code\": \"0000\",\n                                                        			\"message\": \"成功\"\n                                                        		}\n                                                        	}\n                                                        }','','2018-05-10 12:24:38','0'),(10,'query.center.routes','查询路由数据','page,rows,sord','1','{\"param1\":\"select count(1) records,ceil(#page#/#rows#)+1 page,ceil(count(1)/#rows#) total from c_route a,c_service cs WHERE a.`service_id` = cs.`service_id` AND cs.`status_cd` = \'0\' and a.status_cd = \'0\'\",\n                                                             \"param2\":\"SELECT s.id id,s.`app_id` appId,s.`service_id` serviceId,cs.`name` serviceName,cs.`service_code` serviceCode,s.`order_type_cd` orderTypeCd,s.`invoke_limit_times` invokelimitTimes FROM c_route s,c_service cs WHERE s.`service_id` = cs.`service_id` AND cs.`status_cd` = \'0\' AND s.`status_cd` = \'0\' LIMIT #page#, #rows#\"\n                                                             }','',NULL,'{\n                                                        	\"PARAM\": {\n                                                        		\"param1\": \"$.##Object\",\n                                                        		\"param2\": \"$.#rows#Array\"\n                                                        	},\n                                                        	\"TEMPLATE\": {\n                                                        		\"response\": {\n                                                        			\"code\": \"0000\",\n                                                        			\"message\": \"成功\"\n                                                        		}\n                                                        	}\n                                                        }','','2018-05-10 12:24:38','0'),(11,'query.console.caches','查询缓存数据','page,rows,sord','1','{\n                                                             \"param1\":\"SELECT c.id id,c.`cache_code` cacheCode, c.`name` cacheName FROM c_cache c, c_cache_2_user c2u WHERE c.`cache_code` = c2u.`cache_code` AND c.`status_cd` = \'0\'\n                                                                       AND c2u.`status_cd` = \'0\' AND c2u.`user_id` = #userId# AND c.`group` = \'COMMON\' ORDER BY c.`seq` ASC\"\n                                                             }','',NULL,'{\n                                                        	\"PARAM\": {\n                                                        		\"param1\": \"$.#rows#Array\"\n                                                        	},\n                                                        	\"TEMPLATE\": {\n                                                        		\"response\": {\n                                                        			\"code\": \"0000\",\n                                                        			\"message\": \"成功\"\n                                                        		}\n                                                        	}\n                                                        }','','2018-05-10 14:25:37','0'),(12,'query.console.cache','查询单条缓存信息','cacheCode','1','{\n                                                             \"param1\":\"SELECT c.id id,c.`cache_code` cacheCode, c.`name` cacheName,c.`param` param,c.`service_code` serviceCode FROM c_cache c WHERE  c.`status_cd` = \'0\' AND c.`cache_code` = #cacheCode#\"\n                                                             }','',NULL,'{\n                                                        	\"PARAM\": {\n                                                        		\"param1\": \"$.#cache#Object\"\n                                                        	},\n                                                        	\"TEMPLATE\": {\n                                                        		\"response\": {\n                                                        			\"code\": \"0000\",\n                                                        			\"message\": \"成功\"\n                                                        		}\n                                                        	}\n                                                        }','','2018-05-10 16:25:41','0'),(13,'save.center.mapping','保存映射信息','domain,name,key,value,remark','1','{\n                                                             \"param1\":\"INSERT c_mapping(domain,`name`,`key`,`value`,remark) VALUES(#domain#,#name#,#key#,#value#,#remark#)\"\n                                                             }','',NULL,'{\n                                                        	\"TEMPLATE\": {\n                                                        		\"response\": {\n                                                        			\"code\": \"0000\",\n                                                        			\"message\": \"成功\"\n                                                        		}\n                                                        	}\n                                                        }','','2018-05-13 15:20:28','0'),(14,'delete.center.mapping','删除映射信息','domain,name,key,value,remark','1','{\n                                                             \"param1\":\"UPDATE c_mapping m SET m.status_cd = \'1\' WHERE m.status_cd = \'0\' AND m.id = #id#\"\n                                                             }','',NULL,'{\n                                                        	\"TEMPLATE\": {\n                                                        		\"response\": {\n                                                        			\"code\": \"0000\",\n                                                        			\"message\": \"成功\"\n                                                        		}\n                                                        	}\n                                                        }','','2018-05-13 15:40:56','0'),(15,'update.center.mapping','修改映射信息','id,domain,name,key,value,remark','1','{\n                                                             \"param1\":\"UPDATE c_mapping m SET m.domain=#domain#,m.name = #name#,m.key=#key#,m.value=#value#,m.remark=#remark# WHERE m.status_cd = \'0\' AND m.id = #id#\"\n                                                             }','',NULL,'{\n                                                        	\"TEMPLATE\": {\n                                                        		\"response\": {\n                                                        			\"code\": \"0000\",\n                                                        			\"message\": \"成功\"\n                                                        		}\n                                                        	}\n                                                        }','','2018-05-13 15:49:39','0');

/*Table structure for table `c_status` */

DROP TABLE IF EXISTS `c_status`;

CREATE TABLE `c_status` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `status_cd` varchar(4) NOT NULL COMMENT 'çŠ¶æ€',
  `name` varchar(50) NOT NULL COMMENT 'åç§°',
  `description` varchar(200) DEFAULT NULL COMMENT 'æè¿°',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'åˆ›å»ºæ—¶é—´',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

/*Data for the table `c_status` */

insert  into `c_status`(`id`,`status_cd`,`name`,`description`,`create_time`) values (1,'1','æ— æ•ˆçš„ï¼Œä¸åœ¨ç”¨çš„','æ— æ•ˆçš„ï¼Œä¸åœ¨ç”¨çš„','2018-04-24 15:52:20'),(2,'0','æœ‰æ•ˆçš„ï¼Œåœ¨ç”¨çš„','æœ‰æ•ˆçš„ï¼Œåœ¨ç”¨çš„','2018-04-24 15:52:20'),(3,'S','ä¿å­˜æˆåŠŸ','ä¿å­˜æˆåŠŸ','2018-04-24 15:52:20'),(4,'D','ä½œåºŸè®¢å•','ä½œåºŸè®¢å•','2018-04-24 15:52:20'),(5,'E','é”™è¯¯è®¢å•','é”™è¯¯è®¢å•','2018-04-24 15:52:21'),(6,'NE','é€šçŸ¥é”™è¯¯è®¢å•','é€šçŸ¥é”™è¯¯è®¢å•','2018-04-24 15:52:21'),(7,'C','é”™è¯¯è®¢å•','é”™è¯¯è®¢å•','2018-04-24 15:52:21');

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
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

/*Data for the table `c_template` */

insert  into `c_template`(`id`,`template_code`,`name`,`html_name`,`url`,`create_time`,`status_cd`) values (1,'mapping','映射管理','list_template','LIST->query.center.mapping;QUERY->mapping_query_url;INSERT->save.center.mapping;UPDATE->update.center.mapping;DELETE->delete.center.mapping','2018-05-09 12:13:56','0'),(2,'app','外部应用','list_template','LIST->query.center.apps;QUERY->query.center.app','2018-05-10 12:21:16','0'),(3,'service','服务管理','list_template','LIST->query.center.services;QUERY->query.center.service','2018-05-10 12:21:17','0'),(4,'route','路由管理','list_template','LIST->query.center.routes;QUERY->query.center.route','2018-05-10 12:21:17','0'),(5,'cache','刷新缓存','list_template_cache','LIST->query.console.caches;QUERY->query.console.cache','2018-05-10 14:36:01','0');

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
) ENGINE=InnoDB AUTO_INCREMENT=37 DEFAULT CHARSET=utf8;

/*Data for the table `c_template_col` */

insert  into `c_template_col`(`id`,`template_code`,`col_name`,`col_code`,`col_model`,`seq`,`create_time`,`status_cd`) values (1,'mapping','列ID','id','{\r\n	\"name\": \"id\",\r\n	\"index\": \"id\",\r\n	\"width\": \"20\",\r\n	\"editable\": true,\r\n	\"sorttype\": \"int\",\r\n\"align\" : \"center\"\r\n}',1,'2018-05-09 12:14:13','0'),(2,'mapping','域','domain','{\r\n	\"name\": \"domain\",\r\n	\"index\": \"domain\",\r\n	\"width\": \"70\",\r\n	\"editable\": true,\r\n\"align\" : \"center\",\r\n	\"editoptions\" : {\"size\" : \"35\",\"defaultValue\": \"DOMAIN.COMMON\"}\r\n}',2,'2018-05-09 12:14:13','0'),(3,'mapping','名称','name','{\r\n	\"name\": \"name\",\r\n	\"index\": \"name\",\r\n	\"width\": \"90\",\r\n	\"editable\": true,\r\n	\"editoptions\" : {\"size\" : \"35\"}\r\n}',3,'2018-05-09 12:14:13','0'),(4,'mapping','键','key','{\r\n	\"name\": \"key\",\r\n	\"index\": \"key\",\r\n	\"width\": \"90\",\r\n	\"editable\": true,\r\n	\"editoptions\" : {\"size\" : \"35\"}\r\n}',4,'2018-05-09 12:14:13','0'),(5,'mapping','值','value','{\r\n	\"name\": \"value\",\r\n	\"index\": \"value\",\r\n	\"width\": \"90\",\r\n	\"editable\": true,\r\n	\"editoptions\" : {\"size\" : \"35\"}\r\n}',5,'2018-05-09 12:14:13','0'),(6,'mapping',' ','BUTTON','{\r\n	\"name\": \"detail\",\r\n	\"index\": \"\",\r\n	\"width\": \"40\",\r\n	\"fixed\": \"true\",\r\n	\"sortable\": \"false\",\r\n	\"resize\": \"false\",\r\n	\"formatter\": \"function(cellvalue, options, rowObject){ var temp =\\\"<div style=\'margin-left:8px;\'><div title=\'详情记录\' style=\'float:left;cursor:pointer;\' class=\'ui-pg-div\' id=\'jEditButton_3\' onclick=\'detail(\\\" + rowObject + \\\")\' onmouseover=\'jQuery(this).addClass(\'ui-state-hover\');\' onmouseout=\'jQuery(this).removeClass(\'ui-state-hover\');\'><span class=\'ui-icon fa-search-plus\'/></div></div>\\\";return temp; }\"\r\n}',7,'2018-05-09 12:14:13','0'),(7,'mapping','备注','remark','{\r\n	\"name\": \"remark\",\r\n	\"index\": \"remark\",\r\n	\"width\": \"90\",\r\n	\"editable\": true,\r\n	\"edittype\" : \"textarea\",\r\n\"editoptions\" : {\"rows\" : \"2\",\"cols\" : \"35\"}\r\n\r\n}',6,'2018-05-09 13:25:29','0'),(8,'app','列ID','id','{ \"name\": \"id\",\"index\": \"id\",\"width\": \"20\",\n                                                                                                             \"editable\": \"true\",\n                                                                                                             \"sorttype\": \"int\" }',1,'2018-05-10 12:21:16','0'),(9,'app','AppId','domain','{ \"name\": \"appId\",\"index\": \"appId\",\"width\": \"40\",\n                                                                                                             \"editable\": \"true\"\n                                                                                                           }',2,'2018-05-10 12:21:16','0'),(10,'app','名称','name','{ \"name\": \"name\",\"index\": \"name\",\"width\": \"50\",\n                                                                                                             \"editable\": \"true\" }',3,'2018-05-10 12:21:16','0'),(11,'app','秘钥','securityCode','{ \"name\": \"securityCode\",\"index\": \"securityCode\",\"width\": \"50\",\n                                                                                                             \"editable\": \"true\" }',4,'2018-05-10 12:21:17','0'),(12,'app','白名单','whileListIp','{ \"name\": \"whileListIp\",\"index\": \"whileListIp\",\"width\": \"90\",\n                                                                                                             \"editable\": \"true\" }',5,'2018-05-10 12:21:17','0'),(13,'app','黑名单','blackListIp','{ \"name\": \"blackListIp\",\"index\": \"blackListIp\",\"width\": \"40\",\n                                                                                                             \"editable\": \"true\" }',6,'2018-05-10 12:21:17','0'),(14,'app','备注','value','{ \"name\": \"remark\",\"index\": \"remark\",\"width\": \"90\",\n                                                                                                             \"editable\": \"true\" }',7,'2018-05-10 12:21:17','0'),(15,'app','BUTTON','BUTTON',' {\r\n 	\"name\": \"detail\",\r\n 	\"index\": \"\",\r\n 	\"width\": \"40\",\r\n 	\"fixed\": \"true\",\r\n 	\"sortable\": \"false\",\r\n 	\"resize\": \"false\",\r\n 	\"formatter\": \"function(cellvalue, options, rowObject){ var temp =\\\"<div style=\'margin-left:8px;\'><div title=\'详情记录\' style=\'float:left;cursor:pointer;\' class=\'ui-pg-div\' id=\'jEditButton_3\' onclick=\'detail(\\\" + rowObject + \\\")\' onmouseover=\'jQuery(this).addClass(\'ui-state-hover\');\' onmouseout=\'jQuery(this).removeClass(\'ui-state-hover\');\'><span class=\'ui-icon fa-search-plus\'/></div></div>\\\";return temp; }\"\r\n }',8,'2018-05-10 12:21:17','0'),(16,'service','服务ID','serviceId','{ \"name\": \"serviceId\",\"index\": \"serviceId\",\"width\": \"20\",\n                                                                                                             \"editable\": \"true\",\n                                                                                                             \"sorttype\": \"int\" }',1,'2018-05-10 12:21:17','0'),(17,'service','服务编码','serviceCode','{ \"name\": \"serviceCode\",\"index\": \"serviceCode\",\"width\": \"40\",\n                                                                                                             \"editable\": \"true\"\n                                                                                                           }',2,'2018-05-10 12:21:17','0'),(18,'service','调用方式','invokeModel','{ \"name\": \"invokeModel\",\"index\": \"invokeModel\",\"width\": \"50\",\n                                                                                                             \"editable\": \"true\" }',3,'2018-05-10 12:21:17','0'),(19,'service','业务类型','businessTypeCd','{ \"name\": \"businessTypeCd\",\"index\": \"businessTypeCd\",\"width\": \"50\",\n                                                                                                             \"editable\": \"true\" }',4,'2018-05-10 12:21:17','0'),(20,'service','名称','name','{ \"name\": \"name\",\"index\": \"name\",\"width\": \"40\",\n                                                                                                             \"editable\": \"true\" }',5,'2018-05-10 12:21:17','0'),(21,'service','消息队列','messageQueueName','{ \"name\": \"messageQueueName\",\"index\": \"messageQueueName\",\"width\": \"10\",\n                                                                                                             \"editable\": \"true\" }',6,'2018-05-10 12:21:17','0'),(22,'service','URL','url','{ \"name\": \"url\",\"index\": \"url\",\"width\": \"60\",\n                                                                                                             \"editable\": \"true\" }',7,'2018-05-10 12:21:17','0'),(23,'service','提供者AppId','provideAppId','{ \"name\": \"provideAppId\",\"index\": \"provideAppId\",\"width\": \"10\",\n                                                                                                             \"editable\": \"true\" }',8,'2018-05-10 12:21:17','0'),(24,'service','BUTTON','BUTTON',' {\r\n 	\"name\": \"detail\",\r\n 	\"index\": \"\",\r\n 	\"width\": \"40\",\r\n 	\"fixed\": \"true\",\r\n 	\"sortable\": \"false\",\r\n 	\"resize\": \"false\",\r\n 	\"formatter\": \"function(cellvalue, options, rowObject){ var temp =\\\"<div style=\'margin-left:8px;\'><div title=\'详情记录\' style=\'float:left;cursor:pointer;\' class=\'ui-pg-div\' id=\'jEditButton_3\' onclick=\'detail(\\\" + rowObject + \\\")\' onmouseover=\'jQuery(this).addClass(\'ui-state-hover\');\' onmouseout=\'jQuery(this).removeClass(\'ui-state-hover\');\'><span class=\'ui-icon fa-search-plus\'/></div></div>\\\";return temp; }\"\r\n }',9,'2018-05-10 12:21:17','0'),(25,'route','路由ID','id','{ \"name\": \"id\",\"index\": \"id\",\"width\": \"10\",\n                                                                                                             \"editable\": \"true\",\n                                                                                                             \"sorttype\": \"int\" }',1,'2018-05-10 12:21:17','0'),(26,'route','AppId','appId','{ \"name\": \"appId\",\"index\": \"appId\",\"width\": \"30\",\n                                                                                                             \"editable\": \"true\"\n                                                                                                           }',2,'2018-05-10 12:21:17','0'),(27,'route','服务ID','serviceId','{ \"name\": \"serviceId\",\"index\": \"serviceId\",\"width\": \"30\",\n                                                                                                             \"editable\": \"true\" }',3,'2018-05-10 12:21:17','0'),(28,'route','服务名称','serviceName','{ \"name\": \"serviceName\",\"index\": \"serviceName\",\"width\": \"30\",\n                                                                                                             \"editable\": \"true\" }',4,'2018-05-10 12:21:17','0'),(29,'route','服务编码','serviceCode','{ \"name\": \"serviceCode\",\"index\": \"serviceCode\",\"width\": \"30\",\n                                                                                                             \"editable\": \"true\" }',5,'2018-05-10 12:21:17','0'),(30,'route','订单类型','orderTypeCd','{ \"name\": \"orderTypeCd\",\"index\": \"orderTypeCd\",\"width\": \"30\",\n                                                                                                             \"editable\": \"true\" }',6,'2018-05-10 12:21:17','0'),(31,'route','调用次数限制','invokelimitTimes','{ \"name\": \"invokelimitTimes\",\"index\": \"invokelimitTimes\",\"width\": \"40\",\n                                                                                                             \"editable\": \"true\" }',7,'2018-05-10 12:21:18','0'),(32,'route','BUTTON','BUTTON',' {\r\n 	\"name\": \"detail\",\r\n 	\"index\": \"\",\r\n 	\"width\": \"40\",\r\n 	\"fixed\": \"true\",\r\n 	\"sortable\": \"false\",\r\n 	\"resize\": \"false\",\r\n 	\"formatter\": \"function(cellvalue, options, rowObject){ var temp =\\\"<div style=\'margin-left:8px;\'><div title=\'详情记录\' style=\'float:left;cursor:pointer;\' class=\'ui-pg-div\' id=\'jEditButton_3\' onclick=\'detail(\\\" + rowObject + \\\")\' onmouseover=\'jQuery(this).addClass(\'ui-state-hover\');\' onmouseout=\'jQuery(this).removeClass(\'ui-state-hover\');\'><span class=\'ui-icon fa-search-plus\'/></div></div>\\\";return temp; }\"\r\n }',8,'2018-05-10 12:21:18','0'),(33,'cache','缓存ID','id','{ \"name\": \"id\",\"index\": \"id\",\"width\": \"10\",\n                                                                                                             \"editable\": \"true\",\n                                                                                                             \"sorttype\": \"int\" }',1,'2018-05-10 14:36:01','0'),(34,'cache','缓存编码','cacheCode','{ \"name\": \"cacheCode\",\"index\": \"cacheCode\",\"width\": \"20\",\n                                                                                                             \"editable\": \"true\" }',2,'2018-05-10 14:36:01','0'),(35,'cache','缓存名称','cacheName','{ \"name\": \"cacheName\",\"index\": \"cacheName\",\"width\": \"50\",\n                                                                                                             \"editable\": \"true\" }',3,'2018-05-10 14:36:01','0'),(36,'cache','BUTTON','BUTTON',' {\r\n 	\"name\": \"detail\",\r\n 	\"index\": \"\",\r\n 	\"width\": \"120\",\r\n 	\"fixed\": \"true\",\r\n 	\"sortable\": \"false\",\r\n 	\"resize\": \"false\",\r\n 	\"formatter\": \"function(cellvalue, options, rowObject){ var temp =\\\"<div style=\'margin-left:8px;\'><button type=\'button\' class=\'btn btn-warning\' style=\'border-radius: .25rem;\' onclick=\'flush(this,\\\" + rowObject.cacheCode + \\\")\'>刷新缓存</button> </div>\\\";return temp; }\"\r\n }',4,'2018-05-10 14:36:01','0');

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
  `seq` int(11) NOT NULL COMMENT '顺序',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `status_cd` varchar(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考c_status表，0在用，1失效',
  PRIMARY KEY (`m_id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

/*Data for the table `m_menu` */

insert  into `m_menu`(`m_id`,`name`,`level`,`parent_id`,`menu_group`,`user_id`,`remark`,`seq`,`create_time`,`status_cd`) values (1,'系统配置','1',-1,'LEFT','10001','',1,'2018-05-07 12:12:36','0'),(2,'映射管理','2',1,'LEFT','10001','',2,'2018-05-07 12:12:36','0'),(3,'外部应用','2',1,'LEFT','10001','',3,'2018-05-07 12:12:36','0'),(4,'路由管理','2',1,'LEFT','10001','',5,'2018-05-07 12:12:36','0'),(5,'服务管理','2',1,'LEFT','10001','',4,'2018-05-07 12:12:36','0'),(6,'刷新缓存','2',7,'LEFT','10001','',2,'2018-05-07 12:12:36','0'),(7,'缓存管理','1',-1,'LEFT','10001','',1,'2018-05-10 12:17:47','0');

/*Table structure for table `m_menu_2_user` */

DROP TABLE IF EXISTS `m_menu_2_user`;

CREATE TABLE `m_menu_2_user` (
  `m_user_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '菜单用户ID',
  `m_id` int(11) NOT NULL COMMENT '菜单id',
  `user_id` varchar(100) NOT NULL COMMENT '用户id',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `status_cd` varchar(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考c_status表，0在用，1失效',
  PRIMARY KEY (`m_user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

/*Data for the table `m_menu_2_user` */

insert  into `m_menu_2_user`(`m_user_id`,`m_id`,`user_id`,`create_time`,`status_cd`) values (1,1,'10001','2018-05-07 12:12:37','0'),(2,2,'10001','2018-05-07 12:12:37','0'),(3,3,'10001','2018-05-07 12:12:37','0'),(4,4,'10001','2018-05-07 12:12:37','0'),(5,5,'10001','2018-05-07 12:12:37','0'),(6,6,'10001','2018-05-10 12:20:31','0'),(7,7,'10001','2018-05-10 12:20:31','0');

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

insert  into `m_menu_ctg`(`m_ctg_id`,`m_id`,`url`,`template`,`create_time`,`status_cd`) values (1,1,'#','','2018-05-07 12:12:36','0'),(2,2,'/console/list?templateCode=mapping','','2018-05-07 12:12:37','0'),(3,3,'/console/list?templateCode=app','','2018-05-07 12:12:37','0'),(4,4,'/console/list?templateCode=route','','2018-05-07 12:12:37','0'),(5,5,'/console/list?templateCode=service','','2018-05-07 12:12:37','0'),(6,7,'#','','2018-05-10 12:18:34','0'),(7,6,'/console/list?templateCode=cache','','2018-05-10 12:18:34','0');

/*Table structure for table `spec` */

DROP TABLE IF EXISTS `spec`;

CREATE TABLE `spec` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `spec_cd` varchar(4) NOT NULL COMMENT 'ä¸šåŠ¡é¡¹ç±»åž‹è§„æ ¼ç¼–ç ï¼Œä»Žx00020001å¼€å§‹æ¯æ¬¡åŠ ä¸€å°±å¯ä»¥(çº¦å®šï¼Œx=10è¡¨ç¤ºc_orders_attrs ä¸­å±žæ€§ï¼Œx=11è¡¨ç¤ºc_business_attrs ä¸­çš„å±žæ€§)',
  `name` varchar(50) NOT NULL COMMENT 'åç§°',
  `description` varchar(200) DEFAULT NULL COMMENT 'æè¿°',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'åˆ›å»ºæ—¶é—´',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `spec` */

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
