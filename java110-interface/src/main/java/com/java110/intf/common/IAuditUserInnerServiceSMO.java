package com.java110.intf.common;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.auditUser.AuditUserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IAuditUserInnerServiceSMO
 * @Description 审核人员接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "common-service", configuration = {FeignConfiguration.class})
@RequestMapping("/auditUserApi")
public interface IAuditUserInnerServiceSMO {

    /**
     * <p>查询小区楼信息</p>
     *
     * @param auditUserDto 数据对象分享
     * @return AuditUserDto 对象数据
     */
    @RequestMapping(value = "/queryAuditUsers", method = RequestMethod.POST)
    List<AuditUserDto> queryAuditUsers(@RequestBody AuditUserDto auditUserDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param auditUserDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryAuditUsersCount", method = RequestMethod.POST)
    int queryAuditUsersCount(@RequestBody AuditUserDto auditUserDto);
}
