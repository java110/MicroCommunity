package com.java110.api.components.visit;

import com.java110.core.context.IPageData;
import com.java110.api.smo.visit.IAddVisitSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 添加访客登记组件
 */
@Component("addVisit")
public class AddVisitComponent {

    @Autowired
    private IAddVisitSMO addVisitSMOImpl;

    /**
     * 添加访客登记数据
     * @param pd 页面数据封装
     * @return ResponseEntity 对象
     */
    public ResponseEntity<String> save(IPageData pd){
        return addVisitSMOImpl.saveVisit(pd);
    }

    public IAddVisitSMO getAddVisitSMOImpl() {
        return addVisitSMOImpl;
    }

    public void setAddVisitSMOImpl(IAddVisitSMO addVisitSMOImpl) {
        this.addVisitSMOImpl = addVisitSMOImpl;
    }
}
