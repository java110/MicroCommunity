package com.java110.api.components.basePrivilege;

import com.java110.core.context.IPageData;
import com.java110.api.smo.basePrivilege.IEditBasePrivilegeSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 编辑小区组件
 */
@Component("editBasePrivilege")
public class EditBasePrivilegeComponent {

    @Autowired
    private IEditBasePrivilegeSMO editBasePrivilegeSMOImpl;

    /**
     * 添加小区数据
     * @param pd 页面数据封装
     * @return ResponseEntity 对象
     */
    public ResponseEntity<String> update(IPageData pd){
        return editBasePrivilegeSMOImpl.updateBasePrivilege(pd);
    }

    public IEditBasePrivilegeSMO getEditBasePrivilegeSMOImpl() {
        return editBasePrivilegeSMOImpl;
    }

    public void setEditBasePrivilegeSMOImpl(IEditBasePrivilegeSMO editBasePrivilegeSMOImpl) {
        this.editBasePrivilegeSMOImpl = editBasePrivilegeSMOImpl;
    }
}
