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
    public static final String createTableSql = "CREATE TABLE `inspection_task` (\n" +
            "  `task_id` varchar(30) NOT NULL COMMENT '任务编码',\n" +
            "  `b_id` varchar(30) NOT NULL COMMENT '业务id',\n" +
            "  `community_id` varchar(30) NOT NULL COMMENT '小区ID',\n" +
            "  `inspection_plan_id` varchar(30) NOT NULL COMMENT '计划ID',\n" +
            "  `plan_ins_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '计划巡检时间',\n" +
            "  `act_ins_time` datetime DEFAULT NULL COMMENT '实际巡检时间',\n" +
            "  `plan_user_id` varchar(60) NOT NULL COMMENT '计划巡检人员id',\n" +
            "  `plan_user_name` varchar(60) DEFAULT NULL COMMENT '计划巡检人员姓名',\n" +
            "  `act_user_id` varchar(30) DEFAULT NULL COMMENT '实际巡检人员id',\n" +
            "  `act_user_name` varchar(60) DEFAULT NULL COMMENT '实际巡检人员姓名',\n" +
            "  `sign_type` varchar(200) NOT NULL COMMENT '实际巡检方式',\n" +
            "  `status_cd` varchar(2) NOT NULL DEFAULT '0' COMMENT '数据状态，详细参考c_status表，0在用 1失效',\n" +
            "  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,\n" +
            "  `state` varchar(12) NOT NULL DEFAULT '20200405' COMMENT '巡检状态',\n" +
            "  `ip_staff_id` varchar(30) NOT NULL COMMENT '巡检人ID',\n" +
            "  `original_plan_user_id` varchar(60) DEFAULT NULL COMMENT '原计划巡检人userId',\n" +
            "  `original_plan_user_name` varchar(60) DEFAULT NULL COMMENT '原计划巡检人名称',\n" +
            "  `transfer_desc` varchar(255) DEFAULT NULL COMMENT '转移说明',\n" +
            "  `task_type` varchar(255) DEFAULT NULL COMMENT '任务类型 1000日常巡检任务 2000流转巡检任务',\n" +
            "  `plan_end_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '计划巡检结束时间',\n" +
            ") ";

    public static void main(String[] args) {
        String desc = "巡检任务";
        String id = "taskId";
        String name = "inspectionTask";
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
