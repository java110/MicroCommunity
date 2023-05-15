package com.java110.intf.common;

import com.alibaba.fastjson.JSONObject;
import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.auditMessage.AuditMessageDto;
import com.java110.dto.oaWorkflow.OaWorkflowXmlDto;
import com.java110.entity.audit.AuditUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;


@FeignClient(name = "common-service", configuration = {FeignConfiguration.class})
@RequestMapping("/oaWorkflowActivitiApi")
public interface IOaWorkflowActivitiInnerServiceSMO {

    /**
     * 启动流程
     *
     * @param reqJson {
     *                createUserId:'',
     *                flowId:'',
     *                id:'',
     *                auditMessage:'',
     *                storeId:''
     * }
     *
     * @return
     */
    @RequestMapping(value = "/startProcess", method = RequestMethod.POST)
    JSONObject startProcess(@RequestBody JSONObject reqJson);

    /**
     *
     * @param reqJson {
     *                createUserId:'',
     *                processInstanceId:'',
     *                nextUserId:'',
     *                auditCode:'',
     *                storeId:'',
     *                id:'',
     *                flowId:''
     * }
     * @return
     */
    @RequestMapping(value = "/autoFinishFirstTask", method = RequestMethod.POST)
    boolean autoFinishFirstTask(@RequestBody JSONObject reqJson);

    /**
     * 查询用户任务数
     *
     * @param user {
     *             flowId:'',
     *             userId:''
     * }
     * @return
     */
    @RequestMapping(value = "/getUserTaskCount", method = RequestMethod.POST)
    long getUserTaskCount(@RequestBody AuditUser user);

    /**
     * 获取用户任务
     *
     * @param user 用户信息 {
     *             flowId:'',
     *             userId:''
     * }
     */
    @RequestMapping(value = "/getUserTasks", method = RequestMethod.POST)
    List<JSONObject> getUserTasks(@RequestBody AuditUser user);

    /**
     * 查询用户处理任务数
     *
     * @param user {
     *             flowId:'',
     *             userId:''
     * }
     * @return
     */
    @RequestMapping(value = "/getUserHistoryTaskCount", method = RequestMethod.POST)
    long getUserHistoryTaskCount(@RequestBody AuditUser user);

    /**
     * 获取用户处理审批的任务
     *
     * @param user 用户信息{
     *            flowId:'',
     *            userId:''
     *}
     */
    @RequestMapping(value = "/getUserHistoryTasks", method = RequestMethod.POST)
    List<JSONObject> getUserHistoryTasks(@RequestBody AuditUser user);

    /**
     * 查询用户任务数
     *
     * @param user{
     *            userId:'',
     *            processDefinitionkeys:[]
     *}
     * @return
     */
    @RequestMapping(value = "/getDefinitionKeysUserTaskCount", method = RequestMethod.POST)
     long getDefinitionKeysUserTaskCount(@RequestBody AuditUser user) ;

    /**
     * 获取用户任务
     *
     * @param user 用户信息{
     *             userId:'',
     *             page:1,
     *             row:10,
     *             processDefinitionkeys:[]
     *}
     */
    @RequestMapping(value = "/getDefinitionKeysUserTasks", method = RequestMethod.POST)
    List<JSONObject> getDefinitionKeysUserTasks(@RequestBody AuditUser user) ;

    /**
     * 查询用户任务数
     *
     * @param user{
     *              userId:'',
     *              processDefinitionkeys:[]
     * }
     * @return
     */
    @RequestMapping(value = "/getDefinitionKeysUserHistoryTaskCount", method = RequestMethod.POST)
    long getDefinitionKeysUserHistoryTaskCount(@RequestBody AuditUser user) ;

    /**
     * 获取用户审批的任务
     *
     * @param user 用户信息{
     *              userId:'',
     *             page:1,
     *             row:10,
     *              processDefinitionkeys:[]
     * }
     */
    @RequestMapping(value = "/getDefinitionKeysUserHistoryTasks", method = RequestMethod.POST)
    List<JSONObject> getDefinitionKeysUserHistoryTasks(@RequestBody AuditUser user) ;


    /**
     * 处理任务
     *
     * @param reqJson {
     *               taskId:'',
     *               nextUserId:'',
     *               auditMessage:'',
     *               auditCode:'',
     *               id:'',
     *               storeId:'',
     *               flowId:''
     * }
     * @return true 为流程结束 false 为流程没有结束
     */
    @RequestMapping(value = "/completeTask", method = RequestMethod.POST)
    boolean completeTask(@RequestBody JSONObject reqJson);

    /**
     * 处理任务
     *
     * @param reqJson {
     *                storeId:'',
     *                id:'',
     *                nextUserId:'',
     *                taskId:'',
     *                auditMessage:'',
     *                flowId:'',
     *                storeId:'',
     *
     * }
     * @return true 为流程结束 false 为流程没有结束
     */
    @RequestMapping(value = "/changeTaskToOtherUser", method = RequestMethod.POST)
    boolean changeTaskToOtherUser(@RequestBody JSONObject reqJson);

    /**
     * 处理任务
     *
     * @param reqJson {
     *                taskId:'',
     *                nextUserId:'',
     *                auditMessage:'',
     *                auditCode:'',
     * }
     * @return true 为流程结束 false 为流程没有结束
     */
    @RequestMapping(value = "/goBackTask", method = RequestMethod.POST)
    boolean goBackTask(@RequestBody JSONObject reqJson);


    /**
     * 查询批注信息
     *
     * @param reqJson {
     *                taskId:'',
     *
     * }
     * @return
     */
    @RequestMapping(value = "/getAuditMessage", method = RequestMethod.POST)
    List<AuditMessageDto> getAuditMessage(@RequestBody JSONObject reqJson);

    /**
     * 获取任务当前处理人
     *
     * @param reqJson{
     *               id:'',
     *
     * }
     * @return
     */
    @RequestMapping(value = "/getTaskCurrentUser", method = RequestMethod.POST)
    JSONObject getTaskCurrentUser(@RequestBody JSONObject reqJson);

    /**
     *
     * @param reqJson{
     *               taskId:'',
     *               startUserId:''
     * }
     * @return
     */
    @RequestMapping(value = "/nextAllNodeTaskList", method = RequestMethod.POST)
    List<JSONObject> nextAllNodeTaskList(@RequestBody JSONObject reqJson);

    /**
     *
     * @param oaWorkflowXmlDto {
     *                         bpmnXml:''
     * }
     * @return
     */
    @RequestMapping(value = "/queryFirstAuditStaff", method = RequestMethod.POST)
    List<JSONObject> queryFirstAuditStaff(@RequestBody OaWorkflowXmlDto oaWorkflowXmlDto);
}
