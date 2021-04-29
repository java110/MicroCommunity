package com.java110.user.bmo.userQuestionAnswerValue.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.questionAnswer.QuestionAnswerDto;
import com.java110.intf.user.IQuestionAnswerInnerServiceSMO;
import com.java110.intf.user.IUserQuestionAnswerInnerServiceSMO;
import com.java110.intf.user.IUserQuestionAnswerValueInnerServiceSMO;
import com.java110.po.userQuestionAnswer.UserQuestionAnswerPo;
import com.java110.po.userQuestionAnswerValue.UserQuestionAnswerValuePo;
import com.java110.user.bmo.userQuestionAnswerValue.ISaveUserQuestionAnswerValueBMO;
import com.java110.utils.util.Assert;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("saveUserQuestionAnswerValueBMOImpl")
public class SaveUserQuestionAnswerValueBMOImpl implements ISaveUserQuestionAnswerValueBMO {

    @Autowired
    private IUserQuestionAnswerValueInnerServiceSMO userQuestionAnswerValueInnerServiceSMOImpl;

    @Autowired
    private IUserQuestionAnswerInnerServiceSMO userQuestionAnswerInnerServiceSMOImpl;

    @Autowired
    private IQuestionAnswerInnerServiceSMO questionAnswerInnerServiceSMOImpl;


    /**
     * 添加小区信息
     *
     * @param userQuestionAnswerValuePo
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> save(UserQuestionAnswerValuePo userQuestionAnswerValuePo, JSONArray questionAnswerTitles) {

        QuestionAnswerDto questionAnswerDto = new QuestionAnswerDto();
        questionAnswerDto.setObjId(userQuestionAnswerValuePo.getObjId());
        questionAnswerDto.setObjType(userQuestionAnswerValuePo.getObjType());
        questionAnswerDto.setQaId(userQuestionAnswerValuePo.getQaId());
        List<QuestionAnswerDto> questionAnswerDtos = questionAnswerInnerServiceSMOImpl.queryQuestionAnswers(questionAnswerDto);
        Assert.listOnlyOne(questionAnswerDtos, "问卷不存在");

        JSONObject titleObj = null;
        UserQuestionAnswerValuePo tmpUserUserQuestionAnswerValue = null;
        List<UserQuestionAnswerValuePo> tmpUserUserQuestionAnswerValues = new ArrayList<>();
        UserQuestionAnswerPo userQuestionAnswerPo = new UserQuestionAnswerPo();
        if(StringUtil.isEmpty(userQuestionAnswerValuePo.getUserQaId()) || userQuestionAnswerValuePo.getUserQaId().startsWith("-")){
            userQuestionAnswerPo.setUserQaId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_userQaId));
        }else{
            userQuestionAnswerPo.setUserQaId(userQuestionAnswerValuePo.getUserQaId());
        }
        for (int questionAnswerTitleIndex = 0; questionAnswerTitleIndex < questionAnswerTitles.size(); questionAnswerTitleIndex++) {
            titleObj = questionAnswerTitles.getJSONObject(questionAnswerTitleIndex);
            tmpUserUserQuestionAnswerValue = new UserQuestionAnswerValuePo();
            tmpUserUserQuestionAnswerValue.setAnswerType(userQuestionAnswerValuePo.getAnswerType());
            tmpUserUserQuestionAnswerValue.setObjId(userQuestionAnswerValuePo.getObjId());
            tmpUserUserQuestionAnswerValue.setObjType(userQuestionAnswerValuePo.getObjType());
            tmpUserUserQuestionAnswerValue.setPersonId(userQuestionAnswerValuePo.getPersonId());
            tmpUserUserQuestionAnswerValue.setQaId(userQuestionAnswerValuePo.getQaId());
            tmpUserUserQuestionAnswerValue.setScore("0");
            tmpUserUserQuestionAnswerValue.setTitleId(titleObj.getString("titleId"));
            tmpUserUserQuestionAnswerValue.setUserQaId(userQuestionAnswerPo.getUserQaId());
            tmpUserUserQuestionAnswerValue.setUserTitleId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_userTitleId));
            if ("3003".equals(titleObj.getString("titleType"))) {
                tmpUserUserQuestionAnswerValue.setValueId("999");
                tmpUserUserQuestionAnswerValue.setValueContent(titleObj.getString("valueContent"));
            } else {
                tmpUserUserQuestionAnswerValue.setValueId(titleObj.getString("valueContent"));
                tmpUserUserQuestionAnswerValue.setValueContent(titleObj.getString("valueContent"));
            }
            tmpUserUserQuestionAnswerValues.add(tmpUserUserQuestionAnswerValue);
        }

        int flag = userQuestionAnswerValueInnerServiceSMOImpl.saveUserQuestionAnswerValue(tmpUserUserQuestionAnswerValues);
        if (flag < 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
        }

        //如果是领导 评价 直接返回
        String answerType = userQuestionAnswerValuePo.getAnswerType();

        if("2003".equals(answerType)){
            userQuestionAnswerPo.setState("1202");
            userQuestionAnswerInnerServiceSMOImpl.updateUserQuestionAnswer(userQuestionAnswerPo);
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        userQuestionAnswerPo.setEvaluationScore("0");
        userQuestionAnswerPo.setObjId(userQuestionAnswerValuePo.getObjId());
        userQuestionAnswerPo.setObjType(userQuestionAnswerValuePo.getObjType());
        userQuestionAnswerPo.setPersonId(userQuestionAnswerValuePo.getPersonId());
        userQuestionAnswerPo.setQaId(userQuestionAnswerValuePo.getQaId());
        userQuestionAnswerPo.setScore("0");

        if ("2002".equals(questionAnswerDtos.get(0).getQaType())) {
            userQuestionAnswerPo.setState("1201");
        } else {
            userQuestionAnswerPo.setState("1202");
        }
        flag = userQuestionAnswerInnerServiceSMOImpl.saveUserQuestionAnswer(userQuestionAnswerPo);
        if (flag < 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");

        }
        return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
    }

}
