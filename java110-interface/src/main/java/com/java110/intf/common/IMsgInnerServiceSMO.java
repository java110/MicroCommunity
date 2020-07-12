package com.java110.intf.common;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.msg.MsgDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IMsgInnerServiceSMO
 * @Description 消息接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "common-service", configuration = {FeignConfiguration.class})
@RequestMapping("/msgApi")
public interface IMsgInnerServiceSMO {

    /**
     * <p>查询小区楼信息</p>
     *
     * @param msgDto 数据对象分享
     * @return MsgDto 对象数据
     */
    @RequestMapping(value = "/queryMsgs", method = RequestMethod.POST)
    List<MsgDto> queryMsgs(@RequestBody MsgDto msgDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param msgDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryMsgsCount", method = RequestMethod.POST)
    int queryMsgsCount(@RequestBody MsgDto msgDto);
}
