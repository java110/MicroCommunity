package com.java110.api.components.serviceRegister;

import com.java110.core.context.IPageData;
import com.java110.api.smo.service.IEditServiceRegisterSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 编辑小区组件
 */
@Component("editServiceRegister")
public class EditServiceRegisterComponent {

    @Autowired
    private IEditServiceRegisterSMO editServiceRegisterSMOImpl;

    /**
     * 添加小区数据
     * @param pd 页面数据封装
     * @return ResponseEntity 对象
     */
    public ResponseEntity<String> update(IPageData pd){
        return editServiceRegisterSMOImpl.updateServiceRegister(pd);
    }

    public IEditServiceRegisterSMO getEditServiceRegisterSMOImpl() {
        return editServiceRegisterSMOImpl;
    }

    public void setEditServiceRegisterSMOImpl(IEditServiceRegisterSMO editServiceRegisterSMOImpl) {
        this.editServiceRegisterSMOImpl = editServiceRegisterSMOImpl;
    }
}
