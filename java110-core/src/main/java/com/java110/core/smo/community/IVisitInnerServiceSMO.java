package com.java110.core.smo.community;

import com.java110.core.feign.FeignConfiguration;
import com.java110.dto.visit.VisitDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IVisitInnerServiceSMO
 * @Description 访客信息接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "community-service", configuration = {FeignConfiguration.class})
@RequestMapping("/visitApi")
public interface IVisitInnerServiceSMO {

    /**
     * <p>查询小区楼信息</p>
     *
     *
     * @param visitDto 数据对象分享
     * @return VisitDto 对象数据
     */
    @RequestMapping(value = "/queryVisits", method = RequestMethod.POST)
    List<VisitDto> queryVisits(@RequestBody VisitDto visitDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param visitDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryVisitsCount", method = RequestMethod.POST)
    int queryVisitsCount(@RequestBody VisitDto visitDto);
}
