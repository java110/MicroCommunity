package com.java110.report.bmo.reportInfoSettingTitle.impl;

import com.java110.core.annotation.Java110Transactional;
import com.java110.intf.report.IReportInfoSettingTitleInnerServiceSMO;
import com.java110.po.reportInfoSettingTitle.ReportInfoSettingTitlePo;
import com.java110.report.bmo.reportInfoSettingTitle.IDeleteReportInfoSettingTitleBMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("deleteReportInfoSettingTitleBMOImpl")
public class DeleteReportInfoSettingTitleBMOImpl implements IDeleteReportInfoSettingTitleBMO {

    @Autowired
    private IReportInfoSettingTitleInnerServiceSMO reportInfoSettingTitleInnerServiceSMOImpl;

    /**
     * @param reportInfoSettingTitlePo 数据
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> delete(ReportInfoSettingTitlePo reportInfoSettingTitlePo) {

        int flag = reportInfoSettingTitleInnerServiceSMOImpl.deleteReportInfoSettingTitle(reportInfoSettingTitlePo);

        if (flag > 0) {
        return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
