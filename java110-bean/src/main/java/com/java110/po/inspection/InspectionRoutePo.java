package com.java110.po.inspection;

import java.io.Serializable;

/**
 * @ClassName InspectionRoutePo
 * @Description TODO
 * @Author wuxw
 * @Date 2020/5/28 20:50
 * @Version 1.0
 * add by wuxw 2020/5/28
 **/
public class InspectionRoutePo implements Serializable {

    private String inspectionRouteId;
    private String routeName;
    private String communityId;
    private String seq;
    private String remark;

    private String statusCd="0";

    public String getInspectionRouteId() {
        return inspectionRouteId;
    }

    public void setInspectionRouteId(String inspectionRouteId) {
        this.inspectionRouteId = inspectionRouteId;
    }

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getSeq() {
        return seq;
    }

    public void setSeq(String seq) {
        this.seq = seq;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getStatusCd() {
        return statusCd;
    }

    public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
    }
}
