package com.java110.intf.acct;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.systemGoldSetting.SystemGoldSettingDto;
import com.java110.po.systemGoldSetting.SystemGoldSettingPo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName ISystemGoldSettingInnerServiceSMO
 * @Description 金币设置接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "acct-service", configuration = {FeignConfiguration.class})
@RequestMapping("/systemGoldSettingApi")
public interface ISystemGoldSettingInnerServiceSMO {


    @RequestMapping(value = "/saveSystemGoldSetting", method = RequestMethod.POST)
    public int saveSystemGoldSetting(@RequestBody SystemGoldSettingPo systemGoldSettingPo);

    @RequestMapping(value = "/updateSystemGoldSetting", method = RequestMethod.POST)
    public int updateSystemGoldSetting(@RequestBody  SystemGoldSettingPo systemGoldSettingPo);

    @RequestMapping(value = "/deleteSystemGoldSetting", method = RequestMethod.POST)
    public int deleteSystemGoldSetting(@RequestBody  SystemGoldSettingPo systemGoldSettingPo);

    /**
     * <p>查询小区楼信息</p>
     *
     *
     * @param systemGoldSettingDto 数据对象分享
     * @return SystemGoldSettingDto 对象数据
     */
    @RequestMapping(value = "/querySystemGoldSettings", method = RequestMethod.POST)
    List<SystemGoldSettingDto> querySystemGoldSettings(@RequestBody SystemGoldSettingDto systemGoldSettingDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param systemGoldSettingDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/querySystemGoldSettingsCount", method = RequestMethod.POST)
    int querySystemGoldSettingsCount(@RequestBody SystemGoldSettingDto systemGoldSettingDto);
}
