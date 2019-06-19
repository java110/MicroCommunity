
-- 单元信息 building 楼宇管理
CREATE TABLE business_owner_car(
  car_id VARCHAR(30) NOT NULL COMMENT '汽车ID',
  owner_id VARCHAR(30) NOT NULL COMMENT '业主ID',
  b_id VARCHAR(30) NOT NULL COMMENT '业务Id',
  car_num VARCHAR(12) NOT NULL COMMENT '车牌号',
  car_brand VARCHAR(50) NOT NULL COMMENT '汽车品牌',
  car_type VARCHAR(4) NOT NULL COMMENT '9901 家用小汽车，9902 客车，9903 货车',
  car_color varchar(12) not null comment '颜色',
  ps_id VARCHAR(30) NOT NULL COMMENT '车位ID',
  user_id VARCHAR(30) NOT NULL COMMENT '用户ID',
  remark VARCHAR(200) NOT NULL COMMENT '备注',
  create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  operate VARCHAR(4) NOT NULL COMMENT '数据状态，添加ADD，修改MOD 删除DEL'
);


CREATE INDEX idx_boc_car_id ON business_owner_car(car_id);
CREATE INDEX idx_boc_b_id ON business_owner_car(b_id);


CREATE TABLE owner_car(
  car_id VARCHAR(30) NOT NULL COMMENT '汽车ID',
  owner_id VARCHAR(30) NOT NULL COMMENT '业主ID',
  b_id VARCHAR(30) NOT NULL COMMENT '业务Id',
  car_num VARCHAR(12) NOT NULL COMMENT '车牌号',
  car_brand VARCHAR(50) NOT NULL COMMENT '汽车品牌',
  car_type VARCHAR(4) NOT NULL COMMENT '9901 家用小汽车，9902 客车，9903 货车',
  car_color varchar(12) not null comment '颜色',
  ps_id VARCHAR(30) NOT NULL COMMENT '车位ID',
  user_id VARCHAR(30) NOT NULL COMMENT '用户ID',
  remark VARCHAR(200) NOT NULL COMMENT '备注',
  create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  status_cd VARCHAR(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考c_status表，0, 在用 1失效',
  UNIQUE KEY (car_id)
);
CREATE INDEX idx_oc_car_id ON owner_car(car_id);
CREATE INDEX idx_oc_b_id ON owner_car(b_id);

