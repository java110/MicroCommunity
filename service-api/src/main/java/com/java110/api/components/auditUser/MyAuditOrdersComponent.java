package com.java110.api.components.auditUser;


import com.java110.core.context.IPageData;
import com.java110.api.smo.auditUser.IAuditOrdersSMO;
import com.java110.api.smo.auditUser.IListAuditOrdersSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;


/**
 * 我的审核单查询
 * <p>
 * add by wuxw
 * <p>
 * 2019-06-29
 */


@Component("myAuditOrders")
public class MyAuditOrdersComponent {

    @Autowired
    private IListAuditOrdersSMO listAuditOrdersSMOImpl;

    @Autowired
    private IAuditOrdersSMO auditAuditOrdersSMOImpl;

    /**
     * 查询审核人员列表
     *
     * @param pd 页面数据封装
     * @return 返回 ResponseEntity 对象
     */
    public ResponseEntity<String> list(IPageData pd) {
        return listAuditOrdersSMOImpl.listAuditOrders(pd);
    }

    public ResponseEntity<String> audit(IPageData pd) {
        return auditAuditOrdersSMOImpl.auditOrder(pd);
    }


    public IListAuditOrdersSMO getListAuditOrdersSMOImpl() {
        return listAuditOrdersSMOImpl;
    }

    public void setListAuditOrdersSMOImpl(IListAuditOrdersSMO listAuditOrdersSMOImpl) {
        this.listAuditOrdersSMOImpl = listAuditOrdersSMOImpl;
    }
}
