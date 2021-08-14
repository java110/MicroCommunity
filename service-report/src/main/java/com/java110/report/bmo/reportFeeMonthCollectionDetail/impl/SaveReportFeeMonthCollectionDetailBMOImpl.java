package com.java110.report.bmo.reportFeeMonthCollectionDetail.impl;

import com.java110.core.annotation.Java110Transactional;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.intf.report.IReportFeeMonthCollectionDetailInnerServiceSMO;
import com.java110.po.reportFeeMonthCollectionDetail.ReportFeeMonthCollectionDetailPo;
import com.java110.report.bmo.reportFeeMonthCollectionDetail.ISaveReportFeeMonthCollectionDetailBMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("saveReportFeeMonthCollectionDetailBMOImpl")
public class SaveReportFeeMonthCollectionDetailBMOImpl implements ISaveReportFeeMonthCollectionDetailBMO {

    @Autowired
    private IReportFeeMonthCollectionDetailInnerServiceSMO reportFeeMonthCollectionDetailInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param reportFeeMonthCollectionDetailPo
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> save(ReportFeeMonthCollectionDetailPo reportFeeMonthCollectionDetailPo) {

        reportFeeMonthCollectionDetailPo.setCdId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_cdId));
        int flag = reportFeeMonthCollectionDetailInnerServiceSMOImpl.saveReportFeeMonthCollectionDetail(reportFeeMonthCollectionDetailPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
