package com.java110.report.bmo.reportFeeMonthStatistics.impl;

import com.java110.core.annotation.Java110Transactional;
import com.java110.intf.report.IReportFeeMonthStatisticsInnerServiceSMO;
import com.java110.po.reportFeeMonthStatistics.ReportFeeMonthStatisticsPo;
import com.java110.report.bmo.reportFeeMonthStatistics.IDeleteReportFeeMonthStatisticsBMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("deleteReportFeeMonthStatisticsBMOImpl")
public class DeleteReportFeeMonthStatisticsBMOImpl implements IDeleteReportFeeMonthStatisticsBMO {

    @Autowired
    private IReportFeeMonthStatisticsInnerServiceSMO reportFeeMonthStatisticsInnerServiceSMOImpl;

    /**
     * @param reportFeeMonthStatisticsPo 数据
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> delete(ReportFeeMonthStatisticsPo reportFeeMonthStatisticsPo) {

        int flag = reportFeeMonthStatisticsInnerServiceSMOImpl.deleteReportFeeMonthStatistics(reportFeeMonthStatisticsPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
