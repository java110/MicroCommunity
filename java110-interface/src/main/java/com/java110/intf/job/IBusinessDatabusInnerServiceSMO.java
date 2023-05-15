package com.java110.intf.job;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.businessDatabus.BusinessDatabusDto;
import com.java110.po.businessDatabus.BusinessDatabusPo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IBusinessDatabusInnerServiceSMO
 * @Description 业务数据同步接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "job-service", configuration = {FeignConfiguration.class})
@RequestMapping("/businessDatabusApi")
public interface IBusinessDatabusInnerServiceSMO {


    @RequestMapping(value = "/saveBusinessDatabus", method = RequestMethod.POST)
    public int saveBusinessDatabus(@RequestBody BusinessDatabusPo businessDatabusPo);

    @RequestMapping(value = "/updateBusinessDatabus", method = RequestMethod.POST)
    public int updateBusinessDatabus(@RequestBody BusinessDatabusPo businessDatabusPo);

    @RequestMapping(value = "/deleteBusinessDatabus", method = RequestMethod.POST)
    public int deleteBusinessDatabus(@RequestBody BusinessDatabusPo businessDatabusPo);

    /**
     * <p>查询小区楼信息</p>
     *
     * @param businessDatabusDto 数据对象分享
     * @return BusinessDatabusDto 对象数据
     */
    @RequestMapping(value = "/queryBusinessDatabuss", method = RequestMethod.POST)
    List<BusinessDatabusDto> queryBusinessDatabuss(@RequestBody BusinessDatabusDto businessDatabusDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param businessDatabusDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryBusinessDatabussCount", method = RequestMethod.POST)
    int queryBusinessDatabussCount(@RequestBody BusinessDatabusDto businessDatabusDto);
}
