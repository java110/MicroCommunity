package com.java110.report.bmo.reportInfoSetting.impl;

import com.java110.intf.report.IReportInfoSettingInnerServiceSMO;
import com.java110.report.bmo.reportInfoSetting.IGetReportInfoSettingBMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.java110.dto.reportInfoSetting.ReportInfoSettingDto;

import java.util.ArrayList;
import java.util.List;

@Service("getReportInfoSettingBMOImpl")
public class GetReportInfoSettingBMOImpl implements IGetReportInfoSettingBMO {

    @Autowired
    private IReportInfoSettingInnerServiceSMO reportInfoSettingInnerServiceSMOImpl;

    /**
     *
     *
     * @param  reportInfoSettingDto
     * @return 订单服务能够接受的报文
     */
    public ResponseEntity<String> get(ReportInfoSettingDto reportInfoSettingDto) {

        int count = reportInfoSettingInnerServiceSMOImpl.queryReportInfoSettingsCount(reportInfoSettingDto);

        List<ReportInfoSettingDto> reportInfoSettingDtos = null;
        if (count > 0) {
            reportInfoSettingDtos = reportInfoSettingInnerServiceSMOImpl.queryReportInfoSettings(reportInfoSettingDto);
        } else {
            reportInfoSettingDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) reportInfoSettingDto.getRow()), count, reportInfoSettingDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

}
