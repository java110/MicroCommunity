package com.java110.intf.order;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.corder.CorderDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient(name = "order-service", configuration = {FeignConfiguration.class})
@RequestMapping("/ordersApi")
public interface ICordersInnerServiceSMO {

    @RequestMapping(value = "/queryCordersCount", method = RequestMethod.POST)
    int queryCordersCount(@RequestBody CorderDto corderDto);


    @RequestMapping(value = "/queryCorders", method = RequestMethod.POST)
    List<CorderDto> queryCorders(@RequestBody CorderDto corderDto);

}
