package com.java110.community.smo.impl;


import com.java110.community.dao.IInitializeBuildingUnitDao;
import com.java110.core.base.smo.BaseServiceSMO;
import com.java110.intf.community.IinitializeBuildingUnitSMO;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;


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
    public int deleteBuildingUnit(@RequestBody Map floorIds) {
        int deleteFlag = initializeBuildingUnitDaoImpl.deleteBuildingUnit(floorIds);
        return deleteFlag;
    }

    @Override
    public int deletefFloor(@RequestBody Map communityId) {
        int deleteFlag = initializeBuildingUnitDaoImpl.deletefFloor(communityId);
        return deleteFlag;
    }

    @Override
    public int deleteBuildingRoom(@RequestBody Map communityId) {
        int deleteFlag = initializeBuildingUnitDaoImpl.deleteBuildingRoom(communityId);
        return deleteFlag;
    }

    @Override
    public int deleteParkingArea(@RequestBody Map communityId) {
        int deleteFlag = initializeBuildingUnitDaoImpl.deleteParkingArea(communityId);
        return deleteFlag;
    }
    @Override
    public int deleteParkingSpace(@RequestBody Map communityId) {
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
