package com.java110.intf.store;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.storeAds.StoreAdsDto;
import com.java110.po.storeAds.StoreAdsPo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IStoreAdsInnerServiceSMO
 * @Description 商户广告接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "store-service", configuration = {FeignConfiguration.class})
@RequestMapping("/storeAdsApi")
public interface IStoreAdsInnerServiceSMO {


    @RequestMapping(value = "/saveStoreAds", method = RequestMethod.POST)
    public int saveStoreAds(@RequestBody StoreAdsPo storeAdsPo);

    @RequestMapping(value = "/updateStoreAds", method = RequestMethod.POST)
    public int updateStoreAds(@RequestBody  StoreAdsPo storeAdsPo);

    @RequestMapping(value = "/deleteStoreAds", method = RequestMethod.POST)
    public int deleteStoreAds(@RequestBody  StoreAdsPo storeAdsPo);

    /**
     * <p>查询小区楼信息</p>
     *
     *
     * @param storeAdsDto 数据对象分享
     * @return StoreAdsDto 对象数据
     */
    @RequestMapping(value = "/queryStoreAdss", method = RequestMethod.POST)
    List<StoreAdsDto> queryStoreAdss(@RequestBody StoreAdsDto storeAdsDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param storeAdsDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryStoreAdssCount", method = RequestMethod.POST)
    int queryStoreAdssCount(@RequestBody StoreAdsDto storeAdsDto);
}
