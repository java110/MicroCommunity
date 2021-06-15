package com.java110.user.bmo.userQuestionAnswer;
import com.java110.dto.userQuestionAnswer.UserQuestionAnswerDto;
import org.springframework.http.ResponseEntity;
public interface IGetUserQuestionAnswerBMO {


    /**
     * 查询答卷
     * add by wuxw
     * @param  userQuestionAnswerDto
     * @return
     */
    ResponseEntity<String> get(UserQuestionAnswerDto userQuestionAnswerDto);


}
