package com.java110.community.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.utils.util.DateUtil;
import com.java110.community.dao.IParkingSpaceServiceDao;
import com.java110.core.base.dao.BaseServiceDao;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 停车位服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("parkingSpaceServiceDaoImpl")
//@Transactional
public class ParkingSpaceServiceDaoImpl extends BaseServiceDao implements IParkingSpaceServiceDao {

    private static Logger logger = LoggerFactory.getLogger(ParkingSpaceServiceDaoImpl.class);

    /**
     * 停车位信息封装
     *
     * @param businessParkingSpaceInfo 停车位信息 封装
     * @throws DAOException DAO异常
     */
    @Override
    public void saveBusinessParkingSpaceInfo(Map businessParkingSpaceInfo) throws DAOException {
        businessParkingSpaceInfo.put("month", DateUtil.getCurrentMonth());
        // 查询business_user 数据是否已经存在
        logger.debug("保存停车位信息 入参 businessParkingSpaceInfo : {}", businessParkingSpaceInfo);
        int saveFlag = sqlSessionTemplate.insert("parkingSpaceServiceDaoImpl.saveBusinessParkingSpaceInfo", businessParkingSpaceInfo);
        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存停车位数据失败：" + JSONObject.toJSONString(businessParkingSpaceInfo));
        }
    }

    /**
     * 查询停车位信息
     *
     * @param info bId 信息
     * @return 停车位信息
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getBusinessParkingSpaceInfo(Map info) throws DAOException {
        logger.debug("查询停车位信息 入参 info : {}", info);
        List<Map> businessParkingSpaceInfos = sqlSessionTemplate.selectList("parkingSpaceServiceDaoImpl.getBusinessParkingSpaceInfo", info);
        return businessParkingSpaceInfos;
    }


    /**
     * 保存停车位信息 到 instance
     *
     * @param info bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveParkingSpaceInfoInstance(Map info) throws DAOException {
        logger.debug("保存停车位信息Instance 入参 info : {}", info);
        int saveFlag = sqlSessionTemplate.insert("parkingSpaceServiceDaoImpl.saveParkingSpaceInfoInstance", info);
        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存停车位信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询停车位信息（instance）
     *
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getParkingSpaceInfo(Map info) throws DAOException {
        logger.debug("查询停车位信息 入参 info : {}", info);
        List<Map> businessParkingSpaceInfos = sqlSessionTemplate.selectList("parkingSpaceServiceDaoImpl.getParkingSpaceInfo", info);
        return businessParkingSpaceInfos;
    }

    /**
     * 修改停车位信息
     *
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateParkingSpaceInfoInstance(Map info) throws DAOException {
        logger.debug("修改停车位信息Instance 入参 info : {}", info);
        int saveFlag = sqlSessionTemplate.update("parkingSpaceServiceDaoImpl.updateParkingSpaceInfoInstance", info);
        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "修改停车位信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }

    /**
     * 查询停车位数量
     *
     * @param info 停车位信息
     * @return 停车位数量
     */
    @Override
    public int queryParkingSpacesCount(Map info) {
        logger.debug("查询停车位数据 入参 info : {}", info);
        List<Map> businessParkingSpaceInfos = sqlSessionTemplate.selectList("parkingSpaceServiceDaoImpl.queryParkingSpacesCount", info);
        if (businessParkingSpaceInfos.size() < 1) {
            return 0;
        }
        return Integer.parseInt(businessParkingSpaceInfos.get(0).get("count").toString());
    }

    @Override
    public int saveParkingSpace(Map info) {
        logger.debug("保存车位信息Instance 入参 info : {}", info);
        int saveFlag = sqlSessionTemplate.insert("parkingSpaceServiceDaoImpl.saveParkingSpace", info);
        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存车位信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
        return saveFlag;
    }
}
