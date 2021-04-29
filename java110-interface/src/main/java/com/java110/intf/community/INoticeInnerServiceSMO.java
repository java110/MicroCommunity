package com.java110.intf.community;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.notice.NoticeDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName INoticeInnerServiceSMO
 * @Description 通知接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "community-service", configuration = {FeignConfiguration.class})
@RequestMapping("/noticeApi")
public interface INoticeInnerServiceSMO {

    /**
     * <p>查询小区楼信息</p>
     *
     *
     * @param noticeDto 数据对象分享
     * @return NoticeDto 对象数据
     */
    @RequestMapping(value = "/queryNotices", method = RequestMethod.POST)
    List<NoticeDto> queryNotices(@RequestBody NoticeDto noticeDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param noticeDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryNoticesCount", method = RequestMethod.POST)
    int queryNoticesCount(@RequestBody NoticeDto noticeDto);

    @RequestMapping(value = "/updateNotice", method = RequestMethod.POST)
    int updateNotice(@RequestBody NoticeDto noticeDto);
}
