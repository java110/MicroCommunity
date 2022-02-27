package com.java110.code;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.utils.util.StringUtil;

public class TableToJsonWeb {

    //show create table c_orders  用这个语句获取
    public static final String createTableSql = "CREATE TABLE `m_menu_user` (\n" +
            "  `mu_id` varchar(30) NOT NULL COMMENT '编号',\n" +
            "  `m_id` varchar(30) NOT NULL COMMENT '菜单',\n" +
            "  `icon` varchar(128) NOT NULL COMMENT '图标',\n" +
            "  `seq` int(11) NOT NULL COMMENT '列顺序'\n" +
            ")";
    public static void main(String[] args) {
        String templateName = "常用菜单"; //业务名称
        String templateCode = "menuUser"; //表名大写
        String templateKey = "muId"; //表主键
        String templateKeyName = "编号";//主键说明
        String searchCode = "muId"; //分片字段
        String searchName = "编号"; //分片字段说明
        String directories = "frame"; //前端生成到那个目录下
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
            if ("status_cd".equals(key)) {
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
