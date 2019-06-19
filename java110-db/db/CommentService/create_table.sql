-- 评论
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
    comment_photo_type_cd VARCHAR(2) NOT NULL default 'S' COMMENT '评论照片类型,S 商品照片 M 商户ID',
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
    score_type_cd VARCHAR(2) NOT NULL COMMENT '打分类别，S 商品相符，U 卖家打分，T 物流打分',
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
