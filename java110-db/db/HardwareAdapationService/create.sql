-- 硬件设备
create table business_machine(
  machine_id varchar(30) not null comment '设备ID',
  b_id VARCHAR(30) NOT NULL COMMENT '业务Id',
  machine_code varchar(30) not null comment '设备编码',
  machine_version varchar(30) not null comment '设备版本',
  machine_type_cd varchar(12) not null comment '设备类型 门禁9999 详情查看t_dict 表',
  community_id varchar(30) not null comment '小区ID',
  machine_name varchar(200) not null comment '设备名称',
  auth_code varchar(64) not null comment '授权码',
  machine_ip varchar(64) comment '设备IP',
  machine_mac varchar(64) comment '设备mac',
  location_type_cd varchar(12) not null default '1000' comment '位置类型，1000 东大门  1001 西大门 1002 北大门 1003 南大门 2000 单元门 3000 房屋门',
  location_obj_id varchar(30) not null default '-1' comment '对象ID，大门时小区ID，单元门 时单元ID 房屋时房屋ID',
  state varchar(12) not null default '1000' comment '设备状态，设备配置同步状态 1000 未同步 1100 同步中 1200 已同步',
  create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  operate VARCHAR(4) NOT NULL COMMENT '数据状态，添加ADD，修改MOD 删除DEL'
);
CREATE INDEX idx_bm_machine_id ON business_machine(machine_id);
CREATE INDEX idx_bm_b_id ON business_machine(b_id);

create table machine(
  machine_id varchar(30) not null comment '设备ID',
  b_id VARCHAR(30) NOT NULL COMMENT '业务Id',
  machine_code varchar(30) not null comment '设备编码',
  machine_version varchar(30) not null comment '设备版本',
  machine_type_cd varchar(12) not null comment '设备类型 门禁9999 详情查看t_dict 表',
  community_id varchar(30) not null comment '小区ID',
  machine_name varchar(200) not null comment '设备名称',
  auth_code varchar(64) not null comment '授权码',
  machine_ip varchar(64) comment '设备IP',
  machine_mac varchar(64) comment '设备mac',
  location_type_cd varchar(12) not null default '1000' comment '位置类型，1000 东大门  1001 西大门 1002 北大门 1003 南大门 2000 单元门 3000 房屋门',
  location_obj_id varchar(30) not null default '-1' comment '对象ID，大门时小区ID，单元门 时单元ID 房屋时房屋ID',
  state varchar(12) not null default '1000' comment '设备状态，设备配置同步状态 1000 未同步 1100 同步中 1200 已同步',
  create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  status_cd VARCHAR(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考c_status表，S 保存，0, 在用 1失效',
  UNIQUE KEY (machine_id)
);
CREATE INDEX idx_machine_id ON machine(machine_id);
CREATE INDEX idx_machine_b_id ON machine(b_id);

--

CREATE TABLE business_machine_attrs(
    machine_id VARCHAR(30) NOT NULL COMMENT '费用ID',
    b_id VARCHAR(30) NOT NULL COMMENT '业务Id',
    community_id varchar(30) not null comment '小区ID',
    attr_id VARCHAR(30) NOT NULL COMMENT '属性id',
    spec_cd VARCHAR(12) NOT NULL COMMENT '规格id,参考spec表',
    `value` VARCHAR(50) NOT NULL COMMENT '属性值',
    operate VARCHAR(4) NOT NULL COMMENT '数据状态，添加ADD，修改MOD 删除DEL'
);

CREATE INDEX idx_bma_machine_id ON business_machine_attrs(machine_id);
CREATE INDEX idx_bma_b_id ON business_pay_fee_attrs(b_id);

-- c_orders_attrs

CREATE TABLE machine_attrs(
    machine_id VARCHAR(30) NOT NULL COMMENT '设备ID',
    b_id VARCHAR(30) NOT NULL COMMENT '业务Id',
    community_id varchar(30) not null comment '小区ID',
    attr_id VARCHAR(30) NOT NULL COMMENT '属性id',
    spec_cd VARCHAR(12) NOT NULL COMMENT '规格id,参考spec表',
    `value` VARCHAR(50) NOT NULL COMMENT '属性值',
     status_cd VARCHAR(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考c_status表，S 保存，0, 在用 1失效',
     UNIQUE KEY (machine_id)
);
CREATE INDEX idx_machine_a_machine_id ON machine_attrs(machine_id);
CREATE INDEX idx_machine_a_b_id ON machine_attrs(b_id);

