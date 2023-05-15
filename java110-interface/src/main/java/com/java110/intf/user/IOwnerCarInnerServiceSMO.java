package com.java110.intf.user;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.owner.OwnerCarDto;
import com.java110.entity.assetImport.ImportCustomCreateFeeDto;
import com.java110.entity.assetImport.ImportRoomFee;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IOwnerCarInnerServiceSMO
 * @Description 车辆管理接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "user-service", configuration = {FeignConfiguration.class})
@RequestMapping("/ownerCarApi")
public interface IOwnerCarInnerServiceSMO {

    /**
     * <p>查询小区楼信息</p>
     *
     *
     * @param ownerCarDto 数据对象分享
     * @return OwnerCarDto 对象数据
     */
    @RequestMapping(value = "/queryOwnerCars", method = RequestMethod.POST)
    List<OwnerCarDto> queryOwnerCars(@RequestBody OwnerCarDto ownerCarDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param ownerCarDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryOwnerCarsCount", method = RequestMethod.POST)
    int queryOwnerCarsCount(@RequestBody OwnerCarDto ownerCarDto);

    @RequestMapping(value = "/freshCarIds", method = RequestMethod.POST)
    List<ImportRoomFee> freshCarIds(@RequestBody List<ImportRoomFee> tmpImportCarFees);

    @RequestMapping(value = "/freshCarIdsByImportCustomCreateFee", method = RequestMethod.POST)
    List<ImportCustomCreateFeeDto> freshCarIdsByImportCustomCreateFee(@RequestBody List<ImportCustomCreateFeeDto> tmpImportCarFees);
    /**
     * <p>查询业主车位数</p>
     *
     *
     * @param ownerCarDto 数据对象分享
     * @return OwnerCarDto 对象数据
     */
    @RequestMapping(value = "/queryOwnerParkingSpaceCount", method = RequestMethod.POST)
    long queryOwnerParkingSpaceCount(@RequestBody OwnerCarDto ownerCarDto);
}
