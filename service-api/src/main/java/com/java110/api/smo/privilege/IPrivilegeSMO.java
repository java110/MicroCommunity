package com.java110.api.smo.privilege;

import com.java110.core.context.IPageData;
import org.springframework.web.client.RestTemplate;

public interface IPrivilegeSMO {

    void hasPrivilege(RestTemplate restTemplate, IPageData pd, String resource);
}
