package com.java110.community.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.utils.util.DateUtil;
import com.java110.community.dao.IRouteServiceDao;
import com.java110.core.base.dao.BaseServiceDao;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 路由服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("routeServiceDaoImpl")
//@Transactional
public class RouteServiceDaoImpl extends BaseServiceDao implements IRouteServiceDao {

    private static Logger logger = LoggerFactory.getLogger(RouteServiceDaoImpl.class);

    /**
     * 路由信息封装
     * @param businessRouteInfo 路由信息 封装
     * @throws DAOException DAO异常
     */
    @Override
    public void saveBusinessRouteInfo(Map businessRouteInfo) throws DAOException {
        businessRouteInfo.put("month", DateUtil.getCurrentMonth());
        // 查询business_user 数据是否已经存在
        logger.debug("保存路由信息 入参 businessRouteInfo : {}",businessRouteInfo);
        int saveFlag = sqlSessionTemplate.insert("routeServiceDaoImpl.saveBusinessRouteInfo",businessRouteInfo);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存路由数据失败："+ JSONObject.toJSONString(businessRouteInfo));
        }
    }


    /**
     * 查询路由信息
     * @param info bId 信息
     * @return 路由信息
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getBusinessRouteInfo(Map info) throws DAOException {

        logger.debug("查询路由信息 入参 info : {}",info);

        List<Map> businessRouteInfos = sqlSessionTemplate.selectList("routeServiceDaoImpl.getBusinessRouteInfo",info);

        return businessRouteInfos;
    }



    /**
     * 保存路由信息 到 instance
     * @param info   bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public int saveRouteInfo(Map info) throws DAOException {
        logger.debug("保存路由信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.insert("routeServiceDaoImpl.saveRouteInfo",info);

        return saveFlag;
    }


    /**
     * 查询路由信息（instance）
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getRouteInfo(Map info) throws DAOException {
        logger.debug("查询路由信息 入参 info : {}",info);

        List<Map> businessRouteInfos = sqlSessionTemplate.selectList("routeServiceDaoImpl.getRouteInfo",info);

        return businessRouteInfos;
    }


    /**
     * 修改路由信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public int updateRouteInfo(Map info) throws DAOException {
        logger.debug("修改路由信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.update("routeServiceDaoImpl.updateRouteInfo",info);

        return saveFlag;
    }

     /**
     * 查询路由数量
     * @param info 路由信息
     * @return 路由数量
     */
    @Override
    public int queryRoutesCount(Map info) {
        logger.debug("查询路由数据 入参 info : {}",info);

        List<Map> businessRouteInfos = sqlSessionTemplate.selectList("routeServiceDaoImpl.queryRoutesCount", info);
        if (businessRouteInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessRouteInfos.get(0).get("count").toString());
    }


}
