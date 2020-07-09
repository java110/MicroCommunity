package com.java110.intf.user.org;


import com.java110.config.feign.FeignConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(name = "user-service", configuration = {FeignConfiguration.class})
@RequestMapping("/org")
public interface IOrgApi {


}
