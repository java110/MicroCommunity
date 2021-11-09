package com.java110.code;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.utils.util.StringUtil;

public class TableToJson {

    //show create table c_orders  用这个语句获取
    public static final String createTableSql = "CREATE TABLE `machine` (\n" +
            "  `machine_id` varchar(30) NOT NULL COMMENT '设备ID',\n" +
            "  `b_id` varchar(30) NOT NULL COMMENT '业务Id',\n" +
            "  `machine_code` varchar(30) NOT NULL COMMENT '设备编码',\n" +
            "  `machine_version` varchar(30) NOT NULL COMMENT '设备版本',\n" +
            "  `machine_type_cd` varchar(30) NOT NULL COMMENT '设备类型 门禁9999 详情查看t_dict 表',\n" +
            "  `community_id` varchar(30) NOT NULL COMMENT '小区ID',\n" +
            "  `machine_name` varchar(200) NOT NULL COMMENT '设备名称',\n" +
            "  `auth_code` varchar(64) NOT NULL COMMENT '授权码',\n" +
            "  `machine_ip` varchar(64) DEFAULT NULL COMMENT '设备IP',\n" +
            "  `machine_mac` varchar(64) DEFAULT NULL COMMENT '设备mac',\n" +
            "  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',\n" +
            "  `status_cd` varchar(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考c_status表，S 保存，0, 在用 1失效',\n" +
            "  `location_type_cd` varchar(30) NOT NULL COMMENT '位置ID,请查看 community_location',\n" +
            "  `location_obj_id` varchar(30) NOT NULL DEFAULT '-1' COMMENT '对象ID，大门时小区ID，单元门 时单元ID 房屋时房屋ID',\n" +
            "  `state` varchar(12) NOT NULL DEFAULT '1000' COMMENT '设备状态，设备配置同步状态 1000 未同步 1100 同步中 1200 已同步',\n" +
            "  `direction` varchar(12) NOT NULL DEFAULT '3306' COMMENT '设备方向进出，3306 进 3307 出',\n" +
            "  `heartbeat_time` datetime NOT NULL COMMENT '设备心跳时间',\n" +
            "  `type_id` varchar(30) NOT NULL COMMENT '设备类型ID',\n" +
            "  UNIQUE KEY `machine_id` (`machine_id`) USING BTREE,\n" +
            "  KEY `idx_machine_id` (`machine_id`) USING BTREE,\n" +
            "  KEY `idx_machine_b_id` (`b_id`) USING BTREE,\n" +
            "  KEY `i_m_community_id` (`community_id`)\n" +
            ")";

    public static void main(String[] args) {
        String desc = "设备信息";
        String id = "machineId";
        String name = "machine";
        String shareName = "common"; //生成到那个服务下
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
