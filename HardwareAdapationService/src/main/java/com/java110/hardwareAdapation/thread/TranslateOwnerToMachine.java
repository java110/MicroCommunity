package com.java110.hardwareAdapation.thread;

import com.java110.core.smo.order.IOrderInnerServiceSMO;
import com.java110.core.smo.owner.IOwnerInnerServiceSMO;
import com.java110.core.smo.user.IUserInnerServiceSMO;
import com.java110.dto.OwnerDto;
import com.java110.dto.order.OrderDto;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.factory.ApplicationContextFactory;
import com.java110.utils.util.CacheUtil;
import com.java110.utils.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;

import java.util.List;

/**
 * 从订单中同步业主信息至设备中间表
 * add by wuxw 2019-11-14
 */
public class TranslateOwnerToMachine implements Runnable {
    Logger logger = LoggerFactory.getLogger(TranslateOwnerToMachine.class);
    public static final long DEFAULT_WAIT_SECOND = 5000 * 6; // 默认30秒执行一次
    public static boolean TRANSLATE_STATE = false;

    private IOrderInnerServiceSMO orderInnerServiceSMOImpl;
    private IOwnerInnerServiceSMO ownerInnerServiceSMOImpl;

    public TranslateOwnerToMachine() {
        orderInnerServiceSMOImpl = ApplicationContextFactory.getBean("orderInnerServiceSMOImpl", IOrderInnerServiceSMO.class);
        ownerInnerServiceSMOImpl = ApplicationContextFactory.getBean("ownerInnerServiceSMOImpl", IOwnerInnerServiceSMO.class);

    }

    @Override
    public void run() {
        long waitTime = DEFAULT_WAIT_SECOND;
        while (TRANSLATE_STATE) {
            try {
                executeTask();
                waitTime = StringUtil.isNumber(MappingCache.getValue("DEFAULT_WAIT_SECOND")) ?
                        Long.parseLong(MappingCache.getValue("DEFAULT_WAIT_SECOND")) : DEFAULT_WAIT_SECOND;
                Thread.sleep(waitTime);
            } catch (Throwable e) {
                logger.error("执行订单中同步业主信息至设备中失败", e);
            }
        }
    }

    /**
     * 执行任务
     */
    private void executeTask() {
        OwnerDto ownerDto = null;
        //查询订单信息
        OrderDto orderDto = new OrderDto();
        List<OrderDto> orderDtos = orderInnerServiceSMOImpl.queryOwenrOrders(orderDto);
        for (OrderDto tmpOrderDto : orderDtos) {
            try {
                //根据bId 查询业主信息
                ownerDto = new OwnerDto();
                ownerDto.setbId(tmpOrderDto.getbId());
                List<OwnerDto> ownerDtos = ownerInnerServiceSMOImpl.queryOwners(ownerDto);
                dealData(tmpOrderDto, ownerDtos);
            } catch (Exception e) {
                logger.error("执行订单任务失败", e);
            }
        }
    }

    private void dealData(OrderDto tmpOrderDto, List<OwnerDto> ownerDtos) {

    }

}
