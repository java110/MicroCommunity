package com.java110.vo.api.activities;

import com.java110.vo.MorePageVo;

import java.io.Serializable;
import java.util.List;

public class ApiActivitiesVo extends MorePageVo implements Serializable {
    List<ApiActivitiesDataVo> activitiess;


    public List<ApiActivitiesDataVo> getActivitiess() {
        return activitiess;
    }

    public void setActivitiess(List<ApiActivitiesDataVo> activitiess) {
        this.activitiess = activitiess;
    }
}
