create table business_parking_area(
  pa_id varchar(30) not null comment '停车场ID',
  b_id varchar(30) not null comment '业务ID',
  community_id varchar(30) not null comment '小区ID',
  num varchar(12) not null comment '停车场编号',
  type_cd varchar(12) not null comment '停车场类型，1001 地上停车场 2001 地下停车场',
  remark VARCHAR(300) NOT NULL COMMENT '备注',
  create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  operate VARCHAR(4) NOT NULL COMMENT '数据状态，添加ADD，修改MOD 删除DEL'
);

create table parking_area(
  pa_id varchar(30) not null comment '停车场ID',
  b_id varchar(30) not null comment '业务ID',
  community_id varchar(30) not null comment '小区ID',
  num varchar(12) not null comment '停车场编号',
  type_cd varchar(12) not null comment '停车场类型，1001 地上停车场 2001 地下停车场',
  remark VARCHAR(300) NOT NULL COMMENT '备注',
  create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  status_cd VARCHAR(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考c_status表，S 保存，0, 在用 1失效',
  unique key(pa_id)
);
-- 车位
CREATE TABLE business_parking_space(
  ps_id VARCHAR(30) NOT NULL COMMENT '车位ID',
  b_id VARCHAR(30) NOT NULL COMMENT '业务Id',
  community_id varchar(30) not null comment '小区ID',
  num VARCHAR(12) NOT NULL COMMENT '车位编号',
  pa_id VARCHAR(30) NOT NULL COMMENT '停车场ID',
  state VARCHAR(4) NOT NULL COMMENT '车位状态 出售 S，出租 H ，空闲 F',
  area decimal(7,2) NOT NULL COMMENT '车位面积',
  remark VARCHAR(300) NOT NULL COMMENT '备注',
  create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  operate VARCHAR(4) NOT NULL COMMENT '数据状态，添加ADD，修改MOD 删除DEL'
);


CREATE INDEX idx_bps_ps_id ON business_parking_space(ps_id);
CREATE INDEX idx_bps_b_id ON business_parking_space(b_id);

-- 楼信息
CREATE TABLE parking_space(
  ps_id VARCHAR(30) NOT NULL COMMENT '车位ID',
  b_id VARCHAR(30) NOT NULL COMMENT '业务Id',
  community_id varchar(30) not null comment '小区ID',
  num VARCHAR(12) NOT NULL COMMENT '车位编号',
  pa_id VARCHAR(30) NOT NULL COMMENT '停车场ID',
  state VARCHAR(4) NOT NULL COMMENT '车位状态 出售 S，出租 H ，空闲 F',
  area decimal(7,2) NOT NULL COMMENT '车位面积',
  remark VARCHAR(300) NOT NULL COMMENT '用户ID',
  create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  status_cd VARCHAR(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考c_status表，S 保存，0, 在用 1失效',
  unique key (ps_id)
);

CREATE INDEX idx_ps_ps_id ON parking_space(ps_id);
CREATE INDEX idx_ps_b_id ON parking_space(b_id);

-- 车辆进出场
create table business_car_inout(
  inout_id varchar(30) not null comment '进出ID',
  b_id VARCHAR(30) NOT NULL COMMENT '业务Id',
  community_id varchar(30) not null comment '小区ID',
  car_num varchar(12) not null comment '车牌号',
  state VARCHAR(12) NOT NULL COMMENT '状态，100300 进场状态 100400 支付完成 100500 离场状态 100600 支付超时重新支付',
  in_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '进场时间',
  out_time TIMESTAMP  COMMENT '离场时间',
  create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  operate VARCHAR(4) NOT NULL COMMENT '数据状态，添加ADD，修改MOD 删除DEL'
);

create table car_inout(
  inout_id varchar(30) not null comment '进出ID',
  b_id VARCHAR(30) NOT NULL COMMENT '业务Id',
  community_id varchar(30) not null comment '小区ID',
  car_num varchar(12) not null comment '车牌号',
  state VARCHAR(12) NOT NULL COMMENT '状态，100300 进场状态 100400 支付完成 100500 离场状态 100600 支付超时重新支付',
  in_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '进场时间',
  out_time TIMESTAMP  COMMENT '离场时间',
  create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  status_cd VARCHAR(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考c_status表，S 保存，0, 在用 1失效'
);

create table business_car_inout_detail(
  detail_id varchar(30) not null comment '详情ID',
  inout_id varchar(30) not null comment '进出ID',
  b_id VARCHAR(30) NOT NULL COMMENT '业务Id',
  community_id varchar(30) not null comment '小区ID',
  machine_id varchar(30) not null comment '设备ID',
  machine_code varchar(30) not null comment '设备编码',
  inout varchar(12) not null comment '1010 进场 2020 出场',
  car_num varchar(12) not null comment '车牌号',
  create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  operate VARCHAR(4) NOT NULL COMMENT '数据状态，添加ADD，修改MOD 删除DEL'
);

create table car_inout_detail(
  detail_id varchar(30) not null comment '详情ID',
  inout_id varchar(30) not null comment '进出ID',
  b_id VARCHAR(30) NOT NULL COMMENT '业务Id',
  community_id varchar(30) not null comment '小区ID',
  machine_id varchar(30) not null comment '设备ID',
  machine_code varchar(30) not null comment '设备编码',
  inout varchar(12) not null comment '1010 进场 2020 出场',
  car_num varchar(12) not null comment '车牌号',
  create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  status_cd VARCHAR(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考c_status表，S 保存，0, 在用 1失效'
);

-- 车辆黑白名单
create table business_car_black_white(
  bw_id varchar(30) not null comment '黑白名单ID',
  b_id VARCHAR(30) NOT NULL COMMENT '业务Id',
  community_id varchar(30) not null comment '小区ID',
  black_white varchar(12) not null comment '黑白名单标识 1111 黑名单 2222 白名单',
  car_num varchar(12) not null comment '车牌号',
  start_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '开始时间',
  end_time TIMESTAMP NOT NULL  COMMENT '结束时间',
  create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  operate VARCHAR(4) NOT NULL COMMENT '数据状态，添加ADD，修改MOD 删除DEL'
);

-- 车辆黑白名单
create table car_black_white(
  bw_id varchar(30) not null comment '黑白名单ID',
  b_id VARCHAR(30) NOT NULL COMMENT '业务Id',
  community_id varchar(30) not null comment '小区ID',
  black_white varchar(12) not null comment '黑白名单标识 1111 黑名单 2222 白名单',
  car_num varchar(12) not null comment '车牌号',
  start_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '开始时间',
  end_time TIMESTAMP NOT NULL  COMMENT '结束时间',
  create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  status_cd VARCHAR(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考c_status表，S 保存，0, 在用 1失效'
);
