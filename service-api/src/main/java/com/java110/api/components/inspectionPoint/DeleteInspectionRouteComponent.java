package com.java110.api.components.inspectionPoint;

import com.java110.core.context.IPageData;
import com.java110.api.smo.inspection.IDeleteInspectionRouteSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 添加巡检路线组件
 */
@Component("deleteInspectionRoute")
public class DeleteInspectionRouteComponent {

    @Autowired
    private IDeleteInspectionRouteSMO deleteInspectionRouteSMOImpl;

    /**
     * 添加巡检路线数据
     *
     * @param pd 页面数据封装
     * @return ResponseEntity 对象
     */
    public ResponseEntity<String> delete(IPageData pd) {
        return deleteInspectionRouteSMOImpl.deleteInspectionRoute(pd);
    }

    public IDeleteInspectionRouteSMO getDeleteInspectionRouteSMOImpl() {
        return deleteInspectionRouteSMOImpl;
    }

    public void setDeleteInspectionRouteSMOImpl(IDeleteInspectionRouteSMO deleteInspectionRouteSMOImpl) {
        this.deleteInspectionRouteSMOImpl = deleteInspectionRouteSMOImpl;
    }
}
