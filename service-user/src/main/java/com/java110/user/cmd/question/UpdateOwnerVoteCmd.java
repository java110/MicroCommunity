package com.java110.user.cmd.question;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.questionAnswer.QuestionAnswerDto;
import com.java110.dto.questionAnswerTitleRel.QuestionAnswerTitleRelDto;
import com.java110.dto.questionTitle.QuestionTitleDto;
import com.java110.intf.user.*;
import com.java110.po.questionAnswer.QuestionAnswerPo;
import com.java110.po.questionAnswerTitleRel.QuestionAnswerTitleRelPo;
import com.java110.po.questionTitle.QuestionTitlePo;
import com.java110.po.questionTitleValue.QuestionTitleValuePo;
import com.java110.po.user.UserQuestionAnswerPo;
import com.java110.user.bmo.question.IQuestionAnswerBMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.util.List;

/**
 * 修改业主投票功能
 * add by wuxw 2023-07-12
 */

@Java110Cmd(serviceCode = "question.updateOwnerVote")
public class UpdateOwnerVoteCmd extends Cmd {

    public static final String CODE_PREFIX_ID = "10";

    @Autowired
    private IQuestionTitleV1InnerServiceSMO questionTitleV1InnerServiceSMOImpl;

    @Autowired
    private IQuestionTitleValueV1InnerServiceSMO questionTitleValueV1InnerServiceSMOImpl;
    @Autowired
    private IQuestionAnswerV1InnerServiceSMO questionAnswerV1InnerServiceSMOImpl;

    @Autowired
    private IQuestionAnswerTitleRelV1InnerServiceSMO questionAnswerTitleRelV1InnerServiceSMOImpl;

    @Autowired
    private IUserQuestionAnswerV1InnerServiceSMO userQuestionAnswerV1InnerServiceSMOImpl;

    @Autowired
    private IQuestionAnswerBMO questionAnswerBMOImpl;


    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {

        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含communityId");
        Assert.hasKeyAndValue(reqJson, "qaName", "请求报文中未包含投票名称");
        Assert.hasKeyAndValue(reqJson, "qaId", "请求报文中未包含投票名称");
        Assert.hasKeyAndValue(reqJson, "startTime", "未包含开始时间");
        Assert.hasKeyAndValue(reqJson, "endTime", "未包含结束时间");
        Assert.hasKeyAndValue(reqJson, "titleType", "未包含题目类型");
        Assert.hasKeyAndValue(reqJson, "content", "未包含说明");
        JSONArray titleValues = null;
        if (!QuestionTitleDto.TITLE_TYPE_QUESTIONS.equals(reqJson.getString("titleType"))) {
            titleValues = reqJson.getJSONArray("titleValues");
            if (titleValues.size() < 1) {
                throw new IllegalArgumentException("未包含选项");
            }
        }
    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {

        QuestionAnswerTitleRelDto questionAnswerTitleRelDto = new QuestionAnswerTitleRelDto();
        questionAnswerTitleRelDto.setQaId(reqJson.getString("qaId"));
        questionAnswerTitleRelDto.setCommunityId(reqJson.getString("communityId"));
        List<QuestionAnswerTitleRelDto> questionAnswerTitleRelDtos = questionAnswerTitleRelV1InnerServiceSMOImpl.queryQuestionAnswerTitleRels(questionAnswerTitleRelDto);
        Assert.listOnlyOne(questionAnswerTitleRelDtos, "投票没有题目");


        //todo 写入题目信息
        QuestionTitlePo questionTitlePo = new QuestionTitlePo();
        questionTitlePo.setTitleId(questionAnswerTitleRelDtos.get(0).getTitleId());
        questionTitlePo.setCommunityId(reqJson.getString("communityId"));
        questionTitlePo.setQaTitle(reqJson.getString("qaName"));
        questionTitlePo.setTitleType(reqJson.getString("titleType"));
        int flag = questionTitleV1InnerServiceSMOImpl.updateQuestionTitle(questionTitlePo);

        if (flag < 1) {
            throw new CmdException("修改数据失败");
        }

        //todo 删除 题目选项
        QuestionTitleValuePo questionTitleValuePo = new QuestionTitleValuePo();
        questionTitleValuePo.setTitleId(questionAnswerTitleRelDtos.get(0).getTitleId());
        questionTitleValuePo.setCommunityId(reqJson.getString("communityId"));
        questionTitleValueV1InnerServiceSMOImpl.deleteQuestionTitleValue(questionTitleValuePo);
        JSONArray titleValues = reqJson.getJSONArray("titleValues");
        JSONObject valueObj = null;
        for (int titleValueIndex = 0; titleValueIndex < titleValues.size(); titleValueIndex++) {
            valueObj = titleValues.getJSONObject(titleValueIndex);
            questionTitleValuePo = new QuestionTitleValuePo();
            questionTitleValuePo.setQaValue(valueObj.getString("qaValue"));
            questionTitleValuePo.setSeq((titleValueIndex + 1) + "");
            if (valueObj.containsKey("score")) {
                questionTitleValuePo.setScore(valueObj.getString("score"));
            } else {
                questionTitleValuePo.setScore("0");
            }
            questionTitleValuePo.setTitleId(questionTitlePo.getTitleId());
            questionTitleValuePo.setValueId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_valueId));
            questionTitleValuePo.setCommunityId(questionTitlePo.getCommunityId());
            questionTitleValueV1InnerServiceSMOImpl.saveQuestionTitleValue(questionTitleValuePo);
        }

        //todo 写入投票信息
        QuestionAnswerPo questionAnswerPo = new QuestionAnswerPo();
        questionAnswerPo.setContent(reqJson.getString("content"));
        questionAnswerPo.setEndTime(reqJson.getString("endTime"));
        questionAnswerPo.setStartTime(reqJson.getString("startTime"));
        questionAnswerPo.setQaId(reqJson.getString("qaId"));
        questionAnswerPo.setQaName(reqJson.getString("qaName"));
        questionAnswerPo.setCommunityId(reqJson.getString("communityId"));
        questionAnswerPo.setQaType(QuestionAnswerDto.QA_TYPE_VOTE);
        questionAnswerV1InnerServiceSMOImpl.updateQuestionAnswer(questionAnswerPo);

        JSONArray roomIds = reqJson.getJSONArray("roomIds");
        if(roomIds == null || roomIds.size() < 1){
            return ;
        }
        UserQuestionAnswerPo userQuestionAnswerPo = new UserQuestionAnswerPo();
        userQuestionAnswerPo.setQaId(reqJson.getString("qaId"));
        userQuestionAnswerPo.setCommunityId(reqJson.getString("communityId"));
        userQuestionAnswerV1InnerServiceSMOImpl.deleteUserQuestionAnswer(userQuestionAnswerPo);


        questionAnswerBMOImpl.saveUserQuestionAnswer(questionAnswerPo,roomIds);
    }
}
