package com.java110.report.bmo.reportInfoSettingTitleValue.impl;

import com.java110.core.annotation.Java110Transactional;
import com.java110.intf.report.IReportInfoSettingTitleValueInnerServiceSMO;
import com.java110.po.reportInfoSettingTitleValue.ReportInfoSettingTitleValuePo;
import com.java110.report.bmo.reportInfoSettingTitleValue.IUpdateReportInfoSettingTitleValueBMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@Service("updateReportInfoSettingTitleValueBMOImpl")
public class UpdateReportInfoSettingTitleValueBMOImpl implements IUpdateReportInfoSettingTitleValueBMO {

    @Autowired
    private IReportInfoSettingTitleValueInnerServiceSMO reportInfoSettingTitleValueInnerServiceSMOImpl;

    /**
     *
     *
     * @param reportInfoSettingTitleValuePo
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> update(ReportInfoSettingTitleValuePo reportInfoSettingTitleValuePo) {

        int flag = reportInfoSettingTitleValueInnerServiceSMOImpl.updateReportInfoSettingTitleValue(reportInfoSettingTitleValuePo);

        if (flag > 0) {
        return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
