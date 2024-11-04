package com.java110.user.bmo.questionAnswer;

import com.java110.po.question.QuestionAnswerPo;
import org.springframework.http.ResponseEntity;

public interface IDeleteQuestionAnswerBMO {


    /**
     * 修改答卷
     * add by wuxw
     *
     * @param questionAnswerPo
     * @return
     */
    ResponseEntity<String> delete(QuestionAnswerPo questionAnswerPo);


}
