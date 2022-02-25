package com.java110.api.components.visit;

import com.java110.core.context.IPageData;
import com.java110.api.smo.visit.IDeleteVisitSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 添加访客登记组件
 */
@Component("deleteVisit")
public class DeleteVisitComponent {

    @Autowired
    private IDeleteVisitSMO deleteVisitSMOImpl;

    /**
     * 添加访客登记数据
     *
     * @param pd 页面数据封装
     * @return ResponseEntity 对象
     */
    public ResponseEntity<String> delete(IPageData pd) {
        return deleteVisitSMOImpl.deleteVisit(pd);
    }

    public IDeleteVisitSMO getDeleteVisitSMOImpl() {
        return deleteVisitSMOImpl;
    }

    public void setDeleteVisitSMOImpl(IDeleteVisitSMO deleteVisitSMOImpl) {
        this.deleteVisitSMOImpl = deleteVisitSMOImpl;
    }
}
