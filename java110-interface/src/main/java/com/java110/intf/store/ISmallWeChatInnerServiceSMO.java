package com.java110.intf.store;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.smallWeChat.SmallWeChatDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName ISmallWeChatInnerServiceSMO
 * @Description 小程序管理接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "store-service", configuration = {FeignConfiguration.class})
@RequestMapping("/smallWeChatApi")
public interface ISmallWeChatInnerServiceSMO {

    /**
     * <p>查询小区楼信息</p>
     *
     * @param smallWeChatDto 数据对象分享
     * @return SmallWeChatDto 对象数据
     */
    @RequestMapping(value = "/querySmallWeChats", method = RequestMethod.POST)
    List<SmallWeChatDto> querySmallWeChats(@RequestBody SmallWeChatDto smallWeChatDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param smallWeChatDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/querySmallWeChatsCount", method = RequestMethod.POST)
    int querySmallWeChatsCount(@RequestBody SmallWeChatDto smallWeChatDto);
}
