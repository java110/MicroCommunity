package com.java110.intf.user.org;


import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.org.OrgDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "user-service", configuration = {FeignConfiguration.class})
@RequestMapping("/org")
public interface IOrgApi {

    /**
     * 查询员工组织信息
     *
     * @param orgId
     * @return
     */
    @RequestMapping(value = "/queryOrgs", method = RequestMethod.GET)
    List<OrgDto> queryOrgs(@RequestParam(value = "orgId", required = false) String orgId,
                           @RequestParam(value = "staffId", required = false) String staffId);


}
