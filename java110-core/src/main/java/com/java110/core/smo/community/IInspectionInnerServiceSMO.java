package com.java110.core.smo.community;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.inspectionPoint.InspectionDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IInspectionInnerServiceSMO
 * @Description 巡检点接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "community-service", configuration = {FeignConfiguration.class})
@RequestMapping("/inspectionApi")
public interface IInspectionInnerServiceSMO {

    /**
     * <p>查询小区楼信息</p>
     *
     *
     * @param inspectionDto 数据对象分享
     * @return InspectionDto 对象数据
     */
    @RequestMapping(value = "/queryInspections", method = RequestMethod.POST)
    List<InspectionDto> queryInspections(@RequestBody InspectionDto inspectionDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param inspectionDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryInspectionsCount", method = RequestMethod.POST)
    int queryInspectionsCount(@RequestBody InspectionDto inspectionDto);

    @RequestMapping(value = "/getInspectionRelationShip", method = RequestMethod.POST)
    public List<InspectionDto> getInspectionRelationShip(@RequestBody  InspectionDto inspectionDto);

    @RequestMapping(value = "/queryInspectionsRelationShipCount", method = RequestMethod.POST)
    public int queryInspectionsRelationShipCount(@RequestBody InspectionDto inspectionDto);

    @RequestMapping(value = "/queryInspectionsByPlan", method = RequestMethod.POST)
    List<InspectionDto> queryInspectionsByPlan(@RequestBody InspectionDto inspectionDto);
}
