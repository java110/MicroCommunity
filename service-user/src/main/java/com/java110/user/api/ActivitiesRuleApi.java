package com.java110.user.api;

import com.alibaba.fastjson.JSONObject;
import com.java110.dto.activitiesRule.ActivitiesRuleDto;
import com.java110.po.activitiesRule.ActivitiesRulePo;
import com.java110.user.bmo.activitiesRule.IDeleteActivitiesRuleBMO;
import com.java110.user.bmo.activitiesRule.IGetActivitiesRuleBMO;
import com.java110.user.bmo.activitiesRule.ISaveActivitiesRuleBMO;
import com.java110.user.bmo.activitiesRule.IUpdateActivitiesRuleBMO;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/activitiesRule")
public class ActivitiesRuleApi {

    @Autowired
    private ISaveActivitiesRuleBMO saveActivitiesRuleBMOImpl;
    @Autowired
    private IUpdateActivitiesRuleBMO updateActivitiesRuleBMOImpl;
    @Autowired
    private IDeleteActivitiesRuleBMO deleteActivitiesRuleBMOImpl;

    @Autowired
    private IGetActivitiesRuleBMO getActivitiesRuleBMOImpl;

    /**
     * 微信保存消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /activitiesRule/saveActivitiesRule
     * @path /app/activitiesRule/saveActivitiesRule
     */
    @RequestMapping(value = "/saveActivitiesRule", method = RequestMethod.POST)
    public ResponseEntity<String> saveActivitiesRule(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "ruleName", "请求报文中未包含ruleName");
        Assert.hasKeyAndValue(reqJson, "ruleType", "请求报文中未包含ruleType");
        Assert.hasKeyAndValue(reqJson, "startTime", "请求报文中未包含startTime");
        Assert.hasKeyAndValue(reqJson, "endTime", "请求报文中未包含endTime");
        Assert.hasKeyAndValue(reqJson, "activitiesObj", "请求报文中未包含activitiesObj");
        Assert.hasKeyAndValue(reqJson, "remark", "请求报文中未包含remark");


        ActivitiesRulePo activitiesRulePo = BeanConvertUtil.covertBean(reqJson, ActivitiesRulePo.class);
        return saveActivitiesRuleBMOImpl.save(activitiesRulePo);
    }

    /**
     * 微信修改消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /activitiesRule/updateActivitiesRule
     * @path /app/activitiesRule/updateActivitiesRule
     */
    @RequestMapping(value = "/updateActivitiesRule", method = RequestMethod.POST)
    public ResponseEntity<String> updateActivitiesRule(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "ruleName", "请求报文中未包含ruleName");
        Assert.hasKeyAndValue(reqJson, "ruleType", "请求报文中未包含ruleType");
        Assert.hasKeyAndValue(reqJson, "startTime", "请求报文中未包含startTime");
        Assert.hasKeyAndValue(reqJson, "endTime", "请求报文中未包含endTime");
        Assert.hasKeyAndValue(reqJson, "activitiesObj", "请求报文中未包含activitiesObj");
        Assert.hasKeyAndValue(reqJson, "remark", "请求报文中未包含remark");
        Assert.hasKeyAndValue(reqJson, "ruleId", "ruleId不能为空");


        ActivitiesRulePo activitiesRulePo = BeanConvertUtil.covertBean(reqJson, ActivitiesRulePo.class);
        return updateActivitiesRuleBMOImpl.update(activitiesRulePo);
    }

    /**
     * 微信删除消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /activitiesRule/deleteActivitiesRule
     * @path /app/activitiesRule/deleteActivitiesRule
     */
    @RequestMapping(value = "/deleteActivitiesRule", method = RequestMethod.POST)
    public ResponseEntity<String> deleteActivitiesRule(@RequestBody JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "communityId", "小区ID不能为空");

        Assert.hasKeyAndValue(reqJson, "ruleId", "ruleId不能为空");


        ActivitiesRulePo activitiesRulePo = BeanConvertUtil.covertBean(reqJson, ActivitiesRulePo.class);
        return deleteActivitiesRuleBMOImpl.delete(activitiesRulePo);
    }

    /**
     * 微信删除消息模板
     *
     * @param communityId 小区ID
     * @return
     * @serviceCode /activitiesRule/queryActivitiesRule
     * @path /app/activitiesRule/queryActivitiesRule
     */
    @RequestMapping(value = "/queryActivitiesRule", method = RequestMethod.GET)
    public ResponseEntity<String> queryActivitiesRule(@RequestParam(value = "communityId") String communityId,
                                                      @RequestParam(value = "page") int page,
                                                      @RequestParam(value = "row") int row) {
        ActivitiesRuleDto activitiesRuleDto = new ActivitiesRuleDto();
        activitiesRuleDto.setPage(page);
        activitiesRuleDto.setRow(row);
        activitiesRuleDto.setObjId(communityId);
        return getActivitiesRuleBMOImpl.get(activitiesRuleDto);
    }
}
