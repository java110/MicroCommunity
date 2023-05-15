package com.java110.intf.store;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.resourceStoreUseRecord.ResourceStoreUseRecordDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IResourceStoreUseRecordInnerServiceSMO
 * @Description 物品使用记录接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "store-service", configuration = {FeignConfiguration.class})
@RequestMapping("/resourceResourceStoreUseRecordUseRecordApi")
public interface IResourceStoreUseRecordInnerServiceSMO {

    /**
     * <p>查询小区楼信息</p>
     *
     * @param resourceResourceStoreUseRecordUseRecordDto 数据对象分享
     * @return ResourceStoreUseRecordDto 对象数据
     */
    @RequestMapping(value = "/queryResourceStoreUseRecords", method = RequestMethod.POST)
    List<ResourceStoreUseRecordDto> queryResourceStoreUseRecords(@RequestBody ResourceStoreUseRecordDto resourceResourceStoreUseRecordUseRecordDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param resourceResourceStoreUseRecordUseRecordDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryResourceStoreUseRecordsCount", method = RequestMethod.POST)
    int queryResourceStoreUseRecordsCount(@RequestBody ResourceStoreUseRecordDto resourceResourceStoreUseRecordUseRecordDto);
}
