package com.java110.api.components.app;

import com.java110.core.context.IPageData;
import com.java110.api.smo.app.IEditAppSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 编辑小区组件
 */
@Component("editApp")
public class EditAppComponent {

    @Autowired
    private IEditAppSMO editAppSMOImpl;

    /**
     * 添加小区数据
     * @param pd 页面数据封装
     * @return ResponseEntity 对象
     */
    public ResponseEntity<String> update(IPageData pd){
        return editAppSMOImpl.updateApp(pd);
    }

    public IEditAppSMO getEditAppSMOImpl() {
        return editAppSMOImpl;
    }

    public void setEditAppSMOImpl(IEditAppSMO editAppSMOImpl) {
        this.editAppSMOImpl = editAppSMOImpl;
    }
}
