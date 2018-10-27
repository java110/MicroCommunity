-- store 表 过程表

CREATE TABLE business_store(
    store_id VARCHAR(30) NOT NULL COMMENT '商店ID',
    b_id VARCHAR(30) NOT NULL COMMENT '业务Id',
    user_id VARCHAR(30) NOT NULL COMMENT '用户ID',
    `name` VARCHAR(100) NOT NULL COMMENT '店铺名称',
    address VARCHAR(200) NOT NULL COMMENT '店铺地址',
    tel VARCHAR(11) NOT NULL COMMENT '电话',
    store_type_cd VARCHAR(10) NOT NULL COMMENT '店铺种类，对应表 store_type',
    nearby_landmarks VARCHAR(200) COMMENT '地标，如王府井北60米',
    map_x VARCHAR(20) NOT NULL COMMENT '地区 x坐标',
    map_y VARCHAR(20) NOT NULL COMMENT '地区 y坐标',
    `month` INT NOT NULL COMMENT '月份',
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    operate VARCHAR(3) NOT NULL COMMENT '数据状态，添加ADD，修改MOD 删除DEL'
)
PARTITION BY RANGE (`month`) (
    PARTITION business_store_1 VALUES LESS THAN (2),
    PARTITION business_store_2 VALUES LESS THAN (3),
    PARTITION business_store_3 VALUES LESS THAN (4),
    PARTITION business_store_4 VALUES LESS THAN (5),
    PARTITION business_store_5 VALUES LESS THAN (6),
    PARTITION business_store_6 VALUES LESS THAN (7),
    PARTITION business_store_7 VALUES LESS THAN (8),
    PARTITION business_store_8 VALUES LESS THAN (9),
    PARTITION business_store_9 VALUES LESS THAN (10),
    PARTITION business_store_10 VALUES LESS THAN (11),
    PARTITION business_store_11 VALUES LESS THAN (12),
    PARTITION business_store_12 VALUES LESS THAN (13)
);
CREATE INDEX idx_business_store_store_id ON business_store(store_id);
CREATE INDEX idx_business_store_b_id ON business_store(b_id);

create table business_store_attr(
    b_id VARCHAR(30) NOT NULL COMMENT '订单ID',
    attr_id VARCHAR(30) NOT NULL COMMENT '属性id',
    store_id VARCHAR(30) NOT NULL COMMENT '用户ID',
    spec_cd VARCHAR(12) NOT NULL COMMENT '规格id,参考spec表',
    value VARCHAR(50) NOT NULL COMMENT '属性值',
    `month` INT NOT NULL comment '月份',
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    operate VARCHAR(3) NOT NULL COMMENT '数据状态，添加ADD，修改MOD 删除DEL'
)
partition BY RANGE (`month`) (
    partition business_store_attr_1 VALUES LESS THAN (2),
    partition business_store_attr_2 VALUES LESS THAN (3),
    partition business_store_attr_3 VALUES LESS THAN (4),
    partition business_store_attr_4 VALUES LESS THAN (5),
    partition business_store_attr_5 VALUES LESS THAN (6),
    partition business_store_attr_6 VALUES LESS THAN (7),
    partition business_store_attr_7 VALUES LESS THAN (8),
    partition business_store_attr_8 VALUES LESS THAN (9),
    partition business_store_attr_9 VALUES LESS THAN (10),
    partition business_store_attr_10 VALUES LESS THAN (11),
    partition business_store_attr_11 VALUES LESS THAN (12),
    partition business_store_attr_12 VALUES LESS THAN (13)
);
CREATE INDEX idx_business_store_attr_store_id ON business_store_attr(store_id);
CREATE INDEX idx_business_store_attr_b_id ON business_store_attr(b_id);

