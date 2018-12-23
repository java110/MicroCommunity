-- property 表 过程表

CREATE TABLE business_property(
    property_id VARCHAR(30) NOT NULL COMMENT '物业ID',
    b_id VARCHAR(30) NOT NULL COMMENT '业务Id',
    `name` VARCHAR(100) NOT NULL COMMENT '物业名称',
    address VARCHAR(200) NOT NULL COMMENT '物业地址',
    tel VARCHAR(11) NOT NULL COMMENT '电话',
    nearby_landmarks VARCHAR(200) COMMENT '地标，如王府井北60米',
    map_x VARCHAR(20) NOT NULL COMMENT '地区 x坐标',
    map_y VARCHAR(20) NOT NULL COMMENT '地区 y坐标',
    `month` INT NOT NULL COMMENT '月份',
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    operate VARCHAR(3) NOT NULL COMMENT '数据状态，添加ADD，修改MOD 删除DEL'
)
PARTITION BY RANGE (`month`) (
    PARTITION business_property_1 VALUES LESS THAN (2),
    PARTITION business_property_2 VALUES LESS THAN (3),
    PARTITION business_property_3 VALUES LESS THAN (4),
    PARTITION business_property_4 VALUES LESS THAN (5),
    PARTITION business_property_5 VALUES LESS THAN (6),
    PARTITION business_property_6 VALUES LESS THAN (7),
    PARTITION business_property_7 VALUES LESS THAN (8),
    PARTITION business_property_8 VALUES LESS THAN (9),
    PARTITION business_property_9 VALUES LESS THAN (10),
    PARTITION business_property_10 VALUES LESS THAN (11),
    PARTITION business_property_11 VALUES LESS THAN (12),
    PARTITION business_property_12 VALUES LESS THAN (13)
);
CREATE INDEX idx_business_property_id ON business_property(property_id);
CREATE INDEX idx_business_property_b_id ON business_property(b_id);

create table business_property_attr(
    b_id VARCHAR(30) NOT NULL COMMENT '订单ID',
    attr_id VARCHAR(30) NOT NULL COMMENT '属性id',
    property_id VARCHAR(30) NOT NULL COMMENT '用户ID',
    spec_cd VARCHAR(12) NOT NULL COMMENT '规格id,参考spec表',
    value VARCHAR(50) NOT NULL COMMENT '属性值',
    `month` INT NOT NULL comment '月份',
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    operate VARCHAR(3) NOT NULL COMMENT '数据状态，添加ADD，修改MOD 删除DEL'
)
partition BY RANGE (`month`) (
    partition business_property_attr_1 VALUES LESS THAN (2),
    partition business_property_attr_2 VALUES LESS THAN (3),
    partition business_property_attr_3 VALUES LESS THAN (4),
    partition business_property_attr_4 VALUES LESS THAN (5),
    partition business_property_attr_5 VALUES LESS THAN (6),
    partition business_property_attr_6 VALUES LESS THAN (7),
    partition business_property_attr_7 VALUES LESS THAN (8),
    partition business_property_attr_8 VALUES LESS THAN (9),
    partition business_property_attr_9 VALUES LESS THAN (10),
    partition business_property_attr_10 VALUES LESS THAN (11),
    partition business_property_attr_11 VALUES LESS THAN (12),
    partition business_property_attr_12 VALUES LESS THAN (13)
);
CREATE INDEX idx_business_attr_property_id ON business_property_attr(property_id);
CREATE INDEX idx_business_property_attr_b_id ON business_property_attr(b_id);

