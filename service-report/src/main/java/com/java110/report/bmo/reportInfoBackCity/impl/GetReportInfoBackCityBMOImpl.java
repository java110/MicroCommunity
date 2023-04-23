package com.java110.report.bmo.reportInfoBackCity.impl;

import com.java110.dto.reportInfoAnswer.ReportInfoBackCityDto;
import com.java110.intf.report.IReportInfoBackCityInnerServiceSMO;
import com.java110.report.bmo.reportInfoBackCity.IGetReportInfoBackCityBMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("getReportInfoBackCityBMOImpl")
public class GetReportInfoBackCityBMOImpl implements IGetReportInfoBackCityBMO {

    @Autowired
    private IReportInfoBackCityInnerServiceSMO reportInfoBackCityInnerServiceSMOImpl;

    /**
     * @param reportInfoBackCityDto
     * @return 订单服务能够接受的报文
     */
    public ResponseEntity<String> get(ReportInfoBackCityDto reportInfoBackCityDto) {


        int count = reportInfoBackCityInnerServiceSMOImpl.queryReportInfoBackCitysCount(reportInfoBackCityDto);

        List<ReportInfoBackCityDto> reportInfoBackCityDtos = null;
        if (count > 0) {
            reportInfoBackCityDtos = reportInfoBackCityInnerServiceSMOImpl.queryReportInfoBackCitys(reportInfoBackCityDto);
        } else {
            reportInfoBackCityDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) reportInfoBackCityDto.getRow()), count, reportInfoBackCityDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

}
