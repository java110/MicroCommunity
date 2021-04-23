package com.java110.vo.api.inspectionRoute;

import java.io.Serializable;
import java.util.Date;

public class ApiInspectionRouteDataVo implements Serializable {

    private String inspectionRouteId;
    private String routeName;
    private String seq;
    private String remark;

    private String[] InspectionRouteIds;

    private String createTime;

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

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String[] getInspectionRouteIds() {
        return InspectionRouteIds;
    }

    public void setInspectionRouteIds(String[] inspectionRouteIds) {
        InspectionRouteIds = inspectionRouteIds;
    }
}
