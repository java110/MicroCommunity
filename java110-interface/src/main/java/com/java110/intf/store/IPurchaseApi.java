package com.java110.intf.store;


import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.user.StaffDto;
import com.java110.dto.workflow.WorkflowDto;
import com.java110.vo.api.staff.ApiStaffDataVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 采购申请类
 */
@FeignClient(name = "store-service", configuration = {FeignConfiguration.class})
@RequestMapping("/purchase")
public interface IPurchaseApi {

}
