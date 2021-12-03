package com.java110.dto.workflowDataFile;

import com.java110.dto.PageDto;
import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description OA附件数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class WorkflowDataFileDto extends PageDto implements Serializable {

    private String fileName;
private String createUserId;
private String createUserName;
private String id;
private String realFileName;
private String storeId;
private String fileId;


    private Date createTime;

    private String statusCd = "0";


    public String getFileName() {
        return fileName;
    }
public void setFileName(String fileName) {
        this.fileName = fileName;
    }
public String getCreateUserId() {
        return createUserId;
    }
public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }
public String getCreateUserName() {
        return createUserName;
    }
public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }
public String getId() {
        return id;
    }
public void setId(String id) {
        this.id = id;
    }
public String getRealFileName() {
        return realFileName;
    }
public void setRealFileName(String realFileName) {
        this.realFileName = realFileName;
    }
public String getStoreId() {
        return storeId;
    }
public void setStoreId(String storeId) {
        this.storeId = storeId;
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
