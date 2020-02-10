--  巡检路线表
CREATE TABLE inspection_route(
    `inspection_route_id` VARCHAR(30) NOT NULL UNIQUE COMMENT 'Route_ID',
    `b_id` varchar(30) NOT NULL COMMENT '业务Id',
    `route_name` VARCHAR(100) NOT NULL COMMENT '路线名称',
    `machine_quantity` int(11) NOT NULL COMMENT '设备数量',
    `check_quantity` int(11) NOT NULL COMMENT '检查项数量',
    `community_id` varchar(30) NOT NULL COMMENT '小区ID',
    `remark` VARCHAR(200) COMMENT '备注说明',
    `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `status_cd` VARCHAR(2) NOT NULL COMMENT '数据状态，详细参考t_dict表0在用1失效',
    KEY `idx_inspection_inspection_route_id` (`inspection_route_id`)
);

CREATE TABLE `business_inspection_route` (
    `inspection_route_id` VARCHAR(30) NOT NULL UNIQUE COMMENT 'RouteID',
    `b_id` varchar(30) NOT NULL COMMENT '业务Id',
    `route_name` VARCHAR(100) NOT NULL COMMENT '路线名称',
    `machine_quantity` int(11) NOT NULL COMMENT '设备数量',
    `check_quantity` int(11) NOT NULL COMMENT '检查项数量',
    `community_id` varchar(30) NOT NULL COMMENT '小区ID',
    `remark` VARCHAR(200) COMMENT '备注说明',
    `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `operate` varchar(4) NOT NULL COMMENT '数据状态，添加ADD，修改MOD 删除DEL',
    KEY `idx_b_inspection_inspection_route_id` (`inspection_route_id`)
);

--  巡检路线和巡检点关联关系表
CREATE TABLE `inspection_route_point_rel` (
    `irp_rel_id` VARCHAR(30) NOT NULL UNIQUE COMMENT 'inspection_route and point relation ID',
    `b_id` varchar(30) NOT NULL COMMENT '业务Id',
    `inspection_route_id` VARCHAR(30) NOT NULL COMMENT '路线ID',
    `inspection_id` varchar(30) NOT NULL COMMENT '巡检点ID',
    `community_id` varchar(30) NOT NULL COMMENT '小区ID',
    `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `status_cd` VARCHAR(2) NOT NULL COMMENT '数据状态，详细参考t_dict表0在用1失效',
    KEY `idx_b_inspection_inspection_route_point_id` (`irp_rel_id`)
);
CREATE TABLE `business_inspection_route_point_rel` (
    `irp_rel_id` VARCHAR(30) NOT NULL UNIQUE COMMENT 'inspection_route and point relation ID',
    `b_id` varchar(30) NOT NULL COMMENT '业务Id',
    `inspection_route_id` VARCHAR(30) NOT NULL COMMENT '路线ID',
    `inspection_id` varchar(30) NOT NULL COMMENT '巡检点ID',
    `community_id` varchar(30) NOT NULL COMMENT '小区ID',
    `remark` VARCHAR(200) COMMENT '备注说明',
    `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `operate` varchar(4) NOT NULL COMMENT '数据状态，添加ADD，修改MOD 删除DEL',
    KEY `idx_b_inspection_inspection_route_point_id` (`irp_rel_id`)
);
