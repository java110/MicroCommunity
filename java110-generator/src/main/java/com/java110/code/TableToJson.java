package com.java110.code;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.utils.util.StringUtil;

public class TableToJson {

    //show create table c_orders  用这个语句获取
    public static final String createTableSql = "CREATE TABLE `workflow` (\n" +
            "  `flow_id` varchar(30) NOT NULL COMMENT '流程ID',\n" +
            "  `flow_name` varchar(200) NOT NULL COMMENT '流程名称',\n" +
            "  `describle` longtext COMMENT '描述',\n" +
            "  `skip_level` varchar(12) NOT NULL COMMENT '跳过',\n" +
            "  `community_id` varchar(30) NOT NULL COMMENT '小区ID，分片',\n" +
            "  `b_id` varchar(30) NOT NULL COMMENT '业务Id',\n" +
            "  `store_id` varchar(30) NOT NULL COMMENT '商户ID,用来做分区',\n" +
            "  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',\n" +
            "  `status_cd` varchar(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考t_dict表，0, 在用 1失效',\n" +
            "  `flow_type` varchar(12) NOT NULL COMMENT '流程类型，10001 投诉建议 20002 报修 30003 采购',\n" +
            "  `process_definition_key` varchar(64) DEFAULT NULL COMMENT '工作流部署ID'\n" +
            ")";

    public static void main(String[] args) {
        //业务名称 desc 业务编码名称生成后类名 name 主键 id  需要放到那个服务 shareName
        String newSql = createTableSql.substring(createTableSql.indexOf("(") + 1, createTableSql.lastIndexOf(")"));
        String tableName = createTableSql.substring(createTableSql.indexOf("TABLE") + 5, createTableSql.indexOf("("));
        tableName = tableName.replaceAll("`", "").trim();
        newSql = newSql.replaceAll("\n", "");
        String[] rowSqls = newSql.split("',");
        JSONObject param = new JSONObject();
        param.put("autoMove", true);
        param.put("desc", "");
        param.put("id", "");
        param.put("name", "");
        param.put("shareColumn", "community_id");
        param.put("shareName", "");
        param.put("shareParam", "communityId");
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
                if(comment.contains("，")){
                    comment = comment.split("，")[0];
                }
                if(comment.contains(" ")){
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
