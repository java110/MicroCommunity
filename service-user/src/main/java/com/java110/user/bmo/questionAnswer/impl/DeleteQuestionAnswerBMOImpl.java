package com.java110.user.bmo.questionAnswer.impl;

import com.java110.core.annotation.Java110Transactional;
import com.java110.intf.user.IQuestionAnswerInnerServiceSMO;
import com.java110.po.questionAnswer.QuestionAnswerPo;
import com.java110.user.bmo.questionAnswer.IDeleteQuestionAnswerBMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("deleteQuestionAnswerBMOImpl")
public class DeleteQuestionAnswerBMOImpl implements IDeleteQuestionAnswerBMO {

    @Autowired
    private IQuestionAnswerInnerServiceSMO questionAnswerInnerServiceSMOImpl;

    /**
     * @param questionAnswerPo 数据
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> delete(QuestionAnswerPo questionAnswerPo) {

        int flag = questionAnswerInnerServiceSMOImpl.deleteQuestionAnswer(questionAnswerPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
