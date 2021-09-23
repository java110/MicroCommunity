package com.java110.api.components.ownerRepair;

import com.java110.core.context.IPageData;
import com.java110.api.smo.IOwnerServiceSMO;
import com.java110.api.smo.ownerRepair.IAddOwnerRepairSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 添加业主报修组件
 */
@Component("addOwnerRepair")
public class AddOwnerRepairComponent {

    @Autowired
    private IAddOwnerRepairSMO addOwnerRepairSMOImpl;


    @Autowired
    private IOwnerServiceSMO ownerServiceSMOImpl;

    /**
     * 添加业主报修数据
     *
     * @param pd 页面数据封装
     * @return ResponseEntity 对象
     */
    public ResponseEntity<String> save(IPageData pd) {
        return addOwnerRepairSMOImpl.saveOwnerRepair(pd);
    }

    /**
     * 查询业主房屋信息
     *
     * @param pd 页面数据封装
     * @return ResponseEntity 对象
     */
    public ResponseEntity<String> getOwner(IPageData pd) {
        ResponseEntity<String> responseEntity = null;
        responseEntity = ownerServiceSMOImpl.listOwner(pd);

        return responseEntity;
    }


    public IAddOwnerRepairSMO getAddOwnerRepairSMOImpl() {
        return addOwnerRepairSMOImpl;
    }

    public void setAddOwnerRepairSMOImpl(IAddOwnerRepairSMO addOwnerRepairSMOImpl) {
        this.addOwnerRepairSMOImpl = addOwnerRepairSMOImpl;
    }

    public IOwnerServiceSMO getOwnerServiceSMOImpl() {
        return ownerServiceSMOImpl;
    }

    public void setOwnerServiceSMOImpl(IOwnerServiceSMO ownerServiceSMOImpl) {
        this.ownerServiceSMOImpl = ownerServiceSMOImpl;
    }
}
