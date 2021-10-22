package com.java110.api.components.community;


import com.alibaba.fastjson.JSONObject;
import com.java110.core.context.IPageData;
import com.java110.core.context.PageData;
import com.java110.api.smo.community.IAuditEnterCommunitySMO;
import com.java110.api.smo.community.IListAuditEnterCommunitysSMO;
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
@Component("auditEnterCommunityManage")
public class AuditEnterCommunityManageComponent {

    @Autowired
    private IListAuditEnterCommunitysSMO listAuditEnterCommunitysSMOImpl;

    @Autowired
    private IAuditEnterCommunitySMO auditEnterCommunitySMOImpl;

    /**
     * 查询小区列表
     *
     * @param pd 页面数据封装
     * @return 返回 ResponseEntity 对象
     */
    public ResponseEntity<String> list(IPageData pd) {

        JSONObject reqParam = JSONObject.parseObject(pd.getReqData());
        //reqParam.put("auditStatusCd", StateConstant.NO_AUDIT);

        IPageData newPd = PageData.newInstance().builder(pd.getUserId(), pd.getUserName(), pd.getToken(),
                reqParam.toJSONString(), pd.getComponentCode(), pd.getComponentMethod(),
                "", pd.getSessionId(),pd.getAppId(),pd.getHeaders());

        return listAuditEnterCommunitysSMOImpl.listAuditEnterCommunitys(newPd);
    }

    /**
     * 审核 小区
     *
     * @param pd 页面数据封装
     * @return 返回 ResponseEntity 对象
     */
    public ResponseEntity<String> audit(IPageData pd) {
        return auditEnterCommunitySMOImpl.auditEnterCommunity(pd);
    }

    public IAuditEnterCommunitySMO getAuditEnterCommunitySMOImpl() {
        return auditEnterCommunitySMOImpl;
    }

    public void setAuditEnterCommunitySMOImpl(IAuditEnterCommunitySMO auditEnterCommunitySMOImpl) {
        this.auditEnterCommunitySMOImpl = auditEnterCommunitySMOImpl;
    }

    public IListAuditEnterCommunitysSMO getListAuditEnterCommunitysSMOImpl() {
        return listAuditEnterCommunitysSMOImpl;
    }

    public void setListAuditEnterCommunitysSMOImpl(IListAuditEnterCommunitysSMO listAuditEnterCommunitysSMOImpl) {
        this.listAuditEnterCommunitysSMOImpl = listAuditEnterCommunitysSMOImpl;
    }
}
