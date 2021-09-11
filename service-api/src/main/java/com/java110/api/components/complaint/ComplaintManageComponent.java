package com.java110.api.components.complaint;


import com.java110.core.context.IPageData;
import com.java110.api.smo.complaint.IListComplaintsSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;


/**
 * 投诉建议组件管理类
 * <p>
 * add by wuxw
 * <p>
 * 2019-06-29
 */
@Component("complaintManage")
public class ComplaintManageComponent {

    @Autowired
    private IListComplaintsSMO listComplaintsSMOImpl;

    /**
     * 查询投诉建议列表
     *
     * @param pd 页面数据封装
     * @return 返回 ResponseEntity 对象
     */
    public ResponseEntity<String> list(IPageData pd) {
        return listComplaintsSMOImpl.listComplaints(pd);
    }

    public IListComplaintsSMO getListComplaintsSMOImpl() {
        return listComplaintsSMOImpl;
    }

    public void setListComplaintsSMOImpl(IListComplaintsSMO listComplaintsSMOImpl) {
        this.listComplaintsSMOImpl = listComplaintsSMOImpl;
    }
}
