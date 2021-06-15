package com.java110.user.bmo.userQuestionAnswer.impl;

import com.java110.core.annotation.Java110Transactional;
import com.java110.intf.user.IUserQuestionAnswerInnerServiceSMO;
import com.java110.po.userQuestionAnswer.UserQuestionAnswerPo;
import com.java110.user.bmo.userQuestionAnswer.IUpdateUserQuestionAnswerBMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("updateUserQuestionAnswerBMOImpl")
public class UpdateUserQuestionAnswerBMOImpl implements IUpdateUserQuestionAnswerBMO {

    @Autowired
    private IUserQuestionAnswerInnerServiceSMO userQuestionAnswerInnerServiceSMOImpl;

    /**
     * @param userQuestionAnswerPo
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> update(UserQuestionAnswerPo userQuestionAnswerPo) {

        int flag = userQuestionAnswerInnerServiceSMOImpl.updateUserQuestionAnswer(userQuestionAnswerPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
