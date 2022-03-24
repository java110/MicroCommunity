package com.java110.store.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.dto.resourceStore.ResourceStoreDto;
import com.java110.store.dao.IResourceStoreServiceDao;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.utils.util.DateUtil;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 资源服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("resourceResourceStoreServiceDaoImpl")
//@Transactional
public class ResourceStoreServiceDaoImpl extends BaseServiceDao implements IResourceStoreServiceDao {

    private static Logger logger = LoggerFactory.getLogger(ResourceStoreServiceDaoImpl.class);

    /**
     * 资源信息封装
     *
     * @param businessResourceStoreInfo 资源信息 封装
     * @throws DAOException DAO异常
     */
    @Override
    public void saveBusinessResourceStoreInfo(Map businessResourceStoreInfo) throws DAOException {
        businessResourceStoreInfo.put("month", DateUtil.getCurrentMonth());
        // 查询business_user 数据是否已经存在
        logger.debug("保存资源信息 入参 businessResourceStoreInfo : {}", businessResourceStoreInfo);
        int saveFlag = sqlSessionTemplate.insert("resourceResourceStoreServiceDaoImpl.saveBusinessResourceStoreInfo", businessResourceStoreInfo);
        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存资源数据失败：" + JSONObject.toJSONString(businessResourceStoreInfo));
        }
    }


    /**
     * 查询资源信息
     *
     * @param info bId 信息
     * @return 资源信息
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getBusinessResourceStoreInfo(Map info) throws DAOException {
        logger.debug("查询资源信息 入参 info : {}", info);
        List<Map> businessResourceStoreInfos = sqlSessionTemplate.selectList("resourceResourceStoreServiceDaoImpl.getBusinessResourceStoreInfo", info);
        return businessResourceStoreInfos;
    }


    /**
     * 保存资源信息 到 instance
     *
     * @param info bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveResourceStoreInfoInstance(Map info) throws DAOException {
        logger.debug("保存资源信息Instance 入参 info : {}", info);
        int saveFlag = sqlSessionTemplate.insert("resourceResourceStoreServiceDaoImpl.saveResourceStoreInfoInstance", info);
        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存资源信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }

    /**
     * 保存物品信息
     *
     * @param info
     * @throws DAOException
     */
    @Override
    public void saveResourceStoreInfo(Map info) throws DAOException {
        logger.debug("保存物品信息入参 info : {}", info);
        int saveFlag = sqlSessionTemplate.insert("resourceResourceStoreServiceDaoImpl.saveResourceStoreInfo", info);
        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存物品信息数据失败：" + JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询资源信息（instance）
     *
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getResourceStoreInfo(Map info) throws DAOException {
        logger.debug("查询资源信息 入参 info : {}", info);
        List<Map> businessResourceStoreInfos = sqlSessionTemplate.selectList("resourceResourceStoreServiceDaoImpl.getResourceStoreInfo", info);
        return businessResourceStoreInfos;
    }


    /**
     * 修改资源信息
     *
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public int updateResourceStoreInfoInstance(Map info) throws DAOException {
        logger.debug("修改资源信息Instance 入参 info : {}", info);
        int saveFlag = sqlSessionTemplate.update("resourceResourceStoreServiceDaoImpl.updateResourceStoreInfoInstance", info);
        return saveFlag;
    }

    /**
     * 查询资源数量
     *
     * @param info 资源信息
     * @return 资源数量
     */
    @Override
    public int queryResourceStoresCount(Map info) {
        logger.debug("查询资源数据 入参 info : {}", info);
        List<Map> businessResourceStoreInfos = sqlSessionTemplate.selectList("resourceResourceStoreServiceDaoImpl.queryResourceStoresCount", info);
        if (businessResourceStoreInfos.size() < 1) {
            return 0;
        }
        return Integer.parseInt(businessResourceStoreInfos.get(0).get("count").toString());
    }

    /**
     * 查询资源总价
     *
     * @param info 资源信息
     * @return 资源数量
     */
    @Override
    public String queryResourceStoresTotalPrice(Map info) {
        logger.debug("查询资源数据 入参 info : {}", info);
        List<Map> businessResourceStoreInfos = sqlSessionTemplate.selectList("resourceResourceStoreServiceDaoImpl.queryResourceStoresTotalPrice", info);
        if (businessResourceStoreInfos == null || businessResourceStoreInfos.size() < 1) {
            return "0";
        }
        return businessResourceStoreInfos.get(0).get("totalPrice").toString();
    }


}
