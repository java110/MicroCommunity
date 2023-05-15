package com.java110.intf.common;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.auditMessage.AuditMessageDto;
import com.java110.dto.complaint.ComplaintDto;
import com.java110.entity.audit.AuditUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient(name = "common-service", configuration = {FeignConfiguration.class})
@RequestMapping("/complaintUserApi")
public interface IComplaintUserInnerServiceSMO {

    /**
     * 启动流程
     *
     * @return
     */
    @RequestMapping(value = "/startProcess", method = RequestMethod.POST)
    public ComplaintDto startProcess(@RequestBody ComplaintDto complaintDto);

    /**
     * 查询用户任务数
     *
     * @param user
     * @return
     */
    @RequestMapping(value = "/getUserTaskCount", method = RequestMethod.POST)
    public long getUserTaskCount(@RequestBody AuditUser user);

    /**
     * 获取用户任务
     *
     * @param user 用户信息
     */
    @RequestMapping(value = "/getUserTasks", method = RequestMethod.POST)
    public List<ComplaintDto> getUserTasks(@RequestBody AuditUser user);

    /**
     * 查询用户处理任务数
     *
     * @param user
     * @return
     */
    @RequestMapping(value = "/getUserHistoryTaskCount", method = RequestMethod.POST)
    public long getUserHistoryTaskCount(@RequestBody AuditUser user);
    /**
     * 获取用户处理审批的任务
     *
     * @param user 用户信息
     */
    @RequestMapping(value = "/getUserHistoryTasks", method = RequestMethod.POST)
    public List<ComplaintDto> getUserHistoryTasks(@RequestBody AuditUser user);


    /**
     * 处理任务
     * @param complaintDto
     * @return true 为流程结束 false 为流程没有结束
     */
    @RequestMapping(value = "/completeTask", method = RequestMethod.POST)
    public boolean completeTask(@RequestBody ComplaintDto complaintDto);

    /**
     * 查询批注信息
     * @param complaintDto
     * @return
     */
    @RequestMapping(value = "/getAuditMessage", method = RequestMethod.POST)
    public List<AuditMessageDto> getAuditMessage(@RequestBody ComplaintDto complaintDto);

    /**
     * 获取任务当前处理人
     * @param complaintDto
     * @return
     */
    @RequestMapping(value = "/getTaskCurrentUser", method = RequestMethod.POST)
    public ComplaintDto getTaskCurrentUser(@RequestBody ComplaintDto complaintDto);

    @RequestMapping(value = "/deleteTask", method = RequestMethod.POST)
    public boolean deleteTask(@RequestBody ComplaintDto complaintDto);

}
