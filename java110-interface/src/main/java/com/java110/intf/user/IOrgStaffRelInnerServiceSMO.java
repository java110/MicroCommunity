package com.java110.intf.user;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.org.OrgStaffRelDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IOrgStaffRelInnerServiceSMO
 * @Description 组织员工关系接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "user-service", configuration = {FeignConfiguration.class})
@RequestMapping("/orgStaffRelApi")
public interface IOrgStaffRelInnerServiceSMO {

    /**
     * <p>查询小区楼信息</p>
     *
     * @param orgStaffRelDto 数据对象分享
     * @return OrgStaffRelDto 对象数据
     */
    @RequestMapping(value = "/queryOrgStaffRels", method = RequestMethod.POST)
    List<OrgStaffRelDto> queryOrgStaffRels(@RequestBody OrgStaffRelDto orgStaffRelDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param orgStaffRelDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryOrgStaffRelsCount", method = RequestMethod.POST)
    int queryOrgStaffRelsCount(@RequestBody OrgStaffRelDto orgStaffRelDto);

    /**
     * <p>查询部门公司信息</p>
     *
     * @param orgStaffRelDto 员工ids
     * @return OrgStaffRelDto 对象数据
     */
    @RequestMapping(value = "/queryOrgInfoByStaffIds", method = RequestMethod.POST)
    List<OrgStaffRelDto> queryOrgInfoByStaffIds(@RequestBody OrgStaffRelDto orgStaffRelDto);
    /**
     * <p>查询部门公司信息</p>
     *
     * @param orgStaffRelDto 员工ids
     * @return OrgStaffRelDto 对象数据
     */
    @RequestMapping(value = "/queryOrgInfoByStaffIdsNew", method = RequestMethod.POST)
    List<OrgStaffRelDto> queryOrgInfoByStaffIdsNew(@RequestBody OrgStaffRelDto orgStaffRelDto);
}
