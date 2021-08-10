package com.java110.report.bmo.reportInfoBackCity;
import com.java110.po.reportInfoBackCity.ReportInfoBackCityPo;
import org.springframework.http.ResponseEntity;

public interface IUpdateReportInfoBackCityBMO {


    /**
     * 修改返省上报
     * add by wuxw
     * @param reportInfoBackCityPo
     * @return
     */
    ResponseEntity<String> update(ReportInfoBackCityPo reportInfoBackCityPo);


}
