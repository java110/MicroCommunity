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