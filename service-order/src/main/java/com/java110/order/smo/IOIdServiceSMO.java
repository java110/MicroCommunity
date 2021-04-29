package com.java110.order.smo;

import com.java110.dto.order.OrderDto;
import com.java110.dto.order.OrderItemDto;
import org.springframework.http.ResponseEntity;

/**
 * @ClassName TServiceSMO
 * @Description TODO
 * @Author wuxw
 * @Date 2020/7/5 20:30
 * @Version 1.0
 * add by wuxw 2020/7/5
 **/
public interface IOIdServiceSMO {

    /**
     * 创建全局事务ID
     *
     * @return
     */
    ResponseEntity<String> createOId(OrderDto orderDto);

    /**
     * 创建全局事务ID
     *
     * @return
     */
    ResponseEntity<String> fallBackOId(OrderDto orderDto);

    /**
     * 创建 订单项
     * @param orderItemDto
     * @return
     */
    ResponseEntity<String> createOrderItem(OrderItemDto orderItemDto);


    /**
     * 完成事务
     *
     * @return
     */
    ResponseEntity<String> finishOrder(OrderDto orderDto);
}
