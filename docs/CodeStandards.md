## 变更历史
版本|变更内容|变更时间|变更人员
:-: | :-: | :-: | :-:
v0.01|初稿|2019-04-22|wuxw

## mysql数据库规范
* 建表名称 过程表 为 business + 模块 + 业务意义 如 business_sys_privilege  实例表为：模块+业务意义 如 sys_privilege
* 表和表字段需要写备注
* 每个业务必须包含 business表 和 instance表 如权限表 business_sys_privilege 和 sys_privilege
* 新建业务表必须包含 b_id 和 create_time 如：b_id VARCHAR(30) NOT NULL COMMENT '业务Id',  create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
* business表必须包含字段 operate 如： operate VARCHAR(4) NOT NULL COMMENT '数据状态，添加ADD，修改MOD 删除DEL'
* 新建instance表时必须包含字段 status_cd 如：status_cd VARCHAR(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考c_status表，S 保存，0, 在用 1失效'
* 由于框架问题审核状态信息不能和status_cd 合并为一个，故 加入 audit_status_cd varchar(4) NOT NULL DEFAULT '0000' comment '审核状态'



## java代码规范


## 前段代码规范

