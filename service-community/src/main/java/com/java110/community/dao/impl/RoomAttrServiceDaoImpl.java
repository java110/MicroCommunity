package com.java110.community.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.utils.util.DateUtil;
import com.java110.community.dao.IRoomAttrServiceDao;
import com.java110.core.base.dao.BaseServiceDao;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 小区房屋属性服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("roomAttrServiceDaoImpl")
//@Transactional
public class RoomAttrServiceDaoImpl extends BaseServiceDao implements IRoomAttrServiceDao {

    private static Logger logger = LoggerFactory.getLogger(RoomAttrServiceDaoImpl.class);

    /**
     * 小区房屋属性信息封装
     *
     * @param businessRoomAttrInfo 小区房屋属性信息 封装
     * @throws DAOException
     */
    @Override
    public void saveBusinessRoomAttrInfo(Map businessRoomAttrInfo) throws DAOException {
        businessRoomAttrInfo.put("month", DateUtil.getCurrentMonth());
        // 查询business_user 数据是否已经存在
        logger.debug("保存小区房屋属性信息 入参 businessRoomAttrInfo : {}", businessRoomAttrInfo);
        int saveFlag = sqlSessionTemplate.insert("roomAttrServiceDaoImpl.saveBusinessRoomAttrInfo", businessRoomAttrInfo);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存小区房屋属性数据失败：" + JSONObject.toJSONString(businessRoomAttrInfo));
        }
    }


    /**
     * 查询小区房屋属性信息
     *
     * @param info bId 信息
     * @return 小区房屋属性信息
     * @throws DAOException
     */
    @Override
    public List<Map> getBusinessRoomAttrInfo(Map info) throws DAOException {

        logger.debug("查询小区房屋属性信息 入参 info : {}", info);

        List<Map> businessRoomAttrInfos = sqlSessionTemplate.selectList("roomAttrServiceDaoImpl.getBusinessRoomAttrInfo", info);

        return businessRoomAttrInfos;
    }


    /**
     * 保存小区房屋属性信息 到 instance
     *
     * @param info bId 信息
     * @throws DAOException
     */
    @Override
    public void saveRoomAttrInfoInstance(Map info) throws DAOException {
        logger.debug("保存小区房屋属性信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.insert("roomAttrServiceDaoImpl.saveRoomAttrInfoInstance", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存小区房屋属性信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询小区房屋属性信息（instance）
     *
     * @param info bId 信息
     * @return
     * @throws DAOException
     */
    @Override
    public List<Map> getRoomAttrInfo(Map info) throws DAOException {
        logger.debug("查询小区房屋属性信息 入参 info : {}", info);

        List<Map> businessRoomAttrInfos = sqlSessionTemplate.selectList("roomAttrServiceDaoImpl.getRoomAttrInfo", info);

        return businessRoomAttrInfos;
    }


    /**
     * 修改小区房屋属性信息
     *
     * @param info 修改信息
     * @throws DAOException
     */
    @Override
    public int updateRoomAttrInfoInstance(Map info) throws DAOException {
        logger.debug("修改小区房屋属性信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.update("roomAttrServiceDaoImpl.updateRoomAttrInfoInstance", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "修改小区房屋属性信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
        return saveFlag;
    }

    /**
     * 查询小区房屋属性数量
     *
     * @param info 小区房屋属性信息
     * @return 小区房屋属性数量
     */
    @Override
    public int queryRoomAttrsCount(Map info) {
        logger.debug("查询小区房屋属性数据 入参 info : {}", info);

        List<Map> businessRoomAttrInfos = sqlSessionTemplate.selectList("roomAttrServiceDaoImpl.queryRoomAttrsCount", info);
        if (businessRoomAttrInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessRoomAttrInfos.get(0).get("count").toString());
    }


    /**
     * 保存小区房屋属性信息 到 instance
     *
     * @param info bId 信息
     * @throws DAOException
     */
    @Override
    public int saveRoomAttr(Map info) throws DAOException {
        logger.debug("保存小区房屋属性saveRoomAttr入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.insert("roomAttrServiceDaoImpl.saveRoomAttr", info);

        return saveFlag;
    }

}
