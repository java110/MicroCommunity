DROP TABLE r_repair_pool;
CREATE TABLE r_repair_pool(
	repair_id VARCHAR(30) NOT NULL COMMENT '报修ID',
	`b_id` VARCHAR(30) NOT NULL COMMENT '业务Id',
	community_id VARCHAR(30) NOT NULL COMMENT '小区ID',
	repair_type VARCHAR(8) NOT NULL COMMENT '报修类型',
	repair_name VARCHAR(12) NOT NULL COMMENT '报修人姓名',
	tel VARCHAR(11) NOT NULL COMMENT '手机号',
	room_id VARCHAR(30) NOT NULL COMMENT '房屋ID',
	appointment_time TIMESTAMP NOT NULL COMMENT '预约时间',
	context LONGTEXT NOT NULL COMMENT '报修内容',
	`create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
	`state` VARCHAR(4) NOT NULL COMMENT '报修状态，请查看state 表',
	`status_cd` VARCHAR(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考c_status表，S 保存，0, 在用 1失效',
	UNIQUE KEY `repair_id` (`repair_id`)
);
DROP TABLE business_repair_pool;

CREATE TABLE `business_repair_pool` (
	repair_id VARCHAR(30) NOT NULL COMMENT '报修ID',
	`b_id` VARCHAR(30) NOT NULL COMMENT '业务Id',
	community_id VARCHAR(30) NOT NULL COMMENT '小区ID',
	repair_type VARCHAR(8) NOT NULL COMMENT '报修类型',
	repair_name VARCHAR(12) NOT NULL COMMENT '报修人姓名',
	tel VARCHAR(11) NOT NULL COMMENT '手机号',
	room_id VARCHAR(30) NOT NULL COMMENT '房屋ID',
	appointment_time TIMESTAMP NOT NULL COMMENT '预约时间',
	context LONGTEXT NOT NULL COMMENT '报修内容',
	`create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
	`state` VARCHAR(4) NOT NULL COMMENT '报修状态，请查看state 表',
	`operate` VARCHAR(4) NOT NULL COMMENT '数据状态，添加ADD，修改MOD 删除DEL'
);
DROP TABLE r_repair_user;

CREATE TABLE r_repair_user(
    ru_id VARCHAR(30) NOT NULL COMMENT '报修派单ID',
	repair_id VARCHAR(30) NOT NULL COMMENT '报修ID',
	`b_id` VARCHAR(30) NOT NULL COMMENT '业务Id',
	user_id VARCHAR(30) NOT NULL COMMENT '员工ID',
	community_id VARCHAR(30) NOT NULL COMMENT '小区ID',
	`create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
	`state` VARCHAR(4) NOT NULL COMMENT '员工处理状态，请查看state 表',
	context LONGTEXT NOT NULL COMMENT '报修内容',
	`status_cd` VARCHAR(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考c_status表，S 保存，0, 在用 1失效'
);
DROP TABLE business_repair_user;

CREATE TABLE `business_repair_user` (
    ru_id VARCHAR(30) NOT NULL COMMENT '报修派单ID',
	repair_id VARCHAR(30) NOT NULL COMMENT '报修ID',
	`b_id` VARCHAR(30) NOT NULL COMMENT '业务Id',
	user_id VARCHAR(30) NOT NULL COMMENT '员工ID',
	community_id VARCHAR(30) NOT NULL COMMENT '小区ID',
	`create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
	`state` VARCHAR(4) NOT NULL COMMENT '员工处理状态，请查看state 表',
	context LONGTEXT NOT NULL COMMENT '报修内容',
	`operate` VARCHAR(4) NOT NULL COMMENT '数据状态，添加ADD，修改MOD 删除DEL'
);