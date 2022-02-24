package com.java110.community.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.community.dao.IParkingSpaceAttrServiceDao;
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
 * 车位属性服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("parkingSpaceAttrServiceDaoImpl")
//@Transactional
public class ParkingSpaceAttrServiceDaoImpl extends BaseServiceDao implements IParkingSpaceAttrServiceDao {

    private static Logger logger = LoggerFactory.getLogger(ParkingSpaceAttrServiceDaoImpl.class);

    /**
     * 车位属性信息封装
     *
     * @param businessParkingSpaceAttrInfo 车位属性信息 封装
     * @throws DAOException DAO异常
     */
    @Override
    public void saveBusinessParkingSpaceAttrInfo(Map businessParkingSpaceAttrInfo) throws DAOException {
        businessParkingSpaceAttrInfo.put("month", DateUtil.getCurrentMonth());
        // 查询business_user 数据是否已经存在
        logger.debug("保存车位属性信息 入参 businessParkingSpaceAttrInfo : {}", businessParkingSpaceAttrInfo);
        int saveFlag = sqlSessionTemplate.insert("parkingSpaceAttrServiceDaoImpl.saveBusinessParkingSpaceAttrInfo", businessParkingSpaceAttrInfo);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存车位属性数据失败：" + JSONObject.toJSONString(businessParkingSpaceAttrInfo));
        }
    }


    /**
     * 查询车位属性信息
     *
     * @param info bId 信息
     * @return 车位属性信息
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getBusinessParkingSpaceAttrInfo(Map info) throws DAOException {

        logger.debug("查询车位属性信息 入参 info : {}", info);

        List<Map> businessParkingSpaceAttrInfos = sqlSessionTemplate.selectList("parkingSpaceAttrServiceDaoImpl.getBusinessParkingSpaceAttrInfo", info);

        return businessParkingSpaceAttrInfos;
    }


    /**
     * 保存车位属性信息 到 instance
     *
     * @param info bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveParkingSpaceAttrInfoInstance(Map info) throws DAOException {
        logger.debug("保存车位属性信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.insert("parkingSpaceAttrServiceDaoImpl.saveParkingSpaceAttrInfoInstance", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存车位属性信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询车位属性信息（instance）
     *
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getParkingSpaceAttrInfo(Map info) throws DAOException {
        logger.debug("查询车位属性信息 入参 info : {}", info);

        List<Map> businessParkingSpaceAttrInfos = sqlSessionTemplate.selectList("parkingSpaceAttrServiceDaoImpl.getParkingSpaceAttrInfo", info);

        return businessParkingSpaceAttrInfos;
    }


    /**
     * 修改车位属性信息
     *
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateParkingSpaceAttrInfoInstance(Map info) throws DAOException {
        logger.debug("修改车位属性信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.update("parkingSpaceAttrServiceDaoImpl.updateParkingSpaceAttrInfoInstance", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "修改车位属性信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }

    /**
     * 查询车位属性数量
     *
     * @param info 车位属性信息
     * @return 车位属性数量
     */
    @Override
    public int queryParkingSpaceAttrsCount(Map info) {
        logger.debug("查询车位属性数据 入参 info : {}", info);

        List<Map> businessParkingSpaceAttrInfos = sqlSessionTemplate.selectList("parkingSpaceAttrServiceDaoImpl.queryParkingSpaceAttrsCount", info);
        if (businessParkingSpaceAttrInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessParkingSpaceAttrInfos.get(0).get("count").toString());
    }

    @Override
    public int saveParkingSpaceAttr(Map info) {
        logger.debug("修改车位属性信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.update("parkingSpaceAttrServiceDaoImpl.saveParkingSpaceAttr", info);

        return saveFlag;
    }


}
