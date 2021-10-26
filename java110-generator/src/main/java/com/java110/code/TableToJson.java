package com.java110.code;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.utils.util.StringUtil;

public class TableToJson {

    //show create table c_orders  用这个语句获取
    public static final String createTableSql = "CREATE TABLE `fee_receipt_print` (\n" +
            "  `print_id` varchar(30) NOT NULL COMMENT '打印ID',\n" +
            "  `community_id` varchar(30) NOT NULL COMMENT '小区ID',\n" +
            "  `receipt_id` varchar(30) NOT NULL COMMENT '收据ID',\n" +
            "  `print_pre` varchar(12) NOT NULL COMMENT '打印前缀,一般为日期 例如：20211025',\n" +
            "  `num` BIGINT NOT NULL default 0 COMMENT '序号',\n" +
            "   print_num varchar(30) not null comment '打印编号',\n" +
            "  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',\n" +
            "  `status_cd` varchar(12) NOT NULL DEFAULT '0' COMMENT '数据状态',\n" +
            "  `remark` varchar(200) DEFAULT NULL COMMENT '备注'\n" +
            ")";

    public static void main(String[] args) {
        String desc = "收据打印编号";
        String id = "printId";
        String name = "feeReceiptPrint";
        String shareName = "fee"; //生成到那个服务下
        String shareColumn = "community_id";
        String shareParam = "communityId";
        //业务名称 desc 业务编码名称生成后类名 name 主键 id  需要放到那个服务 shareName
        String newSql = createTableSql.substring(createTableSql.indexOf("(") + 1, createTableSql.lastIndexOf(")"));
        String tableName = createTableSql.substring(createTableSql.indexOf("TABLE") + 5, createTableSql.indexOf("("));
        tableName = tableName.replaceAll("`", "").trim();
        newSql = newSql.replaceAll("\n", "");
        String[] rowSqls = newSql.split("',");
        JSONObject param = new JSONObject();
        param.put("autoMove", true);
        param.put("desc", desc);
        param.put("id", id);
        param.put("name", name);
        param.put("shareColumn", shareColumn);
        param.put("shareName", shareName);
        param.put("shareParam", shareParam);
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
            if ("KEY".equals(key)) {
                continue;
            }
            if ("b_id".equals(key)) {
                continue;
            }
            if ("create_time".equals(key)) {
                continue;
            }
            if (rowSql.toLowerCase().contains("not null")) {
                required.put("code", StringUtil.lineToHump(key));
                String comment = rowSql.contains("COMMENT") ? rowSql.substring(rowSql.indexOf("COMMENT '") + 9) : StringUtil.lineToHump(key);
                comment = comment.trim();
                if (comment.contains("，")) {
                    comment = comment.split("，")[0];
                }
                if (comment.contains(" ")) {
                    comment = comment.split(" ")[0];
                }

                required.put("msg", comment + "不能为空");
                requireds.add(required);
            }
            paramColumn.put(StringUtil.lineToHump(key), key);
        }
        param.put("param", paramColumn);
        param.put("required", requireds);
        System.out.println(param.toJSONString());
    }

}
