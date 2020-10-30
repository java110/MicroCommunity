package com.java110.intf.goods;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.storeCart.StoreCartDto;
import com.java110.po.storeCart.StoreCartPo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IStoreCartInnerServiceSMO
 * @Description 购物车接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "goods-service", configuration = {FeignConfiguration.class})
@RequestMapping("/storeCartApi")
public interface IStoreCartInnerServiceSMO {


    @RequestMapping(value = "/saveStoreCart", method = RequestMethod.POST)
    public int saveStoreCart(@RequestBody StoreCartPo storeCartPo);

    @RequestMapping(value = "/updateStoreCart", method = RequestMethod.POST)
    public int updateStoreCart(@RequestBody StoreCartPo storeCartPo);

    @RequestMapping(value = "/deleteStoreCart", method = RequestMethod.POST)
    public int deleteStoreCart(@RequestBody StoreCartPo storeCartPo);

    /**
     * <p>查询小区楼信息</p>
     *
     * @param storeCartDto 数据对象分享
     * @return StoreCartDto 对象数据
     */
    @RequestMapping(value = "/queryStoreCarts", method = RequestMethod.POST)
    List<StoreCartDto> queryStoreCarts(@RequestBody StoreCartDto storeCartDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param storeCartDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryStoreCartsCount", method = RequestMethod.POST)
    int queryStoreCartsCount(@RequestBody StoreCartDto storeCartDto);
}
