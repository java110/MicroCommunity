package com.java110.user.bmo.questionAnswerTitle.impl;

import com.java110.dto.questionAnswerTitle.QuestionAnswerTitleDto;
import com.java110.intf.user.IQuestionAnswerTitleInnerServiceSMO;
import com.java110.user.bmo.questionAnswerTitle.IGetQuestionAnswerTitleBMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("getQuestionAnswerTitleBMOImpl")
public class GetQuestionAnswerTitleBMOImpl implements IGetQuestionAnswerTitleBMO {

    @Autowired
    private IQuestionAnswerTitleInnerServiceSMO questionAnswerTitleInnerServiceSMOImpl;

    /**
     * @param questionAnswerTitleDto
     * @return 订单服务能够接受的报文
     */
    public ResponseEntity<String> get(QuestionAnswerTitleDto questionAnswerTitleDto) {


        int count = questionAnswerTitleInnerServiceSMOImpl.queryQuestionAnswerTitlesCount(questionAnswerTitleDto);

        List<QuestionAnswerTitleDto> questionAnswerTitleDtos = null;
        if (count > 0) {
            questionAnswerTitleDtos = questionAnswerTitleInnerServiceSMOImpl.queryQuestionAnswerTitles(questionAnswerTitleDto);
        } else {
            questionAnswerTitleDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) questionAnswerTitleDto.getRow()), count, questionAnswerTitleDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

}
