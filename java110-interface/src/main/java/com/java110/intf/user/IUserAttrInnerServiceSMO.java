package com.java110.intf.user;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.user.UserAttrDto;
import com.java110.po.userAttr.UserAttrPo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IUserAttrInnerServiceSMO
 * @Description 用户属性接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "user-service", configuration = {FeignConfiguration.class})
@RequestMapping("/userAttrApi")
public interface IUserAttrInnerServiceSMO {

    /**
     * <p>查询小区楼信息</p>
     *
     * @param userAttrDto 数据对象分享
     * @return UserAttrDto 对象数据
     */
    @RequestMapping(value = "/queryUserAttrs", method = RequestMethod.POST)
    List<UserAttrDto> queryUserAttrs(@RequestBody UserAttrDto userAttrDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param userAttrDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryUserAttrsCount", method = RequestMethod.POST)
    int queryUserAttrsCount(@RequestBody UserAttrDto userAttrDto);
    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param userAttrDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/deleteUserAttr", method = RequestMethod.POST)
    int deleteUserAttr(@RequestBody UserAttrDto userAttrDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param userAttrPo 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/saveUserAttr", method = RequestMethod.POST)
    int saveUserAttr(@RequestBody UserAttrPo userAttrPo);
    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param userAttrPo 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/updateUserAttrInfoInstance", method = RequestMethod.POST)
    int updateUserAttrInfoInstance(@RequestBody UserAttrPo userAttrPo);
}
