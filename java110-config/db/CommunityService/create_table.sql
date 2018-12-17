-- community 表 过程表

CREATE TABLE business_community(
    community_id VARCHAR(30) NOT NULL COMMENT '小区ID',
    b_id VARCHAR(30) NOT NULL COMMENT '业务Id',
    `name` VARCHAR(100) NOT NULL COMMENT '小区名称',
    address VARCHAR(200) NOT NULL COMMENT '小区地址',
    city_code varchar(12) not null comment '根据定位获取城市编码',
    nearby_landmarks VARCHAR(200) COMMENT '地标，如王府井北60米',
    map_x VARCHAR(20) NOT NULL COMMENT '地区 x坐标',
    map_y VARCHAR(20) NOT NULL COMMENT '地区 y坐标',
    `month` INT NOT NULL COMMENT '月份',
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    operate VARCHAR(3) NOT NULL COMMENT '数据状态，添加ADD，修改MOD 删除DEL'
)
PARTITION BY RANGE (`month`) (
    PARTITION business_community_1 VALUES LESS THAN (2),
    PARTITION business_community_2 VALUES LESS THAN (3),
    PARTITION business_community_3 VALUES LESS THAN (4),
    PARTITION business_community_4 VALUES LESS THAN (5),
    PARTITION business_community_5 VALUES LESS THAN (6),
    PARTITION business_community_6 VALUES LESS THAN (7),
    PARTITION business_community_7 VALUES LESS THAN (8),
    PARTITION business_community_8 VALUES LESS THAN (9),
    PARTITION business_community_9 VALUES LESS THAN (10),
    PARTITION business_community_10 VALUES LESS THAN (11),
    PARTITION business_community_11 VALUES LESS THAN (12),
    PARTITION business_community_12 VALUES LESS THAN (13)
);
CREATE INDEX idx_business_community_id ON business_community(community_id);
CREATE INDEX idx_business_community_b_id ON business_community(b_id);

create table business_community_attr(
    b_id VARCHAR(30) NOT NULL COMMENT '订单ID',
    attr_id VARCHAR(30) NOT NULL COMMENT '属性id',
    community_id VARCHAR(30) NOT NULL COMMENT '用户ID',
    spec_cd VARCHAR(12) NOT NULL COMMENT '规格id,参考spec表',
    value VARCHAR(50) NOT NULL COMMENT '属性值',
    `month` INT NOT NULL comment '月份',
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    operate VARCHAR(3) NOT NULL COMMENT '数据状态，添加ADD，修改MOD 删除DEL'
)
partition BY RANGE (`month`) (
    partition business_community_attr_1 VALUES LESS THAN (2),
    partition business_community_attr_2 VALUES LESS THAN (3),
    partition business_community_attr_3 VALUES LESS THAN (4),
    partition business_community_attr_4 VALUES LESS THAN (5),
    partition business_community_attr_5 VALUES LESS THAN (6),
    partition business_community_attr_6 VALUES LESS THAN (7),
    partition business_community_attr_7 VALUES LESS THAN (8),
    partition business_community_attr_8 VALUES LESS THAN (9),
    partition business_community_attr_9 VALUES LESS THAN (10),
    partition business_community_attr_10 VALUES LESS THAN (11),
    partition business_community_attr_11 VALUES LESS THAN (12),
    partition business_community_attr_12 VALUES LESS THAN (13)
);
CREATE INDEX idx_business_attr_community_id ON business_community_attr(community_id);
CREATE INDEX idx_business_community_attr_b_id ON business_community_attr(b_id);

-- 小区照片
CREATE TABLE business_community_photo(
    community_photo_id VARCHAR(30) NOT NULL COMMENT '商户照片ID',
    b_id VARCHAR(30) NOT NULL COMMENT '业务Id',
    community_id VARCHAR(30) NOT NULL COMMENT '小区ID',
    community_photo_type_cd VARCHAR(12) NOT NULL COMMENT '商户照片类型,T 门头照 I 内景照',
    photo VARCHAR(100) NOT NULL COMMENT '照片',
    `month` INT NOT NULL COMMENT '月份',
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    operate VARCHAR(3) NOT NULL COMMENT '数据状态，添加ADD，修改MOD 删除DEL'
)
PARTITION BY RANGE (`month`) (
    PARTITION business_community_photo_1 VALUES LESS THAN (2),
    PARTITION business_community_photo_2 VALUES LESS THAN (3),
    PARTITION business_community_photo_3 VALUES LESS THAN (4),
    PARTITION business_community_photo_4 VALUES LESS THAN (5),
    PARTITION business_community_photo_5 VALUES LESS THAN (6),
    PARTITION business_community_photo_6 VALUES LESS THAN (7),
    PARTITION business_community_photo_7 VALUES LESS THAN (8),
    PARTITION business_community_photo_8 VALUES LESS THAN (9),
    PARTITION business_community_photo_9 VALUES LESS THAN (10),
    PARTITION business_community_photo_10 VALUES LESS THAN (11),
    PARTITION business_community_photo_11 VALUES LESS THAN (12),
    PARTITION business_community_photo_12 VALUES LESS THAN (13)
);
CREATE INDEX idx_business_photo_community_id ON business_community_photo(community_id);
CREATE INDEX idx_business_community_photo_b_id ON business_community_photo(b_id);

