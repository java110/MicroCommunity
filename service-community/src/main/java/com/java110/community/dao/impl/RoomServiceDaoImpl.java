package com.java110.community.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.utils.util.DateUtil;
import com.java110.community.dao.IRoomServiceDao;
import com.java110.core.base.dao.BaseServiceDao;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 小区房屋服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("roomServiceDaoImpl")
//@Transactional
public class RoomServiceDaoImpl extends BaseServiceDao implements IRoomServiceDao {

    private static Logger logger = LoggerFactory.getLogger(RoomServiceDaoImpl.class);

    /**
     * 小区房屋信息封装
     *
     * @param businessRoomInfo 小区房屋信息 封装
     * @throws DAOException
     */
    @Override
    public void saveBusinessRoomInfo(Map businessRoomInfo) throws DAOException {
        businessRoomInfo.put("month", DateUtil.getCurrentMonth());
        // 查询business_user 数据是否已经存在
        logger.debug("保存小区房屋信息 入参 businessRoomInfo : {}", businessRoomInfo);
        int saveFlag = sqlSessionTemplate.insert("roomServiceDaoImpl.saveBusinessRoomInfo", businessRoomInfo);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存小区房屋数据失败：" + JSONObject.toJSONString(businessRoomInfo));
        }
    }


    /**
     * 查询小区房屋信息
     *
     * @param info bId 信息
     * @return 小区房屋信息
     * @throws DAOException
     */
    @Override
    public List<Map> getBusinessRoomInfo(Map info) throws DAOException {

        logger.debug("查询小区房屋信息 入参 info : {}", info);

        List<Map> businessRoomInfos = sqlSessionTemplate.selectList("roomServiceDaoImpl.getBusinessRoomInfo", info);

        return businessRoomInfos;
    }


    /**
     * 保存小区房屋信息 到 instance
     *
     * @param info bId 信息
     * @throws DAOException
     */
    @Override
    public void saveRoomInfoInstance(Map info) throws DAOException {
        logger.debug("保存小区房屋信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.insert("roomServiceDaoImpl.saveRoomInfoInstance", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存小区房屋信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询小区房屋信息（instance）
     *
     * @param info bId 信息
     * @return
     * @throws DAOException
     */
    @Override
    public List<Map> getRoomInfo(Map info) throws DAOException {
        logger.debug("查询小区房屋信息 入参 info : {}", info);

        List<Map> businessRoomInfos = sqlSessionTemplate.selectList("roomServiceDaoImpl.getRoomInfo", info);

        return businessRoomInfos;
    }


    /**
     * 修改小区房屋信息
     *
     * @param info 修改信息
     * @throws DAOException
     */
    @Override
    public void updateRoomInfoInstance(Map info) throws DAOException {
        logger.debug("修改小区房屋信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.update("roomServiceDaoImpl.updateRoomInfoInstance", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "修改小区房屋信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }

    /**
     * 查询小区房屋数量
     *
     * @param info 小区房屋信息
     * @return 小区房屋数量
     */
    @Override
    public int queryRoomsCount(Map info) {
        logger.debug("查询小区房屋数据 入参 info : {}", info);

        List<Map> businessRoomInfos = sqlSessionTemplate.selectList("roomServiceDaoImpl.queryRoomsCount", info);
        if (businessRoomInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessRoomInfos.get(0).get("count").toString());
    }

    @Override
    public int queryRoomsByCommunityIdCount(Map info) {
        logger.debug("查询小区房屋数据 入参 info : {}", info);

        List<Map> businessRoomInfos = sqlSessionTemplate.selectList("roomServiceDaoImpl.queryRoomsByCommunityIdCount", info);
        if (businessRoomInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessRoomInfos.get(0).get("count").toString());
    }

    @Override
    public int queryRoomsWithOutSellByCommunityIdCount(Map info) {
        logger.debug("查询小区房屋数据 入参 info : {}", info);

        List<Map> businessRoomInfos = sqlSessionTemplate.selectList("roomServiceDaoImpl.queryRoomsWithOutSellByCommunityIdCount", info);
        if (businessRoomInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessRoomInfos.get(0).get("count").toString());
    }

    @Override
    public int queryRoomsWithSellByCommunityIdCount(Map info) {
        logger.debug("查询小区房屋数据 入参 info : {}", info);

        List<Map> businessRoomInfos = sqlSessionTemplate.selectList("roomServiceDaoImpl.queryRoomsWithSellByCommunityIdCount", info);
        if (businessRoomInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessRoomInfos.get(0).get("count").toString());
    }

    @Override
    public List<Map> getRoomInfoByCommunityId(Map info) {
        logger.debug("查询小区房屋信息 入参 info : {}", info);

        List<Map> businessRoomInfos = sqlSessionTemplate.selectList("roomServiceDaoImpl.getRoomInfoByCommunityId", info);

        return businessRoomInfos;
    }

    @Override
    public List<Map> getRoomInfoByOwner(Map info) {
        logger.debug("查询小区房屋信息 入参 info : {}", info);

        List<Map> businessRoomInfos = sqlSessionTemplate.selectList("roomServiceDaoImpl.getRoomInfoByOwner", info);

        return businessRoomInfos;
    }

    @Override
    public List<Map> getRoomInfoWithOutSellByCommunityId(Map info) {
        logger.debug("查询小区房屋信息 入参 info : {}", info);

        List<Map> businessRoomInfos = sqlSessionTemplate.selectList("roomServiceDaoImpl.getRoomInfoWithOutSellByCommunityId", info);

        return businessRoomInfos;
    }


    @Override
    public List<Map> getRoomInfoWithSellByCommunityId(Map info) {
        logger.debug("查询小区房屋信息 入参 info : {}", info);

        List<Map> businessRoomInfos = sqlSessionTemplate.selectList("roomServiceDaoImpl.getRoomInfoWithSellByCommunityId", info);

        return businessRoomInfos;
    }

    public List<Map> getRoomInfos(Map info){
        logger.debug("查询小区房屋信息 getRoomInfos入参 info : {}", info);
        List<Map> businessRoomInfos = sqlSessionTemplate.selectList("roomServiceDaoImpl.getRoomInfos", info);
        return businessRoomInfos;
    }



}
