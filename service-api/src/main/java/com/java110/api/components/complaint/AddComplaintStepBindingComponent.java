package com.java110.api.components.complaint;

import com.java110.core.context.IPageData;
import com.java110.api.smo.complaint.IAddComplaintSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 添加投诉建议组件
 */
@Component("addComplaintStepBinding")
public class AddComplaintStepBindingComponent {

    @Autowired
    private IAddComplaintSMO addComplaintSMOImpl;

    /**
     * 添加投诉建议数据
     * @param pd 页面数据封装
     * @return ResponseEntity 对象
     */
    public ResponseEntity<String> binding(IPageData pd){
        return addComplaintSMOImpl.saveComplaint(pd);
    }

    public IAddComplaintSMO getAddComplaintSMOImpl() {
        return addComplaintSMOImpl;
    }

    public void setAddComplaintSMOImpl(IAddComplaintSMO addComplaintSMOImpl) {
        this.addComplaintSMOImpl = addComplaintSMOImpl;
    }
}
