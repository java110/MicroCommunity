package com.java110.intf.store;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.complaint.ComplaintDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IComplaintInnerServiceSMO
 * @Description 投诉建议接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "store-service", configuration = {FeignConfiguration.class})
@RequestMapping("/complaintApi")
public interface IComplaintInnerServiceSMO {

    /**
     * <p>查询小区楼信息</p>
     *
     * @param complaintDto 数据对象分享
     * @return ComplaintDto 对象数据
     */
    @RequestMapping(value = "/queryComplaints", method = RequestMethod.POST)
    List<ComplaintDto> queryComplaints(@RequestBody ComplaintDto complaintDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param complaintDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryComplaintsCount", method = RequestMethod.POST)
    int queryComplaintsCount(@RequestBody ComplaintDto complaintDto);
}
