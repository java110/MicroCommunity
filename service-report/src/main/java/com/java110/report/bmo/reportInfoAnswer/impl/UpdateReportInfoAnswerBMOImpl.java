package com.java110.report.bmo.reportInfoAnswer.impl;

import com.java110.core.annotation.Java110Transactional;
import com.java110.intf.report.IReportInfoAnswerInnerServiceSMO;
import com.java110.po.reportInfoAnswer.ReportInfoAnswerPo;
import com.java110.report.bmo.reportInfoAnswer.IUpdateReportInfoAnswerBMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@Service("updateReportInfoAnswerBMOImpl")
public class UpdateReportInfoAnswerBMOImpl implements IUpdateReportInfoAnswerBMO {

    @Autowired
    private IReportInfoAnswerInnerServiceSMO reportInfoAnswerInnerServiceSMOImpl;

    /**
     *
     *
     * @param reportInfoAnswerPo
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> update(ReportInfoAnswerPo reportInfoAnswerPo) {

        int flag = reportInfoAnswerInnerServiceSMOImpl.updateReportInfoAnswer(reportInfoAnswerPo);

        if (flag > 0) {
        return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
