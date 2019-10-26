create table audit_user(
  `audit_user_id` varchar(30) NOT NULL COMMENT '审核ID',
  `b_id` varchar(30) NOT NULL COMMENT '业务Id',
  `store_id` varchar(30) NOT NULL COMMENT '商户ID,用来做分区',
  `user_id` varchar(100) NOT NULL COMMENT '审核用户ID',
  `user_name` varchar(100) NOT NULL COMMENT '审核用户名称',
  `audit_link` varchar(64) NOT NULL COMMENT '审核环节，建t_dict表 如部门经理审核 ， 财务审核 ，采购人员采购',
  `obj_code` varchar(64) NOT NULL COMMENT '流程对象编码,如采购申请 resource_enter',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `status_cd` varchar(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考t_dict表，0, 在用 1失效',
  KEY `idx_audit_user_id` (`audit_user_id`)
);

CREATE TABLE `business_audit_user` (
 `audit_user_id` varchar(30) NOT NULL COMMENT '审核ID',
  `b_id` varchar(30) NOT NULL COMMENT '业务Id',
  `store_id` varchar(30) NOT NULL COMMENT '商户ID,用来做分区',
  `user_id` varchar(100) NOT NULL COMMENT '审核用户ID',
  `user_name` varchar(100) NOT NULL COMMENT '审核用户名称',
  `audit_link` varchar(64) NOT NULL COMMENT '审核环节，建t_dict表 如部门经理审核 ， 财务审核 ，采购人员采购',
  `obj_code` varchar(64) NOT NULL COMMENT '流程对象编码,如采购申请 resource_enter',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `operate` varchar(4) NOT NULL COMMENT '数据状态，添加ADD，修改MOD 删除DEL'
);