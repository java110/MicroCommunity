--  巡检计划
CREATE TABLE inspection_plan(
    `inspection_plan_id` varchar(30) NOT NULL COMMENT '巡检计划ID',
    `b_id` varchar(30)  NOT NULL COMMENT '业务Id',
    `inspection_plan_name` varchar(100)  NOT NULL COMMENT '巡检计划名称',
    `inspection_route_id` varchar(30)  NOT NULL COMMENT '巡检路线ID',
    `community_id` varchar(30) NOT NULL COMMENT '小区ID',
    `start_time` timestamp NOT NULL  COMMENT '开始时间',
    `end_time` timestamp NOT NULL COMMENT '结束时间',
    `inspection_plan_period` varchar(12) NOT NULL COMMENT '执行周期',
    `staff_id` varchar(30) NOT NULL COMMENT '员工id(执行者)',
    `staff_name` varchar(30) NOT NULL COMMENT '员工名称',
    `sign_type` varchar(12) NOT NULL COMMENT '签到方式',
    `state` VARCHAR(12) COMMENT '当前状态',
    `remark` VARCHAR(200) COMMENT '备注说明',
    `create_user_id` VARCHAR(30) NOT NULL COMMENT '制定人员ID',
    `create_user_name` VARCHAR(50) NOT NULL COMMENT '制定人员姓名',
    `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `status_cd` VARCHAR(2) NOT NULL COMMENT '数据状态，详细参考t_dict表0在用1失效',
    KEY `idx_inspection_plan_id` (`inspection_plan_id`)
);



CREATE TABLE `business_inspection_plan` (
    `inspection_plan_id` varchar(30) NOT NULL COMMENT '巡检计划ID',
    `b_id` varchar(30)  NOT NULL COMMENT '业务Id',
    `inspection_plan_name` varchar(100)  NOT NULL COMMENT '巡检计划名称',
    `inspection_route_id` varchar(30)  NOT NULL COMMENT '巡检路线ID',
    `community_id` varchar(30) NOT NULL COMMENT '小区ID',
    `start_time` timestamp NOT NULL  COMMENT '开始时间',
    `end_time` timestamp NOT NULL COMMENT '结束时间',
    `inspection_plan_period` varchar(12) NOT NULL COMMENT '执行周期',
    `staff_id` varchar(30) NOT NULL COMMENT '员工id(执行者)',
    `staff_name` varchar(30) NOT NULL COMMENT '员工名称',
    `sign_type` varchar(12) NOT NULL COMMENT '签到方式',
    `state` VARCHAR(12) COMMENT '当前状态',
    `remark` VARCHAR(200) COMMENT '备注说明',
    `create_user_id` VARCHAR(30) NOT NULL COMMENT '制定人员ID',
    `create_user_name` VARCHAR(50) NOT NULL COMMENT '制定人员姓名',
    `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `operate` varchar(4) NOT NULL COMMENT '数据状态，添加ADD，修改MOD 删除DEL'
)
