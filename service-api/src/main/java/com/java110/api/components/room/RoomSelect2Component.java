package com.java110.api.components.room;

import com.java110.core.context.IPageData;
import com.java110.api.smo.IRoomServiceSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * @ClassName RoomComponent
 * @Description TODO 房屋组件
 * @Author wuxw
 * @Date 2019/5/7 23:40
 * @Version 1.0
 * add by wuxw 2019/5/7
 **/
@Component("roomSelect2")
public class RoomSelect2Component {

    @Autowired
    private IRoomServiceSMO roomServiceSMOImpl;


    /**
     * 显示房屋信息
     *
     * @param pd 页面信息封装
     * @return ResponseEntity对象
     */
    public ResponseEntity<String> listRoom(IPageData pd) {
        return roomServiceSMOImpl.listRoom(pd);
    }

    public IRoomServiceSMO getRoomServiceSMOImpl() {
        return roomServiceSMOImpl;
    }

    public void setRoomServiceSMOImpl(IRoomServiceSMO roomServiceSMOImpl) {
        this.roomServiceSMOImpl = roomServiceSMOImpl;
    }
}
