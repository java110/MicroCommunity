package com.java110.app.smo.payment.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.app.smo.AppAbstractComponentSMO;
import com.java110.app.smo.payment.IToPaySMO;
import com.java110.core.context.IPageData;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

public class ToPaySMOImpl extends AppAbstractComponentSMO implements IToPaySMO {
    @Override
    public ResponseEntity<String> toPay(IPageData pd) {
        return super.businessProcess(pd);
    }

    @Override
    protected void validate(IPageData pd, JSONObject paramIn) {

    }

    @Override
    protected ResponseEntity<String> doBusinessProcess(IPageData pd, JSONObject paramIn) throws IOException {
        return null;
    }
}
