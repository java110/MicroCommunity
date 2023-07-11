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
import com.java110.dto.inspection.InspectionItemTitleDto;
import com.java110.dto.maintainance.MaintainanceItemDto;
import com.java110.dto.maintainance.MaintainanceItemValueDto;
import com.java110.dto.questionTitle.QuestionTitleDto;
import com.java110.dto.questionTitleValue.QuestionTitleValueDto;
import com.java110.intf.user.IQuestionTitleV1InnerServiceSMO;
import com.java110.intf.user.IQuestionTitleValueV1InnerServiceSMO;
import com.java110.po.maintainance.MaintainanceItemValuePo;
import com.java110.po.questionTitle.QuestionTitlePo;
import com.java110.po.questionTitleValue.QuestionTitleValuePo;
import com.java110.utils.exception.CmdException;
import com.java110.utils.util.Assert;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


/**
 * 类表述：更新
 * 服务编码：questionTitle.updateQuestionTitle
 * 请求路劲：/app/questionTitle.UpdateQuestionTitle
 * add by 吴学文 at 2023-07-10 15:10:47 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@Java110Cmd(serviceCode = "question.updateQuestionTitle")
public class UpdateQuestionTitleCmd extends Cmd {

    private static Logger logger = LoggerFactory.getLogger(UpdateQuestionTitleCmd.class);


    @Autowired
    private IQuestionTitleV1InnerServiceSMO questionTitleV1InnerServiceSMOImpl;

    @Autowired
    private IQuestionTitleValueV1InnerServiceSMO questionTitleValueV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "titleId", "titleId不能为空");
        Assert.hasKeyAndValue(reqJson, "communityId", "communityId不能为空");

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
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {

        QuestionTitlePo questionTitlePo = BeanConvertUtil.covertBean(reqJson, QuestionTitlePo.class);
        int flag = questionTitleV1InnerServiceSMOImpl.updateQuestionTitle(questionTitlePo);

        if (flag < 1) {
            throw new CmdException("更新数据失败");
        }

        if (QuestionTitleDto.TITLE_TYPE_QUESTIONS.equals(questionTitlePo.getTitleType())) {
            cmdDataFlowContext.setResponseEntity(ResultVo.success());
            return;
        }
        QuestionTitleValueDto questionTitleValueDto = new QuestionTitleValueDto();
        questionTitleValueDto.setTitleId(reqJson.getString("titleId"));
        List<QuestionTitleValueDto> questionTitleValues = questionTitleValueV1InnerServiceSMOImpl.queryQuestionTitleValues(questionTitleValueDto);
        if (questionTitleValues != null && questionTitleValues.size() > 0) {
            QuestionTitleValuePo deleteQuestionTitleValuePo = new QuestionTitleValuePo();
            deleteQuestionTitleValuePo.setTitleId(questionTitlePo.getTitleId());
            flag = questionTitleValueV1InnerServiceSMOImpl.deleteQuestionTitleValue(deleteQuestionTitleValuePo);
            if (flag < 1) {
                throw new CmdException("更新数据失败");
            }
        }
        JSONArray titleValues = reqJson.getJSONArray("titleValues");
        QuestionTitleValuePo questionTitleValuePo = null;
        for (int titleValueIndex = 0; titleValueIndex < titleValues.size(); titleValueIndex++) {
            questionTitleValuePo = new QuestionTitleValuePo();
            questionTitleValuePo.setQaValue(titleValues.getJSONObject(titleValueIndex).getString("qaValue"));
            questionTitleValuePo.setSeq(titleValues.getJSONObject(titleValueIndex).getString("seq"));
            if(titleValues.getJSONObject(titleValueIndex).containsKey("score")) {
                questionTitleValuePo.setScore(titleValues.getJSONObject(titleValueIndex).getString("score"));
            }else{
                questionTitleValuePo.setScore("0");
            }
            questionTitleValuePo.setTitleId(questionTitlePo.getTitleId());
            questionTitleValuePo.setValueId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_valueId));
            questionTitleValuePo.setCommunityId(questionTitlePo.getCommunityId());
            questionTitleValueV1InnerServiceSMOImpl.saveQuestionTitleValue(questionTitleValuePo);
        }

        cmdDataFlowContext.setResponseEntity(ResultVo.success());
    }
}
