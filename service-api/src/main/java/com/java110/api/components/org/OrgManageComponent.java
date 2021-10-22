package com.java110.api.components.org;


import com.java110.core.context.IPageData;
import com.java110.api.smo.org.IListOrgsSMO;
import com.java110.api.smo.org.IListParentOrgsSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;


/**
 * 组织管理组件管理类
 *
 * add by wuxw
 *
 * 2019-06-29
 */
@Component("orgManage")
public class OrgManageComponent {

    @Autowired
    private IListOrgsSMO listOrgsSMOImpl;

    @Autowired
    private IListParentOrgsSMO listParentOrgsSMOImpl;

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

    public IListOrgsSMO getListOrgsSMOImpl() {
        return listOrgsSMOImpl;
    }

    public void setListOrgsSMOImpl(IListOrgsSMO listOrgsSMOImpl) {
        this.listOrgsSMOImpl = listOrgsSMOImpl;
    }


    public IListParentOrgsSMO getListParentOrgsSMOImpl() {
        return listParentOrgsSMOImpl;
    }

    public void setListParentOrgsSMOImpl(IListParentOrgsSMO listParentOrgsSMOImpl) {
        this.listParentOrgsSMOImpl = listParentOrgsSMOImpl;
    }
}
