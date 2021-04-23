package com.java110.report.bmo.reportFeeYearCollection.impl;

import com.java110.core.annotation.Java110Transactional;
import com.java110.intf.report.IReportFeeYearCollectionInnerServiceSMO;
import com.java110.po.reportFeeYearCollection.ReportFeeYearCollectionPo;
import com.java110.report.bmo.reportFeeYearCollection.IUpdateReportFeeYearCollectionBMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("updateReportFeeYearCollectionBMOImpl")
public class UpdateReportFeeYearCollectionBMOImpl implements IUpdateReportFeeYearCollectionBMO {

    @Autowired
    private IReportFeeYearCollectionInnerServiceSMO reportFeeYearCollectionInnerServiceSMOImpl;

    /**
     * @param reportFeeYearCollectionPo
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> update(ReportFeeYearCollectionPo reportFeeYearCollectionPo) {

        int flag = reportFeeYearCollectionInnerServiceSMOImpl.updateReportFeeYearCollection(reportFeeYearCollectionPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
