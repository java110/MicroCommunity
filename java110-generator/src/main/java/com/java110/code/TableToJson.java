package com.java110.code;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.utils.util.StringUtil;

public class TableToJson {

    //show create table c_orders  用这个语句获取
    public static final String createTableSql = "CREATE TABLE `c_orders` (\n" +
            "  `o_id` varchar(30) NOT NULL COMMENT '订单ID',\n" +
            "  `app_id` varchar(30) NOT NULL COMMENT '应用ID',\n" +
            "  `ext_transaction_id` varchar(36) DEFAULT NULL,\n" +
            "  `user_id` varchar(30) NOT NULL COMMENT '用户ID',\n" +
            "  `request_time` varchar(16) NOT NULL COMMENT '外部系统请求时间',\n" +
            "  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',\n" +
            "  `order_type_cd` varchar(4) NOT NULL COMMENT '订单类型，参考c_order_type表',\n" +
            "  `finish_time` date DEFAULT NULL COMMENT '订单完成时间',\n" +
            "  `remark` varchar(200) DEFAULT NULL COMMENT '备注',\n" +
            "  `status_cd` varchar(2) NOT NULL COMMENT '数据状态，详细参考c_status表',\n" +
            "  UNIQUE KEY `o_id` (`o_id`) USING BTREE,\n" +
            "  UNIQUE KEY `o_id_2` (`o_id`) USING BTREE\n" +
            ")";

    public static void main(String[] args) {

        String newSql = createTableSql.substring(createTableSql.indexOf("(") + 1, createTableSql.lastIndexOf(")"));
        String tableName = createTableSql.substring(createTableSql.indexOf("TABLE") + 5, createTableSql.indexOf("("));
        tableName = tableName.replaceAll("`", "").trim();
        newSql = newSql.replaceAll("\n", "");
        String[] rowSqls = newSql.split(",");
        JSONObject param = new JSONObject();
        param.put("autoMove", true);
        param.put("desc", "");
        param.put("id", "");
        param.put("name", "");
        param.put("shareColumn", "");
        param.put("shareName", "");
        param.put("shareParam", "");
        param.put("tableName", tableName);
        JSONObject paramColumn = new JSONObject();
        JSONArray requireds = new JSONArray();
        JSONObject required = null;
        String key = "";
        for (String rowSql : rowSqls) {
            required = new JSONObject();
            key = rowSql.trim();
            key = key.substring(0, key.indexOf(" "));
            key = key.replaceAll("`", "");
            if ("UNIQUE".equals(key)) {
                continue;
            }
            if ("create_time".equals(key)) {
                continue;
            }
            if(rowSql.toLowerCase().contains("not null")){
                required.put("code",StringUtil.lineToHump(key));
                String comment = rowSql.substring(rowSql.indexOf("COMMENT '")+9,rowSql.lastIndexOf("'"));
                required.put("msg",comment+"不能为空");
                requireds.add(required);
            }
            paramColumn.put(StringUtil.lineToHump(key), key);
        }
        param.put("param", paramColumn);
        param.put("required", requireds);
        System.out.println(param.toJSONString());

    }

}
