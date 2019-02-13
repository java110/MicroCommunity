
CREATE USER 'TT'@'%' IDENTIFIED BY 'TT@12345678';

GRANT ALL ON *.* TO 'TT'@'%';

create database `TT` default character set utf8 collate utf8_general_ci;

use TT;
-- c_orders
set character set utf8;

--
-- Table structure for table `a_agent`
--

DROP TABLE IF EXISTS `a_agent`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `a_agent` (
  `agent_id` varchar(30) NOT NULL COMMENT '代理商ID',
  `b_id` varchar(30) NOT NULL COMMENT '业务Id',
  `name` varchar(100) NOT NULL COMMENT '代理商名称',
  `address` varchar(200) NOT NULL COMMENT '代理商地址',
  `tel` varchar(11) NOT NULL COMMENT '电话',
  `nearby_landmarks` varchar(200) DEFAULT NULL COMMENT '地标，如王府井北60米',
  `map_x` varchar(20) NOT NULL COMMENT '地区 x坐标',
  `map_y` varchar(20) NOT NULL COMMENT '地区 y坐标',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `status_cd` varchar(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考c_status表，S 保存，0, 在用 1失效',
  UNIQUE KEY `agent_id` (`agent_id`),
  UNIQUE KEY `idx_agent_agent_id` (`agent_id`),
  KEY `idx_agent_b_id` (`b_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `a_agent`
--

LOCK TABLES `a_agent` WRITE;
/*!40000 ALTER TABLE `a_agent` DISABLE KEYS */;
INSERT INTO `a_agent` VALUES ('8020181223000001','60234567894','方博家园123','青海省西宁市城中区129号123','17797173952','王府井旁40米','102.801909','37.597263','2018-12-23 08:52:58','0');
/*!40000 ALTER TABLE `a_agent` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `a_agent_attr`
--

DROP TABLE IF EXISTS `a_agent_attr`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `a_agent_attr` (
  `b_id` varchar(30) NOT NULL COMMENT '订单ID',
  `attr_id` varchar(30) NOT NULL COMMENT '属性id',
  `agent_id` varchar(30) NOT NULL COMMENT '用户ID',
  `spec_cd` varchar(12) NOT NULL COMMENT '规格id,参考spec表',
  `VALUE` varchar(50) NOT NULL COMMENT '属性值',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `status_cd` varchar(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考c_status表，S 保存，0, 在用 1失效',
  UNIQUE KEY `attr_id` (`attr_id`),
  KEY `idx_agent_attr_b_id` (`b_id`),
  KEY `idx_agent_attr_agent_id` (`agent_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `a_agent_attr`
--

LOCK TABLES `a_agent_attr` WRITE;
/*!40000 ALTER TABLE `a_agent_attr` DISABLE KEYS */;
INSERT INTO `a_agent_attr` VALUES ('19234567894','112018122300000002','8020181223000001','1001','02','2018-12-23 08:52:58','1');
/*!40000 ALTER TABLE `a_agent_attr` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `a_agent_cerdentials`
--

DROP TABLE IF EXISTS `a_agent_cerdentials`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `a_agent_cerdentials` (
  `agent_cerdentials_id` varchar(30) NOT NULL COMMENT '代理商证件ID',
  `b_id` varchar(30) NOT NULL COMMENT '业务Id',
  `agent_id` varchar(30) NOT NULL COMMENT '代理商ID',
  `credentials_cd` varchar(12) NOT NULL COMMENT '证件类型，对应于 credentials表',
  `value` varchar(50) NOT NULL COMMENT '证件号码',
  `validity_period` date NOT NULL COMMENT '有效期，如果是长期有效 写成 3000/1/1',
  `positive_photo` varchar(100) DEFAULT NULL COMMENT '正面照片',
  `negative_photo` varchar(100) DEFAULT NULL COMMENT '反面照片',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `status_cd` varchar(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考c_status表，S 保存，0, 在用 1失效',
  UNIQUE KEY `agent_cerdentials_id` (`agent_cerdentials_id`),
  KEY `idx_agent_cerdentials_b_id` (`b_id`),
  KEY `idx_agent_cerdentials_agent_id` (`agent_id`),
  KEY `idx__agent_cerdentials_id` (`agent_cerdentials_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `a_agent_cerdentials`
--

LOCK TABLES `a_agent_cerdentials` WRITE;
/*!40000 ALTER TABLE `a_agent_cerdentials` DISABLE KEYS */;
INSERT INTO `a_agent_cerdentials` VALUES ('8220181223000004','116234567894','8020181223000001','1','632126XXXXXXXX2012','3000-02-01','1234567.jpg','11.jpg','2018-12-23 08:57:11','1');
/*!40000 ALTER TABLE `a_agent_cerdentials` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `a_agent_photo`
--

DROP TABLE IF EXISTS `a_agent_photo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `a_agent_photo` (
  `agent_photo_id` varchar(30) NOT NULL COMMENT '代理商照片ID',
  `b_id` varchar(30) NOT NULL COMMENT '业务Id',
  `agent_id` varchar(30) NOT NULL COMMENT '代理商ID',
  `agent_photo_type_cd` varchar(12) NOT NULL COMMENT '代理商照片类型,T 门头照 I 内景照',
  `photo` varchar(100) NOT NULL COMMENT '照片',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `status_cd` varchar(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考c_status表，S 保存，0, 在用 1失效',
  UNIQUE KEY `agent_photo_id` (`agent_photo_id`),
  KEY `idx_agent_photo_b_id` (`b_id`),
  KEY `idx_agent_photo_agent_id` (`agent_id`),
  KEY `idx_agent_photo_agent_photo_id` (`agent_photo_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `a_agent_photo`
--

LOCK TABLES `a_agent_photo` WRITE;
/*!40000 ALTER TABLE `a_agent_photo` DISABLE KEYS */;
INSERT INTO `a_agent_photo` VALUES ('8120181223000003','95234567894','8020181223000001','T','123456789.jpg','2018-12-23 08:55:17','1');
/*!40000 ALTER TABLE `a_agent_photo` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `a_agent_user`
--

DROP TABLE IF EXISTS `a_agent_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `a_agent_user` (
  `agent_user_id` varchar(30) NOT NULL COMMENT '代理商用户ID',
  `agent_id` varchar(30) NOT NULL COMMENT '代理商ID',
  `b_id` varchar(30) NOT NULL COMMENT '业务Id',
  `user_id` varchar(30) NOT NULL COMMENT '用户ID',
  `rel_cd` varchar(30) NOT NULL COMMENT '用户和代理商关系 详情查看 agent_user_rel表',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `status_cd` varchar(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考c_status表，S 保存，0, 在用 1失效',
  UNIQUE KEY `agent_user_id` (`agent_user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `a_agent_user`
--

LOCK TABLES `a_agent_user` WRITE;
/*!40000 ALTER TABLE `a_agent_user` DISABLE KEYS */;
INSERT INTO `a_agent_user` VALUES ('8320181223000005','8020181223000001','31234567894','123','600311000001','2018-12-23 08:58:41','1');
/*!40000 ALTER TABLE `a_agent_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `agent_user_rel`
--

DROP TABLE IF EXISTS `agent_user_rel`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `agent_user_rel` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `rel_cd` varchar(12) NOT NULL COMMENT '代理商用户关系编码',
  `name` varchar(50) NOT NULL COMMENT '代理商用户关系编码名称',
  `description` varchar(200) DEFAULT NULL COMMENT '描述',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `rel_cd` (`rel_cd`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `agent_user_rel`
--

LOCK TABLES `agent_user_rel` WRITE;
/*!40000 ALTER TABLE `agent_user_rel` DISABLE KEYS */;
/*!40000 ALTER TABLE `agent_user_rel` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `business_agent`
--

DROP TABLE IF EXISTS `business_agent`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `business_agent` (
  `agent_id` varchar(30) NOT NULL COMMENT '代理商ID',
  `b_id` varchar(30) NOT NULL COMMENT '业务Id',
  `name` varchar(100) NOT NULL COMMENT '代理商名称',
  `address` varchar(200) NOT NULL COMMENT '代理商地址',
  `tel` varchar(11) NOT NULL COMMENT '电话',
  `nearby_landmarks` varchar(200) DEFAULT NULL COMMENT '地标，如王府井北60米',
  `map_x` varchar(20) NOT NULL COMMENT '地区 x坐标',
  `map_y` varchar(20) NOT NULL COMMENT '地区 y坐标',
  `month` int(11) NOT NULL COMMENT '月份',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `operate` varchar(3) NOT NULL COMMENT '数据状态，添加ADD，修改MOD 删除DEL',
  KEY `idx_business_agent_id` (`agent_id`),
  KEY `idx_business_agent_b_id` (`b_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8
/*!50100 PARTITION BY RANGE (`month`)
(PARTITION business_agent_1 VALUES LESS THAN (2) ENGINE = InnoDB,
 PARTITION business_agent_2 VALUES LESS THAN (3) ENGINE = InnoDB,
 PARTITION business_agent_3 VALUES LESS THAN (4) ENGINE = InnoDB,
 PARTITION business_agent_4 VALUES LESS THAN (5) ENGINE = InnoDB,
 PARTITION business_agent_5 VALUES LESS THAN (6) ENGINE = InnoDB,
 PARTITION business_agent_6 VALUES LESS THAN (7) ENGINE = InnoDB,
 PARTITION business_agent_7 VALUES LESS THAN (8) ENGINE = InnoDB,
 PARTITION business_agent_8 VALUES LESS THAN (9) ENGINE = InnoDB,
 PARTITION business_agent_9 VALUES LESS THAN (10) ENGINE = InnoDB,
 PARTITION business_agent_10 VALUES LESS THAN (11) ENGINE = InnoDB,
 PARTITION business_agent_11 VALUES LESS THAN (12) ENGINE = InnoDB,
 PARTITION business_agent_12 VALUES LESS THAN (13) ENGINE = InnoDB) */;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `business_agent`
--

LOCK TABLES `business_agent` WRITE;
/*!40000 ALTER TABLE `business_agent` DISABLE KEYS */;
INSERT INTO `business_agent` VALUES ('8020181223000001','3234567894','方博家园代理商','青海省西宁市城中区129号','17797173942','王府井旁30米','101.801909','36.597263',12,'2018-12-23 08:52:15','ADD'),('8020181223000001','60234567894','方博家园代理商','青海省西宁市城中区129号','17797173942','王府井旁30米','101.801909','36.597263',12,'2018-12-23 09:00:29','DEl'),('8020181223000001','60234567894','方博家园123','青海省西宁市城中区129号123','17797173952','王府井旁40米','102.801909','37.597263',12,'2018-12-23 09:00:29','ADD');
/*!40000 ALTER TABLE `business_agent` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `business_agent_attr`
--

DROP TABLE IF EXISTS `business_agent_attr`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `business_agent_attr` (
  `b_id` varchar(30) NOT NULL COMMENT '订单ID',
  `attr_id` varchar(30) NOT NULL COMMENT '属性id',
  `agent_id` varchar(30) NOT NULL COMMENT '用户ID',
  `spec_cd` varchar(12) NOT NULL COMMENT '规格id,参考spec表',
  `value` varchar(50) NOT NULL COMMENT '属性值',
  `month` int(11) NOT NULL COMMENT '月份',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `operate` varchar(3) NOT NULL COMMENT '数据状态，添加ADD，修改MOD 删除DEL',
  KEY `idx_business_attr_agent_id` (`agent_id`),
  KEY `idx_business_agent_attr_b_id` (`b_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8
/*!50100 PARTITION BY RANGE (`month`)
(PARTITION business_agent_attr_1 VALUES LESS THAN (2) ENGINE = InnoDB,
 PARTITION business_agent_attr_2 VALUES LESS THAN (3) ENGINE = InnoDB,
 PARTITION business_agent_attr_3 VALUES LESS THAN (4) ENGINE = InnoDB,
 PARTITION business_agent_attr_4 VALUES LESS THAN (5) ENGINE = InnoDB,
 PARTITION business_agent_attr_5 VALUES LESS THAN (6) ENGINE = InnoDB,
 PARTITION business_agent_attr_6 VALUES LESS THAN (7) ENGINE = InnoDB,
 PARTITION business_agent_attr_7 VALUES LESS THAN (8) ENGINE = InnoDB,
 PARTITION business_agent_attr_8 VALUES LESS THAN (9) ENGINE = InnoDB,
 PARTITION business_agent_attr_9 VALUES LESS THAN (10) ENGINE = InnoDB,
 PARTITION business_agent_attr_10 VALUES LESS THAN (11) ENGINE = InnoDB,
 PARTITION business_agent_attr_11 VALUES LESS THAN (12) ENGINE = InnoDB,
 PARTITION business_agent_attr_12 VALUES LESS THAN (13) ENGINE = InnoDB) */;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `business_agent_attr`
--

LOCK TABLES `business_agent_attr` WRITE;
/*!40000 ALTER TABLE `business_agent_attr` DISABLE KEYS */;
INSERT INTO `business_agent_attr` VALUES ('3234567894','112018122300000002','8020181223000001','1001','01',12,'2018-12-23 08:52:15','ADD'),('60234567894','112018122300000002','8020181223000001','1001','01',12,'2018-12-23 09:00:29','DEl'),('60234567894','112018122300000002','8020181223000001','1001','02',12,'2018-12-23 09:00:29','ADD'),('19234567894','112018122300000002','8020181223000001','1001','02',12,'2018-12-23 09:07:54','DEl');
/*!40000 ALTER TABLE `business_agent_attr` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `business_agent_cerdentials`
--

DROP TABLE IF EXISTS `business_agent_cerdentials`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `business_agent_cerdentials` (
  `agent_cerdentials_id` varchar(30) NOT NULL COMMENT '代理商证件ID',
  `b_id` varchar(30) NOT NULL COMMENT '业务Id',
  `agent_id` varchar(30) NOT NULL COMMENT '代理商ID',
  `credentials_cd` varchar(12) NOT NULL COMMENT '证件类型，对应于 credentials表',
  `value` varchar(50) NOT NULL COMMENT '证件号码',
  `validity_period` date NOT NULL COMMENT '有效期，如果是长期有效 写成 3000/1/1',
  `positive_photo` varchar(100) DEFAULT NULL COMMENT '正面照片',
  `negative_photo` varchar(100) DEFAULT NULL COMMENT '反面照片',
  `month` int(11) NOT NULL COMMENT '月份',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `operate` varchar(3) NOT NULL COMMENT '数据状态，添加ADD，修改MOD 删除DEL',
  KEY `idx_business_cerdentials_agent_id` (`agent_id`),
  KEY `idx_business_agent_cerdentials_b_id` (`b_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8
/*!50100 PARTITION BY RANGE (`month`)
(PARTITION business_agent_cerdentials_1 VALUES LESS THAN (2) ENGINE = InnoDB,
 PARTITION business_agent_cerdentials_2 VALUES LESS THAN (3) ENGINE = InnoDB,
 PARTITION business_agent_cerdentials_3 VALUES LESS THAN (4) ENGINE = InnoDB,
 PARTITION business_agent_cerdentials_4 VALUES LESS THAN (5) ENGINE = InnoDB,
 PARTITION business_agent_cerdentials_5 VALUES LESS THAN (6) ENGINE = InnoDB,
 PARTITION business_agent_cerdentials_6 VALUES LESS THAN (7) ENGINE = InnoDB,
 PARTITION business_agent_cerdentials_7 VALUES LESS THAN (8) ENGINE = InnoDB,
 PARTITION business_agent_cerdentials_8 VALUES LESS THAN (9) ENGINE = InnoDB,
 PARTITION business_agent_cerdentials_9 VALUES LESS THAN (10) ENGINE = InnoDB,
 PARTITION business_agent_cerdentials_10 VALUES LESS THAN (11) ENGINE = InnoDB,
 PARTITION business_agent_cerdentials_11 VALUES LESS THAN (12) ENGINE = InnoDB,
 PARTITION business_agent_cerdentials_12 VALUES LESS THAN (13) ENGINE = InnoDB) */;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `business_agent_cerdentials`
--

LOCK TABLES `business_agent_cerdentials` WRITE;
/*!40000 ALTER TABLE `business_agent_cerdentials` DISABLE KEYS */;
INSERT INTO `business_agent_cerdentials` VALUES ('8220181223000004','5234567894','8020181223000001','1','632126XXXXXXXX2011','3000-01-01','正面照片地址，1234567.jpg','反面照片地址，没有不填写',12,'2018-12-23 08:57:05','ADD'),('8220181223000004','82234567894','8020181223000001','1','632126XXXXXXXX2011','3000-01-01','正面照片地址，1234567.jpg','反面照片地址，没有不填写',12,'2018-12-23 09:03:20','DEl'),('8220181223000004','82234567894','8020181223000001','1','632126XXXXXXXX2012','3000-02-01','1234567.jpg','11.jpg',12,'2018-12-23 09:03:20','ADD'),('8220181223000004','116234567894','8020181223000001','1','632126XXXXXXXX2012','3000-02-01','1234567.jpg','11.jpg',12,'2018-12-23 09:05:50','DEl');
/*!40000 ALTER TABLE `business_agent_cerdentials` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `business_agent_photo`
--

DROP TABLE IF EXISTS `business_agent_photo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `business_agent_photo` (
  `agent_photo_id` varchar(30) NOT NULL COMMENT '代理商照片ID',
  `b_id` varchar(30) NOT NULL COMMENT '业务Id',
  `agent_id` varchar(30) NOT NULL COMMENT '代理商ID',
  `agent_photo_type_cd` varchar(12) NOT NULL COMMENT '代理商照片类型,T 门头照 I 内景照',
  `photo` varchar(100) NOT NULL COMMENT '照片',
  `month` int(11) NOT NULL COMMENT '月份',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `operate` varchar(3) NOT NULL COMMENT '数据状态，添加ADD，修改MOD 删除DEL',
  KEY `idx_business_photo_agent_id` (`agent_id`),
  KEY `idx_business_agent_photo_b_id` (`b_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8
/*!50100 PARTITION BY RANGE (`month`)
(PARTITION business_agent_photo_1 VALUES LESS THAN (2) ENGINE = InnoDB,
 PARTITION business_agent_photo_2 VALUES LESS THAN (3) ENGINE = InnoDB,
 PARTITION business_agent_photo_3 VALUES LESS THAN (4) ENGINE = InnoDB,
 PARTITION business_agent_photo_4 VALUES LESS THAN (5) ENGINE = InnoDB,
 PARTITION business_agent_photo_5 VALUES LESS THAN (6) ENGINE = InnoDB,
 PARTITION business_agent_photo_6 VALUES LESS THAN (7) ENGINE = InnoDB,
 PARTITION business_agent_photo_7 VALUES LESS THAN (8) ENGINE = InnoDB,
 PARTITION business_agent_photo_8 VALUES LESS THAN (9) ENGINE = InnoDB,
 PARTITION business_agent_photo_9 VALUES LESS THAN (10) ENGINE = InnoDB,
 PARTITION business_agent_photo_10 VALUES LESS THAN (11) ENGINE = InnoDB,
 PARTITION business_agent_photo_11 VALUES LESS THAN (12) ENGINE = InnoDB,
 PARTITION business_agent_photo_12 VALUES LESS THAN (13) ENGINE = InnoDB) */;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `business_agent_photo`
--

LOCK TABLES `business_agent_photo` WRITE;
/*!40000 ALTER TABLE `business_agent_photo` DISABLE KEYS */;
INSERT INTO `business_agent_photo` VALUES ('8120181223000003','4234567894','8020181223000001','T','12345678.jpg',12,'2018-12-23 08:55:10','ADD'),('8120181223000003','71234567894','8020181223000001','T','12345678.jpg',12,'2018-12-23 09:01:58','DEl'),('8120181223000003','71234567894','8020181223000001','T','123456789.jpg',12,'2018-12-23 09:01:58','ADD'),('8120181223000003','95234567894','8020181223000001','T','123456789.jpg',12,'2018-12-23 09:04:36','DEl');
/*!40000 ALTER TABLE `business_agent_photo` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `business_agent_user`
--

DROP TABLE IF EXISTS `business_agent_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `business_agent_user` (
  `agent_user_id` varchar(30) NOT NULL COMMENT '代理商用户ID',
  `agent_id` varchar(30) NOT NULL COMMENT '代理商ID',
  `b_id` varchar(30) NOT NULL COMMENT '业务Id',
  `user_id` varchar(30) NOT NULL COMMENT '用户ID',
  `rel_cd` varchar(30) NOT NULL COMMENT '用户和代理商关系 详情查看 agent_user_rel表',
  `month` int(11) NOT NULL COMMENT '月份',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `operate` varchar(3) NOT NULL COMMENT '数据状态，添加ADD，修改MOD 删除DEL'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `business_agent_user`
--

LOCK TABLES `business_agent_user` WRITE;
/*!40000 ALTER TABLE `business_agent_user` DISABLE KEYS */;
INSERT INTO `business_agent_user` VALUES ('8320181223000005','8020181223000001','5234567894','123','600311000001',12,'2018-12-23 08:58:34','ADD'),('8320181223000005','8020181223000001','31234567894','123','600311000001',12,'2018-12-23 09:06:55','DEl');
/*!40000 ALTER TABLE `business_agent_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `business_community`
--

DROP TABLE IF EXISTS `business_community`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `business_community` (
  `community_id` varchar(30) NOT NULL COMMENT '小区ID',
  `b_id` varchar(30) NOT NULL COMMENT '业务Id',
  `name` varchar(100) NOT NULL COMMENT '小区名称',
  `address` varchar(200) NOT NULL COMMENT '小区地址',
  `city_code` varchar(12) NOT NULL COMMENT '根据定位获取城市编码',
  `nearby_landmarks` varchar(200) DEFAULT NULL COMMENT '地标，如王府井北60米',
  `map_x` varchar(20) NOT NULL COMMENT '地区 x坐标',
  `map_y` varchar(20) NOT NULL COMMENT '地区 y坐标',
  `month` int(11) NOT NULL COMMENT '月份',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `operate` varchar(3) NOT NULL COMMENT '数据状态，添加ADD，修改MOD 删除DEL',
  KEY `idx_business_community_id` (`community_id`),
  KEY `idx_business_community_b_id` (`b_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8
/*!50100 PARTITION BY RANGE (`month`)
(PARTITION business_community_1 VALUES LESS THAN (2) ENGINE = InnoDB,
 PARTITION business_community_2 VALUES LESS THAN (3) ENGINE = InnoDB,
 PARTITION business_community_3 VALUES LESS THAN (4) ENGINE = InnoDB,
 PARTITION business_community_4 VALUES LESS THAN (5) ENGINE = InnoDB,
 PARTITION business_community_5 VALUES LESS THAN (6) ENGINE = InnoDB,
 PARTITION business_community_6 VALUES LESS THAN (7) ENGINE = InnoDB,
 PARTITION business_community_7 VALUES LESS THAN (8) ENGINE = InnoDB,
 PARTITION business_community_8 VALUES LESS THAN (9) ENGINE = InnoDB,
 PARTITION business_community_9 VALUES LESS THAN (10) ENGINE = InnoDB,
 PARTITION business_community_10 VALUES LESS THAN (11) ENGINE = InnoDB,
 PARTITION business_community_11 VALUES LESS THAN (12) ENGINE = InnoDB,
 PARTITION business_community_12 VALUES LESS THAN (13) ENGINE = InnoDB) */;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `business_community`
--

LOCK TABLES `business_community` WRITE;
/*!40000 ALTER TABLE `business_community` DISABLE KEYS */;
INSERT INTO `business_community` VALUES ('7020181217000001','1234567890','方博家园','青海省西宁市城中区129号','100010','王府井旁30米','101.801909','36.597263',12,'2018-12-17 04:52:55','ADD'),('7020181217000001','1234567891','方博家园','青海省西宁市城中区129号','100010','王府井旁30米','101.801909','36.597263',12,'2018-12-17 05:06:14','DEl'),('7020181217000001','1234567891','万博家博园（城西区）','青海省西宁市城中区129号','100010','王府井旁30米','101.801909','36.597263',12,'2018-12-17 05:06:14','ADD');
/*!40000 ALTER TABLE `business_community` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `business_community_attr`
--

DROP TABLE IF EXISTS `business_community_attr`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `business_community_attr` (
  `b_id` varchar(30) NOT NULL COMMENT '订单ID',
  `attr_id` varchar(30) NOT NULL COMMENT '属性id',
  `community_id` varchar(30) NOT NULL COMMENT '用户ID',
  `spec_cd` varchar(12) NOT NULL COMMENT '规格id,参考spec表',
  `value` varchar(50) NOT NULL COMMENT '属性值',
  `month` int(11) NOT NULL COMMENT '月份',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `operate` varchar(3) NOT NULL COMMENT '数据状态，添加ADD，修改MOD 删除DEL',
  KEY `idx_business_attr_community_id` (`community_id`),
  KEY `idx_business_community_attr_b_id` (`b_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8
/*!50100 PARTITION BY RANGE (`month`)
(PARTITION business_community_attr_1 VALUES LESS THAN (2) ENGINE = InnoDB,
 PARTITION business_community_attr_2 VALUES LESS THAN (3) ENGINE = InnoDB,
 PARTITION business_community_attr_3 VALUES LESS THAN (4) ENGINE = InnoDB,
 PARTITION business_community_attr_4 VALUES LESS THAN (5) ENGINE = InnoDB,
 PARTITION business_community_attr_5 VALUES LESS THAN (6) ENGINE = InnoDB,
 PARTITION business_community_attr_6 VALUES LESS THAN (7) ENGINE = InnoDB,
 PARTITION business_community_attr_7 VALUES LESS THAN (8) ENGINE = InnoDB,
 PARTITION business_community_attr_8 VALUES LESS THAN (9) ENGINE = InnoDB,
 PARTITION business_community_attr_9 VALUES LESS THAN (10) ENGINE = InnoDB,
 PARTITION business_community_attr_10 VALUES LESS THAN (11) ENGINE = InnoDB,
 PARTITION business_community_attr_11 VALUES LESS THAN (12) ENGINE = InnoDB,
 PARTITION business_community_attr_12 VALUES LESS THAN (13) ENGINE = InnoDB) */;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `business_community_attr`
--

LOCK TABLES `business_community_attr` WRITE;
/*!40000 ALTER TABLE `business_community_attr` DISABLE KEYS */;
INSERT INTO `business_community_attr` VALUES ('1234567890','112018121700000002','7020181217000001','1001','01',12,'2018-12-17 04:52:55','ADD'),('1234567891','112018121700000002','7020181217000001','1001','01',12,'2018-12-17 05:06:15','DEl'),('1234567891','112018121700000002','7020181217000001','1001','01',12,'2018-12-17 05:06:15','ADD');
/*!40000 ALTER TABLE `business_community_attr` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `business_community_member`
--

DROP TABLE IF EXISTS `business_community_member`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `business_community_member` (
  `community_member_id` varchar(30) NOT NULL COMMENT 'ID',
  `b_id` varchar(30) NOT NULL COMMENT '业务Id',
  `community_id` varchar(30) NOT NULL COMMENT '小区ID',
  `member_id` varchar(50) NOT NULL COMMENT '成员ID',
  `member_type_cd` varchar(12) NOT NULL COMMENT '成员类型见 community_member_type表',
  `month` int(11) NOT NULL COMMENT '月份',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `operate` varchar(3) NOT NULL COMMENT '数据状态，添加ADD，修改MOD 删除DEL',
  KEY `idx_business_member_community_id` (`community_id`),
  KEY `idx_business_community_member_b_id` (`b_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8
/*!50100 PARTITION BY RANGE (`month`)
(PARTITION business_community_member_1 VALUES LESS THAN (2) ENGINE = InnoDB,
 PARTITION business_community_member_2 VALUES LESS THAN (3) ENGINE = InnoDB,
 PARTITION business_community_member_3 VALUES LESS THAN (4) ENGINE = InnoDB,
 PARTITION business_community_member_4 VALUES LESS THAN (5) ENGINE = InnoDB,
 PARTITION business_community_member_5 VALUES LESS THAN (6) ENGINE = InnoDB,
 PARTITION business_community_member_6 VALUES LESS THAN (7) ENGINE = InnoDB,
 PARTITION business_community_member_7 VALUES LESS THAN (8) ENGINE = InnoDB,
 PARTITION business_community_member_8 VALUES LESS THAN (9) ENGINE = InnoDB,
 PARTITION business_community_member_9 VALUES LESS THAN (10) ENGINE = InnoDB,
 PARTITION business_community_member_10 VALUES LESS THAN (11) ENGINE = InnoDB,
 PARTITION business_community_member_11 VALUES LESS THAN (12) ENGINE = InnoDB,
 PARTITION business_community_member_12 VALUES LESS THAN (13) ENGINE = InnoDB) */;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `business_community_member`
--

LOCK TABLES `business_community_member` WRITE;
/*!40000 ALTER TABLE `business_community_member` DISABLE KEYS */;
INSERT INTO `business_community_member` VALUES ('7220181217000001','1234567891','7020181217000001','345678','390001200001',12,'2018-12-17 14:27:08','ADD'),('7220181217000001','1234567891','7020181217000001','3456789','390001200001',12,'2018-12-17 14:43:37','ADD'),('7220181217000002','1234567891','7020181217000001','3456789','390001200001',12,'2018-12-17 14:44:35','ADD'),('7220181217000003','1234567891','7020181217000001','3456789','390001200001',12,'2018-12-17 14:45:24','ADD'),('7220181217000004','1234567892','7020181217000001','3456789','390001200001',12,'2018-12-17 14:46:03','ADD'),('7220181217000004','1234567893','7020181217000001','3456789','390001200001',12,'2018-12-17 15:05:51','DEl'),('7220181217000004','1234567894','7020181217000001','3456789','390001200001',12,'2018-12-17 15:06:53','DEl');
/*!40000 ALTER TABLE `business_community_member` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `business_community_photo`
--

DROP TABLE IF EXISTS `business_community_photo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `business_community_photo` (
  `community_photo_id` varchar(30) NOT NULL COMMENT '商户照片ID',
  `b_id` varchar(30) NOT NULL COMMENT '业务Id',
  `community_id` varchar(30) NOT NULL COMMENT '小区ID',
  `community_photo_type_cd` varchar(12) NOT NULL COMMENT '商户照片类型,T 门头照 I 内景照',
  `photo` varchar(100) NOT NULL COMMENT '照片',
  `month` int(11) NOT NULL COMMENT '月份',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `operate` varchar(3) NOT NULL COMMENT '数据状态，添加ADD，修改MOD 删除DEL',
  KEY `idx_business_photo_community_id` (`community_id`),
  KEY `idx_business_community_photo_b_id` (`b_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8
/*!50100 PARTITION BY RANGE (`month`)
(PARTITION business_community_photo_1 VALUES LESS THAN (2) ENGINE = InnoDB,
 PARTITION business_community_photo_2 VALUES LESS THAN (3) ENGINE = InnoDB,
 PARTITION business_community_photo_3 VALUES LESS THAN (4) ENGINE = InnoDB,
 PARTITION business_community_photo_4 VALUES LESS THAN (5) ENGINE = InnoDB,
 PARTITION business_community_photo_5 VALUES LESS THAN (6) ENGINE = InnoDB,
 PARTITION business_community_photo_6 VALUES LESS THAN (7) ENGINE = InnoDB,
 PARTITION business_community_photo_7 VALUES LESS THAN (8) ENGINE = InnoDB,
 PARTITION business_community_photo_8 VALUES LESS THAN (9) ENGINE = InnoDB,
 PARTITION business_community_photo_9 VALUES LESS THAN (10) ENGINE = InnoDB,
 PARTITION business_community_photo_10 VALUES LESS THAN (11) ENGINE = InnoDB,
 PARTITION business_community_photo_11 VALUES LESS THAN (12) ENGINE = InnoDB,
 PARTITION business_community_photo_12 VALUES LESS THAN (13) ENGINE = InnoDB) */;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `business_community_photo`
--

LOCK TABLES `business_community_photo` WRITE;
/*!40000 ALTER TABLE `business_community_photo` DISABLE KEYS */;
INSERT INTO `business_community_photo` VALUES ('7120181217000003','1234567890','7020181217000001','T','12345678.jpg',12,'2018-12-17 04:52:55','ADD'),('7120181217000003','1234567891','7020181217000001','T','12345678.jpg',12,'2018-12-17 05:06:15','DEl'),('7120181217000003','1234567891','7020181217000001','T','12345678.jpg',12,'2018-12-17 05:06:15','ADD');
/*!40000 ALTER TABLE `business_community_photo` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `business_member_store`
--

DROP TABLE IF EXISTS `business_member_store`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `business_member_store` (
  `member_store_id` varchar(30) NOT NULL COMMENT 'ID',
  `b_id` varchar(30) NOT NULL COMMENT '业务Id',
  `store_id` varchar(30) NOT NULL COMMENT '商店ID',
  `member_id` varchar(50) NOT NULL COMMENT '商户成员ID',
  `month` int(11) NOT NULL COMMENT '月份',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `operate` varchar(3) NOT NULL COMMENT '数据状态，添加ADD，修改MOD 删除DEL',
  KEY `idx_business_member_store_store_id` (`store_id`),
  KEY `idx_business_member_store_b_id` (`b_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8
/*!50100 PARTITION BY RANGE (`month`)
(PARTITION business_member_store_1 VALUES LESS THAN (2) ENGINE = InnoDB,
 PARTITION business_member_store_2 VALUES LESS THAN (3) ENGINE = InnoDB,
 PARTITION business_member_store_3 VALUES LESS THAN (4) ENGINE = InnoDB,
 PARTITION business_member_store_4 VALUES LESS THAN (5) ENGINE = InnoDB,
 PARTITION business_member_store_5 VALUES LESS THAN (6) ENGINE = InnoDB,
 PARTITION business_member_store_6 VALUES LESS THAN (7) ENGINE = InnoDB,
 PARTITION business_member_store_7 VALUES LESS THAN (8) ENGINE = InnoDB,
 PARTITION business_member_store_8 VALUES LESS THAN (9) ENGINE = InnoDB,
 PARTITION business_member_store_9 VALUES LESS THAN (10) ENGINE = InnoDB,
 PARTITION business_member_store_10 VALUES LESS THAN (11) ENGINE = InnoDB,
 PARTITION business_member_store_11 VALUES LESS THAN (12) ENGINE = InnoDB,
 PARTITION business_member_store_12 VALUES LESS THAN (13) ENGINE = InnoDB) */;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `business_member_store`
--

LOCK TABLES `business_member_store` WRITE;
/*!40000 ALTER TABLE `business_member_store` DISABLE KEYS */;
/*!40000 ALTER TABLE `business_member_store` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `business_property`
--

DROP TABLE IF EXISTS `business_property`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `business_property` (
  `property_id` varchar(30) NOT NULL COMMENT '物业ID',
  `b_id` varchar(30) NOT NULL COMMENT '业务Id',
  `name` varchar(100) NOT NULL COMMENT '物业名称',
  `address` varchar(200) NOT NULL COMMENT '物业地址',
  `tel` varchar(11) NOT NULL COMMENT '电话',
  `nearby_landmarks` varchar(200) DEFAULT NULL COMMENT '地标，如王府井北60米',
  `map_x` varchar(20) NOT NULL COMMENT '地区 x坐标',
  `map_y` varchar(20) NOT NULL COMMENT '地区 y坐标',
  `month` int(11) NOT NULL COMMENT '月份',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `operate` varchar(3) NOT NULL COMMENT '数据状态，添加ADD，修改MOD 删除DEL',
  KEY `idx_business_property_id` (`property_id`),
  KEY `idx_business_property_b_id` (`b_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8
/*!50100 PARTITION BY RANGE (`month`)
(PARTITION business_property_1 VALUES LESS THAN (2) ENGINE = InnoDB,
 PARTITION business_property_2 VALUES LESS THAN (3) ENGINE = InnoDB,
 PARTITION business_property_3 VALUES LESS THAN (4) ENGINE = InnoDB,
 PARTITION business_property_4 VALUES LESS THAN (5) ENGINE = InnoDB,
 PARTITION business_property_5 VALUES LESS THAN (6) ENGINE = InnoDB,
 PARTITION business_property_6 VALUES LESS THAN (7) ENGINE = InnoDB,
 PARTITION business_property_7 VALUES LESS THAN (8) ENGINE = InnoDB,
 PARTITION business_property_8 VALUES LESS THAN (9) ENGINE = InnoDB,
 PARTITION business_property_9 VALUES LESS THAN (10) ENGINE = InnoDB,
 PARTITION business_property_10 VALUES LESS THAN (11) ENGINE = InnoDB,
 PARTITION business_property_11 VALUES LESS THAN (12) ENGINE = InnoDB,
 PARTITION business_property_12 VALUES LESS THAN (13) ENGINE = InnoDB) */;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `business_property`
--

LOCK TABLES `business_property` WRITE;
/*!40000 ALTER TABLE `business_property` DISABLE KEYS */;
INSERT INTO `business_property` VALUES ('9020181218000001','2234567894','方博家园','青海省西宁市城中区129号','17797173942','王府井旁30米','101.801909','36.597263',12,'2018-12-18 14:54:19','ADD'),('9020181218000001','9234567894','方博家园','青海省西宁市城中区129号','17797173942','王府井旁30米','101.801909','36.597263',12,'2018-12-23 04:01:05','DEl'),('9020181218000001','9234567894','方博家园','青海省西宁市城中区129号','17797173942','王府井旁30米','101.801909','36.597263',12,'2018-12-23 04:01:05','ADD'),('9020181218000001','9234567894','方博家园','青海省西宁市城中区129号','17797173942','王府井旁30米','101.801909','36.597263',12,'2018-12-23 04:02:17','DEl'),('9020181218000001','9234567894','方博家园123','青海省西宁市城中区129号123','17797173952','王府井旁40米','102.801909','37.597263',12,'2018-12-23 04:02:17','ADD'),('9020181218000001','10234567894','方博家园','青海省西宁市城中区129号','17797173942','王府井旁30米','101.801909','36.597263',12,'2018-12-23 04:02:24','DEl'),('9020181218000001','10234567894','方博家园123','青海省西宁市城中区129号123','17797173952','王府井旁40米','102.801909','37.597263',12,'2018-12-23 04:02:24','ADD');
/*!40000 ALTER TABLE `business_property` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `business_property_attr`
--

DROP TABLE IF EXISTS `business_property_attr`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `business_property_attr` (
  `b_id` varchar(30) NOT NULL COMMENT '订单ID',
  `attr_id` varchar(30) NOT NULL COMMENT '属性id',
  `property_id` varchar(30) NOT NULL COMMENT '用户ID',
  `spec_cd` varchar(12) NOT NULL COMMENT '规格id,参考spec表',
  `value` varchar(50) NOT NULL COMMENT '属性值',
  `month` int(11) NOT NULL COMMENT '月份',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `operate` varchar(3) NOT NULL COMMENT '数据状态，添加ADD，修改MOD 删除DEL',
  KEY `idx_business_attr_property_id` (`property_id`),
  KEY `idx_business_property_attr_b_id` (`b_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8
/*!50100 PARTITION BY RANGE (`month`)
(PARTITION business_property_attr_1 VALUES LESS THAN (2) ENGINE = InnoDB,
 PARTITION business_property_attr_2 VALUES LESS THAN (3) ENGINE = InnoDB,
 PARTITION business_property_attr_3 VALUES LESS THAN (4) ENGINE = InnoDB,
 PARTITION business_property_attr_4 VALUES LESS THAN (5) ENGINE = InnoDB,
 PARTITION business_property_attr_5 VALUES LESS THAN (6) ENGINE = InnoDB,
 PARTITION business_property_attr_6 VALUES LESS THAN (7) ENGINE = InnoDB,
 PARTITION business_property_attr_7 VALUES LESS THAN (8) ENGINE = InnoDB,
 PARTITION business_property_attr_8 VALUES LESS THAN (9) ENGINE = InnoDB,
 PARTITION business_property_attr_9 VALUES LESS THAN (10) ENGINE = InnoDB,
 PARTITION business_property_attr_10 VALUES LESS THAN (11) ENGINE = InnoDB,
 PARTITION business_property_attr_11 VALUES LESS THAN (12) ENGINE = InnoDB,
 PARTITION business_property_attr_12 VALUES LESS THAN (13) ENGINE = InnoDB) */;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `business_property_attr`
--

LOCK TABLES `business_property_attr` WRITE;
/*!40000 ALTER TABLE `business_property_attr` DISABLE KEYS */;
INSERT INTO `business_property_attr` VALUES ('2234567894','112018121800000002','9020181218000001','1001','01',12,'2018-12-18 14:54:19','ADD'),('9234567894','112018121800000002','9020181218000001','1001','01',12,'2018-12-23 04:01:06','DEl'),('9234567894','112018121800000002','9020181218000001','1001','02',12,'2018-12-23 04:01:06','ADD'),('9234567894','112018121800000002','9020181218000001','1001','01',12,'2018-12-23 04:02:17','DEl'),('9234567894','112018121800000002','9020181218000001','1001','02',12,'2018-12-23 04:02:17','ADD'),('10234567894','112018121800000002','9020181218000001','1001','01',12,'2018-12-23 04:02:25','DEl'),('10234567894','112018121800000002','9020181218000001','1001','02',12,'2018-12-23 04:02:25','ADD'),('19234567894','112018121800000002','9020181218000001','1001','02',12,'2018-12-23 07:45:01','DEl');
/*!40000 ALTER TABLE `business_property_attr` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `business_property_cerdentials`
--

DROP TABLE IF EXISTS `business_property_cerdentials`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `business_property_cerdentials` (
  `property_cerdentials_id` varchar(30) NOT NULL COMMENT '物业证件ID',
  `b_id` varchar(30) NOT NULL COMMENT '业务Id',
  `property_id` varchar(30) NOT NULL COMMENT '物业ID',
  `credentials_cd` varchar(12) NOT NULL COMMENT '证件类型，对应于 credentials表',
  `value` varchar(50) NOT NULL COMMENT '证件号码',
  `validity_period` date NOT NULL COMMENT '有效期，如果是长期有效 写成 3000/1/1',
  `positive_photo` varchar(100) DEFAULT NULL COMMENT '正面照片',
  `negative_photo` varchar(100) DEFAULT NULL COMMENT '反面照片',
  `month` int(11) NOT NULL COMMENT '月份',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `operate` varchar(3) NOT NULL COMMENT '数据状态，添加ADD，修改MOD 删除DEL',
  KEY `idx_business_cerdentials_property_id` (`property_id`),
  KEY `idx_business_property_cerdentials_b_id` (`b_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8
/*!50100 PARTITION BY RANGE (`month`)
(PARTITION business_property_cerdentials_1 VALUES LESS THAN (2) ENGINE = InnoDB,
 PARTITION business_property_cerdentials_2 VALUES LESS THAN (3) ENGINE = InnoDB,
 PARTITION business_property_cerdentials_3 VALUES LESS THAN (4) ENGINE = InnoDB,
 PARTITION business_property_cerdentials_4 VALUES LESS THAN (5) ENGINE = InnoDB,
 PARTITION business_property_cerdentials_5 VALUES LESS THAN (6) ENGINE = InnoDB,
 PARTITION business_property_cerdentials_6 VALUES LESS THAN (7) ENGINE = InnoDB,
 PARTITION business_property_cerdentials_7 VALUES LESS THAN (8) ENGINE = InnoDB,
 PARTITION business_property_cerdentials_8 VALUES LESS THAN (9) ENGINE = InnoDB,
 PARTITION business_property_cerdentials_9 VALUES LESS THAN (10) ENGINE = InnoDB,
 PARTITION business_property_cerdentials_10 VALUES LESS THAN (11) ENGINE = InnoDB,
 PARTITION business_property_cerdentials_11 VALUES LESS THAN (12) ENGINE = InnoDB,
 PARTITION business_property_cerdentials_12 VALUES LESS THAN (13) ENGINE = InnoDB) */;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `business_property_cerdentials`
--

LOCK TABLES `business_property_cerdentials` WRITE;
/*!40000 ALTER TABLE `business_property_cerdentials` DISABLE KEYS */;
INSERT INTO `business_property_cerdentials` VALUES ('9220181218000002','4234567894','9020181218000001','1','632126XXXXXXXX2011','3000-01-01','正面照片地址，1234567.jpg','反面照片地址，没有不填写',12,'2018-12-18 15:32:00','ADD'),('9220181218000002','12234567894','9020181218000001','1','632126XXXXXXXX2011','3000-01-01','正面照片地址，1234567.jpg','反面照片地址，没有不填写',12,'2018-12-23 04:26:54','DEl'),('9220181218000002','12234567894','9020181218000001','1','632126XXXXXXXX2012','3000-02-01','1234567.jpg','11.jpg',12,'2018-12-23 04:26:54','ADD'),('9220181218000002','16234567894','9020181218000001','1','632126XXXXXXXX2012','3000-02-01','1234567.jpg','11.jpg',12,'2018-12-23 07:04:54','DEl');
/*!40000 ALTER TABLE `business_property_cerdentials` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `business_property_fee`
--

DROP TABLE IF EXISTS `business_property_fee`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `business_property_fee` (
  `fee_id` varchar(30) NOT NULL COMMENT 'ID',
  `b_id` varchar(30) NOT NULL COMMENT '业务Id',
  `property_id` varchar(30) NOT NULL COMMENT '物业ID',
  `fee_type_cd` varchar(10) NOT NULL COMMENT '费用类型,物业费，停车费 请查看property_fee_type表',
  `fee_money` varchar(20) NOT NULL COMMENT '费用金额',
  `fee_time` varchar(10) NOT NULL COMMENT '费用周期，一个月，半年，或一年 请查看property_fee_time表',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `start_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '开始时间',
  `end_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '结束时间',
  `month` int(11) NOT NULL COMMENT '月份',
  `operate` varchar(3) NOT NULL COMMENT '数据状态，添加ADD，修改MOD 删除DEL'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `business_property_fee`
--

LOCK TABLES `business_property_fee` WRITE;
/*!40000 ALTER TABLE `business_property_fee` DISABLE KEYS */;
INSERT INTO `business_property_fee` VALUES ('9420181221000001','5234567894','9020181218000001','T','10','0.5','2018-12-20 16:14:57','2018-01-01 00:00:00','2020-12-31 00:00:00',12,'ADD'),('9420181221000001','13234567894','9020181218000001','T','10','0.5','2018-12-23 04:52:34','2018-01-01 00:00:00','2020-12-31 00:00:00',12,'DEl'),('9420181221000001','13234567894','9020181218000001','T','10','0.5','2018-12-23 04:52:34','2018-05-01 00:00:00','2020-11-30 00:00:00',12,'ADD');
/*!40000 ALTER TABLE `business_property_fee` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `business_property_house`
--

DROP TABLE IF EXISTS `business_property_house`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `business_property_house` (
  `house_id` varchar(30) NOT NULL COMMENT 'ID',
  `property_id` varchar(30) NOT NULL COMMENT '物业ID',
  `b_id` varchar(30) NOT NULL COMMENT '业务Id',
  `house_num` varchar(30) NOT NULL COMMENT '门牌号',
  `house_name` varchar(50) NOT NULL COMMENT '住户名称',
  `house_phone` varchar(11) DEFAULT NULL COMMENT '住户联系号码',
  `house_area` varchar(30) NOT NULL COMMENT '房屋面积',
  `fee_type_cd` varchar(10) NOT NULL COMMENT '费用类型 property_fee_type表',
  `fee_price` varchar(30) NOT NULL COMMENT '费用单价',
  `month` int(11) NOT NULL COMMENT '月份',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `operate` varchar(3) NOT NULL COMMENT '数据状态，添加ADD，修改MOD 删除DEL'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `business_property_house`
--

LOCK TABLES `business_property_house` WRITE;
/*!40000 ALTER TABLE `business_property_house` DISABLE KEYS */;
INSERT INTO `business_property_house` VALUES ('9520181222000001','123213','6234567894','T','10',NULL,'01918','111','10.09',12,'2018-12-21 17:01:12','ADD'),('9520181222000001','123213','7234567894','T','10','17797173942','01918','111','10.09',12,'2018-12-21 17:07:34','ADD'),('9520181222000003','123213','7234567894','T','10','17797173942','01918','111','10.09',12,'2018-12-21 17:08:20','ADD'),('9520181222000005','123213','8234567894','T','10','17797173942','01918','111','10.09',12,'2018-12-21 17:08:28','ADD'),('9520181222000005','123213','14234567894','T','10','17797173942','01918','111','10.09',12,'2018-12-23 05:59:48','DEl'),('9520181222000005','123213','14234567894','123123','吴XX住宅','17797173943','01919','112','11.09',12,'2018-12-23 05:59:48','ADD'),('9520181222000005','123213','17234567894','123123','吴XX住宅','17797173943','01919','112','11.09',12,'2018-12-23 07:19:41','DEl');
/*!40000 ALTER TABLE `business_property_house` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `business_property_house_attr`
--

DROP TABLE IF EXISTS `business_property_house_attr`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `business_property_house_attr` (
  `b_id` varchar(30) NOT NULL COMMENT '订单ID',
  `attr_id` varchar(30) NOT NULL COMMENT '属性id',
  `house_id` varchar(30) NOT NULL COMMENT '用户ID',
  `spec_cd` varchar(12) NOT NULL COMMENT '规格id,参考spec表',
  `VALUE` varchar(50) NOT NULL COMMENT '属性值',
  `month` int(11) NOT NULL COMMENT '月份',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `operate` varchar(3) NOT NULL COMMENT '数据状态，添加ADD，修改MOD 删除DEL'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `business_property_house_attr`
--

LOCK TABLES `business_property_house_attr` WRITE;
/*!40000 ALTER TABLE `business_property_house_attr` DISABLE KEYS */;
INSERT INTO `business_property_house_attr` VALUES ('6234567894','112018122200000002','9520181222000001','1001','01',12,'2018-12-21 17:01:12','ADD'),('7234567894','112018122200000002','9520181222000001','1001','01',12,'2018-12-21 17:07:34','ADD'),('7234567894','112018122200000004','9520181222000003','1001','01',12,'2018-12-21 17:08:20','ADD'),('8234567894','112018122200000006','9520181222000005','1001','01',12,'2018-12-21 17:08:28','ADD'),('14234567894','112018122200000006','9520181222000005','1001','01',12,'2018-12-23 05:59:48','DEl'),('14234567894','112018122200000006','9520181222000005','1001','02',12,'2018-12-23 05:59:48','ADD'),('17234567894','112018122200000006','9520181222000005','1001','02',12,'2018-12-23 07:19:42','DEl');
/*!40000 ALTER TABLE `business_property_house_attr` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `business_property_photo`
--

DROP TABLE IF EXISTS `business_property_photo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `business_property_photo` (
  `property_photo_id` varchar(30) NOT NULL COMMENT '物业照片ID',
  `b_id` varchar(30) NOT NULL COMMENT '业务Id',
  `property_id` varchar(30) NOT NULL COMMENT '物业ID',
  `property_photo_type_cd` varchar(12) NOT NULL COMMENT '物业照片类型,T 门头照 I 内景照',
  `photo` varchar(100) NOT NULL COMMENT '照片',
  `month` int(11) NOT NULL COMMENT '月份',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `operate` varchar(3) NOT NULL COMMENT '数据状态，添加ADD，修改MOD 删除DEL',
  KEY `idx_business_photo_property_id` (`property_id`),
  KEY `idx_business_property_photo_b_id` (`b_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8
/*!50100 PARTITION BY RANGE (`month`)
(PARTITION business_property_photo_1 VALUES LESS THAN (2) ENGINE = InnoDB,
 PARTITION business_property_photo_2 VALUES LESS THAN (3) ENGINE = InnoDB,
 PARTITION business_property_photo_3 VALUES LESS THAN (4) ENGINE = InnoDB,
 PARTITION business_property_photo_4 VALUES LESS THAN (5) ENGINE = InnoDB,
 PARTITION business_property_photo_5 VALUES LESS THAN (6) ENGINE = InnoDB,
 PARTITION business_property_photo_6 VALUES LESS THAN (7) ENGINE = InnoDB,
 PARTITION business_property_photo_7 VALUES LESS THAN (8) ENGINE = InnoDB,
 PARTITION business_property_photo_8 VALUES LESS THAN (9) ENGINE = InnoDB,
 PARTITION business_property_photo_9 VALUES LESS THAN (10) ENGINE = InnoDB,
 PARTITION business_property_photo_10 VALUES LESS THAN (11) ENGINE = InnoDB,
 PARTITION business_property_photo_11 VALUES LESS THAN (12) ENGINE = InnoDB,
 PARTITION business_property_photo_12 VALUES LESS THAN (13) ENGINE = InnoDB) */;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `business_property_photo`
--

LOCK TABLES `business_property_photo` WRITE;
/*!40000 ALTER TABLE `business_property_photo` DISABLE KEYS */;
INSERT INTO `business_property_photo` VALUES ('null20181218000001','3234567894','9020181218000001','T','12345678.jpg',12,'2018-12-18 15:21:09','ADD'),('12320181218000001','11234567894','9020181218000001','T','12345678.jpg',12,'2018-12-23 04:15:59','DEl'),('12320181218000001','11234567894','9020181218000001','T','123456789.jpg',12,'2018-12-23 04:16:00','ADD'),('12320181218000001','15234567894','9020181218000001','T','123456789.jpg',12,'2018-12-23 06:57:20','DEl');
/*!40000 ALTER TABLE `business_property_photo` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `business_property_user`
--

DROP TABLE IF EXISTS `business_property_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `business_property_user` (
  `property_user_id` varchar(30) NOT NULL COMMENT '物业用户ID',
  `property_id` varchar(30) NOT NULL COMMENT '物业ID',
  `b_id` varchar(30) NOT NULL COMMENT '业务Id',
  `user_id` varchar(30) NOT NULL COMMENT '用户ID',
  `rel_cd` varchar(30) NOT NULL COMMENT '用户和物业关系 详情查看 property_user_rel表',
  `month` int(11) NOT NULL COMMENT '月份',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `operate` varchar(3) NOT NULL COMMENT '数据状态，添加ADD，修改MOD 删除DEL'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `business_property_user`
--

LOCK TABLES `business_property_user` WRITE;
/*!40000 ALTER TABLE `business_property_user` DISABLE KEYS */;
INSERT INTO `business_property_user` VALUES ('9320181219000001','9020181218000001','4234567894','123','600311000001',12,'2018-12-18 17:28:13','ADD'),('9320181219000001','9020181218000001','18234567894','123','600311000001',12,'2018-12-23 07:36:49','DEl');
/*!40000 ALTER TABLE `business_property_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `business_shop`
--

DROP TABLE IF EXISTS `business_shop`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `business_shop`
--

LOCK TABLES `business_shop` WRITE;
/*!40000 ALTER TABLE `business_shop` DISABLE KEYS */;
/*!40000 ALTER TABLE `business_shop` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `business_shop_attr`
--

DROP TABLE IF EXISTS `business_shop_attr`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `business_shop_attr`
--

LOCK TABLES `business_shop_attr` WRITE;
/*!40000 ALTER TABLE `business_shop_attr` DISABLE KEYS */;
/*!40000 ALTER TABLE `business_shop_attr` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `business_shop_attr_param`
--

DROP TABLE IF EXISTS `business_shop_attr_param`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `business_shop_attr_param`
--

LOCK TABLES `business_shop_attr_param` WRITE;
/*!40000 ALTER TABLE `business_shop_attr_param` DISABLE KEYS */;
/*!40000 ALTER TABLE `business_shop_attr_param` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `business_shop_catalog`
--

DROP TABLE IF EXISTS `business_shop_catalog`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `business_shop_catalog`
--

LOCK TABLES `business_shop_catalog` WRITE;
/*!40000 ALTER TABLE `business_shop_catalog` DISABLE KEYS */;
/*!40000 ALTER TABLE `business_shop_catalog` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `business_shop_desc`
--

DROP TABLE IF EXISTS `business_shop_desc`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `business_shop_desc`
--

LOCK TABLES `business_shop_desc` WRITE;
/*!40000 ALTER TABLE `business_shop_desc` DISABLE KEYS */;
/*!40000 ALTER TABLE `business_shop_desc` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `business_shop_photo`
--

DROP TABLE IF EXISTS `business_shop_photo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `business_shop_photo`
--

LOCK TABLES `business_shop_photo` WRITE;
/*!40000 ALTER TABLE `business_shop_photo` DISABLE KEYS */;
/*!40000 ALTER TABLE `business_shop_photo` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `business_shop_preferential`
--

DROP TABLE IF EXISTS `business_shop_preferential`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `business_shop_preferential`
--

LOCK TABLES `business_shop_preferential` WRITE;
/*!40000 ALTER TABLE `business_shop_preferential` DISABLE KEYS */;
/*!40000 ALTER TABLE `business_shop_preferential` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `business_store`
--

DROP TABLE IF EXISTS `business_store`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `business_store` (
  `store_id` varchar(30) NOT NULL COMMENT '商店ID',
  `b_id` varchar(30) NOT NULL COMMENT '业务Id',
  `user_id` varchar(30) NOT NULL COMMENT '用户ID',
  `name` varchar(100) NOT NULL COMMENT '店铺名称',
  `address` varchar(200) NOT NULL COMMENT '店铺地址',
  `tel` varchar(11) NOT NULL COMMENT '电话',
  `store_type_cd` varchar(12) DEFAULT NULL,
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `business_store`
--

LOCK TABLES `business_store` WRITE;
/*!40000 ALTER TABLE `business_store` DISABLE KEYS */;
/*!40000 ALTER TABLE `business_store` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `business_store_attr`
--

DROP TABLE IF EXISTS `business_store_attr`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `business_store_attr`
--

LOCK TABLES `business_store_attr` WRITE;
/*!40000 ALTER TABLE `business_store_attr` DISABLE KEYS */;
/*!40000 ALTER TABLE `business_store_attr` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `business_store_cerdentials`
--

DROP TABLE IF EXISTS `business_store_cerdentials`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `business_store_cerdentials`
--

LOCK TABLES `business_store_cerdentials` WRITE;
/*!40000 ALTER TABLE `business_store_cerdentials` DISABLE KEYS */;
/*!40000 ALTER TABLE `business_store_cerdentials` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `business_store_photo`
--

DROP TABLE IF EXISTS `business_store_photo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `business_store_photo`
--

LOCK TABLES `business_store_photo` WRITE;
/*!40000 ALTER TABLE `business_store_photo` DISABLE KEYS */;
/*!40000 ALTER TABLE `business_store_photo` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `business_user`
--

DROP TABLE IF EXISTS `business_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
) ENGINE=InnoDB AUTO_INCREMENT=68 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `business_user`
--

LOCK TABLES `business_user` WRITE;
/*!40000 ALTER TABLE `business_user` DISABLE KEYS */;
INSERT INTO `business_user` VALUES (1,'20516329683810271232','30516329684078706688','张三','928255095@qq.com',NULL,'ERCBHDUYFJDNDHDJDNDJDHDUDHDJDDKDK',NULL,NULL,'0','17797173943','0','2018-11-25 11:09:49','ADD'),(2,'20516332294412189696','30516332294873563136','李四','928255095@qq.com',NULL,'ERCBHDUYFJDNDHDJDNDJDHDUDHDJDDKDK',NULL,NULL,'0','17797173943','0','2018-11-25 11:20:12','ADD'),(3,'20516364589907066880','30516364590414577664','王五','928255095@qq.com',NULL,'ERCBHDUYFJDNDHDJDNDJDHDUDHDJDDKDK',NULL,NULL,'0','17797173943','0','2018-11-25 13:28:31','ADD'),(4,'20516365201587585024','30516365201923129344','王五1','928255095@qq.com',NULL,'ERCBHDUYFJDNDHDJDNDJDHDUDHDJDDKDK',NULL,NULL,'0','17797173943','0','2018-11-25 13:30:57','ADD'),(5,'20516366112951123968','30516366113219559424','王五2','928255095@qq.com',NULL,'ERCBHDUYFJDNDHDJDNDJDHDUDHDJDDKDK',NULL,NULL,'0','17797173943','0','2018-11-25 13:34:35','ADD'),(6,'20516366708928167936','30516366709171437568','王五3','928255095@qq.com',NULL,'ERCBHDUYFJDNDHDJDNDJDHDUDHDJDDKDK',NULL,NULL,'0','17797173943','0','2018-11-25 13:36:57','ADD'),(7,'20516368306257543168','30516368306509201408','王五4','928255095@qq.com',NULL,'ERCBHDUYFJDNDHDJDNDJDHDUDHDJDDKDK',NULL,NULL,'0','17797173943','0','2018-11-25 13:43:17','ADD'),(8,'20516374464208846848','30516374464544391168','王五5','928255095@qq.com',NULL,'ERCBHDUYFJDNDHDJDNDJDHDUDHDJDDKDK',NULL,NULL,'0','17797173943','0','2018-11-25 14:07:46','ADD'),(9,'20516374772574076928','30516374772855095296','王五6','928255095@qq.com',NULL,'ERCBHDUYFJDNDHDJDNDJDHDUDHDJDDKDK',NULL,NULL,'0','17797173943','0','2018-11-25 14:08:59','ADD'),(10,'20516374801669963776','30516374801938399232','王五7','928255095@qq.com',NULL,'ERCBHDUYFJDNDHDJDNDJDHDUDHDJDDKDK',NULL,NULL,'0','17797173943','0','2018-11-25 14:09:06','ADD'),(11,'20516377393317822464','30516377393741447168','王五7','928255095@qq.com',NULL,'ERCBHDUYFJDNDHDJDNDJDHDUDHDJDDKDK',NULL,NULL,'0','17797173943','0','2018-11-25 14:19:24','ADD'),(12,'20516381939620397056','30516381939989495808','王五8','928255095@qq.com',NULL,'ERCBHDUYFJDNDHDJDNDJDHDUDHDJDDKDK',NULL,NULL,'0','17797173943','0','2018-11-25 14:37:28','ADD'),(13,'20516387722659643392','30516387723230068736','王五9','928255095@qq.com',NULL,'ERCBHDUYFJDNDHDJDNDJDHDUDHDJDDKDK',NULL,NULL,'0','17797173943','0','2018-11-25 15:00:27','ADD'),(14,'20516389224736374784','30516389225004810240','王五10','928255095@qq.com',NULL,'ERCBHDUYFJDNDHDJDNDJDHDUDHDJDDKDK',NULL,NULL,'0','17797173943','0','2018-11-25 15:06:25','ADD'),(15,'20516389265110745088','30516389265349820416','王五11','928255095@qq.com',NULL,'ERCBHDUYFJDNDHDJDNDJDHDUDHDJDDKDK',NULL,NULL,'0','17797173943','0','2018-11-25 15:06:34','ADD'),(16,'20516398820167270400','30516398820397957120','吴学文','',NULL,'0724094fbcd70db0493034daa2120aa1',NULL,NULL,'0','15897089471','0','2018-11-25 15:44:33','ADD'),(17,'20516400356243030016','30516400356482105344','师师','',NULL,'f1ab59f367854c555a84d42155d39aa3',NULL,NULL,'0','18987095720','0','2018-11-25 15:50:39','ADD'),(18,'20516401273893830656','30516401274132905984','张试卷','',NULL,'cdfea6fbffcd82758eec12a96c64e682',NULL,NULL,'0','15897089471','0','2018-11-25 15:54:18','ADD'),(19,'20517072482935521280','30517072483300425728','张三','',NULL,'dc2263f093f3db5edd6ce5ea6aa81c32',NULL,NULL,'0','17797173947','0','2018-11-27 12:21:26','ADD'),(20,'20517086035457359872','30517086035897761792','吴学文123','',NULL,'c91cf5db90773073a558e8740521c01c',NULL,NULL,'0','17797173942','0','2018-11-27 13:15:17','ADD'),(21,'20517710159825354752','30517710166884368384','王五112','928255095@qq.com',NULL,'ERCBHDUYFJDNDHDJDNDJDHDUDHDJDDKDK',NULL,NULL,'0','17797173943','0','2018-11-29 06:35:22','ADD'),(22,'20518939876956061696','30518939884421922816','admin','',NULL,'23a39fd21728292e1f794b250bc67019',NULL,NULL,'0','17797173944','0','2018-12-02 16:01:50','ADD'),(23,'20518940136344403968','30518940136629616640','wuxw','',NULL,'0bda701c63916b42e71851214373fe1b',NULL,NULL,'0','17797173940','0','2018-12-02 16:02:50','ADD'),(24,'20520751770314489856','30520751775595118592','zhangsan','',NULL,'b8ed29901921a8e7a8ec34d874a5379b',NULL,NULL,'0','17797173942','0','2018-12-07 16:01:38','ADD'),(25,'20520752094244782080','30520752094488051712','zhangsan','',NULL,'b8ed29901921a8e7a8ec34d874a5379b',NULL,NULL,'0','17797173942','0','2018-12-07 16:02:54','ADD'),(26,'20520752515906551808','30520752516195958784','zhangsan','',NULL,'b8ed29901921a8e7a8ec34d874a5379b',NULL,NULL,'0','17797173942','0','2018-12-07 16:04:34','ADD'),(27,'20520752654096285696','30520752654427635712','zhangsan','',NULL,'b8ed29901921a8e7a8ec34d874a5379b',NULL,NULL,'0','17797173942','0','2018-12-07 16:05:07','ADD'),(28,'20520753111019569152','30520753111732600832','lisi','',NULL,'f8b789267d0ea8414dd9dea3f88d2e66',NULL,NULL,'0','17797173942','0','2018-12-07 16:06:56','ADD'),(29,'20521002816139968512','30521002824033648640','admin','',NULL,'cdfea6fbffcd82758eec12a96c64e682',NULL,NULL,'0','15963245875','00','2018-12-08 08:39:13','ADD'),(30,'20521125064184184832','30521125069510950912','wuxw','928255095@qq.com',NULL,'cdfea6fbffcd82758eec12a96c64e682',NULL,NULL,'0','17797289806','01','2018-12-08 16:44:58','ADD'),(31,'20521292250362167296','30521125069510950912','wuxw','928255095@qq.com',NULL,'cdfea6fbffcd82758eec12a96c64e682',NULL,NULL,'0','17797289806','01','2018-12-09 03:49:19','DEl'),(32,'20521294334742511616','30521125069510950912','wuxw','928255095@qq.com',NULL,'cdfea6fbffcd82758eec12a96c64e682',NULL,NULL,'0','17797289806','01','2018-12-09 03:57:36','DEl'),(33,'20521294983668449280','30521125069510950912','wuxw','928255095@qq.com',NULL,'cdfea6fbffcd82758eec12a96c64e682',NULL,NULL,'0','17797289806','01','2018-12-09 04:00:09','ADD'),(34,'20521299093675327488','30521299095785062400','wangym','928255095@qq.com',NULL,'cdfea6fbffcd82758eec12a96c64e682',NULL,NULL,'0','17797289806','01','2018-12-09 04:16:29','ADD'),(35,'20521384632478875648','30521384632806031360','liyongxi','928255095@qq.com',NULL,'cdfea6fbffcd82758eec12a96c64e682',NULL,NULL,'0','17797289806','01','2018-12-09 09:56:23','ADD'),(36,'20521384655455272960','30521384655744679936','zhangshan','928255095@qq.com',NULL,'cdfea6fbffcd82758eec12a96c64e682',NULL,NULL,'0','17797289806','01','2018-12-09 09:56:28','ADD'),(37,'20521384680210055168','30521384680415576064','lisi','928255095@qq.com',NULL,'cdfea6fbffcd82758eec12a96c64e682',NULL,NULL,'0','17797289806','01','2018-12-09 09:56:34','ADD'),(38,'20521384772962893824','30521384773189386240','wangmazi','928255095@qq.com',NULL,'cdfea6fbffcd82758eec12a96c64e682',NULL,NULL,'0','17797289806','01','2018-12-09 09:56:56','ADD'),(39,'20521384859759820800','30521384859990507520','吴学文','928255095@qq.com',NULL,'cdfea6fbffcd82758eec12a96c64e682',NULL,NULL,'0','17797289806','01','2018-12-09 09:57:17','ADD'),(40,'20521388278868361216','30521388279069687808','吴学文1','928255095@qq.com',NULL,'cdfea6fbffcd82758eec12a96c64e682',NULL,NULL,'0','17797289806','01','2018-12-09 10:10:52','ADD'),(41,'20521388292466294784','30521388292680204288','吴学文2','928255095@qq.com',NULL,'cdfea6fbffcd82758eec12a96c64e682',NULL,NULL,'0','17797289806','01','2018-12-09 10:10:55','ADD'),(42,'20521388308534673408','30521388308752777216','吴学文3','928255095@qq.com',NULL,'cdfea6fbffcd82758eec12a96c64e682',NULL,NULL,'0','17797289806','01','2018-12-09 10:10:59','ADD'),(43,'20521388322648506368','30521388322879193088','吴学文4','928255095@qq.com',NULL,'cdfea6fbffcd82758eec12a96c64e682',NULL,NULL,'0','17797289806','01','2018-12-09 10:11:03','ADD'),(44,'20521388335667625984','30521388335978004480','吴学文5','928255095@qq.com',NULL,'cdfea6fbffcd82758eec12a96c64e682',NULL,NULL,'0','17797289806','01','2018-12-09 10:11:06','ADD'),(45,'20521470366867013632','30521470367152226304','王永梅','406943871@qq.com','青海省西宁市城中区申宁路6号','cdfea6fbffcd82758eec12a96c64e682',NULL,NULL,'1','17797173943','01','2018-12-09 15:37:03','ADD'),(46,'20521470728747368448','30521470728940306432','张三丰','123456@qq.com','','cdfea6fbffcd82758eec12a96c64e682',NULL,NULL,'0','18987898111','01','2018-12-09 15:38:30','ADD'),(47,'20521470972776169472','30521470972990078976','李玉刚','0908777@qq.com','','cdfea6fbffcd82758eec12a96c64e682',NULL,NULL,'1','18987164563','01','2018-12-09 15:39:28','ADD'),(48,'20521471733417394176','30521471733694218240','张学良','9088@qq.com','东北老爷们山','cdfea6fbffcd82758eec12a96c64e682',NULL,NULL,'0','17797173945','01','2018-12-09 15:42:29','ADD'),(49,'20521472810674044928','30521472810921508864','qhadmin','',NULL,'cdfea6fbffcd82758eec12a96c64e682',NULL,NULL,'0','17797173940','00','2018-12-09 15:46:46','ADD'),(50,'20521692192004128768','30521125069510950912','wuxw','928255095@qq.com',NULL,'cdfea6fbffcd82758eec12a96c64e682',NULL,NULL,'0','17797289806','01','2018-12-10 06:18:31','DEl'),(51,'20521692260908154880','30521299095785062400','wangym','928255095@qq.com',NULL,'cdfea6fbffcd82758eec12a96c64e682',NULL,NULL,'0','17797289806','01','2018-12-10 06:18:47','DEl'),(52,'20521692315283111936','30521299095785062400','wangym','928255095@qq.com',NULL,'cdfea6fbffcd82758eec12a96c64e682',NULL,NULL,'0','17797289806','01','2018-12-10 06:19:00','ADD'),(53,'20521692341828861952','30521125069510950912','wuxw','928255095@qq.com',NULL,'cdfea6fbffcd82758eec12a96c64e682',NULL,NULL,'0','17797289806','01','2018-12-10 06:19:06','ADD'),(54,'20521692506895695872','30521125069510950912','wuxw','928255095@qq.com',NULL,'cdfea6fbffcd82758eec12a96c64e682',NULL,NULL,'0','17797289806','01','2018-12-10 06:19:46','DEl'),(55,'20521692649367814144','30521125069510950912','wuxw','928255095@qq.com',NULL,'cdfea6fbffcd82758eec12a96c64e682',NULL,NULL,'0','17797289806','01','2018-12-10 06:20:20','ADD'),(56,'20522549147056750592','30522549147283243008','张事假','928255095@qq.com','','cdfea6fbffcd82758eec12a96c64e682',NULL,NULL,'0','18997089471','01','2018-12-12 15:03:45','ADD'),(57,'20523565515797446656','30523565516132990976','jiejie','jiejie@qq.com','','cdfea6fbffcd82758eec12a96c64e682',NULL,NULL,'1','17797173948','01','2018-12-15 10:22:26','ADD'),(58,'202019020200000002','3020190202000001','吴梓豪','928255094@qq.com','青海省','cdfea6fbffcd82758eec12a96c64e682',NULL,NULL,'0','17797173942','01','2019-02-02 13:40:27','ADD'),(59,'202019020200000003','3020190202000001','吴梓豪','928255094@qq.com','青海省','cdfea6fbffcd82758eec12a96c64e682',NULL,NULL,'0','17797173942','01','2019-02-02 14:00:35','DEl'),(60,'202019020300000003','30521388308752777216','吴学文3','928255095@qq.com',NULL,'cdfea6fbffcd82758eec12a96c64e682',NULL,NULL,'0','17797289806','01','2019-02-02 16:00:09','DEl'),(61,'202019021000000009','3020190210000001','吴学文89','928255095@qq.com',NULL,'cdfea6fbffcd82758eec12a96c64e682',NULL,NULL,'0','17797289807','01','2019-02-09 16:57:52','ADD'),(62,'202019021070180002','3020190210000002','吴学文90','928255095@qq.com',NULL,'cdfea6fbffcd82758eec12a96c64e682',NULL,NULL,'0','17797289807','01','2019-02-09 17:17:48','ADD'),(63,'202019021058060004','3020190210000003','吴学文91','928255095@qq.com',NULL,'cdfea6fbffcd82758eec12a96c64e682',NULL,NULL,'0','17797289807','01','2019-02-09 17:23:15','ADD'),(64,'202019021001030002','30521384632806031360','liyongxi','928255095@qq.com',NULL,'cdfea6fbffcd82758eec12a96c64e682',NULL,NULL,'0','17797289806','01','2019-02-10 14:35:39','DEl'),(65,'202019021096440004','30521384632806031360','liyongxi','928255095@qq.com',NULL,'cdfea6fbffcd82758eec12a96c64e682',NULL,NULL,'0','17797289806','01','2019-02-10 14:35:54','ADD'),(66,'202019021059530006','302019021030610001','张时强','wuxw7@www.com','甘肃省金昌市','cdfea6fbffcd82758eec12a96c64e682',NULL,NULL,'0','18909789184','01','2019-02-10 14:37:01','ADD'),(67,'202019021067580008','302019021020990002','cc789','',NULL,'bd06b3bdf498d5fecd1f83964c925750',NULL,NULL,'0','18909871234','00','2019-02-10 14:38:23','ADD');
/*!40000 ALTER TABLE `business_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `business_user_address`
--

DROP TABLE IF EXISTS `business_user_address`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `business_user_address`
--

LOCK TABLES `business_user_address` WRITE;
/*!40000 ALTER TABLE `business_user_address` DISABLE KEYS */;
/*!40000 ALTER TABLE `business_user_address` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `business_user_attr`
--

DROP TABLE IF EXISTS `business_user_attr`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `business_user_attr` (
  `b_id` varchar(30) NOT NULL COMMENT '订单ID',
  `attr_id` varchar(30) NOT NULL COMMENT '属性id',
  `user_id` varchar(30) NOT NULL COMMENT '用户ID',
  `spec_cd` varchar(12) NOT NULL COMMENT '规格id,参考spec表',
  `value` varchar(50) NOT NULL COMMENT '属性值',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `operate` varchar(3) NOT NULL COMMENT '数据状态，添加ADD，修改MOD 删除DEL'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `business_user_attr`
--

LOCK TABLES `business_user_attr` WRITE;
/*!40000 ALTER TABLE `business_user_attr` DISABLE KEYS */;
/*!40000 ALTER TABLE `business_user_attr` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `business_user_credentials`
--

DROP TABLE IF EXISTS `business_user_credentials`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `business_user_credentials` (
  `credentials_id` varchar(30) NOT NULL COMMENT '证件ID',
  `b_id` varchar(30) NOT NULL COMMENT '业务Id',
  `user_id` varchar(30) NOT NULL COMMENT '用户ID',
  `credentials_cd` varchar(12) NOT NULL COMMENT '证件类型',
  `value` varchar(50) NOT NULL COMMENT '证件号码',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `operate` varchar(3) NOT NULL COMMENT '数据状态，添加ADD，修改MOD 删除DEL'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `business_user_credentials`
--

LOCK TABLES `business_user_credentials` WRITE;
/*!40000 ALTER TABLE `business_user_credentials` DISABLE KEYS */;
/*!40000 ALTER TABLE `business_user_credentials` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `business_user_tag`
--

DROP TABLE IF EXISTS `business_user_tag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `business_user_tag` (
  `tag_id` varchar(30) NOT NULL COMMENT '打标ID',
  `b_id` varchar(30) NOT NULL COMMENT '业务Id',
  `user_id` varchar(30) NOT NULL COMMENT '用户ID',
  `tag_cd` varchar(12) NOT NULL COMMENT '标签编码,参考tag表',
  `remark` varchar(200) DEFAULT NULL COMMENT '备注',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `operate` varchar(3) NOT NULL COMMENT '数据状态，添加ADD，修改MOD 删除DEL'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `business_user_tag`
--

LOCK TABLES `business_user_tag` WRITE;
/*!40000 ALTER TABLE `business_user_tag` DISABLE KEYS */;
/*!40000 ALTER TABLE `business_user_tag` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `c_app`
--

DROP TABLE IF EXISTS `c_app`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `c_app`
--

LOCK TABLES `c_app` WRITE;
/*!40000 ALTER TABLE `c_app` DISABLE KEYS */;
INSERT INTO `c_app` VALUES (1,'8000418001','内部测试应用','',NULL,NULL,'记得删除','2018-11-14 13:28:44','0'),(2,'8000418002','控制中心应用','',NULL,NULL,'控制中心应用','2018-11-14 13:28:44','0'),(3,'8000418003','用户管理应用','WEBURFPKIFJUHNCJUEIKMKJUJHULSMNCHDY89KMC',NULL,NULL,'用户管理应用','2018-11-14 13:28:44','0');
/*!40000 ALTER TABLE `c_app` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `c_business`
--

DROP TABLE IF EXISTS `c_business`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `c_business` (
  `b_id` varchar(30) NOT NULL COMMENT '业务Id',
  `o_id` varchar(30) NOT NULL COMMENT '订单ID',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `business_type_cd` varchar(12) DEFAULT NULL,
  `finish_time` date DEFAULT NULL COMMENT '完成时间',
  `remark` varchar(200) DEFAULT NULL COMMENT '备注',
  `status_cd` varchar(2) NOT NULL COMMENT '数据状态，详细参考c_status表',
  UNIQUE KEY `b_id` (`b_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `c_business`
--

LOCK TABLES `c_business` WRITE;
/*!40000 ALTER TABLE `c_business` DISABLE KEYS */;
INSERT INTO `c_business` VALUES ('-1','-1','2018-12-09 03:18:50','D','2018-12-09',NULL,'B'),('202019020200000002','102019020200000001','2019-02-02 13:40:26','D','2019-02-02',NULL,'C'),('202019020200000003','102019020200000002','2019-02-02 14:00:33','D','2019-02-02',NULL,'C'),('202019020300000003','102019020300000002','2019-02-02 16:00:07','D','2019-02-03',NULL,'C'),('202019021000000003','102019021000000002','2019-02-09 16:24:21','100100030001',NULL,NULL,'S'),('202019021000000004','102019021000000003','2019-02-09 16:28:16','100100030001','2019-02-10',NULL,'B'),('202019021000000005','102019021000000004','2019-02-09 16:24:43','100100030001',NULL,NULL,'S'),('202019021000000006','102019021000000005','2019-02-09 16:30:05','100100030001','2019-02-10',NULL,'B'),('202019021000000007','102019021000000006','2019-02-09 16:36:02','100100030001','2019-02-10',NULL,'B'),('202019021000000008','102019021000000007','2019-02-09 16:31:25','100100030001','2019-02-10',NULL,'B'),('202019021000000009','102019021000000008','2019-02-09 16:57:45','100100030001','2019-02-10',NULL,'C'),('202019021001030002','102019021024050001','2019-02-10 14:35:36','100100040002','2019-02-10',NULL,'C'),('202019021058060004','102019021086310003','2019-02-09 17:23:13','100100030001','2019-02-10',NULL,'C'),('202019021059530006','102019021042560005','2019-02-10 14:37:01','100100030001','2019-02-10',NULL,'C'),('202019021067580008','102019021043400007','2019-02-10 14:38:23','100100030001','2019-02-10',NULL,'C'),('202019021070180002','102019021031750001','2019-02-09 17:17:42','100100030001','2019-02-10',NULL,'C'),('202019021096440004','102019021083940003','2019-02-10 14:35:54','100100040003','2019-02-10',NULL,'C'),('20512771454920556544','10512771452089401344','2018-11-15 15:30:42','D',NULL,'保存用户录入费用信息','S'),('20512773563694972928','10512773560800903168','2018-11-15 15:39:05','D',NULL,'保存用户录入费用信息','E'),('20512774199522099200','10512774197236203520','2018-11-15 15:41:36','D',NULL,'保存用户录入费用信息','E'),('20512774704356917248','10512774702192656384','2018-11-15 15:43:37','D',NULL,'保存用户录入费用信息','E'),('20512774784430374912','10512774782215782400','2018-11-15 15:43:56','D',NULL,'保存用户录入费用信息','E'),('20512778369964703744','10512778366974164992','2018-11-15 15:58:11','D',NULL,'保存用户录入费用信息','E'),('20512781063739670528','10512781061441191936','2018-11-15 16:08:53','D',NULL,'保存用户录入费用信息','E'),('20512781924033363968','10512781921827160064','2018-11-15 16:12:18','D',NULL,'保存用户录入费用信息','E'),('20512782243995844608','10512782241781252096','2018-11-15 16:13:34','D',NULL,'保存用户录入费用信息','S'),('20512782362006781952','10512782357623734272','2018-11-15 16:14:03','D',NULL,'保存用户录入费用信息','S'),('20513135740868100096','10513135737009340416','2018-11-16 15:38:14','D',NULL,'保存用户录入费用信息','S'),('20513136394307108864','10513136391413039104','2018-11-16 15:40:50','D',NULL,'保存用户录入费用信息','E'),('20513138496731348992','10513138492071477248','2018-11-16 15:49:11','D',NULL,'保存用户录入费用信息','E'),('20513138960449404928','10513138958218035200','2018-11-16 15:51:02','D',NULL,'保存用户录入费用信息','E'),('20513139668225622016','10513139665960697856','2018-11-16 15:53:51','D',NULL,'保存用户录入费用信息','E'),('20513140469312520192','10513140466590416896','2018-11-16 15:57:02','D',NULL,'保存用户录入费用信息','E'),('20513141047354720256','10513141044963966976','2018-11-16 15:59:20','D',NULL,'保存商户录入用户应缴费用信息','E'),('20513143019977834496','10513143017092153344','2018-11-16 16:07:10','D',NULL,'保存用户录入费用信息','S'),('20513145624225382400','10513145621712994304','2018-11-16 16:17:31','D',NULL,'保存商户录入用户应缴费用信息','S'),('20513145829515591680','10513145827309387776','2018-11-16 16:18:20','D',NULL,'保存商户录入用户应缴费用信息','S'),('20513146865416404992','10513146861406650368','2018-11-16 16:22:27','D',NULL,'保存商户录入用户应缴费用信息','S'),('20516292810350018560','10516292807556612096','2018-11-25 08:43:18','D',NULL,NULL,'E'),('20516293454838382592','10516293454683193344','2018-11-25 08:45:51','D',NULL,NULL,'E'),('20516297441130070016','10516297437925621760','2018-11-25 09:01:42','D',NULL,NULL,'E'),('20516297910573350912','10516297910447521792','2018-11-25 09:03:34','D',NULL,NULL,'E'),('20516329683810271232','10516329683684442112','2018-11-25 11:09:49','D',NULL,NULL,'S'),('20516332294412189696','10516332294286360576','2018-11-25 11:20:11','D','2018-11-25',NULL,'C'),('20516364589907066880','10516364589760266240','2018-11-25 13:28:31','D','2018-11-25',NULL,'C'),('20516365201587585024','10516365201428201472','2018-11-25 13:30:57','D','2018-11-25',NULL,'C'),('20516366112951123968','10516366112816906240','2018-11-25 13:34:34','D','2018-11-25',NULL,'C'),('20516366708928167936','10516366708789755904','2018-11-25 13:36:57','D','2018-11-25',NULL,'C'),('20516368306257543168','10516368306135908352','2018-11-25 13:43:17','D','2018-11-25',NULL,'C'),('20516374464208846848','10516374464053657600','2018-11-25 14:07:46','D','2018-11-25',NULL,'C'),('20516374772574076928','10516374772444053504','2018-11-25 14:08:59','D','2018-11-25',NULL,'C'),('20516374801669963776','10516374801552523264','2018-11-25 14:09:06','D','2018-11-25',NULL,'C'),('20516377393317822464','10516377393070358528','2018-11-25 14:19:24','D','2018-11-25',NULL,'C'),('20516381939620397056','10516381939469402112','2018-11-25 14:37:28','D','2018-11-25',NULL,'C'),('20516387722659643392','10516387722441539584','2018-11-25 15:00:27','D','2018-11-25',NULL,'C'),('20516389224736374784','10516389224614739968','2018-11-25 15:06:25','D','2018-11-25',NULL,'C'),('20516389265110745088','10516389264993304576','2018-11-25 15:06:34','D','2018-11-25',NULL,'C'),('20516398820167270400','10516398820054024192','2018-11-25 15:44:32','D','2018-11-25',NULL,'C'),('20516400356243030016','10516400356129783808','2018-11-25 15:50:39','D','2018-11-25',NULL,'C'),('20516401273893830656','10516401273776390144','2018-11-25 15:54:17','D','2018-11-25',NULL,'C'),('20517072482935521280','10517072482688057344','2018-11-27 12:21:26','D','2018-11-27',NULL,'C'),('20517086035457359872','10517086035339919360','2018-11-27 13:15:17','D','2018-11-27',NULL,'C'),('20517710159825354752','10517710156889341952','2018-11-29 06:35:20','D','2018-11-29',NULL,'C'),('20518939876956061696','10518939876595351552','2018-12-02 16:01:48','D','2018-12-03',NULL,'C'),('20518940136344403968','10518940136222769152','2018-12-02 16:02:49','D','2018-12-03',NULL,'C'),('20520751770314489856','10520751769869893632','2018-12-07 16:01:37','D','2018-12-08',NULL,'C'),('20520752094244782080','10520752094110564352','2018-12-07 16:02:54','D','2018-12-08',NULL,'C'),('20520752515906551808','10520752515780722688','2018-12-07 16:04:34','D','2018-12-08',NULL,'C'),('20520752654096285696','10520752653978845184','2018-12-07 16:05:07','D','2018-12-08',NULL,'C'),('20520753111019569152','10520753110897934336','2018-12-07 16:06:56','D','2018-12-08',NULL,'C'),('20521002816139968512','10521002813006823424','2018-12-08 08:39:11','D','2018-12-08',NULL,'C'),('20521124897368326144','10521124884286291968','2018-12-08 16:44:17','D','2018-12-09',NULL,'B'),('20521125064184184832','10521125063953498112','2018-12-08 16:44:57','D','2018-12-09',NULL,'C'),('20521126624146505728','10521126624020676608','2018-12-08 16:51:09','D','2018-12-09',NULL,'B'),('20521126943907659776','10521126943786024960','2018-12-08 16:52:25','D','2018-12-09',NULL,'B'),('20521280168329756672','10521280167822245888','2018-12-09 03:01:17','D','2018-12-09',NULL,'B'),('20521280438367436800','10521280438203858944','2018-12-09 03:02:21','D','2018-12-09',NULL,'B'),('20521281261436682240','10521281261306658816','2018-12-09 03:05:37','D','2018-12-09',NULL,'B'),('20521289750926082048','10521289747750993920','2018-12-09 03:39:21','D',NULL,NULL,'E'),('20521292250362167296','10521292247023501312','2018-12-09 03:49:17','D','2018-12-09',NULL,'B'),('20521292260218781696','10521292247023501312','2018-12-09 03:49:19','DO',NULL,'业务系统实例失败，发起撤单','DO'),('20521294334742511616','10521294331546451968','2018-12-09 03:57:34','D','2018-12-09',NULL,'C'),('20521294983668449280','10521294983534231552','2018-12-09 04:00:09','D','2018-12-09',NULL,'C'),('20521299093675327488','10521299093553692672','2018-12-09 04:16:29','D','2018-12-09',NULL,'C'),('20521384632478875648','10521384632348852224','2018-12-09 09:56:23','D','2018-12-09',NULL,'C'),('20521384655455272960','10521384655329443840','2018-12-09 09:56:28','D','2018-12-09',NULL,'C'),('20521384680210055168','10521384680075837440','2018-12-09 09:56:34','D','2018-12-09',NULL,'C'),('20521384772962893824','10521384772841259008','2018-12-09 09:56:56','D','2018-12-09',NULL,'C'),('20521384859759820800','10521384859650768896','2018-12-09 09:57:17','D','2018-12-09',NULL,'C'),('20521388278868361216','10521388278742532096','2018-12-09 10:10:52','D','2018-12-09',NULL,'C'),('20521388292466294784','10521388292340465664','2018-12-09 10:10:55','D','2018-12-09',NULL,'C'),('20521388308534673408','10521388308358512640','2018-12-09 10:10:59','D','2018-12-09',NULL,'C'),('20521388322648506368','10521388322531065856','2018-12-09 10:11:03','D','2018-12-09',NULL,'C'),('20521388335667625984','10521388335550185472','2018-12-09 10:11:06','D','2018-12-09',NULL,'C'),('20521470366867013632','10521470366736990208','2018-12-09 15:37:03','D','2018-12-09',NULL,'C'),('20521470728747368448','10521470728638316544','2018-12-09 15:38:30','D','2018-12-09',NULL,'C'),('20521470972776169472','10521470972654534656','2018-12-09 15:39:28','D','2018-12-09',NULL,'C'),('20521471733417394176','10521471733287370752','2018-12-09 15:42:29','D','2018-12-09',NULL,'C'),('20521472810674044928','10521472810514661376','2018-12-09 15:46:46','D','2018-12-09',NULL,'C'),('20521692192004128768','10521692191735693312','2018-12-10 06:18:31','D','2018-12-10',NULL,'C'),('20521692260908154880','10521692260782325760','2018-12-10 06:18:47','D','2018-12-10',NULL,'C'),('20521692315283111936','10521692315148894208','2018-12-10 06:19:00','D','2018-12-10',NULL,'C'),('20521692341828861952','10521692341610758144','2018-12-10 06:19:06','D','2018-12-10',NULL,'C'),('20521692506895695872','10521692506786643968','2018-12-10 06:19:46','D','2018-12-10',NULL,'C'),('20521692649367814144','10521692648902246400','2018-12-10 06:20:20','D','2018-12-10',NULL,'C'),('20522549147056750592','10522549146947698688','2018-12-12 15:03:45','D','2018-12-12',NULL,'C'),('20523565515797446656','10523565515541594112','2018-12-15 10:22:26','D','2018-12-15',NULL,'C');
/*!40000 ALTER TABLE `c_business` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `c_business_attrs`
--

DROP TABLE IF EXISTS `c_business_attrs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `c_business_attrs` (
  `b_id` varchar(30) NOT NULL COMMENT '订单ID',
  `attr_id` varchar(30) NOT NULL COMMENT '属性id',
  `spec_cd` varchar(12) NOT NULL COMMENT '规格id,参考spec表',
  `value` varchar(50) NOT NULL COMMENT '属性值'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `c_business_attrs`
--

LOCK TABLES `c_business_attrs` WRITE;
/*!40000 ALTER TABLE `c_business_attrs` DISABLE KEYS */;
/*!40000 ALTER TABLE `c_business_attrs` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `c_business_type`
--

DROP TABLE IF EXISTS `c_business_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `c_business_type` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `business_type_cd` varchar(12) NOT NULL COMMENT '业务项类型',
  `name` varchar(50) NOT NULL COMMENT '名称',
  `description` varchar(200) DEFAULT NULL COMMENT '描述',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `business_type_cd` (`business_type_cd`),
  UNIQUE KEY `business_type_cd_2` (`business_type_cd`)
) ENGINE=InnoDB AUTO_INCREMENT=56 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `c_business_type`
--

LOCK TABLES `c_business_type` WRITE;
/*!40000 ALTER TABLE `c_business_type` DISABLE KEYS */;
INSERT INTO `c_business_type` VALUES (1,'DO','撤单','作废订单','2019-02-07 17:04:21'),(3,'100100030001','保存用户信息','保存用户信息','2019-02-07 17:05:15'),(4,'100100030002','保存用户地址信息','保存用户地址信息','2019-02-07 17:05:47'),(6,'100100030003','用户打标','用户打标','2019-02-07 17:20:49'),(7,'100100030004','用户证件','保存用户地址信息','2019-02-07 17:20:49'),(8,'100100040001','修改用户信息','修改用户信息','2019-02-07 17:21:08'),(9,'100100040002','停用用户信息','停用用户信息','2019-02-07 17:21:08'),(10,'100100040003','恢复用户信息','恢复用户信息','2019-02-07 17:21:08'),(11,'200100030001','保存商户信息','保存商户信息','2019-02-07 17:21:08'),(12,'200100030002','商户成员加入信息','商户成员加入信息','2019-02-07 17:21:08'),(13,'200100040001','修改商户信息','修改商户信息','2019-02-07 17:21:08'),(14,'200100040002','商户成员退出信息','商户成员退出信息','2019-02-07 17:21:09'),(15,'200100050001','删除商户信息','删除商户信息','2019-02-07 17:21:09'),(16,'300100030001','保存商品信息','保存商品信息','2019-02-07 17:21:09'),(17,'300100030002','购买商品','购买商品','2019-02-07 17:21:09'),(18,'300100030003','保存商品目录','保存商品目录','2019-02-07 17:21:09'),(19,'300100040001','修改商品信息','修改商品信息','2019-02-07 17:21:09'),(20,'300100040002','修改商品目录','修改商品目录','2019-02-07 17:21:09'),(21,'300100050001','删除商品信息','删除商品信息','2019-02-07 17:21:09'),(22,'300100050002','修改商品目录','修改商品目录','2019-02-07 17:21:09'),(23,'400100030001','保存评论','保存评论','2019-02-07 17:21:09'),(24,'400100050001','删除评论','删除评论','2019-02-07 17:21:09'),(25,'500100030001','保存小区信息','保存小区信息','2019-02-07 17:21:09'),(26,'500100030002','小区成员加入信息','小区成员加入信息','2019-02-07 17:21:09'),(27,'500100040001','修改商户信息','修改商户信息','2019-02-07 17:21:10'),(28,'500100040002','小区成员退出信息','小区成员退出信息','2019-02-07 17:21:10'),(29,'500100050001','删除商户信息','删除商户信息','2019-02-07 17:21:10'),(30,'600100030002','保存物业照片','保存物业照片','2019-02-07 17:21:10'),(31,'600100030003','保存物业证件','保存物业证件','2019-02-07 17:21:10'),(32,'600100030004','保存物业员工','保存物业员工','2019-02-07 17:21:10'),(33,'600100030005','保存物业费用','保存物业费用','2019-02-07 17:21:10'),(34,'600100030006','保存住户','保存住户','2019-02-07 17:21:10'),(35,'600100040001','修改物业信息','修改物业信息','2019-02-07 17:21:10'),(36,'600100040002','修改物业照片','修改物业照片','2019-02-07 17:21:10'),(37,'600100040003','修改物业证件','修改物业证件','2019-02-07 17:21:10'),(38,'600100040004','修改 费用信息','修改 费用信息','2019-02-07 17:21:10'),(39,'600100040005','修改住户信息','修改住户信息','2019-02-07 17:21:10'),(40,'600100050001','删除物业属性','删除物业属性','2019-02-07 17:21:10'),(41,'600100050002','删除物业照片','删除物业照片','2019-02-07 17:21:11'),(42,'600100050003','删除 物业证件','删除 物业证件','2019-02-07 17:21:11'),(43,'600100050004','删除物业员工','删除物业员工','2019-02-07 17:21:11'),(44,'600100050005','删除住户','删除住户','2019-02-07 17:21:11'),(45,'700100030001','保存代理商信息','保存代理商信息','2019-02-07 17:21:11'),(46,'700100030002','保存代理商照片','保存代理商照片','2019-02-07 17:21:11'),(47,'700100030003','保存代理商证件','保存代理商证件','2019-02-07 17:21:11'),(48,'700100030004','添加代理商员工','添加代理商员工','2019-02-07 17:21:11'),(49,'700100040001','修改代理商信息','修改代理商信息','2019-02-07 17:21:11'),(50,'700100040002','修改代理商照片','修改代理商照片','2019-02-07 17:21:11'),(51,'700100040003','修改代理商证件','修改代理商证件','2019-02-07 17:21:11'),(52,'700100050001','删除代理商属性','删除代理商属性','2019-02-07 17:21:11'),(53,'700100050002','删除代理商照片','删除代理商照片','2019-02-07 17:21:11'),(54,'700100050003','删除代理商证件','删除代理商证件','2019-02-07 17:21:11'),(55,'700100050004','删除代理商员工','删除代理商员工','2019-02-07 17:21:11');
/*!40000 ALTER TABLE `c_business_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `c_cache`
--

DROP TABLE IF EXISTS `c_cache`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `c_cache`
--

LOCK TABLES `c_cache` WRITE;
/*!40000 ALTER TABLE `c_cache` DISABLE KEYS */;
INSERT INTO `c_cache` VALUES (1,'1001','flush.center.cache','映射缓存（c_mapping表）','{\"cacheName\":\"MAPPING\"}',1,'COMMON','2018-11-14 13:28:51','0'),(2,'1002','flush.center.cache','业务配置缓存（c_app,c_service,c_route表）','{\"cacheName\":\"APP_ROUTE_SERVICE\"}',2,'COMMON','2018-11-14 13:28:51','0'),(3,'1003','flush.center.cache','公用服务缓存（c_service_sql表）','{\"cacheName\":\"SERVICE_SQL\"}',3,'COMMON','2018-11-14 13:28:51','0');
/*!40000 ALTER TABLE `c_cache` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `c_cache_2_user`
--

DROP TABLE IF EXISTS `c_cache_2_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `c_cache_2_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '缓存用户ID',
  `cache_code` int(11) NOT NULL COMMENT '缓存编码',
  `user_id` varchar(30) NOT NULL COMMENT '用户id',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `status_cd` varchar(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考c_status表，0在用，1失效',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `c_cache_2_user`
--

LOCK TABLES `c_cache_2_user` WRITE;
/*!40000 ALTER TABLE `c_cache_2_user` DISABLE KEYS */;
INSERT INTO `c_cache_2_user` VALUES (1,1001,'10001','2018-11-14 13:28:51','0'),(2,1002,'10001','2018-11-14 13:28:51','0'),(3,1003,'10001','2018-11-14 13:28:51','0');
/*!40000 ALTER TABLE `c_cache_2_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `c_comment`
--

DROP TABLE IF EXISTS `c_comment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `c_comment`
--

LOCK TABLES `c_comment` WRITE;
/*!40000 ALTER TABLE `c_comment` DISABLE KEYS */;
/*!40000 ALTER TABLE `c_comment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `c_comment_score`
--

DROP TABLE IF EXISTS `c_comment_score`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `c_comment_score`
--

LOCK TABLES `c_comment_score` WRITE;
/*!40000 ALTER TABLE `c_comment_score` DISABLE KEYS */;
/*!40000 ALTER TABLE `c_comment_score` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `c_mapping`
--

DROP TABLE IF EXISTS `c_mapping`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `c_mapping`
--

LOCK TABLES `c_mapping` WRITE;
/*!40000 ALTER TABLE `c_mapping` DISABLE KEYS */;
INSERT INTO `c_mapping` VALUES (1,'DOMAIN.COMMON','日志开关','LOG_ON_OFF','OFF','日志开关','2018-11-14 13:28:44','0'),(2,'DOMAIN.COMMON','耗时开关','COST_TIME_ON_OFF','OFF','耗时开关','2018-11-14 13:28:44','0'),(3,'DOMAIN.COMMON','规则开关','RULE_ON_OFF','OFF','规则开关','2018-11-14 13:28:44','0'),(4,'DOMAIN.COMMON','不调规则服务的订单类型','NO_NEED_RULE_VALDATE_ORDER','Q','不调规则服务的订单类型','2018-11-14 13:28:44','0'),(5,'DOMAIN.COMMON','不保存订单信息','NO_SAVE_ORDER','Q,T','不保存订单信息','2018-11-14 13:28:44','0'),(6,'DOMAIN.COMMON','不用调用 下游系统的配置','NO_INVOKE_BUSINESS_SYSTEM','Q','不用调用 下游系统的配置(一般不存在这种情况，这里主要是在没有下游系统的情况下测试中心服务用)','2018-11-14 13:28:44','0'),(7,'DOMAIN.COMMON','不用调用 作废下游系统的配置','NO_INVALID_BUSINESS_SYSTEM','Q','不用调用 作废下游系统的配置 (一般不存在这种情况，这里主要是在没有下游系统的情况下测试中心服务用)','2018-11-14 13:28:44','0'),(8,'DOMAIN.COMMON','需要调用服务生成各个ID','NEED_INVOKE_SERVICE_GENERATE_ID','OFF','需要调用服务生成各个ID','2018-11-14 13:28:44','0'),(9,'DOMAIN.COMMON','公钥','PUBLIC_STRING','MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDloKXSBA5+tP39uS8yi5RZOs6Jdrt0znRQetyXX2l/IUCi1x1QAMgoZbnEavmdZ5jOZN/T1WYFbt/VomXEHaTdStAiYm3DCnxxy5CMMyRKai0+6n4lLJQpUmnAQPFENrOV8b70gBSBVjUXksImgui5qYaNqX90pjEzcyq+6CugBwIDAQAB','公钥','2018-11-14 13:28:44','0'),(10,'DOMAIN.COMMON','私钥','PRIVATE_STRING','MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAJbtQYV+VpWZvifoc0R11MyAfIyMGoJKHDrWQau7oxLHotDDJM80o7ea7oL2onaHWOXaybpUp5FpZgjuixYMhlQOA/VXosrJOGJhgNv0dAL6VVXxmjlg2UWqIEoyTS7IzF3BuQCqy2k9aT7mGiC0RYRpndTuwe/0DKwFx70dIIIrAgMBAAECgYBMNMHnqLIJWZa1Sd6hy6lGFP5ObROZg9gbMUH5d4XQnrKsHEyCvz6HH5ic0fGYTaDqdn1zMvllJ8XYbrIV0P8lvHr9/LCnoXessmf61hKZyTKi5ycNkiPHTjmJZCoVTQFprcNgYeVX4cvNsqB2zWwzoAk8bbdWY6X5jB6YEpfBmQJBANiO9GiBtw+T9h60MpyV1xhJKsx0eCvxRZcsDB1OLZvQ7dHnsHmh0xUBL2MraHKnQyxOlrIzOtyttxSTrQzwcM0CQQCyajkzxpF6EjrXWHYVHb3AXFSoz5krjOkLEHegYlGhx0gtytBNVwftCn6hqtaxCxKMp00ZJoXIxo8d9tQy4N7XAkBljnTT5bEBnzPWpk7t298pRnbJtvz8LoOiJ0fvHlCJN+uvemXqRJeGzC165kpvKj14M8q7+wZpoxWukqqe3MspAkAuFYHw/blV7p+EQDU//w6kQTUc5YKK3TrUwMwlgT/UqcTbDyf+0hwZ/jv3RkluMY35Br3DYU/tLFyLQNZOzgbBAkEApWARXVlleEYbv8dPUL+56S0ky1hZSuPfVOBda4V3p0q18LjcHIyYcVhKGqkpii5JgblaYyjUriNDisFalCp8jQ==','私钥','2018-11-14 13:28:44','0'),(11,'DOMAIN.COMMON','外部应用公钥','PUBLIC_STRING','MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCW7UGFflaVmb4n6HNEddTMgHyMjBqCShw61kGru6MSx6LQwyTPNKO3mu6C9qJ2h1jl2sm6VKeRaWYI7osWDIZUDgP1V6LKyThiYYDb9HQC+lVV8Zo5YNlFqiBKMk0uyMxdwbkAqstpPWk+5hogtEWEaZ3U7sHv9AysBce9HSCCKwIDAQAB','外部应用公钥','2018-11-14 13:28:44','0'),(12,'DOMAIN.COMMON','外部应用私钥','PRIVATE_STRING','MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAOWgpdIEDn60/f25LzKLlFk6zol2u3TOdFB63JdfaX8hQKLXHVAAyChlucRq+Z1nmM5k39PVZgVu39WiZcQdpN1K0CJibcMKfHHLkIwzJEpqLT7qfiUslClSacBA8UQ2s5XxvvSAFIFWNReSwiaC6Lmpho2pf3SmMTNzKr7oK6AHAgMBAAECgYEAlfR5FVNM2/X6QC0k408/i53Zru94r2j7kGsLj1bhoAHpIe502AAKtkboL5rkc6Rpp688dCvRug6T4gFxj8cEF7rOOU4CHqVCHUHa4sWSDL2Rg7pMbQOVB7PGmM4C/hEgXcwM6tmLiU3xkkQDrpgT1bPpAm7iwDx4HkZBv1naYnECQQDyk40+KDvyUgp/r1tKbkMLoQOAfTZPXy+HgeAkU3PCUTFQlvn2OU6Fsl3Yjlp6utxPVnd00DoPZ8qvx1falaeLAkEA8lWoIDgyYwnibv2fr3A715PkieJ0exKfLb5lSD9UIfGJ9s7oTcltl7pprykfSP46heWjScS7YJRZHPfqb1/Y9QJAJNUQqjJzv7yDSZX3t5p8ZaSiIn1gpLagQeQPg5SETCoF4eW6uI9FA/nsU/hxdpcu4oEPjFYdqr8owH31MgRtNwJAaE+6qPPHrJ3qnAAMJoZXG/qLG1cg8IEZh6U3D5xC6MGBs31ovWMBC5iwOTeoQdE8+7nXSb+nMHFq0m9cuEg3qQJAH4caPSQ9RjVOP9on+niy9b1mATbvurepIB95KUtaHLz1hpihCLR7dTwrz8JOitgFE75Wzt4a00GZYxnaq3jsjA==','外部应用私钥','2018-11-14 13:28:44','0'),(13,'DOMAIN.COMMON','默认KEY_SIZE','DEFAULT_DECRYPT_KEY_SIZE','2048','默认KEY_SIZE','2018-11-14 13:28:44','0'),(14,'DOMAIN.COMMON','中心服务地址','CENTER_SERVICE_URL','http://center-service/httpApi/service','中心服务地址','2018-11-14 13:28:44','0'),(15,'DOMAIN.COMMON','控制中心APP_ID','CONSOLE_SERVICE_APP_ID','8000418002','控制中心APP_ID','2018-11-14 13:28:44','0'),(16,'DOMAIN.COMMON','控制服务加密开关','KEY_CONSOLE_SERVICE_SECURITY_ON_OFF','ON','控制服务加密开关','2018-11-14 13:28:44','0'),(17,'DOMAIN.COMMON','控制服务鉴权秘钥','CONSOLE_SECURITY_CODE','WEBURFPKIFJUHNCJUEIKMKJUJHULSMNCHDY89KMC','控制服务鉴权秘钥','2018-11-14 13:28:44','0'),(18,'DOMAIN.COMMON','编码生成服务地址','CODE_PATH','http://code-service/codeApi/generate','编码生成服务地址','2018-11-14 13:28:44','0'),(19,'DOMAIN.COMMON','API服务地址','API_SERVICE_URL','http://api-service/api/#serviceCode#','API服务地址','2018-11-18 07:35:03','0'),(20,'DOMAIN.COMMON','员工默认密码','STAFF_DEFAULT_PASSWORD','cdfea6fbffcd82758eec12a96c64e682','员工默认密码','2018-12-08 16:33:14','0'),(21,'DOMAIN.COMMON','用户服务地址','ORDER_USER_SERVICE_URL','http://user-service/userApi/service','用户服务地址','2019-02-08 13:51:33','0'),(22,'DOMAIN.COMMON','商户服务地址','ORDER_STORE_SERVICE_URL','http://store-service/storeApi/service','商户服务地址','2019-02-08 13:54:53','0'),(23,'DOMAIN.COMMON','商品服务地址','ORDER_SHOP_SERVICE_URL','http://shop-service/shopApi/service','商品服务地址','2019-02-08 13:57:57','0'),(24,'DOMAIN.COMMON','评价服务地址','ORDER_COMMENT_SERVICE_URL','http://comment-service/commentApi/service','评价服务地址','2019-02-08 14:00:56','0'),(25,'DOMAIN.COMMON','小区服务地址','ORDER_COMMUNITY_SERVICE_URL','http://community-service/communityApi/service','小区服务地址','2019-02-08 14:04:28','0'),(26,'DOMAIN.COMMON','物业服务地址','ORDER_PROPERTY_SERVICE_URL','http://property-service/propertyApi/service','物业服务地址','2019-02-08 14:07:09','0'),(27,'DOMAIN.COMMON','代理商服务地址','ORDER_AGENT_SERVICE_URL','http://agent-service/agentApi/service','代理商服务地址','2019-02-08 14:10:14','0');
/*!40000 ALTER TABLE `c_mapping` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `c_order_type`
--

DROP TABLE IF EXISTS `c_order_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `c_order_type` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `order_type_cd` varchar(4) NOT NULL COMMENT '订单类型',
  `name` varchar(50) NOT NULL COMMENT '名称',
  `description` varchar(200) DEFAULT NULL COMMENT '描述',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `order_type_cd` (`order_type_cd`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `c_order_type`
--

LOCK TABLES `c_order_type` WRITE;
/*!40000 ALTER TABLE `c_order_type` DISABLE KEYS */;
INSERT INTO `c_order_type` VALUES (1,'Q','查询单','查询单','2018-11-14 13:28:44'),(2,'D','受理单','受理单','2018-11-14 13:28:44');
/*!40000 ALTER TABLE `c_order_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `c_orders`
--

DROP TABLE IF EXISTS `c_orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `c_orders` (
  `o_id` varchar(30) NOT NULL COMMENT '订单ID',
  `app_id` varchar(30) NOT NULL COMMENT '应用ID',
  `ext_transaction_id` varchar(36) DEFAULT NULL,
  `user_id` varchar(30) NOT NULL COMMENT '用户ID',
  `request_time` varchar(16) NOT NULL COMMENT '外部系统请求时间',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `order_type_cd` varchar(4) NOT NULL COMMENT '订单类型，参考c_order_type表',
  `finish_time` date DEFAULT NULL COMMENT '订单完成时间',
  `remark` varchar(200) DEFAULT NULL COMMENT '备注',
  `status_cd` varchar(2) NOT NULL COMMENT '数据状态，详细参考c_status表',
  UNIQUE KEY `o_id` (`o_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `c_orders`
--

LOCK TABLES `c_orders` WRITE;
/*!40000 ALTER TABLE `c_orders` DISABLE KEYS */;
INSERT INTO `c_orders` VALUES ('-1','8000418002','10029082726','1234','20181113225612','2018-12-09 03:16:38','D',NULL,NULL,'C'),('102018111500000001','8000418001','100000000020180409224736000001','1101','20181115215136','2018-11-15 15:04:21','D',NULL,'保存用户录入费用信息','C'),('102018111500000002','8000418001','100000000020180409224736000001','1101','20181115215136','2018-11-15 15:16:48','D',NULL,'保存用户录入费用信息','C'),('102018111500000003','8000418001','100000000020180409224736000001','1101','20181115215136','2018-11-15 15:17:19','D',NULL,'保存用户录入费用信息','C'),('102018111500000004','8000418001','100000000020180409224736000001','1101','20181115215136','2018-11-15 15:18:04','D',NULL,'保存用户录入费用信息','C'),('102018111500000005','8000418001','100000000020180409224736000001','1101','20181115215136','2018-11-15 15:19:03','D',NULL,'保存用户录入费用信息','C'),('102019020200000001','8000418002','18094ce026f011e98223f3a4fd3a50b2','-1','20190202214024','2019-02-02 13:40:26','D',NULL,NULL,'C'),('102019020200000002','8000418002','e87abdd026f211e98223f3a4fd3a50b2','-1','20190202220033','2019-02-02 14:00:33','D',NULL,NULL,'C'),('102019020300000002','8000418002','9c45b530270311e98223f3a4fd3a50b2','-1','20190203000007','2019-02-02 16:00:07','D',NULL,NULL,'C'),('102019021000000001','8000418002','10029082726','-1','20181113225612','2019-02-09 16:06:43','D',NULL,NULL,'S'),('102019021000000002','8000418002','10029082726','-1','20181113225612','2019-02-09 16:24:20','D',NULL,NULL,'S'),('102019021000000003','8000418002','10029082726','-1','20181113225612','2019-02-09 16:28:16','D',NULL,NULL,'S'),('102019021000000004','8000418002','10029082726','-1','20181113225612','2019-02-09 16:24:42','D',NULL,NULL,'S'),('102019021000000005','8000418002','10029082726','-1','20181113225612','2019-02-09 16:30:04','D',NULL,NULL,'S'),('102019021000000006','8000418002','10029082726','-1','20181113225612','2019-02-09 16:36:01','D',NULL,NULL,'S'),('102019021000000007','8000418002','10029082726','-1','20181113225612','2019-02-09 16:31:24','D',NULL,NULL,'S'),('102019021000000008','8000418002','10029082726','-1','20181113225612','2019-02-09 16:57:44','D',NULL,NULL,'C'),('102019021024050001','8000418002','20d69a102d4111e990a5af8dfa9ee364','-1','20190210223535','2019-02-10 14:35:36','D',NULL,NULL,'C'),('102019021031750001','8000418002','10029082726','-1','20181113225612','2019-02-09 17:17:40','D',NULL,NULL,'C'),('102019021042560005','8000418002','53a5f4e02d4111e990a5af8dfa9ee364','-1','20190210223701','2019-02-10 14:37:01','D',NULL,NULL,'C'),('102019021043400007','8000418002','84820b302d4111e990a5af8dfa9ee364','-1','20190210223823','2019-02-10 14:38:23','D',NULL,NULL,'C'),('102019021083940003','8000418002','2c05e9e02d4111e990a5af8dfa9ee364','-1','20190210223554','2019-02-10 14:35:54','D',NULL,NULL,'C'),('102019021086310003','8000418002','10029082726','-1','20181113225612','2019-02-09 17:23:13','D',NULL,NULL,'C'),('10512770937368608768','8000418001','100000000020180409224736000001','1101','20181115215136','2018-11-15 15:28:39','D',NULL,'保存用户录入费用信息','C'),('10512771452089401344','8000418001','100000000020180409224736000001','1101','20181115215136','2018-11-15 15:30:42','D',NULL,'保存用户录入费用信息','C'),('10512773560800903168','8000418001','100000000020180409224736000001','1101','20181115215136','2018-11-15 15:39:04','D',NULL,'保存用户录入费用信息','E'),('10512774197236203520','8000418001','100000000020180409224736000001','1101','20181115215136','2018-11-15 15:41:36','D',NULL,'保存用户录入费用信息','E'),('10512774702192656384','8000418001','100000000020180409224736000001','1101','20181115215136','2018-11-15 15:43:36','D',NULL,'保存用户录入费用信息','E'),('10512774782215782400','8000418001','100000000020180409224736000001','1101','20181115215136','2018-11-15 15:43:55','D',NULL,'保存用户录入费用信息','E'),('10512778366974164992','8000418001','100000000020180409224736000001','1101','20181115215136','2018-11-15 15:58:10','D',NULL,'保存用户录入费用信息','E'),('10512781061441191936','8000418001','100000000020180409224736000001','1101','20181115215136','2018-11-15 16:08:53','D',NULL,'保存用户录入费用信息','E'),('10512781921827160064','8000418001','100000000020180409224736000001','1101','20181115215136','2018-11-15 16:12:18','D',NULL,'保存用户录入费用信息','E'),('10512782241781252096','8000418001','100000000020180409224736000001','1101','20181115215136','2018-11-15 16:13:34','D',NULL,'保存用户录入费用信息','C'),('10512782357623734272','8000418001','100000000020180409224736000001','1101','20181115215136','2018-11-15 16:14:02','D',NULL,'保存用户录入费用信息','C'),('10513135737009340416','8000418001','100000000020180409224736000002','1101','20181115215136','2018-11-16 15:38:14','D',NULL,'保存用户录入费用信息','C'),('10513136391413039104','8000418001','100000000020180409224736000002','1101','20181115215136','2018-11-16 15:40:50','D',NULL,'保存用户录入费用信息','E'),('10513138492071477248','8000418001','100000000020180409224736000002','1101','20181115215136','2018-11-16 15:49:10','D',NULL,'保存用户录入费用信息','E'),('10513138958218035200','8000418001','100000000020180409224736000002','1101','20181115215136','2018-11-16 15:51:01','D',NULL,'保存用户录入费用信息','E'),('10513139665960697856','8000418001','100000000020180409224736000002','1101','20181115215136','2018-11-16 15:53:50','D',NULL,'保存用户录入费用信息','E'),('10513140466590416896','8000418001','100000000020180409224736000002','1101','20181115215136','2018-11-16 15:57:01','D',NULL,'保存用户录入费用信息','E'),('10513141044963966976','8000418001','100000000020180409224736000002','800625487','20181115215136','2018-11-16 15:59:19','D',NULL,'保存用户录入费用信息','E'),('10513143017092153344','8000418001','100000000020180409224736000002','1101','20181115215136','2018-11-16 16:07:09','D',NULL,'保存用户录入费用信息','C'),('10513145621712994304','8000418001','100000000020180409224736000002','800625487','20181115215136','2018-11-16 16:17:30','D',NULL,'保存用户录入费用信息','C'),('10513145827309387776','8000418001','100000000020180409224736000002','800625487','20181115215136','2018-11-16 16:18:19','D',NULL,'保存用户录入费用信息','C'),('10513146861406650368','8000418001','100000000020180409224736000002','800625487','20181115215136','2018-11-16 16:22:26','D',NULL,'保存用户录入费用信息','C'),('10516289245648797696','8000418002','10029082726','-1','20181113225612','2018-11-25 08:29:09','D',NULL,NULL,'C'),('10516292807556612096','8000418002','10029082726','-1','20181113225612','2018-11-25 08:43:18','D',NULL,NULL,'E'),('10516293454683193344','8000418002','10029082726','-1','20181113225612','2018-11-25 08:45:51','D',NULL,NULL,'E'),('10516297437925621760','8000418002','10029082726','-1','20181113225612','2018-11-25 09:01:42','D',NULL,NULL,'E'),('10516297910447521792','8000418002','10029082726','-1','20181113225612','2018-11-25 09:03:34','D',NULL,NULL,'E'),('10516329683684442112','8000418002','10029082726','-1','20181113225612','2018-11-25 11:09:49','D',NULL,NULL,'C'),('10516332294286360576','8000418002','10029082726','-1','20181113225612','2018-11-25 11:20:11','D',NULL,NULL,'C'),('10516364589760266240','8000418002','10029082726','-1','20181113225612','2018-11-25 13:28:31','D',NULL,NULL,'C'),('10516365201428201472','8000418002','10029082726','-1','20181113225612','2018-11-25 13:30:57','D',NULL,NULL,'C'),('10516366112816906240','8000418002','10029082726','-1','20181113225612','2018-11-25 13:34:34','D',NULL,NULL,'C'),('10516366708789755904','8000418002','10029082726','-1','20181113225612','2018-11-25 13:36:57','D',NULL,NULL,'C'),('10516368306135908352','8000418002','10029082726','-1','20181113225612','2018-11-25 13:43:17','D',NULL,NULL,'C'),('10516374464053657600','8000418002','10029082726','-1','20181113225612','2018-11-25 14:07:46','D',NULL,NULL,'C'),('10516374772444053504','8000418002','10029082726','-1','20181113225612','2018-11-25 14:08:59','D',NULL,NULL,'C'),('10516374801552523264','8000418002','10029082726','-1','20181113225612','2018-11-25 14:09:06','D',NULL,NULL,'C'),('10516377393070358528','8000418002','10029082726','-1','20181113225612','2018-11-25 14:19:24','D',NULL,NULL,'C'),('10516381939469402112','8000418002','10029082726','-1','20181113225612','2018-11-25 14:37:28','D',NULL,NULL,'C'),('10516387722441539584','8000418002','10029082726','-1','20181113225612','2018-11-25 15:00:27','D',NULL,NULL,'C'),('10516389224614739968','8000418002','10029082726','-1','20181113225612','2018-11-25 15:06:25','D',NULL,NULL,'C'),('10516389264993304576','8000418002','10029082726','-1','20181113225612','2018-11-25 15:06:34','D',NULL,NULL,'C'),('10516398820054024192','8000418002','00c93840-f0c9-11e8-96a4-6777a97d0cd9','-1','20181125234432','2018-11-25 15:44:32','D',NULL,NULL,'C'),('10516400356129783808','8000418002','db091b60f0c9-11e8-b38a-cb522b025e1e','-1','20181125235038','2018-11-25 15:50:39','D',NULL,NULL,'C'),('10516401273776390144','8000418002','5d6face0f0ca11e8b4db91aaf43b3c01','-1','20181125235417','2018-11-25 15:54:17','D',NULL,NULL,'C'),('10517072482688057344','8000418002','f3e315c0f23e11e8a91fdd40351585ea','-1','20181127202122','2018-11-27 12:21:26','D',NULL,NULL,'C'),('10517086035339919360','8000418002','7bfb0100f24611e8a91fdd40351585ea','-1','20181127211517','2018-11-27 13:15:17','D',NULL,NULL,'C'),('10517710156889341952','8000418002','10029082726','-1','20181113225612','2018-11-29 06:35:20','D',NULL,NULL,'C'),('10518939876595351552','8000418002','918b5d90f64b11e893c895bbc9fa8c03','-1','20181203000145','2018-12-02 16:01:48','D',NULL,NULL,'C'),('10518940136222769152','8000418002','b6db3700f64b11e893c895bbc9fa8c03','-1','20181203000248','2018-12-02 16:02:49','D',NULL,NULL,'C'),('10520751769869893632','8000418002','5fcde8d0fa3911e8ba4ecf9afe208990','-1','20181208000136','2018-12-07 16:01:37','D',NULL,NULL,'C'),('10520752094110564352','8000418002','8e424df0fa3911e8ba4ecf9afe208990','-1','20181208000254','2018-12-07 16:02:54','D',NULL,NULL,'C'),('10520752515780722688','8000418002','ca2dd000fa3911e8ba4ecf9afe208990','-1','20181208000434','2018-12-07 16:04:34','D',NULL,NULL,'C'),('10520752653978845184','8000418002','ddce3a00fa3911e8ba4ecf9afe208990','-1','20181208000507','2018-12-07 16:05:07','D',NULL,NULL,'C'),('10520753110897934336','8000418002','1eb4e960fa3a11e8ba4ecf9afe208990','-1','20181208000656','2018-12-07 16:06:56','D',NULL,NULL,'C'),('10521002813006823424','8000418002','b96295e0fac411e8a78813cab7e028a1','-1','20181208163906','2018-12-08 08:39:11','D',NULL,NULL,'C'),('10521124884286291968','8000418002','10029082726','-1','20181113225612','2018-12-08 16:44:17','D',NULL,NULL,'C'),('10521125063953498112','8000418002','10029082726','-1','20181113225612','2018-12-08 16:44:57','D',NULL,NULL,'C'),('10521126624020676608','8000418002','10029082726','1234','20181113225612','2018-12-08 16:51:09','D',NULL,NULL,'C'),('10521126943786024960','8000418002','10029082726','1234','20181113225612','2018-12-08 16:52:25','D',NULL,NULL,'C'),('10521280167822245888','8000418002','10029082726','1234','20181113225612','2018-12-09 03:01:17','D',NULL,NULL,'C'),('10521280438203858944','8000418002','10029082726','1234','20181113225612','2018-12-09 03:02:21','D',NULL,NULL,'C'),('10521281261306658816','8000418002','10029082726','1234','20181113225612','2018-12-09 03:05:37','D',NULL,NULL,'C'),('10521289747750993920','8000418002','10029082726','1234','20181113225612','2018-12-09 03:39:21','D',NULL,NULL,'E'),('10521292247023501312','8000418002','10029082726','1234','20181113225612','2018-12-09 03:49:17','D',NULL,NULL,'D'),('10521294331546451968','8000418002','10029082726','1234','20181113225612','2018-12-09 03:57:34','D',NULL,NULL,'C'),('10521294983534231552','8000418002','10029082726','1234','20181113225612','2018-12-09 04:00:09','D',NULL,NULL,'C'),('10521299093553692672','8000418002','10029082726','-1','20181113225612','2018-12-09 04:16:29','D',NULL,NULL,'C'),('10521384632348852224','8000418002','10029082726','-1','20181113225612','2018-12-09 09:56:23','D',NULL,NULL,'C'),('10521384655329443840','8000418002','10029082726','-1','20181113225612','2018-12-09 09:56:28','D',NULL,NULL,'C'),('10521384680075837440','8000418002','10029082726','-1','20181113225612','2018-12-09 09:56:34','D',NULL,NULL,'C'),('10521384772841259008','8000418002','10029082726','-1','20181113225612','2018-12-09 09:56:56','D',NULL,NULL,'C'),('10521384859650768896','8000418002','10029082726','-1','20181113225612','2018-12-09 09:57:17','D',NULL,NULL,'C'),('10521388278742532096','8000418002','10029082726','-1','20181113225612','2018-12-09 10:10:52','D',NULL,NULL,'C'),('10521388292340465664','8000418002','10029082726','-1','20181113225612','2018-12-09 10:10:55','D',NULL,NULL,'C'),('10521388308358512640','8000418002','10029082726','-1','20181113225612','2018-12-09 10:10:59','D',NULL,NULL,'C'),('10521388322531065856','8000418002','10029082726','-1','20181113225612','2018-12-09 10:11:03','D',NULL,NULL,'C'),('10521388335550185472','8000418002','10029082726','-1','20181113225612','2018-12-09 10:11:06','D',NULL,NULL,'C'),('10521470366736990208','8000418002','46f4a7f0fbc811e8908d35b3675f85f8','-1','20181209233703','2018-12-09 15:37:03','D',NULL,NULL,'C'),('10521470728638316544','8000418002','7a65e810fbc811e8908d35b3675f85f8','-1','20181209233830','2018-12-09 15:38:30','D',NULL,NULL,'C'),('10521470972654534656','8000418002','9d106b10fbc811e8908d35b3675f85f8','-1','20181209233928','2018-12-09 15:39:28','D',NULL,NULL,'C'),('10521471733287370752','8000418002','0925a360fbc911e8908d35b3675f85f8','-1','20181209234229','2018-12-09 15:42:29','D',NULL,NULL,'C'),('10521472810514661376','8000418002','a23b6580fbc911e8908d35b3675f85f8','-1','20181209234646','2018-12-09 15:46:46','D',NULL,NULL,'C'),('10521692191735693312','8000418002','6a148c20fc4311e8a88c7d9b2593ccaa','-1','20181210141830','2018-12-10 06:18:31','D',NULL,NULL,'C'),('10521692260782325760','8000418002','73e66ed0fc4311e8a88c7d9b2593ccaa','-1','20181210141847','2018-12-10 06:18:47','D',NULL,NULL,'C'),('10521692315148894208','8000418002','7ba06e00fc4311e8a88c7d9b2593ccaa','-1','20181210141900','2018-12-10 06:19:00','D',NULL,NULL,'C'),('10521692341610758144','8000418002','7f69d210fc4311e8a88c7d9b2593ccaa','-1','20181210141906','2018-12-10 06:19:06','D',NULL,NULL,'C'),('10521692506786643968','8000418002','96e6da00fc4311e8a88c7d9b2593ccaa','-1','20181210141945','2018-12-10 06:19:46','D',NULL,NULL,'C'),('10521692648902246400','8000418002','ab1af880fc4311e8a88c7d9b2593ccaa','-1','20181210142019','2018-12-10 06:20:20','D',NULL,NULL,'C'),('10522549146947698688','8000418002','1ed3abf0fe1f11e8bed093e05f6f4593','-1','20181212230344','2018-12-12 15:03:45','D',NULL,NULL,'C'),('10523565515541594112','8000418002','516ec410005311e9bd932f1415ca3f9c','-1','20181215182226','2018-12-15 10:22:26','D',NULL,NULL,'C');
/*!40000 ALTER TABLE `c_orders` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `c_orders_attrs`
--

DROP TABLE IF EXISTS `c_orders_attrs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `c_orders_attrs` (
  `o_id` varchar(30) NOT NULL COMMENT '订单ID',
  `attr_id` varchar(30) NOT NULL COMMENT '属性id',
  `spec_cd` varchar(12) NOT NULL COMMENT '规格id,参考spec表',
  `value` varchar(50) NOT NULL COMMENT '属性值'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `c_orders_attrs`
--

LOCK TABLES `c_orders_attrs` WRITE;
/*!40000 ALTER TABLE `c_orders_attrs` DISABLE KEYS */;
INSERT INTO `c_orders_attrs` VALUES ('10512771452089401344','11512771453565796352','1001','测试案例'),('10512773560800903168','11512773562461847552','1001','测试案例'),('10512774197236203520','11512774198477717504','1001','测试案例'),('10512774702192656384','11512774703383838720','1001','测试案例'),('10512774782215782400','11512774783264358400','1001','测试案例'),('10512778366974164992','11512778368580583424','1001','测试案例'),('10512781061441191936','11512781062695288832','1001','测试案例'),('10512781921827160064','11512781923018342400','1001','测试案例'),('10512782241781252096','11512782243047931904','1001','测试案例'),('10512782357623734272','11512782358638755840','1001','测试案例'),('10513135737009340416','11513135739198767104','1001','测试案例'),('10513136391413039104','11513136392964931584','1001','测试案例'),('10513138492071477248','11513138493426237440','1001','测试案例'),('10513138958218035200','11513138959384051712','1001','测试案例'),('10513139665960697856','11513139667198017536','1001','测试案例'),('10513140466590416896','11513140468163280896','1001','测试案例'),('10513141044963966976','11513141046289367040','1001','测试案例'),('10513143017092153344','11513143018937647104','1001','测试案例'),('10513145621712994304','11513145623130669056','1001','测试案例'),('10513145827309387776','11513145828433461248','1001','测试案例'),('10513146861406650368','11513146864455909376','1001','测试案例');
/*!40000 ALTER TABLE `c_orders_attrs` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `c_route`
--

DROP TABLE IF EXISTS `c_route`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
) ENGINE=InnoDB AUTO_INCREMENT=45 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `c_route`
--

LOCK TABLES `c_route` WRITE;
/*!40000 ALTER TABLE `c_route` DISABLE KEYS */;
INSERT INTO `c_route` VALUES (1,'8000418001',1,'Q',NULL,'S','2018-11-14 13:28:44','0'),(2,'8000418001',3,'Q',NULL,'S','2018-11-14 13:28:44','0'),(3,'8000418002',3,'Q',NULL,'S','2018-11-14 13:28:44','0'),(4,'8000418002',4,'Q',NULL,'S','2018-11-14 13:28:44','0'),(5,'8000418002',5,'Q',NULL,'S','2018-11-14 13:28:44','0'),(6,'8000418002',6,'Q',NULL,'S','2018-11-14 13:28:44','0'),(7,'8000418002',7,'Q',NULL,'S','2018-11-14 13:28:44','0'),(8,'8000418002',8,'Q',NULL,'S','2018-11-14 13:28:44','0'),(9,'8000418002',9,'Q',NULL,'S','2018-11-14 13:28:44','0'),(10,'8000418002',10,'Q',NULL,'S','2018-11-14 13:28:44','0'),(11,'8000418002',11,'Q',NULL,'S','2018-11-14 13:28:44','0'),(12,'8000418002',12,'Q',NULL,'S','2018-11-14 13:28:44','0'),(13,'8000418002',13,'Q',NULL,'S','2018-11-14 13:28:45','0'),(14,'8000418002',14,'Q',NULL,'S','2018-11-14 13:28:45','0'),(15,'8000418002',15,'Q',NULL,'S','2018-11-14 13:28:45','0'),(16,'8000418002',16,'Q',NULL,'S','2018-11-14 13:28:45','0'),(17,'8000418002',17,'Q',NULL,'S','2018-11-14 13:28:45','0'),(18,'8000418001',21,'Q',NULL,'S','2018-11-14 13:28:45','0'),(19,'8000418001',22,'Q',NULL,'S','2018-11-14 13:28:45','0'),(20,'8000418001',23,'Q',NULL,'S','2018-11-14 13:28:45','0'),(21,'8000418001',24,'Q',NULL,'S','2018-11-14 13:28:45','0'),(22,'8000418001',25,'Q',NULL,'S','2018-11-14 13:28:45','0'),(23,'8000418001',26,'Q',NULL,'S','2018-11-14 13:28:45','0'),(24,'8000418001',27,'Q',NULL,'S','2018-11-14 13:28:45','0'),(25,'8000418001',33,'Q',NULL,'S','2018-11-15 13:46:45','0'),(26,'8000418001',34,'Q',NULL,'S','2018-11-16 15:31:35','0'),(27,'8000418002',36,'Q',NULL,'S','2018-11-24 17:01:47','0'),(28,'8000418002',37,'Q',NULL,'S','2018-11-24 17:23:47','0'),(29,'8000418002',38,'Q',NULL,'S','2018-11-27 16:22:58','0'),(30,'8000418002',39,'Q',NULL,'S','2018-11-27 16:22:58','0'),(31,'8000418002',40,'D',NULL,'S','2018-12-08 16:15:22','0'),(32,'8000418002',41,'D',NULL,'S','2018-12-08 16:15:42','0'),(33,'8000418002',42,'D',NULL,'S','2018-12-08 16:15:50','0'),(34,'8000418002',43,'D',NULL,'S','2018-12-08 16:15:57','0'),(35,'8000418002',44,'D',NULL,'S','2018-12-08 16:16:04','0'),(36,'8000418002',45,'D',NULL,'S','2018-12-08 16:16:09','0'),(37,'8000418002',46,'Q',NULL,'S','2018-12-09 07:36:00','0'),(38,'8000418002',47,'Q',NULL,'S','2018-12-12 10:23:04','0'),(39,'8000418002',48,'Q',NULL,'S','2018-12-12 14:24:12','0'),(40,'8000418002',49,'Q',NULL,'S','2018-12-12 14:56:54','0'),(41,'8000418002',50,'D',NULL,'S','2018-12-17 15:29:11','0'),(42,'8000418002',51,'D',NULL,'S','2018-12-17 15:29:11','0'),(43,'8000418002',52,'D',NULL,'S','2018-12-17 15:29:11','0'),(44,'8000418002',53,'D',NULL,'S','2018-12-17 15:29:11','0');
/*!40000 ALTER TABLE `c_route` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `c_service`
--

DROP TABLE IF EXISTS `c_service`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `c_service` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `service_id` varchar(12) NOT NULL COMMENT '服务ID',
  `service_code` varchar(50) NOT NULL COMMENT '自定义，命名方式查询类query.+目标系统+.+业务名称 保存类 save.+目标系统+.+业务名称 修改类 modify.+目标系统+.+业务名称 删除类 remove.+目标系统+.+业务名称 例如：query.user.userinfo save.user.adduserinfo',
  `business_type_cd` varchar(12) DEFAULT NULL,
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
) ENGINE=InnoDB AUTO_INCREMENT=57 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `c_service`
--

LOCK TABLES `c_service` WRITE;
/*!40000 ALTER TABLE `c_service` DISABLE KEYS */;
INSERT INTO `c_service` VALUES (1,'1','query.user.userInfo','API','用户信息查询',1,NULL,'NT','http://...','GET',60,3,'8000418001','2018-11-18 05:44:00','0'),(2,'2','query.order.orderInfo','API','订单信息',1,NULL,'NT','http://order-service/businessApi/query','GET',60,3,'8000418001','2018-11-18 05:44:00','0'),(3,'3','query.console.menu','API','查询菜单',1,NULL,'NT','http://order-service/businessApi/query','GET',60,3,'8000418002','2018-11-18 05:44:00','0'),(4,'4','query.user.loginInfo','API','查询用户登录信息',1,NULL,'NT','http://user-service/businessApi/query','GET',60,3,'8000418002','2018-11-18 05:44:01','0'),(5,'5','query.console.template','API','查询模板信息',1,NULL,'NT','http://order-service/businessApi/query','GET',60,3,'8000418002','2018-11-18 05:44:01','0'),(6,'6','query.console.templateCol','API','查询列模板信息',1,NULL,'NT','http://order-service/businessApi/query','GET',60,3,'8000418002','2018-11-18 05:44:01','0'),(7,'7','query.center.mapping','API','查询映射数据',1,NULL,'NT','http://order-service/businessApi/query','GET',60,3,'8000418002','2018-11-18 05:44:01','0'),(8,'8','query.center.apps','API','查询外部应用',1,NULL,'NT','http://order-service/businessApi/query','GET',60,3,'8000418002','2018-11-18 05:44:01','0'),(9,'9','query.center.services','API','查询服务',1,NULL,'NT','http://order-service/businessApi/query','GET',60,3,'8000418002','2018-11-18 05:44:01','0'),(10,'10','query.center.routes','API','查询路由',1,NULL,'NT','http://order-service/businessApi/query','GET',60,3,'8000418002','2018-11-18 05:44:01','0'),(11,'11','flush.center.cache','API','CenterService缓存',1,NULL,'NT','http://order-service/cacheApi/flush','GET',60,3,'8000418002','2018-11-18 05:44:01','0'),(12,'12','query.console.caches','API','查询所有缓存',1,NULL,'NT','http://order-service/businessApi/query','GET',60,3,'8000418002','2018-11-18 05:44:01','0'),(13,'13','query.console.cache','API','查询所有缓存',1,NULL,'NT','http://order-service/businessApi/query','GET',60,3,'8000418002','2018-11-18 05:44:01','0'),(14,'14','save.center.mapping','Q','保存映射信息',1,NULL,'N','http://order-service/businessApi/do',NULL,60,3,'8000418002','2018-11-18 05:44:01','0'),(15,'15','delete.center.mapping','Q','删除映射信息',1,NULL,'N','http://order-service/businessApi/do',NULL,60,3,'8000418002','2018-11-18 05:44:01','0'),(16,'16','update.center.mapping','Q','修改映射信息',1,NULL,'N','http://order-service/businessApi/do',NULL,60,3,'8000418002','2018-11-18 05:44:01','0'),(17,'17','save.user.info','D','保存用户信息',1,NULL,'Y','http://user-service/userApi/service',NULL,60,3,'8000418003','2018-11-18 05:44:01','0'),(18,'18','save.store.info','D','保存商户信息',1,NULL,'N','http://store-service/storeApi/service',NULL,60,3,'8000418003','2018-11-18 05:44:01','0'),(19,'19','update.store.info','D','修改商户信息',1,NULL,'N','http://store-service/storeApi/service',NULL,60,3,'8000418003','2018-11-18 05:44:01','0'),(20,'20','delete.store.info','D','删除商户信息',1,NULL,'N','http://store-service/storeApi/service',NULL,60,3,'8000418003','2018-11-18 05:44:01','0'),(21,'21','transfer.console.menu','T','透传菜单查询',1,NULL,'N','http://192.168.31.199:8001/userApi/service',NULL,60,3,'8000418001','2018-11-18 05:44:01','0'),(22,'22','save.shop.info','D','保存商品信息',1,NULL,'N','http://shop-service/shopApi/service',NULL,60,3,'8000418003','2018-11-18 05:44:01','0'),(23,'23','update.shop.info','D','修改商品信息',1,NULL,'N','http://shop-service/shopApi/service',NULL,60,3,'8000418003','2018-11-18 05:44:01','0'),(24,'24','delete.shop.info','D','删除商品信息',1,NULL,'N','http://shop-service/shopApi/service',NULL,60,3,'8000418003','2018-11-18 05:44:01','0'),(25,'25','buy.shop.info','D','购买商品信息',1,NULL,'N','http://shop-service/shopApi/service',NULL,60,3,'8000418003','2018-11-18 05:44:01','0'),(26,'26','save.shop.catalog','D','保存商品目录信息',1,NULL,'N','http://shop-service/shopApi/service',NULL,60,3,'8000418003','2018-11-18 05:44:01','0'),(27,'27','update.shop.catalog','D','修改商品目录信息',1,NULL,'N','http://shop-service/shopApi/service',NULL,60,3,'8000418003','2018-11-18 05:44:01','0'),(28,'28','delete.shop.catalog','D','删除商品目录信息',1,NULL,'N','http://shop-service/shopApi/service',NULL,60,3,'8000418003','2018-11-18 05:44:01','0'),(29,'29','save.comment.info','D','保存评论',1,NULL,'N','http://comment-service/commentApi/service',NULL,60,3,'8000418003','2018-11-18 05:44:01','0'),(30,'30','delete.comment.info','D','删除评论',1,NULL,'N','http://comment-service/commentApi/service',NULL,60,3,'8000418003','2018-11-18 05:44:01','0'),(31,'31','member.joined.store','D','商户成员加入',1,NULL,'N','http://store-service/storeApi/service',NULL,60,3,'8000418003','2018-11-18 05:44:01','0'),(32,'32','member.quit.store','D','商户成员退出',1,NULL,'N','http://store-service/storeApi/service',NULL,60,3,'8000418003','2018-11-18 05:44:02','0'),(33,'33','save.storefee.info','D','保存用户费用信息',1,NULL,'N','http://store-service/storeApi/service',NULL,60,3,'8000418001','2018-11-18 05:45:46','0'),(34,'34','save.storehouse.info','D','保存商户录入用户应缴费用信息',1,NULL,'N','http://store-service/storeApi/service',NULL,60,3,'8000418001','2018-11-18 05:45:49','0'),(36,'35','do.service.order','API','订单服务受理',1,NULL,'Y','http://order-service/orderApi/service',NULL,60,3,'8000418003','2018-11-18 05:47:11','0'),(37,'36','user.service.register','API','用户注册',1,NULL,'N','http://order-service/orderApi/service','POST',60,3,'8000418002','2018-11-23 12:59:53','0'),(38,'37','user.service.login','API','用户登录',1,NULL,'N',NULL,'',60,3,'8000418002','2018-11-27 16:20:40','0'),(39,'38','check.service.login','API','检查用户登录服务',1,NULL,'N',NULL,NULL,60,3,'8000418002','2018-11-27 16:21:33','0'),(40,'39','user.service.logout','API','用户退出登录',1,NULL,'N',NULL,NULL,60,3,'8000418002','2018-11-27 16:21:33','0'),(41,'40','user.staff.add','API','添加员工',1,NULL,'N','http://order-service/orderApi/service','POST',60,3,'8000418002','2018-12-08 15:46:04','0'),(42,'41','user.staff.disable','API','停用员工',1,NULL,'N','http://order-service/orderApi/service','POST',60,3,'8000418002','2018-12-08 15:47:20','0'),(43,'42','user.staff.enable','API','启用员工',1,NULL,'N','http://order-service/orderApi/service','POST',60,3,'8000418002','2018-12-08 15:48:27','0'),(44,'43','modify.user.info','D','修改用户信息',1,NULL,'Y','http://user-service/userApi/service','POST',60,3,'8000418002','2018-12-08 15:49:54','0'),(45,'44','remove.user.info','D','删除用户信息',1,NULL,'Y','http://user-service/userApi/service','POST',60,3,'8000418002','2018-12-08 15:50:51','0'),(46,'45','recover.user.info','D','恢复用户信息',1,NULL,'Y','http://user-service/userApi/service','POST',60,3,'8000418002','2018-12-08 15:51:31','0'),(47,'46','query.staff.infos','API','查询员工信息',1,NULL,'NT','http://user-service/businessApi/query','GET',60,3,'8000418002','2018-12-09 07:34:24','0'),(48,'47','search.staff.infos','API','搜索员工信息',1,NULL,'NT','http://user-service/businessApi/query','GET',60,3,'8000418002','2018-12-12 10:18:20','0'),(49,'48','query.community.infos','API','查询小区信息',1,NULL,'NT','http://store-service/businessApi/query','GET',60,3,'8000418002','2018-12-12 14:09:43','0'),(50,'49','search.community.infos','API','搜索小区',1,NULL,'NT','http://store-service/businessApi/query','GET',60,3,'8000418002','2018-12-12 14:53:44','0'),(51,'50','save.community.info','D','保存小区信息',1,NULL,'Y','http://community-service/communityApi/service','POST',60,3,'8000418002','2018-12-17 15:25:13','0'),(52,'51','update.community.info','D','修改小区信息',1,NULL,'Y','http://community-service/communityApi/service','POST',60,3,'8000418002','2018-12-17 15:26:15','0'),(53,'52','member.join.community','D','小区成员加入',1,NULL,'Y','http://community-service/communityApi/service','POST',60,3,'8000418002','2018-12-17 15:27:01','0'),(54,'53','member.quit.community','D','小区成员退出',1,NULL,'Y','http://community-service/communityApi/service','POST',60,3,'8000418002','2018-12-17 15:27:46','0'),(56,'56','check.property.staffHasProperty','API','检查员工是否有物业信息',1,'','N','http://property-service/businessApi/query','GET',60,3,'8000418002','0000-00-00 00:00:00','0');
/*!40000 ALTER TABLE `c_service` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `c_service_business`
--

DROP TABLE IF EXISTS `c_service_business`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `c_service_business` (
  `service_business_id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `business_type_cd` varchar(12) DEFAULT NULL,
  `invoke_type` varchar(4) NOT NULL COMMENT '调用类型，1 http-post(微服务内应用) 2 webservice 3 http-post(微服务之外应用)',
  `url` varchar(200) DEFAULT NULL COMMENT '目标地址',
  `message_topic` varchar(50) DEFAULT NULL COMMENT '异步时的消息topic名称',
  `timeout` int(11) NOT NULL DEFAULT '60' COMMENT '超时时间',
  `retry_count` int(11) NOT NULL DEFAULT '3' COMMENT '重试次数',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `status_cd` varchar(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考c_status表，0在用，1失效',
  PRIMARY KEY (`service_business_id`),
  UNIQUE KEY `business_type_cd` (`business_type_cd`)
) ENGINE=InnoDB AUTO_INCREMENT=53 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `c_service_business`
--

LOCK TABLES `c_service_business` WRITE;
/*!40000 ALTER TABLE `c_service_business` DISABLE KEYS */;
INSERT INTO `c_service_business` VALUES (1,'100100030001','1','ORDER_USER_SERVICE_URL','userServiceTopic',60,3,'2019-02-07 17:29:21','0'),(2,'100100030002','1','ORDER_USER_SERVICE_URL','userServiceTopic',60,3,'2019-02-07 17:29:21','0'),(3,'100100030003','1','ORDER_USER_SERVICE_URL','userServiceTopic',60,3,'2019-02-07 17:29:21','0'),(4,'100100030004','1','ORDER_USER_SERVICE_URL','userServiceTopic',60,3,'2019-02-07 17:29:21','0'),(5,'100100040001','1','ORDER_USER_SERVICE_URL','userServiceTopic',60,3,'2019-02-07 17:29:21','0'),(6,'100100040002','1','ORDER_USER_SERVICE_URL','userServiceTopic',60,3,'2019-02-07 17:29:21','0'),(7,'100100040003','1','ORDER_USER_SERVICE_URL','userServiceTopic',60,3,'2019-02-07 17:29:21','0'),(8,'200100030001','1','ORDER_STORE_SERVICE_URL','storeServiceTopic',60,3,'2019-02-07 17:29:21','0'),(9,'200100030002','1','ORDER_STORE_SERVICE_URL','storeServiceTopic',60,3,'2019-02-07 17:29:22','0'),(10,'200100040001','1','ORDER_STORE_SERVICE_URL','storeServiceTopic',60,3,'2019-02-07 17:29:22','0'),(11,'200100040002','1','ORDER_STORE_SERVICE_URL','storeServiceTopic',60,3,'2019-02-07 17:29:22','0'),(12,'200100050001','1','ORDER_STORE_SERVICE_URL','storeServiceTopic',60,3,'2019-02-07 17:29:22','0'),(13,'300100030001','1','ORDER_SHOP_SERVICE_URL','shopServiceTopic',60,3,'2019-02-07 17:29:22','0'),(14,'300100030002','1','ORDER_SHOP_SERVICE_URL','shopServiceTopic',60,3,'2019-02-07 17:29:22','0'),(15,'300100030003','1','ORDER_SHOP_SERVICE_URL','shopServiceTopic',60,3,'2019-02-07 17:29:22','0'),(16,'300100040001','1','ORDER_SHOP_SERVICE_URL','shopServiceTopic',60,3,'2019-02-07 17:29:22','0'),(17,'300100040002','1','ORDER_SHOP_SERVICE_URL','shopServiceTopic',60,3,'2019-02-07 17:29:22','0'),(18,'300100050001','1','ORDER_SHOP_SERVICE_URL','shopServiceTopic',60,3,'2019-02-07 17:29:22','0'),(19,'300100050002','1','ORDER_SHOP_SERVICE_URL','shopServiceTopic',60,3,'2019-02-07 17:29:22','0'),(20,'400100030001','1','ORDER_COMMENT_SERVICE_URL','commentServiceTopic',60,3,'2019-02-07 17:29:22','0'),(21,'400100050001','1','ORDER_COMMENT_SERVICE_URL','commentServiceTopic',60,3,'2019-02-07 17:29:22','0'),(22,'500100030001','1','ORDER_COMMUNITY_SERVICE_URL','communityServiceTopic',60,3,'2019-02-07 17:29:22','0'),(23,'500100030002','1','ORDER_COMMUNITY_SERVICE_URL','communityServiceTopic',60,3,'2019-02-07 17:29:22','0'),(24,'500100040001','1','ORDER_COMMUNITY_SERVICE_URL','communityServiceTopic',60,3,'2019-02-07 17:29:22','0'),(25,'500100040002','1','ORDER_COMMUNITY_SERVICE_URL','communityServiceTopic',60,3,'2019-02-07 17:29:23','0'),(26,'500100050001','1','ORDER_COMMUNITY_SERVICE_URL','communityServiceTopic',60,3,'2019-02-07 17:29:23','0'),(27,'600100030002','1','ORDER_PROPERTY_SERVICE_URL','propertyServiceTopic',60,3,'2019-02-07 17:29:23','0'),(28,'600100030003','1','ORDER_PROPERTY_SERVICE_URL','propertyServiceTopic',60,3,'2019-02-07 17:29:23','0'),(29,'600100030004','1','ORDER_PROPERTY_SERVICE_URL','propertyServiceTopic',60,3,'2019-02-07 17:29:23','0'),(30,'600100030005','1','ORDER_PROPERTY_SERVICE_URL','propertyServiceTopic',60,3,'2019-02-07 17:29:23','0'),(31,'600100030006','1','ORDER_PROPERTY_SERVICE_URL','propertyServiceTopic',60,3,'2019-02-07 17:29:23','0'),(32,'600100040001','1','ORDER_PROPERTY_SERVICE_URL','propertyServiceTopic',60,3,'2019-02-07 17:29:23','0'),(33,'600100040002','1','ORDER_PROPERTY_SERVICE_URL','propertyServiceTopic',60,3,'2019-02-07 17:29:23','0'),(34,'600100040003','1','ORDER_PROPERTY_SERVICE_URL','propertyServiceTopic',60,3,'2019-02-07 17:29:23','0'),(35,'600100040004','1','ORDER_PROPERTY_SERVICE_URL','propertyServiceTopic',60,3,'2019-02-07 17:29:23','0'),(36,'600100040005','1','ORDER_PROPERTY_SERVICE_URL','propertyServiceTopic',60,3,'2019-02-07 17:29:23','0'),(37,'600100050001','1','ORDER_PROPERTY_SERVICE_URL','propertyServiceTopic',60,3,'2019-02-07 17:29:23','0'),(38,'600100050002','1','ORDER_PROPERTY_SERVICE_URL','propertyServiceTopic',60,3,'2019-02-07 17:29:23','0'),(39,'600100050003','1','ORDER_PROPERTY_SERVICE_URL','propertyServiceTopic',60,3,'2019-02-07 17:29:24','0'),(40,'600100050004','1','ORDER_PROPERTY_SERVICE_URL','propertyServiceTopic',60,3,'2019-02-07 17:29:24','0'),(41,'600100050005','1','ORDER_PROPERTY_SERVICE_URL','propertyServiceTopic',60,3,'2019-02-07 17:29:24','0'),(42,'700100030001','1','ORDER_AGENT_SERVICE_URL','agentServiceTopic',60,3,'2019-02-07 17:29:24','0'),(43,'700100030002','1','ORDER_AGENT_SERVICE_URL','agentServiceTopic',60,3,'2019-02-07 17:29:24','0'),(44,'700100030003','1','ORDER_AGENT_SERVICE_URL','agentServiceTopic',60,3,'2019-02-07 17:29:24','0'),(45,'700100030004','1','ORDER_AGENT_SERVICE_URL','agentServiceTopic',60,3,'2019-02-07 17:29:24','0'),(46,'700100040001','1','ORDER_AGENT_SERVICE_URL','agentServiceTopic',60,3,'2019-02-07 17:29:24','0'),(47,'700100040002','1','ORDER_AGENT_SERVICE_URL','agentServiceTopic',60,3,'2019-02-07 17:29:24','0'),(48,'700100040003','1','ORDER_AGENT_SERVICE_URL','agentServiceTopic',60,3,'2019-02-07 17:29:24','0'),(49,'700100050001','1','ORDER_AGENT_SERVICE_URL','agentServiceTopic',60,3,'2019-02-07 17:29:24','0'),(50,'700100050002','1','ORDER_AGENT_SERVICE_URL','agentServiceTopic',60,3,'2019-02-07 17:29:24','0'),(51,'700100050003','1','ORDER_AGENT_SERVICE_URL','agentServiceTopic',60,3,'2019-02-07 17:29:24','0'),(52,'700100050004','1','ORDER_AGENT_SERVICE_URL','agentServiceTopic',60,3,'2019-02-07 17:29:24','0');
/*!40000 ALTER TABLE `c_service_business` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `c_service_sql`
--

DROP TABLE IF EXISTS `c_service_sql`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `c_service_sql`
--

LOCK TABLES `c_service_sql` WRITE;
/*!40000 ALTER TABLE `c_service_sql` DISABLE KEYS */;
INSERT INTO `c_service_sql` VALUES (1,'query.order.orderInfo','订单信息','oId','1','{\n                                                 	\"param1\":\"SELECT co.app_id appId,co.create_time createTime,co.ext_transaction_id extTransactionId,co.finish_time finishTime\n                                                 ,co.order_type_cd orderTypeCd,co.o_id oId ,co.remark remark ,co.request_time requestTime ,co.user_id userId,co.status_cd statusCd\n                                                  FROM c_orders co WHERE co.o_id = #oId#\",\n                                                  \"param2\":\"SELECT cb.b_id bId, cb.business_type_cd businessTypeCd,cb.create_time createTime,cb.finish_time finishTime ,cb.o_id oId,\n                                                 cb.remark remark,cb.status_cd statusCd FROM c_business cb WHERE cb.o_id = #oId#\"\n                                                 }','',NULL,'{\"PARAM:\"{\n                                                            \"param1\": \"$.#order#Object\",\n                                                            \"param2\": \"$.#business#Array\"\n                                                            },\"TEMPLATE\":\"{\n                                                         \"response\": {\n                                                           \"code\": \"0000\",\n                                                           \"message\": \"成功\"\n                                                         }\n                                                       }\"}','','2018-11-14 13:28:45','0'),(2,'query.console.menu','查询菜单','manageId,menuGroup','1','{\n                                                 	\"param1\":\"select mm.m_id mId,mm.name name,mm.level level,mm.parent_id parentId,mm.menu_group menuGroup,mm.user_id userId,mm.create_time createTime,\n                                                              mm.remark remark,mmc.url url,mmc.template template\n                                                              from m_menu_2_user mm2u,m_menu mm, m_menu_ctg mmc\n                                                              where mm2u.user_id = #manageId#\n                                                              and mm2u.m_id = mm.m_id\n                                                              AND mm.menu_group = #menuGroup#\n                                                              and mm2u.status_cd = \'0\'\n                                                              and mm.status_cd = \'0\'\n                                                              and mmc.m_id = mm.m_id\n                                                              and mmc.status_cd = \'0\'\n                                                              order by mm.seq asc\"\n                                                 }','',NULL,'{\"PARAM\":{\n                                                            \"param1\": \"$.#menus#Array\"\n                                                            },\"TEMPLATE\":{\n                                                         \"response\": {\n                                                           \"code\": \"0000\",\n                                                           \"message\": \"成功\"\n                                                         }\n                                                       }}','','2018-11-14 13:28:45','0'),(3,'query.user.loginInfo','查询用户登录信息','userCode','1','{\"param1\":\"select a.user_id userId,a.name userName,a.password userPwd from u_user a where a.name = #userCode# and a.status_cd = \'0\'\"}','',NULL,'{\"PARAM\":{\n                                                            \"param1\": \"$.#user#Object\"\n                                                            },\"TEMPLATE\":{\n                                                         \"response\": {\n                                                           \"code\": \"0000\",\n                                                           \"message\": \"成功\"\n                                                         }\n                                                       }}','','2018-11-14 13:28:45','0'),(4,'query.console.template','查询模板信息','templateCode','1','{\n                                                 	\"param1\":\"SELECT t.template_code templateCode,t.name templateName,t.`html_name` htmlName,t.`url` templateUrl\n                                                              FROM c_template t WHERE t.status_cd = \'0\' AND t.template_code = #templateCode#\"\n                                                 }','',NULL,'{\"PARAM\":{\n                                                            \"param1\": \"$.#template#Object\"\n                                                            },\"TEMPLATE\":{\n                                                         \"response\": {\n                                                           \"code\": \"0000\",\n                                                           \"message\": \"成功\"\n                                                         }\n                                                       }}','','2018-11-14 13:28:45','0'),(5,'query.console.templateCol','查询模板列信息','templateCode','1','{\n                                                 	\"param1\":\"SELECT t.template_code templateCode,t.name templateName,tc.col_name colName,tc.col_model colModel FROM c_template t,c_template_col tc WHERE t.status_cd = \'0\' AND t.template_code = tc.template_code\n                                                              AND tc.status_cd = \'0\'\n                                                              AND t.template_code = #templateCode#\"\n                                                 }','',NULL,'{\"PARAM\":{\n                                                            \"param1\": \"$.#template#Array\"\n                                                            },\"TEMPLATE\":{\n                                                         \"response\": {\n                                                           \"code\": \"0000\",\n                                                           \"message\": \"成功\"\n                                                         }\n                                                       }}','','2018-11-14 13:28:45','0'),(6,'query.center.mapping','查询映射数据','page,rows,sord','1','{\"param1\":\"select count(1) records,ceil(#page#/#rows#)+1 page,ceil(count(1)/#rows#) total from c_mapping m where m.status_cd = \'0\'\",\n                                                             \"param2\":\"SELECT m.`id` id,m.`domain` domain,m.name name,m.`key`  `key` ,m.`value` `value`,m.`remark` remark FROM c_mapping m WHERE m.`status_cd` = \'0\' LIMIT #page#, #rows#\"\n                                                             }','',NULL,'{\n                                                        	\"PARAM\": {\n                                                        		\"param1\": \"$.##Object\",\n                                                        		\"param2\": \"$.#rows#Array\"\n                                                        	},\n                                                        	\"TEMPLATE\": {\n                                                        		\"response\": {\n                                                        			\"code\": \"0000\",\n                                                        			\"message\": \"成功\"\n                                                        		}\n                                                        	}\n                                                        }','','2018-11-14 13:28:45','0'),(7,'query.center.apps','查询外部应用','page,rows,sord','1','{\"param1\":\"select count(1) records,ceil(#page#/#rows#)+1 page,ceil(count(1)/#rows#) total from c_app a where a.status_cd = \'0\'\",\n                                                             \"param2\":\"SELECT m.`id` id,m.`app_id` appId,m.name `name`,m.`security_code`  securityCode ,m.`while_list_ip` whileListIp,m.`black_list_ip` blackListIp,m.`remark` remark FROM c_app m WHERE m.`status_cd` = \'0\' LIMIT #page#, #rows#\"\n                                                             }','',NULL,'{\n                                                        	\"PARAM\": {\n                                                        		\"param1\": \"$.##Object\",\n                                                        		\"param2\": \"$.#rows#Array\"\n                                                        	},\n                                                        	\"TEMPLATE\": {\n                                                        		\"response\": {\n                                                        			\"code\": \"0000\",\n                                                        			\"message\": \"成功\"\n                                                        		}\n                                                        	}\n                                                        }','','2018-11-14 13:28:45','0'),(8,'query.center.services','查询服务数据','page,rows,sord','1','{\"param1\":\"select count(1) records,ceil(#page#/#rows#)+1 page,ceil(count(1)/#rows#) total from c_service a where a.status_cd = \'0\'\",\n                                                             \"param2\":\"SELECT s.`service_id` serviceId,s.`service_code` serviceCode,s.`business_type_cd`  businessTypeCd,s.name `name`,s.is_instance isInstance,\n                                                                       s.`messageQueueName` messageQueueName,s.url url,s.`provide_app_id` provideAppId FROM c_service s WHERE s.`status_cd` = \'0\' LIMIT #page#, #rows#\"\n                                                             }','',NULL,'{\n                                                        	\"PARAM\": {\n                                                        		\"param1\": \"$.##Object\",\n                                                        		\"param2\": \"$.#rows#Array\"\n                                                        	},\n                                                        	\"TEMPLATE\": {\n                                                        		\"response\": {\n                                                        			\"code\": \"0000\",\n                                                        			\"message\": \"成功\"\n                                                        		}\n                                                        	}\n                                                        }','','2018-11-14 13:28:45','0'),(9,'query.center.route','查询路由数据','page,rows,sord','1','{\"param1\":\"select count(1) records,ceil(#page#/#rows#)+1 page,ceil(count(1)/#rows#) total from c_route a,c_service cs WHERE a.`service_id` = cs.`service_id` AND cs.`status_cd` = \'0\' and a.status_cd = \'0\'\",\n                                                             \"param2\":\"SELECT s.id id,s.`app_id` appId,s.`service_id` serviceId,s.`invoke_model` invokeModel,cs.`name` serviceName,cs.`service_code` serviceCode,s.`order_type_cd` orderTypeCd,s.`invoke_limit_times` invokelimitTimes FROM c_route s,c_service cs WHERE s.`service_id` = cs.`service_id` AND cs.`status_cd` = \'0\' AND s.`status_cd` = \'0\' LIMIT #page#, #rows#\"\n                                                             }','',NULL,'{\n                                                        	\"PARAM\": {\n                                                        		\"param1\": \"$.##Object\",\n                                                        		\"param2\": \"$.#rows#Array\"\n                                                        	},\n                                                        	\"TEMPLATE\": {\n                                                        		\"response\": {\n                                                        			\"code\": \"0000\",\n                                                        			\"message\": \"成功\"\n                                                        		}\n                                                        	}\n                                                        }','','2018-11-14 13:28:45','0'),(10,'query.console.caches','查询缓存数据','userId','1','{\n                                                             \"param1\":\"SELECT c.id id,c.`cache_code` cacheCode, c.`name` cacheName FROM c_cache c, c_cache_2_user c2u WHERE c.`cache_code` = c2u.`cache_code` AND c.`status_cd` = \'0\'\n                                                                       AND c2u.`status_cd` = \'0\' AND c2u.`user_id` = #userId# AND c.`group` = \'COMMON\' ORDER BY c.`seq` ASC\"\n                                                             }','',NULL,'{\n                                                        	\"PARAM\": {\n                                                        		\"param1\": \"$.#rows#Array\"\n                                                        	},\n                                                        	\"TEMPLATE\": {\n                                                        		\"response\": {\n                                                        			\"code\": \"0000\",\n                                                        			\"message\": \"成功\"\n                                                        		}\n                                                        	}\n                                                        }','','2018-11-14 13:28:45','0'),(11,'query.console.cache','查询单条缓存信息','cacheCode','1','{\n                                                             \"param1\":\"SELECT c.id id,c.`cache_code` cacheCode, c.`name` cacheName,c.`param` param,c.`service_code` serviceCode FROM c_cache c WHERE  c.`status_cd` = \'0\' AND c.`cache_code` = #cacheCode#\"\n                                                             }','',NULL,'{\n                                                        	\"PARAM\": {\n                                                        		\"param1\": \"$.#cache#Object\"\n                                                        	},\n                                                        	\"TEMPLATE\": {\n                                                        		\"response\": {\n                                                        			\"code\": \"0000\",\n                                                        			\"message\": \"成功\"\n                                                        		}\n                                                        	}\n                                                        }','','2018-11-14 13:28:45','0'),(12,'save.center.mapping','保存映射信息','domain,name,key,value,remark','1','{\n                                                             \"param1\":\"INSERT c_mapping(domain,`name`,`key`,`value`,remark) VALUES(#domain#,#name#,#key#,#value#,#remark#)\"\n                                                             }','',NULL,'{\n                                                        	\"TEMPLATE\": {\n                                                        		\"response\": {\n                                                        			\"code\": \"0000\",\n                                                        			\"message\": \"成功\"\n                                                        		}\n                                                        	}\n                                                        }','','2018-11-14 13:28:45','0'),(13,'delete.center.mapping','删除映射信息','id','1','{\n                                                             \"param1\":\"UPDATE c_mapping m SET m.status_cd = \'1\' WHERE m.status_cd = \'0\' AND m.id = #id#\"\n                                                             }','',NULL,'{\n                                                        	\"TEMPLATE\": {\n                                                        		\"response\": {\n                                                        			\"code\": \"0000\",\n                                                        			\"message\": \"成功\"\n                                                        		}\n                                                        	}\n                                                        }','','2018-11-14 13:28:45','0'),(14,'update.center.mapping','修改映射信息','id,domain,name,key,value,remark','1','{\n                                                             \"param1\":\"UPDATE c_mapping m SET m.domain=#domain#,m.name = #name#,m.key=#key#,m.value=#value#,m.remark=#remark# WHERE m.status_cd = \'0\' AND m.id = #id#\"\n                                                             }','',NULL,'{\n                                                        	\"TEMPLATE\": {\n                                                        		\"response\": {\n                                                        			\"code\": \"0000\",\n                                                        			\"message\": \"成功\"\n                                                        		}\n                                                        	}\n                                                        }','','2018-11-14 13:28:45','0'),(15,'query.staff.infos','查询员工信息','page,rows','1','{\"param1\":\"select count(1) total,ceil(#page#/#rows#)+1 page,ceil(count(1)/#rows#) records  from u_user a where a.level_cd = \'01\'\",\n\"param2\":\"select a.user_id userId,a.name,a.email,a.address,a.sex,a.status_cd statusCd,a.tel,a.create_time createTime from u_user a where a.level_cd = \'01\' LIMIT #page#, #rows#\"}',NULL,NULL,'{\n                                                        	\"PARAM\": {\n                                                        		\"param1\": \"$.##Object\",\n                                                        		\"param2\": \"$.#rows#Array\"\n                                                        	},\n                                                        	\"TEMPLATE\": {}\n                                                        }',NULL,'2018-12-09 07:50:18','0'),(16,'search.staff.infos','搜索员工信息','page,rows,search','1','{\"param1\":\"select count(1) total,ceil(#page#/#rows#)+1 page,ceil(count(1)/#rows#) records  from u_user a where a.level_cd = \'01\' and a.name = #search#\",\n\"param2\":\"select a.user_id userId,a.name,a.email,a.address,a.sex,a.status_cd statusCd,a.tel,a.create_time createTime from u_user a where a.level_cd = \'01\' and a.name = #search# LIMIT #page#, #rows#\"}',NULL,NULL,'{\n                                                        	\"PARAM\": {\n                                                        		\"param1\": \"$.##Object\",\n                                                        		\"param2\": \"$.#rows#Array\"\n                                                        	},\n                                                        	\"TEMPLATE\": {}\n                                                        }',NULL,'2018-12-12 10:21:55','0'),(17,'query.community.infos','查询小区信息','page,rows','1','{\"param1\":\"select count(1) total,ceil(#page#/#rows#)+1 page,ceil(count(1)/#rows#) records \nfrom s_store a,s_member_store b,s_store c where a.store_type_cd = \'800900000000\' \nand a.store_id = b.store_id and b.member_id = c.store_id and c.store_type_cd = \'800900000002\'\nand a.status_cd = \'0\' and b.status_cd = \'0\' and c.status_cd = \'0\'\",\n\"param2\":\"select a.store_id storeId,a.name storeName,a.nearby_landmarks nearByLandmarks,a.address address,c.name agentName,c.tel agentTel,a.create_time createTime\nfrom s_store a,s_member_store b,s_store c where a.store_type_cd = \'800900000000\' \nand a.store_id = b.store_id and b.member_id = c.store_id and c.store_type_cd = \'800900000002\'\nand a.status_cd = \'0\' and b.status_cd = \'0\' and c.status_cd = \'0\' LIMIT #page#, #rows#\"}',NULL,NULL,'{\n                                                        	\"PARAM\": {\n                                                        		\"param1\": \"$.##Object\",\n                                                        		\"param2\": \"$.#rows#Array\"\n                                                        	},\n                                                        	\"TEMPLATE\": {}\n                                                        }',NULL,'2018-12-12 14:22:05','0'),(18,'search.community.infos','搜索小区','page,rows,search','1','{\"param1\":\"select count(1) total,ceil(#page#/#rows#)+1 page,ceil(count(1)/#rows#) records \nfrom s_store a,s_member_store b,s_store c where a.store_type_cd = \'800900000000\' \nand a.store_id = b.store_id and b.member_id = c.store_id and c.store_type_cd = \'800900000002\'\nand a.status_cd = \'0\' and b.status_cd = \'0\' and c.status_cd = \'0\' and a.name = #search#\",\n\"param2\":\"select a.store_id storeId,a.name storeName,a.nearby_landmarks nearByLandmarks,a.address address,c.name agentName,c.tel agentTel,a.create_time createTime\nfrom s_store a,s_member_store b,s_store c where a.store_type_cd = \'800900000000\' \nand a.store_id = b.store_id and b.member_id = c.store_id and c.store_type_cd = \'800900000002\'\nand a.status_cd = \'0\' and b.status_cd = \'0\' and c.status_cd = \'0\' and a.name = #search# LIMIT #page#, #rows#\"}',NULL,NULL,'{\n                                                        	\"PARAM\": {\n                                                        		\"param1\": \"$.##Object\",\n                                                        		\"param2\": \"$.#rows#Array\"\n                                                        	},\n                                                        	\"TEMPLATE\": {}\n                                                        }',NULL,'2018-12-12 14:56:10','0');
/*!40000 ALTER TABLE `c_service_sql` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `c_status`
--

DROP TABLE IF EXISTS `c_status`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `c_status` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `status_cd` varchar(4) NOT NULL COMMENT '状态',
  `name` varchar(50) NOT NULL COMMENT '名称',
  `description` varchar(200) DEFAULT NULL COMMENT '描述',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `status_cd` (`status_cd`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `c_status`
--

LOCK TABLES `c_status` WRITE;
/*!40000 ALTER TABLE `c_status` DISABLE KEYS */;
INSERT INTO `c_status` VALUES (1,'1','无效的，不在用的','无效的，不在用的','2018-11-14 13:28:44'),(2,'0','有效的，在用的','有效的，在用的','2018-11-14 13:28:44'),(3,'S','保存成功','保存成功','2018-11-14 13:28:44'),(4,'D','作废订单','作废订单','2018-11-14 13:28:44'),(5,'E','错误订单','错误订单','2018-11-14 13:28:44'),(6,'NE','通知错误订单','通知错误订单','2018-11-14 13:28:44'),(7,'C','订单完成','订单完成','2018-11-14 13:28:44'),(8,'B','Business过程','Business过程','2018-11-14 13:28:44'),(9,'I','Instance过程','Instance过程','2018-11-14 13:28:44');
/*!40000 ALTER TABLE `c_status` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `c_sub_comment`
--

DROP TABLE IF EXISTS `c_sub_comment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `c_sub_comment`
--

LOCK TABLES `c_sub_comment` WRITE;
/*!40000 ALTER TABLE `c_sub_comment` DISABLE KEYS */;
/*!40000 ALTER TABLE `c_sub_comment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `c_sub_comment_attr`
--

DROP TABLE IF EXISTS `c_sub_comment_attr`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `c_sub_comment_attr`
--

LOCK TABLES `c_sub_comment_attr` WRITE;
/*!40000 ALTER TABLE `c_sub_comment_attr` DISABLE KEYS */;
/*!40000 ALTER TABLE `c_sub_comment_attr` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `c_sub_comment_photo`
--

DROP TABLE IF EXISTS `c_sub_comment_photo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `c_sub_comment_photo`
--

LOCK TABLES `c_sub_comment_photo` WRITE;
/*!40000 ALTER TABLE `c_sub_comment_photo` DISABLE KEYS */;
/*!40000 ALTER TABLE `c_sub_comment_photo` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `c_template`
--

DROP TABLE IF EXISTS `c_template`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `c_template`
--

LOCK TABLES `c_template` WRITE;
/*!40000 ALTER TABLE `c_template` DISABLE KEYS */;
INSERT INTO `c_template` VALUES (1,'mapping','映射管理','list_template','LIST->query.center.mapping;QUERY->mapping_query_url;INSERT->save.center.mapping;UPDATE->update.center.mapping;DELETE->delete.center.mapping','2018-11-14 13:28:51','0'),(2,'app','外部应用','list_template','LIST->query.center.apps;QUERY->query.center.app','2018-11-14 13:28:51','0'),(3,'service','服务管理','list_template','LIST->query.center.services;QUERY->query.center.service','2018-11-14 13:28:51','0'),(4,'route','路由管理','list_template','LIST->query.center.routes;QUERY->query.center.route','2018-11-14 13:28:51','0'),(5,'cache','刷新缓存','list_template_cache','LIST->query.center.caches;QUERY->query.center.cacheOne','2018-11-14 13:28:51','0');
/*!40000 ALTER TABLE `c_template` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `c_template_col`
--

DROP TABLE IF EXISTS `c_template_col`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `c_template_col`
--

LOCK TABLES `c_template_col` WRITE;
/*!40000 ALTER TABLE `c_template_col` DISABLE KEYS */;
INSERT INTO `c_template_col` VALUES (1,'mapping','列ID','id','{ \"name\": \"id\",\"index\": \"id\",\"width\": \"90\",\"editable\": true,\"sorttype\": \"int\" }',1,'2018-11-14 13:28:51','0'),(2,'mapping','域','domain','{ \"name\": \"domain\",\"index\": \"domain\",\"width\": \"90\",\n                                                                                                             \"editable\": true,\n                                                                                                             \"formatoptions\": { \"defaultValue\": \"DOMAIN.COMMON\" }\n                                                                                                           }',2,'2018-11-14 13:28:51','0'),(3,'mapping','名称','name','{ \"name\": \"name\",\"index\": \"name\",\"width\": \"90\",\n                                                                                                             \"editable\": true }',3,'2018-11-14 13:28:51','0'),(4,'mapping','键','key','{ \"name\": \"key\",\"index\": \"key\",\"width\": \"90\",\n                                                                                                             \"editable\": true }',4,'2018-11-14 13:28:51','0'),(5,'mapping','值','value','{ \"name\": \"value\",\"index\": \"value\",\"width\": \"90\",\n                                                                                                             \"editable\": true }',5,'2018-11-14 13:28:51','0'),(6,'mapping','备注','value','{ \"name\": \"remark\",\"index\": \"remark\",\"width\": \"90\",\n                                                                                                             \"editable\": true }',6,'2018-11-14 13:28:51','0'),(7,'mapping','BUTTON','BUTTON','{\r\n 	\"name\": \"detail\",\r\n 	\"index\": \"\",\r\n 	\"width\": \"40\",\r\n 	\"fixed\": true,\r\n 	\"sortable\": \"false\",\r\n 	\"resize\": \"false\",\r\n 	\"formatter\": \"function(cellvalue, options, rowObject){ var temp =\\\"<div style=\'margin-left:8px;\'><div title=\'详情记录\' style=\'float:left;cursor:pointer;\' class=\'ui-pg-div\' id=\'jEditButton_3\' onclick=\'detail(\\\" + rowObject + \\\")\' onmouseover=\'jQuery(this).addClass(\'ui-state-hover\');\' onmouseout=\'jQuery(this).removeClass(\'ui-state-hover\');\'><span class=\'ui-icon fa-search-plus\'/></div></div>\\\";return temp; }\"\r\n }',7,'2018-11-14 13:28:51','0'),(8,'app','列ID','id','{ \"name\": \"id\",\"index\": \"id\",\"width\": \"20\",\n                                                                                                             \"editable\": true,\n                                                                                                             \"sorttype\": \"int\" }',1,'2018-11-14 13:28:51','0'),(9,'app','AppId','domain','{ \"name\": \"appId\",\"index\": \"appId\",\"width\": \"40\",\n                                                                                                             \"editable\": true\n                                                                                                           }',2,'2018-11-14 13:28:51','0'),(10,'app','名称','name','{ \"name\": \"name\",\"index\": \"name\",\"width\": \"50\",\n                                                                                                             \"editable\": true }',3,'2018-11-14 13:28:51','0'),(11,'app','秘钥','securityCode','{ \"name\": \"securityCode\",\"index\": \"securityCode\",\"width\": \"50\",\n                                                                                                             \"editable\": true }',4,'2018-11-14 13:28:51','0'),(12,'app','白名单','whileListIp','{ \"name\": \"whileListIp\",\"index\": \"whileListIp\",\"width\": \"90\",\n                                                                                                             \"editable\": true }',5,'2018-11-14 13:28:51','0'),(13,'app','黑名单','blackListIp','{ \"name\": \"blackListIp\",\"index\": \"blackListIp\",\"width\": \"40\",\n                                                                                                             \"editable\": true }',6,'2018-11-14 13:28:51','0'),(14,'app','备注','value','{ \"name\": \"remark\",\"index\": \"remark\",\"width\": \"90\",\n                                                                                                             \"editable\": true }',7,'2018-11-14 13:28:51','0'),(15,'app','BUTTON','BUTTON','{\n                                                                                                            \"name\": \"detail\",\n                                                                                                            \"index\": \"\",\n                                                                                                            \"width\": \"40\",\n                                                                                                            \"fixed\": \"true\",\n                                                                                                            \"sortable\": \"false\",\n                                                                                                            \"resize\": \"false\",\n                                                                                                            \"formatter\": \"function(cellvalue, options, rowObject){\n var temp =\"<div style=\'margin-left:8px;\'><div title=\'详情记录\' style=\'float:left;cursor:pointer;\' class=\'ui-pg-div\' id=\'jEditButton_3\' onclick=\'detail(\"+rowObject+\")\' onmouseover=\'jQuery(this).addClass(\'ui-state-hover\');\' onmouseout=\'jQuery(this).removeClass(\'ui-state-hover\');\'><span class=\'ui-icon fa-search-plus\'/></div></div>\";\n return temp; \n}\"\n                                                                                                          }',8,'2018-11-14 13:28:51','0'),(16,'service','服务ID','serviceId','{ \"name\": \"serviceId\",\"index\": \"serviceId\",\"width\": \"20\",\n                                                                                                             \"editable\": true,\n                                                                                                             \"sorttype\": \"int\" }',1,'2018-11-14 13:28:51','0'),(17,'service','服务编码','serviceCode','{ \"name\": \"serviceCode\",\"index\": \"serviceCode\",\"width\": \"40\",\n                                                                                                             \"editable\": true\n                                                                                                           }',2,'2018-11-14 13:28:51','0'),(18,'service','业务类型','businessTypeCd','{ \"name\": \"businessTypeCd\",\"index\": \"businessTypeCd\",\"width\": \"50\",\n                                                                                                             \"editable\": true }',3,'2018-11-14 13:28:51','0'),(19,'service','名称','name','{ \"name\": \"name\",\"index\": \"name\",\"width\": \"40\",\n                                                                                                             \"editable\": true }',4,'2018-11-14 13:28:51','0'),(20,'service','消息队列','messageQueueName','{ \"name\": \"messageQueueName\",\"index\": \"messageQueueName\",\"width\": \"10\",\n                                                                                                             \"editable\": true }',5,'2018-11-14 13:28:51','0'),(21,'service','需要Instance','isInstance','{ \"name\": \"isInstance\",\"index\": \"isInstance\",\"width\": \"10\",\n                                                                                                             \"editable\": true }',6,'2018-11-14 13:28:51','0'),(22,'service','URL','url','{ \"name\": \"url\",\"index\": \"url\",\"width\": \"60\",\n                                                                                                             \"editable\": true }',7,'2018-11-14 13:28:51','0'),(23,'service','提供者AppId','provideAppId','{ \"name\": \"provideAppId\",\"index\": \"provideAppId\",\"width\": \"10\",\n                                                                                                             \"editable\": true }',8,'2018-11-14 13:28:51','0'),(24,'service','BUTTON','BUTTON','{\n                                                                                                            \"name\": \"detail\",\n                                                                                                            \"index\": \"\",\n                                                                                                            \"width\": \"40\",\n                                                                                                            \"fixed\": \"true\",\n                                                                                                            \"sortable\": \"false\",\n                                                                                                            \"resize\": \"false\",\n                                                                                                            \"formatter\": \"function(cellvalue, options, rowObject){\n var temp =\"<div style=\'margin-left:8px;\'><div title=\'详情记录\' style=\'float:left;cursor:pointer;\' class=\'ui-pg-div\' id=\'jEditButton_3\' onclick=\'detail(\"+rowObject+\")\' onmouseover=\'jQuery(this).addClass(\'ui-state-hover\');\' onmouseout=\'jQuery(this).removeClass(\'ui-state-hover\');\'><span class=\'ui-icon fa-search-plus\'/></div></div>\";\n return temp; \n}\"\n                                                                                                          }',9,'2018-11-14 13:28:51','0'),(25,'route','路由ID','id','{ \"name\": \"id\",\"index\": \"id\",\"width\": \"10\",\n                                                                                                             \"editable\": true,\n                                                                                                             \"sorttype\": \"int\" }',1,'2018-11-14 13:28:51','0'),(26,'route','AppId','appId','{ \"name\": \"appId\",\"index\": \"appId\",\"width\": \"30\",\n                                                                                                             \"editable\": true\n                                                                                                           }',2,'2018-11-14 13:28:51','0'),(27,'route','服务ID','serviceId','{ \"name\": \"serviceId\",\"index\": \"serviceId\",\"width\": \"30\",\n                                                                                                             \"editable\": true }',3,'2018-11-14 13:28:51','0'),(28,'route','调用方式','invokeModel','{ \"name\": \"invokeModel\",\"index\": \"invokeModel\",\"width\": \"50\",\n                                                                                                              \"editable\": true }',4,'2018-11-14 13:28:51','0'),(29,'route','服务名称','serviceName','{ \"name\": \"serviceName\",\"index\": \"serviceName\",\"width\": \"30\",\n                                                                                                             \"editable\": true }',5,'2018-11-14 13:28:51','0'),(30,'route','服务编码','serviceCode','{ \"name\": \"serviceCode\",\"index\": \"serviceCode\",\"width\": \"30\",\n                                                                                                             \"editable\": true }',6,'2018-11-14 13:28:51','0'),(31,'route','订单类型','orderTypeCd','{ \"name\": \"orderTypeCd\",\"index\": \"orderTypeCd\",\"width\": \"30\",\n                                                                                                             \"editable\": true }',7,'2018-11-14 13:28:51','0'),(32,'route','调用次数限制','invokelimitTimes','{ \"name\": \"invokelimitTimes\",\"index\": \"invokelimitTimes\",\"width\": \"40\",\n                                                                                                             \"editable\": true }',8,'2018-11-14 13:28:51','0'),(33,'route','BUTTON','BUTTON','{\n                                                                                                            \"name\": \"detail\",\n                                                                                                            \"index\": \"\",\n                                                                                                            \"width\": \"40\",\n                                                                                                            \"fixed\": \"true\",\n                                                                                                            \"sortable\": \"false\",\n                                                                                                            \"resize\": \"false\",\n                                                                                                            \"formatter\": \"function(cellvalue, options, rowObject){\n var temp =\"<div style=\'margin-left:8px;\'><div title=\'详情记录\' style=\'float:left;cursor:pointer;\' class=\'ui-pg-div\' id=\'jEditButton_3\' onclick=\'detail(\"+rowObject+\")\' onmouseover=\'jQuery(this).addClass(\'ui-state-hover\');\' onmouseout=\'jQuery(this).removeClass(\'ui-state-hover\');\'><span class=\'ui-icon fa-search-plus\'/></div></div>\";\n return temp; \n}\"}',9,'2018-11-14 13:28:51','0'),(34,'cache','缓存ID','id','{ \"name\": \"id\",\"index\": \"id\",\"width\": \"10\",\n                                                                                                             \"editable\": true,\n                                                                                                             \"sorttype\": \"int\" }',1,'2018-11-14 13:28:51','0'),(35,'cache','缓存编码','cacheCode','{ \"name\": \"cacheCode\",\"index\": \"cacheCode\",\"width\": \"30\",\n                                                                                                             \"editable\": true }',2,'2018-11-14 13:28:51','0'),(36,'cache','缓存名称','cacheName','{ \"name\": \"cacheName\",\"index\": \"cacheName\",\"width\": \"30\",\n                                                                                                             \"editable\": true }',3,'2018-11-14 13:28:51','0'),(37,'cache','BUTTON','BUTTON','{\n                                                                                                            \"name\": \"detail\",\n                                                                                                            \"index\": \"\",\n                                                                                                            \"width\": \"40\",\n                                                                                                            \"fixed\": \"true\",\n                                                                                                            \"sortable\": \"false\",\n                                                                                                            \"resize\": \"false\",\n                                                                                                            \"formatter\": \"\"function(cellvalue, options, rowObject){ var temp =\"<div style=\'margin-left:8px;\'><button type=\'button\' class=\'btn btn-warning\' style=\'border-radius: .25rem;\' onclick=\'flush(this,\"+rowObject.cacheCode+\")\'>刷新缓存</button></div>\";return temp; }\"\n                                                                                                          }',4,'2018-11-14 13:28:51','0');
/*!40000 ALTER TABLE `c_template_col` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `community_member_type`
--

DROP TABLE IF EXISTS `community_member_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `community_member_type` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `member_type_cd` varchar(12) NOT NULL COMMENT '编码',
  `name` varchar(50) NOT NULL COMMENT '名称',
  `description` varchar(200) DEFAULT NULL COMMENT '描述',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `member_type_cd` (`member_type_cd`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `community_member_type`
--

LOCK TABLES `community_member_type` WRITE;
/*!40000 ALTER TABLE `community_member_type` DISABLE KEYS */;
INSERT INTO `community_member_type` VALUES (1,'390001200001','商户关系','小区和商户之间关系','2018-12-17 04:07:21'),(2,'390001200002','物业关系','小区和物业之间关系','2018-12-17 04:08:16'),(3,'390001200003','代理商关系','小区和代理商之间关系','2018-12-17 04:08:34');
/*!40000 ALTER TABLE `community_member_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `credentials`
--

DROP TABLE IF EXISTS `credentials`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `credentials` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `credentials_cd` varchar(12) NOT NULL COMMENT '证件编码',
  `name` varchar(50) NOT NULL COMMENT '证件名称',
  `description` varchar(200) DEFAULT NULL COMMENT '描述',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `credentials`
--

LOCK TABLES `credentials` WRITE;
/*!40000 ALTER TABLE `credentials` DISABLE KEYS */;
/*!40000 ALTER TABLE `credentials` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `l_transaction_log`
--

DROP TABLE IF EXISTS `l_transaction_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `l_transaction_log`
--

LOCK TABLES `l_transaction_log` WRITE;
/*!40000 ALTER TABLE `l_transaction_log` DISABLE KEYS */;
/*!40000 ALTER TABLE `l_transaction_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `l_transaction_log_message`
--

DROP TABLE IF EXISTS `l_transaction_log_message`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `l_transaction_log_message`
--

LOCK TABLES `l_transaction_log_message` WRITE;
/*!40000 ALTER TABLE `l_transaction_log_message` DISABLE KEYS */;
/*!40000 ALTER TABLE `l_transaction_log_message` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `m_menu`
--

DROP TABLE IF EXISTS `m_menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `m_menu`
--

LOCK TABLES `m_menu` WRITE;
/*!40000 ALTER TABLE `m_menu` DISABLE KEYS */;
INSERT INTO `m_menu` VALUES (1,'系统配置','1',-1,'LEFT','10001','',1,'2018-11-14 13:28:51','0'),(2,'映射管理','2',1,'LEFT','10001','',2,'2018-11-14 13:28:51','0'),(3,'外部应用','2',1,'LEFT','10001','',3,'2018-11-14 13:28:51','0'),(4,'路由管理','2',1,'LEFT','10001','',4,'2018-11-14 13:28:51','0'),(5,'服务管理','2',1,'LEFT','10001','',5,'2018-11-14 13:28:51','0'),(6,'缓存管理','1',-1,'LEFT','10001','',1,'2018-11-14 13:28:51','0'),(7,'刷新缓存','2',1,'LEFT','10001','',2,'2018-11-14 13:28:51','0');
/*!40000 ALTER TABLE `m_menu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `m_menu_2_user`
--

DROP TABLE IF EXISTS `m_menu_2_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `m_menu_2_user` (
  `m_user_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '菜单用户ID',
  `m_id` int(11) NOT NULL COMMENT '菜单id',
  `user_id` varchar(30) NOT NULL COMMENT '用户id',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `status_cd` varchar(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考c_status表，0在用，1失效',
  PRIMARY KEY (`m_user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `m_menu_2_user`
--

LOCK TABLES `m_menu_2_user` WRITE;
/*!40000 ALTER TABLE `m_menu_2_user` DISABLE KEYS */;
INSERT INTO `m_menu_2_user` VALUES (1,1,'10001','2018-11-14 13:28:51','0'),(2,2,'10001','2018-11-14 13:28:51','0'),(3,3,'10001','2018-11-14 13:28:51','0'),(4,4,'10001','2018-11-14 13:28:51','0'),(5,5,'10001','2018-11-14 13:28:51','0'),(6,6,'10001','2018-11-14 13:28:51','0'),(7,7,'10001','2018-11-14 13:28:51','0');
/*!40000 ALTER TABLE `m_menu_2_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `m_menu_ctg`
--

DROP TABLE IF EXISTS `m_menu_ctg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `m_menu_ctg` (
  `m_ctg_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '菜单配置ID',
  `m_id` int(11) NOT NULL COMMENT '菜单ID',
  `url` varchar(100) NOT NULL COMMENT '菜单打开地址',
  `template` varchar(50) DEFAULT NULL COMMENT '页面模板 模板名称',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `status_cd` varchar(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考c_status表，0在用，1失效',
  PRIMARY KEY (`m_ctg_id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `m_menu_ctg`
--

LOCK TABLES `m_menu_ctg` WRITE;
/*!40000 ALTER TABLE `m_menu_ctg` DISABLE KEYS */;
INSERT INTO `m_menu_ctg` VALUES (1,1,'#','','2018-11-14 13:28:51','0'),(2,2,'/console/list?templateCode=mapping','','2018-11-14 13:28:51','0'),(3,3,'/console/list?templateCode=app','','2018-11-14 13:28:51','0'),(4,4,'/console/list?templateCode=service','','2018-11-14 13:28:51','0'),(5,5,'/console/list?templateCode=route','','2018-11-14 13:28:51','0'),(6,6,'#','','2018-11-14 13:28:51','0'),(7,7,'/','','2018-11-14 13:28:51','0');
/*!40000 ALTER TABLE `m_menu_ctg` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `p_property`
--

DROP TABLE IF EXISTS `p_property`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `p_property` (
  `property_id` varchar(30) NOT NULL COMMENT '物业ID',
  `b_id` varchar(30) NOT NULL COMMENT '业务Id',
  `name` varchar(100) NOT NULL COMMENT '物业名称',
  `address` varchar(200) NOT NULL COMMENT '物业地址',
  `tel` varchar(11) NOT NULL COMMENT '电话',
  `nearby_landmarks` varchar(200) DEFAULT NULL COMMENT '地标，如王府井北60米',
  `map_x` varchar(20) NOT NULL COMMENT '地区 x坐标',
  `map_y` varchar(20) NOT NULL COMMENT '地区 y坐标',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `status_cd` varchar(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考c_status表，S 保存，0, 在用 1失效',
  UNIQUE KEY `property_id` (`property_id`),
  UNIQUE KEY `idx_property_property_id` (`property_id`),
  KEY `idx_property_b_id` (`b_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `p_property`
--

LOCK TABLES `p_property` WRITE;
/*!40000 ALTER TABLE `p_property` DISABLE KEYS */;
INSERT INTO `p_property` VALUES ('9020181218000001','10234567894','方博家园123','青海省西宁市城中区129号123','17797173952','王府井旁40米','102.801909','37.597263','2018-12-18 15:01:22','0');
/*!40000 ALTER TABLE `p_property` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `p_property_attr`
--

DROP TABLE IF EXISTS `p_property_attr`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `p_property_attr` (
  `b_id` varchar(30) NOT NULL COMMENT '订单ID',
  `attr_id` varchar(30) NOT NULL COMMENT '属性id',
  `property_id` varchar(30) NOT NULL COMMENT '用户ID',
  `spec_cd` varchar(12) NOT NULL COMMENT '规格id,参考spec表',
  `VALUE` varchar(50) NOT NULL COMMENT '属性值',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `status_cd` varchar(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考c_status表，S 保存，0, 在用 1失效',
  UNIQUE KEY `attr_id` (`attr_id`),
  KEY `idx_property_attr_b_id` (`b_id`),
  KEY `idx_property_attr_property_id` (`property_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `p_property_attr`
--

LOCK TABLES `p_property_attr` WRITE;
/*!40000 ALTER TABLE `p_property_attr` DISABLE KEYS */;
INSERT INTO `p_property_attr` VALUES ('19234567894','112018121800000002','9020181218000001','1001','02','2018-12-18 15:01:22','1');
/*!40000 ALTER TABLE `p_property_attr` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `p_property_cerdentials`
--

DROP TABLE IF EXISTS `p_property_cerdentials`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `p_property_cerdentials` (
  `property_cerdentials_id` varchar(30) NOT NULL COMMENT '物业证件ID',
  `b_id` varchar(30) NOT NULL COMMENT '业务Id',
  `property_id` varchar(30) NOT NULL COMMENT '物业ID',
  `credentials_cd` varchar(12) NOT NULL COMMENT '证件类型，对应于 credentials表',
  `value` varchar(50) NOT NULL COMMENT '证件号码',
  `validity_period` date NOT NULL COMMENT '有效期，如果是长期有效 写成 3000/1/1',
  `positive_photo` varchar(100) DEFAULT NULL COMMENT '正面照片',
  `negative_photo` varchar(100) DEFAULT NULL COMMENT '反面照片',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `status_cd` varchar(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考c_status表，S 保存，0, 在用 1失效',
  UNIQUE KEY `property_cerdentials_id` (`property_cerdentials_id`),
  KEY `idx_property_cerdentials_b_id` (`b_id`),
  KEY `idx_property_cerdentials_property_id` (`property_id`),
  KEY `idx__property_cerdentials_id` (`property_cerdentials_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `p_property_cerdentials`
--

LOCK TABLES `p_property_cerdentials` WRITE;
/*!40000 ALTER TABLE `p_property_cerdentials` DISABLE KEYS */;
INSERT INTO `p_property_cerdentials` VALUES ('9220181218000002','16234567894','9020181218000001','1','632126XXXXXXXX2012','3000-02-01','1234567.jpg','11.jpg','2018-12-18 15:32:38','1');
/*!40000 ALTER TABLE `p_property_cerdentials` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `p_property_fee`
--

DROP TABLE IF EXISTS `p_property_fee`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `p_property_fee` (
  `fee_id` varchar(30) NOT NULL COMMENT 'ID',
  `b_id` varchar(30) NOT NULL COMMENT '业务Id',
  `property_id` varchar(30) NOT NULL COMMENT '物业ID',
  `fee_type_cd` varchar(10) NOT NULL COMMENT '费用类型,物业费，停车费 请查看property_fee_type表',
  `fee_money` varchar(20) NOT NULL COMMENT '费用金额',
  `fee_time` varchar(10) NOT NULL COMMENT '费用周期，一个月，半年，或一年 请查看property_fee_time表',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `start_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '开始时间',
  `end_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '结束时间',
  `status_cd` varchar(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考c_status表，S 保存，0, 在用 1失效',
  UNIQUE KEY `fee_id` (`fee_id`),
  KEY `idx_property_fee_fee_id` (`fee_id`),
  KEY `idx_property_fee_b_id` (`b_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `p_property_fee`
--

LOCK TABLES `p_property_fee` WRITE;
/*!40000 ALTER TABLE `p_property_fee` DISABLE KEYS */;
INSERT INTO `p_property_fee` VALUES ('9420181221000001','13234567894','9020181218000001','T','10','0.5','2018-12-20 16:15:29','2018-05-01 00:00:00','2020-11-30 00:00:00','0');
/*!40000 ALTER TABLE `p_property_fee` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `p_property_house`
--

DROP TABLE IF EXISTS `p_property_house`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `p_property_house` (
  `b_id` varchar(30) NOT NULL COMMENT '业务Id',
  `house_id` varchar(30) NOT NULL COMMENT 'ID',
  `property_id` varchar(30) NOT NULL COMMENT '物业ID',
  `house_num` varchar(30) NOT NULL COMMENT '门牌号',
  `house_name` varchar(50) NOT NULL COMMENT '住户名称',
  `house_phone` varchar(11) DEFAULT NULL COMMENT '住户联系号码',
  `house_area` varchar(30) NOT NULL COMMENT '房屋面积',
  `fee_type_cd` varchar(10) NOT NULL COMMENT '费用类型 property_fee_type表',
  `fee_price` varchar(30) NOT NULL COMMENT '费用单价',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `status_cd` varchar(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考c_status表，S 保存，0, 在用 1失效',
  UNIQUE KEY `house_id` (`house_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `p_property_house`
--

LOCK TABLES `p_property_house` WRITE;
/*!40000 ALTER TABLE `p_property_house` DISABLE KEYS */;
INSERT INTO `p_property_house` VALUES ('6234567894','9520181222000001','123213','T','10',NULL,'01918','111','10.09','2018-12-21 17:02:03','0'),('17234567894','9520181222000005','123213','123123','吴XX住宅','17797173943','01919','112','11.09','2018-12-21 17:08:34','1');
/*!40000 ALTER TABLE `p_property_house` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `p_property_house_attr`
--

DROP TABLE IF EXISTS `p_property_house_attr`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `p_property_house_attr` (
  `b_id` varchar(30) NOT NULL COMMENT '业务Id',
  `attr_id` varchar(30) NOT NULL COMMENT '属性id',
  `house_id` varchar(30) NOT NULL COMMENT '用户ID',
  `spec_cd` varchar(12) NOT NULL COMMENT '规格id,参考spec表',
  `VALUE` varchar(50) NOT NULL COMMENT '属性值',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `status_cd` varchar(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考c_status表，S 保存，0, 在用 1失效',
  UNIQUE KEY `attr_id` (`attr_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `p_property_house_attr`
--

LOCK TABLES `p_property_house_attr` WRITE;
/*!40000 ALTER TABLE `p_property_house_attr` DISABLE KEYS */;
INSERT INTO `p_property_house_attr` VALUES ('6234567894','112018122200000002','9520181222000001','1001','01','2018-12-21 17:02:03','0'),('17234567894','112018122200000006','9520181222000005','1001','02','2018-12-21 17:08:34','1');
/*!40000 ALTER TABLE `p_property_house_attr` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `p_property_photo`
--

DROP TABLE IF EXISTS `p_property_photo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `p_property_photo` (
  `property_photo_id` varchar(30) NOT NULL COMMENT '物业照片ID',
  `b_id` varchar(30) NOT NULL COMMENT '业务Id',
  `property_id` varchar(30) NOT NULL COMMENT '物业ID',
  `property_photo_type_cd` varchar(12) NOT NULL COMMENT '物业照片类型,T 门头照 I 内景照',
  `photo` varchar(100) NOT NULL COMMENT '照片',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `status_cd` varchar(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考c_status表，S 保存，0, 在用 1失效',
  UNIQUE KEY `property_photo_id` (`property_photo_id`),
  KEY `idx_property_photo_b_id` (`b_id`),
  KEY `idx_property_photo_property_id` (`property_id`),
  KEY `idx_property_photo_property_photo_id` (`property_photo_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `p_property_photo`
--

LOCK TABLES `p_property_photo` WRITE;
/*!40000 ALTER TABLE `p_property_photo` DISABLE KEYS */;
INSERT INTO `p_property_photo` VALUES ('12320181218000001','15234567894','9020181218000001','T','123456789.jpg','2018-12-18 15:23:15','1');
/*!40000 ALTER TABLE `p_property_photo` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `p_property_user`
--

DROP TABLE IF EXISTS `p_property_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `p_property_user` (
  `property_user_id` varchar(30) NOT NULL COMMENT '物业用户ID',
  `property_id` varchar(30) NOT NULL COMMENT '物业ID',
  `b_id` varchar(30) NOT NULL COMMENT '业务Id',
  `user_id` varchar(30) NOT NULL COMMENT '用户ID',
  `rel_cd` varchar(30) NOT NULL COMMENT '用户和物业关系 详情查看 property_user_rel表',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `status_cd` varchar(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考c_status表，S 保存，0, 在用 1失效',
  UNIQUE KEY `property_user_id` (`property_user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `p_property_user`
--

LOCK TABLES `p_property_user` WRITE;
/*!40000 ALTER TABLE `p_property_user` DISABLE KEYS */;
INSERT INTO `p_property_user` VALUES ('9320181219000001','9020181218000001','18234567894','123','600311000001','2018-12-18 17:30:46','1');
/*!40000 ALTER TABLE `p_property_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `property_fee_time`
--

DROP TABLE IF EXISTS `property_fee_time`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `property_fee_time` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `domain` varchar(20) NOT NULL COMMENT '域',
  `fee_time_cd` varchar(12) NOT NULL COMMENT '费用周期编码 一年，半年等',
  `name` varchar(50) NOT NULL COMMENT '收费类型编码',
  `description` varchar(200) DEFAULT NULL COMMENT '描述',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `fee_time_cd` (`fee_time_cd`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `property_fee_time`
--

LOCK TABLES `property_fee_time` WRITE;
/*!40000 ALTER TABLE `property_fee_time` DISABLE KEYS */;
/*!40000 ALTER TABLE `property_fee_time` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `property_fee_type`
--

DROP TABLE IF EXISTS `property_fee_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `property_fee_type` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `domain` varchar(20) NOT NULL COMMENT '域',
  `fee_type_cd` varchar(12) NOT NULL COMMENT '收费类型 物业费 停车费等',
  `name` varchar(50) NOT NULL COMMENT '收费类型编码',
  `description` varchar(200) DEFAULT NULL COMMENT '描述',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `fee_type_cd` (`fee_type_cd`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `property_fee_type`
--

LOCK TABLES `property_fee_type` WRITE;
/*!40000 ALTER TABLE `property_fee_type` DISABLE KEYS */;
/*!40000 ALTER TABLE `property_fee_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `property_user_rel`
--

DROP TABLE IF EXISTS `property_user_rel`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `property_user_rel` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `rel_cd` varchar(12) NOT NULL COMMENT '物业用户关系编码',
  `name` varchar(50) NOT NULL COMMENT '物业用户关系编码名称',
  `description` varchar(200) DEFAULT NULL COMMENT '描述',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `rel_cd` (`rel_cd`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `property_user_rel`
--

LOCK TABLES `property_user_rel` WRITE;
/*!40000 ALTER TABLE `property_user_rel` DISABLE KEYS */;
INSERT INTO `property_user_rel` VALUES (1,'600311000001','一般关系','一般关系','2018-12-18 17:11:19');
/*!40000 ALTER TABLE `property_user_rel` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `s_buy_shop`
--

DROP TABLE IF EXISTS `s_buy_shop`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `s_buy_shop`
--

LOCK TABLES `s_buy_shop` WRITE;
/*!40000 ALTER TABLE `s_buy_shop` DISABLE KEYS */;
/*!40000 ALTER TABLE `s_buy_shop` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `s_buy_shop_attr`
--

DROP TABLE IF EXISTS `s_buy_shop_attr`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `s_buy_shop_attr`
--

LOCK TABLES `s_buy_shop_attr` WRITE;
/*!40000 ALTER TABLE `s_buy_shop_attr` DISABLE KEYS */;
/*!40000 ALTER TABLE `s_buy_shop_attr` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `s_community`
--

DROP TABLE IF EXISTS `s_community`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `s_community` (
  `community_id` varchar(30) NOT NULL COMMENT '小区ID',
  `b_id` varchar(30) NOT NULL COMMENT '业务Id',
  `name` varchar(100) NOT NULL COMMENT '小区名称',
  `address` varchar(200) NOT NULL COMMENT '小区地址',
  `nearby_landmarks` varchar(200) DEFAULT NULL COMMENT '地标，如王府井北60米',
  `city_code` varchar(12) NOT NULL COMMENT '根据定位获取城市编码',
  `map_x` varchar(20) NOT NULL COMMENT '地区 x坐标',
  `map_y` varchar(20) NOT NULL COMMENT '地区 y坐标',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `status_cd` varchar(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考c_status表，S 保存，0, 在用 1失效',
  UNIQUE KEY `community_id` (`community_id`),
  UNIQUE KEY `idx_community_id` (`community_id`),
  KEY `idx_community_b_id` (`b_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `s_community`
--

LOCK TABLES `s_community` WRITE;
/*!40000 ALTER TABLE `s_community` DISABLE KEYS */;
INSERT INTO `s_community` VALUES ('7020181217000001','1234567891','万博家博园（城西区）','青海省西宁市城中区129号','王府井旁30米','100010','101.801909','36.597263','2018-12-17 05:00:55','0');
/*!40000 ALTER TABLE `s_community` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `s_community_attr`
--

DROP TABLE IF EXISTS `s_community_attr`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `s_community_attr` (
  `b_id` varchar(30) NOT NULL COMMENT '订单ID',
  `attr_id` varchar(30) NOT NULL COMMENT '属性id',
  `community_id` varchar(30) NOT NULL COMMENT '用户ID',
  `spec_cd` varchar(12) NOT NULL COMMENT '规格id,参考spec表',
  `VALUE` varchar(50) NOT NULL COMMENT '属性值',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `status_cd` varchar(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考c_status表，S 保存，0, 在用 1失效',
  UNIQUE KEY `attr_id` (`attr_id`),
  KEY `idx_community_attr_b_id` (`b_id`),
  KEY `idx_attr_community_id` (`community_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `s_community_attr`
--

LOCK TABLES `s_community_attr` WRITE;
/*!40000 ALTER TABLE `s_community_attr` DISABLE KEYS */;
INSERT INTO `s_community_attr` VALUES ('1234567891','112018121700000002','7020181217000001','1001','01','2018-12-17 05:00:56','0');
/*!40000 ALTER TABLE `s_community_attr` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `s_community_member`
--

DROP TABLE IF EXISTS `s_community_member`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `s_community_member` (
  `community_member_id` varchar(30) NOT NULL COMMENT 'ID',
  `b_id` varchar(30) NOT NULL COMMENT '业务Id',
  `community_id` varchar(30) NOT NULL COMMENT '小区ID',
  `member_id` varchar(50) NOT NULL COMMENT '成员ID',
  `member_type_cd` varchar(12) NOT NULL COMMENT '成员类型见 community_member_type表',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `status_cd` varchar(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考c_status表，S 保存，0, 在用 1失效',
  UNIQUE KEY `community_member_id` (`community_member_id`),
  KEY `idx_s_community_member_id` (`community_id`),
  KEY `idx_s_community_member_b_id` (`b_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `s_community_member`
--

LOCK TABLES `s_community_member` WRITE;
/*!40000 ALTER TABLE `s_community_member` DISABLE KEYS */;
INSERT INTO `s_community_member` VALUES ('7220181217000001','1234567891','7020181217000001','345678','390001200001','2018-12-17 14:42:31','0'),('7220181217000004','1234567894','7020181217000001','3456789','390001200001','2018-12-17 14:46:20','1');
/*!40000 ALTER TABLE `s_community_member` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `s_community_photo`
--

DROP TABLE IF EXISTS `s_community_photo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `s_community_photo` (
  `community_photo_id` varchar(30) NOT NULL COMMENT '商户照片ID',
  `b_id` varchar(30) NOT NULL COMMENT '业务Id',
  `community_id` varchar(30) NOT NULL COMMENT '小区ID',
  `community_photo_type_cd` varchar(12) NOT NULL COMMENT '商户照片类型,T 门头照 I 内景照',
  `photo` varchar(100) NOT NULL COMMENT '照片',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `status_cd` varchar(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考c_status表，S 保存，0, 在用 1失效',
  UNIQUE KEY `community_photo_id` (`community_photo_id`),
  KEY `idx_community_photo_b_id` (`b_id`),
  KEY `idx_community_photo_community_id` (`community_id`),
  KEY `idx_community_photo_community_photo_id` (`community_photo_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `s_community_photo`
--

LOCK TABLES `s_community_photo` WRITE;
/*!40000 ALTER TABLE `s_community_photo` DISABLE KEYS */;
INSERT INTO `s_community_photo` VALUES ('7120181217000003','1234567891','7020181217000001','T','12345678.jpg','2018-12-17 05:00:56','0');
/*!40000 ALTER TABLE `s_community_photo` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `s_member_store`
--

DROP TABLE IF EXISTS `s_member_store`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `s_member_store` (
  `member_store_id` varchar(30) NOT NULL COMMENT 'ID',
  `b_id` varchar(30) NOT NULL COMMENT '业务Id',
  `store_id` varchar(30) NOT NULL COMMENT '商店ID',
  `member_id` varchar(50) NOT NULL COMMENT '商户成员ID',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `status_cd` varchar(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考c_status表，S 保存，0, 在用 1失效',
  UNIQUE KEY `member_store_id` (`member_store_id`),
  KEY `idx_s_member_store_id` (`store_id`),
  KEY `idx_s_member_store_b_id` (`b_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `s_member_store`
--

LOCK TABLES `s_member_store` WRITE;
/*!40000 ALTER TABLE `s_member_store` DISABLE KEYS */;
INSERT INTO `s_member_store` VALUES ('900001','900001','123456','100001','2018-12-12 14:01:52','0');
/*!40000 ALTER TABLE `s_member_store` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `s_shop`
--

DROP TABLE IF EXISTS `s_shop`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `s_shop`
--

LOCK TABLES `s_shop` WRITE;
/*!40000 ALTER TABLE `s_shop` DISABLE KEYS */;
/*!40000 ALTER TABLE `s_shop` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `s_shop_attr`
--

DROP TABLE IF EXISTS `s_shop_attr`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `s_shop_attr`
--

LOCK TABLES `s_shop_attr` WRITE;
/*!40000 ALTER TABLE `s_shop_attr` DISABLE KEYS */;
/*!40000 ALTER TABLE `s_shop_attr` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `s_shop_attr_param`
--

DROP TABLE IF EXISTS `s_shop_attr_param`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `s_shop_attr_param`
--

LOCK TABLES `s_shop_attr_param` WRITE;
/*!40000 ALTER TABLE `s_shop_attr_param` DISABLE KEYS */;
/*!40000 ALTER TABLE `s_shop_attr_param` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `s_shop_catalog`
--

DROP TABLE IF EXISTS `s_shop_catalog`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `s_shop_catalog`
--

LOCK TABLES `s_shop_catalog` WRITE;
/*!40000 ALTER TABLE `s_shop_catalog` DISABLE KEYS */;
/*!40000 ALTER TABLE `s_shop_catalog` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `s_shop_desc`
--

DROP TABLE IF EXISTS `s_shop_desc`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `s_shop_desc`
--

LOCK TABLES `s_shop_desc` WRITE;
/*!40000 ALTER TABLE `s_shop_desc` DISABLE KEYS */;
/*!40000 ALTER TABLE `s_shop_desc` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `s_shop_photo`
--

DROP TABLE IF EXISTS `s_shop_photo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `s_shop_photo`
--

LOCK TABLES `s_shop_photo` WRITE;
/*!40000 ALTER TABLE `s_shop_photo` DISABLE KEYS */;
/*!40000 ALTER TABLE `s_shop_photo` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `s_shop_preferential`
--

DROP TABLE IF EXISTS `s_shop_preferential`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `s_shop_preferential`
--

LOCK TABLES `s_shop_preferential` WRITE;
/*!40000 ALTER TABLE `s_shop_preferential` DISABLE KEYS */;
/*!40000 ALTER TABLE `s_shop_preferential` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `s_store`
--

DROP TABLE IF EXISTS `s_store`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `s_store` (
  `store_id` varchar(30) NOT NULL COMMENT '商店ID',
  `b_id` varchar(30) NOT NULL COMMENT '业务Id',
  `user_id` varchar(30) NOT NULL COMMENT '用户ID',
  `name` varchar(100) NOT NULL COMMENT '店铺名称',
  `address` varchar(200) NOT NULL COMMENT '店铺地址',
  `tel` varchar(11) NOT NULL COMMENT '电话',
  `store_type_cd` varchar(12) DEFAULT NULL,
  `nearby_landmarks` varchar(200) DEFAULT NULL COMMENT '地标，如王府井北60米',
  `map_x` varchar(20) NOT NULL COMMENT '地区 x坐标',
  `map_y` varchar(20) NOT NULL COMMENT '地区 y坐标',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `status_cd` varchar(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考c_status表，S 保存，0, 在用 1失效',
  UNIQUE KEY `store_id` (`store_id`),
  UNIQUE KEY `idx_store_store_id` (`store_id`),
  KEY `idx_store_b_id` (`b_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `s_store`
--

LOCK TABLES `s_store` WRITE;
/*!40000 ALTER TABLE `s_store` DISABLE KEYS */;
INSERT INTO `s_store` VALUES ('100001','100001','30521125069510950912','小文小区代理公司','青海省西宁市城东区昆仑东路100号','13109785273','800900000002','城东区政府斜对面','','','2018-12-12 13:47:34','0'),('123456','123456','30521125069510950912','安鑫雅苑','青海省西宁市城中区神宁路28号','13109785273','800900000000','城东区政府斜对面','','','2018-12-12 13:37:52','0');
/*!40000 ALTER TABLE `s_store` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `s_store_attr`
--

DROP TABLE IF EXISTS `s_store_attr`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `s_store_attr`
--

LOCK TABLES `s_store_attr` WRITE;
/*!40000 ALTER TABLE `s_store_attr` DISABLE KEYS */;
/*!40000 ALTER TABLE `s_store_attr` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `s_store_cerdentials`
--

DROP TABLE IF EXISTS `s_store_cerdentials`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `s_store_cerdentials`
--

LOCK TABLES `s_store_cerdentials` WRITE;
/*!40000 ALTER TABLE `s_store_cerdentials` DISABLE KEYS */;
/*!40000 ALTER TABLE `s_store_cerdentials` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `s_store_fee`
--

DROP TABLE IF EXISTS `s_store_fee`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `s_store_fee`
--

LOCK TABLES `s_store_fee` WRITE;
/*!40000 ALTER TABLE `s_store_fee` DISABLE KEYS */;
INSERT INTO `s_store_fee` VALUES ('43512782246290128896','20512782243995844608','201811150000001','201811150000235','8001','200','7002','2018-11-15 16:13:35','2018-11-16 00:00:00','2018-12-16 00:00:00','0'),('43512782363944550400','20512782362006781952','201811150000001','201811150000235','8001','200','7002','2018-11-15 16:14:03','2018-11-16 00:00:00','2018-12-16 00:00:00','0');
/*!40000 ALTER TABLE `s_store_fee` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `s_store_house`
--

DROP TABLE IF EXISTS `s_store_house`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `s_store_house`
--

LOCK TABLES `s_store_house` WRITE;
/*!40000 ALTER TABLE `s_store_house` DISABLE KEYS */;
INSERT INTO `s_store_house` VALUES ('44513145747198181376','10-1253','测试你大爷','10000','200','7002','54','800625487','2018-11-16 16:18:02','2018-11-16 00:00:00','C'),('44513145904337780736','10-1253','测试你大爷','10000','200','7002','54','800625487','2018-11-16 16:20:07','2018-11-16 00:00:00','C'),('44513146888191475712','10-1253','测试你大爷','10000','200','7002','54','800625487','2018-11-16 16:22:33','2018-11-16 00:00:00','C');
/*!40000 ALTER TABLE `s_store_house` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `s_store_house_attr`
--

DROP TABLE IF EXISTS `s_store_house_attr`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `s_store_house_attr` (
  `attr_id` varchar(30) NOT NULL COMMENT '属性id',
  `house_id` varchar(30) NOT NULL COMMENT '用户ID',
  `spec_cd` varchar(12) NOT NULL COMMENT '规格id,参考spec表',
  `VALUE` varchar(50) NOT NULL COMMENT '属性值',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `status_cd` varchar(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考c_status表，S 保存，0, 在用 1失效',
  UNIQUE KEY `attr_id` (`attr_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `s_store_house_attr`
--

LOCK TABLES `s_store_house_attr` WRITE;
/*!40000 ALTER TABLE `s_store_house_attr` DISABLE KEYS */;
/*!40000 ALTER TABLE `s_store_house_attr` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `s_store_photo`
--

DROP TABLE IF EXISTS `s_store_photo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `s_store_photo`
--

LOCK TABLES `s_store_photo` WRITE;
/*!40000 ALTER TABLE `s_store_photo` DISABLE KEYS */;
/*!40000 ALTER TABLE `s_store_photo` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `spec`
--

DROP TABLE IF EXISTS `spec`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `spec`
--

LOCK TABLES `spec` WRITE;
/*!40000 ALTER TABLE `spec` DISABLE KEYS */;
INSERT INTO `spec` VALUES (1,'ORDERS','1000','订单来源','订单来源','2018-11-14 13:28:44'),(2,'BUSINESS','2000','推荐UserID','推荐UserID','2018-11-14 13:28:44');
/*!40000 ALTER TABLE `spec` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `store_fee_time`
--

DROP TABLE IF EXISTS `store_fee_time`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `store_fee_time`
--

LOCK TABLES `store_fee_time` WRITE;
/*!40000 ALTER TABLE `store_fee_time` DISABLE KEYS */;
INSERT INTO `store_fee_time` VALUES (1,'feetype_domain','7001','一个月','按月缴费','2018-11-15 14:15:28'),(5,'feetype_domain','7002','季度','按季度缴费','2018-11-15 14:16:10'),(6,'feetype_domain','7003','年','按年缴费','2018-11-15 14:16:13');
/*!40000 ALTER TABLE `store_fee_time` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `store_fee_type`
--

DROP TABLE IF EXISTS `store_fee_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `store_fee_type`
--

LOCK TABLES `store_fee_type` WRITE;
/*!40000 ALTER TABLE `store_fee_type` DISABLE KEYS */;
INSERT INTO `store_fee_type` VALUES (1,'feetype_domain','8001','物业费','物业费按年收取','2018-11-15 14:08:11'),(2,'feetype_domain','8002','停车费','物业费按月收取','2018-11-15 14:08:28'),(3,'feetype_domain','8003','水费','物业费按需收取','2018-11-15 14:08:28'),(4,'feetype_domain','8004','电费','物业费按需收取','2018-11-15 14:08:28'),(5,'feetype_domain','8005','暖气费','物业费按年收取','2018-11-15 14:08:28'),(6,'feetype_domain','8006','气费','物业费按需收取','2018-11-15 14:08:28');
/*!40000 ALTER TABLE `store_fee_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `store_type`
--

DROP TABLE IF EXISTS `store_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `store_type` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `store_type_cd` varchar(12) NOT NULL COMMENT '店铺编码',
  `name` varchar(50) NOT NULL COMMENT '店铺种类编码',
  `description` varchar(200) DEFAULT NULL COMMENT '描述',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `store_type_cd` (`store_type_cd`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `store_type`
--

LOCK TABLES `store_type` WRITE;
/*!40000 ALTER TABLE `store_type` DISABLE KEYS */;
INSERT INTO `store_type` VALUES (1,'800900000001','运营团队','运营团队','2018-12-12 12:09:02'),(2,'800900000002','代理商','代理商','2018-12-12 12:09:50'),(3,'800900000003','物业','物业','2018-12-12 12:27:11'),(4,'800900000004','物流公司','物流公司','2018-12-12 12:27:53'),(5,'800900000005','超市','超市','2018-12-12 12:28:23'),(6,'800900000006','餐厅','餐厅','2018-12-12 12:28:43'),(7,'800900000007','饭馆','饭馆','2018-12-12 12:29:17'),(8,'800900000008','药店','药店','2018-12-12 12:29:41'),(9,'800900000000','小区','小区','2018-12-12 13:38:29');
/*!40000 ALTER TABLE `store_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tag`
--

DROP TABLE IF EXISTS `tag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tag` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `domain` varchar(20) NOT NULL COMMENT '标签域',
  `tag_cd` varchar(12) NOT NULL COMMENT '标签编码',
  `name` varchar(50) NOT NULL COMMENT '标签名称',
  `description` varchar(200) DEFAULT NULL COMMENT '描述',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tag`
--

LOCK TABLES `tag` WRITE;
/*!40000 ALTER TABLE `tag` DISABLE KEYS */;
/*!40000 ALTER TABLE `tag` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `u_location`
--

DROP TABLE IF EXISTS `u_location`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `u_location`
--

LOCK TABLES `u_location` WRITE;
/*!40000 ALTER TABLE `u_location` DISABLE KEYS */;
/*!40000 ALTER TABLE `u_location` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `u_user`
--

DROP TABLE IF EXISTS `u_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
) ENGINE=InnoDB AUTO_INCREMENT=54 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `u_user`
--

LOCK TABLES `u_user` WRITE;
/*!40000 ALTER TABLE `u_user` DISABLE KEYS */;
INSERT INTO `u_user` VALUES (1,'30516332294873563136','李四','928255095@qq.com',NULL,'ERCBHDUYFJDNDHDJDNDJDHDUDHDJDDKDK',NULL,NULL,'0','17797173943','02','20516332294412189696','2018-11-25 11:20:12','0'),(2,'30516364590414577664','王五','928255095@qq.com',NULL,'ERCBHDUYFJDNDHDJDNDJDHDUDHDJDDKDK',NULL,NULL,'0','17797173943','02','20516364589907066880','2018-11-25 13:28:32','0'),(3,'30516365201923129344','王五1','928255095@qq.com',NULL,'ERCBHDUYFJDNDHDJDNDJDHDUDHDJDDKDK',NULL,NULL,'0','17797173943','02','20516365201587585024','2018-11-25 13:30:57','0'),(4,'30516366113219559424','王五2','928255095@qq.com',NULL,'ERCBHDUYFJDNDHDJDNDJDHDUDHDJDDKDK',NULL,NULL,'0','17797173943','02','20516366112951123968','2018-11-25 13:34:35','0'),(5,'30516366709171437568','王五3','928255095@qq.com',NULL,'ERCBHDUYFJDNDHDJDNDJDHDUDHDJDDKDK',NULL,NULL,'0','17797173943','02','20516366708928167936','2018-11-25 13:36:57','0'),(6,'30516368306509201408','王五4','928255095@qq.com',NULL,'ERCBHDUYFJDNDHDJDNDJDHDUDHDJDDKDK',NULL,NULL,'0','17797173943','02','20516368306257543168','2018-11-25 13:43:17','0'),(7,'30516374464544391168','王五5','928255095@qq.com',NULL,'ERCBHDUYFJDNDHDJDNDJDHDUDHDJDDKDK',NULL,NULL,'0','17797173943','02','20516374464208846848','2018-11-25 14:07:46','0'),(8,'30516374772855095296','王五6','928255095@qq.com',NULL,'ERCBHDUYFJDNDHDJDNDJDHDUDHDJDDKDK',NULL,NULL,'0','17797173943','02','20516374772574076928','2018-11-25 14:08:59','0'),(9,'30516374801938399232','王五7','928255095@qq.com',NULL,'ERCBHDUYFJDNDHDJDNDJDHDUDHDJDDKDK',NULL,NULL,'0','17797173943','02','20516374801669963776','2018-11-25 14:09:06','0'),(10,'30516377393741447168','王五7','928255095@qq.com',NULL,'ERCBHDUYFJDNDHDJDNDJDHDUDHDJDDKDK',NULL,NULL,'0','17797173943','02','20516377393317822464','2018-11-25 14:19:24','0'),(11,'30516381939989495808','王五8','928255095@qq.com',NULL,'ERCBHDUYFJDNDHDJDNDJDHDUDHDJDDKDK',NULL,NULL,'0','17797173943','02','20516381939620397056','2018-11-25 14:37:28','0'),(12,'30516387723230068736','王五9','928255095@qq.com',NULL,'ERCBHDUYFJDNDHDJDNDJDHDUDHDJDDKDK',NULL,NULL,'0','17797173943','02','20516387722659643392','2018-11-25 15:00:27','0'),(13,'30516389225004810240','王五10','928255095@qq.com',NULL,'ERCBHDUYFJDNDHDJDNDJDHDUDHDJDDKDK',NULL,NULL,'0','17797173943','02','20516389224736374784','2018-11-25 15:06:25','0'),(14,'30516389265349820416','王五11','928255095@qq.com',NULL,'ERCBHDUYFJDNDHDJDNDJDHDUDHDJDDKDK',NULL,NULL,'0','17797173943','02','20516389265110745088','2018-11-25 15:06:34','0'),(15,'30516398820397957120','吴学文','',NULL,'0724094fbcd70db0493034daa2120aa1',NULL,NULL,'0','15897089471','02','20516398820167270400','2018-11-25 15:44:33','0'),(16,'30516400356482105344','师师','',NULL,'f1ab59f367854c555a84d42155d39aa3',NULL,NULL,'0','18987095720','02','20516400356243030016','2018-11-25 15:50:39','0'),(17,'30516401274132905984','张试卷','',NULL,'cdfea6fbffcd82758eec12a96c64e682',NULL,NULL,'0','15897089471','02','20516401273893830656','2018-11-25 15:54:18','0'),(18,'30517072483300425728','张三','',NULL,'dc2263f093f3db5edd6ce5ea6aa81c32',NULL,NULL,'0','17797173947','02','20517072482935521280','2018-11-27 12:21:26','0'),(19,'30517086035897761792','吴学文123','',NULL,'c91cf5db90773073a558e8740521c01c',NULL,NULL,'0','17797173942','02','20517086035457359872','2018-11-27 13:15:18','0'),(20,'30517710166884368384','王五112','928255095@qq.com',NULL,'ERCBHDUYFJDNDHDJDNDJDHDUDHDJDDKDK',NULL,NULL,'0','17797173943','02','20517710159825354752','2018-11-29 06:35:22','0'),(21,'30518939884421922816','admin','',NULL,'23a39fd21728292e1f794b250bc67019',NULL,NULL,'0','17797173944','02','20518939876956061696','2018-12-02 16:01:50','0'),(22,'30518940136629616640','wuxw','',NULL,'0bda701c63916b42e71851214373fe1b',NULL,NULL,'0','17797173940','02','20518940136344403968','2018-12-02 16:02:50','0'),(23,'30520751775595118592','zhangsan','',NULL,'b8ed29901921a8e7a8ec34d874a5379b',NULL,NULL,'0','17797173942','02','20520751770314489856','2018-12-07 16:01:38','0'),(24,'30520752094488051712','zhangsan','',NULL,'b8ed29901921a8e7a8ec34d874a5379b',NULL,NULL,'0','17797173942','02','20520752094244782080','2018-12-07 16:02:54','0'),(25,'30520752516195958784','zhangsan','',NULL,'b8ed29901921a8e7a8ec34d874a5379b',NULL,NULL,'0','17797173942','02','20520752515906551808','2018-12-07 16:04:35','0'),(26,'30520752654427635712','zhangsan','',NULL,'b8ed29901921a8e7a8ec34d874a5379b',NULL,NULL,'0','17797173942','02','20520752654096285696','2018-12-07 16:05:07','0'),(27,'30520753111732600832','lisi','',NULL,'f8b789267d0ea8414dd9dea3f88d2e66',NULL,NULL,'0','17797173942','02','20520753111019569152','2018-12-07 16:06:56','0'),(28,'30521002824033648640','admin','',NULL,'cdfea6fbffcd82758eec12a96c64e682',NULL,NULL,'0','15963245875','00','20521002816139968512','2018-12-08 08:39:13','0'),(29,'30521125069510950912','wuxw','928255095@qq.com',NULL,'cdfea6fbffcd82758eec12a96c64e682',NULL,NULL,'0','17797289806','01','20521692649367814144','2018-12-08 16:44:59','0'),(30,'30521299095785062400','wangym','928255095@qq.com',NULL,'cdfea6fbffcd82758eec12a96c64e682',NULL,NULL,'0','17797289806','01','20521692315283111936','2018-12-09 04:16:29','0'),(31,'30521384632806031360','liyongxi','928255095@qq.com',NULL,'cdfea6fbffcd82758eec12a96c64e682',NULL,NULL,'0','17797289806','01','202019021096440004','2018-12-09 09:56:23','0'),(32,'30521384655744679936','zhangshan','928255095@qq.com',NULL,'cdfea6fbffcd82758eec12a96c64e682',NULL,NULL,'0','17797289806','01','20521384655455272960','2018-12-09 09:56:28','0'),(33,'30521384680415576064','lisi','928255095@qq.com',NULL,'cdfea6fbffcd82758eec12a96c64e682',NULL,NULL,'0','17797289806','01','20521384680210055168','2018-12-09 09:56:34','0'),(34,'30521384773189386240','wangmazi','928255095@qq.com',NULL,'cdfea6fbffcd82758eec12a96c64e682',NULL,NULL,'0','17797289806','01','20521384772962893824','2018-12-09 09:56:56','0'),(35,'30521384859990507520','吴学文','928255095@qq.com',NULL,'cdfea6fbffcd82758eec12a96c64e682',NULL,NULL,'0','17797289806','01','20521384859759820800','2018-12-09 09:57:17','0'),(36,'30521388279069687808','吴学文1','928255095@qq.com',NULL,'cdfea6fbffcd82758eec12a96c64e682',NULL,NULL,'0','17797289806','01','20521388278868361216','2018-12-09 10:10:52','0'),(37,'30521388292680204288','吴学文2','928255095@qq.com',NULL,'cdfea6fbffcd82758eec12a96c64e682',NULL,NULL,'0','17797289806','01','20521388292466294784','2018-12-09 10:10:55','0'),(38,'30521388308752777216','吴学文3','928255095@qq.com',NULL,'cdfea6fbffcd82758eec12a96c64e682',NULL,NULL,'0','17797289806','01','202019020300000003','2018-12-09 10:10:59','1'),(39,'30521388322879193088','吴学文4','928255095@qq.com',NULL,'cdfea6fbffcd82758eec12a96c64e682',NULL,NULL,'0','17797289806','01','20521388322648506368','2018-12-09 10:11:03','0'),(40,'30521388335978004480','吴学文5','928255095@qq.com',NULL,'cdfea6fbffcd82758eec12a96c64e682',NULL,NULL,'0','17797289806','01','20521388335667625984','2018-12-09 10:11:06','0'),(41,'30521470367152226304','王永梅','406943871@qq.com','青海省西宁市城中区申宁路6号','cdfea6fbffcd82758eec12a96c64e682',NULL,NULL,'1','17797173943','01','20521470366867013632','2018-12-09 15:37:04','0'),(42,'30521470728940306432','张三丰','123456@qq.com','','cdfea6fbffcd82758eec12a96c64e682',NULL,NULL,'0','18987898111','01','20521470728747368448','2018-12-09 15:38:30','0'),(43,'30521470972990078976','李玉刚','0908777@qq.com','','cdfea6fbffcd82758eec12a96c64e682',NULL,NULL,'1','18987164563','01','20521470972776169472','2018-12-09 15:39:28','0'),(44,'30521471733694218240','张学良','9088@qq.com','东北老爷们山','cdfea6fbffcd82758eec12a96c64e682',NULL,NULL,'0','17797173945','01','20521471733417394176','2018-12-09 15:42:29','0'),(45,'30521472810921508864','qhadmin','',NULL,'cdfea6fbffcd82758eec12a96c64e682',NULL,NULL,'0','17797173940','00','20521472810674044928','2018-12-09 15:46:46','0'),(46,'30522549147283243008','张事假','928255095@qq.com','','cdfea6fbffcd82758eec12a96c64e682',NULL,NULL,'0','18997089471','01','20522549147056750592','2018-12-12 15:03:45','0'),(47,'30523565516132990976','jiejie','jiejie@qq.com','','cdfea6fbffcd82758eec12a96c64e682',NULL,NULL,'1','17797173948','01','20523565515797446656','2018-12-15 10:22:26','0'),(48,'3020190202000001','吴梓豪','928255094@qq.com','青海省','cdfea6fbffcd82758eec12a96c64e682',NULL,NULL,'0','17797173942','01','202019020200000003','2019-02-02 13:40:27','1'),(49,'3020190210000001','吴学文89','928255095@qq.com',NULL,'cdfea6fbffcd82758eec12a96c64e682',NULL,NULL,'0','17797289807','01','202019021000000009','2019-02-09 16:57:53','0'),(50,'3020190210000002','吴学文90','928255095@qq.com',NULL,'cdfea6fbffcd82758eec12a96c64e682',NULL,NULL,'0','17797289807','01','202019021070180002','2019-02-09 17:17:50','0'),(51,'3020190210000003','吴学文91','928255095@qq.com',NULL,'cdfea6fbffcd82758eec12a96c64e682',NULL,NULL,'0','17797289807','01','202019021058060004','2019-02-09 17:23:16','0'),(52,'302019021030610001','张时强','wuxw7@www.com','甘肃省金昌市','cdfea6fbffcd82758eec12a96c64e682',NULL,NULL,'0','18909789184','01','202019021059530006','2019-02-10 14:37:01','0'),(53,'302019021020990002','cc789','',NULL,'bd06b3bdf498d5fecd1f83964c925750',NULL,NULL,'0','18909871234','00','202019021067580008','2019-02-10 14:38:23','0');
/*!40000 ALTER TABLE `u_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `u_user_address`
--

DROP TABLE IF EXISTS `u_user_address`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `u_user_address`
--

LOCK TABLES `u_user_address` WRITE;
/*!40000 ALTER TABLE `u_user_address` DISABLE KEYS */;
/*!40000 ALTER TABLE `u_user_address` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `u_user_attr`
--

DROP TABLE IF EXISTS `u_user_attr`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `u_user_attr`
--

LOCK TABLES `u_user_attr` WRITE;
/*!40000 ALTER TABLE `u_user_attr` DISABLE KEYS */;
/*!40000 ALTER TABLE `u_user_attr` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `u_user_credentials`
--

DROP TABLE IF EXISTS `u_user_credentials`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `u_user_credentials`
--

LOCK TABLES `u_user_credentials` WRITE;
/*!40000 ALTER TABLE `u_user_credentials` DISABLE KEYS */;
/*!40000 ALTER TABLE `u_user_credentials` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `u_user_tag`
--

DROP TABLE IF EXISTS `u_user_tag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `u_user_tag`
--

LOCK TABLES `u_user_tag` WRITE;
/*!40000 ALTER TABLE `u_user_tag` DISABLE KEYS */;
/*!40000 ALTER TABLE `u_user_tag` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_level`
--

DROP TABLE IF EXISTS `user_level`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_level` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `level_cd` varchar(4) NOT NULL COMMENT '用户级别',
  `name` varchar(50) NOT NULL COMMENT '名称',
  `description` varchar(200) DEFAULT NULL COMMENT '描述',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `level_cd` (`level_cd`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_level`
--

LOCK TABLES `user_level` WRITE;
/*!40000 ALTER TABLE `user_level` DISABLE KEYS */;
INSERT INTO `user_level` VALUES (1,'00','系统管理员','系统管理员','2018-11-14 13:28:59'),(3,'01','员工','员工','2019-02-13 09:03:10'),(4,'02','普通用户','普通用户','2019-02-13 09:04:25');
/*!40000 ALTER TABLE `user_level` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'TT'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2019-02-13 17:28:15