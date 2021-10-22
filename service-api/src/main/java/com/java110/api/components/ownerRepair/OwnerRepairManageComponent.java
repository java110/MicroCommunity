package com.java110.api.components.ownerRepair;


import com.java110.core.context.IPageData;
import com.java110.api.smo.IRoomServiceSMO;
import com.java110.api.smo.ownerRepair.IListOwnerRepairsSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;


/**
 * 业主报修组件管理类
 * <p>
 * add by wuxw
 * <p>
 * 2019-06-29
 */
@Component("ownerRepairManage")
public class OwnerRepairManageComponent {

    @Autowired
    private IListOwnerRepairsSMO listOwnerRepairsSMOImpl;

    @Autowired
    private IRoomServiceSMO roomServiceSMOImpl;

    /**
     * 查询业主报修列表
     *
     * @param pd 页面数据封装
     * @return 返回 ResponseEntity 对象
     */
    public ResponseEntity<String> list(IPageData pd) {
        return listOwnerRepairsSMOImpl.listOwnerRepairs(pd);
    }

    public ResponseEntity<String> getRoom(IPageData pd) {

        return roomServiceSMOImpl.listRoom(pd);
    }

    public IListOwnerRepairsSMO getListOwnerRepairsSMOImpl() {
        return listOwnerRepairsSMOImpl;
    }

    public void setListOwnerRepairsSMOImpl(IListOwnerRepairsSMO listOwnerRepairsSMOImpl) {
        this.listOwnerRepairsSMOImpl = listOwnerRepairsSMOImpl;
    }

    public IRoomServiceSMO getRoomServiceSMOImpl() {
        return roomServiceSMOImpl;
    }

    public void setRoomServiceSMOImpl(IRoomServiceSMO roomServiceSMOImpl) {
        this.roomServiceSMOImpl = roomServiceSMOImpl;
    }
}
