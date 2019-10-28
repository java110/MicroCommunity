package com.java110.core.smo.audit;

import com.java110.core.feign.FeignConfiguration;
import com.java110.dto.auditMessage.AuditMessageDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IAuditMessageInnerServiceSMO
 * @Description 审核原因接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "common-service", configuration = {FeignConfiguration.class})
@RequestMapping("/auditMessageApi")
public interface IAuditMessageInnerServiceSMO {

    /**
     * <p>查询小区楼信息</p>
     *
     *
     * @param auditMessageDto 数据对象分享
     * @return AuditMessageDto 对象数据
     */
    @RequestMapping(value = "/queryAuditMessages", method = RequestMethod.POST)
    List<AuditMessageDto> queryAuditMessages(@RequestBody AuditMessageDto auditMessageDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param auditMessageDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryAuditMessagesCount", method = RequestMethod.POST)
    int queryAuditMessagesCount(@RequestBody AuditMessageDto auditMessageDto);
}
