package com.java110.po.org;

import java.io.Serializable;

/**
 * @ClassName OrgStaffRelPo
 * @Description TODO
 * @Author wuxw
 * @Date 2020/5/30 12:55
 * @Version 1.0
 * add by wuxw 2020/5/30
 **/
public class OrgStaffRelPo implements Serializable {

    private String relId;
    private String orgId;
    private String staffId;
    private String relCd;
    private String storeId;

    public String getRelId() {
        return relId;
    }

    public void setRelId(String relId) {
        this.relId = relId;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public String getRelCd() {
        return relCd;
    }

    public void setRelCd(String relCd) {
        this.relCd = relCd;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }
}
