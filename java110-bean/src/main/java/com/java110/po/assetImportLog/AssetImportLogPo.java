package com.java110.po.assetImportLog;

import java.io.Serializable;

public class AssetImportLogPo implements Serializable {

    private String logType;
    private String successCount;
    private String logId;
    private String remark;
    private String statusCd = "0";
    private String communityId;
    private String errorCount;

    public String getLogType() {
        return logType;
    }

    public void setLogType(String logType) {
        this.logType = logType;
    }

    public String getSuccessCount() {
        return successCount;
    }

    public void setSuccessCount(String successCount) {
        this.successCount = successCount;
    }

    public String getLogId() {
        return logId;
    }

    public void setLogId(String logId) {
        this.logId = logId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getStatusCd() {
        return statusCd;
    }

    public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getErrorCount() {
        return errorCount;
    }

    public void setErrorCount(String errorCount) {
        this.errorCount = errorCount;
    }


}
