package com.java110.web.components.org;

import com.java110.core.context.IPageData;
import com.java110.web.smo.org.IAddOrgSMO;
import com.java110.web.smo.org.IListOrgsSMO;
import com.java110.web.smo.org.IListParentOrgsSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 添加组织管理组件
 */
@Component("addOrg")
public class AddOrgComponent {

    @Autowired
    private IAddOrgSMO addOrgSMOImpl;

    @Autowired
    private IListOrgsSMO listOrgsSMOImpl;

    @Autowired
    private IListParentOrgsSMO listParentOrgsSMOImpl;

    /**
     * 添加组织管理数据
     * @param pd 页面数据封装
     * @return ResponseEntity 对象
     */
    public ResponseEntity<String> save(IPageData pd){
        return addOrgSMOImpl.saveOrg(pd);
    }
    /**
     * 查询组织管理列表
     * @param pd 页面数据封装
     * @return 返回 ResponseEntity 对象
     */
    public ResponseEntity<String> list(IPageData pd){
        return listOrgsSMOImpl.listOrgs(pd);
    }



    public ResponseEntity<String> getParentOrg(IPageData pd){
        return listParentOrgsSMOImpl.listParentOrgs(pd);
    }

    public IAddOrgSMO getAddOrgSMOImpl() {
        return addOrgSMOImpl;
    }

    public void setAddOrgSMOImpl(IAddOrgSMO addOrgSMOImpl) {
        this.addOrgSMOImpl = addOrgSMOImpl;
    }

    public IListParentOrgsSMO getListParentOrgsSMOImpl() {
        return listParentOrgsSMOImpl;
    }

    public void setListParentOrgsSMOImpl(IListParentOrgsSMO listParentOrgsSMOImpl) {
        this.listParentOrgsSMOImpl = listParentOrgsSMOImpl;
    }

    public IListOrgsSMO getListOrgsSMOImpl() {
        return listOrgsSMOImpl;
    }

    public void setListOrgsSMOImpl(IListOrgsSMO listOrgsSMOImpl) {
        this.listOrgsSMOImpl = listOrgsSMOImpl;
    }
}
