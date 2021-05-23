package com.java110.user.bmo.questionAnswerTitleValue.impl;

import com.java110.dto.questionAnswerTitleValue.QuestionAnswerTitleValueDto;
import com.java110.intf.user.IQuestionAnswerTitleValueInnerServiceSMO;
import com.java110.user.bmo.questionAnswerTitleValue.IGetQuestionAnswerTitleValueBMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("getQuestionAnswerTitleValueBMOImpl")
public class GetQuestionAnswerTitleValueBMOImpl implements IGetQuestionAnswerTitleValueBMO {

    @Autowired
    private IQuestionAnswerTitleValueInnerServiceSMO questionAnswerTitleValueInnerServiceSMOImpl;

    /**
     * @param questionAnswerTitleValueDto
     * @return 订单服务能够接受的报文
     */
    public ResponseEntity<String> get(QuestionAnswerTitleValueDto questionAnswerTitleValueDto) {


        int count = questionAnswerTitleValueInnerServiceSMOImpl.queryQuestionAnswerTitleValuesCount(questionAnswerTitleValueDto);

        List<QuestionAnswerTitleValueDto> questionAnswerTitleValueDtos = null;
        if (count > 0) {
            questionAnswerTitleValueDtos = questionAnswerTitleValueInnerServiceSMOImpl.queryQuestionAnswerTitleValues(questionAnswerTitleValueDto);
        } else {
            questionAnswerTitleValueDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) questionAnswerTitleValueDto.getRow()), count, questionAnswerTitleValueDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

    @Override
    public ResponseEntity<String> queryQuestionAnswerTitleValueResult(QuestionAnswerTitleValueDto questionAnswerTitleValueDto) {
        List<QuestionAnswerTitleValueDto>  questionAnswerTitleValueDtos
                = questionAnswerTitleValueInnerServiceSMOImpl.queryQuestionAnswerTitleValueResult(questionAnswerTitleValueDto);
        return ResultVo.createResponseEntity(questionAnswerTitleValueDtos);
    }

}
