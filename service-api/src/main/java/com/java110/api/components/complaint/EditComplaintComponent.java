package com.java110.api.components.complaint;

import com.java110.core.context.IPageData;
import com.java110.api.smo.complaint.IEditComplaintSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 编辑小区组件
 */
@Component("editComplaint")
public class EditComplaintComponent {

    @Autowired
    private IEditComplaintSMO editComplaintSMOImpl;

    /**
     * 添加小区数据
     * @param pd 页面数据封装
     * @return ResponseEntity 对象
     */
    public ResponseEntity<String> update(IPageData pd){
        return editComplaintSMOImpl.updateComplaint(pd);
    }

    public IEditComplaintSMO getEditComplaintSMOImpl() {
        return editComplaintSMOImpl;
    }

    public void setEditComplaintSMOImpl(IEditComplaintSMO editComplaintSMOImpl) {
        this.editComplaintSMOImpl = editComplaintSMOImpl;
    }
}
