package com.java110.api.components.parkingSpace;

import com.java110.core.context.IPageData;
import com.java110.api.smo.IParkingSpaceServiceSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 删除小区楼信息
 */
@Component("deleteParkingSpace")
public class DeleteParkingSpaceComponent {

    @Autowired
    private IParkingSpaceServiceSMO parkingSpaceServiceSMOImpl;

    /**
     * 删除小区楼
     *
     * @param pd 页面数据封装
     * @return 对象ResponseEntity
     */
    public ResponseEntity<String> delete(IPageData pd) {
        return parkingSpaceServiceSMOImpl.deleteParkingSpace(pd);
    }


    public IParkingSpaceServiceSMO getParkingSpaceServiceSMOImpl() {
        return parkingSpaceServiceSMOImpl;
    }

    public void setParkingSpaceServiceSMOImpl(IParkingSpaceServiceSMO parkingSpaceServiceSMOImpl) {
        this.parkingSpaceServiceSMOImpl = parkingSpaceServiceSMOImpl;
    }
}
