package com.java110.user.bmo.questionAnswerTitle.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.questionAnswer.QuestionAnswerTitleDto;
import com.java110.intf.user.IQuestionAnswerTitleInnerServiceSMO;
import com.java110.intf.user.IQuestionAnswerTitleValueInnerServiceSMO;
import com.java110.po.questionAnswerTitle.QuestionAnswerTitlePo;
import com.java110.po.questionAnswerTitleValue.QuestionAnswerTitleValuePo;
import com.java110.user.bmo.questionAnswerTitle.IUpdateQuestionAnswerTitleBMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("updateQuestionAnswerTitleBMOImpl")
public class UpdateQuestionAnswerTitleBMOImpl implements IUpdateQuestionAnswerTitleBMO {

    @Autowired
    private IQuestionAnswerTitleInnerServiceSMO questionAnswerTitleInnerServiceSMOImpl;
    @Autowired
    private IQuestionAnswerTitleValueInnerServiceSMO questionAnswerTitleValueInnerServiceSMOImpl;

    /**
     * @param questionAnswerTitlePo
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> update(QuestionAnswerTitlePo questionAnswerTitlePo, JSONArray titleValues) {

        int flag = questionAnswerTitleInnerServiceSMOImpl.updateQuestionAnswerTitle(questionAnswerTitlePo);

        if (flag < 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");

        }
        if (QuestionAnswerTitleDto.TITLE_TYPE_QUESTIONS.equals(questionAnswerTitlePo.getTitleType())) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }
        QuestionAnswerTitleValuePo questionAnswerTitleValuePo = null;
        JSONObject value = null;
        for (int titleValueIndex = 0; titleValueIndex < titleValues.size(); titleValueIndex++) {
            questionAnswerTitleValuePo = new QuestionAnswerTitleValuePo();
            value = titleValues.getJSONObject(titleValueIndex);
            questionAnswerTitleValuePo.setQaValue(titleValues.getJSONObject(titleValueIndex).getString("qaValue"));
            questionAnswerTitleValuePo.setSeq(titleValues.getJSONObject(titleValueIndex).getString("seq"));
            questionAnswerTitleValuePo.setTitleId(questionAnswerTitlePo.getTitleId());
            questionAnswerTitleValuePo.setObjId(questionAnswerTitlePo.getObjId());
            questionAnswerTitleValuePo.setObjType(questionAnswerTitlePo.getObjType());
            if (value.containsKey("valueId") && !value.getString("valueId").startsWith("-")) {
                questionAnswerTitleValuePo.setValueId(value.getString("valueId"));
                questionAnswerTitleValueInnerServiceSMOImpl.updateQuestionAnswerTitleValue(questionAnswerTitleValuePo);
            } else {
                questionAnswerTitleValuePo.setValueId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_valueId));
                questionAnswerTitleValueInnerServiceSMOImpl.saveQuestionAnswerTitleValue(questionAnswerTitleValuePo);
            }
        }
        return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
    }

}
