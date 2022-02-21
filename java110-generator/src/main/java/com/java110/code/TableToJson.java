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
    public static final String createTableSql = "CREATE TABLE `return_pay_fee` (\n" +
            "  `return_fee_id` varchar(30) NOT NULL COMMENT '退费id',\n" +
            "  `community_id` varchar(30) NOT NULL COMMENT '小区id',\n" +
            "  `b_id` varchar(30) NOT NULL COMMENT '业务id',\n" +
            "  `config_id` varchar(30) NOT NULL COMMENT '费用项id',\n" +
            "  `fee_id` varchar(30) NOT NULL COMMENT '费用id',\n" +
            "  `fee_type_cd` varchar(30) NOT NULL COMMENT '费用类型',\n" +
            "  `detail_id` varchar(30) NOT NULL COMMENT '缴费id',\n" +
            "  `cycles` decimal(9,2) DEFAULT NULL COMMENT '周期',\n" +
            "  `receivable_amount` decimal(10,2) DEFAULT NULL COMMENT '应收金额',\n" +
            "  `received_amount` decimal(10,2) DEFAULT NULL COMMENT '实收金额',\n" +
            "  `prime_rate` decimal(3,2) DEFAULT NULL COMMENT '打折率',\n" +
            "  `remark` varchar(200) DEFAULT NULL COMMENT '备注',\n" +
            "  `state` varchar(4) NOT NULL COMMENT '审核状态：1000待审核1001审核通过10002审核不通过',\n" +
            "  `status_cd` varchar(2) NOT NULL COMMENT '数据状态，详细参考c_status表，S 保存，0, 在用 1失效',\n" +
            "  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '申请退费时间',\n" +
            "  `pay_time` datetime NOT NULL COMMENT '缴费时间',\n" +
            "  `reason` varchar(200) NOT NULL COMMENT '退费原因',\n" +
            "  PRIMARY KEY (`return_fee_id`)\n" +
            ")";

    public static void main(String[] args) {
        String desc = "退费表";
        String id = "returnFeeId";
        String name = "returnPayFee";
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
