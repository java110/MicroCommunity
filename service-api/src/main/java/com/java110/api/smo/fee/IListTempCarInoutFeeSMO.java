package com.java110.api.smo.fee;

import com.java110.core.context.IPageData;
import org.springframework.http.ResponseEntity;

public interface IListTempCarInoutFeeSMO {
    public ResponseEntity<String> list(IPageData pd);
}
