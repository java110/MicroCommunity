package com.java110.user.bmo.questionAnswerTitleValue;

import com.java110.po.questionAnswer.QuestionAnswerTitleValuePo;
import org.springframework.http.ResponseEntity;
public interface ISaveQuestionAnswerTitleValueBMO {


    /**
     * 添加答卷选项
     * add by wuxw
     * @param questionAnswerTitleValuePo
     * @return
     */
    ResponseEntity<String> save(QuestionAnswerTitleValuePo questionAnswerTitleValuePo);


}
