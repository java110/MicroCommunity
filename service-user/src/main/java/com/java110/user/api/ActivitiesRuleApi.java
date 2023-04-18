package com.java110.user.api;

import com.alibaba.fastjson.JSONObject;
import com.java110.dto.activities.ActivitiesBeautifulStaffDto;
import com.java110.dto.activities.ActivitiesRuleDto;
import com.java110.dto.questionAnswer.QuestionAnswerDto;
import com.java110.po.activitiesBeautifulStaff.ActivitiesBeautifulStaffPo;
import com.java110.po.activitiesRule.ActivitiesRulePo;
import com.java110.user.bmo.activitiesBeautifulStaff.IDeleteActivitiesBeautifulStaffBMO;
import com.java110.user.bmo.activitiesBeautifulStaff.IGetActivitiesBeautifulStaffBMO;
import com.java110.user.bmo.activitiesBeautifulStaff.ISaveActivitiesBeautifulStaffBMO;
import com.java110.user.bmo.activitiesBeautifulStaff.IUpdateActivitiesBeautifulStaffBMO;
import com.java110.user.bmo.activitiesRule.IDeleteActivitiesRuleBMO;
import com.java110.user.bmo.activitiesRule.IGetActivitiesRuleBMO;
import com.java110.user.bmo.activitiesRule.ISaveActivitiesRuleBMO;
import com.java110.user.bmo.activitiesRule.IUpdateActivitiesRuleBMO;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    @Autowired
    private ISaveActivitiesBeautifulStaffBMO saveActivitiesBeautifulStaffBMOImpl;

    @Autowired
    private IUpdateActivitiesBeautifulStaffBMO updateActivitiesBeautifulStaffBMOImpl;

    @Autowired
    private IDeleteActivitiesBeautifulStaffBMO deleteActivitiesBeautifulStaffBMOImpl;

    @Autowired
    private IGetActivitiesBeautifulStaffBMO getActivitiesBeautifulStaffBMOImpl;


    /**
     * 微信保存消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /activitiesRule/saveActivitiesRule
     * @path /app/activitiesRule/saveActivitiesRule
     */
    @RequestMapping(value = "/saveActivitiesRule", method = RequestMethod.POST)
    public ResponseEntity<String> saveActivitiesRule(
            @RequestHeader(value = "store-id", required = false) String storeId,
            @RequestBody JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "ruleName", "请求报文中未包含ruleName");
        Assert.hasKeyAndValue(reqJson, "ruleType", "请求报文中未包含ruleType");
        Assert.hasKeyAndValue(reqJson, "startTime", "请求报文中未包含startTime");
        Assert.hasKeyAndValue(reqJson, "endTime", "请求报文中未包含endTime");
        Assert.hasKeyAndValue(reqJson, "activitiesObj", "请求报文中未包含activitiesObj");
        Assert.hasKeyAndValue(reqJson, "remark", "请求报文中未包含remark");
        String activitiesObj = reqJson.getString("activitiesObj");
        ActivitiesRulePo activitiesRulePo = BeanConvertUtil.covertBean(reqJson, ActivitiesRulePo.class);
        if ("4444".equals(activitiesObj)) {
            activitiesRulePo.setObjType(QuestionAnswerDto.QA_TYPE_STORE);
            activitiesRulePo.setObjId(storeId);
        } else {
            Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含小区信息");
            activitiesRulePo.setObjType(QuestionAnswerDto.QA_TYPE_COMMUNITY);
            activitiesRulePo.setObjId(reqJson.getString("communityId"));
        }
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
    public ResponseEntity<String> queryActivitiesRule(@RequestHeader(value = "store-id", required = false) String storeId,
                                                      @RequestParam(value = "ruleType", required = false) String ruleType,
                                                      @RequestParam(value = "ruleName", required = false) String ruleName,
                                                      @RequestParam(value = "activitiesObj", required = false) String activitiesObj,
                                                      @RequestParam(value = "communityId") String communityId,
                                                      @RequestParam(value = "page") int page,
                                                      @RequestParam(value = "row") int row) {
        ActivitiesRuleDto activitiesRuleDto = new ActivitiesRuleDto();
        activitiesRuleDto.setRuleType(ruleType);
        activitiesRuleDto.setRuleName(ruleName);
        activitiesRuleDto.setActivitiesObj(activitiesObj);
        activitiesRuleDto.setPage(page);
        activitiesRuleDto.setRow(row);
        activitiesRuleDto.setObjIds(new String[]{storeId, communityId});
        return getActivitiesRuleBMOImpl.get(activitiesRuleDto);
    }


    /**
     * 微信保存消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /activitiesRule/saveActivitiesBeautifulStaff
     * @path /app/activitiesRule/saveActivitiesBeautifulStaff
     */
    @RequestMapping(value = "/saveActivitiesBeautifulStaff", method = RequestMethod.POST)
    public ResponseEntity<String> saveActivitiesBeautifulStaff(
            @RequestHeader(value = "store-id") String storeId,
            @RequestBody JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "staffId", "请求报文中未包含staffId");
        Assert.hasKeyAndValue(reqJson, "activitiesNum", "请求报文中未包含activitiesNum");
        Assert.hasKeyAndValue(reqJson, "workContent", "请求报文中未包含workContent");
        Assert.hasKeyAndValue(reqJson, "ruleId", "请求报文中未包含ruleId");
        ActivitiesBeautifulStaffPo activitiesBeautifulStaffPo = BeanConvertUtil.covertBean(reqJson, ActivitiesBeautifulStaffPo.class);
        activitiesBeautifulStaffPo.setStoreId(storeId);
        return saveActivitiesBeautifulStaffBMOImpl.save(activitiesBeautifulStaffPo);
    }

    /**
     * 微信修改消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /activitiesRule/updateActivitiesBeautifulStaff
     * @path /app/activitiesRule/updateActivitiesBeautifulStaff
     */
    @RequestMapping(value = "/updateActivitiesBeautifulStaff", method = RequestMethod.POST)
    public ResponseEntity<String> updateActivitiesBeautifulStaff(@RequestBody JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "staffId", "请求报文中未包含staffId");
        Assert.hasKeyAndValue(reqJson, "activitiesNum", "请求报文中未包含activitiesNum");
        Assert.hasKeyAndValue(reqJson, "workContent", "请求报文中未包含workContent");
        Assert.hasKeyAndValue(reqJson, "ruleId", "请求报文中未包含ruleId");
        Assert.hasKeyAndValue(reqJson, "beId", "beId不能为空");
        ActivitiesBeautifulStaffPo activitiesBeautifulStaffPo = BeanConvertUtil.covertBean(reqJson, ActivitiesBeautifulStaffPo.class);
        return updateActivitiesBeautifulStaffBMOImpl.update(activitiesBeautifulStaffPo);
    }

    /**
     * 微信删除消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /activitiesRule/deleteActivitiesBeautifulStaff
     * @path /app/activitiesRule/deleteActivitiesBeautifulStaff
     */
    @RequestMapping(value = "/deleteActivitiesBeautifulStaff", method = RequestMethod.POST)
    public ResponseEntity<String> deleteActivitiesBeautifulStaff(@RequestBody JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "communityId", "小区ID不能为空");
        Assert.hasKeyAndValue(reqJson, "beId", "beId不能为空");
        ActivitiesBeautifulStaffPo activitiesBeautifulStaffPo = BeanConvertUtil.covertBean(reqJson, ActivitiesBeautifulStaffPo.class);
        return deleteActivitiesBeautifulStaffBMOImpl.delete(activitiesBeautifulStaffPo);
    }

    /**
     * 微信删除消息模板
     *
     * @param storeId 小区ID
     * @return
     * @serviceCode /activitiesRule/queryActivitiesBeautifulStaff
     * @path /app/activitiesRule/queryActivitiesBeautifulStaff
     */
    @RequestMapping(value = "/queryActivitiesBeautifulStaff", method = RequestMethod.GET)
    public ResponseEntity<String> queryActivitiesBeautifulStaff(@RequestHeader(value = "store-id", required = false) String storeId,
                                                                @RequestParam(value = "ruleId", required = false) String ruleId,
                                                                @RequestParam(value = "activitiesNum", required = false) String activitiesNum,
                                                                @RequestParam(value = "staffName", required = false) String staffName,
                                                                @RequestParam(value = "page") int page,
                                                                @RequestParam(value = "row") int row) {
        ActivitiesBeautifulStaffDto activitiesBeautifulStaffDto = new ActivitiesBeautifulStaffDto();
        activitiesBeautifulStaffDto.setStaffName(staffName);
        activitiesBeautifulStaffDto.setRuleId(ruleId);
        activitiesBeautifulStaffDto.setActivitiesNum(activitiesNum);
        activitiesBeautifulStaffDto.setPage(page);
        activitiesBeautifulStaffDto.setRow(row);
        activitiesBeautifulStaffDto.setStoreId(storeId);
        return getActivitiesBeautifulStaffBMOImpl.get(activitiesBeautifulStaffDto);
    }
}
