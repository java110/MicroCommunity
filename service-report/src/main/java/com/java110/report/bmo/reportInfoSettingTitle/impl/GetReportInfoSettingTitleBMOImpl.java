package com.java110.report.bmo.reportInfoSettingTitle.impl;

import com.java110.intf.report.IReportInfoSettingTitleInnerServiceSMO;
import com.java110.report.bmo.reportInfoSettingTitle.IGetReportInfoSettingTitleBMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.java110.dto.reportInfoSettingTitle.ReportInfoSettingTitleDto;

import java.util.ArrayList;
import java.util.List;

@Service("getReportInfoSettingTitleBMOImpl")
public class GetReportInfoSettingTitleBMOImpl implements IGetReportInfoSettingTitleBMO {

    @Autowired
    private IReportInfoSettingTitleInnerServiceSMO reportInfoSettingTitleInnerServiceSMOImpl;

    /**
     *
     *
     * @param  reportInfoSettingTitleDto
     * @return 订单服务能够接受的报文
     */
    public ResponseEntity<String> get(ReportInfoSettingTitleDto reportInfoSettingTitleDto) {


        int count = reportInfoSettingTitleInnerServiceSMOImpl.queryReportInfoSettingTitlesCount(reportInfoSettingTitleDto);

        List<ReportInfoSettingTitleDto> reportInfoSettingTitleDtos = null;
        if (count > 0) {
            reportInfoSettingTitleDtos = reportInfoSettingTitleInnerServiceSMOImpl.queryReportInfoSettingTitles(reportInfoSettingTitleDto);
        } else {
            reportInfoSettingTitleDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) reportInfoSettingTitleDto.getRow()), count, reportInfoSettingTitleDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

}
