package com.java110.user.cmd.question;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.questionAnswer.QuestionAnswerDto;
import com.java110.intf.user.IQuestionAnswerV1InnerServiceSMO;
import com.java110.po.questionAnswer.QuestionAnswerPo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;

/**
 * 发布 投票和问卷
 */
@Java110Cmd(serviceCode = "question.publishQuestion")
public class PublishQuestionCmd extends Cmd {

    @Autowired
    private IQuestionAnswerV1InnerServiceSMO questionAnswerV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区信息");
        Assert.hasKeyAndValue(reqJson, "qaId", "未包含qaId");

    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {

        QuestionAnswerPo questionAnswerPo = new QuestionAnswerPo();
        questionAnswerPo.setQaId(reqJson.getString("qaId"));
        questionAnswerPo.setCommunityId(reqJson.getString("communityId"));
        questionAnswerPo.setState(QuestionAnswerDto.STATE_COMPLETE);
        int flag = questionAnswerV1InnerServiceSMOImpl.updateQuestionAnswer(questionAnswerPo);
        if (flag < 1) {
            throw new CmdException("发布失败");
        }

        //todo 通知信息推送模块

    }
}
