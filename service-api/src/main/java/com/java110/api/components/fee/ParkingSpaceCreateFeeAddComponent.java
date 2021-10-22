package com.java110.api.components.fee;


import com.java110.core.context.IPageData;
import com.java110.api.smo.fee.IListFeeConfigsSMO;
import com.java110.api.smo.fee.IParkingSpaceCreateFeeSMO;
import com.java110.api.smo.parkingArea.IListParkingAreasSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;


/**
 * 费用项组件管理类
 * <p>
 * add by wuxw
 * <p>
 * 2019-06-29
 */
@Component("parkingSpaceCreateFeeAdd")
public class ParkingSpaceCreateFeeAddComponent {

    @Autowired
    private IListFeeConfigsSMO listFeeConfigsSMOImpl;

    @Autowired
    private IParkingSpaceCreateFeeSMO parkingSpaceCreateFeeSMOImpl;

    @Autowired
    private IListParkingAreasSMO listParkingAreasSMOImpl;



    /**
     * 查询费用项列表
     *
     * @param pd 页面数据封装
     * @return 返回 ResponseEntity 对象
     */
    public ResponseEntity<String> list(IPageData pd) {
        return listFeeConfigsSMOImpl.listFeeConfigs(pd);
    }


    public ResponseEntity<String> listParkingArea(IPageData pd){
        return listParkingAreasSMOImpl.listParkingAreas(pd);
    }

    /**
     * 批量创建费用
     * @param pd
     * @return
     */
    public ResponseEntity<String> save(IPageData pd) {
        return parkingSpaceCreateFeeSMOImpl.createFee(pd);
    }

    public IListFeeConfigsSMO getListFeeConfigsSMOImpl() {
        return listFeeConfigsSMOImpl;
    }

    public void setListFeeConfigsSMOImpl(IListFeeConfigsSMO listFeeConfigsSMOImpl) {
        this.listFeeConfigsSMOImpl = listFeeConfigsSMOImpl;
    }

    public IParkingSpaceCreateFeeSMO getParkingSpaceCreateFeeSMOImpl() {
        return parkingSpaceCreateFeeSMOImpl;
    }

    public void setParkingSpaceCreateFeeSMOImpl(IParkingSpaceCreateFeeSMO parkingSpaceCreateFeeSMOImpl) {
        this.parkingSpaceCreateFeeSMOImpl = parkingSpaceCreateFeeSMOImpl;
    }

    public IListParkingAreasSMO getListParkingAreasSMOImpl() {
        return listParkingAreasSMOImpl;
    }

    public void setListParkingAreasSMOImpl(IListParkingAreasSMO listParkingAreasSMOImpl) {
        this.listParkingAreasSMOImpl = listParkingAreasSMOImpl;
    }

}
