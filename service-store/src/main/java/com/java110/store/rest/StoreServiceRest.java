package com.java110.store.rest;

import com.java110.core.base.controller.BaseController;
import com.java110.store.smo.IStoreServiceSMO;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 用户服务提供类
 * Created by wuxw on 2017/4/5.
 */
//@RestController
public class StoreServiceRest extends BaseController  {

    private final static Logger logger = LoggerFactory.getLogger(StoreServiceRest.class);

    @Autowired
    IStoreServiceSMO storeServiceSMOImpl;




    public IStoreServiceSMO getStoreServiceSMOImpl() {
        return storeServiceSMOImpl;
    }

    public void setStoreServiceSMOImpl(IStoreServiceSMO storeServiceSMOImpl) {
        this.storeServiceSMOImpl = storeServiceSMOImpl;
    }
}
