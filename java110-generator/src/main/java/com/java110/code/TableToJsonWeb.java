package com.java110.code;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.utils.util.StringUtil;

public class TableToJsonWeb {

    //show create table c_orders  用这个语句获取
    public static final String createTableSql = "CREATE TABLE `machine_type` (\n" +
            "  `type_id` varchar(30) NOT NULL COMMENT '设备类型ID',\n" +
            "  `machine_type_cd` varchar(30) NOT NULL COMMENT '设备大类 详情看t_dict',\n" +
            "  `machine_type_name` varchar(30) NOT NULL COMMENT '设备类型名称',\n" +
            "  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',\n" +
            "  `status_cd` varchar(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考c_status表，S 保存，0, 在用 1失效',\n" +
            "  KEY `idx_bm_machine_id` (`type_id`) USING BTREE\n" +
            ")";

    public static void main(String[] args) {
        String templateName = "设备类型"; //业务名称
        String templateCode = "machineType"; //表名大写
        String templateKey = "typeId"; //表主键
        String templateKeyName = "设备类型ID";//主键说明
        String searchCode = "typeId"; //分片字段
        String searchName = "设备类型ID"; //分片字段说明
        String directories = "property"; //前端生成到那个目录下

        // templateName 业务名称 业务编码名称生成后文件名 templateCode 主键 templateKey
        // 业务主键名称 templateKeyName=templateName+ID 主机驼峰 searchCode 主键名称 searchName
        // directories 放在前端那个目录下
        String newSql = createTableSql.substring(createTableSql.indexOf("(") + 1, createTableSql.lastIndexOf(")"));
        String tableName = createTableSql.substring(createTableSql.indexOf("TABLE") + 5, createTableSql.indexOf("("));
        tableName = tableName.replaceAll("`", "").trim();
        newSql = newSql.replaceAll("\n", "");
        String[] rowSqls = newSql.split("',");
        JSONObject param = new JSONObject();
        param.put("templateName", templateName);
        param.put("templateCode", templateCode);
        param.put("templateKey", templateKey);
        param.put("templateKeyName", templateKeyName);
        param.put("searchCode", searchCode);
        param.put("searchName", searchName);
        param.put("directories", directories);
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
            if (comment.contains("，")) {
                comment = comment.split("，")[0];
            }
            if (comment.contains(" ")) {
                comment = comment.split(" ")[0];
            }
            paramColumn.put("desc", comment);
            if (rowSql.toLowerCase().contains("not null")) {

                condition.put("name", comment);
                condition.put("inputType", "input");
                condition.put("code", StringUtil.lineToHump(key));
                condition.put("whereCondition", "equal");
                conditions.add(condition);
                paramColumn.put("desc", "必填，" + comment);
            }
            String limit = rowSql.substring(rowSql.indexOf("(") + 1, rowSql.indexOf(")"));
            if (limit.contains(",")) {
                limit = limit.split(",")[0];
            }

            paramColumn.put("code", StringUtil.lineToHump(key));
            paramColumn.put("cnCode", comment);
            paramColumn.put("required", true);
            paramColumn.put("hasDefaultValue", false);
            paramColumn.put("inputType", "input");
            paramColumn.put("limit", "maxLength");
            paramColumn.put("limitParam", limit);
            paramColumn.put("limitErrInfo", comment + "不能超过" + limit);
            paramColumn.put("show", true);
            paramColumns.add(paramColumn);
        }
        param.put("columns", paramColumns);
        param.put("conditions", conditions);
        System.out.println(param.toJSONString());

    }

}
