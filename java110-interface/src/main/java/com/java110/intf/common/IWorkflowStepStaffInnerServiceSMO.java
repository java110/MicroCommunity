package com.java110.intf.common;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.workflow.WorkflowStepStaffDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IWorkflowStepStaffInnerServiceSMO
 * @Description 工作流节点接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "common-service", configuration = {FeignConfiguration.class})
@RequestMapping("/workflowStepStaffApi")
public interface IWorkflowStepStaffInnerServiceSMO {

    /**
     * <p>查询小区楼信息</p>
     *
     *
     * @param workflowStepStaffDto 数据对象分享
     * @return WorkflowStepStaffDto 对象数据
     */
    @RequestMapping(value = "/queryWorkflowStepStaffs", method = RequestMethod.POST)
    List<WorkflowStepStaffDto> queryWorkflowStepStaffs(@RequestBody WorkflowStepStaffDto workflowStepStaffDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param workflowStepStaffDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryWorkflowStepStaffsCount", method = RequestMethod.POST)
    int queryWorkflowStepStaffsCount(@RequestBody WorkflowStepStaffDto workflowStepStaffDto);
}
