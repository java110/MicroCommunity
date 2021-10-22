package com.java110.api.components.parkingSpace;

import com.java110.core.context.IPageData;
import com.java110.api.smo.IParkingSpaceServiceSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 选择业主
 */
@Component("searchParkingSpace")
public class SearchParkingSpaceComponent {
    @Autowired
    private IParkingSpaceServiceSMO parkingSpaceServiceSMOImpl;

    /**
     * 查询小区楼信息
     *
     * @param pd 页面封装对象 包含页面请求数据
     * @return ResponseEntity对象返回给页面
     */
    public ResponseEntity<String> listParkingSpace(IPageData pd) {

        return parkingSpaceServiceSMOImpl.listParkingSpace(pd);
    }


    public IParkingSpaceServiceSMO getParkingSpaceServiceSMOImpl() {
        return parkingSpaceServiceSMOImpl;
    }

    public void setParkingSpaceServiceSMOImpl(IParkingSpaceServiceSMO parkingSpaceServiceSMOImpl) {
        this.parkingSpaceServiceSMOImpl = parkingSpaceServiceSMOImpl;
    }
}
