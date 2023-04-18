package com.java110.oa.bmo.oaWorkflowForm;

import com.alibaba.fastjson.JSONObject;
import com.java110.dto.oaWorkflow.OaWorkflowFormDto;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface IGetOaWorkflowFormBMO {


    /**
     * 查询OA表单
     * add by wuxw
     *
     * @param oaWorkflowFormDto
     * @return
     */
    ResponseEntity<String> get(OaWorkflowFormDto oaWorkflowFormDto);


    ResponseEntity<String> queryOaWorkflowFormData(Map paramIn);

    /**
     * 保存表单数据
     *
     * @param reqJson
     * @return
     */
    ResponseEntity<String> saveOaWorkflowFormData(JSONObject reqJson);

    /**
     * 查询工作流待办
     *
     * @param paramIn
     * @return
     */
    ResponseEntity<String> queryOaWorkflowUserTaskFormData(JSONObject paramIn);

    /**
     * 查询工作流程已办
     *
     * @param paramIn
     * @return
     */
    ResponseEntity<String> queryOaWorkflowUserHisTaskFormData(JSONObject paramIn);

    /**
     * 审核 工作流
     *
     * @param reqJson
     * @return
     */
    ResponseEntity<String> auditOaWorkflow(JSONObject reqJson);

    /**
     * 查询 下一任务数
     * @param reqJson
     * @return
     */
    ResponseEntity<String> getNextTask(JSONObject reqJson);

    /**
     * 查询审批流程
     * @param paramIn
     * @return
     */
    ResponseEntity<String> queryOaWorkflowUser(JSONObject paramIn);
}
