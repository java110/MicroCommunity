package com.java110.web.components.auditUser;

import com.java110.core.context.IPageData;
import com.java110.web.smo.auditUser.IEditAuditUserSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 编辑小区组件
 */
@Component("editAuditUser")
public class EditAuditUserComponent {

    @Autowired
    private IEditAuditUserSMO editAuditUserSMOImpl;

    /**
     * 添加小区数据
     *
     * @param pd 页面数据封装
     * @return ResponseEntity 对象
     */
    public ResponseEntity<String> update(IPageData pd) {
        return editAuditUserSMOImpl.updateAuditUser(pd);
    }

    public IEditAuditUserSMO getEditAuditUserSMOImpl() {
        return editAuditUserSMOImpl;
    }

    public void setEditAuditUserSMOImpl(IEditAuditUserSMO editAuditUserSMOImpl) {
        this.editAuditUserSMOImpl = editAuditUserSMOImpl;
    }
}
