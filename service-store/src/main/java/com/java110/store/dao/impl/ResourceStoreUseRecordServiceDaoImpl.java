package com.java110.store.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.utils.util.DateUtil;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.store.dao.IResourceStoreUseRecordServiceDao;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 物品使用记录服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("resourceResourceStoreUseRecordUseRecordServiceDaoImpl")
//@Transactional
public class ResourceStoreUseRecordServiceDaoImpl extends BaseServiceDao implements IResourceStoreUseRecordServiceDao {

    private static Logger logger = LoggerFactory.getLogger(ResourceStoreUseRecordServiceDaoImpl.class);

    /**
     * 物品使用记录信息封装
     *
     * @param businessResourceStoreUseRecordInfo 物品使用记录信息 封装
     * @throws DAOException DAO异常
     */
    @Override
    public void saveBusinessResourceStoreUseRecordInfo(Map businessResourceStoreUseRecordInfo) throws DAOException {
        businessResourceStoreUseRecordInfo.put("month", DateUtil.getCurrentMonth());
        // 查询business_user 数据是否已经存在
        logger.debug("保存物品使用记录信息 入参 businessResourceStoreUseRecordInfo : {}", businessResourceStoreUseRecordInfo);
        int saveFlag = sqlSessionTemplate.insert("resourceResourceStoreUseRecordUseRecordServiceDaoImpl.saveBusinessResourceStoreUseRecordInfo", businessResourceStoreUseRecordInfo);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存物品使用记录数据失败：" + JSONObject.toJSONString(businessResourceStoreUseRecordInfo));
        }
    }


    /**
     * 查询物品使用记录信息
     *
     * @param info bId 信息
     * @return 物品使用记录信息
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getBusinessResourceStoreUseRecordInfo(Map info) throws DAOException {

        logger.debug("查询物品使用记录信息 入参 info : {}", info);

        List<Map> businessResourceStoreUseRecordInfos = sqlSessionTemplate.selectList("resourceResourceStoreUseRecordUseRecordServiceDaoImpl.getBusinessResourceStoreUseRecordInfo", info);

        return businessResourceStoreUseRecordInfos;
    }


    /**
     * 保存物品使用记录信息 到 instance
     *
     * @param info bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveResourceStoreUseRecordInfoInstance(Map info) throws DAOException {
        logger.debug("保存物品使用记录信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.insert("resourceResourceStoreUseRecordUseRecordServiceDaoImpl.saveResourceStoreUseRecordInfoInstance", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存物品使用记录信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询物品使用记录信息（instance）
     *
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getResourceStoreUseRecordInfo(Map info) throws DAOException {
        logger.debug("查询物品使用记录信息 入参 info : {}", info);

        List<Map> businessResourceStoreUseRecordInfos = sqlSessionTemplate.selectList("resourceResourceStoreUseRecordUseRecordServiceDaoImpl.getResourceStoreUseRecordInfo", info);

        return businessResourceStoreUseRecordInfos;
    }


    /**
     * 修改物品使用记录信息
     *
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateResourceStoreUseRecordInfoInstance(Map info) throws DAOException {
        logger.debug("修改物品使用记录信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.update("resourceResourceStoreUseRecordUseRecordServiceDaoImpl.updateResourceStoreUseRecordInfoInstance", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "修改物品使用记录信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }

    /**
     * 查询物品使用记录数量
     *
     * @param info 物品使用记录信息
     * @return 物品使用记录数量
     */
    @Override
    public int queryResourceStoreUseRecordsCount(Map info) {
        logger.debug("查询物品使用记录数据 入参 info : {}", info);

        List<Map> businessResourceStoreUseRecordInfos = sqlSessionTemplate.selectList("resourceResourceStoreUseRecordUseRecordServiceDaoImpl.queryResourceStoreUseRecordsCount", info);
        if (businessResourceStoreUseRecordInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessResourceStoreUseRecordInfos.get(0).get("count").toString());
    }


}
