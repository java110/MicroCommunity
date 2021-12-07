package com.java110.code;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.client.RestTemplate;
import com.java110.db.dao.IQueryServiceDAO;
import com.java110.utils.factory.ApplicationContextFactory;
import com.java110.utils.util.StringUtil;
import org.springframework.beans.BeansException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.http.converter.StringHttpMessageConverter;

import java.nio.charset.Charset;

public class TableToJson {

    //show create table c_orders  用这个语句获取
    public static final String createTableSql = "CREATE TABLE `r_repair_user` (\n" +
            "  `ru_id` varchar(30) NOT NULL COMMENT '报修派单ID',\n" +
            "  `repair_id` varchar(30) NOT NULL COMMENT '报修ID',\n" +
            "  `b_id` varchar(30) NOT NULL COMMENT '业务Id',\n" +
            "  `community_id` varchar(30) NOT NULL COMMENT '小区ID',\n" +
            "  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',\n" +
            "  `state` varchar(8) NOT NULL COMMENT '员工处理状态，请查看t_dict 表',\n" +
            "  `context` longtext COMMENT '报修内容',\n" +
            "  `status_cd` varchar(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考c_status表，S 保存，0, 在用 1失效',\n" +
            "  `staff_id` varchar(30) NOT NULL COMMENT '当前处理员工',\n" +
            "  `staff_name` varchar(64) NOT NULL COMMENT '当前处理员工名称',\n" +
            "  `pre_staff_id` varchar(30) NOT NULL COMMENT '上一节点处理员工',\n" +
            "  `pre_staff_name` varchar(64) NOT NULL COMMENT '上一节点处理员工名称',\n" +
            "  `start_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '开始时间',\n" +
            "  `end_time` timestamp NULL DEFAULT NULL COMMENT '结束时间',\n" +
            "  `repair_event` varchar(12) NOT NULL COMMENT '事件，startUser 开始用户 auditUser 审核处理员工',\n" +
            "  `pre_ru_id` varchar(30) NOT NULL COMMENT '上一节点处理ID'\n" +
            ")";

    public static void main(String[] args) {
        String desc = "费用明细";
        String id = "ruId";
        String name = "repairUserNew";
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
            if ("PRIMARY".equals(key)) {
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
