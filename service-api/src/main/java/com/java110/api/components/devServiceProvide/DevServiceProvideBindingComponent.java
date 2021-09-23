package com.java110.api.components.devServiceProvide;

import com.java110.core.context.IPageData;
import com.java110.api.smo.devServiceProvide.IDevServiceProvideBindingSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 添加开发服务提供组件
 */
@Component("devServiceProvideBinding")
public class DevServiceProvideBindingComponent {

    @Autowired
    private IDevServiceProvideBindingSMO devServiceProvideBindingSMOImpl;

    /**
     * 添加开发服务提供数据
     * @param pd 页面数据封装
     * @return ResponseEntity 对象
     */
    public ResponseEntity<String> binding(IPageData pd){
        return devServiceProvideBindingSMOImpl.bindingDevServiceProvide(pd);
    }

    public IDevServiceProvideBindingSMO getDevServiceProvideBindingSMOImpl() {
        return devServiceProvideBindingSMOImpl;
    }

    public void setDevServiceProvideBindingSMOImpl(IDevServiceProvideBindingSMO devServiceProvideBindingSMOImpl) {
        this.devServiceProvideBindingSMOImpl = devServiceProvideBindingSMOImpl;
    }
}
