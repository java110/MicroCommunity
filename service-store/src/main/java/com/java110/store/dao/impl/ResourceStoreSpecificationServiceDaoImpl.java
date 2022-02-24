package com.java110.store.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.utils.util.DateUtil;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.store.dao.IResourceStoreSpecificationServiceDao;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 物品规格服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("resourceStoreSpecificationServiceDaoImpl")
//@Transactional
public class ResourceStoreSpecificationServiceDaoImpl extends BaseServiceDao implements IResourceStoreSpecificationServiceDao {

    private static Logger logger = LoggerFactory.getLogger(ResourceStoreSpecificationServiceDaoImpl.class);

    /**
     * 物品规格信息封装
     *
     * @param businessResourceStoreSpecificationInfo 物品规格信息 封装
     * @throws DAOException DAO异常
     */
    @Override
    public void saveBusinessResourceStoreSpecificationInfo(Map businessResourceStoreSpecificationInfo) throws DAOException {
        businessResourceStoreSpecificationInfo.put("month", DateUtil.getCurrentMonth());
        // 查询business_user 数据是否已经存在
        logger.debug("保存物品规格信息 入参 businessResourceStoreSpecificationInfo : {}", businessResourceStoreSpecificationInfo);
        int saveFlag = sqlSessionTemplate.insert("resourceStoreSpecificationServiceDaoImpl.saveBusinessResourceStoreSpecificationInfo", businessResourceStoreSpecificationInfo);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存物品规格数据失败：" + JSONObject.toJSONString(businessResourceStoreSpecificationInfo));
        }
    }


    /**
     * 查询物品规格信息
     *
     * @param info bId 信息
     * @return 物品规格信息
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getBusinessResourceStoreSpecificationInfo(Map info) throws DAOException {

        logger.debug("查询物品规格信息 入参 info : {}", info);

        List<Map> businessResourceStoreSpecificationInfos = sqlSessionTemplate.selectList("resourceStoreSpecificationServiceDaoImpl.getBusinessResourceStoreSpecificationInfo", info);

        return businessResourceStoreSpecificationInfos;
    }


    /**
     * 保存物品规格信息 到 instance
     *
     * @param info bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveResourceStoreSpecificationInfoInstance(Map info) throws DAOException {
        logger.debug("保存物品规格信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.insert("resourceStoreSpecificationServiceDaoImpl.saveResourceStoreSpecificationInfoInstance", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存物品规格信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询物品规格信息（instance）
     *
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getResourceStoreSpecificationInfo(Map info) throws DAOException {
        logger.debug("查询物品规格信息 入参 info : {}", info);

        List<Map> businessResourceStoreSpecificationInfos = sqlSessionTemplate.selectList("resourceStoreSpecificationServiceDaoImpl.getResourceStoreSpecificationInfo", info);

        return businessResourceStoreSpecificationInfos;
    }


    /**
     * 修改物品规格信息
     *
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateResourceStoreSpecificationInfoInstance(Map info) throws DAOException {
        logger.debug("修改物品规格信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.update("resourceStoreSpecificationServiceDaoImpl.updateResourceStoreSpecificationInfoInstance", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "修改物品规格信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }

    /**
     * 查询物品规格数量
     *
     * @param info 物品规格信息
     * @return 物品规格数量
     */
    @Override
    public int queryResourceStoreSpecificationsCount(Map info) {
        logger.debug("查询物品规格数据 入参 info : {}", info);

        List<Map> businessResourceStoreSpecificationInfos = sqlSessionTemplate.selectList("resourceStoreSpecificationServiceDaoImpl.queryResourceStoreSpecificationsCount", info);
        if (businessResourceStoreSpecificationInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessResourceStoreSpecificationInfos.get(0).get("count").toString());
    }


}
