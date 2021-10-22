package com.java110.api.components.inspectionPoint;

import com.java110.core.context.IPageData;
import com.java110.api.smo.inspection.IAddInspectionPlanSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 添加巡检计划组件
 */
@Component("addInspectionPlan")
public class AddInspectionPlanComponent {

    @Autowired
    private IAddInspectionPlanSMO addInspectionPlanSMOImpl;

    /**
     * 添加巡检计划数据
     * @param pd 页面数据封装
     * @return ResponseEntity 对象
     */
    public ResponseEntity<String> save(IPageData pd){
        return addInspectionPlanSMOImpl.saveInspectionPlan(pd);
    }

    public IAddInspectionPlanSMO getAddInspectionPlanSMOImpl() {
        return addInspectionPlanSMOImpl;
    }

    public void setAddInspectionPlanSMOImpl(IAddInspectionPlanSMO addInspectionPlanSMOImpl) {
        this.addInspectionPlanSMOImpl = addInspectionPlanSMOImpl;
    }
}
