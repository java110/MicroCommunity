package com.java110.api.components.owner;

import com.java110.core.context.IPageData;
import com.java110.api.smo.IOwnerServiceSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * @ClassName EditOwnerComponent
 * @Description TODO 编辑小区楼信息
 * @Author wuxw
 * @Date 2019/4/28 15:10
 * @Version 1.0
 * add by wuxw 2019/4/28
 **/

@Component("editOwner")
public class EditOwnerComponent {
    @Autowired
    private IOwnerServiceSMO ownerServiceSMOImpl;

    /**
     * 修改小区楼信息
     *
     * @param pd 页面数据封装
     * @return 返回ResponseEntity对象
     */
    public ResponseEntity<String> changeOwner(IPageData pd) {
        return ownerServiceSMOImpl.editOwner(pd);
    }


    public IOwnerServiceSMO getOwnerServiceSMOImpl() {
        return ownerServiceSMOImpl;
    }

    public void setOwnerServiceSMOImpl(IOwnerServiceSMO ownerServiceSMOImpl) {
        this.ownerServiceSMOImpl = ownerServiceSMOImpl;
    }
}
