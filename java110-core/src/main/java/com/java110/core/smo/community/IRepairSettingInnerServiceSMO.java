package com.java110.core.smo.repairSetting;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.repair.RepairSettingDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IRepairSettingInnerServiceSMO
 * @Description 报修设置接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "community-service", configuration = {FeignConfiguration.class})
@RequestMapping("/repairSettingApi")
public interface IRepairSettingInnerServiceSMO {

    /**
     * <p>查询小区楼信息</p>
     *
     *
     * @param repairSettingDto 数据对象分享
     * @return RepairSettingDto 对象数据
     */
    @RequestMapping(value = "/queryRepairSettings", method = RequestMethod.POST)
    List<RepairSettingDto> queryRepairSettings(@RequestBody RepairSettingDto repairSettingDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param repairSettingDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryRepairSettingsCount", method = RequestMethod.POST)
    int queryRepairSettingsCount(@RequestBody RepairSettingDto repairSettingDto);
}
