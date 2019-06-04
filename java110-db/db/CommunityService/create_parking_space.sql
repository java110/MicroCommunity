-- 楼信息
CREATE TABLE business_parking_space(
  ps_id VARCHAR(30) NOT NULL COMMENT '车位ID',
  b_id VARCHAR(30) NOT NULL COMMENT '业务Id',
  community_id varchar(30) not null comment '小区ID',
  num VARCHAR(12) NOT NULL COMMENT '车位编号',
  type_cd VARCHAR(4) NOT NULL COMMENT '车位类型,地上停车位1001 地下停车位 2001',
  state VARCHAR(4) NOT NULL COMMENT '车位状态 出售 S，出租 H ，空闲 F',
  area decimal(7,2) NOT NULL COMMENT '车位面积',
  user_id VARCHAR(30) NOT NULL COMMENT '用户ID',
  remark VARCHAR(300) NOT NULL COMMENT '用户ID',
  create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  operate VARCHAR(4) NOT NULL COMMENT '数据状态，添加ADD，修改MOD 删除DEL'
);


CREATE INDEX idx_bps_ps_id ON business_parking_space(ps_id);
CREATE INDEX idx_bps_b_id ON business_parking_space(b_id);

-- 楼信息
CREATE TABLE p_parking_space(
  ps_id VARCHAR(30) NOT NULL COMMENT '车位ID',
  b_id VARCHAR(30) NOT NULL COMMENT '业务Id',
  community_id varchar(30) not null comment '小区ID',
  num VARCHAR(12) NOT NULL COMMENT '车位编号',
  type_cd VARCHAR(4) NOT NULL COMMENT '车位类型,地上停车位1001 地下停车位 2001',
  state VARCHAR(4) NOT NULL COMMENT '车位状态 出售 S，出租 H ，空闲 F',
  area decimal(7,2) NOT NULL COMMENT '车位面积',
  user_id VARCHAR(30) NOT NULL COMMENT '用户ID',
  remark VARCHAR(300) NOT NULL COMMENT '用户ID',
  create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  status_cd VARCHAR(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考c_status表，S 保存，0, 在用 1失效',
  unique key (ps_id)
);

CREATE INDEX idx_ps_ps_id ON p_parking_space(ps_id);
CREATE INDEX idx_ps_b_id ON p_parking_space(b_id);
