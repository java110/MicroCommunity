package com.java110.user.bmo.questionAnswerTitle.impl;

import com.java110.core.annotation.Java110Transactional;
import com.java110.intf.user.IQuestionAnswerTitleInnerServiceSMO;
import com.java110.po.questionAnswerTitle.QuestionAnswerTitlePo;
import com.java110.user.bmo.questionAnswerTitle.IUpdateQuestionAnswerTitleBMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("updateQuestionAnswerTitleBMOImpl")
public class UpdateQuestionAnswerTitleBMOImpl implements IUpdateQuestionAnswerTitleBMO {

    @Autowired
    private IQuestionAnswerTitleInnerServiceSMO questionAnswerTitleInnerServiceSMOImpl;

    /**
     * @param questionAnswerTitlePo
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> update(QuestionAnswerTitlePo questionAnswerTitlePo) {

        int flag = questionAnswerTitleInnerServiceSMOImpl.updateQuestionAnswerTitle(questionAnswerTitlePo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
