package com.java110.code;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.utils.util.StringUtil;

public class TableToJson {

    //show create table c_orders  用这个语句获取
    public static final String createTableSql = "CREATE TABLE `owner_app_user` (\n" +
            "  `app_user_id` varchar(30) NOT NULL COMMENT 'app用户ID',\n" +
            "  `member_id` varchar(30) NOT NULL COMMENT '业主成员ID',\n" +
            "  `b_id` varchar(30) NOT NULL COMMENT '业务Id',\n" +
            "  `community_id` varchar(30) NOT NULL COMMENT '小区Id',\n" +
            "  `community_name` varchar(100) NOT NULL COMMENT '小区名称',\n" +
            "  `app_user_name` varchar(100) NOT NULL COMMENT 'app用户名称',\n" +
            "  `id_card` varchar(20) NOT NULL COMMENT '身份证号',\n" +
            "  `link` varchar(11) NOT NULL COMMENT '联系人手机号',\n" +
            "  `open_id` varchar(30) NOT NULL COMMENT 'app 开放ID',\n" +
            "  `app_type_cd` varchar(12) NOT NULL COMMENT '应用类型 10010 微信小程序',\n" +
            "  `state` varchar(12) NOT NULL COMMENT '状态类型，10000 审核中，12000 审核成功，13000 审核失败',\n" +
            "  `remark` varchar(200) DEFAULT NULL COMMENT '备注',\n" +
            "  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',\n" +
            "  `status_cd` varchar(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考c_status表，0, 在用 1失效',\n" +
            "  `user_id` varchar(30) DEFAULT NULL COMMENT '用户ID',\n" +
            "  `app_type` varchar(12) NOT NULL DEFAULT 'APP' COMMENT '绑定业主手机端类型',\n" +
            "  `nickname` varchar(255) DEFAULT NULL COMMENT '昵称',\n" +
            "  `headimgurl` varchar(255) DEFAULT NULL COMMENT '微信头像',\n" +
            "  UNIQUE KEY `app_user_id` (`app_user_id`) USING BTREE,\n" +
            "  UNIQUE KEY `idx_owner_app_user_id` (`app_user_id`) USING BTREE,\n" +
            "  KEY `idx_owner_app_user_b_id` (`b_id`) USING BTREE\n" +
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
        param.put("desc", "业主用户小区关系");
        param.put("id", "appUserId");
        param.put("name", "ownerAppUser");
        param.put("shareColumn", "community_id");
        param.put("shareName", "user");
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
