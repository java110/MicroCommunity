package com.java110.user.api;

import com.alibaba.fastjson.JSONObject;
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
     * @param communityId 小区ID
     * @return
     * @serviceCode /userQuestionAnswer/queryUserQuestionAnswer
     * @path /app/userQuestionAnswer/queryUserQuestionAnswer
     */
    @RequestMapping(value = "/queryUserQuestionAnswer", method = RequestMethod.GET)
    public ResponseEntity<String> queryUserQuestionAnswer(@RequestParam(value = "communityId") String communityId,
                                                          @RequestParam(value = "page") int page,
                                                          @RequestParam(value = "row") int row) {
        UserQuestionAnswerDto userQuestionAnswerDto = new UserQuestionAnswerDto();
        userQuestionAnswerDto.setPage(page);
        userQuestionAnswerDto.setRow(row);
        userQuestionAnswerDto.setObjId(communityId);
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
    public ResponseEntity<String> saveUserQuestionAnswerValue(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "qaId", "请求报文中未包含qaId");


        UserQuestionAnswerValuePo userQuestionAnswerValuePo = BeanConvertUtil.covertBean(reqJson, UserQuestionAnswerValuePo.class);
        return saveUserQuestionAnswerValueBMOImpl.save(userQuestionAnswerValuePo);
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
