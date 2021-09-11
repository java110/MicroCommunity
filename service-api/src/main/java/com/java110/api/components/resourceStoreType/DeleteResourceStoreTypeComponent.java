package com.java110.api.components.resourceStoreType;

import com.java110.core.context.IPageData;
import com.java110.api.smo.resourceStoreType.IDeleteResourceStoreTypeSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 添加物品管理组件
 */
@Component("deleteResourceStoreType")
public class DeleteResourceStoreTypeComponent {

    @Autowired
    private IDeleteResourceStoreTypeSMO deleteResourceStoreTypeSMOImpl;

    /**
     * 添加物品管理数据
     *
     * @param pd 页面数据封装
     * @return ResponseEntity 对象
     */
    public ResponseEntity<String> delete(IPageData pd) {
        return deleteResourceStoreTypeSMOImpl.deleteResourceStoreType(pd);
    }

    public IDeleteResourceStoreTypeSMO getDeleteResourceStoreTypeSMOImpl() {
        return deleteResourceStoreTypeSMOImpl;
    }

    public void setDeleteResourceStoreTypeSMOImpl(IDeleteResourceStoreTypeSMO deleteResourceStoreTypeSMOImpl) {
        this.deleteResourceStoreTypeSMOImpl = deleteResourceStoreTypeSMOImpl;
    }
}
