package com.java110.core.smo.inspectionRoute;

import com.java110.core.feign.FeignConfiguration;
import com.java110.dto.inspectionRoute.InspectionRoutePointRelDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IInspectionRoutePointRelInnerServiceSMO
 * @Description 巡检路线巡检点关系接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "community-service", configuration = {FeignConfiguration.class})
@RequestMapping("/inspectionRoutePointRelApi")
public interface IInspectionRoutePointRelInnerServiceSMO {

    /**
     * <p>查询小区楼信息</p>
     *
     * @param inspectionRoutePointRelDto 数据对象分享
     * @return InspectionRoutePointRelDto 对象数据
     */
    @RequestMapping(value = "/queryInspectionRoutePointRels", method = RequestMethod.POST)
    List<InspectionRoutePointRelDto> queryInspectionRoutePointRels(@RequestBody InspectionRoutePointRelDto inspectionRoutePointRelDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param inspectionRoutePointRelDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryInspectionRoutePointRelsCount", method = RequestMethod.POST)
    int queryInspectionRoutePointRelsCount(@RequestBody InspectionRoutePointRelDto inspectionRoutePointRelDto);
}
