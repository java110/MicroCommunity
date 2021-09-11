package com.java110.api.components.car;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.context.IPageData;
import com.java110.api.smo.ICarServiceSMO;
import com.java110.api.smo.IFeeServiceSMO;
import com.java110.utils.constant.FeeTypeConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 车位出售
 */
@Component("sellParkingSpace")
public class SellParkingSpaceComponent {

    @Autowired
    private ICarServiceSMO carServiceSMOImpl;


    @Autowired
    private IFeeServiceSMO feeServiceSMOImpl;

    /**
     * 查询小区楼信息
     *
     * @param pd 页面封装对象 包含页面请求数据
     * @return ResponseEntity对象返回给页面
     */
    public ResponseEntity<String> sell(IPageData pd) {

        return carServiceSMOImpl.saveCar(pd);
    }

    /**
     * 查询出售费用配置
     *
     * @param pd 页面封装对象 包含页面请求数据
     * @return ResponseEntity对象返回给页面
     */
    public ResponseEntity<String> loadSellParkingSpaceConfigData(IPageData pd) {
        String paramIn = pd.getReqData();
        JSONObject paramObj = JSONObject.parseObject(paramIn);
        return feeServiceSMOImpl.loadPropertyConfigFee(pd, "1001".equals(paramObj.getString("typeCd"))
                ? FeeTypeConstant.FEE_TYPE_SELL_UP_PARKING_SPACE : FeeTypeConstant.FEE_TYPE_SELL_DOWN_PARKING_SPACE);
    }


    public ICarServiceSMO getCarServiceSMOImpl() {
        return carServiceSMOImpl;
    }

    public void setCarServiceSMOImpl(ICarServiceSMO carServiceSMOImpl) {
        this.carServiceSMOImpl = carServiceSMOImpl;
    }

    public IFeeServiceSMO getFeeServiceSMOImpl() {
        return feeServiceSMOImpl;
    }

    public void setFeeServiceSMOImpl(IFeeServiceSMO feeServiceSMOImpl) {
        this.feeServiceSMOImpl = feeServiceSMOImpl;
    }
}
