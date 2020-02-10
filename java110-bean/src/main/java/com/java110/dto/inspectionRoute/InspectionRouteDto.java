package com.java110.dto.inspectionRoute;

import com.java110.dto.PageDto;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 巡检路线数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class InspectionRouteDto extends PageDto implements Serializable {

    private String inspectionRouteId;
    private String checkQuantity;
    private String machineQuantity;
    private String remark;
    private String communityId;
    private String routeName;


    private Date createTime;

    private String statusCd = "0";


    public String getInspectionRouteId() {
        return inspectionRouteId;
    }

    public void setInspectionRouteId(String inspectionRouteId) {
        this.inspectionRouteId = inspectionRouteId;
    }

    public String getCheckQuantity() {
        return checkQuantity;
    }

    public void setCheckQuantity(String checkQuantity) {
        this.checkQuantity = checkQuantity;
    }

    public String getMachineQuantity() {
        return machineQuantity;
    }

    public void setMachineQuantity(String machineQuantity) {
        this.machineQuantity = machineQuantity;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
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
