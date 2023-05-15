package com.java110.intf.common;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.auditMessage.AuditMessageDto;
import com.java110.dto.contract.ContractDto;
import com.java110.dto.purchaseApply.PurchaseApplyDto;
import com.java110.entity.audit.AuditUser;
import com.java110.po.contract.ContractPo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient(name = "common-service", configuration = {FeignConfiguration.class})
@RequestMapping("/contractApplyUserApi")
public interface IContractApplyUserInnerServiceSMO {


    /**
     * <p>启动流程</p>
     *
     *
     * @return CommunityDto 对象数据
     */
    @RequestMapping(value = "/startProcess", method = RequestMethod.POST)
    public ContractDto startProcess(@RequestBody ContractDto contractDto);


    /**
     * 查询用户任务数
     * @param user
     * @return
     */
    @RequestMapping(value = "/getUserTaskCount", method = RequestMethod.POST)
    public long getUserTaskCount(@RequestBody AuditUser user);

    /**
     *  获取用户任务
     * @param user 用户信息
     */
    @RequestMapping(value = "/getUserTasks", method = RequestMethod.POST)
    public List<ContractDto> getUserTasks(@RequestBody AuditUser user);

    /**
     * 同意
     * @param contractDto
     * @return
     */
    @RequestMapping(value = "/agreeCompleteTask", method = RequestMethod.POST)
    public boolean agreeCompleteTask(@RequestBody ContractDto contractDto);


    /**
     * 反驳
     * @param contractDto
     * @return
     */
    @RequestMapping(value = "/refuteCompleteTask", method = RequestMethod.POST)
    public boolean refuteCompleteTask(@RequestBody ContractDto contractDto);

    /**
     * 完成任务
     * @param contractDto
     */
    @RequestMapping(value = "/complete", method = RequestMethod.GET)
    public boolean complete(@RequestBody ContractDto contractDto);

    /**
     * 查询用户任务数
     *
     * @param user
     * @return
     */
    @RequestMapping(value = "/getUserHistoryTaskCount", method = RequestMethod.POST)
    public long getUserHistoryTaskCount(@RequestBody AuditUser user);

    /**
     * 获取用户审批的任务
     *
     * @param user 用户信息
     */
    @RequestMapping(value = "/getUserHistoryTasks", method = RequestMethod.POST)
    public List<ContractDto> getUserHistoryTasks(@RequestBody AuditUser user);

    /**
     * 处理任务
     * @return true 为流程结束 false 为流程没有结束
     */
    @RequestMapping(value = "/completeTask", method = RequestMethod.POST)
    public boolean completeTask(@RequestBody ContractDto contractDto);
    /**
     * 处理任务
     * @return true 为流程结束 false 为流程没有结束
     */
    @RequestMapping(value = "/deleteTask", method = RequestMethod.POST)
    public boolean deleteTask(@RequestBody ContractPo contractDto);

}
