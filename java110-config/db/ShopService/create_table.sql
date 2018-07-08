-- create 商品表
create table business_shop(
    shop_id VARCHAR(30) NOT NULL COMMENT '商品ID',
    b_id VARCHAR(30) NOT NULL COMMENT '业务Id',
    catalog_id varchar(30) not null comment '目录ID',
    store_id VARCHAR(30) NOT NULL COMMENT '商店ID',
    `name` VARCHAR(100) NOT NULL COMMENT '商品名称',
    hot_buy varchar(2) not null default 'N' comment '是否热卖 Y是 N否',
    sale_price DECIMAL(10,2) not null comment '商品销售价,再没有打折情况下显示',
    open_shop_count varchar(2) not null default 'N' comment '是否开启库存管理，默认N Y开启，N关闭，开启后界面显示数量，如果为0 则下架',
    shop_count DECIMAL(10,0) not null default 10000 comment '商品库存',
    start_date date not null comment '商品开始时间',
    end_date date not null comment '商品结束时间',
    `month` INT NOT NULL COMMENT '月份',
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    operate VARCHAR(3) NOT NULL COMMENT '数据状态，添加ADD，修改MOD 删除DEL'
)
PARTITION BY RANGE (`month`) (
    PARTITION business_shop_1 VALUES LESS THAN (2),
    PARTITION business_shop_2 VALUES LESS THAN (3),
    PARTITION business_shop_3 VALUES LESS THAN (4),
    PARTITION business_shop_4 VALUES LESS THAN (5),
    PARTITION business_shop_5 VALUES LESS THAN (6),
    PARTITION business_shop_6 VALUES LESS THAN (7),
    PARTITION business_shop_7 VALUES LESS THAN (8),
    PARTITION business_shop_8 VALUES LESS THAN (9),
    PARTITION business_shop_9 VALUES LESS THAN (10),
    PARTITION business_shop_10 VALUES LESS THAN (11),
    PARTITION business_shop_11 VALUES LESS THAN (12),
    PARTITION business_shop_12 VALUES LESS THAN (13)
);
CREATE INDEX idx_business_shop_shop_id ON business_shop(shop_id);
CREATE INDEX idx_business_shop_b_id ON business_shop(b_id);
-- 商品属性表
create table business_shop_attr(
    b_id VARCHAR(30) NOT NULL COMMENT '订单ID',
    attr_id VARCHAR(30) NOT NULL COMMENT '属性id',
    shop_id VARCHAR(30) NOT NULL COMMENT '商品ID',
    spec_cd VARCHAR(12) NOT NULL COMMENT '规格id,参考spec表',
    value VARCHAR(50) NOT NULL COMMENT '属性值',
    `month` INT NOT NULL COMMENT '月份',
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    operate VARCHAR(3) NOT NULL COMMENT '数据状态，添加ADD，修改MOD 删除DEL'
)
PARTITION BY RANGE (`month`) (
    PARTITION business_shop_attr_1 VALUES LESS THAN (2),
    PARTITION business_shop_attr_2 VALUES LESS THAN (3),
    PARTITION business_shop_attr_3 VALUES LESS THAN (4),
    PARTITION business_shop_attr_4 VALUES LESS THAN (5),
    PARTITION business_shop_attr_5 VALUES LESS THAN (6),
    PARTITION business_shop_attr_6 VALUES LESS THAN (7),
    PARTITION business_shop_attr_7 VALUES LESS THAN (8),
    PARTITION business_shop_attr_8 VALUES LESS THAN (9),
    PARTITION business_shop_attr_9 VALUES LESS THAN (10),
    PARTITION business_shop_attr_10 VALUES LESS THAN (11),
    PARTITION business_shop_attr_11 VALUES LESS THAN (12),
    PARTITION business_shop_attr_12 VALUES LESS THAN (13)
);
CREATE INDEX idx_business_shop_attr_shop_id ON business_shop_attr(shop_id);
CREATE INDEX idx_business_shop_attr_b_id ON business_shop_attr(b_id);

