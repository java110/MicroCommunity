package com.java110.api.smo.impl;

import com.java110.api.rest.TestApi;
import com.java110.api.smo.ITestServiceSMO;
import com.java110.core.annotation.Java110Transactional;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class TestServiceSMOImpl implements ITestServiceSMO {
    private static Logger logger = LoggerFactory.getLogger(TestServiceSMOImpl.class);

    @Override
    @Java110Transactional
    public String getVersion(String name) {
        logger.debug("调用方法调用");
        return "123";
    }
}
