package com.java110.intf.user;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.owner.OwnerAttrDto;
import com.java110.po.owner.OwnerAttrPo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IOwnerAttrInnerServiceSMO
 * @Description 业主属性接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "user-service", configuration = {FeignConfiguration.class})
@RequestMapping("/ownerAttrApi")
public interface IOwnerAttrInnerServiceSMO {

    /**
     * <p>查询小区楼信息</p>
     *
     * @param ownerAttrDto 数据对象分享
     * @return OwnerAttrDto 对象数据
     */
    @RequestMapping(value = "/queryOwnerAttrs", method = RequestMethod.POST)
    List<OwnerAttrDto> queryOwnerAttrs(@RequestBody OwnerAttrDto ownerAttrDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param ownerAttrDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryOwnerAttrsCount", method = RequestMethod.POST)
    int queryOwnerAttrsCount(@RequestBody OwnerAttrDto ownerAttrDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param ownerAttrPo 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/saveOwnerAttr", method = RequestMethod.POST)
    int saveOwnerAttr(@RequestBody OwnerAttrPo ownerAttrPo);
    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param ownerAttrPo 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/updateOwnerAttrInfoInstance", method = RequestMethod.POST)
    int updateOwnerAttrInfoInstance(@RequestBody OwnerAttrPo ownerAttrPo);
}
