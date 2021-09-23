
package com.java110.api.components.parkingSpace;


import com.java110.core.context.IPageData;
import com.java110.api.smo.IParkingSpaceServiceSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 停车位 组件处理类
 */
@Component("listParkingSpace")
public class ListParkingSpaceComponent {


    @Autowired
    private IParkingSpaceServiceSMO ownerServiceSMOImpl;

    /**
     * 显示 停车位
     *
     * @param pd 页面数据封装对象
     * @return 停车位信息
     */
    public ResponseEntity<String> list(IPageData pd) {

        return ownerServiceSMOImpl.listParkingSpace(pd);
    }


    public IParkingSpaceServiceSMO getParkingSpaceServiceSMOImpl() {
        return ownerServiceSMOImpl;
    }

    public void setParkingSpaceServiceSMOImpl(IParkingSpaceServiceSMO ownerServiceSMOImpl) {
        this.ownerServiceSMOImpl = ownerServiceSMOImpl;
    }
}
