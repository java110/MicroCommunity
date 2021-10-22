package com.java110.api.components.auditUser;


import com.java110.core.context.IPageData;
import com.java110.api.smo.auditUser.IListAuditComplaintsSMO;
import com.java110.api.smo.complaint.IAuditComplaintSMO;
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
@Component("myAuditComplaints")
public class MyAuditComplaintsComponent {

    @Autowired
    private IListAuditComplaintsSMO listAuditComplaintsSMOImpl;

    @Autowired
    private IAuditComplaintSMO auditComplaintSMOImpl;

    /**
     * 查询审核人员列表
     *
     * @param pd 页面数据封装
     * @return 返回 ResponseEntity 对象
     */
    public ResponseEntity<String> list(IPageData pd) {
        return listAuditComplaintsSMOImpl.listAuditComplaints(pd);
    }

    /**
     * 审核任务
     *
     * @param pd 页面数据封装
     * @return 返回 ResponseEntity 对象
     */
    public ResponseEntity<String> audit(IPageData pd) {
        return auditComplaintSMOImpl.auditComplaint(pd);
    }

    public IListAuditComplaintsSMO getListAuditComplaintsSMOImpl() {
        return listAuditComplaintsSMOImpl;
    }

    public void setListAuditComplaintsSMOImpl(IListAuditComplaintsSMO listAuditComplaintsSMOImpl) {
        this.listAuditComplaintsSMOImpl = listAuditComplaintsSMOImpl;
    }

    public IAuditComplaintSMO getAuditComplaintSMOImpl() {
        return auditComplaintSMOImpl;
    }

    public void setAuditComplaintSMOImpl(IAuditComplaintSMO auditComplaintSMOImpl) {
        this.auditComplaintSMOImpl = auditComplaintSMOImpl;
    }
}
