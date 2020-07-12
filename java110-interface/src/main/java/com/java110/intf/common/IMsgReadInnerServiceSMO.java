package com.java110.intf.common;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.msg.MsgReadDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IMsgReadInnerServiceSMO
 * @Description 消息阅读接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "common-service", configuration = {FeignConfiguration.class})
@RequestMapping("/msgReadApi")
public interface IMsgReadInnerServiceSMO {

    /**
     * <p>查询小区楼信息</p>
     *
     * @param msgReadDto 数据对象分享
     * @return MsgReadDto 对象数据
     */
    @RequestMapping(value = "/queryMsgReads", method = RequestMethod.POST)
    List<MsgReadDto> queryMsgReads(@RequestBody MsgReadDto msgReadDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param msgReadDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryMsgReadsCount", method = RequestMethod.POST)
    int queryMsgReadsCount(@RequestBody MsgReadDto msgReadDto);
}
