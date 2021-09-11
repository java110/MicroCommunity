package com.java110.api.components.owner;

import com.java110.core.context.IPageData;
import com.java110.api.smo.IOwnerServiceSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 删除小区楼信息
 */
@Component("deleteOwner")
public class DeleteOwnerComponent {

    @Autowired
    private IOwnerServiceSMO ownerServiceSMOImpl;

    /**
     * 删除小区楼
     *
     * @param pd 页面数据封装
     * @return 对象ResponseEntity
     */
    public ResponseEntity<String> delete(IPageData pd) {
        return ownerServiceSMOImpl.deleteOwner(pd);
    }


    public IOwnerServiceSMO getOwnerServiceSMOImpl() {
        return ownerServiceSMOImpl;
    }

    public void setOwnerServiceSMOImpl(IOwnerServiceSMO ownerServiceSMOImpl) {
        this.ownerServiceSMOImpl = ownerServiceSMOImpl;
    }
}
