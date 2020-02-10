package com.java110.vo.api.inspectionPoint;

import com.java110.vo.MorePageVo;

import java.io.Serializable;
import java.util.List;

public class ApiInspectionPointVo extends MorePageVo implements Serializable {
    List<ApiInspectionPointDataVo> inspectionPoints;


    public List<ApiInspectionPointDataVo> getInspectionPoints() {
        return inspectionPoints;
    }

    public void setInspectionPoints(List<ApiInspectionPointDataVo> inspectionPoints) {
        this.inspectionPoints = inspectionPoints;
    }
}
