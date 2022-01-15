package com.java110.community.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.community.dao.IParkingAreaServiceDao;
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
 * 停车场服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("parkingAreaServiceDaoImpl")
//@Transactional
public class ParkingAreaServiceDaoImpl extends BaseServiceDao implements IParkingAreaServiceDao {

    private static Logger logger = LoggerFactory.getLogger(ParkingAreaServiceDaoImpl.class);

    /**
     * 停车场信息封装
     *
     * @param businessParkingAreaInfo 停车场信息 封装
     * @throws DAOException DAO异常
     */
    @Override
    public void saveBusinessParkingAreaInfo(Map businessParkingAreaInfo) throws DAOException {
        businessParkingAreaInfo.put("month", DateUtil.getCurrentMonth());
        // 查询business_user 数据是否已经存在
        logger.debug("保存停车场信息 入参 businessParkingAreaInfo : {}", businessParkingAreaInfo);
        int saveFlag = sqlSessionTemplate.insert("parkingAreaServiceDaoImpl.saveBusinessParkingAreaInfo", businessParkingAreaInfo);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存停车场数据失败：" + JSONObject.toJSONString(businessParkingAreaInfo));
        }
    }


    /**
     * 查询停车场信息
     *
     * @param info bId 信息
     * @return 停车场信息
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getBusinessParkingAreaInfo(Map info) throws DAOException {

        logger.debug("查询停车场信息 入参 info : {}", info);

        List<Map> businessParkingAreaInfos = sqlSessionTemplate.selectList("parkingAreaServiceDaoImpl.getBusinessParkingAreaInfo", info);

        return businessParkingAreaInfos;
    }


    /**
     * 保存停车场信息 到 instance
     *
     * @param info bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveParkingAreaInfoInstance(Map info) throws DAOException {
        logger.debug("保存停车场信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.insert("parkingAreaServiceDaoImpl.saveParkingAreaInfoInstance", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存停车场信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询停车场信息（instance）
     *
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getParkingAreaInfo(Map info) throws DAOException {
        logger.debug("查询停车场信息 入参 info : {}", info);

        List<Map> businessParkingAreaInfos = sqlSessionTemplate.selectList("parkingAreaServiceDaoImpl.getParkingAreaInfo", info);

        return businessParkingAreaInfos;
    }


    /**
     * 修改停车场信息
     *
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateParkingAreaInfoInstance(Map info) throws DAOException {
        logger.debug("修改停车场信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.update("parkingAreaServiceDaoImpl.updateParkingAreaInfoInstance", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "修改停车场信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }

    /**
     * 查询停车场数量
     *
     * @param info 停车场信息
     * @return 停车场数量
     */
    @Override
    public int queryParkingAreasCount(Map info) {
        logger.debug("查询停车场数据 入参 info : {}", info);

        List<Map> businessParkingAreaInfos = sqlSessionTemplate.selectList("parkingAreaServiceDaoImpl.queryParkingAreasCount", info);
        if (businessParkingAreaInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessParkingAreaInfos.get(0).get("count").toString());
    }


}
