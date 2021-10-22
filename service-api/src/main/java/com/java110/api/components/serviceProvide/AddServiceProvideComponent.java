package com.java110.api.components.serviceProvide;

import com.java110.core.context.IPageData;
import com.java110.api.smo.service.IAddServiceProvideSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 添加服务提供组件
 */
@Component("addServiceProvide")
public class AddServiceProvideComponent {

    @Autowired
    private IAddServiceProvideSMO addServiceProvideSMOImpl;

    /**
     * 添加服务提供数据
     * @param pd 页面数据封装
     * @return ResponseEntity 对象
     */
    public ResponseEntity<String> save(IPageData pd){
        return addServiceProvideSMOImpl.saveServiceProvide(pd);
    }

    public IAddServiceProvideSMO getAddServiceProvideSMOImpl() {
        return addServiceProvideSMOImpl;
    }

    public void setAddServiceProvideSMOImpl(IAddServiceProvideSMO addServiceProvideSMOImpl) {
        this.addServiceProvideSMOImpl = addServiceProvideSMOImpl;
    }
}
