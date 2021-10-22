package com.java110.api.components.applicationKey;

import com.java110.core.context.IPageData;
import com.java110.api.smo.applicationKey.IEditApplicationKeySMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 编辑小区组件
 */
@Component("editApplicationKey")
public class EditApplicationKeyComponent {

    @Autowired
    private IEditApplicationKeySMO editApplicationKeySMOImpl;

    /**
     * 添加小区数据
     *
     * @param pd 页面数据封装
     * @return ResponseEntity 对象
     */
    public ResponseEntity<String> update(IPageData pd) {
        return editApplicationKeySMOImpl.updateApplicationKey(pd);
    }

    public IEditApplicationKeySMO getEditApplicationKeySMOImpl() {
        return editApplicationKeySMOImpl;
    }

    public void setEditApplicationKeySMOImpl(IEditApplicationKeySMO editApplicationKeySMOImpl) {
        this.editApplicationKeySMOImpl = editApplicationKeySMOImpl;
    }
}
