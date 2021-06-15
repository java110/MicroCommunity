package com.java110.user.bmo.userQuestionAnswerValue;

import com.java110.po.userQuestionAnswerValue.UserQuestionAnswerValuePo;
import org.springframework.http.ResponseEntity;

public interface IUpdateUserQuestionAnswerValueBMO {


    /**
     * 修改答卷答案
     * add by wuxw
     *
     * @param userQuestionAnswerValuePo
     * @return
     */
    ResponseEntity<String> update(UserQuestionAnswerValuePo userQuestionAnswerValuePo);


}
