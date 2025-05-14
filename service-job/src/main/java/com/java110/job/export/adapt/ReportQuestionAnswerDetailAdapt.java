package com.java110.job.export.adapt;

import com.alibaba.fastjson.JSONObject;
import com.java110.dto.data.ExportDataDto;
import com.java110.dto.user.UserQuestionAnswerValueDto;
import com.java110.intf.report.IReportUserQuestionAnswerValueInnerServiceSMO;
import com.java110.job.export.IExportDataAdapt;
import com.java110.utils.util.BeanConvertUtil;
import com.java110.utils.util.ListUtil;
import com.java110.utils.util.StringUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Service("reportQuestionAnswerDetail")
public class ReportQuestionAnswerDetailAdapt implements IExportDataAdapt {

    @Autowired
    private IReportUserQuestionAnswerValueInnerServiceSMO reportUserQuestionAnswerValueInnerServiceSMOImpl;


    @Override
    public SXSSFWorkbook exportData(ExportDataDto exportDataDto) throws ParseException {
        JSONObject reqJson = exportDataDto.getReqJson();
        SXSSFWorkbook workbook = null;  //工作簿
        //工作表
        workbook = new SXSSFWorkbook();
        workbook.setCompressTempFiles(false);
        Sheet sheet = workbook.createSheet("问卷明细表");
        Row row = sheet.createRow(0);
        row.createCell(0).setCellValue("问卷人");
        row.createCell(1).setCellValue("问卷类型");
        row.createCell(2).setCellValue("问卷名称");
        row.createCell(3).setCellValue("问卷题目");
        row.createCell(4).setCellValue("答案");
        row.createCell(5).setCellValue("时间");

        List<UserQuestionAnswerValueDto> questionAnswers = this.getReportQuestionAnswerDetail(reqJson);
        if (ListUtil.isNull(questionAnswers)) {
            return workbook;
        }
        UserQuestionAnswerValueDto dataObj = null;
        for (int roomIndex = 0; roomIndex < questionAnswers.size(); roomIndex++) {
            row = sheet.createRow(roomIndex + 1);
            dataObj = questionAnswers.get(roomIndex);
            row.createCell(0).setCellValue(dataObj.getUserName());
            row.createCell(1).setCellValue(dataObj.getQaTypeName());
            row.createCell(2).setCellValue(dataObj.getQaName());
            row.createCell(3).setCellValue(dataObj.getQaTitle());
            row.createCell(4).setCellValue(dataObj.getQaValue());
            row.createCell(5).setCellValue(dataObj.getCreateTime());
        }
        return workbook;
    }

    private List<UserQuestionAnswerValueDto> getReportQuestionAnswerDetail(JSONObject reqJson) {

        UserQuestionAnswerValueDto userQuestionAnswerValueDto = BeanConvertUtil.covertBean(reqJson, UserQuestionAnswerValueDto.class);
        userQuestionAnswerValueDto.setPage(1);
        userQuestionAnswerValueDto.setRow(10000);

        int count = reportUserQuestionAnswerValueInnerServiceSMOImpl.queryUserQuestionAnswerValuesCount(userQuestionAnswerValueDto);

        List<UserQuestionAnswerValueDto> userQuestionAnswerValueDtos = null;
        if (count > 0) {
            userQuestionAnswerValueDtos = reportUserQuestionAnswerValueInnerServiceSMOImpl.queryUserQuestionAnswerValues(userQuestionAnswerValueDto);

            refreshQuestionAnswerValue(userQuestionAnswerValueDtos);
        } else {
            userQuestionAnswerValueDtos = new ArrayList<>();
        }

        return userQuestionAnswerValueDtos;
    }

    private void refreshQuestionAnswerValue(List<UserQuestionAnswerValueDto> userQuestionAnswerValueDtos) {

        if (userQuestionAnswerValueDtos == null || userQuestionAnswerValueDtos.size() < 1) {
            return;
        }

        for (UserQuestionAnswerValueDto userQuestionAnswerValueDto : userQuestionAnswerValueDtos) {
            if (StringUtil.isEmpty(userQuestionAnswerValueDto.getValueContent())) {
                userQuestionAnswerValueDto.setValueContent(userQuestionAnswerValueDto.getValueContent());
            }
        }
    }
}
