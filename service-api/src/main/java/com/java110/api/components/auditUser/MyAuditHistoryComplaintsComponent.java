package com.java110.api.components.auditUser;


import com.java110.core.context.IPageData;
import com.java110.api.smo.auditUser.IListAuditHistoryComplaintsSMO;
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
@Component("myAuditHistoryComplaints")
public class MyAuditHistoryComplaintsComponent {

    @Autowired
    private IListAuditHistoryComplaintsSMO listAuditHistoryComplaintsSMOImpl;



    /**
     * 查询审核人员列表
     *
     * @param pd 页面数据封装
     * @return 返回 ResponseEntity 对象
     */
    public ResponseEntity<String> list(IPageData pd) {
        return listAuditHistoryComplaintsSMOImpl.listAuditHistoryComplaints(pd);
    }

    public IListAuditHistoryComplaintsSMO getListAuditHistoryComplaintsSMOImpl() {
        return listAuditHistoryComplaintsSMOImpl;
    }

    public void setListAuditHistoryComplaintsSMOImpl(IListAuditHistoryComplaintsSMO listAuditHistoryComplaintsSMOImpl) {
        this.listAuditHistoryComplaintsSMOImpl = listAuditHistoryComplaintsSMOImpl;
    }

}
