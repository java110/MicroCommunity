package com.java110.code;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.utils.util.StringUtil;

public class TableToJson {

    //show create table c_orders  用这个语句获取
    public static final String createTableSql = "CREATE TABLE `owner_car_open_user` (\n" +
            "  `open_user_id` varchar(30) NOT NULL COMMENT 'app用户ID',\n" +
            "  `open_id` varchar(128) NOT NULL COMMENT '微信或支付宝开放ID',\n" +
            "  `car_num` varchar(32) NOT NULL COMMENT '车牌号',\n" +
            "  `open_type` varchar(12) NOT NULL DEFAULT 'WECHAT' COMMENT '微信WECHAT 支付宝 ALI',\n" +
            "  `nickname` varchar(255) DEFAULT NULL COMMENT '昵称',\n" +
            "  `headimgurl` varchar(255) DEFAULT NULL COMMENT '微信头像',\n" +
            "  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',\n" +
            "  `status_cd` varchar(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考c_status表，0, 在用 1失效',\n" +
            "  PRIMARY KEY (`open_user_id`)\n" +
            ")";

    public static void main(String[] args) {
        String desc = "车辆微信关系";
        String id = "openUserId";
        String name = "ownerCarOpenUser";
        String shareName = "user"; //生成到那个服务下
        String shareColumn = "open_user_id";
        String shareParam = "openUserId";

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
