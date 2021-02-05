package com.java110.user.api;

import com.alibaba.fastjson.JSONObject;
import com.java110.dto.questionAnswer.QuestionAnswerDto;
import com.java110.po.questionAnswer.QuestionAnswerPo;
import com.java110.user.bmo.questionAnswer.IDeleteQuestionAnswerBMO;
import com.java110.user.bmo.questionAnswer.IGetQuestionAnswerBMO;
import com.java110.user.bmo.questionAnswer.ISaveQuestionAnswerBMO;
import com.java110.user.bmo.questionAnswer.IUpdateQuestionAnswerBMO;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/questionAnswer")
public class QuestionAnswerApi {

    @Autowired
    private ISaveQuestionAnswerBMO saveQuestionAnswerBMOImpl;
    @Autowired
    private IUpdateQuestionAnswerBMO updateQuestionAnswerBMOImpl;
    @Autowired
    private IDeleteQuestionAnswerBMO deleteQuestionAnswerBMOImpl;

    @Autowired
    private IGetQuestionAnswerBMO getQuestionAnswerBMOImpl;

    /**
     * 微信保存消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /questionAnswer/saveQuestionAnswer
     * @path /app/questionAnswer/saveQuestionAnswer
     */
    @RequestMapping(value = "/saveQuestionAnswer", method = RequestMethod.POST)
    public ResponseEntity<String> saveQuestionAnswer(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "qaType", "请求报文中未包含qaType");
        Assert.hasKeyAndValue(reqJson, "qaName", "请求报文中未包含qaName");
        Assert.hasKeyAndValue(reqJson, "startTime", "请求报文中未包含startTime");
        Assert.hasKeyAndValue(reqJson, "endTime", "请求报文中未包含endTime");
        Assert.hasKeyAndValue(reqJson, "objType", "请求报文中未包含objType");


        QuestionAnswerPo questionAnswerPo = BeanConvertUtil.covertBean(reqJson, QuestionAnswerPo.class);
        return saveQuestionAnswerBMOImpl.save(questionAnswerPo);
    }

    /**
     * 微信修改消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /questionAnswer/updateQuestionAnswer
     * @path /app/questionAnswer/updateQuestionAnswer
     */
    @RequestMapping(value = "/updateQuestionAnswer", method = RequestMethod.POST)
    public ResponseEntity<String> updateQuestionAnswer(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "qaType", "请求报文中未包含qaType");
        Assert.hasKeyAndValue(reqJson, "qaName", "请求报文中未包含qaName");
        Assert.hasKeyAndValue(reqJson, "startTime", "请求报文中未包含startTime");
        Assert.hasKeyAndValue(reqJson, "endTime", "请求报文中未包含endTime");
        Assert.hasKeyAndValue(reqJson, "objType", "请求报文中未包含objType");
        Assert.hasKeyAndValue(reqJson, "qaId", "qaId不能为空");


        QuestionAnswerPo questionAnswerPo = BeanConvertUtil.covertBean(reqJson, QuestionAnswerPo.class);
        return updateQuestionAnswerBMOImpl.update(questionAnswerPo);
    }

    /**
     * 微信删除消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /questionAnswer/deleteQuestionAnswer
     * @path /app/questionAnswer/deleteQuestionAnswer
     */
    @RequestMapping(value = "/deleteQuestionAnswer", method = RequestMethod.POST)
    public ResponseEntity<String> deleteQuestionAnswer(@RequestBody JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "communityId", "小区ID不能为空");

        Assert.hasKeyAndValue(reqJson, "qaId", "qaId不能为空");


        QuestionAnswerPo questionAnswerPo = BeanConvertUtil.covertBean(reqJson, QuestionAnswerPo.class);
        return deleteQuestionAnswerBMOImpl.delete(questionAnswerPo);
    }

    /**
     * 微信删除消息模板
     *
     * @param communityId 小区ID
     * @return
     * @serviceCode /questionAnswer/queryQuestionAnswer
     * @path /app/questionAnswer/queryQuestionAnswer
     */
    @RequestMapping(value = "/queryQuestionAnswer", method = RequestMethod.GET)
    public ResponseEntity<String> queryQuestionAnswer(
            @RequestHeader(value = "store-id") String storeId,
            @RequestParam(value = "communityId", required = false) String communityId,
            @RequestParam(value = "objType") String objType,
            @RequestParam(value = "page") int page,
            @RequestParam(value = "row") int row) {
        QuestionAnswerDto questionAnswerDto = new QuestionAnswerDto();
        questionAnswerDto.setPage(page);
        questionAnswerDto.setRow(row);
        questionAnswerDto.setObjType(objType);
        questionAnswerDto.setObjTd(QuestionAnswerDto.QA_TYPE_COMMUNITY.endsWith(objType) ? communityId : storeId);
        return getQuestionAnswerBMOImpl.get(questionAnswerDto);
    }
}
