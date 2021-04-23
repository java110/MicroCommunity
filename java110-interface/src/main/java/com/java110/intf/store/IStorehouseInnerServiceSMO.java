package com.java110.intf.store;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.storehouse.StorehouseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IStorehouseInnerServiceSMO
 * @Description 仓库接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "store-service", configuration = {FeignConfiguration.class})
@RequestMapping("/storehouseApi")
public interface IStorehouseInnerServiceSMO {

    /**
     * <p>查询小区楼信息</p>
     *
     *
     * @param storehouseDto 数据对象分享
     * @return StorehouseDto 对象数据
     */
    @RequestMapping(value = "/queryStorehouses", method = RequestMethod.POST)
    List<StorehouseDto> queryStorehouses(@RequestBody StorehouseDto storehouseDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param storehouseDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryStorehousesCount", method = RequestMethod.POST)
    int queryStorehousesCount(@RequestBody StorehouseDto storehouseDto);
}
