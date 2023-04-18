package com.java110.dto.hcGovTranslate;

import com.java110.dto.PageDto;
import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 信息分类数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class HcGovTranslateDetailDto extends PageDto implements Serializable {

    private String tranId;
private String resBody;
private String reqBody;
private String detailId;
private String communityId;


    private Date createTime;

    private String statusCd = "0";


    public String getTranId() {
        return tranId;
    }
public void setTranId(String tranId) {
        this.tranId = tranId;
    }
public String getResBody() {
        return resBody;
    }
public void setResBody(String resBody) {
        this.resBody = resBody;
    }
public String getReqBody() {
        return reqBody;
    }
public void setReqBody(String reqBody) {
        this.reqBody = reqBody;
    }
public String getDetailId() {
        return detailId;
    }
public void setDetailId(String detailId) {
        this.detailId = detailId;
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
