package com.java110.community.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.community.dao.ICommunityServiceDao;
import com.java110.community.dao.IInitializeBuildingUnitDao;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.utils.util.DateUtil;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 小区服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("initializeBuildingUnitDaoImpl")
//@Transactional
public class InitializeBuildingUnitDaoImpl extends BaseServiceDao implements IInitializeBuildingUnitDao {

    private static Logger logger = LoggerFactory.getLogger(InitializeBuildingUnitDaoImpl.class);

    /**
     * 修改小区成员加入信息
     *
     * @param info 修改信息
     * @throws DAOException
     */
    public int deleteBuildingUnit(Map info) throws DAOException {
        logger.debug("初始化单元Instance 入参 info : {}", info);

        int deleteFlag = sqlSessionTemplate.delete("initializeBuildingUnitDaoImpl.deleteBuildingUnit", info);

/*        if (deleteFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "building_unit初始化失败：" + JSONObject.toJSONString(info));
        }*/
        return deleteFlag;
    }
    /**
     * 修改小区成员加入信息
     *
     * @param info 修改信息
     * @throws DAOException
     */
    public int deletefFloor(Map info) throws DAOException {
        logger.debug("初始化楼栋Instance 入参 info : {}", info);

        int deleteFlag = sqlSessionTemplate.delete("initializeBuildingUnitDaoImpl.deletefFloor", info);

/*        if (deleteFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "f_floor初始化失败：" + JSONObject.toJSONString(info));
        }*/
        return deleteFlag;
    }
    /**
     * 修改小区成员加入信息
     *
     * @param info 修改信息
     * @throws DAOException
     */
    public int deleteBuildingRoom(Map info) throws DAOException {
        logger.debug("初始化房屋Instance 入参 info : {}", info);

        int deleteFlag = sqlSessionTemplate.delete("initializeBuildingUnitDaoImpl.deleteBuildingRoom", info);

 /*       if (deleteFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "f_floor初始化失败：" + JSONObject.toJSONString(info));
        }*/
        return deleteFlag;
    }

    public int deleteParkingArea(Map info) throws DAOException {
        logger.debug("停车场Instance 入参 info : {}", info);

        int deleteFlag = sqlSessionTemplate.delete("initializeBuildingUnitDaoImpl.deleteParkingArea", info);

  /*      if (deleteFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "f_floor初始化失败：" + JSONObject.toJSONString(info));
        }*/
        return deleteFlag;
    }
    /**
     * 修改小区成员加入信息
     *
     * @param info 修改信息
     * @throws DAOException
     */
    public int deleteParkingSpace(Map info) throws DAOException {
        logger.debug("停车位Instance 入参 info : {}", info);

        int deleteFlag = sqlSessionTemplate.delete("initializeBuildingUnitDaoImpl.deleteParkingSpace", info);

  /*      if (deleteFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "f_floor初始化失败：" + JSONObject.toJSONString(info));
        }*/
        return deleteFlag;
    }


}
