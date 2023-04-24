package com.java110.intf.goods;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.storeOrder.StoreOrderCartDto;
import com.java110.po.storeOrderCart.StoreOrderCartPo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IStoreOrderCartInnerServiceSMO
 * @Description 订单购物车接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "goods-service", configuration = {FeignConfiguration.class})
@RequestMapping("/storeOrderCartApi")
public interface IStoreOrderCartInnerServiceSMO {


    @RequestMapping(value = "/saveStoreOrderCart", method = RequestMethod.POST)
    public int saveStoreOrderCart(@RequestBody StoreOrderCartPo storeOrderCartPo);

    @RequestMapping(value = "/updateStoreOrderCart", method = RequestMethod.POST)
    public int updateStoreOrderCart(@RequestBody StoreOrderCartPo storeOrderCartPo);

    @RequestMapping(value = "/deleteStoreOrderCart", method = RequestMethod.POST)
    public int deleteStoreOrderCart(@RequestBody StoreOrderCartPo storeOrderCartPo);

    /**
     * <p>查询小区楼信息</p>
     *
     * @param storeOrderCartDto 数据对象分享
     * @return StoreOrderCartDto 对象数据
     */
    @RequestMapping(value = "/queryStoreOrderCarts", method = RequestMethod.POST)
    List<StoreOrderCartDto> queryStoreOrderCarts(@RequestBody StoreOrderCartDto storeOrderCartDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param storeOrderCartDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryStoreOrderCartsCount", method = RequestMethod.POST)
    int queryStoreOrderCartsCount(@RequestBody StoreOrderCartDto storeOrderCartDto);
}
