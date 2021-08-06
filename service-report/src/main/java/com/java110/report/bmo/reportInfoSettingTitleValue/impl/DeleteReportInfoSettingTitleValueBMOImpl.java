package com.java110.report.bmo.reportInfoSettingTitleValue.impl;

import com.java110.core.annotation.Java110Transactional;
import com.java110.intf.report.IReportInfoSettingTitleValueInnerServiceSMO;
import com.java110.po.reportInfoSettingTitleValue.ReportInfoSettingTitleValuePo;
import com.java110.report.bmo.reportInfoSettingTitleValue.IDeleteReportInfoSettingTitleValueBMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@Service("deleteReportInfoSettingTitleValueBMOImpl")
public class DeleteReportInfoSettingTitleValueBMOImpl implements IDeleteReportInfoSettingTitleValueBMO {

    @Autowired
    private IReportInfoSettingTitleValueInnerServiceSMO reportInfoSettingTitleValueInnerServiceSMOImpl;

    /**
     * @param reportInfoSettingTitleValuePo 数据
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> delete(ReportInfoSettingTitleValuePo reportInfoSettingTitleValuePo) {

        int flag = reportInfoSettingTitleValueInnerServiceSMOImpl.deleteReportInfoSettingTitleValue(reportInfoSettingTitleValuePo);

        if (flag > 0) {
        return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
