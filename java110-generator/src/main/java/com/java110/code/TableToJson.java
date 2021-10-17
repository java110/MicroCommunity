package com.java110.code;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.utils.util.StringUtil;

public class TableToJson {

    //show create table c_orders  用这个语句获取
    public static final String createTableSql = "CREATE TABLE `parking_box` (\n" +
            "  `box_id` varchar(30) NOT NULL COMMENT '岗亭ID',\n" +
            "  `box_name` varchar(64) NOT NULL COMMENT '岗亭名称',\n" +
            "  `community_id` varchar(30) NOT NULL COMMENT '小区ID',\n" +
            "  `temp_car_in` varchar(12) NOT NULL COMMENT '临时车是否进场 Y 进场 N 不进场',\n" +
            "  `fee` varchar(12) NOT NULL DEFAULT 'Y' COMMENT '岗亭是否 收费，主要考虑 岗亭嵌套问题 Y 收费 N 不收费',\n" +
            "  `blue_car_in` varchar(12) NOT NULL COMMENT '蓝牌车是否可以进场 Y 进场 N 不进场',\n" +
            "  `yelow_car_in` varchar(12) NOT NULL COMMENT '黄牌车是否可以进场 Y 进场 N 不进场',\n" +
            "  `remark` varchar(300) DEFAULT NULL COMMENT '备注',\n" +
            "  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',\n" +
            "  `status_cd` varchar(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考c_status表，S 保存，0, 在用 1失效'\n" +
            ") ";

    public static void main(String[] args) {
        String desc = "岗亭";
        String id = "boxId";
        String name = "parkingBox";
        String shareName = "community"; //生成到那个服务下
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
