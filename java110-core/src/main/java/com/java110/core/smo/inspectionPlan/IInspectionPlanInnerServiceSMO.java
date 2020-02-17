package com.java110.core.smo.inspectionPlan;

import com.java110.core.feign.FeignConfiguration;
import com.java110.dto.inspectionPlan.InspectionPlanDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IInspectionPlanInnerServiceSMO
 * @Description 巡检计划接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "community-service", configuration = {FeignConfiguration.class})
@RequestMapping("/inspectionPlanApi")
public interface IInspectionPlanInnerServiceSMO {

    /**
     * <p>查询小区楼信息</p>
     *
     *
     * @param inspectionPlanDto 数据对象分享
     * @return InspectionPlanDto 对象数据
     */
    @RequestMapping(value = "/queryInspectionPlans", method = RequestMethod.POST)
    List<InspectionPlanDto> queryInspectionPlans(@RequestBody InspectionPlanDto inspectionPlanDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param inspectionPlanDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryInspectionPlansCount", method = RequestMethod.POST)
    int queryInspectionPlansCount(@RequestBody InspectionPlanDto inspectionPlanDto);
}
