package com.java110.api.components.service;

import com.java110.core.context.IPageData;
import com.java110.api.smo.service.IEditServiceImplSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 编辑小区组件
 */
@Component("editServiceImpl")
public class EditServiceImplComponent {

    @Autowired
    private IEditServiceImplSMO editServiceImplSMOImpl;

    /**
     * 添加小区数据
     * @param pd 页面数据封装
     * @return ResponseEntity 对象
     */
    public ResponseEntity<String> update(IPageData pd){
        return editServiceImplSMOImpl.updateServiceImpl(pd);
    }

    public IEditServiceImplSMO getEditServiceImplSMOImpl() {
        return editServiceImplSMOImpl;
    }

    public void setEditServiceImplSMOImpl(IEditServiceImplSMO editServiceImplSMOImpl) {
        this.editServiceImplSMOImpl = editServiceImplSMOImpl;
    }
}
