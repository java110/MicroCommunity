-- 组织部门
create table business_org(
    org_id VARCHAR(30) NOT NULL COMMENT '组织ID',
    b_id VARCHAR(30) NOT NULL COMMENT '业务Id',
    store_id varchar(30) not null comment '商户ID',
    org_name varchar(200) not null comment '组织名称',
    org_level varchar(10) not null comment '组织级别1 公司级 2 分公司级，3 部门级 查看t_dict表',
    parent_org_id varchar(30) not null comment '上级组织ID，一级时填写org_id',
    description varchar(200) not null comment '组织描述',
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    operate VARCHAR(3) NOT NULL COMMENT '数据状态，添加ADD，修改MOD 删除DEL'
);

-- 组织员工关系
create table business_org_staff_rel(
    rel_id varchar(30) not null comment '组织员工ID',
    b_id VARCHAR(30) NOT NULL COMMENT '业务Id',
    org_id varchar(30) not null comment '组织ID',
    staff_id varchar(30) not null comment '员工ID',
    store_id varchar(30) not null comment '商户ID，分片建',
    rel_cd varchar(30) not null comment '关系角色，10000 普通员工， 20000部门经理 查看t_dict表',
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    operate VARCHAR(3) NOT NULL COMMENT '数据状态，添加ADD，修改MOD 删除DEL'
);

-- 组织部门
create table business_org_community(
    org_community_id VARCHAR(30) NOT NULL COMMENT '组织ID',
    org_id VARCHAR(30) NOT NULL COMMENT '组织ID',
    community_id VARCHAR(30) NOT NULL COMMENT '小区ID',
    community_name VARCHAR(30) NOT NULL COMMENT '小区名称',
    b_id VARCHAR(30) NOT NULL COMMENT '业务Id',
    store_id varchar(30) not null comment '商户ID',
    org_name varchar(200) not null comment '组织名称',
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    operate VARCHAR(3) NOT NULL COMMENT '数据状态，添加ADD，修改MOD 删除DEL'
);

create table u_org(
    org_id VARCHAR(30) NOT NULL COMMENT '组织ID',
    b_id VARCHAR(30) NOT NULL COMMENT '业务Id',
    store_id varchar(30) not null comment '商户ID',
    org_name varchar(200) not null comment '组织名称',
    org_level varchar(10) not null comment '组织级别1 公司级 2 分公司级，3 部门级 查看t_dict表',
    parent_org_id varchar(30) not null comment '上级组织ID，一级时填写org_id',
    description varchar(200) not null comment '组织描述',
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    status_cd VARCHAR(2) NOT NULL default '0' COMMENT '数据状态，详细参考c_status表，0在用，1失效'
);

-- 组织员工关系
create table u_org_staff_rel(
    rel_id varchar(30) not null comment '组织员工ID',
    b_id VARCHAR(30) NOT NULL COMMENT '业务Id',
    org_id varchar(30) not null comment '组织ID',
    staff_id varchar(30) not null comment '员工ID',
    store_id varchar(30) not null comment '商户ID，分片建',
    rel_cd varchar(30) not null comment '关系角色，10000 普通员工， 20000部门经理 查看t_dict表',
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    status_cd VARCHAR(2) NOT NULL default '0' COMMENT '数据状态，详细参考c_status表，0在用，1失效'
);

-- 组织部门
create table u_org_community(
    org_community_id VARCHAR(30) NOT NULL COMMENT '组织ID',
    org_id VARCHAR(30) NOT NULL COMMENT '组织ID',
    community_id VARCHAR(30) NOT NULL COMMENT '小区ID',
    community_name VARCHAR(30) NOT NULL COMMENT '小区名称',
    b_id VARCHAR(30) NOT NULL COMMENT '业务Id',
    store_id varchar(30) not null comment '商户ID',
    org_name varchar(200) not null comment '组织名称',
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    status_cd VARCHAR(2) NOT NULL default '0' COMMENT '数据状态，详细参考c_status表，0在用，1失效'
);
