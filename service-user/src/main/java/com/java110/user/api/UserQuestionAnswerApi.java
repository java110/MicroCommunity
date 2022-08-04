package com.java110.user.api;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.dto.questionAnswer.QuestionAnswerDto;
import com.java110.dto.userQuestionAnswer.UserQuestionAnswerDto;
import com.java110.dto.userQuestionAnswerValue.UserQuestionAnswerValueDto;
import com.java110.po.userQuestionAnswer.UserQuestionAnswerPo;
import com.java110.po.userQuestionAnswerValue.UserQuestionAnswerValuePo;
import com.java110.user.bmo.userQuestionAnswer.IDeleteUserQuestionAnswerBMO;
import com.java110.user.bmo.userQuestionAnswer.IGetUserQuestionAnswerBMO;
import com.java110.user.bmo.userQuestionAnswer.ISaveUserQuestionAnswerBMO;
import com.java110.user.bmo.userQuestionAnswer.IUpdateUserQuestionAnswerBMO;
import com.java110.user.bmo.userQuestionAnswerValue.IDeleteUserQuestionAnswerValueBMO;
import com.java110.user.bmo.userQuestionAnswerValue.IGetUserQuestionAnswerValueBMO;
import com.java110.user.bmo.userQuestionAnswerValue.ISaveUserQuestionAnswerValueBMO;
import com.java110.user.bmo.userQuestionAnswerValue.IUpdateUserQuestionAnswerValueBMO;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/userQuestionAnswer")
public class UserQuestionAnswerApi {

    @Autowired
    private ISaveUserQuestionAnswerBMO saveUserQuestionAnswerBMOImpl;
    @Autowired
    private IUpdateUserQuestionAnswerBMO updateUserQuestionAnswerBMOImpl;
    @Autowired
    private IDeleteUserQuestionAnswerBMO deleteUserQuestionAnswerBMOImpl;

    @Autowired
    private IGetUserQuestionAnswerBMO getUserQuestionAnswerBMOImpl;

    @Autowired
    private ISaveUserQuestionAnswerValueBMO saveUserQuestionAnswerValueBMOImpl;
    @Autowired
    private IUpdateUserQuestionAnswerValueBMO updateUserQuestionAnswerValueBMOImpl;
    @Autowired
    private IDeleteUserQuestionAnswerValueBMO deleteUserQuestionAnswerValueBMOImpl;

    @Autowired
    private IGetUserQuestionAnswerValueBMO getUserQuestionAnswerValueBMOImpl;

    /**
     * 微信保存消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /userQuestionAnswer/saveUserQuestionAnswer
     * @path /app/userQuestionAnswer/saveUserQuestionAnswer
     */
    @RequestMapping(value = "/saveUserQuestionAnswer", method = RequestMethod.POST)
    public ResponseEntity<String> saveUserQuestionAnswer(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "qaId", "请求报文中未包含qaId");


