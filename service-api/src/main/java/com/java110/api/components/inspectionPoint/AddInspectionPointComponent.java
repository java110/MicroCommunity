package com.java110.api.components.inspectionPoint;

import com.java110.core.context.IPageData;
import com.java110.api.smo.inspection.IAddInspectionPointSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 添加巡检点组件
 * <p>
 * add by zc
 * <p>
 * 2020-02-10
 */
@Component("addInspectionPoint")
public class AddInspectionPointComponent {

    @Autowired
    private IAddInspectionPointSMO addInspectionPointSMOImpl;

    /**
     * 添加巡检点数据
     * @param pd 页面数据封装
     * @return ResponseEntity 对象
     */
    public ResponseEntity<String> save(IPageData pd){
        return addInspectionPointSMOImpl.saveInspectionPoint(pd);
    }

    public IAddInspectionPointSMO getAddInspectionPointSMOImpl() {
        return addInspectionPointSMOImpl;
    }

    public void setAddInspectionPointSMOImpl(IAddInspectionPointSMO addInspectionPointSMOImpl) {
        this.addInspectionPointSMOImpl = addInspectionPointSMOImpl;
    }
}
