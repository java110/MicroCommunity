package com.java110.report.bmo.reportInfoBackCity;

import com.java110.dto.reportInfoAnswer.ReportInfoBackCityDto;
import org.springframework.http.ResponseEntity;

public interface IGetReportInfoBackCityBMO {

    /**
     * 查询返省上报
     * add by wuxw
     *
     * @param reportInfoBackCityDto
     * @return
     */
    ResponseEntity<String> get(ReportInfoBackCityDto reportInfoBackCityDto);

}
