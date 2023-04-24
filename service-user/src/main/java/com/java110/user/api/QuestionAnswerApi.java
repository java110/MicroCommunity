package com.java110.user.api;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.dto.questionAnswer.QuestionAnswerDto;
import com.java110.dto.questionAnswer.QuestionAnswerTitleDto;
import com.java110.dto.questionAnswer.QuestionAnswerTitleValueDto;
import com.java110.po.questionAnswer.QuestionAnswerPo;
import com.java110.po.questionAnswerTitle.QuestionAnswerTitlePo;
import com.java110.po.questionAnswerTitleValue.QuestionAnswerTitleValuePo;
import com.java110.user.bmo.questionAnswer.IDeleteQuestionAnswerBMO;
import com.java110.user.bmo.questionAnswer.IGetQuestionAnswerBMO;
import com.java110.user.bmo.questionAnswer.ISaveQuestionAnswerBMO;
import com.java110.user.bmo.questionAnswer.IUpdateQuestionAnswerBMO;
import com.java110.user.bmo.questionAnswerTitle.IDeleteQuestionAnswerTitleBMO;
import com.java110.user.bmo.questionAnswerTitle.IGetQuestionAnswerTitleBMO;
import com.java110.user.bmo.questionAnswerTitle.ISaveQuestionAnswerTitleBMO;
import com.java110.user.bmo.questionAnswerTitle.IUpdateQuestionAnswerTitleBMO;
import com.java110.user.bmo.questionAnswerTitleValue.IDeleteQuestionAnswerTitleValueBMO;
import com.java110.user.bmo.questionAnswerTitleValue.IGetQuestionAnswerTitleValueBMO;
import com.java110.user.bmo.questionAnswerTitleValue.ISaveQuestionAnswerTitleValueBMO;
import com.java110.user.bmo.questionAnswerTitleValue.IUpdateQuestionAnswerTitleValueBMO;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    @Autowired
    private ISaveQuestionAnswerTitleBMO saveQuestionAnswerTitleBMOImpl;

    @Autowired
    private IUpdateQuestionAnswerTitleBMO updateQuestionAnswerTitleBMOImpl;

    @Autowired
    private IDeleteQuestionAnswerTitleBMO deleteQuestionAnswerTitleBMOImpl;

    @Autowired
    private IGetQuestionAnswerTitleBMO getQuestionAnswerTitleBMOImpl;

    @Autowired
    private ISaveQuestionAnswerTitleValueBMO saveQuestionAnswerTitleValueBMOImpl;

    @Autowired
    private IUpdateQuestionAnswerTitleValueBMO updateQuestionAnswerTitleValueBMOImpl;

    @Autowired
    private IDeleteQuestionAnswerTitleValueBMO deleteQuestionAnswerTitleValueBMOImpl;

    @Autowired
    private IGetQuestionAnswerTitleValueBMO getQuestionAnswerTitleValueBMOImpl;

    /**
     * 微信保存消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /questionAnswer/saveQuestionAnswer
     * @path /app/questionAnswer/saveQuestionAnswer
     */
    @RequestMapping(value = "/saveQuestionAnswer", method = RequestMethod.POST)
    public ResponseEntity<String> saveQuestionAnswer(@RequestHeader(value = "store-id", required = false) String storeId,
                                                     @RequestBody JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "qaType", "请求报文中未包含qaType");
        Assert.hasKeyAndValue(reqJson, "qaName", "请求报文中未包含qaName");
        Assert.hasKeyAndValue(reqJson, "startTime", "请求报文中未包含startTime");
        Assert.hasKeyAndValue(reqJson, "endTime", "请求报文中未包含endTime");
        Assert.hasKeyAndValue(reqJson, "objType", "请求报文中未包含objType");

        if (QuestionAnswerDto.QA_TYPE_STORE.equals(reqJson.getString("objType"))) {
            reqJson.put("objId", storeId);
        }
        Assert.hasKeyAndValue(reqJson, "objId", "请求报文中未包含objId");

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
    public ResponseEntity<String> queryQuestionAnswer(@RequestHeader(value = "store-id", required = false) String storeId,
                                                      @RequestHeader(value = "user-id", required = false) String userId,
                                                      @RequestParam(value = "communityId", required = false) String communityId,
                                                      @RequestParam(value = "objType", required = false) String objType,
                                                      @RequestParam(value = "qaType", required = false) String qaType,
                                                      @RequestParam(value = "qaName", required = false) String qaName,
                                                      @RequestParam(value = "qaId", required = false) String qaId,
                                                      @RequestParam(value = "page") int page,
                                                      @RequestParam(value = "row") int row) {
        QuestionAnswerDto questionAnswerDto = new QuestionAnswerDto();
        questionAnswerDto.setPage(page);
        questionAnswerDto.setRow(row);
        questionAnswerDto.setUserId(userId);
        questionAnswerDto.setQaType(qaType);
        questionAnswerDto.setQaName(qaName);
        questionAnswerDto.setQaId(qaId);
        if (!StringUtil.isEmpty(objType)) {
            questionAnswerDto.setObjType(objType);
            questionAnswerDto.setObjId(QuestionAnswerDto.QA_TYPE_COMMUNITY.equals(objType) ? communityId : storeId);
        } else {
            questionAnswerDto.setObjIds(new String[]{storeId, communityId});
        }
        return getQuestionAnswerBMOImpl.get(questionAnswerDto);
    }

    /**
     * 微信保存消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /questionAnswer/saveQuestionAnswerTitle
     * @path /app/questionAnswer/saveQuestionAnswerTitle
     */
    @RequestMapping(value = "/saveQuestionAnswerTitle", method = RequestMethod.POST)
    public ResponseEntity<String> saveQuestionAnswerTitle(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "qaId", "请求报文中未包含qaId");
        Assert.hasKeyAndValue(reqJson, "qaTitle", "请求报文中未包含qaTitle");
        Assert.hasKeyAndValue(reqJson, "titleType", "请求报文中未包含titleType");
        Assert.hasKeyAndValue(reqJson, "objId", "请求报文中未包含objId");
        Assert.hasKeyAndValue(reqJson, "objType", "请求报文中未包含objType");
        Assert.hasKeyAndValue(reqJson, "seq", "请求报文中未包含seq");

        JSONArray titleValues = null;
        if (!QuestionAnswerTitleDto.TITLE_TYPE_QUESTIONS.equals(reqJson.getString("titleType"))) {
            titleValues = reqJson.getJSONArray("titleValues");

            if (titleValues.size() < 1) {
                throw new IllegalArgumentException("未包含选项");
            }
        }
        QuestionAnswerTitlePo questionAnswerTitlePo = BeanConvertUtil.covertBean(reqJson, QuestionAnswerTitlePo.class);
        return saveQuestionAnswerTitleBMOImpl.save(questionAnswerTitlePo, titleValues);
    }

    /**
     * 微信修改消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /questionAnswer/updateQuestionAnswerTitle
     * @path /app/questionAnswer/updateQuestionAnswerTitle
     */
    @RequestMapping(value = "/updateQuestionAnswerTitle", method = RequestMethod.POST)
    public ResponseEntity<String> updateQuestionAnswerTitle(@RequestBody JSONObject reqJson) {
//        Assert.hasKeyAndValue(reqJson, "qaId", "请求报文中未包含qaId");
        Assert.hasKeyAndValue(reqJson, "qaTitle", "请求报文中未包含qaTitle");
        Assert.hasKeyAndValue(reqJson, "titleType", "请求报文中未包含titleType");
        Assert.hasKeyAndValue(reqJson, "objType", "请求报文中未包含objType");
        Assert.hasKeyAndValue(reqJson, "seq", "请求报文中未包含seq");
        Assert.hasKeyAndValue(reqJson, "titleId", "titleId不能为空");
        JSONArray titleValues = null;
        if (!QuestionAnswerTitleDto.TITLE_TYPE_QUESTIONS.equals(reqJson.getString("titleType"))) {
            titleValues = reqJson.getJSONArray("titleValues");

            if (titleValues.size() < 1) {
                throw new IllegalArgumentException("未包含选项");
            }
        }
        QuestionAnswerTitlePo questionAnswerTitlePo = BeanConvertUtil.covertBean(reqJson, QuestionAnswerTitlePo.class);
        return updateQuestionAnswerTitleBMOImpl.update(questionAnswerTitlePo, titleValues);
    }

    /**
     * 微信删除消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /questionAnswer/deleteQuestionAnswerTitle
     * @path /app/questionAnswer/deleteQuestionAnswerTitle
     */
    @RequestMapping(value = "/deleteQuestionAnswerTitle", method = RequestMethod.POST)
    public ResponseEntity<String> deleteQuestionAnswerTitle(@RequestBody JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "communityId", "小区ID不能为空");

        Assert.hasKeyAndValue(reqJson, "titleId", "titleId不能为空");

        QuestionAnswerTitlePo questionAnswerTitlePo = BeanConvertUtil.covertBean(reqJson, QuestionAnswerTitlePo.class);
        return deleteQuestionAnswerTitleBMOImpl.delete(questionAnswerTitlePo);
    }

    /**
     * 微信删除消息模板
     *
     * @param communityId 小区ID
     * @return
     * @serviceCode /questionAnswer/queryQuestionAnswerTitle
     * @path /app/questionAnswer/queryQuestionAnswerTitle
     */
    @RequestMapping(value = "/queryQuestionAnswerTitle", method = RequestMethod.GET)
    public ResponseEntity<String> queryQuestionAnswerTitle(@RequestHeader(value = "store-id", required = false) String storeId,
                                                           // @RequestHeader(value = "user-id", required = false) String userId,
                                                           @RequestParam(value = "titleType", required = false) String titleType,
                                                           @RequestParam(value = "qaTitle", required = false) String qaTitle,
                                                           @RequestParam(value = "titleId", required = false) String titleId,
                                                           @RequestParam(value = "communityId", required = false) String communityId,
                                                           @RequestParam(value = "objType") String objType,
                                                           @RequestParam(value = "qaId") String qaId,
                                                           @RequestParam(value = "objId") String objId,
                                                           @RequestParam(value = "page") int page,
                                                           @RequestParam(value = "row") int row) {
        QuestionAnswerTitleDto questionAnswerTitleDto = new QuestionAnswerTitleDto();
        questionAnswerTitleDto.setTitleType(titleType);
        questionAnswerTitleDto.setQaTitle(qaTitle);
        questionAnswerTitleDto.setTitleId(titleId);
        questionAnswerTitleDto.setPage(page);
        questionAnswerTitleDto.setRow(row);
        questionAnswerTitleDto.setQaId(qaId);
        questionAnswerTitleDto.setObjId(objId);
        //questionAnswerTitleDto.setUserId(userId);
        if (!StringUtil.isEmpty(objType)) {
            questionAnswerTitleDto.setObjType(objType);
            questionAnswerTitleDto.setObjId(QuestionAnswerDto.QA_TYPE_COMMUNITY.equals(objType) ? communityId : storeId);
        } else {
            questionAnswerTitleDto.setObjIds(new String[]{storeId, communityId});
        }
        return getQuestionAnswerTitleBMOImpl.get(questionAnswerTitleDto);
    }

    /**
     * 微信保存消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /questionAnswer/saveQuestionAnswerTitleValue
     * @path /app/questionAnswer/saveQuestionAnswerTitleValue
     */
    @RequestMapping(value = "/saveQuestionAnswerTitleValue", method = RequestMethod.POST)
    public ResponseEntity<String> saveQuestionAnswerTitleValue(@RequestBody JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "titleId", "请求报文中未包含titleId");
        Assert.hasKeyAndValue(reqJson, "qaValue", "请求报文中未包含qaValue");
        Assert.hasKeyAndValue(reqJson, "objType", "请求报文中未包含objType");
        Assert.hasKeyAndValue(reqJson, "seq", "请求报文中未包含seq");
        QuestionAnswerTitleValuePo questionAnswerTitleValuePo = BeanConvertUtil.covertBean(reqJson, QuestionAnswerTitleValuePo.class);
        return saveQuestionAnswerTitleValueBMOImpl.save(questionAnswerTitleValuePo);
    }

    /**
     * 微信修改消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /questionAnswer/updateQuestionAnswerTitleValue
     * @path /app/questionAnswer/updateQuestionAnswerTitleValue
     */
    @RequestMapping(value = "/updateQuestionAnswerTitleValue", method = RequestMethod.POST)
    public ResponseEntity<String> updateQuestionAnswerTitleValue(@RequestBody JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "titleId", "请求报文中未包含titleId");
        Assert.hasKeyAndValue(reqJson, "qaValue", "请求报文中未包含qaValue");
        Assert.hasKeyAndValue(reqJson, "objType", "请求报文中未包含objType");
        Assert.hasKeyAndValue(reqJson, "seq", "请求报文中未包含seq");
        Assert.hasKeyAndValue(reqJson, "valueId", "valueId不能为空");
        QuestionAnswerTitleValuePo questionAnswerTitleValuePo = BeanConvertUtil.covertBean(reqJson, QuestionAnswerTitleValuePo.class);
        return updateQuestionAnswerTitleValueBMOImpl.update(questionAnswerTitleValuePo);
    }

    /**
     * 微信删除消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /questionAnswer/deleteQuestionAnswerTitleValue
     * @path /app/questionAnswer/deleteQuestionAnswerTitleValue
     */
    @RequestMapping(value = "/deleteQuestionAnswerTitleValue", method = RequestMethod.POST)
    public ResponseEntity<String> deleteQuestionAnswerTitleValue(@RequestBody JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "communityId", "小区ID不能为空");

        Assert.hasKeyAndValue(reqJson, "valueId", "valueId不能为空");

        QuestionAnswerTitleValuePo questionAnswerTitleValuePo = BeanConvertUtil.covertBean(reqJson, QuestionAnswerTitleValuePo.class);
        return deleteQuestionAnswerTitleValueBMOImpl.delete(questionAnswerTitleValuePo);
    }

    /**
     * 微信删除消息模板
     *
     * @param communityId 小区ID
     * @return
     * @serviceCode /questionAnswer/queryQuestionAnswerTitleValue
     * @path /app/questionAnswer/queryQuestionAnswerTitleValue
     */
    @RequestMapping(value = "/queryQuestionAnswerTitleValue", method = RequestMethod.GET)
    public ResponseEntity<String> queryQuestionAnswerTitleValue(@RequestHeader(value = "store-id") String storeId,
                                                                @RequestParam(value = "communityId", required = false) String communityId,
                                                                @RequestParam(value = "objType") String objType,
                                                                @RequestParam(value = "page") int page,
                                                                @RequestParam(value = "row") int row) {
        QuestionAnswerTitleValueDto questionAnswerTitleValueDto = new QuestionAnswerTitleValueDto();
        questionAnswerTitleValueDto.setPage(page);
        questionAnswerTitleValueDto.setRow(row);
        questionAnswerTitleValueDto.setObjType(objType);
        questionAnswerTitleValueDto.setObjId(QuestionAnswerDto.QA_TYPE_COMMUNITY.equals(objType) ? communityId : storeId);
        return getQuestionAnswerTitleValueBMOImpl.get(questionAnswerTitleValueDto);
    }


    /**
     * 微信删除消息模板
     *
     * @param communityId 小区ID
     * @return
     * @serviceCode /questionAnswer/queryTitleValueResult
     * @path /app/questionAnswer/queryTitleValueResult
     */
    @RequestMapping(value = "/queryTitleValueResult", method = RequestMethod.GET)
    public ResponseEntity<String> queryTitleValueResult(@RequestHeader(value = "store-id") String storeId,
                                                        @RequestParam(value = "communityId", required = false) String communityId,
                                                        @RequestParam(value = "objType") String objType,
                                                        @RequestParam(value = "titleId", required = false) String titleId,
                                                        @RequestParam(value = "page") int page,
                                                        @RequestParam(value = "row") int row) {
        QuestionAnswerTitleValueDto questionAnswerTitleValueDto = new QuestionAnswerTitleValueDto();
        questionAnswerTitleValueDto.setPage(page);
        questionAnswerTitleValueDto.setRow(row);
        questionAnswerTitleValueDto.setObjType(objType);
        questionAnswerTitleValueDto.setTitleId(titleId);
        questionAnswerTitleValueDto.setObjId(QuestionAnswerDto.QA_TYPE_COMMUNITY.equals(objType) ? communityId : storeId);
        return getQuestionAnswerTitleValueBMOImpl.queryQuestionAnswerTitleValueResult(questionAnswerTitleValueDto);
    }
}
