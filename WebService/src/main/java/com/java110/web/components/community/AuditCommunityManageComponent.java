package com.java110.web.components.community;


import com.alibaba.fastjson.JSONObject;
import com.java110.common.constant.CommunityStateConstant;
import com.java110.core.context.IPageData;
import com.java110.core.context.PageData;
import com.java110.web.smo.community.IListCommunitysSMO;
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

    /**
     * 查询小区列表
     *
     * @param pd 页面数据封装
     * @return 返回 ResponseEntity 对象
     */
    public ResponseEntity<String> list(IPageData pd) {

        JSONObject reqParam = JSONObject.parseObject(pd.getReqData());
        reqParam.put("state", CommunityStateConstant.NO_AUDIT);

        IPageData newPd = PageData.newInstance().builder(pd.getUserId(), pd.getToken(),
                reqParam.toJSONString(), pd.getComponentCode(), pd.getComponentMethod(), "", pd.getSessionId());

        return listCommunitysSMOImpl.listCommunitys(newPd);
    }

    public IListCommunitysSMO getListCommunitysSMOImpl() {
        return listCommunitysSMOImpl;
    }

    public void setListCommunitysSMOImpl(IListCommunitysSMO listCommunitysSMOImpl) {
        this.listCommunitysSMOImpl = listCommunitysSMOImpl;
    }
}
