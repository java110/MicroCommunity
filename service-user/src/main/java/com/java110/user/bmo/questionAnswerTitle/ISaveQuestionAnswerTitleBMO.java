package com.java110.user.bmo.questionAnswerTitle;

import com.alibaba.fastjson.JSONArray;
import com.java110.po.questionAnswerTitle.QuestionAnswerTitlePo;
import org.springframework.http.ResponseEntity;
public interface ISaveQuestionAnswerTitleBMO {


    /**
     * 添加答卷
     * add by wuxw
     * @param questionAnswerTitlePo
     * @return
     */
    ResponseEntity<String> save(QuestionAnswerTitlePo questionAnswerTitlePo, JSONArray titleValues);


}
