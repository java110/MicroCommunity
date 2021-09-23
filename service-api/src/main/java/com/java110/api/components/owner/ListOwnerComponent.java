package com.java110.api.components.owner;

import com.java110.core.context.IPageData;
import com.java110.api.smo.IOwnerServiceSMO;
import com.java110.api.smo.IParkingSpaceServiceSMO;
import com.java110.api.smo.IRoomServiceSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 查询楼列表组件
 */
@Component("listOwner")
public class ListOwnerComponent {

    @Autowired
    private IOwnerServiceSMO ownerServiceSMOImpl;

    @Autowired
    private IRoomServiceSMO roomServiceSMOImpl;

    @Autowired
    private IParkingSpaceServiceSMO parkingSpaceServiceSMOImpl;

    /**
     * 查询小区楼信息
     *
     * @param pd 页面封装对象 包含页面请求数据
     * @return ResponseEntity对象返回给页面
     */
    public ResponseEntity<String> list(IPageData pd) {

        return ownerServiceSMOImpl.listOwner(pd);
    }

    public ResponseEntity<String> getRooms(IPageData pd) {
        return roomServiceSMOImpl.listRoomByOwner(pd);
    }

    public ResponseEntity<String> getParkingSpace(IPageData pd) {
        return parkingSpaceServiceSMOImpl.listParkingSpaceByOwner(pd);
    }


    public IOwnerServiceSMO getOwnerServiceSMOImpl() {
        return ownerServiceSMOImpl;
    }

    public void setOwnerServiceSMOImpl(IOwnerServiceSMO ownerServiceSMOImpl) {
        this.ownerServiceSMOImpl = ownerServiceSMOImpl;
    }

    public IRoomServiceSMO getRoomServiceSMOImpl() {
        return roomServiceSMOImpl;
    }

    public void setRoomServiceSMOImpl(IRoomServiceSMO roomServiceSMOImpl) {
        this.roomServiceSMOImpl = roomServiceSMOImpl;
    }

    public IParkingSpaceServiceSMO getParkingSpaceServiceSMOImpl() {
        return parkingSpaceServiceSMOImpl;
    }

    public void setParkingSpaceServiceSMOImpl(IParkingSpaceServiceSMO parkingSpaceServiceSMOImpl) {
        this.parkingSpaceServiceSMOImpl = parkingSpaceServiceSMOImpl;
    }
}