-- 商店照片
CREATE TABLE business_store_photo(
    store_photo_id VARCHAR(30) NOT NULL COMMENT '商户照片ID',
    b_id VARCHAR(30) NOT NULL COMMENT '业务Id',
    store_id VARCHAR(30) NOT NULL COMMENT '商店ID',
    store_photo_type_cd VARCHAR(12) NOT NULL COMMENT '商户照片类型,T 门头照 I 内景照',
    photo VARCHAR(100) NOT NULL COMMENT '照片',
    `month` INT NOT NULL COMMENT '月份',
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    operate VARCHAR(3) NOT NULL COMMENT '数据状态，添加ADD，修改MOD 删除DEL'
)
PARTITION BY RANGE (`month`) (
    PARTITION business_store_photo_1 VALUES LESS THAN (2),
    PARTITION business_store_photo_2 VALUES LESS THAN (3),
    PARTITION business_store_photo_3 VALUES LESS THAN (4),
    PARTITION business_store_photo_4 VALUES LESS THAN (5),
    PARTITION business_store_photo_5 VALUES LESS THAN (6),
    PARTITION business_store_photo_6 VALUES LESS THAN (7),
    PARTITION business_store_photo_7 VALUES LESS THAN (8),
    PARTITION business_store_photo_8 VALUES LESS THAN (9),
    PARTITION business_store_photo_9 VALUES LESS THAN (10),
    PARTITION business_store_photo_10 VALUES LESS THAN (11),
    PARTITION business_store_photo_11 VALUES LESS THAN (12),
    PARTITION business_store_photo_12 VALUES LESS THAN (13)
);
CREATE INDEX idx_business_store_photo_store_id ON business_store_photo(store_id);
CREATE INDEX idx_business_store_photo_b_id ON business_store_photo(b_id);
-- 商户证件
create table business_store_cerdentials(
    store_cerdentials_id varchar(30) not null comment '商户证件ID',
    b_id VARCHAR(30) NOT NULL COMMENT '业务Id',
    store_id VARCHAR(30) NOT NULL COMMENT '商店ID',
    credentials_cd varchar(12) not null comment '证件类型，对应于 credentials表',
    value varchar(50) not null  comment '证件号码',
    validity_period DATE NOT NULL  COMMENT '有效期，如果是长期有效 写成 3000/1/1',
    positive_photo varchar(100) comment '正面照片',
    negative_photo varchar(100) comment '反面照片',
    `month` INT NOT NULL comment '月份',
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    operate VARCHAR(3) NOT NULL COMMENT '数据状态，添加ADD，修改MOD 删除DEL'
)
partition BY RANGE (`month`) (
    partition business_store_cerdentials_1 VALUES LESS THAN (2),
    partition business_store_cerdentials_2 VALUES LESS THAN (3),
    partition business_store_cerdentials_3 VALUES LESS THAN (4),
    partition business_store_cerdentials_4 VALUES LESS THAN (5),
    partition business_store_cerdentials_5 VALUES LESS THAN (6),
    partition business_store_cerdentials_6 VALUES LESS THAN (7),
    partition business_store_cerdentials_7 VALUES LESS THAN (8),
    partition business_store_cerdentials_8 VALUES LESS THAN (9),
    partition business_store_cerdentials_9 VALUES LESS THAN (10),
    partition business_store_cerdentials_10 VALUES LESS THAN (11),
    partition business_store_cerdentials_11 VALUES LESS THAN (12),
    partition business_store_cerdentials_12 VALUES LESS THAN (13)
);
CREATE INDEX idx_business_store_cerdentials_store_id ON business_store_cerdentials(store_id);
CREATE INDEX idx_business_store_cerdentials_b_id ON business_store_cerdentials(b_id);

-- 商户成员
create table business_member_store(
    member_store_id varchar(30) not null comment 'ID',
    b_id VARCHAR(30) NOT NULL COMMENT '业务Id',
    store_id VARCHAR(30) NOT NULL COMMENT '商店ID',
    member_id varchar(50) not null  comment '商户成员ID',
    `month` INT NOT NULL comment '月份',
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    operate VARCHAR(3) NOT NULL COMMENT '数据状态，添加ADD，修改MOD 删除DEL'
)
partition BY RANGE (`month`) (
    partition business_member_store_1 VALUES LESS THAN (2),
    partition business_member_store_2 VALUES LESS THAN (3),
    partition business_member_store_3 VALUES LESS THAN (4),
    partition business_member_store_4 VALUES LESS THAN (5),
    partition business_member_store_5 VALUES LESS THAN (6),
    partition business_member_store_6 VALUES LESS THAN (7),
    partition business_member_store_7 VALUES LESS THAN (8),
    partition business_member_store_8 VALUES LESS THAN (9),
    partition business_member_store_9 VALUES LESS THAN (10),
    partition business_member_store_10 VALUES LESS THAN (11),
    partition business_member_store_11 VALUES LESS THAN (12),
    partition business_member_store_12 VALUES LESS THAN (13)
);
CREATE INDEX idx_business_member_store_store_id ON business_member_store(store_id);
CREATE INDEX idx_business_member_store_b_id ON business_member_store(b_id);



