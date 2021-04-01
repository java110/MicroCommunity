package com.java110.intf.community;

import com.java110.config.feign.FeignConfiguration;
import com.java110.po.purchase.ResourceStorePo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient(name = "community-service", configuration = {FeignConfiguration.class})
@RequestMapping("/resourceStoreApi")
public interface IResourceStoreServiceSMO {

    /**
     * 查询资源物品信息
     *
     * @param resourceStorePo
     * @return
     */
    @RequestMapping(value = "/getResourceStores", method = RequestMethod.POST)
    List<ResourceStorePo> getResourceStores(@RequestBody ResourceStorePo resourceStorePo);

    /**
     * 查询资源物品信息数量
     *
     * @param resourceStorePo
     * @return
     */
    @RequestMapping(value = "/getResourceStoresCount", method = RequestMethod.POST)
    int getResourceStoresCount(@RequestBody ResourceStorePo resourceStorePo);

}
