package com.java110.intf.common;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.workflow.WorkflowAuditInfoDto;
import com.java110.dto.workflow.WorkflowDto;
import com.java110.dto.workflow.WorkflowModelDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IWorkflowInnerServiceSMO
 * @Description 工作流接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "common-service", configuration = {FeignConfiguration.class})
@RequestMapping("/workflowApi")
public interface IWorkflowInnerServiceSMO {

    /**
     * <p>查询小区楼信息</p>
     *
     * @param workflowDto 数据对象分享
     * @return WorkflowDto 对象数据
     */
    @RequestMapping(value = "/queryWorkflows", method = RequestMethod.POST)
    List<WorkflowDto> queryWorkflows(@RequestBody WorkflowDto workflowDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param workflowDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryWorkflowsCount", method = RequestMethod.POST)
    int queryWorkflowsCount(@RequestBody WorkflowDto workflowDto);

    @RequestMapping(value = "/getWorkflowImage", method = RequestMethod.POST)
    String getWorkflowImage(@RequestBody WorkflowDto workflowDto);

    /**
     * @Date：2017/11/24
     * @Description：创建流程并部署
     */
    @RequestMapping(value = "/addFlowDeployment", method = RequestMethod.POST)
    WorkflowDto addFlowDeployment(@RequestBody WorkflowDto workflowDto);

    @RequestMapping(value = "/getRunWorkflowImage", method = RequestMethod.POST)
    String getRunWorkflowImage(@RequestBody String businessKey);

    /**
     * 查询审核历史
     *
     * @param workflowAuditInfoDto
     * @return
     */
    @RequestMapping(value = "/queryWorkflowAuditHistory", method = RequestMethod.POST)
    List<WorkflowAuditInfoDto> queryWorkflowAuditHistory(@RequestBody WorkflowAuditInfoDto workflowAuditInfoDto);

    /**
     * 创建模型
     * @param workflowModelDto
     * @return
     */
    @RequestMapping(value = "/createModel", method = RequestMethod.POST)
    WorkflowModelDto createModel(@RequestBody WorkflowModelDto workflowModelDto);


}
