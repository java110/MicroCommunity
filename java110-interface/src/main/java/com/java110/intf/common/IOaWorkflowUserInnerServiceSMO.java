package com.java110.intf.common;

import com.alibaba.fastjson.JSONObject;
import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.auditMessage.AuditMessageDto;
import com.java110.entity.audit.AuditUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;


@FeignClient(name = "${java110.common-service}", configuration = {FeignConfiguration.class})
@RequestMapping("/oaWorkflowUserApi")
public interface IOaWorkflowUserInnerServiceSMO {

    /**
     * 启动流程
     *
     * @return
     */
    @RequestMapping(value = "/startProcess", method = RequestMethod.POST)
    JSONObject startProcess(@RequestBody JSONObject reqJson);

    /**
     * 查询用户任务数
     *
     * @param user
     * @return
     */
    @RequestMapping(value = "/getUserTaskCount", method = RequestMethod.POST)
    long getUserTaskCount(@RequestBody AuditUser user);

    /**
     * 获取用户任务
     *
     * @param user 用户信息
     */
    @RequestMapping(value = "/getUserTasks", method = RequestMethod.POST)
    List<JSONObject> getUserTasks(@RequestBody AuditUser user);

    /**
     * 查询用户处理任务数
     *
     * @param user
     * @return
     */
    @RequestMapping(value = "/getUserHistoryTaskCount", method = RequestMethod.POST)
    long getUserHistoryTaskCount(@RequestBody AuditUser user);

    /**
     * 获取用户处理审批的任务
     *
     * @param user 用户信息
     */
    @RequestMapping(value = "/getUserHistoryTasks", method = RequestMethod.POST)
    List<JSONObject> getUserHistoryTasks(@RequestBody AuditUser user);


    /**
     * 处理任务
     *
     * @param reqJson
     * @return true 为流程结束 false 为流程没有结束
     */
    @RequestMapping(value = "/completeTask", method = RequestMethod.POST)
    boolean completeTask(@RequestBody JSONObject reqJson);

    /**
     * 处理任务
     *
     * @param reqJson
     * @return true 为流程结束 false 为流程没有结束
     */
    @RequestMapping(value = "/changeTaskToOtherUser", method = RequestMethod.POST)
    boolean changeTaskToOtherUser(@RequestBody JSONObject reqJson);

    /**
     * 处理任务
     *
     * @param reqJson
     * @return true 为流程结束 false 为流程没有结束
     */
    @RequestMapping(value = "/goBackTask", method = RequestMethod.POST)
    boolean goBackTask(@RequestBody JSONObject reqJson);


    /**
     * 查询批注信息
     *
     * @param reqJson
     * @return
     */
    @RequestMapping(value = "/getAuditMessage", method = RequestMethod.POST)
    List<AuditMessageDto> getAuditMessage(@RequestBody JSONObject reqJson);

    /**
     * 获取任务当前处理人
     *
     * @param reqJson
     * @return
     */
    @RequestMapping(value = "/getTaskCurrentUser", method = RequestMethod.POST)
    JSONObject getTaskCurrentUser(@RequestBody JSONObject reqJson);

    @RequestMapping(value = "/nextAllNodeTaskList", method = RequestMethod.POST)
    List<JSONObject> nextAllNodeTaskList(@RequestBody JSONObject reqJson);
}
