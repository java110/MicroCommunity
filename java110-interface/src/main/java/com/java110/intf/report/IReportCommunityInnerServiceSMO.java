package com.java110.intf.report;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.RoomDto;
import com.java110.dto.fee.FeeDto;
import com.java110.dto.owner.OwnerCarDto;
import com.java110.dto.owner.OwnerDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IReportOwnerPayFeeInnerServiceSMO
 * @Description 业主缴费明细接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "report-service", configuration = {FeignConfiguration.class})
@RequestMapping("/reportCommunityApi")
public interface IReportCommunityInnerServiceSMO {


    /**
     * <p>查询结构化房屋信息</p>
     *
     * @param roomDto 数据对象分享
     * @return ReportOwnerPayFeeDto 对象数据
     */
    @RequestMapping(value = "/queryRoomStructures", method = RequestMethod.POST)
    List<RoomDto> queryRoomStructures(@RequestBody RoomDto roomDto);

    /**
     * <p>查询结构化房屋信息</p>
     *
     * @param ownerCarDto 数据对象分享
     * @return ReportOwnerPayFeeDto 对象数据
     */
    @RequestMapping(value = "/queryCarStructures", method = RequestMethod.POST)
    List<OwnerCarDto> queryCarStructures(@RequestBody OwnerCarDto ownerCarDto);

    /**
     * 查询房屋树形
     * @param roomDto
     * @return
     */
    @RequestMapping(value = "/queryRoomsTree", method = RequestMethod.POST)
    List<RoomDto> queryRoomsTree(@RequestBody RoomDto roomDto);

    /**
     * 查询车辆变更总数
     * @param ownerCarDto
     * @return
     */
    @RequestMapping(value = "/queryHisOwnerCarCount", method = RequestMethod.POST)
    int queryHisOwnerCarCount(@RequestBody OwnerCarDto ownerCarDto);

    /**
     * 查询车辆变更
     * @param ownerCarDto
     * @return
     */
    @RequestMapping(value = "/queryHisOwnerCars", method = RequestMethod.POST)
    List<OwnerCarDto> queryHisOwnerCars(@RequestBody OwnerCarDto ownerCarDto);

    @RequestMapping(value = "/queryHisOwnerCount", method = RequestMethod.POST)
    int queryHisOwnerCount(@RequestBody OwnerDto ownerDto);

    @RequestMapping(value = "/queryHisOwners", method = RequestMethod.POST)
    List<OwnerDto> queryHisOwners(@RequestBody OwnerDto ownerDto);

    @RequestMapping(value = "/queryHisFeeCount", method = RequestMethod.POST)
    int queryHisFeeCount(@RequestBody FeeDto feeDto);

    @RequestMapping(value = "/queryHisFees", method = RequestMethod.POST)
    List<FeeDto> queryHisFees(@RequestBody FeeDto feeDto);
}
