package com.java110.community.smo.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.java110.community.dao.IInitializeBuildingUnitDao;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.core.context.BusinessServiceDataFlow;
import com.java110.core.event.service.BusinessServiceDataFlowEventPublishing;
import com.java110.core.factory.DataFlowFactory;
import com.java110.entity.center.DataFlowLinksCost;
import com.java110.entity.center.DataFlowLog;
import com.java110.intf.community.IinitializeBuildingUnitSMO;
import com.java110.utils.cache.MappingCache;
import com.java110.utils.constant.KafkaConstant;
import com.java110.utils.constant.MappingConstant;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.SMOException;
import com.java110.utils.kafka.KafkaFactory;
import com.java110.utils.util.Assert;
import com.java110.utils.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


import java.util.Map;

/**
 * 用户服务信息管理业务信息实现
 * Created by wuxw on 2017/4/5.
 */
@Service
public class InitializeBuildingUnitSMOImpl extends BaseServiceSMO implements IinitializeBuildingUnitSMO {

    private static Logger logger = LoggerFactory.getLogger(InitializeBuildingUnitSMOImpl.class);

    @Autowired
    private IInitializeBuildingUnitDao initializeBuildingUnitDaoImpl;

    @Override
    public int deleteBuildingUnit(Map floorIds) {
        int deleteFlag = initializeBuildingUnitDaoImpl.deleteBuildingUnit(floorIds);
        return deleteFlag;
    }

    @Override
    public int deletefFloor(Map communityId) {
        int deleteFlag = initializeBuildingUnitDaoImpl.deletefFloor(communityId);
        return deleteFlag;
    }

    @Override
    public int deleteBuildingRoom(Map communityId) {
        int deleteFlag = initializeBuildingUnitDaoImpl.deleteBuildingRoom(communityId);
        return deleteFlag;
    }

    @Override
    public int deleteParkingArea(Map communityId) {
        int deleteFlag = initializeBuildingUnitDaoImpl.deleteParkingArea(communityId);
        return deleteFlag;
    }
    @Override
    public int deleteParkingSpace(Map communityId) {
        int deleteFlag = initializeBuildingUnitDaoImpl.deleteParkingSpace(communityId);
        return deleteFlag;
    }

    public static Logger getLogger() {
        return logger;
    }

    public static void setLogger(Logger logger) {
        InitializeBuildingUnitSMOImpl.logger = logger;
    }

    public IInitializeBuildingUnitDao getInitializeBuildingUnitDaoImpl() {
        return initializeBuildingUnitDaoImpl;
    }

    public void setInitializeBuildingUnitDaoImpl(IInitializeBuildingUnitDao initializeBuildingUnitDaoImpl) {
        this.initializeBuildingUnitDaoImpl = initializeBuildingUnitDaoImpl;
    }
}
