package com.java110.dto.contract;

import com.java110.dto.PageDto;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 合同附件数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class ContractFileDto extends PageDto implements Serializable {

    private String fileRealName;
private String contractFileId;
private String contractId;
private String fileSaveName;


    private Date createTime;

    private String statusCd = "0";


    public String getFileRealName() {
        return fileRealName;
    }
public void setFileRealName(String fileRealName) {
        this.fileRealName = fileRealName;
    }
public String getContractFileId() {
        return contractFileId;
    }
public void setContractFileId(String contractFileId) {
        this.contractFileId = contractFileId;
    }
public String getContractId() {
        return contractId;
    }
public void setContractId(String contractId) {
        this.contractId = contractId;
    }
public String getFileSaveName() {
        return fileSaveName;
    }
public void setFileSaveName(String fileSaveName) {
        this.fileSaveName = fileSaveName;
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
