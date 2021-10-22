package com.java110.api.components.inspectionPoint;

import com.java110.core.context.IPageData;
import com.java110.api.smo.inspection.IInspectionPlanStateSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 编辑小区组件
 */
@Component("updateInspectionPlanState")
public class UpdateInspectionPlanStateComponent {

    @Autowired
    private IInspectionPlanStateSMO iInspectionPlanStateSMOImpl;

    /**
     * 添加小区数据
     * @param pd 页面数据封装
     * @return ResponseEntity 对象
     */
    public ResponseEntity<String> update(IPageData pd){
        return iInspectionPlanStateSMOImpl.updateInspectionPlanState(pd);
    }

    public IInspectionPlanStateSMO getiInspectionPlanStateSMOImpl() {
        return iInspectionPlanStateSMOImpl;
    }

    public void setiInspectionPlanStateSMOImpl(IInspectionPlanStateSMO iInspectionPlanStateSMOImpl) {
        this.iInspectionPlanStateSMOImpl = iInspectionPlanStateSMOImpl;
    }
}
