package com.java110.api.components.parkingSpace;

import com.java110.core.context.IPageData;
import com.java110.api.smo.ICarServiceSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 车辆管理
 */
@Component("hireParkingSpace")
public class HireParkingSpaceComponent {

    @Autowired
    private ICarServiceSMO carServiceSMOImpl;


    /**
     * 查询小区楼信息
     *
     * @param pd 页面封装对象 包含页面请求数据
     * @return ResponseEntity对象返回给页面
     */
    public ResponseEntity<String> sell(IPageData pd) {

        return carServiceSMOImpl.saveCar(pd);
    }

    public ResponseEntity<String> listCarType(IPageData pd) {
        return this.carServiceSMOImpl.listCarType(pd);
    }


    public ICarServiceSMO getCarServiceSMOImpl() {
        return carServiceSMOImpl;
    }

    public void setCarServiceSMOImpl(ICarServiceSMO carServiceSMOImpl) {
        this.carServiceSMOImpl = carServiceSMOImpl;
    }

}
