-- business_user

create table business_user(
    id INT NOT NULL AUTO_INCREMENT KEY COMMENT 'id',
    b_id VARCHAR(30) NOT NULL COMMENT '业务Id',
    user_id VARCHAR(30) NOT NULL COMMENT '用户ID',
    name VARCHAR(50) NOT NULL COMMENT '名称',
    email VARCHAR(30) COMMENT '邮箱地址',
    address VARCHAR(200) COMMENT '现居住地址',
    password varchar(128) comment '用户密码，加密过后',
    location_cd varchar(8) comment '用户地区，编码详见 u_location',
    age int comment '用户年龄',
    sex varchar(1) comment '性别，0表示男孩 1表示女孩',
    tel varchar(11) comment '用户手机',
    level_cd varchar(2) not null default '0' comment '用户级别,关联user_level',
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    operate VARCHAR(3) NOT NULL COMMENT '数据状态，添加ADD，修改MOD 删除DEL'
);

-- user_level

CREATE TABLE user_level(
    id INT NOT NULL AUTO_INCREMENT KEY COMMENT 'id',
    level_cd VARCHAR(4) NOT NULL UNIQUE COMMENT '用户级别',
    `name` VARCHAR(50) NOT NULL COMMENT '名称',
    description VARCHAR(200) COMMENT '描述',
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
);

insert into user_level(level_cd,name,description) values('0','普通用户','普通用户');

-- u_location
CREATE TABLE u_location(
    id INT NOT NULL AUTO_INCREMENT KEY COMMENT 'id',
    location_cd VARCHAR(4) NOT NULL UNIQUE COMMENT '区域编码',
    level VARCHAR(4) not null comment '区域级别，1 表示一级地区',
    `name` VARCHAR(50) NOT NULL COMMENT '区域名称',
    description VARCHAR(200) COMMENT '描述',
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
);

-- business_user_attr
CREATE TABLE business_user_attr(
    b_id VARCHAR(30) NOT NULL COMMENT '订单ID',
    attr_id VARCHAR(30) NOT NULL COMMENT '属性id',
    user_id VARCHAR(30) NOT NULL COMMENT '用户ID',
    spec_cd VARCHAR(12) NOT NULL COMMENT '规格id,参考spec表',
    value VARCHAR(50) NOT NULL COMMENT '属性值',
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    operate VARCHAR(3) NOT NULL COMMENT '数据状态，添加ADD，修改MOD 删除DEL'
);

-- u_user
create table u_user(
    id INT NOT NULL AUTO_INCREMENT KEY COMMENT 'id',
    user_id VARCHAR(30) NOT NULL UNIQUE COMMENT '用户ID',
    name VARCHAR(50) NOT NULL COMMENT '名称',
    email VARCHAR(30) COMMENT '邮箱地址',
    address VARCHAR(200) COMMENT '现居住地址',
    password varchar(128) comment '用户密码，加密过后',
    location_cd varchar(8) comment '用户地区，编码详见 u_location',
    age int comment '用户年龄',
    sex varchar(1) comment '性别，0表示男孩 1表示女孩',
    tel varchar(11) comment '用户手机',
    level_cd varchar(2) not null default '0' comment '用户级别,关联user_level',
    b_id VARCHAR(30) NOT NULL COMMENT '业务Id',
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    status_cd VARCHAR(2) NOT NULL default '0' COMMENT '数据状态，详细参考c_status表，S 保存，0, 在用 1失效'
);


-- u_user_attr

CREATE TABLE u_user_attr(
    id INT NOT NULL AUTO_INCREMENT KEY COMMENT 'id',
    attr_id VARCHAR(30) NOT NULL COMMENT '属性id',
    user_id VARCHAR(30) NOT NULL COMMENT '用户ID',
    spec_cd VARCHAR(12) NOT NULL COMMENT '规格id,参考spec表',
    value VARCHAR(50) NOT NULL COMMENT '属性值',
    b_id VARCHAR(30) NOT NULL COMMENT '业务ID',
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    status_cd VARCHAR(2) NOT NULL default '0' COMMENT '数据状态，详细参考c_status表，S 保存，0在用，1失效'
);
-- 用户 物流地址
create table business_user_address(
    address_id VARCHAR(30) NOT NULL COMMENT '地址ID',
    b_id VARCHAR(30) NOT NULL COMMENT '业务Id',
    user_id VARCHAR(30) NOT NULL COMMENT '用户ID',
    tel varchar(11) not null comment '电话',
    postal_code varchar(10) not null comment '邮政编码',
    address varchar(200) not null comment '地址',
    is_default varchar(1) not null comment '是否为默认地址 1，表示默认地址 0 或空不是默认地址',
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    operate VARCHAR(3) NOT NULL COMMENT '数据状态，添加ADD，修改MOD 删除DEL'
);

