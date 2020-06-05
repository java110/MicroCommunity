package com.java110.core.smo.common;

import com.java110.core.feign.FeignConfiguration;
import com.java110.dto.auditMessage.AuditMessageDto;
import com.java110.dto.purchaseApply.PurchaseApplyDto;
import com.java110.entity.audit.AuditUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient(name = "common-service", configuration = {FeignConfiguration.class})
@RequestMapping("/activitiWorkflowImageApi")
public interface IActivitiWorkflowImageInnerServiceSMO {

    /**
     * 启动流程
     *
     * @return
     */
    @RequestMapping(value = "/getWorkflowImage", method = RequestMethod.POST)
    public String getWorkflowImage(@RequestBody String taskId);



}
