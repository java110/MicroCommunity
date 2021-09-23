package com.java110.api.components.community;

import com.java110.core.context.IPageData;
import com.java110.api.smo.ICommunityServiceSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 商户申请入驻小区
 */
@Component("storeEnterCommunity")
public class StoreEnterCommunityComponent {

    @Autowired
    private ICommunityServiceSMO communityServiceSMOImpl;


    /**
     * 显示还没有入驻的小区
     * @param pd
     * @return
     */
    public ResponseEntity<String> listNoEnterCommunity(IPageData pd){
            ResponseEntity<String> responseEntity = null;
            try{
                responseEntity =  communityServiceSMOImpl.listNoEnterCommunity(pd);
            }catch (Exception e){
                responseEntity = new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }finally {
            }
            
            return responseEntity;
    }

    /**
     * 入驻小区
     * @param pd
     * @return
     */
    public ResponseEntity<String> _saveEnterCommunity(IPageData pd){
        ResponseEntity<String> responseEntity = null;
        try{
            responseEntity =  communityServiceSMOImpl._saveEnterCommunity(pd);
        }catch (Exception e){
            responseEntity = new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }finally {
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
