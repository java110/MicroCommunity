package com.java110.web.components.owner;

import com.java110.core.context.IPageData;
import com.java110.web.smo.addOwner.IAddOwnerBindingSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 添加添加业主组件
 */
@Component("addOwnerBinding")
public class AddOwnerBindingComponent {

    @Autowired
    private IAddOwnerBindingSMO addOwnerBindingSMOImpl;

    /**
     * 添加添加业主数据
     * @param pd 页面数据封装
     * @return ResponseEntity 对象
     */
    public ResponseEntity<String> binding(IPageData pd){
        return addOwnerBindingSMOImpl.bindingAddOwner(pd);
    }

    public IAddOwnerBindingSMO getAddOwnerBindingSMOImpl() {
        return addOwnerBindingSMOImpl;
    }

    public void setAddOwnerBindingSMOImpl(IAddOwnerBindingSMO addOwnerBindingSMOImpl) {
        this.addOwnerBindingSMOImpl = addOwnerBindingSMOImpl;
    }
}
