package com.java110.feign.base;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 主键信息查询
 * Created by wuxw on 2017/4/11.
 */
@FeignClient(name = "base-service", fallback = PrimaryKeyServiceFallback.class)
public interface IPrimaryKeyService {

    @RequestMapping("/primaryKeyService/queryPrimaryKey")
    public String queryPrimaryKey(@RequestParam("data") String data);
}
