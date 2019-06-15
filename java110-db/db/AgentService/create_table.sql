-- agent 表 过程表

CREATE TABLE business_agent(
    agent_id VARCHAR(30) NOT NULL COMMENT '代理商ID',
    b_id VARCHAR(30) NOT NULL COMMENT '业务Id',
    `name` VARCHAR(100) NOT NULL COMMENT '代理商名称',
    address VARCHAR(200) NOT NULL COMMENT '代理商地址',
    tel VARCHAR(11) NOT NULL COMMENT '电话',
    nearby_landmarks VARCHAR(200) COMMENT '地标，如王府井北60米',
    map_x VARCHAR(20) NOT NULL COMMENT '地区 x坐标',
    map_y VARCHAR(20) NOT NULL COMMENT '地区 y坐标',
    `month` INT NOT NULL COMMENT '月份',
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    operate VARCHAR(3) NOT NULL COMMENT '数据状态，添加ADD，修改MOD 删除DEL'
)
PARTITION BY RANGE (`month`) (
    PARTITION business_agent_1 VALUES LESS THAN (2),
    PARTITION business_agent_2 VALUES LESS THAN (3),
    PARTITION business_agent_3 VALUES LESS THAN (4),
    PARTITION business_agent_4 VALUES LESS THAN (5),
    PARTITION business_agent_5 VALUES LESS THAN (6),
    PARTITION business_agent_6 VALUES LESS THAN (7),
    PARTITION business_agent_7 VALUES LESS THAN (8),
    PARTITION business_agent_8 VALUES LESS THAN (9),
    PARTITION business_agent_9 VALUES LESS THAN (10),
    PARTITION business_agent_10 VALUES LESS THAN (11),
    PARTITION business_agent_11 VALUES LESS THAN (12),
    PARTITION business_agent_12 VALUES LESS THAN (13)
);
CREATE INDEX idx_business_agent_id ON business_agent(agent_id);
CREATE INDEX idx_business_agent_b_id ON business_agent(b_id);

create table business_agent_attr(
    b_id VARCHAR(30) NOT NULL COMMENT '订单ID',
    attr_id VARCHAR(30) NOT NULL COMMENT '属性id',
    agent_id VARCHAR(30) NOT NULL COMMENT '用户ID',
    spec_cd VARCHAR(12) NOT NULL COMMENT '规格id,参考spec表',
    value VARCHAR(50) NOT NULL COMMENT '属性值',
    `month` INT NOT NULL comment '月份',
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    operate VARCHAR(3) NOT NULL COMMENT '数据状态，添加ADD，修改MOD 删除DEL'
)
partition BY RANGE (`month`) (
    partition business_agent_attr_1 VALUES LESS THAN (2),
    partition business_agent_attr_2 VALUES LESS THAN (3),
    partition business_agent_attr_3 VALUES LESS THAN (4),
    partition business_agent_attr_4 VALUES LESS THAN (5),
    partition business_agent_attr_5 VALUES LESS THAN (6),
    partition business_agent_attr_6 VALUES LESS THAN (7),
    partition business_agent_attr_7 VALUES LESS THAN (8),
    partition business_agent_attr_8 VALUES LESS THAN (9),
    partition business_agent_attr_9 VALUES LESS THAN (10),
    partition business_agent_attr_10 VALUES LESS THAN (11),
    partition business_agent_attr_11 VALUES LESS THAN (12),
    partition business_agent_attr_12 VALUES LESS THAN (13)
);
CREATE INDEX idx_business_attr_agent_id ON business_agent_attr(agent_id);
CREATE INDEX idx_business_agent_attr_b_id ON business_agent_attr(b_id);