-- 商户成员
create table business_community_member(
    community_member_id varchar(30) not null comment 'ID',
    b_id VARCHAR(30) NOT NULL COMMENT '业务Id',
    community_id VARCHAR(30) NOT NULL COMMENT '小区ID',
    member_id varchar(50) not null  comment '成员ID',
    member_type_cd varchar(12) not null comment '成员类型见 community_member_type表',
    `month` INT NOT NULL comment '月份',
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    operate VARCHAR(3) NOT NULL COMMENT '数据状态，添加ADD，修改MOD 删除DEL'
)
partition BY RANGE (`month`) (
    partition business_community_member_1 VALUES LESS THAN (2),
    partition business_community_member_2 VALUES LESS THAN (3),
    partition business_community_member_3 VALUES LESS THAN (4),
    partition business_community_member_4 VALUES LESS THAN (5),
    partition business_community_member_5 VALUES LESS THAN (6),
    partition business_community_member_6 VALUES LESS THAN (7),
    partition business_community_member_7 VALUES LESS THAN (8),
    partition business_community_member_8 VALUES LESS THAN (9),
    partition business_community_member_9 VALUES LESS THAN (10),
    partition business_community_member_10 VALUES LESS THAN (11),
    partition business_community_member_11 VALUES LESS THAN (12),
    partition business_community_member_12 VALUES LESS THAN (13)
);
CREATE INDEX idx_business_member_community_id ON business_community_member(community_id);
CREATE INDEX idx_business_community_member_b_id ON business_community_member(b_id);



CREATE TABLE s_community(
    community_id VARCHAR(30) NOT NULL COMMENT '小区ID',
    b_id VARCHAR(30) NOT NULL COMMENT '业务Id',
    `name` VARCHAR(100) NOT NULL COMMENT '小区名称',
    address VARCHAR(200) NOT NULL COMMENT '小区地址',
    nearby_landmarks VARCHAR(200) COMMENT '地标，如王府井北60米',
    city_code varchar(12) not null comment '根据定位获取城市编码',
    map_x VARCHAR(20) NOT NULL COMMENT '地区 x坐标',
    map_y VARCHAR(20) NOT NULL COMMENT '地区 y坐标',
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    status_cd VARCHAR(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考c_status表，S 保存，0, 在用 1失效',
    UNIQUE KEY (community_id)
);
CREATE INDEX idx_community_b_id ON s_community(b_id);
CREATE UNIQUE INDEX idx_community_id ON s_community(community_id);

CREATE TABLE s_community_attr(
    b_id VARCHAR(30) NOT NULL COMMENT '订单ID',
    attr_id VARCHAR(30) NOT NULL COMMENT '属性id',
    community_id VARCHAR(30) NOT NULL COMMENT '用户ID',
    spec_cd VARCHAR(12) NOT NULL COMMENT '规格id,参考spec表',
    VALUE VARCHAR(50) NOT NULL COMMENT '属性值',
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    status_cd VARCHAR(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考c_status表，S 保存，0, 在用 1失效',
    UNIQUE KEY (attr_id)
);
CREATE INDEX idx_community_attr_b_id ON s_community_attr(b_id);
CREATE INDEX idx_attr_community_id ON s_community_attr(community_id);

-- 小区照片
CREATE TABLE s_community_photo(
    community_photo_id VARCHAR(30) NOT NULL COMMENT '商户照片ID',
    b_id VARCHAR(30) NOT NULL COMMENT '业务Id',
    community_id VARCHAR(30) NOT NULL COMMENT '小区ID',
    community_photo_type_cd VARCHAR(12) NOT NULL COMMENT '商户照片类型,T 门头照 I 内景照',
    photo VARCHAR(100) NOT NULL COMMENT '照片',
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    status_cd VARCHAR(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考c_status表，S 保存，0, 在用 1失效',
    UNIQUE KEY (community_photo_id)
);
CREATE INDEX idx_community_photo_b_id ON s_community_photo(b_id);
CREATE INDEX idx_community_photo_community_id ON s_community_photo(community_id);
CREATE INDEX idx_community_photo_community_photo_id ON s_community_photo(community_photo_id);


-- 商户成员
create table s_community_member(
    community_member_id varchar(30) not null comment 'ID',
    b_id VARCHAR(30) NOT NULL COMMENT '业务Id',
    community_id VARCHAR(30) NOT NULL COMMENT '小区ID',
    member_id varchar(50) not null  comment '成员ID',
    member_type_cd varchar(12) not null comment '成员类型见 community_member_type表',
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    status_cd VARCHAR(2) NOT NULL default '0' COMMENT '数据状态，详细参考c_status表，S 保存，0, 在用 1失效',
    unique KEY (community_member_id)
);
CREATE INDEX idx_s_community_member_id ON s_community_member(community_id);
CREATE INDEX idx_s_community_member_b_id ON s_community_member(b_id);

create table  community_member_type(
    id INT NOT NULL AUTO_INCREMENT KEY COMMENT 'id',
    member_type_cd VARCHAR(12) NOT NULL UNIQUE COMMENT '编码',
    `name` VARCHAR(50) NOT NULL COMMENT '名称',
    description VARCHAR(200) COMMENT '描述',
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
);
