package com.java110.intf.user;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.rentingAppointment.RentingAppointmentDto;
import com.java110.po.rentingAppointment.RentingAppointmentPo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IRentingAppointmentInnerServiceSMO
 * @Description 租赁预约接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "user-service", configuration = {FeignConfiguration.class})
@RequestMapping("/rentingAppointmentApi")
public interface IRentingAppointmentInnerServiceSMO {


    @RequestMapping(value = "/saveRentingAppointment", method = RequestMethod.POST)
    public int saveRentingAppointment(@RequestBody RentingAppointmentPo rentingAppointmentPo);

    @RequestMapping(value = "/updateRentingAppointment", method = RequestMethod.POST)
    public int updateRentingAppointment(@RequestBody RentingAppointmentPo rentingAppointmentPo);

    @RequestMapping(value = "/deleteRentingAppointment", method = RequestMethod.POST)
    public int deleteRentingAppointment(@RequestBody RentingAppointmentPo rentingAppointmentPo);

    /**
     * <p>查询小区楼信息</p>
     *
     * @param rentingAppointmentDto 数据对象分享
     * @return RentingAppointmentDto 对象数据
     */
    @RequestMapping(value = "/queryRentingAppointments", method = RequestMethod.POST)
    List<RentingAppointmentDto> queryRentingAppointments(@RequestBody RentingAppointmentDto rentingAppointmentDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param rentingAppointmentDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryRentingAppointmentsCount", method = RequestMethod.POST)
    int queryRentingAppointmentsCount(@RequestBody RentingAppointmentDto rentingAppointmentDto);
}
