package com.java110.community.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.community.dao.IRoomRenovationRecordServiceDao;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 装修记录
 *
 * @author fqz
 * @date 2021-02-23 17:19
 */
@Service("roomRenovationRecordServiceDaoImpl")
public class RoomRenovationRecordServiceDaoImpl extends BaseServiceDao implements IRoomRenovationRecordServiceDao {

    private static Logger logger = LoggerFactory.getLogger(RoomRenovationRecordServiceDaoImpl.class);

    /**
     * 存储装修记录信息
     *
     * @param info
     * @throws DAOException
     */
    @Override
    public void saveRoomRenovationRecordInfo(Map info) throws DAOException {
        logger.debug("保存装修记录信息Instance 入参 info : {}", info);
        int saveFlag = sqlSessionTemplate.insert("roomRenovationRecordServiceDaoImpl.saveRoomRenovationRecordInfo", info);
        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存装修记录信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }

    /**
     * 查询装修记录信息(与文件表关联)
     *
     * @param info bId 信息
     * @return
     * @throws DAOException
     */
    @Override
    public List<Map> getRoomRenovationRecordsInfo(Map info) throws DAOException {
        logger.debug("查询装修记录信息 入参 info : {}", info);
        List<Map> businessRoomRenovationInfos = sqlSessionTemplate.selectList("roomRenovationRecordServiceDaoImpl.getRoomRenovationRecordsInfo", info);
        return businessRoomRenovationInfos;
    }

    /**
     * 查询装修记录
     *
     * @param info
     * @return
     */
    @Override
    public List<Map> findRoomRenovationRecordsInfo(Map info) {
        logger.debug("查询装修记录信息 入参 info : {}", info);
        List<Map> businessRoomRenovationInfos = sqlSessionTemplate.selectList("roomRenovationRecordServiceDaoImpl.getRoomRenovationRecords", info);
        return businessRoomRenovationInfos;
    }

    @Override
    public int queryRoomRenovationRecordsCount(Map info) {
        logger.debug("查询装修记录数据 入参 info : {}", info);
        List<Map> businessRoomRenovationInfos = sqlSessionTemplate.selectList("roomRenovationRecordServiceDaoImpl.queryRoomRenovationRecordsCount", info);
        if (businessRoomRenovationInfos.size() < 1) {
            return 0;
        }
        return Integer.parseInt(businessRoomRenovationInfos.get(0).get("count").toString());
    }

    @Override
    public int getRoomRenovationRecordsCount(Map info) {
        logger.debug("查询装修记录数据 入参 info : {}", info);
        List<Map> businessRoomRenovationInfos = sqlSessionTemplate.selectList("roomRenovationRecordServiceDaoImpl.getRoomRenovationRecordsCount", info);
        if (businessRoomRenovationInfos.size() < 1) {
            return 0;
        }
        return Integer.parseInt(businessRoomRenovationInfos.get(0).get("count").toString());
    }

    @Override
    public void updateRoomRenovationRecordInfo(Map info) {
        logger.debug("修改装修记录信息Instance 入参 info : {}", info);
        int saveFlag = sqlSessionTemplate.update("roomRenovationRecordServiceDaoImpl.updateRoomRenovationRecordInfo", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "修改装修记录信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }

}
