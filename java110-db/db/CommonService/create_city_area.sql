create table city_area(
  id int not null comment '主键ID',
  area_code varchar(6) not null comment '城市编码',
  area_name varchar(64) not null comment '城市名称',
  area_level varchar(3) not null comment '101 省级 202 市州 303 区县',
  parent_area_code varchar(6) not null comment '父级城市编码',
  parent_area_name varchar(6) not null comment '父级城市编码',
  lon varchar(12) not null comment '经度',
  lat varchar(12) not null comment '维度',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  status_cd varchar(1) not null default '0' comment '数据状态 0 有效 1失效'
);