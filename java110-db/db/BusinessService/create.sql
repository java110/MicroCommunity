-- 服务注册过程表

create table business_bus(
  id varchar(30) not null comment '服务ID',
  b_id VARCHAR(30) NOT NULL COMMENT '业务Id',
  business_type_cd VARCHAR(20) NOT  null COMMENT '服务编码',
  name VARCHAR(20) NOT  null COMMENT '服务名称',
  description VARCHAR(20) NOT  null COMMENT '服务描述',
  user_id varchar(30) not null comment '创建用户ID',
  create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  operate VARCHAR(4) NOT NULL COMMENT '数据状态，添加ADD，修改MOD 删除DEL'
);