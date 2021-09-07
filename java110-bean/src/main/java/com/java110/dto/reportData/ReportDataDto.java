package com.java110.dto.reportData;

import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;

public class ReportDataDto implements Serializable {

    private ReportDataHeaderDto reportDataHeaderDto;
    private JSONObject reportDataBodyDto;


    public ReportDataHeaderDto getReportDataHeaderDto() {
        return reportDataHeaderDto;
    }

    public void setReportDataHeaderDto(ReportDataHeaderDto reportDataHeaderDto) {
        this.reportDataHeaderDto = reportDataHeaderDto;
    }

    public JSONObject getReportDataBodyDto() {
        return reportDataBodyDto;
    }

    public void setReportDataBodyDto(JSONObject reportDataBodyDto) {
        this.reportDataBodyDto = reportDataBodyDto;
    }
}
