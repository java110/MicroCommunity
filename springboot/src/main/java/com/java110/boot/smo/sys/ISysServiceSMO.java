package com.java110.boot.smo.sys;

import com.java110.core.context.IPageData;
import org.springframework.http.ResponseEntity;

public interface ISysServiceSMO {

    ResponseEntity<String> getSysInfo(IPageData pd);
}
