package com.java110.api.components.community;


import com.alibaba.fastjson.JSONObject;
import com.java110.core.context.IPageData;
import com.java110.core.context.PageData;
import com.java110.api.smo.community.IAuditCommunitySMO;
import com.java110.api.smo.community.IListCommunitysSMO;
import com.java110.utils.constant.StateConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;


/**
 * 小区审核
 * <p>
 * add by wuxw
 * <p>
 * 2019-06-29
 */
@Component("auditCommunityManage")
public class AuditCommunityManageComponent {

    @Autowired
    private IListCommunitysSMO listCommunitysSMOImpl;

    @Autowired
    private IAuditCommunitySMO auditCommunitySMOImpl;

    /**
     * 查询小区列表
     *
     * @param pd 页面数据封装
     * @return 返回 ResponseEntity 对象
     */
    public ResponseEntity<String> list(IPageData pd) {

        JSONObject reqParam = JSONObject.parseObject(pd.getReqData());
        reqParam.put("state", StateConstant.NO_AUDIT);

        IPageData newPd = PageData.newInstance().builder(pd.getUserId(), pd.getUserName(), pd.getToken(),
                reqParam.toJSONString(), pd.getComponentCode(), pd.getComponentMethod(), "", pd.getSessionId(),pd.getAppId(),pd.getHeaders());

        return listCommunitysSMOImpl.listCommunitys(newPd);
    }

    /**
     * 审核 小区
     *
     * @param pd 页面数据封装
     * @return 返回 ResponseEntity 对象
     */
    public ResponseEntity<String> audit(IPageData pd) {
        return auditCommunitySMOImpl.auditCommunity(pd);
    }

    public IListCommunitysSMO getListCommunitysSMOImpl() {
        return listCommunitysSMOImpl;
    }

    public void setListCommunitysSMOImpl(IListCommunitysSMO listCommunitysSMOImpl) {
        this.listCommunitysSMOImpl = listCommunitysSMOImpl;
    }

    public IAuditCommunitySMO getAuditCommunitySMOImpl() {
        return auditCommunitySMOImpl;
    }

    public void setAuditCommunitySMOImpl(IAuditCommunitySMO auditCommunitySMOImpl) {
        this.auditCommunitySMOImpl = auditCommunitySMOImpl;
    }
}
