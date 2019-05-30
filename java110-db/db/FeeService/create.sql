create table fee(
  fee_id varchar(30) not null comment '费用ID',
  fee_type_cd varchar(12) not null comment '费用类型，物业费，停车费',
  community_id varchar(30) not null comment '小区ID',
  payer_obj_id varchar(30) not null comment '付款方ID',
  income_obj_id varchar(30) not null comment '收入方ID',
  cycle int not null comment '周期数，以月为单位',
  price DECIMAL(7,2) not null comment '金额',
  remark VARCHAR(300) NOT NULL COMMENT '用户ID',
  create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  status_cd VARCHAR(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考c_status表，S 保存，0, 在用 1失效',
  UNIQUE KEY (fee_id)
);