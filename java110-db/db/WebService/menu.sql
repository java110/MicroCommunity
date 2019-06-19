
-- 这里只做二级菜单
 -- 菜单组
create table m_menu_group(
    g_id varchar(12) not null comment '菜单组ID',
    name varchar(10) not null comment '菜单组名称',
    icon varchar(20) not null comment '菜单图片',
    label varchar(20) not null comment '菜单标签',
    seq INT NOT NULL  COMMENT '列顺序',
    description VARCHAR(200) COMMENT '菜单描述',
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    status_cd VARCHAR(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考c_status表，0在用，1失效',
    UNIQUE KEY (g_id)
);

-- 菜单
create table m_menu(
    m_id varchar(12) not null comment '菜单ID',
    name varchar(10) not null comment '菜单名称',
    g_id varchar(12) not null comment '菜单组ID',
    url VARCHAR(200)  not null COMMENT '打开地址',
    seq INT NOT NULL  COMMENT '列顺序',
    p_id varchar(12) not null comment '权限ID',
    description VARCHAR(200) COMMENT '菜单描述',
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    status_cd VARCHAR(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考c_status表，0在用，1失效',
    UNIQUE KEY (m_id)
);

-- 权限组

create table p_privilege_group(
    pg_id varchar(12) not null comment '权限组ID',
    name varchar(10) not null comment '权限组名称',
    description VARCHAR(200) COMMENT '权限组描述',
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    store_id varchar(30) not null comment '商户ID',
    status_cd VARCHAR(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考c_status表，0在用，1失效',
    UNIQUE KEY (pg_id)

);
-- 权限关系表
create table p_privilege_rel(
    rel_id INT NOT NULL AUTO_INCREMENT KEY comment '权限关系ID',
    p_id varchar(12) not null comment '权限ID',
    pg_id varchar(12) comment '权限组ID',
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    status_cd VARCHAR(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考c_status表，0在用，1失效',
    UNIQUE KEY (rel_id)
);

-- 权限表
create table p_privilege(
    p_id varchar(12) not null comment '权限ID',
    name varchar(10) not null comment '权限名称',
    domain varchar(12) not null comment '权限域 商户详见store_type store_type_cd',
    description VARCHAR(200) COMMENT '权限描述',
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    status_cd VARCHAR(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考c_status表，0在用，1失效',
    UNIQUE KEY (p_id)
);

-- 权限用户表
create table p_privilege_user(
    pu_id INT NOT NULL AUTO_INCREMENT KEY comment '权限用户ID',
    p_id varchar(12) not null comment '权限ID 或权限组ID',
    privilege_flag varchar(4) not null DEFAULT '0' comment '权限标志 是 1是权限组 0是权限',
    user_id varchar(30) not null comment '用户ID',
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    status_cd VARCHAR(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考c_status表，0在用，1失效',
    UNIQUE KEY (pu_id)
);