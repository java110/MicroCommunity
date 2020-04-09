-- 活动信息表
CREATE TABLE business_fast_user(
  fast_user_id VARCHAR(30) NOT NULL COMMENT '用户首次测试ID',
  b_id VARCHAR(30) NOT NULL COMMENT '业务Id',
  fast_user_title VARCHAR(12) NOT NULL COMMENT '测试标题',
  fast_user_context longtext not null comment '测试内容',
  community_id varchar(30) not null comment '小区ID',
  user_id VARCHAR(30) NOT NULL COMMENT '创建用户Id',
  user_name VARCHAR(30) NOT NULL COMMENT '创建用户名称',
  start_time timestamp not null default  current_timestamp comment '开始时间',
  end_time timestamp not null comment '结束时间',
  state varchar(12) not null default '10000' comment '审核状态，10000 待审核 ，11000 审核完成 12000 审核失败',
  create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  operate VARCHAR(4) NOT NULL COMMENT '数据状态，添加ADD，修改MOD 删除DEL'
);


CREATE INDEX idx_business_fast_user_id ON business_fastuser(fast_user_id);
CREATE INDEX idx_business_fast_user_b_id ON business_fastuser(b_id);


CREATE TABLE fast_user(
    fast_user_id VARCHAR(30) NOT NULL COMMENT '用户首次测试ID',
    b_id VARCHAR(30) NOT NULL COMMENT '业务Id',
    fast_user_title VARCHAR(12) NOT NULL COMMENT '测试标题',
    fast_user_context longtext not null comment '测试内容',
    community_id varchar(30) not null comment '小区ID',
    user_id VARCHAR(30) NOT NULL COMMENT '创建用户Id',
    user_name VARCHAR(30) NOT NULL COMMENT '创建用户名称',
    start_time timestamp not null default  current_timestamp comment '开始时间',
    end_time timestamp not null comment '结束时间',
     state varchar(12) not null default '10000' comment '审核状态，10000 待审核 ，11000 审核完成 12000 审核失败',
  create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  status_cd VARCHAR(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考c_status表，S 保存，0, 在用 1失效',
  UNIQUE KEY (fast_user_id)
);
CREATE INDEX idx_fast_user_b_id ON fast_user(b_id);
CREATE UNIQUE INDEX idx_fast_user_id ON fast_user(fast_user_id);