package com.java110.boot.smo.privilege.impl;

import com.java110.boot.smo.DefaultAbstractComponentSMO;
import com.java110.boot.smo.privilege.IPrivilegeSMO;
import com.java110.core.context.IPageData;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class BootPrivilegeSMOImpl extends DefaultAbstractComponentSMO implements IPrivilegeSMO {



    @Override
    public void hasPrivilege(RestTemplate restTemplate, IPageData pd, String resource) {


        super.hasPrivilege(restTemplate, pd, resource);
    }
}
