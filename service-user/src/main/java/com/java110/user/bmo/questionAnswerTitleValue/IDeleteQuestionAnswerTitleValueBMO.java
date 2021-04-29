package com.java110.user.bmo.questionAnswerTitleValue;
import com.java110.po.questionAnswerTitleValue.QuestionAnswerTitleValuePo;
import org.springframework.http.ResponseEntity;

public interface IDeleteQuestionAnswerTitleValueBMO {


    /**
     * 修改答卷选项
     * add by wuxw
     * @param questionAnswerTitleValuePo
     * @return
     */
    ResponseEntity<String> delete(QuestionAnswerTitleValuePo questionAnswerTitleValuePo);


}
