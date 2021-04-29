package com.java110.intf.user;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.userLogin.UserLoginDto;
import com.java110.po.userLogin.UserLoginPo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IUserLoginInnerServiceSMO
 * @Description 用户登录接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "user-service", configuration = {FeignConfiguration.class})
@RequestMapping("/userLoginApi")
public interface IUserLoginInnerServiceSMO {


    @RequestMapping(value = "/saveUserLogin", method = RequestMethod.POST)
    public int saveUserLogin(@RequestBody UserLoginPo userLoginPo);

    @RequestMapping(value = "/updateUserLogin", method = RequestMethod.POST)
    public int updateUserLogin(@RequestBody  UserLoginPo userLoginPo);

    @RequestMapping(value = "/deleteUserLogin", method = RequestMethod.POST)
    public int deleteUserLogin(@RequestBody  UserLoginPo userLoginPo);

    /**
     * <p>查询小区楼信息</p>
     *
     *
     * @param userLoginDto 数据对象分享
     * @return UserLoginDto 对象数据
     */
    @RequestMapping(value = "/queryUserLogins", method = RequestMethod.POST)
    List<UserLoginDto> queryUserLogins(@RequestBody UserLoginDto userLoginDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param userLoginDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryUserLoginsCount", method = RequestMethod.POST)
    int queryUserLoginsCount(@RequestBody UserLoginDto userLoginDto);
}
