package com.java110.user.bmo.userQuestionAnswerValue;
import com.java110.dto.userQuestionAnswerValue.UserQuestionAnswerValueDto;
import org.springframework.http.ResponseEntity;
public interface IGetUserQuestionAnswerValueBMO {


    /**
     * 查询答卷答案
     * add by wuxw
     * @param  userQuestionAnswerValueDto
     * @return
     */
    ResponseEntity<String> get(UserQuestionAnswerValueDto userQuestionAnswerValueDto);


}
