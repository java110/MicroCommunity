package com.java110.report.bmo.reportFeeMonthCollectionDetail.impl;

import com.java110.core.annotation.Java110Transactional;
import com.java110.intf.report.IReportFeeMonthCollectionDetailInnerServiceSMO;
import com.java110.po.reportFeeMonthCollectionDetail.ReportFeeMonthCollectionDetailPo;
import com.java110.report.bmo.reportFeeMonthCollectionDetail.IDeleteReportFeeMonthCollectionDetailBMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("deleteReportFeeMonthCollectionDetailBMOImpl")
public class DeleteReportFeeMonthCollectionDetailBMOImpl implements IDeleteReportFeeMonthCollectionDetailBMO {

    @Autowired
    private IReportFeeMonthCollectionDetailInnerServiceSMO reportFeeMonthCollectionDetailInnerServiceSMOImpl;

    /**
     * @param reportFeeMonthCollectionDetailPo 数据
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> delete(ReportFeeMonthCollectionDetailPo reportFeeMonthCollectionDetailPo) {

        int flag = reportFeeMonthCollectionDetailInnerServiceSMOImpl.deleteReportFeeMonthCollectionDetail(reportFeeMonthCollectionDetailPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