-- 代理商照片
CREATE TABLE business_agent_photo(
    agent_photo_id VARCHAR(30) NOT NULL COMMENT '代理商照片ID',
    b_id VARCHAR(30) NOT NULL COMMENT '业务Id',
    agent_id VARCHAR(30) NOT NULL COMMENT '代理商ID',
    agent_photo_type_cd VARCHAR(12) NOT NULL COMMENT '代理商照片类型,T 门头照 I 内景照',
    photo VARCHAR(100) NOT NULL COMMENT '照片',
    `month` INT NOT NULL COMMENT '月份',
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    operate VARCHAR(3) NOT NULL COMMENT '数据状态，添加ADD，修改MOD 删除DEL'
)
PARTITION BY RANGE (`month`) (
    PARTITION business_agent_photo_1 VALUES LESS THAN (2),
    PARTITION business_agent_photo_2 VALUES LESS THAN (3),
    PARTITION business_agent_photo_3 VALUES LESS THAN (4),
    PARTITION business_agent_photo_4 VALUES LESS THAN (5),
    PARTITION business_agent_photo_5 VALUES LESS THAN (6),
    PARTITION business_agent_photo_6 VALUES LESS THAN (7),
    PARTITION business_agent_photo_7 VALUES LESS THAN (8),
    PARTITION business_agent_photo_8 VALUES LESS THAN (9),
    PARTITION business_agent_photo_9 VALUES LESS THAN (10),
    PARTITION business_agent_photo_10 VALUES LESS THAN (11),
    PARTITION business_agent_photo_11 VALUES LESS THAN (12),
    PARTITION business_agent_photo_12 VALUES LESS THAN (13)
);
CREATE INDEX idx_business_photo_agent_id ON business_agent_photo(agent_id);
CREATE INDEX idx_business_agent_photo_b_id ON business_agent_photo(b_id);
-- 代理商证件
create table business_agent_cerdentials(
    agent_cerdentials_id varchar(30) not null comment '代理商证件ID',
    b_id VARCHAR(30) NOT NULL COMMENT '业务Id',
    agent_id VARCHAR(30) NOT NULL COMMENT '代理商ID',
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
    partition business_agent_cerdentials_1 VALUES LESS THAN (2),
    partition business_agent_cerdentials_2 VALUES LESS THAN (3),
    partition business_agent_cerdentials_3 VALUES LESS THAN (4),
    partition business_agent_cerdentials_4 VALUES LESS THAN (5),
    partition business_agent_cerdentials_5 VALUES LESS THAN (6),
    partition business_agent_cerdentials_6 VALUES LESS THAN (7),
    partition business_agent_cerdentials_7 VALUES LESS THAN (8),
    partition business_agent_cerdentials_8 VALUES LESS THAN (9),
    partition business_agent_cerdentials_9 VALUES LESS THAN (10),
    partition business_agent_cerdentials_10 VALUES LESS THAN (11),
    partition business_agent_cerdentials_11 VALUES LESS THAN (12),
    partition business_agent_cerdentials_12 VALUES LESS THAN (13)
);
CREATE INDEX idx_business_cerdentials_agent_id ON business_agent_cerdentials(agent_id);
CREATE INDEX idx_business_agent_cerdentials_b_id ON business_agent_cerdentials(b_id);

-- 代理商用户关系
CREATE TABLE business_agent_user(
    agent_user_id VARCHAR(30) NOT NULL COMMENT '代理商用户ID',
    agent_id VARCHAR(30) NOT NULL COMMENT '代理商ID',
    b_id VARCHAR(30) NOT NULL COMMENT '业务Id',
    user_id VARCHAR(30) NOT NULL COMMENT '用户ID',
    rel_cd varchar(30) not null comment '用户和代理商关系 详情查看 agent_user_rel表',
    `month` INT NOT NULL COMMENT '月份',
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    operate VARCHAR(3) NOT NULL COMMENT '数据状态，添加ADD，修改MOD 删除DEL'
);

-- agent 表 过程表

