
CREATE USER 'TT'@'%' IDENTIFIED BY 'TT@12345678';

GRANT ALL ON *.* TO 'TT'@'%';

create database `TT` default character set utf8 collate utf8_general_ci;

use TT;
-- c_orders
set character set utf8;

CREATE TABLE c_orders(
    o_id VARCHAR(30) NOT NULL UNIQUE COMMENT '订单ID',
    app_id VARCHAR(30) NOT NULL COMMENT '应用ID',
    ext_transaction_id VARCHAR(30) NOT NULL COMMENT '外部交易流水',
    user_id VARCHAR(30) NOT NULL COMMENT '用户ID',
    request_time VARCHAR(16) NOT NULL COMMENT '外部系统请求时间',
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    order_type_cd VARCHAR(4) NOT NULL COMMENT '订单类型，参考c_order_type表',
    finish_time DATE COMMENT '订单完成时间',
    remark VARCHAR(200) COMMENT '备注',
    status_cd VARCHAR(2) NOT NULL COMMENT '数据状态，详细参考c_status表'
);

-- c_orders_attrs

CREATE TABLE c_orders_attrs(
    o_id VARCHAR(30) NOT NULL COMMENT '订单ID',
    attr_id VARCHAR(30) NOT NULL COMMENT '属性id',
    spec_cd VARCHAR(12) NOT NULL COMMENT '规格id,参考spec表',
    `value` VARCHAR(50) NOT NULL COMMENT '属性值'
);



-- c_business

CREATE TABLE c_business(
    b_id VARCHAR(30) NOT NULL UNIQUE COMMENT '业务Id',
    o_id VARCHAR(30) NOT NULL COMMENT '订单ID',
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    business_type_cd VARCHAR(4) NOT NULL COMMENT '业务项类型，参考c_business_type表',
    finish_time DATE COMMENT '完成时间',
    remark VARCHAR(200) COMMENT '备注',
    status_cd VARCHAR(2) NOT NULL COMMENT '数据状态，详细参考c_status表'
);

-- business_attrs

CREATE TABLE c_business_attrs(
    b_id VARCHAR(30) NOT NULL COMMENT '订单ID',
    attr_id VARCHAR(30) NOT NULL COMMENT '属性id',
    spec_cd VARCHAR(12) NOT NULL COMMENT '规格id,参考spec表',
    `value` VARCHAR(50) NOT NULL COMMENT '属性值'
);

-- c_status

CREATE TABLE c_status(
    id INT NOT NULL AUTO_INCREMENT KEY COMMENT 'id',
    status_cd VARCHAR(4) NOT NULL UNIQUE COMMENT '状态',
    `name` VARCHAR(50) NOT NULL COMMENT '名称',
    description VARCHAR(200) COMMENT '描述',
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
);

INSERT INTO c_status(status_cd,NAME,description) VALUES('1','无效的，不在用的','无效的，不在用的');
INSERT INTO c_status(status_cd,NAME,description) VALUES('0','有效的，在用的','有效的，在用的');
INSERT INTO c_status(status_cd,NAME,description) VALUES('S','保存成功','保存成功');
INSERT INTO c_status(status_cd,NAME,description) VALUES('D','作废订单','作废订单');
INSERT INTO c_status(status_cd,NAME,description) VALUES('E','错误订单','错误订单');
INSERT INTO c_status(status_cd,NAME,description) VALUES('NE','通知错误订单','通知错误订单');
INSERT INTO c_status(status_cd,NAME,description) VALUES('C','订单完成','订单完成');
INSERT INTO c_status(status_cd,NAME,description) VALUES('B','Business过程','Business过程');
INSERT INTO c_status(status_cd,NAME,description) VALUES('I','Instance过程','Instance过程');


-- c_order_type

CREATE TABLE c_order_type(
    id INT NOT NULL AUTO_INCREMENT KEY COMMENT 'id',
    order_type_cd VARCHAR(4) NOT NULL UNIQUE COMMENT '订单类型',
    `name` VARCHAR(50) NOT NULL COMMENT '名称',
    description VARCHAR(200) COMMENT '描述',
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
);




-- c_business_type

CREATE TABLE c_business_type(
    id INT NOT NULL AUTO_INCREMENT KEY COMMENT 'id',
    business_type_cd VARCHAR(4) NOT NULL UNIQUE COMMENT '业务项类型',
    `name` VARCHAR(50) NOT NULL COMMENT '名称',
    description VARCHAR(200) COMMENT '描述',
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
);

insert into c_business_type(business_type_cd,name,description) values('DO','撤单','作废订单');

-- spec

CREATE TABLE spec(
    id INT NOT NULL AUTO_INCREMENT KEY COMMENT 'id',
    domain varchar(20) not null comment '属性域',
    spec_cd VARCHAR(4) NOT NULL UNIQUE COMMENT '业务项类型规格编码，从x00020001开始每次加一就可以(约定，x=10表示c_orders_attrs 中属性，x=11表示c_business_attrs 中的属性)',
    `name` VARCHAR(50) NOT NULL COMMENT '名称',
    description VARCHAR(200) COMMENT '描述',
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
);

insert into spec(domain,spec_cd,name,description) values('ORDERS','100001','订单来源','订单来源');
insert into spec(domain,spec_cd,name,description) values('BUSINESS','200001','推荐UserID','推荐UserID');

-- c_route

