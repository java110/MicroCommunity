package com.java110.api.components.owner;


import com.java110.core.context.IPageData;
import com.java110.api.smo.auditUser.IEditAuditAppUserBindingOwnerSMO;
import com.java110.api.smo.auditUser.IListAuditAppUserBindingOwnersSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;


/**
 * 审核业主绑定组件管理类
 * <p>
 * add by wuxw
 * <p>
 * 2019-06-29
 */
@Component("auditAppUserBindingOwnerManage")
public class AuditAppUserBindingOwnerManageComponent {

    @Autowired
    private IListAuditAppUserBindingOwnersSMO listAuditAppUserBindingOwnersSMOImpl;

    @Autowired
    private IEditAuditAppUserBindingOwnerSMO editAuditAppUserBindingOwnerSMOImpl;

    /**
     * 查询审核业主绑定列表
     *
     * @param pd 页面数据封装
     * @return 返回 ResponseEntity 对象
     */
    public ResponseEntity<String> list(IPageData pd) {
        return listAuditAppUserBindingOwnersSMOImpl.listAuditAppUserBindingOwners(pd);
    }
    /**
     * 查询审核业主绑定列表
     *
     * @param pd 页面数据封装
     * @return 返回 ResponseEntity 对象
     */
    public ResponseEntity<String> audit(IPageData pd) {
        return editAuditAppUserBindingOwnerSMOImpl.updateAuditAppUserBindingOwner(pd);
    }



    public IListAuditAppUserBindingOwnersSMO getListAuditAppUserBindingOwnersSMOImpl() {
        return listAuditAppUserBindingOwnersSMOImpl;
    }

    public void setListAuditAppUserBindingOwnersSMOImpl(IListAuditAppUserBindingOwnersSMO listAuditAppUserBindingOwnersSMOImpl) {
        this.listAuditAppUserBindingOwnersSMOImpl = listAuditAppUserBindingOwnersSMOImpl;
    }

    public IEditAuditAppUserBindingOwnerSMO getEditAuditAppUserBindingOwnerSMOImpl() {
        return editAuditAppUserBindingOwnerSMOImpl;
    }

    public void setEditAuditAppUserBindingOwnerSMOImpl(IEditAuditAppUserBindingOwnerSMO editAuditAppUserBindingOwnerSMOImpl) {
        this.editAuditAppUserBindingOwnerSMOImpl = editAuditAppUserBindingOwnerSMOImpl;
    }
}
