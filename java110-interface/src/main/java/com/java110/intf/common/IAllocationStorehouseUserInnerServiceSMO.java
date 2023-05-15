package com.java110.intf.common;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.allocationStorehouse.AllocationStorehouseApplyDto;
import com.java110.entity.audit.AuditUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient(name = "common-service", configuration = {FeignConfiguration.class})
@RequestMapping("/allocationStorehouseUserApi")
public interface IAllocationStorehouseUserInnerServiceSMO {


    /**
     * <p>启动流程</p>
     *
     * @return CommunityDto 对象数据
     */
    @RequestMapping(value = "/startProcess", method = RequestMethod.POST)
    public AllocationStorehouseApplyDto startProcess(@RequestBody AllocationStorehouseApplyDto allocationStorehouseDto);


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
    public List<AllocationStorehouseApplyDto> getUserTasks(@RequestBody AuditUser user);

    /**
     * 同意
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/agreeCompleteTask", method = RequestMethod.POST)
    public boolean agreeCompleteTask(@RequestBody AllocationStorehouseApplyDto allocationStorehouseDto);


    /**
     * 反驳
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/refuteCompleteTask", method = RequestMethod.POST)
    public boolean refuteCompleteTask(@RequestBody AllocationStorehouseApplyDto allocationStorehouseDto);

    /**
     * 完成任务
     *
     * @param
     */
    @RequestMapping(value = "/complete", method = RequestMethod.GET)
    public boolean complete(@RequestBody AllocationStorehouseApplyDto allocationStorehouseDto);

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
    public List<AllocationStorehouseApplyDto> getUserHistoryTasks(@RequestBody AuditUser user);

    /**
     * 处理任务
     *
     * @return true 为流程结束 false 为流程没有结束
     */
    @RequestMapping(value = "/completeTask", method = RequestMethod.POST)
    public boolean completeTask(@RequestBody AllocationStorehouseApplyDto allocationStorehouseDto);


}
