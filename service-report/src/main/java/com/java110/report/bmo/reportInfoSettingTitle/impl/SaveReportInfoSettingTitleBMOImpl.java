package com.java110.report.bmo.reportInfoSettingTitle.impl;

import com.alibaba.fastjson.JSONArray;
import com.java110.core.annotation.Java110Transactional;
import com.java110.core.factory.GenerateCodeFactory;
import com.java110.dto.reportInfoSetting.ReportInfoSettingTitleDto;
import com.java110.intf.report.IReportInfoSettingTitleInnerServiceSMO;
import com.java110.intf.report.IReportInfoSettingTitleValueInnerServiceSMO;
import com.java110.po.reportInfoSettingTitle.ReportInfoSettingTitlePo;
import com.java110.po.reportInfoSettingTitleValue.ReportInfoSettingTitleValuePo;
import com.java110.report.bmo.reportInfoSettingTitle.ISaveReportInfoSettingTitleBMO;
import com.java110.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("saveReportInfoSettingTitleBMOImpl")
public class SaveReportInfoSettingTitleBMOImpl implements ISaveReportInfoSettingTitleBMO {

    @Autowired
    private IReportInfoSettingTitleInnerServiceSMO reportInfoSettingTitleInnerServiceSMOImpl;
    @Autowired
    private IReportInfoSettingTitleValueInnerServiceSMO reportInfoSettingTitleValueInnerServiceSMOImpl;
    /**
     * 添加小区信息
     *
     * @param reportInfoSettingTitlePo
     * @return 订单服务能够接受的报文
     */
    @Java110Transactional
    public ResponseEntity<String> save(ReportInfoSettingTitlePo reportInfoSettingTitlePo, JSONArray titleValues) {

        reportInfoSettingTitlePo.setTitleId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_titleId));
        int flag = reportInfoSettingTitleInnerServiceSMOImpl.saveReportInfoSettingTitle(reportInfoSettingTitlePo);

        if (flag < 0) {
        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
        }
        if (ReportInfoSettingTitleDto.TITLE_TYPE_QUESTIONS.equals(reportInfoSettingTitlePo.getTitleType())) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }
        ReportInfoSettingTitleValuePo reportInfoSettingTitleValuePo = null;
        for (int titleValueIndex = 0; titleValueIndex < titleValues.size(); titleValueIndex++) {
            reportInfoSettingTitleValuePo = new ReportInfoSettingTitleValuePo();
            reportInfoSettingTitleValuePo.setQaValue(titleValues.getJSONObject(titleValueIndex).getString("qaValue"));
            reportInfoSettingTitleValuePo.setSeq(titleValues.getJSONObject(titleValueIndex).getString("seq"));
            reportInfoSettingTitleValuePo.setTitleId(reportInfoSettingTitlePo.getTitleId());
            reportInfoSettingTitleValuePo.setValueId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_valueId));
            reportInfoSettingTitleValuePo.setCommunityId(reportInfoSettingTitlePo.getCommunityId());
            reportInfoSettingTitleValueInnerServiceSMOImpl.saveReportInfoSettingTitleValue(reportInfoSettingTitleValuePo);
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
    }

}
