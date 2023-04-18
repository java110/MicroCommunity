package com.java110.dto.owner;

import com.java110.dto.PageDto;
import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 紧急联系人数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class OwnerCommitteeContractDto extends PageDto implements Serializable {

    private String relName;
private String address;
private String contractId;
private String name;
private String link;
private String remark;
private String ocId;
private String communityId;


    private Date createTime;

    private String statusCd = "0";


    public String getRelName() {
        return relName;
    }
public void setRelName(String relName) {
        this.relName = relName;
    }
public String getAddress() {
        return address;
    }
public void setAddress(String address) {
        this.address = address;
    }
public String getContractId() {
        return contractId;
    }
public void setContractId(String contractId) {
        this.contractId = contractId;
    }
public String getName() {
        return name;
    }
public void setName(String name) {
        this.name = name;
    }
public String getLink() {
        return link;
    }
public void setLink(String link) {
        this.link = link;
    }
public String getRemark() {
        return remark;
    }
public void setRemark(String remark) {
        this.remark = remark;
    }
public String getOcId() {
        return ocId;
    }
public void setOcId(String ocId) {
        this.ocId = ocId;
    }
public String getCommunityId() {
        return communityId;
    }
public void setCommunityId(String communityId) {
        this.communityId = communityId;
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
