package com.java110.core.smo.inspectionPlanStaff;

import com.java110.core.feign.FeignConfiguration;
import com.java110.dto.inspectionPlanStaff.InspectionPlanStaffDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IInspectionPlanStaffInnerServiceSMO
 * @Description 执行计划人接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "community-service", configuration = {FeignConfiguration.class})
@RequestMapping("/inspectionPlanStaffApi")
public interface IInspectionPlanStaffInnerServiceSMO {

    /**
     * <p>查询小区楼信息</p>
     *
     * @param inspectionPlanStaffDto 数据对象分享
     * @return InspectionPlanStaffDto 对象数据
     */
    @RequestMapping(value = "/queryInspectionPlanStaffs", method = RequestMethod.POST)
    List<InspectionPlanStaffDto> queryInspectionPlanStaffs(@RequestBody InspectionPlanStaffDto inspectionPlanStaffDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param inspectionPlanStaffDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryInspectionPlanStaffsCount", method = RequestMethod.POST)
    int queryInspectionPlanStaffsCount(@RequestBody InspectionPlanStaffDto inspectionPlanStaffDto);
}
