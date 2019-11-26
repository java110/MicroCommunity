-- 硬件数据同步
create table business_machine_record(
  machine_record_id varchar(30) not null comment '记录ID',
  b_id VARCHAR(30) NOT NULL COMMENT '业务Id',
  machine_id varchar(30) not null comment '设备ID',
  machine_code varchar(30) not null comment '设备编码',
  name varchar(30) not null comment '用户名称',
  open_type_cd varchar(12) not null comment '开门方式，1000 人脸开门 2000 钥匙开门',
  community_id varchar(30) not null comment '小区ID',
  tel varchar(11) not null comment '手机号',
  idCard varchar(11) not null comment '用户身份证',
  file_id varchar(11) comment '文件ID',
  file_time timestamp not null default  CURRENT_TIMESTAMP COMMENT '文件生成时间',
  create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  operate VARCHAR(4) NOT NULL COMMENT '数据状态，添加ADD，修改MOD 删除DEL'
);
-- 数据同步
create table machine_record(
  machine_record_id varchar(30) not null comment '记录ID',
  b_id VARCHAR(30) NOT NULL COMMENT '业务Id',
  machine_id varchar(30) not null comment '设备ID',
  machine_code varchar(30) not null comment '设备编码',
  name varchar(30) not null comment '用户名称',
  open_type_cd varchar(12) not null comment '开门方式，1000 人脸开门 2000 钥匙开门',
  community_id varchar(30) not null comment '小区ID',
  tel varchar(11) not null comment '手机号',
  idCard varchar(11) not null comment '用户身份证',
  file_id varchar(11) comment '文件ID',
  file_time timestamp not null default  CURRENT_TIMESTAMP COMMENT '文件生成时间',
  create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  status_cd VARCHAR(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考c_status表，S 保存，0, 在用 1失效',
  UNIQUE KEY (machine_record_id)
);