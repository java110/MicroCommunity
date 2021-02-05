package com.java110.user.bmo.questionAnswerTitle;
import com.java110.po.questionAnswerTitle.QuestionAnswerTitlePo;
import org.springframework.http.ResponseEntity;

public interface IUpdateQuestionAnswerTitleBMO {


    /**
     * 修改答卷
     * add by wuxw
     * @param questionAnswerTitlePo
     * @return
     */
    ResponseEntity<String> update(QuestionAnswerTitlePo questionAnswerTitlePo);


}