-- 物业照片
CREATE TABLE business_property_photo(
    property_photo_id VARCHAR(30) NOT NULL COMMENT '物业照片ID',
    b_id VARCHAR(30) NOT NULL COMMENT '业务Id',
    property_id VARCHAR(30) NOT NULL COMMENT '物业ID',
    property_photo_type_cd VARCHAR(12) NOT NULL COMMENT '物业照片类型,T 门头照 I 内景照',
    photo VARCHAR(100) NOT NULL COMMENT '照片',
    `month` INT NOT NULL COMMENT '月份',
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    operate VARCHAR(3) NOT NULL COMMENT '数据状态，添加ADD，修改MOD 删除DEL'
)
PARTITION BY RANGE (`month`) (
    PARTITION business_property_photo_1 VALUES LESS THAN (2),
    PARTITION business_property_photo_2 VALUES LESS THAN (3),
    PARTITION business_property_photo_3 VALUES LESS THAN (4),
    PARTITION business_property_photo_4 VALUES LESS THAN (5),
    PARTITION business_property_photo_5 VALUES LESS THAN (6),
    PARTITION business_property_photo_6 VALUES LESS THAN (7),
    PARTITION business_property_photo_7 VALUES LESS THAN (8),
    PARTITION business_property_photo_8 VALUES LESS THAN (9),
    PARTITION business_property_photo_9 VALUES LESS THAN (10),
    PARTITION business_property_photo_10 VALUES LESS THAN (11),
    PARTITION business_property_photo_11 VALUES LESS THAN (12),
    PARTITION business_property_photo_12 VALUES LESS THAN (13)
);
CREATE INDEX idx_business_photo_property_id ON business_property_photo(property_id);
CREATE INDEX idx_business_property_photo_b_id ON business_property_photo(b_id);
-- 物业证件
create table business_property_cerdentials(
    property_cerdentials_id varchar(30) not null comment '物业证件ID',
    b_id VARCHAR(30) NOT NULL COMMENT '业务Id',
    property_id VARCHAR(30) NOT NULL COMMENT '物业ID',
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
    partition business_property_cerdentials_1 VALUES LESS THAN (2),
    partition business_property_cerdentials_2 VALUES LESS THAN (3),
    partition business_property_cerdentials_3 VALUES LESS THAN (4),
    partition business_property_cerdentials_4 VALUES LESS THAN (5),
    partition business_property_cerdentials_5 VALUES LESS THAN (6),
    partition business_property_cerdentials_6 VALUES LESS THAN (7),
    partition business_property_cerdentials_7 VALUES LESS THAN (8),
    partition business_property_cerdentials_8 VALUES LESS THAN (9),
    partition business_property_cerdentials_9 VALUES LESS THAN (10),
    partition business_property_cerdentials_10 VALUES LESS THAN (11),
    partition business_property_cerdentials_11 VALUES LESS THAN (12),
    partition business_property_cerdentials_12 VALUES LESS THAN (13)
);
CREATE INDEX idx_business_cerdentials_property_id ON business_property_cerdentials(property_id);
CREATE INDEX idx_business_property_cerdentials_b_id ON business_property_cerdentials(b_id);

-- 物业用户关系
CREATE TABLE business_property_user(
    property_user_id VARCHAR(30) NOT NULL COMMENT '物业用户ID',
    property_id VARCHAR(30) NOT NULL COMMENT '物业ID',
    b_id VARCHAR(30) NOT NULL COMMENT '业务Id',
    user_id VARCHAR(30) NOT NULL COMMENT '用户ID',
    rel_cd varchar(30) not null comment '用户和物业关系 详情查看 property_user_rel表',
    `month` INT NOT NULL COMMENT '月份',
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    operate VARCHAR(3) NOT NULL COMMENT '数据状态，添加ADD，修改MOD 删除DEL'
);

-- 物业费 停车费
CREATE TABLE business_property_fee(
    fee_id VARCHAR(30) NOT NULL COMMENT 'ID',
    b_id VARCHAR(30) NOT NULL COMMENT '业务Id',
    property_id VARCHAR(30) NOT NULL COMMENT '物业ID',
    fee_type_cd VARCHAR(10) NOT NULL COMMENT '费用类型,物业费，停车费 请查看property_fee_type表',
    fee_money VARCHAR(20) NOT NULL COMMENT '费用金额',
    fee_time VARCHAR(10) NOT NULL COMMENT '费用周期，一个月，半年，或一年 请查看property_fee_time表',
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    start_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '开始时间',
    end_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '结束时间',
    `month` INT NOT NULL COMMENT '月份',
    operate VARCHAR(3) NOT NULL COMMENT '数据状态，添加ADD，修改MOD 删除DEL'
);

CREATE TABLE business_property_house(
    house_id VARCHAR(30) NOT NULL COMMENT 'ID',
    property_id VARCHAR(30) NOT NULL COMMENT '物业ID',
    b_id VARCHAR(30) NOT NULL COMMENT '业务Id',
    house_num VARCHAR(30) NOT NULL COMMENT '门牌号',
    house_name VARCHAR(50) NOT NULL COMMENT '住户名称',
    house_phone VARCHAR(11) COMMENT '住户联系号码',
    house_area VARCHAR(30) NOT NULL COMMENT '房屋面积',
    fee_type_cd VARCHAR(10) NOT NULL COMMENT '费用类型 property_fee_type表',
    fee_price VARCHAR(30) NOT NULL COMMENT '费用单价',
    `month` INT NOT NULL COMMENT '月份',
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    operate VARCHAR(3) NOT NULL COMMENT '数据状态，添加ADD，修改MOD 删除DEL'
);

