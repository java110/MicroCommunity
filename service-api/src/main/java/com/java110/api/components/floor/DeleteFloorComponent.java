package com.java110.api.components.floor;

import com.java110.core.context.IPageData;
import com.java110.api.smo.IFloorServiceSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 删除小区楼信息
 */
@Component("deleteFloor")
public class DeleteFloorComponent {

    @Autowired
    private IFloorServiceSMO floorServiceSMOImpl;

    /**
     * 删除小区楼
     *
     * @param pd 页面数据封装
     * @return 对象ResponseEntity
     */
    public ResponseEntity<String> delete(IPageData pd) {
        return floorServiceSMOImpl.deleteFloor(pd);
    }


    public IFloorServiceSMO getFloorServiceSMOImpl() {
        return floorServiceSMOImpl;
    }

    public void setFloorServiceSMOImpl(IFloorServiceSMO floorServiceSMOImpl) {
        this.floorServiceSMOImpl = floorServiceSMOImpl;
    }
}
