package com.java110.vo.api.inspectionPlan;

import com.java110.vo.MorePageVo;

import java.io.Serializable;
import java.util.List;

public class ApiInspectionPlanVo extends MorePageVo implements Serializable {
    List<ApiInspectionPlanDataVo> inspectionPlans;


    public List<ApiInspectionPlanDataVo> getInspectionPlans() {
        return inspectionPlans;
    }

    public void setInspectionPlans(List<ApiInspectionPlanDataVo> inspectionPlans) {
        this.inspectionPlans = inspectionPlans;
    }
}