-- 商品 目录
create table business_shop_catalog(
    catalog_id varchar(30) not null comment '目录ID',
    b_id VARCHAR(30) NOT NULL COMMENT '业务Id',
    store_id VARCHAR(30) NOT NULL COMMENT '商店ID',
    `name` varchar(100) not null comment '目录名称',
    level varchar(2) not null comment '目录等级',
    parent_catalog_id varchar(30) not null default '-1' comment '父目录ID，一级目录则写-1',
    `month` INT NOT NULL COMMENT '月份',
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    operate VARCHAR(3) NOT NULL COMMENT '数据状态，添加ADD，修改MOD 删除DEL'
)
PARTITION BY RANGE (`month`) (
    PARTITION business_shop_catalog_1 VALUES LESS THAN (2),
    PARTITION business_shop_catalog_2 VALUES LESS THAN (3),
    PARTITION business_shop_catalog_3 VALUES LESS THAN (4),
    PARTITION business_shop_catalog_4 VALUES LESS THAN (5),
    PARTITION business_shop_catalog_5 VALUES LESS THAN (6),
    PARTITION business_shop_catalog_6 VALUES LESS THAN (7),
    PARTITION business_shop_catalog_7 VALUES LESS THAN (8),
    PARTITION business_shop_catalog_8 VALUES LESS THAN (9),
    PARTITION business_shop_catalog_9 VALUES LESS THAN (10),
    PARTITION business_shop_catalog_10 VALUES LESS THAN (11),
    PARTITION business_shop_catalog_11 VALUES LESS THAN (12),
    PARTITION business_shop_catalog_12 VALUES LESS THAN (13)
);
CREATE INDEX idx_business_shop_catalog_store_id ON business_shop_catalog(store_id);
CREATE INDEX idx_business_shop_catalog_b_id ON business_shop_catalog(b_id);


-- 商店照片
CREATE TABLE business_shop_photo(
    shop_photo_id VARCHAR(30) NOT NULL COMMENT '商品照片ID',
    b_id VARCHAR(30) NOT NULL COMMENT '业务Id',
    shop_id VARCHAR(30) NOT NULL COMMENT '商店ID',
    shop_photo_type_cd VARCHAR(12) NOT NULL COMMENT '商品照片类型,L logo O 其他照片',
    photo VARCHAR(100) NOT NULL COMMENT '照片',
    `month` INT NOT NULL COMMENT '月份',
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    operate VARCHAR(3) NOT NULL COMMENT '数据状态，添加ADD，修改MOD 删除DEL'
)
PARTITION BY RANGE (`month`) (
    PARTITION business_shop_photo_1 VALUES LESS THAN (2),
    PARTITION business_shop_photo_2 VALUES LESS THAN (3),
    PARTITION business_shop_photo_3 VALUES LESS THAN (4),
    PARTITION business_shop_photo_4 VALUES LESS THAN (5),
    PARTITION business_shop_photo_5 VALUES LESS THAN (6),
    PARTITION business_shop_photo_6 VALUES LESS THAN (7),
    PARTITION business_shop_photo_7 VALUES LESS THAN (8),
    PARTITION business_shop_photo_8 VALUES LESS THAN (9),
    PARTITION business_shop_photo_9 VALUES LESS THAN (10),
    PARTITION business_shop_photo_10 VALUES LESS THAN (11),
    PARTITION business_shop_photo_11 VALUES LESS THAN (12),
    PARTITION business_shop_photo_12 VALUES LESS THAN (13)
);

CREATE INDEX idx_business_shop_photo_shop_id ON business_shop_photo(shop_id);
CREATE INDEX idx_business_shop_photo_b_id ON business_shop_photo(b_id);


