package com.java110.intf.user;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.owner.OwnerAppUserDto;
import com.java110.po.owner.OwnerAppUserPo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IOwnerAppUserInnerServiceSMO
 * @Description 绑定业主接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "user-service", configuration = {FeignConfiguration.class})
@RequestMapping("/ownerAppUserApi")
public interface IOwnerAppUserInnerServiceSMO {

    /**
     * <p>查询小区楼信息</p>
     *
     * @param ownerAppUserDto 数据对象分享
     * @return OwnerAppUserDto 对象数据
     */
    @RequestMapping(value = "/queryOwnerAppUsers", method = RequestMethod.POST)
    List<OwnerAppUserDto> queryOwnerAppUsers(@RequestBody OwnerAppUserDto ownerAppUserDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param ownerAppUserDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryOwnerAppUsersCount", method = RequestMethod.POST)
    int queryOwnerAppUsersCount(@RequestBody OwnerAppUserDto ownerAppUserDto);

    @RequestMapping(value = "/updateOwnerAppUser", method = RequestMethod.POST)
    int updateOwnerAppUser(@RequestBody OwnerAppUserPo ownerAppUserPo);
}