CREATE TABLE c_route(
    id INT NOT NULL AUTO_INCREMENT KEY COMMENT 'id',
    app_id VARCHAR(30) NOT NULL COMMENT '应用ID',
    service_id INT NOT NULL COMMENT '下游接口配置ID',
    order_type_cd VARCHAR(4) NOT NULL COMMENT '订单类型，参考c_order_type表',
    invoke_limit_times INT COMMENT '接口调用一分钟调用次数',
    invoke_model VARCHAR(1) NOT NULL COMMENT '1-同步方式 2-异步方式',
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    status_cd VARCHAR(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考c_status表，0在用，1失效，2 表示下线（当组件调用服务超过限制时自动下线）'
);



-- c_service

CREATE TABLE c_service(
    id INT NOT NULL AUTO_INCREMENT KEY COMMENT 'id',
    service_id VARCHAR(12) NOT NULL COMMENT '服务ID',
    service_code VARCHAR(50) NOT NULL UNIQUE COMMENT '自定义，命名方式查询类query.+目标系统+.+业务名称 保存类 save.+目标系统+.+业务名称 修改类 modify.+目标系统+.+业务名称 删除类 remove.+目标系统+.+业务名称 例如：query.user.userinfo save.user.adduserinfo',
    business_type_cd VARCHAR(4) NOT NULL COMMENT '业务项类型，参考c_business_type表',
    `name` VARCHAR(50) NOT NULL COMMENT '服务名称',
    seq INT NOT NULL COMMENT '顺序 只有同步方式下根据seq从小到大调用接口',
    messageQueueName VARCHAR(50) COMMENT '消息队里名称 只有异步时有用',
    is_instance varchar(2) not null default 'N' comment '是否Instance Y 需要，N不需要，NT透传类',
    url VARCHAR(200) COMMENT '目标地址',
    method VARCHAR(50) COMMENT '方法 空 为http post LOCAL_SERVICE 为调用本地服务 其他为webservice方式调用',
    timeout INT NOT NULL DEFAULT 60 COMMENT '超时时间',
    retry_count INT NOT NULL DEFAULT 3 COMMENT '重试次数',
    provide_app_id VARCHAR(30) NOT NULL COMMENT '应用ID',
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    status_cd VARCHAR(2) NOT NULL default '0' COMMENT '数据状态，详细参考c_status表，0在用，1失效',
    UNIQUE KEY (service_id)
);


-- c_mapping

CREATE TABLE c_mapping(
    id INT NOT NULL AUTO_INCREMENT KEY COMMENT 'id',
    domain VARCHAR(50) NOT NULL COMMENT '域',
    `name` VARCHAR(50) NOT NULL COMMENT '名称',
    `key` VARCHAR(100) NOT NULL COMMENT 'key',
    `value` VARCHAR(1000) NOT NULL COMMENT 'value',
    remark VARCHAR(200) COMMENT '描述',
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    status_cd VARCHAR(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考c_status表，0在用，1失效'
);

INSERT c_mapping(domain,`name`,`key`,`value`,remark) VALUES('DOMAIN.COMMON','日志开关','LOG_ON_OFF','ON','日志开关');
INSERT c_mapping(domain,`name`,`key`,`value`,remark) VALUES('DOMAIN.COMMON','耗时开关','COST_TIME_ON_OFF','ON','耗时开关');
INSERT c_mapping(domain,`name`,`key`,`value`,remark) VALUES('DOMAIN.COMMON','规则开关','RULE_ON_OFF','OFF','规则开关');
INSERT c_mapping(domain,`name`,`key`,`value`,remark) VALUES('DOMAIN.COMMON','不调规则服务的订单类型','NO_NEED_RULE_VALDATE_ORDER','Q','不调规则服务的订单类型');
INSERT c_mapping(domain,`name`,`key`,`value`,remark) VALUES('DOMAIN.COMMON','不保存订单信息','NO_SAVE_ORDER','Q,T','不保存订单信息');
INSERT c_mapping(domain,`name`,`key`,`value`,remark) VALUES('DOMAIN.COMMON','不用调用 下游系统的配置','NO_INVOKE_BUSINESS_SYSTEM','Q','不用调用 下游系统的配置(一般不存在这种情况，这里主要是在没有下游系统的情况下测试中心服务用)');
INSERT c_mapping(domain,`name`,`key`,`value`,remark) VALUES('DOMAIN.COMMON','不用调用 作废下游系统的配置','NO_INVALID_BUSINESS_SYSTEM','Q','不用调用 作废下游系统的配置 (一般不存在这种情况，这里主要是在没有下游系统的情况下测试中心服务用)');
INSERT c_mapping(domain,`name`,`key`,`value`,remark) VALUES('DOMAIN.COMMON','需要调用服务生成各个ID','NEED_INVOKE_SERVICE_GENERATE_ID','OFF','需要调用服务生成各个ID');
INSERT c_mapping(domain,`name`,`key`,`value`,remark) VALUES('DOMAIN.COMMON','公钥','PUBLIC_STRING','MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDloKXSBA5+tP39uS8yi5RZOs6Jdrt0znRQetyXX2l/IUCi1x1QAMgoZbnEavmdZ5jOZN/T1WYFbt/VomXEHaTdStAiYm3DCnxxy5CMMyRKai0+6n4lLJQpUmnAQPFENrOV8b70gBSBVjUXksImgui5qYaNqX90pjEzcyq+6CugBwIDAQAB','公钥');
INSERT c_mapping(domain,`name`,`key`,`value`,remark) VALUES('DOMAIN.COMMON','私钥','PRIVATE_STRING','MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAJbtQYV+VpWZvifoc0R11MyAfIyMGoJKHDrWQau7oxLHotDDJM80o7ea7oL2onaHWOXaybpUp5FpZgjuixYMhlQOA/VXosrJOGJhgNv0dAL6VVXxmjlg2UWqIEoyTS7IzF3BuQCqy2k9aT7mGiC0RYRpndTuwe/0DKwFx70dIIIrAgMBAAECgYBMNMHnqLIJWZa1Sd6hy6lGFP5ObROZg9gbMUH5d4XQnrKsHEyCvz6HH5ic0fGYTaDqdn1zMvllJ8XYbrIV0P8lvHr9/LCnoXessmf61hKZyTKi5ycNkiPHTjmJZCoVTQFprcNgYeVX4cvNsqB2zWwzoAk8bbdWY6X5jB6YEpfBmQJBANiO9GiBtw+T9h60MpyV1xhJKsx0eCvxRZcsDB1OLZvQ7dHnsHmh0xUBL2MraHKnQyxOlrIzOtyttxSTrQzwcM0CQQCyajkzxpF6EjrXWHYVHb3AXFSoz5krjOkLEHegYlGhx0gtytBNVwftCn6hqtaxCxKMp00ZJoXIxo8d9tQy4N7XAkBljnTT5bEBnzPWpk7t298pRnbJtvz8LoOiJ0fvHlCJN+uvemXqRJeGzC165kpvKj14M8q7+wZpoxWukqqe3MspAkAuFYHw/blV7p+EQDU//w6kQTUc5YKK3TrUwMwlgT/UqcTbDyf+0hwZ/jv3RkluMY35Br3DYU/tLFyLQNZOzgbBAkEApWARXVlleEYbv8dPUL+56S0ky1hZSuPfVOBda4V3p0q18LjcHIyYcVhKGqkpii5JgblaYyjUriNDisFalCp8jQ==','私钥');
INSERT c_mapping(domain,`name`,`key`,`value`,remark) VALUES('DOMAIN.COMMON','外部应用公钥','PUBLIC_STRING','MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCW7UGFflaVmb4n6HNEddTMgHyMjBqCShw61kGru6MSx6LQwyTPNKO3mu6C9qJ2h1jl2sm6VKeRaWYI7osWDIZUDgP1V6LKyThiYYDb9HQC+lVV8Zo5YNlFqiBKMk0uyMxdwbkAqstpPWk+5hogtEWEaZ3U7sHv9AysBce9HSCCKwIDAQAB','外部应用公钥');
INSERT c_mapping(domain,`name`,`key`,`value`,remark) VALUES('DOMAIN.COMMON','外部应用私钥','PRIVATE_STRING','MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAOWgpdIEDn60/f25LzKLlFk6zol2u3TOdFB63JdfaX8hQKLXHVAAyChlucRq+Z1nmM5k39PVZgVu39WiZcQdpN1K0CJibcMKfHHLkIwzJEpqLT7qfiUslClSacBA8UQ2s5XxvvSAFIFWNReSwiaC6Lmpho2pf3SmMTNzKr7oK6AHAgMBAAECgYEAlfR5FVNM2/X6QC0k408/i53Zru94r2j7kGsLj1bhoAHpIe502AAKtkboL5rkc6Rpp688dCvRug6T4gFxj8cEF7rOOU4CHqVCHUHa4sWSDL2Rg7pMbQOVB7PGmM4C/hEgXcwM6tmLiU3xkkQDrpgT1bPpAm7iwDx4HkZBv1naYnECQQDyk40+KDvyUgp/r1tKbkMLoQOAfTZPXy+HgeAkU3PCUTFQlvn2OU6Fsl3Yjlp6utxPVnd00DoPZ8qvx1falaeLAkEA8lWoIDgyYwnibv2fr3A715PkieJ0exKfLb5lSD9UIfGJ9s7oTcltl7pprykfSP46heWjScS7YJRZHPfqb1/Y9QJAJNUQqjJzv7yDSZX3t5p8ZaSiIn1gpLagQeQPg5SETCoF4eW6uI9FA/nsU/hxdpcu4oEPjFYdqr8owH31MgRtNwJAaE+6qPPHrJ3qnAAMJoZXG/qLG1cg8IEZh6U3D5xC6MGBs31ovWMBC5iwOTeoQdE8+7nXSb+nMHFq0m9cuEg3qQJAH4caPSQ9RjVOP9on+niy9b1mATbvurepIB95KUtaHLz1hpihCLR7dTwrz8JOitgFE75Wzt4a00GZYxnaq3jsjA==','外部应用私钥');
INSERT c_mapping(domain,`name`,`key`,`value`,remark) VALUES('DOMAIN.COMMON','默认KEY_SIZE','DEFAULT_DECRYPT_KEY_SIZE','2048','默认KEY_SIZE');
INSERT c_mapping(domain,`name`,`key`,`value`,remark) VALUES('DOMAIN.COMMON','中心服务地址','CENTER_SERVICE_URL','http://center-service/httpApi/service','中心服务地址');
INSERT c_mapping(domain,`name`,`key`,`value`,remark) VALUES('DOMAIN.COMMON','控制中心APP_ID','CONSOLE_SERVICE_APP_ID','8000418002','控制中心APP_ID');
INSERT c_mapping(domain,`name`,`key`,`value`,remark) VALUES('DOMAIN.COMMON','控制服务加密开关','KEY_CONSOLE_SERVICE_SECURITY_ON_OFF','ON','控制服务加密开关');
INSERT c_mapping(domain,`name`,`key`,`value`,remark) VALUES('DOMAIN.COMMON','控制服务鉴权秘钥','CONSOLE_SECURITY_CODE','WEBURFPKIFJUHNCJUEIKMKJUJHULSMNCHDY89KMC','控制服务鉴权秘钥');
INSERT c_mapping(domain,`name`,`key`,`value`,remark) VALUES('DOMAIN.COMMON','编码生成服务地址','CODE_PATH','http://code-service/codeApi/generate','编码生成服务地址');

-- c_app
CREATE TABLE c_app(
    id INT NOT NULL AUTO_INCREMENT KEY COMMENT 'id',
    app_id VARCHAR(30) NOT NULL UNIQUE COMMENT '应用ID',
    `name` VARCHAR(50) NOT NULL COMMENT '名称 对应系统名称',
    security_code VARCHAR(64) NOT NULL COMMENT '签名码 sign签名时用',
    while_list_ip VARCHAR(200) COMMENT '白名单ip 多个之间用;隔开',
    black_list_ip VARCHAR(200) COMMENT '黑名单ip 多个之间用;隔开',
    remark VARCHAR(200) COMMENT '描述',
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    status_cd VARCHAR(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考c_status表，0在用，1失效',
    UNIQUE KEY (app_id)
);

-- c_service_sql
CREATE TABLE c_service_sql(
    id INT NOT NULL AUTO_INCREMENT KEY COMMENT 'id',
    service_code VARCHAR(50) NOT NULL UNIQUE COMMENT '对应c_service表',
    `name` VARCHAR(50) NOT NULL COMMENT '名称',
    params VARCHAR(500) NOT NULL COMMENT '参数',
    query_model VARCHAR(1) NOT NULL COMMENT '查询方式 1、sql,2、存储过程',
    `sql` LONGTEXT COMMENT '执行sql',
    proc VARCHAR(200) COMMENT '存储过程名称',
    java_script LONGTEXT COMMENT '执行java脚本代码',
    template LONGTEXT COMMENT '输出模板',
    remark VARCHAR(200) COMMENT '描述',
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    status_cd VARCHAR(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考c_status表，0在用，1失效'
);

-- 测试用

insert into c_order_type(order_type_cd,`name`,description) values('Q','查询单','查询单');
insert into c_order_type(order_type_cd,`name`,description) values('D','受理单','受理单');

insert into c_app(app_id,`name`,security_code,remark,status_cd)
values('8000418001','内部测试应用','WEBURFPKIFJUHNCJUEIKMKJUJHULSMNCHDY89KMC','记得删除','0');

insert into c_app(app_id,`name`,security_code,remark,status_cd)
values('8000418002','控制中心应用','WEBURFPKIFJUHNCJUEIKMKJUJHULSMNCHDY89KMC','控制中心应用','0');

insert into c_app(app_id,`name`,security_code,remark,status_cd)
values('8000418003','用户管理应用','WEBURFPKIFJUHNCJUEIKMKJUJHULSMNCHDY89KMC','用户管理应用','0');


insert into c_service(service_id,service_code,business_type_cd,`name`,seq,url,provide_app_id,status_cd)
values('1','query.user.userInfo','Q','用户信息查询',1,'http://...','8000418001','0');

insert into c_service(service_id,service_code,business_type_cd,`name`,seq,url,provide_app_id,status_cd)
values('2','query.order.orderInfo','Q','订单信息',1,'http://center-service/businessApi/query','8000418001','0');

insert into c_service(service_id,service_code,business_type_cd,`name`,seq,url,provide_app_id,status_cd)
values('3','query.console.menu','Q','查询菜单',1,'http://center-service/businessApi/query','8000418002','0');

insert into c_service(service_id,service_code,business_type_cd,`name`,seq,url,provide_app_id,status_cd)
values('4','query.user.loginInfo','Q','查询用户登录信息',1,'http://center-service/businessApi/query','8000418002','0');

INSERT INTO c_service(service_id,service_code,business_type_cd,`name`,seq,url,provide_app_id)
VALUES('5','query.console.template','Q','查询模板信息',1,'http://center-service/businessApi/query','8000418002');

INSERT INTO c_service(service_id,service_code,business_type_cd,`name`,seq,url,provide_app_id)
VALUES('6','query.console.templateCol','Q','查询列模板信息',1,'http://center-service/businessApi/query','8000418002');

INSERT INTO c_service(service_id,service_code,business_type_cd,`name`,seq,url,provide_app_id)
VALUES('7','query.center.mapping','Q','查询映射数据',1,'http://center-service/businessApi/query','8000418002');

INSERT INTO c_service(service_id,service_code,business_type_cd,`name`,seq,url,provide_app_id)
VALUES('8','query.center.apps','Q','查询外部应用',1,'http://center-service/businessApi/query','8000418002');

INSERT INTO c_service(service_id,service_code,business_type_cd,`name`,seq,url,provide_app_id)
VALUES('9','query.center.services','Q','查询服务',1,'http://center-service/businessApi/query','8000418002');

INSERT INTO c_service(service_id,service_code,business_type_cd,`name`,seq,url,provide_app_id)
VALUES('10','query.center.routes','Q','查询路由',1,'http://center-service/businessApi/query','8000418002');

INSERT INTO c_service(service_id,service_code,business_type_cd,`name`,seq,url,provide_app_id)
VALUES('11','flush.center.cache','Q','CenterService缓存',1,'http://center-service/cacheApi/flush','8000418002');

INSERT INTO c_service(service_id,service_code,business_type_cd,`name`,seq,url,provide_app_id)
VALUES('12','query.console.caches','Q','查询所有缓存',1,'http://center-service/businessApi/query','8000418002');

INSERT INTO c_service(service_id,service_code,business_type_cd,`name`,seq,url,provide_app_id)
VALUES('13','query.console.cache','Q','查询所有缓存',1,'http://center-service/businessApi/query','8000418002');


INSERT INTO c_service(service_id,service_code,business_type_cd,`name`,seq,url,provide_app_id)
VALUES('14','save.center.mapping','Q','保存映射信息',1,'http://center-service/businessApi/do','8000418002');

INSERT INTO c_service(service_id,service_code,business_type_cd,`name`,seq,url,provide_app_id)
VALUES('15','delete.center.mapping','Q','删除映射信息',1,'http://center-service/businessApi/do','8000418002');

INSERT INTO c_service(service_id,service_code,business_type_cd,`name`,seq,url,provide_app_id)
VALUES('16','update.center.mapping','Q','修改映射信息',1,'http://center-service/businessApi/do','8000418002');

INSERT INTO c_service(service_id,service_code,business_type_cd,`name`,seq,url,provide_app_id)
VALUES('17','save.user.userInfo','D','保存用户信息',1,'http://user-service/userApi/service','8000418003');

INSERT INTO c_service(service_id,service_code,business_type_cd,`name`,seq,url,provide_app_id)
VALUES('18','save.store.info','D','保存商户信息',1,'http://store-service/storeApi/service','8000418003');

INSERT INTO c_service(service_id,service_code,business_type_cd,`name`,seq,url,provide_app_id)
VALUES('19','update.store.info','D','修改商户信息',1,'http://store-service/storeApi/service','8000418003');

INSERT INTO c_service(service_id,service_code,business_type_cd,`name`,seq,url,provide_app_id)
VALUES('20','delete.store.info','D','删除商户信息',1,'http://store-service/storeApi/service','8000418003');


INSERT INTO c_service(service_id,service_code,business_type_cd,`name`,seq,url,provide_app_id)
VALUES('21','transfer.console.menu','T','透传菜单查询',1,'http://192.168.31.199:8001/userApi/service','8000418001');

INSERT INTO c_service(service_id,service_code,business_type_cd,`name`,seq,url,provide_app_id)
VALUES('22','save.shop.info','D','保存商品信息',1,'http://shop-service/shopApi/service','8000418003');

INSERT INTO c_service(service_id,service_code,business_type_cd,`name`,seq,url,provide_app_id)
VALUES('23','update.shop.info','D','修改商品信息',1,'http://shop-service/shopApi/service','8000418003');

INSERT INTO c_service(service_id,service_code,business_type_cd,`name`,seq,url,provide_app_id)
VALUES('24','delete.shop.info','D','删除商品信息',1,'http://shop-service/shopApi/service','8000418003');

INSERT INTO c_service(service_id,service_code,business_type_cd,`name`,seq,url,provide_app_id)
VALUES('25','buy.shop.info','D','购买商品信息',1,'http://shop-service/shopApi/service','8000418003');

INSERT INTO c_service(service_id,service_code,business_type_cd,`name`,seq,url,provide_app_id)
VALUES('26','save.shop.catalog','D','保存商品目录信息',1,'http://shop-service/shopApi/service','8000418003');

INSERT INTO c_service(service_id,service_code,business_type_cd,`name`,seq,url,provide_app_id)
VALUES('27','update.shop.catalog','D','修改商品目录信息',1,'http://shop-service/shopApi/service','8000418003');

INSERT INTO c_service(service_id,service_code,business_type_cd,`name`,seq,url,provide_app_id)
VALUES('28','delete.shop.catalog','D','删除商品目录信息',1,'http://shop-service/shopApi/service','8000418003');


INSERT INTO c_service(service_id,service_code,business_type_cd,`name`,seq,url,provide_app_id)
VALUES('29','save.comment.info','D','保存评论',1,'http://comment-service/commentApi/service','8000418003');

INSERT INTO c_service(service_id,service_code,business_type_cd,`name`,seq,url,provide_app_id)
VALUES('30','delete.comment.info','D','删除评论',1,'http://comment-service/commentApi/service','8000418003');


INSERT INTO c_service(service_id,service_code,business_type_cd,`name`,seq,url,provide_app_id)
VALUES('31','member.joined.store','D','商户成员加入',1,'http://store-service/storeApi/service','8000418003');

INSERT INTO c_service(service_id,service_code,business_type_cd,`name`,seq,url,provide_app_id)
VALUES('32','member.quit.store','D','商户成员退出',1,'http://store-service/storeApi/service','8000418003');

insert into c_route(app_id,service_id,invoke_model,order_type_cd,status_cd) values(
'8000418001','1','S','Q','0'
);

insert into c_route(app_id,service_id,invoke_model,order_type_cd,status_cd) values(
'8000418001','3','S','Q','0'
);

insert into c_route(app_id,service_id,invoke_model,order_type_cd,status_cd) values(
'8000418002','3','S','Q','0'
);


insert into c_route(app_id,service_id,invoke_model,order_type_cd,status_cd) values(
'8000418002','4','S','Q','0'
);

insert into c_route(app_id,service_id,invoke_model,order_type_cd,status_cd) values(
'8000418002','5','S','Q','0'
);

INSERT INTO c_route(app_id,service_id,invoke_model,order_type_cd,status_cd) VALUES(
'8000418002','6','S','Q','0'
);

INSERT INTO c_route(app_id,service_id,invoke_model,order_type_cd,status_cd) VALUES(
'8000418002','7','S','Q','0'
);

INSERT INTO c_route(app_id,service_id,invoke_model,order_type_cd,status_cd) VALUES(
'8000418002','8','S','Q','0'
);

INSERT INTO c_route(app_id,service_id,invoke_model,order_type_cd,status_cd) VALUES(
'8000418002','9','S','Q','0'
);


INSERT INTO c_route(app_id,service_id,invoke_model,order_type_cd,status_cd) VALUES(
'8000418002','10','S','Q','0'
);

INSERT INTO c_route(app_id,service_id,invoke_model,order_type_cd,status_cd) VALUES(
'8000418002','11','S','Q','0'
);

INSERT INTO c_route(app_id,service_id,invoke_model,order_type_cd,status_cd) VALUES(
'8000418002','12','S','Q','0'
);
INSERT INTO c_route(app_id,service_id,invoke_model,order_type_cd,status_cd) VALUES(
'8000418002','13','S','Q','0'
);

INSERT INTO c_route(app_id,service_id,invoke_model,order_type_cd,status_cd) VALUES(
'8000418002','14','S','Q','0'
);
INSERT INTO c_route(app_id,service_id,invoke_model,order_type_cd,status_cd) VALUES(
'8000418002','15','S','Q','0'
);

INSERT INTO c_route(app_id,service_id,invoke_model,order_type_cd,status_cd) VALUES(
'8000418002','16','S','Q','0'
);
INSERT INTO c_route(app_id,service_id,invoke_model,order_type_cd,status_cd) VALUES(
'8000418002','17','S','Q','0'
);

insert into c_route(app_id,service_id,invoke_model,order_type_cd,status_cd) values(
'8000418001','21','S','Q','0'
);

insert into c_route(app_id,service_id,invoke_model,order_type_cd,status_cd) values(
'8000418001','22','S','Q','0'
);

insert into c_route(app_id,service_id,invoke_model,order_type_cd,status_cd) values(
'8000418001','23','S','Q','0'
);

insert into c_route(app_id,service_id,invoke_model,order_type_cd,status_cd) values(
'8000418001','24','S','Q','0'
);
insert into c_route(app_id,service_id,invoke_model,order_type_cd,status_cd) values(
'8000418001','25','S','Q','0'
);
insert into c_route(app_id,service_id,invoke_model,order_type_cd,status_cd) values(
'8000418001','26','S','Q','0'
);
insert into c_route(app_id,service_id,invoke_model,order_type_cd,status_cd) values(
'8000418001','27','S','Q','0'
);






INSERT INTO c_service_sql(service_code,`name`,params,query_model,`sql`,proc,template,remark,status_cd)
VALUES('query.order.orderInfo','订单信息','oId','1','{
                                                 	"param1":"SELECT co.app_id appId,co.create_time createTime,co.ext_transaction_id extTransactionId,co.finish_time finishTime
                                                 ,co.order_type_cd orderTypeCd,co.o_id oId ,co.remark remark ,co.request_time requestTime ,co.user_id userId,co.status_cd statusCd
                                                  FROM c_orders co WHERE co.o_id = #oId#",
                                                  "param2":"SELECT cb.b_id bId, cb.business_type_cd businessTypeCd,cb.create_time createTime,cb.finish_time finishTime ,cb.o_id oId,
                                                 cb.remark remark,cb.status_cd statusCd FROM c_business cb WHERE cb.o_id = #oId#"
                                                 }','','{"PARAM:"{
                                                            "param1": "$.#order#Object",
                                                            "param2": "$.#business#Array"
                                                            },"TEMPLATE":"{
                                                         "response": {
                                                           "code": "0000",
                                                           "message": "成功"
                                                         }
                                                       }"}','','0');

INSERT INTO c_service_sql(service_code,`name`,params,query_model,`sql`,proc,template,remark,status_cd)
VALUES('query.console.menu','查询菜单','manageId,menuGroup','1','{
                                                 	"param1":"select mm.m_id mId,mm.name name,mm.level level,mm.parent_id parentId,mm.menu_group menuGroup,mm.user_id userId,mm.create_time createTime,
                                                              mm.remark remark,mmc.url url,mmc.template template
                                                              from m_menu_2_user mm2u,m_menu mm, m_menu_ctg mmc
                                                              where mm2u.user_id = #manageId#
                                                              and mm2u.m_id = mm.m_id
                                                              AND mm.menu_group = #menuGroup#
                                                              and mm2u.status_cd = ''0''
                                                              and mm.status_cd = ''0''
                                                              and mmc.m_id = mm.m_id
                                                              and mmc.status_cd = ''0''
                                                              order by mm.seq asc"
                                                 }','','{"PARAM":{
                                                            "param1": "$.#menus#Array"
                                                            },"TEMPLATE":{
                                                         "response": {
                                                           "code": "0000",
                                                           "message": "成功"
                                                         }
                                                       }}','','0');

INSERT INTO c_service_sql(service_code,`name`,params,query_model,`sql`,proc,template,remark,status_cd)
VALUES('query.user.loginInfo','查询用户登录信息','userCode','1','{
                                                 	"param1":"SELECT ''10001'' userId,''admin'' userName,''d57167e07915c9428b1c3aae57003807'' userPwd FROM DUAL WHERE #userCode#=''admin''"
                                                 }','','{"PARAM":{
                                                            "param1": "$.#user#Object"
                                                            },"TEMPLATE":{
                                                         "response": {
                                                           "code": "0000",
                                                           "message": "成功"
                                                         }
                                                       }}','','0');

INSERT INTO c_service_sql(service_code,`name`,params,query_model,`sql`,proc,template,remark,status_cd)
VALUES('query.console.template','查询模板信息','templateCode','1','{
                                                 	"param1":"SELECT t.template_code templateCode,t.name templateName,t.`html_name` htmlName,t.`url` templateUrl
                                                              FROM c_template t WHERE t.status_cd = ''0'' AND t.template_code = #templateCode#"
                                                 }','','{"PARAM":{
                                                            "param1": "$.#template#Object"
                                                            },"TEMPLATE":{
                                                         "response": {
                                                           "code": "0000",
                                                           "message": "成功"
                                                         }
                                                       }}','','0');


INSERT INTO c_service_sql(service_code,`name`,params,query_model,`sql`,proc,template,remark,status_cd)
VALUES('query.console.templateCol','查询模板列信息','templateCode','1','{
                                                 	"param1":"SELECT t.template_code templateCode,t.name templateName,tc.col_name colName,tc.col_model colModel FROM c_template t,c_template_col tc WHERE t.status_cd = ''0'' AND t.template_code = tc.template_code
                                                              AND tc.status_cd = ''0''
                                                              AND t.template_code = #templateCode#"
                                                 }','','{"PARAM":{
                                                            "param1": "$.#template#Array"
                                                            },"TEMPLATE":{
                                                         "response": {
                                                           "code": "0000",
                                                           "message": "成功"
                                                         }
                                                       }}','','0');

INSERT INTO c_service_sql(service_code,`name`,params,query_model,`sql`,proc,template,remark,status_cd)
VALUES('query.center.mapping','查询映射数据','page,rows,sord','1','{"param1":"select count(1) records,ceil(#page#/#rows#)+1 page,ceil(count(1)/#rows#) total from c_mapping m where m.status_cd = ''0''",
                                                             "param2":"SELECT m.`id` id,m.`domain` domain,m.name name,m.`key`  `key` ,m.`value` `value`,m.`remark` remark FROM c_mapping m WHERE m.`status_cd` = ''0'' LIMIT #page#, #rows#"
                                                             }','','{
                                                        	"PARAM": {
                                                        		"param1": "$.##Object",
                                                        		"param2": "$.#rows#Array"
                                                        	},
                                                        	"TEMPLATE": {
                                                        		"response": {
                                                        			"code": "0000",
                                                        			"message": "成功"
                                                        		}
                                                        	}
                                                        }','','0');
INSERT INTO c_service_sql(service_code,`name`,params,query_model,`sql`,proc,template,remark,status_cd)
VALUES('query.center.apps','查询外部应用','page,rows,sord','1','{"param1":"select count(1) records,ceil(#page#/#rows#)+1 page,ceil(count(1)/#rows#) total from c_app a where a.status_cd = ''0''",
                                                             "param2":"SELECT m.`id` id,m.`app_id` appId,m.name `name`,m.`security_code`  securityCode ,m.`while_list_ip` whileListIp,m.`black_list_ip` blackListIp,m.`remark` remark FROM c_app m WHERE m.`status_cd` = ''0'' LIMIT #page#, #rows#"
                                                             }','','{
                                                        	"PARAM": {
                                                        		"param1": "$.##Object",
                                                        		"param2": "$.#rows#Array"
                                                        	},
                                                        	"TEMPLATE": {
                                                        		"response": {
                                                        			"code": "0000",
                                                        			"message": "成功"
                                                        		}
                                                        	}
                                                        }','','0');

INSERT INTO c_service_sql(service_code,`name`,params,query_model,`sql`,proc,template,remark,status_cd)
VALUES('query.center.services','查询服务数据','page,rows,sord','1','{"param1":"select count(1) records,ceil(#page#/#rows#)+1 page,ceil(count(1)/#rows#) total from c_service a where a.status_cd = ''0''",
                                                             "param2":"SELECT s.`service_id` serviceId,s.`service_code` serviceCode,s.`business_type_cd`  businessTypeCd,s.name `name`,s.is_instance isInstance,
                                                                       s.`messageQueueName` messageQueueName,s.url url,s.`provide_app_id` provideAppId FROM c_service s WHERE s.`status_cd` = ''0'' LIMIT #page#, #rows#"
                                                             }','','{
                                                        	"PARAM": {
                                                        		"param1": "$.##Object",
                                                        		"param2": "$.#rows#Array"
                                                        	},
                                                        	"TEMPLATE": {
                                                        		"response": {
                                                        			"code": "0000",
                                                        			"message": "成功"
                                                        		}
                                                        	}
                                                        }','','0');

INSERT INTO c_service_sql(service_code,`name`,params,query_model,`sql`,proc,template,remark,status_cd)
VALUES('query.center.route','查询路由数据','page,rows,sord','1','{"param1":"select count(1) records,ceil(#page#/#rows#)+1 page,ceil(count(1)/#rows#) total from c_route a,c_service cs WHERE a.`service_id` = cs.`service_id` AND cs.`status_cd` = ''0'' and a.status_cd = ''0''",
                                                             "param2":"SELECT s.id id,s.`app_id` appId,s.`service_id` serviceId,s.`invoke_model` invokeModel,cs.`name` serviceName,cs.`service_code` serviceCode,s.`order_type_cd` orderTypeCd,s.`invoke_limit_times` invokelimitTimes FROM c_route s,c_service cs WHERE s.`service_id` = cs.`service_id` AND cs.`status_cd` = ''0'' AND s.`status_cd` = ''0'' LIMIT #page#, #rows#"
                                                             }','','{
                                                        	"PARAM": {
                                                        		"param1": "$.##Object",
                                                        		"param2": "$.#rows#Array"
                                                        	},
                                                        	"TEMPLATE": {
                                                        		"response": {
                                                        			"code": "0000",
                                                        			"message": "成功"
                                                        		}
                                                        	}
                                                        }','','0');

INSERT INTO c_service_sql(service_code,`name`,params,query_model,`sql`,proc,template,remark,status_cd)
VALUES('query.console.caches','查询缓存数据','userId','1','{
                                                             "param1":"SELECT c.id id,c.`cache_code` cacheCode, c.`name` cacheName FROM c_cache c, c_cache_2_user c2u WHERE c.`cache_code` = c2u.`cache_code` AND c.`status_cd` = ''0''
                                                                       AND c2u.`status_cd` = ''0'' AND c2u.`user_id` = #userId# AND c.`group` = ''COMMON'' ORDER BY c.`seq` ASC"
                                                             }','','{
                                                        	"PARAM": {
                                                        		"param1": "$.#rows#Array"
                                                        	},
                                                        	"TEMPLATE": {
                                                        		"response": {
                                                        			"code": "0000",
                                                        			"message": "成功"
                                                        		}
                                                        	}
                                                        }','','0');
INSERT INTO c_service_sql(service_code,`name`,params,query_model,`sql`,proc,template,remark,status_cd)
VALUES('query.console.cache','查询单条缓存信息','cacheCode','1','{
                                                             "param1":"SELECT c.id id,c.`cache_code` cacheCode, c.`name` cacheName,c.`param` param,c.`service_code` serviceCode FROM c_cache c WHERE  c.`status_cd` = ''0'' AND c.`cache_code` = #cacheCode#"
                                                             }','','{
                                                        	"PARAM": {
                                                        		"param1": "$.#cache#Object"
                                                        	},
                                                        	"TEMPLATE": {
                                                        		"response": {
                                                        			"code": "0000",
                                                        			"message": "成功"
                                                        		}
                                                        	}
                                                        }','','0');


INSERT INTO c_service_sql(service_code,`name`,params,query_model,`sql`,proc,template,remark,status_cd)
VALUES('save.center.mapping','保存映射信息','domain,name,key,value,remark','1','{
                                                             "param1":"INSERT c_mapping(domain,`name`,`key`,`value`,remark) VALUES(#domain#,#name#,#key#,#value#,#remark#)"
                                                             }','','{
                                                        	"TEMPLATE": {
                                                        		"response": {
                                                        			"code": "0000",
                                                        			"message": "成功"
                                                        		}
                                                        	}
                                                        }','','0');
INSERT INTO c_service_sql(service_code,`name`,params,query_model,`sql`,proc,template,remark,status_cd)
VALUES('delete.center.mapping','删除映射信息','id','1','{
                                                             "param1":"UPDATE c_mapping m SET m.status_cd = ''1'' WHERE m.status_cd = ''0'' AND m.id = #id#"
                                                             }','','{
                                                        	"TEMPLATE": {
                                                        		"response": {
                                                        			"code": "0000",
                                                        			"message": "成功"
                                                        		}
                                                        	}
                                                        }','','0');
INSERT INTO c_service_sql(service_code,`name`,params,query_model,`sql`,proc,template,remark,status_cd)
VALUES('update.center.mapping','修改映射信息','id,domain,name,key,value,remark','1','{
                                                             "param1":"UPDATE c_mapping m SET m.domain=#domain#,m.name = #name#,m.key=#key#,m.value=#value#,m.remark=#remark# WHERE m.status_cd = ''0'' AND m.id = #id#"
                                                             }','','{
                                                        	"TEMPLATE": {
                                                        		"response": {
                                                        			"code": "0000",
                                                        			"message": "成功"
                                                        		}
                                                        	}
                                                        }','','0');

create table c_comment(
    comment_id VARCHAR(30) NOT NULL COMMENT '评论ID',
    user_id varchar(30) not null comment '评论者用户ID',
    b_id VARCHAR(30) NOT NULL COMMENT '订单ID',
    comment_type_cd varchar(2) not null default 'S' comment '评论类型 S表示 商品 M表示 商户 T 物流',
    out_id varchar(30) not null comment '外部ID，如商品ID 商户ID等',
    `month` INT NOT NULL COMMENT '月份',
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    status_cd VARCHAR(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考c_status表，S 保存，0, 在用 1失效'
)
PARTITION BY RANGE (`month`) (
    PARTITION comment_1 VALUES LESS THAN (2),
    PARTITION comment_2 VALUES LESS THAN (3),
    PARTITION comment_3 VALUES LESS THAN (4),
    PARTITION comment_4 VALUES LESS THAN (5),
    PARTITION comment_5 VALUES LESS THAN (6),
    PARTITION comment_6 VALUES LESS THAN (7),
    PARTITION comment_7 VALUES LESS THAN (8),
    PARTITION comment_8 VALUES LESS THAN (9),
    PARTITION comment_9 VALUES LESS THAN (10),
    PARTITION comment_10 VALUES LESS THAN (11),
    PARTITION comment_11 VALUES LESS THAN (12),
    PARTITION comment_12 VALUES LESS THAN (13)
);

CREATE INDEX idx_comment_b_id ON c_comment(b_id);
CREATE INDEX idx_comment_out_id ON c_comment(out_id);
-- 评论子表
create table c_sub_comment(
    sub_comment_id varchar(30) not null comment '子评论ID',
    comment_id varchar(30) not null  comment '评论ID ',
    b_id VARCHAR(30) NOT NULL COMMENT '订单ID',
    parent_sub_comment_id varchar(30) not null default '-1' comment '父 子评论ID 如果不是回复 写成-1',
    sub_comment_type_cd varchar(2) not null default 'C' comment '评论类型 C 评论 R 回复 A 追加',
    comment_context LONGTEXT not null COMMENT '评论内容',
    `month` INT NOT NULL COMMENT '月份',
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    status_cd VARCHAR(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考c_status表，S 保存，0, 在用 1失效'
)
PARTITION BY RANGE (`month`) (
    PARTITION sub_comment_1 VALUES LESS THAN (2),
    PARTITION sub_comment_2 VALUES LESS THAN (3),
    PARTITION sub_comment_3 VALUES LESS THAN (4),
    PARTITION sub_comment_4 VALUES LESS THAN (5),
    PARTITION sub_comment_5 VALUES LESS THAN (6),
    PARTITION sub_comment_6 VALUES LESS THAN (7),
    PARTITION sub_comment_7 VALUES LESS THAN (8),
    PARTITION sub_comment_8 VALUES LESS THAN (9),
    PARTITION sub_comment_9 VALUES LESS THAN (10),
    PARTITION sub_comment_10 VALUES LESS THAN (11),
    PARTITION sub_comment_11 VALUES LESS THAN (12),
    PARTITION sub_comment_12 VALUES LESS THAN (13)
);

CREATE INDEX idx_sub_comment_b_id ON c_sub_comment(b_id);
CREATE INDEX idx_sub_comment_comment_id ON c_sub_comment(comment_id);
CREATE INDEX idx_sub_comment_parent_sub_comment_id ON c_sub_comment(parent_sub_comment_id);

-- 属性
create table c_sub_comment_attr(
    attr_id VARCHAR(30) NOT NULL COMMENT '属性id',
    sub_comment_id VARCHAR(30) NOT NULL COMMENT '子评论ID',
    b_id VARCHAR(30) NOT NULL COMMENT '订单ID',
    spec_cd VARCHAR(12) NOT NULL COMMENT '规格id,参考spec表',
    value VARCHAR(50) NOT NULL COMMENT '属性值',
    `month` INT NOT NULL COMMENT '月份',
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    status_cd VARCHAR(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考c_status表，S 保存，0, 在用 1失效'
)
PARTITION BY RANGE (`month`) (
    PARTITION sub_comment_attr_1 VALUES LESS THAN (2),
    PARTITION sub_comment_attr_2 VALUES LESS THAN (3),
    PARTITION sub_comment_attr_3 VALUES LESS THAN (4),
    PARTITION sub_comment_attr_4 VALUES LESS THAN (5),
    PARTITION sub_comment_attr_5 VALUES LESS THAN (6),
    PARTITION sub_comment_attr_6 VALUES LESS THAN (7),
    PARTITION sub_comment_attr_7 VALUES LESS THAN (8),
    PARTITION sub_comment_attr_8 VALUES LESS THAN (9),
    PARTITION sub_comment_attr_9 VALUES LESS THAN (10),
    PARTITION sub_comment_attr_10 VALUES LESS THAN (11),
    PARTITION sub_comment_attr_11 VALUES LESS THAN (12),
    PARTITION sub_comment_attr_12 VALUES LESS THAN (13)
);

CREATE INDEX idx_sub_comment_attr_b_id ON c_sub_comment_attr(b_id);
CREATE INDEX idx_sub_comment_attr_sub_comment_id ON c_sub_comment_attr(sub_comment_id);
CREATE INDEX idx_sub_comment_attr_spec_cd ON c_sub_comment_attr(spec_cd);



-- 评论 照片
CREATE TABLE c_sub_comment_photo(
    comment_photo_id VARCHAR(30) NOT NULL COMMENT '评论照片ID',
    b_id VARCHAR(30) NOT NULL COMMENT '业务Id',
    sub_comment_id VARCHAR(30) NOT NULL COMMENT '商店ID',
    comment_photo_type_cd VARCHAR(12) NOT NULL default 'S' COMMENT '评论照片类型,S 商品照片 M 商户ID',
    photo VARCHAR(100) NOT NULL COMMENT '照片',
    `month` INT NOT NULL COMMENT '月份',
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    status_cd VARCHAR(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考c_status表，S 保存，0, 在用 1失效'
)
PARTITION BY RANGE (`month`) (
    PARTITION sub_comment_photo_1 VALUES LESS THAN (2),
    PARTITION sub_comment_photo_2 VALUES LESS THAN (3),
    PARTITION sub_comment_photo_3 VALUES LESS THAN (4),
    PARTITION sub_comment_photo_4 VALUES LESS THAN (5),
    PARTITION sub_comment_photo_5 VALUES LESS THAN (6),
    PARTITION sub_comment_photo_6 VALUES LESS THAN (7),
    PARTITION sub_comment_photo_7 VALUES LESS THAN (8),
    PARTITION sub_comment_photo_8 VALUES LESS THAN (9),
    PARTITION sub_comment_photo_9 VALUES LESS THAN (10),
    PARTITION sub_comment_photo_10 VALUES LESS THAN (11),
    PARTITION sub_comment_photo_11 VALUES LESS THAN (12),
    PARTITION sub_comment_photo_12 VALUES LESS THAN (13)
);
CREATE INDEX idx_sub_comment_photo_b_id ON c_sub_comment_photo(b_id);
CREATE INDEX idx_sub_comment_photo_sub_comment_id ON c_sub_comment_photo(sub_comment_id);
-- 评论分数
CREATE TABLE c_comment_score(
    comment_score_id VARCHAR(30) NOT NULL COMMENT '评论分数ID',
    comment_id VARCHAR(30) NOT NULL COMMENT '评论ID',
    b_id VARCHAR(30) NOT NULL COMMENT '业务Id',
    score_type_cd VARCHAR(30) NOT NULL COMMENT '打分类别，S 商品相符，U 卖家打分，T 物流打分',
    `value` INT NOT NULL COMMENT '分数',
    `month` INT NOT NULL COMMENT '月份',
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    status_cd VARCHAR(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考c_status表，S 保存，0, 在用 1失效'
)
PARTITION BY RANGE (`month`) (
    PARTITION comment_score_1 VALUES LESS THAN (2),
    PARTITION comment_score_2 VALUES LESS THAN (3),
    PARTITION comment_score_3 VALUES LESS THAN (4),
    PARTITION comment_score_4 VALUES LESS THAN (5),
    PARTITION comment_score_5 VALUES LESS THAN (6),
    PARTITION comment_score_6 VALUES LESS THAN (7),
    PARTITION comment_score_7 VALUES LESS THAN (8),
    PARTITION comment_score_8 VALUES LESS THAN (9),
    PARTITION comment_score_9 VALUES LESS THAN (10),
    PARTITION comment_score_10 VALUES LESS THAN (11),
    PARTITION comment_score_11 VALUES LESS THAN (12),
    PARTITION comment_score_12 VALUES LESS THAN (13)
);

CREATE INDEX idx_comment_score_b_id ON c_comment_score(b_id);
CREATE INDEX idx_comment_score_comment_id ON c_comment_score(comment_id);


create table m_menu(
    m_id INT NOT NULL AUTO_INCREMENT KEY COMMENT '菜单ID',
    name varchar(10) not null comment '菜单名称',
    level varchar(2) not null comment '菜单级别 一级菜单 为 1 二级菜单为2',
    parent_id int not null comment '父类菜单id 如果是一类菜单则写为-1 如果是二类菜单则写父类的菜单id',
    menu_group varchar(10) not null comment '菜单组 left 显示在页面左边的菜单',
    user_id varchar(12) not null comment '创建菜单的用户id',
    remark VARCHAR(200) COMMENT '描述',
    seq INT NOT NULL  COMMENT '列顺序',
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    status_cd VARCHAR(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考c_status表，0在用，1失效'
);

-- m_menu_ctg

create table m_menu_ctg(
    m_ctg_id INT NOT NULL AUTO_INCREMENT KEY COMMENT '菜单配置ID',
    m_id INT NOT NULL COMMENT '菜单ID',
    url varchar(100) not null comment '菜单打开地址',
    template varchar(50) comment '页面模板 模板名称',
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    status_cd VARCHAR(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考c_status表，0在用，1失效'
);

-- m_menu_2_user

create table m_menu_2_user(
    m_user_id INT NOT NULL AUTO_INCREMENT KEY COMMENT '菜单用户ID',
    m_id int not null comment '菜单id',
    user_id varchar(30) not null comment '用户id',
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    status_cd VARCHAR(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考c_status表，0在用，1失效'
);

insert into m_menu(name,level,parent_id,menu_group,user_id,remark,seq) values('系统配置','1','-1','LEFT','10001','',1);
insert into m_menu(name,level,parent_id,menu_group,user_id,remark,seq) values('映射管理','2','1','LEFT','10001','',2);
insert into m_menu(name,level,parent_id,menu_group,user_id,remark,seq) values('外部应用','2','1','LEFT','10001','',3);
insert into m_menu(name,level,parent_id,menu_group,user_id,remark,seq) values('路由管理','2','1','LEFT','10001','',4);
insert into m_menu(name,level,parent_id,menu_group,user_id,remark,seq) values('服务管理','2','1','LEFT','10001','',5);
insert into m_menu(name,level,parent_id,menu_group,user_id,remark,seq) values('缓存管理','1','-1','LEFT','10001','',1);
insert into m_menu(name,level,parent_id,menu_group,user_id,remark,seq) values('刷新缓存','2','1','LEFT','10001','',2);

insert into m_menu_ctg(m_id,url,template) values(1,'#','');
insert into m_menu_ctg(m_id,url,template) values(2,'/console/list?templateCode=mapping','');
insert into m_menu_ctg(m_id,url,template) values(3,'/console/list?templateCode=app','');
insert into m_menu_ctg(m_id,url,template) values(4,'/console/list?templateCode=service','');
insert into m_menu_ctg(m_id,url,template) values(5,'/console/list?templateCode=route','');
insert into m_menu_ctg(m_id,url,template) values(6,'#','');
insert into m_menu_ctg(m_id,url,template) values(7,'/','');


insert into m_menu_2_user(m_id,user_id) values(1,'10001');
insert into m_menu_2_user(m_id,user_id) values(2,'10001');
insert into m_menu_2_user(m_id,user_id) values(3,'10001');
insert into m_menu_2_user(m_id,user_id) values(4,'10001');
insert into m_menu_2_user(m_id,user_id) values(5,'10001');
insert into m_menu_2_user(m_id,user_id) values(6,'10001');
insert into m_menu_2_user(m_id,user_id) values(7,'10001');

-- c_template
create table c_template(
    id INT NOT NULL AUTO_INCREMENT KEY COMMENT '模板ID',
    template_code varchar(20) not null UNIQUE comment '模板编码 模板英文名',
    name varchar(50) not null comment '模板名称',
    html_name varchar(20) not null comment '对应HTML文件名称',
    url varchar(200) not null comment '查询数据，修改数据url 其真实地址对应于mapping表中 LIST->key 对应 查询多条数据 QUERY->key 对应单条数据 UPDATE-> 对应修改数据 DELETE->key 对应删除数据 多条之间用 ; 分隔',
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    status_cd VARCHAR(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考c_status表，0在用，1失效'
);

-- c_template_col

create table c_template_col(
    id INT NOT NULL AUTO_INCREMENT KEY COMMENT '模板ID',
    template_code varchar(20) not null comment '模板编码 模板英文名',
    col_name varchar(50) not null comment '前台显示名称',
    col_code varchar(20) not null comment '字段的编码',
    col_model longtext not null comment 'jqgrid的colmodel',
    seq INT NOT NULL  COMMENT '列顺序',
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    status_cd VARCHAR(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考c_status表，0在用，1失效'
);


insert into c_template(template_code,name,html_name,url) values('mapping','映射管理','list_template','LIST->query.center.mapping;QUERY->mapping_query_url;INSERT->save.center.mapping;UPDATE->update.center.mapping;DELETE->delete.center.mapping');

insert into c_template_col(template_code,col_name,col_code,col_model,seq) values('mapping','列ID','id','{ "name": "id","index": "id","width": "90",
                                                                                                             "editable": true,
                                                                                                             "sorttype": "int" }',1);
insert into c_template_col(template_code,col_name,col_code,col_model,seq) values('mapping','域','domain','{ "name": "domain","index": "domain","width": "90",
                                                                                                             "editable": true,
                                                                                                             "formatoptions": { "defaultValue": "DOMAIN.COMMON" }
                                                                                                           }',2);
insert into c_template_col(template_code,col_name,col_code,col_model,seq) values('mapping','名称','name','{ "name": "name","index": "name","width": "90",
                                                                                                             "editable": true }',3);

insert into c_template_col(template_code,col_name,col_code,col_model,seq) values('mapping','键','key','{ "name": "key","index": "key","width": "90",
                                                                                                             "editable": true }',4);

insert into c_template_col(template_code,col_name,col_code,col_model,seq) values('mapping','值','value','{ "name": "value","index": "value","width": "90",
                                                                                                             "editable": true }',5);
insert into c_template_col(template_code,col_name,col_code,col_model,seq) values('mapping','备注','value','{ "name": "remark","index": "remark","width": "90",
                                                                                                             "editable": true }',6);

INSERT INTO c_template_col(template_code,col_name,col_code,col_model,seq) VALUES('mapping','BUTTON','BUTTON','{
                                                                                                            "name": "detail",
                                                                                                            "index": "",
                                                                                                            "width": "40",
                                                                                                            "fixed": "true",
                                                                                                            "sortable": "false",
                                                                                                            "resize": "false",
                                                                                                            "formatter": "function(cellvalue, options, rowObject){\n var temp =\"<div style=''margin-left:8px;''><div title=''详情记录'' style=''float:left;cursor:pointer;'' class=''ui-pg-div'' id=''jEditButton_3'' onclick=''detail(\"+rowObject+\")'' onmouseover=''jQuery(this).addClass(''ui-state-hover'');'' onmouseout=''jQuery(this).removeClass(''ui-state-hover'');''><span class=''ui-icon fa-search-plus''/></div></div>\";\n return temp; \n}"
                                                                                                          }',7);


insert into c_template(template_code,name,html_name,url) values('app','外部应用','list_template','LIST->query.center.apps;QUERY->query.center.app');

insert into c_template_col(template_code,col_name,col_code,col_model,seq) values('app','列ID','id','{ "name": "id","index": "id","width": "20",
                                                                                                             "editable": true,
                                                                                                             "sorttype": "int" }',1);
insert into c_template_col(template_code,col_name,col_code,col_model,seq) values('app','AppId','domain','{ "name": "appId","index": "appId","width": "40",
                                                                                                             "editable": true
                                                                                                           }',2);
insert into c_template_col(template_code,col_name,col_code,col_model,seq) values('app','名称','name','{ "name": "name","index": "name","width": "50",
                                                                                                             "editable": true }',3);

insert into c_template_col(template_code,col_name,col_code,col_model,seq) values('app','秘钥','securityCode','{ "name": "securityCode","index": "securityCode","width": "50",
                                                                                                             "editable": true }',4);

insert into c_template_col(template_code,col_name,col_code,col_model,seq) values('app','白名单','whileListIp','{ "name": "whileListIp","index": "whileListIp","width": "90",
                                                                                                             "editable": true }',5);

insert into c_template_col(template_code,col_name,col_code,col_model,seq) values('app','黑名单','blackListIp','{ "name": "blackListIp","index": "blackListIp","width": "40",
                                                                                                             "editable": true }',6);

insert into c_template_col(template_code,col_name,col_code,col_model,seq) values('app','备注','value','{ "name": "remark","index": "remark","width": "90",
                                                                                                             "editable": true }',7);

INSERT INTO c_template_col(template_code,col_name,col_code,col_model,seq) VALUES('app','BUTTON','BUTTON','{
                                                                                                            "name": "detail",
                                                                                                            "index": "",
                                                                                                            "width": "40",
                                                                                                            "fixed": "true",
                                                                                                            "sortable": "false",
                                                                                                            "resize": "false",
                                                                                                            "formatter": "function(cellvalue, options, rowObject){\n var temp =\"<div style=''margin-left:8px;''><div title=''详情记录'' style=''float:left;cursor:pointer;'' class=''ui-pg-div'' id=''jEditButton_3'' onclick=''detail(\"+rowObject+\")'' onmouseover=''jQuery(this).addClass(''ui-state-hover'');'' onmouseout=''jQuery(this).removeClass(''ui-state-hover'');''><span class=''ui-icon fa-search-plus''/></div></div>\";\n return temp; \n}"
                                                                                                          }',8);


insert into c_template(template_code,name,html_name,url) values('service','服务管理','list_template','LIST->query.center.services;QUERY->query.center.service');

insert into c_template_col(template_code,col_name,col_code,col_model,seq) values('service','服务ID','serviceId','{ "name": "serviceId","index": "serviceId","width": "20",
                                                                                                             "editable": true,
                                                                                                             "sorttype": "int" }',1);
insert into c_template_col(template_code,col_name,col_code,col_model,seq) values('service','服务编码','serviceCode','{ "name": "serviceCode","index": "serviceCode","width": "40",
                                                                                                             "editable": true
                                                                                                           }',2);

insert into c_template_col(template_code,col_name,col_code,col_model,seq) values('service','业务类型','businessTypeCd','{ "name": "businessTypeCd","index": "businessTypeCd","width": "50",
                                                                                                             "editable": true }',3);

insert into c_template_col(template_code,col_name,col_code,col_model,seq) values('service','名称','name','{ "name": "name","index": "name","width": "40",
                                                                                                             "editable": true }',4);

insert into c_template_col(template_code,col_name,col_code,col_model,seq) values('service','消息队列','messageQueueName','{ "name": "messageQueueName","index": "messageQueueName","width": "10",
                                                                                                             "editable": true }',5);
insert into c_template_col(template_code,col_name,col_code,col_model,seq) values('service','需要Instance','isInstance','{ "name": "isInstance","index": "isInstance","width": "10",
                                                                                                             "editable": true }',6);

insert into c_template_col(template_code,col_name,col_code,col_model,seq) values('service','URL','url','{ "name": "url","index": "url","width": "60",
                                                                                                             "editable": true }',7);

insert into c_template_col(template_code,col_name,col_code,col_model,seq) values('service','提供者AppId','provideAppId','{ "name": "provideAppId","index": "provideAppId","width": "10",
                                                                                                             "editable": true }',8);

INSERT INTO c_template_col(template_code,col_name,col_code,col_model,seq) VALUES('service','BUTTON','BUTTON','{
                                                                                                            "name": "detail",
                                                                                                            "index": "",
                                                                                                            "width": "40",
                                                                                                            "fixed": "true",
                                                                                                            "sortable": "false",
                                                                                                            "resize": "false",
                                                                                                            "formatter": "function(cellvalue, options, rowObject){\n var temp =\"<div style=''margin-left:8px;''><div title=''详情记录'' style=''float:left;cursor:pointer;'' class=''ui-pg-div'' id=''jEditButton_3'' onclick=''detail(\"+rowObject+\")'' onmouseover=''jQuery(this).addClass(''ui-state-hover'');'' onmouseout=''jQuery(this).removeClass(''ui-state-hover'');''><span class=''ui-icon fa-search-plus''/></div></div>\";\n return temp; \n}"
                                                                                                          }',9);


insert into c_template(template_code,name,html_name,url) values('route','路由管理','list_template','LIST->query.center.routes;QUERY->query.center.route');

insert into c_template_col(template_code,col_name,col_code,col_model,seq) values('route','路由ID','id','{ "name": "id","index": "id","width": "10",
                                                                                                             "editable": true,
                                                                                                             "sorttype": "int" }',1);
insert into c_template_col(template_code,col_name,col_code,col_model,seq) values('route','AppId','appId','{ "name": "appId","index": "appId","width": "30",
                                                                                                             "editable": true
                                                                                                           }',2);
insert into c_template_col(template_code,col_name,col_code,col_model,seq) values('route','服务ID','serviceId','{ "name": "serviceId","index": "serviceId","width": "30",
                                                                                                             "editable": true }',3);
insert into c_template_col(template_code,col_name,col_code,col_model,seq) values('route','调用方式','invokeModel','{ "name": "invokeModel","index": "invokeModel","width": "50",
                                                                                                              "editable": true }',4);
insert into c_template_col(template_code,col_name,col_code,col_model,seq) values('route','服务名称','serviceName','{ "name": "serviceName","index": "serviceName","width": "30",
                                                                                                             "editable": true }',5);
insert into c_template_col(template_code,col_name,col_code,col_model,seq) values('route','服务编码','serviceCode','{ "name": "serviceCode","index": "serviceCode","width": "30",
                                                                                                             "editable": true }',6);

insert into c_template_col(template_code,col_name,col_code,col_model,seq) values('route','订单类型','orderTypeCd','{ "name": "orderTypeCd","index": "orderTypeCd","width": "30",
                                                                                                             "editable": true }',7);

insert into c_template_col(template_code,col_name,col_code,col_model,seq) values('route','调用次数限制','invokelimitTimes','{ "name": "invokelimitTimes","index": "invokelimitTimes","width": "40",
                                                                                                             "editable": true }',8);

INSERT INTO c_template_col(template_code,col_name,col_code,col_model,seq) VALUES('route','BUTTON','BUTTON','{
                                                                                                            "name": "detail",
                                                                                                            "index": "",
                                                                                                            "width": "40",
                                                                                                            "fixed": "true",
                                                                                                            "sortable": "false",
                                                                                                            "resize": "false",
                                                                                                            "formatter": "function(cellvalue, options, rowObject){\n var temp =\"<div style=''margin-left:8px;''><div title=''详情记录'' style=''float:left;cursor:pointer;'' class=''ui-pg-div'' id=''jEditButton_3'' onclick=''detail(\"+rowObject+\")'' onmouseover=''jQuery(this).addClass(''ui-state-hover'');'' onmouseout=''jQuery(this).removeClass(''ui-state-hover'');''><span class=''ui-icon fa-search-plus''/></div></div>\";\n return temp; \n}"}',9);


insert into c_template(template_code,name,html_name,url) values('cache','刷新缓存','list_template_cache','LIST->query.center.caches;QUERY->query.center.cacheOne');

insert into c_template_col(template_code,col_name,col_code,col_model,seq) values('cache','缓存ID','id','{ "name": "id","index": "id","width": "10",
                                                                                                             "editable": true,
                                                                                                             "sorttype": "int" }',1);

insert into c_template_col(template_code,col_name,col_code,col_model,seq) values('cache','缓存编码','cacheCode','{ "name": "cacheCode","index": "cacheCode","width": "30",
                                                                                                             "editable": true }',2);
insert into c_template_col(template_code,col_name,col_code,col_model,seq) values('cache','缓存名称','cacheName','{ "name": "cacheName","index": "cacheName","width": "30",
                                                                                                             "editable": true }',3);
INSERT INTO c_template_col(template_code,col_name,col_code,col_model,seq) VALUES('cache','BUTTON','BUTTON','{
                                                                                                            "name": "detail",
                                                                                                            "index": "",
                                                                                                            "width": "40",
                                                                                                            "fixed": "true",
                                                                                                            "sortable": "false",
                                                                                                            "resize": "false",
                                                                                                            "formatter": ""function(cellvalue, options, rowObject){ var temp =\"<div style=''margin-left:8px;''><button type=''button'' class=''btn btn-warning'' style=''border-radius: .25rem;'' onclick=''flush(this,\"+rowObject.cacheCode+\")''>刷新缓存</button></div>\";return temp; }"
                                                                                                          }',4);


-- 缓存配置表
CREATE TABLE c_cache(
    id INT NOT NULL AUTO_INCREMENT KEY COMMENT '缓存ID',
    cache_code  VARCHAR(10) NOT NULL UNIQUE COMMENT '缓存编码 开始于1001',
    service_code  VARCHAR(50) NOT NULL COMMENT '调用服务编码 对应 c_service',
    `name` VARCHAR(50) NOT NULL COMMENT '前台显示名称',
    param LONGTEXT NOT NULL COMMENT '请求缓存系统时的参数',
    seq INT NOT NULL  COMMENT '列顺序',
    `group` VARCHAR(10) NOT NULL DEFAULT 'COMMON' COMMENT '组，缓存属于哪个组',
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    status_cd VARCHAR(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考c_status表，0在用，1失效'
);
-- 缓存用户表
create table c_cache_2_user(
     id INT NOT NULL AUTO_INCREMENT KEY COMMENT '缓存用户ID',
     cache_code int not null comment '缓存编码',
     user_id varchar(30) not null comment '用户id',
     create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
     status_cd VARCHAR(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考c_status表，0在用，1失效'
);


insert into  c_cache (cache_code,service_code,`name`,param,seq) values('1001','flush.center.cache','映射缓存（c_mapping表）','{"cacheName":"MAPPING"}',1);
insert into  c_cache (cache_code,service_code,`name`,param,seq) values('1002','flush.center.cache','业务配置缓存（c_app,c_service,c_route表）','{"cacheName":"APP_ROUTE_SERVICE"}',2);
insert into  c_cache (cache_code,service_code,`name`,param,seq) values('1003','flush.center.cache','公用服务缓存（c_service_sql表）','{"cacheName":"SERVICE_SQL"}',3);

insert into c_cache_2_user(cache_code,user_id) values('1001','10001');
insert into c_cache_2_user(cache_code,user_id) values('1002','10001');
insert into c_cache_2_user(cache_code,user_id) values('1003','10001');



create table l_transaction_log(
    log_id varchar(30) not null  COMMENT 'id',
    transaction_id VARCHAR(30) NOT NULL COMMENT '外部交易流水',
    contract_id varchar(64) not null comment '上下文ID',
    ip varchar(20) not null comment '日志产生主机IP',
    port varchar(10) not null comment '日志产生端口',
    src_ip varchar(20) comment '调用方IP',
    src_port varchar(10) comment '调用方端口',
    app_id varchar(30) not null comment '调用方应用ID',
    user_id varchar(30) comment '用户ID',
    service_code varchar(50) comment '服务编码',
    service_name varchar(50) comment '服务名称',
    timestamp TIMESTAMP NOT NULL comment '日志交互时间，时间戳',
    cost_time int not null default 0 comment '耗时',
    status_cd varchar(2) not null comment '交互状态 S 成功 F 失败',
    month INT NOT NULL comment '月份',
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    unique KEY (log_id,month)
)
partition BY RANGE (month) (
    partition transaction_log_1 VALUES LESS THAN (2),
    partition transaction_log_2 VALUES LESS THAN (3),
    partition transaction_log_3 VALUES LESS THAN (4),
    partition transaction_log_4 VALUES LESS THAN (5),
    partition transaction_log_5 VALUES LESS THAN (6),
    partition transaction_log_6 VALUES LESS THAN (7),
    partition transaction_log_7 VALUES LESS THAN (8),
    partition transaction_log_8 VALUES LESS THAN (9),
    partition transaction_log_9 VALUES LESS THAN (10),
    partition transaction_log_10 VALUES LESS THAN (11),
    partition transaction_log_11 VALUES LESS THAN (12),
    partition transaction_log_12 VALUES LESS THAN (13)
);



-- 日志原始内容表

create table l_transaction_log_message(
    log_id varchar(30) not null  COMMENT 'id',
    request_header LONGTEXT COMMENT '请求头信息',
    response_header LONGTEXT COMMENT '返回头信息',
    request_message LONGTEXT comment '请求报文',
    response_message LONGTEXT comment '返回报文',
    remark varchar(200) comment '备注',
    month INT NOT NULL  comment '月份',
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
     UNIQUE KEY (log_id,`month`)
)
partition BY RANGE (month) (
    partition transaction_log_message_1 VALUES LESS THAN (2),
    partition transaction_log_message_2 VALUES LESS THAN (3),
    partition transaction_log_message_3 VALUES LESS THAN (4),
    partition transaction_log_message_4 VALUES LESS THAN (5),
    partition transaction_log_message_5 VALUES LESS THAN (6),
    partition transaction_log_message_6 VALUES LESS THAN (7),
    partition transaction_log_message_7 VALUES LESS THAN (8),
    partition transaction_log_message_8 VALUES LESS THAN (9),
    partition transaction_log_message_9 VALUES LESS THAN (10),
    partition transaction_log_message_10 VALUES LESS THAN (11),
    partition transaction_log_message_11 VALUES LESS THAN (12),
    partition transaction_log_message_12 VALUES LESS THAN (13)
);



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

-- 店铺种类
create table store_type(
    id INT NOT NULL AUTO_INCREMENT KEY COMMENT 'id',
    store_type_cd VARCHAR(12) NOT NULL COMMENT '店铺编码',
    `name` VARCHAR(50) NOT NULL COMMENT '店铺种类编码',
    description VARCHAR(200) COMMENT '描述',
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    unique KEY (store_type_cd)
);


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






