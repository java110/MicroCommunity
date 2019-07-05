-- 楼信息
CREATE TABLE business_notice(
  notice_id VARCHAR(30) NOT NULL COMMENT '通知ID',
  b_id VARCHAR(30) NOT NULL COMMENT '业务Id',
  title VARCHAR(12) NOT NULL COMMENT '通知标题',
  notice_type_cd VARCHAR(4) NOT NULL COMMENT '类型 1000 业主通知，1001员工通知，1002小区通知',
  context longtext not null comment '通知内容',
  community_id varchar(30) not null comment '小区ID',
  user_id VARCHAR(30) NOT NULL COMMENT '用户ID',
  start_time timestamp not null default  current_timestamp comment '开始时间',
  create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  operate VARCHAR(4) NOT NULL COMMENT '数据状态，添加ADD，修改MOD 删除DEL'
);


CREATE INDEX idx_business_notice_id ON business_notice(notice_id);
CREATE INDEX idx_business_notice_b_id ON business_notice(b_id);


CREATE TABLE n_notice(
  notice_id VARCHAR(30) NOT NULL COMMENT '通知ID',
  b_id VARCHAR(30) NOT NULL COMMENT '业务Id',
  title VARCHAR(12) NOT NULL COMMENT '通知标题',
  notice_type_cd VARCHAR(4) NOT NULL COMMENT '类型 1000 业主通知，1001员工通知，1002小区通知',
  context longtext not null comment '通知内容',
  community_id varchar(30) not null comment '小区ID',
  user_id VARCHAR(30) NOT NULL COMMENT '用户ID',
  start_time timestamp not null default  current_timestamp comment '开始时间',
  create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  status_cd VARCHAR(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考c_status表，S 保存，0, 在用 1失效',
  UNIQUE KEY (notice_id)
);
CREATE INDEX idx_notice_b_id ON n_notice(b_id);
CREATE UNIQUE INDEX idx_notice_id ON n_notice(notice_id);
