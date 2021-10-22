package com.java110.api.components.resourceStore;

import com.java110.core.context.IPageData;
import com.java110.api.smo.resourceStore.IEditResourceStoreSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 编辑小区组件
 */
@Component("editResourceStore")
public class EditResourceStoreComponent {

    @Autowired
    private IEditResourceStoreSMO editResourceStoreSMOImpl;

    /**
     * 添加小区数据
     * @param pd 页面数据封装
     * @return ResponseEntity 对象
     */
    public ResponseEntity<String> update(IPageData pd){
        return editResourceStoreSMOImpl.updateResourceStore(pd);
    }

    public IEditResourceStoreSMO getEditResourceStoreSMOImpl() {
        return editResourceStoreSMOImpl;
    }

    public void setEditResourceStoreSMOImpl(IEditResourceStoreSMO editResourceStoreSMOImpl) {
        this.editResourceStoreSMOImpl = editResourceStoreSMOImpl;
    }
}
