
-- 单元信息 building 楼宇管理
CREATE TABLE business_junk_requirement(
  junk_requirement_id VARCHAR(30) NOT NULL COMMENT 'ID编码',
  b_id VARCHAR(30) NOT NULL COMMENT '业务Id',
  type_cd VARCHAR(12) NOT NULL COMMENT '类型 旧货还是需求 旧货 222222 需求 333333',
  classification VARCHAR(12) NOT NULL COMMENT '类别，具体查看t_dict 表',
  context VARCHAR(200) NOT NULL COMMENT '内容',
  reference_price DECIMAL(12,2) NOT NULL COMMENT '参考价格',
  community_id varchar(30) not null comment '小区ID',
  publish_user_id VARCHAR(30) NOT NULL COMMENT '发布用户ID',
  publish_user_name VARCHAR(30) NOT NULL COMMENT '发布用户名称',
  publish_user_link VARCHAR(11) NOT NULL COMMENT '联系电话',
  state VARCHAR(12) NOT NULL COMMENT '状态，12001 未审核 13001 审核通过 14001 审核失败  15001 处理完成',
  create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  operate VARCHAR(4) NOT NULL COMMENT '数据状态，添加ADD，修改MOD 删除DEL'
);


CREATE TABLE junk_requirement(
  junk_requirement_id VARCHAR(30) NOT NULL COMMENT 'ID编码',
  b_id VARCHAR(30) NOT NULL COMMENT '业务Id',
  type_cd VARCHAR(12) NOT NULL COMMENT '类型 旧货还是需求 旧货 222222 需求 333333',
  classification VARCHAR(12) NOT NULL COMMENT '类别，具体查看t_dict 表',
  context VARCHAR(200) NOT NULL COMMENT '内容',
  reference_price DECIMAL(12,2) NOT NULL COMMENT '参考价格',
  community_id varchar(30) not null comment '小区ID',
  publish_user_id VARCHAR(30) NOT NULL COMMENT '发布用户ID',
  publish_user_name VARCHAR(30) NOT NULL COMMENT '发布用户名称',
  publish_user_link VARCHAR(11) NOT NULL COMMENT '联系电话',
  state VARCHAR(12) NOT NULL COMMENT '状态，12001 未审核 13001 审核通过 14001 审核失败  15001 处理完成',
  create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  status_cd VARCHAR(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考c_status表，0, 在用 1失效',
  UNIQUE KEY (junk_requirement_id)
);
CREATE INDEX idx_junk_req_id ON junk_requirement(junk_requirement_id);
CREATE INDEX idx_junk_req_b_id ON junk_requirement(b_id);

