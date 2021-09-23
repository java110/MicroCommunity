package com.java110.api.components.applicationKey;

import com.java110.core.context.IPageData;
import com.java110.api.smo.applicationKey.IAddApplicationKeySMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 添加钥匙申请组件
 */
@Component("addApplicationKey")
public class AddApplicationKeyComponent {

    @Autowired
    private IAddApplicationKeySMO addApplicationKeySMOImpl;

    /**
     * 添加钥匙申请数据
     * @param pd 页面数据封装
     * @return ResponseEntity 对象
     */
    public ResponseEntity<String> save(IPageData pd){
        return addApplicationKeySMOImpl.saveApplicationKey(pd);
    }

    public IAddApplicationKeySMO getAddApplicationKeySMOImpl() {
        return addApplicationKeySMOImpl;
    }

    public void setAddApplicationKeySMOImpl(IAddApplicationKeySMO addApplicationKeySMOImpl) {
        this.addApplicationKeySMOImpl = addApplicationKeySMOImpl;
    }
}
