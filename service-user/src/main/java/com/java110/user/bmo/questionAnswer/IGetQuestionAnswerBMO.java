package com.java110.user.bmo.questionAnswer;

import com.java110.dto.questionAnswer.QuestionAnswerDto;
import org.springframework.http.ResponseEntity;

public interface IGetQuestionAnswerBMO {


    /**
     * 查询答卷
     * add by wuxw
     *
     * @param questionAnswerDto
     * @return
     */
    ResponseEntity<String> get(QuestionAnswerDto questionAnswerDto);


}
