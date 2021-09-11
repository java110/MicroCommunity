package com.java110.api.components.fee;

import com.java110.core.context.IPageData;
import com.java110.api.smo.IFeeServiceSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * @ClassName ListArrearsComponent
 * @Description TODO
 * @Author wuxw
 * @Date 2019/6/18 19:11
 * @Version 1.0
 * add by wuxw 2019/6/18
 **/

@Component("listArrears")
public class ListArrearsComponent {

    @Autowired
    private IFeeServiceSMO feeServiceSMOImpl;

    public ResponseEntity<String> list(IPageData pd) {
        return feeServiceSMOImpl.listArrearsFee(pd);
    }


    public IFeeServiceSMO getFeeServiceSMOImpl() {
        return feeServiceSMOImpl;
    }

    public void setFeeServiceSMOImpl(IFeeServiceSMO feeServiceSMOImpl) {
        this.feeServiceSMOImpl = feeServiceSMOImpl;
    }
}
