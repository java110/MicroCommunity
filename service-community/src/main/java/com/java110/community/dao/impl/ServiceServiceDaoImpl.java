package com.java110.community.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.utils.util.DateUtil;
import com.java110.community.dao.IServiceServiceDao;
import com.java110.core.base.dao.BaseServiceDao;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 服务服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("serviceServiceDaoImpl")
//@Transactional
public class ServiceServiceDaoImpl extends BaseServiceDao implements IServiceServiceDao {

    private static Logger logger = LoggerFactory.getLogger(ServiceServiceDaoImpl.class);

    /**
     * 服务信息封装
     *
     * @param businessServiceInfo 服务信息 封装
     * @throws DAOException DAO异常
     */
    @Override
    public int saveServiceInfo(Map businessServiceInfo) throws DAOException {
        businessServiceInfo.put("month", DateUtil.getCurrentMonth());
        // 查询business_user 数据是否已经存在
        logger.debug("保存服务信息 入参 businessServiceInfo : {}", businessServiceInfo);
        int saveFlag = sqlSessionTemplate.insert("serviceServiceDaoImpl.saveServiceInfo", businessServiceInfo);

        return saveFlag;
    }


    /**
     * 查询服务信息
     *
     * @param info bId 信息
     * @return 服务信息
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getBusinessServiceInfo(Map info) throws DAOException {

        logger.debug("查询服务信息 入参 info : {}", info);

        List<Map> businessServiceInfos = sqlSessionTemplate.selectList("serviceServiceDaoImpl.getBusinessServiceInfo", info);

        return businessServiceInfos;
    }


    /**
     * 保存服务信息 到 instance
     *
     * @param info bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveServiceInfoInstance(Map info) throws DAOException {
        logger.debug("保存服务信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.insert("serviceServiceDaoImpl.saveServiceInfoInstance", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存服务信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询服务信息（instance）
     *
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getServiceInfo(Map info) throws DAOException {
        logger.debug("查询服务信息 入参 info : {}", info);

        List<Map> businessServiceInfos = sqlSessionTemplate.selectList("serviceServiceDaoImpl.getServiceInfo", info);

        return businessServiceInfos;
    }


    /**
     * 修改服务信息
     *
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public int updateServiceInfo(Map info) throws DAOException {
        logger.debug("修改服务信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.update("serviceServiceDaoImpl.updateServiceInfo", info);

        return saveFlag;
    }

    /**
     * 查询服务数量
     *
     * @param info 服务信息
     * @return 服务数量
     */
    @Override
    public int queryServicesCount(Map info) {
        logger.debug("查询服务数据 入参 info : {}", info);

        List<Map> businessServiceInfos = sqlSessionTemplate.selectList("serviceServiceDaoImpl.queryServicesCount", info);
        if (businessServiceInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessServiceInfos.get(0).get("count").toString());
    }


    /**
     * 服务信息封装
     *
     * @param businessServiceInfo 服务信息 封装
     * @throws DAOException DAO异常
     */
    @Override
    public int saveServiceProvideInfo(Map businessServiceInfo) throws DAOException {
        // 查询business_user 数据是否已经存在
        logger.debug("保存服务提供信息 入参 saveServiceProvideInfo : {}", businessServiceInfo);
        int saveFlag = sqlSessionTemplate.insert("serviceServiceDaoImpl.saveServiceProvideInfo", businessServiceInfo);

        return saveFlag;
    }


    /**
     * 查询服务信息（instance）
     *
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getServiceProvideInfo(Map info) throws DAOException {
        logger.debug("查询服务信息 入参 info : {}", info);

        List<Map> businessServiceInfos = sqlSessionTemplate.selectList("serviceServiceDaoImpl.getServiceProvideInfo", info);

        return businessServiceInfos;
    }


    /**
     * 修改服务信息
     *
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public int updateServiceProvideInfo(Map info) throws DAOException {
        logger.debug("修改服务信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.update("serviceServiceDaoImpl.updateServiceProvideInfo", info);

        return saveFlag;
    }

    /**
     * 查询服务数量
     *
     * @param info 服务信息
     * @return 服务数量
     */
    @Override
    public int queryServiceProvidesCount(Map info) {
        logger.debug("查询服务数据 入参 info : {}", info);

        List<Map> businessServiceInfos = sqlSessionTemplate.selectList("serviceServiceDaoImpl.queryServiceProvidesCount", info);
        if (businessServiceInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessServiceInfos.get(0).get("count").toString());
    }


}
