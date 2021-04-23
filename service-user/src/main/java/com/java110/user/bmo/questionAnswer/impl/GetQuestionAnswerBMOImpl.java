package com.java110.user.bmo.questionAnswer.impl;

import com.java110.dto.questionAnswer.QuestionAnswerDto;
import com.java110.intf.user.IQuestionAnswerInnerServiceSMO;
import com.java110.user.bmo.questionAnswer.IGetQuestionAnswerBMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("getQuestionAnswerBMOImpl")
public class GetQuestionAnswerBMOImpl implements IGetQuestionAnswerBMO {

    @Autowired
    private IQuestionAnswerInnerServiceSMO questionAnswerInnerServiceSMOImpl;

    /**
     * @param questionAnswerDto
     * @return 订单服务能够接受的报文
     */
    public ResponseEntity<String> get(QuestionAnswerDto questionAnswerDto) {


        int count = questionAnswerInnerServiceSMOImpl.queryQuestionAnswersCount(questionAnswerDto);

        List<QuestionAnswerDto> questionAnswerDtos = null;
        if (count > 0) {
            questionAnswerDtos = questionAnswerInnerServiceSMOImpl.queryQuestionAnswers(questionAnswerDto);
        } else {
            questionAnswerDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) questionAnswerDto.getRow()), count, questionAnswerDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

}
