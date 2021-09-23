package com.java110.api.components.room;

import com.java110.core.context.IPageData;
import com.java110.api.smo.IRoomServiceSMO;
import com.java110.api.smo.IUnitServiceSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * @ClassName AddRoomComponent
 * @Description TODO 编辑房屋
 * @Author wuxw
 * @Date 2019/5/6 20:23
 * @Version 1.0
 * add by wuxw 2019/5/6
 **/
@Component("editRoom")
public class EditRoomComponent {

    @Autowired
    private IUnitServiceSMO unitServiceSMOImpl;

    @Autowired
    private IRoomServiceSMO roomServiceSMOImpl;

    /**
     * 根据 floorId 查询单元信息
     *
     * @param pd 包含floorId 和小区ID 页面封装对象
     * @return 单元信息
     */
    public ResponseEntity<String> loadUnits(IPageData pd) {
        return unitServiceSMOImpl.listUnits(pd);
    }

    /**
     * 保存房屋信息
     *
     * @param pd 房屋信息
     * @return 单元信息
     */
    public ResponseEntity<String> update(IPageData pd) {
        return roomServiceSMOImpl.updateRoom(pd);
    }


    public IUnitServiceSMO getUnitServiceSMOImpl() {
        return unitServiceSMOImpl;
    }

    public void setUnitServiceSMOImpl(IUnitServiceSMO unitServiceSMOImpl) {
        this.unitServiceSMOImpl = unitServiceSMOImpl;
    }

    public IRoomServiceSMO getRoomServiceSMOImpl() {
        return roomServiceSMOImpl;
    }

    public void setRoomServiceSMOImpl(IRoomServiceSMO roomServiceSMOImpl) {
        this.roomServiceSMOImpl = roomServiceSMOImpl;
    }
}
