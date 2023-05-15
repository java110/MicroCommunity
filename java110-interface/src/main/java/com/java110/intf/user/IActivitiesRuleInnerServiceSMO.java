package com.java110.intf.user;

import com.java110.config.feign.FeignConfiguration;
import com.java110.dto.activities.ActivitiesRuleDto;
import com.java110.po.activitiesRule.ActivitiesRulePo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IActivitiesRuleInnerServiceSMO
 * @Description 活动规则接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "user-service", configuration = {FeignConfiguration.class})
@RequestMapping("/activitiesRuleApi")
public interface IActivitiesRuleInnerServiceSMO {


    @RequestMapping(value = "/saveActivitiesRule", method = RequestMethod.POST)
    public int saveActivitiesRule(@RequestBody ActivitiesRulePo activitiesRulePo);

    @RequestMapping(value = "/updateActivitiesRule", method = RequestMethod.POST)
    public int updateActivitiesRule(@RequestBody  ActivitiesRulePo activitiesRulePo);

    @RequestMapping(value = "/deleteActivitiesRule", method = RequestMethod.POST)
    public int deleteActivitiesRule(@RequestBody  ActivitiesRulePo activitiesRulePo);

    /**
     * <p>查询小区楼信息</p>
     *
     *
     * @param activitiesRuleDto 数据对象分享
     * @return ActivitiesRuleDto 对象数据
     */
    @RequestMapping(value = "/queryActivitiesRules", method = RequestMethod.POST)
    List<ActivitiesRuleDto> queryActivitiesRules(@RequestBody ActivitiesRuleDto activitiesRuleDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param activitiesRuleDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryActivitiesRulesCount", method = RequestMethod.POST)
    int queryActivitiesRulesCount(@RequestBody ActivitiesRuleDto activitiesRuleDto);
}
