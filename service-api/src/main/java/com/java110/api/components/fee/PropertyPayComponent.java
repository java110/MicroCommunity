package com.java110.api.components.fee;

import com.java110.core.context.IPageData;
import com.java110.api.smo.IFeeServiceSMO;
import com.java110.api.smo.fee.IListFeeConfigsSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * @ClassName ViewPropertyFeeConfigComponent
 * @Description 展示物业费信息
 * @Author wuxw
 * @Date 2019/6/1 14:33
 * @Version 1.0
 * add by wuxw 2019/6/1
 **/
@Component("propertyPay")
public class PropertyPayComponent {

    @Autowired
    private IFeeServiceSMO feeServiceSMOImpl;

    @Autowired
    private IListFeeConfigsSMO listFeeConfigsSMOImpl;

    public ResponseEntity<String> loadPropertyConfigData(IPageData pd) {
        return listFeeConfigsSMOImpl.listFeeConfigs(pd);
    }


    /**
     * 缴费
     *
     * @param pd 页面数据封装
     * @return 缴费接口
     */
    public ResponseEntity<String> payFee(IPageData pd) {
        return feeServiceSMOImpl.payFee(pd);
    }


    public IFeeServiceSMO getFeeServiceSMOImpl() {
        return feeServiceSMOImpl;
    }

    public void setFeeServiceSMOImpl(IFeeServiceSMO feeServiceSMOImpl) {
        this.feeServiceSMOImpl = feeServiceSMOImpl;
    }
}
