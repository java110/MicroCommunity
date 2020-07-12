package com.java110.intf.store;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.wechatMenu.WechatMenuDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IWechatMenuInnerServiceSMO
 * @Description 公众号菜单接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "store-service", configuration = {FeignConfiguration.class})
@RequestMapping("/wechatMenuApi")
public interface IWechatMenuInnerServiceSMO {

    /**
     * <p>查询小区楼信息</p>
     *
     *
     * @param wechatMenuDto 数据对象分享
     * @return WechatMenuDto 对象数据
     */
    @RequestMapping(value = "/queryWechatMenus", method = RequestMethod.POST)
    List<WechatMenuDto> queryWechatMenus(@RequestBody WechatMenuDto wechatMenuDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param wechatMenuDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryWechatMenusCount", method = RequestMethod.POST)
    int queryWechatMenusCount(@RequestBody WechatMenuDto wechatMenuDto);
}
