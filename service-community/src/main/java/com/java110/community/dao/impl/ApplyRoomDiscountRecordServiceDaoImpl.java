package com.java110.community.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.utils.util.DateUtil;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.community.dao.IApplyRoomDiscountRecordServiceDao;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 验房记录服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("applyRoomDiscountRecordServiceDaoImpl")
//@Transactional
public class ApplyRoomDiscountRecordServiceDaoImpl extends BaseServiceDao implements IApplyRoomDiscountRecordServiceDao {

    private static Logger logger = LoggerFactory.getLogger(ApplyRoomDiscountRecordServiceDaoImpl.class);

    /**
     * 验房记录信息封装
     *
     * @param businessApplyRoomDiscountRecordInfo 验房记录信息 封装
     * @throws DAOException DAO异常
     */
    @Override
    public void saveBusinessApplyRoomDiscountRecordInfo(Map businessApplyRoomDiscountRecordInfo) throws DAOException {
        businessApplyRoomDiscountRecordInfo.put("month", DateUtil.getCurrentMonth());
        // 查询business_user 数据是否已经存在
        logger.debug("保存验房记录信息 入参 businessApplyRoomDiscountRecordInfo : {}", businessApplyRoomDiscountRecordInfo);
        int saveFlag = sqlSessionTemplate.insert("applyRoomDiscountRecordServiceDaoImpl.saveBusinessApplyRoomDiscountRecordInfo", businessApplyRoomDiscountRecordInfo);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存验房记录数据失败：" + JSONObject.toJSONString(businessApplyRoomDiscountRecordInfo));
        }
    }


    /**
     * 查询验房记录信息
     *
     * @param info bId 信息
     * @return 验房记录信息
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getBusinessApplyRoomDiscountRecordInfo(Map info) throws DAOException {

        logger.debug("查询验房记录信息 入参 info : {}", info);

        List<Map> businessApplyRoomDiscountRecordInfos = sqlSessionTemplate.selectList("applyRoomDiscountRecordServiceDaoImpl.getBusinessApplyRoomDiscountRecordInfo", info);

        return businessApplyRoomDiscountRecordInfos;
    }


    /**
     * 保存验房记录信息 到 instance
     *
     * @param info bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public int saveApplyRoomDiscountRecordInfoInstance(Map info) throws DAOException {
        logger.debug("保存验房记录信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.insert("applyRoomDiscountRecordServiceDaoImpl.saveApplyRoomDiscountRecordInfoInstance", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存验房记录信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
        return saveFlag;
    }

    @Override
    public int saveApplyRoomDiscountRecordInfo(Map info) throws DAOException {
        logger.debug("保存验房记录信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.insert("applyRoomDiscountRecordServiceDaoImpl.saveApplyRoomDiscountRecordInfo", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存验房记录信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
        return saveFlag;
    }


    /**
     * 查询验房记录信息（instance）
     *
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getApplyRoomDiscountRecordInfo(Map info) throws DAOException {
        logger.debug("查询验房记录信息 入参 info : {}", info);

        List<Map> businessApplyRoomDiscountRecordInfos = sqlSessionTemplate.selectList("applyRoomDiscountRecordServiceDaoImpl.getApplyRoomDiscountRecordInfo", info);

        return businessApplyRoomDiscountRecordInfos;
    }

    @Override
    public List<Map> selectApplyRoomDiscountRecordInfo(Map info) throws DAOException {
        logger.debug("查询验房记录信息 入参 info : {}", info);

        List<Map> businessApplyRoomDiscountRecordInfos = sqlSessionTemplate.selectList("applyRoomDiscountRecordServiceDaoImpl.selectApplyRoomDiscountRecordInfo", info);

        return businessApplyRoomDiscountRecordInfos;
    }


    /**
     * 修改验房记录信息
     *
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateApplyRoomDiscountRecordInfoInstance(Map info) throws DAOException {
        logger.debug("修改验房记录信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.update("applyRoomDiscountRecordServiceDaoImpl.updateApplyRoomDiscountRecordInfoInstance", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "修改验房记录信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }

    /**
     * 查询验房记录数量
     *
     * @param info 验房记录信息
     * @return 验房记录数量
     */
    @Override
    public int queryApplyRoomDiscountRecordsCount(Map info) {
        logger.debug("查询验房记录数据 入参 info : {}", info);

        List<Map> businessApplyRoomDiscountRecordInfos = sqlSessionTemplate.selectList("applyRoomDiscountRecordServiceDaoImpl.queryApplyRoomDiscountRecordsCount", info);
        if (businessApplyRoomDiscountRecordInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessApplyRoomDiscountRecordInfos.get(0).get("count").toString());
    }

    @Override
    public int selectApplyRoomDiscountRecordsCount(Map info) {
        logger.debug("查询验房记录数据 入参 info : {}", info);

        List<Map> businessApplyRoomDiscountRecordInfos = sqlSessionTemplate.selectList("applyRoomDiscountRecordServiceDaoImpl.selectApplyRoomDiscountRecordsCount", info);
        if (businessApplyRoomDiscountRecordInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessApplyRoomDiscountRecordInfos.get(0).get("count").toString());
    }

}
