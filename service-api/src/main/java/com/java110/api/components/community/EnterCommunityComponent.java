package com.java110.api.components.community;

import com.java110.core.context.IPageData;
import com.java110.api.smo.ICommunityServiceSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 入驻小区组件处理类
 * <p>
 * add by wuxw 2019-04-15
 */
@Component("enterCommunity")
public class EnterCommunityComponent {

    @Autowired
    private ICommunityServiceSMO communityServiceSMOImpl;

    /**
     * 查询我入驻的小区
     *
     * @param pd
     * @return
     */
    public ResponseEntity<String> listMyCommunity(IPageData pd) {
        ResponseEntity<String> responseEntity = null;
        try {
            responseEntity = communityServiceSMOImpl.listMyCommunity(pd);
        } catch (Exception e) {
            responseEntity = new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
        }
        return responseEntity;
    }


    public ICommunityServiceSMO getCommunityServiceSMOImpl() {
        return communityServiceSMOImpl;
    }

    public void setCommunityServiceSMOImpl(ICommunityServiceSMO communityServiceSMOImpl) {
        this.communityServiceSMOImpl = communityServiceSMOImpl;
    }
}
