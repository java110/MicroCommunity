package com.java110.api.components.parkingArea;


import com.java110.core.context.IPageData;
import com.java110.api.smo.parkingArea.IListParkingAreasSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;


/**
 * 停车场组件管理类
 * <p>
 * add by wuxw
 * <p>
 * 2019-06-29
 */
@Component("parkingAreaManage")
public class ParkingAreaManageComponent {

    @Autowired
    private IListParkingAreasSMO listParkingAreasSMOImpl;

    /**
     * 查询停车场列表
     *
     * @param pd 页面数据封装
     * @return 返回 ResponseEntity 对象
     */
    public ResponseEntity<String> list(IPageData pd) {
        return listParkingAreasSMOImpl.listParkingAreas(pd);
    }

    public IListParkingAreasSMO getListParkingAreasSMOImpl() {
        return listParkingAreasSMOImpl;
    }

    public void setListParkingAreasSMOImpl(IListParkingAreasSMO listParkingAreasSMOImpl) {
        this.listParkingAreasSMOImpl = listParkingAreasSMOImpl;
    }
}
