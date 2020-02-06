--  巡检点表
CREATE TABLE inspection_point(
    `inspection_id` VARCHAR(30) NOT NULL UNIQUE COMMENT 'ID', `community_id` varchar(30) NOT NULL COMMENT '小区ID',
    `b_id` varchar(30) NOT NULL COMMENT '业务Id',
    `inspection_name` VARCHAR(100) NOT NULL COMMENT '巡检名称',
    `machine_id` VARCHAR(30) NOT NULL COMMENT '设备ID',
    `community_id` varchar(30) NOT NULL COMMENT '小区ID',
    `remark` VARCHAR(200) COMMENT '备注说明',
    `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `status_cd` VARCHAR(2) NOT NULL COMMENT '数据状态，详细参考t_dict表0在用1失效',
    KEY `idx_inspection_inspection_id` (`inspection_id`)
);




CREATE TABLE `business_inspection_point` (
    `inspection_id` VARCHAR(30) NOT NULL UNIQUE COMMENT 'ID',
    `b_id` varchar(30) NOT NULL COMMENT '业务Id',
    `inspection_name` VARCHAR(100) NOT NULL COMMENT '巡检名称',
    `machine_id` VARCHAR(30) NOT NULL COMMENT '设备ID',
    `community_id` varchar(30) NOT NULL COMMENT '小区ID',
    `remark` VARCHAR(200) COMMENT '备注说明',
    `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `operate` varchar(4) NOT NULL COMMENT '数据状态，添加ADD，修改MOD 删除DEL',
    KEY `idx_b_inspection_inspection_id` (`inspection_id`)
);
