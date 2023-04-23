package com.java110.report.bmo.reportInfoSettingTitleValue.impl;

import com.java110.intf.report.IReportInfoSettingTitleValueInnerServiceSMO;
import com.java110.report.bmo.reportInfoSettingTitleValue.IGetReportInfoSettingTitleValueBMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.java110.dto.reportInfoSetting.ReportInfoSettingTitleValueDto;

import java.util.ArrayList;
import java.util.List;

@Service("getReportInfoSettingTitleValueBMOImpl")
public class GetReportInfoSettingTitleValueBMOImpl implements IGetReportInfoSettingTitleValueBMO {

    @Autowired
    private IReportInfoSettingTitleValueInnerServiceSMO reportInfoSettingTitleValueInnerServiceSMOImpl;

    /**
     *
     *
     * @param  reportInfoSettingTitleValueDto
     * @return 订单服务能够接受的报文
     */
    public ResponseEntity<String> get(ReportInfoSettingTitleValueDto reportInfoSettingTitleValueDto) {


        int count = reportInfoSettingTitleValueInnerServiceSMOImpl.queryReportInfoSettingTitleValuesCount(reportInfoSettingTitleValueDto);

        List<ReportInfoSettingTitleValueDto> reportInfoSettingTitleValueDtos = null;
        if (count > 0) {
            reportInfoSettingTitleValueDtos = reportInfoSettingTitleValueInnerServiceSMOImpl.queryReportInfoSettingTitleValues(reportInfoSettingTitleValueDto);
        } else {
            reportInfoSettingTitleValueDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) reportInfoSettingTitleValueDto.getRow()), count, reportInfoSettingTitleValueDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

    /**
     *
     *
     * @param  reportInfoSettingTitleValueDto
     * @return 订单服务能够接受的报文
     */
    public ResponseEntity<String> getReportInfoSettingTitleValueInfoResult(ReportInfoSettingTitleValueDto reportInfoSettingTitleValueDto) {


        List<ReportInfoSettingTitleValueDto> reportInfoSettingTitleValueDtos =  reportInfoSettingTitleValueDtos = reportInfoSettingTitleValueInnerServiceSMOImpl.getReportInfoSettingTitleValueInfoResult(reportInfoSettingTitleValueDto);
        return ResultVo.createResponseEntity(reportInfoSettingTitleValueDtos);
    }

}
