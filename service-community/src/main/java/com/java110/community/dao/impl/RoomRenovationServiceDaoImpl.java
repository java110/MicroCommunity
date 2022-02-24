package com.java110.community.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.community.dao.IRoomRenovationServiceDao;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 装修申请服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("roomRenovationServiceDaoImpl")
//@Transactional
public class RoomRenovationServiceDaoImpl extends BaseServiceDao implements IRoomRenovationServiceDao {

    private static Logger logger = LoggerFactory.getLogger(RoomRenovationServiceDaoImpl.class);

    /**
     * 保存装修申请信息 到 instance
     *
     * @param info bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveRoomRenovationInfo(Map info) throws DAOException {
        logger.debug("保存装修申请信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.insert("roomRenovationServiceDaoImpl.saveRoomRenovationInfo", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存装修申请信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询装修申请信息（instance）
     *
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getRoomRenovationInfo(Map info) throws DAOException {
        logger.debug("查询装修申请信息 入参 info : {}", info);

        List<Map> businessRoomRenovationInfos = sqlSessionTemplate.selectList("roomRenovationServiceDaoImpl.getRoomRenovationInfo", info);

        return businessRoomRenovationInfos;
    }


    /**
     * 修改装修申请信息
     *
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateRoomRenovationInfo(Map info) throws DAOException {
        logger.debug("修改装修申请信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.update("roomRenovationServiceDaoImpl.updateRoomRenovationInfo", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "修改装修申请信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }

    @Override
    public void updateRoom(Map info) throws DAOException {
        logger.debug("修改房屋信息Instance 入参 info : {}", info);
        int saveFlag = sqlSessionTemplate.update("roomRenovationServiceDaoImpl.updateRoom", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "修改房屋信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }

    /**
     * 查询装修申请数量
     *
     * @param info 装修申请信息
     * @return 装修申请数量
     */
    @Override
    public int queryRoomRenovationsCount(Map info) {
        logger.debug("查询装修申请数据 入参 info : {}", info);

        List<Map> businessRoomRenovationInfos = sqlSessionTemplate.selectList("roomRenovationServiceDaoImpl.queryRoomRenovationsCount", info);
        if (businessRoomRenovationInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessRoomRenovationInfos.get(0).get("count").toString());
    }


}
