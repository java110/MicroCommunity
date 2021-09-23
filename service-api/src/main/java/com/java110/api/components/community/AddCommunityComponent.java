package com.java110.api.components.community;

import com.java110.core.context.IPageData;
import com.java110.api.smo.ICompanyServiceSMO;
import com.java110.api.smo.community.IAddCommunitySMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 添加小区组件
 */
@Component("addCommunity")
public class AddCommunityComponent {

    @Autowired
    private IAddCommunitySMO addCommunitySMOImpl;

    @Autowired
    private ICompanyServiceSMO companyServiceSMOImpl;

    /**
     * 添加小区数据
     *
     * @param pd 页面数据封装
     * @return ResponseEntity 对象
     */
    public ResponseEntity<String> save(IPageData pd) {
        return addCommunitySMOImpl.saveCommunity(pd);
    }

    /**
     * 功能描述: 查询所有省市
     *
     * @param pd
     * @return
     */
    public ResponseEntity<String> getAreas(IPageData pd) {
        ResponseEntity<String> responseEntity = null;
        try {
            responseEntity = companyServiceSMOImpl.getAreas(pd);
        } catch (Exception e) {
            responseEntity = new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
        }
        
        return responseEntity;
    }

    public IAddCommunitySMO getAddCommunitySMOImpl() {
        return addCommunitySMOImpl;
    }

    public void setAddCommunitySMOImpl(IAddCommunitySMO addCommunitySMOImpl) {
        this.addCommunitySMOImpl = addCommunitySMOImpl;
    }

    public ICompanyServiceSMO getCompanyServiceSMOImpl() {
        return companyServiceSMOImpl;
    }

    public void setCompanyServiceSMOImpl(ICompanyServiceSMO companyServiceSMOImpl) {
        this.companyServiceSMOImpl = companyServiceSMOImpl;
    }
}
