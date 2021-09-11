package com.java110.api.components.room;

import com.java110.core.context.IPageData;
import com.java110.api.smo.IFloorServiceSMO;
import com.java110.api.smo.IRoomServiceSMO;
import com.java110.api.smo.IUnitServiceSMO;
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
@Component("room")
public class RoomComponent {


    @Autowired
    private IUnitServiceSMO unitServiceSMOImpl;


    @Autowired
    private IRoomServiceSMO roomServiceSMOImpl;

    @Autowired
    private IFloorServiceSMO floorServiceSMOImpl;

    /**
     * 显示房屋信息
     *
     * @param pd 页面信息封装
     * @return ResponseEntity对象
     */
    public ResponseEntity<String> listRoom(IPageData pd) {
        return roomServiceSMOImpl.listRoom(pd);
    }

    /**
     * 根据 floorId 查询单元信息
     *
     * @param pd 包含floorId 和小区ID 页面封装对象
     * @return 单元信息
     */
    public ResponseEntity<String> loadFloor(IPageData pd) {
        return floorServiceSMOImpl.getFloor(pd);
    }

    /**
     * 根据 floorId 查询单元信息
     *
     * @param pd 包含floorId 和小区ID 页面封装对象
     * @return 单元信息
     */
    public ResponseEntity<String> loadUnits(IPageData pd) {
        return unitServiceSMOImpl.listUnits(pd);
    }

    public IRoomServiceSMO getRoomServiceSMOImpl() {
        return roomServiceSMOImpl;
    }

    public void setRoomServiceSMOImpl(IRoomServiceSMO roomServiceSMOImpl) {
        this.roomServiceSMOImpl = roomServiceSMOImpl;
    }

    public IUnitServiceSMO getUnitServiceSMOImpl() {
        return unitServiceSMOImpl;
    }

    public void setUnitServiceSMOImpl(IUnitServiceSMO unitServiceSMOImpl) {
        this.unitServiceSMOImpl = unitServiceSMOImpl;
    }

    public IFloorServiceSMO getFloorServiceSMOImpl() {
        return floorServiceSMOImpl;
    }

    public void setFloorServiceSMOImpl(IFloorServiceSMO floorServiceSMOImpl) {
        this.floorServiceSMOImpl = floorServiceSMOImpl;
    }
}
