package com.java110.intf.common.workflow;


import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.user.StaffDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "common-service", configuration = {FeignConfiguration.class})
@RequestMapping("/workflow")
public interface IWorkflowApi {

    /**
     * 查询 工作流 第一个处理人
     *
     * @param flowType 工作流类型
     * @param communityId 小区ID
     * @return
     */
    @RequestMapping(value = "/getFirstStaff", method = RequestMethod.POST)
    StaffDto getFirstStaff(@RequestParam(name = "flowType") String flowType,
                           @RequestParam(name = "communityId") String communityId);
}
