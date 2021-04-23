package com.java110.order.api;

import com.alibaba.fastjson.JSONObject;
import com.java110.dto.order.OrderDto;
import com.java110.dto.order.OrderItemDto;
import com.java110.order.smo.IOIdServiceSMO;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName TApi
 * @Description TODO 事务处理类
 * @Author wuxw
 * @Date 2020/7/5 20:27
 * @Version 1.0
 * add by wuxw 2020/7/5
 **/
@RestController
@RequestMapping(path = "/order/oIdApi")
public class OIdApi {
    @Autowired
    private IOIdServiceSMO tServiceSMOImpl;

    /**
     * 创建事务ID
     *
     * @return
     */
    @RequestMapping(path = "/createOId", method = RequestMethod.POST)
    public ResponseEntity<String> createOId(@RequestBody String order) {
        JSONObject orderObj = JSONObject.parseObject(order);
        OrderDto orderDto = BeanConvertUtil.covertBean(orderObj, OrderDto.class);
        return tServiceSMOImpl.createOId(orderDto);
    }

    /**
     * 回退事务
     *
     * @param order
     * @return
     */
    @RequestMapping(path = "/fallBackOId", method = RequestMethod.POST)
    public ResponseEntity<String> fallBackOId(@RequestBody String order) {
        JSONObject orderObj = JSONObject.parseObject(order);
        OrderDto orderDto = BeanConvertUtil.covertBean(orderObj, OrderDto.class);
        return tServiceSMOImpl.fallBackOId(orderDto);
    }

    /**
     * 上报Item
     *
     * @param orderItem
     * @return
     */
    @RequestMapping(path = "/createOrderItem", method = RequestMethod.POST)
    public ResponseEntity<String> createOrderItem(@RequestBody String orderItem) {
        JSONObject orderItemObj = JSONObject.parseObject(orderItem);
        OrderItemDto orderItemDto = BeanConvertUtil.covertBean(orderItemObj, OrderItemDto.class);
        return tServiceSMOImpl.createOrderItem(orderItemDto);
    }

    /**
     * 完成事务
     *
     * @return
     */
    @RequestMapping(path = "/finishOId", method = RequestMethod.POST)
    public ResponseEntity<String> finishOId(@RequestBody String order) {
        JSONObject orderObj = JSONObject.parseObject(order);
        OrderDto orderDto = BeanConvertUtil.covertBean(orderObj, OrderDto.class);
        return tServiceSMOImpl.finishOrder(orderDto);
    }
}
