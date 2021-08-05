package com.java110.dto.assetImportLog;

import com.java110.dto.PageDto;
import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 批量操作日志数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class AssetImportLogDto extends PageDto implements Serializable {

    private String logType;
private String successCount;
private String logId;
private String remark;
private String communityId;
private String errorCount;


    private Date createTime;

    private String statusCd = "0";


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


    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getStatusCd() {
        return statusCd;
    }

    public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
    }
}
