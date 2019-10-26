package com.java110.web.components.auditUser;

import com.java110.core.context.IPageData;
import com.java110.web.smo.auditUser.IAddAuditUserSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 添加审核人员组件
 */
@Component("addAuditUser")
public class AddAuditUserComponent {

    @Autowired
    private IAddAuditUserSMO addAuditUserSMOImpl;

    /**
     * 添加审核人员数据
     * @param pd 页面数据封装
     * @return ResponseEntity 对象
     */
    public ResponseEntity<String> save(IPageData pd){
        return addAuditUserSMOImpl.saveAuditUser(pd);
    }

    public IAddAuditUserSMO getAddAuditUserSMOImpl() {
        return addAuditUserSMOImpl;
    }

    public void setAddAuditUserSMOImpl(IAddAuditUserSMO addAuditUserSMOImpl) {
        this.addAuditUserSMOImpl = addAuditUserSMOImpl;
    }
}