-- 商品属性 离散值表，例如 手机颜色 黑 白 红
create table business_shop_attr_param(
    attr_param_id VARCHAR(30) NOT NULL COMMENT '商品属性参数ID',
    b_id VARCHAR(30) NOT NULL COMMENT '订单ID',
    shop_id VARCHAR(30) NOT NULL COMMENT '商品ID',
    spec_cd VARCHAR(12) NOT NULL COMMENT '规格id,参考spec表',
    param VARCHAR(50) NOT NULL COMMENT '参数值',
    `month` INT NOT NULL COMMENT '月份',
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    operate VARCHAR(3) NOT NULL COMMENT '数据状态，添加ADD，修改MOD 删除DEL'
)
PARTITION BY RANGE (`month`) (
    PARTITION business_shop_attr_param_1 VALUES LESS THAN (2),
    PARTITION business_shop_attr_param_2 VALUES LESS THAN (3),
    PARTITION business_shop_attr_param_3 VALUES LESS THAN (4),
    PARTITION business_shop_attr_param_4 VALUES LESS THAN (5),
    PARTITION business_shop_attr_param_5 VALUES LESS THAN (6),
    PARTITION business_shop_attr_param_6 VALUES LESS THAN (7),
    PARTITION business_shop_attr_param_7 VALUES LESS THAN (8),
    PARTITION business_shop_attr_param_8 VALUES LESS THAN (9),
    PARTITION business_shop_attr_param_9 VALUES LESS THAN (10),
    PARTITION business_shop_attr_param_10 VALUES LESS THAN (11),
    PARTITION business_shop_attr_param_11 VALUES LESS THAN (12),
    PARTITION business_shop_attr_param_12 VALUES LESS THAN (13)
);

CREATE INDEX idx_business_shop_attr_param_shop_id ON business_shop_attr_param(shop_id);
CREATE INDEX idx_business_shop_attr_param_b_id ON business_shop_attr_param(b_id);

-- 商品优惠表
CREATE TABLE business_shop_preferential(
    shop_preferential_id VARCHAR(30) NOT NULL COMMENT '商品ID',
    shop_id VARCHAR(30) NOT NULL COMMENT '商品ID',
    b_id VARCHAR(30) NOT NULL COMMENT '业务Id',
    original_price DECIMAL(10,2) NOT NULL COMMENT '商品销售价，再没有优惠的情况下和售价是一致的',
    discount_rate DECIMAL(3,2) NOT NULL DEFAULT 1.00 COMMENT '商品打折率',
    show_original_price VARCHAR(2) NOT NULL DEFAULT 'N' COMMENT '是否显示原价，Y显示，N 不显示',
    preferential_start_date DATE NOT NULL COMMENT '商品优惠开始时间',
    preferential_end_date DATE NOT NULL COMMENT '商品优惠结束时间',
    `month` INT NOT NULL COMMENT '月份',
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    operate VARCHAR(3) NOT NULL COMMENT '数据状态，添加ADD，修改MOD 删除DEL'
)
PARTITION BY RANGE (`month`) (
    PARTITION business_shop_preferential_1 VALUES LESS THAN (2),
    PARTITION business_shop_preferential_2 VALUES LESS THAN (3),
    PARTITION business_shop_preferential_3 VALUES LESS THAN (4),
    PARTITION business_shop_preferential_4 VALUES LESS THAN (5),
    PARTITION business_shop_preferential_5 VALUES LESS THAN (6),
    PARTITION business_shop_preferential_6 VALUES LESS THAN (7),
    PARTITION business_shop_preferential_7 VALUES LESS THAN (8),
    PARTITION business_shop_preferential_8 VALUES LESS THAN (9),
    PARTITION business_shop_preferential_9 VALUES LESS THAN (10),
    PARTITION business_shop_preferential_10 VALUES LESS THAN (11),
    PARTITION business_shop_preferential_11 VALUES LESS THAN (12),
    PARTITION business_shop_preferential_12 VALUES LESS THAN (13)
);
CREATE INDEX idx_business_shop_preferential_shop_id ON business_shop_preferential(shop_id);
CREATE INDEX idx_business_shop_preferential_b_id ON business_shop_preferential(b_id);
-- 商品描述
create table business_shop_desc(
    shop_desc_id VARCHAR(30) NOT NULL COMMENT '商品描述ID',
    shop_id VARCHAR(30) NOT NULL COMMENT '商品ID',
    b_id VARCHAR(30) NOT NULL COMMENT '业务Id',
    shop_describe LONGTEXT not null COMMENT '商品描述',
    `month` INT NOT NULL COMMENT '月份',
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    operate VARCHAR(3) NOT NULL COMMENT '数据状态，添加ADD，修改MOD 删除DEL'
)
PARTITION BY RANGE (`month`) (
    PARTITION business_shop_desc_1 VALUES LESS THAN (2),
    PARTITION business_shop_desc_2 VALUES LESS THAN (3),
    PARTITION business_shop_desc_3 VALUES LESS THAN (4),
    PARTITION business_shop_desc_4 VALUES LESS THAN (5),
    PARTITION business_shop_desc_5 VALUES LESS THAN (6),
    PARTITION business_shop_desc_6 VALUES LESS THAN (7),
    PARTITION business_shop_desc_7 VALUES LESS THAN (8),
    PARTITION business_shop_desc_8 VALUES LESS THAN (9),
    PARTITION business_shop_desc_9 VALUES LESS THAN (10),
    PARTITION business_shop_desc_10 VALUES LESS THAN (11),
    PARTITION business_shop_desc_11 VALUES LESS THAN (12),
    PARTITION business_shop_desc_12 VALUES LESS THAN (13)
);
CREATE INDEX idx_business_shop_desc_shop_id ON business_shop_desc(shop_id);
CREATE INDEX idx_business_shop_desc_b_id ON business_shop_desc(b_id);


