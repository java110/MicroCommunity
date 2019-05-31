-- 费用主表

create table business_pay_fee(
  fee_id varchar(30) not null comment '费用ID',
  b_id VARCHAR(30) NOT NULL COMMENT '业务Id',
  fee_type_cd varchar(12) not null comment '费用类型，物业费，停车费',
  community_id varchar(30) not null comment '小区ID',
  payer_obj_id varchar(30) not null comment '付款方ID',
  income_obj_id varchar(30) not null comment '收款方ID',
  start_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '开始时间',
  end_time TIMESTAMP NOT NULL COMMENT '结束时间',
  amount decimal(7,2) not null default -1.00 comment '总金额，如物业费，停车费等没有总金额的，填写为-1.00',
  user_id varchar(30) not null comment '创建用户ID',
  create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  operate VARCHAR(4) NOT NULL COMMENT '数据状态，添加ADD，修改MOD 删除DEL'
);
CREATE INDEX idx_bpf_fee_id ON business_pay_fee(fee_id);
CREATE INDEX idx_bpf_b_id ON business_pay_fee(b_id);

create table pay_fee(
  fee_id varchar(30) not null comment '费用ID',
  b_id VARCHAR(30) NOT NULL COMMENT '业务Id',
  fee_type_cd varchar(12) not null comment '费用类型，物业费，停车费',
  community_id varchar(30) not null comment '小区ID',
  payer_obj_id varchar(30) not null comment '付款方ID',
  income_obj_id varchar(30) not null comment '收款方ID',
  start_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '开始时间',
  end_time TIMESTAMP NOT NULL COMMENT '结束时间',
  amount decimal(7,2) not null default -1.00 comment '总金额，如物业费，停车费等没有总金额的，填写为-1.00',
  user_id varchar(30) not null comment '创建用户ID',
  create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  status_cd VARCHAR(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考c_status表，S 保存，0, 在用 1失效',
  UNIQUE KEY (fee_id)
);
CREATE INDEX idx_pf_fee_id ON business_pay_fee(fee_id);
CREATE INDEX idx_pf_b_id ON business_pay_fee(b_id);

--

CREATE TABLE business_pay_fee_attrs(
    fee_id VARCHAR(30) NOT NULL COMMENT '费用ID',
    b_id VARCHAR(30) NOT NULL COMMENT '业务Id',
    community_id varchar(30) not null comment '小区ID',
    attr_id VARCHAR(30) NOT NULL COMMENT '属性id',
    spec_cd VARCHAR(12) NOT NULL COMMENT '规格id,参考spec表',
    `value` VARCHAR(50) NOT NULL COMMENT '属性值',
      operate VARCHAR(4) NOT NULL COMMENT '数据状态，添加ADD，修改MOD 删除DEL'
);

CREATE INDEX idx_bpfa_fee_id ON business_pay_fee_attrs(fee_id);
CREATE INDEX idx_bpfa_b_id ON business_pay_fee_attrs(b_id);

-- c_orders_attrs

CREATE TABLE pay_fee_attrs(
    fee_id VARCHAR(30) NOT NULL COMMENT '费用ID',
    b_id VARCHAR(30) NOT NULL COMMENT '业务Id',
    community_id varchar(30) not null comment '小区ID',
    attr_id VARCHAR(30) NOT NULL COMMENT '属性id',
    spec_cd VARCHAR(12) NOT NULL COMMENT '规格id,参考spec表',
    `value` VARCHAR(50) NOT NULL COMMENT '属性值',
     status_cd VARCHAR(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考c_status表，S 保存，0, 在用 1失效',
     UNIQUE KEY (attr_id)
);
CREATE INDEX idx_pfa_fee_id ON business_pay_fee_attrs(fee_id);
CREATE INDEX idx_pfa_b_id ON business_pay_fee_attrs(b_id);

-- 费用明细表
create table business_pay_fee_detail(
    detail_id varchar(30) not null comment '费用明细ID',
    b_id VARCHAR(30) NOT NULL COMMENT '业务Id',
    fee_id varchar(30) not null comment '费用ID',
    community_id varchar(30) not null comment '小区ID',
    cycles int not null comment '周期，以月为单位',
    receivable_amount decimal(7,2) not null comment '应收金额',
    received_amount decimal(7,2) not null comment '实收金额',
    prime_rate decimal(3,2) not null comment '打折率',
    remark VARCHAR(200) NOT NULL COMMENT '备注',
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    operate VARCHAR(4) NOT NULL COMMENT '数据状态，添加ADD，修改MOD 删除DEL'
);
CREATE INDEX idx_bpfd_detail_id ON business_pay_fee_detail(detail_id);
CREATE INDEX idx_bpfd_b_id ON business_pay_fee_detail(b_id);


-- 费用明细表
create table pay_fee_detail(
    detail_id varchar(30) not null comment '费用明细ID',
    b_id VARCHAR(30) NOT NULL COMMENT '业务Id',
    fee_id varchar(30) not null comment '费用ID',
    community_id varchar(30) not null comment '小区ID',
    cycles int not null comment '周期，以月为单位',
    receivable_amount decimal(7,2) not null comment '应收金额',
    received_amount decimal(7,2) not null comment '实收金额',
    prime_rate decimal(3,2) not null comment '打折率',
    remark VARCHAR(200) NOT NULL COMMENT '备注',
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    status_cd VARCHAR(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考c_status表，S 保存，0, 在用 1失效',
    UNIQUE KEY (detail_id)
);
CREATE INDEX idx_pfd_detail_id ON business_pay_fee_detail(detail_id);
CREATE INDEX idx_pfd_b_id ON business_pay_fee_detail(b_id);


-- 费用配置表
create table business_pay_fee_config(
      config_id varchar(30) not null comment '费用ID',
      b_id VARCHAR(30) NOT NULL COMMENT '业务Id',
      community_id varchar(30) not null comment '小区ID',
      fee_type_cd varchar(12) not null comment '费用类型，物业费，停车费',
      square_price decimal(7,2) not null comment '每平米收取的单价',
      additional_amount decimal(7,2) not null comment '附加费用',
      create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
      operate VARCHAR(4) NOT NULL COMMENT '数据状态，添加ADD，修改MOD 删除DEL'
);
CREATE INDEX idx_bpfc_config_id ON business_pay_fee_config(config_id);
CREATE INDEX idx_bpfc_b_id ON business_pay_fee_config(b_id);

-- 费用配置表
create table pay_fee_config(
      config_id varchar(30) not null comment '费用ID',
      b_id VARCHAR(30) NOT NULL COMMENT '业务Id',
      community_id varchar(30) not null comment '小区ID',
      fee_type_cd varchar(12) not null comment '费用类型，物业费，停车费',
      square_price decimal(7,2) not null comment '每平米收取的单价',
      additional_amount decimal(7,2) not null comment '附加费用',
      create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
      status_cd VARCHAR(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考c_status表，S 保存，0, 在用 1失效',
      UNIQUE KEY (config_id)
);
CREATE INDEX idx_pfc_config_id ON business_pay_fee_config(config_id);
CREATE INDEX idx_pfc_b_id ON business_pay_fee_config(b_id);

