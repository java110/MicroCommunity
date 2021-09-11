package com.java110.api.components.org;


import com.java110.core.context.IPageData;
import com.java110.api.smo.org.IListOrgCommunitysSMO;
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
@Component("orgCommunityManage")
public class OrgCommunityManageComponent {

    @Autowired
    private IListOrgCommunitysSMO listOrgCommunitysSMOImpl;


    /**
     * 查询组织管理列表
     * @param pd 页面数据封装
     * @return 返回 ResponseEntity 对象
     */
    public ResponseEntity<String> list(IPageData pd){
        return listOrgCommunitysSMOImpl.listOrgCommunitys(pd);
    }

    public IListOrgCommunitysSMO getListOrgCommunitysSMOImpl() {
        return listOrgCommunitysSMOImpl;
    }

    public void setListOrgCommunitysSMOImpl(IListOrgCommunitysSMO listOrgCommunitysSMOImpl) {
        this.listOrgCommunitysSMOImpl = listOrgCommunitysSMOImpl;
    }
}