-- create 商品表
create table s_shop(
    shop_id VARCHAR(30) NOT NULL COMMENT '商品ID',
    b_id VARCHAR(30) NOT NULL COMMENT '业务Id',
    store_id VARCHAR(30) NOT NULL COMMENT '商店ID',
    catalog_id varchar(30) not null comment '目录ID',
    `name` VARCHAR(100) NOT NULL COMMENT '商品名称',
    hot_buy varchar(2) not null default 'N' comment '是否热卖 Y是 N否',
    sale_price DECIMAL(10,2) not null comment '商品销售价,再没有打折情况下显示',
    open_shop_count varchar(2) not null default 'N' comment '是否开启库存管理，默认N Y开启，N关闭，开启后界面显示数量，如果为0 则下架',
    shop_count DECIMAL(10,0) not null default 10000 comment '商品库存',
    start_date date not null comment '商品开始时间',
    end_date date not null comment '商品结束时间',
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    status_cd VARCHAR(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考c_status表，S 保存，0, 在用 1失效',
    UNIQUE KEY (shop_id)
);
CREATE INDEX idx_store_b_id ON s_shop(b_id);
CREATE UNIQUE INDEX idx_shop_shop_id ON s_shop(shop_id);
-- 商品属性表
create table s_shop_attr(
    b_id VARCHAR(30) NOT NULL COMMENT '订单ID',
    attr_id VARCHAR(30) NOT NULL COMMENT '属性id',
    shop_id VARCHAR(30) NOT NULL COMMENT '商品ID',
    spec_cd VARCHAR(12) NOT NULL COMMENT '规格id,参考spec表',
    value VARCHAR(50) NOT NULL COMMENT '属性值',
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    status_cd VARCHAR(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考c_status表，S 保存，0, 在用 1失效',
    UNIQUE KEY (attr_id)
);
CREATE INDEX idx_store_attr_b_id ON s_shop_attr(b_id);
CREATE INDEX idx_shop_attr_shop_id ON s_shop_attr(shop_id);

-- 商品属性 离散值表，例如 手机颜色 黑 白 红
create table s_shop_attr_param(
    attr_param_id VARCHAR(30) NOT NULL COMMENT '商品属性参数ID',
    b_id VARCHAR(30) NOT NULL COMMENT '订单ID',
    shop_id VARCHAR(30) NOT NULL COMMENT '商品ID',
    spec_cd VARCHAR(12) NOT NULL COMMENT '规格id,参考spec表',
    param VARCHAR(50) NOT NULL COMMENT '参数值',
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    status_cd VARCHAR(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考c_status表，S 保存，0, 在用 1失效',
    UNIQUE KEY (attr_param_id)
);
CREATE INDEX idx_shop_attr_param_b_id ON s_shop_attr_param(b_id);
CREATE INDEX idx_shop_attr_param_shop_id ON s_shop_attr_param(shop_id);

-- 商品优惠表
create table s_shop_preferential(
    shop_preferential_id VARCHAR(30) NOT NULL COMMENT '商品ID',
    shop_id VARCHAR(30) NOT NULL COMMENT '商品ID',
    b_id VARCHAR(30) NOT NULL COMMENT '业务Id',
    original_price DECIMAL(10,2) not null comment '商品销售价，再没有优惠的情况下和售价是一致的',
    discount_rate decimal(3,2) not null default 1.00 comment '商品打折率',
    show_original_price varchar(2) not null default 'N' comment '是否显示原价，Y显示，N 不显示',
    preferential_start_date date not null comment '商品优惠开始时间',
    preferential_end_date date not null comment '商品优惠结束时间',
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    status_cd VARCHAR(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考c_status表，S 保存，0, 在用 1失效',
    UNIQUE KEY (shop_preferential_id)
);
CREATE INDEX idx_shop_preferential_b_id ON s_shop_preferential(b_id);
CREATE INDEX idx_shop_preferential_shop_id ON s_shop_preferential(shop_id);
-- 商品描述
create table s_shop_desc(
    shop_desc_id VARCHAR(30) NOT NULL COMMENT '商品描述ID',
    shop_id VARCHAR(30) NOT NULL COMMENT '商品ID',
    b_id VARCHAR(30) NOT NULL COMMENT '业务Id',
    shop_describe LONGTEXT not null COMMENT '商品描述',
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    status_cd VARCHAR(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考c_status表，S 保存，0, 在用 1失效',
    UNIQUE KEY (shop_desc_id)
);
CREATE INDEX idx_shop_desc_b_id ON s_shop_desc(b_id);
CREATE INDEX idx_shop_desc_shop_id ON s_shop_desc(shop_id);

-- 商店照片
CREATE TABLE s_shop_photo(
    shop_photo_id VARCHAR(30) NOT NULL COMMENT '商品照片ID',
    b_id VARCHAR(30) NOT NULL COMMENT '业务Id',
    shop_id VARCHAR(30) NOT NULL COMMENT '商品ID',
    shop_photo_type_cd VARCHAR(12) NOT NULL COMMENT '商品照片类型,L logo O 其他照片',
    photo VARCHAR(100) NOT NULL COMMENT '照片',
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    status_cd VARCHAR(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考c_status表，S 保存，0, 在用 1失效',
    UNIQUE KEY (shop_photo_id)
);
CREATE INDEX idx_shop_photo_b_id ON s_shop_photo(b_id);
CREATE INDEX idx_shop_photo_shop_id ON s_shop_photo(shop_id);
CREATE INDEX idx_shop_photo_shop_photo_id ON s_shop_photo(shop_photo_id);

create table s_shop_catalog(
    catalog_id varchar(30) not null comment '目录ID',
    b_id VARCHAR(30) NOT NULL COMMENT '业务Id',
    store_id VARCHAR(30) NOT NULL COMMENT '商店ID',
    `name` varchar(100) not null comment '目录名称',
    level varchar(2) not null comment '目录等级',
    parent_catalog_id varchar(30) not null default '-1' comment '父目录ID，一级目录则写-1',
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    status_cd VARCHAR(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考c_status表，S 保存，0, 在用 1失效',
     UNIQUE KEY (catalog_id)
);
CREATE INDEX idx_shop_catalog_b_id ON s_shop_catalog(b_id);
CREATE INDEX idx_shop_catalog_store_id ON s_shop_catalog(store_id);
CREATE INDEX idx_shop_catalog_catalog_id ON s_shop_catalog(catalog_id);

-- 商品购买记录

CREATE TABLE s_buy_shop(
    buy_id VARCHAR(30) NOT NULL COMMENT '购买ID',
    b_id VARCHAR(30) NOT NULL COMMENT '业务Id',
    shop_id VARCHAR(30) NOT NULL COMMENT '商品ID',
    buy_count DECIMAL(10,0) NOT NULL DEFAULT 1 COMMENT '购买商品数',
    `month` INT NOT NULL COMMENT '月份',
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    status_cd VARCHAR(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考c_status表，S 保存，0, 在用 1失效',
     UNIQUE KEY (buy_id,`month`)
)
PARTITION BY RANGE (`month`) (
    PARTITION buy_shop_1 VALUES LESS THAN (2),
    PARTITION buy_shop_2 VALUES LESS THAN (3),
    PARTITION buy_shop_3 VALUES LESS THAN (4),
    PARTITION buy_shop_4 VALUES LESS THAN (5),
    PARTITION buy_shop_5 VALUES LESS THAN (6),
    PARTITION buy_shop_6 VALUES LESS THAN (7),
    PARTITION buy_shop_7 VALUES LESS THAN (8),
    PARTITION buy_shop_8 VALUES LESS THAN (9),
    PARTITION buy_shop_9 VALUES LESS THAN (10),
    PARTITION buy_shop_10 VALUES LESS THAN (11),
    PARTITION buy_shop_11 VALUES LESS THAN (12),
    PARTITION buy_shop_12 VALUES LESS THAN (13)
);
CREATE INDEX idx_buy_shop_b_id ON s_buy_shop(b_id);
-- 商品购买属性
create table s_buy_shop_attr(
    b_id VARCHAR(30) NOT NULL COMMENT '订单ID',
    attr_id VARCHAR(30) NOT NULL COMMENT '属性id',
    buy_id VARCHAR(30) NOT NULL COMMENT '购买ID buy_id',
    spec_cd VARCHAR(12) NOT NULL COMMENT '规格id,参考spec表',
    value VARCHAR(50) NOT NULL COMMENT '属性值',
    `month` INT NOT NULL COMMENT '月份',
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    status_cd VARCHAR(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考c_status表，S 保存，0, 在用 1失效',
     UNIQUE KEY (attr_id,`month`)
)
PARTITION BY RANGE (`month`) (
    PARTITION buy_shop_attr_1 VALUES LESS THAN (2),
    PARTITION buy_shop_attr_2 VALUES LESS THAN (3),
    PARTITION buy_shop_attr_3 VALUES LESS THAN (4),
    PARTITION buy_shop_attr_4 VALUES LESS THAN (5),
    PARTITION buy_shop_attr_5 VALUES LESS THAN (6),
    PARTITION buy_shop_attr_6 VALUES LESS THAN (7),
    PARTITION buy_shop_attr_7 VALUES LESS THAN (8),
    PARTITION buy_shop_attr_8 VALUES LESS THAN (9),
    PARTITION buy_shop_attr_9 VALUES LESS THAN (10),
    PARTITION buy_shop_attr_10 VALUES LESS THAN (11),
    PARTITION buy_shop_attr_11 VALUES LESS THAN (12),
    PARTITION buy_shop_attr_12 VALUES LESS THAN (13)
);
CREATE INDEX idx_buy_shop_attr_b_id ON s_buy_shop_attr(b_id);
CREATE INDEX idx_buy_shop_attr_buy_id ON s_buy_shop_attr(buy_id);
