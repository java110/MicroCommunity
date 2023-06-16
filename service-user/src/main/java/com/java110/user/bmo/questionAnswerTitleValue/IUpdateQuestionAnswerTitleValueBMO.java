package com.java110.user.bmo.questionAnswerTitleValue;
import com.java110.po.questionAnswer.QuestionAnswerTitleValuePo;
import org.springframework.http.ResponseEntity;

public interface IUpdateQuestionAnswerTitleValueBMO {


    /**
     * 修改答卷选项
     * add by wuxw
     * @param questionAnswerTitleValuePo
     * @return
     */
    ResponseEntity<String> update(QuestionAnswerTitleValuePo questionAnswerTitleValuePo);


}
