package com.java110.api.components.parkingSpace;

import com.java110.core.context.IPageData;
import com.java110.api.smo.IParkingSpaceServiceSMO;
import com.java110.api.smo.parkingArea.IListParkingAreasSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 添加停车位组件
 */
@Component("addParkingSpace")
public class AddParkingSpaceComponent {

    @Autowired
    private IParkingSpaceServiceSMO parkingSpaceServiceSMOImpl;

    @Autowired
    private IListParkingAreasSMO listParkingAreasSMOImpl;

    /**
     * 查询小区楼信息
     *
     * @param pd 页面封装对象 包含页面请求数据
     * @return ResponseEntity对象返回给页面
     */
    public ResponseEntity<String> saveParkingSpace(IPageData pd) {

        return parkingSpaceServiceSMOImpl.saveParkingSpace(pd);
    }

    public ResponseEntity<String> listParkingArea(IPageData pd) {
        return listParkingAreasSMOImpl.listParkingAreas(pd);
    }


    public IParkingSpaceServiceSMO getParkingSpaceServiceSMOImpl() {
        return parkingSpaceServiceSMOImpl;
    }

    public void setParkingSpaceServiceSMOImpl(IParkingSpaceServiceSMO parkingSpaceServiceSMOImpl) {
        this.parkingSpaceServiceSMOImpl = parkingSpaceServiceSMOImpl;
    }

    public IListParkingAreasSMO getListParkingAreasSMOImpl() {
        return listParkingAreasSMOImpl;
    }

    public void setListParkingAreasSMOImpl(IListParkingAreasSMO listParkingAreasSMOImpl) {
        this.listParkingAreasSMOImpl = listParkingAreasSMOImpl;
    }
}
