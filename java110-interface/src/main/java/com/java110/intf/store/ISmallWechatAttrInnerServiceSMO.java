package com.java110.intf.store;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.smallWechatAttr.SmallWechatAttrDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName ISmallWechatAttrInnerServiceSMO
 * @Description 微信属性接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "store-service", configuration = {FeignConfiguration.class})
@RequestMapping("/smallWechatAttrApi")
public interface ISmallWechatAttrInnerServiceSMO {

    /**
     * <p>查询小区楼信息</p>
     *
     *
     * @param smallWechatAttrDto 数据对象分享
     * @return SmallWechatAttrDto 对象数据
     */
    @RequestMapping(value = "/querySmallWechatAttrs", method = RequestMethod.POST)
    List<SmallWechatAttrDto> querySmallWechatAttrs(@RequestBody SmallWechatAttrDto smallWechatAttrDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param smallWechatAttrDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/querySmallWechatAttrsCount", method = RequestMethod.POST)
    int querySmallWechatAttrsCount(@RequestBody SmallWechatAttrDto smallWechatAttrDto);
}
