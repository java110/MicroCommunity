package com.java110.code;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.utils.util.StringUtil;

public class TableToJsonWeb {

    //show create table c_orders  用这个语句获取
    public static final String createTableSql = "CREATE TABLE `building_room` (\n" +
            "  `room_id` varchar(30) NOT NULL COMMENT '房屋ID',\n" +
            "  `b_id` varchar(30) NOT NULL COMMENT '业务Id',\n" +
            "  `room_num` varchar(12) NOT NULL COMMENT '房屋编号',\n" +
            "  `unit_id` varchar(30) NOT NULL COMMENT '单元ID',\n" +
            "  `layer` int(11) NOT NULL COMMENT '层数',\n" +
            "  `section` int(11) DEFAULT NULL COMMENT '室',\n" +
            "  `apartment` varchar(20) NOT NULL COMMENT '户型',\n" +
            "  `built_up_area` decimal(6,2) NOT NULL COMMENT '建筑面积',\n" +
            "  `fee_coefficient` decimal(12,2) NOT NULL DEFAULT '1.00' COMMENT '算费系数',\n" +
            "  `user_id` varchar(30) NOT NULL COMMENT '用户ID',\n" +
            "  `remark` varchar(200) DEFAULT NULL COMMENT '备注',\n" +
            "  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',\n" +
            "  `status_cd` varchar(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考c_status表，S 保存，0, 在用 1失效',\n" +
            "  `state` varchar(4) NOT NULL COMMENT '房屋状态，如房屋出售等，请查看state 表',\n" +
            "  `community_id` varchar(30) DEFAULT NULL COMMENT '小区ID',\n" +
            "  `room_type` varchar(12) NOT NULL DEFAULT '1010301' COMMENT '房屋类型',\n" +
            "  `room_sub_type` varchar(12) NOT NULL DEFAULT '110' COMMENT '房屋类型 110 住宅房屋，119 办公室 120 宿舍',\n" +
            "  `room_area` decimal(6,2) NOT NULL COMMENT '室内面积',\n" +
            "  `room_rent` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '租金',\n" +
            "  UNIQUE KEY `room_id` (`room_id`) USING BTREE,\n" +
            "  UNIQUE KEY `idx_room_id` (`room_id`) USING BTREE,\n" +
            "  KEY `idx_room_b_id` (`b_id`) USING BTREE,\n" +
            "  KEY `i_br_unit_id` (`unit_id`)\n" +
            ")";

    public static void main(String[] args) {

        // templateName 业务名称 业务编码名称生成后文件名 templateCode 主键 templateKey
        // 业务主键名称 templateKeyName=templateName+ID 主机驼峰 searchCode 主键名称 searchName
        // directories 放在前端那个目录下
        String newSql = createTableSql.substring(createTableSql.indexOf("(") + 1, createTableSql.lastIndexOf(")"));
        String tableName = createTableSql.substring(createTableSql.indexOf("TABLE") + 5, createTableSql.indexOf("("));
        tableName = tableName.replaceAll("`", "").trim();
        newSql = newSql.replaceAll("\n", "");
        String[] rowSqls = newSql.split("',");
        JSONObject param = new JSONObject();
        param.put("templateName", "");
        param.put("templateCode", "");
        param.put("templateKey", "");
        param.put("templateKeyName", "");
        param.put("searchCode", "");
        param.put("searchName", "");
        param.put("directories", "");
        JSONObject paramColumn = null;
        JSONArray conditions = new JSONArray();
        JSONArray paramColumns = new JSONArray();
        JSONObject condition = null;
        String key = "";
        for (String rowSql : rowSqls) {
            condition = new JSONObject();
            paramColumn = new JSONObject();
            key = rowSql.trim();
            key = key.substring(0, key.indexOf(" "));
            key = key.replaceAll("`", "");
            if ("UNIQUE".equals(key)) {
                continue;
            }
            if ("KEY".equals(key)) {
                continue;
            }
            if ("b_id".equals(key)) {
                continue;
            }
            if ("create_time".equals(key)) {
                continue;
            }
            String comment = rowSql.contains("COMMENT") ? rowSql.substring(rowSql.indexOf("COMMENT '") + 9) : StringUtil.lineToHump(key);
            comment = comment.trim();
            if(comment.contains("，")){
                comment = comment.split("，")[0];
            }
            if(comment.contains(" ")){
                comment = comment.split(" ")[0];
            }
            paramColumn.put("desc", comment);
            if (rowSql.toLowerCase().contains("not null")) {

                condition.put("name", comment);
                condition.put("inputType", "input");
                condition.put("code", StringUtil.lineToHump(key));
                condition.put("whereCondition", "equal");
                conditions.add(condition);
                paramColumn.put("desc", "必填，"+comment);
            }
            String limit = rowSql.substring(rowSql.indexOf("(") + 1,rowSql.indexOf(")"));
            if(limit.contains(",")){
                limit = limit.split(",")[0];
            }

            paramColumn.put("code", StringUtil.lineToHump(key));
            paramColumn.put("cnCode", comment);
            paramColumn.put("required", true);
            paramColumn.put("hasDefaultValue", false);
            paramColumn.put("inputType", "input");
            paramColumn.put("limit", "maxLength");
            paramColumn.put("limitParam",limit );
            paramColumn.put("limitErrInfo", comment+"不能超过"+limit);
            paramColumn.put("show", true);
            paramColumns.add(paramColumn);
        }
        param.put("columns", paramColumns);
        param.put("conditions", conditions);
        System.out.println(param.toJSONString());

    }

}
