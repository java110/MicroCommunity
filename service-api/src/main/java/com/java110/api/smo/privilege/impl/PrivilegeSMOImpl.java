package com.java110.api.smo.privilege.impl;

import com.java110.api.smo.DefaultAbstractComponentSMO;
import com.java110.api.smo.privilege.IPrivilegeSMO;
import com.java110.core.context.IPageData;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PrivilegeSMOImpl extends DefaultAbstractComponentSMO implements IPrivilegeSMO {



    @Override
    public void hasPrivilege(RestTemplate restTemplate, IPageData pd, String resource) {


        super.hasPrivilege(restTemplate, pd, resource);
    }
}
