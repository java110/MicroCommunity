
-- 这里只做二级菜单
 -- 菜单组
create table m_menu_group(
    group_id int not null auto_increment key comment '权限线组ID',
    name varchar(10) not null comment '菜单名称',
    icon varchar(20) not null comment '菜单图片',
    seq INT NOT NULL  COMMENT '列顺序',
    create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    status_cd VARCHAR(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考c_status表，0在用，1失效'
);

-- 菜单
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

-- 权限组

create table p_privilege_group(

);
-- 权限表
create table p_privilege(

);