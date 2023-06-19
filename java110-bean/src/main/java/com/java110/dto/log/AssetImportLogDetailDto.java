package com.java110.dto.log;

import com.java110.dto.PageDto;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 批量操作日志详情数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class AssetImportLogDetailDto extends PageDto implements Serializable {

    public static final String STATE_WAIT_IMPORT = "W"; //W 待导入 C 导入成功 F 导入失败
    public static final String STATE_SUCCESS_IMPORT = "C"; //W 待导入 C 导入成功 F 导入失败
    public static final String STATE_FAIL_IMPORT = "F"; //W 待导入 C 导入成功 F 导入失败

    private String detailId;
    private String logId;
    private String state;
    private String content;
    private String communityId;
    private String message;

    private String stateName;


    private String createTime;

    private String statusCd = "0";


    public String getDetailId() {
        return detailId;
    }

    public void setDetailId(String detailId) {
        this.detailId = detailId;
    }

    public String getLogId() {
        return logId;
    }

    public void setLogId(String logId) {
        this.logId = logId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }




    public String getStatusCd() {
        return statusCd;
    }

    public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
