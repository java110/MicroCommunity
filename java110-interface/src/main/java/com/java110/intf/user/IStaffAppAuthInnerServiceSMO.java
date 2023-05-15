package com.java110.intf.user;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.staffAppAuth.StaffAppAuthDto;
import com.java110.po.staffAppAuth.StaffAppAuthPo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IStaffAppAuthInnerServiceSMO
 * @Description 员工微信认证接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "user-service", configuration = {FeignConfiguration.class})
@RequestMapping("/staffAppAuthApi")
public interface IStaffAppAuthInnerServiceSMO {


    @RequestMapping(value = "/saveStaffAppAuth", method = RequestMethod.POST)
    public int saveStaffAppAuth(@RequestBody StaffAppAuthPo staffAppAuthPo);

    @RequestMapping(value = "/updateStaffAppAuth", method = RequestMethod.POST)
    public int updateStaffAppAuth(@RequestBody  StaffAppAuthPo staffAppAuthPo);

    @RequestMapping(value = "/deleteStaffAppAuth", method = RequestMethod.POST)
    public int deleteStaffAppAuth(@RequestBody  StaffAppAuthPo staffAppAuthPo);

    /**
     * <p>查询小区楼信息</p>
     *
     *
     * @param staffAppAuthDto 数据对象分享
     * @return StaffAppAuthDto 对象数据
     */
    @RequestMapping(value = "/queryStaffAppAuths", method = RequestMethod.POST)
    List<StaffAppAuthDto> queryStaffAppAuths(@RequestBody StaffAppAuthDto staffAppAuthDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param staffAppAuthDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryStaffAppAuthsCount", method = RequestMethod.POST)
    int queryStaffAppAuthsCount(@RequestBody StaffAppAuthDto staffAppAuthDto);
}
