package com.java110.api.components.parkingSpace;


import com.java110.core.context.IPageData;
import com.java110.api.smo.IParkingSpaceServiceSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 退房
 * <p>
 * add by wuxw 2019-05-22
 */
@Component("ownerExitParkingSpace")
public class OwnerExitParkingSpaceComponent {

    @Autowired
    private IParkingSpaceServiceSMO parkingSpaceServiceSMOImpl;

    /**
     * 售卖房屋
     *
     * @param pd 包含floorId 和小区ID 页面封装对象
     * @return 单元信息
     */
    public ResponseEntity<String> exit(IPageData pd) {
        return parkingSpaceServiceSMOImpl.exitParkingSpace(pd);
    }


    public IParkingSpaceServiceSMO getParkingSpaceServiceSMOImpl() {
        return parkingSpaceServiceSMOImpl;
    }

    public void setParkingSpaceServiceSMOImpl(IParkingSpaceServiceSMO parkingSpaceServiceSMOImpl) {
        this.parkingSpaceServiceSMOImpl = parkingSpaceServiceSMOImpl;
    }
}
