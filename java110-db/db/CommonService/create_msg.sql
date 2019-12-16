create table msg(
  `msg_id` varchar(30) NOT NULL COMMENT '消息ID',
  `b_id` varchar(30) NOT NULL COMMENT '业务Id',
  `msg_type` varchar(12) not null comment '消息类型 10001 公告，10002 钥匙审核，10003 小区入驻审核，10004 小区添加审核',
  `title` varchar(30) NOT NULL COMMENT '消息标题',
  `url` varchar(100) NOT NULL COMMENT '消息路径',
  `view_type_cd` varchar(100) NOT NULL COMMENT '受众类型，10000 系统内消息，20000 小区内消息， 30000 商户内， 40000 员工ID（userId）',
  `view_obj_id` varchar(64) NOT NULL COMMENT '对象ID',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `status_cd` varchar(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考t_dict表，0, 在用 1失效',
  KEY `idx_msg_id` (`msg_id`)
);

CREATE TABLE `business_msg` (
  `msg_id` varchar(30) NOT NULL COMMENT '消息ID',
  `b_id` varchar(30) NOT NULL COMMENT '业务Id',
  `msg_type` varchar(12) not null comment '消息类型 10001 公告，10002 钥匙审核，10003 小区入驻审核，10004 小区添加审核',
  `title` varchar(30) NOT NULL COMMENT '消息标题',
  `url` varchar(100) NOT NULL COMMENT '消息路径',
  `view_type_cd` varchar(100) NOT NULL COMMENT '受众类型，10000 系统内消息，20000 小区内消息， 30000 商户内， 40000 员工ID（userId）',
  `view_obj_id` varchar(64) NOT NULL COMMENT '对象ID',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `operate` varchar(4) NOT NULL COMMENT '数据状态，添加ADD，修改MOD 删除DEL'
);

create table msg_read(
    msg_read_id varchar(30) not null comment '消息阅读ID',
    `b_id` varchar(30) NOT NULL COMMENT '业务Id',
    `msg_id` varchar(30) NOT NULL COMMENT '消息ID',
    user_id varchar(30) not null comment '阅读者',
    user_name varchar(30) not null comment '阅读者姓名',
    `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `status_cd` varchar(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考t_dict表，0, 在用 1失效',
    KEY `idx_msg_read_id` (`msg_read_id`)
);

CREATE TABLE `business_msg_read` (
  msg_read_id varchar(30) not null comment '消息阅读ID',
    `b_id` varchar(30) NOT NULL COMMENT '业务Id',
    `msg_id` varchar(30) NOT NULL COMMENT '消息ID',
    user_id varchar(30) not null comment '阅读者',
    user_name varchar(30) not null comment '阅读者姓名',
    `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `operate` varchar(4) NOT NULL COMMENT '数据状态，添加ADD，修改MOD 删除DEL'
);