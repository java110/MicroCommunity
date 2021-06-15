package com.java110.report.bmo.reportOwnerPayFee.impl;

import com.java110.core.annotation.Java110Transactional;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.intf.report.IReportOwnerPayFeeInnerServiceSMO;
import com.java110.po.reportOwnerPayFee.ReportOwnerPayFeePo;
import com.java110.report.bmo.reportOwnerPayFee.ISaveReportOwnerPayFeeBMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("saveReportOwnerPayFeeBMOImpl")
public class SaveReportOwnerPayFeeBMOImpl implements ISaveReportOwnerPayFeeBMO {

    @Autowired
    private IReportOwnerPayFeeInnerServiceSMO reportOwnerPayFeeInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param reportOwnerPayFeePo
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> save(ReportOwnerPayFeePo reportOwnerPayFeePo) {

        reportOwnerPayFeePo.setPfId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_pfId));
        int flag = reportOwnerPayFeeInnerServiceSMOImpl.saveReportOwnerPayFee(reportOwnerPayFeePo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
