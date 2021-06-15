package com.java110.report.bmo.reportFeeYearCollection.impl;

import com.java110.core.annotation.Java110Transactional;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.intf.report.IReportFeeYearCollectionInnerServiceSMO;
import com.java110.po.reportFeeYearCollection.ReportFeeYearCollectionPo;
import com.java110.report.bmo.reportFeeYearCollection.ISaveReportFeeYearCollectionBMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("saveReportFeeYearCollectionBMOImpl")
public class SaveReportFeeYearCollectionBMOImpl implements ISaveReportFeeYearCollectionBMO {

    @Autowired
    private IReportFeeYearCollectionInnerServiceSMO reportFeeYearCollectionInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param reportFeeYearCollectionPo
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> save(ReportFeeYearCollectionPo reportFeeYearCollectionPo) {

        reportFeeYearCollectionPo.setCollectionId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_collectionId));
        int flag = reportFeeYearCollectionInnerServiceSMOImpl.saveReportFeeYearCollection(reportFeeYearCollectionPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
