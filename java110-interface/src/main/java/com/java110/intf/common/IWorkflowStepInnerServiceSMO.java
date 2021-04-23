package com.java110.intf.common;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.workflow.WorkflowStepDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IWorkflowStepInnerServiceSMO
 * @Description 工作流节点接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "common-service", configuration = {FeignConfiguration.class})
@RequestMapping("/workflowStepApi")
public interface IWorkflowStepInnerServiceSMO {

    /**
     * <p>查询小区楼信息</p>
     *
     *
     * @param workflowStepDto 数据对象分享
     * @return WorkflowStepDto 对象数据
     */
    @RequestMapping(value = "/queryWorkflowSteps", method = RequestMethod.POST)
    List<WorkflowStepDto> queryWorkflowSteps(@RequestBody WorkflowStepDto workflowStepDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param workflowStepDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryWorkflowStepsCount", method = RequestMethod.POST)
    int queryWorkflowStepsCount(@RequestBody WorkflowStepDto workflowStepDto);
}
