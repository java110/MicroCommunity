-- 采购申请表
create table purchase_apply(
    `apply_order_id` varchar(30) NOT NULL COMMENT '订单号',
    `b_id` varchar(30) NOT NULL COMMENT '业务Id',
    `store_id` varchar(30) NOT NULL COMMENT '商户ID',
    `user_id` varchar(30) NOT NULL COMMENT '使用人ID',
    `entry_person` varchar(30) NOT NULL COMMENT '录入人ID',
    `apply_detail_id` varchar(30) NOT NULL COMMENT '领用物资明细表id',
    `description` varchar(200) NOT NULL COMMENT '申请说明',
    `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `res_order_type` varchar(8) not null comment '出库类型 10000 入库 20000 出库 在t_dict表查看',
    `state` varchar(12) NOT NULL COMMENT '申请状态',
    `status_cd` varchar(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考t_dict表，0, 在用 1失效',
    KEY `idx_apply_id` (`order_id`)
);

CREATE TABLE `business_purchase_apply` (
   `apply_order_id` varchar(30) NOT NULL COMMENT '订单号',
    `b_id` varchar(30) NOT NULL COMMENT '业务Id',
    `store_id` varchar(30) NOT NULL COMMENT '商户ID',
    `user_id` varchar(30) NOT NULL COMMENT '使用人ID',
    `entry_person` varchar(30) NOT NULL COMMENT '录入人ID',
    `apply_detail_id` varchar(30) NOT NULL COMMENT '领用物资明细表id',
    `description` varchar(200) NOT NULL COMMENT '申请说明',
    `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `res_order_type` varchar(8) not null comment '出库类型 10000 入库 20000 出库 在t_dict表查看',
    `state` varchar(12) NOT NULL COMMENT '申请状态',
    `status_cd` varchar(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考t_dict表，0, 在用 1失效',
    `operate` varchar(4) NOT NULL COMMENT '数据状态，添加ADD，修改MOD 删除DEL',
);

-- 采购明细表
create table purchase_apply_detail(
    `apply_order_id` varchar(30) not null comment '订单号',
    `res_id` varchar(30) NOT NULL COMMENT '资源ID',
    `quantity` varchar(100) NOT NULL COMMENT '数量',
    `remark` varchar(100) NOT NULL COMMENT '备注',
    KEY `idx_apply_detail_id` (`detail_id`)
);


