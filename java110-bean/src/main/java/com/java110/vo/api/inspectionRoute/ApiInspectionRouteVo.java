package com.java110.vo.api.inspectionRoute;

import com.java110.vo.MorePageVo;

import java.io.Serializable;
import java.util.List;

public class ApiInspectionRouteVo extends MorePageVo implements Serializable {
    List<ApiInspectionRouteDataVo> inspectionRoutes;


    public List<ApiInspectionRouteDataVo> getInspectionRoutes() {
        return inspectionRoutes;
    }

    public void setInspectionRoutes(List<ApiInspectionRouteDataVo> inspectionRoutes) {
        this.inspectionRoutes = inspectionRoutes;
    }
}
