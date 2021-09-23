package com.java110.api.components.community;


import com.java110.core.context.IPageData;
import com.java110.api.smo.ICommunityServiceSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 商户退出组件
 */
@Component("storeExitCommunity")
public class storeExitCommunityComponent {

    @Autowired
    private ICommunityServiceSMO communityServiceSMOImpl;

    /**
     * 商户退出组件
     *
     * @param pd
     * @return
     */
    public ResponseEntity<String> exit(IPageData pd) {
        ResponseEntity<String> responseEntity = null;
        try {
            responseEntity = communityServiceSMOImpl.exitCommunity(pd);
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
