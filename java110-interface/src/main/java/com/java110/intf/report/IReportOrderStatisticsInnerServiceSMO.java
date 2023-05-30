package com.java110.intf.report;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.owner.OwnerDto;
import com.java110.dto.report.QueryStatisticsDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.Map;

/**
 * @ClassName IReportFeeStatisticsInnerServiceSMO
 * @Description 费用统计类 服务类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "report-service", configuration = {FeignConfiguration.class})
@RequestMapping("/reportOrderStatisticsApi")
public interface IReportOrderStatisticsInnerServiceSMO {


    /**
     * <p>查询投诉工单数</p>
     *
     * @param queryStatisticsDto 数据对象分享
     */
    @RequestMapping(value = "/getComplaintOrderCount", method = RequestMethod.POST)
    double getComplaintOrderCount(@RequestBody QueryStatisticsDto queryStatisticsDto);


    /**
     * 查询未处理 投诉工单
     * @param queryStatisticsDto
     * @return
     */
    @RequestMapping(value = "/getUndoComplaintOrderCount", method = RequestMethod.POST)
    double getUndoComplaintOrderCount(@RequestBody QueryStatisticsDto queryStatisticsDto);

    /**
     * 查询已处理 投诉工单
     * @param queryStatisticsDto
     * @return
     */
    @RequestMapping(value = "/getFinishComplaintOrderCount", method = RequestMethod.POST)
    double getFinishComplaintOrderCount(@RequestBody QueryStatisticsDto queryStatisticsDto);

    @RequestMapping(value = "/getRepairOrderCount", method = RequestMethod.POST)
    double getRepairOrderCount(@RequestBody QueryStatisticsDto queryStatisticsDto);

    @RequestMapping(value = "/getUndoRepairOrderCount", method = RequestMethod.POST)
    double getUndoRepairOrderCount(@RequestBody QueryStatisticsDto queryStatisticsDto);

    @RequestMapping(value = "/getFinishRepairOrderCount", method = RequestMethod.POST)
    double getFinishRepairOrderCount(@RequestBody QueryStatisticsDto queryStatisticsDto);

    @RequestMapping(value = "/getInspectionOrderCount", method = RequestMethod.POST)
    double getInspectionOrderCount(@RequestBody QueryStatisticsDto queryStatisticsDto);

    @RequestMapping(value = "/getUndoInspectionOrderCount", method = RequestMethod.POST)
    double getUndoInspectionOrderCount(@RequestBody QueryStatisticsDto queryStatisticsDto);

    @RequestMapping(value = "/getFinishInspectionOrderCount", method = RequestMethod.POST)
    double getFinishInspectionOrderCount(@RequestBody QueryStatisticsDto queryStatisticsDto);

    @RequestMapping(value = "/getMaintainanceOrderCount", method = RequestMethod.POST)
    double getMaintainanceOrderCount(@RequestBody QueryStatisticsDto queryStatisticsDto);

    @RequestMapping(value = "/getUndoMaintainanceOrderCount", method = RequestMethod.POST)
    double getUndoMaintainanceOrderCount(@RequestBody QueryStatisticsDto queryStatisticsDto);

    @RequestMapping(value = "/getFinishMaintainanceOrderCount", method = RequestMethod.POST)
    double getFinishMaintainanceOrderCount(@RequestBody QueryStatisticsDto queryStatisticsDto);

    @RequestMapping(value = "/getNotepadOrderCount", method = RequestMethod.POST)
    double getNotepadOrderCount(@RequestBody QueryStatisticsDto queryStatisticsDto);

    @RequestMapping(value = "/getChargeMachineOrderCount", method = RequestMethod.POST)
    double getChargeMachineOrderCount(@RequestBody QueryStatisticsDto queryStatisticsDto);

    @RequestMapping(value = "/getChargeMonthOrderCount", method = RequestMethod.POST)
    double getChargeMonthOrderCount(@RequestBody QueryStatisticsDto queryStatisticsDto);

    @RequestMapping(value = "/getOwnerReserveGoodsCount", method = RequestMethod.POST)
    int getOwnerReserveGoodsCount(@RequestBody OwnerDto ownerDto);

    @RequestMapping(value = "/getOwnerReserveGoods", method = RequestMethod.POST)
    List<Map> getOwnerReserveGoods(@RequestBody OwnerDto ownerDto);

    @RequestMapping(value = "/getOwnerDiningCount", method = RequestMethod.POST)
    int getOwnerDiningCount(@RequestBody OwnerDto ownerDto);

    @RequestMapping(value = "/getOwnerDinings", method = RequestMethod.POST)
    List<Map> getOwnerDinings(@RequestBody OwnerDto ownerDto);
}
