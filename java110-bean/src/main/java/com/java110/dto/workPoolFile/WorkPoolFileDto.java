package com.java110.dto.workPoolFile;

import com.java110.dto.PageDto;
import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 工作单文件数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class WorkPoolFileDto extends PageDto implements Serializable {

    private String pathUrl;
private String communityId;
private String storeId;
private String workId;
private String fileId;


    private Date createTime;

    private String statusCd = "0";


    public String getPathUrl() {
        return pathUrl;
    }
public void setPathUrl(String pathUrl) {
        this.pathUrl = pathUrl;
    }
public String getCommunityId() {
        return communityId;
    }
public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }
public String getStoreId() {
        return storeId;
    }
public void setStoreId(String storeId) {
        this.storeId = storeId;
    }
public String getWorkId() {
        return workId;
    }
public void setWorkId(String workId) {
        this.workId = workId;
    }
public String getFileId() {
        return fileId;
    }
public void setFileId(String fileId) {
        this.fileId = fileId;
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