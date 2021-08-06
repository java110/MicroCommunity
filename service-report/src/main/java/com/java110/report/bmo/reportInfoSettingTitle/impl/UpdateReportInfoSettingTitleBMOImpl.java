package com.java110.report.bmo.reportInfoSettingTitle.impl;

import com.java110.core.annotation.Java110Transactional;
import com.java110.intf.report.IReportInfoSettingTitleInnerServiceSMO;
import com.java110.po.reportInfoSettingTitle.ReportInfoSettingTitlePo;
import com.java110.report.bmo.reportInfoSettingTitle.IUpdateReportInfoSettingTitleBMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("updateReportInfoSettingTitleBMOImpl")
public class UpdateReportInfoSettingTitleBMOImpl implements IUpdateReportInfoSettingTitleBMO {

    @Autowired
    private IReportInfoSettingTitleInnerServiceSMO reportInfoSettingTitleInnerServiceSMOImpl;

    /**
     *
     *
     * @param reportInfoSettingTitlePo
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> update(ReportInfoSettingTitlePo reportInfoSettingTitlePo) {

        int flag = reportInfoSettingTitleInnerServiceSMOImpl.updateReportInfoSettingTitle(reportInfoSettingTitlePo);

        if (flag > 0) {
        return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
