package com.java110.report.bmo.reportInfoAnswerValue.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.reportInfoAnswerValue.ReportInfoAnswerValueDto;
import com.java110.dto.reportInfoSetting.ReportInfoSettingDto;
import com.java110.dto.reportInfoSettingTitleValue.ReportInfoSettingTitleValueDto;
import com.java110.dto.user.UserDto;
import com.java110.intf.report.IReportInfoAnswerInnerServiceSMO;
import com.java110.intf.report.IReportInfoAnswerValueInnerServiceSMO;
import com.java110.intf.report.IReportInfoSettingInnerServiceSMO;
import com.java110.intf.report.IReportInfoSettingTitleValueInnerServiceSMO;
import com.java110.intf.user.IUserInnerServiceSMO;
import com.java110.po.reportInfoAnswer.ReportInfoAnswerPo;
import com.java110.po.reportInfoAnswerValue.ReportInfoAnswerValuePo;
import com.java110.po.userQuestionAnswer.UserQuestionAnswerPo;
import com.java110.report.bmo.reportInfoAnswerValue.ISaveReportInfoAnswerValueBMO;
import com.java110.utils.util.Assert;
import com.java110.utils.util.StringUtil;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("saveReportInfoAnswerValueBMOImpl")
public class SaveReportInfoAnswerValueBMOImpl implements ISaveReportInfoAnswerValueBMO {

    @Autowired
    private IReportInfoAnswerValueInnerServiceSMO reportInfoAnswerValueInnerServiceSMOImpl;


    @Autowired
    private IReportInfoSettingInnerServiceSMO reportInfoSettingInnerServiceSMOImpl;

