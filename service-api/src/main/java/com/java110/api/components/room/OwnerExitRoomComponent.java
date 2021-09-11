package com.java110.api.components.room;


import com.java110.core.context.IPageData;
import com.java110.api.smo.IRoomServiceSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 退房
 * <p>
 * add by wuxw 2019-05-22
 */
@Component("ownerExitRoom")
public class OwnerExitRoomComponent {

    @Autowired
    private IRoomServiceSMO roomServiceSMOImpl;

    /**
     * 售卖房屋
     *
     * @param pd 包含floorId 和小区ID 页面封装对象
     * @return 单元信息
     */
    public ResponseEntity<String> exit(IPageData pd) {
        return roomServiceSMOImpl.exitRoom(pd);
    }


    public IRoomServiceSMO getRoomServiceSMOImpl() {
        return roomServiceSMOImpl;
    }

    public void setRoomServiceSMOImpl(IRoomServiceSMO roomServiceSMOImpl) {
        this.roomServiceSMOImpl = roomServiceSMOImpl;
    }
}
