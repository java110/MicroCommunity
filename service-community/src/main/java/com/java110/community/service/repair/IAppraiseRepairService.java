package com.java110.community.service.repair;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.appraise.AppraiseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 保存评价接口类
 */
public interface IAppraiseRepairService {

    /**
     * 保存接口评价
     * @param appraiseDto
     * @return
     */
    public AppraiseDto appraiseRepair(AppraiseDto appraiseDto);
}
