package com.java110.po.machine;

import java.io.Serializable;

/**
 * @ClassName MachinePo
 * @Description TODO
 * @Author wuxw
 * @Date 2020/5/29 8:55
 * @Version 1.0
 * add by wuxw 2020/5/29
 **/
public class MachineAttrPo implements Serializable {

    private String machineId;
    private String communityId;
    private String attrId;
    private String specCd;
    private String value;

    public String getMachineId() {
        return machineId;
    }

    public void setMachineId(String machineId) {
        this.machineId = machineId;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getAttrId() {
        return attrId;
    }

    public void setAttrId(String attrId) {
        this.attrId = attrId;
    }

    public String getSpecCd() {
        return specCd;
    }

    public void setSpecCd(String specCd) {
        this.specCd = specCd;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