        UserQuestionAnswerPo userQuestionAnswerPo = BeanConvertUtil.covertBean(reqJson, UserQuestionAnswerPo.class);
        return saveUserQuestionAnswerBMOImpl.save(userQuestionAnswerPo);
    }

    /**
     * 微信修改消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /userQuestionAnswer/updateUserQuestionAnswer
     * @path /app/userQuestionAnswer/updateUserQuestionAnswer
     */
    @RequestMapping(value = "/updateUserQuestionAnswer", method = RequestMethod.POST)
    public ResponseEntity<String> updateUserQuestionAnswer(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "qaId", "请求报文中未包含qaId");
        Assert.hasKeyAndValue(reqJson, "userQaId", "userQaId不能为空");


        UserQuestionAnswerPo userQuestionAnswerPo = BeanConvertUtil.covertBean(reqJson, UserQuestionAnswerPo.class);
        return updateUserQuestionAnswerBMOImpl.update(userQuestionAnswerPo);
    }

    /**
     * 微信删除消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /userQuestionAnswer/deleteUserQuestionAnswer
     * @path /app/userQuestionAnswer/deleteUserQuestionAnswer
     */
    @RequestMapping(value = "/deleteUserQuestionAnswer", method = RequestMethod.POST)
    public ResponseEntity<String> deleteUserQuestionAnswer(@RequestBody JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "communityId", "小区ID不能为空");

        Assert.hasKeyAndValue(reqJson, "userQaId", "userQaId不能为空");


        UserQuestionAnswerPo userQuestionAnswerPo = BeanConvertUtil.covertBean(reqJson, UserQuestionAnswerPo.class);
        return deleteUserQuestionAnswerBMOImpl.delete(userQuestionAnswerPo);
    }

    /**
     * 微信删除消息模板
     *
     * @param storeId 小区ID
     * @return
     * @serviceCode /userQuestionAnswer/queryUserQuestionAnswer
     * @path /app/userQuestionAnswer/queryUserQuestionAnswer
     */
    @RequestMapping(value = "/queryUserQuestionAnswer", method = RequestMethod.GET)
    public ResponseEntity<String> queryUserQuestionAnswer(@RequestHeader(value = "store-id") String storeId,
                                                          @RequestHeader(value = "user-id") String userId,
                                                          @RequestParam(value = "communityId", required = false) String communityId,
                                                          @RequestParam(value = "roleCd", required = false) String roleCd,
                                                          @RequestParam(value = "state", required = false) String state,
                                                          @RequestParam(value = "page") int page,
                                                          @RequestParam(value = "row") int row) {
        UserQuestionAnswerDto userQuestionAnswerDto = new UserQuestionAnswerDto();
        userQuestionAnswerDto.setPage(page);
        userQuestionAnswerDto.setRow(row);
        userQuestionAnswerDto.setState(state);
        if ("owner".equals(roleCd)) {
            userQuestionAnswerDto.setObjType(QuestionAnswerDto.QA_TYPE_COMMUNITY);
            userQuestionAnswerDto.setObjId(communityId);
            userQuestionAnswerDto.setPersonId(userId);
            userQuestionAnswerDto.setQaTypes(new String[]{"1001", "3003"});
        } else {
            userQuestionAnswerDto.setObjType(QuestionAnswerDto.QA_TYPE_STORE);
            userQuestionAnswerDto.setObjId(storeId);
            userQuestionAnswerDto.setPersonId(userId);
            userQuestionAnswerDto.setQaTypes(new String[]{"2002", "4004"});
        }
        return getUserQuestionAnswerBMOImpl.get(userQuestionAnswerDto);
    }

    /**
     * 微信保存消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /userQuestionAnswer/saveUserQuestionAnswerValue
     * @path /app/userQuestionAnswer/saveUserQuestionAnswerValue
     */
    @RequestMapping(value = "/saveUserQuestionAnswerValue", method = RequestMethod.POST)
    public ResponseEntity<String> saveUserQuestionAnswerValue(
            @RequestHeader(value = "user-id") String userId,
            @RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "qaId", "请求报文中未包含qaId");
        Assert.hasKeyAndValue(reqJson, "objType", "请求报文中未包含objType");
        Assert.hasKeyAndValue(reqJson, "objId", "请求报文中未包含objId");
        Assert.hasKeyAndValue(reqJson, "answerType", "请求报文中未包含answerType");
        Assert.hasKey(reqJson, "questionAnswerTitles", "未包含答案");

        JSONArray questionAnswerTitles = reqJson.getJSONArray("questionAnswerTitles");

        if (questionAnswerTitles == null || questionAnswerTitles.size() < 1) {
            throw new IllegalArgumentException("未包含答案");
        }

        JSONObject titleObj = null;
        for (int questionAnswerTitleIndex = 0; questionAnswerTitleIndex < questionAnswerTitles.size(); questionAnswerTitleIndex++) {
            titleObj = questionAnswerTitles.getJSONObject(questionAnswerTitleIndex);
            if (titleObj.containsKey("qaTitle") && !StringUtil.isEmpty(titleObj.getString("qaTitle"))) {
                Assert.hasKeyAndValue(titleObj, "valueContent", titleObj.getString("qaTitle") + ",未填写答案");
            } else {
                Assert.hasKeyAndValue(titleObj, "valueContent", "未填写答案");
            }
        }

        UserQuestionAnswerValuePo userQuestionAnswerValuePo = BeanConvertUtil.covertBean(reqJson, UserQuestionAnswerValuePo.class);

        userQuestionAnswerValuePo.setPersonId(userId);


        return saveUserQuestionAnswerValueBMOImpl.save(userQuestionAnswerValuePo, questionAnswerTitles);
    }

    /**
     * 微信修改消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /userQuestionAnswer/updateUserQuestionAnswerValue
     * @path /app/userQuestionAnswer/updateUserQuestionAnswerValue
     */
    @RequestMapping(value = "/updateUserQuestionAnswerValue", method = RequestMethod.POST)
    public ResponseEntity<String> updateUserQuestionAnswerValue(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "qaId", "请求报文中未包含qaId");
        Assert.hasKeyAndValue(reqJson, "userTitleId", "userTitleId不能为空");


        UserQuestionAnswerValuePo userQuestionAnswerValuePo = BeanConvertUtil.covertBean(reqJson, UserQuestionAnswerValuePo.class);
        return updateUserQuestionAnswerValueBMOImpl.update(userQuestionAnswerValuePo);
    }

    /**
     * 微信删除消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /userQuestionAnswer/deleteUserQuestionAnswerValue
     * @path /app/userQuestionAnswer/deleteUserQuestionAnswerValue
     */
    @RequestMapping(value = "/deleteUserQuestionAnswerValue", method = RequestMethod.POST)
    public ResponseEntity<String> deleteUserQuestionAnswerValue(@RequestBody JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "communityId", "小区ID不能为空");

        Assert.hasKeyAndValue(reqJson, "userTitleId", "userTitleId不能为空");


        UserQuestionAnswerValuePo userQuestionAnswerValuePo = BeanConvertUtil.covertBean(reqJson, UserQuestionAnswerValuePo.class);
        return deleteUserQuestionAnswerValueBMOImpl.delete(userQuestionAnswerValuePo);
    }

    /**
     * 微信删除消息模板
     *
     * @param communityId 小区ID
     * @return
     * @serviceCode /userQuestionAnswer/queryUserQuestionAnswerValue
     * @path /app/userQuestionAnswer/queryUserQuestionAnswerValue
     */
    @RequestMapping(value = "/queryUserQuestionAnswerValue", method = RequestMethod.GET)
    public ResponseEntity<String> queryUserQuestionAnswerValue(@RequestParam(value = "communityId") String communityId,
                                                               @RequestParam(value = "page") int page,
                                                               @RequestParam(value = "row") int row) {
        UserQuestionAnswerValueDto userQuestionAnswerValueDto = new UserQuestionAnswerValueDto();
        userQuestionAnswerValueDto.setPage(page);
        userQuestionAnswerValueDto.setRow(row);
        userQuestionAnswerValueDto.setObjId(communityId);
        return getUserQuestionAnswerValueBMOImpl.get(userQuestionAnswerValueDto);
    }
}
