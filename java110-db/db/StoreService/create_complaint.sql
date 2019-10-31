create table complaint(
  `complaint_id` varchar(30) NOT NULL COMMENT '投诉ID',
  `b_id` varchar(30) NOT NULL COMMENT '业务Id',
  `store_id` varchar(30) NOT NULL COMMENT '商户ID',
  `type_cd` varchar(30) NOT NULL COMMENT '投诉类型，见t_dict表',
  `room_id` varchar(100) NOT NULL COMMENT '房屋ID',
  `complaint_name` varchar(200) NOT NULL COMMENT '投诉人',
  `tel` varchar(11) NOT NULL COMMENT '投诉人联系方式',
  `context` longtext NOT NULL COMMENT '投诉内容',
  state varchar(12) not null comment '投诉处理状态，见 t_dict表',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `status_cd` varchar(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考t_dict表，0, 在用 1失效',
  KEY `idx_complaint_complaint_id` (`complaint_id`)
);

CREATE TABLE `business_complaint` (
  `complaint_id` varchar(30) NOT NULL COMMENT '投诉ID',
  `b_id` varchar(30) NOT NULL COMMENT '业务Id',
  `store_id` varchar(30) NOT NULL COMMENT '商户ID',
    `type_cd` varchar(30) NOT NULL COMMENT '投诉类型，见t_dict表',
  `room_id` varchar(100) NOT NULL COMMENT '房间ID',
  `complaint_name` varchar(200) NOT NULL COMMENT '投诉人',
  `tel` varchar(11) NOT NULL COMMENT '投诉人联系方式',
  `context` longtext NOT NULL COMMENT '投诉内容',
  state varchar(12) not null comment '投诉处理状态，见 t_dict表',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `operate` varchar(4) NOT NULL COMMENT '数据状态，添加ADD，修改MOD 删除DEL',
  KEY `idx_b_complaint_complaint_id` (`complaint_id`)
);
