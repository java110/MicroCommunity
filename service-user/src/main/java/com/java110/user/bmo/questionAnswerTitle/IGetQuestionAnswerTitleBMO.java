package com.java110.user.bmo.questionAnswerTitle;
import com.java110.dto.questionAnswer.QuestionAnswerTitleDto;
import org.springframework.http.ResponseEntity;
public interface IGetQuestionAnswerTitleBMO {


    /**
     * 查询答卷
     * add by wuxw
     * @param  questionAnswerTitleDto
     * @return
     */
    ResponseEntity<String> get(QuestionAnswerTitleDto questionAnswerTitleDto);


}
