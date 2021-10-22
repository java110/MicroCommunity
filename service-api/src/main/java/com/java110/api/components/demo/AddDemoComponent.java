package com.java110.api.components.demo;

import com.java110.core.context.IPageData;
import com.java110.api.smo.IDemoServiceSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 添加小区楼组件
 */
@Component("addDemo")
public class AddDemoComponent {

    @Autowired
    private IDemoServiceSMO demoServiceSMOImpl;

    /**
     * 查询小区楼信息
     * @param pd 页面封装对象 包含页面请求数据
     * @return ResponseEntity对象返回给页面
     */
    public ResponseEntity<String> saveDemo(IPageData pd) {

        return demoServiceSMOImpl.saveDemo(pd);
    }

    public IDemoServiceSMO getDemoServiceSMOImpl() {
        return demoServiceSMOImpl;
    }

    public void setDemoServiceSMOImpl(IDemoServiceSMO demoServiceSMOImpl) {
        this.demoServiceSMOImpl = demoServiceSMOImpl;
    }
}
