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
import com.java110.utils.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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


    public static final String FALLBACK_URL = "http://SERVICE_NAME/fallBack";

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
                httpEntity = new HttpEntity<String>(JSONObject.toJSONString(orderItemDto), header);
                restTemplate.exchange(FALLBACK_URL.replace(SERVICE_NAME, orderItemDto.getServiceName()), HttpMethod.POST, httpEntity, String.class);
            } catch (Exception e) {
                logger.error("回退事务失败", e);
                errorOrderItemDtos.add(orderItemDto);
            }
        }
        if (errorOrderItemDtos.size() > 0) {
            return new ResponseEntity<String>(JSONArray.toJSONString(errorOrderItemDtos), HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<String>("", HttpStatus.OK);
        }
    }
}
