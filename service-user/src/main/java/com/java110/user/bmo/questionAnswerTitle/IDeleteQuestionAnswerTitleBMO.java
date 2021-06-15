package com.java110.user.bmo.questionAnswerTitle;

import com.java110.po.questionAnswerTitle.QuestionAnswerTitlePo;
import org.springframework.http.ResponseEntity;

public interface IDeleteQuestionAnswerTitleBMO {


    /**
     * 修改答卷
     * add by wuxw
     *
     * @param questionAnswerTitlePo
     * @return
     */
    ResponseEntity<String> delete(QuestionAnswerTitlePo questionAnswerTitlePo);


}