CREATE TABLE s_store(
    store_id VARCHAR(30) NOT NULL COMMENT '商店ID',
    b_id VARCHAR(30) NOT NULL COMMENT '业务Id',
    user_id VARCHAR(30) NOT NULL COMMENT '用户ID',
    `name` VARCHAR(100) NOT NULL COMMENT '店铺名称',
    address VARCHAR(200) NOT NULL COMMENT '店铺地址',
    tel VARCHAR(11) NOT NULL COMMENT '电话',
    store_type_cd VARCHAR(10) NOT NULL COMMENT '店铺种类',
    nearby_landmarks VARCHAR(200) COMMENT '地标，如王府井北60米',
    map_x VARCHAR(20) NOT NULL COMMENT '地区 x坐标',
    map_y VARCHAR(20) NOT NULL COMMENT '地区 y坐标',
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    status_cd VARCHAR(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考c_status表，S 保存，0, 在用 1失效',
    UNIQUE KEY (store_id)
);
CREATE INDEX idx_store_b_id ON s_store(b_id);
CREATE UNIQUE INDEX idx_store_store_id ON s_store(store_id);

CREATE TABLE s_store_attr(
    b_id VARCHAR(30) NOT NULL COMMENT '订单ID',
    attr_id VARCHAR(30) NOT NULL COMMENT '属性id',
    store_id VARCHAR(30) NOT NULL COMMENT '用户ID',
    spec_cd VARCHAR(12) NOT NULL COMMENT '规格id,参考spec表',
    VALUE VARCHAR(50) NOT NULL COMMENT '属性值',
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    status_cd VARCHAR(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考c_status表，S 保存，0, 在用 1失效',
    UNIQUE KEY (attr_id)
);
CREATE INDEX idx_store_attr_b_id ON s_store_attr(b_id);
CREATE INDEX idx_store_attr_store_id ON s_store_attr(store_id);

-- 商店照片
CREATE TABLE s_store_photo(
    store_photo_id VARCHAR(30) NOT NULL COMMENT '商户照片ID',
    b_id VARCHAR(30) NOT NULL COMMENT '业务Id',
    store_id VARCHAR(30) NOT NULL COMMENT '商店ID',
    store_photo_type_cd VARCHAR(12) NOT NULL COMMENT '商户照片类型,T 门头照 I 内景照',
    photo VARCHAR(100) NOT NULL COMMENT '照片',
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    status_cd VARCHAR(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考c_status表，S 保存，0, 在用 1失效',
    UNIQUE KEY (store_photo_id)
);
CREATE INDEX idx_store_photo_b_id ON s_store_photo(b_id);
CREATE INDEX idx_store_photo_store_id ON s_store_photo(store_id);
CREATE INDEX idx_store_photo_store_photo_id ON s_store_photo(store_photo_id);

-- 商户证件
create table s_store_cerdentials(
    store_cerdentials_id varchar(30) not null comment '商户证件ID',
    b_id VARCHAR(30) NOT NULL COMMENT '业务Id',
    store_id VARCHAR(30) NOT NULL COMMENT '商店ID',
    credentials_cd varchar(12) not null comment '证件类型，对应于 credentials表',
    value varchar(50) not null  comment '证件号码',
    validity_period DATE NOT NULL  COMMENT '有效期，如果是长期有效 写成 3000/1/1',
    positive_photo varchar(100) comment '正面照片',
    negative_photo varchar(100) comment '反面照片',
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    status_cd VARCHAR(2) NOT NULL default '0' COMMENT '数据状态，详细参考c_status表，S 保存，0, 在用 1失效',
    unique KEY (store_cerdentials_id)
);

CREATE INDEX idx_store_cerdentials_b_id ON s_store_cerdentials(b_id);
CREATE INDEX idx_store_cerdentials_store_id ON s_store_cerdentials(store_id);
CREATE INDEX idx_store_cerdentials_store_cerdentials_id ON s_store_cerdentials(store_cerdentials_id);

-- 商户成员
create table s_member_store(
    member_store_id varchar(30) not null comment 'ID',
    b_id VARCHAR(30) NOT NULL COMMENT '业务Id',
    store_id VARCHAR(30) NOT NULL COMMENT '商店ID',
    member_id varchar(50) not null  comment '商户成员ID',
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    status_cd VARCHAR(2) NOT NULL default '0' COMMENT '数据状态，详细参考c_status表，S 保存，0, 在用 1失效',
    unique KEY (member_store_id)
)
CREATE INDEX idx_s_member_store_store_id ON s_member_store(store_id);
CREATE INDEX idx_s_member_store_b_id ON s_member_store(b_id);

-- 店铺种类
create table store_type(
    id INT NOT NULL AUTO_INCREMENT KEY COMMENT 'id',
    domain varchar(20) not null comment '域',
    store_type_cd VARCHAR(12) NOT NULL COMMENT '店铺编码',
    `name` VARCHAR(50) NOT NULL COMMENT '店铺种类编码',
    description VARCHAR(200) COMMENT '描述',
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    unique KEY (store_type_cd)
);

insert into store_type(domain,store_type_cd,name,description) values('CORE_STROE','870000000001','小区','小区');
insert into store_type(domain,store_type_cd,name,description) values('CORE_STROE','870000000002','物业','物业');
insert into store_type(domain,store_type_cd,name,description) values('CORE_STROE','870000000003','物流公司','物流公司');
insert into store_type(domain,store_type_cd,name,description) values('APP_VIEW','870181027001','饭店','饭店');
insert into store_type(domain,store_type_cd,name,description) values('APP_VIEW','870181027002','餐厅','餐厅');
insert into store_type(domain,store_type_cd,name,description) values('APP_VIEW','870181027003','火锅店','火锅店');
insert into store_type(domain,store_type_cd,name,description) values('APP_VIEW','870181027004','超市','超市');
