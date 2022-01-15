package com.java110.user.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.user.dao.IOwnerServiceDao;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.utils.util.DateUtil;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 业主服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("ownerServiceDaoImpl")
//@Transactional
public class OwnerServiceDaoImpl extends BaseServiceDao implements IOwnerServiceDao {

    private static Logger logger = LoggerFactory.getLogger(OwnerServiceDaoImpl.class);

    /**
     * 业主信息封装
     *
     * @param businessOwnerInfo 业主信息 封装
     * @throws DAOException DAO异常
     */
    @Override
    public void saveBusinessOwnerInfo(Map businessOwnerInfo) throws DAOException {
        businessOwnerInfo.put("month", DateUtil.getCurrentMonth());
        // 查询business_user 数据是否已经存在
        logger.debug("保存业主信息 入参 businessOwnerInfo : {}", businessOwnerInfo);
        int saveFlag = sqlSessionTemplate.insert("ownerServiceDaoImpl.saveBusinessOwnerInfo", businessOwnerInfo);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存业主数据失败：" + JSONObject.toJSONString(businessOwnerInfo));
        }
    }


    /**
     * 查询业主信息
     *
     * @param info bId 信息
     * @return 业主信息
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getBusinessOwnerInfo(Map info) throws DAOException {

        logger.debug("查询业主信息 入参 info : {}", info);

        List<Map> businessOwnerInfos = sqlSessionTemplate.selectList("ownerServiceDaoImpl.getBusinessOwnerInfo", info);

        return businessOwnerInfos;
    }


    /**
     * 保存业主信息 到 instance
     *
     * @param info bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveOwnerInfoInstance(Map info) throws DAOException {
        logger.debug("保存业主信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.insert("ownerServiceDaoImpl.saveOwnerInfoInstance", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存业主信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询业主信息（instance）
     *
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getOwnerInfo(Map info) throws DAOException {
        logger.debug("查询业主信息 入参 info : {}", info);

        List<Map> businessOwnerInfos = sqlSessionTemplate.selectList("ownerServiceDaoImpl.getOwnerInfo", info);

        return businessOwnerInfos;
    }

    /**
     * 查询业主信息（instance）
     *
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public int getOwnerInfoCount(Map info) throws DAOException {
        logger.debug("查询业主信息 入参 info : {}", info);
        List<Map> businessOwnerInfos = sqlSessionTemplate.selectList("ownerServiceDaoImpl.getOwnerInfoCount", info);
        if (businessOwnerInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessOwnerInfos.get(0).get("count").toString());
    }


    /**
     * 修改业主信息
     *
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateOwnerInfoInstance(Map info) throws DAOException {
        logger.debug("修改业主信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.update("ownerServiceDaoImpl.updateOwnerInfoInstance", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "修改业主信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }

    /**
     * 查询业主数量
     *
     * @param info 业主信息
     * @return 业主数量
     */
    @Override
    public int queryOwnersCount(Map info) {
        logger.debug("查询业主数据 入参 info : {}", info);

        List<Map> businessOwnerInfos = sqlSessionTemplate.selectList("ownerServiceDaoImpl.queryOwnersCount", info);
        if (businessOwnerInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessOwnerInfos.get(0).get("count").toString());
    }

    /**
     * 查询业主数量
     *
     * @param info 业主信息
     * @return 业主数量
     */
    @Override
    public int queryOwnersCountByCondition(Map info) {
        logger.debug("查询业主数据 入参 info : {}", info);

        List<Map> businessOwnerInfos = sqlSessionTemplate.selectList("ownerServiceDaoImpl.queryOwnersCountByCondition", info);
        if (businessOwnerInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessOwnerInfos.get(0).get("count").toString());
    }

    /**
     * 查询业主信息（instance）
     *
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getOwnerInfoByCondition(Map info) throws DAOException {
        logger.debug("查询业主信息 入参 info : {}", info);

        List<Map> businessOwnerInfos = sqlSessionTemplate.selectList("ownerServiceDaoImpl.getOwnerInfoByCondition", info);

        return businessOwnerInfos;
    }

    @Override
    public int queryNoEnterRoomOwnerCount(Map info) {
        logger.debug("查询业主数据 入参 info : {}", info);

        List<Map> businessOwnerInfos = sqlSessionTemplate.selectList("ownerServiceDaoImpl.queryNoEnterRoomOwnerCount", info);
        if (businessOwnerInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessOwnerInfos.get(0).get("count").toString());
    }

    @Override
    public List<Map> queryOwnersByRoom(Map info) throws DAOException {
        logger.debug("queryOwnersByRoom 入参 info : {}", info);

        List<Map> businessOwnerInfos = sqlSessionTemplate.selectList("ownerServiceDaoImpl.queryOwnersByRoom", info);

        return businessOwnerInfos;
    }

    @Override
    public List<Map> queryOwnersByParkingSpace(Map info) throws DAOException {
        logger.debug("queryOwnersByParkingSpace 入参 info : {}", info);

        List<Map> businessOwnerInfos = sqlSessionTemplate.selectList("ownerServiceDaoImpl.queryOwnersByParkingSpace", info);

        return businessOwnerInfos;
    }

    @Override
    public int queryOwnerLogsCountByRoom(Map info) {
        logger.debug("查询业主数据 入参 info : {}", info);

        List<Map> businessOwnerInfos = sqlSessionTemplate.selectList("ownerServiceDaoImpl.queryOwnerLogsCountByRoom", info);
        if (businessOwnerInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessOwnerInfos.get(0).get("count").toString());
    }

    @Override
    public List<Map> queryOwnerLogsByRoom(Map info) {
        logger.debug("queryOwnerLogsByRoom 入参 info : {}", info);

        List<Map> businessOwnerInfos = sqlSessionTemplate.selectList("ownerServiceDaoImpl.queryOwnerLogsByRoom", info);

        return businessOwnerInfos;
    }


}
