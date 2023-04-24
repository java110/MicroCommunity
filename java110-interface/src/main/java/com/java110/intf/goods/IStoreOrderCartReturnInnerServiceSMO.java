package com.java110.intf.goods;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.storeOrder.StoreOrderCartReturnDto;
import com.java110.po.storeOrderCartReturn.StoreOrderCartReturnPo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IStoreOrderCartReturnInnerServiceSMO
 * @Description 购物车事件接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "goods-service", configuration = {FeignConfiguration.class})
@RequestMapping("/storeOrderCartReturnApi")
public interface IStoreOrderCartReturnInnerServiceSMO {


    @RequestMapping(value = "/saveStoreOrderCartReturn", method = RequestMethod.POST)
    public int saveStoreOrderCartReturn(@RequestBody StoreOrderCartReturnPo storeOrderCartReturnPo);

    @RequestMapping(value = "/updateStoreOrderCartReturn", method = RequestMethod.POST)
    public int updateStoreOrderCartReturn(@RequestBody StoreOrderCartReturnPo storeOrderCartReturnPo);

    @RequestMapping(value = "/deleteStoreOrderCartReturn", method = RequestMethod.POST)
    public int deleteStoreOrderCartReturn(@RequestBody StoreOrderCartReturnPo storeOrderCartReturnPo);

    /**
     * <p>查询小区楼信息</p>
     *
     * @param storeOrderCartReturnDto 数据对象分享
     * @return StoreOrderCartReturnDto 对象数据
     */
    @RequestMapping(value = "/queryStoreOrderCartReturns", method = RequestMethod.POST)
    List<StoreOrderCartReturnDto> queryStoreOrderCartReturns(@RequestBody StoreOrderCartReturnDto storeOrderCartReturnDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param storeOrderCartReturnDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryStoreOrderCartReturnsCount", method = RequestMethod.POST)
    int queryStoreOrderCartReturnsCount(@RequestBody StoreOrderCartReturnDto storeOrderCartReturnDto);
}
