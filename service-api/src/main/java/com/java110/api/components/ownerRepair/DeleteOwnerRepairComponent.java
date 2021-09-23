package com.java110.api.components.ownerRepair;

import com.java110.core.context.IPageData;
import com.java110.api.smo.ownerRepair.IDeleteOwnerRepairSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 添加业主报修组件
 */
@Component("deleteOwnerRepair")
public class DeleteOwnerRepairComponent {

    @Autowired
    private IDeleteOwnerRepairSMO deleteOwnerRepairSMOImpl;

    /**
     * 添加业主报修数据
     *
     * @param pd 页面数据封装
     * @return ResponseEntity 对象
     */
    public ResponseEntity<String> delete(IPageData pd) {
        return deleteOwnerRepairSMOImpl.deleteOwnerRepair(pd);
    }

    public IDeleteOwnerRepairSMO getDeleteOwnerRepairSMOImpl() {
        return deleteOwnerRepairSMOImpl;
    }

    public void setDeleteOwnerRepairSMOImpl(IDeleteOwnerRepairSMO deleteOwnerRepairSMOImpl) {
        this.deleteOwnerRepairSMOImpl = deleteOwnerRepairSMOImpl;
    }
}
