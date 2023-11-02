/*
 * Copyright 2017-2020 吴学文 and java110 team.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
import com.java110.intf.user.IQuestionAnswerTitleRelV1InnerServiceSMO;
import com.java110.intf.user.IQuestionAnswerV1InnerServiceSMO;
import com.java110.intf.user.IUserQuestionAnswerV1InnerServiceSMO;
import com.java110.po.questionAnswer.QuestionAnswerPo;
import com.java110.po.questionAnswerTitleRel.QuestionAnswerTitleRelPo;
import com.java110.po.user.UserQuestionAnswerPo;
import com.java110.user.bmo.question.IQuestionAnswerBMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 类表述：更新
 * 服务编码：questionAnswer.updateQuestionAnswer
 * 请求路劲：/app/questionAnswer.UpdateQuestionAnswer
 * add by 吴学文 at 2023-07-13 00:03:56 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@Java110Cmd(serviceCode = "question.updateQuestionAnswer")
public class UpdateQuestionAnswerCmd extends Cmd {

    private static Logger logger = LoggerFactory.getLogger(UpdateQuestionAnswerCmd.class);

    @Autowired
    private IQuestionAnswerV1InnerServiceSMO questionAnswerV1InnerServiceSMOImpl;

    @Autowired
    private IQuestionAnswerTitleRelV1InnerServiceSMO questionAnswerTitleRelV1InnerServiceSMOImpl;

    @Autowired
    private IQuestionAnswerBMO questionAnswerBMOImpl;

    @Autowired
    private IUserQuestionAnswerV1InnerServiceSMO userQuestionAnswerV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含communityId");
        Assert.hasKeyAndValue(reqJson, "qaName", "请求报文中未包含投票名称");
        Assert.hasKeyAndValue(reqJson, "qaId", "请求报文中未包含题目");
        Assert.hasKeyAndValue(reqJson, "startTime", "未包含开始时间");
        Assert.hasKeyAndValue(reqJson, "endTime", "未包含结束时间");
        Assert.hasKeyAndValue(reqJson, "content", "未包含调研说明");
        Assert.hasKey(reqJson, "questionTitles", "请求报文中未包含题目");

        JSONArray questionTitles = reqJson.getJSONArray("questionTitles");
        if (questionTitles == null || questionTitles.size() < 1) {
            throw new IllegalArgumentException("未包含题目");
        }

    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {

        //todo 写入投票信息
        QuestionAnswerPo questionAnswerPo = new QuestionAnswerPo();
        questionAnswerPo.setContent(reqJson.getString("content"));
        questionAnswerPo.setEndTime(reqJson.getString("endTime"));
        questionAnswerPo.setStartTime(reqJson.getString("startTime"));
        questionAnswerPo.setQaId(reqJson.getString("qaId"));
        questionAnswerPo.setQaName(reqJson.getString("qaName"));
        questionAnswerPo.setCommunityId(reqJson.getString("communityId"));
        questionAnswerPo.setQaType(QuestionAnswerDto.QA_TYPE_QUESTION);
        questionAnswerPo.setState(QuestionAnswerDto.STATE_WAIT);
        int flag = questionAnswerV1InnerServiceSMOImpl.updateQuestionAnswer(questionAnswerPo);

        if (flag < 1) {
            throw new CmdException("保存数据失败");
        }
        JSONArray questionTitles = reqJson.getJSONArray("questionTitles");
        QuestionAnswerTitleRelPo questionAnswerTitleRelPo = new QuestionAnswerTitleRelPo();
        questionAnswerTitleRelPo.setQaId(reqJson.getString("qaId"));
        questionAnswerTitleRelPo.setCommunityId(reqJson.getString("communityId"));
        questionAnswerTitleRelV1InnerServiceSMOImpl.deleteQuestionAnswerTitleRel(questionAnswerTitleRelPo);
        JSONObject title = null;
        for (int titleIndex = 0; titleIndex < questionTitles.size(); titleIndex++) {
            title= questionTitles.getJSONObject(titleIndex);
            questionAnswerTitleRelPo = new QuestionAnswerTitleRelPo();
            questionAnswerTitleRelPo.setCommunityId(reqJson.getString("communityId"));
            questionAnswerTitleRelPo.setTitleId(title.getString("titleId"));
            questionAnswerTitleRelPo.setSeq((titleIndex + 1) + "");
            questionAnswerTitleRelPo.setScore("0");
            if(title.containsKey("score")){
                questionAnswerTitleRelPo.setScore(title.getString("score"));
            }
            questionAnswerTitleRelPo.setQaId(questionAnswerPo.getQaId());
            questionAnswerTitleRelPo.setQatrId(GenerateCodeFactory.getGeneratorId("11"));
            questionAnswerTitleRelV1InnerServiceSMOImpl.saveQuestionAnswerTitleRel(questionAnswerTitleRelPo);
        }

        JSONArray roomIds = reqJson.getJSONArray("roomIds");
        if(roomIds == null || roomIds.size() < 1){
            return ;
        }
        UserQuestionAnswerPo userQuestionAnswerPo = new UserQuestionAnswerPo();
        userQuestionAnswerPo.setQaId(reqJson.getString("qaId"));
        userQuestionAnswerPo.setCommunityId(reqJson.getString("communityId"));
        userQuestionAnswerV1InnerServiceSMOImpl.deleteUserQuestionAnswer(userQuestionAnswerPo);

        questionAnswerBMOImpl.saveUserQuestionAnswer(questionAnswerPo, reqJson.getJSONArray("roomIds"));

        cmdDataFlowContext.setResponseEntity(ResultVo.success());
    }
}
