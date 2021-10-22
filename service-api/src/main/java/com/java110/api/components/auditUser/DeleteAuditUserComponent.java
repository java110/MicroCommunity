package com.java110.api.components.auditUser;

import com.java110.core.context.IPageData;
import com.java110.api.smo.auditUser.IDeleteAuditUserSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 添加审核人员组件
 */
@Component("deleteAuditUser")
public class DeleteAuditUserComponent {

@Autowired
private IDeleteAuditUserSMO deleteAuditUserSMOImpl;

/**
 * 添加审核人员数据
 * @param pd 页面数据封装
 * @return ResponseEntity 对象
 */
public ResponseEntity<String> delete(IPageData pd){
        return deleteAuditUserSMOImpl.deleteAuditUser(pd);
    }

public IDeleteAuditUserSMO getDeleteAuditUserSMOImpl() {
        return deleteAuditUserSMOImpl;
    }

public void setDeleteAuditUserSMOImpl(IDeleteAuditUserSMO deleteAuditUserSMOImpl) {
        this.deleteAuditUserSMOImpl = deleteAuditUserSMOImpl;
    }
            }
