package com.java110.report.bmo.reportInfoSetting.impl;

import com.java110.core.annotation.Java110Transactional;
import com.java110.intf.report.IReportInfoSettingInnerServiceSMO;
import com.java110.po.reportInfoSetting.ReportInfoSettingPo;
import com.java110.report.bmo.reportInfoSetting.IUpdateReportInfoSettingBMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("updateReportInfoSettingBMOImpl")
public class UpdateReportInfoSettingBMOImpl implements IUpdateReportInfoSettingBMO {

    @Autowired
    private IReportInfoSettingInnerServiceSMO reportInfoSettingInnerServiceSMOImpl;

    /**
     *
     *
     * @param reportInfoSettingPo
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> update(ReportInfoSettingPo reportInfoSettingPo) {

        int flag = reportInfoSettingInnerServiceSMOImpl.updateReportInfoSetting(reportInfoSettingPo);

        if (flag > 0) {
        return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
