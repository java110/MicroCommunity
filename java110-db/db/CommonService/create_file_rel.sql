create table file_rel(
  `file_rel_id` varchar(30) NOT NULL COMMENT '文件关系ID，主键',
  `b_id` varchar(30) NOT NULL COMMENT '业务Id',
  `rel_type_cd` varchar(30) NOT NULL COMMENT '文件类型,用来做分区,业主照片，商户照片，具体查看t_dict表',
  `save_way` varchar(12) NOT NULL COMMENT '存放方式，ftp table,fastdfs 具体查看t_dict表',
  `obj_id` varchar(30) NOT NULL COMMENT '对象ID，及说明这个文件归宿于谁，业主则填写业主ID',
  `file_real_name` varchar(200) NOT NULL COMMENT '文件真实名称',
  `file_save_name` varchar(200) NOT NULL COMMENT '文件存储名称',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `status_cd` varchar(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考t_dict表，0, 在用 1失效',
  KEY `idx_file_rel_id` (`file_rel_id`)
);

CREATE TABLE `business_file_rel` (
   `file_rel_id` varchar(30) NOT NULL COMMENT '文件关系ID，主键',
  `b_id` varchar(30) NOT NULL COMMENT '业务Id',
  `rel_type_cd` varchar(30) NOT NULL COMMENT '文件类型,用来做分区,业主照片，商户照片，具体查看t_dict表',
  `save_way` varchar(12) NOT NULL COMMENT '存放方式，ftp table,fastdfs 具体查看t_dict表',
  `obj_id` varchar(30) NOT NULL COMMENT '对象ID，及说明这个文件归宿于谁，业主则填写业主ID',
  `file_real_name` varchar(200) NOT NULL COMMENT '文件真实名称',
  `file_save_name` varchar(200) NOT NULL COMMENT '文件存储名称',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `operate` varchar(4) NOT NULL COMMENT '数据状态，添加ADD，修改MOD 删除DEL'
);