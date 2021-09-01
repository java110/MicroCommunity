package com.java110.po.hcGovTranslateDetail;

import java.io.Serializable;
import java.util.Date;

public class HcGovTranslateDetailPo implements Serializable {

    private String tranId;
private String resBody;
private String reqBody;
private String detailId;
private String statusCd = "0";
private String communityId;
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



}
