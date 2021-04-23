package com.java110.user.bmo.questionAnswer;

import com.java110.po.questionAnswer.QuestionAnswerPo;
import org.springframework.http.ResponseEntity;

public interface ISaveQuestionAnswerBMO {


    /**
     * 添加答卷
     * add by wuxw
     *
     * @param questionAnswerPo
     * @return
     */
    ResponseEntity<String> save(QuestionAnswerPo questionAnswerPo);


}
