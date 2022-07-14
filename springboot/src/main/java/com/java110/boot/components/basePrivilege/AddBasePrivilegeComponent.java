package com.java110.boot.components.basePrivilege;

import com.java110.boot.smo.basePrivilege.IAddBasePrivilegeSMO;
import com.java110.core.context.IPageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 添加权限组件
 */
@Component("addBasePrivilege")
public class AddBasePrivilegeComponent {

    @Autowired
    private IAddBasePrivilegeSMO addBasePrivilegeSMOImpl;

    /**
     * 添加权限数据
     * @param pd 页面数据封装
     * @return ResponseEntity 对象
     */
    public ResponseEntity<String> save(IPageData pd){
        return addBasePrivilegeSMOImpl.saveBasePrivilege(pd);
    }

    public IAddBasePrivilegeSMO getAddBasePrivilegeSMOImpl() {
        return addBasePrivilegeSMOImpl;
    }

    public void setAddBasePrivilegeSMOImpl(IAddBasePrivilegeSMO addBasePrivilegeSMOImpl) {
        this.addBasePrivilegeSMOImpl = addBasePrivilegeSMOImpl;
    }
}
