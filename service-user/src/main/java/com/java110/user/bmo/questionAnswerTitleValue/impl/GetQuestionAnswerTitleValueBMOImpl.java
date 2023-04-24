package com.java110.user.bmo.questionAnswerTitleValue.impl;

import com.java110.dto.questionAnswer.QuestionAnswerTitleValueDto;
import com.java110.intf.user.IQuestionAnswerTitleValueInnerServiceSMO;
import com.java110.user.bmo.questionAnswerTitleValue.IGetQuestionAnswerTitleValueBMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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
        List<QuestionAnswerTitleValueDto> questionAnswerTitleValueDtos = new ArrayList<>();
        List<QuestionAnswerTitleValueDto> questionAnswerTitleValueResultCountDtos
                = questionAnswerTitleValueInnerServiceSMOImpl.queryQuestionAnswerTitleValueResultCount(questionAnswerTitleValueDto);
        if (questionAnswerTitleValueResultCountDtos != null && questionAnswerTitleValueResultCountDtos.size() > 0) {
            //获取总人数
            BigDecimal allCount = new BigDecimal(questionAnswerTitleValueResultCountDtos.get(0).getAllCount());
            List<QuestionAnswerTitleValueDto> questionAnswerTitleValues
                    = questionAnswerTitleValueInnerServiceSMOImpl.queryQuestionAnswerTitleValueResult(questionAnswerTitleValueDto);
            for (QuestionAnswerTitleValueDto questionAnswerTitleValue : questionAnswerTitleValues) {
                if (allCount.compareTo(BigDecimal.ZERO) == 0) {
                    questionAnswerTitleValue.setPercentage("0.0%");
                } else {
                    //获取选择人数
                    BigDecimal userCount = new BigDecimal(questionAnswerTitleValue.getUserCount());
                    double result = userCount.doubleValue() / allCount.doubleValue();
                    BigDecimal divide = new BigDecimal(result).setScale(4, BigDecimal.ROUND_HALF_UP);
                    BigDecimal percentage = divide.multiply(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_HALF_UP);
                    questionAnswerTitleValue.setPercentage(percentage + "%");
                }
                questionAnswerTitleValueDtos.add(questionAnswerTitleValue);
            }
        }
        return ResultVo.createResponseEntity(questionAnswerTitleValueDtos);
    }

}
