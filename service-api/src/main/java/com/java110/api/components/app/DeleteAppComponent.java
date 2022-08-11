package com.java110.api.components.app;

import com.java110.core.context.IPageData;
import com.java110.api.smo.app.IDeleteAppSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 添加应用组件
 */
@Component("deleteApp")
public class DeleteAppComponent {

    @Autowired
    private IDeleteAppSMO deleteAppSMOImpl;

    /**
     * 添加应用数据
     *
     * @param pd 页面数据封装
     * @return ResponseEntity 对象
     */
    public ResponseEntity<String> delete(IPageData pd) {
        return deleteAppSMOImpl.deleteApp(pd);
    }

    public IDeleteAppSMO getDeleteAppSMOImpl() {
        return deleteAppSMOImpl;
    }

    public void setDeleteAppSMOImpl(IDeleteAppSMO deleteAppSMOImpl) {
        this.deleteAppSMOImpl = deleteAppSMOImpl;
    }
}
