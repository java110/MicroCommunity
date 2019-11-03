package com.java110.core.smo.complaint;

import com.java110.core.feign.FeignConfiguration;
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


    @RequestMapping(value = "/agreeCompleteTask", method = RequestMethod.POST)
    public boolean agreeCompleteTask(@RequestBody ComplaintDto complaintDto);

    @RequestMapping(value = "/refuteCompleteTask", method = RequestMethod.POST)
    public boolean refuteCompleteTask(@RequestBody ComplaintDto complaintDto);

    /**
     * 审核 当前任务
     *
     * @param complaintDto 资源订单
     * @return
     */
    @RequestMapping(value = "/complete", method = RequestMethod.POST)
    public boolean complete(@RequestBody ComplaintDto complaintDto);
}
