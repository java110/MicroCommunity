-- 楼信息
CREATE TABLE business_floor(
  floor_id VARCHAR(30) NOT NULL COMMENT '楼ID',
  b_id VARCHAR(30) NOT NULL COMMENT '业务Id',
  floor_num VARCHAR(12) NOT NULL COMMENT '楼编号',
  `name` VARCHAR(100) NOT NULL COMMENT '小区楼名称',
  user_id VARCHAR(30) NOT NULL COMMENT '用户ID',
  remark VARCHAR(300) NOT NULL COMMENT '用户ID',
  create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  operate VARCHAR(4) NOT NULL COMMENT '数据状态，添加ADD，修改MOD 删除DEL'
);


CREATE INDEX idx_business_floor_id ON business_floor(floor_id);
CREATE INDEX idx_business_floor_b_id ON business_floor(b_id);


CREATE TABLE f_floor(
  floor_id VARCHAR(30) NOT NULL COMMENT '楼ID',
  b_id VARCHAR(30) NOT NULL COMMENT '业务Id',
  floor_num VARCHAR(12) NOT NULL COMMENT '楼编号',
  `name` VARCHAR(100) NOT NULL COMMENT '小区楼名称',
  user_id VARCHAR(30) NOT NULL COMMENT '用户ID',
  remark VARCHAR(300) NOT NULL COMMENT '用户ID',
  create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  status_cd VARCHAR(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考c_status表，S 保存，0, 在用 1失效',
  UNIQUE KEY (floor_id)
);
CREATE INDEX idx_floor_b_id ON f_floor(b_id);
CREATE UNIQUE INDEX idx_floor_id ON f_floor(floor_id);


-- 单元信息 building 楼宇管理
CREATE TABLE business_building_unit(
  unit_id VARCHAR(30) NOT NULL COMMENT '单元ID',
  b_id VARCHAR(30) NOT NULL COMMENT '业务Id',
  unit_num VARCHAR(12) NOT NULL COMMENT '单元编号',
  floor_id VARCHAR(30) NOT NULL COMMENT '楼ID',
  layer_count int NOT NULL COMMENT '总层数',
  lift varchar(4) NOT NULL COMMENT '是否有电梯 1010有 2020 无',
  user_id VARCHAR(30) NOT NULL COMMENT '用户ID',
  remark VARCHAR(200) NOT NULL COMMENT '备注',
  create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  operate VARCHAR(4) NOT NULL COMMENT '数据状态，添加ADD，修改MOD 删除DEL'
);


CREATE INDEX idx_business_unit_id ON business_building_unit(unit_id);
CREATE INDEX idx_business_unit_b_id ON business_building_unit(b_id);


CREATE TABLE building_unit(
  unit_id VARCHAR(30) NOT NULL COMMENT '单元ID',
  b_id VARCHAR(30) NOT NULL COMMENT '业务Id',
  unit_num VARCHAR(12) NOT NULL COMMENT '单元编号',
  floor_id VARCHAR(30) NOT NULL COMMENT '楼ID',
  layer_count int NOT NULL COMMENT '总层数',
  lift varchar(4) NOT NULL COMMENT '是否有电梯 1010有 2020 无',
  user_id VARCHAR(30) NOT NULL COMMENT '用户ID',
  remark VARCHAR(200) NOT NULL COMMENT '备注',
  create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  status_cd VARCHAR(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考c_status表，S 保存，0, 在用 1失效',
  UNIQUE KEY (unit_id)
);
CREATE INDEX idx_unit_b_id ON building_unit(b_id);
CREATE UNIQUE INDEX idx_unit_id ON building_unit(unit_id);



-- 单元信息 building 楼宇管理
CREATE TABLE business_building_room(
  room_id VARCHAR(30) NOT NULL COMMENT '房屋ID',
  b_id VARCHAR(30) NOT NULL COMMENT '业务Id',
  room_num VARCHAR(12) NOT NULL COMMENT '房屋编号',
  unit_id VARCHAR(30) NOT NULL COMMENT '单元ID',
  layer int NOT NULL COMMENT '层数',
  section varchar(4) NOT NULL COMMENT '室',
  apartment varchar(4) NOT NULL COMMENT '户型',
  built_up_area varchar(4) NOT NULL COMMENT '建筑面积',
  unit_price varchar(4) NOT NULL COMMENT '每平米单价',
  user_id VARCHAR(30) NOT NULL COMMENT '用户ID',
  remark VARCHAR(200) NOT NULL COMMENT '备注',
  create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  operate VARCHAR(4) NOT NULL COMMENT '数据状态，添加ADD，修改MOD 删除DEL'
);


CREATE INDEX idx_business_room_id ON business_building_room(room_id);
CREATE INDEX idx_business_room_b_id ON business_building_room(b_id);


CREATE TABLE building_room(
  room_id VARCHAR(30) NOT NULL COMMENT '房屋ID',
  b_id VARCHAR(30) NOT NULL COMMENT '业务Id',
  room_num VARCHAR(12) NOT NULL COMMENT '房屋编号',
  unit_id VARCHAR(30) NOT NULL COMMENT '单元ID',
  layer int NOT NULL COMMENT '层数',
  section varchar(4) NOT NULL COMMENT '室',
  apartment varchar(4) NOT NULL COMMENT '户型',
  built_up_area varchar(4) NOT NULL COMMENT '建筑面积',
  unit_price varchar(4) NOT NULL COMMENT '每平米单价',
  user_id VARCHAR(30) NOT NULL COMMENT '用户ID',
  remark VARCHAR(200) NOT NULL COMMENT '备注',
  create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  status_cd VARCHAR(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考c_status表，S 保存，0, 在用 1失效',
  UNIQUE KEY (room_id)
);
CREATE INDEX idx_room_b_id ON building_room(b_id);
CREATE UNIQUE INDEX idx_room_id ON building_room(room_id);

