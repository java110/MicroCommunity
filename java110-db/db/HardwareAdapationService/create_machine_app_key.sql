-- 硬件数据同步
create table business_application_key(
  application_key_id varchar(30) not null comment '申请ID',
  b_id VARCHAR(30) NOT NULL COMMENT '业务Id',
  machine_id varchar(30) not null comment '设备ID',
  type_cd varchar(12) not null comment '类型，10001 保洁 10002 保安 10003 其他人员，详情查看t_dict表',
  name varchar(64) not null comment '申请人名称',
  tel varchar(12) not null comment '申请人手机号码',
  sex varchar(12) not null comment '申请人男女  0男 1女',
  age int not null comment '申请人年龄',
  id_card varchar(12) not null comment '申请人身份证号',
  community_id varchar(30) not null comment '小区ID',
  state varchar(8) not null comment '状态，10002 未审核,10001 完成审核 10003 审核拒绝',
  start_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '开始时间',
  end_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '结束时间',
  create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  operate VARCHAR(4) NOT NULL COMMENT '数据状态，添加ADD，修改MOD 删除DEL'
);
-- 数据同步
create table application_key(
  application_key_id varchar(30) not null comment '申请ID',
  b_id VARCHAR(30) NOT NULL COMMENT '业务Id',
  machine_id varchar(30) not null comment '设备ID',
  type_cd varchar(12) not null comment '类型，10001 保洁 10002 保安 10003 其他人员，详情查看t_dict表',
  name varchar(64) not null comment '申请人名称',
  tel varchar(12) not null comment '申请人手机号码',
  sex varchar(2) not null comment '申请人男女 0男 1女',
  age int not null comment '申请人年龄',
  id_card varchar(12) not null comment '申请人身份证号',
  community_id varchar(30) not null comment '小区ID',
  state varchar(8) not null comment '状态，10002 未审核,10001 完成审核 10003 审核拒绝',
  start_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '开始时间',
  end_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '结束时间',
  create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  status_cd VARCHAR(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考c_status表，S 保存，0, 在用 1失效',
  UNIQUE KEY (application_key_id)
);