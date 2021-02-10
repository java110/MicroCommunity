package com.java110.user.bmo.questionAnswerTitleValue.impl;

import com.java110.core.annotation.Java110Transactional;
import com.java110.intf.user.IQuestionAnswerTitleValueInnerServiceSMO;
import com.java110.po.questionAnswerTitleValue.QuestionAnswerTitleValuePo;
import com.java110.user.bmo.questionAnswerTitleValue.IUpdateQuestionAnswerTitleValueBMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("updateQuestionAnswerTitleValueBMOImpl")
public class UpdateQuestionAnswerTitleValueBMOImpl implements IUpdateQuestionAnswerTitleValueBMO {

    @Autowired
    private IQuestionAnswerTitleValueInnerServiceSMO questionAnswerTitleValueInnerServiceSMOImpl;

    /**
     * @param questionAnswerTitleValuePo
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> update(QuestionAnswerTitleValuePo questionAnswerTitleValuePo) {

        int flag = questionAnswerTitleValueInnerServiceSMOImpl.updateQuestionAnswerTitleValue(questionAnswerTitleValuePo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
