package com.java110.order.smo.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.client.RestTemplate;
import com.java110.dto.businessTableHis.BusinessTableHisDto;
import com.java110.dto.order.OrderItemDto;
import com.java110.order.smo.IAsynNotifySubService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class AsynNotifySubServiceImpl implements IAsynNotifySubService {
    private static Logger logger = LoggerFactory.getLogger(AsynNotifySubServiceImpl.class);

    @Autowired
    private RestTemplate restTemplate;

    public static final String FALLBACK_URL = "http://SERVICE_NAME/businessApi/fallBack";

    public static final String SERVICE_NAME = "SERVICE_NAME";

    @Override
    @Async
    public void notifySubService(OrderItemDto orderItemDto, BusinessTableHisDto businessTableHisDto) {
        HttpEntity<String> httpEntity = null;
        HttpHeaders header = new HttpHeaders();
        try {
            JSONArray params = generateBusinessParam(orderItemDto, businessTableHisDto);
            httpEntity = new HttpEntity<String>(params.toJSONString(), header);
            //通过fallBack 的方式生成Business
            restTemplate.exchange(FALLBACK_URL.replace(SERVICE_NAME, orderItemDto.getServiceName()), HttpMethod.POST, httpEntity, String.class);
        } catch (Exception e) {
            logger.error("生成business失败", e);

        }
    }

    /**
     * 生成回滚sql
     *
     * @param orderItemDto
     * @return
     */
    private JSONArray generateBusinessParam(OrderItemDto orderItemDto, BusinessTableHisDto businessTableHisDto) {
        JSONArray params = null;
        switch (orderItemDto.getAction()) {
            case "ADD":
                params = generateBusinessInsertInsertSql(orderItemDto, businessTableHisDto);
                break;
            case "MOD":
                params = generateBusinessDelInsertSql(orderItemDto, businessTableHisDto);
                JSONArray paramAdds = generateBusinessInsertInsertSql(orderItemDto, businessTableHisDto);
                params.add(paramAdds);
                break;
            case "DEL":
                params = generateBusinessDelInsertSql(orderItemDto, businessTableHisDto);
                break;
        }

        return params;
    }


    /**
     * 生成添加的insert语句
     *
     * @param orderItemDto
     * @return
     */
    private JSONArray generateBusinessInsertInsertSql(OrderItemDto orderItemDto, BusinessTableHisDto businessTableHisDto) {
        JSONArray params = new JSONArray();
        JSONObject param = null;
        String sql = "";
        String logText = orderItemDto.getLogText();

        JSONObject logTextObj = JSONObject.parseObject(logText);
        JSONArray afterValues = logTextObj.getJSONArray("afterValue");
        for (int afterValueIndex = 0; afterValueIndex < afterValues.size(); afterValueIndex++) {
            sql = "insert into " + businessTableHisDto.getActionObjHis() + " ";
            param = new JSONObject();
            JSONObject keyValue = afterValues.getJSONObject(afterValueIndex);
            if (keyValue.isEmpty()) {
                continue;
            }
            String keySql = "( ";
            String valueSql = " values (";
            for (String key : keyValue.keySet()) {
                if ("status_cd".equals(key) || "create_time".equals(key)) {
                    continue;
                }
                keySql += (key + ",");
                valueSql += (keyValue.getString(key) + ",");
            }
            keySql += "operate,b_id";
            valueSql += "'ADD','" + orderItemDto.getbId() + "'";
            if (keySql.endsWith(",")) {
                keySql = keySql.substring(0, keySql.length() - 1);
            }
            if (valueSql.endsWith(",")) {
                valueSql = valueSql.substring(0, valueSql.length() - 1);
            }
            sql = sql + keySql + ") " + valueSql + ") ";
            param.put("fallBackSql", sql);
            params.add(param);
        }

        return params;
    }


    /**
     * 生成删除的insert语句
     *
     * @param orderItemDto
     * @return
     */
    private JSONArray generateBusinessDelInsertSql(OrderItemDto orderItemDto, BusinessTableHisDto businessTableHisDto) {
        JSONArray params = new JSONArray();
        JSONObject param = null;
        String sql = "";
        String logText = orderItemDto.getLogText();

        JSONObject logTextObj = JSONObject.parseObject(logText);
        JSONArray preValues = logTextObj.getJSONArray("preValue");
        for (int preValueIndex = 0; preValueIndex < preValues.size(); preValueIndex++) {
            sql = "insert into " + businessTableHisDto.getActionObjHis() + " ";
            param = new JSONObject();
            JSONObject keyValue = preValues.getJSONObject(preValueIndex);
            if (keyValue.isEmpty()) {
                continue;
            }
            String keySql = "( ";
            String valueSql = " values (";
            for (String key : keyValue.keySet()) {
                if ("status_cd".equals(key) || "create_time".equals(key)) {
                    continue;
                }
                keySql += (key + ",");
                valueSql += (keyValue.getString(key) + ",");
            }
            keySql += "operate,b_id";
            valueSql += "'DEL','" + orderItemDto.getbId() + "'";
            if (keySql.endsWith(",")) {
                keySql = keySql.substring(0, keySql.length() - 1);
            }
            if (valueSql.endsWith(",")) {
                valueSql = valueSql.substring(0, valueSql.length() - 1);
            }
            sql = sql + keySql + ") " + valueSql + ") ";
            param.put("fallBackSql", sql);
            params.add(param);
        }

        return params;
    }

}
