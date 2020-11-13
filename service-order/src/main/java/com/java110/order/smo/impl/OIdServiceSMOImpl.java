package com.java110.order.smo.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.client.RestTemplate;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.order.OrderDto;
import com.java110.dto.order.OrderItemDto;
import com.java110.order.dao.ICenterServiceDAO;
import com.java110.order.smo.IOIdServiceSMO;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.DateUtil;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 事务处理服务类
 * Created by wuxw on 2018/4/13.
 */
@Service("oIdServiceSMOImpl")
public class OIdServiceSMOImpl implements IOIdServiceSMO {

    private static Logger logger = LoggerFactory.getLogger(OIdServiceSMOImpl.class);


    public static final String FALLBACK_URL = "http://SERVICE_NAME/businessApi/fallBack";

    public static final String SERVICE_NAME = "SERVICE_NAME";


    @Autowired
    private ICenterServiceDAO centerServiceDAOImpl;

    @Autowired
    private RestTemplate restTemplate;


    @Override
    public ResponseEntity<String> createOId(OrderDto orderDto) {

        orderDto.setoId(GenerateCodeFactory.getOId());
        if (StringUtil.isEmpty(orderDto.getAppId())) {
            throw new IllegalArgumentException("未包含appId");
        }

        if (StringUtil.isEmpty(orderDto.getExtTransactionId())) {
            throw new IllegalArgumentException("未包含交互日志");
        }

        if (StringUtil.isEmpty(orderDto.getRequestTime())) {
            throw new IllegalArgumentException("未包含请求时间");
        }

        if (StringUtil.isEmpty(orderDto.getUserId())) {
            throw new IllegalArgumentException("未包含用户ID");
        }

        //保存订单信息
        centerServiceDAOImpl.saveOrder(BeanConvertUtil.beanCovertMap(orderDto));
        return new ResponseEntity<String>(JSONObject.toJSONString(orderDto), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> fallBackOId(OrderDto orderDto) {
        if (StringUtil.isEmpty(orderDto.getoId())) {
            throw new IllegalArgumentException("未包含事务ID");
        }

        //判断OID是否存在
        orderDto = BeanConvertUtil.covertBean(centerServiceDAOImpl.getOrder(BeanConvertUtil.beanCovertMap(orderDto)), OrderDto.class);

        if (orderDto == null) {
            return new ResponseEntity<String>("没有需要回退事务", HttpStatus.NOT_FOUND);
        }

        //查询 事务项
        Map orderItem = new HashMap();
        orderItem.put("oId", orderDto.getoId());
        List<Map> orderItemMaps = centerServiceDAOImpl.getOrderItems(orderItem);
        if (orderItemMaps == null || orderItemMaps.size() < 1) {
            return new ResponseEntity<String>("没有需要回退事务", HttpStatus.NOT_FOUND);
        }
        List<OrderItemDto> orderItemDtos = BeanConvertUtil.covertBeanList(orderItemMaps, OrderItemDto.class);

        HttpEntity<String> httpEntity = null;
        HttpHeaders header = new HttpHeaders();
        List<OrderItemDto> errorOrderItemDtos = new ArrayList<>();
        for (OrderItemDto orderItemDto : orderItemDtos) {
            try {
                JSONArray params = generateParam(orderItemDto);
                httpEntity = new HttpEntity<String>(params.toJSONString(), header);
                restTemplate.exchange(FALLBACK_URL.replace(SERVICE_NAME, orderItemDto.getServiceName()), HttpMethod.POST, httpEntity, String.class);

                //标记为订单项失败
                Map info = new HashMap();
                info.put("finishTime", DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
                info.put("statusCd", "E");
                info.put("bId", orderItemDto.getbId());
                info.put("oId", orderDto.getoId());
                centerServiceDAOImpl.updateOrderItem(info);

                //删除 事务日志
                //centerServiceDAOImpl.deleteUnItemLog(info);
            } catch (Exception e) {
                logger.error("回退事务失败", e);
                errorOrderItemDtos.add(orderItemDto);
            }
        }


        //标记为订单失败
        Map info = new HashMap();
        info.put("finishTime", DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        info.put("statusCd", "E");
        info.put("oId", orderDto.getoId());
        centerServiceDAOImpl.updateOrder(info);

        if (errorOrderItemDtos.size() > 0) {
            return new ResponseEntity<String>(JSONArray.toJSONString(errorOrderItemDtos), HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<String>("", HttpStatus.OK);
        }
    }

    /**
     * 生成回滚sql
     *
     * @param orderItemDto
     * @return
     */
    private JSONArray generateParam(OrderItemDto orderItemDto) {
        JSONArray params = null;
        switch (orderItemDto.getAction()) {
            case "ADD":
                params = generateDeleteSql(orderItemDto);
                break;
            case "MOD":
                params = generateUpdateSql(orderItemDto);
                break;
            case "DEL":
                params = generateInsertSql(orderItemDto);
                break;
        }

        return params;
    }

    /**
     * 生成insert语句
     *
     * @param orderItemDto
     * @return
     */
    private JSONArray generateInsertSql(OrderItemDto orderItemDto) {
        JSONArray params = new JSONArray();
        JSONObject param = null;
        String sql = "";
        String logText = orderItemDto.getLogText();

        JSONObject logTextObj = JSONObject.parseObject(logText);
        JSONArray preValues = logTextObj.getJSONArray("preValue");
        for (int preValueIndex = 0; preValueIndex < preValues.size(); preValueIndex++) {
            sql = "insert into " + orderItemDto.getActionObj() + " ";
            param = new JSONObject();
            JSONObject keyValue = preValues.getJSONObject(preValueIndex);
            String keySql = "( ";
            String valueSql = " values (";
            for (String key : keyValue.keySet()) {
                keySql += (key + ",");
                valueSql += (keyValue.getString(key) + ",");
            }
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

    private JSONArray generateUpdateSql(OrderItemDto orderItemDto) {
        JSONArray params = new JSONArray();
        JSONObject param = null;
        String sql = "";
        String logText = orderItemDto.getLogText();

        JSONObject logTextObj = JSONObject.parseObject(logText);
        JSONArray preValues = logTextObj.getJSONArray("preValue");
        JSONArray afterValues = logTextObj.getJSONArray("afterValue");

        for (int preValueIndex = 0; preValueIndex < preValues.size(); preValueIndex++) {
            sql = "update " + orderItemDto.getActionObj() + " set ";
            param = new JSONObject();
            JSONObject keyValue = preValues.getJSONObject(preValueIndex);
            JSONObject afterKeyValue = afterValues.getJSONObject(preValueIndex);
            String whereSql = " where 1=1 ";
            for (String key : keyValue.keySet()) {
                sql += (key + "=" + keyValue.getString(key) + ",");
                if ("''".equals(afterKeyValue.getString(key))) { //条件中不拼写 为空的结果
                    continue;
                }
                whereSql += (" and " + key + " = " + afterKeyValue.getString(key));
            }
            if (sql.endsWith(",")) {
                sql = sql.substring(0, sql.length() - 1);
            }

            sql += whereSql;

            param.put("fallBackSql", sql);
            params.add(param);
        }

        return params;
    }

    /**
     * 生成删除语句
     *
     * @param orderItemDto
     */
    private JSONArray generateDeleteSql(OrderItemDto orderItemDto) {
        JSONArray params = new JSONArray();
        JSONObject param = null;
        String sql = "";
        String logText = orderItemDto.getLogText();

        JSONObject logTextObj = JSONObject.parseObject(logText);
        JSONArray afterValues = logTextObj.getJSONArray("afterValue");
        for (int preValueIndex = 0; preValueIndex < afterValues.size(); preValueIndex++) {
            sql = "delete from " + orderItemDto.getActionObj() + " where 1=1 ";
            param = new JSONObject();
            JSONObject keyValue = afterValues.getJSONObject(preValueIndex);
            for (String key : keyValue.keySet()) {
                if (!StringUtil.isEmpty(keyValue.getString(key))) {
                    sql += (" and " + key + "=" + keyValue.getString(key));
                }
            }
            param.put("fallBackSql", sql);
            params.add(param);
        }

        return params;
    }

    @Override
    public ResponseEntity<String> createOrderItem(OrderItemDto orderItemDto) {

        if (StringUtil.isEmpty(orderItemDto.getoId())) {
            return new ResponseEntity<String>("请求报文中未包含事务ID", HttpStatus.NOT_FOUND);
        }

        if (StringUtil.isEmpty(orderItemDto.getAction())) {
            return new ResponseEntity<String>("请求报文中未包含动作", HttpStatus.NOT_FOUND);
        }

        if (StringUtil.isEmpty(orderItemDto.getActionObj())) {
            return new ResponseEntity<String>("请求报文中未包含动作对象", HttpStatus.NOT_FOUND);
        }

        if (StringUtil.isEmpty(orderItemDto.getServiceName())) {
            return new ResponseEntity<String>("请求报文中未包含服务", HttpStatus.NOT_FOUND);
        }
        if (StringUtil.isEmpty(orderItemDto.getLogText())) {
            return new ResponseEntity<String>("请求报文中未包含回滚日志", HttpStatus.NOT_FOUND);
        }
        if (StringUtil.isEmpty(orderItemDto.getbId()) || orderItemDto.getbId().startsWith("-")) {
            orderItemDto.setbId(GenerateCodeFactory.getBId());
        }
        //判断OID是否存在
        OrderDto orderDto = BeanConvertUtil.covertBean(centerServiceDAOImpl.getOrder(BeanConvertUtil.beanCovertMap(orderItemDto)), OrderDto.class);

        if (orderDto == null || "E".equals(orderDto.getStatusCd())) {
            return new ResponseEntity<String>("当前没有事务或者事务已经回滚", HttpStatus.NOT_FOUND);
        }
        centerServiceDAOImpl.saveOrderItem(BeanConvertUtil.beanCovertMap(orderItemDto));

        return ResultVo.createResponseEntity(ResultVo.CODE_OK, ResultVo.MSG_OK);
    }

    /**
     * 完成事务
     *
     * @param orderDto
     * @return
     */
    @Override
    public ResponseEntity<String> finishOrder(OrderDto orderDto) {
        if (StringUtil.isEmpty(orderDto.getoId())) {
            return new ResponseEntity<String>("请求报文中未包含事务ID", HttpStatus.NOT_FOUND);
        }

        //完成订单项
        Map info = new HashMap();
        info.put("finishTime", DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        info.put("statusCd", "C");
        info.put("oId", orderDto.getoId());
        centerServiceDAOImpl.updateOrderItem(info);

        //删除 事务日志
        //centerServiceDAOImpl.deleteUnItemLog(info);

        //完成订单
        info = new HashMap();
        info.put("finishTime", DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        info.put("statusCd", "C");
        info.put("oId", orderDto.getoId());
        centerServiceDAOImpl.updateOrder(info);

        return ResultVo.createResponseEntity(ResultVo.CODE_OK, ResultVo.MSG_OK);
    }
}
