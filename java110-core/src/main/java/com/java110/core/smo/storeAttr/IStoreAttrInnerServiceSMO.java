package com.java110.core.smo.storeAttr;

import com.java110.core.feign.FeignConfiguration;
import com.java110.dto.store.StoreAttrDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IStoreAttrInnerServiceSMO
 * @Description 商户属性接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "store-service", configuration = {FeignConfiguration.class})
@RequestMapping("/storeAttrApi")
public interface IStoreAttrInnerServiceSMO {

    /**
     * <p>查询小区楼信息</p>
     *
     *
     * @param storeAttrDto 数据对象分享
     * @return StoreAttrDto 对象数据
     */
    @RequestMapping(value = "/queryStoreAttrs", method = RequestMethod.POST)
    List<StoreAttrDto> queryStoreAttrs(@RequestBody StoreAttrDto storeAttrDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param storeAttrDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryStoreAttrsCount", method = RequestMethod.POST)
    int queryStoreAttrsCount(@RequestBody StoreAttrDto storeAttrDto);
}
