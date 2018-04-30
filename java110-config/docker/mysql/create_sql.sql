
CREATE USER 'TT'@'%' IDENTIFIED BY 'TT@12345678';

GRANT ALL ON *.* TO 'TT'@'%';

create database `TT` default character set utf8 collate utf8_general_ci;

use TT;
-- c_orders

CREATE TABLE c_orders(
    o_id VARCHAR(18) NOT NULL COMMENT '订单ID',
    app_id VARCHAR(10) NOT NULL COMMENT '应用ID',
    ext_transaction_id VARCHAR(30) NOT NULL COMMENT '外部交易流水',
    user_id VARCHAR(12) NOT NULL COMMENT '用户ID',
    request_time VARCHAR(16) NOT NULL COMMENT '外部系统请求时间',
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    order_type_cd VARCHAR(4) NOT NULL COMMENT '订单类型，参考c_order_type表',
    finish_time DATE COMMENT '订单完成时间',
    remark VARCHAR(200) COMMENT '备注',
    status_cd VARCHAR(2) NOT NULL COMMENT '数据状态，详细参考c_status表'
);

-- c_orders_attrs

CREATE TABLE c_orders_attrs(
    o_id VARCHAR(18) NOT NULL COMMENT '订单ID',
    attr_id VARCHAR(18) NOT NULL COMMENT '属性id',
    spec_cd VARCHAR(12) NOT NULL COMMENT '规格id,参考spec表',
    VALUE VARCHAR(50) NOT NULL COMMENT '属性值'
);

-- c_business

CREATE TABLE c_business(
    b_id VARCHAR(18) NOT NULL COMMENT '业务Id',
    o_id VARCHAR(18) NOT NULL COMMENT '订单ID',
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    business_type_cd VARCHAR(4) NOT NULL COMMENT '业务项类型，参考c_business_type表',
    finish_time DATE COMMENT '完成时间',
    remark VARCHAR(200) COMMENT '备注',
    status_cd VARCHAR(2) NOT NULL COMMENT '数据状态，详细参考c_status表'
);

-- c_business_attrs

CREATE TABLE c_business_attrs(
    b_id VARCHAR(18) NOT NULL COMMENT '订单ID',
    attr_id VARCHAR(18) NOT NULL COMMENT '属性id',
    spec_cd VARCHAR(12) NOT NULL COMMENT '规格id,参考spec表',
    VALUE VARCHAR(50) NOT NULL COMMENT '属性值'
);

-- c_status

