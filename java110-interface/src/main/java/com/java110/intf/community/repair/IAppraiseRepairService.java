package com.java110.intf.community.repair;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.appraise.AppraiseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 保存评价接口类
 */
@FeignClient(name = "community-service", configuration = {FeignConfiguration.class})
@RequestMapping("/repair")
public interface IAppraiseRepairService {

    /**
     * 保存接口评价
     * @param appraiseDto
     * @return
     */
    @RequestMapping(value = "/appraiseRepair", method = RequestMethod.POST)
    public AppraiseDto appraiseRepair(@RequestBody AppraiseDto appraiseDto);
}
