package com.java110.user.bmo.userQuestionAnswer.impl;

import com.java110.core.annotation.Java110Transactional;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.intf.user.IUserQuestionAnswerInnerServiceSMO;
import com.java110.po.userQuestionAnswer.UserQuestionAnswerPo;
import com.java110.user.bmo.userQuestionAnswer.ISaveUserQuestionAnswerBMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("saveUserQuestionAnswerBMOImpl")
public class SaveUserQuestionAnswerBMOImpl implements ISaveUserQuestionAnswerBMO {

    @Autowired
    private IUserQuestionAnswerInnerServiceSMO userQuestionAnswerInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param userQuestionAnswerPo
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> save(UserQuestionAnswerPo userQuestionAnswerPo) {

        userQuestionAnswerPo.setUserQaId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_userQaId));
        int flag = userQuestionAnswerInnerServiceSMOImpl.saveUserQuestionAnswer(userQuestionAnswerPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
