package com.java110.api.components.resourceStoreType;

import com.java110.core.context.IPageData;
import com.java110.api.smo.resourceStoreType.IEditResourceStoreTypeSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 编辑小区组件
 */
@Component("editResourceStoreType")
public class EditResourceStoreTypeComponent {

    @Autowired
    private IEditResourceStoreTypeSMO editResourceStoreTypeSMOImpl;

    /**
     * 添加小区数据
     *
     * @param pd 页面数据封装
     * @return ResponseEntity 对象
     */
    public ResponseEntity<String> update(IPageData pd) {
        return editResourceStoreTypeSMOImpl.updateResourceStoreType(pd);
    }

    public IEditResourceStoreTypeSMO getEditResourceStoreTypeSMOImpl() {
        return editResourceStoreTypeSMOImpl;
    }

    public void setEditResourceStoreTypeSMOImpl(IEditResourceStoreTypeSMO editResourceStoreTypeSMOImpl) {
        this.editResourceStoreTypeSMOImpl = editResourceStoreTypeSMOImpl;
    }
}
