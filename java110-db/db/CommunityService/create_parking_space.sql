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


create table car_inout(
  inout_id varchar(30) not null comment '进出ID',
  b_id VARCHAR(30) NOT NULL COMMENT '业务Id',
  community_id varchar(30) not null comment '小区ID',
  car_num varchar(12) not null comment '车牌号',

);
