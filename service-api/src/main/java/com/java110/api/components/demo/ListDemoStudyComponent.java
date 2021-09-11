package com.java110.api.components.demo;


import com.java110.core.context.IPageData;
import com.java110.api.smo.IDemoServiceSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component("listDemoStudy")
public class ListDemoStudyComponent {

    @Autowired
    private IDemoServiceSMO demoServiceSMOImpl;


    /**
     * 查询demo
     *
     * @param pd 页面数据封装对象
     * @return ResponseEntity对象
     */
    public ResponseEntity<String> list(IPageData pd) {

        return demoServiceSMOImpl.listDemoStudy(pd);

    }

    public IDemoServiceSMO getDemoServiceSMOImpl() {
        return demoServiceSMOImpl;
    }

    public void setDemoServiceSMOImpl(IDemoServiceSMO demoServiceSMOImpl) {
        this.demoServiceSMOImpl = demoServiceSMOImpl;
    }
}
