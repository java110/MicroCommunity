create table resource_store(
  `res_id` varchar(30) NOT NULL COMMENT '资源ID',
  `b_id` varchar(30) NOT NULL COMMENT '业务Id',
  `store_id` varchar(30) NOT NULL COMMENT '商户ID',
  `res_name` varchar(100) NOT NULL COMMENT '资源名称',
  `res_code` varchar(30) NOT NULL COMMENT '资源编码',
  `description` varchar(11) NOT NULL COMMENT '资源描述',
  `price` DECIMAL(9,2) NOT NULL COMMENT '资源单价',
  `stock` int(11) DEFAULT NULL COMMENT '库存数',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `status_cd` varchar(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考t_dict表，0, 在用 1失效',
  KEY `idx_res_res_id` (`res_id`)
);

CREATE TABLE `business_resource_store` (
  `res_id` varchar(30) NOT NULL COMMENT '资源ID',
  `b_id` varchar(30) NOT NULL COMMENT '业务Id',
  `store_id` varchar(30) NOT NULL COMMENT '商户ID',
  `res_name` varchar(100) NOT NULL COMMENT '资源名称',
  `res_code` varchar(30) NOT NULL COMMENT '资源编码',
  `description` varchar(11) NOT NULL COMMENT '资源描述',
  `price` DECIMAL(9,2) NOT NULL COMMENT '资源单价',
  `stock` int(11) DEFAULT NULL COMMENT '库存数',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `operate` varchar(4) NOT NULL COMMENT '数据状态，添加ADD，修改MOD 删除DEL',
  KEY `idx_b_res_res_id` (`res_id`)
);

-- 资源 出入库 记录订单
create table resource_order(
	res_order_id varchar(30) not null comment '订单ID',
	`b_id` varchar(30) NOT NULL COMMENT '业务Id',
	`store_id` VARCHAR(30) NOT NULL COMMENT '商户ID',
	res_order_type varchar(8) not null comment '出库类型 10000 入库 20000 出库 在t_dict表查看',
	state varchar(8) not null comment '审核状态',
	`create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
	status_cd varchar(2) not null default '0' COMMENT '数据状态，详细参考t_dict表，0, 在用 1失效',
	KEY `idx_b_res_order_id` (`res_order_id`)
);

-- 资源 出入库 记录订单
create table business_resource_order(
	res_order_id varchar(30) not null comment '订单ID',
	`b_id` varchar(30) NOT NULL COMMENT '业务Id',
	`store_id` VARCHAR(30) NOT NULL COMMENT '商户ID',
	res_order_type varchar(8) not null comment '出库类型 10000 入库 20000 出库 在t_dict表查看',
	state varchar(8) not null comment '审核状态',
	`create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `operate` varchar(4) NOT NULL COMMENT '数据状态，添加ADD，修改MOD 删除DEL'
);

-- 资源项
create table resource_item(
	res_item_id varchar(30) not null comment '订单项ID',
	res_order_id varchar(30) not null comment '订单ID',
	`b_id` varchar(30) NOT NULL COMMENT '业务Id',
	`store_id` VARCHAR(30) NOT NULL COMMENT '商户ID',
	`res_id` varchar(30) NOT NULL COMMENT '资源ID',
  `res_count` int(11) DEFAULT NULL COMMENT '数量',
	remark varchar(200) comment '备注',
	`create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
	status_cd varchar(2) not null default '0' COMMENT '数据状态，详细参考t_dict表，0, 在用 1失效'
);


-- 资源项
create table business_resource_item(
	res_item_id varchar(30) not null comment '订单项ID',
	res_order_id varchar(30) not null comment '订单ID',
	`b_id` varchar(30) NOT NULL COMMENT '业务Id',
	`store_id` VARCHAR(30) NOT NULL COMMENT '商户ID',
	`res_id` varchar(30) NOT NULL COMMENT '资源ID',
  `res_count` int(11) DEFAULT NULL COMMENT '数量',
	remark varchar(200) comment '备注',
	`create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `operate` varchar(4) NOT NULL COMMENT '数据状态，添加ADD，修改MOD 删除DEL'
);