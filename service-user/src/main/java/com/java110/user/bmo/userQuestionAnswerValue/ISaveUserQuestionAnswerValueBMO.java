package com.java110.user.bmo.userQuestionAnswerValue;

import com.java110.po.userQuestionAnswerValue.UserQuestionAnswerValuePo;
import org.springframework.http.ResponseEntity;
public interface ISaveUserQuestionAnswerValueBMO {


    /**
     * 添加答卷答案
     * add by wuxw
     * @param userQuestionAnswerValuePo
     * @return
     */
    ResponseEntity<String> save(UserQuestionAnswerValuePo userQuestionAnswerValuePo);


}
