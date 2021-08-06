package com.java110.report.bmo.reportInfoBackCity.impl;

import com.java110.core.annotation.Java110Transactional;
import com.java110.intf.report.IReportInfoBackCityInnerServiceSMO;
import com.java110.po.reportInfoBackCity.ReportInfoBackCityPo;
import com.java110.report.bmo.reportInfoBackCity.IUpdateReportInfoBackCityBMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("updateReportInfoBackCityBMOImpl")
public class UpdateReportInfoBackCityBMOImpl implements IUpdateReportInfoBackCityBMO {

    @Autowired
    private IReportInfoBackCityInnerServiceSMO reportInfoBackCityInnerServiceSMOImpl;

    /**
     * @param reportInfoBackCityPo
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> update(ReportInfoBackCityPo reportInfoBackCityPo) {

        int flag = reportInfoBackCityInnerServiceSMOImpl.updateReportInfoBackCity(reportInfoBackCityPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
