package com.java110.web.components.ownerRepair;


import com.java110.core.context.IPageData;
import com.java110.web.smo.IRoomServiceSMO;
import com.java110.web.smo.ownerRepair.IListOwnerRepairsSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;


/**
 * 业主报修组件管理类
 *
 * add by wuxw
 *
 * 2019-06-29
 */
@Component("ownerRepairDetail")
public class OwnerRepairDetailComponent {


    @Autowired
    private IRoomServiceSMO roomServiceSMOImpl;


    public ResponseEntity<String> getRoom(IPageData pd){

        return roomServiceSMOImpl.listRoom(pd);
    }

    public IRoomServiceSMO getRoomServiceSMOImpl() {
        return roomServiceSMOImpl;
    }

    public void setRoomServiceSMOImpl(IRoomServiceSMO roomServiceSMOImpl) {
        this.roomServiceSMOImpl = roomServiceSMOImpl;
    }
}
