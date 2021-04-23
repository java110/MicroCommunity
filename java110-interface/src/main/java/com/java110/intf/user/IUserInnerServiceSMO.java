package com.java110.intf.user;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.user.UserAttrDto;
import com.java110.dto.user.UserDto;
import com.java110.po.user.UserPo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 用户服务接口类（供服务间调用）
 */
@FeignClient(name = "user-service", configuration = {FeignConfiguration.class})
@RequestMapping("/userApi")
public interface IUserInnerServiceSMO {

    /**
     * 查询用户服务版本
     *
     * @param code 编码 没有任何意义 随便传一个字符串就可以
     * @return 编码 + 版本号
     */
    @RequestMapping(value = "/getUserServiceVersion", method = RequestMethod.GET)
    String getUserServiceVersion(@RequestParam("code") String code);

    /**
     * 查询用户信息
     *
     * @param userIds 用户ID
     *                支持 多个查询
     * @return 用户封装信息
     */
    @RequestMapping(value = "/getUserInfo", method = RequestMethod.GET)
    List<UserDto> getUserInfo(@RequestParam("userIds") String[] userIds);


    /**
     * 查询员工总数
     *
     * @param userDto 用户ID
     *                支持 多个查询
     * @return 用户封装信息
     */
    @RequestMapping(value = "/getStaffCount", method = RequestMethod.POST)
    int getStaffCount(@RequestBody UserDto userDto);


    /**
     * 查询员工信息
     *
     * @param userDto 用户ID
     *                支持 多个查询
     * @return 用户封装信息
     */
    @RequestMapping(value = "/getStaffs", method = RequestMethod.POST)
    List<UserDto> getStaffs(@RequestBody UserDto userDto);


    /**
     * 查询用户总数
     *
     * @param userDto 用户ID
     *                支持 多个查询
     * @return 用户封装信息
     */
    @RequestMapping(value = "/getUserCount", method = RequestMethod.POST)
    int getUserCount(@RequestBody UserDto userDto);


    /**
     * 查询员工信息
     *
     * @param userDto 用户ID
     *                支持 多个查询
     * @return 用户封装信息
     */
    @RequestMapping(value = "/getUsers", method = RequestMethod.POST)
    List<UserDto> getUsers(@RequestBody UserDto userDto);

    @RequestMapping(value = "/getUserHasPwd", method = RequestMethod.POST)
    List<UserDto> getUserHasPwd(@RequestBody UserDto userDto);

    /**
     * 查询用户属性
     *
     * @param userAttrDto 用户ID
     *                支持 多个查询
     * @return 用户封装信息
     */
    @RequestMapping(value = "/getUserAttrs", method = RequestMethod.POST)
    List<UserAttrDto> getUserAttrs(@RequestBody UserAttrDto userAttrDto);

    @RequestMapping(value = "/updateUser", method = RequestMethod.POST)
    int updateUser(@RequestBody  UserPo userPo);
}
