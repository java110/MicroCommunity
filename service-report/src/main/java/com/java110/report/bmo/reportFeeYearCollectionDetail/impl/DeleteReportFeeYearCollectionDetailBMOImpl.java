package com.java110.report.bmo.reportFeeYearCollectionDetail.impl;

import com.java110.core.annotation.Java110Transactional;
import com.java110.intf.report.IReportFeeYearCollectionDetailInnerServiceSMO;
import com.java110.po.reportFeeYearCollectionDetail.ReportFeeYearCollectionDetailPo;
import com.java110.report.bmo.reportFeeYearCollectionDetail.IDeleteReportFeeYearCollectionDetailBMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("deleteReportFeeYearCollectionDetailBMOImpl")
public class DeleteReportFeeYearCollectionDetailBMOImpl implements IDeleteReportFeeYearCollectionDetailBMO {

    @Autowired
    private IReportFeeYearCollectionDetailInnerServiceSMO reportFeeYearCollectionDetailInnerServiceSMOImpl;

    /**
     * @param reportFeeYearCollectionDetailPo 数据
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> delete(ReportFeeYearCollectionDetailPo reportFeeYearCollectionDetailPo) {

        int flag = reportFeeYearCollectionDetailInnerServiceSMOImpl.deleteReportFeeYearCollectionDetail(reportFeeYearCollectionDetailPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
