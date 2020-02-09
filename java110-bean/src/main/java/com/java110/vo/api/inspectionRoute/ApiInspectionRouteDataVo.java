package com.java110.vo.api.inspectionRoute;

import java.io.Serializable;
import java.util.Date;

public class ApiInspectionRouteDataVo implements Serializable {

    private String configId;
private String routeName;
private String inspectionName;
private String machineQuantity;
private String checkQuantity;
private String remark;
public String getConfigId() {
        return configId;
    }
public void setConfigId(String configId) {
        this.configId = configId;
    }
public String getRouteName() {
        return routeName;
    }
public void setRouteName(String routeName) {
        this.routeName = routeName;
    }
public String getInspectionName() {
        return inspectionName;
    }
public void setInspectionName(String inspectionName) {
        this.inspectionName = inspectionName;
    }
public String getMachineQuantity() {
        return machineQuantity;
    }
public void setMachineQuantity(String machineQuantity) {
        this.machineQuantity = machineQuantity;
    }
public String getCheckQuantity() {
        return checkQuantity;
    }
public void setCheckQuantity(String checkQuantity) {
        this.checkQuantity = checkQuantity;
    }
public String getRemark() {
        return remark;
    }
public void setRemark(String remark) {
        this.remark = remark;
    }



}
