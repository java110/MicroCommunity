
-- 单元信息 building 楼宇管理
CREATE TABLE business_building_owner(
  member_id VARCHAR(30) NOT NULL COMMENT '业主成员ID',
  owner_id VARCHAR(30) NOT NULL COMMENT '业主ID',
  b_id VARCHAR(30) NOT NULL COMMENT '业务Id',
  name VARCHAR(12) NOT NULL COMMENT '业主名称',
  sex int NOT NULL COMMENT '性别',
  age int NOT NULL COMMENT '年龄',
  link varchar(11) NOT NULL COMMENT '联系人手机号',
  user_id VARCHAR(30) NOT NULL COMMENT '用户ID',
  remark VARCHAR(200) NOT NULL COMMENT '备注',
  create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  operate VARCHAR(4) NOT NULL COMMENT '数据状态，添加ADD，修改MOD 删除DEL'
);


CREATE INDEX idx_business_owner_id ON business_building_owner(member_id);
CREATE INDEX idx_business_owner_b_id ON business_building_owner(b_id);


CREATE TABLE building_owner(
  member_id VARCHAR(30) NOT NULL COMMENT '业主成员ID',
  owner_id VARCHAR(30) NOT NULL COMMENT '业主ID',
  b_id VARCHAR(30) NOT NULL COMMENT '业务Id',
  name VARCHAR(12) NOT NULL COMMENT '业主名称',
  sex int NOT NULL COMMENT '性别',
  age int NOT NULL COMMENT '年龄',
  link varchar(11) NOT NULL COMMENT '联系人手机号',
  user_id VARCHAR(30) NOT NULL COMMENT '用户ID',
  remark VARCHAR(200) NOT NULL COMMENT '备注',
  create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  status_cd VARCHAR(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考c_status表，0, 在用 1失效',
  UNIQUE KEY (member_id)
);
CREATE INDEX idx_owner_b_id ON building_owner(b_id);
CREATE UNIQUE INDEX idx_owner_id ON building_owner(member_id);

-- 单元信息 building 楼宇管理
CREATE TABLE business_building_owner_room_rel(
  rel_id VARCHAR(30) NOT NULL COMMENT '关系ID',
  owner_id VARCHAR(30) NOT NULL COMMENT '业主ID',
  room_id VARCHAR(30) NOT NULL COMMENT '房间ID',
  b_id VARCHAR(30) NOT NULL COMMENT '业务Id',
  state varchar(4) NOT NULL COMMENT '业务状态  2001 业主未迁入 2002 业主迁入 2003 业主迁出',
  user_id VARCHAR(30) NOT NULL COMMENT '用户ID',
  remark VARCHAR(200) NOT NULL COMMENT '备注',
  create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  operate VARCHAR(4) NOT NULL COMMENT '数据状态，添加ADD，修改MOD 删除DEL'
);


CREATE INDEX idx_bborrl_owner_id ON business_building_owner_room_rel(rel_id);
CREATE INDEX idx_bborrl_b_id ON business_building_owner_room_rel(b_id);


CREATE TABLE building_owner_room_rel(
  rel_id VARCHAR(30) NOT NULL COMMENT '关系ID',
  owner_id VARCHAR(30) NOT NULL COMMENT '业主ID',
  room_id VARCHAR(30) NOT NULL COMMENT '房间ID',
  b_id VARCHAR(30) NOT NULL COMMENT '业务Id',
  state varchar(4) NOT NULL COMMENT '业务状态 2001 业主未迁入 2002 业主迁入 2003 业主迁出',
  user_id VARCHAR(30) NOT NULL COMMENT '用户ID',
  remark VARCHAR(200) NOT NULL COMMENT '备注',
  create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  status_cd VARCHAR(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考c_status表，0, 在用 1失效',
  UNIQUE KEY (rel_id)
);
CREATE INDEX idx_borr_b_id ON building_owner_room_rel(b_id);
CREATE UNIQUE INDEX idx_borr_rel_id ON building_owner_room_rel(rel_id);