package com.java110.api.components.service;


import com.java110.core.context.IPageData;
import com.java110.api.smo.service.IBindingServiceSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 服务绑定组件类
 */
@Component("serviceBinding")
public class ServiceBindingComponent {

    @Autowired
    private IBindingServiceSMO bindingServiceSMOImpl;

    /**
     * 服务绑定
     * @param pd 页面数据封装
     * @return 返回 ResponseEntity对象
     */
    public ResponseEntity<String> binding(IPageData pd){
       return bindingServiceSMOImpl.binding(pd);
    }

    public IBindingServiceSMO getBindingServiceSMOImpl() {
        return bindingServiceSMOImpl;
    }

    public void setBindingServiceSMOImpl(IBindingServiceSMO bindingServiceSMOImpl) {
        this.bindingServiceSMOImpl = bindingServiceSMOImpl;
    }
}
