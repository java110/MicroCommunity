package com.java110.vo.api.inspectionTaskDetail;

import com.java110.vo.MorePageVo;

import java.io.Serializable;
import java.util.List;

public class ApiInspectionTaskDetailVo extends MorePageVo implements Serializable {
    List<ApiInspectionTaskDetailDataVo> inspectionTaskDetails;


    public List<ApiInspectionTaskDetailDataVo> getInspectionTaskDetails() {
        return inspectionTaskDetails;
    }

    public void setInspectionTaskDetails(List<ApiInspectionTaskDetailDataVo> inspectionTaskDetails) {
        this.inspectionTaskDetails = inspectionTaskDetails;
    }
}
