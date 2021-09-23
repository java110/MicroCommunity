package com.java110.api.components.inspectionPoint;

import com.java110.core.context.IPageData;
import com.java110.api.smo.inspection.IEditInspectionRoutePointRelSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 编辑小区组件
 */
@Component("editInspectionRoutePointRel")
public class EditInspectionRoutePointRelComponent {

    @Autowired
    private IEditInspectionRoutePointRelSMO editInspectionRoutePointRelSMOImpl;

    /**
     * 添加小区数据
     * @param pd 页面数据封装
     * @return ResponseEntity 对象
     */
    public ResponseEntity<String> update(IPageData pd){
        return editInspectionRoutePointRelSMOImpl.updateInspectionRoutePointRel(pd);
    }

    public IEditInspectionRoutePointRelSMO getEditInspectionRoutePointRelSMOImpl() {
        return editInspectionRoutePointRelSMOImpl;
    }

    public void setEditInspectionRoutePointRelSMOImpl(IEditInspectionRoutePointRelSMO editInspectionRoutePointRelSMOImpl) {
        this.editInspectionRoutePointRelSMOImpl = editInspectionRoutePointRelSMOImpl;
    }
}
