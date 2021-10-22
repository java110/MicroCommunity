package com.java110.api.components.inspectionPoint;

import com.java110.core.context.IPageData;
import com.java110.api.smo.inspection.IEditInspectionPlanSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 编辑小区组件
 */
@Component("editInspectionPlan")
public class EditInspectionPlanComponent {

    @Autowired
    private IEditInspectionPlanSMO editInspectionPlanSMOImpl;

    /**
     * 添加小区数据
     * @param pd 页面数据封装
     * @return ResponseEntity 对象
     */
    public ResponseEntity<String> update(IPageData pd){
        return editInspectionPlanSMOImpl.updateInspectionPlan(pd);
    }

    public IEditInspectionPlanSMO getEditInspectionPlanSMOImpl() {
        return editInspectionPlanSMOImpl;
    }

    public void setEditInspectionPlanSMOImpl(IEditInspectionPlanSMO editInspectionPlanSMOImpl) {
        this.editInspectionPlanSMOImpl = editInspectionPlanSMOImpl;
    }
}
