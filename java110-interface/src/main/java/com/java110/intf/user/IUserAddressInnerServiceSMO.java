package com.java110.intf.user;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.userAddress.UserAddressDto;
import com.java110.po.userAddress.UserAddressPo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IUserAddressInnerServiceSMO
 * @Description 用户联系地址接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "user-service", configuration = {FeignConfiguration.class})
@RequestMapping("/userAddressApi")
public interface IUserAddressInnerServiceSMO {


    @RequestMapping(value = "/saveUserAddress", method = RequestMethod.POST)
    public int saveUserAddress(@RequestBody UserAddressPo userAddressPo);

    @RequestMapping(value = "/updateUserAddress", method = RequestMethod.POST)
    public int updateUserAddress(@RequestBody  UserAddressPo userAddressPo);

    @RequestMapping(value = "/deleteUserAddress", method = RequestMethod.POST)
    public int deleteUserAddress(@RequestBody  UserAddressPo userAddressPo);

    /**
     * <p>查询小区楼信息</p>
     *
     *
     * @param userAddressDto 数据对象分享
     * @return UserAddressDto 对象数据
     */
    @RequestMapping(value = "/queryUserAddresss", method = RequestMethod.POST)
    List<UserAddressDto> queryUserAddresss(@RequestBody UserAddressDto userAddressDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param userAddressDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryUserAddresssCount", method = RequestMethod.POST)
    int queryUserAddresssCount(@RequestBody UserAddressDto userAddressDto);
}
