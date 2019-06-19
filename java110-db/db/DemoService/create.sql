-- 费用主表

create table business_demo(
  demo_id varchar(30) not null comment '费用ID',
  b_id VARCHAR(30) NOT NULL COMMENT '业务Id',
  demo_name VARCHAR(20) NOT  null COMMENT '用例名称',
  demo_value VARCHAR(20) NOT  null COMMENT '用例值',
  demo_remark VARCHAR(20) NOT  null COMMENT '用例描述',
  user_id varchar(30) not null comment '创建用户ID',
  create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  operate VARCHAR(4) NOT NULL COMMENT '数据状态，添加ADD，修改MOD 删除DEL'
);

create table demo(
   demo_id varchar(30) not null comment '费用ID',
  b_id VARCHAR(30) NOT NULL COMMENT '业务Id',
  demo_name VARCHAR(20) NOT  null COMMENT '用例名称',
  demo_value VARCHAR(20) NOT  null COMMENT '用例值',
  demo_remark VARCHAR(20) NOT  null COMMENT '用例描述',
  user_id varchar(30) not null comment '创建用户ID',
  create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  status_cd VARCHAR(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考c_status表，S 保存，0, 在用 1失效'
);


