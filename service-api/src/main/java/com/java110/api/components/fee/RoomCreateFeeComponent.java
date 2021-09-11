package com.java110.api.components.fee;

import com.java110.core.context.IPageData;
import com.java110.api.smo.IFloorServiceSMO;
import com.java110.api.smo.IUnitServiceSMO;
import com.java110.api.smo.fee.IListRoomsWhereFeeSetSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * @ClassName RoomCreateFeeComponent
 * @Description TODO 房屋创建费用
 * @Author wuxw
 * @Date 2020/1/30 21:35
 * @Version 1.0
 * add by wuxw 2020/1/30
 **/
@Component("roomCreateFee")
public class RoomCreateFeeComponent {

    @Autowired
    private IFloorServiceSMO floorServiceSMOImpl;

    @Autowired
    private IUnitServiceSMO unitServiceSMOImpl;

    @Autowired
    private IListRoomsWhereFeeSetSMO listRoomsWhereFeeSetSMOImpl;

    public ResponseEntity<String> listRoom(IPageData pd) {
        return listRoomsWhereFeeSetSMOImpl.listRooms(pd);
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
}
