package com.java110.api.components.applicationKey;

import com.java110.core.context.IPageData;
import com.java110.api.smo.applicationKey.IAuditApplicationKeySMO;
import com.java110.api.smo.applicationKey.IListApplicationKeysSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 编辑小区组件
 */
@Component("auditApplicationKeyManage")
public class AuditApplicationKeyManageComponent {

    @Autowired
    private IAuditApplicationKeySMO auditApplicationKeySMOImpl;

    @Autowired
    private IListApplicationKeysSMO listApplicationKeysSMOImpl;

    public ResponseEntity<String> list(IPageData pd) {
        return listApplicationKeysSMOImpl.listApplicationKeys(pd);
    }

    /**
     * 添加小区数据
     *
     * @param pd 页面数据封装
     * @return ResponseEntity 对象
     */
    public ResponseEntity<String> audit(IPageData pd) {
        return auditApplicationKeySMOImpl.auditApplicationKey(pd);
    }

    public IAuditApplicationKeySMO getAuditApplicationKeySMOImpl() {
        return auditApplicationKeySMOImpl;
    }

    public void setAuditApplicationKeySMOImpl(IAuditApplicationKeySMO auditApplicationKeySMOImpl) {
        this.auditApplicationKeySMOImpl = auditApplicationKeySMOImpl;
    }

    public IListApplicationKeysSMO getListApplicationKeysSMOImpl() {
        return listApplicationKeysSMOImpl;
    }

    public void setListApplicationKeysSMOImpl(IListApplicationKeysSMO listApplicationKeysSMOImpl) {
        this.listApplicationKeysSMOImpl = listApplicationKeysSMOImpl;
    }
}