    @Autowired
    private IReportInfoAnswerInnerServiceSMO reportInfoAnswerInnerServiceSMOImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;
    @Autowired
    private IReportInfoSettingTitleValueInnerServiceSMO reportInfoSettingTitleValueInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> save(JSONObject reqJson,String userId) {

       /* UserDto userDto = new UserDto();
        userDto.setUserId(userId);
        List<UserDto> userDtoList = userInnerServiceSMOImpl.getUsers(userDto);
        if (userDtoList == null || userDtoList.size() < 1) {
            throw new IllegalArgumentException("查询用户信息失败！");
        }*/
        ReportInfoAnswerPo reportInfoAnswerPo = new ReportInfoAnswerPo();
        reportInfoAnswerPo.setUserAnId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_userAnId));
        reportInfoAnswerPo.setSettingId(reqJson.getString("settingId"));
        reportInfoAnswerPo.setPersonId("-1");
        reportInfoAnswerPo.setPersonName("未知");
        reportInfoAnswerPo.setCommunityId(reqJson.getString("communityId"));
        int flag = reportInfoAnswerInnerServiceSMOImpl.saveReportInfoAnswer(reportInfoAnswerPo);
        if (flag < 1) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存失败");
        }
        JSONArray questionAnswerTitles = reqJson.getJSONArray("questionAnswerTitles");

        if (questionAnswerTitles == null || questionAnswerTitles.size() < 1) {
            throw new IllegalArgumentException("未包含题目及答案");
        }
        JSONObject titleObj = null;
        for (int questionAnswerTitleIndex = 0; questionAnswerTitleIndex < questionAnswerTitles.size(); questionAnswerTitleIndex++) {
            titleObj = questionAnswerTitles.getJSONObject(questionAnswerTitleIndex);
            String titleType = titleObj.getString("titleType");
            if(ReportInfoAnswerValueDto.TITLETYPE_CHECKBOX.equals(titleType)){
                JSONArray valueContent = titleObj.getJSONArray("valueContent");
                if(valueContent == null || valueContent.size() < 1){
                    throw new IllegalArgumentException("多选题目未包含答案");
                }
                for(int checkBoxIndex = 0;checkBoxIndex<valueContent.size();checkBoxIndex++){
                    ReportInfoAnswerValuePo reportInfoAnswerValuePo = new ReportInfoAnswerValuePo();
                    reportInfoAnswerValuePo.setAnValueId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_anValueId));
                    reportInfoAnswerValuePo.setUserAnId(reportInfoAnswerPo.getUserAnId());
                    reportInfoAnswerValuePo.setValueId(valueContent.getString(checkBoxIndex));
                    reportInfoAnswerValuePo.setSettingId(reqJson.getString("settingId"));
                    reportInfoAnswerValuePo.setTitleId(titleObj.getString("titleId"));
                    reportInfoAnswerValuePo.setCommunityId(titleObj.getString("communityId"));
                    ReportInfoSettingTitleValueDto reportInfoSettingTitleValueDto = new ReportInfoSettingTitleValueDto();


                    reportInfoSettingTitleValueDto.setCommunityId(titleObj.getString("communityId"));
                    reportInfoSettingTitleValueDto.setTitleId(titleObj.getString("titleId"));
                    reportInfoSettingTitleValueDto.setValueId(valueContent.getString(checkBoxIndex));
                    List<ReportInfoSettingTitleValueDto> reportInfoSettingTitleValueDtos = reportInfoSettingTitleValueInnerServiceSMOImpl.queryReportInfoSettingTitleValues(reportInfoSettingTitleValueDto);
                    if(reportInfoSettingTitleValueDtos == null || reportInfoSettingTitleValueDtos.size() < 1){
                        throw new IllegalArgumentException("多选题目未查询到答案信息");
                    }
                   if (valueContent.getString(checkBoxIndex).equals(reportInfoSettingTitleValueDtos.get(0).getValueId())) {
                       reportInfoAnswerValuePo.setValueContent(reportInfoSettingTitleValueDtos.get(0).getQaValue());
                   }
                    flag = reportInfoAnswerValueInnerServiceSMOImpl.saveReportInfoAnswerValue(reportInfoAnswerValuePo);
                    if (flag < 1) {
                        return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存失败");
                    }
                }
            }
            if(ReportInfoAnswerValueDto.TITLETYPE_RADIO.equals(titleType)){
                String valueContent = titleObj.getString("valueContent");
                if(valueContent == null || "".equals(valueContent)){
                    throw new IllegalArgumentException("单选题目未包含答案");
                }
                ReportInfoAnswerValuePo reportInfoAnswerValuePo = new ReportInfoAnswerValuePo();
                reportInfoAnswerValuePo.setAnValueId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_anValueId));
                reportInfoAnswerValuePo.setUserAnId(reportInfoAnswerPo.getUserAnId());
                reportInfoAnswerValuePo.setValueId(valueContent);
                reportInfoAnswerValuePo.setTitleId(titleObj.getString("titleId"));
                reportInfoAnswerValuePo.setCommunityId(titleObj.getString("communityId"));
                reportInfoAnswerValuePo.setSettingId(reqJson.getString("settingId"));
                ReportInfoSettingTitleValueDto reportInfoSettingTitleValueDto = new ReportInfoSettingTitleValueDto();


                reportInfoSettingTitleValueDto.setCommunityId(titleObj.getString("communityId"));
                reportInfoSettingTitleValueDto.setTitleId(titleObj.getString("titleId"));
                reportInfoSettingTitleValueDto.setValueId(valueContent);
                List<ReportInfoSettingTitleValueDto> reportInfoSettingTitleValueDtos = reportInfoSettingTitleValueInnerServiceSMOImpl.queryReportInfoSettingTitleValues(reportInfoSettingTitleValueDto);
                if(reportInfoSettingTitleValueDtos == null || reportInfoSettingTitleValueDtos.size() < 1){
                    throw new IllegalArgumentException("多选题目未查询到答案信息");
                }
                if (valueContent.equals(reportInfoSettingTitleValueDtos.get(0).getValueId())) {
                    reportInfoAnswerValuePo.setValueContent(reportInfoSettingTitleValueDtos.get(0).getQaValue());
                }
                flag = reportInfoAnswerValueInnerServiceSMOImpl.saveReportInfoAnswerValue(reportInfoAnswerValuePo);
                if (flag < 1) {
                    return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存失败");
                }
            }
            if(ReportInfoAnswerValueDto.ITLETYPE_TEXTAREA.equals(titleType)){
                String valueContent = titleObj.getString("valueContent");
                if(valueContent == null || "".equals(valueContent)){
                    throw new IllegalArgumentException("单选题目未包含答案");
                }
                ReportInfoAnswerValuePo reportInfoAnswerValuePo = new ReportInfoAnswerValuePo();
                reportInfoAnswerValuePo.setAnValueId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_anValueId));
                reportInfoAnswerValuePo.setUserAnId(reportInfoAnswerPo.getUserAnId());
                reportInfoAnswerValuePo.setValueId(valueContent);
                reportInfoAnswerValuePo.setSettingId(reqJson.getString("settingId"));
                reportInfoAnswerValuePo.setTitleId(titleObj.getString("titleId"));
                reportInfoAnswerValuePo.setCommunityId(titleObj.getString("communityId"));
                reportInfoAnswerValuePo.setValueContent(valueContent);

                flag = reportInfoAnswerValueInnerServiceSMOImpl.saveReportInfoAnswerValue(reportInfoAnswerValuePo);
                if (flag < 1) {
                    return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存失败");
                }
            }

            if (flag > 0) {
                return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
            }
        }


        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
