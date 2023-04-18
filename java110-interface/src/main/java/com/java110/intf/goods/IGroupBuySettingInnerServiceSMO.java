package com.java110.intf.goods;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.groupBuy.GroupBuySettingDto;
import com.java110.po.groupBuySetting.GroupBuySettingPo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IGroupBuySettingInnerServiceSMO
 * @Description 拼团设置接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "goods-service", configuration = {FeignConfiguration.class})
@RequestMapping("/groupBuySettingApi")
public interface IGroupBuySettingInnerServiceSMO {


    @RequestMapping(value = "/saveGroupBuySetting", method = RequestMethod.POST)
    public int saveGroupBuySetting(@RequestBody GroupBuySettingPo groupBuySettingPo);

    @RequestMapping(value = "/updateGroupBuySetting", method = RequestMethod.POST)
    public int updateGroupBuySetting(@RequestBody GroupBuySettingPo groupBuySettingPo);

    @RequestMapping(value = "/deleteGroupBuySetting", method = RequestMethod.POST)
    public int deleteGroupBuySetting(@RequestBody GroupBuySettingPo groupBuySettingPo);

    /**
     * <p>查询小区楼信息</p>
     *
     * @param groupBuySettingDto 数据对象分享
     * @return GroupBuySettingDto 对象数据
     */
    @RequestMapping(value = "/queryGroupBuySettings", method = RequestMethod.POST)
    List<GroupBuySettingDto> queryGroupBuySettings(@RequestBody GroupBuySettingDto groupBuySettingDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param groupBuySettingDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryGroupBuySettingsCount", method = RequestMethod.POST)
    int queryGroupBuySettingsCount(@RequestBody GroupBuySettingDto groupBuySettingDto);
}
