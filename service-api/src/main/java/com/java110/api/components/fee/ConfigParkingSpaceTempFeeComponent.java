package com.java110.api.components.fee;

import com.java110.core.context.IPageData;
import com.java110.api.smo.IFeeServiceSMO;
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
@Component("configParkingSpaceTempFee")
public class ConfigParkingSpaceTempFeeComponent {

    @Autowired
    private IFeeServiceSMO feeServiceSMOImpl;

    /**
     * 修改费用
     *
     * @param pd 页面上数据封装
     * @return ResponseEntity 对象
     */
    public ResponseEntity<String> change(IPageData pd) {
        return feeServiceSMOImpl.saveOrUpdateParkingSpaceFeeConfig(pd);
    }


    public IFeeServiceSMO getFeeServiceSMOImpl() {
        return feeServiceSMOImpl;
    }

    public void setFeeServiceSMOImpl(IFeeServiceSMO feeServiceSMOImpl) {
        this.feeServiceSMOImpl = feeServiceSMOImpl;
    }
}
