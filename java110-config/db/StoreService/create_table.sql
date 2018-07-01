-- store 表 过程表

create table business_store(
    store_id VARCHAR(30) NOT NULL COMMENT '商店ID',
    b_id VARCHAR(30) NOT NULL COMMENT '业务Id',
    user_id VARCHAR(30) NOT NULL COMMENT '用户ID',
    name varchar(100) not null comment '店铺名称',
    address varchar(200) not null comment '店铺地址',
    tel varchar(11) not null comment '电话',
    store_type_cd varchar(10) not null commnet '店铺种类，对应表 store_type',
    nearby_landmarks varchar(200) comment '地标，如王府井北60米',
    map_x varchar(20) not null comment '地区 x坐标',
    map_y varchar(20) not null comment '地区 y坐标',
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    operate VARCHAR(3) NOT NULL COMMENT '数据状态，添加ADD，修改MOD 删除DEL'
);

create table business_store_attr(
    b_id VARCHAR(30) NOT NULL COMMENT '订单ID',
    attr_id VARCHAR(30) NOT NULL COMMENT '属性id',
    store_id VARCHAR(30) NOT NULL COMMENT '用户ID',
    spec_cd VARCHAR(12) NOT NULL COMMENT '规格id,参考spec表',
    value VARCHAR(50) NOT NULL COMMENT '属性值',
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    operate VARCHAR(3) NOT NULL COMMENT '数据状态，添加ADD，修改MOD 删除DEL'
);

-- 商店照片
create table business_store_photo(
    store_photo_id varchar(30) not null commnet '商户照片ID',
    b_id VARCHAR(30) NOT NULL COMMENT '业务Id',
    store_id VARCHAR(30) NOT NULL COMMENT '商店ID',
    store_photo_type_cd varchar(12) not null comment '商户照片类型,T 门头照 I 内景照',
    photo varchar(100) not null comment '照片',
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    operate VARCHAR(3) NOT NULL COMMENT '数据状态，添加ADD，修改MOD 删除DEL'
);
-- 商户证件
create table business_store_cerdentials(
    store_cerdentials_id varchar(30) not null comment '商户证件ID',
    b_id VARCHAR(30) NOT NULL COMMENT '业务Id',
    store_id VARCHAR(30) NOT NULL COMMENT '商店ID',
    credentials_cd varchar(12) not null comment '证件类型，对应于 credentials表',
    value varchar(50) not null  comment '证件号码',
    validity_period TIMESTAMP NOT NULL  COMMENT '有效期，如果是长期有效 写成 3000/1/1',
    positive_photo varchar(100) comment '正面照片',
    negative_photo varchar(100) comment '反面照片',
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    operate VARCHAR(3) NOT NULL COMMENT '数据状态，添加ADD，修改MOD 删除DEL'
);



create table s_store(
    id INT NOT NULL AUTO_INCREMENT KEY COMMENT 'id',
    store_id VARCHAR(30) NOT NULL COMMENT '商店ID',
    b_id VARCHAR(30) NOT NULL COMMENT '业务Id',
    user_id VARCHAR(30) NOT NULL COMMENT '用户ID',
    name varchar(100) not null comment '店铺名称',
    address varchar(200) not null comment '店铺地址',
    tel varchar(11) not null comment '电话',
    store_type varchar(10) not null commnet '店铺种类',
    nearby_landmarks varchar(200) comment '地标，如王府井北60米',
    map_x varchar(20) not null comment '地区 x坐标',
    map_y varchar(20) not null comment '地区 y坐标',
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    status_cd VARCHAR(2) NOT NULL default '0' COMMENT '数据状态，详细参考c_status表，S 保存，0, 在用 1失效'
);

create table s_store_attr(
    id INT NOT NULL AUTO_INCREMENT KEY COMMENT 'id',
    b_id VARCHAR(30) NOT NULL COMMENT '订单ID',
    attr_id VARCHAR(30) NOT NULL COMMENT '属性id',
    store_id VARCHAR(30) NOT NULL COMMENT '用户ID',
    spec_cd VARCHAR(12) NOT NULL COMMENT '规格id,参考spec表',
    value VARCHAR(50) NOT NULL COMMENT '属性值',
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    status_cd VARCHAR(2) NOT NULL default '0' COMMENT '数据状态，详细参考c_status表，S 保存，0, 在用 1失效'
);

-- 商店照片
create table s_store_photo(
    id INT NOT NULL AUTO_INCREMENT KEY COMMENT 'id',
    store_photo_id varchar(30) not null commnet '商户照片ID',
    b_id VARCHAR(30) NOT NULL COMMENT '业务Id',
    store_id VARCHAR(30) NOT NULL COMMENT '商店ID',
    store_photo_type_cd varchar(12) not null comment '商户照片类型,T 门头照 I 内景照',
    photo varchar(100) not null comment '照片',
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    status_cd VARCHAR(2) NOT NULL default '0' COMMENT '数据状态，详细参考c_status表，S 保存，0, 在用 1失效'
);
-- 商户证件
create table s_store_cerdentials(
    id INT NOT NULL AUTO_INCREMENT KEY COMMENT 'id',
    store_cerdentials_id varchar(30) not null comment '商户证件ID',
    b_id VARCHAR(30) NOT NULL COMMENT '业务Id',
    store_id VARCHAR(30) NOT NULL COMMENT '商店ID',
    credentials_cd varchar(12) not null comment '证件类型，对应于 credentials表',
    value varchar(50) not null  comment '证件号码',
    validity_period TIMESTAMP NOT NULL  COMMENT '有效期，如果是长期有效 写成 3000/1/1',
    positive_photo varchar(100) comment '正面照片',
    negative_photo varchar(100) comment '反面照片',
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    status_cd VARCHAR(2) NOT NULL default '0' COMMENT '数据状态，详细参考c_status表，S 保存，0, 在用 1失效'
);

-- 店铺种类
create table store_type(
    id INT NOT NULL AUTO_INCREMENT KEY COMMENT 'id',
    store_type_cd VARCHAR(12) NOT NULL COMMENT '店铺编码',
    `name` VARCHAR(50) NOT NULL COMMENT '店铺种类编码',
    description VARCHAR(200) COMMENT '描述',
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
);
