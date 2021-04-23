package com.java110.user.bmo.userQuestionAnswer;
import com.java110.po.userQuestionAnswer.UserQuestionAnswerPo;
import org.springframework.http.ResponseEntity;

public interface IDeleteUserQuestionAnswerBMO {


    /**
     * 修改答卷
     * add by wuxw
     * @param userQuestionAnswerPo
     * @return
     */
    ResponseEntity<String> delete(UserQuestionAnswerPo userQuestionAnswerPo);


}
