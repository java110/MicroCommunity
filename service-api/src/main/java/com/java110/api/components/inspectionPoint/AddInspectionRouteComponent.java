package com.java110.api.components.inspectionPoint;

import com.java110.core.context.IPageData;
import com.java110.api.smo.inspection.IAddInspectionRouteSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 添加巡检路线组件
 */
@Component("addInspectionRoute")
public class AddInspectionRouteComponent {

    @Autowired
    private IAddInspectionRouteSMO addInspectionRouteSMOImpl;

    /**
     * 添加巡检路线数据
     * @param pd 页面数据封装
     * @return ResponseEntity 对象
     */
    public ResponseEntity<String> save(IPageData pd){
        return addInspectionRouteSMOImpl.saveInspectionRoute(pd);
    }

    public IAddInspectionRouteSMO getAddInspectionRouteSMOImpl() {
        return addInspectionRouteSMOImpl;
    }

    public void setAddInspectionRouteSMOImpl(IAddInspectionRouteSMO addInspectionRouteSMOImpl) {
        this.addInspectionRouteSMOImpl = addInspectionRouteSMOImpl;
    }
}
