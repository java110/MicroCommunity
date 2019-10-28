create table audit_message(
  `audit_message_id` varchar(30) NOT NULL COMMENT '审核ID',
  `b_id` varchar(30) NOT NULL COMMENT '业务Id',
  `store_id` varchar(30) NOT NULL COMMENT '商户ID,用来做分区',
  `user_id` varchar(100) NOT NULL COMMENT '审核用户ID',
  `user_name` varchar(100) NOT NULL COMMENT '审核用户名称',
  `audit_order_id` varchar(64) NOT NULL COMMENT '审核环节，审核订单ID ',
  `audit_order_type` varchar(64) NOT NULL COMMENT '审核工单类型，如采购，小区入驻审核 详看t_dict表',
  state varchar(12) not null comment '审核状态 ，10000 同意 20000 反驳，详见t_dict表',
  message longtext not null comment '审核描述',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `status_cd` varchar(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考t_dict表，0, 在用 1失效',
  KEY `idx_audit_message_id` (`audit_message_id`)
);

CREATE TABLE `business_audit_user` (
    `audit_message_id` varchar(30) NOT NULL COMMENT '审核ID',
  `b_id` varchar(30) NOT NULL COMMENT '业务Id',
  `store_id` varchar(30) NOT NULL COMMENT '商户ID,用来做分区',
  `user_id` varchar(100) NOT NULL COMMENT '审核用户ID',
  `user_name` varchar(100) NOT NULL COMMENT '审核用户名称',
  `audit_order_id` varchar(64) NOT NULL COMMENT '审核环节，审核订单ID ',
  `audit_order_type` varchar(64) NOT NULL COMMENT '审核工单类型，如采购，小区入驻审核 详看t_dict表',
  state varchar(12) not null comment '审核状态 ，10000 同意 20000 反驳，详见t_dict表',
  message longtext not null comment '审核描述',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `operate` varchar(4) NOT NULL COMMENT '数据状态，添加ADD，修改MOD 删除DEL'
);