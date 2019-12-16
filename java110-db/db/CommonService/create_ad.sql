create table advert(
  `advert_id` varchar(30) NOT NULL COMMENT '广告ID',
  `b_id` varchar(30) NOT NULL COMMENT '业务Id',
  `ad_name` varchar(200) not null comment '广告名称',
  `ad_type_cd` varchar(12) NOT NULL COMMENT '广告类型,门禁机 10000',
  `classify` varchar(12) NOT NULL COMMENT '广告分类 9001 物流 9002 餐饮 9003 旅游 9004 酒店 9005 教育 9006 互联网',
  community_id varchar(30) not null comment '小区ID,主要用来做分片',
  location_type_cd varchar(12) not null default '1000' comment '位置类型，1000 大门 2000 单元门 3000 房屋门 4000 楼栋',
  location_obj_id varchar(30) not null default '-1' comment '对象ID，大门时小区ID，单元门 时单元ID 房屋时房屋ID，楼栋时 楼栋ID',
  state varchar(12) not null default '1000' comment '广告状态，1000 未审核 2000 审核通过 3000 审核拒绝 4000 未播放 5000 播放中',
   seq int not null default 1 comment '播放顺序',
  `start_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '投放时间',
  `end_time` timestamp NOT NULL COMMENT '结束时间',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `status_cd` varchar(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考t_dict表，0, 在用 1失效',
  KEY `idx_msg_id` (`msg_id`)
);

CREATE TABLE `business_advert` (
  `advert_id` varchar(30) NOT NULL COMMENT '广告ID',
  `b_id` varchar(30) NOT NULL COMMENT '业务Id',
  `ad_name` varchar(200) not null comment '广告名称',
  `ad_type_cd` varchar(12) NOT NULL COMMENT '广告类型,门禁机 10000',
  `classify` varchar(12) NOT NULL COMMENT '广告分类 9001 物流 9002 餐饮 9003 旅游 9004 酒店 9005 教育 9006 互联网',
  community_id varchar(30) not null comment '小区ID,主要用来做分片',
  location_type_cd varchar(12) not null default '1000' comment '位置类型，1000 大门 2000 单元门 3000 房屋门 4000 楼栋',
  location_obj_id varchar(30) not null default '-1' comment '对象ID，大门时小区ID，单元门 时单元ID 房屋时房屋ID，楼栋时 楼栋ID',
  state varchar(12) not null default '1000' comment '广告状态，1000 未审核 2000 审核通过 3000 审核拒绝 4000 未播放 5000 播放中',
  seq int not null default 1 comment '播放顺序',
  `start_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '投放时间',
  `end_time` timestamp NOT NULL COMMENT '结束时间',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `operate` varchar(4) NOT NULL COMMENT '数据状态，添加ADD，修改MOD 删除DEL'
);

create table advert_item(
  `advert_item_id` varchar(30) NOT NULL COMMENT '广告ID',
  `b_id` varchar(30) NOT NULL COMMENT '业务Id',
  `advert_id` varchar(200) not null comment '广告名称',
  `item_type_cd` varchar(12) NOT NULL COMMENT '8888 图片 9999 视频',
  `url` varchar(200) NOT NULL COMMENT '图片或视频地址',
  community_id varchar(30) not null comment '小区ID,主要用来做分片',
  seq int not null default 1 comment '播放顺序',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `status_cd` varchar(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考t_dict表，0, 在用 1失效',
  KEY `idx_advert_item` (`advert_item_id`)
);

create table business_advert_item(
  `advert_item_id` varchar(30) NOT NULL COMMENT '广告ID',
  `b_id` varchar(30) NOT NULL COMMENT '业务Id',
  `advert_id` varchar(200) not null comment '广告名称',
  `item_type_cd` varchar(12) NOT NULL COMMENT '8888 图片 9999 视频',
  `url` varchar(200) NOT NULL COMMENT '图片或视频地址',
  community_id varchar(30) not null comment '小区ID,主要用来做分片',
  seq int not null default 1 comment '播放顺序',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `operate` varchar(4) NOT NULL COMMENT '数据状态，添加ADD，修改MOD 删除DEL'
);

