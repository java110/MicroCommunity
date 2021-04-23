package com.java110.user.bmo.questionAnswerTitle.impl;

import com.java110.core.annotation.Java110Transactional;
import com.java110.intf.user.IQuestionAnswerTitleInnerServiceSMO;
import com.java110.po.questionAnswerTitle.QuestionAnswerTitlePo;
import com.java110.user.bmo.questionAnswerTitle.IDeleteQuestionAnswerTitleBMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("deleteQuestionAnswerTitleBMOImpl")
public class DeleteQuestionAnswerTitleBMOImpl implements IDeleteQuestionAnswerTitleBMO {

    @Autowired
    private IQuestionAnswerTitleInnerServiceSMO questionAnswerTitleInnerServiceSMOImpl;

    /**
     * @param questionAnswerTitlePo 数据
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> delete(QuestionAnswerTitlePo questionAnswerTitlePo) {

        int flag = questionAnswerTitleInnerServiceSMOImpl.deleteQuestionAnswerTitle(questionAnswerTitlePo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
