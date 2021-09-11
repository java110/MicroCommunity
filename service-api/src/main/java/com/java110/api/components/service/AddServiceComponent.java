package com.java110.api.components.service;

import com.java110.core.context.IPageData;
import com.java110.api.smo.service.IAddServiceSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 添加服务组件
 */
@Component("addService")
public class AddServiceComponent {

    @Autowired
    private IAddServiceSMO addServiceSMOImpl;

    /**
     * 添加服务数据
     * @param pd 页面数据封装
     * @return ResponseEntity 对象
     */
    public ResponseEntity<String> save(IPageData pd){
        return addServiceSMOImpl.saveService(pd);
    }

    public IAddServiceSMO getAddServiceSMOImpl() {
        return addServiceSMOImpl;
    }

    public void setAddServiceSMOImpl(IAddServiceSMO addServiceSMOImpl) {
        this.addServiceSMOImpl = addServiceSMOImpl;
    }
}
