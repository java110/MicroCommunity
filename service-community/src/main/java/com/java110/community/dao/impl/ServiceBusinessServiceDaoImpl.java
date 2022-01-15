package com.java110.community.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.constant.StatusConstant;
import com.java110.utils.exception.DAOException;
import com.java110.utils.util.DateUtil;
import com.java110.community.dao.IServiceBusinessServiceDao;
import com.java110.core.base.dao.BaseServiceDao;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 服务实现服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("serviceBusinessServiceDaoImpl")
@Transactional
public class ServiceBusinessServiceDaoImpl extends BaseServiceDao implements IServiceBusinessServiceDao {

    private static Logger logger = LoggerFactory.getLogger(ServiceBusinessServiceDaoImpl.class);

    /**
     * 服务实现信息封装
     *
     * @param businessServiceBusinessInfo 服务实现信息 封装
     * @throws DAOException DAO异常
     */
    @Override
    public void saveBusinessServiceBusinessInfo(Map businessServiceBusinessInfo) throws DAOException {
        businessServiceBusinessInfo.put("month", DateUtil.getCurrentMonth());
        // 查询business_user 数据是否已经存在
        logger.debug("保存服务实现信息 入参 businessServiceBusinessInfo : {}", businessServiceBusinessInfo);
        int saveFlag = sqlSessionTemplate.insert("serviceBusinessServiceDaoImpl.saveBusinessServiceBusinessInfo", businessServiceBusinessInfo);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存服务实现数据失败：" + JSONObject.toJSONString(businessServiceBusinessInfo));
        }
    }


    /**
     * 查询服务实现信息
     *
     * @param info bId 信息
     * @return 服务实现信息
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getBusinessServiceBusinessInfo(Map info) throws DAOException {

        logger.debug("查询服务实现信息 入参 info : {}", info);

        List<Map> businessServiceBusinessInfos = sqlSessionTemplate.selectList("serviceBusinessServiceDaoImpl.getBusinessServiceBusinessInfo", info);

        return businessServiceBusinessInfos;
    }


    /**
     * 保存服务实现信息 到 instance
     *
     * @param info bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveServiceBusinessInfoInstance(Map info) throws DAOException {
        logger.debug("保存服务实现信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.insert("serviceBusinessServiceDaoImpl.saveServiceBusinessInfoInstance", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存服务实现信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询服务实现信息（instance）
     *
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getServiceBusinessInfo(Map info) throws DAOException {
        logger.debug("查询服务实现信息 入参 info : {}", info);

        List<Map> businessServiceBusinessInfos = sqlSessionTemplate.selectList("serviceBusinessServiceDaoImpl.getServiceBusinessInfo", info);

        return businessServiceBusinessInfos;
    }


    /**
     * 修改服务实现信息
     *
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateServiceBusinessInfoInstance(Map info) throws DAOException {
        logger.debug("修改服务实现信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.update("serviceBusinessServiceDaoImpl.updateServiceBusinessInfoInstance", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "修改服务实现信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }

    /**
     * 查询服务实现数量
     *
     * @param info 服务实现信息
     * @return 服务实现数量
     */
    @Override
    public int queryServiceBusinesssCount(Map info) {
        logger.debug("查询服务实现数据 入参 info : {}", info);

        List<Map> businessServiceBusinessInfos = sqlSessionTemplate.selectList("serviceBusinessServiceDaoImpl.queryServiceBusinesssCount", info);
        if (businessServiceBusinessInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessServiceBusinessInfos.get(0).get("count").toString());
    }

    @Override
    public int saveServiceBusiness(Map info) {
        logger.debug("保存服务实现信息入参 info : {}", info);

        int saveFlag = 0;

        //查询是否存在businessType
        List<Map> businessTypes = sqlSessionTemplate.selectList("serviceBusinessServiceDaoImpl.queryBusinessType", info);

        if (businessTypes == null || businessTypes.size() == 0) {
            // 保存 businessType
            saveFlag = sqlSessionTemplate.insert("serviceBusinessServiceDaoImpl.saveBusinessType", info);
            if (saveFlag < 1) {
                return saveFlag;
            }
        }

        saveFlag = sqlSessionTemplate.insert("serviceBusinessServiceDaoImpl.saveServiceBusiness", info);

        return saveFlag;

    }

    @Override
    public int updateServiceBusiness(Map info) {
        logger.debug("保存服务实现信息入参 info : {}", info);

        int updateFlag = sqlSessionTemplate.update("serviceBusinessServiceDaoImpl.updateBusinessType", info);

        if (updateFlag < 1) {
            return updateFlag;
        }

        updateFlag = sqlSessionTemplate.update("serviceBusinessServiceDaoImpl.updateServiceBusiness", info);

        return updateFlag;
    }

    @Override
    public int deleteServiceBusiness(Map info) {
        logger.debug("保存服务实现信息入参 info : {}", info);
        info.put("statusCd", StatusConstant.STATUS_CD_INVALID);

        int saveFlag = sqlSessionTemplate.insert("serviceBusinessServiceDaoImpl.updateServiceBusiness", info);

        return saveFlag;
    }


}