CREATE TABLE c_status(
    id INT NOT NULL AUTO_INCREMENT KEY COMMENT 'id',
    status_cd VARCHAR(4) NOT NULL COMMENT '状态',
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
INSERT INTO c_status(status_cd,NAME,description) VALUES('C','错误订单','错误订单');

-- c_order_type

CREATE TABLE c_order_type(
    id INT NOT NULL AUTO_INCREMENT KEY COMMENT 'id',
    order_type_cd VARCHAR(4) NOT NULL COMMENT '订单类型',
    `name` VARCHAR(50) NOT NULL COMMENT '名称',
    description VARCHAR(200) COMMENT '描述',
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
);


-- c_business_type

CREATE TABLE c_business_type(
    id INT NOT NULL AUTO_INCREMENT KEY COMMENT 'id',
    business_type_cd VARCHAR(4) NOT NULL COMMENT '业务项类型',
    `name` VARCHAR(50) NOT NULL COMMENT '名称',
    description VARCHAR(200) COMMENT '描述',
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
);

-- spec

CREATE TABLE spec(
    id INT NOT NULL AUTO_INCREMENT KEY COMMENT 'id',
    spec_cd VARCHAR(4) NOT NULL COMMENT '业务项类型规格编码，从x00020001开始每次加一就可以(约定，x=10表示c_orders_attrs 中属性，x=11表示c_business_attrs 中的属性)',
    `name` VARCHAR(50) NOT NULL COMMENT '名称',
    description VARCHAR(200) COMMENT '描述',
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
);

-- c_route

CREATE TABLE c_route(
    id INT NOT NULL AUTO_INCREMENT KEY COMMENT 'id',
    app_id VARCHAR(10) NOT NULL COMMENT '应用ID',
    service_id INT NOT NULL COMMENT '下游接口配置ID',
    order_type_cd VARCHAR(4) NOT NULL COMMENT '订单类型，参考c_order_type表',
    invoke_limit_times INT COMMENT '接口调用一分钟调用次数',
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    status_cd VARCHAR(2) NOT NULL COMMENT '数据状态，详细参考c_status表，0在用，1失效，2 表示下线（当组件调用服务超过限制时自动下线）'
);



-- c_service

CREATE TABLE c_service(
    service_id INT NOT NULL AUTO_INCREMENT KEY COMMENT 'id',
    service_code VARCHAR(50) NOT NULL COMMENT '自定义，命名方式查询类query.+目标系统+.+业务名称 保存类 save.+目标系统+.+业务名称 修改类 modify.+目标系统+.+业务名称 删除类 remove.+目标系统+.+业务名称 例如：query.user.userinfo save.user.adduserinfo',
    invoke_model VARCHAR(1) NOT NULL COMMENT '1-同步方式 2-异步方式',
    business_type_cd VARCHAR(4) NOT NULL COMMENT '业务项类型，参考c_business_type表',
    `name` VARCHAR(50) NOT NULL COMMENT '服务名称',
    seq INT NOT NULL COMMENT '顺序 只有同步方式下根据seq从小到大调用接口',
    messageQueueName VARCHAR(50) COMMENT '消息队里名称 只有异步时有用',
    url VARCHAR(200) COMMENT '目标地址',
    method VARCHAR(50) COMMENT '方法 空 为http post LOCAL_SERVICE 为调用本地服务 其他为webservice方式调用',
    timeout INT NOT NULL DEFAULT 60 COMMENT '超时时间',
    retry_count INT NOT NULL DEFAULT 3 COMMENT '重试次数',
    provide_app_id VARCHAR(10) NOT NULL COMMENT '应用ID',
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    status_cd VARCHAR(2) NOT NULL default '0' COMMENT '数据状态，详细参考c_status表，0在用，1失效'
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
INSERT c_mapping(domain,`name`,`key`,`value`,remark) VALUES('DOMAIN.COMMON','不保存订单信息','NO_SAVE_ORDER','Q','不保存订单信息');
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

-- c_app
CREATE TABLE c_app(
    id INT NOT NULL AUTO_INCREMENT KEY COMMENT 'id',
    app_id VARCHAR(10) NOT NULL COMMENT '应用ID',
    `name` VARCHAR(50) NOT NULL COMMENT '名称 对应系统名称',
    security_code VARCHAR(64) NOT NULL COMMENT '签名码 sign签名时用',
    while_list_ip VARCHAR(200) COMMENT '白名单ip 多个之间用;隔开',
    black_list_ip VARCHAR(200) COMMENT '黑名单ip 多个之间用;隔开',
    remark VARCHAR(200) COMMENT '描述',
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    status_cd VARCHAR(2) NOT NULL COMMENT '数据状态，详细参考c_status表，0在用，1失效'
);

-- c_service_sql
CREATE TABLE c_service_sql(
    id INT NOT NULL AUTO_INCREMENT KEY COMMENT 'id',
    service_code VARCHAR(50) NOT NULL COMMENT '对应c_service表',
    `name` VARCHAR(50) NOT NULL COMMENT '名称',
    params VARCHAR(500) NOT NULL COMMENT '参数',
    query_model VARCHAR(1) NOT NULL COMMENT '查询方式 1、sql,2、存储过程',
    `sql` LONGTEXT COMMENT '执行sql',
    proc VARCHAR(200) COMMENT '存储过程名称',
    java_script LONGTEXT COMMENT '执行java脚本代码',
    template LONGTEXT COMMENT '输出模板',
    remark VARCHAR(200) COMMENT '描述',
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    status_cd VARCHAR(2) NOT NULL COMMENT '数据状态，详细参考c_status表，0在用，1失效'
);

-- 测试用

insert into c_order_type(order_type_cd,`name`,description) values('Q','查询单','查询单');

insert into c_app(app_id,`name`,security_code,remark,status_cd)
values('8000418001','内部测试应用','WEBURFPKIFJUHNCJUEIKMKJUJHULSMNCHDY89KMC','记得删除','0');

INSERT INTO c_app(app_id,`name`,security_code,remark,status_cd)
VALUES('8000418002','控制中心应用','WEBURFPKIFJUHNCJUEIKMKJUJHULSMNCHDY89KMC','控制中心应用','0');

insert into c_route(app_id,service_id,order_type_cd,status_cd) values(
'8000418001','1','Q','0'
);

insert into c_route(app_id,service_id,order_type_cd,status_cd) values(
'8000418001','3','Q','0'
);

insert into c_service(service_code,invoke_model,business_type_cd,`name`,seq,url,provide_app_id,status_cd)
values('query.user.userInfo','1','Q','用户信息查询',1,'http://...','8000418001','0');

insert into c_service(service_code,invoke_model,business_type_cd,`name`,seq,url,provide_app_id,status_cd)
values('query.order.orderInfo','1','Q','订单信息',1,'http://center-service/businessApi/query','8000418001','0');

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







