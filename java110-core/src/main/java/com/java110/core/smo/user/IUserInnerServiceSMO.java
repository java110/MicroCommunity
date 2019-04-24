package com.java110.core.smo.user;

import com.java110.core.feign.FeignConfiguration;
import com.java110.dto.UserDto;
import org.springframework.cloud.netflix.feign.FeignClient;
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
}
