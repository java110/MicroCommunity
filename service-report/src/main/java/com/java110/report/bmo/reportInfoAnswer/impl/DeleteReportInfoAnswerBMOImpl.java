package com.java110.report.bmo.reportInfoAnswer.impl;

import com.java110.core.annotation.Java110Transactional;
import com.java110.intf.report.IReportInfoAnswerInnerServiceSMO;
import com.java110.po.reportInfoAnswer.ReportInfoAnswerPo;
import com.java110.report.bmo.reportInfoAnswer.IDeleteReportInfoAnswerBMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("deleteReportInfoAnswerBMOImpl")
public class DeleteReportInfoAnswerBMOImpl implements IDeleteReportInfoAnswerBMO {

    @Autowired
    private IReportInfoAnswerInnerServiceSMO reportInfoAnswerInnerServiceSMOImpl;

    /**
     * @param reportInfoAnswerPo 数据
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> delete(ReportInfoAnswerPo reportInfoAnswerPo) {

        int flag = reportInfoAnswerInnerServiceSMOImpl.deleteReportInfoAnswer(reportInfoAnswerPo);

        if (flag > 0) {
        return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
