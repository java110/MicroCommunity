package com.java110.api.components.inspectionPoint;

import com.java110.core.context.IPageData;
import com.java110.api.smo.inspection.IEditInspectionPointSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 编辑巡检点组件
 */
@Component("editInspectionPoint")
public class EditInspectionPointComponent {

    @Autowired
    private IEditInspectionPointSMO editInspectionPointSMOImpl;

    /**
     * 添加巡检点数据
     * @param pd 页面数据封装
     * @return ResponseEntity 对象
     */
    public ResponseEntity<String> update(IPageData pd){
        return editInspectionPointSMOImpl.updateInspectionPoint(pd);
    }

    public IEditInspectionPointSMO getEditInspectionPointSMOImpl() {
        return editInspectionPointSMOImpl;
    }

    public void setEditInspectionPointSMOImpl(IEditInspectionPointSMO editInspectionPointSMOImpl) {
        this.editInspectionPointSMOImpl = editInspectionPointSMOImpl;
    }
}
