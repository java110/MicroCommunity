package com.java110.api.components.inspectionPoint;

import com.java110.core.context.IPageData;
import com.java110.api.smo.inspection.IDeleteInspectionPointSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 添加巡检点组件
 */
@Component("deleteInspectionPoint")
public class DeleteInspectionPointComponent {

    @Autowired
    private IDeleteInspectionPointSMO deleteInspectionPointSMOImpl;

    /**
     * 添加巡检点数据
     *
     * @param pd 页面数据封装
     * @return ResponseEntity 对象
     */
    public ResponseEntity<String> delete(IPageData pd) {
        return deleteInspectionPointSMOImpl.deleteInspectionPoint(pd);
    }

    public IDeleteInspectionPointSMO getDeleteInspectionPointSMOImpl() {
        return deleteInspectionPointSMOImpl;
    }

    public void setDeleteInspectionPointSMOImpl(IDeleteInspectionPointSMO deleteInspectionPointSMOImpl) {
        this.deleteInspectionPointSMOImpl = deleteInspectionPointSMOImpl;
    }
}