CREATE TABLE a_agent(
    agent_id VARCHAR(30) NOT NULL COMMENT '代理商ID',
    b_id VARCHAR(30) NOT NULL COMMENT '业务Id',
    `name` VARCHAR(100) NOT NULL COMMENT '代理商名称',
    address VARCHAR(200) NOT NULL COMMENT '代理商地址',
    tel VARCHAR(11) NOT NULL COMMENT '电话',
    nearby_landmarks VARCHAR(200) COMMENT '地标，如王府井北60米',
    map_x VARCHAR(20) NOT NULL COMMENT '地区 x坐标',
    map_y VARCHAR(20) NOT NULL COMMENT '地区 y坐标',
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    status_cd VARCHAR(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考c_status表，S 保存，0, 在用 1失效',
    UNIQUE KEY (agent_id)
);
CREATE INDEX idx_agent_b_id ON a_agent(b_id);
CREATE UNIQUE INDEX idx_agent_agent_id ON a_agent(agent_id);

CREATE TABLE a_agent_attr(
    b_id VARCHAR(30) NOT NULL COMMENT '订单ID',
    attr_id VARCHAR(30) NOT NULL COMMENT '属性id',
    agent_id VARCHAR(30) NOT NULL COMMENT '用户ID',
    spec_cd VARCHAR(12) NOT NULL COMMENT '规格id,参考spec表',
    VALUE VARCHAR(50) NOT NULL COMMENT '属性值',
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    status_cd VARCHAR(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考c_status表，S 保存，0, 在用 1失效',
    UNIQUE KEY (attr_id)
);
CREATE INDEX idx_agent_attr_b_id ON a_agent_attr(b_id);
CREATE INDEX idx_agent_attr_agent_id ON a_agent_attr(agent_id);

-- 代理商照片
CREATE TABLE a_agent_photo(
    agent_photo_id VARCHAR(30) NOT NULL COMMENT '代理商照片ID',
    b_id VARCHAR(30) NOT NULL COMMENT '业务Id',
    agent_id VARCHAR(30) NOT NULL COMMENT '代理商ID',
    agent_photo_type_cd VARCHAR(12) NOT NULL COMMENT '代理商照片类型,T 门头照 I 内景照',
    photo VARCHAR(100) NOT NULL COMMENT '照片',
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    status_cd VARCHAR(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考c_status表，S 保存，0, 在用 1失效',
    UNIQUE KEY (agent_photo_id)
);
CREATE INDEX idx_agent_photo_b_id ON a_agent_photo(b_id);
CREATE INDEX idx_agent_photo_agent_id ON a_agent_photo(agent_id);
CREATE INDEX idx_agent_photo_agent_photo_id ON a_agent_photo(agent_photo_id);

-- 代理商证件
create table a_agent_cerdentials(
    agent_cerdentials_id varchar(30) not null comment '代理商证件ID',
    b_id VARCHAR(30) NOT NULL COMMENT '业务Id',
    agent_id VARCHAR(30) NOT NULL COMMENT '代理商ID',
    credentials_cd varchar(12) not null comment '证件类型，对应于 credentials表',
    value varchar(50) not null  comment '证件号码',
    validity_period DATE NOT NULL  COMMENT '有效期，如果是长期有效 写成 3000/1/1',
    positive_photo varchar(100) comment '正面照片',
    negative_photo varchar(100) comment '反面照片',
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    status_cd VARCHAR(2) NOT NULL default '0' COMMENT '数据状态，详细参考c_status表，S 保存，0, 在用 1失效',
    unique KEY (agent_cerdentials_id)
);



CREATE INDEX idx_agent_cerdentials_b_id ON a_agent_cerdentials(b_id);
CREATE INDEX idx_agent_cerdentials_agent_id ON a_agent_cerdentials(agent_id);
CREATE INDEX idx__agent_cerdentials_id ON a_agent_cerdentials(agent_cerdentials_id);

CREATE TABLE a_agent_user(
    agent_user_id VARCHAR(30) NOT NULL COMMENT '代理商用户ID',
    agent_id VARCHAR(30) NOT NULL COMMENT '代理商ID',
    b_id VARCHAR(30) NOT NULL COMMENT '业务Id',
    user_id VARCHAR(30) NOT NULL COMMENT '用户ID',
    rel_cd varchar(30) not null comment '用户和代理商关系 详情查看 agent_user_rel表',
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
        status_cd VARCHAR(2) NOT NULL default '0' COMMENT '数据状态，详细参考c_status表，S 保存，0, 在用 1失效',
        unique KEY (agent_user_id)
);

CREATE TABLE agent_user_rel(
    id INT NOT NULL AUTO_INCREMENT KEY COMMENT 'id',
    rel_cd VARCHAR(12) NOT NULL COMMENT '代理商用户关系编码',
    `name` VARCHAR(50) NOT NULL COMMENT '代理商用户关系编码名称',
    description VARCHAR(200) COMMENT '描述',
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    UNIQUE KEY (rel_cd)
);
