package com.java110.api.components.allocationUserStorehouse;

import com.java110.core.context.IPageData;
import com.java110.api.smo.allocationUserStorehouse.IAddAllocationUserStorehouseSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 添加采购申请组件
 */
@Component("addAllocationUserStorehouse")
public class AddAllocationUserStorehouseComponent {

    @Autowired
    private IAddAllocationUserStorehouseSMO addAllocationUserStorehouseSMOImpl;

    /**
     * 添加采购申请数据
     *
     * @param pd 页面数据封装
     * @return ResponseEntity 对象
     */
    public ResponseEntity<String> save(IPageData pd) {
        return addAllocationUserStorehouseSMOImpl.saveAllocationUserStorehouse(pd);
    }

    public IAddAllocationUserStorehouseSMO getAddAllocationUserStorehouseSMOImpl() {
        return addAllocationUserStorehouseSMOImpl;
    }

    public void setAddAllocationUserStorehouseSMOImpl(IAddAllocationUserStorehouseSMO addAllocationUserStorehouseSMOImpl) {
        this.addAllocationUserStorehouseSMOImpl = addAllocationUserStorehouseSMOImpl;
    }
}
