package com.java110.intf.community;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.repair.RepairReturnVisitDto;
import com.java110.po.repair.RepairReturnVisitPo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IRepairReturnVisitInnerServiceSMO
 * @Description 报修回访接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "community-service", configuration = {FeignConfiguration.class})
@RequestMapping("/repairReturnVisitApi")
public interface IRepairReturnVisitInnerServiceSMO {


    @RequestMapping(value = "/saveRepairReturnVisit", method = RequestMethod.POST)
    public int saveRepairReturnVisit(@RequestBody RepairReturnVisitPo repairReturnVisitPo);

    @RequestMapping(value = "/updateRepairReturnVisit", method = RequestMethod.POST)
    public int updateRepairReturnVisit(@RequestBody  RepairReturnVisitPo repairReturnVisitPo);

    @RequestMapping(value = "/deleteRepairReturnVisit", method = RequestMethod.POST)
    public int deleteRepairReturnVisit(@RequestBody  RepairReturnVisitPo repairReturnVisitPo);

    /**
     * <p>查询小区楼信息</p>
     *
     *
     * @param repairReturnVisitDto 数据对象分享
     * @return RepairReturnVisitDto 对象数据
     */
    @RequestMapping(value = "/queryRepairReturnVisits", method = RequestMethod.POST)
    List<RepairReturnVisitDto> queryRepairReturnVisits(@RequestBody RepairReturnVisitDto repairReturnVisitDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param repairReturnVisitDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryRepairReturnVisitsCount", method = RequestMethod.POST)
    int queryRepairReturnVisitsCount(@RequestBody RepairReturnVisitDto repairReturnVisitDto);
}
