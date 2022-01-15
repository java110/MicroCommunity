package com.java110.community.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.utils.util.DateUtil;
import com.java110.community.dao.IAppServiceDao;
import com.java110.core.base.dao.BaseServiceDao;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 应用服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("appServiceDaoImpl")
//@Transactional
public class AppServiceDaoImpl extends BaseServiceDao implements IAppServiceDao {

    private static Logger logger = LoggerFactory.getLogger(AppServiceDaoImpl.class);

    /**
     * 应用信息封装
     * @param businessAppInfo 应用信息 封装
     * @throws DAOException DAO异常
     */
    @Override
    public int saveAppInfo(Map businessAppInfo) throws DAOException {
        businessAppInfo.put("month", DateUtil.getCurrentMonth());
        // 查询business_user 数据是否已经存在
        logger.debug("保存应用信息 入参 businessAppInfo : {}",businessAppInfo);
        int saveFlag = sqlSessionTemplate.insert("appServiceDaoImpl.saveAppInfo",businessAppInfo);

        return saveFlag;
    }


    /**
     * 查询应用信息
     * @param info bId 信息
     * @return 应用信息
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getBusinessAppInfo(Map info) throws DAOException {

        logger.debug("查询应用信息 入参 info : {}",info);

        List<Map> businessAppInfos = sqlSessionTemplate.selectList("appServiceDaoImpl.getBusinessAppInfo",info);

        return businessAppInfos;
    }



    /**
     * 保存应用信息 到 instance
     * @param info   bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveAppInfoInstance(Map info) throws DAOException {
        logger.debug("保存应用信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.insert("appServiceDaoImpl.saveAppInfoInstance",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存应用信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询应用信息（instance）
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getAppInfo(Map info) throws DAOException {
        logger.debug("查询应用信息 入参 info : {}",info);

        List<Map> businessAppInfos = sqlSessionTemplate.selectList("appServiceDaoImpl.getAppInfo",info);

        return businessAppInfos;
    }


    /**
     * 修改应用信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public int updateAppInfo(Map info) throws DAOException {
        logger.debug("修改应用信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.update("appServiceDaoImpl.updateAppInfo",info);

        return saveFlag;
    }

     /**
     * 查询应用数量
     * @param info 应用信息
     * @return 应用数量
     */
    @Override
    public int queryAppsCount(Map info) {
        logger.debug("查询应用数据 入参 info : {}",info);

        List<Map> businessAppInfos = sqlSessionTemplate.selectList("appServiceDaoImpl.queryAppsCount", info);
        if (businessAppInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessAppInfos.get(0).get("count").toString());
    }


}
