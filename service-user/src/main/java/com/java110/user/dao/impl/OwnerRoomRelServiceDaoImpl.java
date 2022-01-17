package com.java110.user.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.user.dao.IOwnerRoomRelServiceDao;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.utils.util.DateUtil;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 业主房屋服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("ownerRoomRelServiceDaoImpl")
//@Transactional
public class OwnerRoomRelServiceDaoImpl extends BaseServiceDao implements IOwnerRoomRelServiceDao {

    private static Logger logger = LoggerFactory.getLogger(OwnerRoomRelServiceDaoImpl.class);

    /**
     * 业主房屋信息封装
     *
     * @param businessOwnerRoomRelInfo 业主房屋信息 封装
     * @throws DAOException DAO异常
     */
    @Override
    public void saveBusinessOwnerRoomRelInfo(Map businessOwnerRoomRelInfo) throws DAOException {
        businessOwnerRoomRelInfo.put("month", DateUtil.getCurrentMonth());
        // 查询business_user 数据是否已经存在
        logger.debug("保存业主房屋信息 入参 businessOwnerRoomRelInfo : {}", businessOwnerRoomRelInfo);
        int saveFlag = sqlSessionTemplate.insert("ownerRoomRelServiceDaoImpl.saveBusinessOwnerRoomRelInfo", businessOwnerRoomRelInfo);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存业主房屋数据失败：" + JSONObject.toJSONString(businessOwnerRoomRelInfo));
        }
    }


    /**
     * 查询业主房屋信息
     *
     * @param info bId 信息
     * @return 业主房屋信息
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getBusinessOwnerRoomRelInfo(Map info) throws DAOException {

        logger.debug("查询业主房屋信息 入参 info : {}", info);

        List<Map> businessOwnerRoomRelInfos = sqlSessionTemplate.selectList("ownerRoomRelServiceDaoImpl.getBusinessOwnerRoomRelInfo", info);

        return businessOwnerRoomRelInfos;
    }


    /**
     * 保存业主房屋信息 到 instance
     *
     * @param info bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveOwnerRoomRelInfoInstance(Map info) throws DAOException {
        logger.debug("保存业主房屋信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.insert("ownerRoomRelServiceDaoImpl.saveOwnerRoomRelInfoInstance", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存业主房屋信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询业主房屋信息（instance）
     *
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map<Object, Object>> getOwnerRoomRelInfo(Map<Object, Object> info) throws DAOException {
        logger.debug("查询业主房屋信息 入参 info : {}", info);

        List<Map<Object, Object>> businessOwnerRoomRelInfos = sqlSessionTemplate.selectList("ownerRoomRelServiceDaoImpl.getOwnerRoomRelInfo", info);

        return businessOwnerRoomRelInfos;
    }


    /**
     * 修改业主房屋信息
     *
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateOwnerRoomRelInfoInstance(Map info) throws DAOException {
        logger.debug("修改业主房屋信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.update("ownerRoomRelServiceDaoImpl.updateOwnerRoomRelInfoInstance", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "修改业主房屋信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }

    /**
     * 查询业主房屋数量
     *
     * @param info 业主房屋信息
     * @return 业主房屋数量
     */
    @Override
    public int queryOwnerRoomRelsCount(Map info) {
        logger.debug("查询业主房屋数据 入参 info : {}", info);

        List<Map> businessOwnerRoomRelInfos = sqlSessionTemplate.selectList("ownerRoomRelServiceDaoImpl.queryOwnerRoomRelsCount", info);
        if (businessOwnerRoomRelInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessOwnerRoomRelInfos.get(0).get("count").toString());
    }

    @Override
    public int saveOwnerRoomRels(Map info) {
        int saveFlag = sqlSessionTemplate.update("ownerRoomRelServiceDaoImpl.saveOwnerRoomRels", info);
        return saveFlag;
    }

    @Override
    public int updateOwnerRoomRels(Map info) {
        int saveFlag = sqlSessionTemplate.update("ownerRoomRelServiceDaoImpl.updateOwnerRoomRelInfoInstance", info);
        return saveFlag;
    }


}
