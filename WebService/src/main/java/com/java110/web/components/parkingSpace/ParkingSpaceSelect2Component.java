package com.java110.web.components.parkingSpace;

import com.java110.core.context.IPageData;
import com.java110.web.smo.IParkingSpaceServiceSMO;
import com.java110.web.smo.parkingArea.IListParkingAreasSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 添加停车位组件
 */
@Component("parkingSpaceSelect2")
public class ParkingSpaceSelect2Component {

    @Autowired
    private IListParkingAreasSMO listParkingAreasSMOImpl;


    public ResponseEntity<String> list(IPageData pd){
        return listParkingAreasSMOImpl.listParkingAreas(pd);
    }


    public IListParkingAreasSMO getListParkingAreasSMOImpl() {
        return listParkingAreasSMOImpl;
    }

    public void setListParkingAreasSMOImpl(IListParkingAreasSMO listParkingAreasSMOImpl) {
        this.listParkingAreasSMOImpl = listParkingAreasSMOImpl;
    }
}
