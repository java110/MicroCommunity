package com.java110.api.components.org;

import com.java110.core.context.IPageData;
import com.java110.api.smo.ICommunityServiceSMO;
import com.java110.api.smo.org.IAddOrgCommunitySMO;
import com.java110.api.smo.org.IListOrgNoCommunitysSMO;
import com.java110.api.smo.org.IListParentOrgsSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 添加组织管理组件
 */
@Component("addOrgCommunity")
public class AddOrgCommunityComponent {

    @Autowired
    private IAddOrgCommunitySMO addOrgCommunitySMOImpl;

    @Autowired
    private IListOrgNoCommunitysSMO listOrgNoCommunitysSMOImpl;

    @Autowired
    private IListParentOrgsSMO listParentOrgsSMOImpl;
    @Autowired
    private ICommunityServiceSMO communityServiceSMOImpl;

    /**
     * 添加组织管理数据
     *
     * @param pd 页面数据封装
     * @return ResponseEntity 对象
     */
    public ResponseEntity<String> save(IPageData pd) {
        return addOrgCommunitySMOImpl.saveOrgCommunity(pd);
    }

    /**
     * 查询组织管理列表
     *
     * @param pd 页面数据封装
     * @return 返回 ResponseEntity 对象
     */
    public ResponseEntity<String> list(IPageData pd) {
        return listOrgNoCommunitysSMOImpl.listOrgNoCommunitys(pd);
    }


    public ResponseEntity<String> getParentOrg(IPageData pd) {
        return listParentOrgsSMOImpl.listParentOrgs(pd);
    }

    public IListParentOrgsSMO getListParentOrgsSMOImpl() {
        return listParentOrgsSMOImpl;
    }

    public void setListParentOrgsSMOImpl(IListParentOrgsSMO listParentOrgsSMOImpl) {
        this.listParentOrgsSMOImpl = listParentOrgsSMOImpl;
    }


    public ICommunityServiceSMO getCommunityServiceSMOImpl() {
        return communityServiceSMOImpl;
    }

    public void setCommunityServiceSMOImpl(ICommunityServiceSMO communityServiceSMOImpl) {
        this.communityServiceSMOImpl = communityServiceSMOImpl;
    }
}
