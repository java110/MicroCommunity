-- 活动信息表
CREATE TABLE business_activities(
  activities_id VARCHAR(30) NOT NULL COMMENT '活动ID',
  b_id VARCHAR(30) NOT NULL COMMENT '业务Id',
  title VARCHAR(12) NOT NULL COMMENT '活动标题',
  type_cd VARCHAR(4) NOT NULL COMMENT '活动类型，详细查看t_dict 表',
  header_img varchar(200) not null comment '头部照片,照片名称',
  context longtext not null comment '活动内容',
  community_id varchar(30) not null comment '小区ID',
  read_count int not null default 0 comment '阅读数',
  like_count int not null default 0 comment '点赞数',
  collect_count int not null default 0 comment '收藏数',
  user_id VARCHAR(30) NOT NULL COMMENT '创建用户Id',
  user_name VARCHAR(30) NOT NULL COMMENT '创建用户名称',
  start_time timestamp not null default  current_timestamp comment '开始时间',
  end_time timestamp not null comment '结束时间',
  create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  operate VARCHAR(4) NOT NULL COMMENT '数据状态，添加ADD，修改MOD 删除DEL'
);


CREATE INDEX idx_business_activities_id ON business_activities(notice_id);
CREATE INDEX idx_business_activities_b_id ON business_activities(b_id);


CREATE TABLE activities(
  activities_id VARCHAR(30) NOT NULL COMMENT '活动ID',
    b_id VARCHAR(30) NOT NULL COMMENT '业务Id',
    title VARCHAR(12) NOT NULL COMMENT '活动标题',
    type_cd VARCHAR(4) NOT NULL COMMENT '活动类型，详细查看t_dict 表',
    header_img varchar(200) not null comment '头部照片,照片名称',
    context longtext not null comment '活动内容',
    community_id varchar(30) not null comment '小区ID',
    read_count int not null default 0 comment '阅读数',
    like_count int not null default 0 comment '点赞数',
    collect_count int not null default 0 comment '收藏数',
    user_id VARCHAR(30) NOT NULL COMMENT '创建用户Id',
    user_name VARCHAR(30) NOT NULL COMMENT '创建用户名称',
    start_time timestamp not null default  current_timestamp comment '开始时间',
    end_time timestamp not null comment '结束时间',
  create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  status_cd VARCHAR(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考c_status表，S 保存，0, 在用 1失效',
  UNIQUE KEY (activities_id)
);
CREATE INDEX idx_activities_b_id ON activities(b_id);
CREATE UNIQUE INDEX idx_activities_id ON activities(activities_id);


create table activities_event(
    event_id varchar(30) not null comment '事件ID',
    `b_id` varchar(30) NOT NULL COMMENT '业务Id',
    `activities_id` varchar(30) NOT NULL COMMENT '活动ID',
    event_type_cd varchar(12) not null comment '事件类型，999999阅读，888888 收藏 777777 点赞',
    user_id varchar(30) not null comment '人员ID',
    user_name varchar(30) not null comment '人员名称',
    `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `status_cd` varchar(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考t_dict表，0, 在用 1失效',
    KEY `idx_act_event_id` (`event_id`)
);

CREATE TABLE `business_activities_event` (
  event_id varchar(30) not null comment '事件ID',
  `b_id` varchar(30) NOT NULL COMMENT '业务Id',
  `activities_id` varchar(30) NOT NULL COMMENT '活动ID',
  event_type_cd varchar(12) not null comment '事件类型，999999阅读，888888 收藏 777777 点赞',
  user_id varchar(30) not null comment '人员ID',
  user_name varchar(30) not null comment '人员名称',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `operate` varchar(4) NOT NULL COMMENT '数据状态，添加ADD，修改MOD 删除DEL'
);