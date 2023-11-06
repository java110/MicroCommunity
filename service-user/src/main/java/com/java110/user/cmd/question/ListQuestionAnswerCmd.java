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

import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Cmd;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.intf.user.IQuestionAnswerV1InnerServiceSMO;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import com.java110.dto.questionAnswer.QuestionAnswerDto;

import java.util.List;
import java.util.ArrayList;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 类表述：查询
 * 服务编码：questionAnswer.listQuestionAnswer
 * 请求路劲：/app/questionAnswer.ListQuestionAnswer
 * add by 吴学文 at 2023-07-13 00:03:56 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@Java110Cmd(serviceCode = "question.listQuestionAnswer")
public class ListQuestionAnswerCmd extends Cmd {

    private static Logger logger = LoggerFactory.getLogger(ListQuestionAnswerCmd.class);

    @Autowired
    private IQuestionAnswerV1InnerServiceSMO questionAnswerV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        super.validatePageInfo(reqJson);
        Assert.hasKeyAndValue(reqJson, "communityId", "communityId不能为空");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {

        QuestionAnswerDto questionAnswerDto = BeanConvertUtil.covertBean(reqJson, QuestionAnswerDto.class);

        int count = questionAnswerV1InnerServiceSMOImpl.queryQuestionAnswersCount(questionAnswerDto);

        List<QuestionAnswerDto> questionAnswerDtos = null;

        if (count > 0) {
            questionAnswerDtos = questionAnswerV1InnerServiceSMOImpl.queryQuestionAnswers(questionAnswerDto);
        } else {
            questionAnswerDtos = new ArrayList<>();
        }

        //todo 查询已投票数据和 得分
        computeVotedCountAndScore(questionAnswerDtos);

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) reqJson.getInteger("row")), count, questionAnswerDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        cmdDataFlowContext.setResponseEntity(responseEntity);
    }

    /**
     * 计算 投票和得分
     *
     * @param questionAnswerDtos
     */
    private void computeVotedCountAndScore(List<QuestionAnswerDto> questionAnswerDtos) {
        if (questionAnswerDtos == null || questionAnswerDtos.size() < 1) {
            return;
        }
        List<String> qaIds = new ArrayList<>();
        for (QuestionAnswerDto questionAnswerDto : questionAnswerDtos) {
            qaIds.add(questionAnswerDto.getQaId());
        }
        // todo 查询投票人数和得分
        List<QuestionAnswerDto> votedQAs = questionAnswerV1InnerServiceSMOImpl.queryVotedCountAndScore(qaIds.toArray(new String[qaIds.size()]));
        if (votedQAs == null || votedQAs.size() < 1) {
            return;
        }
        for (QuestionAnswerDto questionAnswerDto : questionAnswerDtos) {
            for (QuestionAnswerDto votedQa : votedQAs) {
                if (!questionAnswerDto.getQaId().equals(votedQa.getQaId())) {
                    continue;
                }
                questionAnswerDto.setVotedCount(votedQa.getVotedCount());
                questionAnswerDto.setScore(votedQa.getScore());
            }
        }
    }
}
