package com.java110.user.bmo.userQuestionAnswer.impl;

import com.java110.dto.userQuestionAnswer.UserQuestionAnswerDto;
import com.java110.intf.user.IUserQuestionAnswerInnerServiceSMO;
import com.java110.user.bmo.userQuestionAnswer.IGetUserQuestionAnswerBMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("getUserQuestionAnswerBMOImpl")
public class GetUserQuestionAnswerBMOImpl implements IGetUserQuestionAnswerBMO {

    @Autowired
    private IUserQuestionAnswerInnerServiceSMO userQuestionAnswerInnerServiceSMOImpl;

    /**
     * @param userQuestionAnswerDto
     * @return 订单服务能够接受的报文
     */
    public ResponseEntity<String> get(UserQuestionAnswerDto userQuestionAnswerDto) {


        int count = userQuestionAnswerInnerServiceSMOImpl.queryUserQuestionAnswersCount(userQuestionAnswerDto);

        List<UserQuestionAnswerDto> userQuestionAnswerDtos = null;
        if (count > 0) {
            userQuestionAnswerDtos = userQuestionAnswerInnerServiceSMOImpl.queryUserQuestionAnswers(userQuestionAnswerDto);
        } else {
            userQuestionAnswerDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) userQuestionAnswerDto.getRow()), count, userQuestionAnswerDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

}
