package com.java110.user.cmd.question;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.questionAnswer.QuestionAnswerDto;
import com.java110.dto.user.UserDto;
import com.java110.dto.user.UserQuestionAnswerDto;
import com.java110.intf.user.IUserQuestionAnswerV1InnerServiceSMO;
import com.java110.intf.user.IUserV1InnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.util.List;

/**
 * 查询用户投票 或者问卷
 */
@Java110Cmd(serviceCode = "question.queryOwnerQuestionAnswer")
public class QueryOwnerQuestionAnswerCmd extends Cmd {

    @Autowired
    private IUserV1InnerServiceSMO userV1InnerServiceSMOImpl;

    @Autowired
    private IUserQuestionAnswerV1InnerServiceSMO userQuestionAnswerV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区信息");
        //Assert.hasKeyAndValue(reqJson, "qaType", "未包含qaType");
        String userId = context.getReqHeaders().get("user-id");
        Assert.hasLength(userId, "用户未登录");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        String userId = context.getReqHeaders().get("user-id");
        UserDto userDto = new UserDto();
        userDto.setUserId(userId);
        List<UserDto> userDtos = userV1InnerServiceSMOImpl.queryUsers(userDto);
        Assert.listOnlyOne(userDtos, "用户不存在");
        UserQuestionAnswerDto userQuestionAnswerDto = new UserQuestionAnswerDto();
        userQuestionAnswerDto.setLink(userDtos.get(0).getTel());
        userQuestionAnswerDto.setCommunityId(reqJson.getString("communityId"));
        userQuestionAnswerDto.setQaType(reqJson.getString("qaType"));
        userQuestionAnswerDto.setState(reqJson.getString("state"));
        userQuestionAnswerDto.setUserQaId(reqJson.getString("userQaId"));
        List<QuestionAnswerDto> questionAnswerDtos = userQuestionAnswerV1InnerServiceSMOImpl.queryOwnerQuestionAnswers(userQuestionAnswerDto);
        context.setResponseEntity(ResultVo.createResponseEntity(questionAnswerDtos));
    }
}
