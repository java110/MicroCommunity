package com.java110.code;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.utils.util.StringUtil;

public class TableToJson {

    //show create table c_orders  用这个语句获取
    public static final String createTableSql = "CREATE TABLE `gov_car` (\n" +
            "  `car_id` varchar(30) NOT NULL PRIMARY key COMMENT '汽车ID',\n" +
            "  `pa_id` varchar(30) NOT NULL COMMENT '停车场ID',\n" +
            "  `car_num` varchar(12) NOT NULL COMMENT '车牌号',\n" +
            "  `car_brand` varchar(50) NOT NULL COMMENT '汽车品牌',\n" +
            "  `car_type` varchar(4) NOT NULL COMMENT '9901 家用小汽车，9902 客车，9903 货车',\n" +
            "  `car_color` varchar(12) NOT NULL COMMENT '颜色',\n" +
            "  `ca_id` varchar(30) NOT NULL COMMENT '区域ID',\n" +
            "  `gov_community_id` varchar(30) NOT NULL COMMENT '小区ID',\n" +
            "  `start_time` datetime NOT NULL COMMENT '起租时间',\n" +
            "  `end_time` datetime NOT NULL COMMENT '结租时间',\n" +
            "   gov_person_id varchar(30) not null comment '车主ID',\n" +
            "  `car_type_cd` varchar(4) NOT NULL DEFAULT '1001' COMMENT '1001 主车辆 1002 成员车辆',\n" +
            "  `remark` varchar(200) DEFAULT NULL COMMENT '备注',\n" +
            "  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',\n" +
            "  `status_cd` varchar(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考c_status表，0, 在用 1失效',\n" +
            "  `datasource_type` varchar(30) NOT NULL DEFAULT '999999' COMMENT '数据来源 999999 政务系统添加 777777 物业系统同步'\n" +
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
        param.put("shareColumn", "ca_id");
        param.put("shareName", "");
        param.put("shareParam", "caId");
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