-- u_user_address
create table u_user_address(
    id INT NOT NULL AUTO_INCREMENT KEY COMMENT 'id',
    address_id VARCHAR(30) NOT NULL COMMENT '地址ID',
    b_id VARCHAR(30) NOT NULL COMMENT '业务Id',
    user_id VARCHAR(30) NOT NULL COMMENT '用户ID',
    tel varchar(11) not null comment '电话',
    postal_code varchar(10) not null comment '邮政编码',
    address varchar(200) not null comment '地址',
    is_default varchar(1) not null comment '是否为默认地址 1，表示默认地址 0 或空不是默认地址',
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    status_cd VARCHAR(2) NOT NULL default '0' COMMENT '数据状态，详细参考c_status表，S 保存，0在用，1失效'
);
-- 用户标签
create table business_user_tag(
    tag_id VARCHAR(30) NOT NULL COMMENT '打标ID',
    b_id VARCHAR(30) NOT NULL COMMENT '业务Id',
    user_id VARCHAR(30) NOT NULL COMMENT '用户ID',
    tag_cd VARCHAR(12) NOT NULL COMMENT '标签编码,参考tag表',
    remark VARCHAR(200) COMMENT '备注',
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    operate VARCHAR(3) NOT NULL COMMENT '数据状态，添加ADD，修改MOD 删除DEL'

);

-- 用户标签
create table u_user_tag(
    id INT NOT NULL AUTO_INCREMENT KEY COMMENT 'id',
    tag_id VARCHAR(30) NOT NULL COMMENT '打标ID',
    user_id VARCHAR(30) NOT NULL COMMENT '用户ID',
    tag_cd VARCHAR(12) NOT NULL COMMENT '标签编码,参考tag表',
    b_id VARCHAR(30) NOT NULL COMMENT '业务Id',
    remark VARCHAR(200) COMMENT '备注',
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    status_cd VARCHAR(2) NOT NULL default '0' COMMENT '数据状态，详细参考c_status表，S 保存，0在用，1失效'
);

-- 标签
create table tag(
    id INT NOT NULL AUTO_INCREMENT KEY COMMENT 'id',
    domain varchar(20) not null comment '标签域',
    tag_cd VARCHAR(12) NOT NULL COMMENT '标签编码',
    `name` VARCHAR(50) NOT NULL COMMENT '标签名称',
    description VARCHAR(200) COMMENT '描述',
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
);
-- 用户证件 表（过程表）
create table business_user_credentials(
    credentials_id VARCHAR(30) NOT NULL COMMENT '证件ID',
    b_id VARCHAR(30) NOT NULL COMMENT '业务Id',
    user_id VARCHAR(30) NOT NULL COMMENT '用户ID',
    credentials_cd varchar(12) not null comment '证件类型',
    value varchar(50) not null  comment '证件号码',
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    operate VARCHAR(3) NOT NULL COMMENT '数据状态，添加ADD，修改MOD 删除DEL'
);


-- 用户证件表
create table u_user_credentials(
    id INT NOT NULL AUTO_INCREMENT KEY COMMENT 'id',
    credentials_id VARCHAR(30) NOT NULL COMMENT '证件ID',
    b_id VARCHAR(30) NOT NULL COMMENT '业务Id',
    user_id VARCHAR(30) NOT NULL COMMENT '用户ID',
    credentials_cd varchar(12) not null comment '证件类型',
    value varchar(50) not null  comment '证件号码',
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    status_cd VARCHAR(2) NOT NULL default '0' COMMENT '数据状态，详细参考c_status表，S 保存，0在用，1失效'
);

-- 标签
create table credentials(
    id INT NOT NULL AUTO_INCREMENT KEY COMMENT 'id',
    credentials_cd VARCHAR(12) NOT NULL COMMENT '证件编码',
    `name` VARCHAR(50) NOT NULL COMMENT '证件名称',
    description VARCHAR(200) COMMENT '描述',
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
);