package com.java110.report.bmo.reportInfoAnswerValue.impl;

import com.java110.core.annotation.Java110Transactional;

import com.java110.intf.report.IReportInfoAnswerValueInnerServiceSMO;
import com.java110.po.reportInfoAnswerValue.ReportInfoAnswerValuePo;
import com.java110.report.bmo.reportInfoAnswerValue.IDeleteReportInfoAnswerValueBMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@Service("deleteReportInfoAnswerValueBMOImpl")
public class DeleteReportInfoAnswerValueBMOImpl implements IDeleteReportInfoAnswerValueBMO {

    @Autowired
    private IReportInfoAnswerValueInnerServiceSMO reportInfoAnswerValueInnerServiceSMOImpl;

    /**
     * @param reportInfoAnswerValuePo 数据
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> delete(ReportInfoAnswerValuePo reportInfoAnswerValuePo) {

        int flag = reportInfoAnswerValueInnerServiceSMOImpl.deleteReportInfoAnswerValue(reportInfoAnswerValuePo);

        if (flag > 0) {
        return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
