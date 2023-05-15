package com.java110.intf.common;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.workflowAuditMessage.WorkflowAuditMessageDto;
import com.java110.po.workflowAuditMessage.WorkflowAuditMessagePo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IWorkflowAuditMessageInnerServiceSMO
 * @Description 流程审核表接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "common-service", configuration = {FeignConfiguration.class})
@RequestMapping("/workflowAuditMessageApi")
public interface IWorkflowAuditMessageInnerServiceSMO {


    @RequestMapping(value = "/saveWorkflowAuditMessage", method = RequestMethod.POST)
    public int saveWorkflowAuditMessage(@RequestBody WorkflowAuditMessagePo workflowAuditMessagePo);

    @RequestMapping(value = "/updateWorkflowAuditMessage", method = RequestMethod.POST)
    public int updateWorkflowAuditMessage(@RequestBody  WorkflowAuditMessagePo workflowAuditMessagePo);

    @RequestMapping(value = "/deleteWorkflowAuditMessage", method = RequestMethod.POST)
    public int deleteWorkflowAuditMessage(@RequestBody  WorkflowAuditMessagePo workflowAuditMessagePo);

    /**
     * <p>查询小区楼信息</p>
     *
     *
     * @param workflowAuditMessageDto 数据对象分享
     * @return WorkflowAuditMessageDto 对象数据
     */
    @RequestMapping(value = "/queryWorkflowAuditMessages", method = RequestMethod.POST)
    List<WorkflowAuditMessageDto> queryWorkflowAuditMessages(@RequestBody WorkflowAuditMessageDto workflowAuditMessageDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param workflowAuditMessageDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryWorkflowAuditMessagesCount", method = RequestMethod.POST)
    int queryWorkflowAuditMessagesCount(@RequestBody WorkflowAuditMessageDto workflowAuditMessageDto);
}
