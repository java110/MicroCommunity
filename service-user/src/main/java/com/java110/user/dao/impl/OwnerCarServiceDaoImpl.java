package com.java110.user.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.utils.util.DateUtil;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.user.dao.IOwnerCarServiceDao;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 车辆管理服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("ownerCarServiceDaoImpl")
//@Transactional
public class OwnerCarServiceDaoImpl extends BaseServiceDao implements IOwnerCarServiceDao {

    private static Logger logger = LoggerFactory.getLogger(OwnerCarServiceDaoImpl.class);

    /**
     * 车辆管理信息封装
     *
     * @param businessOwnerCarInfo 车辆管理信息 封装
     * @throws DAOException DAO异常
     */
    @Override
    public void saveBusinessOwnerCarInfo(Map businessOwnerCarInfo) throws DAOException {
        businessOwnerCarInfo.put("month", DateUtil.getCurrentMonth());
        // 查询business_user 数据是否已经存在
        logger.debug("保存车辆管理信息 入参 businessOwnerCarInfo : {}", businessOwnerCarInfo);
        int saveFlag = sqlSessionTemplate.insert("ownerCarServiceDaoImpl.saveBusinessOwnerCarInfo", businessOwnerCarInfo);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存车辆管理数据失败：" + JSONObject.toJSONString(businessOwnerCarInfo));
        }
    }


    /**
     * 查询车辆管理信息
     *
     * @param info bId 信息
     * @return 车辆管理信息
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getBusinessOwnerCarInfo(Map info) throws DAOException {

        logger.debug("查询车辆管理信息 入参 info : {}", info);

        List<Map> businessOwnerCarInfos = sqlSessionTemplate.selectList("ownerCarServiceDaoImpl.getBusinessOwnerCarInfo", info);

        return businessOwnerCarInfos;
    }


    /**
     * 保存车辆管理信息 到 instance
     *
     * @param info bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveOwnerCarInfoInstance(Map info) throws DAOException {
        logger.debug("保存车辆管理信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.insert("ownerCarServiceDaoImpl.saveOwnerCarInfoInstance", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存车辆管理信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询车辆管理信息（instance）
     *
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getOwnerCarInfo(Map info) throws DAOException {
        logger.debug("查询车辆管理信息 入参 info : {}", info);

        List<Map> businessOwnerCarInfos = sqlSessionTemplate.selectList("ownerCarServiceDaoImpl.getOwnerCarInfo", info);

        return businessOwnerCarInfos;
    }


    /**
     * 修改车辆管理信息
     *
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateOwnerCarInfoInstance(Map info) throws DAOException {
        logger.debug("修改车辆管理信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.update("ownerCarServiceDaoImpl.updateOwnerCarInfoInstance", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "修改车辆管理信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }

    /**
     * 查询车辆管理数量
     *
     * @param info 车辆管理信息
     * @return 车辆管理数量
     */
    @Override
    public int queryOwnerCarsCount(Map info) {
        logger.debug("查询车辆管理数据 入参 info : {}", info);

        List<Map> businessOwnerCarInfos = sqlSessionTemplate.selectList("ownerCarServiceDaoImpl.queryOwnerCarsCount", info);
        if (businessOwnerCarInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessOwnerCarInfos.get(0).get("count").toString());
    }

    @Override
    public long queryOwnerParkingSpaceCount(Map info) {
        logger.debug("查询车辆管理数据 入参 info : {}", info);

        List<Map> businessOwnerCarInfos = sqlSessionTemplate.selectList("ownerCarServiceDaoImpl.queryOwnerParkingSpaceCount", info);
        if (businessOwnerCarInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessOwnerCarInfos.get(0).get("count").toString());
    }


}
