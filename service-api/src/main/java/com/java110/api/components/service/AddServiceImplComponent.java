package com.java110.api.components.service;

import com.java110.core.context.IPageData;
import com.java110.api.smo.service.IAddServiceImplSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 添加服务实现组件
 */
@Component("addServiceImpl")
public class AddServiceImplComponent {

    @Autowired
    private IAddServiceImplSMO addServiceImplSMOImpl;

    /**
     * 添加服务实现数据
     * @param pd 页面数据封装
     * @return ResponseEntity 对象
     */
    public ResponseEntity<String> save(IPageData pd){
        return addServiceImplSMOImpl.saveServiceImpl(pd);
    }

    public IAddServiceImplSMO getAddServiceImplSMOImpl() {
        return addServiceImplSMOImpl;
    }

    public void setAddServiceImplSMOImpl(IAddServiceImplSMO addServiceImplSMOImpl) {
        this.addServiceImplSMOImpl = addServiceImplSMOImpl;
    }
}
