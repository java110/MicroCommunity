-- 硬件数据同步
create table business_machine_translate(
  machine_translate_id varchar(30) not null comment '设备同步ID',
  b_id VARCHAR(30) NOT NULL COMMENT '业务Id',
  machine_id varchar(30) not null comment '设备ID',
  machine_code varchar(30) not null comment '设备编码',
  type_cd varchar(30) not null comment '类型，是同步业主还是同步小区信息，详情查看t_dict表',
  obj_id varchar(12) not null comment '对象ID 业主时，业主ID，小区是小区ID',
  community_id varchar(30) not null comment '小区ID',
  obj_name varchar(200) not null comment '对象名称，业主时业主名称，小区时小区名称',
  state varchar(8) not null comment '状态，10000 初始状态,20000 同步完成',
  create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  operate VARCHAR(4) NOT NULL COMMENT '数据状态，添加ADD，修改MOD 删除DEL'
);
-- 数据同步
create table machine_translate(
  machine_translate_id varchar(30) not null comment '设备同步ID',
  b_id VARCHAR(30) NOT NULL COMMENT '业务Id',
  machine_id varchar(30) not null comment '设备ID',
  machine_code varchar(30) not null comment '设备编码',
  type_cd varchar(30) not null comment '类型，是同步业主还是同步小区信息，详情查看t_dict表',
  obj_id varchar(12) not null comment '对象ID 业主时，业主ID，小区是小区ID',
  community_id varchar(30) not null comment '小区ID',
  obj_name varchar(200) not null comment '对象名称，业主时业主名称，小区时小区名称',
  state varchar(8) not null comment '状态，10000 初始状态,20000 同步完成',
  create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  status_cd VARCHAR(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考c_status表，S 保存，0, 在用 1失效',
  UNIQUE KEY (machine_id)
);