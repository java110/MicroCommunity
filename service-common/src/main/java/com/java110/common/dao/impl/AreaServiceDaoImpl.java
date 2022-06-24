package com.java110.common.dao.impl;

import com.java110.common.dao.IAreaServiceDao;
import com.java110.common.dao.IFileServiceDao;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.utils.exception.DAOException;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 应用服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("areaServiceDaoImpl")
//@Transactional
public class AreaServiceDaoImpl extends BaseServiceDao implements IAreaServiceDao {

    private static Logger logger = LoggerFactory.getLogger(AreaServiceDaoImpl.class);

    /**
     * 查询文件信息
     * @param info bId 信息
     * @return 应用信息
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getAreas(Map info) throws DAOException {

        logger.debug("查询应用信息 入参 info : {}",info);

        List<Map> businessAppInfos = sqlSessionTemplate.selectList("areaServiceDaoImpl.getAreas",info);

        return businessAppInfos;
    }

    /**
     * 查询省份 城市 区域
     * @param info bId 信息
     * @return 应用信息
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getProvCityArea(Map info) throws DAOException {

        logger.debug("getProvCityArea 入参 info : {}",info);

        List<Map> businessAppInfos = sqlSessionTemplate.selectList("areaServiceDaoImpl.getProvCityArea",info);

        return businessAppInfos;
    }

    /**
     * 查询完整省份 城市 区域
     * @param info bId 信息
     * @return 应用信息
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getWholeArea(Map info) throws DAOException {

        logger.debug("getWholeArea 入参 info : {}",info);

        List<Map> businessAppInfos = sqlSessionTemplate.selectList("areaServiceDaoImpl.getWholeArea",info);

        return businessAppInfos;
    }


}
