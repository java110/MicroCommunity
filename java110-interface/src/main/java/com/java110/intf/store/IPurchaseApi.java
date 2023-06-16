package com.java110.intf.store;


import com.java110.config.feign.FeignConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 采购申请类
 */
@FeignClient(name = "store-service", configuration = {FeignConfiguration.class})
@RequestMapping("/purchase")
public interface IPurchaseApi {

}
