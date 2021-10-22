package com.java110.api.components.businesstype;

import com.java110.core.context.IPageData;
import com.java110.api.smo.ICbusinessTypeServiceSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component("listBusinessType")
public class ListBusinessTypeComponent {
    @Autowired
    private ICbusinessTypeServiceSMO iCbusinessTypeServiceSMOImpl;

    /**
     * 查询小区楼信息
     *
     * @param pd 页面封装对象 包含页面请求数据
     * @return ResponseEntity对象返回给页面
     */
    public ResponseEntity<String> list(IPageData pd) {

        return iCbusinessTypeServiceSMOImpl.listBusinessType(pd);
    }


}
