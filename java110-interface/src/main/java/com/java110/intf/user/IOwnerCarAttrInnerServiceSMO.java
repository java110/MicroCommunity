package com.java110.intf.user;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.owner.OwnerCarAttrDto;
import com.java110.po.ownerCarAttr.OwnerCarAttrPo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IOwnerCarAttrInnerServiceSMO
 * @Description 业主车辆属性接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "user-service", configuration = {FeignConfiguration.class})
@RequestMapping("/ownerCarAttrApi")
public interface IOwnerCarAttrInnerServiceSMO {

    /**
     * <p>查询小区楼信息</p>
     *
     *
     * @param ownerCarAttrDto 数据对象分享
     * @return OwnerCarAttrDto 对象数据
     */
    @RequestMapping(value = "/queryOwnerCarAttrs", method = RequestMethod.POST)
    List<OwnerCarAttrDto> queryOwnerCarAttrs(@RequestBody OwnerCarAttrDto ownerCarAttrDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param ownerCarAttrDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryOwnerCarAttrsCount", method = RequestMethod.POST)
    int queryOwnerCarAttrsCount(@RequestBody OwnerCarAttrDto ownerCarAttrDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param ownerCarAttrPo 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/saveOwnerCarAttr", method = RequestMethod.POST)
    int saveOwnerCarAttr(@RequestBody OwnerCarAttrPo ownerCarAttrPo);
}
