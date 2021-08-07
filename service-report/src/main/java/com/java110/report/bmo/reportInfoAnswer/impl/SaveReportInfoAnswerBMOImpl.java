package com.java110.report.bmo.reportInfoAnswer.impl;

import com.java110.core.annotation.Java110Transactional;
import com.java110.core.factory.GenerateCodeFactory;

import com.java110.intf.report.IReportInfoAnswerInnerServiceSMO;
import com.java110.po.reportInfoAnswer.ReportInfoAnswerPo;
import com.java110.report.bmo.reportInfoAnswer.ISaveReportInfoAnswerBMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("saveReportInfoAnswerBMOImpl")
public class SaveReportInfoAnswerBMOImpl implements ISaveReportInfoAnswerBMO {

    @Autowired
    private IReportInfoAnswerInnerServiceSMO reportInfoAnswerInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param reportInfoAnswerPo
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> save(ReportInfoAnswerPo reportInfoAnswerPo) {

        reportInfoAnswerPo.setUserAnId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_userAnId));
        int flag = reportInfoAnswerInnerServiceSMOImpl.saveReportInfoAnswer(reportInfoAnswerPo);

        if (flag > 0) {
        return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
