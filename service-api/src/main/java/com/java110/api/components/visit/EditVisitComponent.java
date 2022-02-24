package com.java110.api.components.visit;

import com.java110.core.context.IPageData;
import com.java110.api.smo.visit.IEditVisitSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 编辑小区组件
 */
@Component("editVisit")
public class EditVisitComponent {

    @Autowired
    private IEditVisitSMO editVisitSMOImpl;

    /**
     * 添加小区数据
     *
     * @param pd 页面数据封装
     * @return ResponseEntity 对象
     */
    public ResponseEntity<String> update(IPageData pd) {
        return editVisitSMOImpl.updateVisit(pd);
    }

    public IEditVisitSMO getEditVisitSMOImpl() {
        return editVisitSMOImpl;
    }

    public void setEditVisitSMOImpl(IEditVisitSMO editVisitSMOImpl) {
        this.editVisitSMOImpl = editVisitSMOImpl;
    }
}
