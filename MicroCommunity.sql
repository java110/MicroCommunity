
CREATE DATABASE /*!32312 IF NOT EXISTS*/`TT` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `TT`;

/*Table structure for table `a_agent` */

DROP TABLE IF EXISTS `a_agent`;

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

/*Data for the table `a_agent` */

insert  into `a_agent`(`agent_id`,`b_id`,`name`,`address`,`tel`,`nearby_landmarks`,`map_x`,`map_y`,`create_time`,`status_cd`) values ('8020181223000001','60234567894','方博家园123','青海省西宁市城中区129号123','17797173952','王府井旁40米','102.801909','37.597263','2018-12-23 08:52:58','0');

/*Table structure for table `a_agent_attr` */

DROP TABLE IF EXISTS `a_agent_attr`;

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

/*Data for the table `a_agent_attr` */

insert  into `a_agent_attr`(`b_id`,`attr_id`,`agent_id`,`spec_cd`,`VALUE`,`create_time`,`status_cd`) values ('19234567894','112018122300000002','8020181223000001','1001','02','2018-12-23 08:52:58','1');

/*Table structure for table `a_agent_cerdentials` */

DROP TABLE IF EXISTS `a_agent_cerdentials`;

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

/*Data for the table `a_agent_cerdentials` */

insert  into `a_agent_cerdentials`(`agent_cerdentials_id`,`b_id`,`agent_id`,`credentials_cd`,`value`,`validity_period`,`positive_photo`,`negative_photo`,`create_time`,`status_cd`) values ('8220181223000004','116234567894','8020181223000001','1','632126XXXXXXXX2012','3000-02-01','1234567.jpg','11.jpg','2018-12-23 08:57:11','1');

/*Table structure for table `a_agent_photo` */

DROP TABLE IF EXISTS `a_agent_photo`;

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

/*Data for the table `a_agent_photo` */

insert  into `a_agent_photo`(`agent_photo_id`,`b_id`,`agent_id`,`agent_photo_type_cd`,`photo`,`create_time`,`status_cd`) values ('8120181223000003','95234567894','8020181223000001','T','123456789.jpg','2018-12-23 08:55:17','1');

/*Table structure for table `a_agent_user` */

DROP TABLE IF EXISTS `a_agent_user`;

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

/*Data for the table `a_agent_user` */

insert  into `a_agent_user`(`agent_user_id`,`agent_id`,`b_id`,`user_id`,`rel_cd`,`create_time`,`status_cd`) values ('8320181223000005','8020181223000001','31234567894','123','600311000001','2018-12-23 08:58:41','1');

/*Table structure for table `agent_user_rel` */

DROP TABLE IF EXISTS `agent_user_rel`;

CREATE TABLE `agent_user_rel` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `rel_cd` varchar(12) NOT NULL COMMENT '代理商用户关系编码',
  `name` varchar(50) NOT NULL COMMENT '代理商用户关系编码名称',
  `description` varchar(200) DEFAULT NULL COMMENT '描述',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `rel_cd` (`rel_cd`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `agent_user_rel` */

/*Table structure for table `building_owner` */

DROP TABLE IF EXISTS `building_owner`;

CREATE TABLE `building_owner` (
  `member_id` varchar(30) NOT NULL COMMENT '业主成员ID',
  `owner_id` varchar(30) NOT NULL COMMENT '业主ID',
  `b_id` varchar(30) NOT NULL COMMENT '业务Id',
  `name` varchar(12) NOT NULL COMMENT '业主名称',
  `sex` int(11) NOT NULL COMMENT '性别',
  `age` int(11) NOT NULL COMMENT '年龄',
  `link` varchar(11) NOT NULL COMMENT '联系人手机号',
  `user_id` varchar(30) NOT NULL COMMENT '用户ID',
  `remark` varchar(200) NOT NULL COMMENT '备注',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `status_cd` varchar(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考c_status表，0, 在用 1失效',
  `owner_type_cd` varchar(4) NOT NULL DEFAULT '1001' COMMENT '1001 业主本人 1002 家庭成员',
  UNIQUE KEY `member_id` (`member_id`),
  UNIQUE KEY `idx_owner_id` (`member_id`),
  KEY `idx_owner_b_id` (`b_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `building_owner` */

insert  into `building_owner`(`member_id`,`owner_id`,`b_id`,`name`,`sex`,`age`,`link`,`user_id`,`remark`,`create_time`,`status_cd`,`owner_type_cd`) values ('772019051712520001','772019051712520001','202019051885450002','张三1',1,25,'17797173941','30518940136629616640','test','2019-05-17 14:11:35','0','1001'),('772019051730670001','772019051730670001','202019052646680008','吴学文',0,23,'17797173942','30518940136629616640','','2019-05-17 14:29:39','1','1001'),('772019051807130001','772019051807130001','202019051851950019','吴梓豪',1,2,'17797173940','30518940136629616640','测试','2019-05-17 16:49:05','0','1001'),('772019051824170001','772019051824170001','202019051886350015','张颖',1,32,'18909789999','30518940136629616640','','2019-05-18 10:43:20','0','1001'),('772019051826810003','772019051730670001','202019051966520002','张三安1',1,18,'17797173991','30518940136629616640','','2019-05-18 12:04:49','0','1002'),('772019051853860001','','202019051842650002','找大难',1,25,'17797177777','30518940136629616640','','2019-05-18 08:11:18','0','1001'),('772019051856610003','','202019051887830008','xxx',1,14,'18977777777','30518940136629616640','','2019-05-18 08:19:18','0','1001'),('772019051860030001','772019051860030001','202019051843140008','王茜茜1',1,26,'18909789998','30518940136629616640','1234','2019-05-18 08:32:16','0','1001'),('772019051883280002','','202019051801160005','王大拿',0,25,'17797173444','30518940136629616640','','2019-05-18 08:12:10','0','1001'),('772019051901000001','772019051932200003','202019051983570004','黄晓明',0,40,'13397089678','30518940136629616640','','2019-05-19 01:50:54','0','1002'),('772019051923410001','772019051730670001','202019051966970002','闪粉',1,12,'17797173909','30518940136629616640','','2019-05-19 00:26:56','0','1002'),('772019051932200003','772019051932200003','202019051944770006','皮晓兰',1,30,'18909786789','30518940136629616640','','2019-05-19 00:44:19','0','1001'),('772019051940060006','772019051932200003','202019051971540013','皮皮男',0,13,'17798788881','30518940136629616640','','2019-05-19 00:55:36','0','1002'),('772019051948380005','772019051932200003','202019051901950011','皮皮虾',1,18,'18909789996','30518940136629616640','','2019-05-19 00:51:20','0','1002'),('772019051994430002','772019051730670001','202019051933500004','王霞霞',1,25,'18909789999','30518940136629616640','','2019-05-19 00:27:43','0','1002'),('772019051998120004','772019051932200003','202019051931590002','皮皮',0,3,'17798789001','30518940136629616640','','2019-05-19 00:44:57','1','1002'),('772019052337170001','772019052337170001','202019052616520011','张三',1,888,'13812345678','30518940136629616640','','2019-05-23 06:53:34','0','1001'),('772019052630240001','772019052630240001','202019052616400013','潘杰',1,38,'18888888888','30518940136629616640','皓晨潘杰','2019-05-26 12:50:33','0','1001');

/*Table structure for table `building_owner_room_rel` */

DROP TABLE IF EXISTS `building_owner_room_rel`;

CREATE TABLE `building_owner_room_rel` (
  `rel_id` varchar(30) NOT NULL COMMENT '关系ID',
  `owner_id` varchar(30) NOT NULL COMMENT '业主ID',
  `room_id` varchar(30) NOT NULL COMMENT '房间ID',
  `b_id` varchar(30) NOT NULL COMMENT '业务Id',
  `state` varchar(4) NOT NULL COMMENT '业务状态 2001 业主未迁入 2002 业主迁入 2003 业主迁出',
  `user_id` varchar(30) NOT NULL COMMENT '用户ID',
  `remark` varchar(200) NOT NULL COMMENT '备注',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `status_cd` varchar(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考c_status表，0, 在用 1失效',
  UNIQUE KEY `rel_id` (`rel_id`),
  UNIQUE KEY `idx_borr_rel_id` (`rel_id`),
  UNIQUE KEY `idx_orr_rel_id` (`rel_id`),
  KEY `idx_borr_b_id` (`b_id`),
  KEY `idx_orr_b_id` (`b_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `building_owner_room_rel` */

insert  into `building_owner_room_rel`(`rel_id`,`owner_id`,`room_id`,`b_id`,`state`,`user_id`,`remark`,`create_time`,`status_cd`) values ('12313','123123','752019050811420005','123123','0000','123123','','2019-05-21 02:50:26','1'),('234234','123123','752019050811420005','12313','0000','123213','','2019-05-21 02:51:47','0'),('782019052227190001','772019051824170001','752019050876320004','202019052268080002','2001','30518940136629616640','','2019-05-22 13:36:52','0'),('782019052243940001','772019051824170001','752019050994740002','202019052250200004','2001','30518940136629616640','','2019-05-22 15:21:46','0'),('782019052281900002','772019051824170001','752019050994740002','202019052219400002','2001','30518940136629616640','','2019-05-22 13:38:07','1'),('782019052290990001','772019051730670001','752019050845270003','202019052255290002','2001','30518940136629616640','','2019-05-21 16:30:15','0'),('782019052299880001','772019051932200003','752019050870640002','202019052282950002','2001','30518940136629616640','','2019-05-22 03:53:32','0'),('782019060213970001','772019052630240001','752019050790250003','202019060284310002','2001','30518940136629616640','','2019-06-02 04:48:10','1'),('782019060243470001','772019052630240001','752019050860300001','202019060225140005','2001','30518940136629616640','','2019-06-02 05:22:44','0'),('782019060259310001','772019052630240001','752019050860300001','202019060296240002','2001','30518940136629616640','','2019-06-02 03:40:03','1'),('782019060480000001','772019052337170001','752019050896140002','202019060411750008','2001','30518940136629616640','','2019-06-04 06:57:59','0');

/*Table structure for table `building_room` */

DROP TABLE IF EXISTS `building_room`;

CREATE TABLE `building_room` (
  `room_id` varchar(30) NOT NULL COMMENT '房屋ID',
  `b_id` varchar(30) NOT NULL COMMENT '业务Id',
  `room_num` varchar(12) NOT NULL COMMENT '房屋编号',
  `unit_id` varchar(30) NOT NULL COMMENT '单元ID',
  `layer` int(11) NOT NULL COMMENT '层数',
  `section` int(11) NOT NULL COMMENT '室',
  `apartment` varchar(4) NOT NULL COMMENT '户型',
  `built_up_area` decimal(6,2) NOT NULL COMMENT '建筑面积',
  `unit_price` decimal(12,2) NOT NULL COMMENT '每平米单价',
  `user_id` varchar(30) NOT NULL COMMENT '用户ID',
  `remark` varchar(200) NOT NULL COMMENT '备注',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `status_cd` varchar(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考c_status表，S 保存，0, 在用 1失效',
  `state` varchar(4) NOT NULL COMMENT '房屋状态，如房屋出售等，请查看state 表',
  UNIQUE KEY `room_id` (`room_id`),
  UNIQUE KEY `idx_room_id` (`room_id`),
  KEY `idx_room_b_id` (`b_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `building_room` */

insert  into `building_room`(`room_id`,`b_id`,`room_num`,`unit_id`,`layer`,`section`,`apartment`,`built_up_area`,`unit_price`,`user_id`,`remark`,`create_time`,`status_cd`,`state`) values ('752019050790250003','202019050955970004','1123','742019050304250004',12,2,'1010','95.46','4800.00','30518940136629616640','测试','2019-05-07 15:35:55','0','2002'),('752019050811420005','202019050812510002','1113','742019050396500001',11,3,'2020','95.45','4800.00','30518940136629616640','','2019-05-08 08:57:24','1',''),('752019050814710001','202019051397610016','1123','742019050396500001',12,2,'2020','95.45','4800.00','30518940136629616640','','2019-05-08 06:07:27','1','2001'),('752019050845270003','202019050838100006','1121','742019050396500001',12,2,'2020','87.90','4800.00','30518940136629616640','','2019-05-08 07:40:32','0',''),('752019050856240001','202019050927090006','1113','742019050304250004',11,2,'2020','97.45','4700.00','30518940136629616640','','2019-05-08 05:36:20','0','2003'),('752019050860300001','202019050942230008','1124','742019050304250004',12,3,'2020','124.89','4800.00','30518940136629616640','隔壁老王','2019-05-07 17:55:40','0','2004'),('752019050861590001','202019050801410008','12345','742019050396500001',22,8,'2020','129.99','10000.00','30518940136629616640','','2019-05-08 10:57:30','1',''),('752019050870640002','202019050835720004','1124','742019050396500001',12,2,'2020','97.09','4800.00','30518940136629616640','测试','2019-05-08 07:39:39','0',''),('752019050876320004','202019050858950008','1122','742019050396500001',12,4,'2020','127.98','4800.00','30518940136629616640','','2019-05-08 07:41:21','0',''),('752019050896140002','202019050812980004','1121','742019050304250004',12,2,'2020','92.34','4800.00','30518940136629616640','','2019-05-07 17:57:46','0',''),('752019050906680001','202019050968960002','2121','742019050362220003',12,2,'2020','97.33','4800.00','30518940136629616640','','2019-05-09 06:10:53','0','2001'),('752019050994740002','202019050942660010','2123','742019050362220003',12,2,'2020','97.88','4800.00','30518940136629616640','','2019-05-09 05:29:53','0','');

/*Table structure for table `building_room_attr` */

DROP TABLE IF EXISTS `building_room_attr`;

CREATE TABLE `building_room_attr` (
  `b_id` varchar(30) NOT NULL COMMENT '订单ID',
  `attr_id` varchar(30) NOT NULL COMMENT '属性id',
  `room_id` varchar(30) NOT NULL COMMENT '房屋ID',
  `spec_cd` varchar(12) NOT NULL COMMENT '规格id,参考spec表',
  `value` varchar(50) NOT NULL COMMENT '属性值',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `status_cd` varchar(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考c_status表，S 保存，0, 在用 1失效',
  UNIQUE KEY `attr_id` (`attr_id`),
  KEY `idx_b_attr_b_id` (`b_id`),
  KEY `idx_attr_room_id` (`room_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `building_room_attr` */

/*Table structure for table `building_unit` */

DROP TABLE IF EXISTS `building_unit`;

CREATE TABLE `building_unit` (
  `unit_id` varchar(30) NOT NULL COMMENT '单元ID',
  `b_id` varchar(30) NOT NULL COMMENT '业务Id',
  `unit_num` varchar(12) NOT NULL COMMENT '单元编号',
  `floor_id` varchar(30) NOT NULL COMMENT '楼ID',
  `layer_count` int(11) NOT NULL COMMENT '总层数',
  `lift` varchar(4) NOT NULL COMMENT '是否有电梯 1010有 2020 无',
  `user_id` varchar(30) NOT NULL COMMENT '用户ID',
  `remark` varchar(200) NOT NULL COMMENT '备注',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `status_cd` varchar(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考c_status表，S 保存，0, 在用 1失效',
  UNIQUE KEY `unit_id` (`unit_id`),
  UNIQUE KEY `idx_unit_id` (`unit_id`),
  KEY `idx_unit_b_id` (`b_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `building_unit` */

insert  into `building_unit`(`unit_id`,`b_id`,`unit_num`,`floor_id`,`layer_count`,`lift`,`user_id`,`remark`,`create_time`,`status_cd`) values ('742019050304250004','202019050399040008','001','732019042188750002',33,'1010','30518940136629616640','测试','2019-05-03 09:53:18','0'),('742019050325230001','202019050357280004','001','732019042644990001',33,'1010','30518940136629616640','测试','2019-05-03 11:37:00','0'),('742019050344140006','202019050328930012','002','732019042218110002',33,'1010','30518940136629616640','测试','2019-05-03 09:53:37','0'),('742019050358480002','202019050467640002','002','732019042181450002',33,'1010','30518940136629616640','测试','2019-05-03 09:52:42','1'),('742019050362220003','202019050354360006','002','732019042188750002',33,'1010','30518940136629616640','测试','2019-05-03 09:53:09','0'),('742019050374590007','202019050346780002','0011','732019042644990001',31,'2020','30518940136629616640','测试1','2019-05-03 10:01:33','0'),('742019050389780008','202019050307280016','002','732019042644990001',33,'1010','30518940136629616640','测试','2019-05-03 10:01:49','0'),('742019050396500001','202019050398970002','001','732019042181450002',33,'1010','30518940136629616640','测试','2019-05-03 09:52:13','0'),('742019050398030005','202019050318680010','001','732019042218110002',33,'1010','30518940136629616640','测试','2019-05-03 09:53:30','0'),('742019050466970002','202019050486630010','001','732019042790640002',33,'1010','30518940136629616640','123456','2019-05-04 10:48:39','0'),('742019050493150001','202019050486680006','002','732019042181450002',33,'1010','30518940136629616640','123456','2019-05-04 13:45:47','1'),('742019050497530002','202019050422440008','001','732019042921980002',33,'2020','30518940136629616640','','2019-05-04 13:47:01','0'),('742019050702710001','202019050745870002','001','732019042715940003',33,'1010','30518940136629616640','','2019-05-06 16:08:48','0'),('742019050785380002','202019050722290004','001','732019042773170001',33,'1010','30518940136629616640','','2019-05-06 19:41:42','0');

/*Table structure for table `business_agent` */

DROP TABLE IF EXISTS `business_agent`;

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

/*Data for the table `business_agent` */

insert  into `business_agent`(`agent_id`,`b_id`,`name`,`address`,`tel`,`nearby_landmarks`,`map_x`,`map_y`,`month`,`create_time`,`operate`) values ('8020181223000001','3234567894','方博家园代理商','青海省西宁市城中区129号','17797173942','王府井旁30米','101.801909','36.597263',12,'2018-12-23 08:52:15','ADD'),('8020181223000001','60234567894','方博家园代理商','青海省西宁市城中区129号','17797173942','王府井旁30米','101.801909','36.597263',12,'2018-12-23 09:00:29','DEl'),('8020181223000001','60234567894','方博家园123','青海省西宁市城中区129号123','17797173952','王府井旁40米','102.801909','37.597263',12,'2018-12-23 09:00:29','ADD');

/*Table structure for table `business_agent_attr` */

DROP TABLE IF EXISTS `business_agent_attr`;

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

/*Data for the table `business_agent_attr` */

insert  into `business_agent_attr`(`b_id`,`attr_id`,`agent_id`,`spec_cd`,`value`,`month`,`create_time`,`operate`) values ('3234567894','112018122300000002','8020181223000001','1001','01',12,'2018-12-23 08:52:15','ADD'),('60234567894','112018122300000002','8020181223000001','1001','01',12,'2018-12-23 09:00:29','DEl'),('60234567894','112018122300000002','8020181223000001','1001','02',12,'2018-12-23 09:00:29','ADD'),('19234567894','112018122300000002','8020181223000001','1001','02',12,'2018-12-23 09:07:54','DEl');

/*Table structure for table `business_agent_cerdentials` */

DROP TABLE IF EXISTS `business_agent_cerdentials`;

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

/*Data for the table `business_agent_cerdentials` */

insert  into `business_agent_cerdentials`(`agent_cerdentials_id`,`b_id`,`agent_id`,`credentials_cd`,`value`,`validity_period`,`positive_photo`,`negative_photo`,`month`,`create_time`,`operate`) values ('8220181223000004','5234567894','8020181223000001','1','632126XXXXXXXX2011','3000-01-01','正面照片地址，1234567.jpg','反面照片地址，没有不填写',12,'2018-12-23 08:57:05','ADD'),('8220181223000004','82234567894','8020181223000001','1','632126XXXXXXXX2011','3000-01-01','正面照片地址，1234567.jpg','反面照片地址，没有不填写',12,'2018-12-23 09:03:20','DEl'),('8220181223000004','82234567894','8020181223000001','1','632126XXXXXXXX2012','3000-02-01','1234567.jpg','11.jpg',12,'2018-12-23 09:03:20','ADD'),('8220181223000004','116234567894','8020181223000001','1','632126XXXXXXXX2012','3000-02-01','1234567.jpg','11.jpg',12,'2018-12-23 09:05:50','DEl');

/*Table structure for table `business_agent_photo` */

DROP TABLE IF EXISTS `business_agent_photo`;

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

/*Data for the table `business_agent_photo` */

insert  into `business_agent_photo`(`agent_photo_id`,`b_id`,`agent_id`,`agent_photo_type_cd`,`photo`,`month`,`create_time`,`operate`) values ('8120181223000003','4234567894','8020181223000001','T','12345678.jpg',12,'2018-12-23 08:55:10','ADD'),('8120181223000003','71234567894','8020181223000001','T','12345678.jpg',12,'2018-12-23 09:01:58','DEl'),('8120181223000003','71234567894','8020181223000001','T','123456789.jpg',12,'2018-12-23 09:01:58','ADD'),('8120181223000003','95234567894','8020181223000001','T','123456789.jpg',12,'2018-12-23 09:04:36','DEl');

/*Table structure for table `business_agent_user` */

DROP TABLE IF EXISTS `business_agent_user`;

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

/*Data for the table `business_agent_user` */

insert  into `business_agent_user`(`agent_user_id`,`agent_id`,`b_id`,`user_id`,`rel_cd`,`month`,`create_time`,`operate`) values ('8320181223000005','8020181223000001','5234567894','123','600311000001',12,'2018-12-23 08:58:34','ADD'),('8320181223000005','8020181223000001','31234567894','123','600311000001',12,'2018-12-23 09:06:55','DEl');

/*Table structure for table `business_building_owner` */

DROP TABLE IF EXISTS `business_building_owner`;

CREATE TABLE `business_building_owner` (
  `member_id` varchar(30) NOT NULL COMMENT '业主成员ID',
  `owner_id` varchar(30) NOT NULL COMMENT '业主ID',
  `b_id` varchar(30) NOT NULL COMMENT '业务Id',
  `name` varchar(12) NOT NULL COMMENT '业主名称',
  `sex` int(11) NOT NULL COMMENT '性别',
  `age` int(11) NOT NULL COMMENT '年龄',
  `link` varchar(11) NOT NULL COMMENT '联系人手机号',
  `user_id` varchar(30) NOT NULL COMMENT '用户ID',
  `remark` varchar(200) NOT NULL COMMENT '备注',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `operate` varchar(4) NOT NULL COMMENT '数据状态，添加ADD，修改MOD 删除DEL',
  `owner_type_cd` varchar(4) NOT NULL DEFAULT '1001' COMMENT '1001 业主本人 1002 家庭成员',
  KEY `idx_business_owner_id` (`member_id`),
  KEY `idx_business_owner_b_id` (`b_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `business_building_owner` */

insert  into `business_building_owner`(`member_id`,`owner_id`,`b_id`,`name`,`sex`,`age`,`link`,`user_id`,`remark`,`create_time`,`operate`,`owner_type_cd`) values ('772019051712520001','772019051712520001','202019051796390002','张三',0,24,'17797173942','30518940136629616640','','2019-05-17 14:11:32','ADD','1001'),('772019051730670001','772019051730670001','202019051798740002','吴学文',0,23,'17797173942','30518940136629616640','','2019-05-17 14:29:38','ADD','1001'),('772019051712520001','772019051712520001','202019051885450002','张三',0,24,'17797173942','30518940136629616640','','2019-05-17 16:48:19','DEl','1001'),('772019051712520001','772019051712520001','202019051885450002','张三1',1,25,'17797173941','30518940136629616640','test','2019-05-17 16:48:19','ADD','1001'),('772019051807130001','772019051807130001','202019051880120004','吴梓豪',0,2,'17797173940','30518940136629616640','测试','2019-05-17 16:49:04','ADD','1001'),('772019051888960001','','202019051839700002','找大难',1,25,'17797177777','30518940136629616640','','2019-05-18 07:59:27','ADD','1001'),('772019051839350002','','202019051872120006','找大难',1,25,'17797177777','30518940136629616640','','2019-05-18 08:01:18','ADD','1001'),('772019051853860001','','202019051842650002','找大难',1,25,'17797177777','30518940136629616640','','2019-05-18 08:11:15','ADD','1001'),('772019051883280002','','202019051801160005','王大拿',0,25,'17797173444','30518940136629616640','','2019-05-18 08:12:10','ADD','1001'),('772019051856610003','','202019051887830008','xxx',1,14,'18977777777','30518940136629616640','','2019-05-18 08:19:18','ADD','1001'),('772019051860030001','772019051860030001','202019051888940002','王茜茜',1,24,'18909789999','30518940136629616640','','2019-05-18 08:32:15','ADD','1001'),('772019051860030001','772019051860030001','202019051870570002','王茜茜',1,24,'18909789999','30518940136629616640','','2019-05-18 10:36:24','DEl','1001'),('772019051860030001','772019051860030001','202019051870570002','王茜茜1',0,25,'18909789998','30518940136629616640','123','2019-05-18 10:36:24','ADD','1001'),('772019051860030001','772019051860030001','202019051863710004','王茜茜1',1,25,'18909789998','30518940136629616640','123','2019-05-18 10:36:51','DEl','1001'),('772019051860030001','772019051860030001','202019051863710004','王茜茜1',0,25,'18909789998','30518940136629616640','123','2019-05-18 10:36:51','ADD','1001'),('772019051860030001','772019051860030001','202019051837120006','王茜茜1',1,25,'18909789998','30518940136629616640','123','2019-05-18 10:38:34','DEl','1001'),('772019051860030001','772019051860030001','202019051837120006','王茜茜1',1,26,'18909789998','30518940136629616640','1234','2019-05-18 10:38:34','ADD','1001'),('772019051860030001','772019051860030001','202019051843140008','王茜茜1',1,26,'18909789998','30518940136629616640','1234','2019-05-18 10:40:29','DEl','1001'),('772019051860030001','772019051860030001','202019051843140008','王茜茜1',0,26,'18909789998','30518940136629616640','1234','2019-05-18 10:40:29','ADD','1001'),('772019051807130001','772019051807130001','202019051811350010','吴梓豪',0,2,'17797173940','30518940136629616640','测试','2019-05-18 10:42:05','DEl','1001'),('772019051807130001','772019051807130001','202019051811350010','吴梓豪',1,2,'17797173940','30518940136629616640','测试','2019-05-18 10:42:05','ADD','1001'),('772019051824170001','772019051824170001','202019051864800012','张颖',1,32,'18909789999','30518940136629616640','','2019-05-18 10:43:19','ADD','1001'),('772019051824170001','772019051824170001','202019051886350015','张颖',1,32,'18909789999','30518940136629616640','','2019-05-18 10:43:33','DEl','1001'),('772019051824170001','772019051824170001','202019051886350015','张颖',0,32,'18909789999','30518940136629616640','','2019-05-18 10:43:34','ADD','1001'),('772019051807130001','772019051807130001','202019051873410017','吴梓豪',1,2,'17797173940','30518940136629616640','测试','2019-05-18 10:43:45','DEl','1001'),('772019051807130001','772019051807130001','202019051873410017','吴梓豪',0,2,'17797173940','30518940136629616640','测试','2019-05-18 10:43:45','ADD','1001'),('772019051807130001','772019051807130001','202019051851950019','吴梓豪',1,2,'17797173940','30518940136629616640','测试','2019-05-18 10:44:57','DEl','1001'),('772019051807130001','772019051807130001','202019051851950019','吴梓豪',0,2,'17797173940','30518940136629616640','测试','2019-05-18 10:44:57','ADD','1001'),('772019051826810003','772019051730670001','202019051830060026','张三安',0,19,'17797173999','30518940136629616640','','2019-05-18 12:04:49','ADD','1002'),('772019051923410001','772019051730670001','202019051966970002','闪粉',1,12,'17797173909','30518940136629616640','','2019-05-19 00:26:56','ADD','1002'),('772019051994430002','772019051730670001','202019051933500004','王霞霞',1,25,'18909789999','30518940136629616640','','2019-05-19 00:27:43','ADD','1002'),('772019051932200003','772019051932200003','202019051944770006','皮晓兰',1,30,'18909786789','30518940136629616640','','2019-05-19 00:44:18','ADD','1001'),('772019051998120004','772019051932200003','202019051916160009','皮皮',0,3,'17798789001','30518940136629616640','','2019-05-19 00:44:57','ADD','1002'),('772019051948380005','772019051932200003','202019051901950011','皮皮虾',1,18,'18909789996','30518940136629616640','','2019-05-19 00:51:20','ADD','1002'),('772019051940060006','772019051932200003','202019051971540013','皮皮男',0,13,'17798788881','30518940136629616640','','2019-05-19 00:55:36','ADD','1002'),('772019051826810003','772019051730670001','202019051966520002','张三安',0,19,'17797173999','30518940136629616640','','2019-05-19 01:20:46','DEl','1002'),('772019051826810003','772019051730670001','202019051966520002','张三安1',1,18,'17797173991','30518940136629616640','','2019-05-19 01:20:46','ADD','1002'),('772019051998120004','772019051932200003','202019051931590002','皮皮',0,3,'17798789001','30518940136629616640','','2019-05-19 01:50:27','DEl','1002'),('772019051901000001','772019051932200003','202019051983570004','黄晓明',0,40,'13397089678','30518940136629616640','','2019-05-19 01:50:54','ADD','1002'),('772019052337170001','772019052337170001','202019052308670006','张三',0,88,'13812345678','30518940136629616640','','2019-05-23 06:53:33','ADD','1001'),('772019052337170001','772019052337170001','202019052370090009','张三',0,88,'13812345678','30518940136629616640','','2019-05-23 06:53:57','DEl','1001'),('772019052337170001','772019052337170001','202019052370090009','张三',0,888,'13812345678','30518940136629616640','','2019-05-23 06:53:57','ADD','1001'),('772019051730670001','772019051730670001','202019052646680008','吴学文',0,23,'17797173942','30518940136629616640','','2019-05-26 12:49:58','DEl','1001'),('772019052337170001','772019052337170001','202019052616520011','张三',0,888,'13812345678','30518940136629616640','','2019-05-26 12:50:07','DEl','1001'),('772019052337170001','772019052337170001','202019052616520011','张三',1,888,'13812345678','30518940136629616640','','2019-05-26 12:50:07','ADD','1001'),('772019052630240001','772019052630240001','202019052616400013','潘杰',1,38,'18888888888','30518940136629616640','皓晨潘杰','2019-05-26 12:50:33','ADD','1001');

/*Table structure for table `business_building_owner_room_rel` */

DROP TABLE IF EXISTS `business_building_owner_room_rel`;

CREATE TABLE `business_building_owner_room_rel` (
  `rel_id` varchar(30) NOT NULL COMMENT '关系ID',
  `owner_id` varchar(30) NOT NULL COMMENT '业主ID',
  `room_id` varchar(30) NOT NULL COMMENT '房间ID',
  `b_id` varchar(30) NOT NULL COMMENT '业务Id',
  `state` varchar(4) NOT NULL COMMENT '业务状态  2001 业主未迁入 2002 业主迁入 2003 业主迁出',
  `user_id` varchar(30) NOT NULL COMMENT '用户ID',
  `remark` varchar(200) NOT NULL COMMENT '备注',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `operate` varchar(4) NOT NULL COMMENT '数据状态，添加ADD，修改MOD 删除DEL',
  KEY `idx_bborrl_owner_id` (`rel_id`),
  KEY `idx_bborrl_b_id` (`b_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `business_building_owner_room_rel` */

insert  into `business_building_owner_room_rel`(`rel_id`,`owner_id`,`room_id`,`b_id`,`state`,`user_id`,`remark`,`create_time`,`operate`) values ('782019052290990001','772019051730670001','752019050845270003','202019052255290002','2001','30518940136629616640','','2019-05-21 16:30:15','ADD'),('782019052299880001','772019051932200003','752019050870640002','202019052282950002','2001','30518940136629616640','','2019-05-22 03:53:32','ADD'),('782019052227190001','772019051824170001','752019050876320004','202019052268080002','2001','30518940136629616640','','2019-05-22 13:36:51','ADD'),('782019052281900002','772019051824170001','752019050994740002','202019052296630004','2001','30518940136629616640','','2019-05-22 13:38:07','ADD'),('782019052281900002','772019051824170001','752019050994740002','202019052219400002','2001','30518940136629616640','','2019-05-22 15:19:05','DEl'),('782019052243940001','772019051824170001','752019050994740002','202019052250200004','2001','30518940136629616640','','2019-05-22 15:21:46','ADD'),('782019060259310001','772019052630240001','752019050860300001','202019060248010008','2001','30518940136629616640','','2019-06-02 03:40:03','ADD'),('782019060259310001','772019052630240001','752019050860300001','202019060296240002','2001','30518940136629616640','','2019-06-02 04:14:22','DEl'),('782019060292060001','772019052630240001','752019050906680001','202019060203320004','2001','30518940136629616640','','2019-06-02 04:15:12','ADD'),('782019060213970001','772019052630240001','752019050790250003','202019060241450002','2001','30518940136629616640','','2019-06-02 04:48:07','ADD'),('782019060213970001','772019052630240001','752019050790250003','202019060233250002','2001','30518940136629616640','','2019-06-02 05:02:16','DEl'),('782019060213970001','772019052630240001','752019050790250003','202019060284310002','2001','30518940136629616640','','2019-06-02 05:21:18','DEl'),('782019060243470001','772019052630240001','752019050860300001','202019060225140005','2001','30518940136629616640','','2019-06-02 05:22:43','ADD'),('782019060480000001','772019052337170001','752019050896140002','202019060411750008','2001','30518940136629616640','','2019-06-04 06:57:58','ADD');

/*Table structure for table `business_building_room` */

DROP TABLE IF EXISTS `business_building_room`;

CREATE TABLE `business_building_room` (
  `room_id` varchar(30) NOT NULL COMMENT '房屋ID',
  `b_id` varchar(30) NOT NULL COMMENT '业务Id',
  `room_num` varchar(12) NOT NULL COMMENT '房屋编号',
  `unit_id` varchar(30) NOT NULL COMMENT '单元ID',
  `layer` int(11) NOT NULL COMMENT '层数',
  `section` int(11) NOT NULL COMMENT '室',
  `apartment` varchar(4) NOT NULL COMMENT '户型',
  `built_up_area` decimal(6,2) NOT NULL COMMENT '建筑面积',
  `unit_price` decimal(12,2) NOT NULL COMMENT '每平米单价',
  `user_id` varchar(30) NOT NULL COMMENT '用户ID',
  `remark` varchar(200) NOT NULL COMMENT '备注',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `operate` varchar(4) NOT NULL COMMENT '数据状态，添加ADD，修改MOD 删除DEL',
  `state` varchar(4) NOT NULL COMMENT '房屋状态，如房屋出售等，请查看state 表',
  KEY `idx_business_room_id` (`room_id`),
  KEY `idx_business_room_b_id` (`b_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `business_building_room` */

insert  into `business_building_room`(`room_id`,`b_id`,`room_num`,`unit_id`,`layer`,`section`,`apartment`,`built_up_area`,`unit_price`,`user_id`,`remark`,`create_time`,`operate`,`state`) values ('752019050790250003','202019050762320006','1123','742019050304250004',12,2,'1010','95.46','4800.00','30518940136629616640','测试','2019-05-07 15:35:55','ADD',''),('752019050860300001','202019050890510002','1124','742019050304250004',12,3,'2020','124.89','4800.00','30518940136629616640','隔壁老王','2019-05-07 17:55:40','ADD',''),('752019050896140002','202019050812980004','1121','742019050304250004',12,2,'2020','92.34','4800.00','30518940136629616640','','2019-05-07 17:57:46','ADD',''),('752019050856240001','202019050822640002','1113','742019050304250004',11,2,'2020','97.45','4700.00','30518940136629616640','','2019-05-08 05:36:19','ADD',''),('752019050814710001','202019050893350002','1123','742019050396500001',12,2,'2020','95.45','4800.00','30518940136629616640','','2019-05-08 06:07:27','ADD',''),('752019050870640002','202019050835720004','1124','742019050396500001',12,2,'2020','97.09','4800.00','30518940136629616640','测试','2019-05-08 07:39:39','ADD',''),('752019050845270003','202019050838100006','1121','742019050396500001',12,2,'2020','87.90','4800.00','30518940136629616640','','2019-05-08 07:40:32','ADD',''),('752019050876320004','202019050858950008','1122','742019050396500001',12,4,'2020','127.98','4800.00','30518940136629616640','','2019-05-08 07:41:21','ADD',''),('752019050811420005','202019050893040010','1123','742019050396500001',12,2,'2020','95.45','4800.00','30518940136629616640','','2019-05-08 08:57:24','ADD',''),('752019050811420005','202019050857790002','1123','742019050396500001',12,2,'2020','95.45','4800.00','30518940136629616640','','2019-05-08 10:05:44','DEl',''),('752019050811420005','202019050857790002','1113','742019050396500001',11,2,'2020','95.45','4800.00','30518940136629616640','','2019-05-08 10:05:44','ADD',''),('752019050811420005','202019050823880004','1113','742019050396500001',11,2,'2020','95.45','4800.00','30518940136629616640','','2019-05-08 10:14:57','DEl',''),('752019050811420005','202019050823880004','1113','742019050396500001',11,3,'2020','95.45','4800.00','30518940136629616640','','2019-05-08 10:14:57','ADD',''),('752019050811420005','202019050812510002','1113','742019050396500001',11,3,'2020','95.45','4800.00','30518940136629616640','','2019-05-08 10:40:34','DEl',''),('752019050861590001','202019050853120004','123456','742019050396500001',22,8,'2020','129.99','10000.00','30518940136629616640','','2019-05-08 10:57:30','ADD',''),('752019050861590001','202019050847810006','123456','742019050396500001',22,8,'2020','129.99','10000.00','30518940136629616640','','2019-05-08 10:57:52','DEl',''),('752019050861590001','202019050847810006','12345','742019050396500001',22,8,'2020','129.99','10000.00','30518940136629616640','','2019-05-08 10:57:52','ADD',''),('752019050861590001','202019050801410008','12345','742019050396500001',22,8,'2020','129.99','10000.00','30518940136629616640','','2019-05-08 10:57:57','DEl',''),('752019050994740002','202019050942660010','2123','742019050362220003',12,2,'2020','97.88','4800.00','30518940136629616640','','2019-05-09 05:29:53','ADD',''),('752019050906680001','202019050968960002','2121','742019050362220003',12,2,'2020','97.33','4800.00','30518940136629616640','','2019-05-09 06:10:53','ADD','2001'),('752019050790250003','202019050955970004','1123','742019050304250004',12,2,'1010','95.46','4800.00','30518940136629616640','测试','2019-05-09 06:23:12','DEl',''),('752019050790250003','202019050955970004','1123','742019050304250004',12,2,'1010','95.46','4800.00','30518940136629616640','测试','2019-05-09 06:23:12','ADD','2002'),('752019050856240001','202019050927090006','1113','742019050304250004',11,2,'2020','97.45','4700.00','30518940136629616640','','2019-05-09 06:23:26','DEl',''),('752019050856240001','202019050927090006','1113','742019050304250004',11,2,'2020','97.45','4700.00','30518940136629616640','','2019-05-09 06:23:26','ADD','2003'),('752019050860300001','202019050942230008','1124','742019050304250004',12,3,'2020','124.89','4800.00','30518940136629616640','隔壁老王','2019-05-09 06:24:05','DEl',''),('752019050860300001','202019050942230008','1124','742019050304250004',12,3,'2020','124.89','4800.00','30518940136629616640','隔壁老王','2019-05-09 06:24:05','ADD','2004'),('752019050814710001','202019051032590010','1123','742019050396500001',12,2,'2020','95.45','4800.00','30518940136629616640','','2019-05-10 05:55:35','DEl',''),('752019050814710001','202019051032590010','1123','742019050396500001',12,2,'2020','95.45','4800.00','30518940136629616640','','2019-05-10 05:55:35','ADD','2001'),('752019050814710001','202019051397610016','1123','742019050396500001',12,2,'2020','95.45','4800.00','30518940136629616640','','2019-05-13 09:54:39','DEl','2001');

/*Table structure for table `business_building_room_attr` */

DROP TABLE IF EXISTS `business_building_room_attr`;

CREATE TABLE `business_building_room_attr` (
  `b_id` varchar(30) NOT NULL COMMENT '订单ID',
  `attr_id` varchar(30) NOT NULL COMMENT '属性id',
  `room_id` varchar(30) NOT NULL COMMENT '用户ID',
  `spec_cd` varchar(12) NOT NULL COMMENT '规格id,参考spec表',
  `value` varchar(50) NOT NULL COMMENT '属性值',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `operate` varchar(3) NOT NULL COMMENT '数据状态，添加ADD，修改MOD 删除DEL',
  KEY `idx_business_b_room_attr` (`room_id`),
  KEY `idx_business_b_room_attr_b_id` (`b_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `business_building_room_attr` */

/*Table structure for table `business_building_unit` */

DROP TABLE IF EXISTS `business_building_unit`;

CREATE TABLE `business_building_unit` (
  `unit_id` varchar(30) NOT NULL COMMENT '单元ID',
  `b_id` varchar(30) NOT NULL COMMENT '业务Id',
  `unit_num` varchar(12) NOT NULL COMMENT '单元编号',
  `floor_id` varchar(30) NOT NULL COMMENT '楼ID',
  `layer_count` int(11) NOT NULL COMMENT '总层数',
  `lift` varchar(4) NOT NULL COMMENT '是否有电梯 1010有 2020 无',
  `user_id` varchar(30) NOT NULL COMMENT '用户ID',
  `remark` varchar(200) NOT NULL COMMENT '备注',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `operate` varchar(4) NOT NULL COMMENT '数据状态，添加ADD，修改MOD 删除DEL',
  KEY `idx_business_unit_id` (`unit_id`),
  KEY `idx_business_unit_b_id` (`b_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `business_building_unit` */

insert  into `business_building_unit`(`unit_id`,`b_id`,`unit_num`,`floor_id`,`layer_count`,`lift`,`user_id`,`remark`,`create_time`,`operate`) values ('742019050396500001','202019050398970002','001','732019042181450002',33,'1010','30518940136629616640','测试','2019-05-03 09:52:12','ADD'),('742019050358480002','202019050342470004','002','732019042181450002',33,'1010','30518940136629616640','测试','2019-05-03 09:52:42','ADD'),('742019050362220003','202019050354360006','002','732019042188750002',33,'1010','30518940136629616640','测试','2019-05-03 09:53:09','ADD'),('742019050304250004','202019050399040008','001','732019042188750002',33,'1010','30518940136629616640','测试','2019-05-03 09:53:18','ADD'),('742019050398030005','202019050318680010','001','732019042218110002',33,'1010','30518940136629616640','测试','2019-05-03 09:53:30','ADD'),('742019050344140006','202019050328930012','002','732019042218110002',33,'1010','30518940136629616640','测试','2019-05-03 09:53:37','ADD'),('742019050374590007','202019050395750014','001','732019042644990001',33,'1010','30518940136629616640','测试','2019-05-03 10:01:33','ADD'),('742019050389780008','202019050307280016','002','732019042644990001',33,'1010','30518940136629616640','测试','2019-05-03 10:01:49','ADD'),('742019050374590007','202019050375210002','001','732019042644990001',33,'1010','30518940136629616640','测试','2019-05-03 11:06:58','DEl'),('742019050374590007','202019050375210002','0011','732019042644990001',31,'2020','30518940136629616640','测试1','2019-05-03 11:06:58','ADD'),('742019050374590007','202019050346780002','001','732019042644990001',33,'1010','30518940136629616640','测试','2019-05-03 11:36:42','DEl'),('742019050374590007','202019050346780002','0011','732019042644990001',31,'2020','30518940136629616640','测试1','2019-05-03 11:36:42','ADD'),('742019050325230001','202019050357280004','001','732019042644990001',33,'1010','30518940136629616640','测试','2019-05-03 11:37:00','ADD'),('742019050466970002','202019050474860006','001','732019042790640002',33,'1010','30518940136629616640','123456','2019-05-04 10:48:39','ADD'),('742019050466970002','202019050470920008','001','732019042790640002',33,'1010','30518940136629616640','123456','2019-05-04 10:48:57','DEl'),('742019050466970002','202019050470920008','002','732019042790640002',33,'1010','30518940136629616640','123456','2019-05-04 10:48:57','ADD'),('742019050466970002','202019050486630010','002','732019042790640002',33,'1010','30518940136629616640','123456','2019-05-04 10:49:06','DEl'),('742019050466970002','202019050486630010','001','732019042790640002',33,'1010','30518940136629616640','123456','2019-05-04 10:49:06','ADD'),('742019050358480002','202019050467640002','002','732019042181450002',33,'1010','30518940136629616640','测试','2019-05-04 13:24:57','DEl'),('742019050493150001','202019050497010002','002','732019042181450002',33,'1010','30518940136629616640','','2019-05-04 13:45:46','ADD'),('742019050493150001','202019050441380004','002','732019042181450002',33,'1010','30518940136629616640','','2019-05-04 13:45:57','DEl'),('742019050493150001','202019050441380004','002','732019042181450002',33,'1010','30518940136629616640','123456','2019-05-04 13:45:57','ADD'),('742019050493150001','202019050486680006','002','732019042181450002',33,'1010','30518940136629616640','123456','2019-05-04 13:46:08','DEl'),('742019050497530002','202019050422440008','001','732019042921980002',33,'2020','30518940136629616640','','2019-05-04 13:47:01','ADD'),('742019050697580001','202019050638030002','002','732019042181450002',33,'1010','30518940136629616640','','2019-05-06 02:16:50','ADD'),('742019050683430002','202019050656610005','002','732019042181450002',33,'1010','30518940136629616640','','2019-05-06 02:17:02','ADD'),('742019050702710001','202019050745870002','001','732019042715940003',33,'1010','30518940136629616640','','2019-05-06 16:08:48','ADD'),('742019050785380002','202019050722290004','001','732019042773170001',33,'1010','30518940136629616640','','2019-05-06 19:41:42','ADD');

/*Table structure for table `business_community` */

DROP TABLE IF EXISTS `business_community`;

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

/*Data for the table `business_community` */

insert  into `business_community`(`community_id`,`b_id`,`name`,`address`,`city_code`,`nearby_landmarks`,`map_x`,`map_y`,`month`,`create_time`,`operate`) values ('702019051474290001','1234567891','万博家博园（城西区）','青海省西宁市城中区129号','100010','王府井旁30米','101.801909','36.597263',5,'2019-05-14 09:19:04','ADD'),('702019051443120001','22234567891','万博家博园（城南区）','青海省西宁市城中区129号','100010','王府井旁30米','101.801909','36.597263',5,'2019-05-14 09:24:07','ADD'),('702019051551820001','3234567891','万博家博园（城中区）','青海省西宁市城中区129号','100010','王府井旁30米','101.801909','36.597263',5,'2019-05-15 12:49:49','ADD'),('702019051695520001','42234567891','万博家博园（城V区）','青海省西宁市城中区129号','100010','王府井旁30米','101.801909','36.597263',5,'2019-05-16 01:48:17','ADD'),('702019051650000005','62234567891','万博家博园（城V区）','青海省西宁市城中区129号','100010','王府井旁30米','101.801909','36.597263',5,'2019-05-16 02:05:02','ADD'),('702019051687020001','72234567891','万博家博园（城V区）','青海省西宁市城中区129号','100010','王府井旁30米','101.801909','36.597263',5,'2019-05-16 02:10:55','ADD'),('702019051657250007','92234567891','万博家博园（城V区）','青海省西宁市城中区129号','100010','王府井旁30米','101.801909','36.597263',5,'2019-05-16 02:12:04','ADD'),('702019051666020001','92234567895','万博家博园（城V区）','青海省西宁市城中区129号','100010','王府井旁30米','101.801909','36.597263',5,'2019-05-16 02:34:37','ADD'),('702019051696930001','92234567897','万博家博园（城V区）','青海省西宁市城中区129号','100010','王府井旁30米','101.801909','36.597263',5,'2019-05-16 02:43:26','ADD'),('702019051651830001','92234567898','万博家博园（城H区）','青海省西宁市城中区129号','100010','王府井旁30米','101.801909','36.597263',5,'2019-05-16 02:50:55','ADD'),('7020181217000001','1234567890','方博家园','青海省西宁市城中区129号','100010','王府井旁30米','101.801909','36.597263',12,'2018-12-17 04:52:55','ADD'),('7020181217000001','1234567891','方博家园','青海省西宁市城中区129号','100010','王府井旁30米','101.801909','36.597263',12,'2018-12-17 05:06:14','DEl'),('7020181217000001','1234567891','万博家博园（城西区）','青海省西宁市城中区129号','100010','王府井旁30米','101.801909','36.597263',12,'2018-12-17 05:06:14','ADD');

/*Table structure for table `business_community_attr` */

DROP TABLE IF EXISTS `business_community_attr`;

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
  KEY `idx_business_community_attr_b_id` (`b_id`),
  KEY `idx_business_b_room_attr_b_id` (`b_id`)
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

/*Data for the table `business_community_attr` */

insert  into `business_community_attr`(`b_id`,`attr_id`,`community_id`,`spec_cd`,`value`,`month`,`create_time`,`operate`) values ('72234567891','112019051689480002','702019051687020001','1001','01',5,'2019-05-16 02:10:55','ADD'),('82234567891','112019051609710005','702019051621670004','1001','01',5,'2019-05-16 02:11:01','ADD'),('92234567891','112019051685870008','702019051657250007','1001','01',5,'2019-05-16 02:12:04','ADD'),('92234567898','112019051651830002','702019051651830001','1001','01',5,'2019-05-16 02:50:55','ADD'),('1234567890','112018121700000002','7020181217000001','1001','01',12,'2018-12-17 04:52:55','ADD'),('1234567891','112018121700000002','7020181217000001','1001','01',12,'2018-12-17 05:06:15','DEl'),('1234567891','112018121700000002','7020181217000001','1001','01',12,'2018-12-17 05:06:15','ADD');

/*Table structure for table `business_community_member` */

DROP TABLE IF EXISTS `business_community_member`;

CREATE TABLE `business_community_member` (
  `community_member_id` varchar(30) NOT NULL COMMENT 'ID',
  `b_id` varchar(30) NOT NULL COMMENT '业务Id',
  `community_id` varchar(30) NOT NULL COMMENT '小区ID',
  `member_id` varchar(50) NOT NULL COMMENT '成员ID',
  `member_type_cd` varchar(12) NOT NULL COMMENT '成员类型见 community_member_type表',
  `month` int(11) NOT NULL COMMENT '月份',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `operate` varchar(3) NOT NULL COMMENT '数据状态，添加ADD，修改MOD 删除DEL',
  `audit_status_cd` varchar(4) NOT NULL DEFAULT '0000' COMMENT '审核状态',
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

/*Data for the table `business_community_member` */

insert  into `business_community_member`(`community_member_id`,`b_id`,`community_id`,`member_id`,`member_type_cd`,`month`,`create_time`,`operate`,`audit_status_cd`) values ('722019041852540001','202019041834510002','7020181217000002','402019032924930007','390001200002',4,'2019-04-18 04:03:08','ADD','0000'),('722019041887470002','202019041891220004','7020181217000002','402019032924930007','390001200002',4,'2019-04-18 07:09:21','ADD','0000'),('7220181217000001','202019041834430002','7020181217000001','402019032924930007','390001200002',4,'2019-04-18 15:31:02','DEl','0000'),('7220181217000001','202019041884620004','7020181217000001','402019032924930007','390001200002',4,'2019-04-18 15:39:27','DEl','0000'),('7220181217000001','202019041850040006','7020181217000001','402019032924930007','390001200002',4,'2019-04-18 15:44:55','DEl','0000'),('722019042647500001','202019042632530003','7020181217000001','732019042665380001','390001200004',4,'2019-04-26 08:34:30','ADD','0000'),('722019042683690002','202019042646640006','7020181217000001','732019042604640002','390001200004',4,'2019-04-26 08:37:11','ADD','0000'),('722019042658320003','202019042644750009','7020181217000001','732019042622070003','390001200004',4,'2019-04-26 08:45:15','ADD','0000'),('722019042695080004','202019042644760012','7020181217000001','732019042656310004','390001200004',4,'2019-04-26 08:57:16','ADD','0000'),('722019042615080005','202019042641830015','7020181217000001','732019042657550005','390001200004',4,'2019-04-26 08:59:41','ADD','0000'),('722019042626080001','202019042640630003','7020181217000001','732019042607010001','390001200004',4,'2019-04-26 09:14:11','ADD','0000'),('722019042667870002','202019042653890006','7020181217000001','732019042653170002','390001200004',4,'2019-04-26 09:14:59','ADD','0000'),('722019042619710003','202019042649120009','7020181217000001','732019042653880003','390001200004',4,'2019-04-26 09:22:08','ADD','0000'),('722019042699390004','202019042655970012','7020181217000001','732019042687750004','390001200004',4,'2019-04-26 09:24:55','ADD','0000'),('722019042613970001','202019042677430003','7020181217000001','732019042644990001','390001200004',4,'2019-04-26 09:53:42','ADD','0000'),('722019042782690001','202019042723970002','7020181217000002','402019032924930007','390001200002',4,'2019-04-26 16:59:00','ADD','1000'),('722019042708930002','202019042776600004','7020181217000002','402019032924930007','390001200002',4,'2019-04-26 16:59:32','ADD','1000'),('722019042788240003','202019042799620006','7020181217000002','402019032924930007','390001200002',4,'2019-04-26 17:05:00','ADD','1000'),('722019042737600001','202019042750320003','7020181217000001','732019042773170001','390001200004',4,'2019-04-26 17:30:55','ADD','0000'),('722019042774370002','202019042735920006','7020181217000001','732019042790640002','390001200004',4,'2019-04-26 17:37:14','ADD','0000'),('722019042735900003','202019042785490009','7020181217000001','732019042715940003','390001200004',4,'2019-04-26 17:38:57','ADD','0000'),('722019042785690004','202019042717160014','7020181217000001','732019042784130004','390001200004',4,'2019-04-27 11:56:56','ADD','0000'),('722019042851120001','202019042875350007','7020181217000001','732019042893370001','390001200004',4,'2019-04-28 09:53:54','ADD','0000'),('722019042851120001','202019042988310003','7020181217000001','732019042893370001','390001200004',4,'2019-04-28 17:17:10','DEl','0000'),('722019042956360001','202019042922430006','7020181217000001','732019042937930001','390001200004',4,'2019-04-28 17:19:24','ADD','0000'),('722019042956360001','202019042912540011','7020181217000001','732019042937930001','390001200004',4,'2019-04-28 17:19:44','DEl','0000'),('722019042953950001','202019042971000003','7020181217000001','732019042908600001','390001200004',4,'2019-04-29 09:14:52','ADD','0000'),('722019042922660002','202019042921120006','7020181217000001','732019042921980002','390001200004',4,'2019-04-29 09:21:46','ADD','0000'),('722019042983420003','202019042979290009','7020181217000001','732019042986310003','390001200004',4,'2019-04-29 09:21:59','ADD','0000'),('722019042992290004','202019042945820012','7020181217000001','732019042905340004','390001200004',4,'2019-04-29 09:22:28','ADD','0000'),('722019042933990005','202019042915470015','7020181217000001','732019042939210005','390001200004',4,'2019-04-29 09:22:43','ADD','0000'),('722019042921800006','202019042987920018','7020181217000001','732019042925840006','390001200004',4,'2019-04-29 09:22:55','ADD','0000'),('722019042960050007','202019042948530021','7020181217000001','732019042954400007','390001200004',4,'2019-04-29 09:23:08','ADD','0000'),('722019042940530008','202019042936100024','7020181217000001','732019042907570008','390001200004',4,'2019-04-29 09:23:40','ADD','0000'),('722019042933000001','202019042995080003','7020181217000001','732019042904050001','390001200004',4,'2019-04-29 11:47:35','ADD','0000'),('722019042974160002','202019042970510006','7020181217000001','732019042937750002','390001200004',4,'2019-04-29 11:48:06','ADD','0000'),('722019051741410001','202019051735310003','7020181217000001','772019051712520001','390001200005',5,'2019-05-17 14:11:35','ADD','0000'),('722019051755950001','202019051752520003','7020181217000001','772019051730670001','390001200005',5,'2019-05-17 14:29:39','ADD','0000'),('722019051835910001','202019051820940005','7020181217000001','772019051807130001','390001200005',5,'2019-05-17 16:49:05','ADD','0000'),('722019051741410001','202019051829600003','7020181217000001','772019051712520001','390001200005',5,'2019-05-17 16:58:22','DEl','0000'),('722019051817770001','202019051847830003','7020181217000001','772019051888960001','390001200005',5,'2019-05-18 07:59:28','ADD','0000'),('722019051835390002','202019051883170007','7020181217000001','772019051839350002','390001200005',5,'2019-05-18 08:01:18','ADD','0000'),('722019051800120001','202019051846110003','7020181217000001','772019051853860001','390001200005',5,'2019-05-18 08:11:17','ADD','0000'),('722019051878390002','202019051814090006','7020181217000001','772019051883280002','390001200005',5,'2019-05-18 08:12:10','ADD','0000'),('722019051820930003','202019051861450009','7020181217000001','772019051856610003','390001200005',5,'2019-05-18 08:19:18','ADD','0000'),('722019051872430001','202019051864990003','7020181217000001','772019051860030001','390001200005',5,'2019-05-18 08:32:16','ADD','0000'),('722019051888830001','202019051885250013','7020181217000001','772019051824170001','390001200005',5,'2019-05-18 10:43:20','ADD','0000'),('722019051835910001','202019051893170022','7020181217000001','772019051807130001','390001200005',5,'2019-05-18 10:51:33','DEl','0000'),('722019051975520001','202019051959440007','7020181217000001','772019051932200003','390001200005',5,'2019-05-19 00:44:19','ADD','0000'),('722019051872430001','202019051961460009','7020181217000001','772019051860030001','390001200005',5,'2019-05-19 01:25:49','DEl','0000'),('722019052372380001','202019052366960007','7020181217000001','772019052337170001','390001200005',5,'2019-05-23 06:53:34','ADD','0000'),('722019051755950001','202019052655040009','7020181217000001','772019051730670001','390001200005',5,'2019-05-26 12:49:58','DEl','0000'),('722019052693600002','202019052624900014','7020181217000001','772019052630240001','390001200005',5,'2019-05-26 12:50:33','ADD','0000'),('722019052619090003','202019052650230016','702019051666020001','402019032924930007','390001200002',5,'2019-05-26 12:50:57','ADD','1000'),('722019052942510007','202019052990380028','702019051696930001','402019032924930007','390001200002',5,'2019-05-29 09:16:36','ADD','1000'),('722019060636810002','202019060621530004','702019051651830001','402019032924930007','390001200002',6,'2019-06-05 16:39:00','ADD','1000'),('722019060639010004','202019060639000008','702019051657250007','402019032924930007','390001200002',6,'2019-06-05 16:39:14','ADD','1000'),('7220181217000001','1234567891','7020181217000001','345678','390001200001',12,'2018-12-17 14:27:08','ADD','0000'),('7220181217000001','1234567891','7020181217000001','3456789','390001200001',12,'2018-12-17 14:43:37','ADD','0000'),('7220181217000002','1234567891','7020181217000001','3456789','390001200001',12,'2018-12-17 14:44:35','ADD','0000'),('7220181217000003','1234567891','7020181217000001','3456789','390001200001',12,'2018-12-17 14:45:24','ADD','0000'),('7220181217000004','1234567892','7020181217000001','3456789','390001200001',12,'2018-12-17 14:46:03','ADD','0000'),('7220181217000004','1234567893','7020181217000001','3456789','390001200001',12,'2018-12-17 15:05:51','DEl','0000'),('7220181217000004','1234567894','7020181217000001','3456789','390001200001',12,'2018-12-17 15:06:53','DEl','0000');

/*Table structure for table `business_community_photo` */

DROP TABLE IF EXISTS `business_community_photo`;

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

/*Data for the table `business_community_photo` */

insert  into `business_community_photo`(`community_photo_id`,`b_id`,`community_id`,`community_photo_type_cd`,`photo`,`month`,`create_time`,`operate`) values ('712019051639830003','72234567891','702019051687020001','T','12345678.jpg',5,'2019-05-16 02:10:55','ADD'),('712019051635190006','82234567891','702019051621670004','T','12345678.jpg',5,'2019-05-16 02:11:01','ADD'),('712019051659510009','92234567891','702019051657250007','T','12345678.jpg',5,'2019-05-16 02:12:05','ADD'),('712019051693130003','92234567897','702019051696930001','T','12345678.jpg',5,'2019-05-16 02:43:27','ADD'),('712019051618080003','92234567898','702019051651830001','T','12345678.jpg',5,'2019-05-16 02:50:56','ADD'),('7120181217000003','1234567890','7020181217000001','T','12345678.jpg',12,'2018-12-17 04:52:55','ADD'),('7120181217000003','1234567891','7020181217000001','T','12345678.jpg',12,'2018-12-17 05:06:15','DEl'),('7120181217000003','1234567891','7020181217000001','T','12345678.jpg',12,'2018-12-17 05:06:15','ADD');

/*Table structure for table `business_floor` */

DROP TABLE IF EXISTS `business_floor`;

CREATE TABLE `business_floor` (
  `floor_id` varchar(30) NOT NULL COMMENT '楼ID',
  `b_id` varchar(30) NOT NULL COMMENT '业务Id',
  `floor_num` varchar(12) NOT NULL COMMENT '楼编号',
  `name` varchar(100) NOT NULL COMMENT '小区楼名称',
  `user_id` varchar(30) NOT NULL COMMENT '用户ID',
  `remark` varchar(200) DEFAULT NULL COMMENT '备注',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `operate` varchar(4) NOT NULL COMMENT '数据状态，添加ADD，修改MOD 删除DEL',
  KEY `idx_business_floor_id` (`floor_id`),
  KEY `idx_business_floor_b_id` (`b_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `business_floor` */

insert  into `business_floor`(`floor_id`,`b_id`,`floor_num`,`name`,`user_id`,`remark`,`create_time`,`operate`) values ('732019042181450002','89861991919','01','1号楼','302019042192720001','填写具体值','2019-04-21 15:09:48','ADD'),('732019042188750002','89861991920','01','1号楼','302019042192720001',NULL,'2019-04-21 15:21:27','ADD'),('732019042188750002','89861991922','01','1号楼','302019042192720001',NULL,'2019-04-21 15:40:14','DEl'),('732019042188750002','89861991922','02','2号楼','302019042192720001',NULL,'2019-04-21 15:40:14','ADD'),('732019042188750002','89861991923','02','2号楼','302019042192720001',NULL,'2019-04-21 15:41:34','DEl'),('732019042248530001','89861991923','03','3号楼','302019042192720001',NULL,'2019-04-21 16:40:06','ADD'),('732019042218110002','89861991925','04','4号楼','302019042192720001',NULL,'2019-04-21 16:41:46','ADD'),('732019042237920003','89861991925','05','5号楼','302019042192720001',NULL,'2019-04-21 16:41:46','ADD'),('732019042248530001','89861991926','03','3号楼','302019042192720001',NULL,'2019-04-21 16:43:18','DEl'),('732019042248530001','89861991926','004','4号楼01','302019042192720001',NULL,'2019-04-21 16:43:18','ADD'),('732019042248530001','89861991927','03','3号楼','302019042192720001',NULL,'2019-04-21 16:43:35','DEl'),('732019042248530001','89861991927','004','4号楼01','302019042192720001',NULL,'2019-04-21 16:43:35','ADD'),('732019042248530001','89861991928','004','4号楼01','302019042192720001',NULL,'2019-04-21 16:44:34','DEl'),('732019042218110002','89861991930','04','4号楼','302019042192720001',NULL,'2019-04-21 16:46:24','DEl'),('732019042218110002','89861991930','003','3号楼01','302019042192720001',NULL,'2019-04-21 16:46:24','ADD'),('732019042237920003','89861991930','05','5号楼','302019042192720001',NULL,'2019-04-21 16:46:24','DEl'),('732019042237920003','89861991930','005','5号楼01','302019042192720001',NULL,'2019-04-21 16:46:24','ADD'),('732019042218110002','89861991931','003','3号楼01','302019042192720001',NULL,'2019-04-21 16:47:29','DEl'),('732019042237920003','89861991931','005','5号楼01','302019042192720001',NULL,'2019-04-21 16:47:30','DEl'),('732019042644990001','202019042604660002','045','045','30518940136629616640','测试，必须成功','2019-04-26 09:53:41','ADD'),('732019042717050001','202019042757120008','021','021','30518940136629616640','021','2019-04-26 17:12:03','ADD'),('732019042729750001','202019042785720002','021','021','30518940136629616640','021','2019-04-26 17:20:53','ADD'),('732019042773170001','202019042729460002','20','20','30518940136629616640','20','2019-04-26 17:30:55','ADD'),('732019042790640002','202019042775180005','21','21','30518940136629616640','21','2019-04-26 17:37:14','ADD'),('732019042715940003','202019042731610008','22','22','30518940136629616640','22','2019-04-26 17:38:57','ADD'),('732019042784130004','202019042711910013','023','23','30518940136629616640','测试','2019-04-27 11:56:56','ADD'),('732019042188750002','202019042833710002','02','2号楼','302019041824320001',NULL,'2019-04-28 09:47:07','DEl'),('732019042188750002','202019042833710002','021','2号楼1','30518940136629616640','021','2019-04-28 09:47:07','ADD'),('732019042773170001','202019042828060004','20','20','30518940136629616640','20','2019-04-28 09:47:53','DEl'),('732019042773170001','202019042828060004','24','24','30518940136629616640','24','2019-04-28 09:47:53','ADD'),('732019042893370001','202019042870220006','26','26','30518940136629616640','26','2019-04-28 09:53:53','ADD'),('732019042893370001','202019042827770009','26','26','30518940136629616640','26','2019-04-28 09:54:10','DEl'),('732019042893370001','202019042827770009','27','27','30518940136629616640','27','2019-04-28 09:54:10','ADD'),('732019042893370001','202019042893250011','27','27','30518940136629616640','27','2019-04-28 14:03:35','DEl'),('732019042893370001','202019042893250011','26','26','30518940136629616640','26','2019-04-28 14:03:35','ADD'),('732019042893370001','202019042911830002','26','26','30518940136629616640','26','2019-04-28 16:09:14','DEl'),('732019042893370001','202019042911830002','26','26','30518940136629616640','26','2019-04-28 16:09:14','ADD'),('732019042893370001','202019042962160002','26','26','30518940136629616640','26','2019-04-28 16:57:35','DEl'),('732019042893370001','202019042942050005','26','26','30518940136629616640','26','2019-04-28 17:06:21','DEl'),('732019042893370001','202019042979960002','26','26','30518940136629616640','26','2019-04-28 17:17:10','DEl'),('732019042937930001','202019042905090005','27','27','30518940136629616640','27','2019-04-28 17:19:24','ADD'),('732019042937930001','202019042900640008','27','27','30518940136629616640','27','2019-04-28 17:19:37','DEl'),('732019042937930001','202019042900640008','28','28','30518940136629616640','28','2019-04-28 17:19:37','ADD'),('732019042937930001','202019042932230010','28','28','30518940136629616640','28','2019-04-28 17:19:44','DEl'),('732019042908600001','202019042982100002','34','34','30518940136629616640','34','2019-04-29 09:14:52','ADD'),('732019042921980002','202019042976730005','33','33','30518940136629616640','33','2019-04-29 09:21:46','ADD'),('732019042986310003','202019042927450008','32','32','30518940136629616640','32','2019-04-29 09:21:59','ADD'),('732019042905340004','202019042953870011','36','36','30518940136629616640','36','2019-04-29 09:22:28','ADD'),('732019042939210005','202019042978390014','37','37','30518940136629616640','37','2019-04-29 09:22:43','ADD'),('732019042925840006','202019042971980017','38','38','30518940136629616640','38','2019-04-29 09:22:55','ADD'),('732019042954400007','202019042996460020','39','39','30518940136629616640','39','2019-04-29 09:23:08','ADD'),('732019042907570008','202019042910990023','38','38','30518940136629616640','38','2019-04-29 09:23:40','ADD'),('732019042904050001','202019042908790002','40','40','30518940136629616640','40','2019-04-29 11:47:35','ADD'),('732019042937750002','202019042900080005','42','42','30518940136629616640','42','2019-04-29 11:48:06','ADD');

/*Table structure for table `business_member_store` */

DROP TABLE IF EXISTS `business_member_store`;

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

/*Data for the table `business_member_store` */

/*Table structure for table `business_owner_car` */

DROP TABLE IF EXISTS `business_owner_car`;

CREATE TABLE `business_owner_car` (
  `car_id` varchar(30) NOT NULL COMMENT '汽车ID',
  `owner_id` varchar(30) NOT NULL COMMENT '业主ID',
  `b_id` varchar(30) NOT NULL COMMENT '业务Id',
  `car_num` varchar(12) NOT NULL COMMENT '车牌号',
  `car_brand` varchar(50) NOT NULL COMMENT '汽车品牌',
  `car_type` varchar(4) NOT NULL COMMENT '9901 家用小汽车，9902 客车，9903 货车',
  `car_color` varchar(12) NOT NULL COMMENT '颜色',
  `ps_id` varchar(30) NOT NULL COMMENT '车位ID',
  `user_id` varchar(30) NOT NULL COMMENT '用户ID',
  `remark` varchar(200) NOT NULL COMMENT '备注',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `operate` varchar(4) NOT NULL COMMENT '数据状态，添加ADD，修改MOD 删除DEL',
  KEY `idx_boc_car_id` (`car_id`),
  KEY `idx_boc_b_id` (`b_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `business_owner_car` */

insert  into `business_owner_car`(`car_id`,`owner_id`,`b_id`,`car_num`,`car_brand`,`car_type`,`car_color`,`ps_id`,`user_id`,`remark`,`create_time`,`operate`) values ('802019060884330001','772019051824170001','202019060818100002','青AGK916','传祺GS4','9901','白色','792019060544610001','30518940136629616640','','2019-06-08 04:04:56','ADD'),('802019060861200002','772019051824170001','202019060846020007','青AGK916','传祺GS4','9901','白色','792019060544610001','30518940136629616640','','2019-06-08 04:09:24','ADD'),('802019060814860001','772019051824170001','202019060803860002','青AGK916','传祺GS4','9901','白色','792019060544610001','30518940136629616640','','2019-06-08 04:24:23','ADD'),('802019060815380002','772019051824170001','202019060847790007','青AGK916','传祺GS4','9901','白色','792019060544610001','30518940136629616640','','2019-06-08 04:27:51','ADD'),('802019060855880003','772019051824170001','202019060810530012','青AGK916','传祺GS4','9901','白色','792019060544610001','30518940136629616640','','2019-06-08 04:28:52','ADD'),('802019060865530004','772019051824170001','202019060806430017','青AGK916','传祺GS4','9901','白色','792019060544610001','30518940136629616640','','2019-06-08 04:29:25','ADD'),('802019060818600001','772019051824170001','202019060845080002','青AGK916','传祺GS4','9901','白色','792019060544610001','30518940136629616640','测试','2019-06-08 05:55:06','ADD'),('802019060818600001','772019051824170001','202019060905050002','青AGK916','传祺GS4','9901','白色','792019060544610001','30518940136629616640','测试','2019-06-08 16:50:47','DEl'),('802019060996090001','772019051824170001','202019060967560006','青AGK917','大众CC','9901','白色','792019060544610001','30518940136629616640','测试','2019-06-08 16:54:03','ADD'),('802019060996090001','772019051824170001','202019060905130011','青AGK917','大众CC','9901','白色','792019060544610001','30518940136629616640','测试','2019-06-08 16:56:43','DEl'),('802019060957150002','772019051824170001','202019060915600015','青AGK918','东风雷诺','9901','红色','792019060544610001','30518940136629616640','测试','2019-06-08 16:57:29','ADD'),('802019061088450001','772019052630240001','202019061072890002','青CBB666','奥迪A8','9901','黑色','792019060695730005','30518940136629616640','','2019-06-09 16:08:20','ADD'),('802019061038310002','772019051932200003','202019061044690013','青ACC888','宝马X6','9901','白色','792019061076110002','30518940136629616640','','2019-06-09 16:11:03','ADD'),('802019061088450001','772019052630240001','202019061029490002','青CBB666','奥迪A8','9901','黑色','792019060695730005','30518940136629616640','','2019-06-09 16:28:05','DEl'),('802019061038310002','772019051932200003','202019061070810006','青ACC888','宝马X6','9901','白色','792019061076110002','30518940136629616640','','2019-06-09 16:28:49','DEl');

/*Table structure for table `business_parking_space` */

DROP TABLE IF EXISTS `business_parking_space`;

CREATE TABLE `business_parking_space` (
  `ps_id` varchar(30) NOT NULL COMMENT '车位ID',
  `b_id` varchar(30) NOT NULL COMMENT '业务Id',
  `community_id` varchar(30) NOT NULL COMMENT '小区ID',
  `num` varchar(12) NOT NULL COMMENT '车位编号',
  `type_cd` varchar(4) NOT NULL COMMENT '车位类型,地上停车位1001 地下停车位 2001',
  `state` varchar(4) NOT NULL COMMENT '车位状态 出售 S，出租 H ，空闲 F',
  `area` decimal(7,2) NOT NULL COMMENT '车位面积',
  `user_id` varchar(30) NOT NULL COMMENT '用户ID',
  `remark` varchar(300) NOT NULL COMMENT '用户ID',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `operate` varchar(4) NOT NULL COMMENT '数据状态，添加ADD，修改MOD 删除DEL',
  KEY `idx_bps_ps_id` (`ps_id`),
  KEY `idx_bps_b_id` (`b_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `business_parking_space` */

insert  into `business_parking_space`(`ps_id`,`b_id`,`community_id`,`num`,`type_cd`,`state`,`area`,`user_id`,`remark`,`create_time`,`operate`) values ('792019060575090001','202019060536030004','7020181217000001','WB001','1001','F','12.90','30518940136629616640','','2019-06-04 16:25:54','ADD'),('792019060575090001','202019060508030008','7020181217000001','WB001','1001','F','12.90','30518940136629616640','','2019-06-04 16:37:32','DEl'),('792019060544610001','202019060580010002','7020181217000001','WB002','1001','F','3.89','30518940136629616640','','2019-06-04 16:46:43','ADD'),('792019060544610001','202019060567090004','7020181217000001','WB002','1001','F','3.89','30518940136629616640','','2019-06-04 16:47:11','DEl'),('792019060544610001','202019060567090004','7020181217000001','WB003','2001','F','3.88','30518940136629616640','测试','2019-06-04 16:47:11','ADD'),('792019060695730005','202019060684560018','7020181217000001','1232','1001','F','3.00','30518940136629616640','123','2019-06-06 10:18:49','ADD'),('792019060544610001','202019060811200003','7020181217000001','WB003','2001','F','3.88','30518940136629616640','测试','2019-06-08 04:24:24','DEl'),('792019060544610001','202019060811200003','7020181217000001','WB003','2001','S','3.88','30518940136629616640','测试','2019-06-08 04:24:24','ADD'),('792019060544610001','202019060893580008','7020181217000001','WB003','2001','F','3.88','30518940136629616640','测试','2019-06-08 04:27:51','DEl'),('792019060544610001','202019060893580008','7020181217000001','WB003','2001','S','3.88','30518940136629616640','测试','2019-06-08 04:27:51','ADD'),('792019060544610001','202019060894030013','7020181217000001','WB003','2001','F','3.88','30518940136629616640','测试','2019-06-08 04:28:52','DEl'),('792019060544610001','202019060894030013','7020181217000001','WB003','2001','S','3.88','30518940136629616640','测试','2019-06-08 04:28:52','ADD'),('792019060544610001','202019060824640018','7020181217000001','WB003','2001','F','3.88','30518940136629616640','测试','2019-06-08 04:29:25','DEl'),('792019060544610001','202019060824640018','7020181217000001','WB003','2001','S','3.88','30518940136629616640','测试','2019-06-08 04:29:25','ADD'),('792019060544610001','202019060863360003','7020181217000001','WB003','2001','F','3.88','30518940136629616640','测试','2019-06-08 05:55:07','DEl'),('792019060544610001','202019060863360003','7020181217000001','WB003','2001','S','3.88','30518940136629616640','测试','2019-06-08 05:55:07','ADD'),('792019060544610001','202019060926800003','7020181217000001','WB003','2001','S','3.88','30518940136629616640','测试','2019-06-08 16:50:49','DEl'),('792019060544610001','202019060926800003','7020181217000001','WB003','2001','F','3.88','30518940136629616640','测试','2019-06-08 16:50:49','ADD'),('792019060544610001','202019060978860007','7020181217000001','WB003','2001','F','3.88','30518940136629616640','测试','2019-06-08 16:54:03','DEl'),('792019060544610001','202019060978860007','7020181217000001','WB003','2001','S','3.88','30518940136629616640','测试','2019-06-08 16:54:03','ADD'),('792019060544610001','202019060984100012','7020181217000001','WB003','2001','S','3.88','30518940136629616640','测试','2019-06-08 16:56:44','DEl'),('792019060544610001','202019060984100012','7020181217000001','WB003','2001','F','3.88','30518940136629616640','测试','2019-06-08 16:56:44','ADD'),('792019060544610001','202019060913420016','7020181217000001','WB003','2001','F','3.88','30518940136629616640','测试','2019-06-08 16:57:29','DEl'),('792019060544610001','202019060913420016','7020181217000001','WB003','2001','S','3.88','30518940136629616640','测试','2019-06-08 16:57:29','ADD'),('792019060695730005','202019061039240003','7020181217000001','1232','1001','F','3.00','30518940136629616640','123','2019-06-09 16:08:21','DEl'),('792019060695730005','202019061039240003','7020181217000001','1232','1001','H','3.00','30518940136629616640','123','2019-06-09 16:08:21','ADD'),('792019061013090001','202019061017320007','7020181217000001','WB004','1001','F','12.08','30518940136629616640','','2019-06-09 16:09:19','ADD'),('792019061076110002','202019061093600009','7020181217000001','WB005','2001','F','12.07','30518940136629616640','','2019-06-09 16:09:43','ADD'),('792019061044360003','202019061090550011','7020181217000001','WB006','2001','F','12.36','30518940136629616640','','2019-06-09 16:10:00','ADD'),('792019061076110002','202019061018820014','7020181217000001','WB005','2001','F','12.07','30518940136629616640','','2019-06-09 16:11:03','DEl'),('792019061076110002','202019061018820014','7020181217000001','WB005','2001','S','12.07','30518940136629616640','','2019-06-09 16:11:03','ADD'),('792019060695730005','202019061066360003','7020181217000001','1232','1001','H','3.00','30518940136629616640','123','2019-06-09 16:28:08','DEl'),('792019060695730005','202019061066360003','7020181217000001','1232','1001','F','3.00','30518940136629616640','123','2019-06-09 16:28:08','ADD'),('792019061076110002','202019061086170007','7020181217000001','WB005','2001','S','12.07','30518940136629616640','','2019-06-09 16:28:49','DEl'),('792019061076110002','202019061086170007','7020181217000001','WB005','2001','F','12.07','30518940136629616640','','2019-06-09 16:28:49','ADD');

/*Table structure for table `business_pay_fee` */

DROP TABLE IF EXISTS `business_pay_fee`;

CREATE TABLE `business_pay_fee` (
  `fee_id` varchar(30) NOT NULL COMMENT '费用ID',
  `b_id` varchar(30) NOT NULL COMMENT '业务Id',
  `fee_type_cd` varchar(12) NOT NULL COMMENT '费用类型，物业费，停车费',
  `community_id` varchar(30) NOT NULL COMMENT '小区ID',
  `payer_obj_id` varchar(30) NOT NULL COMMENT '付款方ID',
  `income_obj_id` varchar(30) NOT NULL COMMENT '收款方ID',
  `start_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '开始时间',
  `end_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '结束时间',
  `amount` decimal(7,2) NOT NULL DEFAULT '-1.00' COMMENT '总金额，如物业费，停车费等没有总金额的，填写为-1.00',
  `user_id` varchar(30) NOT NULL COMMENT '创建用户ID',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `operate` varchar(4) NOT NULL COMMENT '数据状态，添加ADD，修改MOD 删除DEL',
  KEY `idx_bpf_fee_id` (`fee_id`),
  KEY `idx_bpf_b_id` (`b_id`),
  KEY `idx_pf_fee_id` (`fee_id`),
  KEY `idx_pf_b_id` (`b_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `business_pay_fee` */

insert  into `business_pay_fee`(`fee_id`,`b_id`,`fee_type_cd`,`community_id`,`payer_obj_id`,`income_obj_id`,`start_time`,`end_time`,`amount`,`user_id`,`create_time`,`operate`) values ('902019060295320001','202019060268370003','888800010001','7020181217000001','752019050790250003','402019032924930007','2019-06-02 12:48:05','2019-06-02 12:48:05','-1.00','30518940136629616640','2019-06-02 04:48:10','ADD'),('902019060295320001','202019060229640003','888800010001','7020181217000001','752019050790250003','402019032924930007','2019-06-02 12:48:05','2019-06-02 12:48:05','-1.00','30518940136629616640','2019-06-02 05:02:17','DEl'),('902019060295320001','202019060206150003','888800010001','7020181217000001','752019050790250003','402019032924930007','2019-06-02 12:48:05','2019-06-02 12:48:05','-1.00','30518940136629616640','2019-06-02 05:21:20','DEl'),('902019060201520001','202019060254680006','888800010001','7020181217000001','752019050860300001','402019032924930007','2019-06-02 13:22:43','2019-06-02 13:22:43','-1.00','30518940136629616640','2019-06-02 05:22:44','ADD'),('902019060201520001','202019060317050003','888800010001','7020181217000001','752019050860300001','402019032924930007','2019-06-02 13:22:43','2019-06-02 13:22:43','-1.00','30518940136629616640','2019-06-03 08:06:01','DEl'),('902019060201520001','202019060317050003','888800010001','7020181217000001','752019050860300001','402019032924930007','2019-06-02 13:22:43','2019-09-02 13:22:43','-1.00','30518940136629616640','2019-06-03 08:06:01','ADD'),('902019060201520001','202019060392620003','888800010001','7020181217000001','752019050860300001','402019032924930007','2019-06-02 13:22:43','2019-09-02 13:22:43','-1.00','30518940136629616640','2019-06-03 13:22:54','DEl'),('902019060201520001','202019060392620003','888800010001','7020181217000001','752019050860300001','402019032924930007','2019-06-02 13:22:43','2019-10-02 13:22:43','-1.00','30518940136629616640','2019-06-03 13:22:54','ADD'),('902019060201520001','202019060444320003','888800010001','7020181217000001','752019050860300001','402019032924930007','2019-06-02 13:22:43','2019-10-02 13:22:43','-1.00','30518940136629616640','2019-06-03 17:48:08','DEl'),('902019060201520001','202019060444320003','888800010001','7020181217000001','752019050860300001','402019032924930007','2019-06-02 13:22:43','2020-01-02 13:22:43','-1.00','30518940136629616640','2019-06-03 17:48:08','ADD'),('902019060447390002','202019060408020009','888800010001','7020181217000001','752019050896140002','402019032924930007','2019-06-04 14:57:58','2019-06-04 14:57:58','-1.00','30518940136629616640','2019-06-04 06:57:58','ADD'),('902019060201520001','202019060446370012','888800010001','7020181217000001','752019050860300001','402019032924930007','2019-06-02 13:22:43','2020-01-02 13:22:43','-1.00','30518940136629616640','2019-06-04 07:00:44','DEl'),('902019060201520001','202019060446370012','888800010001','7020181217000001','752019050860300001','402019032924930007','2019-06-02 13:22:43','2020-02-02 13:22:43','-1.00','30518940136629616640','2019-06-04 07:00:44','ADD'),('902019060201520001','202019060546810010','888800010001','7020181217000001','752019050860300001','402019032924930007','2019-06-02 13:22:43','2020-02-02 13:22:43','-1.00','30518940136629616640','2019-06-05 13:48:56','DEl'),('902019060201520001','202019060546810010','888800010001','7020181217000001','752019050860300001','402019032924930007','2019-06-02 13:22:43','2021-02-02 13:22:43','-1.00','30518940136629616640','2019-06-05 13:48:56','ADD'),('902019060824090004','202019060888560019','888800010003','7020181217000001','792019060544610001','402019032924930007','2019-06-08 12:29:24','0000-00-00 00:00:00','-1.00','30518940136629616640','2019-06-08 04:31:16','ADD'),('902019060824090004','202019060888560019','888800010003','7020181217000001','792019060544610001','402019032924930007','2019-06-08 12:29:24','0000-00-00 00:00:00','-1.00','30518940136629616640','2019-06-08 04:31:55','ADD'),('902019060824090004','202019060888560019','888800010003','7020181217000001','792019060544610001','402019032924930007','2019-06-08 12:29:24','0000-00-00 00:00:00','-1.00','30518940136629616640','2019-06-08 04:32:19','ADD'),('902019060824090004','202019060888560019','888800010003','7020181217000001','792019060544610001','402019032924930007','2019-06-08 12:29:24','0000-00-00 00:00:00','-1.00','30518940136629616640','2019-06-08 04:34:14','ADD'),('902019060824090004','202019060888560019','888800010003','7020181217000001','792019060544610001','402019032924930007','2019-06-08 12:29:24','0000-00-00 00:00:00','-1.00','30518940136629616640','2019-06-08 04:37:30','ADD'),('902019060824090004','202019060888560019','888800010003','7020181217000001','792019060544610001','402019032924930007','2019-06-08 12:29:24','2037-12-30 12:12:12','-1.00','30518940136629616640','2019-06-08 04:37:57','ADD'),('902019060824090004','202019060888560019','888800010003','7020181217000001','792019060544610001','402019032924930007','2019-06-08 12:29:24','2038-01-01 00:00:00','-1.00','30518940136629616640','2019-06-08 04:38:27','ADD'),('902019060882350001','202019060868610004','888800010003','7020181217000001','792019060544610001','402019032924930007','2019-06-08 13:55:04','2038-01-01 00:00:00','-1.00','30518940136629616640','2019-06-08 05:55:08','ADD'),('902019060882350001','202019060969220004','888800010003','7020181217000001','792019060544610001','402019032924930007','2019-06-08 13:55:04','2038-01-01 00:00:00','-1.00','30518940136629616640','2019-06-08 16:50:48','DEl'),('902019060939110001','202019060926100008','888800010003','7020181217000001','792019060544610001','402019032924930007','2019-06-09 00:54:02','2038-01-01 00:00:00','-1.00','30518940136629616640','2019-06-08 16:54:03','ADD'),('902019060939110001','202019060943070013','888800010003','7020181217000001','792019060544610001','402019032924930007','2019-06-09 00:54:02','2038-01-01 00:00:00','-1.00','30518940136629616640','2019-06-08 16:56:43','DEl'),('902019060918930002','202019060956040017','888800010003','7020181217000001','792019060544610001','402019032924930007','2019-06-09 00:57:29','2038-01-01 00:00:00','-1.00','30518940136629616640','2019-06-08 16:57:30','ADD'),('902019061018030001','202019061039910004','888800010004','7020181217000001','792019060695730005','402019032924930007','2019-06-10 00:08:17','2019-06-10 00:08:17','-1.00','30518940136629616640','2019-06-09 16:08:22','ADD'),('902019061077390002','202019061080790015','888800010003','7020181217000001','792019061076110002','402019032924930007','2019-06-10 00:11:03','2038-01-01 00:00:00','99999.99','30518940136629616640','2019-06-09 16:11:03','ADD'),('902019061018030001','202019061089200004','888800010004','7020181217000001','792019060695730005','402019032924930007','2019-06-10 00:08:17','2019-06-10 00:08:17','-1.00','30518940136629616640','2019-06-09 16:28:06','DEl'),('902019061077390002','202019061062540008','888800010003','7020181217000001','792019061076110002','402019032924930007','2019-06-10 00:11:03','2038-01-01 00:00:00','99999.99','30518940136629616640','2019-06-09 16:28:49','DEl');

/*Table structure for table `business_pay_fee_attrs` */

DROP TABLE IF EXISTS `business_pay_fee_attrs`;

CREATE TABLE `business_pay_fee_attrs` (
  `fee_id` varchar(30) NOT NULL COMMENT '费用ID',
  `b_id` varchar(30) NOT NULL COMMENT '业务Id',
  `community_id` varchar(30) NOT NULL COMMENT '小区ID',
  `attr_id` varchar(30) NOT NULL COMMENT '属性id',
  `spec_cd` varchar(12) NOT NULL COMMENT '规格id,参考spec表',
  `value` varchar(50) NOT NULL COMMENT '属性值',
  `operate` varchar(4) NOT NULL COMMENT '数据状态，添加ADD，修改MOD 删除DEL',
  KEY `idx_bpfa_fee_id` (`fee_id`),
  KEY `idx_bpfa_b_id` (`b_id`),
  KEY `idx_pfa_fee_id` (`fee_id`),
  KEY `idx_pfa_b_id` (`b_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `business_pay_fee_attrs` */

/*Table structure for table `business_pay_fee_config` */

DROP TABLE IF EXISTS `business_pay_fee_config`;

CREATE TABLE `business_pay_fee_config` (
  `config_id` varchar(30) NOT NULL COMMENT '费用ID',
  `b_id` varchar(30) NOT NULL COMMENT '业务Id',
  `community_id` varchar(30) NOT NULL COMMENT '小区ID',
  `fee_type_cd` varchar(12) NOT NULL COMMENT '费用类型，物业费，停车费',
  `square_price` decimal(7,2) NOT NULL COMMENT '每平米收取的单价',
  `additional_amount` decimal(7,2) NOT NULL COMMENT '附加费用',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `operate` varchar(4) NOT NULL COMMENT '数据状态，添加ADD，修改MOD 删除DEL',
  KEY `idx_bpfc_config_id` (`config_id`),
  KEY `idx_bpfc_b_id` (`b_id`),
  KEY `idx_pfc_config_id` (`config_id`),
  KEY `idx_pfc_b_id` (`b_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `business_pay_fee_config` */

insert  into `business_pay_fee_config`(`config_id`,`b_id`,`community_id`,`fee_type_cd`,`square_price`,`additional_amount`,`create_time`,`operate`) values ('922019060280800001','202019060243950002','7020181217000001','888800010001','3.15','200.00','2019-06-01 16:11:45','ADD'),('922019060280800001','202019060296560002','7020181217000001','888800010001','3.15','200.00','2019-06-01 16:33:34','DEl'),('922019060280800001','202019060296560002','7020181217000001','888800010001','3.16','200.00','2019-06-01 16:33:34','ADD'),('922019060280800001','202019060297870004','7020181217000001','888800010001','3.16','200.00','2019-06-01 16:33:43','DEl'),('922019060280800001','202019060297870004','7020181217000001','888800010001','3.16','201.00','2019-06-01 16:33:43','ADD'),('922019060280800001','202019060235590006','7020181217000001','888800010001','3.16','201.00','2019-06-02 02:07:18','DEl'),('922019060280800001','202019060235590006','7020181217000001','888800010001','3.17','201.00','2019-06-02 02:07:18','ADD');

/*Table structure for table `business_pay_fee_detail` */

DROP TABLE IF EXISTS `business_pay_fee_detail`;

CREATE TABLE `business_pay_fee_detail` (
  `detail_id` varchar(30) NOT NULL COMMENT '费用明细ID',
  `b_id` varchar(30) NOT NULL COMMENT '业务Id',
  `fee_id` varchar(30) NOT NULL COMMENT '费用ID',
  `community_id` varchar(30) NOT NULL COMMENT '小区ID',
  `cycles` int(11) NOT NULL COMMENT '周期，以月为单位',
  `receivable_amount` decimal(7,2) NOT NULL COMMENT '应收金额',
  `received_amount` decimal(7,2) NOT NULL COMMENT '实收金额',
  `prime_rate` decimal(3,2) NOT NULL COMMENT '打折率',
  `remark` varchar(200) NOT NULL COMMENT '备注',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `operate` varchar(4) NOT NULL COMMENT '数据状态，添加ADD，修改MOD 删除DEL',
  KEY `idx_bpfd_detail_id` (`detail_id`),
  KEY `idx_bpfd_b_id` (`b_id`),
  KEY `idx_pfd_detail_id` (`detail_id`),
  KEY `idx_pfd_b_id` (`b_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `business_pay_fee_detail` */

insert  into `business_pay_fee_detail`(`detail_id`,`b_id`,`fee_id`,`community_id`,`cycles`,`receivable_amount`,`received_amount`,`prime_rate`,`remark`,`create_time`,`operate`) values ('912019060365080001','202019060323600002','902019060201520001','7020181217000001',3,'1790.70','1790.70','1.00','测试','2019-06-03 07:49:03','ADD'),('912019060325760001','202019060386250002','902019060201520001','7020181217000001',3,'1790.70','1790.70','1.00','测试','2019-06-03 08:06:00','ADD'),('912019060382700001','202019060360120002','902019060201520001','7020181217000001',1,'596.90','596.90','1.00','测试','2019-06-03 13:22:54','ADD'),('912019060427550001','202019060487860002','902019060201520001','7020181217000001',3,'1790.70','1790.70','1.00','','2019-06-03 17:48:08','ADD'),('912019060459310003','202019060473410011','902019060201520001','7020181217000001',1,'596.90','596.90','1.00','测试','2019-06-04 07:00:44','ADD'),('912019060558580001','202019060597950009','902019060201520001','7020181217000001',12,'7162.82','7162.82','1.00','','2019-06-05 13:48:56','ADD'),('912019060862910001','202019060899970005','902019060882350001','7020181217000001',1,'99999.99','99999.99','1.00','测试','2019-06-08 05:55:08','ADD'),('912019060954310001','202019060995750009','902019060939110001','7020181217000001',1,'99999.99','99999.99','1.00','测试','2019-06-08 16:54:03','ADD'),('912019060935270002','202019060971230018','902019060918930002','7020181217000001',1,'99999.99','99999.99','1.00','测试','2019-06-08 16:57:30','ADD'),('912019061086730001','202019061002600005','902019061018030001','7020181217000001',1,'80.00','79999.99','1.00','','2019-06-09 16:08:22','ADD'),('912019061029450002','202019061027520016','902019061077390002','7020181217000001',1,'99999.99','99999.99','1.00','','2019-06-09 16:11:03','ADD');

/*Table structure for table `business_property` */

DROP TABLE IF EXISTS `business_property`;

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

/*Data for the table `business_property` */

insert  into `business_property`(`property_id`,`b_id`,`name`,`address`,`tel`,`nearby_landmarks`,`map_x`,`map_y`,`month`,`create_time`,`operate`) values ('9020181218000001','2234567894','方博家园','青海省西宁市城中区129号','17797173942','王府井旁30米','101.801909','36.597263',12,'2018-12-18 14:54:19','ADD'),('9020181218000001','9234567894','方博家园','青海省西宁市城中区129号','17797173942','王府井旁30米','101.801909','36.597263',12,'2018-12-23 04:01:05','DEl'),('9020181218000001','9234567894','方博家园','青海省西宁市城中区129号','17797173942','王府井旁30米','101.801909','36.597263',12,'2018-12-23 04:01:05','ADD'),('9020181218000001','9234567894','方博家园','青海省西宁市城中区129号','17797173942','王府井旁30米','101.801909','36.597263',12,'2018-12-23 04:02:17','DEl'),('9020181218000001','9234567894','方博家园123','青海省西宁市城中区129号123','17797173952','王府井旁40米','102.801909','37.597263',12,'2018-12-23 04:02:17','ADD'),('9020181218000001','10234567894','方博家园','青海省西宁市城中区129号','17797173942','王府井旁30米','101.801909','36.597263',12,'2018-12-23 04:02:24','DEl'),('9020181218000001','10234567894','方博家园123','青海省西宁市城中区129号123','17797173952','王府井旁40米','102.801909','37.597263',12,'2018-12-23 04:02:24','ADD');

/*Table structure for table `business_property_attr` */

DROP TABLE IF EXISTS `business_property_attr`;

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

/*Data for the table `business_property_attr` */

insert  into `business_property_attr`(`b_id`,`attr_id`,`property_id`,`spec_cd`,`value`,`month`,`create_time`,`operate`) values ('2234567894','112018121800000002','9020181218000001','1001','01',12,'2018-12-18 14:54:19','ADD'),('9234567894','112018121800000002','9020181218000001','1001','01',12,'2018-12-23 04:01:06','DEl'),('9234567894','112018121800000002','9020181218000001','1001','02',12,'2018-12-23 04:01:06','ADD'),('9234567894','112018121800000002','9020181218000001','1001','01',12,'2018-12-23 04:02:17','DEl'),('9234567894','112018121800000002','9020181218000001','1001','02',12,'2018-12-23 04:02:17','ADD'),('10234567894','112018121800000002','9020181218000001','1001','01',12,'2018-12-23 04:02:25','DEl'),('10234567894','112018121800000002','9020181218000001','1001','02',12,'2018-12-23 04:02:25','ADD'),('19234567894','112018121800000002','9020181218000001','1001','02',12,'2018-12-23 07:45:01','DEl');

/*Table structure for table `business_property_cerdentials` */

DROP TABLE IF EXISTS `business_property_cerdentials`;

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

/*Data for the table `business_property_cerdentials` */

insert  into `business_property_cerdentials`(`property_cerdentials_id`,`b_id`,`property_id`,`credentials_cd`,`value`,`validity_period`,`positive_photo`,`negative_photo`,`month`,`create_time`,`operate`) values ('9220181218000002','4234567894','9020181218000001','1','632126XXXXXXXX2011','3000-01-01','正面照片地址，1234567.jpg','反面照片地址，没有不填写',12,'2018-12-18 15:32:00','ADD'),('9220181218000002','12234567894','9020181218000001','1','632126XXXXXXXX2011','3000-01-01','正面照片地址，1234567.jpg','反面照片地址，没有不填写',12,'2018-12-23 04:26:54','DEl'),('9220181218000002','12234567894','9020181218000001','1','632126XXXXXXXX2012','3000-02-01','1234567.jpg','11.jpg',12,'2018-12-23 04:26:54','ADD'),('9220181218000002','16234567894','9020181218000001','1','632126XXXXXXXX2012','3000-02-01','1234567.jpg','11.jpg',12,'2018-12-23 07:04:54','DEl');

/*Table structure for table `business_property_fee` */

DROP TABLE IF EXISTS `business_property_fee`;

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

/*Data for the table `business_property_fee` */

insert  into `business_property_fee`(`fee_id`,`b_id`,`property_id`,`fee_type_cd`,`fee_money`,`fee_time`,`create_time`,`start_time`,`end_time`,`month`,`operate`) values ('9420181221000001','5234567894','9020181218000001','T','10','0.5','2018-12-20 16:14:57','2018-01-01 00:00:00','2020-12-31 00:00:00',12,'ADD'),('9420181221000001','13234567894','9020181218000001','T','10','0.5','2018-12-23 04:52:34','2018-01-01 00:00:00','2020-12-31 00:00:00',12,'DEl'),('9420181221000001','13234567894','9020181218000001','T','10','0.5','2018-12-23 04:52:34','2018-05-01 00:00:00','2020-11-30 00:00:00',12,'ADD');

/*Table structure for table `business_property_house` */

DROP TABLE IF EXISTS `business_property_house`;

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

/*Data for the table `business_property_house` */

insert  into `business_property_house`(`house_id`,`property_id`,`b_id`,`house_num`,`house_name`,`house_phone`,`house_area`,`fee_type_cd`,`fee_price`,`month`,`create_time`,`operate`) values ('9520181222000001','123213','6234567894','T','10',NULL,'01918','111','10.09',12,'2018-12-21 17:01:12','ADD'),('9520181222000001','123213','7234567894','T','10','17797173942','01918','111','10.09',12,'2018-12-21 17:07:34','ADD'),('9520181222000003','123213','7234567894','T','10','17797173942','01918','111','10.09',12,'2018-12-21 17:08:20','ADD'),('9520181222000005','123213','8234567894','T','10','17797173942','01918','111','10.09',12,'2018-12-21 17:08:28','ADD'),('9520181222000005','123213','14234567894','T','10','17797173942','01918','111','10.09',12,'2018-12-23 05:59:48','DEl'),('9520181222000005','123213','14234567894','123123','吴XX住宅','17797173943','01919','112','11.09',12,'2018-12-23 05:59:48','ADD'),('9520181222000005','123213','17234567894','123123','吴XX住宅','17797173943','01919','112','11.09',12,'2018-12-23 07:19:41','DEl');

/*Table structure for table `business_property_house_attr` */

DROP TABLE IF EXISTS `business_property_house_attr`;

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

/*Data for the table `business_property_house_attr` */

insert  into `business_property_house_attr`(`b_id`,`attr_id`,`house_id`,`spec_cd`,`VALUE`,`month`,`create_time`,`operate`) values ('6234567894','112018122200000002','9520181222000001','1001','01',12,'2018-12-21 17:01:12','ADD'),('7234567894','112018122200000002','9520181222000001','1001','01',12,'2018-12-21 17:07:34','ADD'),('7234567894','112018122200000004','9520181222000003','1001','01',12,'2018-12-21 17:08:20','ADD'),('8234567894','112018122200000006','9520181222000005','1001','01',12,'2018-12-21 17:08:28','ADD'),('14234567894','112018122200000006','9520181222000005','1001','01',12,'2018-12-23 05:59:48','DEl'),('14234567894','112018122200000006','9520181222000005','1001','02',12,'2018-12-23 05:59:48','ADD'),('17234567894','112018122200000006','9520181222000005','1001','02',12,'2018-12-23 07:19:42','DEl');

/*Table structure for table `business_property_photo` */

DROP TABLE IF EXISTS `business_property_photo`;

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

/*Data for the table `business_property_photo` */

insert  into `business_property_photo`(`property_photo_id`,`b_id`,`property_id`,`property_photo_type_cd`,`photo`,`month`,`create_time`,`operate`) values ('null20181218000001','3234567894','9020181218000001','T','12345678.jpg',12,'2018-12-18 15:21:09','ADD'),('12320181218000001','11234567894','9020181218000001','T','12345678.jpg',12,'2018-12-23 04:15:59','DEl'),('12320181218000001','11234567894','9020181218000001','T','123456789.jpg',12,'2018-12-23 04:16:00','ADD'),('12320181218000001','15234567894','9020181218000001','T','123456789.jpg',12,'2018-12-23 06:57:20','DEl');

/*Table structure for table `business_property_user` */

DROP TABLE IF EXISTS `business_property_user`;

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

/*Data for the table `business_property_user` */

insert  into `business_property_user`(`property_user_id`,`property_id`,`b_id`,`user_id`,`rel_cd`,`month`,`create_time`,`operate`) values ('9320181219000001','9020181218000001','4234567894','123','600311000001',12,'2018-12-18 17:28:13','ADD'),('9320181219000001','9020181218000001','18234567894','123','600311000001',12,'2018-12-23 07:36:49','DEl');

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

/*Data for the table `business_store` */

insert  into `business_store`(`store_id`,`b_id`,`user_id`,`name`,`address`,`tel`,`store_type_cd`,`nearby_landmarks`,`map_x`,`map_y`,`month`,`create_time`,`operate`) values ('402019032973010004','202019032981850008','123','齐天超时（王府井店）','青海省西宁市城中区129号','15897089471','M','王府井内','101.801909','36.597263',3,'2019-03-29 14:13:26','ADD'),('402019032924930007','202019032924200010','30518940136629616640','小吴工作室','城南格兰小镇','17797173942','800900000003','青海','','',3,'2019-03-29 14:21:41','ADD'),('402019033005920002','202019033072790004','123','齐天超时（王府井店）','青海省西宁市城中区129号','15897089471','M','王府井内','101.801909','36.597263',3,'2019-03-30 05:32:16','ADD'),('402019033051590001','202019033063640004','123','齐天超时（王府井店）','青海省西宁市城中区129号','15897089471','M','王府井内','101.801909','36.597263',3,'2019-03-30 05:56:22','ADD'),('402019033064560001','202019033033220002','123','齐天超时（王府井店）','青海省西宁市城中区129号','15897089471','M','王府井内','101.801909','36.597263',3,'2019-03-30 06:51:23','ADD'),('402019033060910002','202019033011280002','30518940136629616640','齐天超时（王府井店）','青海省西宁市城中区129号','15897089471','M','王府井内','101.801909','36.597263',3,'2019-03-30 07:47:23','ADD'),('402019033011430001','202019033046130002','30518940136629616640','齐天超时（王府井店）','青海省西宁市城中区129号','15897089471','M','王府井内','101.801909','36.597263',3,'2019-03-30 08:04:39','ADD'),('402019033089200001','202019033072520002','30518940136629616640','齐天超时（王府井店）','青海省西宁市城中区129号','15897089471','M','王府井内','101.801909','36.597263',3,'2019-03-30 08:31:29','ADD'),('402019033091620005','202019033076390007','302019033054910001','996icu公司','青海省西宁市城中区','17797173952','800900000004','中心广场','','',3,'2019-03-30 08:37:31','ADD'),('402019040288380001','202019040289110004','302019040270260001','东方国信科技公司','青海省西宁市','18909798989','800900000003','青海移动公司','','',4,'2019-04-01 16:43:00','ADD'),('402019040289200009','202019040231680007','302019040270260001','东方国信科技公司','青海省西宁市','18909798989','800900000003','青海移动公司','','',4,'2019-04-01 16:45:06','ADD'),('402019040244100017','202019040281460010','302019040270260001','东方国信科技公司','青海省西宁市','18909798989','800900000003','青海移动公司','','',4,'2019-04-01 16:48:29','ADD'),('402019040287010001','202019040234930002','302019040270260001','东方国信科技公司','青海省西宁市','18909798989','800900000003','青海移动公司','','',4,'2019-04-01 16:57:22','ADD'),('402019040290830009','202019040243080007','302019040265910001','中心奥蛋','青海西宁市','17797173990','800900000003','青海','','',4,'2019-04-01 17:02:38','ADD'),('402019041335090001','202019041317610002','30518940136629616640','齐天超时1（王府井店）','青海省西宁市城中区129号','15897089471','M','王府井内','101.801909','36.597263',4,'2019-04-13 15:11:13','ADD'),('402019041322840005','202019041327040005','30518940136629616640','齐天超时1（王府井店）','青海省西宁市城中区129号','15897089471','800900000003','王府井内','101.801909','36.597263',4,'2019-04-13 15:12:27','ADD'),('402019042844630001','202019042862250016','300019042192720001','xxx','xxx','17133858872','800900000001','xxx','','',4,'2019-04-28 02:08:55','ADD'),('402019042896260009','202019042864170019','300019042192720001','xxx','xxx','17133858872','800900000001','xxx','','',4,'2019-04-28 02:09:06','ADD'),('402019042832220017','202019042810810022','300019042192720001','xxx','xxx','17133858872','800900000001','xxx','','',4,'2019-04-28 02:09:06','ADD'),('402019042871510025','202019042809150025','300019042192720001','xxx','xxx','17133858872','800900000001','xxx','','',4,'2019-04-28 02:09:06','ADD'),('402019042831390033','202019042890620028','300019042192720001','xxx','xxx','17133858872','800900000001','xxx','','',4,'2019-04-28 02:09:07','ADD'),('402019042892500040','202019042857010031','300019042192720001','xxx','xxx','17133858872','800900000001','xxx','','',4,'2019-04-28 02:09:07','ADD'),('402019042859260049','202019042803330038','300019042192720001','xxx','xxx','17133858872','800900000001','xxx','','',4,'2019-04-28 02:09:08','ADD'),('402019042892600053','202019042888600034','300019042192720001','xxx','xxx','17133858872','800900000001','xxx','','',4,'2019-04-28 02:09:08','ADD'),('402019042861350065','202019042897110043','300019042192720001','xxx','xxx','17133858872','800900000001','xxx','','',4,'2019-04-28 02:09:08','ADD'),('402019042877090067','202019042832720040','300019042192720001','xxx','xxx','17133858872','800900000001','xxx','','',4,'2019-04-28 02:09:08','ADD'),('402019042800610081','202019042850210049','300019042192720001','xxx','xxx','17133858872','800900000001','xxx','','',4,'2019-04-28 02:09:09','ADD'),('402019042825880088','202019042870660046','300019042192720001','xxx','xxx','17133858872','800900000001','xxx','','',4,'2019-04-28 02:09:10','ADD'),('402019042891860097','202019042897550052','300019042192720001','xxx','xxx','17133858872','800900000001','xxx','','',4,'2019-04-28 02:09:10','ADD'),('402019042873090105','202019042876120055','300019042192720001','xxx','xxx','17133858872','800900000001','xxx','','',4,'2019-04-28 02:09:11','ADD'),('402019042817270113','202019042807020064','300019042192720001','xxx','xxx','17133858872','800900000001','xxx','','',4,'2019-04-28 02:09:12','ADD'),('402019042890260121','202019042819410058','300019042192720001','xxx','xxx','17133858872','800900000001','xxx','','',4,'2019-04-28 02:09:12','ADD'),('402019042817430126','202019042852920061','300019042192720001','xxx','xxx','17133858872','800900000001','xxx','','',4,'2019-04-28 02:09:12','ADD'),('402019042869020136','202019042829820067','300019042192720001','xxx','xxx','17133858872','800900000001','xxx','','',4,'2019-04-28 02:09:13','ADD');

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

insert  into `business_store_attr`(`b_id`,`attr_id`,`store_id`,`spec_cd`,`value`,`month`,`create_time`,`operate`) values ('202019032981850008','112019032904350005','402019032973010004','1001','01',3,'2019-03-29 14:13:26','ADD'),('202019032924200010','112019032945350008','402019032924930007','100201903001','吴学文',3,'2019-03-29 14:21:41','ADD'),('202019032924200010','112019032930420009','402019032924930007','100201903002','55',3,'2019-03-29 14:21:41','ADD'),('202019032924200010','112019032984510010','402019032924930007','100201903003','2019-09-31',3,'2019-03-29 14:21:41','ADD'),('202019032924200010','112019032976770011','402019032924930007','100201903004','城中区派出所',3,'2019-03-29 14:21:41','ADD'),('202019032924200010','112019032981460012','402019032924930007','100201903005','城南新区',3,'2019-03-29 14:21:41','ADD'),('202019033072790004','112019033029610003','402019033005920002','1001','01',3,'2019-03-30 05:32:16','ADD'),('202019033063640004','112019033030100002','402019033051590001','1001','01',3,'2019-03-30 05:56:23','ADD'),('202019033033220002','112019033011270002','402019033064560001','1001','01',3,'2019-03-30 06:51:24','ADD'),('202019033011280002','112019033048910003','402019033060910002','1001','01',3,'2019-03-30 07:47:23','ADD'),('202019033046130002','112019033074490002','402019033011430001','1001','01',3,'2019-03-30 08:04:39','ADD'),('202019033072520002','112019033028520002','402019033089200001','1001','01',3,'2019-03-30 08:31:29','ADD'),('202019033076390007','112019033074130006','402019033091620005','100201903001','吴少华',3,'2019-03-30 08:37:31','ADD'),('202019033076390007','112019033074710007','402019033091620005','100201903002','55',3,'2019-03-30 08:37:31','ADD'),('202019033076390007','112019033049210008','402019033091620005','100201903003','2019-09-31',3,'2019-03-30 08:37:31','ADD'),('202019033076390007','112019033009530009','402019033091620005','100201903004','城中区法院',3,'2019-03-30 08:37:31','ADD'),('202019033076390007','112019033015820010','402019033091620005','100201903005','城中区',3,'2019-03-30 08:37:31','ADD'),('202019040289110004','112019040238070002','402019040288380001','100201903001','张永强',4,'2019-04-01 16:43:00','ADD'),('202019040289110004','112019040223030003','402019040288380001','100201903002','78',4,'2019-04-01 16:43:00','ADD'),('202019040289110004','112019040295210004','402019040288380001','100201903003','2013-08-09',4,'2019-04-01 16:43:00','ADD'),('202019040289110004','112019040284060005','402019040288380001','100201903004','青海省法院',4,'2019-04-01 16:43:00','ADD'),('202019040289110004','112019040233330006','402019040288380001','100201903005','软件类',4,'2019-04-01 16:43:00','ADD'),('202019040231680007','112019040242550010','402019040289200009','100201903001','张永强',4,'2019-04-01 16:45:06','ADD'),('202019040231680007','112019040218590011','402019040289200009','100201903002','78',4,'2019-04-01 16:45:06','ADD'),('202019040231680007','112019040216240012','402019040289200009','100201903003','2013-08-09',4,'2019-04-01 16:45:06','ADD'),('202019040231680007','112019040247160013','402019040289200009','100201903004','青海省法院',4,'2019-04-01 16:45:06','ADD'),('202019040231680007','112019040202100014','402019040289200009','100201903005','软件类',4,'2019-04-01 16:45:06','ADD'),('202019040281460010','112019040203560018','402019040244100017','100201903001','张永强',4,'2019-04-01 16:48:29','ADD'),('202019040281460010','112019040203120019','402019040244100017','100201903002','78',4,'2019-04-01 16:48:29','ADD'),('202019040281460010','112019040256540020','402019040244100017','100201903003','2013-08-09',4,'2019-04-01 16:48:29','ADD'),('202019040281460010','112019040227250021','402019040244100017','100201903004','青海省法院',4,'2019-04-01 16:48:29','ADD'),('202019040281460010','112019040261050022','402019040244100017','100201903005','软件类',4,'2019-04-01 16:48:29','ADD'),('202019040234930002','112019040267790002','402019040287010001','100201903001','张永强',4,'2019-04-01 16:57:22','ADD'),('202019040234930002','112019040239380003','402019040287010001','100201903002','78',4,'2019-04-01 16:57:22','ADD'),('202019040234930002','112019040284120004','402019040287010001','100201903003','2013-08-09',4,'2019-04-01 16:57:22','ADD'),('202019040234930002','112019040275090005','402019040287010001','100201903004','青海省法院',4,'2019-04-01 16:57:22','ADD'),('202019040234930002','112019040217860006','402019040287010001','100201903005','软件类',4,'2019-04-01 16:57:22','ADD'),('202019040243080007','112019040295150010','402019040290830009','100201903001','张啤酒',4,'2019-04-01 17:02:38','ADD'),('202019040243080007','112019040238800011','402019040290830009','100201903002','2',4,'2019-04-01 17:02:38','ADD'),('202019040243080007','112019040241670012','402019040290830009','100201903003','2018-12-12',4,'2019-04-01 17:02:38','ADD'),('202019040243080007','112019040212430013','402019040290830009','100201903004','青海女子监狱',4,'2019-04-01 17:02:38','ADD'),('202019040243080007','112019040270810014','402019040290830009','100201903005','经济开发区',4,'2019-04-01 17:02:38','ADD'),('202019041317610002','112019041378600002','402019041335090001','1001','01',4,'2019-04-13 15:11:13','ADD'),('202019041327040005','112019041394890006','402019041322840005','1001','01',4,'2019-04-13 15:12:27','ADD'),('202019042862250016','112019042818430002','402019042844630001','100201903001','xxx',4,'2019-04-28 02:08:55','ADD'),('202019042862250016','112019042850280003','402019042844630001','100201903002','1000000',4,'2019-04-28 02:08:55','ADD'),('202019042862250016','112019042833220004','402019042844630001','100201903003','2019-04-28',4,'2019-04-28 02:08:55','ADD'),('202019042862250016','112019042863400005','402019042844630001','100201903004','xxx',4,'2019-04-28 02:08:55','ADD'),('202019042862250016','112019042835070006','402019042844630001','100201903005','xxx',4,'2019-04-28 02:08:55','ADD'),('202019042864170019','112019042898920010','402019042896260009','100201903001','xxx',4,'2019-04-28 02:09:06','ADD'),('202019042864170019','112019042850310011','402019042896260009','100201903002','1000000',4,'2019-04-28 02:09:06','ADD'),('202019042864170019','112019042834630012','402019042896260009','100201903003','2019-04-28',4,'2019-04-28 02:09:06','ADD'),('202019042864170019','112019042833560013','402019042896260009','100201903004','xxx',4,'2019-04-28 02:09:06','ADD'),('202019042864170019','112019042808460014','402019042896260009','100201903005','xxx',4,'2019-04-28 02:09:06','ADD'),('202019042810810022','112019042890490018','402019042832220017','100201903001','xxx',4,'2019-04-28 02:09:06','ADD'),('202019042810810022','112019042812790019','402019042832220017','100201903002','1000000',4,'2019-04-28 02:09:06','ADD'),('202019042810810022','112019042811050020','402019042832220017','100201903003','2019-04-28',4,'2019-04-28 02:09:06','ADD'),('202019042810810022','112019042856300021','402019042832220017','100201903004','xxx',4,'2019-04-28 02:09:06','ADD'),('202019042810810022','112019042881220022','402019042832220017','100201903005','xxx',4,'2019-04-28 02:09:06','ADD'),('202019042809150025','112019042840460026','402019042871510025','100201903001','xxx',4,'2019-04-28 02:09:06','ADD'),('202019042809150025','112019042821350027','402019042871510025','100201903002','1000000',4,'2019-04-28 02:09:06','ADD'),('202019042809150025','112019042829780028','402019042871510025','100201903003','2019-04-28',4,'2019-04-28 02:09:06','ADD'),('202019042809150025','112019042889780029','402019042871510025','100201903004','xxx',4,'2019-04-28 02:09:06','ADD'),('202019042809150025','112019042839020030','402019042871510025','100201903005','xxx',4,'2019-04-28 02:09:06','ADD'),('202019042890620028','112019042803750034','402019042831390033','100201903001','xxx',4,'2019-04-28 02:09:07','ADD'),('202019042890620028','112019042820130035','402019042831390033','100201903002','1000000',4,'2019-04-28 02:09:07','ADD'),('202019042890620028','112019042899040036','402019042831390033','100201903003','2019-04-28',4,'2019-04-28 02:09:07','ADD'),('202019042890620028','112019042847670037','402019042831390033','100201903004','xxx',4,'2019-04-28 02:09:07','ADD'),('202019042890620028','112019042885210038','402019042831390033','100201903005','xxx',4,'2019-04-28 02:09:07','ADD'),('202019042857010031','112019042886390041','402019042892500040','100201903001','xxx',4,'2019-04-28 02:09:07','ADD'),('202019042857010031','112019042869250042','402019042892500040','100201903002','1000000',4,'2019-04-28 02:09:07','ADD'),('202019042857010031','112019042811630043','402019042892500040','100201903003','2019-04-28',4,'2019-04-28 02:09:07','ADD'),('202019042857010031','112019042857760044','402019042892500040','100201903004','xxx',4,'2019-04-28 02:09:07','ADD'),('202019042857010031','112019042864830045','402019042892500040','100201903005','xxx',4,'2019-04-28 02:09:07','ADD'),('202019042803330038','112019042840820050','402019042859260049','100201903001','xxx',4,'2019-04-28 02:09:08','ADD'),('202019042803330038','112019042852670051','402019042859260049','100201903002','1000000',4,'2019-04-28 02:09:08','ADD'),('202019042803330038','112019042809960052','402019042859260049','100201903003','2019-04-28',4,'2019-04-28 02:09:08','ADD'),('202019042803330038','112019042809950054','402019042859260049','100201903004','xxx',4,'2019-04-28 02:09:08','ADD'),('202019042888600034','112019042860770055','402019042892600053','100201903001','xxx',4,'2019-04-28 02:09:08','ADD'),('202019042803330038','112019042816690056','402019042859260049','100201903005','xxx',4,'2019-04-28 02:09:08','ADD'),('202019042888600034','112019042812360057','402019042892600053','100201903002','1000000',4,'2019-04-28 02:09:08','ADD'),('202019042888600034','112019042828220059','402019042892600053','100201903003','2019-04-28',4,'2019-04-28 02:09:08','ADD'),('202019042888600034','112019042899790060','402019042892600053','100201903004','xxx',4,'2019-04-28 02:09:08','ADD'),('202019042888600034','112019042858600061','402019042892600053','100201903005','xxx',4,'2019-04-28 02:09:08','ADD'),('202019042897110043','112019042854370066','402019042861350065','100201903001','xxx',4,'2019-04-28 02:09:08','ADD'),('202019042897110043','112019042802080068','402019042861350065','100201903002','1000000',4,'2019-04-28 02:09:08','ADD'),('202019042832720040','112019042858360069','402019042877090067','100201903001','xxx',4,'2019-04-28 02:09:08','ADD'),('202019042897110043','112019042866510070','402019042861350065','100201903003','2019-04-28',4,'2019-04-28 02:09:08','ADD'),('202019042832720040','112019042849850071','402019042877090067','100201903002','1000000',4,'2019-04-28 02:09:08','ADD'),('202019042897110043','112019042884780072','402019042861350065','100201903004','xxx',4,'2019-04-28 02:09:08','ADD'),('202019042832720040','112019042816480073','402019042877090067','100201903003','2019-04-28',4,'2019-04-28 02:09:08','ADD'),('202019042897110043','112019042839980074','402019042861350065','100201903005','xxx',4,'2019-04-28 02:09:08','ADD'),('202019042832720040','112019042807720075','402019042877090067','100201903004','xxx',4,'2019-04-28 02:09:08','ADD'),('202019042832720040','112019042885450077','402019042877090067','100201903005','xxx',4,'2019-04-28 02:09:09','ADD'),('202019042850210049','112019042842270082','402019042800610081','100201903001','xxx',4,'2019-04-28 02:09:09','ADD'),('202019042850210049','112019042894130083','402019042800610081','100201903002','1000000',4,'2019-04-28 02:09:09','ADD'),('202019042850210049','112019042803300084','402019042800610081','100201903003','2019-04-28',4,'2019-04-28 02:09:09','ADD'),('202019042850210049','112019042886920085','402019042800610081','100201903004','xxx',4,'2019-04-28 02:09:09','ADD'),('202019042850210049','112019042821740086','402019042800610081','100201903005','xxx',4,'2019-04-28 02:09:09','ADD'),('202019042870660046','112019042886040089','402019042825880088','100201903001','xxx',4,'2019-04-28 02:09:10','ADD'),('202019042870660046','112019042847940090','402019042825880088','100201903002','1000000',4,'2019-04-28 02:09:10','ADD'),('202019042870660046','112019042864700091','402019042825880088','100201903003','2019-04-28',4,'2019-04-28 02:09:10','ADD'),('202019042870660046','112019042868080092','402019042825880088','100201903004','xxx',4,'2019-04-28 02:09:10','ADD'),('202019042870660046','112019042817230093','402019042825880088','100201903005','xxx',4,'2019-04-28 02:09:10','ADD'),('202019042897550052','112019042868520098','402019042891860097','100201903001','xxx',4,'2019-04-28 02:09:10','ADD'),('202019042897550052','112019042891650099','402019042891860097','100201903002','1000000',4,'2019-04-28 02:09:10','ADD'),('202019042897550052','112019042868420100','402019042891860097','100201903003','2019-04-28',4,'2019-04-28 02:09:10','ADD'),('202019042897550052','112019042827550101','402019042891860097','100201903004','xxx',4,'2019-04-28 02:09:10','ADD'),('202019042897550052','112019042823110102','402019042891860097','100201903005','xxx',4,'2019-04-28 02:09:10','ADD'),('202019042876120055','112019042834970106','402019042873090105','100201903001','xxx',4,'2019-04-28 02:09:11','ADD'),('202019042876120055','112019042836850107','402019042873090105','100201903002','1000000',4,'2019-04-28 02:09:11','ADD'),('202019042876120055','112019042848110108','402019042873090105','100201903003','2019-04-28',4,'2019-04-28 02:09:11','ADD'),('202019042876120055','112019042872230109','402019042873090105','100201903004','xxx',4,'2019-04-28 02:09:11','ADD'),('202019042876120055','112019042868410110','402019042873090105','100201903005','xxx',4,'2019-04-28 02:09:11','ADD'),('202019042807020064','112019042800880114','402019042817270113','100201903001','xxx',4,'2019-04-28 02:09:12','ADD'),('202019042807020064','112019042850250115','402019042817270113','100201903002','1000000',4,'2019-04-28 02:09:12','ADD'),('202019042807020064','112019042847750116','402019042817270113','100201903003','2019-04-28',4,'2019-04-28 02:09:12','ADD'),('202019042807020064','112019042892310117','402019042817270113','100201903004','xxx',4,'2019-04-28 02:09:12','ADD'),('202019042807020064','112019042889350118','402019042817270113','100201903005','xxx',4,'2019-04-28 02:09:12','ADD'),('202019042819410058','112019042899900122','402019042890260121','100201903001','xxx',4,'2019-04-28 02:09:12','ADD'),('202019042819410058','112019042863870123','402019042890260121','100201903002','1000000',4,'2019-04-28 02:09:12','ADD'),('202019042819410058','112019042855890124','402019042890260121','100201903003','2019-04-28',4,'2019-04-28 02:09:12','ADD'),('202019042819410058','112019042802340125','402019042890260121','100201903004','xxx',4,'2019-04-28 02:09:12','ADD'),('202019042819410058','112019042827120127','402019042890260121','100201903005','xxx',4,'2019-04-28 02:09:12','ADD'),('202019042852920061','112019042887550128','402019042817430126','100201903001','xxx',4,'2019-04-28 02:09:12','ADD'),('202019042852920061','112019042842980130','402019042817430126','100201903002','1000000',4,'2019-04-28 02:09:12','ADD'),('202019042852920061','112019042855700131','402019042817430126','100201903003','2019-04-28',4,'2019-04-28 02:09:12','ADD'),('202019042852920061','112019042866850132','402019042817430126','100201903004','xxx',4,'2019-04-28 02:09:12','ADD'),('202019042852920061','112019042821250133','402019042817430126','100201903005','xxx',4,'2019-04-28 02:09:12','ADD'),('202019042829820067','112019042808070137','402019042869020136','100201903001','xxx',4,'2019-04-28 02:09:13','ADD'),('202019042829820067','112019042883900138','402019042869020136','100201903002','1000000',4,'2019-04-28 02:09:13','ADD'),('202019042829820067','112019042860250139','402019042869020136','100201903003','2019-04-28',4,'2019-04-28 02:09:13','ADD'),('202019042829820067','112019042894590140','402019042869020136','100201903004','xxx',4,'2019-04-28 02:09:13','ADD'),('202019042829820067','112019042854450141','402019042869020136','100201903005','xxx',4,'2019-04-28 02:09:13','ADD');

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

insert  into `business_store_cerdentials`(`store_cerdentials_id`,`b_id`,`store_id`,`credentials_cd`,`value`,`validity_period`,`positive_photo`,`negative_photo`,`month`,`create_time`,`operate`) values ('422019032937620006','202019032981850008','402019032973010004','300200900001','632126XXXXXXXX2011','3000-01-01','1234567.jpg','',3,'2019-03-29 14:13:26','ADD'),('422019032958420013','202019032924200010','402019032924930007','300200900001','632126199109162011','2030-10-01','','',3,'2019-03-29 14:21:41','ADD'),('422019033094470004','202019033072790004','402019033005920002','300200900001','632126XXXXXXXX2011','3000-01-01','1234567.jpg','',3,'2019-03-30 05:32:16','ADD'),('422019033049340003','202019033063640004','402019033051590001','300200900001','632126XXXXXXXX2011','3000-01-01','1234567.jpg','',3,'2019-03-30 05:56:23','ADD'),('422019033004800003','202019033033220002','402019033064560001','300200900001','632126XXXXXXXX2011','3000-01-01','1234567.jpg','',3,'2019-03-30 06:51:24','ADD'),('422019033073800004','202019033011280002','402019033060910002','300200900001','632126XXXXXXXX2011','3000-01-01','1234567.jpg','',3,'2019-03-30 07:47:23','ADD'),('422019033088010003','202019033046130002','402019033011430001','300200900001','632126XXXXXXXX2011','3000-01-01','1234567.jpg','',3,'2019-03-30 08:04:39','ADD'),('422019033031850003','202019033072520002','402019033089200001','300200900001','632126XXXXXXXX2011','3000-01-01','1234567.jpg','',3,'2019-03-30 08:31:29','ADD'),('422019033000830011','202019033076390007','402019033091620005','300200900001','632126199109162088','2030-02-09','','',3,'2019-03-30 08:37:31','ADD'),('422019040221330007','202019040289110004','402019040288380001','300200900001','632126199109162099','2019-09-09','','',4,'2019-04-01 16:43:00','ADD'),('422019040258080015','202019040231680007','402019040289200009','300200900001','632126199109162099','2019-09-09','','',4,'2019-04-01 16:45:06','ADD'),('422019040233760023','202019040281460010','402019040244100017','300200900001','632126199109162099','2019-09-09','','',4,'2019-04-01 16:48:29','ADD'),('422019040286960007','202019040234930002','402019040287010001','300200900001','632126199109162099','2019-09-09','','',4,'2019-04-01 16:57:22','ADD'),('422019040215000015','202019040243080007','402019040290830009','300200900001','632126199809162033','2035-09-08','','',4,'2019-04-01 17:02:38','ADD'),('422019041368290003','202019041317610002','402019041335090001','300200900001','632126XXXXXXXX2011','3000-01-01','1234567.jpg','',4,'2019-04-13 15:11:13','ADD'),('422019041364480007','202019041327040005','402019041322840005','300200900001','632126XXXXXXXX2011','3000-01-01','1234567.jpg','',4,'2019-04-13 15:12:27','ADD'),('422019042873500007','202019042862250016','402019042844630001','300200900001','xxx','2119-04-28','','',4,'2019-04-28 02:08:55','ADD'),('422019042899240015','202019042864170019','402019042896260009','300200900001','xxx','2119-04-28','','',4,'2019-04-28 02:09:06','ADD'),('422019042807860023','202019042810810022','402019042832220017','300200900001','xxx','2119-04-28','','',4,'2019-04-28 02:09:06','ADD'),('422019042831960031','202019042809150025','402019042871510025','300200900001','xxx','2119-04-28','','',4,'2019-04-28 02:09:06','ADD'),('422019042842490039','202019042890620028','402019042831390033','300200900001','xxx','2119-04-28','','',4,'2019-04-28 02:09:07','ADD'),('422019042873850046','202019042857010031','402019042892500040','300200900001','xxx','2119-04-28','','',4,'2019-04-28 02:09:07','ADD'),('422019042883320058','202019042803330038','402019042859260049','300200900001','xxx','2119-04-28','','',4,'2019-04-28 02:09:08','ADD'),('422019042866290062','202019042888600034','402019042892600053','300200900001','xxx','2119-04-28','','',4,'2019-04-28 02:09:08','ADD'),('422019042872800076','202019042897110043','402019042861350065','300200900001','xxx','2119-04-28','','',4,'2019-04-28 02:09:08','ADD'),('422019042830520078','202019042832720040','402019042877090067','300200900001','xxx','2119-04-28','','',4,'2019-04-28 02:09:09','ADD'),('422019042869300087','202019042850210049','402019042800610081','300200900001','xxx','2119-04-28','','',4,'2019-04-28 02:09:09','ADD'),('422019042812100094','202019042870660046','402019042825880088','300200900001','xxx','2119-04-28','','',4,'2019-04-28 02:09:10','ADD'),('422019042875860103','202019042897550052','402019042891860097','300200900001','xxx','2119-04-28','','',4,'2019-04-28 02:09:10','ADD'),('422019042825760111','202019042876120055','402019042873090105','300200900001','xxx','2119-04-28','','',4,'2019-04-28 02:09:11','ADD'),('422019042829710119','202019042807020064','402019042817270113','300200900001','xxx','2119-04-28','','',4,'2019-04-28 02:09:12','ADD'),('422019042832350129','202019042819410058','402019042890260121','300200900001','xxx','2119-04-28','','',4,'2019-04-28 02:09:12','ADD'),('422019042857470134','202019042852920061','402019042817430126','300200900001','xxx','2119-04-28','','',4,'2019-04-28 02:09:12','ADD'),('422019042807680142','202019042829820067','402019042869020136','300200900001','xxx','2119-04-28','','',4,'2019-04-28 02:09:13','ADD');

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

/*Table structure for table `business_store_user` */

DROP TABLE IF EXISTS `business_store_user`;

CREATE TABLE `business_store_user` (
  `store_user_id` varchar(30) NOT NULL COMMENT '代理商用户ID',
  `store_id` varchar(30) NOT NULL COMMENT '代理商ID',
  `b_id` varchar(30) NOT NULL COMMENT '业务Id',
  `user_id` varchar(30) NOT NULL COMMENT '用户ID',
  `rel_cd` varchar(30) NOT NULL COMMENT '用户和代理商关系 详情查看 agent_user_rel表',
  `month` int(11) NOT NULL COMMENT '月份',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `operate` varchar(3) NOT NULL COMMENT '数据状态，添加ADD，修改MOD 删除DEL'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `business_store_user` */

insert  into `business_store_user`(`store_user_id`,`store_id`,`b_id`,`user_id`,`rel_cd`,`month`,`create_time`,`operate`) values ('null2019033029230001','402019032924930007','202019033013440002','','600311000001',3,'2019-03-30 04:04:38','ADD'),('null2019033049030001','402019032924930007','202019033086100002','','600311000001',3,'2019-03-30 05:17:15','ADD'),('452019033088370001','-1','202019033050340003','30518940136629616640','600311000001',3,'2019-03-30 07:47:23','ADD'),('452019033087150004','402019033011430001','202019033046310003','30518940136629616640','600311000001',3,'2019-03-30 08:04:39','ADD'),('452019033038670004','402019033089200001','202019033081420003','30518940136629616640','600311000001',3,'2019-03-30 08:31:29','ADD'),('452019033054120012','402019033091620005','202019033069920008','302019033054910001','600311000001',3,'2019-03-30 08:37:31','ADD'),('452019040265580008','402019040288380001','202019040228000005','302019040270260001','600311000001',4,'2019-04-01 16:43:01','ADD'),('452019040205330016','402019040289200009','202019040204720008','302019040270260001','600311000001',4,'2019-04-01 16:45:06','ADD'),('452019040261310024','402019040244100017','202019040247210011','302019040270260001','600311000001',4,'2019-04-01 16:48:29','ADD'),('452019040229440008','402019040287010001','202019040244340003','302019040270260001','600311000001',4,'2019-04-01 16:57:22','ADD'),('452019040227320016','402019040290830009','202019040270570008','302019040265910001','600311000001',4,'2019-04-01 17:02:38','ADD'),('452019040293800017','402019032924930007','202019040259660010','','600311000001',4,'2019-04-02 09:25:16','ADD'),('452019040216260001','402019032924930007','202019040227860003','302019040246120002','600311000002',4,'2019-04-02 11:36:44','ADD'),('452019040369300001','402019032924930007','202019040356450003','302019040317140001','600311000002',4,'2019-04-03 02:24:54','ADD'),('452019040398910002','402019032924930007','202019040389340006','302019040366990002','600311000002',4,'2019-04-03 02:27:23','ADD'),('452019040395580001','402019032924930007','202019040346960003','302019040367880001','600311000002',4,'2019-04-03 03:12:05','ADD'),('452019040392660001','402019032924930007','202019040376180005','302019040356630001','600311000002',4,'2019-04-03 14:50:57','ADD'),('452019040307490002','402019032924930007','202019040342840008','302019040332500002','600311000002',4,'2019-04-03 14:51:56','ADD'),('452019040347590003','402019032924930007','202019040367530011','302019040343530003','600311000002',4,'2019-04-03 14:52:54','ADD'),('452019040357990004','402019032924930007','202019040320970014','302019040356300004','600311000002',4,'2019-04-03 14:53:47','ADD'),('452019040393370005','402019032924930007','202019040397350017','302019040355650005','600311000002',4,'2019-04-03 14:54:59','ADD'),('452019040372570006','402019032924930007','202019040334540020','302019040339750006','600311000002',4,'2019-04-03 14:55:54','ADD'),('452019040395580001','402019032924930007','202019040491800002','302019040367880001','600311000002',4,'2019-04-04 08:09:32','DEl'),('452019040395580001','402019032924930007','202019040488730005','302019040367880001','600311000002',4,'2019-04-04 08:14:03','DEl'),('452019040395580001','402019032924930007','202019040438610002','302019040367880001','600311000002',4,'2019-04-04 08:22:38','DEl'),('452019040395580001','402019032924930007','202019040459910005','302019040367880001','600311000002',4,'2019-04-04 08:25:49','DEl'),('452019040395580001','402019032924930007','202019040406410008','302019040367880001','600311000002',4,'2019-04-04 08:35:54','DEl'),('452019040395580001','402019032924930007','202019040400180011','302019040367880001','600311000002',4,'2019-04-04 08:39:35','DEl'),('452019040369300001','402019032924930007','202019040479990014','302019040317140001','600311000002',4,'2019-04-04 08:39:58','DEl'),('452019040456490001','402019032924930007','202019040496010018','302019040422250001','600311000002',4,'2019-04-04 08:40:57','ADD'),('452019040426520001','402019032924930007','202019040494260003','302019040481060001','600311000002',4,'2019-04-04 14:24:57','ADD'),('452019033038670004','402019032924930007','202019041376160004','30518940136629616640','600311000001',4,'2019-04-13 04:50:25','DEl'),('452019041305490004','402019041335090001','202019041375730003','30518940136629616640','600311000001',4,'2019-04-13 15:11:13','ADD'),('452019041336750008','402019041322840005','202019041318030006','30518940136629616640','600311000001',4,'2019-04-13 15:12:27','ADD'),('452019041888240001','402019032924930007','202019041810750003','302019041824320001','600311000002',4,'2019-04-18 02:20:04','ADD'),('452019042191050001','402019032924930007','202019042139970003','302019042192720001','600311000002',4,'2019-04-21 10:04:02','ADD'),('452019042886910008','402019042844630001','202019042892790017','300019042192720001','600311000001',4,'2019-04-28 02:08:55','ADD'),('452019042859590016','402019042896260009','202019042813460020','300019042192720001','600311000001',4,'2019-04-28 02:09:06','ADD'),('452019042854370024','402019042832220017','202019042879890023','300019042192720001','600311000001',4,'2019-04-28 02:09:06','ADD'),('452019042856970032','402019042871510025','202019042821020026','300019042192720001','600311000001',4,'2019-04-28 02:09:06','ADD'),('452019042852570047','402019042831390033','202019042874650029','300019042192720001','600311000001',4,'2019-04-28 02:09:07','ADD'),('452019042829530048','402019042892500040','202019042885450032','300019042192720001','600311000001',4,'2019-04-28 02:09:07','ADD'),('452019042877420063','402019042859260049','202019042893500039','300019042192720001','600311000001',4,'2019-04-28 02:09:08','ADD'),('452019042883290064','402019042892600053','202019042820240035','300019042192720001','600311000001',4,'2019-04-28 02:09:08','ADD'),('452019042834270079','402019042877090067','202019042888960041','300019042192720001','600311000001',4,'2019-04-28 02:09:09','ADD'),('452019042869240080','402019042861350065','202019042836610044','300019042192720001','600311000001',4,'2019-04-28 02:09:09','ADD'),('452019042820940095','402019042825880088','202019042888030047','300019042192720001','600311000001',4,'2019-04-28 02:09:10','ADD'),('452019042851520096','402019042800610081','202019042806640050','300019042192720001','600311000001',4,'2019-04-28 02:09:10','ADD'),('452019042844860104','402019042891860097','202019042813420053','300019042192720001','600311000001',4,'2019-04-28 02:09:10','ADD'),('452019042854350112','402019042873090105','202019042899920056','300019042192720001','600311000001',4,'2019-04-28 02:09:11','ADD'),('452019042853400120','402019042817270113','202019042848480065','300019042192720001','600311000001',4,'2019-04-28 02:09:12','ADD'),('452019042866050135','402019042890260121','202019042842350059','300019042192720001','600311000001',4,'2019-04-28 02:09:12','ADD'),('452019042859780143','402019042869020136','202019042839280068','300019042192720001','600311000001',4,'2019-04-28 02:09:13','ADD'),('452019042892920144','402019042817430126','202019042830960062','300019042192720001','600311000001',4,'2019-04-28 02:09:13','ADD'),('452019042830310145','402019032924930007','202019042882310071','302019042846260005','600311000002',4,'2019-04-28 06:59:56','ADD'),('452019052162080001','402019032924930007','202019052168890003','302019052133990001','600311000002',5,'2019-05-21 07:49:48','ADD'),('452019052162080001','402019032924930007','202019052175270005','302019052133990001','600311000002',5,'2019-05-21 07:58:03','DEl'),('452019040347590003','402019032924930007','202019053123400034','302019040343530003','600311000002',5,'2019-05-31 15:31:37','DEl'),('452019040426520001','402019032924930007','202019060495500005','302019040481060001','600311000002',6,'2019-06-04 05:20:55','DEl'),('452019060657890001','402019032924930007','202019060647320011','302019060653180001','600311000002',6,'2019-06-06 05:08:56','ADD'),('452019040216260001','402019032924930007','202019060696860015','302019040246120002','600311000002',6,'2019-06-06 05:09:43','DEl');

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
  `tel` varchar(11) NOT NULL,
  `level_cd` varchar(2) NOT NULL DEFAULT '0' COMMENT '用户级别,关联user_level',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `operate` varchar(3) NOT NULL COMMENT '数据状态，添加ADD，修改MOD 删除DEL',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=122 DEFAULT CHARSET=utf8;

/*Data for the table `business_user` */

insert  into `business_user`(`id`,`b_id`,`user_id`,`name`,`email`,`address`,`password`,`location_cd`,`age`,`sex`,`tel`,`level_cd`,`create_time`,`operate`) values (1,'20516329683810271232','30516329684078706688','张三','928255095@qq.com',NULL,'ERCBHDUYFJDNDHDJDNDJDHDUDHDJDDKDK',NULL,NULL,'0','17797173943','0','2018-11-25 11:09:49','ADD'),(2,'20516332294412189696','30516332294873563136','李四','928255095@qq.com',NULL,'ERCBHDUYFJDNDHDJDNDJDHDUDHDJDDKDK',NULL,NULL,'0','17797173943','0','2018-11-25 11:20:12','ADD'),(3,'20516364589907066880','30516364590414577664','王五','928255095@qq.com',NULL,'ERCBHDUYFJDNDHDJDNDJDHDUDHDJDDKDK',NULL,NULL,'0','17797173943','0','2018-11-25 13:28:31','ADD'),(4,'20516365201587585024','30516365201923129344','王五1','928255095@qq.com',NULL,'ERCBHDUYFJDNDHDJDNDJDHDUDHDJDDKDK',NULL,NULL,'0','17797173943','0','2018-11-25 13:30:57','ADD'),(5,'20516366112951123968','30516366113219559424','王五2','928255095@qq.com',NULL,'ERCBHDUYFJDNDHDJDNDJDHDUDHDJDDKDK',NULL,NULL,'0','17797173943','0','2018-11-25 13:34:35','ADD'),(6,'20516366708928167936','30516366709171437568','王五3','928255095@qq.com',NULL,'ERCBHDUYFJDNDHDJDNDJDHDUDHDJDDKDK',NULL,NULL,'0','17797173943','0','2018-11-25 13:36:57','ADD'),(7,'20516368306257543168','30516368306509201408','王五4','928255095@qq.com',NULL,'ERCBHDUYFJDNDHDJDNDJDHDUDHDJDDKDK',NULL,NULL,'0','17797173943','0','2018-11-25 13:43:17','ADD'),(8,'20516374464208846848','30516374464544391168','王五5','928255095@qq.com',NULL,'ERCBHDUYFJDNDHDJDNDJDHDUDHDJDDKDK',NULL,NULL,'0','17797173943','0','2018-11-25 14:07:46','ADD'),(9,'20516374772574076928','30516374772855095296','王五6','928255095@qq.com',NULL,'ERCBHDUYFJDNDHDJDNDJDHDUDHDJDDKDK',NULL,NULL,'0','17797173943','0','2018-11-25 14:08:59','ADD'),(10,'20516374801669963776','30516374801938399232','王五7','928255095@qq.com',NULL,'ERCBHDUYFJDNDHDJDNDJDHDUDHDJDDKDK',NULL,NULL,'0','17797173943','0','2018-11-25 14:09:06','ADD'),(11,'20516377393317822464','30516377393741447168','王五7','928255095@qq.com',NULL,'ERCBHDUYFJDNDHDJDNDJDHDUDHDJDDKDK',NULL,NULL,'0','17797173943','0','2018-11-25 14:19:24','ADD'),(12,'20516381939620397056','30516381939989495808','王五8','928255095@qq.com',NULL,'ERCBHDUYFJDNDHDJDNDJDHDUDHDJDDKDK',NULL,NULL,'0','17797173943','0','2018-11-25 14:37:28','ADD'),(13,'20516387722659643392','30516387723230068736','王五9','928255095@qq.com',NULL,'ERCBHDUYFJDNDHDJDNDJDHDUDHDJDDKDK',NULL,NULL,'0','17797173943','0','2018-11-25 15:00:27','ADD'),(14,'20516389224736374784','30516389225004810240','王五10','928255095@qq.com',NULL,'ERCBHDUYFJDNDHDJDNDJDHDUDHDJDDKDK',NULL,NULL,'0','17797173943','0','2018-11-25 15:06:25','ADD'),(15,'20516389265110745088','30516389265349820416','王五11','928255095@qq.com',NULL,'ERCBHDUYFJDNDHDJDNDJDHDUDHDJDDKDK',NULL,NULL,'0','17797173943','0','2018-11-25 15:06:34','ADD'),(16,'20516398820167270400','30516398820397957120','吴学文','',NULL,'0724094fbcd70db0493034daa2120aa1',NULL,NULL,'0','15897089471','0','2018-11-25 15:44:33','ADD'),(17,'20516400356243030016','30516400356482105344','师师','',NULL,'f1ab59f367854c555a84d42155d39aa3',NULL,NULL,'0','18987095720','0','2018-11-25 15:50:39','ADD'),(18,'20516401273893830656','30516401274132905984','张试卷','',NULL,'cdfea6fbffcd82758eec12a96c64e682',NULL,NULL,'0','15897089471','0','2018-11-25 15:54:18','ADD'),(19,'20517072482935521280','30517072483300425728','张三','',NULL,'dc2263f093f3db5edd6ce5ea6aa81c32',NULL,NULL,'0','17797173947','0','2018-11-27 12:21:26','ADD'),(20,'20517086035457359872','30517086035897761792','吴学文123','',NULL,'c91cf5db90773073a558e8740521c01c',NULL,NULL,'0','17797173942','0','2018-11-27 13:15:17','ADD'),(21,'20517710159825354752','30517710166884368384','王五112','928255095@qq.com',NULL,'ERCBHDUYFJDNDHDJDNDJDHDUDHDJDDKDK',NULL,NULL,'0','17797173943','0','2018-11-29 06:35:22','ADD'),(22,'20518939876956061696','30518939884421922816','admin','',NULL,'23a39fd21728292e1f794b250bc67019',NULL,NULL,'0','17797173944','0','2018-12-02 16:01:50','ADD'),(23,'20518940136344403968','30518940136629616640','wuxw','',NULL,'0bda701c63916b42e71851214373fe1b',NULL,NULL,'0','17797173940','0','2018-12-02 16:02:50','ADD'),(24,'20520751770314489856','30520751775595118592','zhangsan','',NULL,'b8ed29901921a8e7a8ec34d874a5379b',NULL,NULL,'0','17797173942','0','2018-12-07 16:01:38','ADD'),(25,'20520752094244782080','30520752094488051712','zhangsan','',NULL,'b8ed29901921a8e7a8ec34d874a5379b',NULL,NULL,'0','17797173942','0','2018-12-07 16:02:54','ADD'),(26,'20520752515906551808','30520752516195958784','zhangsan','',NULL,'b8ed29901921a8e7a8ec34d874a5379b',NULL,NULL,'0','17797173942','0','2018-12-07 16:04:34','ADD'),(27,'20520752654096285696','30520752654427635712','zhangsan','',NULL,'b8ed29901921a8e7a8ec34d874a5379b',NULL,NULL,'0','17797173942','0','2018-12-07 16:05:07','ADD'),(28,'20520753111019569152','30520753111732600832','lisi','',NULL,'f8b789267d0ea8414dd9dea3f88d2e66',NULL,NULL,'0','17797173942','0','2018-12-07 16:06:56','ADD'),(29,'20521002816139968512','30521002824033648640','admin','',NULL,'cdfea6fbffcd82758eec12a96c64e682',NULL,NULL,'0','15963245875','00','2018-12-08 08:39:13','ADD'),(30,'20521125064184184832','30521125069510950912','wuxw','928255095@qq.com',NULL,'cdfea6fbffcd82758eec12a96c64e682',NULL,NULL,'0','17797289806','01','2018-12-08 16:44:58','ADD'),(31,'20521292250362167296','30521125069510950912','wuxw','928255095@qq.com',NULL,'cdfea6fbffcd82758eec12a96c64e682',NULL,NULL,'0','17797289806','01','2018-12-09 03:49:19','DEl'),(32,'20521294334742511616','30521125069510950912','wuxw','928255095@qq.com',NULL,'cdfea6fbffcd82758eec12a96c64e682',NULL,NULL,'0','17797289806','01','2018-12-09 03:57:36','DEl'),(33,'20521294983668449280','30521125069510950912','wuxw','928255095@qq.com',NULL,'cdfea6fbffcd82758eec12a96c64e682',NULL,NULL,'0','17797289806','01','2018-12-09 04:00:09','ADD'),(34,'20521299093675327488','30521299095785062400','wangym','928255095@qq.com',NULL,'cdfea6fbffcd82758eec12a96c64e682',NULL,NULL,'0','17797289806','01','2018-12-09 04:16:29','ADD'),(35,'20521384632478875648','30521384632806031360','liyongxi','928255095@qq.com',NULL,'cdfea6fbffcd82758eec12a96c64e682',NULL,NULL,'0','17797289806','01','2018-12-09 09:56:23','ADD'),(36,'20521384655455272960','30521384655744679936','zhangshan','928255095@qq.com',NULL,'cdfea6fbffcd82758eec12a96c64e682',NULL,NULL,'0','17797289806','01','2018-12-09 09:56:28','ADD'),(37,'20521384680210055168','30521384680415576064','lisi','928255095@qq.com',NULL,'cdfea6fbffcd82758eec12a96c64e682',NULL,NULL,'0','17797289806','01','2018-12-09 09:56:34','ADD'),(38,'20521384772962893824','30521384773189386240','wangmazi','928255095@qq.com',NULL,'cdfea6fbffcd82758eec12a96c64e682',NULL,NULL,'0','17797289806','01','2018-12-09 09:56:56','ADD'),(39,'20521384859759820800','30521384859990507520','吴学文','928255095@qq.com',NULL,'cdfea6fbffcd82758eec12a96c64e682',NULL,NULL,'0','17797289806','01','2018-12-09 09:57:17','ADD'),(40,'20521388278868361216','30521388279069687808','吴学文1','928255095@qq.com',NULL,'cdfea6fbffcd82758eec12a96c64e682',NULL,NULL,'0','17797289806','01','2018-12-09 10:10:52','ADD'),(41,'20521388292466294784','30521388292680204288','吴学文2','928255095@qq.com',NULL,'cdfea6fbffcd82758eec12a96c64e682',NULL,NULL,'0','17797289806','01','2018-12-09 10:10:55','ADD'),(42,'20521388308534673408','30521388308752777216','吴学文3','928255095@qq.com',NULL,'cdfea6fbffcd82758eec12a96c64e682',NULL,NULL,'0','17797289806','01','2018-12-09 10:10:59','ADD'),(43,'20521388322648506368','30521388322879193088','吴学文4','928255095@qq.com',NULL,'cdfea6fbffcd82758eec12a96c64e682',NULL,NULL,'0','17797289806','01','2018-12-09 10:11:03','ADD'),(44,'20521388335667625984','30521388335978004480','吴学文5','928255095@qq.com',NULL,'cdfea6fbffcd82758eec12a96c64e682',NULL,NULL,'0','17797289806','01','2018-12-09 10:11:06','ADD'),(45,'20521470366867013632','30521470367152226304','王永梅','406943871@qq.com','青海省西宁市城中区申宁路6号','cdfea6fbffcd82758eec12a96c64e682',NULL,NULL,'1','17797173943','01','2018-12-09 15:37:03','ADD'),(46,'20521470728747368448','30521470728940306432','张三丰','123456@qq.com','','cdfea6fbffcd82758eec12a96c64e682',NULL,NULL,'0','18987898111','01','2018-12-09 15:38:30','ADD'),(47,'20521470972776169472','30521470972990078976','李玉刚','0908777@qq.com','','cdfea6fbffcd82758eec12a96c64e682',NULL,NULL,'1','18987164563','01','2018-12-09 15:39:28','ADD'),(48,'20521471733417394176','30521471733694218240','张学良','9088@qq.com','东北老爷们山','cdfea6fbffcd82758eec12a96c64e682',NULL,NULL,'0','17797173945','01','2018-12-09 15:42:29','ADD'),(49,'20521472810674044928','30521472810921508864','qhadmin','',NULL,'cdfea6fbffcd82758eec12a96c64e682',NULL,NULL,'0','17797173940','00','2018-12-09 15:46:46','ADD'),(50,'20521692192004128768','30521125069510950912','wuxw','928255095@qq.com',NULL,'cdfea6fbffcd82758eec12a96c64e682',NULL,NULL,'0','17797289806','01','2018-12-10 06:18:31','DEl'),(51,'20521692260908154880','30521299095785062400','wangym','928255095@qq.com',NULL,'cdfea6fbffcd82758eec12a96c64e682',NULL,NULL,'0','17797289806','01','2018-12-10 06:18:47','DEl'),(52,'20521692315283111936','30521299095785062400','wangym','928255095@qq.com',NULL,'cdfea6fbffcd82758eec12a96c64e682',NULL,NULL,'0','17797289806','01','2018-12-10 06:19:00','ADD'),(53,'20521692341828861952','30521125069510950912','wuxw','928255095@qq.com',NULL,'cdfea6fbffcd82758eec12a96c64e682',NULL,NULL,'0','17797289806','01','2018-12-10 06:19:06','ADD'),(54,'20521692506895695872','30521125069510950912','wuxw','928255095@qq.com',NULL,'cdfea6fbffcd82758eec12a96c64e682',NULL,NULL,'0','17797289806','01','2018-12-10 06:19:46','DEl'),(55,'20521692649367814144','30521125069510950912','wuxw','928255095@qq.com',NULL,'cdfea6fbffcd82758eec12a96c64e682',NULL,NULL,'0','17797289806','01','2018-12-10 06:20:20','ADD'),(56,'20522549147056750592','30522549147283243008','张事假','928255095@qq.com','','cdfea6fbffcd82758eec12a96c64e682',NULL,NULL,'0','18997089471','01','2018-12-12 15:03:45','ADD'),(57,'20523565515797446656','30523565516132990976','jiejie','jiejie@qq.com','','cdfea6fbffcd82758eec12a96c64e682',NULL,NULL,'1','17797173948','01','2018-12-15 10:22:26','ADD'),(58,'202019020200000002','3020190202000001','吴梓豪','928255094@qq.com','青海省','cdfea6fbffcd82758eec12a96c64e682',NULL,NULL,'0','17797173942','01','2019-02-02 13:40:27','ADD'),(59,'202019020200000003','3020190202000001','吴梓豪','928255094@qq.com','青海省','cdfea6fbffcd82758eec12a96c64e682',NULL,NULL,'0','17797173942','01','2019-02-02 14:00:35','DEl'),(60,'202019020300000003','30521388308752777216','吴学文3','928255095@qq.com',NULL,'cdfea6fbffcd82758eec12a96c64e682',NULL,NULL,'0','17797289806','01','2019-02-02 16:00:09','DEl'),(61,'202019021000000009','3020190210000001','吴学文89','928255095@qq.com',NULL,'cdfea6fbffcd82758eec12a96c64e682',NULL,NULL,'0','17797289807','01','2019-02-09 16:57:52','ADD'),(62,'202019021070180002','3020190210000002','吴学文90','928255095@qq.com',NULL,'cdfea6fbffcd82758eec12a96c64e682',NULL,NULL,'0','17797289807','01','2019-02-09 17:17:48','ADD'),(63,'202019021058060004','3020190210000003','吴学文91','928255095@qq.com',NULL,'cdfea6fbffcd82758eec12a96c64e682',NULL,NULL,'0','17797289807','01','2019-02-09 17:23:15','ADD'),(64,'202019021001030002','30521384632806031360','liyongxi','928255095@qq.com',NULL,'cdfea6fbffcd82758eec12a96c64e682',NULL,NULL,'0','17797289806','01','2019-02-10 14:35:39','DEl'),(65,'202019021096440004','30521384632806031360','liyongxi','928255095@qq.com',NULL,'cdfea6fbffcd82758eec12a96c64e682',NULL,NULL,'0','17797289806','01','2019-02-10 14:35:54','ADD'),(66,'202019021059530006','302019021030610001','张时强','wuxw7@www.com','甘肃省金昌市','cdfea6fbffcd82758eec12a96c64e682',NULL,NULL,'0','18909789184','01','2019-02-10 14:37:01','ADD'),(67,'202019021067580008','302019021020990002','cc789','',NULL,'bd06b3bdf498d5fecd1f83964c925750',NULL,NULL,'0','18909871234','00','2019-02-10 14:38:23','ADD'),(68,'202019032344890002','302019032346330001','王五1123','928255095@qq.com',NULL,'ERCBHDUYFJDNDHDJDNDJDHDUDHDJDDKDK',NULL,NULL,'0','17797173948','00','2019-03-23 08:59:28','ADD'),(69,'202019032587620006','302019032517520003','zhangqq',NULL,NULL,'3b85f4fb07881869aae2fa7fb1994580',NULL,NULL,NULL,'18987095720','00','2019-03-25 08:30:52','ADD'),(70,'202019032531280008','302019032508330004','feitao',NULL,NULL,'515edae4c4a80249d6801a2015ccbd68',NULL,NULL,NULL,'17797173947','00','2019-03-25 08:34:56','ADD'),(71,'202019032517680010','302019032567920005','adminc',NULL,NULL,'90007ba81043076df2f241cebf95b18c',NULL,NULL,NULL,'17797173947','00','2019-03-25 15:55:33','ADD'),(72,'202019033083450005','302019033054910001','996icu',NULL,NULL,'e1b2ed71460529672e74f31a307bfe63',NULL,NULL,NULL,'18909780341','00','2019-03-30 08:34:54','ADD'),(73,'202019040291880002','302019040270260001','zhangx',NULL,NULL,'fe6eab9f2b73152fb8948c5446e553fd',NULL,NULL,NULL,'18909785209','00','2019-04-01 16:40:49','ADD'),(74,'202019040287710005','302019040265910001','zhangb',NULL,NULL,'360de47da6aff8bcace8fdbfc3c8076f',NULL,NULL,NULL,'17797173985','00','2019-04-01 17:00:43','ADD'),(75,'202019040270560002','302019040231400001','user1',NULL,NULL,'5b7ac7ba433dc0842fb8d3bc15e4b2aa',NULL,NULL,NULL,'18300862162','00','2019-04-02 09:57:46','ADD'),(76,'202019040256780002','302019040246120002','王志超1','928255095@qq.com','青海省西宁市','cdfea6fbffcd82758eec12a96c64e682',NULL,NULL,'0','18909783333','01','2019-04-02 11:36:44','ADD'),(77,'202019040314020002','302019040317140001','shishi','928255095@qq.com','西宁','cdfea6fbffcd82758eec12a96c64e682',NULL,NULL,'0','17797173944','01','2019-04-03 02:24:54','ADD'),(78,'202019040303420005','302019040366990002','任建宇1','7898@qq.com','西宁','cdfea6fbffcd82758eec12a96c64e682',NULL,NULL,'0','17797173989','01','2019-04-03 02:27:23','ADD'),(79,'202019040372460002','302019040367880001','zhichao','987898@qq.com','西宁市','5b7ac7ba433dc0842fb8d3bc15e4b2aa',NULL,NULL,'0','17797350001','01','2019-04-03 03:12:04','ADD'),(80,'202019040363750002','302019040317140001','shishi','928255095@qq.com','西宁','cdfea6fbffcd82758eec12a96c64e682',NULL,NULL,'0','17797173944','01','2019-04-03 07:45:36','DEl'),(81,'202019040363750002','302019040317140001','shishi1','9282550951@qq.com','西宁1',NULL,NULL,NULL,'1','17797173942','01','2019-04-03 07:45:36','ADD'),(82,'202019040335310002','302019040367880001','zhichao','987898@qq.com','西宁市','5b7ac7ba433dc0842fb8d3bc15e4b2aa',NULL,NULL,'0','17797350001','01','2019-04-03 08:09:49','DEl'),(83,'202019040335310002','302019040367880001','zhichao1','9878981@qq.com','西宁市1',NULL,NULL,NULL,'1','17797350002','01','2019-04-03 08:09:49','ADD'),(84,'202019040306710004','302019040356630001','王咏梅','406943871@qq.com','青海省西宁市','5b7ac7ba433dc0842fb8d3bc15e4b2aa',NULL,NULL,'1','17797154305','01','2019-04-03 14:50:56','ADD'),(85,'202019040304510007','302019040332500002','玉波','yubo@163.com','西双版纳','5b7ac7ba433dc0842fb8d3bc15e4b2aa',NULL,NULL,'1','17797173884','01','2019-04-03 14:51:56','ADD'),(86,'202019040339540010','302019040343530003','玉儿香','yuerxiang@163.com','西双版纳','5b7ac7ba433dc0842fb8d3bc15e4b2aa',NULL,NULL,'1','15597174306','01','2019-04-03 14:52:54','ADD'),(87,'202019040333220013','302019040356300004','布鲁漂','bulupiao@163.com','西双版纳','5b7ac7ba433dc0842fb8d3bc15e4b2aa',NULL,NULL,'0','18997096269','01','2019-04-03 14:53:47','ADD'),(88,'202019040313040016','302019040355650005','徐远达','xuyuanda@163.com','上海','5b7ac7ba433dc0842fb8d3bc15e4b2aa',NULL,NULL,'0','18909788448','01','2019-04-03 14:54:59','ADD'),(89,'202019040399500019','302019040339750006','徐浩宁','xuhaoning@163.com','上海','5b7ac7ba433dc0842fb8d3bc15e4b2aa',NULL,NULL,'0','18976544567','01','2019-04-03 14:55:54','ADD'),(90,'202019040469410022','302019040367880001','zhichao1','9878981@qq.com','西宁市1','5b7ac7ba433dc0842fb8d3bc15e4b2aa',NULL,NULL,'1','17797350002','01','2019-04-03 16:02:16','DEl'),(91,'202019040469410022','302019040367880001','zhichao2','9878981@qq.com','西宁市1',NULL,NULL,NULL,'1','17797350002','01','2019-04-03 16:02:16','ADD'),(92,'202019040458180024','302019040366990002','任建宇1','7898@qq.com','西宁','cdfea6fbffcd82758eec12a96c64e682',NULL,NULL,'0','17797173989','01','2019-04-03 16:04:30','DEl'),(93,'202019040458180024','302019040366990002','任建宇2','7898@qq.com','西宁',NULL,NULL,NULL,'0','17797173989','01','2019-04-03 16:04:30','ADD'),(94,'202019040427900003','302019040367880001','zhichao2','9878981@qq.com','西宁市1','5b7ac7ba433dc0842fb8d3bc15e4b2aa',NULL,NULL,'1','17797350002','01','2019-04-04 08:22:39','DEl'),(95,'202019040442220012','302019040367880001','zhichao2','9878981@qq.com','西宁市1','5b7ac7ba433dc0842fb8d3bc15e4b2aa',NULL,NULL,'1','17797350002','01','2019-04-04 08:39:35','DEl'),(96,'202019040445720015','302019040317140001','shishi1','9282550951@qq.com','西宁1','cdfea6fbffcd82758eec12a96c64e682',NULL,NULL,'1','17797173942','01','2019-04-04 08:39:58','DEl'),(97,'202019040434790017','302019040422250001','师延俊1','shiyj@163.com','城东区','5b7ac7ba433dc0842fb8d3bc15e4b2aa',NULL,NULL,'0','18907788888','01','2019-04-04 08:40:57','ADD'),(98,'202019040471000002','302019040481060001','罗比特','luobite@163.com','西双版纳','5b7ac7ba433dc0842fb8d3bc15e4b2aa',NULL,NULL,'1','18909786789','01','2019-04-04 14:24:57','ADD'),(99,'202019040585760005','302019040481060001','罗比特','luobite@163.com','西双版纳','5b7ac7ba433dc0842fb8d3bc15e4b2aa',NULL,NULL,'1','18909786789','01','2019-04-05 13:17:10','DEl'),(100,'202019040585760005','302019040481060001','罗比特1','luobite@163.com','西双版纳',NULL,NULL,NULL,'1','18909786789','01','2019-04-05 13:17:10','ADD'),(101,'202019041120660002','302019041110350001','zwff',NULL,NULL,'5b7ac7ba433dc0842fb8d3bc15e4b2aa',NULL,NULL,NULL,'15695200291','00','2019-04-11 08:07:54','ADD'),(102,'202019041340460005','30518940136629616640','wuxw','',NULL,'3ad352384261cbc2c7462210dbb3ce61',NULL,NULL,'0','17797173940','02','2019-04-13 04:50:25','DEl'),(103,'202019041801200002','302019041824320001','钟原','zongyuan@163.com','中国反恐特战队猎鹰组','5b7ac7ba433dc0842fb8d3bc15e4b2aa',NULL,NULL,'0','18909786666','01','2019-04-18 02:20:03','ADD'),(104,'202019042126440002','302019042192720001','毛彬彬','maobinbin@163.com','青海省西宁市城中区','5b7ac7ba433dc0842fb8d3bc15e4b2aa',NULL,NULL,'0','18909789999','01','2019-04-21 10:04:01','ADD'),(105,'202019042771400011','302019042771470001','q\'q\'q',NULL,NULL,'fbd7b2884b79aa4c9bc971911eaf31b7',NULL,NULL,NULL,'18909764563','00','2019-04-27 10:43:09','ADD'),(106,'202019042887970070','302019042846260005','孙悟空','sunwk@163.com','花果山水莲洞','5b7ac7ba433dc0842fb8d3bc15e4b2aa',NULL,NULL,'0','18907889800','01','2019-04-28 06:59:56','ADD'),(107,'202019051147830012','302019051126800001','hewei',NULL,NULL,'5b7ac7ba433dc0842fb8d3bc15e4b2aa',NULL,NULL,NULL,'15919163134','00','2019-05-11 13:41:21','ADD'),(108,'202019051151120014','302019051126240002','tom-01',NULL,NULL,'5b7ac7ba433dc0842fb8d3bc15e4b2aa',NULL,NULL,NULL,'13000000001','00','2019-05-11 13:48:20','ADD'),(109,'202019051774820002','302019051724240001','aaaa',NULL,NULL,'34e212112386af3ede23b9535eed589d',NULL,NULL,NULL,'13689050406','00','2019-05-17 03:21:20','ADD'),(111,'202019052157660006','302019052133990001','董耀明','270307872@qq.com','asdfasdf','5b7ac7ba433dc0842fb8d3bc15e4b2aa',NULL,NULL,'0','18603928785','01','2019-05-21 07:58:03','DEl'),(112,'202019053129390032','302019040332500002','玉波','yubo@163.com','西双版纳','5b7ac7ba433dc0842fb8d3bc15e4b2aa',NULL,NULL,'1','17797173884','01','2019-05-31 15:31:33','DEl'),(113,'202019053129390032','302019040332500002','玉波','yubo@163.com','西双版纳',NULL,NULL,NULL,'1','17797173884','01','2019-05-31 15:31:33','ADD'),(114,'202019053118750035','302019040343530003','玉儿香','yuerxiang@163.com','西双版纳','5b7ac7ba433dc0842fb8d3bc15e4b2aa',NULL,NULL,'1','15597174306','01','2019-05-31 15:31:37','DEl'),(115,'202019053129130037','302019040332500002','玉波','yubo@163.com','西双版纳','5b7ac7ba433dc0842fb8d3bc15e4b2aa',NULL,NULL,'1','17797173884','01','2019-05-31 15:31:41','DEl'),(116,'202019053129130037','302019040332500002','玉波','yubo@163.com','西双版纳',NULL,NULL,NULL,'1','17797173884','01','2019-05-31 15:31:41','ADD'),(117,'202019060481650006','302019040481060001','罗比特1','luobite@163.com','西双版纳','5b7ac7ba433dc0842fb8d3bc15e4b2aa',NULL,NULL,'1','18909786789','01','2019-06-04 05:20:56','DEl'),(118,'202019060694370010','302019060653180001','米虎子','784240617@qq.com','弗兰省','5b7ac7ba433dc0842fb8d3bc15e4b2aa',NULL,NULL,'0','17623430978','01','2019-06-06 05:08:54','ADD'),(119,'202019060697010013','302019040246120002','王志超1','928255095@qq.com','青海省西宁市','cdfea6fbffcd82758eec12a96c64e682',NULL,NULL,'0','18909783333','01','2019-06-06 05:09:38','DEl'),(120,'202019060697010013','302019040246120002','米虎子','928255095@qq.com','青海省西宁市',NULL,NULL,NULL,'0','18909783333','01','2019-06-06 05:09:38','ADD'),(121,'202019060621390016','302019040246120002','米虎子','928255095@qq.com','青海省西宁市','cdfea6fbffcd82758eec12a96c64e682',NULL,NULL,'0','18909783333','01','2019-06-06 05:09:43','DEl');

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
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

/*Data for the table `c_app` */

insert  into `c_app`(`id`,`app_id`,`name`,`security_code`,`while_list_ip`,`black_list_ip`,`remark`,`create_time`,`status_cd`) values (1,'8000418001','内部测试应用','',NULL,NULL,'记得删除','2018-11-14 13:28:44','0'),(2,'8000418002','控制中心应用','',NULL,NULL,'控制中心应用','2018-11-14 13:28:44','0'),(3,'8000418003','用户管理应用','WEBURFPKIFJUHNCJUEIKMKJUJHULSMNCHDY89KMC',NULL,NULL,'用户管理应用','2018-11-14 13:28:44','0'),(4,'8000418004','小区管理系统web端','',NULL,NULL,'小区管理系统web端','2019-03-20 03:36:21','0');

/*Table structure for table `c_business` */

DROP TABLE IF EXISTS `c_business`;

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

/*Data for the table `c_business` */

insert  into `c_business`(`b_id`,`o_id`,`create_time`,`business_type_cd`,`finish_time`,`remark`,`status_cd`) values ('-1','-1','2018-12-09 03:18:50','D','2018-12-09',NULL,'B'),('202019020200000002','102019020200000001','2019-02-02 13:40:26','D','2019-02-02',NULL,'C'),('202019020200000003','102019020200000002','2019-02-02 14:00:33','D','2019-02-02',NULL,'C'),('202019020300000003','102019020300000002','2019-02-02 16:00:07','D','2019-02-03',NULL,'C'),('202019021000000003','102019021000000002','2019-02-09 16:24:21','100100030001',NULL,NULL,'S'),('202019021000000004','102019021000000003','2019-02-09 16:28:16','100100030001','2019-02-10',NULL,'B'),('202019021000000005','102019021000000004','2019-02-09 16:24:43','100100030001',NULL,NULL,'S'),('202019021000000006','102019021000000005','2019-02-09 16:30:05','100100030001','2019-02-10',NULL,'B'),('202019021000000007','102019021000000006','2019-02-09 16:36:02','100100030001','2019-02-10',NULL,'B'),('202019021000000008','102019021000000007','2019-02-09 16:31:25','100100030001','2019-02-10',NULL,'B'),('202019021000000009','102019021000000008','2019-02-09 16:57:45','100100030001','2019-02-10',NULL,'C'),('202019021001030002','102019021024050001','2019-02-10 14:35:36','100100040002','2019-02-10',NULL,'C'),('202019021058060004','102019021086310003','2019-02-09 17:23:13','100100030001','2019-02-10',NULL,'C'),('202019021059530006','102019021042560005','2019-02-10 14:37:01','100100030001','2019-02-10',NULL,'C'),('202019021067580008','102019021043400007','2019-02-10 14:38:23','100100030001','2019-02-10',NULL,'C'),('202019021070180002','102019021031750001','2019-02-09 17:17:42','100100030001','2019-02-10',NULL,'C'),('202019021096440004','102019021083940003','2019-02-10 14:35:54','100100040003','2019-02-10',NULL,'C'),('202019032344890002','102019032334900001','2019-03-23 08:59:27','100100030001','2019-03-23',NULL,'C'),('202019032517680010','102019032519540009','2019-03-25 15:55:33','100100030001','2019-03-25',NULL,'C'),('202019032531280008','102019032551670007','2019-03-25 08:34:56','100100030001','2019-03-25',NULL,'C'),('202019032582020004','102019032517210003','2019-03-25 08:24:20','100100030001','2019-03-25',NULL,'B'),('202019032587620006','102019032510060005','2019-03-25 08:30:52','100100030001','2019-03-25',NULL,'C'),('202019032906510006','102019032995960005','2019-03-29 14:13:02','200100030001','2019-03-29',NULL,'B'),('202019032916960002','102019032994640001','2019-03-29 14:11:20','200100030001','2019-03-29',NULL,'B'),('202019032924200010','102019032961460009','2019-03-29 14:21:41','200100030001','2019-03-29',NULL,'C'),('202019032934510004','102019032923910003','2019-03-29 14:11:50','200100030001','2019-03-29',NULL,'B'),('202019032981850008','102019032967090007','2019-03-29 14:13:26','200100030001','2019-03-29',NULL,'C'),('202019033011280002','102019033089400001','2019-03-30 07:47:21','200100030001','2019-03-30',NULL,'C'),('202019033013440002','102019033002560001','2019-03-30 04:04:35','200100060001','2019-03-30',NULL,'B'),('202019033033220002','102019033010240001','2019-03-30 06:51:21','200100030001','2019-03-30',NULL,'C'),('202019033042790002','102019033061390001','2019-03-30 03:57:06','200100060001','2019-03-30',NULL,'B'),('202019033045680004','102019033018530003','2019-03-30 03:20:35','200100060001','2019-03-30',NULL,'B'),('202019033046130002','102019033047090001','2019-03-30 08:04:36','200100030001','2019-03-30',NULL,'C'),('202019033046310003','102019033047090001','2019-03-30 08:04:36','200100060001','2019-03-30',NULL,'C'),('202019033050340003','102019033089400001','2019-03-30 07:47:21','200100060001','2019-03-30',NULL,'C'),('202019033063640004','102019033046150003','2019-03-30 05:56:21','200100030001','2019-03-30',NULL,'C'),('202019033069920008','102019033030080006','2019-03-30 08:37:31','200100060001','2019-03-30',NULL,'C'),('202019033072520002','102019033026120001','2019-03-30 08:31:27','200100030001','2019-03-30',NULL,'C'),('202019033072790004','102019033009000003','2019-03-30 05:32:16','200100030001','2019-03-30',NULL,'C'),('202019033073080002','102019033074770001','2019-03-30 03:05:28','200100060001','2019-03-30',NULL,'B'),('202019033076390007','102019033030080006','2019-03-30 08:37:31','200100030001','2019-03-30',NULL,'C'),('202019033081420003','102019033026120001','2019-03-30 08:31:27','200100060001','2019-03-30',NULL,'C'),('202019033081800002','102019033090360001','2019-03-30 05:53:33','200100030001','2019-03-30',NULL,'B'),('202019033083450005','102019033059140004','2019-03-30 08:34:54','100100030001','2019-03-30',NULL,'C'),('202019033086100002','102019033028470001','2019-03-30 05:17:13','200100060001','2019-03-30',NULL,'C'),('202019033086420003','102019033002560001','2019-03-30 04:04:38','DO',NULL,'业务系统实例失败，发起撤单','DO'),('202019040204720008','102019040226760006','2019-04-01 16:45:06','200100060001','2019-04-02',NULL,'C'),('202019040227860003','102019040265670001','2019-04-02 11:36:43','200100060001','2019-04-02',NULL,'C'),('202019040227890012','102019040250230011','2019-04-02 09:42:44','100100030001','2019-04-02',NULL,'B'),('202019040228000005','102019040219420003','2019-04-01 16:43:00','200100060001','2019-04-02',NULL,'C'),('202019040231680007','102019040226760006','2019-04-01 16:45:06','200100030001','2019-04-02',NULL,'C'),('202019040234930002','102019040241380001','2019-04-01 16:57:20','200100030001','2019-04-02',NULL,'C'),('202019040243080007','102019040238780006','2019-04-01 17:02:38','200100030001','2019-04-02',NULL,'C'),('202019040244340003','102019040241380001','2019-04-01 16:57:20','200100060001','2019-04-02',NULL,'C'),('202019040247210011','102019040234620009','2019-04-01 16:48:29','200100060001','2019-04-02',NULL,'C'),('202019040256780002','102019040265670001','2019-04-02 11:36:43','100100030001','2019-04-02',NULL,'C'),('202019040258800013','102019040250230011','2019-04-02 09:42:44','200100060001',NULL,NULL,'S'),('202019040259660010','102019040282470009','2019-04-02 09:25:16','200100060001','2019-04-02',NULL,'C'),('202019040270560002','102019040254500001','2019-04-02 09:57:45','100100030001','2019-04-02',NULL,'C'),('202019040270570008','102019040238780006','2019-04-01 17:02:38','200100060001','2019-04-02',NULL,'C'),('202019040281460010','102019040234620009','2019-04-01 16:48:29','200100030001','2019-04-02',NULL,'C'),('202019040287710005','102019040205460004','2019-04-01 17:00:43','100100030001','2019-04-02',NULL,'C'),('202019040289110004','102019040219420003','2019-04-01 16:43:00','200100030001','2019-04-02',NULL,'C'),('202019040291880002','102019040283770001','2019-04-01 16:40:48','100100030001','2019-04-02',NULL,'C'),('202019040303420005','102019040392690004','2019-04-03 02:27:23','100100030001','2019-04-03',NULL,'C'),('202019040304510007','102019040335780006','2019-04-03 14:51:56','100100030001','2019-04-03',NULL,'C'),('202019040306710004','102019040399640003','2019-04-03 14:50:56','100100030001','2019-04-03',NULL,'C'),('202019040313040016','102019040347710015','2019-04-03 14:54:59','100100030001','2019-04-03',NULL,'C'),('202019040314020002','102019040366270001','2019-04-03 02:24:53','100100030001','2019-04-03',NULL,'C'),('202019040320970014','102019040394340012','2019-04-03 14:53:47','200100060001','2019-04-03',NULL,'C'),('202019040333220013','102019040394340012','2019-04-03 14:53:47','100100030001','2019-04-03',NULL,'C'),('202019040334540020','102019040335660018','2019-04-03 14:55:54','200100060001','2019-04-03',NULL,'C'),('202019040335310002','102019040340780001','2019-04-03 08:09:48','100100040001','2019-04-03',NULL,'C'),('202019040339540010','102019040354340009','2019-04-03 14:52:54','100100030001','2019-04-03',NULL,'C'),('202019040342840008','102019040335780006','2019-04-03 14:51:56','200100060001','2019-04-03',NULL,'C'),('202019040346960003','102019040380840001','2019-04-03 03:12:04','200100060001','2019-04-03',NULL,'C'),('202019040356450003','102019040366270001','2019-04-03 02:24:53','200100060001','2019-04-03',NULL,'C'),('202019040363750002','102019040382720001','2019-04-03 07:45:35','100100040001','2019-04-03',NULL,'C'),('202019040367530011','102019040354340009','2019-04-03 14:52:54','200100060001','2019-04-03',NULL,'C'),('202019040372460002','102019040380840001','2019-04-03 03:12:04','100100030001','2019-04-03',NULL,'C'),('202019040376180005','102019040399640003','2019-04-03 14:50:56','200100060001','2019-04-03',NULL,'C'),('202019040389340006','102019040392690004','2019-04-03 02:27:23','200100060001','2019-04-03',NULL,'C'),('202019040397350017','102019040347710015','2019-04-03 14:54:59','200100060001','2019-04-03',NULL,'C'),('202019040399500019','102019040335660018','2019-04-03 14:55:54','100100030001','2019-04-03',NULL,'C'),('202019040400180011','102019040466410010','2019-04-04 08:39:35','200100070001','2019-04-04',NULL,'C'),('202019040406410008','102019040430010007','2019-04-04 08:35:54','200100070001','2019-04-04',NULL,'B'),('202019040407460003','102019040415460001','2019-04-04 07:26:27','100100040002',NULL,NULL,'S'),('202019040427900003','102019040428870001','2019-04-04 08:22:37','100100040002','2019-04-04',NULL,'C'),('202019040429480003','102019040467730001','2019-04-04 08:09:31','100100040002','2019-04-04',NULL,'B'),('202019040434790017','102019040457070016','2019-04-04 08:40:57','100100030001','2019-04-04',NULL,'C'),('202019040438610002','102019040428870001','2019-04-04 08:22:37','200100070001','2019-04-04',NULL,'C'),('202019040438900009','102019040430010007','2019-04-04 08:35:54','100100040002','2019-04-04',NULL,'B'),('202019040442220012','102019040466410010','2019-04-04 08:39:35','100100040002','2019-04-04',NULL,'C'),('202019040445720015','102019040440350013','2019-04-04 08:39:58','100100040002','2019-04-04',NULL,'C'),('202019040451960006','102019040452150004','2019-04-04 08:25:49','100100040002','2019-04-04',NULL,'B'),('202019040458180024','102019040430240023','2019-04-03 16:04:30','100100040001','2019-04-04',NULL,'C'),('202019040459910005','102019040452150004','2019-04-04 08:25:49','200100070001','2019-04-04',NULL,'B'),('202019040465520006','102019040413410004','2019-04-04 08:14:03','100100040002','2019-04-04',NULL,'B'),('202019040469410022','102019040472570021','2019-04-03 16:02:16','100100040001','2019-04-04',NULL,'C'),('202019040471000002','102019040429190001','2019-04-04 14:24:56','100100030001','2019-04-04',NULL,'C'),('202019040479990014','102019040440350013','2019-04-04 08:39:58','200100070001','2019-04-04',NULL,'C'),('202019040488730005','102019040413410004','2019-04-04 08:14:03','200100070001','2019-04-04',NULL,'B'),('202019040490830002','102019040415460001','2019-04-04 07:26:27','200100070001','2019-04-04',NULL,'B'),('202019040491800002','102019040467730001','2019-04-04 08:09:31','200100070001','2019-04-04',NULL,'B'),('202019040494260003','102019040429190001','2019-04-04 14:24:56','200100060001','2019-04-04',NULL,'C'),('202019040496010018','102019040457070016','2019-04-04 08:40:57','200100060001','2019-04-04',NULL,'C'),('202019040585760005','102019040538650004','2019-04-05 13:17:10','100100040001','2019-04-05',NULL,'C'),('202019041120660002','102019041104470001','2019-04-11 08:07:53','100100030001','2019-04-11',NULL,'C'),('202019041317610002','102019041341210001','2019-04-13 15:11:12','200100030001','2019-04-13',NULL,'C'),('202019041318030006','102019041379170004','2019-04-13 15:12:27','200100060001','2019-04-13',NULL,'C'),('202019041327040005','102019041379170004','2019-04-13 15:12:27','200100030001','2019-04-13',NULL,'C'),('202019041340460005','102019041311380003','2019-04-13 04:50:24','100100040002','2019-04-13',NULL,'C'),('202019041375730003','102019041341210001','2019-04-13 15:11:12','200100060001','2019-04-13',NULL,'C'),('202019041376160004','102019041311380003','2019-04-13 04:50:24','200100070001','2019-04-13',NULL,'C'),('202019041801200002','102019041860210001','2019-04-18 02:20:02','100100030001','2019-04-18',NULL,'C'),('202019041810750003','102019041860210001','2019-04-18 02:20:02','200100060001','2019-04-18',NULL,'C'),('202019041834430002','102019041809820001','2019-04-18 15:31:00','500100040002','2019-04-18',NULL,'C'),('202019041834510002','102019041821040001','2019-04-18 04:03:06','500100030002','2019-04-18',NULL,'C'),('202019041850040006','102019041833300005','2019-04-18 15:44:55','500100040002','2019-04-18',NULL,'C'),('202019041884620004','102019041814960003','2019-04-18 15:39:27','500100040002','2019-04-18',NULL,'C'),('202019041891220004','102019041832560003','2019-04-18 07:09:21','500100030002','2019-04-18',NULL,'C'),('202019042126440002','102019042182010001','2019-04-21 10:04:01','100100030001','2019-04-21',NULL,'C'),('202019042139970003','102019042182010001','2019-04-21 10:04:01','200100060001','2019-04-21',NULL,'C'),('202019042604090005','102019042659490004','2019-04-26 08:37:11','510100030001','2019-04-26',NULL,'C'),('202019042604660002','102019042634280001','2019-04-26 09:53:40','510100030001','2019-04-26',NULL,'C'),('202019042605500011','102019042677780010','2019-04-26 09:24:55','510100030001','2019-04-26',NULL,'C'),('202019042632530003','102019042691850001','2019-04-26 08:34:28','500100030002','2019-04-26',NULL,'C'),('202019042632540002','102019042667300001','2019-04-26 09:14:09','510100030001','2019-04-26',NULL,'C'),('202019042640630003','102019042667300001','2019-04-26 09:14:09','500100030002','2019-04-26',NULL,'C'),('202019042641830015','102019042665790013','2019-04-26 08:59:41','500100030002','2019-04-26',NULL,'C'),('202019042644750009','102019042620240007','2019-04-26 08:45:15','500100030002','2019-04-26',NULL,'C'),('202019042644760012','102019042680900010','2019-04-26 08:57:16','500100030002','2019-04-26',NULL,'C'),('202019042646640006','102019042659490004','2019-04-26 08:37:11','500100030002','2019-04-26',NULL,'C'),('202019042648080002','102019042691850001','2019-04-26 08:34:28','510100030001','2019-04-26',NULL,'C'),('202019042649120009','102019042648040007','2019-04-26 09:22:08','500100030002','2019-04-26',NULL,'C'),('202019042653890006','102019042671900004','2019-04-26 09:14:59','500100030002','2019-04-26',NULL,'C'),('202019042655970012','102019042677780010','2019-04-26 09:24:55','500100030002','2019-04-26',NULL,'C'),('202019042657960008','102019042648040007','2019-04-26 09:22:08','510100030001','2019-04-26',NULL,'C'),('202019042661510014','102019042665790013','2019-04-26 08:59:41','510100030001','2019-04-26',NULL,'C'),('202019042677430003','102019042634280001','2019-04-26 09:53:40','500100030002','2019-04-26',NULL,'C'),('202019042679110005','102019042671900004','2019-04-26 09:14:59','510100030001','2019-04-26',NULL,'C'),('202019042688520008','102019042620240007','2019-04-26 08:45:15','510100030001','2019-04-26',NULL,'C'),('202019042695240011','102019042680900010','2019-04-26 08:57:16','510100030001','2019-04-26',NULL,'C'),('202019042711910013','102019042743480012','2019-04-27 11:56:56','510100030001','2019-04-27',NULL,'C'),('202019042717160014','102019042743480012','2019-04-27 11:56:56','500100030002','2019-04-27',NULL,'C'),('202019042723970002','102019042769570001','2019-04-26 16:58:58','500100030002','2019-04-27',NULL,'C'),('202019042729460002','102019042765340001','2019-04-26 17:30:54','510100030001','2019-04-27',NULL,'C'),('202019042731610008','102019042703930007','2019-04-26 17:38:57','510100030001','2019-04-27',NULL,'C'),('202019042735220009','102019042720500007','2019-04-26 17:12:03','500100030002','2019-04-27',NULL,'B'),('202019042735920006','102019042703920004','2019-04-26 17:37:14','500100030002','2019-04-27',NULL,'C'),('202019042750320003','102019042765340001','2019-04-26 17:30:54','500100030002','2019-04-27',NULL,'C'),('202019042757120008','102019042720500007','2019-04-26 17:12:03','510100030001','2019-04-27',NULL,'B'),('202019042761570003','102019042793180001','2019-04-26 17:20:51','500100030002','2019-04-27',NULL,'B'),('202019042771400011','102019042710640010','2019-04-27 10:43:08','100100030001','2019-04-27',NULL,'C'),('202019042775180005','102019042703920004','2019-04-26 17:37:14','510100030001','2019-04-27',NULL,'C'),('202019042776600004','102019042799060003','2019-04-26 16:59:32','500100030002','2019-04-27',NULL,'C'),('202019042785490009','102019042703930007','2019-04-26 17:38:57','500100030002','2019-04-27',NULL,'C'),('202019042785720002','102019042793180001','2019-04-26 17:20:51','510100030001','2019-04-27',NULL,'B'),('202019042799620006','102019042721580005','2019-04-26 17:05:00','500100030002','2019-04-27',NULL,'C'),('202019042803330038','102019042888740037','2019-04-28 02:09:07','200100030001','2019-04-28',NULL,'C'),('202019042806640050','102019042896400048','2019-04-28 02:09:09','200100060001','2019-04-28',NULL,'C'),('202019042807020064','102019042844980063','2019-04-28 02:09:12','200100030001','2019-04-28',NULL,'C'),('202019042809150025','102019042894500024','2019-04-28 02:09:06','200100030001','2019-04-28',NULL,'C'),('202019042810810022','102019042842770021','2019-04-28 02:09:06','200100030001','2019-04-28',NULL,'C'),('202019042813420053','102019042891080051','2019-04-28 02:09:10','200100060001','2019-04-28',NULL,'C'),('202019042813460020','102019042846870018','2019-04-28 02:09:06','200100060001','2019-04-28',NULL,'C'),('202019042819410058','102019042803000057','2019-04-28 02:09:11','200100030001','2019-04-28',NULL,'C'),('202019042820240035','102019042892400033','2019-04-28 02:09:07','200100060001','2019-04-28',NULL,'C'),('202019042821020026','102019042894500024','2019-04-28 02:09:06','200100060001','2019-04-28',NULL,'C'),('202019042827770009','102019042866910008','2019-04-28 09:54:10','510100040001','2019-04-28',NULL,'C'),('202019042828060004','102019042849530003','2019-04-28 09:47:53','510100040001','2019-04-28',NULL,'C'),('202019042829820067','102019042811680066','2019-04-28 02:09:12','200100030001','2019-04-28',NULL,'C'),('202019042830960062','102019042869120060','2019-04-28 02:09:11','200100060001','2019-04-28',NULL,'C'),('202019042832720040','102019042850720036','2019-04-28 02:09:08','200100030001','2019-04-28',NULL,'C'),('202019042833710002','102019042845220001','2019-04-28 09:47:04','510100040001','2019-04-28',NULL,'C'),('202019042836610044','102019042869590042','2019-04-28 02:09:08','200100060001','2019-04-28',NULL,'C'),('202019042839280068','102019042811680066','2019-04-28 02:09:12','200100060001','2019-04-28',NULL,'C'),('202019042842350059','102019042803000057','2019-04-28 02:09:11','200100060001','2019-04-28',NULL,'C'),('202019042848480065','102019042844980063','2019-04-28 02:09:12','200100060001','2019-04-28',NULL,'C'),('202019042850210049','102019042896400048','2019-04-28 02:09:09','200100030001','2019-04-28',NULL,'C'),('202019042852920061','102019042869120060','2019-04-28 02:09:11','200100030001','2019-04-28',NULL,'C'),('202019042857010031','102019042859200030','2019-04-28 02:09:07','200100030001','2019-04-28',NULL,'C'),('202019042862250016','102019042819450015','2019-04-28 02:08:54','200100030001','2019-04-28',NULL,'C'),('202019042864170019','102019042846870018','2019-04-28 02:09:06','200100030001','2019-04-28',NULL,'C'),('202019042870220006','102019042810330005','2019-04-28 09:53:53','510100030001','2019-04-28',NULL,'C'),('202019042870660046','102019042892670045','2019-04-28 02:09:09','200100030001','2019-04-28',NULL,'C'),('202019042870910002','102019042822560001','2019-04-28 09:32:52','510100040001','2019-04-28',NULL,'B'),('202019042874650029','102019042807240027','2019-04-28 02:09:07','200100060001','2019-04-28',NULL,'C'),('202019042875350007','102019042810330005','2019-04-28 09:53:53','500100030002','2019-04-28',NULL,'C'),('202019042876120055','102019042838480054','2019-04-28 02:09:11','200100030001','2019-04-28',NULL,'C'),('202019042879890023','102019042842770021','2019-04-28 02:09:06','200100060001','2019-04-28',NULL,'C'),('202019042882310071','102019042843460069','2019-04-28 06:59:56','200100060001','2019-04-28',NULL,'C'),('202019042885450032','102019042859200030','2019-04-28 02:09:07','200100060001','2019-04-28',NULL,'C'),('202019042887970070','102019042843460069','2019-04-28 06:59:56','100100030001','2019-04-28',NULL,'C'),('202019042888030047','102019042892670045','2019-04-28 02:09:09','200100060001','2019-04-28',NULL,'C'),('202019042888600034','102019042892400033','2019-04-28 02:09:07','200100030001','2019-04-28',NULL,'C'),('202019042888960041','102019042850720036','2019-04-28 02:09:08','200100060001','2019-04-28',NULL,'C'),('202019042890620028','102019042807240027','2019-04-28 02:09:07','200100030001','2019-04-28',NULL,'C'),('202019042892790017','102019042819450015','2019-04-28 02:08:54','200100060001','2019-04-28',NULL,'C'),('202019042893250011','102019042829590010','2019-04-28 14:03:35','510100040001','2019-04-28',NULL,'C'),('202019042893500039','102019042888740037','2019-04-28 02:09:07','200100060001','2019-04-28',NULL,'C'),('202019042897110043','102019042869590042','2019-04-28 02:09:08','200100030001','2019-04-28',NULL,'C'),('202019042897550052','102019042891080051','2019-04-28 02:09:10','200100030001','2019-04-28',NULL,'C'),('202019042899920056','102019042838480054','2019-04-28 02:09:11','200100060001','2019-04-28',NULL,'C'),('202019042900080005','102019042911210004','2019-04-29 11:48:06','510100030001','2019-04-29',NULL,'C'),('202019042900640008','102019042999050007','2019-04-28 17:19:37','510100040001','2019-04-29',NULL,'C'),('202019042905090005','102019042983310004','2019-04-28 17:19:24','510100030001','2019-04-29',NULL,'C'),('202019042908790002','102019042968020001','2019-04-29 11:47:34','510100030001','2019-04-29',NULL,'C'),('202019042910990023','102019042933020022','2019-04-29 09:23:40','510100030001','2019-04-29',NULL,'C'),('202019042911830002','102019042932620001','2019-04-28 16:09:13','510100040001','2019-04-29',NULL,'C'),('202019042912540011','102019042947230009','2019-04-28 17:19:44','500100040002','2019-04-29',NULL,'C'),('202019042915470015','102019042911820013','2019-04-29 09:22:43','500100030002','2019-04-29',NULL,'C'),('202019042921120006','102019042949200004','2019-04-29 09:21:46','500100030002','2019-04-29',NULL,'C'),('202019042922430006','102019042983310004','2019-04-28 17:19:24','500100030002','2019-04-29',NULL,'C'),('202019042927450008','102019042961210007','2019-04-29 09:21:59','510100030001','2019-04-29',NULL,'C'),('202019042932230010','102019042947230009','2019-04-28 17:19:44','510100050001','2019-04-29',NULL,'C'),('202019042936100024','102019042933020022','2019-04-29 09:23:40','500100030002','2019-04-29',NULL,'C'),('202019042942050005','102019042948840004','2019-04-28 17:06:21','510100050001','2019-04-29',NULL,'B'),('202019042945820012','102019042965400010','2019-04-29 09:22:28','500100030002','2019-04-29',NULL,'C'),('202019042948530021','102019042940790019','2019-04-29 09:23:08','500100030002','2019-04-29',NULL,'C'),('202019042953870011','102019042965400010','2019-04-29 09:22:28','510100030001','2019-04-29',NULL,'C'),('202019042962160002','102019042986530001','2019-04-28 16:57:34','510100050001','2019-04-29',NULL,'B'),('202019042964530003','102019042986530001','2019-04-28 16:57:35','500100040002','2019-04-29',NULL,'B'),('202019042967870006','102019042948840004','2019-04-28 17:06:21','500100040002','2019-04-29',NULL,'B'),('202019042970510006','102019042911210004','2019-04-29 11:48:06','500100030002','2019-04-29',NULL,'C'),('202019042971000003','102019042930590001','2019-04-29 09:14:51','500100030002','2019-04-29',NULL,'C'),('202019042971980017','102019042905230016','2019-04-29 09:22:54','510100030001','2019-04-29',NULL,'C'),('202019042976730005','102019042949200004','2019-04-29 09:21:46','510100030001','2019-04-29',NULL,'C'),('202019042978390014','102019042911820013','2019-04-29 09:22:43','510100030001','2019-04-29',NULL,'C'),('202019042979290009','102019042961210007','2019-04-29 09:21:59','500100030002','2019-04-29',NULL,'C'),('202019042979960002','102019042974910001','2019-04-28 17:17:09','510100050001','2019-04-29',NULL,'C'),('202019042982100002','102019042930590001','2019-04-29 09:14:51','510100030001','2019-04-29',NULL,'C'),('202019042987920018','102019042905230016','2019-04-29 09:22:54','500100030002','2019-04-29',NULL,'C'),('202019042988310003','102019042974910001','2019-04-28 17:17:09','500100040002','2019-04-29',NULL,'C'),('202019042995080003','102019042968020001','2019-04-29 11:47:34','500100030002','2019-04-29',NULL,'C'),('202019042996460020','102019042940790019','2019-04-29 09:23:08','510100030001','2019-04-29',NULL,'C'),('202019050307230002','102019050326060001','2019-05-03 10:52:30','520100040001','2019-05-03',NULL,'B'),('202019050307280016','102019050366970015','2019-05-03 10:01:49','520100030001','2019-05-03',NULL,'C'),('202019050314230004','102019050382250003','2019-05-03 09:41:29','520100030001','2019-05-03',NULL,'B'),('202019050318680010','102019050370720009','2019-05-03 09:53:30','520100030001','2019-05-03',NULL,'C'),('202019050328930012','102019050398430011','2019-05-03 09:53:37','520100030001','2019-05-03',NULL,'C'),('202019050332150002','102019050354750001','2019-05-03 09:34:44','520100030001','2019-05-03',NULL,'B'),('202019050342470004','102019050357590003','2019-05-03 09:52:42','520100030001','2019-05-03',NULL,'C'),('202019050346780002','102019050358980001','2019-05-03 11:36:41','520100040001','2019-05-03',NULL,'C'),('202019050354360006','102019050394550005','2019-05-03 09:53:09','520100030001','2019-05-03',NULL,'C'),('202019050357280004','102019050361830003','2019-05-03 11:37:00','520100030001','2019-05-03',NULL,'C'),('202019050375210002','102019050370860001','2019-05-03 11:06:57','520100040001','2019-05-03',NULL,'B'),('202019050386950004','102019050333110003','2019-05-03 10:55:47','520100040001','2019-05-03',NULL,'B'),('202019050388740003','102019050370860001','2019-05-03 11:06:58','DO',NULL,'业务系统实例失败，发起撤单','DO'),('202019050395750014','102019050315000013','2019-05-03 10:01:33','520100030001','2019-05-03',NULL,'C'),('202019050397190006','102019050329540005','2019-05-03 10:56:10','520100040001','2019-05-03',NULL,'B'),('202019050398970002','102019050317100001','2019-05-03 09:52:11','520100030001','2019-05-03',NULL,'C'),('202019050399040008','102019050347360007','2019-05-03 09:53:18','520100030001','2019-05-03',NULL,'C'),('202019050422440008','102019050490890007','2019-05-04 13:47:01','520100030001','2019-05-04',NULL,'C'),('202019050441380004','102019050488530003','2019-05-04 13:45:57','520100040001','2019-05-04',NULL,'C'),('202019050467640002','102019050496210001','2019-05-04 13:24:56','520100050001','2019-05-04',NULL,'C'),('202019050470920008','102019050418260007','2019-05-04 10:48:57','520100040001','2019-05-04',NULL,'C'),('202019050474860006','102019050403630005','2019-05-04 10:48:39','520100030001','2019-05-04',NULL,'C'),('202019050486630010','102019050425190009','2019-05-04 10:49:06','520100040001','2019-05-04',NULL,'C'),('202019050486680006','102019050495470005','2019-05-04 13:46:08','520100050001','2019-05-04',NULL,'C'),('202019050497010002','102019050417410001','2019-05-04 13:45:45','520100030001','2019-05-04',NULL,'C'),('202019050498920004','102019050462530003','2019-05-04 13:28:15','520100050001','2019-05-04',NULL,'B'),('202019050638030002','102019050625890001','2019-05-06 02:16:47','520100030001','2019-05-06',NULL,'B'),('202019050641470006','102019050607830004','2019-05-06 02:17:02','DO',NULL,'业务系统实例失败，发起撤单','DO'),('202019050645380003','102019050625890001','2019-05-06 02:16:50','DO',NULL,'业务系统实例失败，发起撤单','DO'),('202019050649670002','102019050612160001','2019-05-06 14:06:43','520100030001','2019-05-06',NULL,'B'),('202019050656610005','102019050607830004','2019-05-06 02:17:02','520100030001','2019-05-06',NULL,'B'),('202019050700230002','102019050709110001','2019-05-07 15:18:37','530100030001','2019-05-07',NULL,'C'),('202019050722290004','102019050756560003','2019-05-06 19:41:42','520100030001','2019-05-07',NULL,'C'),('202019050745870002','102019050702040001','2019-05-06 16:08:46','520100030001','2019-05-07',NULL,'C'),('202019050757780002','102019050766510001','2019-05-07 14:40:48','530100030001','2019-05-07',NULL,'C'),('202019050762320006','102019050758150005','2019-05-07 15:35:55','530100030001','2019-05-07',NULL,'C'),('202019050779380002','102019050737140001','2019-05-07 14:59:33','530100030001','2019-05-07',NULL,'B'),('202019050784770004','102019050740250003','2019-05-07 15:20:05','530100030001','2019-05-07',NULL,'B'),('202019050801410008','102019050873290007','2019-05-08 10:57:57','530100050001','2019-05-08',NULL,'C'),('202019050812510002','102019050899270001','2019-05-08 10:40:33','530100050001','2019-05-08',NULL,'C'),('202019050812980004','102019050828340003','2019-05-07 17:57:46','530100030001','2019-05-08',NULL,'C'),('202019050822640002','102019050842980001','2019-05-08 05:36:18','530100030001','2019-05-08',NULL,'C'),('202019050823880004','102019050888880003','2019-05-08 10:14:57','530100040001','2019-05-08',NULL,'C'),('202019050835720004','102019050833420003','2019-05-08 07:39:39','530100030001','2019-05-08',NULL,'C'),('202019050838100006','102019050818440005','2019-05-08 07:40:32','530100030001','2019-05-08',NULL,'C'),('202019050847810006','102019050869400005','2019-05-08 10:57:51','530100040001','2019-05-08',NULL,'C'),('202019050853120004','102019050812360003','2019-05-08 10:57:30','530100030001','2019-05-08',NULL,'C'),('202019050857790002','102019050867530001','2019-05-08 10:05:43','530100040001','2019-05-08',NULL,'C'),('202019050858950008','102019050816190007','2019-05-08 07:41:21','530100030001','2019-05-08',NULL,'C'),('202019050890510002','102019050896060001','2019-05-07 17:55:39','530100030001','2019-05-08',NULL,'C'),('202019050893040010','102019050873670009','2019-05-08 08:57:24','530100030001','2019-05-08',NULL,'C'),('202019050893350002','102019050840970001','2019-05-08 06:07:26','530100030001','2019-05-08',NULL,'C'),('202019050927090006','102019050911760005','2019-05-09 06:23:26','530100040001','2019-05-09',NULL,'C'),('202019050942230008','102019050960380007','2019-05-09 06:24:05','530100040001','2019-05-09',NULL,'C'),('202019050942660010','102019050904150009','2019-05-09 05:29:53','530100030001','2019-05-09',NULL,'C'),('202019050955970004','102019050973890003','2019-05-09 06:23:12','530100040001','2019-05-09',NULL,'C'),('202019050968960002','102019050920550001','2019-05-09 06:10:52','530100030001','2019-05-09',NULL,'C'),('202019051032590010','102019051078100009','2019-05-10 05:55:35','530100040001','2019-05-10',NULL,'C'),('202019051147830012','102019051159070011','2019-05-11 13:41:20','100100030001','2019-05-11',NULL,'C'),('202019051151120014','102019051117980013','2019-05-11 13:48:20','100100030001','2019-05-11',NULL,'C'),('202019051397610016','102019051313310015','2019-05-13 09:54:39','530100050001','2019-05-13',NULL,'C'),('202019051704540010','102019051790130009','2019-05-17 13:41:17','110100030001','2019-05-17',NULL,'B'),('202019051707110007','102019051734170006','2019-05-17 13:24:28','110100030001','2019-05-17',NULL,'B'),('202019051710870011','102019051790130009','2019-05-17 13:41:17','500100030002',NULL,NULL,'S'),('202019051731140004','102019051768310003','2019-05-17 09:47:10','110100030001','2019-05-17',NULL,'B'),('202019051734630008','102019051734170006','2019-05-17 13:24:28','500100030002',NULL,NULL,'S'),('202019051735310003','102019051731630001','2019-05-17 14:11:29','500100030002','2019-05-17',NULL,'C'),('202019051750220005','102019051768310003','2019-05-17 09:47:10','500100030002',NULL,NULL,'S'),('202019051752520003','102019051789100001','2019-05-17 14:29:37','500100030002','2019-05-17',NULL,'C'),('202019051774820002','102019051793850001','2019-05-17 03:21:18','100100030001','2019-05-17',NULL,'C'),('202019051782740003','102019051741560001','2019-05-17 14:01:29','500100030002',NULL,NULL,'S'),('202019051796390002','102019051731630001','2019-05-17 14:11:29','110100030001','2019-05-17',NULL,'C'),('202019051798670002','102019051741560001','2019-05-17 14:01:29','110100030001','2019-05-17',NULL,'B'),('202019051798740002','102019051789100001','2019-05-17 14:29:37','110100030001','2019-05-17',NULL,'C'),('202019051801160005','102019051891280004','2019-05-18 08:12:10','110100030001','2019-05-18',NULL,'C'),('202019051811350010','102019051877940009','2019-05-18 10:42:05','110100040001','2019-05-18',NULL,'C'),('202019051814090006','102019051891280004','2019-05-18 08:12:10','500100030002','2019-05-18',NULL,'C'),('202019051814560002','102019051856160001','2019-05-17 16:58:20','510100050001','2019-05-18',NULL,'C'),('202019051820940005','102019051867700003','2019-05-17 16:49:04','500100030002','2019-05-18',NULL,'C'),('202019051829600003','102019051856160001','2019-05-17 16:58:20','500100040002','2019-05-18',NULL,'C'),('202019051830060026','102019051880110025','2019-05-18 12:04:49','110100030001','2019-05-18',NULL,'C'),('202019051837120006','102019051853460005','2019-05-18 10:38:34','110100040001','2019-05-18',NULL,'C'),('202019051839700002','102019051801920001','2019-05-18 07:59:26','110100030001','2019-05-18',NULL,'B'),('202019051842650002','102019051843080001','2019-05-18 08:11:12','110100030001','2019-05-18',NULL,'C'),('202019051843140008','102019051897310007','2019-05-18 10:40:29','110100040001','2019-05-18',NULL,'C'),('202019051846110003','102019051843080001','2019-05-18 08:11:12','500100030002','2019-05-18',NULL,'C'),('202019051847830003','102019051801920001','2019-05-18 07:59:26','500100030002','2019-05-18',NULL,'B'),('202019051851690004','102019051801920001','2019-05-18 07:59:28','DO',NULL,'业务系统实例失败，发起撤单','DO'),('202019051851950019','102019051861920018','2019-05-18 10:44:56','110100040001','2019-05-18',NULL,'C'),('202019051861450009','102019051833310007','2019-05-18 08:19:18','500100030002','2019-05-18',NULL,'C'),('202019051862280002','102019051835010001','2019-05-17 16:30:24','110100040001','2019-05-18',NULL,'B'),('202019051863710004','102019051803730003','2019-05-18 10:36:51','110100040001','2019-05-18',NULL,'C'),('202019051864800012','102019051829070011','2019-05-18 10:43:19','110100030001','2019-05-18',NULL,'C'),('202019051864990003','102019051857060001','2019-05-18 08:32:14','500100030002','2019-05-18',NULL,'C'),('202019051870570002','102019051812890001','2019-05-18 10:36:23','110100040001','2019-05-18',NULL,'C'),('202019051872120006','102019051805200005','2019-05-18 08:01:18','110100030001','2019-05-18',NULL,'B'),('202019051873410017','102019051837100016','2019-05-18 10:43:45','110100040001','2019-05-18',NULL,'C'),('202019051873530008','102019051805200005','2019-05-18 08:01:18','DO',NULL,'业务系统实例失败，发起撤单','DO'),('202019051880120004','102019051867700003','2019-05-17 16:49:04','110100030001','2019-05-18',NULL,'C'),('202019051881250024','102019051851920023','2019-05-18 12:01:30','110100030001','2019-05-18',NULL,'B'),('202019051883170007','102019051805200005','2019-05-18 08:01:18','500100030002','2019-05-18',NULL,'B'),('202019051885250013','102019051829070011','2019-05-18 10:43:19','500100030002','2019-05-18',NULL,'C'),('202019051885450002','102019051861290001','2019-05-17 16:48:18','110100040001','2019-05-18',NULL,'C'),('202019051886350015','102019051810130014','2019-05-18 10:43:33','110100040001','2019-05-18',NULL,'C'),('202019051887830008','102019051833310007','2019-05-18 08:19:18','110100030001','2019-05-18',NULL,'C'),('202019051888340021','102019051812900020','2019-05-18 10:51:33','510100050001','2019-05-18',NULL,'C'),('202019051888940002','102019051857060001','2019-05-18 08:32:14','110100030001','2019-05-18',NULL,'C'),('202019051893170022','102019051812900020','2019-05-18 10:51:33','500100040002','2019-05-18',NULL,'C'),('202019051895620002','102019051806160001','2019-05-17 16:41:22','110100040001','2019-05-18',NULL,'B'),('202019051901950011','102019051949640010','2019-05-19 00:51:20','110100030001','2019-05-19',NULL,'C'),('202019051905100006','102019051956350005','2019-05-19 01:23:49','510100050001','2019-05-19',NULL,'C'),('202019051916160009','102019051950840008','2019-05-19 00:44:57','110100030001','2019-05-19',NULL,'C'),('202019051922270004','102019051906010003','2019-05-19 01:21:03','510100050001','2019-05-19',NULL,'C'),('202019051931590002','102019051982360001','2019-05-19 01:50:26','110100050001','2019-05-19',NULL,'C'),('202019051933500004','102019051947160003','2019-05-19 00:27:43','110100030001','2019-05-19',NULL,'C'),('202019051944770006','102019051952980005','2019-05-19 00:44:18','110100030001','2019-05-19',NULL,'C'),('202019051959440007','102019051952980005','2019-05-19 00:44:18','500100030002','2019-05-19',NULL,'C'),('202019051961460009','102019051962500007','2019-05-19 01:25:48','500100040002','2019-05-19',NULL,'C'),('202019051965430008','102019051962500007','2019-05-19 01:25:48','510100050001','2019-05-19',NULL,'C'),('202019051966520002','102019051919600001','2019-05-19 01:20:45','110100040001','2019-05-19',NULL,'C'),('202019051966970002','102019051918540001','2019-05-19 00:26:55','110100030001','2019-05-19',NULL,'C'),('202019051971540013','102019051906090012','2019-05-19 00:55:36','110100030001','2019-05-19',NULL,'C'),('202019051983570004','102019051983860003','2019-05-19 01:50:54','110100030001','2019-05-19',NULL,'C'),('202019051987920015','102019051912820014','2019-05-19 01:05:35','110100040001','2019-05-19',NULL,'B'),('202019052157660006','102019052194570004','2019-05-21 07:58:03','100100040002','2019-05-21',NULL,'C'),('202019052168890003','102019052194570001','2019-05-21 07:49:46','200100060001','2019-05-21',NULL,'C'),('202019052172780002','102019052194570001','2019-05-21 07:49:46','100100030001','2019-05-21',NULL,'C'),('202019052175270005','102019052194570004','2019-05-21 07:58:03','200100070001','2019-05-21',NULL,'C'),('202019052219400002','102019052214810001','2019-05-22 15:19:04','111100050001','2019-05-22',NULL,'C'),('202019052250200004','102019052218900003','2019-05-22 15:21:46','111100030001','2019-05-22',NULL,'C'),('202019052255290002','102019052298100001','2019-05-21 16:30:14','111100030001','2019-05-22',NULL,'C'),('202019052268080002','102019052231880001','2019-05-22 13:36:50','111100030001','2019-05-22',NULL,'C'),('202019052282950002','102019052269970001','2019-05-22 03:53:31','111100030001','2019-05-22',NULL,'C'),('202019052296630004','102019052219490003','2019-05-22 13:38:07','111100030001','2019-05-22',NULL,'C'),('202019052308670006','102019052331030005','2019-05-23 06:53:33','110100030001','2019-05-23',NULL,'C'),('202019052366960007','102019052331030005','2019-05-23 06:53:33','500100030002','2019-05-23',NULL,'C'),('202019052370090009','102019052358400008','2019-05-23 06:53:57','110100040001','2019-05-23',NULL,'C'),('202019052611510002','102019052652750001','2019-05-26 00:32:59','500100030002','2019-05-26',NULL,'C'),('202019052613960006','102019052640620005','2019-05-26 12:49:44','500100040002','2019-05-26',NULL,'B'),('202019052616400013','102019052665950012','2019-05-26 12:50:33','110100030001','2019-05-26',NULL,'C'),('202019052616520011','102019052695670010','2019-05-26 12:50:06','110100040001','2019-05-26',NULL,'C'),('202019052624900014','102019052665950012','2019-05-26 12:50:33','500100030002','2019-05-26',NULL,'C'),('202019052646680008','102019052626930007','2019-05-26 12:49:58','110100050001','2019-05-26',NULL,'C'),('202019052650230016','102019052675020015','2019-05-26 12:50:57','500100030002','2019-05-26',NULL,'C'),('202019052655040009','102019052626930007','2019-05-26 12:49:58','500100040002','2019-05-26',NULL,'C'),('202019052690410004','102019052670270003','2019-05-26 12:49:42','500100040002','2019-05-26',NULL,'B'),('202019052710460018','102019052714350017','2019-05-27 01:52:38','510100040001','2019-05-27',NULL,'B'),('202019052764590020','102019052782980019','2019-05-27 01:52:57','510100040001','2019-05-27',NULL,'B'),('202019052781440022','102019052720840021','2019-05-27 03:49:01','500100030002','2019-05-27',NULL,'C'),('202019052971770026','102019052922300025','2019-05-29 09:16:15','500100030002','2019-05-29',NULL,'C'),('202019052983980024','102019052937640023','2019-05-29 01:06:30','500100030002','2019-05-29',NULL,'C'),('202019052990380028','102019052930750027','2019-05-29 09:16:36','500100030002','2019-05-29',NULL,'C'),('202019053102250039','102019053106230038','2019-05-31 15:31:48','100100040001','2019-05-31',NULL,'B'),('202019053118750035','102019053177220033','2019-05-31 15:31:36','100100040002','2019-05-31',NULL,'C'),('202019053123400034','102019053177220033','2019-05-31 15:31:36','200100070001','2019-05-31',NULL,'C'),('202019053129130037','102019053130210036','2019-05-31 15:31:41','100100040001','2019-05-31',NULL,'C'),('202019053129390032','102019053189380031','2019-05-31 15:31:33','100100040001','2019-05-31',NULL,'C'),('202019053176570030','102019053162680029','2019-05-31 02:01:58','500100030002','2019-05-31',NULL,'C'),('202019060203320004','102019060263110003','2019-06-02 04:15:12','111100030001','2019-06-02',NULL,'B'),('202019060206150003','102019060258440001','2019-06-02 05:21:15','600100050001','2019-06-02',NULL,'C'),('202019060225140005','102019060203170004','2019-06-02 05:22:43','111100030001','2019-06-02',NULL,'C'),('202019060229640003','102019060283010001','2019-06-02 05:02:14','600100050001','2019-06-02',NULL,'B'),('202019060233250002','102019060283010001','2019-06-02 05:02:14','111100050001','2019-06-02',NULL,'C'),('202019060235590006','102019060255430005','2019-06-02 02:07:18','620100040001','2019-06-02',NULL,'C'),('202019060241450002','102019060275830001','2019-06-02 04:48:06','111100030001','2019-06-02',NULL,'C'),('202019060243610008','102019060242100007','2019-06-02 05:27:02','500100030002','2019-06-02',NULL,'C'),('202019060243950002','102019060212210001','2019-06-01 16:11:44','620100030001','2019-06-02',NULL,'C'),('202019060244160005','102019060263110003','2019-06-02 04:15:12','600100030001','2019-06-02',NULL,'B'),('202019060248010008','102019060244610007','2019-06-02 03:40:02','111100030001','2019-06-02',NULL,'C'),('202019060250310004','102019060283010001','2019-06-02 05:02:18','DO',NULL,'业务系统实例失败，发起撤单','DO'),('202019060254680006','102019060203170004','2019-06-02 05:22:43','600100030001','2019-06-02',NULL,'C'),('202019060268370003','102019060275830001','2019-06-02 04:48:06','600100030001','2019-06-02',NULL,'C'),('202019060282330004','102019060202660003','2019-06-01 16:11:57','620100040001','2019-06-02',NULL,'B'),('202019060284310002','102019060258440001','2019-06-02 05:21:15','111100050001','2019-06-02',NULL,'C'),('202019060296240002','102019060201910001','2019-06-02 04:14:20','111100050001','2019-06-02',NULL,'C'),('202019060296560002','102019060204980001','2019-06-01 16:33:32','620100040001','2019-06-02',NULL,'C'),('202019060297870004','102019060222870003','2019-06-01 16:33:43','620100040001','2019-06-02',NULL,'C'),('202019060317050003','102019060370080001','2019-06-03 08:05:59','600100040001','2019-06-03',NULL,'C'),('202019060323600002','102019060348810001','2019-06-03 07:49:01','610100030001','2019-06-03',NULL,'B'),('202019060359820003','102019060348810001','2019-06-03 07:49:01','600100040001','2019-06-03',NULL,'B'),('202019060360120002','102019060353620001','2019-06-03 13:22:53','610100030001','2019-06-03',NULL,'C'),('202019060386250002','102019060370080001','2019-06-03 08:05:59','610100030001','2019-06-03',NULL,'C'),('202019060392620003','102019060353620001','2019-06-03 13:22:53','600100040001','2019-06-03',NULL,'C'),('202019060408020009','102019060423120007','2019-06-04 06:57:58','600100030001','2019-06-04',NULL,'C'),('202019060411750008','102019060423120007','2019-06-04 06:57:58','111100030001','2019-06-04',NULL,'C'),('202019060430620014','102019060411160013','2019-06-04 09:36:57','500100030002','2019-06-04',NULL,'C'),('202019060444320003','102019060413270001','2019-06-03 17:48:06','600100040001','2019-06-04',NULL,'C'),('202019060446370012','102019060437820010','2019-06-04 07:00:44','600100040001','2019-06-04',NULL,'C'),('202019060473410011','102019060437820010','2019-06-04 07:00:44','610100030001','2019-06-04',NULL,'C'),('202019060481650006','102019060482110004','2019-06-04 05:20:53','100100040002','2019-06-04',NULL,'C'),('202019060487860002','102019060413270001','2019-06-03 17:48:06','610100030001','2019-06-04',NULL,'C'),('202019060495500005','102019060482110004','2019-06-04 05:20:53','200100070001','2019-06-04',NULL,'C'),('202019060508030008','102019060576320007','2019-06-04 16:37:32','540100050001','2019-06-05',NULL,'C'),('202019060527930005','102019060551920003','2019-06-05 09:35:55','500100040002',NULL,NULL,'S'),('202019060536030004','102019060536340003','2019-06-04 16:25:52','540100030001','2019-06-05',NULL,'C'),('202019060538540004','102019060551920003','2019-06-05 09:35:55','510100050001','2019-06-05',NULL,'B'),('202019060546810010','102019060519640008','2019-06-05 13:48:55','600100040001','2019-06-05',NULL,'C'),('202019060548330007','102019060559430006','2019-06-05 10:41:47','500100030002','2019-06-05',NULL,'C'),('202019060550640006','102019060588900005','2019-06-04 16:26:48','540100040001','2019-06-05',NULL,'B'),('202019060567090004','102019060534750003','2019-06-04 16:47:11','540100040001','2019-06-05',NULL,'C'),('202019060575570002','102019060528910001','2019-06-05 03:25:33','500100030002','2019-06-05',NULL,'C'),('202019060580010002','102019060591170001','2019-06-04 16:46:41','540100030001','2019-06-05',NULL,'C'),('202019060590530002','102019060500850001','2019-06-04 16:21:35','540100030001','2019-06-05',NULL,'B'),('202019060597950009','102019060519640008','2019-06-05 13:48:55','610100030001','2019-06-05',NULL,'C'),('202019060621390016','102019060659470014','2019-06-06 05:09:43','100100040002','2019-06-06',NULL,'C'),('202019060621530004','102019060665630003','2019-06-05 16:39:00','500100030002','2019-06-06',NULL,'C'),('202019060639000008','102019060670880007','2019-06-05 16:39:14','500100030002','2019-06-06',NULL,'C'),('202019060647320011','102019060609090009','2019-06-06 05:08:53','200100060001','2019-06-06',NULL,'C'),('202019060652360002','102019060612300001','2019-06-05 16:38:50','500100030002','2019-06-06',NULL,'C'),('202019060669570006','102019060621550005','2019-06-05 16:39:09','500100030002','2019-06-06',NULL,'C'),('202019060684560018','102019060684800017','2019-06-06 10:18:49','540100030001','2019-06-06',NULL,'C'),('202019060694370010','102019060609090009','2019-06-06 05:08:53','100100030001','2019-06-06',NULL,'C'),('202019060696860015','102019060659470014','2019-06-06 05:09:43','200100070001','2019-06-06',NULL,'C'),('202019060697010013','102019060652810012','2019-06-06 05:09:37','100100040001','2019-06-06',NULL,'C'),('202019060803860002','102019060863430001','2019-06-08 04:24:21','111200030001','2019-06-08',NULL,'B'),('202019060804680020','102019060878480016','2019-06-08 04:29:25','610100030001',NULL,NULL,'S'),('202019060806430017','102019060878480016','2019-06-08 04:29:25','111200030001','2019-06-08',NULL,'B'),('202019060810530012','102019060885810011','2019-06-08 04:28:52','111200030001','2019-06-08',NULL,'B'),('202019060811200003','102019060863430001','2019-06-08 04:24:21','540100040001','2019-06-08',NULL,'B'),('202019060818100002','102019060880610001','2019-06-08 04:04:54','111200030001','2019-06-08',NULL,'B'),('202019060824640018','102019060878480016','2019-06-08 04:29:25','540100040001','2019-06-08',NULL,'B'),('202019060824650004','102019060863430001','2019-06-08 04:24:21','600100030001','2019-06-08',NULL,'B'),('202019060829860005','102019060863430001','2019-06-08 04:24:21','610100030001',NULL,NULL,'S'),('202019060837510009','102019060899750006','2019-06-08 04:09:24','600100030001',NULL,NULL,'S'),('202019060843340010','102019060881190006','2019-06-08 04:27:51','610100030001',NULL,NULL,'S'),('202019060844940010','102019060899750006','2019-06-08 04:09:24','610100030001',NULL,NULL,'S'),('202019060845080002','102019060873850001','2019-06-08 05:55:04','111200030001','2019-06-08',NULL,'C'),('202019060845520005','102019060880610001','2019-06-08 04:04:54','610100030001',NULL,NULL,'S'),('202019060846020007','102019060899750006','2019-06-08 04:09:24','111200030001','2019-06-08',NULL,'B'),('202019060847790007','102019060881190006','2019-06-08 04:27:50','111200030001','2019-06-08',NULL,'B'),('202019060855190008','102019060899750006','2019-06-08 04:09:24','540100040001','2019-06-08',NULL,'B'),('202019060863360003','102019060873850001','2019-06-08 05:55:04','540100040001','2019-06-08',NULL,'C'),('202019060866090015','102019060885810011','2019-06-08 04:28:52','610100030001',NULL,NULL,'S'),('202019060868610004','102019060873850001','2019-06-08 05:55:04','600100030001','2019-06-08',NULL,'C'),('202019060868900003','102019060880610001','2019-06-08 04:04:54','540100040001','2019-06-08',NULL,'B'),('202019060876380014','102019060885810011','2019-06-08 04:28:52','600100030001','2019-06-08',NULL,'B'),('202019060888560019','102019060878480016','2019-06-08 04:29:25','600100030001','2019-06-08',NULL,'B'),('202019060888850004','102019060880610001','2019-06-08 04:04:54','600100030001',NULL,NULL,'S'),('202019060890760009','102019060881190006','2019-06-08 04:27:51','600100030001','2019-06-08',NULL,'B'),('202019060893580008','102019060881190006','2019-06-08 04:27:51','540100040001','2019-06-08',NULL,'B'),('202019060894030013','102019060885810011','2019-06-08 04:28:52','540100040001','2019-06-08',NULL,'B'),('202019060899970005','102019060873850001','2019-06-08 05:55:04','610100030001','2019-06-08',NULL,'C'),('202019060905050002','102019060930270001','2019-06-08 16:50:46','111200050001','2019-06-09',NULL,'C'),('202019060905130011','102019060931930010','2019-06-08 16:56:43','111200050001','2019-06-09',NULL,'C'),('202019060913420016','102019060930740014','2019-06-08 16:57:29','540100040001','2019-06-09',NULL,'C'),('202019060915600015','102019060930740014','2019-06-08 16:57:29','111200030001','2019-06-09',NULL,'C'),('202019060926100008','102019060990270005','2019-06-08 16:54:03','600100030001','2019-06-09',NULL,'C'),('202019060926800003','102019060930270001','2019-06-08 16:50:46','540100040001','2019-06-09',NULL,'C'),('202019060943070013','102019060931930010','2019-06-08 16:56:43','600100050001','2019-06-09',NULL,'C'),('202019060956040017','102019060930740014','2019-06-08 16:57:29','600100030001','2019-06-09',NULL,'C'),('202019060967560006','102019060990270005','2019-06-08 16:54:03','111200030001','2019-06-09',NULL,'C'),('202019060969220004','102019060930270001','2019-06-08 16:50:46','600100050001','2019-06-09',NULL,'C'),('202019060971230018','102019060930740014','2019-06-08 16:57:29','610100030001','2019-06-09',NULL,'C'),('202019060978860007','102019060990270005','2019-06-08 16:54:03','540100040001','2019-06-09',NULL,'C'),('202019060984100012','102019060931930010','2019-06-08 16:56:43','540100040001','2019-06-09',NULL,'C'),('202019060995750009','102019060990270005','2019-06-08 16:54:03','610100030001','2019-06-09',NULL,'C'),('202019061002600005','102019061048060001','2019-06-09 16:08:17','610100030001','2019-06-10',NULL,'C'),('202019061017320007','102019061050830006','2019-06-09 16:09:19','540100030001','2019-06-10',NULL,'C'),('202019061018820014','102019061095420012','2019-06-09 16:11:03','540100040001','2019-06-10',NULL,'C'),('202019061027520016','102019061095420012','2019-06-09 16:11:03','610100030001','2019-06-10',NULL,'C'),('202019061029490002','102019061025560001','2019-06-09 16:28:03','111200050001','2019-06-10',NULL,'C'),('202019061039240003','102019061048060001','2019-06-09 16:08:17','540100040001','2019-06-10',NULL,'C'),('202019061039910004','102019061048060001','2019-06-09 16:08:17','600100030001','2019-06-10',NULL,'C'),('202019061044690013','102019061095420012','2019-06-09 16:11:03','111200030001','2019-06-10',NULL,'C'),('202019061062540008','102019061000810005','2019-06-09 16:28:49','600100050001','2019-06-10',NULL,'C'),('202019061066360003','102019061025560001','2019-06-09 16:28:03','540100040001','2019-06-10',NULL,'C'),('202019061070810006','102019061000810005','2019-06-09 16:28:49','111200050001','2019-06-10',NULL,'C'),('202019061072890002','102019061048060001','2019-06-09 16:08:17','111200030001','2019-06-10',NULL,'C'),('202019061080790015','102019061095420012','2019-06-09 16:11:03','600100030001','2019-06-10',NULL,'C'),('202019061086170007','102019061000810005','2019-06-09 16:28:49','540100040001','2019-06-10',NULL,'C'),('202019061089200004','102019061025560001','2019-06-09 16:28:03','600100050001','2019-06-10',NULL,'C'),('202019061090550011','102019061046360010','2019-06-09 16:10:00','540100030001','2019-06-10',NULL,'C'),('202019061093600009','102019061018570008','2019-06-09 16:09:43','540100030001','2019-06-10',NULL,'C'),('20512771454920556544','10512771452089401344','2018-11-15 15:30:42','D',NULL,'保存用户录入费用信息','S'),('20512773563694972928','10512773560800903168','2018-11-15 15:39:05','D',NULL,'保存用户录入费用信息','E'),('20512774199522099200','10512774197236203520','2018-11-15 15:41:36','D',NULL,'保存用户录入费用信息','E'),('20512774704356917248','10512774702192656384','2018-11-15 15:43:37','D',NULL,'保存用户录入费用信息','E'),('20512774784430374912','10512774782215782400','2018-11-15 15:43:56','D',NULL,'保存用户录入费用信息','E'),('20512778369964703744','10512778366974164992','2018-11-15 15:58:11','D',NULL,'保存用户录入费用信息','E'),('20512781063739670528','10512781061441191936','2018-11-15 16:08:53','D',NULL,'保存用户录入费用信息','E'),('20512781924033363968','10512781921827160064','2018-11-15 16:12:18','D',NULL,'保存用户录入费用信息','E'),('20512782243995844608','10512782241781252096','2018-11-15 16:13:34','D',NULL,'保存用户录入费用信息','S'),('20512782362006781952','10512782357623734272','2018-11-15 16:14:03','D',NULL,'保存用户录入费用信息','S'),('20513135740868100096','10513135737009340416','2018-11-16 15:38:14','D',NULL,'保存用户录入费用信息','S'),('20513136394307108864','10513136391413039104','2018-11-16 15:40:50','D',NULL,'保存用户录入费用信息','E'),('20513138496731348992','10513138492071477248','2018-11-16 15:49:11','D',NULL,'保存用户录入费用信息','E'),('20513138960449404928','10513138958218035200','2018-11-16 15:51:02','D',NULL,'保存用户录入费用信息','E'),('20513139668225622016','10513139665960697856','2018-11-16 15:53:51','D',NULL,'保存用户录入费用信息','E'),('20513140469312520192','10513140466590416896','2018-11-16 15:57:02','D',NULL,'保存用户录入费用信息','E'),('20513141047354720256','10513141044963966976','2018-11-16 15:59:20','D',NULL,'保存商户录入用户应缴费用信息','E'),('20513143019977834496','10513143017092153344','2018-11-16 16:07:10','D',NULL,'保存用户录入费用信息','S'),('20513145624225382400','10513145621712994304','2018-11-16 16:17:31','D',NULL,'保存商户录入用户应缴费用信息','S'),('20513145829515591680','10513145827309387776','2018-11-16 16:18:20','D',NULL,'保存商户录入用户应缴费用信息','S'),('20513146865416404992','10513146861406650368','2018-11-16 16:22:27','D',NULL,'保存商户录入用户应缴费用信息','S'),('20516292810350018560','10516292807556612096','2018-11-25 08:43:18','D',NULL,NULL,'E'),('20516293454838382592','10516293454683193344','2018-11-25 08:45:51','D',NULL,NULL,'E'),('20516297441130070016','10516297437925621760','2018-11-25 09:01:42','D',NULL,NULL,'E'),('20516297910573350912','10516297910447521792','2018-11-25 09:03:34','D',NULL,NULL,'E'),('20516329683810271232','10516329683684442112','2018-11-25 11:09:49','D',NULL,NULL,'S'),('20516332294412189696','10516332294286360576','2018-11-25 11:20:11','D','2018-11-25',NULL,'C'),('20516364589907066880','10516364589760266240','2018-11-25 13:28:31','D','2018-11-25',NULL,'C'),('20516365201587585024','10516365201428201472','2018-11-25 13:30:57','D','2018-11-25',NULL,'C'),('20516366112951123968','10516366112816906240','2018-11-25 13:34:34','D','2018-11-25',NULL,'C'),('20516366708928167936','10516366708789755904','2018-11-25 13:36:57','D','2018-11-25',NULL,'C'),('20516368306257543168','10516368306135908352','2018-11-25 13:43:17','D','2018-11-25',NULL,'C'),('20516374464208846848','10516374464053657600','2018-11-25 14:07:46','D','2018-11-25',NULL,'C'),('20516374772574076928','10516374772444053504','2018-11-25 14:08:59','D','2018-11-25',NULL,'C'),('20516374801669963776','10516374801552523264','2018-11-25 14:09:06','D','2018-11-25',NULL,'C'),('20516377393317822464','10516377393070358528','2018-11-25 14:19:24','D','2018-11-25',NULL,'C'),('20516381939620397056','10516381939469402112','2018-11-25 14:37:28','D','2018-11-25',NULL,'C'),('20516387722659643392','10516387722441539584','2018-11-25 15:00:27','D','2018-11-25',NULL,'C'),('20516389224736374784','10516389224614739968','2018-11-25 15:06:25','D','2018-11-25',NULL,'C'),('20516389265110745088','10516389264993304576','2018-11-25 15:06:34','D','2018-11-25',NULL,'C'),('20516398820167270400','10516398820054024192','2018-11-25 15:44:32','D','2018-11-25',NULL,'C'),('20516400356243030016','10516400356129783808','2018-11-25 15:50:39','D','2018-11-25',NULL,'C'),('20516401273893830656','10516401273776390144','2018-11-25 15:54:17','D','2018-11-25',NULL,'C'),('20517072482935521280','10517072482688057344','2018-11-27 12:21:26','D','2018-11-27',NULL,'C'),('20517086035457359872','10517086035339919360','2018-11-27 13:15:17','D','2018-11-27',NULL,'C'),('20517710159825354752','10517710156889341952','2018-11-29 06:35:20','D','2018-11-29',NULL,'C'),('20518939876956061696','10518939876595351552','2018-12-02 16:01:48','D','2018-12-03',NULL,'C'),('20518940136344403968','10518940136222769152','2018-12-02 16:02:49','D','2018-12-03',NULL,'C'),('20520751770314489856','10520751769869893632','2018-12-07 16:01:37','D','2018-12-08',NULL,'C'),('20520752094244782080','10520752094110564352','2018-12-07 16:02:54','D','2018-12-08',NULL,'C'),('20520752515906551808','10520752515780722688','2018-12-07 16:04:34','D','2018-12-08',NULL,'C'),('20520752654096285696','10520752653978845184','2018-12-07 16:05:07','D','2018-12-08',NULL,'C'),('20520753111019569152','10520753110897934336','2018-12-07 16:06:56','D','2018-12-08',NULL,'C'),('20521002816139968512','10521002813006823424','2018-12-08 08:39:11','D','2018-12-08',NULL,'C'),('20521124897368326144','10521124884286291968','2018-12-08 16:44:17','D','2018-12-09',NULL,'B'),('20521125064184184832','10521125063953498112','2018-12-08 16:44:57','D','2018-12-09',NULL,'C'),('20521126624146505728','10521126624020676608','2018-12-08 16:51:09','D','2018-12-09',NULL,'B'),('20521126943907659776','10521126943786024960','2018-12-08 16:52:25','D','2018-12-09',NULL,'B'),('20521280168329756672','10521280167822245888','2018-12-09 03:01:17','D','2018-12-09',NULL,'B'),('20521280438367436800','10521280438203858944','2018-12-09 03:02:21','D','2018-12-09',NULL,'B'),('20521281261436682240','10521281261306658816','2018-12-09 03:05:37','D','2018-12-09',NULL,'B'),('20521289750926082048','10521289747750993920','2018-12-09 03:39:21','D',NULL,NULL,'E'),('20521292250362167296','10521292247023501312','2018-12-09 03:49:17','D','2018-12-09',NULL,'B'),('20521292260218781696','10521292247023501312','2018-12-09 03:49:19','DO',NULL,'业务系统实例失败，发起撤单','DO'),('20521294334742511616','10521294331546451968','2018-12-09 03:57:34','D','2018-12-09',NULL,'C'),('20521294983668449280','10521294983534231552','2018-12-09 04:00:09','D','2018-12-09',NULL,'C'),('20521299093675327488','10521299093553692672','2018-12-09 04:16:29','D','2018-12-09',NULL,'C'),('20521384632478875648','10521384632348852224','2018-12-09 09:56:23','D','2018-12-09',NULL,'C'),('20521384655455272960','10521384655329443840','2018-12-09 09:56:28','D','2018-12-09',NULL,'C'),('20521384680210055168','10521384680075837440','2018-12-09 09:56:34','D','2018-12-09',NULL,'C'),('20521384772962893824','10521384772841259008','2018-12-09 09:56:56','D','2018-12-09',NULL,'C'),('20521384859759820800','10521384859650768896','2018-12-09 09:57:17','D','2018-12-09',NULL,'C'),('20521388278868361216','10521388278742532096','2018-12-09 10:10:52','D','2018-12-09',NULL,'C'),('20521388292466294784','10521388292340465664','2018-12-09 10:10:55','D','2018-12-09',NULL,'C'),('20521388308534673408','10521388308358512640','2018-12-09 10:10:59','D','2018-12-09',NULL,'C'),('20521388322648506368','10521388322531065856','2018-12-09 10:11:03','D','2018-12-09',NULL,'C'),('20521388335667625984','10521388335550185472','2018-12-09 10:11:06','D','2018-12-09',NULL,'C'),('20521470366867013632','10521470366736990208','2018-12-09 15:37:03','D','2018-12-09',NULL,'C'),('20521470728747368448','10521470728638316544','2018-12-09 15:38:30','D','2018-12-09',NULL,'C'),('20521470972776169472','10521470972654534656','2018-12-09 15:39:28','D','2018-12-09',NULL,'C'),('20521471733417394176','10521471733287370752','2018-12-09 15:42:29','D','2018-12-09',NULL,'C'),('20521472810674044928','10521472810514661376','2018-12-09 15:46:46','D','2018-12-09',NULL,'C'),('20521692192004128768','10521692191735693312','2018-12-10 06:18:31','D','2018-12-10',NULL,'C'),('20521692260908154880','10521692260782325760','2018-12-10 06:18:47','D','2018-12-10',NULL,'C'),('20521692315283111936','10521692315148894208','2018-12-10 06:19:00','D','2018-12-10',NULL,'C'),('20521692341828861952','10521692341610758144','2018-12-10 06:19:06','D','2018-12-10',NULL,'C'),('20521692506895695872','10521692506786643968','2018-12-10 06:19:46','D','2018-12-10',NULL,'C'),('20521692649367814144','10521692648902246400','2018-12-10 06:20:20','D','2018-12-10',NULL,'C'),('20522549147056750592','10522549146947698688','2018-12-12 15:03:45','D','2018-12-12',NULL,'C'),('20523565515797446656','10523565515541594112','2018-12-15 10:22:26','D','2018-12-15',NULL,'C');

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
  `business_type_cd` varchar(12) NOT NULL COMMENT '业务项类型',
  `name` varchar(50) NOT NULL COMMENT '名称',
  `description` varchar(200) DEFAULT NULL COMMENT '描述',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `business_type_cd` (`business_type_cd`),
  UNIQUE KEY `business_type_cd_2` (`business_type_cd`)
) ENGINE=InnoDB AUTO_INCREMENT=73 DEFAULT CHARSET=utf8;

/*Data for the table `c_business_type` */

insert  into `c_business_type`(`id`,`business_type_cd`,`name`,`description`,`create_time`) values (1,'DO','撤单','作废订单','2019-02-07 17:04:21'),(3,'100100030001','保存用户信息','保存用户信息','2019-02-07 17:05:15'),(4,'100100030002','保存用户地址信息','保存用户地址信息','2019-02-07 17:05:47'),(6,'100100030003','用户打标','用户打标','2019-02-07 17:20:49'),(7,'100100030004','用户证件','保存用户地址信息','2019-02-07 17:20:49'),(8,'100100040001','修改用户信息','修改用户信息','2019-02-07 17:21:08'),(9,'100100040002','停用用户信息','停用用户信息','2019-02-07 17:21:08'),(10,'100100040003','恢复用户信息','恢复用户信息','2019-02-07 17:21:08'),(11,'200100030001','保存商户信息','保存商户信息','2019-02-07 17:21:08'),(12,'200100030002','商户成员加入信息','商户成员加入信息','2019-02-07 17:21:08'),(13,'200100040001','修改商户信息','修改商户信息','2019-02-07 17:21:08'),(14,'200100040002','商户成员退出信息','商户成员退出信息','2019-02-07 17:21:09'),(15,'200100050001','删除商户信息','删除商户信息','2019-02-07 17:21:09'),(16,'300100030001','保存商品信息','保存商品信息','2019-02-07 17:21:09'),(17,'300100030002','购买商品','购买商品','2019-02-07 17:21:09'),(18,'300100030003','保存商品目录','保存商品目录','2019-02-07 17:21:09'),(19,'300100040001','修改商品信息','修改商品信息','2019-02-07 17:21:09'),(20,'300100040002','修改商品目录','修改商品目录','2019-02-07 17:21:09'),(21,'300100050001','删除商品信息','删除商品信息','2019-02-07 17:21:09'),(22,'300100050002','修改商品目录','修改商品目录','2019-02-07 17:21:09'),(23,'400100030001','保存评论','保存评论','2019-02-07 17:21:09'),(24,'400100050001','删除评论','删除评论','2019-02-07 17:21:09'),(25,'500100030001','保存小区信息','保存小区信息','2019-02-07 17:21:09'),(26,'500100030002','小区成员加入信息','小区成员加入信息','2019-02-07 17:21:09'),(27,'500100040001','修改商户信息','修改商户信息','2019-02-07 17:21:10'),(28,'500100040002','小区成员退出信息','小区成员退出信息','2019-02-07 17:21:10'),(29,'500100050001','删除商户信息','删除商户信息','2019-02-07 17:21:10'),(30,'610100030001','保存费用明细信息','保存费用明细信息','2019-02-07 17:21:10'),(31,'610100040001','修改费用明细信息','修改费用明细信息','2019-02-07 17:21:10'),(32,'610100050001','删除费用明细信息','删除费用明细信息','2019-02-07 17:21:10'),(33,'620100030001','保存费用配置','保存费用配置','2019-02-07 17:21:10'),(34,'620100040001','修改费用配置','修改费用配置','2019-02-07 17:21:10'),(35,'620100050001','删除费用配置','删除费用配置','2019-02-07 17:21:10'),(36,'600100030001','保存费用信息','保存费用信息','2019-02-07 17:21:10'),(37,'600100040001','修改费用信息','修改费用信息','2019-02-07 17:21:10'),(38,'600100050001','删除费用信息','删除费用信息','2019-02-07 17:21:10'),(45,'700100030001','保存代理商信息','保存代理商信息','2019-02-07 17:21:11'),(46,'700100030002','保存代理商照片','保存代理商照片','2019-02-07 17:21:11'),(47,'700100030003','保存代理商证件','保存代理商证件','2019-02-07 17:21:11'),(48,'700100030004','添加代理商员工','添加代理商员工','2019-02-07 17:21:11'),(49,'700100040001','修改代理商信息','修改代理商信息','2019-02-07 17:21:11'),(50,'700100040002','修改代理商照片','修改代理商照片','2019-02-07 17:21:11'),(51,'700100040003','修改代理商证件','修改代理商证件','2019-02-07 17:21:11'),(52,'700100050001','删除代理商属性','删除代理商属性','2019-02-07 17:21:11'),(53,'700100050002','删除代理商照片','删除代理商照片','2019-02-07 17:21:11'),(54,'700100050003','删除代理商证件','删除代理商证件','2019-02-07 17:21:11'),(55,'700100050004','删除代理商员工','删除代理商员工','2019-02-07 17:21:11'),(56,'200100060001','添加商户员工','添加商户员工','2019-03-30 03:17:24'),(57,'200100070001','删除商户员工','删除商户员工','2019-03-30 03:17:52'),(58,'510100030001','保存小区楼','保存小区楼','2019-04-21 09:05:20'),(59,'510100040001','修改小区楼','修改小区楼','2019-04-21 09:05:57'),(60,'510100050001','删除小区楼','删除小区楼','2019-04-21 09:06:15'),(61,'520100030001','保存小区单元','保存小区单元','2019-04-21 09:05:20'),(62,'520100040001','修改小区单元','修改小区单元','2019-04-21 09:05:57'),(63,'520100050001','删除小区单元','删除小区单元','2019-04-21 09:06:15'),(64,'530100030001','保存房屋信息','保存房屋信息','2019-05-08 10:46:47'),(65,'530100040001','修改房屋信息','修改房屋信息','2019-05-08 10:47:04'),(66,'530100050001','删除房屋信息','删除房屋信息','2019-05-08 10:47:17'),(67,'540100030001','保存停车位信息','保存停车位信息','2019-05-08 10:46:47'),(68,'540100040001','修改停车位信息','修改停车位信息','2019-05-08 10:47:04'),(69,'540100050001','删除停车位信息','删除停车位信息','2019-05-08 10:47:17'),(70,'111200030001','保存业主车辆','保存业主车辆','2019-02-07 17:21:09'),(71,'111200050001','修改业主车辆','删除业主车辆','2019-02-07 17:21:09'),(72,'111200040001','删除业主车辆','修改业主车辆','2019-02-07 17:21:10');

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
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8;

/*Data for the table `c_mapping` */

insert  into `c_mapping`(`id`,`domain`,`name`,`key`,`value`,`remark`,`create_time`,`status_cd`) values (1,'DOMAIN.COMMON','日志开关','LOG_ON_OFF','OFF','日志开关','2018-11-14 13:28:44','0'),(2,'DOMAIN.COMMON','耗时开关','COST_TIME_ON_OFF','OFF','耗时开关','2018-11-14 13:28:44','0'),(3,'DOMAIN.COMMON','规则开关','RULE_ON_OFF','OFF','规则开关','2018-11-14 13:28:44','0'),(4,'DOMAIN.COMMON','不调规则服务的订单类型','NO_NEED_RULE_VALDATE_ORDER','Q','不调规则服务的订单类型','2018-11-14 13:28:44','0'),(5,'DOMAIN.COMMON','不保存订单信息','NO_SAVE_ORDER','Q,T','不保存订单信息','2018-11-14 13:28:44','0'),(6,'DOMAIN.COMMON','不用调用 下游系统的配置','NO_INVOKE_BUSINESS_SYSTEM','Q','不用调用 下游系统的配置(一般不存在这种情况，这里主要是在没有下游系统的情况下测试中心服务用)','2018-11-14 13:28:44','0'),(7,'DOMAIN.COMMON','不用调用 作废下游系统的配置','NO_INVALID_BUSINESS_SYSTEM','Q','不用调用 作废下游系统的配置 (一般不存在这种情况，这里主要是在没有下游系统的情况下测试中心服务用)','2018-11-14 13:28:44','0'),(8,'DOMAIN.COMMON','需要调用服务生成各个ID','NEED_INVOKE_SERVICE_GENERATE_ID','OFF','需要调用服务生成各个ID','2018-11-14 13:28:44','0'),(9,'DOMAIN.COMMON','公钥','PUBLIC_STRING','MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDloKXSBA5+tP39uS8yi5RZOs6Jdrt0znRQetyXX2l/IUCi1x1QAMgoZbnEavmdZ5jOZN/T1WYFbt/VomXEHaTdStAiYm3DCnxxy5CMMyRKai0+6n4lLJQpUmnAQPFENrOV8b70gBSBVjUXksImgui5qYaNqX90pjEzcyq+6CugBwIDAQAB','公钥','2018-11-14 13:28:44','0'),(10,'DOMAIN.COMMON','私钥','PRIVATE_STRING','MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAJbtQYV+VpWZvifoc0R11MyAfIyMGoJKHDrWQau7oxLHotDDJM80o7ea7oL2onaHWOXaybpUp5FpZgjuixYMhlQOA/VXosrJOGJhgNv0dAL6VVXxmjlg2UWqIEoyTS7IzF3BuQCqy2k9aT7mGiC0RYRpndTuwe/0DKwFx70dIIIrAgMBAAECgYBMNMHnqLIJWZa1Sd6hy6lGFP5ObROZg9gbMUH5d4XQnrKsHEyCvz6HH5ic0fGYTaDqdn1zMvllJ8XYbrIV0P8lvHr9/LCnoXessmf61hKZyTKi5ycNkiPHTjmJZCoVTQFprcNgYeVX4cvNsqB2zWwzoAk8bbdWY6X5jB6YEpfBmQJBANiO9GiBtw+T9h60MpyV1xhJKsx0eCvxRZcsDB1OLZvQ7dHnsHmh0xUBL2MraHKnQyxOlrIzOtyttxSTrQzwcM0CQQCyajkzxpF6EjrXWHYVHb3AXFSoz5krjOkLEHegYlGhx0gtytBNVwftCn6hqtaxCxKMp00ZJoXIxo8d9tQy4N7XAkBljnTT5bEBnzPWpk7t298pRnbJtvz8LoOiJ0fvHlCJN+uvemXqRJeGzC165kpvKj14M8q7+wZpoxWukqqe3MspAkAuFYHw/blV7p+EQDU//w6kQTUc5YKK3TrUwMwlgT/UqcTbDyf+0hwZ/jv3RkluMY35Br3DYU/tLFyLQNZOzgbBAkEApWARXVlleEYbv8dPUL+56S0ky1hZSuPfVOBda4V3p0q18LjcHIyYcVhKGqkpii5JgblaYyjUriNDisFalCp8jQ==','私钥','2018-11-14 13:28:44','0'),(11,'DOMAIN.COMMON','外部应用公钥','PUBLIC_STRING','MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCW7UGFflaVmb4n6HNEddTMgHyMjBqCShw61kGru6MSx6LQwyTPNKO3mu6C9qJ2h1jl2sm6VKeRaWYI7osWDIZUDgP1V6LKyThiYYDb9HQC+lVV8Zo5YNlFqiBKMk0uyMxdwbkAqstpPWk+5hogtEWEaZ3U7sHv9AysBce9HSCCKwIDAQAB','外部应用公钥','2018-11-14 13:28:44','0'),(12,'DOMAIN.COMMON','外部应用私钥','PRIVATE_STRING','MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAOWgpdIEDn60/f25LzKLlFk6zol2u3TOdFB63JdfaX8hQKLXHVAAyChlucRq+Z1nmM5k39PVZgVu39WiZcQdpN1K0CJibcMKfHHLkIwzJEpqLT7qfiUslClSacBA8UQ2s5XxvvSAFIFWNReSwiaC6Lmpho2pf3SmMTNzKr7oK6AHAgMBAAECgYEAlfR5FVNM2/X6QC0k408/i53Zru94r2j7kGsLj1bhoAHpIe502AAKtkboL5rkc6Rpp688dCvRug6T4gFxj8cEF7rOOU4CHqVCHUHa4sWSDL2Rg7pMbQOVB7PGmM4C/hEgXcwM6tmLiU3xkkQDrpgT1bPpAm7iwDx4HkZBv1naYnECQQDyk40+KDvyUgp/r1tKbkMLoQOAfTZPXy+HgeAkU3PCUTFQlvn2OU6Fsl3Yjlp6utxPVnd00DoPZ8qvx1falaeLAkEA8lWoIDgyYwnibv2fr3A715PkieJ0exKfLb5lSD9UIfGJ9s7oTcltl7pprykfSP46heWjScS7YJRZHPfqb1/Y9QJAJNUQqjJzv7yDSZX3t5p8ZaSiIn1gpLagQeQPg5SETCoF4eW6uI9FA/nsU/hxdpcu4oEPjFYdqr8owH31MgRtNwJAaE+6qPPHrJ3qnAAMJoZXG/qLG1cg8IEZh6U3D5xC6MGBs31ovWMBC5iwOTeoQdE8+7nXSb+nMHFq0m9cuEg3qQJAH4caPSQ9RjVOP9on+niy9b1mATbvurepIB95KUtaHLz1hpihCLR7dTwrz8JOitgFE75Wzt4a00GZYxnaq3jsjA==','外部应用私钥','2018-11-14 13:28:44','0'),(13,'DOMAIN.COMMON','默认KEY_SIZE','DEFAULT_DECRYPT_KEY_SIZE','2048','默认KEY_SIZE','2018-11-14 13:28:44','0'),(14,'DOMAIN.COMMON','中心服务地址','CENTER_SERVICE_URL','http://center-service/httpApi/service','中心服务地址','2018-11-14 13:28:44','0'),(15,'DOMAIN.COMMON','控制中心APP_ID','CONSOLE_SERVICE_APP_ID','8000418002','控制中心APP_ID','2018-11-14 13:28:44','0'),(16,'DOMAIN.COMMON','控制服务加密开关','KEY_CONSOLE_SERVICE_SECURITY_ON_OFF','ON','控制服务加密开关','2018-11-14 13:28:44','0'),(17,'DOMAIN.COMMON','控制服务鉴权秘钥','CONSOLE_SECURITY_CODE','WEBURFPKIFJUHNCJUEIKMKJUJHULSMNCHDY89KMC','控制服务鉴权秘钥','2018-11-14 13:28:44','0'),(18,'DOMAIN.COMMON','编码生成服务地址','CODE_PATH','http://code-service/codeApi/generate','编码生成服务地址','2018-11-14 13:28:44','0'),(19,'DOMAIN.COMMON','API服务地址','API_SERVICE_URL','http://api-service/api/#serviceCode#','API服务地址','2018-11-18 07:35:03','0'),(20,'DOMAIN.COMMON','员工默认密码','STAFF_DEFAULT_PASSWORD','123456','员工默认密码','2018-12-08 16:33:14','0'),(21,'DOMAIN.COMMON','用户服务地址','ORDER_USER_SERVICE_URL','http://user-service/userApi/service','用户服务地址','2019-02-08 13:51:33','0'),(22,'DOMAIN.COMMON','商户服务地址','ORDER_STORE_SERVICE_URL','http://store-service/storeApi/service','商户服务地址','2019-02-08 13:54:53','0'),(23,'DOMAIN.COMMON','商品服务地址','ORDER_SHOP_SERVICE_URL','http://shop-service/shopApi/service','商品服务地址','2019-02-08 13:57:57','0'),(24,'DOMAIN.COMMON','评价服务地址','ORDER_COMMENT_SERVICE_URL','http://comment-service/commentApi/service','评价服务地址','2019-02-08 14:00:56','0'),(25,'DOMAIN.COMMON','小区服务地址','ORDER_COMMUNITY_SERVICE_URL','http://community-service/communityApi/service','小区服务地址','2019-02-08 14:04:28','0'),(26,'DOMAIN.COMMON','费用服务地址','ORDER_FEE_SERVICE_URL','http://fee-service/feeApi/service','费用服务地址','2019-02-08 14:07:09','0'),(27,'DOMAIN.COMMON','代理商服务地址','ORDER_AGENT_SERVICE_URL','http://agent-service/agentApi/service','代理商服务地址','2019-02-08 14:10:14','0'),(28,'DEFAULT_PRIVILEGE_ADMIN','物业对应的管理员权限组','800900000003','600201904002','物业对应的管理员权限组','2019-04-01 12:24:36','0'),(29,'DEFAULT_PRIVILEGE','物业对应的员工权限组','800900000003','600201904001','物业对应的员工权限组','2019-04-02 10:55:53','0'),(30,'STORE_TYPE_2_COMMUNITY_MEMBER_TYPE','商户类型对应小区成员类型映射','800900000003','390001200002','商户类型对应小区成员类型映射','2019-04-15 02:49:43','0'),(31,'COMMUNITY_MEMBER_AUDIT','物业入驻小区需要审核','390001200002','1000','物业入驻小区需要审核','2019-04-26 16:35:39','0'),(32,'COMMUNITY_MEMBER_AUDIT','物业入驻商户需要审核','390001200001','1000','物业入驻商户需要审核','2019-04-26 16:35:49','0');

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

/*Data for the table `c_orders` */

insert  into `c_orders`(`o_id`,`app_id`,`ext_transaction_id`,`user_id`,`request_time`,`create_time`,`order_type_cd`,`finish_time`,`remark`,`status_cd`) values ('-1','8000418002','10029082726','1234','20181113225612','2018-12-09 03:16:38','D',NULL,NULL,'C'),('102018111500000001','8000418001','100000000020180409224736000001','1101','20181115215136','2018-11-15 15:04:21','D',NULL,'保存用户录入费用信息','C'),('102018111500000002','8000418001','100000000020180409224736000001','1101','20181115215136','2018-11-15 15:16:48','D',NULL,'保存用户录入费用信息','C'),('102018111500000003','8000418001','100000000020180409224736000001','1101','20181115215136','2018-11-15 15:17:19','D',NULL,'保存用户录入费用信息','C'),('102018111500000004','8000418001','100000000020180409224736000001','1101','20181115215136','2018-11-15 15:18:04','D',NULL,'保存用户录入费用信息','C'),('102018111500000005','8000418001','100000000020180409224736000001','1101','20181115215136','2018-11-15 15:19:03','D',NULL,'保存用户录入费用信息','C'),('102019020200000001','8000418002','18094ce026f011e98223f3a4fd3a50b2','-1','20190202214024','2019-02-02 13:40:26','D',NULL,NULL,'C'),('102019020200000002','8000418002','e87abdd026f211e98223f3a4fd3a50b2','-1','20190202220033','2019-02-02 14:00:33','D',NULL,NULL,'C'),('102019020300000002','8000418002','9c45b530270311e98223f3a4fd3a50b2','-1','20190203000007','2019-02-02 16:00:07','D',NULL,NULL,'C'),('102019021000000001','8000418002','10029082726','-1','20181113225612','2019-02-09 16:06:43','D',NULL,NULL,'S'),('102019021000000002','8000418002','10029082726','-1','20181113225612','2019-02-09 16:24:20','D',NULL,NULL,'S'),('102019021000000003','8000418002','10029082726','-1','20181113225612','2019-02-09 16:28:16','D',NULL,NULL,'S'),('102019021000000004','8000418002','10029082726','-1','20181113225612','2019-02-09 16:24:42','D',NULL,NULL,'S'),('102019021000000005','8000418002','10029082726','-1','20181113225612','2019-02-09 16:30:04','D',NULL,NULL,'S'),('102019021000000006','8000418002','10029082726','-1','20181113225612','2019-02-09 16:36:01','D',NULL,NULL,'S'),('102019021000000007','8000418002','10029082726','-1','20181113225612','2019-02-09 16:31:24','D',NULL,NULL,'S'),('102019021000000008','8000418002','10029082726','-1','20181113225612','2019-02-09 16:57:44','D',NULL,NULL,'C'),('102019021024050001','8000418002','20d69a102d4111e990a5af8dfa9ee364','-1','20190210223535','2019-02-10 14:35:36','D',NULL,NULL,'C'),('102019021031750001','8000418002','10029082726','-1','20181113225612','2019-02-09 17:17:40','D',NULL,NULL,'C'),('102019021042560005','8000418002','53a5f4e02d4111e990a5af8dfa9ee364','-1','20190210223701','2019-02-10 14:37:01','D',NULL,NULL,'C'),('102019021043400007','8000418002','84820b302d4111e990a5af8dfa9ee364','-1','20190210223823','2019-02-10 14:38:23','D',NULL,NULL,'C'),('102019021083940003','8000418002','2c05e9e02d4111e990a5af8dfa9ee364','-1','20190210223554','2019-02-10 14:35:54','D',NULL,NULL,'C'),('102019021086310003','8000418002','10029082726','-1','20181113225612','2019-02-09 17:23:13','D',NULL,NULL,'C'),('102019032334900001','8000418002','10029082726','-1','20181113225612','2019-03-23 08:59:27','D',NULL,NULL,'C'),('102019032510060005','8000418004','e95441d7-2b10-4492-a64f-dd413c70e084','-1','20190325043048','2019-03-25 08:30:52','D',NULL,NULL,'C'),('102019032517210003','8000418004','6eceaddd-e772-4996-9c13-6def7d51c1ba','-1','20190325042417','2019-03-25 08:24:20','D',NULL,NULL,'S'),('102019032519540009','8000418004','c233cca6-afcd-4fcb-8a11-93c1dbab2b4a','-1','20190325115533','2019-03-25 15:55:33','D',NULL,NULL,'C'),('102019032551670007','8000418004','d76bc287-2e79-4108-8494-2b0fe2c76c70','-1','20190325043452','2019-03-25 08:34:56','D',NULL,NULL,'C'),('102019032923910003','8000418004','10029082726','-1','20181113225612','2019-03-29 14:11:50','D',NULL,NULL,'S'),('102019032961460009','8000418004','6d2ceb9f-e20b-4ba3-a0d5-2d81585f5079','-1','20190329102141','2019-03-29 14:21:41','D',NULL,NULL,'C'),('102019032967090007','8000418004','10029082726','-1','20181113225612','2019-03-29 14:13:26','D',NULL,NULL,'C'),('102019032994640001','8000418004','10029082726','-1','20181113225612','2019-03-29 14:11:20','D',NULL,NULL,'S'),('102019032995960005','8000418004','10029082726','-1','20181113225612','2019-03-29 14:13:02','D',NULL,NULL,'S'),('102019033002560001','8000418002','10029082726','','20181113225612','2019-03-30 04:04:35','D',NULL,NULL,'D'),('102019033009000003','8000418004','10029082726','-1','20181113225612','2019-03-30 05:32:16','D',NULL,NULL,'C'),('102019033010240001','8000418004','10029082726','-1','20181113225612','2019-03-30 06:51:21','D',NULL,NULL,'C'),('102019033018530003','8000418002','10029082726','','20181113225612','2019-03-30 03:20:35','D',NULL,NULL,'S'),('102019033026120001','8000418004','10029082726','-1','20181113225612','2019-03-30 08:31:27','D',NULL,NULL,'C'),('102019033028470001','8000418002','10029082726','','20181113225612','2019-03-30 05:17:13','D',NULL,NULL,'C'),('102019033030080006','8000418004','e2efb8a8-adb7-4142-85ca-81f0567e58bf','-1','20190330043731','2019-03-30 08:37:31','D',NULL,NULL,'C'),('102019033046150003','8000418004','10029082726','-1','20181113225612','2019-03-30 05:56:21','D',NULL,NULL,'C'),('102019033047090001','8000418004','10029082726','-1','20181113225612','2019-03-30 08:04:36','D',NULL,NULL,'C'),('102019033059140004','8000418004','c1120d79-32ac-47d2-ad71-12fca4ad5930','-1','20190330043451','2019-03-30 08:34:54','D',NULL,NULL,'C'),('102019033061390001','8000418002','10029082726','','20181113225612','2019-03-30 03:57:06','D',NULL,NULL,'S'),('102019033074770001','8000418002','10029082726','','20181113225612','2019-03-30 03:05:28','D',NULL,NULL,'S'),('102019033089400001','8000418004','10029082726','-1','20181113225612','2019-03-30 07:47:21','D',NULL,NULL,'C'),('102019033090360001','8000418004','10029082726','-1','20181113225612','2019-03-30 05:53:33','D',NULL,NULL,'S'),('102019040205460004','8000418004','d7b2f34e-3262-459c-ac92-8e2da80da84a','-1','20190402010043','2019-04-01 17:00:43','D',NULL,NULL,'C'),('102019040219420003','8000418004','0ac198d5-307d-4249-a563-7edc7444e385','-1','20190402124300','2019-04-01 16:43:00','D',NULL,NULL,'C'),('102019040226760006','8000418004','1d8f4cba-f35c-4d31-a0d2-848c98ef01fd','-1','20190402124506','2019-04-01 16:45:06','D',NULL,NULL,'C'),('102019040234620009','8000418004','0abc6289-3a17-46c6-b35c-6fce48c86439','-1','20190402124829','2019-04-01 16:48:29','D',NULL,NULL,'C'),('102019040238780006','8000418004','a7e9c016-7fd8-442a-bea1-16bc67ff69ce','-1','20190402010238','2019-04-01 17:02:38','D',NULL,NULL,'C'),('102019040241380001','8000418004','585a518e-4ed8-4229-9051-e203841443b8','-1','20190402125720','2019-04-01 16:57:20','D',NULL,NULL,'C'),('102019040250230011','8000418002','10029082726','302019040253900001','20181113225612','2019-04-02 09:42:44','D',NULL,NULL,'S'),('102019040254500001','8000418004','172fa3a0-8bfa-4564-9176-5ce27630ebea','-1','20190402055639','2019-04-02 09:57:45','D',NULL,NULL,'C'),('102019040265670001','8000418004','2a63f222-71b8-43c3-83f4-66c60b317cad','302019040246120002','20190402073641','2019-04-02 11:36:43','D',NULL,NULL,'C'),('102019040282470009','8000418002','10029082726','','20181113225612','2019-04-02 09:25:16','D',NULL,NULL,'C'),('102019040283770001','8000418004','57c2b0c5-b5aa-478c-81c0-deeb19c4baad','-1','20190402124047','2019-04-01 16:40:48','D',NULL,NULL,'C'),('102019040335660018','8000418004','f2d0163f-2c88-4716-a033-e0306cb65804','302019040339750006','20190403105554','2019-04-03 14:55:54','D',NULL,NULL,'C'),('102019040335780006','8000418004','09561d70-5b63-4248-8c12-9f7e30557d92','302019040332500002','20190403105156','2019-04-03 14:51:56','D',NULL,NULL,'C'),('102019040340780001','8000418004','9bb3a3fa-5baa-4850-8abc-c9b5085d1892','302019040367880001','20190403040946','2019-04-03 08:09:48','D',NULL,NULL,'C'),('102019040347710015','8000418004','5c05509f-fb85-461f-9e74-bfb76b0494a8','302019040355650005','20190403105459','2019-04-03 14:54:59','D',NULL,NULL,'C'),('102019040354340009','8000418004','7cf3dc3d-3bd3-4229-b235-4cb48469250a','302019040343530003','20190403105253','2019-04-03 14:52:54','D',NULL,NULL,'C'),('102019040366270001','8000418004','b0cd5c0d-bea9-4683-aaab-01eb167121fb','302019040317140001','20190403102452','2019-04-03 02:24:53','D',NULL,NULL,'C'),('102019040380840001','8000418004','9ecbdbab-4071-4311-8f93-a89c90de0809','302019040367880001','20190403111202','2019-04-03 03:12:04','D',NULL,NULL,'C'),('102019040382720001','8000418004','41025159-ddce-420b-8dd3-87165e5a3a34','302019040317140001','20190403034532','2019-04-03 07:45:35','D',NULL,NULL,'C'),('102019040392690004','8000418004','6f0c71c1-c8dc-4e0c-8915-594bdc81b264','302019040366990002','20190403102722','2019-04-03 02:27:23','D',NULL,NULL,'C'),('102019040394340012','8000418004','ea0f5db4-a347-4867-9044-ff9ecaab7852','302019040356300004','20190403105347','2019-04-03 14:53:47','D',NULL,NULL,'C'),('102019040399640003','8000418004','e2914928-cc08-4dac-a068-01aedd215a64','302019040356630001','20190403105056','2019-04-03 14:50:56','D',NULL,NULL,'C'),('102019040413410004','8000418004','44c3e1b0-fb2d-4187-80dd-257c194875b9','302019040367880001','20190404041402','2019-04-04 08:14:03','D',NULL,NULL,'S'),('102019040415460001','8000418004','9037bdd6-73fe-4fe9-8b25-e78cc39eccab','302019040367880001','20190404032627','2019-04-04 07:26:27','D',NULL,NULL,'S'),('102019040428870001','8000418004','23031a12-93b1-4a87-8f1e-87786d68dad4','302019040367880001','20190404042236','2019-04-04 08:22:37','D',NULL,NULL,'C'),('102019040429190001','8000418004','69747720-c14d-4c61-9f0a-3f86fa186b0a','302019040481060001','20190404102456','2019-04-04 14:24:56','D',NULL,NULL,'C'),('102019040430010007','8000418004','e087797b-2ecc-40de-b409-d4b861cb3e76','302019040367880001','20190404043554','2019-04-04 08:35:54','D',NULL,NULL,'S'),('102019040430240023','8000418004','10e43195-e72b-4c55-8dae-8891ba7a5ff6','302019040366990002','20190404120430','2019-04-03 16:04:30','D',NULL,NULL,'C'),('102019040440350013','8000418004','3527e58c-05c4-4e36-b5a8-95e50171c450','302019040317140001','20190404043958','2019-04-04 08:39:58','D',NULL,NULL,'C'),('102019040452150004','8000418004','a8d6604b-fb43-4ce8-bf4b-873b7d58712b','302019040367880001','20190404042549','2019-04-04 08:25:49','D',NULL,NULL,'S'),('102019040457070016','8000418004','272d2bbd-e902-48d0-826c-7268d93555a8','302019040422250001','20190404044056','2019-04-04 08:40:57','D',NULL,NULL,'C'),('102019040466410010','8000418004','873062da-e7ed-4449-9002-fde67f3ebe05','302019040367880001','20190404043935','2019-04-04 08:39:35','D',NULL,NULL,'C'),('102019040467730001','8000418004','98233105-c204-4338-883b-015bc8bf31e4','302019040367880001','20190404040931','2019-04-04 08:09:31','D',NULL,NULL,'S'),('102019040472570021','8000418004','0638e532-a0d3-4d04-8457-fa18a25f9480','302019040367880001','20190404120216','2019-04-03 16:02:16','D',NULL,NULL,'C'),('102019040538650004','8000418004','84f1bb6a-50f1-42e0-999e-cb1ccf8b6c5e','302019040481060001','20190405091710','2019-04-05 13:17:10','D',NULL,NULL,'C'),('102019041104470001','8000418004','6b15695d-ca20-4501-ac6d-2d4c3d6073d9','-1','20190411040753','2019-04-11 08:07:53','D',NULL,NULL,'C'),('102019041311380003','8000418004','73fb1daa-0ce2-4b19-8c23-2dc501b51ec7','30518940136629616640','20190413125023','2019-04-13 04:50:24','D',NULL,NULL,'C'),('102019041341210001','8000418004','10029082726','-1','20181113225612','2019-04-13 15:11:12','D',NULL,NULL,'C'),('102019041379170004','8000418004','10029082726','-1','20181113225612','2019-04-13 15:12:26','D',NULL,NULL,'C'),('102019041809820001','8000418004','40878a7e-96a4-4821-ba35-b87a1c21c40e','-1','20190418113058','2019-04-18 15:31:00','D',NULL,NULL,'C'),('102019041814960003','8000418004','f68ed2ed-33ce-4c56-a28d-020c767bb3cc','-1','20190418113926','2019-04-18 15:39:27','D',NULL,NULL,'C'),('102019041821040001','8000418004','e13b6d92-4b5b-41fc-9769-f8f09a254236','-1','20190418120305','2019-04-18 04:03:06','D',NULL,NULL,'C'),('102019041832560003','8000418004','a9cf1a15-2674-41d5-8bdd-eb1823554fae','-1','20190418030921','2019-04-18 07:09:21','D',NULL,NULL,'C'),('102019041833300005','8000418004','dbe5ed5c-ad7a-4e4f-8712-0bb79f01decd','-1','20190418114454','2019-04-18 15:44:55','D',NULL,NULL,'C'),('102019041860210001','8000418004','f685e35b-9211-49a9-b4e7-b06192a2a2f7','302019041824320001','20190418102002','2019-04-18 02:20:02','D',NULL,NULL,'C'),('102019042182010001','8000418004','06184c4d-70d9-4bab-a7d6-a9e6424a485c','302019042192720001','20190421060400','2019-04-21 10:04:01','D',NULL,NULL,'C'),('102019042620240007','8000418004','da88f082-5667-4b72-b658-07800317ec6f','-1','20190426044513','2019-04-26 08:45:15','D',NULL,NULL,'C'),('102019042634280001','8000418004','fbae0e26-2fb9-47c9-8737-443ccd4076c3','-1','20190426055338','2019-04-26 09:53:40','D',NULL,NULL,'C'),('102019042648040007','8000418004','8ac336fe-7eec-491f-bb7d-e519b377d069','-1','20190426052206','2019-04-26 09:22:08','D',NULL,NULL,'C'),('102019042659490004','8000418004','581a5b32-8fee-435b-952a-9c35c16d9b0f','-1','20190426043701','2019-04-26 08:37:11','D',NULL,NULL,'C'),('102019042665790013','8000418004','6ca03046-61e5-45ff-9f7f-66081e334d5f','-1','20190426045939','2019-04-26 08:59:41','D',NULL,NULL,'C'),('102019042667300001','8000418004','0cf1a67c-4a7b-40c5-bc56-2228e54cfee2','-1','20190426051407','2019-04-26 09:14:09','D',NULL,NULL,'C'),('102019042671900004','8000418004','a57d7d72-5f4d-4862-8487-109292115b52','-1','20190426051457','2019-04-26 09:14:59','D',NULL,NULL,'C'),('102019042677780010','8000418004','81755aa0-50d7-48e3-8417-5628c6e8ad5a','-1','20190426052453','2019-04-26 09:24:55','D',NULL,NULL,'C'),('102019042680900010','8000418004','8f77a3f8-ea41-4bb3-ac43-fa90b5d48d0a','-1','20190426045714','2019-04-26 08:57:16','D',NULL,NULL,'C'),('102019042691850001','8000418004','5fe40df7-a6c6-4e7a-a533-49f52aa67001','-1','20190426043426','2019-04-26 08:34:28','D',NULL,NULL,'C'),('102019042703920004','8000418004','9e37d5b7-4ada-46d1-8eca-734ec54f188a','-1','20190427013714','2019-04-26 17:37:14','D',NULL,NULL,'C'),('102019042703930007','8000418004','f696214a-2893-44bf-a22d-7de06988fff8','-1','20190427013856','2019-04-26 17:38:57','D',NULL,NULL,'C'),('102019042710640010','8000418004','620b131b-b8b0-46f6-8e00-af8cd3b9b51c','-1','20190427064308','2019-04-27 10:43:08','D',NULL,NULL,'C'),('102019042720500007','8000418004','cc7bf6f5-a901-4e3c-ad2f-b2c79f97eaae','-1','20190427011203','2019-04-26 17:12:03','D',NULL,NULL,'S'),('102019042721580005','8000418004','39a32c65-3ced-49f6-bb33-59b144fc097c','-1','20190427010500','2019-04-26 17:05:00','D',NULL,NULL,'C'),('102019042743480012','8000418004','0314ab1f-a973-41ea-a06c-79cf9154ad40','-1','20190427075655','2019-04-27 11:56:55','D',NULL,NULL,'C'),('102019042765340001','8000418004','f66f0f1b-2968-4e4a-9b8f-89b61e7e623b','-1','20190427013054','2019-04-26 17:30:54','D',NULL,NULL,'C'),('102019042769570001','8000418004','30a9368d-4378-487c-9289-679fd6622609','-1','20190427125858','2019-04-26 16:58:58','D',NULL,NULL,'C'),('102019042793180001','8000418004','ae498728-8037-45d8-800a-a00b0ba3974f','-1','20190427012048','2019-04-26 17:20:51','D',NULL,NULL,'S'),('102019042799060003','8000418004','146c95c3-4fe3-4a76-93ae-846d5efe8bd9','-1','20190427125932','2019-04-26 16:59:32','D',NULL,NULL,'C'),('102019042803000057','8000418004','735b6280-ccfa-45ae-800c-2aadf4b9416b','-1','20190428100911','2019-04-28 02:09:11','D',NULL,NULL,'C'),('102019042807240027','8000418004','0d07800c-bb40-4654-b233-c03214715732','-1','20190428100906','2019-04-28 02:09:07','D',NULL,NULL,'C'),('102019042810330005','8000418004','b675f06b-6d2c-4141-84aa-4143339899d8','-1','20190428055353','2019-04-28 09:53:53','D',NULL,NULL,'C'),('102019042811680066','8000418004','0ac878d5-7388-4581-a7d5-7caa056d4f9a','-1','20190428100911','2019-04-28 02:09:12','D',NULL,NULL,'C'),('102019042819450015','8000418004','237949be-7501-408f-baae-f1f790420fb9','-1','20190428100854','2019-04-28 02:08:54','D',NULL,NULL,'C'),('102019042822560001','8000418004','c077f483-cafa-4563-85db-5ca036ad94c7','30518940136629616640','20190428053251','2019-04-28 09:32:52','D',NULL,NULL,'S'),('102019042829590010','8000418004','cf35c321-5211-461e-9e27-36970201f637','30518940136629616640','20190428100321','2019-04-28 14:03:35','D',NULL,NULL,'C'),('102019042838480054','8000418004','94fa02b7-c3c0-410d-8ea5-e663ad11110c','-1','20190428100910','2019-04-28 02:09:11','D',NULL,NULL,'C'),('102019042842770021','8000418004','11f9d761-3cea-44d5-972f-af4c264a1352','-1','20190428100906','2019-04-28 02:09:06','D',NULL,NULL,'C'),('102019042843460069','8000418004','7fe8074d-05d1-4206-8ef6-bd7a4fadcb94','302019042846260005','20190428025956','2019-04-28 06:59:56','D',NULL,NULL,'C'),('102019042844980063','8000418004','7e2d0f33-738e-4a11-90c7-e85b29175b01','-1','20190428100911','2019-04-28 02:09:11','D',NULL,NULL,'C'),('102019042845220001','8000418004','7f7f2570-7e03-4c5c-b6d0-4508a34f6a24','30518940136629616640','20190428054659','2019-04-28 09:47:04','D',NULL,NULL,'C'),('102019042846870018','8000418004','32cd31ce-13be-4df1-a201-85a819e23dc5','-1','20190428100906','2019-04-28 02:09:06','D',NULL,NULL,'C'),('102019042849530003','8000418004','1ae7e167-66a5-48d7-bd83-8424668347b0','30518940136629616640','20190428054752','2019-04-28 09:47:53','D',NULL,NULL,'C'),('102019042850720036','8000418004','6d6cc874-ae4e-4cf7-b6b2-2bbae70d3646','-1','20190428100907','2019-04-28 02:09:07','D',NULL,NULL,'C'),('102019042859200030','8000418004','720de0a7-3af9-4285-810c-1b3c034428cc','-1','20190428100907','2019-04-28 02:09:07','D',NULL,NULL,'C'),('102019042866910008','8000418004','8d8d8a70-c678-48c6-b1fc-6bd96fba7d59','30518940136629616640','20190428055410','2019-04-28 09:54:10','D',NULL,NULL,'C'),('102019042869120060','8000418004','31483ab0-7945-4a0f-9a33-21c1788fe13a','-1','20190428100910','2019-04-28 02:09:11','D',NULL,NULL,'C'),('102019042869590042','8000418004','c62eebb2-c581-4057-a964-e16c14fd2502','-1','20190428100907','2019-04-28 02:09:08','D',NULL,NULL,'C'),('102019042888740037','8000418004','9bd0005e-b4d0-4892-9adb-f9013231b8fa','-1','20190428100907','2019-04-28 02:09:07','D',NULL,NULL,'C'),('102019042891080051','8000418004','191cc7d8-f333-4ec2-ab58-8e91f840beaf','-1','20190428100909','2019-04-28 02:09:10','D',NULL,NULL,'C'),('102019042892400033','8000418004','ffe5f961-3ea9-4dd1-aabf-137009dcbab7','-1','20190428100907','2019-04-28 02:09:07','D',NULL,NULL,'C'),('102019042892670045','8000418004','455ab975-c61f-4190-ba0d-39e6027c08f8','-1','20190428100908','2019-04-28 02:09:09','D',NULL,NULL,'C'),('102019042894500024','8000418004','e17efec5-4964-437f-a56c-a4c6e606c414','-1','20190428100906','2019-04-28 02:09:06','D',NULL,NULL,'C'),('102019042896400048','8000418004','f331fa45-69d8-4c0a-9d1a-6b08312e18e8','-1','20190428100908','2019-04-28 02:09:09','D',NULL,NULL,'C'),('102019042905230016','8000418004','5d71146c-8de8-4f92-9aba-1268927467fd','-1','20190429052252','2019-04-29 09:22:54','D',NULL,NULL,'C'),('102019042911210004','8000418004','39d9dc43-c085-43c2-8fe1-067cfe087a0b','-1','20190429074805','2019-04-29 11:48:06','D',NULL,NULL,'C'),('102019042911820013','8000418004','eb0c8aa8-c42e-465e-bdd5-7e6cfee7b2ed','-1','20190429052241','2019-04-29 09:22:43','D',NULL,NULL,'C'),('102019042930590001','8000418004','4b999af1-4b41-4560-9c6c-a4cf2fb17071','-1','20190429051449','2019-04-29 09:14:51','D',NULL,NULL,'C'),('102019042932620001','8000418004','01fb20de-dcf7-493f-ab6d-a730e5b31e0d','30518940136629616640','20190429120912','2019-04-28 16:09:13','D',NULL,NULL,'C'),('102019042933020022','8000418004','239f9a2c-becf-4637-8b48-bcbcc3f26e66','-1','20190429052338','2019-04-29 09:23:40','D',NULL,NULL,'C'),('102019042940790019','8000418004','46cefbbc-0c9c-4b9a-a693-bfe0181ad468','-1','20190429052306','2019-04-29 09:23:08','D',NULL,NULL,'C'),('102019042947230009','8000418004','3aec2ae2-b12c-4e45-aa36-54e362f30580','30518940136629616640','20190429011944','2019-04-28 17:19:44','D',NULL,NULL,'C'),('102019042948840004','8000418004','e8940330-e1d0-422f-abf4-ac272c86a1b7','30518940136629616640','20190429010620','2019-04-28 17:06:21','D',NULL,NULL,'S'),('102019042949200004','8000418004','eac5bde6-5a20-45f1-b4f2-bfb3b5afef04','-1','20190429052143','2019-04-29 09:21:46','D',NULL,NULL,'C'),('102019042961210007','8000418004','f1e1543b-de4e-43b2-b2fe-2646fc23171d','-1','20190429052156','2019-04-29 09:21:59','D',NULL,NULL,'C'),('102019042965400010','8000418004','55108c84-8432-4e7f-be2f-62f366c63be7','-1','20190429052226','2019-04-29 09:22:28','D',NULL,NULL,'C'),('102019042968020001','8000418004','d2bfaf2d-3ff7-48bf-8bc5-df21954adb56','-1','20190429074733','2019-04-29 11:47:34','D',NULL,NULL,'C'),('102019042974910001','8000418004','84ea6583-b0a0-4373-a69e-4980b8573d2b','30518940136629616640','20190429011708','2019-04-28 17:17:09','D',NULL,NULL,'C'),('102019042983310004','8000418004','497f8e5c-fa21-4742-b79f-12de62dac039','-1','20190429011923','2019-04-28 17:19:24','D',NULL,NULL,'C'),('102019042986530001','8000418004','feb335b0-0e47-4653-b473-6aa8a3735a39','30518940136629616640','20190429125734','2019-04-28 16:57:34','D',NULL,NULL,'S'),('102019042999050007','8000418004','8434da78-d047-4ae4-8f8d-1998576eee44','30518940136629616640','20190429011937','2019-04-28 17:19:37','D',NULL,NULL,'C'),('102019050315000013','8000418004','2b73540b-036a-4fa2-bcb2-94f8c2f3aeeb','30518940136629616640','20190503060130','2019-05-03 10:01:33','D',NULL,NULL,'C'),('102019050317100001','8000418004','5896de22-b5ca-4086-b3ff-33f609812c4a','30518940136629616640','20190503055208','2019-05-03 09:52:11','D',NULL,NULL,'C'),('102019050326060001','8000418004','bb2c8b4b-2d65-4a83-9f8e-df9c3822d5af','30518940136629616640','20190503065226','2019-05-03 10:52:30','D',NULL,NULL,'S'),('102019050329540005','8000418004','d5305a68-c3d9-4f70-8005-c1744067d5b7','30518940136629616640','20190503065606','2019-05-03 10:56:10','D',NULL,NULL,'S'),('102019050333110003','8000418004','d06fbb52-3960-4eeb-ae29-7a0549ed4ed2','30518940136629616640','20190503065543','2019-05-03 10:55:47','D',NULL,NULL,'S'),('102019050347360007','8000418004','d517133b-7d0a-4963-95b3-e49f64175863','30518940136629616640','20190503055315','2019-05-03 09:53:18','D',NULL,NULL,'C'),('102019050354750001','8000418004','4a4da567-3758-4a30-a1ea-174da5536b4f','30518940136629616640','20190503053441','2019-05-03 09:34:44','D',NULL,NULL,'S'),('102019050357590003','8000418004','3d338c05-5e95-4859-80e2-1a3bbf9c8007','30518940136629616640','20190503055238','2019-05-03 09:52:42','D',NULL,NULL,'C'),('102019050358980001','8000418004','61fc89bd-ebb7-45e6-8266-ec805b5626b2','30518940136629616640','20190503073637','2019-05-03 11:36:41','D',NULL,NULL,'C'),('102019050361830003','8000418004','992e705e-2031-4f24-9e4c-f1f669b0ac90','30518940136629616640','20190503073656','2019-05-03 11:37:00','D',NULL,NULL,'C'),('102019050366970015','8000418004','e4824c97-87fa-4537-a5a2-baa326c3b711','30518940136629616640','20190503060145','2019-05-03 10:01:49','D',NULL,NULL,'C'),('102019050370720009','8000418004','34586bee-e5e6-4e96-8c38-9a4f80e6ea38','30518940136629616640','20190503055327','2019-05-03 09:53:30','D',NULL,NULL,'C'),('102019050370860001','8000418004','73cc9853-a217-4bad-a048-1419f8dac44c','30518940136629616640','20190503070653','2019-05-03 11:06:57','D',NULL,NULL,'D'),('102019050382250003','8000418004','d83a2efd-7493-4661-bbe2-526d4d4af062','30518940136629616640','20190503054125','2019-05-03 09:41:29','D',NULL,NULL,'S'),('102019050394550005','8000418004','d08a5563-c0d2-4319-a6fd-43f42394ee32','30518940136629616640','20190503055306','2019-05-03 09:53:09','D',NULL,NULL,'C'),('102019050398430011','8000418004','348b663e-0b2b-4401-816e-c26c87cbe94f','30518940136629616640','20190503055333','2019-05-03 09:53:37','D',NULL,NULL,'C'),('102019050403630005','8000418004','cc58a5c1-047d-4373-9729-efc38cf648e4','30518940136629616640','20190504064836','2019-05-04 10:48:39','D',NULL,NULL,'C'),('102019050417410001','8000418004','4e434c2f-d60d-48b8-bb43-69d2dbfb3f38','30518940136629616640','20190504094545','2019-05-04 13:45:45','D',NULL,NULL,'C'),('102019050418260007','8000418004','73778bb6-42ff-4d87-bf72-6f7f339b685e','30518940136629616640','20190504064857','2019-05-04 10:48:57','D',NULL,NULL,'C'),('102019050425190009','8000418004','89ae29f4-6c04-47f6-8a85-b7308ea20432','30518940136629616640','20190504064905','2019-05-04 10:49:05','D',NULL,NULL,'C'),('102019050462530003','8000418004','702b33a0-16a8-4c6d-a094-9fd8ac77454e','30518940136629616640','20190504092814','2019-05-04 13:28:15','D',NULL,NULL,'S'),('102019050488530003','8000418004','4b0d52ae-34a1-4c7f-95c8-a1b100923294','30518940136629616640','20190504094556','2019-05-04 13:45:57','D',NULL,NULL,'C'),('102019050490890007','8000418004','578d4c98-b97c-40ce-9378-26da185c9b5a','30518940136629616640','20190504094700','2019-05-04 13:47:01','D',NULL,NULL,'C'),('102019050495470005','8000418004','97e3424c-e72c-4ac6-b29d-7441978329ee','30518940136629616640','20190504094607','2019-05-04 13:46:08','D',NULL,NULL,'C'),('102019050496210001','8000418004','afc1f73a-11a9-4646-a66b-407ac5e7d24f','30518940136629616640','20190504092455','2019-05-04 13:24:56','D',NULL,NULL,'C'),('102019050607830004','8000418004','5252c024-b8ec-4357-83dd-97a5fcb0b144','30518940136629616640','20190506101700','2019-05-06 02:17:02','D',NULL,NULL,'D'),('102019050612160001','8000418004','ec64580f-3e16-431a-9952-f4cd3c31ac49','30518940136629616640','20190506100641','2019-05-06 14:06:43','D',NULL,NULL,'S'),('102019050625890001','8000418004','ad85a1eb-5f96-4e18-86bf-95e06082611b','30518940136629616640','20190506101645','2019-05-06 02:16:47','D',NULL,NULL,'D'),('102019050702040001','8000418004','1117b11d-830b-4619-91ca-4367e1065bed','30518940136629616640','20190507120844','2019-05-06 16:08:46','D',NULL,NULL,'C'),('102019050709110001','8000418004','2b966c57-83f9-4c44-b5ff-47611b7b7f92','30518940136629616640','20190507111833','2019-05-07 15:18:37','D',NULL,NULL,'C'),('102019050737140001','8000418004','bb4329b6-159b-4ce1-8594-03a55813fc38','30518940136629616640','20190507105929','2019-05-07 14:59:33','D',NULL,NULL,'S'),('102019050740250003','8000418004','1a63d09e-ecbf-4029-878a-2b019a1063a8','30518940136629616640','20190507112001','2019-05-07 15:20:05','D',NULL,NULL,'S'),('102019050756560003','8000418004','a3705b8c-d6ae-4129-94bc-2b835b3f44f3','30518940136629616640','20190507034141','2019-05-06 19:41:41','D',NULL,NULL,'C'),('102019050758150005','8000418004','d5fc3541-6bd8-45ba-aa4c-71e5f0e1e98c','30518940136629616640','20190507113551','2019-05-07 15:35:55','D',NULL,NULL,'C'),('102019050766510001','8000418004','986295c8-e4b5-424c-8d3c-8e619268084d','30518940136629616640','20190507104044','2019-05-07 14:40:48','D',NULL,NULL,'C'),('102019050812360003','8000418004','c6fea1a3-2a7a-4458-9aea-6cadb9cd6751','30518940136629616640','20190508065725','2019-05-08 10:57:30','D',NULL,NULL,'C'),('102019050816190007','8000418004','61a38783-57ca-41ad-b506-6594d24263aa','30518940136629616640','20190508034116','2019-05-08 07:41:21','D',NULL,NULL,'C'),('102019050818440005','8000418004','305fa456-1171-4de5-bec8-929ff3acbe37','30518940136629616640','20190508034027','2019-05-08 07:40:32','D',NULL,NULL,'C'),('102019050828340003','8000418004','ed1ab0aa-b402-4b75-9742-d5a25f6a3035','30518940136629616640','20190508015742','2019-05-07 17:57:46','D',NULL,NULL,'C'),('102019050833420003','8000418004','17b48b40-4943-4308-b580-937d2555d0b3','30518940136629616640','20190508033934','2019-05-08 07:39:39','D',NULL,NULL,'C'),('102019050840970001','8000418004','28ee0cd5-7446-49a5-ae32-3d30a3759ef9','30518940136629616640','20190508020721','2019-05-08 06:07:26','D',NULL,NULL,'C'),('102019050842980001','8000418004','26874176-dfbb-49cf-b523-3f1dbb0f8231','30518940136629616640','20190508013613','2019-05-08 05:36:18','D',NULL,NULL,'C'),('102019050867530001','8000418004','c752b5df-629f-4d69-92c6-a2ca947bdd34','30518940136629616640','20190508060537','2019-05-08 10:05:43','D',NULL,NULL,'C'),('102019050869400005','8000418004','26e62d72-6446-4eb6-b62b-9e800d99afc0','30518940136629616640','20190508065746','2019-05-08 10:57:51','D',NULL,NULL,'C'),('102019050873290007','8000418004','2317d567-5ece-4860-ba64-8b94ade2a92a','30518940136629616640','20190508065752','2019-05-08 10:57:57','D',NULL,NULL,'C'),('102019050873670009','8000418004','c80324cd-5b37-4c39-9fbd-dc61e956ba86','30518940136629616640','20190508045718','2019-05-08 08:57:24','D',NULL,NULL,'C'),('102019050888880003','8000418004','d99b4720-0ff8-456a-8dae-d5327867f478','30518940136629616640','20190508061451','2019-05-08 10:14:57','D',NULL,NULL,'C'),('102019050896060001','8000418004','cf550761-a033-4f16-9794-2c4ac6cbaa65','30518940136629616640','20190508015535','2019-05-07 17:55:39','D',NULL,NULL,'C'),('102019050899270001','8000418004','a83dffe2-8da1-492a-94d7-ec98cf4d819f','30518940136629616640','20190508064027','2019-05-08 10:40:33','D',NULL,NULL,'C'),('102019050904150009','8000418004','df5cff73-1639-43a5-aa3b-8633913d16ce','30518940136629616640','20190509012947','2019-05-09 05:29:53','D',NULL,NULL,'C'),('102019050911760005','8000418004','eeab0ae6-0d08-48d0-9901-df33668832d3','30518940136629616640','20190509022319','2019-05-09 06:23:26','D',NULL,NULL,'C'),('102019050920550001','8000418004','35b68972-dd72-4218-b2b9-81953c984b04','30518940136629616640','20190509021045','2019-05-09 06:10:52','D',NULL,NULL,'C'),('102019050960380007','8000418004','1a6458f2-1934-4822-a01f-0949e8cffca8','30518940136629616640','20190509022359','2019-05-09 06:24:05','D',NULL,NULL,'C'),('102019050973890003','8000418004','4b0b889c-6b66-454f-978a-000ca0f786ea','30518940136629616640','20190509022305','2019-05-09 06:23:12','D',NULL,NULL,'C'),('102019051078100009','8000418004','6f8fcb20-41d5-45fa-a42e-e841cf74d2fa','30518940136629616640','20190510015535','2019-05-10 05:55:35','D',NULL,NULL,'C'),('102019051117980013','8000418004','f89f22bc-f863-48e5-87c2-948568f762bc','-1','20190511094819','2019-05-11 13:48:20','D',NULL,NULL,'C'),('102019051159070011','8000418004','84d0ff00-7518-4422-89c1-583d61288ff9','-1','20190511094120','2019-05-11 13:41:20','D',NULL,NULL,'C'),('102019051313310015','8000418004','02d391f4-187b-4af6-a6ed-a148702039a8','30518940136629616640','20190513055439','2019-05-13 09:54:39','D',NULL,NULL,'C'),('102019051731630001','8000418004','37361a89-fa32-4d97-815f-24dc842353c2','30518940136629616640','20190517101124','2019-05-17 14:11:29','D',NULL,NULL,'C'),('102019051734170006','8000418004','3b75b8f5-0b06-49e5-a842-b4add441a30d','30518940136629616640','20190517092427','2019-05-17 13:24:28','D',NULL,NULL,'S'),('102019051741560001','8000418004','aeb9db44-17e9-47b1-9e71-fc64c5c97d29','30518940136629616640','20190517100123','2019-05-17 14:01:29','D',NULL,NULL,'S'),('102019051768310003','8000418004','df303613-015b-4c2a-92f8-54e57ad5276a','30518940136629616640','20190517054708','2019-05-17 09:47:10','D',NULL,NULL,'S'),('102019051789100001','8000418004','ac6ec035-c424-4e36-88e5-813724560846','30518940136629616640','20190517102936','2019-05-17 14:29:37','D',NULL,NULL,'C'),('102019051790130009','8000418004','f7ba45b3-bdd6-4ebd-a698-7527f24ebcb9','30518940136629616640','20190517094116','2019-05-17 13:41:17','D',NULL,NULL,'S'),('102019051793850001','8000418004','6771eef0-ed6f-44eb-a9bd-065d7bfb5da4','-1','20190517112117','2019-05-17 03:21:18','D',NULL,NULL,'C'),('102019051801920001','8000418004','992480a2-e155-468f-ae14-1b2a83fcd9e9','30518940136629616640','20190518035925','2019-05-18 07:59:26','D',NULL,NULL,'D'),('102019051803730003','8000418004','7ef8b36d-5abe-458f-9aca-b3d06cfb4761','30518940136629616640','20190518063650','2019-05-18 10:36:51','D',NULL,NULL,'C'),('102019051805200005','8000418004','b194e307-28b5-4bc7-97a8-e16f32e5a213','30518940136629616640','20190518040116','2019-05-18 08:01:18','D',NULL,NULL,'D'),('102019051806160001','8000418004','2a00804e-6871-4d02-838e-7de863293e15','30518940136629616640','20190518124121','2019-05-17 16:41:22','D',NULL,NULL,'S'),('102019051810130014','8000418004','e105caec-08b9-4b51-8304-122ce63deb02','30518940136629616640','20190518064333','2019-05-18 10:43:33','D',NULL,NULL,'C'),('102019051812890001','8000418004','f72aee86-6afd-441f-9685-d61af0362c6e','30518940136629616640','20190518063622','2019-05-18 10:36:23','D',NULL,NULL,'C'),('102019051812900020','8000418004','31d263f6-7fa9-4b58-99b9-4d10d6c21ea1','30518940136629616640','20190518065131','2019-05-18 10:51:33','D',NULL,NULL,'C'),('102019051829070011','8000418004','bb0631e3-ef28-47a3-8792-79abd9b07a72','30518940136629616640','20190518064318','2019-05-18 10:43:19','D',NULL,NULL,'C'),('102019051833310007','8000418004','3dbcf7ce-a172-4519-a935-bcc383c73663','30518940136629616640','20190518041917','2019-05-18 08:19:18','D',NULL,NULL,'C'),('102019051835010001','8000418004','e7a6f0b2-a1b4-4861-9c24-ca99ad97c5b7','30518940136629616640','20190518123024','2019-05-17 16:30:24','D',NULL,NULL,'S'),('102019051837100016','8000418004','4cd035d4-2169-4065-8635-ed89b5c775cc','30518940136629616640','20190518064344','2019-05-18 10:43:45','D',NULL,NULL,'C'),('102019051843080001','8000418004','cb7639b9-dcb7-47d5-b0cb-ab48d3fe3c69','30518940136629616640','20190518041106','2019-05-18 08:11:12','D',NULL,NULL,'C'),('102019051851920023','8000418004','7111bcb7-9935-488f-a2b2-f4cdc16b313b','30518940136629616640','20190518080130','2019-05-18 12:01:30','D',NULL,NULL,'S'),('102019051853460005','8000418004','d83d2246-09a3-4ebe-b101-a8a1235eec28','30518940136629616640','20190518063833','2019-05-18 10:38:34','D',NULL,NULL,'C'),('102019051856160001','8000418004','9295c761-95ee-4684-87d1-3ec161652384','30518940136629616640','20190518125819','2019-05-17 16:58:20','D',NULL,NULL,'C'),('102019051857060001','8000418004','6c76ba65-54c3-4ec6-94ee-17d0facca679','30518940136629616640','20190518043214','2019-05-18 08:32:14','D',NULL,NULL,'C'),('102019051861290001','8000418004','f7fa0926-85b5-463b-a501-bf2f5cddd222','30518940136629616640','20190518124817','2019-05-17 16:48:18','D',NULL,NULL,'C'),('102019051861920018','8000418004','dd6d0701-51a3-474d-8066-34b09b12437b','30518940136629616640','20190518064456','2019-05-18 10:44:56','D',NULL,NULL,'C'),('102019051867700003','8000418004','1e398af9-704a-435d-985c-9910e368d57a','30518940136629616640','20190518124903','2019-05-17 16:49:04','D',NULL,NULL,'C'),('102019051877940009','8000418004','be54f419-6d55-4041-9560-80fb8a0b3bc3','30518940136629616640','20190518064204','2019-05-18 10:42:05','D',NULL,NULL,'C'),('102019051880110025','8000418004','b0b1f70e-f266-4374-a90a-a3daf2353be9','30518940136629616640','20190518080448','2019-05-18 12:04:49','D',NULL,NULL,'C'),('102019051891280004','8000418004','191fda8a-85a7-4ceb-98b8-59eee77dabea','30518940136629616640','20190518041209','2019-05-18 08:12:10','D',NULL,NULL,'C'),('102019051897310007','8000418004','319db002-9d40-44f5-bac8-1b8aa61b42cc','30518940136629616640','20190518064028','2019-05-18 10:40:29','D',NULL,NULL,'C'),('102019051906010003','8000418004','46ae48c7-e82d-402c-a527-33f65ef6743a','30518940136629616640','20190519092102','2019-05-19 01:21:03','D',NULL,NULL,'C'),('102019051906090012','8000418004','4564c2ec-d620-4715-aca3-e57ae6675fd6','30518940136629616640','20190519085536','2019-05-19 00:55:36','D',NULL,NULL,'C'),('102019051912820014','8000418004','ff09aaab-ddc3-4ed5-b2d0-1264ad443704','30518940136629616640','20190519090535','2019-05-19 01:05:35','D',NULL,NULL,'S'),('102019051918540001','8000418004','58275bf0-81c6-412f-98f0-8f5ac8b5fce8','30518940136629616640','20190519082654','2019-05-19 00:26:54','D',NULL,NULL,'C'),('102019051919600001','8000418004','1c44752b-deb0-4f72-85c0-9d4187e4facd','30518940136629616640','20190519092044','2019-05-19 01:20:45','D',NULL,NULL,'C'),('102019051947160003','8000418004','87f58f5c-e103-4d16-80ec-aba46e47007b','30518940136629616640','20190519082742','2019-05-19 00:27:43','D',NULL,NULL,'C'),('102019051949640010','8000418004','89c8c0d1-4411-4a47-a4f4-ec4f654b2f4e','30518940136629616640','20190519085120','2019-05-19 00:51:20','D',NULL,NULL,'C'),('102019051950840008','8000418004','926d671f-f62f-48ca-85b0-ccb208b4ca4f','30518940136629616640','20190519084457','2019-05-19 00:44:57','D',NULL,NULL,'C'),('102019051952980005','8000418004','de767434-6511-43e8-8b41-c8e95544aa2c','30518940136629616640','20190519084418','2019-05-19 00:44:18','D',NULL,NULL,'C'),('102019051956350005','8000418004','cea68be4-66fd-4ed3-b0ea-26e63390b563','30518940136629616640','20190519092348','2019-05-19 01:23:49','D',NULL,NULL,'C'),('102019051962500007','8000418004','10165e7f-0252-40d6-96f5-66fafe1dbfee','30518940136629616640','20190519092548','2019-05-19 01:25:48','D',NULL,NULL,'C'),('102019051982360001','8000418004','21b218e3-b3dd-46dd-a364-e30518a02295','30518940136629616640','20190519095025','2019-05-19 01:50:26','D',NULL,NULL,'C'),('102019051983860003','8000418004','676b177d-4fe7-4489-8591-17138611e0ee','30518940136629616640','20190519095054','2019-05-19 01:50:54','D',NULL,NULL,'C'),('102019052194570001','8000418004','371282da-edf3-4a66-9870-f2328d849520','302019052133990001','20190521034945','2019-05-21 07:49:46','D',NULL,NULL,'C'),('102019052194570004','8000418004','bd107a7c-dbc5-4e1b-86ef-13760c772a73','302019052133990001','20190521035803','2019-05-21 07:58:03','D',NULL,NULL,'C'),('102019052214810001','8000418004','48cdcc68-959d-4640-8038-84dc300fa53e','30518940136629616640','20190522111903','2019-05-22 15:19:04','D',NULL,NULL,'C'),('102019052218900003','8000418004','617d7135-bad8-4f82-898e-26f6db9f9b1a','30518940136629616640','20190522112146','2019-05-22 15:21:46','D',NULL,NULL,'C'),('102019052219490003','8000418004','d550d3f2-05ae-43f4-8c6d-485b5531facb','30518940136629616640','20190522093806','2019-05-22 13:38:07','D',NULL,NULL,'C'),('102019052231880001','8000418004','90a27d8a-80fb-49d6-b829-bd72266ea8b1','30518940136629616640','20190522093649','2019-05-22 13:36:50','D',NULL,NULL,'C'),('102019052269970001','8000418004','001b4da1-f8ed-45ea-9ace-9fd5317ac993','30518940136629616640','20190522115323','2019-05-22 03:53:31','D',NULL,NULL,'C'),('102019052298100001','8000418004','ac38833b-cf17-4aaf-986b-5e77db9ce34b','30518940136629616640','20190522123013','2019-05-21 16:30:14','D',NULL,NULL,'C'),('102019052331030005','8000418004','b6a944ae-7d44-4176-a830-4cee4472355d','30518940136629616640','20190523025602','2019-05-23 06:53:33','D',NULL,NULL,'C'),('102019052358400008','8000418004','02d8e9f9-5097-452b-9ae8-bc0c36b67849','30518940136629616640','20190523025626','2019-05-23 06:53:57','D',NULL,NULL,'C'),('102019052626930007','8000418004','a6073b01-4a7a-48f9-ad88-f652a02cdc5b','30518940136629616640','20190526084957','2019-05-26 12:49:58','D',NULL,NULL,'C'),('102019052640620005','8000418004','439e3124-91a9-490b-8e51-ba5fba0eabb3','-1','20190526084944','2019-05-26 12:49:44','D',NULL,NULL,'S'),('102019052652750001','8000418004','32ccde9d-ef61-4cc7-a587-d67cdb6d6377','-1','20190526083258','2019-05-26 00:32:58','D',NULL,NULL,'C'),('102019052665950012','8000418004','0985090d-e6fa-42a5-8967-9d4a393ddefb','30518940136629616640','20190526085033','2019-05-26 12:50:33','D',NULL,NULL,'C'),('102019052670270003','8000418004','65fb835a-cb69-4106-b9e9-b4ef2fc042c6','-1','20190526084942','2019-05-26 12:49:42','D',NULL,NULL,'S'),('102019052675020015','8000418004','93c19e85-f892-47fe-8b9a-cc26f3c55bb0','-1','20190526085056','2019-05-26 12:50:57','D',NULL,NULL,'C'),('102019052695670010','8000418004','d4b611fc-f0e7-42ec-9cfc-5763968c1c0a','30518940136629616640','20190526085006','2019-05-26 12:50:06','D',NULL,NULL,'C'),('102019052714350017','8000418004','31da3f59-522a-4e7c-9e57-82a82287328e','30518940136629616640','20190527095238','2019-05-27 01:52:38','D',NULL,NULL,'S'),('102019052720840021','8000418004','c5206b39-7295-48b7-a783-e248c4cc2d8b','-1','20190527114901','2019-05-27 03:49:01','D',NULL,NULL,'C'),('102019052782980019','8000418004','1d6f8db6-5b25-442a-aa5b-841642b354de','30518940136629616640','20190527095257','2019-05-27 01:52:57','D',NULL,NULL,'S'),('102019052922300025','8000418004','eb59b92a-6700-4f28-8147-b940881a6728','-1','20190529051615','2019-05-29 09:16:15','D',NULL,NULL,'C'),('102019052930750027','8000418004','0ca69902-be39-4a7e-9aad-1e428de27d16','-1','20190529051636','2019-05-29 09:16:36','D',NULL,NULL,'C'),('102019052937640023','8000418004','c57ace8c-3e28-4d4a-b210-013d4ae63835','-1','20190529090630','2019-05-29 01:06:30','D',NULL,NULL,'C'),('102019053106230038','8000418004','2b79dbb9-2b70-4bbb-8b96-7b22d7bc611e','302019040343530003','20190531113148','2019-05-31 15:31:48','D',NULL,NULL,'S'),('102019053130210036','8000418004','84493758-44c4-41cd-8798-e7b757ab0366','302019040332500002','20190531113140','2019-05-31 15:31:41','D',NULL,NULL,'C'),('102019053162680029','8000418004','f771dad0-cd7c-43d6-bb13-cc3c1eaff5af','-1','20190531100157','2019-05-31 02:01:57','D',NULL,NULL,'C'),('102019053177220033','8000418004','e90e5e95-84db-4de7-bf6c-deae3e53b636','302019040343530003','20190531113136','2019-05-31 15:31:36','D',NULL,NULL,'C'),('102019053189380031','8000418004','cbf9ba86-26c9-42a3-a3d2-6b5ec9bd6545','302019040332500002','20190531113133','2019-05-31 15:31:33','D',NULL,NULL,'C'),('102019060201910001','8000418004','c71748b4-f47e-4ee0-a694-e79fd4c78631','30518940136629616640','20190602121418','2019-06-02 04:14:20','D',NULL,NULL,'C'),('102019060202660003','8000418004','f3c6fee2-cbd9-4442-a5bd-f4d74553710a','30518940136629616640','20190602121157','2019-06-01 16:11:57','D',NULL,NULL,'S'),('102019060203170004','8000418004','955f31bf-38ba-4f33-8dc0-5c2498382260','30518940136629616640','20190602012241','2019-06-02 05:22:43','D',NULL,NULL,'C'),('102019060204980001','8000418004','f452dfdd-20e5-4903-acfc-d62722dab556','30518940136629616640','20190602123332','2019-06-01 16:33:32','D',NULL,NULL,'C'),('102019060212210001','8000418004','573aad98-fbb3-424d-8f08-15db806a9429','30518940136629616640','20190602121143','2019-06-01 16:11:44','D',NULL,NULL,'C'),('102019060222870003','8000418004','9bc9bf52-87e8-4b8e-9e03-5d2d430df0a3','30518940136629616640','20190602123343','2019-06-01 16:33:43','D',NULL,NULL,'C'),('102019060242100007','8000418004','69018058-ce1e-4410-8001-4c0bed7279e9','-1','20190602012702','2019-06-02 05:27:02','D',NULL,NULL,'C'),('102019060244610007','8000418004','83a5570b-7b58-4331-88f0-d6b101c6c903','30518940136629616640','20190602114001','2019-06-02 03:40:02','D',NULL,NULL,'C'),('102019060255430005','8000418004','ecb63e86-9bf6-4464-b656-c5a544b8d59a','30518940136629616640','20190602100717','2019-06-02 02:07:17','D',NULL,NULL,'C'),('102019060258440001','8000418004','cec8d5e5-0226-4139-875f-a5a4548613be','30518940136629616640','20190602012110','2019-06-02 05:21:15','D',NULL,NULL,'C'),('102019060263110003','8000418004','7f22d6bc-491d-4c14-bcb6-a4e4a80a4cd1','30518940136629616640','20190602121511','2019-06-02 04:15:12','D',NULL,NULL,'S'),('102019060275830001','8000418004','2db59cb4-0772-4a25-90d8-bde0989df9e7','30518940136629616640','20190602124803','2019-06-02 04:48:05','D',NULL,NULL,'C'),('102019060283010001','8000418004','770e6d86-0455-4525-9042-b937b9e0fb74','30518940136629616640','20190602010211','2019-06-02 05:02:14','D',NULL,NULL,'D'),('102019060348810001','8000418004','08031a2c-b75b-4421-90ba-1794708f6c16','30518940136629616640','20190603034900','2019-06-03 07:49:01','D',NULL,NULL,'S'),('102019060353620001','8000418004','03f97baa-86e3-47ee-a2df-2503809038c0','30518940136629616640','20190603092252','2019-06-03 13:22:53','D',NULL,NULL,'C'),('102019060370080001','8000418004','9848c065-695d-4bd0-9ee8-63a02baa5114','30518940136629616640','20190603040557','2019-06-03 08:05:58','D',NULL,NULL,'C'),('102019060411160013','8000418004','bba1e518-3492-4432-bc6b-3e25b4b12fcc','-1','20190604053657','2019-06-04 09:36:57','D',NULL,NULL,'C'),('102019060413270001','8000418004','1b9c9ba3-0c7c-4f4d-9e38-d632371f91c6','30518940136629616640','20190604014805','2019-06-03 17:48:06','D',NULL,NULL,'C'),('102019060423120007','8000418004','48249ba2-908c-42d4-ac9d-8c4156f472ab','30518940136629616640','20190604025758','2019-06-04 06:57:58','D',NULL,NULL,'C'),('102019060437820010','8000418004','924982c1-abed-40e6-a783-6316305c61f8','30518940136629616640','20190604030043','2019-06-04 07:00:44','D',NULL,NULL,'C'),('102019060482110004','8000418004','9cf14678-8595-4c69-b556-d5ffe4f3776e','302019040481060001','20190604012053','2019-06-04 05:20:53','D',NULL,NULL,'C'),('102019060500850001','8000418004','65e8185b-8bba-4abf-b335-0052b1699375','30518940136629616640','20190605122135','2019-06-04 16:21:35','D',NULL,NULL,'S'),('102019060519640008','8000418004','a979f56a-f01e-49c2-bc39-edea1d993e73','30518940136629616640','20190605094854','2019-06-05 13:48:55','D',NULL,NULL,'C'),('102019060528910001','8000418004','7071b073-1ec2-4e51-8553-ef47f5752ed0','-1','20190605112532','2019-06-05 03:25:33','D',NULL,NULL,'C'),('102019060534750003','8000418004','274fd2fb-fa51-45b8-9b8c-0fa5e2c5b70f','30518940136629616640','20190605124710','2019-06-04 16:47:11','D',NULL,NULL,'C'),('102019060536340003','8000418004','021285c7-91ed-4118-8d55-05c3abde4737','30518940136629616640','20190605122552','2019-06-04 16:25:52','D',NULL,NULL,'C'),('102019060551920003','8000418004','a679e687-8a79-49e9-9c55-a3ecc0e36554','30518940136629616640','20190605053554','2019-06-05 09:35:55','D',NULL,NULL,'S'),('102019060559430006','8000418004','d82221a5-b24a-4679-a98b-ccfd59320821','-1','20190605064147','2019-06-05 10:41:47','D',NULL,NULL,'C'),('102019060576320007','8000418004','9dfd546e-d902-4cce-836e-a729e3b14a53','30518940136629616640','20190605123732','2019-06-04 16:37:32','D',NULL,NULL,'C'),('102019060588900005','8000418004','5086ddac-0a2c-4295-9720-b8422bc18b6a','30518940136629616640','20190605122648','2019-06-04 16:26:48','D',NULL,NULL,'S'),('102019060591170001','8000418004','61047915-48ce-4518-882d-18cf9c4203bd','30518940136629616640','20190605124640','2019-06-04 16:46:41','D',NULL,NULL,'C'),('102019060609090009','8000418004','31244af1-be4c-4058-be8c-e98272eeb30f','302019060653180001','20190606010852','2019-06-06 05:08:53','D',NULL,NULL,'C'),('102019060612300001','8000418004','baefea0f-3c8a-46c9-a1a3-caa72f222949','-1','20190606123849','2019-06-05 16:38:50','D',NULL,NULL,'C'),('102019060621550005','8000418004','bee26ec4-3fa6-4e51-9993-b7f0d6698e95','-1','20190606123909','2019-06-05 16:39:09','D',NULL,NULL,'C'),('102019060652810012','8000418004','6af1330c-02ba-4505-830b-b646cfe232dd','302019040246120002','20190606010937','2019-06-06 05:09:37','D',NULL,NULL,'C'),('102019060659470014','8000418004','1a38fedd-bbb9-4e58-87e5-38d254467cd8','302019040246120002','20190606010943','2019-06-06 05:09:43','D',NULL,NULL,'C'),('102019060665630003','8000418004','2fed6545-cc97-4ff4-9f81-34f5d0759e15','-1','20190606123900','2019-06-05 16:39:00','D',NULL,NULL,'C'),('102019060670880007','8000418004','73fdc6fd-219b-48cc-80fc-d03ed318984c','-1','20190606123914','2019-06-05 16:39:14','D',NULL,NULL,'C'),('102019060684800017','8000418004','3dea608b-3c53-4624-9979-b246dd59a920','30518940136629616640','20190606061848','2019-06-06 10:18:49','D',NULL,NULL,'C'),('102019060863430001','8000418004','6b807cc3-d4d9-45a4-8022-b01c1af5de66','30518940136629616640','20190608122420','2019-06-08 04:24:21','D',NULL,NULL,'S'),('102019060873850001','8000418004','971d3d2a-a5f1-4517-a32b-ec3cac87269a','30518940136629616640','20190608015503','2019-06-08 05:55:04','D',NULL,NULL,'C'),('102019060878480016','8000418004','3c0f58b0-8c4c-421a-ae03-9a78f3e0a239','30518940136629616640','20190608122923','2019-06-08 04:29:25','D',NULL,NULL,'S'),('102019060880610001','8000418004','6eab3a6c-4c16-4cb7-af16-a4a02d9f2275','30518940136629616640','20190608120454','2019-06-08 04:04:54','D',NULL,NULL,'S'),('102019060881190006','8000418004','55c2dc26-3eda-4dcb-be27-55569c14ea08','30518940136629616640','20190608122750','2019-06-08 04:27:50','D',NULL,NULL,'S'),('102019060885810011','8000418004','21245a45-ab53-4656-9cfb-33dbc92ff30f','30518940136629616640','20190608122851','2019-06-08 04:28:52','D',NULL,NULL,'S'),('102019060899750006','8000418004','ad2fb0da-2e1c-4dec-8852-137c26ebfe77','30518940136629616640','20190608120923','2019-06-08 04:09:24','D',NULL,NULL,'S'),('102019060930270001','8000418004','8065f83b-3a74-425f-942d-da9f2da915da','30518940136629616640','20190609125045','2019-06-08 16:50:46','D',NULL,NULL,'C'),('102019060930740014','8000418004','ca2739cc-2afc-4a06-aa78-d90d10599155','30518940136629616640','20190609125729','2019-06-08 16:57:29','D',NULL,NULL,'C'),('102019060931930010','8000418004','53a63a09-5558-4650-b108-0715c3d27506','30518940136629616640','20190609125642','2019-06-08 16:56:43','D',NULL,NULL,'C'),('102019060990270005','8000418004','06d425ed-7122-4f21-b4cb-d1bbafd95157','30518940136629616640','20190609125402','2019-06-08 16:54:03','D',NULL,NULL,'C'),('102019061000810005','8000418004','02018cad-f703-4098-baa4-3c1a71afb5c8','30518940136629616640','20190610122843','2019-06-09 16:28:49','D',NULL,NULL,'C'),('102019061018570008','8000418004','3089bfb2-a88d-4ac8-9970-38a73b001f70','30518940136629616640','20190610120937','2019-06-09 16:09:43','D',NULL,NULL,'C'),('102019061025560001','8000418004','2b7352e9-6816-4e87-b462-4f615d83d1d8','30518940136629616640','20190610122756','2019-06-09 16:28:03','D',NULL,NULL,'C'),('102019061046360010','8000418004','625b679d-045d-4886-bd56-c9866d214939','30518940136629616640','20190610120954','2019-06-09 16:10:00','D',NULL,NULL,'C'),('102019061048060001','8000418004','71b84924-dcb9-4b8e-98aa-687bc04988ed','30518940136629616640','20190610120811','2019-06-09 16:08:17','D',NULL,NULL,'C'),('102019061050830006','8000418004','b1a72e96-f800-4b63-b606-ec1519ec0e8d','30518940136629616640','20190610120914','2019-06-09 16:09:19','D',NULL,NULL,'C'),('102019061095420012','8000418004','ff1cd5c4-aa74-4509-b5f9-a541b3aa9dc5','30518940136629616640','20190610121057','2019-06-09 16:11:03','D',NULL,NULL,'C'),('10512770937368608768','8000418001','100000000020180409224736000001','1101','20181115215136','2018-11-15 15:28:39','D',NULL,'保存用户录入费用信息','C'),('10512771452089401344','8000418001','100000000020180409224736000001','1101','20181115215136','2018-11-15 15:30:42','D',NULL,'保存用户录入费用信息','C'),('10512773560800903168','8000418001','100000000020180409224736000001','1101','20181115215136','2018-11-15 15:39:04','D',NULL,'保存用户录入费用信息','E'),('10512774197236203520','8000418001','100000000020180409224736000001','1101','20181115215136','2018-11-15 15:41:36','D',NULL,'保存用户录入费用信息','E'),('10512774702192656384','8000418001','100000000020180409224736000001','1101','20181115215136','2018-11-15 15:43:36','D',NULL,'保存用户录入费用信息','E'),('10512774782215782400','8000418001','100000000020180409224736000001','1101','20181115215136','2018-11-15 15:43:55','D',NULL,'保存用户录入费用信息','E'),('10512778366974164992','8000418001','100000000020180409224736000001','1101','20181115215136','2018-11-15 15:58:10','D',NULL,'保存用户录入费用信息','E'),('10512781061441191936','8000418001','100000000020180409224736000001','1101','20181115215136','2018-11-15 16:08:53','D',NULL,'保存用户录入费用信息','E'),('10512781921827160064','8000418001','100000000020180409224736000001','1101','20181115215136','2018-11-15 16:12:18','D',NULL,'保存用户录入费用信息','E'),('10512782241781252096','8000418001','100000000020180409224736000001','1101','20181115215136','2018-11-15 16:13:34','D',NULL,'保存用户录入费用信息','C'),('10512782357623734272','8000418001','100000000020180409224736000001','1101','20181115215136','2018-11-15 16:14:02','D',NULL,'保存用户录入费用信息','C'),('10513135737009340416','8000418001','100000000020180409224736000002','1101','20181115215136','2018-11-16 15:38:14','D',NULL,'保存用户录入费用信息','C'),('10513136391413039104','8000418001','100000000020180409224736000002','1101','20181115215136','2018-11-16 15:40:50','D',NULL,'保存用户录入费用信息','E'),('10513138492071477248','8000418001','100000000020180409224736000002','1101','20181115215136','2018-11-16 15:49:10','D',NULL,'保存用户录入费用信息','E'),('10513138958218035200','8000418001','100000000020180409224736000002','1101','20181115215136','2018-11-16 15:51:01','D',NULL,'保存用户录入费用信息','E'),('10513139665960697856','8000418001','100000000020180409224736000002','1101','20181115215136','2018-11-16 15:53:50','D',NULL,'保存用户录入费用信息','E'),('10513140466590416896','8000418001','100000000020180409224736000002','1101','20181115215136','2018-11-16 15:57:01','D',NULL,'保存用户录入费用信息','E'),('10513141044963966976','8000418001','100000000020180409224736000002','800625487','20181115215136','2018-11-16 15:59:19','D',NULL,'保存用户录入费用信息','E'),('10513143017092153344','8000418001','100000000020180409224736000002','1101','20181115215136','2018-11-16 16:07:09','D',NULL,'保存用户录入费用信息','C'),('10513145621712994304','8000418001','100000000020180409224736000002','800625487','20181115215136','2018-11-16 16:17:30','D',NULL,'保存用户录入费用信息','C'),('10513145827309387776','8000418001','100000000020180409224736000002','800625487','20181115215136','2018-11-16 16:18:19','D',NULL,'保存用户录入费用信息','C'),('10513146861406650368','8000418001','100000000020180409224736000002','800625487','20181115215136','2018-11-16 16:22:26','D',NULL,'保存用户录入费用信息','C'),('10516289245648797696','8000418002','10029082726','-1','20181113225612','2018-11-25 08:29:09','D',NULL,NULL,'C'),('10516292807556612096','8000418002','10029082726','-1','20181113225612','2018-11-25 08:43:18','D',NULL,NULL,'E'),('10516293454683193344','8000418002','10029082726','-1','20181113225612','2018-11-25 08:45:51','D',NULL,NULL,'E'),('10516297437925621760','8000418002','10029082726','-1','20181113225612','2018-11-25 09:01:42','D',NULL,NULL,'E'),('10516297910447521792','8000418002','10029082726','-1','20181113225612','2018-11-25 09:03:34','D',NULL,NULL,'E'),('10516329683684442112','8000418002','10029082726','-1','20181113225612','2018-11-25 11:09:49','D',NULL,NULL,'C'),('10516332294286360576','8000418002','10029082726','-1','20181113225612','2018-11-25 11:20:11','D',NULL,NULL,'C'),('10516364589760266240','8000418002','10029082726','-1','20181113225612','2018-11-25 13:28:31','D',NULL,NULL,'C'),('10516365201428201472','8000418002','10029082726','-1','20181113225612','2018-11-25 13:30:57','D',NULL,NULL,'C'),('10516366112816906240','8000418002','10029082726','-1','20181113225612','2018-11-25 13:34:34','D',NULL,NULL,'C'),('10516366708789755904','8000418002','10029082726','-1','20181113225612','2018-11-25 13:36:57','D',NULL,NULL,'C'),('10516368306135908352','8000418002','10029082726','-1','20181113225612','2018-11-25 13:43:17','D',NULL,NULL,'C'),('10516374464053657600','8000418002','10029082726','-1','20181113225612','2018-11-25 14:07:46','D',NULL,NULL,'C'),('10516374772444053504','8000418002','10029082726','-1','20181113225612','2018-11-25 14:08:59','D',NULL,NULL,'C'),('10516374801552523264','8000418002','10029082726','-1','20181113225612','2018-11-25 14:09:06','D',NULL,NULL,'C'),('10516377393070358528','8000418002','10029082726','-1','20181113225612','2018-11-25 14:19:24','D',NULL,NULL,'C'),('10516381939469402112','8000418002','10029082726','-1','20181113225612','2018-11-25 14:37:28','D',NULL,NULL,'C'),('10516387722441539584','8000418002','10029082726','-1','20181113225612','2018-11-25 15:00:27','D',NULL,NULL,'C'),('10516389224614739968','8000418002','10029082726','-1','20181113225612','2018-11-25 15:06:25','D',NULL,NULL,'C'),('10516389264993304576','8000418002','10029082726','-1','20181113225612','2018-11-25 15:06:34','D',NULL,NULL,'C'),('10516398820054024192','8000418002','00c93840-f0c9-11e8-96a4-6777a97d0cd9','-1','20181125234432','2018-11-25 15:44:32','D',NULL,NULL,'C'),('10516400356129783808','8000418002','db091b60f0c9-11e8-b38a-cb522b025e1e','-1','20181125235038','2018-11-25 15:50:39','D',NULL,NULL,'C'),('10516401273776390144','8000418002','5d6face0f0ca11e8b4db91aaf43b3c01','-1','20181125235417','2018-11-25 15:54:17','D',NULL,NULL,'C'),('10517072482688057344','8000418002','f3e315c0f23e11e8a91fdd40351585ea','-1','20181127202122','2018-11-27 12:21:26','D',NULL,NULL,'C'),('10517086035339919360','8000418002','7bfb0100f24611e8a91fdd40351585ea','-1','20181127211517','2018-11-27 13:15:17','D',NULL,NULL,'C'),('10517710156889341952','8000418002','10029082726','-1','20181113225612','2018-11-29 06:35:20','D',NULL,NULL,'C'),('10518939876595351552','8000418002','918b5d90f64b11e893c895bbc9fa8c03','-1','20181203000145','2018-12-02 16:01:48','D',NULL,NULL,'C'),('10518940136222769152','8000418002','b6db3700f64b11e893c895bbc9fa8c03','-1','20181203000248','2018-12-02 16:02:49','D',NULL,NULL,'C'),('10520751769869893632','8000418002','5fcde8d0fa3911e8ba4ecf9afe208990','-1','20181208000136','2018-12-07 16:01:37','D',NULL,NULL,'C'),('10520752094110564352','8000418002','8e424df0fa3911e8ba4ecf9afe208990','-1','20181208000254','2018-12-07 16:02:54','D',NULL,NULL,'C'),('10520752515780722688','8000418002','ca2dd000fa3911e8ba4ecf9afe208990','-1','20181208000434','2018-12-07 16:04:34','D',NULL,NULL,'C'),('10520752653978845184','8000418002','ddce3a00fa3911e8ba4ecf9afe208990','-1','20181208000507','2018-12-07 16:05:07','D',NULL,NULL,'C'),('10520753110897934336','8000418002','1eb4e960fa3a11e8ba4ecf9afe208990','-1','20181208000656','2018-12-07 16:06:56','D',NULL,NULL,'C'),('10521002813006823424','8000418002','b96295e0fac411e8a78813cab7e028a1','-1','20181208163906','2018-12-08 08:39:11','D',NULL,NULL,'C'),('10521124884286291968','8000418002','10029082726','-1','20181113225612','2018-12-08 16:44:17','D',NULL,NULL,'C'),('10521125063953498112','8000418002','10029082726','-1','20181113225612','2018-12-08 16:44:57','D',NULL,NULL,'C'),('10521126624020676608','8000418002','10029082726','1234','20181113225612','2018-12-08 16:51:09','D',NULL,NULL,'C'),('10521126943786024960','8000418002','10029082726','1234','20181113225612','2018-12-08 16:52:25','D',NULL,NULL,'C'),('10521280167822245888','8000418002','10029082726','1234','20181113225612','2018-12-09 03:01:17','D',NULL,NULL,'C'),('10521280438203858944','8000418002','10029082726','1234','20181113225612','2018-12-09 03:02:21','D',NULL,NULL,'C'),('10521281261306658816','8000418002','10029082726','1234','20181113225612','2018-12-09 03:05:37','D',NULL,NULL,'C'),('10521289747750993920','8000418002','10029082726','1234','20181113225612','2018-12-09 03:39:21','D',NULL,NULL,'E'),('10521292247023501312','8000418002','10029082726','1234','20181113225612','2018-12-09 03:49:17','D',NULL,NULL,'D'),('10521294331546451968','8000418002','10029082726','1234','20181113225612','2018-12-09 03:57:34','D',NULL,NULL,'C'),('10521294983534231552','8000418002','10029082726','1234','20181113225612','2018-12-09 04:00:09','D',NULL,NULL,'C'),('10521299093553692672','8000418002','10029082726','-1','20181113225612','2018-12-09 04:16:29','D',NULL,NULL,'C'),('10521384632348852224','8000418002','10029082726','-1','20181113225612','2018-12-09 09:56:23','D',NULL,NULL,'C'),('10521384655329443840','8000418002','10029082726','-1','20181113225612','2018-12-09 09:56:28','D',NULL,NULL,'C'),('10521384680075837440','8000418002','10029082726','-1','20181113225612','2018-12-09 09:56:34','D',NULL,NULL,'C'),('10521384772841259008','8000418002','10029082726','-1','20181113225612','2018-12-09 09:56:56','D',NULL,NULL,'C'),('10521384859650768896','8000418002','10029082726','-1','20181113225612','2018-12-09 09:57:17','D',NULL,NULL,'C'),('10521388278742532096','8000418002','10029082726','-1','20181113225612','2018-12-09 10:10:52','D',NULL,NULL,'C'),('10521388292340465664','8000418002','10029082726','-1','20181113225612','2018-12-09 10:10:55','D',NULL,NULL,'C'),('10521388308358512640','8000418002','10029082726','-1','20181113225612','2018-12-09 10:10:59','D',NULL,NULL,'C'),('10521388322531065856','8000418002','10029082726','-1','20181113225612','2018-12-09 10:11:03','D',NULL,NULL,'C'),('10521388335550185472','8000418002','10029082726','-1','20181113225612','2018-12-09 10:11:06','D',NULL,NULL,'C'),('10521470366736990208','8000418002','46f4a7f0fbc811e8908d35b3675f85f8','-1','20181209233703','2018-12-09 15:37:03','D',NULL,NULL,'C'),('10521470728638316544','8000418002','7a65e810fbc811e8908d35b3675f85f8','-1','20181209233830','2018-12-09 15:38:30','D',NULL,NULL,'C'),('10521470972654534656','8000418002','9d106b10fbc811e8908d35b3675f85f8','-1','20181209233928','2018-12-09 15:39:28','D',NULL,NULL,'C'),('10521471733287370752','8000418002','0925a360fbc911e8908d35b3675f85f8','-1','20181209234229','2018-12-09 15:42:29','D',NULL,NULL,'C'),('10521472810514661376','8000418002','a23b6580fbc911e8908d35b3675f85f8','-1','20181209234646','2018-12-09 15:46:46','D',NULL,NULL,'C'),('10521692191735693312','8000418002','6a148c20fc4311e8a88c7d9b2593ccaa','-1','20181210141830','2018-12-10 06:18:31','D',NULL,NULL,'C'),('10521692260782325760','8000418002','73e66ed0fc4311e8a88c7d9b2593ccaa','-1','20181210141847','2018-12-10 06:18:47','D',NULL,NULL,'C'),('10521692315148894208','8000418002','7ba06e00fc4311e8a88c7d9b2593ccaa','-1','20181210141900','2018-12-10 06:19:00','D',NULL,NULL,'C'),('10521692341610758144','8000418002','7f69d210fc4311e8a88c7d9b2593ccaa','-1','20181210141906','2018-12-10 06:19:06','D',NULL,NULL,'C'),('10521692506786643968','8000418002','96e6da00fc4311e8a88c7d9b2593ccaa','-1','20181210141945','2018-12-10 06:19:46','D',NULL,NULL,'C'),('10521692648902246400','8000418002','ab1af880fc4311e8a88c7d9b2593ccaa','-1','20181210142019','2018-12-10 06:20:20','D',NULL,NULL,'C'),('10522549146947698688','8000418002','1ed3abf0fe1f11e8bed093e05f6f4593','-1','20181212230344','2018-12-12 15:03:45','D',NULL,NULL,'C'),('10523565515541594112','8000418002','516ec410005311e9bd932f1415ca3f9c','-1','20181215182226','2018-12-15 10:22:26','D',NULL,NULL,'C');

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
) ENGINE=InnoDB AUTO_INCREMENT=121 DEFAULT CHARSET=utf8;

/*Data for the table `c_route` */

insert  into `c_route`(`id`,`app_id`,`service_id`,`order_type_cd`,`invoke_limit_times`,`invoke_model`,`create_time`,`status_cd`) values (1,'8000418001',1,'Q',NULL,'S','2018-11-14 13:28:44','0'),(2,'8000418001',3,'Q',NULL,'S','2018-11-14 13:28:44','0'),(3,'8000418002',3,'Q',NULL,'S','2018-11-14 13:28:44','0'),(4,'8000418002',4,'Q',NULL,'S','2018-11-14 13:28:44','0'),(5,'8000418002',5,'Q',NULL,'S','2018-11-14 13:28:44','0'),(6,'8000418002',6,'Q',NULL,'S','2018-11-14 13:28:44','0'),(7,'8000418002',7,'Q',NULL,'S','2018-11-14 13:28:44','0'),(8,'8000418002',8,'Q',NULL,'S','2018-11-14 13:28:44','0'),(9,'8000418002',9,'Q',NULL,'S','2018-11-14 13:28:44','0'),(10,'8000418002',10,'Q',NULL,'S','2018-11-14 13:28:44','0'),(11,'8000418002',11,'Q',NULL,'S','2018-11-14 13:28:44','0'),(12,'8000418002',12,'Q',NULL,'S','2018-11-14 13:28:44','0'),(13,'8000418002',13,'Q',NULL,'S','2018-11-14 13:28:45','0'),(14,'8000418002',14,'Q',NULL,'S','2018-11-14 13:28:45','0'),(15,'8000418002',15,'Q',NULL,'S','2018-11-14 13:28:45','0'),(16,'8000418002',16,'Q',NULL,'S','2018-11-14 13:28:45','0'),(17,'8000418002',17,'Q',NULL,'S','2018-11-14 13:28:45','0'),(18,'8000418001',21,'Q',NULL,'S','2018-11-14 13:28:45','0'),(19,'8000418001',22,'Q',NULL,'S','2018-11-14 13:28:45','0'),(20,'8000418001',23,'Q',NULL,'S','2018-11-14 13:28:45','0'),(21,'8000418001',24,'Q',NULL,'S','2018-11-14 13:28:45','0'),(22,'8000418001',25,'Q',NULL,'S','2018-11-14 13:28:45','0'),(23,'8000418001',26,'Q',NULL,'S','2018-11-14 13:28:45','0'),(24,'8000418001',27,'Q',NULL,'S','2018-11-14 13:28:45','0'),(25,'8000418001',33,'Q',NULL,'S','2018-11-15 13:46:45','0'),(26,'8000418001',34,'Q',NULL,'S','2018-11-16 15:31:35','0'),(27,'8000418002',36,'Q',NULL,'S','2018-11-24 17:01:47','0'),(28,'8000418002',37,'Q',NULL,'S','2018-11-24 17:23:47','0'),(29,'8000418002',38,'Q',NULL,'S','2018-11-27 16:22:58','0'),(30,'8000418002',39,'Q',NULL,'S','2018-11-27 16:22:58','0'),(31,'8000418002',40,'D',NULL,'S','2018-12-08 16:15:22','0'),(32,'8000418002',41,'D',NULL,'S','2018-12-08 16:15:42','0'),(33,'8000418002',42,'D',NULL,'S','2018-12-08 16:15:50','0'),(34,'8000418002',43,'D',NULL,'S','2018-12-08 16:15:57','0'),(35,'8000418002',44,'D',NULL,'S','2018-12-08 16:16:04','0'),(36,'8000418002',45,'D',NULL,'S','2018-12-08 16:16:09','0'),(37,'8000418002',46,'Q',NULL,'S','2018-12-09 07:36:00','0'),(38,'8000418002',47,'Q',NULL,'S','2018-12-12 10:23:04','0'),(39,'8000418002',48,'Q',NULL,'S','2018-12-12 14:24:12','0'),(40,'8000418002',49,'Q',NULL,'S','2018-12-12 14:56:54','0'),(41,'8000418002',50,'D',NULL,'S','2018-12-17 15:29:11','0'),(42,'8000418002',51,'D',NULL,'S','2018-12-17 15:29:11','0'),(43,'8000418002',52,'D',NULL,'S','2018-12-17 15:29:11','0'),(44,'8000418002',53,'D',NULL,'S','2018-12-17 15:29:11','0'),(45,'8000418002',54,'Q',NULL,'S','2018-12-17 15:29:11','0'),(46,'8000418002',55,'Q',NULL,'S','2019-02-17 14:24:15','0'),(47,'8000418004',38,'Q',NULL,'S','2019-03-20 06:24:10','0'),(48,'8000418004',37,'Q',NULL,'S','2019-03-20 09:00:29','0'),(49,'8000418004',4,'Q',NULL,'S','2019-03-20 09:02:19','0'),(50,'8000418004',36,'Q',NULL,'S','2019-03-25 08:21:53','0'),(51,'8000418004',56,'Q',NULL,'S','2019-02-17 14:24:15','0'),(52,'8000418004',57,'Q',NULL,'S','2019-03-27 13:28:00','0'),(53,'8000418004',58,'Q',NULL,'S','2019-03-28 03:24:20','0'),(54,'8000418004',18,'D',NULL,'S','2019-03-29 13:46:24','0'),(55,'8000418004',59,'Q',NULL,'S','2019-04-01 03:07:03','0'),(56,'8000418004',3,'Q',NULL,'S','2019-04-01 03:33:22','0'),(57,'8000418004',39,'Q',NULL,'S','2019-04-01 11:17:12','0'),(58,'8000418004',1,'Q',NULL,'S','2019-04-01 11:47:02','0'),(59,'8000418004',60,'D',NULL,'S','2019-04-01 16:19:18','0'),(60,'8000418004',40,'D',NULL,'S','2019-04-02 11:31:01','0'),(61,'8000418004',61,'Q',NULL,'S','2019-04-02 12:03:33','0'),(62,'8000418004',46,'Q',NULL,'S','2019-04-03 01:27:28','0'),(63,'8000418004',62,'D',NULL,'S','2019-04-03 07:42:46','0'),(64,'8000418004',63,'D',NULL,'S','2019-04-04 03:55:20','0'),(65,'8000418004',64,'D',NULL,'S','2019-04-04 08:24:57','0'),(66,'8000418004',65,'Q',NULL,'S','2019-04-05 15:55:43','0'),(67,'8000418004',66,'Q',NULL,'S','2019-04-07 10:33:37','0'),(68,'8000418004',67,'Q',NULL,'S','2019-04-08 07:54:56','0'),(69,'8000418004',68,'Q',NULL,'S','2019-04-08 10:01:12','0'),(70,'8000418004',69,'Q',NULL,'S','2019-04-08 14:26:08','0'),(71,'8000418004',70,'Q',NULL,'S','2019-04-08 16:00:27','0'),(72,'8000418004',71,'Q',NULL,'S','2019-04-09 06:44:17','0'),(73,'8000418004',72,'Q',NULL,'S','2019-04-10 06:07:01','0'),(74,'8000418004',73,'Q',NULL,'S','2019-04-11 01:15:53','0'),(75,'8000418004',74,'Q',NULL,'S','2019-04-11 02:09:23','0'),(76,'8000418004',75,'Q',NULL,'S','2019-04-13 03:43:22','0'),(77,'8000418004',76,'Q',NULL,'S','2019-04-13 05:25:17','0'),(78,'8000418004',77,'Q',NULL,'S','2019-04-13 06:41:18','0'),(79,'8000418004',78,'Q',NULL,'S','2019-04-14 16:37:35','0'),(80,'8000418004',79,'Q',NULL,'S','2019-04-15 03:29:51','0'),(81,'8000418004',80,'Q',NULL,'S','2019-04-15 14:40:47','0'),(82,'8000418004',81,'Q',NULL,'S','2019-04-16 01:04:53','0'),(83,'8000418004',82,'Q',NULL,'S','2019-04-18 01:25:37','0'),(84,'8000418004',52,'D',NULL,'S','2019-04-18 03:45:23','0'),(85,'8000418004',53,'D',NULL,'S','2019-04-18 12:49:14','0'),(86,'8000418004',83,'Q',NULL,'S','2019-04-24 09:07:10','0'),(87,'8000418004',84,'Q',NULL,'S','2019-04-26 07:41:08','0'),(88,'8000418004',85,'Q',NULL,'S','2019-04-28 09:30:19','0'),(89,'8000418004',86,'Q',NULL,'S','2019-04-28 15:41:11','0'),(90,'8000418004',87,'Q',NULL,'S','2019-05-02 13:23:26','0'),(91,'8000418004',88,'Q',NULL,'S','2019-05-03 04:08:13','0'),(92,'8000418004',89,'Q',NULL,'S','2019-05-03 10:35:00','0'),(93,'8000418004',90,'Q',NULL,'S','2019-05-04 13:24:13','0'),(94,'8000418004',91,'Q',NULL,'S','2019-05-07 09:08:57','0'),(95,'8000418004',92,'Q',NULL,'S','2019-05-07 17:09:34','0'),(96,'8000418004',93,'Q',NULL,'S','2019-05-08 09:53:30','0'),(97,'8000418004',94,'Q',NULL,'S','2019-05-08 10:33:40','0'),(98,'8000418004',95,'Q',NULL,'S','2019-05-16 08:21:58','0'),(99,'8000418004',96,'Q',NULL,'S','2019-05-16 15:17:44','0'),(100,'8000418004',97,'Q',NULL,'S','2019-05-17 15:09:39','0'),(101,'8000418004',98,'Q',NULL,'S','2019-05-17 15:10:11','0'),(102,'8000418004',99,'Q',NULL,'S','2019-05-18 07:04:51','0'),(103,'8000418004',100,'Q',NULL,'S','2019-05-21 06:47:25','0'),(104,'8000418004',101,'Q',NULL,'S','2019-05-21 13:44:57','0'),(105,'8000418004',102,'Q',NULL,'S','2019-05-22 01:57:32','0'),(106,'8000418004',103,'Q',NULL,'S','2019-05-22 14:31:20','0'),(107,'8000418004',104,'Q',NULL,'S','2019-06-01 12:12:02','0'),(108,'8000418004',105,'Q',NULL,'S','2019-06-01 13:13:44','0'),(109,'8000418004',106,'Q',NULL,'S','2019-06-01 13:13:49','0'),(110,'8000418004',107,'Q',NULL,'S','2019-06-02 12:13:26','0'),(111,'8000418004',108,'Q',NULL,'S','2019-06-02 12:26:22','0'),(112,'8000418004',109,'Q',NULL,'S','2019-06-02 14:32:57','0'),(113,'8000418004',110,'Q',NULL,'S','2019-06-03 07:30:01','0'),(114,'8000418004',111,'Q',NULL,'S','2019-06-04 15:41:42','0'),(115,'8000418004',112,'Q',NULL,'S','2019-06-04 15:41:48','0'),(116,'8000418004',113,'Q',NULL,'S','2019-06-04 15:41:52','0'),(117,'8000418004',114,'Q',NULL,'S','2019-06-04 15:41:56','0'),(118,'8000418004',115,'Q',NULL,'S','2019-06-04 15:41:56','0'),(119,'8000418004',116,'Q',NULL,'S','2019-06-04 15:41:56','0'),(120,'8000418004',117,'Q',NULL,'S','2019-06-04 15:41:56','0');

/*Table structure for table `c_service` */

DROP TABLE IF EXISTS `c_service`;

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
) ENGINE=InnoDB AUTO_INCREMENT=126 DEFAULT CHARSET=utf8;

/*Data for the table `c_service` */

insert  into `c_service`(`id`,`service_id`,`service_code`,`business_type_cd`,`name`,`seq`,`messageQueueName`,`is_instance`,`url`,`method`,`timeout`,`retry_count`,`provide_app_id`,`create_time`,`status_cd`) values (1,'1','query.user.userInfo','API','用户信息查询',1,NULL,'NT','http://user-service/businessApi/query','GET',60,3,'8000418001','2018-11-18 05:44:00','0'),(2,'2','query.order.orderInfo','API','订单信息',1,NULL,'NT','http://order-service/businessApi/query','GET',60,3,'8000418001','2018-11-18 05:44:00','0'),(3,'3','query.menu.info','API','查询菜单',1,NULL,'N','http://order-service/businessApi/query','GET',60,3,'8000418002','2018-11-18 05:44:00','0'),(4,'4','query.user.loginInfo','API','查询用户登录信息',1,NULL,'NT','http://user-service/businessApi/query','GET',60,3,'8000418002','2018-11-18 05:44:01','0'),(5,'5','query.console.template','API','查询模板信息',1,NULL,'NT','http://order-service/businessApi/query','GET',60,3,'8000418002','2018-11-18 05:44:01','0'),(6,'6','query.console.templateCol','API','查询列模板信息',1,NULL,'NT','http://order-service/businessApi/query','GET',60,3,'8000418002','2018-11-18 05:44:01','0'),(7,'7','query.center.mapping','API','查询映射数据',1,NULL,'NT','http://order-service/businessApi/query','GET',60,3,'8000418002','2018-11-18 05:44:01','0'),(8,'8','query.center.apps','API','查询外部应用',1,NULL,'NT','http://order-service/businessApi/query','GET',60,3,'8000418002','2018-11-18 05:44:01','0'),(9,'9','query.center.services','API','查询服务',1,NULL,'NT','http://order-service/businessApi/query','GET',60,3,'8000418002','2018-11-18 05:44:01','0'),(10,'10','query.center.routes','API','查询路由',1,NULL,'NT','http://order-service/businessApi/query','GET',60,3,'8000418002','2018-11-18 05:44:01','0'),(11,'11','flush.center.cache','API','CenterService缓存',1,NULL,'NT','http://order-service/cacheApi/flush','GET',60,3,'8000418002','2018-11-18 05:44:01','0'),(12,'12','query.console.caches','API','查询所有缓存',1,NULL,'NT','http://order-service/businessApi/query','GET',60,3,'8000418002','2018-11-18 05:44:01','0'),(13,'13','query.console.cache','API','查询所有缓存',1,NULL,'NT','http://order-service/businessApi/query','GET',60,3,'8000418002','2018-11-18 05:44:01','0'),(14,'14','save.center.mapping','Q','保存映射信息',1,NULL,'N','http://order-service/businessApi/do',NULL,60,3,'8000418002','2018-11-18 05:44:01','0'),(15,'15','delete.center.mapping','Q','删除映射信息',1,NULL,'N','http://order-service/businessApi/do',NULL,60,3,'8000418002','2018-11-18 05:44:01','0'),(16,'16','update.center.mapping','Q','修改映射信息',1,NULL,'N','http://order-service/businessApi/do',NULL,60,3,'8000418002','2018-11-18 05:44:01','0'),(17,'17','save.user.info','D','保存用户信息',1,NULL,'Y','http://user-service/userApi/service',NULL,60,3,'8000418003','2018-11-18 05:44:01','0'),(18,'18','save.store.info','API','保存商户信息',1,NULL,'N','http://order-service/orderApi/service','POST',60,3,'8000418003','2018-11-18 05:44:01','0'),(19,'19','update.store.info','D','修改商户信息',1,NULL,'N','http://store-service/storeApi/service',NULL,60,3,'8000418003','2018-11-18 05:44:01','0'),(20,'20','delete.store.info','D','删除商户信息',1,NULL,'N','http://store-service/storeApi/service',NULL,60,3,'8000418003','2018-11-18 05:44:01','0'),(21,'21','transfer.console.menu','T','透传菜单查询',1,NULL,'N','http://192.168.31.199:8001/userApi/service',NULL,60,3,'8000418001','2018-11-18 05:44:01','0'),(22,'22','save.shop.info','D','保存商品信息',1,NULL,'N','http://shop-service/shopApi/service',NULL,60,3,'8000418003','2018-11-18 05:44:01','0'),(23,'23','update.shop.info','D','修改商品信息',1,NULL,'N','http://shop-service/shopApi/service',NULL,60,3,'8000418003','2018-11-18 05:44:01','0'),(24,'24','delete.shop.info','D','删除商品信息',1,NULL,'N','http://shop-service/shopApi/service',NULL,60,3,'8000418003','2018-11-18 05:44:01','0'),(25,'25','buy.shop.info','D','购买商品信息',1,NULL,'N','http://shop-service/shopApi/service',NULL,60,3,'8000418003','2018-11-18 05:44:01','0'),(26,'26','save.shop.catalog','D','保存商品目录信息',1,NULL,'N','http://shop-service/shopApi/service',NULL,60,3,'8000418003','2018-11-18 05:44:01','0'),(27,'27','update.shop.catalog','D','修改商品目录信息',1,NULL,'N','http://shop-service/shopApi/service',NULL,60,3,'8000418003','2018-11-18 05:44:01','0'),(28,'28','delete.shop.catalog','D','删除商品目录信息',1,NULL,'N','http://shop-service/shopApi/service',NULL,60,3,'8000418003','2018-11-18 05:44:01','0'),(29,'29','save.comment.info','D','保存评论',1,NULL,'N','http://comment-service/commentApi/service',NULL,60,3,'8000418003','2018-11-18 05:44:01','0'),(30,'30','delete.comment.info','D','删除评论',1,NULL,'N','http://comment-service/commentApi/service',NULL,60,3,'8000418003','2018-11-18 05:44:01','0'),(31,'31','member.joined.store','D','商户成员加入',1,NULL,'N','http://store-service/storeApi/service',NULL,60,3,'8000418003','2018-11-18 05:44:01','0'),(32,'32','member.quit.store','D','商户成员退出',1,NULL,'N','http://store-service/storeApi/service',NULL,60,3,'8000418003','2018-11-18 05:44:02','0'),(33,'33','save.storefee.info','D','保存用户费用信息',1,NULL,'N','http://store-service/storeApi/service',NULL,60,3,'8000418001','2018-11-18 05:45:46','0'),(34,'34','save.storehouse.info','D','保存商户录入用户应缴费用信息',1,NULL,'N','http://store-service/storeApi/service',NULL,60,3,'8000418001','2018-11-18 05:45:49','0'),(36,'35','do.service.order','API','订单服务受理',1,NULL,'Y','http://order-service/orderApi/service',NULL,60,3,'8000418003','2018-11-18 05:47:11','0'),(37,'36','user.service.register','API','用户注册',1,NULL,'N','http://order-service/orderApi/service','POST',60,3,'8000418002','2018-11-23 12:59:53','0'),(38,'37','user.service.login','API','用户登录',1,NULL,'N',NULL,'',60,3,'8000418002','2018-11-27 16:20:40','0'),(39,'38','check.service.login','API','检查用户登录服务',1,NULL,'N',NULL,NULL,60,3,'8000418002','2018-11-27 16:21:33','0'),(40,'39','user.service.logout','API','用户退出登录',1,NULL,'N',NULL,NULL,60,3,'8000418002','2018-11-27 16:21:33','0'),(41,'40','user.staff.add','API','添加员工',1,NULL,'N','http://order-service/orderApi/service','POST',60,3,'8000418002','2018-12-08 15:46:04','0'),(42,'41','user.staff.disable','API','停用员工',1,NULL,'N','http://order-service/orderApi/service','POST',60,3,'8000418002','2018-12-08 15:47:20','0'),(43,'42','user.staff.enable','API','启用员工',1,NULL,'N','http://order-service/orderApi/service','POST',60,3,'8000418002','2018-12-08 15:48:27','0'),(44,'43','modify.user.info','D','修改用户信息',1,NULL,'Y','http://user-service/userApi/service','POST',60,3,'8000418002','2018-12-08 15:49:54','0'),(45,'44','remove.user.info','D','删除用户信息',1,NULL,'Y','http://user-service/userApi/service','POST',60,3,'8000418002','2018-12-08 15:50:51','0'),(46,'45','recover.user.info','D','恢复用户信息',1,NULL,'Y','http://user-service/userApi/service','POST',60,3,'8000418002','2018-12-08 15:51:31','0'),(47,'46','query.staff.infos','API','查询员工信息',1,NULL,'N','http://user-service/businessApi/query','GET',60,3,'8000418002','2018-12-09 07:34:24','0'),(48,'47','search.staff.infos','API','搜索员工信息',1,NULL,'NT','http://user-service/businessApi/query','GET',60,3,'8000418002','2018-12-12 10:18:20','0'),(49,'48','query.community.infos','API','查询小区信息',1,NULL,'NT','http://store-service/businessApi/query','GET',60,3,'8000418002','2018-12-12 14:09:43','0'),(50,'49','search.community.infos','API','搜索小区',1,NULL,'NT','http://store-service/businessApi/query','GET',60,3,'8000418002','2018-12-12 14:53:44','0'),(51,'50','save.community.info','D','保存小区信息',1,NULL,'Y','http://community-service/communityApi/service','POST',60,3,'8000418002','2018-12-17 15:25:13','0'),(52,'51','update.community.info','D','修改小区信息',1,NULL,'Y','http://community-service/communityApi/service','POST',60,3,'8000418002','2018-12-17 15:26:15','0'),(53,'52','member.join.community','API','小区成员加入',1,NULL,'Y','http://order-service/orderApi/service','POST',60,3,'8000418002','2018-12-17 15:27:01','0'),(54,'53','member.quit.community','API','小区成员退出',1,NULL,'Y','http://order-service/orderApi/service','POST',60,3,'8000418002','2018-12-17 15:27:46','0'),(56,'54','check.property.staffHasProperty','API','检查员工是否有物业信息',1,'','N','http://order-service/businessApi/query','GET',60,3,'8000418002','0000-00-00 00:00:00','0'),(57,'55','query.property.info','API','查询物业信息',1,NULL,'NT','http://order-service/businessApi/query','GET',60,3,'8000418002','2019-02-17 13:25:55','0'),(58,'56','query.store.byuser','API','通过用户ID查询参与组织信息',1,NULL,'NT','http://store-service/businessApi/query','GET',60,3,'8000418002','2019-03-26 15:06:07','0'),(59,'57','check.hasUser.byNameOrTel','API','判断是否存在用户名或手机号的用户',1,NULL,'NT','http://user-service/businessApi/query','GET',60,3,'8000418002','2019-03-27 13:26:10','0'),(60,'58','query.store.type','API','查询商户类型',1,NULL,'NT','http://store-service/businessApi/query','GET',60,3,'8000418002','2019-03-28 03:18:19','0'),(61,'59','query.user.privilege','API','查询用户权限',1,NULL,'NT','http://order-service/businessApi/query','GET',60,3,'8000418002','2019-04-01 03:05:25','0'),(62,'60','save.user.defaultPrivilege','API','保存用户默认权限',1,NULL,'N','http://order-service/privilegeApi/saveUserDefaultPrivilege','POST',60,3,'8000418002','2019-04-01 16:17:53','0'),(63,'61','query.store.users','API','查询商户员工',1,NULL,'NT','http://store-service/businessApi/query','GET',60,3,'8000418002','2019-04-02 12:02:19','0'),(64,'62','user.staff.modify','API','修改员工信息',1,NULL,'Y','http://order-service/orderApi/service','POST',60,3,'8000418002','2019-04-03 06:08:48','0'),(65,'63','user.staff.delete','API','删除员工信息',1,NULL,'Y','http://order-service/orderApi/service','POST',60,3,'8000418002','2019-04-04 03:54:40','0'),(66,'64','delete.user.allPrivilege','API','删除用户所有权限',1,NULL,'Y','http://order-service/privilegeApi/deleteUserAllPrivilege','POST',60,3,'','2019-04-04 03:59:14','0'),(67,'65','query.store.privilegeGroup','API','查询商户权限组',1,NULL,'NT','http://order-service/businessApi/query','GET',60,3,'8000418002','2019-04-05 15:27:09','0'),(68,'66','query.privilege.byPgId','API','根据权限组ID查询权限',1,NULL,'NT','http://order-service/businessApi/query','GET',60,3,'8000418002','2019-04-07 10:31:31','0'),(69,'67','save.privilegeGroup.info','API','保存权限组信息',1,NULL,'NT','http://order-service/privilegeApi/savePrivilegeGroup','POST',60,3,'8000418002','2019-04-08 07:52:27','0'),(70,'68','delete.privilegeGroup.info','API','删除权限组信息',1,NULL,'NT','http://order-service/privilegeApi/deletePrivilegeGroup','POST',60,3,'8000418002','2019-04-08 09:59:55','0'),(71,'69','query.privilegeGroup.noAddPrivilege','API','查询权限组中未添加的权限',1,NULL,'NT','http://order-service/businessApi/query','GET',60,3,'8000418002','2019-04-08 14:24:33','0'),(72,'70','add.privilege.PrivilegeGroup','API','权限组中添加权限',1,NULL,'NT','http://order-service/privilegeApi/addPrivilegeToPrivilegeGroup','POST',60,3,'8000418002','2019-04-08 15:55:38','0'),(73,'71','delete.privilege.PrivilegeGroup','API','从权限组中删除权限',1,NULL,'NT','http://order-service/privilegeApi/deletePrivilegeFromPrivilegeGroup','POST',60,3,'8000418002','2019-04-09 06:43:04','0'),(74,'72','query.user.byName','API','根据名称查询用户',1,NULL,'NT','http://user-service/businessApi/query','GET',60,3,'8000418002','2019-04-10 06:05:36','0'),(75,'74','query.staff.byName','API','根据商户名称查询商户员工',1,NULL,'N',NULL,'GET',60,3,'8000418002','2019-04-10 06:05:37','0'),(76,'73','query.storeUser.byUserIds','API','查询用户是否是商户员工',1,NULL,'NT','http://store-service/businessApi/query','GET',60,3,'8000418002','2019-04-11 01:14:50','0'),(81,'75','query.privilegeGroup.noAddPrivilegeGroup','API','员工未添加的权限组',1,NULL,'NT','http://order-service/businessApi/query','GET',60,3,'8000418002','2019-04-13 03:37:41','0'),(82,'76','query.privilege.noAddPrivilege','API','查询没有给员工赋权限',1,NULL,'NT','http://order-service/businessApi/query','GET',60,3,'8000418002','2019-04-13 05:24:28','0'),(83,'77','add.privilege.userPrivilege','API','添加用户权限',1,NULL,'NT','http://order-service/privilegeApi/addStaffPrivilegeOrPrivilegeGroup','POST',60,3,'8000418002','2019-04-13 06:38:56','0'),(84,'78','delete.privilege.userPrivilege','API','删除用户权限',1,NULL,'NT','http://order-service/privilegeApi/deleteStaffPrivilegeOrPrivilegeGroup','POST',60,3,'8000418002','2019-04-14 16:35:11','0'),(85,'79','query.myCommunity.byMember','API','查询商户入驻小区信息',1,NULL,'NT','http://order-service/businessApi/query','GET',60,3,'8000418002','2019-04-15 03:29:27','0'),(86,'80','query.noEnterCommunity.byMember','API','查询未入驻的小区',1,NULL,'NT','http://order-service/businessApi/query','GET',60,3,'8000418002','2019-04-15 14:40:05','0'),(88,'81','check.user.hasPrivilege','API','检查用户是否有权限',1,NULL,'N','http://order-service/businessApi/query','GET',60,3,'8000418002','2019-04-16 01:04:10','0'),(89,'82','query.noEnterCommunity.byMemberAndName','API','根据小区名称查询未入驻小区',1,NULL,'NT','http://order-service/businessApi/query','GET',60,3,'8000418002','2019-04-18 01:24:37','0'),(90,'83','floor.queryFloors','API','查询小区楼信息',1,NULL,'N',NULL,'GET',60,3,'8000418002','2019-04-24 09:03:22','0'),(91,'84','floor.saveFloor','API','添加小区楼信息',1,NULL,'Y','http://order-service/orderApi/service','POST',60,3,'8000418002','2019-04-26 07:38:28','0'),(92,'85','floor.editFloor','API','编辑小区楼信息',1,NULL,'Y','http://order-service/orderApi/service','POST',60,3,'8000418002','2019-04-28 09:29:23','0'),(93,'86','floor.deleteFloor','API','删除小区楼信息',1,NULL,'Y','http://order-service/orderApi/service','POST',60,3,'8000418002','2019-04-28 15:40:35','0'),(94,'87','unit.queryUnits','API','查询小区单元信息',1,NULL,'N',NULL,'GET',60,3,'8000418002','2019-05-02 13:23:12','0'),(95,'88','unit.saveUnit','API','添加小区楼单元',1,NULL,'Y','http://order-service/orderApi/service','POST',60,3,'8000418002','2019-05-03 04:07:53','0'),(96,'89','unit.updateUnit','API','修改小区单元信息',1,NULL,'Y','http://order-service/orderApi/service','POST',60,3,'8000418002','2019-05-03 10:34:36','0'),(97,'90','unit.deleteUnit','API','删除小区单元信息',1,NULL,'Y','http://order-service/orderApi/service','POST',60,3,'8000418002','2019-05-04 13:23:51','0'),(98,'91','room.saveRoom','API','删除用户权限',1,NULL,'Y','http://order-service/orderApi/service','POST',60,3,'8000418002','2019-05-07 09:08:37','0'),(99,'92','room.queryRooms','API','查询房屋信息',1,NULL,'N',NULL,'GET',60,3,'8000418002','2019-05-07 17:09:18','0'),(101,'93','room.updateRoom','API','修改房屋信息',1,NULL,'Y','http://order-service/orderApi/service','POST',60,3,'8000418002','2019-05-08 09:52:55','0'),(102,'94','room.deleteRoom','API','删除房屋信息',1,NULL,'Y','http://order-service/orderApi/service','POST',60,3,'8000418002','2019-05-08 10:33:33','0'),(103,'95','owner.queryOwners','API','查询业主',1,NULL,'N',NULL,'POST',60,3,'8000418002','2019-05-16 08:21:32','0'),(104,'96','owner.saveOwner','API','保存业主信息',1,NULL,'Y','http://order-service/orderApi/service','POST',60,3,'8000418002','2019-05-16 15:17:28','0'),(105,'97','owner.editOwner','API','编辑业主信息',1,NULL,'Y','http://order-service/orderApi/service','POST',60,3,'8000418002','2019-05-17 15:09:14','0'),(106,'98','owner.deleteOwner','API','删除业主信息',1,NULL,'Y','http://order-service/orderApi/service','POST',60,3,'8000418002','2019-05-17 15:10:11','0'),(107,'99','owner.queryOwnerMembers','API','编辑业主信息',1,NULL,'N',NULL,'GET',60,3,'8000418002','2019-05-18 07:04:36','0'),(108,'100','room.queryRoomsWithOutSell','API','查询未销售的房屋信息',1,NULL,'N',NULL,'POST',60,3,'8000418002','2019-05-21 06:47:04','0'),(109,'101','room.sellRoom','API','房屋售卖',1,NULL,'Y','http://order-service/orderApi/service','POST',60,3,'8000418002','2019-05-21 13:44:39','0'),(110,'102','room.queryRoomsByOwner','API','查询业主房屋',1,NULL,'Y',NULL,'POST',60,3,'8000418002','2019-05-22 01:57:11','0'),(111,'103','room.exitRoom','API','退房',1,NULL,'Y','http://order-service/orderApi/service','POST',60,3,'8000418002','2019-05-22 14:30:36','0'),(112,'104','fee.queryFeeConfig','API','查询费用配置',1,NULL,'N',NULL,'GET',60,3,'8000418002','2019-06-01 12:11:44','0'),(113,'105','fee.saveFeeConfig','API','保存费用配置',1,NULL,'Y','http://order-service/orderApi/service','POST',60,3,'8000418002','2019-06-01 13:12:56','0'),(114,'106','fee.updateFeeConfig','API','修改费用配置',1,NULL,'Y','http://order-service/orderApi/service','POST',60,3,'8000418002','2019-06-01 13:13:19','0'),(115,'107','room.queryRoomsWithSell','API','查询已销售的房屋信息',1,NULL,'N',NULL,'GET',60,3,'8000418002','2019-06-02 12:13:05','0'),(116,'108','fee.queryFee','API','查询费用',1,NULL,'N',NULL,'GET',60,3,'8000418002','2019-06-02 12:26:02','0'),(117,'109','fee.queryFeeDetail','API','查询费用明细',1,NULL,'N',NULL,'GET',60,3,'8000418002','2019-06-02 14:32:42','0'),(118,'110','fee.payFee','API','缴费',1,NULL,'Y','http://order-service/orderApi/service','POST',60,3,'8000418002','2019-06-03 07:29:19','0'),(119,'111','parkingSpace.queryParkingSpaces','API','查询车位',1,NULL,'N',NULL,'GET',60,3,'8000418002','2019-05-16 08:21:32','0'),(120,'112','parkingSpace.saveParkingSpace','API','保存车位信息',1,NULL,'Y','http://order-service/orderApi/service','POST',60,3,'8000418002','2019-05-16 15:17:28','0'),(121,'113','parkingSpace.editParkingSpace','API','编辑车位信息',1,NULL,'Y','http://order-service/orderApi/service','POST',60,3,'8000418002','2019-05-17 15:09:14','0'),(122,'114','parkingSpace.deleteParkingSpace','API','删除车位信息',1,NULL,'Y','http://order-service/orderApi/service','POST',60,3,'8000418002','2019-05-17 15:10:11','0'),(123,'115','parkingSpace.sellParkingSpace','API','车位出售',1,NULL,'Y','http://order-service/orderApi/service','POST',60,3,'8000418002','2019-05-16 15:17:28','0'),(124,'116','parkingSpace.queryParkingSpacesByOwner','API','查询业主车位',1,NULL,'N',NULL,'GET',60,3,'8000418002','2019-05-16 08:21:32','0'),(125,'117','parkingSpace.exitParkingSpace','API','停车位退款',1,NULL,'Y','http://order-service/orderApi/service','POST',60,3,'8000418002','2019-05-16 15:17:28','0');

/*Table structure for table `c_service_business` */

DROP TABLE IF EXISTS `c_service_business`;

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
) ENGINE=InnoDB AUTO_INCREMENT=76 DEFAULT CHARSET=utf8;

/*Data for the table `c_service_business` */

insert  into `c_service_business`(`service_business_id`,`business_type_cd`,`invoke_type`,`url`,`message_topic`,`timeout`,`retry_count`,`create_time`,`status_cd`) values (1,'100100030001','1','ORDER_USER_SERVICE_URL','userServiceTopic',60,3,'2019-02-07 17:29:21','0'),(2,'100100030002','1','ORDER_USER_SERVICE_URL','userServiceTopic',60,3,'2019-02-07 17:29:21','0'),(3,'100100030003','1','ORDER_USER_SERVICE_URL','userServiceTopic',60,3,'2019-02-07 17:29:21','0'),(4,'100100030004','1','ORDER_USER_SERVICE_URL','userServiceTopic',60,3,'2019-02-07 17:29:21','0'),(5,'100100040001','1','ORDER_USER_SERVICE_URL','userServiceTopic',60,3,'2019-02-07 17:29:21','0'),(6,'100100040002','1','ORDER_USER_SERVICE_URL','userServiceTopic',60,3,'2019-02-07 17:29:21','0'),(7,'100100040003','1','ORDER_USER_SERVICE_URL','userServiceTopic',60,3,'2019-02-07 17:29:21','0'),(8,'200100030001','1','ORDER_STORE_SERVICE_URL','storeServiceTopic',60,3,'2019-02-07 17:29:21','0'),(9,'200100030002','1','ORDER_STORE_SERVICE_URL','storeServiceTopic',60,3,'2019-02-07 17:29:22','0'),(10,'200100040001','1','ORDER_STORE_SERVICE_URL','storeServiceTopic',60,3,'2019-02-07 17:29:22','0'),(11,'200100040002','1','ORDER_STORE_SERVICE_URL','storeServiceTopic',60,3,'2019-02-07 17:29:22','0'),(12,'200100050001','1','ORDER_STORE_SERVICE_URL','storeServiceTopic',60,3,'2019-02-07 17:29:22','0'),(13,'300100030001','1','ORDER_SHOP_SERVICE_URL','shopServiceTopic',60,3,'2019-02-07 17:29:22','0'),(14,'300100030002','1','ORDER_SHOP_SERVICE_URL','shopServiceTopic',60,3,'2019-02-07 17:29:22','0'),(15,'300100030003','1','ORDER_SHOP_SERVICE_URL','shopServiceTopic',60,3,'2019-02-07 17:29:22','0'),(16,'300100040001','1','ORDER_SHOP_SERVICE_URL','shopServiceTopic',60,3,'2019-02-07 17:29:22','0'),(17,'300100040002','1','ORDER_SHOP_SERVICE_URL','shopServiceTopic',60,3,'2019-02-07 17:29:22','0'),(18,'300100050001','1','ORDER_SHOP_SERVICE_URL','shopServiceTopic',60,3,'2019-02-07 17:29:22','0'),(19,'300100050002','1','ORDER_SHOP_SERVICE_URL','shopServiceTopic',60,3,'2019-02-07 17:29:22','0'),(20,'400100030001','1','ORDER_COMMENT_SERVICE_URL','commentServiceTopic',60,3,'2019-02-07 17:29:22','0'),(21,'400100050001','1','ORDER_COMMENT_SERVICE_URL','commentServiceTopic',60,3,'2019-02-07 17:29:22','0'),(22,'500100030001','1','ORDER_COMMUNITY_SERVICE_URL','communityServiceTopic',60,3,'2019-02-07 17:29:22','0'),(23,'500100030002','1','ORDER_COMMUNITY_SERVICE_URL','communityServiceTopic',60,3,'2019-02-07 17:29:22','0'),(24,'500100040001','1','ORDER_COMMUNITY_SERVICE_URL','communityServiceTopic',60,3,'2019-02-07 17:29:22','0'),(25,'500100040002','1','ORDER_COMMUNITY_SERVICE_URL','communityServiceTopic',60,3,'2019-02-07 17:29:23','0'),(26,'500100050001','1','ORDER_COMMUNITY_SERVICE_URL','communityServiceTopic',60,3,'2019-02-07 17:29:23','0'),(27,'610100030001','1','ORDER_FEE_SERVICE_URL','feeServiceTopic',60,3,'2019-02-07 17:29:23','0'),(28,'610100040001','1','ORDER_FEE_SERVICE_URL','feeServiceTopic',60,3,'2019-02-07 17:29:23','0'),(29,'610100050001','1','ORDER_FEE_SERVICE_URL','feeServiceTopic',60,3,'2019-02-07 17:29:23','0'),(30,'620100030001','1','ORDER_FEE_SERVICE_URL','feeServiceTopic',60,3,'2019-02-07 17:29:23','0'),(31,'620100040001','1','ORDER_FEE_SERVICE_URL','feeServiceTopic',60,3,'2019-02-07 17:29:23','0'),(32,'620100050001','1','ORDER_FEE_SERVICE_URL','feeServiceTopic',60,3,'2019-02-07 17:29:23','0'),(33,'600100030001','1','ORDER_FEE_SERVICE_URL','feeServiceTopic',60,3,'2019-02-07 17:29:23','0'),(34,'600100040001','1','ORDER_FEE_SERVICE_URL','feeServiceTopic',60,3,'2019-02-07 17:29:23','0'),(35,'600100050001','1','ORDER_FEE_SERVICE_URL','feeServiceTopic',60,3,'2019-02-07 17:29:23','0'),(42,'700100030001','1','ORDER_AGENT_SERVICE_URL','agentServiceTopic',60,3,'2019-02-07 17:29:24','0'),(43,'700100030002','1','ORDER_AGENT_SERVICE_URL','agentServiceTopic',60,3,'2019-02-07 17:29:24','0'),(44,'700100030003','1','ORDER_AGENT_SERVICE_URL','agentServiceTopic',60,3,'2019-02-07 17:29:24','0'),(45,'700100030004','1','ORDER_AGENT_SERVICE_URL','agentServiceTopic',60,3,'2019-02-07 17:29:24','0'),(46,'700100040001','1','ORDER_AGENT_SERVICE_URL','agentServiceTopic',60,3,'2019-02-07 17:29:24','0'),(47,'700100040002','1','ORDER_AGENT_SERVICE_URL','agentServiceTopic',60,3,'2019-02-07 17:29:24','0'),(48,'700100040003','1','ORDER_AGENT_SERVICE_URL','agentServiceTopic',60,3,'2019-02-07 17:29:24','0'),(49,'700100050001','1','ORDER_AGENT_SERVICE_URL','agentServiceTopic',60,3,'2019-02-07 17:29:24','0'),(50,'700100050002','1','ORDER_AGENT_SERVICE_URL','agentServiceTopic',60,3,'2019-02-07 17:29:24','0'),(51,'700100050003','1','ORDER_AGENT_SERVICE_URL','agentServiceTopic',60,3,'2019-02-07 17:29:24','0'),(52,'700100050004','1','ORDER_AGENT_SERVICE_URL','agentServiceTopic',60,3,'2019-02-07 17:29:24','0'),(53,'200100060001','1','ORDER_STORE_SERVICE_URL','storeServiceTopic',60,3,'2019-03-30 03:18:21','0'),(54,'200100070001','1','ORDER_STORE_SERVICE_URL','storeServiceTopic',60,3,'2019-03-30 03:20:08','0'),(55,'510100030001','1','ORDER_COMMUNITY_SERVICE_URL','communityServiceTopic',60,3,'2019-04-21 09:08:35','0'),(56,'510100040001','1','ORDER_COMMUNITY_SERVICE_URL','communityServiceTopic',60,3,'2019-04-21 09:10:44','0'),(57,'510100050001','1','ORDER_COMMUNITY_SERVICE_URL','communityServiceTopic',60,3,'2019-04-21 09:10:52','0'),(58,'520100030001','1','ORDER_COMMUNITY_SERVICE_URL','communityServiceTopic',60,3,'2019-05-03 09:39:49','0'),(59,'520100040001','1','ORDER_COMMUNITY_SERVICE_URL','communityServiceTopic',60,3,'2019-05-03 09:40:10','0'),(60,'520100050001','1','ORDER_COMMUNITY_SERVICE_URL','communityServiceTopic',60,3,'2019-05-03 09:40:24','0'),(61,'530100030001','1','ORDER_COMMUNITY_SERVICE_URL','communityServiceTopic',60,3,'2019-05-05 01:34:14','0'),(62,'530100040001','1','ORDER_COMMUNITY_SERVICE_URL','communityServiceTopic',60,3,'2019-05-05 01:34:25','0'),(63,'530100050001','1','ORDER_COMMUNITY_SERVICE_URL','communityServiceTopic',60,3,'2019-05-05 01:34:35','0'),(64,'110100030001','1','ORDER_USER_SERVICE_URL','userServiceTopic',60,3,'2019-05-17 13:31:23','0'),(65,'110100040001','1','ORDER_USER_SERVICE_URL','userServiceTopic',60,3,'2019-05-17 13:31:35','0'),(66,'110100050001','1','ORDER_USER_SERVICE_URL','userServiceTopic',60,3,'2019-05-17 13:31:48','0'),(67,'111100030001','1','ORDER_USER_SERVICE_URL','userServiceTopic',60,3,'2019-05-17 13:31:23','0'),(68,'111100040001','1','ORDER_USER_SERVICE_URL','userServiceTopic',60,3,'2019-05-17 13:31:23','0'),(69,'111100050001','1','ORDER_USER_SERVICE_URL','userServiceTopic',60,3,'2019-05-17 13:31:23','0'),(70,'540100030001','1','ORDER_COMMUNITY_SERVICE_URL','communityServiceTopic',60,3,'2019-05-05 01:34:14','0'),(71,'540100040001','1','ORDER_COMMUNITY_SERVICE_URL','communityServiceTopic',60,3,'2019-05-05 01:34:25','0'),(72,'540100050001','1','ORDER_COMMUNITY_SERVICE_URL','communityServiceTopic',60,3,'2019-05-05 01:34:35','0'),(73,'111200030001','1','ORDER_USER_SERVICE_URL','userServiceTopic',60,3,'2019-05-17 13:31:23','0'),(74,'111200040001','1','ORDER_USER_SERVICE_URL','userServiceTopic',60,3,'2019-05-17 13:31:23','0'),(75,'111200050001','1','ORDER_USER_SERVICE_URL','userServiceTopic',60,3,'2019-05-17 13:31:23','0');

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
) ENGINE=InnoDB AUTO_INCREMENT=38 DEFAULT CHARSET=utf8;

/*Data for the table `c_service_sql` */

insert  into `c_service_sql`(`id`,`service_code`,`name`,`params`,`query_model`,`sql`,`proc`,`java_script`,`template`,`remark`,`create_time`,`status_cd`) values (1,'query.order.orderInfo','订单信息','oId','1','{\n                                                 	\"param1\":\"SELECT co.app_id appId,co.create_time createTime,co.ext_transaction_id extTransactionId,co.finish_time finishTime\n                                                 ,co.order_type_cd orderTypeCd,co.o_id oId ,co.remark remark ,co.request_time requestTime ,co.user_id userId,co.status_cd statusCd\n                                                  FROM c_orders co WHERE co.o_id = #oId#\",\n                                                  \"param2\":\"SELECT cb.b_id bId, cb.business_type_cd businessTypeCd,cb.create_time createTime,cb.finish_time finishTime ,cb.o_id oId,\n                                                 cb.remark remark,cb.status_cd statusCd FROM c_business cb WHERE cb.o_id = #oId#\"\n                                                 }','',NULL,'{\"PARAM:\"{\n                                                            \"param1\": \"$.#order#Object\",\n                                                            \"param2\": \"$.#business#Array\"\n                                                            },\"TEMPLATE\":\"{\n                                                         \"response\": {\n                                                           \"code\": \"0000\",\n                                                           \"message\": \"成功\"\n                                                         }\n                                                       }\"}','','2018-11-14 13:28:45','0'),(2,'query.menu.info','查询菜单1','userId,domain','1','{\"param1\":\"SELECT mm.`m_id` `mId`,mm.name menuName,mm.`g_id` gId,mm.`url`,mm.seq menuSeq,mm.`p_id` pId,\r\n	mm.`description` menuDescription, mmg.name menuGroupName,mmg.`icon`,mmg.`label`,mmg.`seq` menuGroupSeq,\r\n	mmg.`description` menuGroupDescription FROM m_menu mm,m_menu_group mmg\r\n	WHERE mm.`g_id` = mmg.`g_id`\r\n	AND mm.`p_id` IN (\r\n		SELECT pp.`p_id` FROM p_privilege_user ppu,p_privilege pp\r\n		WHERE ppu.`p_id` = pp.`p_id`\r\n		and pp.domain = #domain#\r\n		AND ppu.`privilege_flag` = \'0\'\r\n		AND ppu.`user_id` = #userId#\r\n		AND ppu.`status_cd` = \'0\'\r\n		AND pp.`status_cd` = \'0\'\r\n		UNION\r\n		SELECT pp.`p_id` FROM p_privilege_user ppu,p_privilege_group ppg,p_privilege pp,p_privilege_rel ppr\r\n		WHERE ppu.`p_id` = ppr.pg_id\r\n		AND ppr.pg_id = ppg.pg_id\r\n		AND ppr.p_id = pp.`p_id`\r\n		and pp.domain = #domain#\r\n		AND ppu.`privilege_flag` = \'1\'\r\n		AND ppu.`user_id` = #userId#\r\n		AND ppu.`status_cd` = \'0\'\r\n		AND pp.`status_cd` = \'0\'\r\n		AND ppg.status_cd = \'0\'\r\n		AND ppr.status_cd = \'0\'\r\n	)\r\n	AND mm.`status_cd` = \'0\'\r\n	AND mmg.`status_cd` = \'0\'\"}','',NULL,'{\"PARAM\":{\n                                                            \"param1\": \"$.#menus#Array\"\n                                                            },\"TEMPLATE\":{\n                                                        \n                                                       }}','','2018-11-14 13:28:45','0'),(3,'query.user.loginInfo','查询用户登录信息','userCode','1','{\"param1\":\"select a.user_id userId,a.name userName,a.password userPwd from u_user a where a.name = #userCode# and a.status_cd = \'0\'\"}','',NULL,'{\"PARAM\":{                                                            \"param1\": \"$.#user#Object\"                                                            },\"TEMPLATE\":{                                                         \"response\": {                                                           \"code\": \"0000\",                                                           \"message\": \"成功\"                                                         }                                                       }}','','2018-11-14 13:28:45','0'),(4,'query.console.template','查询模板信息','templateCode','1','{\n                                                 	\"param1\":\"SELECT t.template_code templateCode,t.name templateName,t.`html_name` htmlName,t.`url` templateUrl\n                                                              FROM c_template t WHERE t.status_cd = \'0\' AND t.template_code = #templateCode#\"\n                                                 }','',NULL,'{\"PARAM\":{\n                                                            \"param1\": \"$.#template#Object\"\n                                                            },\"TEMPLATE\":{\n                                                         \"response\": {\n                                                           \"code\": \"0000\",\n                                                           \"message\": \"成功\"\n                                                         }\n                                                       }}','','2018-11-14 13:28:45','0'),(5,'query.console.templateCol','查询模板列信息','templateCode','1','{\n                                                 	\"param1\":\"SELECT t.template_code templateCode,t.name templateName,tc.col_name colName,tc.col_model colModel FROM c_template t,c_template_col tc WHERE t.status_cd = \'0\' AND t.template_code = tc.template_code\n                                                              AND tc.status_cd = \'0\'\n                                                              AND t.template_code = #templateCode#\"\n                                                 }','',NULL,'{\"PARAM\":{\n                                                            \"param1\": \"$.#template#Array\"\n                                                            },\"TEMPLATE\":{\n                                                         \"response\": {\n                                                           \"code\": \"0000\",\n                                                           \"message\": \"成功\"\n                                                         }\n                                                       }}','','2018-11-14 13:28:45','0'),(6,'query.center.mapping','查询映射数据','page,rows,sord','1','{\"param1\":\"select count(1) records,ceil(#page#/#rows#)+1 page,ceil(count(1)/#rows#) total from c_mapping m where m.status_cd = \'0\'\",\n                                                             \"param2\":\"SELECT m.`id` id,m.`domain` domain,m.name name,m.`key`  `key` ,m.`value` `value`,m.`remark` remark FROM c_mapping m WHERE m.`status_cd` = \'0\' LIMIT #page#, #rows#\"\n                                                             }','',NULL,'{\n                                                        	\"PARAM\": {\n                                                        		\"param1\": \"$.##Object\",\n                                                        		\"param2\": \"$.#rows#Array\"\n                                                        	},\n                                                        	\"TEMPLATE\": {\n                                                        		\"response\": {\n                                                        			\"code\": \"0000\",\n                                                        			\"message\": \"成功\"\n                                                        		}\n                                                        	}\n                                                        }','','2018-11-14 13:28:45','0'),(7,'query.center.apps','查询外部应用','page,rows,sord','1','{\"param1\":\"select count(1) records,ceil(#page#/#rows#)+1 page,ceil(count(1)/#rows#) total from c_app a where a.status_cd = \'0\'\",\n                                                             \"param2\":\"SELECT m.`id` id,m.`app_id` appId,m.name `name`,m.`security_code`  securityCode ,m.`while_list_ip` whileListIp,m.`black_list_ip` blackListIp,m.`remark` remark FROM c_app m WHERE m.`status_cd` = \'0\' LIMIT #page#, #rows#\"\n                                                             }','',NULL,'{\n                                                        	\"PARAM\": {\n                                                        		\"param1\": \"$.##Object\",\n                                                        		\"param2\": \"$.#rows#Array\"\n                                                        	},\n                                                        	\"TEMPLATE\": {\n                                                        		\"response\": {\n                                                        			\"code\": \"0000\",\n                                                        			\"message\": \"成功\"\n                                                        		}\n                                                        	}\n                                                        }','','2018-11-14 13:28:45','0'),(8,'query.center.services','查询服务数据','page,rows,sord','1','{\"param1\":\"select count(1) records,ceil(#page#/#rows#)+1 page,ceil(count(1)/#rows#) total from c_service a where a.status_cd = \'0\'\",\n                                                             \"param2\":\"SELECT s.`service_id` serviceId,s.`service_code` serviceCode,s.`business_type_cd`  businessTypeCd,s.name `name`,s.is_instance isInstance,\n                                                                       s.`messageQueueName` messageQueueName,s.url url,s.`provide_app_id` provideAppId FROM c_service s WHERE s.`status_cd` = \'0\' LIMIT #page#, #rows#\"\n                                                             }','',NULL,'{\n                                                        	\"PARAM\": {\n                                                        		\"param1\": \"$.##Object\",\n                                                        		\"param2\": \"$.#rows#Array\"\n                                                        	},\n                                                        	\"TEMPLATE\": {\n                                                        		\"response\": {\n                                                        			\"code\": \"0000\",\n                                                        			\"message\": \"成功\"\n                                                        		}\n                                                        	}\n                                                        }','','2018-11-14 13:28:45','0'),(9,'query.center.route','查询路由数据','page,rows,sord','1','{\"param1\":\"select count(1) records,ceil(#page#/#rows#)+1 page,ceil(count(1)/#rows#) total from c_route a,c_service cs WHERE a.`service_id` = cs.`service_id` AND cs.`status_cd` = \'0\' and a.status_cd = \'0\'\",\n                                                             \"param2\":\"SELECT s.id id,s.`app_id` appId,s.`service_id` serviceId,s.`invoke_model` invokeModel,cs.`name` serviceName,cs.`service_code` serviceCode,s.`order_type_cd` orderTypeCd,s.`invoke_limit_times` invokelimitTimes FROM c_route s,c_service cs WHERE s.`service_id` = cs.`service_id` AND cs.`status_cd` = \'0\' AND s.`status_cd` = \'0\' LIMIT #page#, #rows#\"\n                                                             }','',NULL,'{\n                                                        	\"PARAM\": {\n                                                        		\"param1\": \"$.##Object\",\n                                                        		\"param2\": \"$.#rows#Array\"\n                                                        	},\n                                                        	\"TEMPLATE\": {\n                                                        		\"response\": {\n                                                        			\"code\": \"0000\",\n                                                        			\"message\": \"成功\"\n                                                        		}\n                                                        	}\n                                                        }','','2018-11-14 13:28:45','0'),(10,'query.console.caches','查询缓存数据','userId','1','{\n                                                             \"param1\":\"SELECT c.id id,c.`cache_code` cacheCode, c.`name` cacheName FROM c_cache c, c_cache_2_user c2u WHERE c.`cache_code` = c2u.`cache_code` AND c.`status_cd` = \'0\'\n                                                                       AND c2u.`status_cd` = \'0\' AND c2u.`user_id` = #userId# AND c.`group` = \'COMMON\' ORDER BY c.`seq` ASC\"\n                                                             }','',NULL,'{\n                                                        	\"PARAM\": {\n                                                        		\"param1\": \"$.#rows#Array\"\n                                                        	},\n                                                        	\"TEMPLATE\": {\n                                                        		\"response\": {\n                                                        			\"code\": \"0000\",\n                                                        			\"message\": \"成功\"\n                                                        		}\n                                                        	}\n                                                        }','','2018-11-14 13:28:45','0'),(11,'query.console.cache','查询单条缓存信息','cacheCode','1','{\n                                                             \"param1\":\"SELECT c.id id,c.`cache_code` cacheCode, c.`name` cacheName,c.`param` param,c.`service_code` serviceCode FROM c_cache c WHERE  c.`status_cd` = \'0\' AND c.`cache_code` = #cacheCode#\"\n                                                             }','',NULL,'{\n                                                        	\"PARAM\": {\n                                                        		\"param1\": \"$.#cache#Object\"\n                                                        	},\n                                                        	\"TEMPLATE\": {\n                                                        		\"response\": {\n                                                        			\"code\": \"0000\",\n                                                        			\"message\": \"成功\"\n                                                        		}\n                                                        	}\n                                                        }','','2018-11-14 13:28:45','0'),(12,'save.center.mapping','保存映射信息','domain,name,key,value,remark','1','{\n                                                             \"param1\":\"INSERT c_mapping(domain,`name`,`key`,`value`,remark) VALUES(#domain#,#name#,#key#,#value#,#remark#)\"\n                                                             }','',NULL,'{\n                                                        	\"TEMPLATE\": {\n                                                        		\"response\": {\n                                                        			\"code\": \"0000\",\n                                                        			\"message\": \"成功\"\n                                                        		}\n                                                        	}\n                                                        }','','2018-11-14 13:28:45','0'),(13,'delete.center.mapping','删除映射信息','id','1','{\n                                                             \"param1\":\"UPDATE c_mapping m SET m.status_cd = \'1\' WHERE m.status_cd = \'0\' AND m.id = #id#\"\n                                                             }','',NULL,'{\n                                                        	\"TEMPLATE\": {\n                                                        		\"response\": {\n                                                        			\"code\": \"0000\",\n                                                        			\"message\": \"成功\"\n                                                        		}\n                                                        	}\n                                                        }','','2018-11-14 13:28:45','0'),(14,'update.center.mapping','修改映射信息','id,domain,name,key,value,remark','1','{\n                                                             \"param1\":\"UPDATE c_mapping m SET m.domain=#domain#,m.name = #name#,m.key=#key#,m.value=#value#,m.remark=#remark# WHERE m.status_cd = \'0\' AND m.id = #id#\"\n                                                             }','',NULL,'{\n                                                        	\"TEMPLATE\": {\n                                                        		\"response\": {\n                                                        			\"code\": \"0000\",\n                                                        			\"message\": \"成功\"\n                                                        		}\n                                                        	}\n                                                        }','','2018-11-14 13:28:45','0'),(15,'query.staff.infos','查询员工信息','page,rows,storeId','1','{\"param1\":\"select count(1) total,ceil(#page#/#rows#)+1 page,ceil(count(1)/#rows#) records  from u_user a where a.level_cd = \'01\'\",\n\"param2\":\"select a.user_id userId,a.name,a.email,a.address,a.sex,a.status_cd statusCd,a.tel,a.create_time createTime from u_user a where a.level_cd = \'01\' LIMIT #page#, #rows#\"}',NULL,NULL,'{\n                                                        	\"PARAM\": {\n                                                        		\"param1\": \"$.##Object\",\n                                                        		\"param2\": \"$.#rows#Array\"\n                                                        	},\n                                                        	\"TEMPLATE\": {}\n                                                        }',NULL,'2018-12-09 07:50:18','0'),(16,'search.staff.infos','搜索员工信息','page,rows,search','1','{\"param1\":\"select count(1) total,ceil(#page#/#rows#)+1 page,ceil(count(1)/#rows#) records  from u_user a where a.level_cd = \'01\' and a.name = #search#\",\n\"param2\":\"select a.user_id userId,a.name,a.email,a.address,a.sex,a.status_cd statusCd,a.tel,a.create_time createTime from u_user a where a.level_cd = \'01\' and a.name = #search# LIMIT #page#, #rows#\"}',NULL,NULL,'{\n                                                        	\"PARAM\": {\n                                                        		\"param1\": \"$.##Object\",\n                                                        		\"param2\": \"$.#rows#Array\"\n                                                        	},\n                                                        	\"TEMPLATE\": {}\n                                                        }',NULL,'2018-12-12 10:21:55','0'),(17,'query.community.infos','查询小区信息','page,rows','1','{\"param1\":\"select count(1) total,ceil(#page#/#rows#)+1 page,ceil(count(1)/#rows#) records \nfrom s_store a,s_member_store b,s_store c where a.store_type_cd = \'800900000000\' \nand a.store_id = b.store_id and b.member_id = c.store_id and c.store_type_cd = \'800900000002\'\nand a.status_cd = \'0\' and b.status_cd = \'0\' and c.status_cd = \'0\'\",\n\"param2\":\"select a.store_id storeId,a.name storeName,a.nearby_landmarks nearByLandmarks,a.address address,c.name agentName,c.tel agentTel,a.create_time createTime\nfrom s_store a,s_member_store b,s_store c where a.store_type_cd = \'800900000000\' \nand a.store_id = b.store_id and b.member_id = c.store_id and c.store_type_cd = \'800900000002\'\nand a.status_cd = \'0\' and b.status_cd = \'0\' and c.status_cd = \'0\' LIMIT #page#, #rows#\"}',NULL,NULL,'{\n                                                        	\"PARAM\": {\n                                                        		\"param1\": \"$.##Object\",\n                                                        		\"param2\": \"$.#rows#Array\"\n                                                        	},\n                                                        	\"TEMPLATE\": {}\n                                                        }',NULL,'2018-12-12 14:22:05','0'),(18,'search.community.infos','搜索小区','page,rows,search','1','{\"param1\":\"select count(1) total,ceil(#page#/#rows#)+1 page,ceil(count(1)/#rows#) records \nfrom s_store a,s_member_store b,s_store c where a.store_type_cd = \'800900000000\' \nand a.store_id = b.store_id and b.member_id = c.store_id and c.store_type_cd = \'800900000002\'\nand a.status_cd = \'0\' and b.status_cd = \'0\' and c.status_cd = \'0\' and a.name = #search#\",\n\"param2\":\"select a.store_id storeId,a.name storeName,a.nearby_landmarks nearByLandmarks,a.address address,c.name agentName,c.tel agentTel,a.create_time createTime\nfrom s_store a,s_member_store b,s_store c where a.store_type_cd = \'800900000000\' \nand a.store_id = b.store_id and b.member_id = c.store_id and c.store_type_cd = \'800900000002\'\nand a.status_cd = \'0\' and b.status_cd = \'0\' and c.status_cd = \'0\' and a.name = #search# LIMIT #page#, #rows#\"}',NULL,NULL,'{\n                                                        	\"PARAM\": {\n                                                        		\"param1\": \"$.##Object\",\n                                                        		\"param2\": \"$.#rows#Array\"\n                                                        	},\n                                                        	\"TEMPLATE\": {}\n                                                        }',NULL,'2018-12-12 14:56:10','0'),(19,'check.property.staffHasProperty','判断是否有查询物业信息','userId','1','{\"param1\":\"select count(1) count from p_property p,p_property_user pu\nwhere p.property_id = pu.property_id\nand pu.rel_cd = \'600311000002\'\nand p.status_cd = \'0\'\nand pu.status_cd = \'0\'\n and pu.user_id = #userId#\"}',NULL,NULL,'{\"PARAM\": {\"param1\": \"$.##Object\"},\"TEMPLATE\": {}}',NULL,'2019-02-13 12:18:27','0'),(20,'query.property.info','查询物业信息','userId','1','{\"param1\":\"SELECT p.property_id propertyId,p.map_x mapX,p.map_y mapY,p.b_id bId,p.name name,p.nearby_landmarks nearbyLandmarks,p.tel tel\nFROM p_property p,p_property_user pu\nWHERE p.property_id = pu.property_id AND pu.rel_cd = \'600311000002\' AND p.status_cd = \'0\' AND pu.status_cd = \'0\' and pu.user_id = #userId#\",\n\"param2\":\"select a.attr_id attrId,a.spec_cd specCd,s.name propertyAttrName,a.value propertyAttrValue from p_property_attr a,spec s where a.property_id = #PARENT_propertyId# and a.spec_cd = s.spec_cd\n and s.domain = \'PROPERTY\'\"}',NULL,NULL,'{\n                                                        	\"PARAM\": {\n                                                        		\"param1\": \"$.##Object\",\n                                                        		\"param2\": \"$.#attrs#Array\"\n                                                        	},\n                                                        	\"TEMPLATE\": {}\n                                                        }',NULL,'2019-02-17 14:22:54','0'),(21,'query.store.byuser','通过用户ID查询参与组织信息','userId','1','{\"param1\":\"select ss.store_id storeId,ss.name,ss.address,ss.tel,ss.store_type_cd storeTypeCd,ss.nearby_landmarks nearbyLandmarks,ss.map_x mapX,ss.map_y mapY,ssu.user_id userId,ssu.rel_cd relCd from s_store ss,s_store_user ssu where ss.store_id = ssu.store_id and ssu.user_id = #userId# and ss.status_cd = \'0\' and ssu.status_cd = \'0\'\",\"param2\":\"select ssa.attr_id attrId,ssa.spec_cd specCd,ssa.value from s_store_attr ssa where ssa.store_id = #PARENT_storeId# and ssa.status_cd = \'0\'\"}',NULL,NULL,'{\n                                                        	\"PARAM\": {\n                                                        		\"param1\": \"$.##Object\",\n                                                        		\"param2\": \"$.#attrs#Array\"\n                                                        	},\n                                                        	\"TEMPLATE\": {}\n                                                        }',NULL,'2019-03-26 15:50:34','0'),(22,'check.hasUser.byNameOrTel','判断是否存在用户名或手机号的用户','name,tel','1','{\"param1\":\"select count(1) userCount from u_user a where a.name = #name# or a.tel = #tel# and a.status_cd = \'0\'\"}',NULL,NULL,'{\n                                                        	\"PARAM\": {\n                                                        		\"param1\": \"$.##Object\"\n                                                        	},\n                                                        	\"TEMPLATE\": {}\n                                                        }',NULL,'2019-03-27 13:40:18','0'),(23,'query.store.type','查询商户类型','type','1','{\"param1\":\"SELECT a.`store_type_cd` storeTypeCd,a.name,a.`id` FROM store_type a\"}',NULL,NULL,'{\"PARAM\": {\"param1\": \"$.#storeType#Array\"\r\n                                                        	},\r\n                                                        	\"TEMPLATE\": {}\r\n                                                        }',NULL,'2019-03-28 03:20:30','0'),(24,'query.user.privilege','查询用户权限','userId,domain','1','{\"param1\":\"SELECT  DISTINCT  tt.p_id `pId`,tt.name,tt.pg_id pgId,tt.pg_name pgName,tt.description,tt.create_time createTime FROM (\r\n	SELECT pp.*,\'-1\' AS pg_id,\'\' AS pg_name FROM p_privilege_user ppu,p_privilege pp\r\n	WHERE ppu.`p_id` = pp.`p_id`\r\n	AND pp.domain = #domain#\r\n	AND ppu.`privilege_flag` = \'0\'\r\n	AND ppu.`user_id` = #userId#\r\n	AND ppu.`status_cd` = \'0\'\r\n	AND pp.`status_cd` = \'0\'\r\n	UNION\r\n	SELECT pp.*,ppg.pg_id,ppg.name pg_name FROM p_privilege_user ppu,p_privilege_group ppg,p_privilege pp,p_privilege_rel ppr\r\n	WHERE ppu.`p_id` = ppr.pg_id\r\n	AND ppr.pg_id = ppg.pg_id\r\n	AND ppr.p_id = pp.`p_id`\r\n	AND pp.domain = #domain#\r\n	AND ppu.`privilege_flag` = \'1\'\r\n	AND ppu.`user_id` = #userId#\r\n	AND ppu.`status_cd` = \'0\'\r\n	AND pp.`status_cd` = \'0\'\r\n	AND ppg.status_cd = \'0\'\r\n	AND ppr.status_cd = \'0\'\r\n) tt  \"}\r\n',NULL,NULL,'{\"PARAM\": {\"param1\": \"$.#privileges#Array\"\r\n                                                        	},\r\n                                                        	\"TEMPLATE\": {}\r\n                                                        }',NULL,'2019-04-01 03:08:08','0'),(25,'query.user.userInfo','查询用户信息','userId','1','{\"param1\":\"SELECT \r\n  a.`user_id` userId,\r\n  a.name,\r\n  a.email,\r\n  a.`address`,\r\n  a.`location_cd` localtionCd,\r\n  a.sex,\r\n  a.tel,\r\n  a.`level_cd` levelCd,\r\n  a.status_cd statusCd,\r\n  a.create_time createTime \r\nFROM\r\n  u_user a \r\nWHERE a.`user_id` = #userId# \r\n\"}',NULL,NULL,'{\"PARAM\": {\"param1\": \"$.##Object\"\r\n                                                        	},\r\n                                                        	\"TEMPLATE\": {}\r\n                                                        }',NULL,'2019-04-01 11:43:00','0'),(26,'query.store.users','查询商户用户','page,rows,storeId','1','{\"param1\":\"SELECT COUNT(1) total,CEIL(#page#/#rows#)+1 page,ceil(count(1)/#rows#) records  \r\nFROM s_store ss,s_store_user ssu WHERE ss.`store_id` = ssu.`store_id`\r\nAND ss.`store_id` = #storeId#\r\nAND ss.`status_cd` = \'0\'\r\nAND ssu.`status_cd` = \'0\'\",\r\n\"param2\":\"SELECT \r\n  ss.`store_id` storeId,\r\n  ssu.`rel_cd` relCd,\r\n  ssu.`user_id` userId \r\nFROM\r\n  s_store ss,\r\n  s_store_user ssu \r\nWHERE ss.`store_id` = ssu.`store_id` \r\n  AND ss.`store_id` = #storeId#\r\n  AND ss.`status_cd` = \'0\' \r\n  AND ssu.`status_cd` = \'0\'\r\nLIMIT #page#, #rows#  \"}',NULL,NULL,'{\r\n                                                        	\"PARAM\": {\r\n                                                        		\"param1\": \"$.##Object\",\r\n                                                        		\"param2\": \"$.#datas#Array\"\r\n                                                        	},\r\n                                                        	\"TEMPLATE\": {}\r\n                                                        }',NULL,'2019-04-02 12:08:45','0'),(27,'query.store.privilegeGroup','根据商户查询权限组','storeId,storeTypeCd','1','{\"param1\":\"select a.pg_id pgId,a.name,a.description,a.create_time createTime,a.store_id storeId,a.domain storeTypeCd from p_privilege_group a where a.status_cd = \'0\' and a.store_id in (\'9999\',#storeId#) and a.domain = #storeTypeCd#\"}',NULL,NULL,'{\"PARAM\": {\"param1\": \"$.#privilegeGroups#Array\"\n                                                        	},\n                                                        	\"TEMPLATE\": {}\n                                                        }',NULL,'2019-04-05 15:36:19','0'),(28,'query.privilege.byPgId','根据权限组ID查询权限','pgId','1','{\"param1\":\"select pp.p_id pId,pp.name,pp.description,pp.create_time createTime from p_privilege pp,p_privilege_rel ppr\nwhere pp.p_id = ppr.p_id\nand ppr.pg_id = #pgId#\nand pp.status_cd = \'0\'\nand ppr.status_cd = \'0\'\"}',NULL,NULL,'{\"PARAM\": {\"param1\": \"$.#privileges#Array\"\n                                                        	},\n                                                        	\"TEMPLATE\": {}\n                                                        }',NULL,'2019-04-07 10:27:19','0'),(29,'query.privilegeGroup.noAddPrivilege','查询权限组中未添加的权限','storeId,pgId,storeTypeCd','1','{\"param1\":\"\n	select pp.p_id pId,pp.name,pp.description,pp.create_time createTime,pp.domain,pp.status_cd statusCd from p_privilege pp\nwhere pp.domain = #storeTypeCd#\nand pp.status_cd = \'0\'\nand not exists(\n	select * from p_privilege_rel ppr,p_privilege_group ppg\n    where ppr.pg_id = ppg.pg_id\n    and ppr.p_id = pp.p_id\n    and ppg.pg_id = #pgId#\n    and ppg.store_id = #storeId#\n    and ppr.status_cd = \'0\'\n    and ppg.status_cd = \'0\'\n)\"}',NULL,NULL,'{\"PARAM\": {\"param1\": \"$.#privileges#Array\"\n                                                        	},\n                                                        	\"TEMPLATE\": {}\n                                                        }',NULL,'2019-04-08 14:44:15','0'),(30,'query.user.byName','根据用户名称查询用户','name','1','{\"param1\":\"\r\nSELECT \r\n  a.`user_id` userId,\r\n  a.name,\r\n  a.email,\r\n  a.`address`,\r\n  a.`location_cd` localtionCd,\r\n  a.sex,\r\n  a.tel,\r\n  a.`level_cd` levelCd,\r\n  a.status_cd statusCd,\r\n  a.create_time createTime \r\nFROM\r\n  u_user a \r\nWHERE a.`name` = #name# \r\n\"}',NULL,NULL,'{\"PARAM\": {\"param1\": \"$.#users#Array\"\r\n                                                        	},\r\n                                                        	\"TEMPLATE\": {}\r\n                                                        }',NULL,'2019-04-10 06:03:50','0'),(31,'query.storeUser.byUserIds','根据用户ID查询是不是商户ID','userIds,storeId','1','{\"param1\":\"\r\nSELECT a.`user_id` userId,a.`rel_cd` relCd,a.`store_user_id` storeUserId,a.`store_id` storeId,a.`user_id` userId FROM s_store_user a WHERE a.`user_id` IN (#userIds#) and a.`store_id` = #storeId# and a.status_cd = \'0\'\r\n\"}',NULL,NULL,'{\"PARAM\": {\"param1\": \"$.#storeUsers#Array\"\r\n                                                        	},\r\n                                                        	\"TEMPLATE\": {}\r\n                                                        }',NULL,'2019-04-11 01:12:30','0'),(32,'query.privilegeGroup.noAddPrivilegeGroup','查询没有给员工赋权限组','userId,storeId,storeTypeCd','1','{\"param1\":\"\r\nSELECT \r\n  ppg.`pg_id` pgId,\r\n  ppg.`name`,\r\n  ppg.`description`,\r\n  ppg.`create_time` createTime,\r\n  ppg.`store_id` storeId,\r\n  ppg.`domain` \r\nFROM\r\n  p_privilege_group ppg \r\nWHERE ppg.`domain` = #storeTypeCd#\r\n  AND ppg.`store_id` = #storeId#\r\n  AND ppg.`status_cd` = \'0\' \r\n  AND NOT EXISTS \r\n  (SELECT \r\n    1 \r\n  FROM\r\n    p_privilege_user ppu \r\n  WHERE ppu.`status_cd` = \'0\' \r\n    AND ppu.`user_id` = #userId#\r\n    AND ppu.`p_id` = ppg.`pg_id` \r\n    AND ppu.`privilege_flag` = \'1\')\r\n\"\r\n}',NULL,NULL,'{\"PARAM\": {\"param1\": \"$.#privilgeGroups#Array\"\r\n                                                        	},\r\n                                                        	\"TEMPLATE\": {}\r\n                                                        }',NULL,'2019-04-13 03:39:47','0'),(33,'query.privilege.noAddPrivilege','查询没有给员工赋权限','userId,storeId,storeTypeCd','1','{\"param1\":\"\r\nSELECT \r\n  pp.`p_id` pId,\r\n  pp.`name`,\r\n  pp.`description`,\r\n  pp.`create_time` createTime,\r\n  pp.`domain` \r\nFROM\r\n  p_privilege pp \r\nWHERE pp.`domain` = #storeTypeCd#\r\n  AND pp.`status_cd` = \'0\' \r\n  AND NOT EXISTS \r\n  (SELECT \r\n    1 \r\n  FROM\r\n    p_privilege_user ppu \r\n  WHERE ppu.p_id = pp.`p_id` \r\n    AND ppu.privilege_flag = \'0\' \r\n    AND ppu.user_id = #userId#\r\n    AND ppu.status_cd = \'0\')\r\n\"\r\n}',NULL,NULL,'{\"PARAM\": {\"param1\": \"$.#privilges#Array\"\r\n                                                        	},\r\n                                                        	\"TEMPLATE\": {}\r\n                                                        }',NULL,'2019-04-13 05:17:12','0'),(34,'query.myCommunity.byMember','查询商户入驻小区信息','memberId,memberTypeCd','1','{\n	\"param1\": \"SELECT    sc.`community_id` communityId,   sc.`name`,   sc.`address`,   sc.`nearby_landmarks` nearbyLandmarks,   sc.`city_code` cityCode,   sc.`map_x` mapX,   sc.`map_y` mapY, scm.`community_member_id` communityMemberId,  scm.`member_id` memberId,   scm.`member_type_cd` memberTypeCd, scm.audit_status_cd auditStatusCd, scm.`status_cd` statusCd FROM   s_community sc,   s_community_member scm  WHERE sc.`community_id` = scm.`community_id`    AND sc.`status_cd` = \'0\'    AND scm.`member_id` = #memberId#   AND scm.`member_type_cd` = #memberTypeCd#   AND scm.`status_cd` = \'0\' and scm.audit_status_cd in (\'0000\', \'1000\',\'1001\')\",\n	\"param2\": \"select sca.attr_id attrId,sca.spec_cd specCd,sca.value,s.name from s_community_attr sca,spec s where sca.status_cd = \'0\' and sca.community_id = #PARENT_communityId# and s.domain=\'COMMUNITY_ATTR\' and s.spec_cd=sca.spec_cd\"\n}',NULL,NULL,'{\"PARAM\": {\"param1\": \"$.#communitys#Array\",\"param2\":\"$.communitys.#attrs#Array\"},\"TEMPLATE\": {}}',NULL,'2019-04-15 03:36:02','0'),(35,'query.noEnterCommunity.byMember','查询商户未入驻小区信息','memberId,memberTypeCd','1','{\"param1\":\"SELECT sc.community_id communityId, sc.name, sc.address, sc.nearby_landmarks nearbyLandmarks, sc.city_code cityCode, sc.map_x mapX, sc.map_y mapY\nFROM s_community sc\nWHERE sc.status_cd = \'0\'\nand not exists(\n	select 1 from s_community_member scm \n    where scm.status_cd <> \'1\'\n    and scm.community_id = sc.community_id\n    and scm.member_id = #memberId#\n    and scm.member_type_cd = #memberTypeCd#\n)\",\"param2\":\"select sca.attr_id attrId,sca.spec_cd specCd,sca.value,s.name from s_community_attr sca,spec s where sca.status_cd = \'0\' and sca.community_id = #PARENT_communityId# and s.domain=\'COMMUNITY_ATTR\' and s.spec_cd=sca.spec_cd\"}',NULL,NULL,'{\"PARAM\": {\"param1\": \"$.#communitys#Array\",\"param2\":\"$.communitys.#attrs#Array\"},\"TEMPLATE\": {}}',NULL,'2019-04-15 14:38:38','0'),(36,'check.user.hasPrivilege','检查用户是否有权限','pId,userId','1','{\"param1\":\"SELECT  DISTINCT  tt.p_id `pId`,tt.name,tt.pg_id pgId,tt.pg_name pgName,tt.description,tt.create_time createTime FROM (\n	SELECT pp.*,\'-1\' AS pg_id,\'\' AS pg_name FROM p_privilege_user ppu,p_privilege pp\n	WHERE ppu.`p_id` = pp.`p_id`\n	AND pp.p_id = #pId#\n	AND ppu.`privilege_flag` = \'0\'\n	AND ppu.`user_id` = #userId#\n	AND ppu.`status_cd` = \'0\'\n	AND pp.`status_cd` = \'0\'\n	UNION\n	SELECT pp.*,ppg.pg_id,ppg.name pg_name FROM p_privilege_user ppu,p_privilege_group ppg,p_privilege pp,p_privilege_rel ppr\n	WHERE ppu.`p_id` = ppr.pg_id\n	AND ppr.pg_id = ppg.pg_id\n	AND ppr.p_id = pp.`p_id`\n	AND pp.p_id = #pId#\n	AND ppu.`privilege_flag` = \'1\'\n	AND ppu.`user_id` = #userId#\n	AND ppu.`status_cd` = \'0\'\n	AND pp.`status_cd` = \'0\'\n	AND ppg.status_cd = \'0\'\n	AND ppr.status_cd = \'0\'\n) tt  \"}',NULL,NULL,'{\"PARAM\": {\"param1\": \"$.#privileges#Array\"},\"TEMPLATE\": {}}',NULL,'2019-04-16 01:14:43','0'),(37,'query.noEnterCommunity.byMemberAndName','根据商户名称查询未入住小区信息','memberId,memberTypeCd,name','1','{\"param1\":\"SELECT sc.community_id communityId, sc.name, sc.address, sc.nearby_landmarks nearbyLandmarks, sc.city_code cityCode, sc.map_x mapX, sc.map_y mapY\r\nFROM s_community sc\r\nWHERE sc.status_cd = \'0\'\r\nand sc.name = #name#\r\nand not exists(\r\n	select 1 from s_community_member scm \r\n    where scm.status_cd <> \'1\'\r\n    and scm.community_id = sc.community_id\r\n    and scm.member_id = #memberId#\r\n    and scm.member_type_cd = #memberTypeCd#\r\n)\",\"param2\":\"select sca.attr_id attrId,sca.spec_cd specCd,sca.value,s.name from s_community_attr sca,spec s where sca.status_cd = \'0\' and sca.community_id = #PARENT_communityId# and s.domain=\'COMMUNITY_ATTR\' and s.spec_cd=sca.spec_cd\"}',NULL,NULL,'{\"PARAM\": {\"param1\": \"$.#communitys#Array\",\"param2\":\"$.communitys.#attrs#Array\"},\"TEMPLATE\": {}}',NULL,'2019-04-18 01:22:36','0');

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
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8;

/*Data for the table `c_status` */

insert  into `c_status`(`id`,`status_cd`,`name`,`description`,`create_time`) values (1,'1','无效的，不在用的','无效的，不在用的','2018-11-14 13:28:44'),(2,'0','有效的，在用的','有效的，在用的','2018-11-14 13:28:44'),(3,'S','保存成功','保存成功','2018-11-14 13:28:44'),(4,'D','作废订单','作废订单','2018-11-14 13:28:44'),(5,'E','错误订单','错误订单','2018-11-14 13:28:44'),(6,'NE','通知错误订单','通知错误订单','2018-11-14 13:28:44'),(7,'C','订单完成','订单完成','2018-11-14 13:28:44'),(8,'B','Business过程','Business过程','2018-11-14 13:28:44'),(9,'I','Instance过程','Instance过程','2018-11-14 13:28:44'),(10,'1000','入驻小区审核','入驻小区审核','2019-04-15 03:26:51'),(11,'2001','房屋已售','房屋已售','2019-04-17 03:10:48'),(12,'2002','房屋未售','房屋未售','2019-04-17 03:11:10'),(13,'1001','申请退出小区审核','申请退出小区审核','2019-04-18 07:46:14'),(14,'0000','审核完成','审核完成','2019-04-26 16:12:42'),(15,'2003','房屋出售已交定金','房屋出售已交定金','2019-05-09 05:45:00'),(16,'2004','房屋已出租','房屋已出租','2019-05-09 05:46:30');

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

insert  into `c_template_col`(`id`,`template_code`,`col_name`,`col_code`,`col_model`,`seq`,`create_time`,`status_cd`) values (1,'mapping','列ID','id','{ \"name\": \"id\",\"index\": \"id\",\"width\": \"90\",\"editable\": true,\"sorttype\": \"int\" }',1,'2018-11-14 13:28:51','0'),(2,'mapping','域','domain','{ \"name\": \"domain\",\"index\": \"domain\",\"width\": \"90\",\n                                                                                                             \"editable\": true,\n                                                                                                             \"formatoptions\": { \"defaultValue\": \"DOMAIN.COMMON\" }\n                                                                                                           }',2,'2018-11-14 13:28:51','0'),(3,'mapping','名称','name','{ \"name\": \"name\",\"index\": \"name\",\"width\": \"90\",\n                                                                                                             \"editable\": true }',3,'2018-11-14 13:28:51','0'),(4,'mapping','键','key','{ \"name\": \"key\",\"index\": \"key\",\"width\": \"90\",\n                                                                                                             \"editable\": true }',4,'2018-11-14 13:28:51','0'),(5,'mapping','值','value','{ \"name\": \"value\",\"index\": \"value\",\"width\": \"90\",\n                                                                                                             \"editable\": true }',5,'2018-11-14 13:28:51','0'),(6,'mapping','备注','value','{ \"name\": \"remark\",\"index\": \"remark\",\"width\": \"90\",\n                                                                                                             \"editable\": true }',6,'2018-11-14 13:28:51','0'),(7,'mapping','BUTTON','BUTTON','{\r\n 	\"name\": \"detail\",\r\n 	\"index\": \"\",\r\n 	\"width\": \"40\",\r\n 	\"fixed\": true,\r\n 	\"sortable\": \"false\",\r\n 	\"resize\": \"false\",\r\n 	\"formatter\": \"function(cellvalue, options, rowObject){ var temp =\\\"<div style=\'margin-left:8px;\'><div title=\'详情记录\' style=\'float:left;cursor:pointer;\' class=\'ui-pg-div\' id=\'jEditButton_3\' onclick=\'detail(\\\" + rowObject + \\\")\' onmouseover=\'jQuery(this).addClass(\'ui-state-hover\');\' onmouseout=\'jQuery(this).removeClass(\'ui-state-hover\');\'><span class=\'ui-icon fa-search-plus\'/></div></div>\\\";return temp; }\"\r\n }',7,'2018-11-14 13:28:51','0'),(8,'app','列ID','id','{ \"name\": \"id\",\"index\": \"id\",\"width\": \"20\",\n                                                                                                             \"editable\": true,\n                                                                                                             \"sorttype\": \"int\" }',1,'2018-11-14 13:28:51','0'),(9,'app','AppId','domain','{ \"name\": \"appId\",\"index\": \"appId\",\"width\": \"40\",\n                                                                                                             \"editable\": true\n                                                                                                           }',2,'2018-11-14 13:28:51','0'),(10,'app','名称','name','{ \"name\": \"name\",\"index\": \"name\",\"width\": \"50\",\n                                                                                                             \"editable\": true }',3,'2018-11-14 13:28:51','0'),(11,'app','秘钥','securityCode','{ \"name\": \"securityCode\",\"index\": \"securityCode\",\"width\": \"50\",\n                                                                                                             \"editable\": true }',4,'2018-11-14 13:28:51','0'),(12,'app','白名单','whileListIp','{ \"name\": \"whileListIp\",\"index\": \"whileListIp\",\"width\": \"90\",\n                                                                                                             \"editable\": true }',5,'2018-11-14 13:28:51','0'),(13,'app','黑名单','blackListIp','{ \"name\": \"blackListIp\",\"index\": \"blackListIp\",\"width\": \"40\",\n                                                                                                             \"editable\": true }',6,'2018-11-14 13:28:51','0'),(14,'app','备注','value','{ \"name\": \"remark\",\"index\": \"remark\",\"width\": \"90\",\n                                                                                                             \"editable\": true }',7,'2018-11-14 13:28:51','0'),(15,'app','BUTTON','BUTTON','{\n                                                                                                            \"name\": \"detail\",\n                                                                                                            \"index\": \"\",\n                                                                                                            \"width\": \"40\",\n                                                                                                            \"fixed\": \"true\",\n                                                                                                            \"sortable\": \"false\",\n                                                                                                            \"resize\": \"false\",\n                                                                                                            \"formatter\": \"function(cellvalue, options, rowObject){\n var temp =\"<div style=\'margin-left:8px;\'><div title=\'详情记录\' style=\'float:left;cursor:pointer;\' class=\'ui-pg-div\' id=\'jEditButton_3\' onclick=\'detail(\"+rowObject+\")\' onmouseover=\'jQuery(this).addClass(\'ui-state-hover\');\' onmouseout=\'jQuery(this).removeClass(\'ui-state-hover\');\'><span class=\'ui-icon fa-search-plus\'/></div></div>\";\n return temp; \n}\"\n                                                                                                          }',8,'2018-11-14 13:28:51','0'),(16,'service','服务ID','serviceId','{ \"name\": \"serviceId\",\"index\": \"serviceId\",\"width\": \"20\",\n                                                                                                             \"editable\": true,\n                                                                                                             \"sorttype\": \"int\" }',1,'2018-11-14 13:28:51','0'),(17,'service','服务编码','serviceCode','{ \"name\": \"serviceCode\",\"index\": \"serviceCode\",\"width\": \"40\",\n                                                                                                             \"editable\": true\n                                                                                                           }',2,'2018-11-14 13:28:51','0'),(18,'service','业务类型','businessTypeCd','{ \"name\": \"businessTypeCd\",\"index\": \"businessTypeCd\",\"width\": \"50\",\n                                                                                                             \"editable\": true }',3,'2018-11-14 13:28:51','0'),(19,'service','名称','name','{ \"name\": \"name\",\"index\": \"name\",\"width\": \"40\",\n                                                                                                             \"editable\": true }',4,'2018-11-14 13:28:51','0'),(20,'service','消息队列','messageQueueName','{ \"name\": \"messageQueueName\",\"index\": \"messageQueueName\",\"width\": \"10\",\n                                                                                                             \"editable\": true }',5,'2018-11-14 13:28:51','0'),(21,'service','需要Instance','isInstance','{ \"name\": \"isInstance\",\"index\": \"isInstance\",\"width\": \"10\",\n                                                                                                             \"editable\": true }',6,'2018-11-14 13:28:51','0'),(22,'service','URL','url','{ \"name\": \"url\",\"index\": \"url\",\"width\": \"60\",\n                                                                                                             \"editable\": true }',7,'2018-11-14 13:28:51','0'),(23,'service','提供者AppId','provideAppId','{ \"name\": \"provideAppId\",\"index\": \"provideAppId\",\"width\": \"10\",\n                                                                                                             \"editable\": true }',8,'2018-11-14 13:28:51','0'),(24,'service','BUTTON','BUTTON','{\n                                                                                                            \"name\": \"detail\",\n                                                                                                            \"index\": \"\",\n                                                                                                            \"width\": \"40\",\n                                                                                                            \"fixed\": \"true\",\n                                                                                                            \"sortable\": \"false\",\n                                                                                                            \"resize\": \"false\",\n                                                                                                            \"formatter\": \"function(cellvalue, options, rowObject){\n var temp =\"<div style=\'margin-left:8px;\'><div title=\'详情记录\' style=\'float:left;cursor:pointer;\' class=\'ui-pg-div\' id=\'jEditButton_3\' onclick=\'detail(\"+rowObject+\")\' onmouseover=\'jQuery(this).addClass(\'ui-state-hover\');\' onmouseout=\'jQuery(this).removeClass(\'ui-state-hover\');\'><span class=\'ui-icon fa-search-plus\'/></div></div>\";\n return temp; \n}\"\n                                                                                                          }',9,'2018-11-14 13:28:51','0'),(25,'route','路由ID','id','{ \"name\": \"id\",\"index\": \"id\",\"width\": \"10\",\n                                                                                                             \"editable\": true,\n                                                                                                             \"sorttype\": \"int\" }',1,'2018-11-14 13:28:51','0'),(26,'route','AppId','appId','{ \"name\": \"appId\",\"index\": \"appId\",\"width\": \"30\",\n                                                                                                             \"editable\": true\n                                                                                                           }',2,'2018-11-14 13:28:51','0'),(27,'route','服务ID','serviceId','{ \"name\": \"serviceId\",\"index\": \"serviceId\",\"width\": \"30\",\n                                                                                                             \"editable\": true }',3,'2018-11-14 13:28:51','0'),(28,'route','调用方式','invokeModel','{ \"name\": \"invokeModel\",\"index\": \"invokeModel\",\"width\": \"50\",\n                                                                                                              \"editable\": true }',4,'2018-11-14 13:28:51','0'),(29,'route','服务名称','serviceName','{ \"name\": \"serviceName\",\"index\": \"serviceName\",\"width\": \"30\",\n                                                                                                             \"editable\": true }',5,'2018-11-14 13:28:51','0'),(30,'route','服务编码','serviceCode','{ \"name\": \"serviceCode\",\"index\": \"serviceCode\",\"width\": \"30\",\n                                                                                                             \"editable\": true }',6,'2018-11-14 13:28:51','0'),(31,'route','订单类型','orderTypeCd','{ \"name\": \"orderTypeCd\",\"index\": \"orderTypeCd\",\"width\": \"30\",\n                                                                                                             \"editable\": true }',7,'2018-11-14 13:28:51','0'),(32,'route','调用次数限制','invokelimitTimes','{ \"name\": \"invokelimitTimes\",\"index\": \"invokelimitTimes\",\"width\": \"40\",\n                                                                                                             \"editable\": true }',8,'2018-11-14 13:28:51','0'),(33,'route','BUTTON','BUTTON','{\n                                                                                                            \"name\": \"detail\",\n                                                                                                            \"index\": \"\",\n                                                                                                            \"width\": \"40\",\n                                                                                                            \"fixed\": \"true\",\n                                                                                                            \"sortable\": \"false\",\n                                                                                                            \"resize\": \"false\",\n                                                                                                            \"formatter\": \"function(cellvalue, options, rowObject){\n var temp =\"<div style=\'margin-left:8px;\'><div title=\'详情记录\' style=\'float:left;cursor:pointer;\' class=\'ui-pg-div\' id=\'jEditButton_3\' onclick=\'detail(\"+rowObject+\")\' onmouseover=\'jQuery(this).addClass(\'ui-state-hover\');\' onmouseout=\'jQuery(this).removeClass(\'ui-state-hover\');\'><span class=\'ui-icon fa-search-plus\'/></div></div>\";\n return temp; \n}\"}',9,'2018-11-14 13:28:51','0'),(34,'cache','缓存ID','id','{ \"name\": \"id\",\"index\": \"id\",\"width\": \"10\",\n                                                                                                             \"editable\": true,\n                                                                                                             \"sorttype\": \"int\" }',1,'2018-11-14 13:28:51','0'),(35,'cache','缓存编码','cacheCode','{ \"name\": \"cacheCode\",\"index\": \"cacheCode\",\"width\": \"30\",\n                                                                                                             \"editable\": true }',2,'2018-11-14 13:28:51','0'),(36,'cache','缓存名称','cacheName','{ \"name\": \"cacheName\",\"index\": \"cacheName\",\"width\": \"30\",\n                                                                                                             \"editable\": true }',3,'2018-11-14 13:28:51','0'),(37,'cache','BUTTON','BUTTON','{\n                                                                                                            \"name\": \"detail\",\n                                                                                                            \"index\": \"\",\n                                                                                                            \"width\": \"40\",\n                                                                                                            \"fixed\": \"true\",\n                                                                                                            \"sortable\": \"false\",\n                                                                                                            \"resize\": \"false\",\n                                                                                                            \"formatter\": \"\"function(cellvalue, options, rowObject){ var temp =\"<div style=\'margin-left:8px;\'><button type=\'button\' class=\'btn btn-warning\' style=\'border-radius: .25rem;\' onclick=\'flush(this,\"+rowObject.cacheCode+\")\'>刷新缓存</button></div>\";return temp; }\"\n                                                                                                          }',4,'2018-11-14 13:28:51','0');

/*Table structure for table `community_member_type` */

DROP TABLE IF EXISTS `community_member_type`;

CREATE TABLE `community_member_type` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `member_type_cd` varchar(12) NOT NULL COMMENT '编码',
  `name` varchar(50) NOT NULL COMMENT '名称',
  `description` varchar(200) DEFAULT NULL COMMENT '描述',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `member_type_cd` (`member_type_cd`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

/*Data for the table `community_member_type` */

insert  into `community_member_type`(`id`,`member_type_cd`,`name`,`description`,`create_time`) values (1,'390001200001','商户关系','小区和商户之间关系','2018-12-17 04:07:21'),(2,'390001200002','物业关系','小区和物业之间关系','2018-12-17 04:08:16'),(3,'390001200003','代理商关系','小区和代理商之间关系','2018-12-17 04:08:34'),(4,'390001200004','小区楼','小区楼','2019-04-24 05:43:41');

/*Table structure for table `credentials` */

DROP TABLE IF EXISTS `credentials`;

CREATE TABLE `credentials` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `credentials_cd` varchar(12) NOT NULL COMMENT '证件编码',
  `name` varchar(50) NOT NULL COMMENT '证件名称',
  `description` varchar(200) DEFAULT NULL COMMENT '描述',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

/*Data for the table `credentials` */

insert  into `credentials`(`id`,`credentials_cd`,`name`,`description`,`create_time`) values (1,'300200900001','营业执照','营业执照','2019-03-28 14:56:45');

/*Table structure for table `f_floor` */

DROP TABLE IF EXISTS `f_floor`;

CREATE TABLE `f_floor` (
  `floor_id` varchar(30) NOT NULL COMMENT '楼ID',
  `b_id` varchar(30) NOT NULL COMMENT '业务Id',
  `floor_num` varchar(12) NOT NULL COMMENT '楼编号',
  `name` varchar(100) NOT NULL COMMENT '小区楼名称',
  `user_id` varchar(30) NOT NULL COMMENT '用户ID',
  `remark` varchar(200) DEFAULT NULL COMMENT '备注',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `status_cd` varchar(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考c_status表，S 保存，0, 在用 1失效',
  UNIQUE KEY `floor_id` (`floor_id`),
  UNIQUE KEY `idx_floor_id` (`floor_id`),
  KEY `idx_floor_b_id` (`b_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `f_floor` */

insert  into `f_floor`(`floor_id`,`b_id`,`floor_num`,`name`,`user_id`,`remark`,`create_time`,`status_cd`) values ('732019042181450002','89861991919','01','1号楼','302019042192720001','填写具体值','2019-04-21 15:18:14','0'),('732019042188750002','89861991920','021','2号楼1','30518940136629616640','021','2019-04-21 15:21:36','0'),('732019042218110002','89861991925','003','3号楼01','302019042192720001',NULL,'2019-04-21 16:41:54','0'),('732019042237920003','89861991925','005','5号楼01','302019042192720001',NULL,'2019-04-21 16:41:54','0'),('732019042248530001','89861991923','004','4号楼01','302019042192720001',NULL,'2019-04-21 16:40:19','0'),('732019042644990001','202019042604660002','045','045','30518940136629616640','测试，必须成功','2019-04-26 09:53:42','0'),('732019042715940003','202019042731610008','22','22','30518940136629616640','22','2019-04-26 17:38:57','0'),('732019042773170001','202019042729460002','24','24','30518940136629616640','24','2019-04-26 17:30:55','0'),('732019042784130004','202019042711910013','023','23','30518940136629616640','测试','2019-04-27 11:56:56','0'),('732019042790640002','202019042775180005','21','21','30518940136629616640','21','2019-04-26 17:37:14','0'),('732019042893370001','202019042870220006','26','26','30518940136629616640','26','2019-04-28 09:53:54','1'),('732019042904050001','202019042908790002','40','40','30518940136629616640','40','2019-04-29 11:47:36','0'),('732019042905340004','202019042953870011','36','36','30518940136629616640','36','2019-04-29 09:22:28','0'),('732019042907570008','202019042910990023','38','38','30518940136629616640','38','2019-04-29 09:23:40','0'),('732019042908600001','202019042982100002','34','34','30518940136629616640','34','2019-04-29 09:14:52','0'),('732019042921980002','202019042976730005','33','33','30518940136629616640','33','2019-04-29 09:21:46','0'),('732019042925840006','202019042971980017','38','38','30518940136629616640','38','2019-04-29 09:22:55','0'),('732019042937750002','202019042900080005','42','42','30518940136629616640','42','2019-04-29 11:48:06','0'),('732019042937930001','202019042905090005','28','28','30518940136629616640','28','2019-04-28 17:19:24','1'),('732019042939210005','202019042978390014','37','37','30518940136629616640','37','2019-04-29 09:22:43','0'),('732019042954400007','202019042996460020','39','39','30518940136629616640','39','2019-04-29 09:23:08','0'),('732019042986310003','202019042927450008','32','32','30518940136629616640','32','2019-04-29 09:21:59','0');

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
  `m_id` varchar(12) NOT NULL COMMENT '菜单ID',
  `name` varchar(10) NOT NULL COMMENT '菜单名称',
  `g_id` varchar(12) NOT NULL COMMENT '菜单组ID',
  `url` varchar(200) NOT NULL COMMENT '打开地址',
  `seq` int(11) NOT NULL COMMENT '列顺序',
  `p_id` varchar(12) NOT NULL COMMENT '权限ID',
  `description` varchar(200) DEFAULT NULL COMMENT '菜单描述',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `status_cd` varchar(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考c_status表，0在用，1失效',
  UNIQUE KEY `m_id` (`m_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `m_menu` */

insert  into `m_menu`(`m_id`,`name`,`g_id`,`url`,`seq`,`p_id`,`description`,`create_time`,`status_cd`) values ('700201904001','员工信息','800201904001','/flow/staff',1,'500201904001','员工信息','2019-04-01 02:19:51','0'),('700201904002','员工权限','800201904002','/flow/staffPrivilege',2,'500201904002','员工权限','2019-04-01 08:05:51','0'),('700201904003','权限组','800201904002','/flow/privilege',2,'500201904003','权限组','2019-04-01 08:06:16','0'),('700201904004','物业费','800201904008','/flow/propertyFeeFlow',3,'500201904004','物业费','2019-04-09 14:41:40','0'),('700201904005','停车费','800201904008','/',4,'500201904005','停车费','2019-04-09 14:50:56','0'),('700201904006','房屋管理','800201904006','/flow/roomFlow',3,'500201904006','房屋管理','2019-04-09 14:55:59','0'),('700201904007','我的报表','800201904009','/',1,'500201904007','我的报表','2019-04-09 14:58:32','0'),('700201904008','入住小区','800201904004','/flow/enterCommunity',1,'500201904008','入住小区','2019-04-09 15:02:14','0'),('700201904009','发布公告','800201904007','/',1,'500201904009','发布公告','2019-04-09 15:05:56','0'),('700201904010','添加业主','800201904005','/flow/ownerFlow',1,'500201904010','添加业主','2019-04-09 15:07:53','0'),('700201904011','小区楼','800201904006','/flow/floorFlow',1,'500201904011','小区楼','2019-04-19 07:59:49','0'),('700201904012','小区楼单元','800201904006','/flow/unitFlow',2,'500201904012','小区楼单元','2019-04-19 08:00:24','0'),('700201904013','业主成员','800201904005','/flow/ownerMemberFlow',2,'500201904013','业主成员','2019-05-17 13:59:07','0'),('700201904014','房屋售卖','800201904006','/flow/sellRoomFlow',4,'500201904014','房屋售卖','2019-05-20 07:05:25','0'),('700201904015','业主房产','800201904005','/flow/ownerRoomFlow',4,'500201904015','业主房产','2019-05-22 01:06:26','0'),('700201904016','初始化物业费','800201904008','/flow/propertyFeeConfigFlow',1,'500201904016','配置物业费','2019-06-01 04:56:45','0'),('700201906017','添加车位','800201906010','/flow/parkingSpaceFlow',1,'500201906017','添加车位','2019-06-01 04:56:45','0'),('700201906018','车位出租','800201906010','/flow/hireParkingSpaceFlow',2,'500201906018','车位出租','2019-06-01 04:56:45','0'),('700201906019','车位出售','800201906010','/flow/sellOwnerCarFlow',3,'500201906019','车位出售','2019-06-01 04:56:45','0'),('700201906020','业主车位','800201904005','/flow/ownerParkingSpaceFlow',4,'500201906020','业主车位','2019-06-01 04:56:45','0');

/*Table structure for table `m_menu_group` */

DROP TABLE IF EXISTS `m_menu_group`;

CREATE TABLE `m_menu_group` (
  `g_id` varchar(12) NOT NULL COMMENT '菜单组ID',
  `name` varchar(10) NOT NULL COMMENT '菜单组名称',
  `icon` varchar(20) NOT NULL COMMENT '菜单图片',
  `label` varchar(20) NOT NULL COMMENT '菜单标签',
  `seq` int(11) NOT NULL COMMENT '列顺序',
  `description` varchar(200) DEFAULT NULL COMMENT '菜单描述',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `status_cd` varchar(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考c_status表，0在用，1失效',
  UNIQUE KEY `g_id` (`g_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `m_menu_group` */

insert  into `m_menu_group`(`g_id`,`name`,`icon`,`label`,`seq`,`description`,`create_time`,`status_cd`) values ('800201904001','员工管理','fa-desktop','',8,'员工管理','2019-04-01 02:17:37','0'),('800201904002','权限管理','fa fa-diamond','HOT',9,'权限管理','2019-04-01 07:55:22','0'),('800201904003','物业管理','fa fa-globe','',1,'物业管理','2019-04-01 07:55:51','0'),('800201904004','小区管理','fa fa-laptop','',2,'小区管理','2019-04-09 14:33:19','0'),('800201904005','业主管理','fa-flask','',4,'业主管理','2019-04-09 14:34:12','0'),('800201904006','楼宇管理','fa fa-th-large','',3,'楼宇管理','2019-04-09 14:35:07','0'),('800201904007','公告管理','fa fa-edit','',7,'公告管理','2019-04-09 14:35:50','0'),('800201904008','缴费管理','fa fa-files-o','',6,'缴费管理','2019-04-09 14:36:41','0'),('800201904009','报表管理','fa fa-bar-chart-o','',10,'报表管理','2019-04-09 14:37:25','0'),('800201906010','车位管理','fa fa-globe','',5,'车位管理','2019-04-01 07:55:51','0');

/*Table structure for table `owner_car` */

DROP TABLE IF EXISTS `owner_car`;

CREATE TABLE `owner_car` (
  `car_id` varchar(30) NOT NULL COMMENT '汽车ID',
  `owner_id` varchar(30) NOT NULL COMMENT '业主ID',
  `b_id` varchar(30) NOT NULL COMMENT '业务Id',
  `car_num` varchar(12) NOT NULL COMMENT '车牌号',
  `car_brand` varchar(50) NOT NULL COMMENT '汽车品牌',
  `car_type` varchar(4) NOT NULL COMMENT '9901 家用小汽车，9902 客车，9903 货车',
  `car_color` varchar(12) NOT NULL COMMENT '颜色',
  `ps_id` varchar(30) NOT NULL COMMENT '车位ID',
  `user_id` varchar(30) NOT NULL COMMENT '用户ID',
  `remark` varchar(200) NOT NULL COMMENT '备注',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `status_cd` varchar(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考c_status表，0, 在用 1失效',
  UNIQUE KEY `car_id` (`car_id`),
  KEY `idx_oc_car_id` (`car_id`),
  KEY `idx_oc_b_id` (`b_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `owner_car` */

insert  into `owner_car`(`car_id`,`owner_id`,`b_id`,`car_num`,`car_brand`,`car_type`,`car_color`,`ps_id`,`user_id`,`remark`,`create_time`,`status_cd`) values ('802019060818600001','772019051824170001','202019060905050002','青AGK916','传祺GS4','9901','白色','792019060544610001','30518940136629616640','测试','2019-06-08 05:55:08','1'),('802019060957150002','772019051824170001','202019060915600015','青AGK918','东风雷诺','9901','红色','792019060544610001','30518940136629616640','测试','2019-06-08 16:57:30','0'),('802019060996090001','772019051824170001','202019060905130011','青AGK917','大众CC','9901','白色','792019060544610001','30518940136629616640','测试','2019-06-08 16:54:03','1'),('802019061038310002','772019051932200003','202019061070810006','青ACC888','宝马X6','9901','白色','792019061076110002','30518940136629616640','','2019-06-09 16:11:04','1'),('802019061088450001','772019052630240001','202019061029490002','青CBB666','奥迪A8','9901','黑色','792019060695730005','30518940136629616640','','2019-06-09 16:08:22','1');

/*Table structure for table `p_parking_space` */

DROP TABLE IF EXISTS `p_parking_space`;

CREATE TABLE `p_parking_space` (
  `ps_id` varchar(30) NOT NULL COMMENT '车位ID',
  `b_id` varchar(30) NOT NULL COMMENT '业务Id',
  `community_id` varchar(30) NOT NULL COMMENT '小区ID',
  `num` varchar(12) NOT NULL COMMENT '车位编号',
  `type_cd` varchar(4) NOT NULL COMMENT '车位类型,地上停车位1001 地下停车位 2001',
  `state` varchar(4) NOT NULL COMMENT '车位状态 出售 S，出租 H ，空闲 F',
  `area` decimal(7,2) NOT NULL COMMENT '车位面积',
  `user_id` varchar(30) NOT NULL COMMENT '用户ID',
  `remark` varchar(300) NOT NULL COMMENT '用户ID',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `status_cd` varchar(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考c_status表，S 保存，0, 在用 1失效',
  UNIQUE KEY `ps_id` (`ps_id`),
  KEY `idx_ps_ps_id` (`ps_id`),
  KEY `idx_ps_b_id` (`b_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `p_parking_space` */

insert  into `p_parking_space`(`ps_id`,`b_id`,`community_id`,`num`,`type_cd`,`state`,`area`,`user_id`,`remark`,`create_time`,`status_cd`) values ('792019060544610001','202019060913420016','7020181217000001','WB003','2001','S','3.88','30518940136629616640','测试','2019-06-04 16:46:43','0'),('792019060575090001','202019060508030008','7020181217000001','WB001','1001','F','12.90','30518940136629616640','','2019-06-04 16:25:54','1'),('792019060695730005','202019061066360003','7020181217000001','1232','1001','F','3.00','30518940136629616640','123','2019-06-06 10:18:49','0'),('792019061013090001','202019061017320007','7020181217000001','WB004','1001','F','12.08','30518940136629616640','','2019-06-09 16:09:19','0'),('792019061044360003','202019061090550011','7020181217000001','WB006','2001','F','12.36','30518940136629616640','','2019-06-09 16:10:00','0'),('792019061076110002','202019061086170007','7020181217000001','WB005','2001','F','12.07','30518940136629616640','','2019-06-09 16:09:43','0');

/*Table structure for table `p_privilege` */

DROP TABLE IF EXISTS `p_privilege`;

CREATE TABLE `p_privilege` (
  `p_id` varchar(12) NOT NULL COMMENT '权限ID',
  `name` varchar(10) NOT NULL COMMENT '权限名称',
  `description` varchar(200) DEFAULT NULL COMMENT '权限描述',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `status_cd` varchar(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考c_status表，0在用，1失效',
  `domain` varchar(12) NOT NULL DEFAULT '-1' COMMENT '权限域 商户详见store_type store_type_cd',
  UNIQUE KEY `p_id` (`p_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `p_privilege` */

insert  into `p_privilege`(`p_id`,`name`,`description`,`create_time`,`status_cd`,`domain`) values ('500201904001','员工信息','员工信息','2019-04-01 02:24:53','0','800900000003'),('500201904002','添加权限','添加权限','2019-04-01 08:00:52','0','800900000003'),('500201904003','删除权限','删除权限','2019-04-01 08:01:12','0','800900000003'),('500201904004','物业费','物业费','2019-04-08 14:46:28','0','800900000003'),('500201904005','停车费','停车费','2019-04-08 14:46:42','0','800900000003'),('500201904006','房屋管理','房屋管理','2019-04-08 14:46:54','0','800900000003'),('500201904007','我的报表','我的报表','2019-04-08 14:47:06','0','800900000003'),('500201904008','入住小区','入住小区','2019-04-09 15:01:30','0','800900000003'),('500201904009','发布公告','发布公告','2019-04-09 15:05:21','0','800900000003'),('500201904010','添加业主','添加业主','2019-04-09 15:07:15','0','800900000003'),('500201904011','小区楼','小区楼','2019-04-19 08:01:21','0','800900000003'),('500201904012','小区楼单元','小区楼单元','2019-04-19 08:01:36','0','800900000003'),('500201904013','业主成员','业主成员','2019-05-17 13:56:15','0','800900000003'),('500201904014','房屋售卖','房屋售卖','2019-05-20 07:00:33','0','800900000003'),('500201904015','业主房产','业主房产','2019-05-22 01:04:05','0','800900000003'),('500201904016','初始化物业费','初始化物业费','2019-06-01 04:53:56','0','800900000003'),('500201906017','添加车位','添加车位','2019-06-01 04:53:56','0','800900000003'),('500201906018','车位出租','车位出租','2019-06-01 04:53:56','0','800900000003'),('500201906019','车位出售','车位出售','2019-06-01 04:53:56','0','800900000003'),('500201906020','业主车位','业主车位','2019-05-22 01:04:05','0','800900000003');

/*Table structure for table `p_privilege_group` */

DROP TABLE IF EXISTS `p_privilege_group`;

CREATE TABLE `p_privilege_group` (
  `pg_id` varchar(30) NOT NULL COMMENT '权限组ID',
  `name` varchar(10) NOT NULL COMMENT '权限组名称',
  `description` varchar(200) DEFAULT NULL COMMENT '权限组描述',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `status_cd` varchar(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考c_status表，0在用，1失效',
  `store_id` varchar(30) NOT NULL DEFAULT '9999' COMMENT '商户ID',
  `domain` varchar(12) NOT NULL DEFAULT '-1' COMMENT '权限组域 商户详见store_type store_type_cd',
  UNIQUE KEY `pg_id` (`pg_id`),
  UNIQUE KEY `pg_id_2` (`pg_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `p_privilege_group` */

insert  into `p_privilege_group`(`pg_id`,`name`,`description`,`create_time`,`status_cd`,`store_id`,`domain`) values ('600201904001','物业员工默认组','物业员工默认组','2019-04-01 02:22:10','0','9999','800900000003'),('600201904002','物业管理员权限组','物业管理员权限组','2019-04-01 08:34:58','0','9999','800900000003'),('600201904003','代理商员工默认组','代理商员工默认组','2019-04-01 12:00:18','0','9999','800900000002'),('600201904004','代理商管理员权限组','代理商管理员权限组','2019-04-01 12:01:45','0','9999','800900000002'),('6002019040814840005','财务权限组','财务权限组财务权限组','2019-04-08 09:15:29','0','402019032924930007','800900000003'),('6002019040849930003','测试组2','测试组2测试组2','2019-04-08 09:09:35','1','402019032924930007','800900000003'),('6002019040860680004','测试3','测试3','2019-04-08 09:10:06','1','402019032924930007','800900000003'),('6002019040866210002','测试组','测试组测试组','2019-04-08 08:22:25','0','402019032924930007','800900000003'),('6002019040898030006','HR权限组','HR权限组','2019-04-08 09:18:12','0','402019032924930007','800900000003'),('6002019040927460001','保安权限组','保安权限组','2019-04-08 16:18:24','0','402019032924930007','800900000003'),('6002019040955920002','保洁权限组','保洁权限组','2019-04-08 16:18:38','0','402019032924930007','800900000003');

/*Table structure for table `p_privilege_rel` */

DROP TABLE IF EXISTS `p_privilege_rel`;

CREATE TABLE `p_privilege_rel` (
  `rel_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '权限关系ID',
  `p_id` varchar(12) NOT NULL COMMENT '权限ID',
  `pg_id` varchar(30) NOT NULL COMMENT '权限组ID',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `status_cd` varchar(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考c_status表，0在用，1失效',
  PRIMARY KEY (`rel_id`),
  UNIQUE KEY `rel_id` (`rel_id`)
) ENGINE=InnoDB AUTO_INCREMENT=36 DEFAULT CHARSET=utf8;

/*Data for the table `p_privilege_rel` */

insert  into `p_privilege_rel`(`rel_id`,`p_id`,`pg_id`,`create_time`,`status_cd`) values (1,'500201904001','600201904001','2019-04-01 08:18:29','0'),(2,'500201904002','600201904002','2019-04-01 08:18:35','0'),(3,'500201904003','600201904002','2019-04-01 08:18:59','0'),(4,'500201904004','600201904002','2019-04-08 16:11:43','0'),(5,'500201904006','6002019040814840005','2019-04-08 16:12:16','1'),(6,'500201904007','6002019040814840005','2019-04-08 16:16:26','1'),(7,'500201904001','6002019040866210002','2019-04-08 16:16:43','0'),(8,'500201904003','6002019040866210002','2019-04-08 16:16:45','0'),(9,'500201904006','6002019040898030006','2019-04-08 16:17:02','0'),(10,'500201904007','6002019040898030006','2019-04-08 16:17:05','0'),(11,'500201904004','6002019040955920002','2019-04-08 16:18:46','0'),(12,'500201904005','6002019040955920002','2019-04-08 16:18:49','0'),(13,'500201904006','6002019040955920002','2019-04-08 16:18:51','0'),(14,'500201904007','6002019040955920002','2019-04-08 16:18:54','0'),(15,'500201904007','6002019040814840005','2019-04-09 06:59:23','0'),(16,'500201904006','6002019040814840005','2019-04-09 07:21:31','0'),(17,'500201904005','600201904002','2019-04-09 14:52:32','0'),(18,'500201904006','600201904002','2019-04-09 14:56:50','0'),(19,'500201904007','600201904002','2019-04-09 14:58:41','0'),(20,'500201904008','600201904002','2019-04-09 15:02:28','0'),(21,'500201904009','600201904002','2019-04-09 15:06:12','0'),(22,'500201904010','600201904002','2019-04-09 15:08:03','0'),(23,'500201904007','6002019040927460001','2019-04-09 15:08:50','0'),(24,'500201904009','6002019040927460001','2019-04-11 08:16:28','0'),(25,'500201904009','6002019040898030006','2019-04-17 15:01:53','0'),(26,'500201904012','600201904002','2019-04-19 08:05:29','0'),(27,'500201904011','600201904002','2019-04-19 08:05:36','0'),(28,'500201904013','600201904002','2019-05-18 10:55:56','0'),(29,'500201904014','600201904002','2019-05-20 07:02:03','0'),(30,'500201904015','600201904002','2019-05-22 01:05:19','0'),(31,'500201904016','600201904002','2019-06-01 04:58:44','0'),(32,'500201906017','600201904002','2019-05-20 07:02:03','0'),(33,'500201906018','600201904002','2019-05-20 07:02:03','0'),(34,'500201906019','600201904002','2019-05-20 07:02:03','0'),(35,'500201906020','600201904002','2019-05-20 07:02:03','0');

/*Table structure for table `p_privilege_user` */

DROP TABLE IF EXISTS `p_privilege_user`;

CREATE TABLE `p_privilege_user` (
  `pu_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '权限用户ID',
  `p_id` varchar(30) NOT NULL COMMENT '权限标志 是 1是权限组 0是权限',
  `privilege_flag` varchar(4) NOT NULL DEFAULT '0' COMMENT '权限标志 是 1是权限组 0是权限',
  `user_id` varchar(30) NOT NULL COMMENT '用户ID',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `status_cd` varchar(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考c_status表，0在用，1失效',
  PRIMARY KEY (`pu_id`),
  UNIQUE KEY `pu_id` (`pu_id`)
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8;

/*Data for the table `p_privilege_user` */

insert  into `p_privilege_user`(`pu_id`,`p_id`,`privilege_flag`,`user_id`,`create_time`,`status_cd`) values (1,'500201904001','0','30518940136629616640','2019-04-01 02:27:45','0'),(2,'600201904002','1','30518940136629616640','2019-04-01 08:37:00','0'),(3,'600201904002','1','302019040270260001','2019-04-01 16:57:23','0'),(4,'600201904002','1','302019040265910001','2019-04-01 17:02:38','0'),(5,'600201904001','1','302019040246120002','2019-04-02 11:36:45','1'),(6,'600201904001','1','302019040317140001','2019-04-03 02:24:55','0'),(7,'600201904001','1','302019040366990002','2019-04-03 02:27:24','0'),(8,'600201904001','1','302019040367880001','2019-04-03 03:12:05','0'),(9,'600201904001','1','302019040356630001','2019-04-03 14:50:57','0'),(10,'600201904001','1','302019040332500002','2019-04-03 14:51:57','0'),(11,'600201904001','1','302019040343530003','2019-04-03 14:52:54','1'),(12,'600201904001','1','302019040356300004','2019-04-03 14:53:47','0'),(13,'600201904001','1','302019040355650005','2019-04-03 14:54:59','0'),(14,'600201904001','1','302019040339750006','2019-04-03 14:55:54','0'),(15,'600201904001','1','302019040422250001','2019-04-04 08:40:58','0'),(16,'600201904001','1','302019040481060001','2019-04-04 14:24:58','1'),(17,'6002019040898030006','1','302019040332500002','2019-04-13 07:21:45','0'),(18,'500201904001','0','302019040332500002','2019-04-13 07:22:14','1'),(19,'500201904007','0','302019040343530003','2019-04-13 07:23:19','1'),(20,'500201904009','0','302019040332500002','2019-04-13 17:23:45','0'),(21,'6002019040955920002','1','302019040356300004','2019-04-14 11:04:23','0'),(22,'6002019040927460001','1','302019040356300004','2019-04-17 15:01:37','0'),(23,'600201904001','1','302019041824320001','2019-04-18 02:20:04','0'),(24,'600201904001','1','302019042192720001','2019-04-21 10:04:02','0'),(25,'6002019040898030006','1','302019040356630001','2019-04-25 13:19:52','0'),(26,'600201904001','1','302019042846260005','2019-04-28 06:59:57','0'),(27,'600201904001','1','302019052133990001','2019-05-21 07:49:49','1'),(28,'6002019040814840005','1','302019040332500002','2019-06-04 05:21:32','0'),(29,'6002019040866210002','1','302019040332500002','2019-06-04 05:21:38','0'),(30,'6002019040927460001','1','302019040332500002','2019-06-04 05:21:44','0'),(31,'500201904001','0','302019040332500002','2019-06-04 05:21:48','0'),(32,'600201904001','1','302019060653180001','2019-06-06 05:08:57','0');

/*Table structure for table `p_property` */

DROP TABLE IF EXISTS `p_property`;

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

/*Data for the table `p_property` */

insert  into `p_property`(`property_id`,`b_id`,`name`,`address`,`tel`,`nearby_landmarks`,`map_x`,`map_y`,`create_time`,`status_cd`) values ('9020181218000001','10234567894','方博家园123','青海省西宁市城中区129号123','17797173952','王府井旁40米','102.801909','37.597263','2018-12-18 15:01:22','0');

/*Table structure for table `p_property_attr` */

DROP TABLE IF EXISTS `p_property_attr`;

CREATE TABLE `p_property_attr` (
  `b_id` varchar(30) NOT NULL COMMENT '订单ID',
  `attr_id` varchar(30) NOT NULL COMMENT '属性id',
  `property_id` varchar(30) NOT NULL COMMENT '用户ID',
  `spec_cd` varchar(12) NOT NULL COMMENT '规格id,参考spec表',
  `value` varchar(50) NOT NULL COMMENT '属性值',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `status_cd` varchar(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考c_status表，S 保存，0, 在用 1失效',
  UNIQUE KEY `attr_id` (`attr_id`),
  KEY `idx_property_attr_b_id` (`b_id`),
  KEY `idx_property_attr_property_id` (`property_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `p_property_attr` */

/*Table structure for table `p_property_cerdentials` */

DROP TABLE IF EXISTS `p_property_cerdentials`;

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

/*Data for the table `p_property_cerdentials` */

insert  into `p_property_cerdentials`(`property_cerdentials_id`,`b_id`,`property_id`,`credentials_cd`,`value`,`validity_period`,`positive_photo`,`negative_photo`,`create_time`,`status_cd`) values ('9220181218000002','16234567894','9020181218000001','1','632126XXXXXXXX2012','3000-02-01','1234567.jpg','11.jpg','2018-12-18 15:32:38','1');

/*Table structure for table `p_property_fee` */

DROP TABLE IF EXISTS `p_property_fee`;

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

/*Data for the table `p_property_fee` */

insert  into `p_property_fee`(`fee_id`,`b_id`,`property_id`,`fee_type_cd`,`fee_money`,`fee_time`,`create_time`,`start_time`,`end_time`,`status_cd`) values ('9420181221000001','13234567894','9020181218000001','T','10','0.5','2018-12-20 16:15:29','2018-05-01 00:00:00','2020-11-30 00:00:00','0');

/*Table structure for table `p_property_house` */

DROP TABLE IF EXISTS `p_property_house`;

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

/*Data for the table `p_property_house` */

insert  into `p_property_house`(`b_id`,`house_id`,`property_id`,`house_num`,`house_name`,`house_phone`,`house_area`,`fee_type_cd`,`fee_price`,`create_time`,`status_cd`) values ('6234567894','9520181222000001','123213','T','10',NULL,'01918','111','10.09','2018-12-21 17:02:03','0'),('17234567894','9520181222000005','123213','123123','吴XX住宅','17797173943','01919','112','11.09','2018-12-21 17:08:34','1');

/*Table structure for table `p_property_house_attr` */

DROP TABLE IF EXISTS `p_property_house_attr`;

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

/*Data for the table `p_property_house_attr` */

insert  into `p_property_house_attr`(`b_id`,`attr_id`,`house_id`,`spec_cd`,`VALUE`,`create_time`,`status_cd`) values ('6234567894','112018122200000002','9520181222000001','1001','01','2018-12-21 17:02:03','0'),('17234567894','112018122200000006','9520181222000005','1001','02','2018-12-21 17:08:34','1');

/*Table structure for table `p_property_photo` */

DROP TABLE IF EXISTS `p_property_photo`;

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

/*Data for the table `p_property_photo` */

insert  into `p_property_photo`(`property_photo_id`,`b_id`,`property_id`,`property_photo_type_cd`,`photo`,`create_time`,`status_cd`) values ('12320181218000001','15234567894','9020181218000001','T','123456789.jpg','2018-12-18 15:23:15','1');

/*Table structure for table `p_property_user` */

DROP TABLE IF EXISTS `p_property_user`;

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

/*Data for the table `p_property_user` */

insert  into `p_property_user`(`property_user_id`,`property_id`,`b_id`,`user_id`,`rel_cd`,`create_time`,`status_cd`) values ('9320181219000001','9020181218000001','18234567894','123','600311000001','2018-12-18 17:30:46','1'),('9320181219000002','9020181218000001','9020181218000001','30518939884421922816','600311000002','2019-02-13 15:19:45','0');

/*Table structure for table `pay_fee` */

DROP TABLE IF EXISTS `pay_fee`;

CREATE TABLE `pay_fee` (
  `fee_id` varchar(30) NOT NULL COMMENT '费用ID',
  `b_id` varchar(30) NOT NULL COMMENT '业务Id',
  `fee_type_cd` varchar(12) NOT NULL COMMENT '费用类型，物业费，停车费',
  `community_id` varchar(30) NOT NULL COMMENT '小区ID',
  `payer_obj_id` varchar(30) NOT NULL COMMENT '付款方ID',
  `income_obj_id` varchar(30) NOT NULL COMMENT '收款方ID',
  `start_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '开始时间',
  `end_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '结束时间',
  `amount` decimal(7,2) NOT NULL DEFAULT '-1.00' COMMENT '总金额，如物业费，停车费等没有总金额的，填写为-1.00',
  `user_id` varchar(30) NOT NULL COMMENT '创建用户ID',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `status_cd` varchar(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考c_status表，S 保存，0, 在用 1失效',
  UNIQUE KEY `fee_id` (`fee_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `pay_fee` */

insert  into `pay_fee`(`fee_id`,`b_id`,`fee_type_cd`,`community_id`,`payer_obj_id`,`income_obj_id`,`start_time`,`end_time`,`amount`,`user_id`,`create_time`,`status_cd`) values ('902019060201520001','202019060546810010','888800010001','7020181217000001','752019050860300001','402019032924930007','2019-06-02 13:22:43','2021-02-02 13:22:43','-1.00','30518940136629616640','2019-06-02 05:22:44','0'),('902019060295320001','202019060206150003','888800010001','7020181217000001','752019050790250003','402019032924930007','2019-06-02 12:48:05','2019-06-02 12:48:05','-1.00','30518940136629616640','2019-06-02 04:48:12','1'),('902019060447390002','202019060408020009','888800010001','7020181217000001','752019050896140002','402019032924930007','2019-06-04 14:57:58','2019-06-04 14:57:58','-1.00','30518940136629616640','2019-06-04 06:57:59','0'),('902019060882350001','202019060969220004','888800010003','7020181217000001','792019060544610001','402019032924930007','2019-06-08 13:55:04','2038-01-01 00:00:00','-1.00','30518940136629616640','2019-06-08 05:55:09','1'),('902019060918930002','202019060956040017','888800010003','7020181217000001','792019060544610001','402019032924930007','2019-06-09 00:57:29','2038-01-01 00:00:00','-1.00','30518940136629616640','2019-06-08 16:57:30','0'),('902019060939110001','202019060943070013','888800010003','7020181217000001','792019060544610001','402019032924930007','2019-06-09 00:54:02','2038-01-01 00:00:00','-1.00','30518940136629616640','2019-06-08 16:54:03','1'),('902019061018030001','202019061089200004','888800010004','7020181217000001','792019060695730005','402019032924930007','2019-06-10 00:08:17','2019-06-10 00:08:17','-1.00','30518940136629616640','2019-06-09 16:08:23','1'),('902019061077390002','202019061062540008','888800010003','7020181217000001','792019061076110002','402019032924930007','2019-06-10 00:11:03','2038-01-01 00:00:00','99999.99','30518940136629616640','2019-06-09 16:11:04','1');

/*Table structure for table `pay_fee_attrs` */

DROP TABLE IF EXISTS `pay_fee_attrs`;

CREATE TABLE `pay_fee_attrs` (
  `fee_id` varchar(30) NOT NULL COMMENT '费用ID',
  `b_id` varchar(30) NOT NULL COMMENT '业务Id',
  `community_id` varchar(30) NOT NULL COMMENT '小区ID',
  `attr_id` varchar(30) NOT NULL COMMENT '属性id',
  `spec_cd` varchar(12) NOT NULL COMMENT '规格id,参考spec表',
  `value` varchar(50) NOT NULL COMMENT '属性值',
  `status_cd` varchar(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考c_status表，S 保存，0, 在用 1失效',
  UNIQUE KEY `attr_id` (`attr_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `pay_fee_attrs` */

/*Table structure for table `pay_fee_config` */

DROP TABLE IF EXISTS `pay_fee_config`;

CREATE TABLE `pay_fee_config` (
  `config_id` varchar(30) NOT NULL COMMENT '费用ID',
  `b_id` varchar(30) NOT NULL COMMENT '业务Id',
  `community_id` varchar(30) NOT NULL COMMENT '小区ID',
  `fee_type_cd` varchar(12) NOT NULL COMMENT '费用类型，物业费，停车费',
  `square_price` decimal(7,2) NOT NULL COMMENT '每平米收取的单价',
  `additional_amount` decimal(7,2) NOT NULL COMMENT '附加费用',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `status_cd` varchar(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考c_status表，S 保存，0, 在用 1失效',
  UNIQUE KEY `config_id` (`config_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `pay_fee_config` */

insert  into `pay_fee_config`(`config_id`,`b_id`,`community_id`,`fee_type_cd`,`square_price`,`additional_amount`,`create_time`,`status_cd`) values ('922019060280800001','202019060235590006','7020181217000001','888800010001','3.17','201.00','2019-06-01 16:11:46','0'),('922019060280800002','202019060235590007','7020181217000001','888800010002','0.00','79999.99','2019-06-01 16:11:46','0'),('922019060280800003','202019060235590008','7020181217000001','888800010003','0.00','99999.99','2019-06-01 16:11:46','0'),('922019060280800004','202019060235590009','7020181217000001','888800010004','0.00','80.00','2019-06-01 16:11:46','0'),('922019060280800005','202019060235590010','7020181217000001','888800010005','0.00','200.00','2019-06-01 16:11:46','0');

/*Table structure for table `pay_fee_detail` */

DROP TABLE IF EXISTS `pay_fee_detail`;

CREATE TABLE `pay_fee_detail` (
  `detail_id` varchar(30) NOT NULL COMMENT '费用明细ID',
  `b_id` varchar(30) NOT NULL COMMENT '业务Id',
  `fee_id` varchar(30) NOT NULL COMMENT '费用ID',
  `community_id` varchar(30) NOT NULL COMMENT '小区ID',
  `cycles` int(11) NOT NULL COMMENT '周期，以月为单位',
  `receivable_amount` decimal(7,2) NOT NULL COMMENT '应收金额',
  `received_amount` decimal(7,2) NOT NULL COMMENT '实收金额',
  `prime_rate` decimal(3,2) NOT NULL COMMENT '打折率',
  `remark` varchar(200) NOT NULL COMMENT '备注',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `status_cd` varchar(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考c_status表，S 保存，0, 在用 1失效',
  UNIQUE KEY `detail_id` (`detail_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `pay_fee_detail` */

insert  into `pay_fee_detail`(`detail_id`,`b_id`,`fee_id`,`community_id`,`cycles`,`receivable_amount`,`received_amount`,`prime_rate`,`remark`,`create_time`,`status_cd`) values ('912019060325760001','202019060386250002','902019060201520001','7020181217000001',3,'1790.70','1790.70','1.00','测试','2019-06-03 08:06:01','0'),('912019060382700001','202019060360120002','902019060201520001','7020181217000001',1,'596.90','596.90','1.00','测试','2019-06-03 13:22:55','0'),('912019060427550001','202019060487860002','902019060201520001','7020181217000001',3,'1790.70','1790.70','1.00','','2019-06-03 17:48:08','0'),('912019060459310003','202019060473410011','902019060201520001','7020181217000001',1,'596.90','596.90','1.00','测试','2019-06-04 07:00:44','0'),('912019060558580001','202019060597950009','902019060201520001','7020181217000001',12,'7162.82','7162.82','1.00','','2019-06-05 13:48:56','0'),('912019060862910001','202019060899970005','902019060882350001','7020181217000001',1,'99999.99','99999.99','1.00','测试','2019-06-08 05:55:09','0'),('912019060935270002','202019060971230018','902019060918930002','7020181217000001',1,'99999.99','99999.99','1.00','测试','2019-06-08 16:57:30','0'),('912019060954310001','202019060995750009','902019060939110001','7020181217000001',1,'99999.99','99999.99','1.00','测试','2019-06-08 16:54:03','0'),('912019061029450002','202019061027520016','902019061077390002','7020181217000001',1,'99999.99','99999.99','1.00','','2019-06-09 16:11:04','0'),('912019061086730001','202019061002600005','902019061018030001','7020181217000001',1,'80.00','79999.99','1.00','','2019-06-09 16:08:24','0');

/*Table structure for table `property_fee_time` */

DROP TABLE IF EXISTS `property_fee_time`;

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

/*Data for the table `property_fee_time` */

/*Table structure for table `property_fee_type` */

DROP TABLE IF EXISTS `property_fee_type`;

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

/*Data for the table `property_fee_type` */

/*Table structure for table `property_user_rel` */

DROP TABLE IF EXISTS `property_user_rel`;

CREATE TABLE `property_user_rel` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `rel_cd` varchar(12) NOT NULL COMMENT '物业用户关系编码',
  `name` varchar(50) NOT NULL COMMENT '物业用户关系编码名称',
  `description` varchar(200) DEFAULT NULL COMMENT '描述',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `rel_cd` (`rel_cd`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

/*Data for the table `property_user_rel` */

insert  into `property_user_rel`(`id`,`rel_cd`,`name`,`description`,`create_time`) values (1,'600311000001','一般关系','一般关系','2018-12-18 17:11:19'),(2,'600311000002','员工关系','员工关系','2019-02-13 12:12:12');

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

/*Table structure for table `s_community` */

DROP TABLE IF EXISTS `s_community`;

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

/*Data for the table `s_community` */

insert  into `s_community`(`community_id`,`b_id`,`name`,`address`,`nearby_landmarks`,`city_code`,`map_x`,`map_y`,`create_time`,`status_cd`) values ('7020181217000001','1234567891','万博家博园（城西区）','青海省西宁市城中区129号','王府井旁30米','100010','101.801909','36.597263','2018-12-17 05:00:55','0'),('7020181217000002','234466','格兰小镇(西宁)','青海省西宁市城中区申宁路6','安馨雅苑北100米','8630100','102.909090','','2019-04-15 14:45:02','0'),('702019051621670004','82234567891','万博家博园（城V区）','青海省西宁市城中区129号','王府井旁30米','100010','101.801909','36.597263','2019-05-16 02:11:12','0'),('702019051635110010','92234567892','万博家博园（城V区）','青海省西宁市城中区129号','王府井旁30米','100010','101.801909','36.597263','2019-05-16 02:18:42','0'),('702019051651830001','92234567898','万博家博园（城H区）','青海省西宁市城中区129号','王府井旁30米','100010','101.801909','36.597263','2019-05-16 02:51:05','0'),('702019051657250007','92234567891','万博家博园（城V区）','青海省西宁市城中区129号','王府井旁30米','100010','101.801909','36.597263','2019-05-16 02:12:17','0'),('702019051666020001','92234567895','万博家博园（城V区）','青海省西宁市城中区129号','王府井旁30米','100010','101.801909','36.597263','2019-05-16 02:34:53','0'),('702019051696930001','92234567897','万博家博园（城V区）','青海省西宁市城中区129号','王府井旁30米','100010','101.801909','36.597263','2019-05-16 02:43:35','0');

/*Table structure for table `s_community_attr` */

DROP TABLE IF EXISTS `s_community_attr`;

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

/*Data for the table `s_community_attr` */

insert  into `s_community_attr`(`b_id`,`attr_id`,`community_id`,`spec_cd`,`VALUE`,`create_time`,`status_cd`) values ('1234567891','112018121700000002','7020181217000001','1001','01','2018-12-17 05:00:56','0'),('12323','112019041500000003','7020181217000001','100201904006','17797173942','2019-04-15 15:25:13','0'),('234234','112019041500000004','7020181217000002','100201904006','15897089471','2019-04-15 15:26:17','0'),('82234567891','112019051609710005','702019051621670004','1001','01','2019-05-16 02:11:12','0'),('92234567898','112019051651830002','702019051651830001','1001','01','2019-05-16 02:51:06','0'),('92234567891','112019051685870008','702019051657250007','1001','01','2019-05-16 02:12:17','0');

/*Table structure for table `s_community_member` */

DROP TABLE IF EXISTS `s_community_member`;

CREATE TABLE `s_community_member` (
  `community_member_id` varchar(30) NOT NULL COMMENT 'ID',
  `b_id` varchar(30) NOT NULL COMMENT '业务Id',
  `community_id` varchar(30) NOT NULL COMMENT '小区ID',
  `member_id` varchar(50) NOT NULL COMMENT '成员ID',
  `member_type_cd` varchar(12) NOT NULL COMMENT '成员类型见 community_member_type表',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `status_cd` varchar(4) NOT NULL,
  `audit_status_cd` varchar(4) NOT NULL DEFAULT '0000' COMMENT '审核状态',
  UNIQUE KEY `community_member_id` (`community_member_id`),
  KEY `idx_s_community_member_id` (`community_id`),
  KEY `idx_s_community_member_b_id` (`b_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `s_community_member` */

insert  into `s_community_member`(`community_member_id`,`b_id`,`community_id`,`member_id`,`member_type_cd`,`create_time`,`status_cd`,`audit_status_cd`) values ('7220181217000001','202019041850040006','7020181217000001','402019032924930007','390001200002','2018-12-17 14:42:31','0','0000'),('7220181217000004','1234567894','7020181217000001','3456789','390001200001','2018-12-17 14:46:20','1','0000'),('722019041887470002','202019041891220004','7020181217000002','402019032924930007','390001200002','2019-04-18 07:09:21','1','1000'),('722019041887470003','202019041891220005','7020181217000001','732019042181450002','390001200004','2019-04-24 14:16:40','0','0000'),('722019041887470004','202019041891220006','7020181217000001','732019042188750002','390001200004','2019-04-24 16:54:19','0','0000'),('722019041887470005','202019041891220007','7020181217000001','732019042218110002','390001200004','2019-04-24 16:54:35','0','0000'),('722019042613970001','202019042677430003','7020181217000001','732019042644990001','390001200004','2019-04-26 09:53:42','0','0000'),('722019042615080005','202019042641830015','7020181217000001','732019042657550005','390001200004','2019-04-26 08:59:41','1000','0000'),('722019042619710003','202019042649120009','7020181217000001','732019042653880003','390001200004','2019-04-26 09:22:09','1000','0000'),('722019042626080001','202019042640630003','7020181217000001','732019042607010001','390001200004','2019-04-26 09:14:11','1000','0000'),('722019042647500001','202019042632530003','7020181217000001','732019042665380001','390001200004','2019-04-26 08:34:30','1000','0000'),('722019042658320003','202019042644750009','7020181217000001','732019042622070003','390001200004','2019-04-26 08:45:15','1000','0000'),('722019042667870002','202019042653890006','7020181217000001','732019042653170002','390001200004','2019-04-26 09:14:59','1000','0000'),('722019042683690002','202019042646640006','7020181217000001','732019042604640002','390001200004','2019-04-26 08:37:11','1000','0000'),('722019042695080004','202019042644760012','7020181217000001','732019042656310004','390001200004','2019-04-26 08:57:16','1000','0000'),('722019042699390004','202019042655970012','7020181217000001','732019042687750004','390001200004','2019-04-26 09:24:55','0','0000'),('722019042708930002','202019042776600004','7020181217000002','402019032924930007','390001200002','2019-04-26 16:59:32','1','1000'),('722019042735900003','202019042785490009','7020181217000001','732019042715940003','390001200004','2019-04-26 17:38:57','0','0000'),('722019042737600001','202019042750320003','7020181217000001','732019042773170001','390001200004','2019-04-26 17:30:56','0','0000'),('722019042774370002','202019042735920006','7020181217000001','732019042790640002','390001200004','2019-04-26 17:37:14','0','0000'),('722019042782690001','202019042723970002','7020181217000002','402019032924930007','390001200002','2019-04-26 16:59:00','1','1000'),('722019042785690004','202019042717160014','7020181217000001','732019042784130004','390001200004','2019-04-27 11:56:56','0','0000'),('722019042788240003','202019042799620006','7020181217000002','402019032924930007','390001200002','2019-04-26 17:05:01','0','1000'),('722019042851120001','202019042988310003','7020181217000001','732019042893370001','390001200004','2019-04-28 09:53:54','1001','0000'),('722019042921800006','202019042987920018','7020181217000001','732019042925840006','390001200004','2019-04-29 09:22:55','0','0000'),('722019042922660002','202019042921120006','7020181217000001','732019042921980002','390001200004','2019-04-29 09:21:47','0','0000'),('722019042933000001','202019042995080003','7020181217000001','732019042904050001','390001200004','2019-04-29 11:47:36','0','0000'),('722019042933990005','202019042915470015','7020181217000001','732019042939210005','390001200004','2019-04-29 09:22:43','0','0000'),('722019042940530008','202019042936100024','7020181217000001','732019042907570008','390001200004','2019-04-29 09:23:40','0','0000'),('722019042953950001','202019042971000003','7020181217000001','732019042908600001','390001200004','2019-04-29 09:14:52','0','0000'),('722019042956360001','202019042912540011','7020181217000001','732019042937930001','390001200004','2019-04-28 17:19:24','1001','0000'),('722019042960050007','202019042948530021','7020181217000001','732019042954400007','390001200004','2019-04-29 09:23:08','0','0000'),('722019042974160002','202019042970510006','7020181217000001','732019042937750002','390001200004','2019-04-29 11:48:06','0','0000'),('722019042983420003','202019042979290009','7020181217000001','732019042986310003','390001200004','2019-04-29 09:21:59','0','0000'),('722019042992290004','202019042945820012','7020181217000001','732019042905340004','390001200004','2019-04-29 09:22:28','0','0000'),('722019051741410001','202019051829600003','7020181217000001','772019051712520001','390001200005','2019-05-17 14:11:36','1001','0000'),('722019051755950001','202019052655040009','7020181217000001','772019051730670001','390001200005','2019-05-17 14:29:39','1001','0000'),('722019051800120001','202019051846110003','7020181217000001','772019051853860001','390001200005','2019-05-18 08:11:18','0','0000'),('722019051820930003','202019051861450009','7020181217000001','772019051856610003','390001200005','2019-05-18 08:19:18','0','0000'),('722019051835910001','202019051893170022','7020181217000001','772019051807130001','390001200005','2019-05-17 16:49:05','1001','0000'),('722019051872430001','202019051961460009','7020181217000001','772019051860030001','390001200005','2019-05-18 08:32:16','1001','0000'),('722019051878390002','202019051814090006','7020181217000001','772019051883280002','390001200005','2019-05-18 08:12:10','0','0000'),('722019051888830001','202019051885250013','7020181217000001','772019051824170001','390001200005','2019-05-18 10:43:21','0','0000'),('722019051975520001','202019051959440007','7020181217000001','772019051932200003','390001200005','2019-05-19 00:44:19','0','0000'),('722019052372380001','202019052366960007','7020181217000001','772019052337170001','390001200005','2019-05-23 06:53:34','0','0000'),('722019052619090003','202019052650230016','702019051666020001','402019032924930007','390001200002','2019-05-26 12:50:57','0','1000'),('722019052693600002','202019052624900014','7020181217000001','772019052630240001','390001200005','2019-05-26 12:50:33','0','0000'),('722019052942510007','202019052990380028','702019051696930001','402019032924930007','390001200002','2019-05-29 09:16:36','0','1000'),('722019060636810002','202019060621530004','702019051651830001','402019032924930007','390001200002','2019-06-05 16:39:00','0','1000'),('722019060639010004','202019060639000008','702019051657250007','402019032924930007','390001200002','2019-06-05 16:39:14','0','1000');

/*Table structure for table `s_community_photo` */

DROP TABLE IF EXISTS `s_community_photo`;

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

/*Data for the table `s_community_photo` */

insert  into `s_community_photo`(`community_photo_id`,`b_id`,`community_id`,`community_photo_type_cd`,`photo`,`create_time`,`status_cd`) values ('7120181217000003','1234567891','7020181217000001','T','12345678.jpg','2018-12-17 05:00:56','0'),('712019051618080003','92234567898','702019051651830001','T','12345678.jpg','2019-05-16 02:51:07','0'),('712019051635190006','82234567891','702019051621670004','T','12345678.jpg','2019-05-16 02:11:13','0'),('712019051659510009','92234567891','702019051657250007','T','12345678.jpg','2019-05-16 02:12:17','0'),('712019051693130003','92234567897','702019051696930001','T','12345678.jpg','2019-05-16 02:43:36','0');

/*Table structure for table `s_member_store` */

DROP TABLE IF EXISTS `s_member_store`;

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

/*Data for the table `s_member_store` */

insert  into `s_member_store`(`member_store_id`,`b_id`,`store_id`,`member_id`,`create_time`,`status_cd`) values ('900001','900001','123456','100001','2018-12-12 14:01:52','0');

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

/*Data for the table `s_store` */

insert  into `s_store`(`store_id`,`b_id`,`user_id`,`name`,`address`,`tel`,`store_type_cd`,`nearby_landmarks`,`map_x`,`map_y`,`create_time`,`status_cd`) values ('100001','100001','30521125069510950912','小文小区代理公司','青海省西宁市城东区昆仑东路100号','13109785273','800900000002','城东区政府斜对面','','','2018-12-12 13:47:34','0'),('123456','123456','30521125069510950912','安鑫雅苑','青海省西宁市城中区神宁路28号','13109785273','800900000000','城东区政府斜对面','','','2018-12-12 13:37:52','0'),('402019032924930007','202019032924200010','30518940136629616640','小吴工作室','城南格兰小镇','17797173942','800900000003','青海','','','2019-03-29 14:21:41','0'),('402019032973010004','202019032981850008','123','齐天超时（王府井店）','青海省西宁市城中区129号','15897089471','M','王府井内','101.801909','36.597263','2019-03-29 14:13:26','0'),('402019033005920002','202019033072790004','123','齐天超时（王府井店）','青海省西宁市城中区129号','15897089471','M','王府井内','101.801909','36.597263','2019-03-30 05:32:16','0'),('402019033011430001','202019033046130002','30518940136629616640','齐天超时（王府井店）','青海省西宁市城中区129号','15897089471','M','王府井内','101.801909','36.597263','2019-03-30 08:04:39','0'),('402019033051590001','202019033063640004','123','齐天超时（王府井店）','青海省西宁市城中区129号','15897089471','M','王府井内','101.801909','36.597263','2019-03-30 05:56:23','0'),('402019033060910002','202019033011280002','30518940136629616640','齐天超时（王府井店）','青海省西宁市城中区129号','15897089471','M','王府井内','101.801909','36.597263','2019-03-30 07:47:24','0'),('402019033064560001','202019033033220002','123','齐天超时（王府井店）','青海省西宁市城中区129号','15897089471','M','王府井内','101.801909','36.597263','2019-03-30 06:51:24','0'),('402019033089200001','202019033072520002','30518940136629616640','齐天超时（王府井店）','青海省西宁市城中区129号','15897089471','M','王府井内','101.801909','36.597263','2019-03-30 08:31:29','0'),('402019033091620005','202019033076390007','302019033054910001','996icu公司','青海省西宁市城中区','17797173952','800900000004','中心广场','','','2019-03-30 08:37:31','0'),('402019040244100017','202019040281460010','302019040270260001','东方国信科技公司','青海省西宁市','18909798989','800900000003','青海移动公司','','','2019-04-01 16:48:29','0'),('402019040287010001','202019040234930002','302019040270260001','东方国信科技公司','青海省西宁市','18909798989','800900000003','青海移动公司','','','2019-04-01 16:57:22','0'),('402019040288380001','202019040289110004','302019040270260001','东方国信科技公司','青海省西宁市','18909798989','800900000003','青海移动公司','','','2019-04-01 16:43:01','0'),('402019040289200009','202019040231680007','302019040270260001','东方国信科技公司','青海省西宁市','18909798989','800900000003','青海移动公司','','','2019-04-01 16:45:06','0'),('402019040290830009','202019040243080007','302019040265910001','中心奥蛋','青海西宁市','17797173990','800900000003','青海','','','2019-04-01 17:02:38','0'),('402019041322840005','202019041327040005','30518940136629616640','齐天超时1（王府井店）','青海省西宁市城中区129号','15897089471','800900000003','王府井内','101.801909','36.597263','2019-04-13 15:12:27','0'),('402019041335090001','202019041317610002','30518940136629616640','齐天超时1（王府井店）','青海省西宁市城中区129号','15897089471','M','王府井内','101.801909','36.597263','2019-04-13 15:11:13','0'),('402019042800610081','202019042850210049','300019042192720001','xxx','xxx','17133858872','800900000001','xxx','','','2019-04-28 02:09:10','0'),('402019042817270113','202019042807020064','300019042192720001','xxx','xxx','17133858872','800900000001','xxx','','','2019-04-28 02:09:12','0'),('402019042817430126','202019042852920061','300019042192720001','xxx','xxx','17133858872','800900000001','xxx','','','2019-04-28 02:09:13','0'),('402019042825880088','202019042870660046','300019042192720001','xxx','xxx','17133858872','800900000001','xxx','','','2019-04-28 02:09:10','0'),('402019042831390033','202019042890620028','300019042192720001','xxx','xxx','17133858872','800900000001','xxx','','','2019-04-28 02:09:07','0'),('402019042832220017','202019042810810022','300019042192720001','xxx','xxx','17133858872','800900000001','xxx','','','2019-04-28 02:09:06','0'),('402019042844630001','202019042862250016','300019042192720001','xxx','xxx','17133858872','800900000001','xxx','','','2019-04-28 02:08:55','0'),('402019042859260049','202019042803330038','300019042192720001','xxx','xxx','17133858872','800900000001','xxx','','','2019-04-28 02:09:08','0'),('402019042861350065','202019042897110043','300019042192720001','xxx','xxx','17133858872','800900000001','xxx','','','2019-04-28 02:09:09','0'),('402019042869020136','202019042829820067','300019042192720001','xxx','xxx','17133858872','800900000001','xxx','','','2019-04-28 02:09:13','0'),('402019042871510025','202019042809150025','300019042192720001','xxx','xxx','17133858872','800900000001','xxx','','','2019-04-28 02:09:06','0'),('402019042873090105','202019042876120055','300019042192720001','xxx','xxx','17133858872','800900000001','xxx','','','2019-04-28 02:09:11','0'),('402019042877090067','202019042832720040','300019042192720001','xxx','xxx','17133858872','800900000001','xxx','','','2019-04-28 02:09:09','0'),('402019042890260121','202019042819410058','300019042192720001','xxx','xxx','17133858872','800900000001','xxx','','','2019-04-28 02:09:13','0'),('402019042891860097','202019042897550052','300019042192720001','xxx','xxx','17133858872','800900000001','xxx','','','2019-04-28 02:09:11','0'),('402019042892500040','202019042857010031','300019042192720001','xxx','xxx','17133858872','800900000001','xxx','','','2019-04-28 02:09:07','0'),('402019042892600053','202019042888600034','300019042192720001','xxx','xxx','17133858872','800900000001','xxx','','','2019-04-28 02:09:08','0'),('402019042896260009','202019042864170019','300019042192720001','xxx','xxx','17133858872','800900000001','xxx','','','2019-04-28 02:09:06','0');

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

insert  into `s_store_attr`(`b_id`,`attr_id`,`store_id`,`spec_cd`,`VALUE`,`create_time`,`status_cd`) values ('202019032981850008','112019032904350005','402019032973010004','1001','01','2019-03-29 14:13:26','0'),('202019032924200010','112019032930420009','402019032924930007','100201903002','55','2019-03-29 14:21:41','0'),('202019032924200010','112019032945350008','402019032924930007','100201903001','吴学文','2019-03-29 14:21:41','0'),('202019032924200010','112019032976770011','402019032924930007','100201903004','城中区派出所','2019-03-29 14:21:41','0'),('202019032924200010','112019032981460012','402019032924930007','100201903005','城南新区','2019-03-29 14:21:41','0'),('202019032924200010','112019032984510010','402019032924930007','100201903003','2019-09-31','2019-03-29 14:21:41','0'),('202019033076390007','112019033009530009','402019033091620005','100201903004','城中区法院','2019-03-30 08:37:31','0'),('202019033033220002','112019033011270002','402019033064560001','1001','01','2019-03-30 06:51:24','0'),('202019033076390007','112019033015820010','402019033091620005','100201903005','城中区','2019-03-30 08:37:31','0'),('202019033072520002','112019033028520002','402019033089200001','1001','01','2019-03-30 08:31:29','0'),('202019033072790004','112019033029610003','402019033005920002','1001','01','2019-03-30 05:32:16','0'),('202019033063640004','112019033030100002','402019033051590001','1001','01','2019-03-30 05:56:23','0'),('202019033011280002','112019033048910003','402019033060910002','1001','01','2019-03-30 07:47:24','0'),('202019033076390007','112019033049210008','402019033091620005','100201903003','2019-09-31','2019-03-30 08:37:31','0'),('202019033076390007','112019033074130006','402019033091620005','100201903001','吴少华','2019-03-30 08:37:31','0'),('202019033046130002','112019033074490002','402019033011430001','1001','01','2019-03-30 08:04:39','0'),('202019033076390007','112019033074710007','402019033091620005','100201903002','55','2019-03-30 08:37:31','0'),('202019040231680007','112019040202100014','402019040289200009','100201903005','软件类','2019-04-01 16:45:06','0'),('202019040281460010','112019040203120019','402019040244100017','100201903002','78','2019-04-01 16:48:29','0'),('202019040281460010','112019040203560018','402019040244100017','100201903001','张永强','2019-04-01 16:48:29','0'),('202019040243080007','112019040212430013','402019040290830009','100201903004','青海女子监狱','2019-04-01 17:02:38','0'),('202019040231680007','112019040216240012','402019040289200009','100201903003','2013-08-09','2019-04-01 16:45:06','0'),('202019040234930002','112019040217860006','402019040287010001','100201903005','软件类','2019-04-01 16:57:22','0'),('202019040231680007','112019040218590011','402019040289200009','100201903002','78','2019-04-01 16:45:06','0'),('202019040289110004','112019040223030003','402019040288380001','100201903002','78','2019-04-01 16:43:01','0'),('202019040281460010','112019040227250021','402019040244100017','100201903004','青海省法院','2019-04-01 16:48:29','0'),('202019040289110004','112019040233330006','402019040288380001','100201903005','软件类','2019-04-01 16:43:01','0'),('202019040289110004','112019040238070002','402019040288380001','100201903001','张永强','2019-04-01 16:43:01','0'),('202019040243080007','112019040238800011','402019040290830009','100201903002','2','2019-04-01 17:02:38','0'),('202019040234930002','112019040239380003','402019040287010001','100201903002','78','2019-04-01 16:57:22','0'),('202019040243080007','112019040241670012','402019040290830009','100201903003','2018-12-12','2019-04-01 17:02:38','0'),('202019040231680007','112019040242550010','402019040289200009','100201903001','张永强','2019-04-01 16:45:06','0'),('202019040231680007','112019040247160013','402019040289200009','100201903004','青海省法院','2019-04-01 16:45:06','0'),('202019040281460010','112019040256540020','402019040244100017','100201903003','2013-08-09','2019-04-01 16:48:29','0'),('202019040281460010','112019040261050022','402019040244100017','100201903005','软件类','2019-04-01 16:48:29','0'),('202019040234930002','112019040267790002','402019040287010001','100201903001','张永强','2019-04-01 16:57:22','0'),('202019040243080007','112019040270810014','402019040290830009','100201903005','经济开发区','2019-04-01 17:02:38','0'),('202019040234930002','112019040275090005','402019040287010001','100201903004','青海省法院','2019-04-01 16:57:22','0'),('202019040289110004','112019040284060005','402019040288380001','100201903004','青海省法院','2019-04-01 16:43:01','0'),('202019040234930002','112019040284120004','402019040287010001','100201903003','2013-08-09','2019-04-01 16:57:22','0'),('202019040243080007','112019040295150010','402019040290830009','100201903001','张啤酒','2019-04-01 17:02:38','0'),('202019040289110004','112019040295210004','402019040288380001','100201903003','2013-08-09','2019-04-01 16:43:01','0'),('202019041317610002','112019041378600002','402019041335090001','1001','01','2019-04-13 15:11:13','0'),('202019041327040005','112019041394890006','402019041322840005','1001','01','2019-04-13 15:12:27','0'),('202019042807020064','112019042800880114','402019042817270113','100201903001','xxx','2019-04-28 02:09:12','0'),('202019042897110043','112019042802080068','402019042861350065','100201903002','1000000','2019-04-28 02:09:09','0'),('202019042819410058','112019042802340125','402019042890260121','100201903004','xxx','2019-04-28 02:09:13','0'),('202019042850210049','112019042803300084','402019042800610081','100201903003','2019-04-28','2019-04-28 02:09:10','0'),('202019042890620028','112019042803750034','402019042831390033','100201903001','xxx','2019-04-28 02:09:07','0'),('202019042832720040','112019042807720075','402019042877090067','100201903004','xxx','2019-04-28 02:09:09','0'),('202019042829820067','112019042808070137','402019042869020136','100201903001','xxx','2019-04-28 02:09:13','0'),('202019042864170019','112019042808460014','402019042896260009','100201903005','xxx','2019-04-28 02:09:06','0'),('202019042803330038','112019042809950054','402019042859260049','100201903004','xxx','2019-04-28 02:09:08','0'),('202019042803330038','112019042809960052','402019042859260049','100201903003','2019-04-28','2019-04-28 02:09:08','0'),('202019042810810022','112019042811050020','402019042832220017','100201903003','2019-04-28','2019-04-28 02:09:06','0'),('202019042857010031','112019042811630043','402019042892500040','100201903003','2019-04-28','2019-04-28 02:09:07','0'),('202019042888600034','112019042812360057','402019042892600053','100201903002','1000000','2019-04-28 02:09:08','0'),('202019042810810022','112019042812790019','402019042832220017','100201903002','1000000','2019-04-28 02:09:06','0'),('202019042832720040','112019042816480073','402019042877090067','100201903003','2019-04-28','2019-04-28 02:09:09','0'),('202019042803330038','112019042816690056','402019042859260049','100201903005','xxx','2019-04-28 02:09:08','0'),('202019042870660046','112019042817230093','402019042825880088','100201903005','xxx','2019-04-28 02:09:10','0'),('202019042862250016','112019042818430002','402019042844630001','100201903001','xxx','2019-04-28 02:08:55','0'),('202019042890620028','112019042820130035','402019042831390033','100201903002','1000000','2019-04-28 02:09:07','0'),('202019042852920061','112019042821250133','402019042817430126','100201903005','xxx','2019-04-28 02:09:13','0'),('202019042809150025','112019042821350027','402019042871510025','100201903002','1000000','2019-04-28 02:09:07','0'),('202019042850210049','112019042821740086','402019042800610081','100201903005','xxx','2019-04-28 02:09:10','0'),('202019042897550052','112019042823110102','402019042891860097','100201903005','xxx','2019-04-28 02:09:11','0'),('202019042819410058','112019042827120127','402019042890260121','100201903005','xxx','2019-04-28 02:09:13','0'),('202019042897550052','112019042827550101','402019042891860097','100201903004','xxx','2019-04-28 02:09:11','0'),('202019042888600034','112019042828220059','402019042892600053','100201903003','2019-04-28','2019-04-28 02:09:08','0'),('202019042809150025','112019042829780028','402019042871510025','100201903003','2019-04-28','2019-04-28 02:09:07','0'),('202019042862250016','112019042833220004','402019042844630001','100201903003','2019-04-28','2019-04-28 02:08:55','0'),('202019042864170019','112019042833560013','402019042896260009','100201903004','xxx','2019-04-28 02:09:06','0'),('202019042864170019','112019042834630012','402019042896260009','100201903003','2019-04-28','2019-04-28 02:09:06','0'),('202019042876120055','112019042834970106','402019042873090105','100201903001','xxx','2019-04-28 02:09:11','0'),('202019042862250016','112019042835070006','402019042844630001','100201903005','xxx','2019-04-28 02:08:55','0'),('202019042876120055','112019042836850107','402019042873090105','100201903002','1000000','2019-04-28 02:09:11','0'),('202019042809150025','112019042839020030','402019042871510025','100201903005','xxx','2019-04-28 02:09:07','0'),('202019042897110043','112019042839980074','402019042861350065','100201903005','xxx','2019-04-28 02:09:09','0'),('202019042809150025','112019042840460026','402019042871510025','100201903001','xxx','2019-04-28 02:09:07','0'),('202019042803330038','112019042840820050','402019042859260049','100201903001','xxx','2019-04-28 02:09:08','0'),('202019042850210049','112019042842270082','402019042800610081','100201903001','xxx','2019-04-28 02:09:10','0'),('202019042852920061','112019042842980130','402019042817430126','100201903002','1000000','2019-04-28 02:09:13','0'),('202019042890620028','112019042847670037','402019042831390033','100201903004','xxx','2019-04-28 02:09:07','0'),('202019042807020064','112019042847750116','402019042817270113','100201903003','2019-04-28','2019-04-28 02:09:12','0'),('202019042870660046','112019042847940090','402019042825880088','100201903002','1000000','2019-04-28 02:09:10','0'),('202019042876120055','112019042848110108','402019042873090105','100201903003','2019-04-28','2019-04-28 02:09:11','0'),('202019042832720040','112019042849850071','402019042877090067','100201903002','1000000','2019-04-28 02:09:09','0'),('202019042807020064','112019042850250115','402019042817270113','100201903002','1000000','2019-04-28 02:09:12','0'),('202019042862250016','112019042850280003','402019042844630001','100201903002','1000000','2019-04-28 02:08:55','0'),('202019042864170019','112019042850310011','402019042896260009','100201903002','1000000','2019-04-28 02:09:06','0'),('202019042803330038','112019042852670051','402019042859260049','100201903002','1000000','2019-04-28 02:09:08','0'),('202019042897110043','112019042854370066','402019042861350065','100201903001','xxx','2019-04-28 02:09:09','0'),('202019042829820067','112019042854450141','402019042869020136','100201903005','xxx','2019-04-28 02:09:13','0'),('202019042852920061','112019042855700131','402019042817430126','100201903003','2019-04-28','2019-04-28 02:09:13','0'),('202019042819410058','112019042855890124','402019042890260121','100201903003','2019-04-28','2019-04-28 02:09:13','0'),('202019042810810022','112019042856300021','402019042832220017','100201903004','xxx','2019-04-28 02:09:06','0'),('202019042857010031','112019042857760044','402019042892500040','100201903004','xxx','2019-04-28 02:09:07','0'),('202019042832720040','112019042858360069','402019042877090067','100201903001','xxx','2019-04-28 02:09:09','0'),('202019042888600034','112019042858600061','402019042892600053','100201903005','xxx','2019-04-28 02:09:08','0'),('202019042829820067','112019042860250139','402019042869020136','100201903003','2019-04-28','2019-04-28 02:09:13','0'),('202019042888600034','112019042860770055','402019042892600053','100201903001','xxx','2019-04-28 02:09:08','0'),('202019042862250016','112019042863400005','402019042844630001','100201903004','xxx','2019-04-28 02:08:55','0'),('202019042819410058','112019042863870123','402019042890260121','100201903002','1000000','2019-04-28 02:09:13','0'),('202019042870660046','112019042864700091','402019042825880088','100201903003','2019-04-28','2019-04-28 02:09:10','0'),('202019042857010031','112019042864830045','402019042892500040','100201903005','xxx','2019-04-28 02:09:07','0'),('202019042897110043','112019042866510070','402019042861350065','100201903003','2019-04-28','2019-04-28 02:09:09','0'),('202019042852920061','112019042866850132','402019042817430126','100201903004','xxx','2019-04-28 02:09:13','0'),('202019042870660046','112019042868080092','402019042825880088','100201903004','xxx','2019-04-28 02:09:10','0'),('202019042876120055','112019042868410110','402019042873090105','100201903005','xxx','2019-04-28 02:09:11','0'),('202019042897550052','112019042868420100','402019042891860097','100201903003','2019-04-28','2019-04-28 02:09:11','0'),('202019042897550052','112019042868520098','402019042891860097','100201903001','xxx','2019-04-28 02:09:11','0'),('202019042857010031','112019042869250042','402019042892500040','100201903002','1000000','2019-04-28 02:09:07','0'),('202019042876120055','112019042872230109','402019042873090105','100201903004','xxx','2019-04-28 02:09:11','0'),('202019042810810022','112019042881220022','402019042832220017','100201903005','xxx','2019-04-28 02:09:06','0'),('202019042829820067','112019042883900138','402019042869020136','100201903002','1000000','2019-04-28 02:09:13','0'),('202019042897110043','112019042884780072','402019042861350065','100201903004','xxx','2019-04-28 02:09:09','0'),('202019042890620028','112019042885210038','402019042831390033','100201903005','xxx','2019-04-28 02:09:07','0'),('202019042832720040','112019042885450077','402019042877090067','100201903005','xxx','2019-04-28 02:09:09','0'),('202019042870660046','112019042886040089','402019042825880088','100201903001','xxx','2019-04-28 02:09:10','0'),('202019042857010031','112019042886390041','402019042892500040','100201903001','xxx','2019-04-28 02:09:07','0'),('202019042850210049','112019042886920085','402019042800610081','100201903004','xxx','2019-04-28 02:09:10','0'),('202019042852920061','112019042887550128','402019042817430126','100201903001','xxx','2019-04-28 02:09:13','0'),('202019042807020064','112019042889350118','402019042817270113','100201903005','xxx','2019-04-28 02:09:12','0'),('202019042809150025','112019042889780029','402019042871510025','100201903004','xxx','2019-04-28 02:09:07','0'),('202019042810810022','112019042890490018','402019042832220017','100201903001','xxx','2019-04-28 02:09:06','0'),('202019042897550052','112019042891650099','402019042891860097','100201903002','1000000','2019-04-28 02:09:11','0'),('202019042807020064','112019042892310117','402019042817270113','100201903004','xxx','2019-04-28 02:09:12','0'),('202019042850210049','112019042894130083','402019042800610081','100201903002','1000000','2019-04-28 02:09:10','0'),('202019042829820067','112019042894590140','402019042869020136','100201903004','xxx','2019-04-28 02:09:13','0'),('202019042864170019','112019042898920010','402019042896260009','100201903001','xxx','2019-04-28 02:09:06','0'),('202019042890620028','112019042899040036','402019042831390033','100201903003','2019-04-28','2019-04-28 02:09:07','0'),('202019042888600034','112019042899790060','402019042892600053','100201903004','xxx','2019-04-28 02:09:08','0'),('202019042819410058','112019042899900122','402019042890260121','100201903001','xxx','2019-04-28 02:09:13','0');

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

insert  into `s_store_cerdentials`(`store_cerdentials_id`,`b_id`,`store_id`,`credentials_cd`,`value`,`validity_period`,`positive_photo`,`negative_photo`,`create_time`,`status_cd`) values ('422019032937620006','202019032981850008','402019032973010004','300200900001','632126XXXXXXXX2011','3000-01-01','1234567.jpg','','2019-03-29 14:13:26','0'),('422019032958420013','202019032924200010','402019032924930007','300200900001','632126199109162011','2030-10-01','','','2019-03-29 14:21:41','0'),('422019033000830011','202019033076390007','402019033091620005','300200900001','632126199109162088','2030-02-09','','','2019-03-30 08:37:31','0'),('422019033004800003','202019033033220002','402019033064560001','300200900001','632126XXXXXXXX2011','3000-01-01','1234567.jpg','','2019-03-30 06:51:24','0'),('422019033031850003','202019033072520002','402019033089200001','300200900001','632126XXXXXXXX2011','3000-01-01','1234567.jpg','','2019-03-30 08:31:29','0'),('422019033049340003','202019033063640004','402019033051590001','300200900001','632126XXXXXXXX2011','3000-01-01','1234567.jpg','','2019-03-30 05:56:23','0'),('422019033073800004','202019033011280002','402019033060910002','300200900001','632126XXXXXXXX2011','3000-01-01','1234567.jpg','','2019-03-30 07:47:24','0'),('422019033088010003','202019033046130002','402019033011430001','300200900001','632126XXXXXXXX2011','3000-01-01','1234567.jpg','','2019-03-30 08:04:39','0'),('422019033094470004','202019033072790004','402019033005920002','300200900001','632126XXXXXXXX2011','3000-01-01','1234567.jpg','','2019-03-30 05:32:16','0'),('422019040215000015','202019040243080007','402019040290830009','300200900001','632126199809162033','2035-09-08','','','2019-04-01 17:02:38','0'),('422019040221330007','202019040289110004','402019040288380001','300200900001','632126199109162099','2019-09-09','','','2019-04-01 16:43:01','0'),('422019040233760023','202019040281460010','402019040244100017','300200900001','632126199109162099','2019-09-09','','','2019-04-01 16:48:29','0'),('422019040258080015','202019040231680007','402019040289200009','300200900001','632126199109162099','2019-09-09','','','2019-04-01 16:45:06','0'),('422019040286960007','202019040234930002','402019040287010001','300200900001','632126199109162099','2019-09-09','','','2019-04-01 16:57:22','0'),('422019041364480007','202019041327040005','402019041322840005','300200900001','632126XXXXXXXX2011','3000-01-01','1234567.jpg','','2019-04-13 15:12:27','0'),('422019041368290003','202019041317610002','402019041335090001','300200900001','632126XXXXXXXX2011','3000-01-01','1234567.jpg','','2019-04-13 15:11:13','0'),('422019042807680142','202019042829820067','402019042869020136','300200900001','xxx','2119-04-28','','','2019-04-28 02:09:13','0'),('422019042807860023','202019042810810022','402019042832220017','300200900001','xxx','2119-04-28','','','2019-04-28 02:09:06','0'),('422019042812100094','202019042870660046','402019042825880088','300200900001','xxx','2119-04-28','','','2019-04-28 02:09:10','0'),('422019042825760111','202019042876120055','402019042873090105','300200900001','xxx','2119-04-28','','','2019-04-28 02:09:11','0'),('422019042829710119','202019042807020064','402019042817270113','300200900001','xxx','2119-04-28','','','2019-04-28 02:09:12','0'),('422019042830520078','202019042832720040','402019042877090067','300200900001','xxx','2119-04-28','','','2019-04-28 02:09:09','0'),('422019042831960031','202019042809150025','402019042871510025','300200900001','xxx','2119-04-28','','','2019-04-28 02:09:07','0'),('422019042832350129','202019042819410058','402019042890260121','300200900001','xxx','2119-04-28','','','2019-04-28 02:09:13','0'),('422019042842490039','202019042890620028','402019042831390033','300200900001','xxx','2119-04-28','','','2019-04-28 02:09:07','0'),('422019042857470134','202019042852920061','402019042817430126','300200900001','xxx','2119-04-28','','','2019-04-28 02:09:13','0'),('422019042866290062','202019042888600034','402019042892600053','300200900001','xxx','2119-04-28','','','2019-04-28 02:09:08','0'),('422019042869300087','202019042850210049','402019042800610081','300200900001','xxx','2119-04-28','','','2019-04-28 02:09:10','0'),('422019042872800076','202019042897110043','402019042861350065','300200900001','xxx','2119-04-28','','','2019-04-28 02:09:09','0'),('422019042873500007','202019042862250016','402019042844630001','300200900001','xxx','2119-04-28','','','2019-04-28 02:08:55','0'),('422019042873850046','202019042857010031','402019042892500040','300200900001','xxx','2119-04-28','','','2019-04-28 02:09:07','0'),('422019042875860103','202019042897550052','402019042891860097','300200900001','xxx','2119-04-28','','','2019-04-28 02:09:11','0'),('422019042883320058','202019042803330038','402019042859260049','300200900001','xxx','2119-04-28','','','2019-04-28 02:09:08','0'),('422019042899240015','202019042864170019','402019042896260009','300200900001','xxx','2119-04-28','','','2019-04-28 02:09:06','0');

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

/*Table structure for table `s_store_user` */

DROP TABLE IF EXISTS `s_store_user`;

CREATE TABLE `s_store_user` (
  `store_user_id` varchar(30) NOT NULL COMMENT '代理商用户ID',
  `store_id` varchar(30) NOT NULL COMMENT '代理商ID',
  `b_id` varchar(30) NOT NULL COMMENT '业务Id',
  `user_id` varchar(30) NOT NULL COMMENT '用户ID',
  `rel_cd` varchar(30) NOT NULL COMMENT '用户和代理商关系 详情查看 agent_user_rel表',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `status_cd` varchar(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考c_status表，S 保存，0, 在用 1失效',
  UNIQUE KEY `store_user_id` (`store_user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `s_store_user` */

insert  into `s_store_user`(`store_user_id`,`store_id`,`b_id`,`user_id`,`rel_cd`,`create_time`,`status_cd`) values ('452019033038670004','402019032924930007','202019033081420003','30518940136629616640','600311000001','2019-03-30 08:31:29','0'),('452019033054120012','402019033091620005','202019033069920008','302019033054910001','600311000001','2019-03-30 08:37:31','0'),('452019040205330016','402019040289200009','202019040204720008','302019040270260001','600311000001','2019-04-01 16:45:06','0'),('452019040216260001','402019032924930007','202019040227860003','302019040246120002','600311000002','2019-04-02 11:36:45','0'),('452019040227320016','402019040290830009','202019040270570008','302019040265910001','600311000001','2019-04-01 17:02:38','0'),('452019040229440008','402019040287010001','202019040244340003','302019040270260001','600311000001','2019-04-01 16:57:22','0'),('452019040261310024','402019040244100017','202019040247210011','302019040270260001','600311000001','2019-04-01 16:48:29','0'),('452019040265580008','402019040288380001','202019040228000005','302019040270260001','600311000001','2019-04-01 16:43:01','0'),('452019040307490002','402019032924930007','202019040342840008','302019040332500002','600311000002','2019-04-03 14:51:57','0'),('452019040347590003','402019032924930007','202019040367530011','302019040343530003','600311000002','2019-04-03 14:52:54','0'),('452019040357990004','402019032924930007','202019040320970014','302019040356300004','600311000002','2019-04-03 14:53:47','0'),('452019040369300001','402019032924930007','202019040356450003','302019040317140001','600311000002','2019-04-03 02:24:55','1'),('452019040372570006','402019032924930007','202019040334540020','302019040339750006','600311000002','2019-04-03 14:55:54','0'),('452019040392660001','402019032924930007','202019040376180005','302019040356630001','600311000002','2019-04-03 14:50:57','0'),('452019040393370005','402019032924930007','202019040397350017','302019040355650005','600311000002','2019-04-03 14:54:59','0'),('452019040395580001','402019032924930007','202019040346960003','302019040367880001','600311000002','2019-04-03 03:12:05','1'),('452019040398910002','402019032924930007','202019040389340006','302019040366990002','600311000002','2019-04-03 02:27:23','0'),('452019040426520001','402019032924930007','202019040494260003','302019040481060001','600311000002','2019-04-04 14:24:58','0'),('452019040456490001','402019032924930007','202019040496010018','302019040422250001','600311000002','2019-04-04 08:40:57','0'),('452019041305490004','402019041335090001','202019041375730003','30518940136629616640','600311000001','2019-04-13 15:11:13','0'),('452019041336750008','402019041322840005','202019041318030006','30518940136629616640','600311000001','2019-04-13 15:12:27','0'),('452019041888240001','402019032924930007','202019041810750003','302019041824320001','600311000002','2019-04-18 02:20:04','0'),('452019042191050001','402019032924930007','202019042139970003','302019042192720001','600311000002','2019-04-21 10:04:02','0'),('452019042820940095','402019042825880088','202019042888030047','300019042192720001','600311000001','2019-04-28 02:09:10','0'),('452019042829530048','402019042892500040','202019042885450032','300019042192720001','600311000001','2019-04-28 02:09:07','0'),('452019042830310145','402019032924930007','202019042882310071','302019042846260005','600311000002','2019-04-28 06:59:56','0'),('452019042834270079','402019042877090067','202019042888960041','300019042192720001','600311000001','2019-04-28 02:09:09','0'),('452019042844860104','402019042891860097','202019042813420053','300019042192720001','600311000001','2019-04-28 02:09:12','0'),('452019042851520096','402019042800610081','202019042806640050','300019042192720001','600311000001','2019-04-28 02:09:10','0'),('452019042852570047','402019042831390033','202019042874650029','300019042192720001','600311000001','2019-04-28 02:09:07','0'),('452019042853400120','402019042817270113','202019042848480065','300019042192720001','600311000001','2019-04-28 02:09:12','0'),('452019042854350112','402019042873090105','202019042899920056','300019042192720001','600311000001','2019-04-28 02:09:11','0'),('452019042854370024','402019042832220017','202019042879890023','300019042192720001','600311000001','2019-04-28 02:09:06','0'),('452019042856970032','402019042871510025','202019042821020026','300019042192720001','600311000001','2019-04-28 02:09:07','0'),('452019042859590016','402019042896260009','202019042813460020','300019042192720001','600311000001','2019-04-28 02:09:06','0'),('452019042859780143','402019042869020136','202019042839280068','300019042192720001','600311000001','2019-04-28 02:09:13','0'),('452019042866050135','402019042890260121','202019042842350059','300019042192720001','600311000001','2019-04-28 02:09:13','0'),('452019042869240080','402019042861350065','202019042836610044','300019042192720001','600311000001','2019-04-28 02:09:09','0'),('452019042877420063','402019042859260049','202019042893500039','300019042192720001','600311000001','2019-04-28 02:09:08','0'),('452019042883290064','402019042892600053','202019042820240035','300019042192720001','600311000001','2019-04-28 02:09:09','0'),('452019042886910008','402019042844630001','202019042892790017','300019042192720001','600311000001','2019-04-28 02:08:55','0'),('452019042892920144','402019042817430126','202019042830960062','300019042192720001','600311000001','2019-04-28 02:09:13','0'),('452019052162080001','402019032924930007','202019052168890003','302019052133990001','600311000002','2019-05-21 07:49:49','0'),('452019060657890001','402019032924930007','202019060647320011','302019060653180001','600311000002','2019-06-06 05:08:56','0'),('901','123456','990','30518939884421922816','600311000001','2019-03-27 11:47:58','0');

/*Table structure for table `spec` */

DROP TABLE IF EXISTS `spec`;

CREATE TABLE `spec` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `domain` varchar(20) NOT NULL COMMENT '属性域',
  `spec_cd` varchar(12) DEFAULT NULL,
  `name` varchar(50) NOT NULL COMMENT '名称',
  `description` varchar(200) DEFAULT NULL COMMENT '描述',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `spec_cd` (`spec_cd`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;

/*Data for the table `spec` */

insert  into `spec`(`id`,`domain`,`spec_cd`,`name`,`description`,`create_time`) values (1,'ORDERS','1000','订单来源','订单来源','2018-11-14 13:28:44'),(2,'BUSINESS','2000','推荐UserID','推荐UserID','2018-11-14 13:28:44'),(3,'STORE_ATTR','100201903001','企业法人','企业法人','2019-03-29 13:24:36'),(4,'STORE_ATTR','100201903002','注册资本','注册资本','2019-03-29 13:26:00'),(5,'STORE_ATTR','100201903003','成立日期','成立日期','2019-03-29 13:26:21'),(6,'STORE_ATTR','100201903004','登记机关','登记机关','2019-03-29 13:26:39'),(7,'STORE_ATTR','100201903005','经营范围','经营范围','2019-03-29 13:26:56'),(8,'COMMUNITY_ATTR','100201904006','小区联系方式','小区联系方式','2019-04-15 15:22:56');

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
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;

/*Data for the table `store_type` */

insert  into `store_type`(`id`,`store_type_cd`,`name`,`description`,`create_time`) values (1,'800900000001','运营团队','运营团队','2018-12-12 12:09:02'),(2,'800900000002','代理商','代理商','2018-12-12 12:09:50'),(3,'800900000003','物业','物业','2018-12-12 12:27:11'),(4,'800900000004','物流公司','物流公司','2018-12-12 12:27:53'),(5,'800900000005','超市','超市','2018-12-12 12:28:23'),(6,'800900000006','餐厅','餐厅','2018-12-12 12:28:43'),(7,'800900000007','饭馆','饭馆','2018-12-12 12:29:17'),(8,'800900000008','药店','药店','2018-12-12 12:29:41');

/*Table structure for table `store_user_rel` */

DROP TABLE IF EXISTS `store_user_rel`;

CREATE TABLE `store_user_rel` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `rel_cd` varchar(12) NOT NULL COMMENT '代理商用户关系编码',
  `name` varchar(50) NOT NULL COMMENT '代理商用户关系编码名称',
  `description` varchar(200) DEFAULT NULL COMMENT '描述',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `rel_cd` (`rel_cd`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

/*Data for the table `store_user_rel` */

insert  into `store_user_rel`(`id`,`rel_cd`,`name`,`description`,`create_time`) values (1,'600311000001','管理员关系','管理员关系','2018-12-18 17:11:19'),(2,'600311000002','员工关系','员工关系','2019-02-13 12:12:12');

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
  `tel` varchar(11) NOT NULL,
  `level_cd` varchar(2) NOT NULL DEFAULT '0' COMMENT '用户级别,关联user_level',
  `b_id` varchar(30) NOT NULL COMMENT '业务Id',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `status_cd` varchar(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考c_status表，S 保存，0, 在用 1失效',
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_id` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1107 DEFAULT CHARSET=utf8;

/*Data for the table `u_user` */

insert  into `u_user`(`id`,`user_id`,`name`,`email`,`address`,`password`,`location_cd`,`age`,`sex`,`tel`,`level_cd`,`b_id`,`create_time`,`status_cd`) values (1,'30516332294873563136','李四','928255095@qq.com',NULL,'ERCBHDUYFJDNDHDJDNDJDHDUDHDJDDKDK',NULL,NULL,'0','17797173943','02','20516332294412189696','2018-11-25 11:20:12','0'),(2,'30516364590414577664','王五','928255095@qq.com',NULL,'ERCBHDUYFJDNDHDJDNDJDHDUDHDJDDKDK',NULL,NULL,'0','17797173943','02','20516364589907066880','2018-11-25 13:28:32','0'),(3,'30516365201923129344','王五1','928255095@qq.com',NULL,'ERCBHDUYFJDNDHDJDNDJDHDUDHDJDDKDK',NULL,NULL,'0','17797173943','02','20516365201587585024','2018-11-25 13:30:57','0'),(4,'30516366113219559424','王五2','928255095@qq.com',NULL,'ERCBHDUYFJDNDHDJDNDJDHDUDHDJDDKDK',NULL,NULL,'0','17797173943','02','20516366112951123968','2018-11-25 13:34:35','0'),(5,'30516366709171437568','王五3','928255095@qq.com',NULL,'ERCBHDUYFJDNDHDJDNDJDHDUDHDJDDKDK',NULL,NULL,'0','17797173943','02','20516366708928167936','2018-11-25 13:36:57','0'),(6,'30516368306509201408','王五4','928255095@qq.com',NULL,'ERCBHDUYFJDNDHDJDNDJDHDUDHDJDDKDK',NULL,NULL,'0','17797173943','02','20516368306257543168','2018-11-25 13:43:17','0'),(7,'30516374464544391168','王五5','928255095@qq.com',NULL,'ERCBHDUYFJDNDHDJDNDJDHDUDHDJDDKDK',NULL,NULL,'0','17797173943','02','20516374464208846848','2018-11-25 14:07:46','0'),(8,'30516374772855095296','王五6','928255095@qq.com',NULL,'ERCBHDUYFJDNDHDJDNDJDHDUDHDJDDKDK',NULL,NULL,'0','17797173943','02','20516374772574076928','2018-11-25 14:08:59','0'),(9,'30516374801938399232','王五7','928255095@qq.com',NULL,'ERCBHDUYFJDNDHDJDNDJDHDUDHDJDDKDK',NULL,NULL,'0','17797173943','02','20516374801669963776','2018-11-25 14:09:06','0'),(10,'30516377393741447168','王五7','928255095@qq.com',NULL,'ERCBHDUYFJDNDHDJDNDJDHDUDHDJDDKDK',NULL,NULL,'0','17797173943','02','20516377393317822464','2018-11-25 14:19:24','0'),(11,'30516381939989495808','王五8','928255095@qq.com',NULL,'ERCBHDUYFJDNDHDJDNDJDHDUDHDJDDKDK',NULL,NULL,'0','17797173943','02','20516381939620397056','2018-11-25 14:37:28','0'),(12,'30516387723230068736','王五9','928255095@qq.com',NULL,'ERCBHDUYFJDNDHDJDNDJDHDUDHDJDDKDK',NULL,NULL,'0','17797173943','02','20516387722659643392','2018-11-25 15:00:27','0'),(13,'30516389225004810240','王五10','928255095@qq.com',NULL,'ERCBHDUYFJDNDHDJDNDJDHDUDHDJDDKDK',NULL,NULL,'0','17797173943','02','20516389224736374784','2018-11-25 15:06:25','0'),(14,'30516389265349820416','王五11','928255095@qq.com',NULL,'ERCBHDUYFJDNDHDJDNDJDHDUDHDJDDKDK',NULL,NULL,'0','17797173943','02','20516389265110745088','2018-11-25 15:06:34','0'),(15,'30516398820397957120','吴学文','',NULL,'0724094fbcd70db0493034daa2120aa1',NULL,NULL,'0','15897089471','02','20516398820167270400','2018-11-25 15:44:33','0'),(16,'30516400356482105344','师师','',NULL,'f1ab59f367854c555a84d42155d39aa3',NULL,NULL,'0','18987095720','02','20516400356243030016','2018-11-25 15:50:39','0'),(17,'30516401274132905984','张试卷','',NULL,'cdfea6fbffcd82758eec12a96c64e682',NULL,NULL,'0','15897089471','02','20516401273893830656','2018-11-25 15:54:18','0'),(18,'30517072483300425728','张三','',NULL,'dc2263f093f3db5edd6ce5ea6aa81c32',NULL,NULL,'0','17797173947','02','20517072482935521280','2018-11-27 12:21:26','0'),(19,'30517086035897761792','吴学文123','',NULL,'c91cf5db90773073a558e8740521c01c',NULL,NULL,'0','17797173942','02','20517086035457359872','2018-11-27 13:15:18','0'),(20,'30517710166884368384','王五112','928255095@qq.com',NULL,'ERCBHDUYFJDNDHDJDNDJDHDUDHDJDDKDK',NULL,NULL,'0','17797173943','02','20517710159825354752','2018-11-29 06:35:22','0'),(21,'30518939884421922816','admin','',NULL,'3ad352384261cbc2c7462210dbb3ce61',NULL,NULL,'0','17797173944','02','20518939876956061696','2018-12-02 16:01:50','0'),(22,'30518940136629616640','wuxw','',NULL,'3ad352384261cbc2c7462210dbb3ce61',NULL,NULL,'0','17797173940','02','202019041340460005','2018-12-02 16:02:50','0'),(23,'30520751775595118592','zhangsan','',NULL,'b8ed29901921a8e7a8ec34d874a5379b',NULL,NULL,'0','17797173942','02','20520751770314489856','2018-12-07 16:01:38','0'),(24,'30520752094488051712','zhangsan','',NULL,'b8ed29901921a8e7a8ec34d874a5379b',NULL,NULL,'0','17797173942','02','20520752094244782080','2018-12-07 16:02:54','0'),(25,'30520752516195958784','zhangsan','',NULL,'b8ed29901921a8e7a8ec34d874a5379b',NULL,NULL,'0','17797173942','02','20520752515906551808','2018-12-07 16:04:35','0'),(26,'30520752654427635712','zhangsan','',NULL,'b8ed29901921a8e7a8ec34d874a5379b',NULL,NULL,'0','17797173942','02','20520752654096285696','2018-12-07 16:05:07','0'),(27,'30520753111732600832','lisi','',NULL,'f8b789267d0ea8414dd9dea3f88d2e66',NULL,NULL,'0','17797173942','02','20520753111019569152','2018-12-07 16:06:56','0'),(28,'30521002824033648640','admin','',NULL,'3ad352384261cbc2c7462210dbb3ce61',NULL,NULL,'0','15963245875','00','20521002816139968512','2018-12-08 08:39:13','0'),(30,'30521299095785062400','wangym','928255095@qq.com',NULL,'cdfea6fbffcd82758eec12a96c64e682',NULL,NULL,'0','17797289806','01','20521692315283111936','2018-12-09 04:16:29','0'),(31,'30521384632806031360','liyongxi','928255095@qq.com',NULL,'cdfea6fbffcd82758eec12a96c64e682',NULL,NULL,'0','17797289806','01','202019021096440004','2018-12-09 09:56:23','0'),(32,'30521384655744679936','zhangshan','928255095@qq.com',NULL,'cdfea6fbffcd82758eec12a96c64e682',NULL,NULL,'0','17797289806','01','20521384655455272960','2018-12-09 09:56:28','0'),(33,'30521384680415576064','lisi','928255095@qq.com',NULL,'cdfea6fbffcd82758eec12a96c64e682',NULL,NULL,'0','17797289806','01','20521384680210055168','2018-12-09 09:56:34','0'),(34,'30521384773189386240','wangmazi','928255095@qq.com',NULL,'cdfea6fbffcd82758eec12a96c64e682',NULL,NULL,'0','17797289806','01','20521384772962893824','2018-12-09 09:56:56','0'),(35,'30521384859990507520','吴学文','928255095@qq.com',NULL,'cdfea6fbffcd82758eec12a96c64e682',NULL,NULL,'0','17797289806','01','20521384859759820800','2018-12-09 09:57:17','0'),(36,'30521388279069687808','吴学文1','928255095@qq.com',NULL,'cdfea6fbffcd82758eec12a96c64e682',NULL,NULL,'0','17797289806','01','20521388278868361216','2018-12-09 10:10:52','0'),(37,'30521388292680204288','吴学文2','928255095@qq.com',NULL,'cdfea6fbffcd82758eec12a96c64e682',NULL,NULL,'0','17797289806','01','20521388292466294784','2018-12-09 10:10:55','0'),(38,'30521388308752777216','吴学文3','928255095@qq.com',NULL,'cdfea6fbffcd82758eec12a96c64e682',NULL,NULL,'0','17797289806','01','202019020300000003','2018-12-09 10:10:59','1'),(39,'30521388322879193088','吴学文4','928255095@qq.com',NULL,'cdfea6fbffcd82758eec12a96c64e682',NULL,NULL,'0','17797289806','01','20521388322648506368','2018-12-09 10:11:03','0'),(40,'30521388335978004480','吴学文5','928255095@qq.com',NULL,'cdfea6fbffcd82758eec12a96c64e682',NULL,NULL,'0','17797289806','01','20521388335667625984','2018-12-09 10:11:06','0'),(41,'30521470367152226304','王永梅','406943871@qq.com','青海省西宁市城中区申宁路6号','cdfea6fbffcd82758eec12a96c64e682',NULL,NULL,'1','17797173943','01','20521470366867013632','2018-12-09 15:37:04','0'),(42,'30521470728940306432','张三丰','123456@qq.com','','cdfea6fbffcd82758eec12a96c64e682',NULL,NULL,'0','18987898111','01','20521470728747368448','2018-12-09 15:38:30','0'),(43,'30521470972990078976','李玉刚','0908777@qq.com','','cdfea6fbffcd82758eec12a96c64e682',NULL,NULL,'1','18987164563','01','20521470972776169472','2018-12-09 15:39:28','0'),(44,'30521471733694218240','张学良','9088@qq.com','东北老爷们山','cdfea6fbffcd82758eec12a96c64e682',NULL,NULL,'0','17797173945','01','20521471733417394176','2018-12-09 15:42:29','0'),(45,'30521472810921508864','qhadmin','',NULL,'cdfea6fbffcd82758eec12a96c64e682',NULL,NULL,'0','17797173940','00','20521472810674044928','2018-12-09 15:46:46','0'),(46,'30522549147283243008','张事假','928255095@qq.com','','cdfea6fbffcd82758eec12a96c64e682',NULL,NULL,'0','18997089471','01','20522549147056750592','2018-12-12 15:03:45','0'),(47,'30523565516132990976','jiejie','jiejie@qq.com','','cdfea6fbffcd82758eec12a96c64e682',NULL,NULL,'1','17797173948','01','20523565515797446656','2018-12-15 10:22:26','0'),(48,'3020190202000001','吴梓豪','928255094@qq.com','青海省','cdfea6fbffcd82758eec12a96c64e682',NULL,NULL,'0','17797173942','01','202019020200000003','2019-02-02 13:40:27','1'),(49,'3020190210000001','吴学文89','928255095@qq.com',NULL,'cdfea6fbffcd82758eec12a96c64e682',NULL,NULL,'0','17797289807','01','202019021000000009','2019-02-09 16:57:53','0'),(50,'3020190210000002','吴学文90','928255095@qq.com',NULL,'cdfea6fbffcd82758eec12a96c64e682',NULL,NULL,'0','17797289807','01','202019021070180002','2019-02-09 17:17:50','0'),(51,'3020190210000003','吴学文91','928255095@qq.com',NULL,'cdfea6fbffcd82758eec12a96c64e682',NULL,NULL,'0','17797289807','01','202019021058060004','2019-02-09 17:23:16','0'),(52,'302019021030610001','张时强','wuxw7@www.com','甘肃省金昌市','cdfea6fbffcd82758eec12a96c64e682',NULL,NULL,'0','18909789184','01','202019021059530006','2019-02-10 14:37:01','0'),(53,'302019021020990002','cc789','',NULL,'bd06b3bdf498d5fecd1f83964c925750',NULL,NULL,'0','18909871234','00','202019021067580008','2019-02-10 14:38:23','0'),(54,'302019032346330001','王五1123','928255095@qq.com',NULL,'ERCBHDUYFJDNDHDJDNDJDHDUDHDJDDKDK',NULL,NULL,'0','17797173948','00','202019032344890002','2019-03-23 08:59:29','0'),(55,'302019032517520003','zhangqq',NULL,NULL,'3b85f4fb07881869aae2fa7fb1994580',NULL,NULL,NULL,'18987095720','00','202019032587620006','2019-03-25 08:30:52','0'),(56,'302019032508330004','feitao',NULL,NULL,'515edae4c4a80249d6801a2015ccbd68',NULL,NULL,NULL,'17797173947','00','202019032531280008','2019-03-25 08:34:56','0'),(57,'302019032567920005','adminc',NULL,NULL,'90007ba81043076df2f241cebf95b18c',NULL,NULL,NULL,'17797173947','00','202019032517680010','2019-03-25 15:55:33','0'),(58,'302019033054910001','996icu',NULL,NULL,'e1b2ed71460529672e74f31a307bfe63',NULL,NULL,NULL,'18909780341','00','202019033083450005','2019-03-30 08:34:54','0'),(59,'302019040270260001','zhangx',NULL,NULL,'fe6eab9f2b73152fb8948c5446e553fd',NULL,NULL,NULL,'18909785209','00','202019040291880002','2019-04-01 16:40:49','0'),(60,'302019040265910001','zhangb',NULL,NULL,'360de47da6aff8bcace8fdbfc3c8076f',NULL,NULL,NULL,'17797173985','00','202019040287710005','2019-04-01 17:00:44','0'),(61,'302019040231400001','user1',NULL,NULL,'5b7ac7ba433dc0842fb8d3bc15e4b2aa',NULL,NULL,NULL,'18300862162','00','202019040270560002','2019-04-02 09:57:46','0'),(62,'302019040246120002','米虎子','928255095@qq.com','青海省西宁市','cdfea6fbffcd82758eec12a96c64e682',NULL,NULL,'0','18909783333','01','202019060621390016','2019-04-02 11:36:44','1'),(63,'302019040317140001','shishi1','9282550951@qq.com','西宁1','cdfea6fbffcd82758eec12a96c64e682',NULL,NULL,'1','17797173942','01','202019040445720015','2019-04-03 02:24:55','1'),(64,'302019040366990002','任建宇2','7898@qq.com','西宁','cdfea6fbffcd82758eec12a96c64e682',NULL,NULL,'0','17797173989','01','202019040458180024','2019-04-03 02:27:23','0'),(65,'302019040367880001','zhichao2','9878981@qq.com','西宁市1','5b7ac7ba433dc0842fb8d3bc15e4b2aa',NULL,NULL,'1','17797350002','01','202019040442220012','2019-04-03 03:12:05','1'),(66,'302019040356630001','王咏梅','406943871@qq.com','青海省西宁市','5b7ac7ba433dc0842fb8d3bc15e4b2aa',NULL,NULL,'1','17797154305','01','202019040306710004','2019-04-03 14:50:57','0'),(67,'302019040332500002','玉波','yubo@163.com','西双版纳','5b7ac7ba433dc0842fb8d3bc15e4b2aa',NULL,NULL,'1','17797173884','01','202019053129130037','2019-04-03 14:51:57','0'),(68,'302019040343530003','玉儿香','yuerxiang@163.com','西双版纳','5b7ac7ba433dc0842fb8d3bc15e4b2aa',NULL,NULL,'1','15597174306','01','202019053118750035','2019-04-03 14:52:54','1'),(69,'302019040356300004','布鲁漂','bulupiao@163.com','西双版纳','5b7ac7ba433dc0842fb8d3bc15e4b2aa',NULL,NULL,'0','18997096269','01','202019040333220013','2019-04-03 14:53:47','0'),(70,'302019040355650005','徐远达','xuyuanda@163.com','上海','5b7ac7ba433dc0842fb8d3bc15e4b2aa',NULL,NULL,'0','18909788448','01','202019040313040016','2019-04-03 14:54:59','0'),(71,'302019040339750006','徐浩宁','xuhaoning@163.com','上海','5b7ac7ba433dc0842fb8d3bc15e4b2aa',NULL,NULL,'0','18976544567','01','202019040399500019','2019-04-03 14:55:54','0'),(72,'302019040422250001','师延俊1','shiyj@163.com','城东区','5b7ac7ba433dc0842fb8d3bc15e4b2aa',NULL,NULL,'0','18907788888','01','202019040434790017','2019-04-04 08:40:57','0'),(73,'302019040481060001','罗比特1','luobite@163.com','西双版纳','5b7ac7ba433dc0842fb8d3bc15e4b2aa',NULL,NULL,'1','18909786789','01','202019060481650006','2019-04-04 14:24:57','1'),(74,'302019041110350001','zwff',NULL,NULL,'5b7ac7ba433dc0842fb8d3bc15e4b2aa',NULL,NULL,NULL,'15695200291','00','202019041120660002','2019-04-11 08:07:54','0'),(75,'302019041824320001','钟原','zongyuan@163.com','中国反恐特战队猎鹰组','5b7ac7ba433dc0842fb8d3bc15e4b2aa',NULL,NULL,'0','18909786666','01','202019041801200002','2019-04-18 02:20:04','0'),(76,'302019042192720001','毛彬彬','maobinbin@163.com','青海省西宁市城中区','5b7ac7ba433dc0842fb8d3bc15e4b2aa',NULL,NULL,'0','18909789999','01','202019042126440002','2019-04-21 10:04:02','0'),(77,'302019042771470001','qqq',NULL,NULL,'fbd7b2884b79aa4c9bc971911eaf31b7',NULL,NULL,NULL,'18909764563','00','202019042771400011','2019-04-27 10:43:09','0'),(1100,'300019042192720001','dyaoming','270307872@qq.com','河南省郑州市','3ad352384261cbc2c7462210dbb3ce61',NULL,NULL,'1','17133858872','00','202019042126440002','2019-04-28 01:00:00','0'),(1101,'302019042846260005','孙悟空','sunwk@163.com','花果山水莲洞','5b7ac7ba433dc0842fb8d3bc15e4b2aa',NULL,NULL,'0','18907889800','01','202019042887970070','2019-04-28 06:59:56','0'),(1102,'302019051126800001','hewei',NULL,NULL,'5b7ac7ba433dc0842fb8d3bc15e4b2aa',NULL,NULL,NULL,'15919163134','00','202019051147830012','2019-05-11 13:41:21','0'),(1103,'302019051126240002','tom-01',NULL,NULL,'5b7ac7ba433dc0842fb8d3bc15e4b2aa',NULL,NULL,NULL,'13000000001','00','202019051151120014','2019-05-11 13:48:20','0'),(1104,'302019051724240001','aaaa',NULL,NULL,'34e212112386af3ede23b9535eed589d',NULL,NULL,NULL,'13689050406','00','202019051774820002','2019-05-17 03:21:20','0'),(1105,'302019052133990001','董耀明','270307872@qq.com','asdfasdf','5b7ac7ba433dc0842fb8d3bc15e4b2aa',NULL,NULL,'0','18603928785','01','202019052157660006','2019-05-21 07:49:48','1'),(1106,'302019060653180001','米虎子','784240617@qq.com','弗兰省','5b7ac7ba433dc0842fb8d3bc15e4b2aa',NULL,NULL,'0','17623430978','01','202019060694370010','2019-06-06 05:08:56','0');

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
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

/*Data for the table `user_level` */

insert  into `user_level`(`id`,`level_cd`,`name`,`description`,`create_time`) values (1,'00','系统管理员','系统管理员','2018-11-14 13:28:59'),(3,'01','员工','员工','2019-02-13 09:03:10'),(4,'02','普通用户','普通用户','2019-02-13 09:04:25');

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;