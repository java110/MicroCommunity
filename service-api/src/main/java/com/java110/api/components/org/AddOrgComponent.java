package com.java110.api.components.org;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.context.IPageData;
import com.java110.core.context.PageData;
import com.java110.api.smo.ICommunityServiceSMO;
import com.java110.api.smo.org.IAddOrgSMO;
import com.java110.api.smo.org.IListOrgsSMO;
import com.java110.api.smo.org.IListParentOrgsSMO;
import com.java110.utils.constant.StateConstant;
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
    @Autowired
    private ICommunityServiceSMO communityServiceSMOImpl;

    /**
     * 添加组织管理数据
     *
     * @param pd 页面数据封装
     * @return ResponseEntity 对象
     */
    public ResponseEntity<String> save(IPageData pd) {
        return addOrgSMOImpl.saveOrg(pd);
    }

    public ResponseEntity<String> listEnterCommunitys(IPageData pd) {

        JSONObject paramIn = JSONObject.parseObject(pd.getReqData());
        paramIn.put("auditStatusCd", StateConstant.AGREE_AUDIT);

        IPageData newPd = PageData.newInstance().builder(pd.getUserId(),
                pd.getUserName(), pd.getToken(),
                paramIn.toJSONString(), pd.getComponentCode(),
                pd.getComponentMethod(), "",
                pd.getSessionId(),
                pd.getAppId(),
                pd.getHeaders());

        return communityServiceSMOImpl.listMyCommunity(newPd);

    }

    /**
     * 查询组织管理列表
     *
     * @param pd 页面数据封装
     * @return 返回 ResponseEntity 对象
     */
    public ResponseEntity<String> list(IPageData pd) {
        return listOrgsSMOImpl.listOrgs(pd);
    }


    public ResponseEntity<String> getParentOrg(IPageData pd) {
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

    public ICommunityServiceSMO getCommunityServiceSMOImpl() {
        return communityServiceSMOImpl;
    }

    public void setCommunityServiceSMOImpl(ICommunityServiceSMO communityServiceSMOImpl) {
        this.communityServiceSMOImpl = communityServiceSMOImpl;
    }
}
