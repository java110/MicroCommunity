package com.java110.api.components.resourceStore;

import com.java110.api.smo.resourceStore.IDeleteResourceStoreSMO;
import com.java110.core.context.IPageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 添加物品管理组件
 */
@Component("deleteResourceStore")
public class DeleteResourceStoreComponent {

    @Autowired
    private IDeleteResourceStoreSMO deleteResourceStoreSMOImpl;

    /**
     * 添加物品管理数据
     *
     * @param pd 页面数据封装
     * @return ResponseEntity 对象
     */
    public ResponseEntity<String> delete(IPageData pd) {
        return deleteResourceStoreSMOImpl.deleteResourceStore(pd);
    }

    public IDeleteResourceStoreSMO getDeleteResourceStoreSMOImpl() {
        return deleteResourceStoreSMOImpl;
    }

    public void setDeleteResourceStoreSMOImpl(IDeleteResourceStoreSMO deleteResourceStoreSMOImpl) {
        this.deleteResourceStoreSMOImpl = deleteResourceStoreSMOImpl;
    }
}
