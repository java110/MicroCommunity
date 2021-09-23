package com.java110.api.components.parkingSpace;

import com.java110.core.context.IPageData;
import com.java110.api.smo.IParkingSpaceServiceSMO;
import com.java110.api.smo.parkingArea.IListParkingAreasSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * @ClassName EditParkingSpaceComponent
 * @Description TODO 编辑小区楼信息
 * @Author wuxw
 * @Date 2019/4/28 15:10
 * @Version 1.0
 * add by wuxw 2019/4/28
 **/

@Component("editParkingSpace")
public class EditParkingSpaceComponent {
    @Autowired
    private IParkingSpaceServiceSMO parkingSpaceServiceSMOImpl;

    @Autowired
    private IListParkingAreasSMO listParkingAreasSMOImpl;

    /**
     * 修改小区楼信息
     *
     * @param pd 页面数据封装
     * @return 返回ResponseEntity对象
     */
    public ResponseEntity<String> changeParkingSpace(IPageData pd) {
        return parkingSpaceServiceSMOImpl.editParkingSpace(pd);
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
