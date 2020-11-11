package com.java110.report.bmo.reportFeeMonthStatistics.impl;

import com.java110.core.annotation.Java110Transactional;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.intf.report.IReportFeeMonthStatisticsInnerServiceSMO;
import com.java110.po.reportFeeMonthStatistics.ReportFeeMonthStatisticsPo;
import com.java110.report.bmo.reportFeeMonthStatistics.ISaveReportFeeMonthStatisticsBMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("saveReportFeeMonthStatisticsBMOImpl")
public class SaveReportFeeMonthStatisticsBMOImpl implements ISaveReportFeeMonthStatisticsBMO {

    @Autowired
    private IReportFeeMonthStatisticsInnerServiceSMO reportFeeMonthStatisticsInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param reportFeeMonthStatisticsPo
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> save(ReportFeeMonthStatisticsPo reportFeeMonthStatisticsPo) {

        reportFeeMonthStatisticsPo.setStatisticsId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_statisticsId));
        int flag = reportFeeMonthStatisticsInnerServiceSMOImpl.saveReportFeeMonthStatistics(reportFeeMonthStatisticsPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
