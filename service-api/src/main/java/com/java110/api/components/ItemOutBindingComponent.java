package com.java110.api.components;

import com.java110.core.context.IPageData;
import com.java110.api.smo.itemOut.IItemOutBindingSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 添加物品出库组件
 */
@Component("itemOutBinding")
public class ItemOutBindingComponent {

    @Autowired
    private IItemOutBindingSMO itemOutBindingSMOImpl;

    /**
     * 添加物品出库数据
     *
     * @param pd 页面数据封装
     * @return ResponseEntity 对象
     */
    public ResponseEntity<String> binding(IPageData pd) {
        return itemOutBindingSMOImpl.bindingItemOut(pd);
    }

    public IItemOutBindingSMO getItemOutBindingSMOImpl() {
        return itemOutBindingSMOImpl;
    }

    public void setItemOutBindingSMOImpl(IItemOutBindingSMO itemOutBindingSMOImpl) {
        this.itemOutBindingSMOImpl = itemOutBindingSMOImpl;
    }
}