CREATE TABLE business_property_house_attr(
    b_id VARCHAR(30) NOT NULL COMMENT '订单ID',
    attr_id VARCHAR(30) NOT NULL COMMENT '属性id',
    house_id VARCHAR(30) NOT NULL COMMENT '用户ID',
    spec_cd VARCHAR(12) NOT NULL COMMENT '规格id,参考spec表',
    VALUE VARCHAR(50) NOT NULL COMMENT '属性值',
    `month` INT NOT NULL COMMENT '月份',
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    operate VARCHAR(3) NOT NULL COMMENT '数据状态，添加ADD，修改MOD 删除DEL'
);

-- property 表 过程表

CREATE TABLE p_property(
    property_id VARCHAR(30) NOT NULL COMMENT '物业ID',
    b_id VARCHAR(30) NOT NULL COMMENT '业务Id',
    `name` VARCHAR(100) NOT NULL COMMENT '物业名称',
    address VARCHAR(200) NOT NULL COMMENT '物业地址',
    tel VARCHAR(11) NOT NULL COMMENT '电话',
    nearby_landmarks VARCHAR(200) COMMENT '地标，如王府井北60米',
    map_x VARCHAR(20) NOT NULL COMMENT '地区 x坐标',
    map_y VARCHAR(20) NOT NULL COMMENT '地区 y坐标',
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    status_cd VARCHAR(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考c_status表，S 保存，0, 在用 1失效',
    UNIQUE KEY (property_id)
);
CREATE INDEX idx_property_b_id ON p_property(b_id);
CREATE UNIQUE INDEX idx_property_property_id ON p_property(property_id);

CREATE TABLE p_property_attr(
    b_id VARCHAR(30) NOT NULL COMMENT '订单ID',
    attr_id VARCHAR(30) NOT NULL COMMENT '属性id',
    property_id VARCHAR(30) NOT NULL COMMENT '用户ID',
    spec_cd VARCHAR(12) NOT NULL COMMENT '规格id,参考spec表',
    VALUE VARCHAR(50) NOT NULL COMMENT '属性值',
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    status_cd VARCHAR(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考c_status表，S 保存，0, 在用 1失效',
    UNIQUE KEY (attr_id)
);
CREATE INDEX idx_property_attr_b_id ON p_property_attr(b_id);
CREATE INDEX idx_property_attr_property_id ON p_property_attr(property_id);

-- 物业照片
CREATE TABLE p_property_photo(
    property_photo_id VARCHAR(30) NOT NULL COMMENT '物业照片ID',
    b_id VARCHAR(30) NOT NULL COMMENT '业务Id',
    property_id VARCHAR(30) NOT NULL COMMENT '物业ID',
    property_photo_type_cd VARCHAR(12) NOT NULL COMMENT '物业照片类型,T 门头照 I 内景照',
    photo VARCHAR(100) NOT NULL COMMENT '照片',
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    status_cd VARCHAR(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考c_status表，S 保存，0, 在用 1失效',
    UNIQUE KEY (property_photo_id)
);
CREATE INDEX idx_property_photo_b_id ON p_property_photo(b_id);
CREATE INDEX idx_property_photo_property_id ON p_property_photo(property_id);
CREATE INDEX idx_property_photo_property_photo_id ON p_property_photo(property_photo_id);

-- 物业证件
create table p_property_cerdentials(
    property_cerdentials_id varchar(30) not null comment '物业证件ID',
    b_id VARCHAR(30) NOT NULL COMMENT '业务Id',
    property_id VARCHAR(30) NOT NULL COMMENT '物业ID',
    credentials_cd varchar(12) not null comment '证件类型，对应于 credentials表',
    value varchar(50) not null  comment '证件号码',
    validity_period DATE NOT NULL  COMMENT '有效期，如果是长期有效 写成 3000/1/1',
    positive_photo varchar(100) comment '正面照片',
    negative_photo varchar(100) comment '反面照片',
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    status_cd VARCHAR(2) NOT NULL default '0' COMMENT '数据状态，详细参考c_status表，S 保存，0, 在用 1失效',
    unique KEY (property_cerdentials_id)
);



CREATE INDEX idx_property_cerdentials_b_id ON p_property_cerdentials(b_id);
CREATE INDEX idx_property_cerdentials_property_id ON p_property_cerdentials(property_id);
CREATE INDEX idx__property_cerdentials_id ON p_property_cerdentials(property_cerdentials_id);

CREATE TABLE p_property_user(
    property_user_id VARCHAR(30) NOT NULL COMMENT '物业用户ID',
    property_id VARCHAR(30) NOT NULL COMMENT '物业ID',
    b_id VARCHAR(30) NOT NULL COMMENT '业务Id',
    user_id VARCHAR(30) NOT NULL COMMENT '用户ID',
    rel_cd varchar(30) not null comment '用户和物业关系 详情查看 property_user_rel表',
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
        status_cd VARCHAR(2) NOT NULL default '0' COMMENT '数据状态，详细参考c_status表，S 保存，0, 在用 1失效',
        unique KEY (property_user_id)
);
-- 物业费 停车费
CREATE TABLE p_property_fee(
    fee_id VARCHAR(30) NOT NULL COMMENT 'ID',
    b_id VARCHAR(30) NOT NULL COMMENT '业务Id',
    property_id VARCHAR(30) NOT NULL COMMENT '物业ID',
    fee_type_cd VARCHAR(10) NOT NULL COMMENT '费用类型,物业费，停车费 请查看property_fee_type表',
    fee_money VARCHAR(20) NOT NULL COMMENT '费用金额',
    fee_time VARCHAR(10) NOT NULL COMMENT '费用周期，一个月，半年，或一年 请查看property_fee_time表',
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    start_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '开始时间',
    end_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '结束时间',
    status_cd VARCHAR(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考c_status表，S 保存，0, 在用 1失效',
    UNIQUE KEY (fee_id)
);
CREATE INDEX idx_property_fee_fee_id ON p_property_fee(fee_id);
CREATE INDEX idx_property_fee_b_id ON p_property_fee(b_id);

CREATE TABLE p_property_house(
    b_id VARCHAR(30) NOT NULL COMMENT '业务Id',
    house_id VARCHAR(30) NOT NULL COMMENT 'ID',
    property_id VARCHAR(30) NOT NULL COMMENT '物业ID',
    house_num VARCHAR(30) NOT NULL COMMENT '门牌号',
    house_name VARCHAR(50) NOT NULL COMMENT '住户名称',
    house_phone VARCHAR(11) COMMENT '住户联系号码',
    house_area VARCHAR(30) NOT NULL COMMENT '房屋面积',
    fee_type_cd VARCHAR(10) NOT NULL COMMENT '费用类型 property_fee_type表',
    fee_price VARCHAR(30) NOT NULL COMMENT '费用单价',
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    status_cd VARCHAR(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考c_status表，S 保存，0, 在用 1失效',
    UNIQUE KEY (house_id)
);

CREATE TABLE p_property_house_attr(
    b_id VARCHAR(30) NOT NULL COMMENT '业务Id',
    attr_id VARCHAR(30) NOT NULL COMMENT '属性id',
    house_id VARCHAR(30) NOT NULL COMMENT '用户ID',
    spec_cd VARCHAR(12) NOT NULL COMMENT '规格id,参考spec表',
    VALUE VARCHAR(50) NOT NULL COMMENT '属性值',
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    status_cd VARCHAR(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考c_status表，S 保存，0, 在用 1失效',
    UNIQUE KEY (attr_id)
);



-- 收费类型表
CREATE TABLE property_fee_type(
    id INT NOT NULL AUTO_INCREMENT KEY COMMENT 'id',
    domain VARCHAR(20) NOT NULL COMMENT '域',
    fee_type_cd VARCHAR(12) NOT NULL COMMENT '收费类型 物业费 停车费等',
    `name` VARCHAR(50) NOT NULL COMMENT '收费类型编码',
    description VARCHAR(200) COMMENT '描述',
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    UNIQUE KEY (fee_type_cd)
);

CREATE TABLE property_user_rel(
    id INT NOT NULL AUTO_INCREMENT KEY COMMENT 'id',
    rel_cd VARCHAR(12) NOT NULL COMMENT '物业用户关系编码',
    `name` VARCHAR(50) NOT NULL COMMENT '物业用户关系编码名称',
    description VARCHAR(200) COMMENT '描述',
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    UNIQUE KEY (rel_cd)
);


-- 收费周期表
CREATE TABLE property_fee_time(
    id INT NOT NULL AUTO_INCREMENT KEY COMMENT 'id',
    domain VARCHAR(20) NOT NULL COMMENT '域',
    fee_time_cd VARCHAR(12) NOT NULL COMMENT '费用周期编码 一年，半年等',
    `name` VARCHAR(50) NOT NULL COMMENT '收费类型编码',
    description VARCHAR(200) COMMENT '描述',
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    UNIQUE KEY (fee_time_cd)
);

