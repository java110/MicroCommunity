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
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.context.ICmdDataFlowContext;
import com.java110.core.event.cmd.Cmd;
import com.java110.core.event.cmd.CmdEvent;
import com.java110.dto.questionAnswerTitleRel.QuestionAnswerTitleRelDto;
import com.java110.intf.user.*;
import com.java110.po.questionAnswer.QuestionAnswerPo;
import com.java110.po.questionAnswer.QuestionAnswerTitlePo;
import com.java110.po.questionAnswerTitleRel.QuestionAnswerTitleRelPo;
import com.java110.po.questionTitle.QuestionTitlePo;
import com.java110.po.questionTitleValue.QuestionTitleValuePo;
import com.java110.po.user.UserQuestionAnswerPo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 类表述：删除
 * 服务编码：questionAnswer.deleteQuestionAnswer
 * 请求路劲：/app/questionAnswer.DeleteQuestionAnswer
 * add by 吴学文 at 2023-07-13 00:03:56 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@Java110Cmd(serviceCode = "question.deleteQuestionAnswer")
public class DeleteQuestionAnswerCmd extends Cmd {
    private static Logger logger = LoggerFactory.getLogger(DeleteQuestionAnswerCmd.class);

    @Autowired
    private IQuestionAnswerV1InnerServiceSMO questionAnswerV1InnerServiceSMOImpl;

    @Autowired
    private IQuestionAnswerTitleRelV1InnerServiceSMO questionAnswerTitleRelV1InnerServiceSMOImpl;

    @Autowired
    private IQuestionTitleV1InnerServiceSMO questionTitleV1InnerServiceSMOImpl;

    @Autowired
    private IQuestionTitleValueV1InnerServiceSMO questionTitleValueV1InnerServiceSMOImpl;

    @Autowired
    private IUserQuestionAnswerV1InnerServiceSMO userQuestionAnswerV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "qaId", "qaId不能为空");
        Assert.hasKeyAndValue(reqJson, "communityId", "communityId不能为空");

    }

    @Override
    @Java110Transactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {

        QuestionAnswerPo questionAnswerPo = BeanConvertUtil.covertBean(reqJson, QuestionAnswerPo.class);
        int flag = questionAnswerV1InnerServiceSMOImpl.deleteQuestionAnswer(questionAnswerPo);

        if (flag < 1) {
            throw new CmdException("删除数据失败");
        }


        //todo 删除 题目
        QuestionAnswerTitleRelDto questionAnswerTitleRelDto = new QuestionAnswerTitleRelDto();
        questionAnswerTitleRelDto.setQaId(reqJson.getString("qaId"));
        questionAnswerTitleRelDto.setCommunityId(reqJson.getString("communityId"));
        List<QuestionAnswerTitleRelDto> questionAnswerTitleRelDtos = questionAnswerTitleRelV1InnerServiceSMOImpl.queryQuestionAnswerTitleRels(questionAnswerTitleRelDto);
        if (questionAnswerTitleRelDtos == null || questionAnswerTitleRelDtos.size() < 1) {
            return;
        }

        QuestionAnswerTitleRelPo questionAnswerTitleRelPo = new QuestionAnswerTitleRelPo();
        questionAnswerTitleRelPo.setQatrId(questionAnswerTitleRelDtos.get(0).getQatrId());
        questionAnswerTitleRelPo.setCommunityId(reqJson.getString("communityId"));
        questionAnswerTitleRelV1InnerServiceSMOImpl.deleteQuestionAnswerTitleRel(questionAnswerTitleRelPo);

        //todo 删除题目
        QuestionTitlePo questionTitlePo = new QuestionTitlePo();
        questionTitlePo.setTitleId(questionAnswerTitleRelDtos.get(0).getTitleId());
        questionTitlePo.setCommunityId(reqJson.getString("communityId"));
        questionTitleV1InnerServiceSMOImpl.deleteQuestionTitle(questionTitlePo);

        //todo 删除选项
        QuestionTitleValuePo questionTitleValuePo = new QuestionTitleValuePo();
        questionTitleValuePo.setTitleId(questionAnswerTitleRelDtos.get(0).getTitleId());
        questionTitleValuePo.setCommunityId(reqJson.getString("communityId"));
        questionTitleValueV1InnerServiceSMOImpl.deleteQuestionTitleValue(questionTitleValuePo);

        //todo 删除用户投票
        UserQuestionAnswerPo userQuestionAnswerPo = new UserQuestionAnswerPo();
        userQuestionAnswerPo.setQaId(reqJson.getString("qaId"));
        userQuestionAnswerPo.setCommunityId(reqJson.getString("communityId"));
        userQuestionAnswerV1InnerServiceSMOImpl.deleteUserQuestionAnswer(userQuestionAnswerPo);

        //todo 删除用户投票值


        cmdDataFlowContext.setResponseEntity(ResultVo.success());
    }
}
