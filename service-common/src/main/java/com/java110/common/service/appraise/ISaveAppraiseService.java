package com.java110.common.service.appraise;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.appraise.AppraiseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 保存评价接口类
 */

public interface ISaveAppraiseService {

    /**
     * 保存接口评价
     * @param appraiseDto
     * @return
     */
    public AppraiseDto saveAppraise(AppraiseDto appraiseDto);
}
