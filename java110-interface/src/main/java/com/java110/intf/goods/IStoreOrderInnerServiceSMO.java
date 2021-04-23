package com.java110.intf.goods;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.storeOrder.StoreOrderDto;
import com.java110.po.storeOrder.StoreOrderPo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IStoreOrderInnerServiceSMO
 * @Description 购物车接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "goods-service", configuration = {FeignConfiguration.class})
@RequestMapping("/storeOrderApi")
public interface IStoreOrderInnerServiceSMO {


    @RequestMapping(value = "/saveStoreOrder", method = RequestMethod.POST)
    public int saveStoreOrder(@RequestBody StoreOrderPo storeOrderPo);

    @RequestMapping(value = "/updateStoreOrder", method = RequestMethod.POST)
    public int updateStoreOrder(@RequestBody  StoreOrderPo storeOrderPo);

    @RequestMapping(value = "/deleteStoreOrder", method = RequestMethod.POST)
    public int deleteStoreOrder(@RequestBody  StoreOrderPo storeOrderPo);

    /**
     * <p>查询小区楼信息</p>
     *
     *
     * @param storeOrderDto 数据对象分享
     * @return StoreOrderDto 对象数据
     */
    @RequestMapping(value = "/queryStoreOrders", method = RequestMethod.POST)
    List<StoreOrderDto> queryStoreOrders(@RequestBody StoreOrderDto storeOrderDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param storeOrderDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryStoreOrdersCount", method = RequestMethod.POST)
    int queryStoreOrdersCount(@RequestBody StoreOrderDto storeOrderDto);
}
