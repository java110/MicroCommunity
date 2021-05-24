package com.java110.report.bmo.reportQuestionAnswer.impl;

import com.java110.dto.userQuestionAnswerValue.UserQuestionAnswerValueDto;
import com.java110.intf.report.IReportUserQuestionAnswerValueInnerServiceSMO;
import com.java110.report.bmo.reportQuestionAnswer.IGetReportQuestionAnswerBMO;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("getReportQuestionAnswerBMOImpl")
public class GetReportQuestionAnswerBMOImpl implements IGetReportQuestionAnswerBMO {

    @Autowired
    private IReportUserQuestionAnswerValueInnerServiceSMO reportUserQuestionAnswerValueInnerServiceSMOImpl;


    /**
     * @param userQuestionAnswerValueDto
     * @return 订单服务能够接受的报文
     */
    public ResponseEntity<String> get(UserQuestionAnswerValueDto userQuestionAnswerValueDto) {


        int count = reportUserQuestionAnswerValueInnerServiceSMOImpl.queryUserQuestionAnswerValuesCount(userQuestionAnswerValueDto);

        List<UserQuestionAnswerValueDto> userQuestionAnswerValueDtos = null;
        if (count > 0) {
            userQuestionAnswerValueDtos = reportUserQuestionAnswerValueInnerServiceSMOImpl.queryUserQuestionAnswerValues(userQuestionAnswerValueDto);

            refreshQuestionAnswerValue(userQuestionAnswerValueDtos);
        } else {
            userQuestionAnswerValueDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) userQuestionAnswerValueDto.getRow()), count, userQuestionAnswerValueDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

    private void refreshQuestionAnswerValue(List<UserQuestionAnswerValueDto> userQuestionAnswerValueDtos) {

        if (userQuestionAnswerValueDtos == null || userQuestionAnswerValueDtos.size() < 1) {
            return;
        }

        for (UserQuestionAnswerValueDto userQuestionAnswerValueDto : userQuestionAnswerValueDtos) {
            if (StringUtil.isEmpty(userQuestionAnswerValueDto.getQaValue())) {
                userQuestionAnswerValueDto.setQaValue(userQuestionAnswerValueDto.getValueContent());
            }
        }
    }

}
