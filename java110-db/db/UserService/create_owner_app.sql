
-- 单元信息 building 楼宇管理
CREATE TABLE business_owner_app_user(
  app_user_id VARCHAR(30) NOT NULL COMMENT 'app用户ID',
  member_id VARCHAR(30) NOT NULL COMMENT '业主成员ID',
  b_id VARCHAR(30) NOT NULL COMMENT '业务Id',
  community_id VARCHAR(30) NOT NULL COMMENT '小区Id',
  community_name VARCHAR(100) NOT NULL COMMENT '小区名称',
  app_user_name VARCHAR(100) NOT NULL COMMENT 'app用户名称',
  id_card VARCHAR(20) NOT NULL COMMENT '身份证号',
  link VARCHAR(11) NOT NULL COMMENT '联系人手机号',
  open_id VARCHAR(30) NOT NULL COMMENT 'app 开放ID',
  app_type_cd VARCHAR(12) NOT NULL COMMENT '应用类型 10010 微信小程序',
  state VARCHAR(12) NOT NULL COMMENT '状态类型，10000 审核中，12000 审核成功，13000 审核失败',
  remark VARCHAR(200) NOT NULL COMMENT '备注',
  create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  operate VARCHAR(4) NOT NULL COMMENT '数据状态，添加ADD，修改MOD 删除DEL'
);


CREATE INDEX idx_b_owner_app_app_user_id ON business_owner_app_user(app_user_id);
CREATE INDEX idx_b_owner_app_b_id ON business_owner_app_user(b_id);


CREATE TABLE owner_app_user(
  app_user_id VARCHAR(30) NOT NULL COMMENT 'app用户ID',
    member_id VARCHAR(30) NOT NULL COMMENT '业主成员ID',
    b_id VARCHAR(30) NOT NULL COMMENT '业务Id',
    community_id VARCHAR(30) NOT NULL COMMENT '小区Id',
    community_name VARCHAR(100) NOT NULL COMMENT '小区名称',
    app_user_name VARCHAR(100) NOT NULL COMMENT 'app用户名称',
    id_card VARCHAR(20) NOT NULL COMMENT '身份证号',
    link VARCHAR(11) NOT NULL COMMENT '联系人手机号',
    open_id VARCHAR(30) NOT NULL COMMENT 'app 开放ID',
    app_type_cd VARCHAR(12) NOT NULL COMMENT '应用类型 10010 微信小程序',
    state VARCHAR(12) NOT NULL COMMENT '状态类型，10000 审核中，12000 审核成功，13000 审核失败',
    remark VARCHAR(200) NOT NULL COMMENT '备注',
  create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  status_cd VARCHAR(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考c_status表，0, 在用 1失效',
  UNIQUE KEY (app_user_id)
);
CREATE INDEX idx_owner_app_user_b_id ON owner_app_user(b_id);
CREATE UNIQUE INDEX idx_owner_app_user_id ON owner_app_user(app_user_id);