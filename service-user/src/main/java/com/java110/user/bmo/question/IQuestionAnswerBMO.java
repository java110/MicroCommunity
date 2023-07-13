package com.java110.user.bmo.question;


import com.alibaba.fastjson.JSONArray;
import com.java110.po.questionAnswer.QuestionAnswerPo;

public interface IQuestionAnswerBMO {
    /**
     * 保存用户问卷投票信息
     * @param questionAnswerPo
     * @param roomIds
     */
    void saveUserQuestionAnswer(QuestionAnswerPo questionAnswerPo, JSONArray roomIds);
}
