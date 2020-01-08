package com.java110.app.smo.complaint.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.app.smo.AppAbstractComponentSMO;
import com.java110.app.smo.complaint.ISaveComplaintSMO;
import com.java110.core.context.IPageData;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

public class SaveComplaintSMOImpl extends AppAbstractComponentSMO implements ISaveComplaintSMO {
    @Override
    public ResponseEntity<String> save(IPageData pd) {
        return null;
    }

    @Override
    protected void validate(IPageData pd, JSONObject paramIn) {

    }

    @Override
    protected ResponseEntity<String> doBusinessProcess(IPageData pd, JSONObject paramIn) throws IOException {
        return null;
    }
}
