package com.java110.po.inspection;

import java.io.Serializable;

/**
 * @ClassName InspectionRoutePointRelPo
 * @Description TODO
 * @Author wuxw
 * @Date 2020/5/28 20:58
 * @Version 1.0
 * add by wuxw 2020/5/28
 **/
public class InspectionRoutePointRelPo implements Serializable {

    private String irpRelId;

    private String inspectionRouteId;
    private String inspectionId;
    private String communityId;

    public String getIrpRelId() {
        return irpRelId;
    }

    public void setIrpRelId(String irpRelId) {
        this.irpRelId = irpRelId;
    }

    public String getInspectionRouteId() {
        return inspectionRouteId;
    }

    public void setInspectionRouteId(String inspectionRouteId) {
        this.inspectionRouteId = inspectionRouteId;
    }

    public String getInspectionId() {
        return inspectionId;
    }

    public void setInspectionId(String inspectionId) {
        this.inspectionId = inspectionId;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }
}
