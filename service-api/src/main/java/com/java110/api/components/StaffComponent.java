package com.java110.api.components;

import com.java110.core.context.IPageData;
import com.java110.api.smo.IStaffServiceSMO;
import com.java110.api.smo.org.IListOrgsSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 员工展示组件
 * Created by Administrator on 2019/4/2.
 */
@Component("staff")
public class StaffComponent {


    @Autowired
    IStaffServiceSMO staffServiceSMOImpl;

    @Autowired
    private IListOrgsSMO listOrgsSMOImpl;


    /**
     * 查询组织管理列表
     *
     * @param pd 页面数据封装
     * @return 返回 ResponseEntity 对象
     */
    public ResponseEntity<String> list(IPageData pd) {
        return listOrgsSMOImpl.listOrgs(pd);
    }

    public ResponseEntity<String> loadData(IPageData pd) {
        ResponseEntity<String> responseEntity = null;
        try {
            responseEntity = staffServiceSMOImpl.loadData(pd);
        } catch (Exception e) {
            responseEntity = new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
        }
        
        return responseEntity;
    }


    public IStaffServiceSMO getStaffServiceSMOImpl() {
        return staffServiceSMOImpl;
    }

    public void setStaffServiceSMOImpl(IStaffServiceSMO staffServiceSMOImpl) {
        this.staffServiceSMOImpl = staffServiceSMOImpl;
    }

    public IListOrgsSMO getListOrgsSMOImpl() {
        return listOrgsSMOImpl;
    }

    public void setListOrgsSMOImpl(IListOrgsSMO listOrgsSMOImpl) {
        this.listOrgsSMOImpl = listOrgsSMOImpl;
    }
}
