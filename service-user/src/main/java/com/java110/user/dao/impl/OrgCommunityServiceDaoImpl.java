package com.java110.user.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.user.dao.IOrgCommunityServiceDao;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.utils.util.DateUtil;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 隶属小区服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("orgCommunityServiceDaoImpl")
//@Transactional
public class OrgCommunityServiceDaoImpl extends BaseServiceDao implements IOrgCommunityServiceDao {

    private static Logger logger = LoggerFactory.getLogger(OrgCommunityServiceDaoImpl.class);

    /**
     * 隶属小区信息封装
     *
     * @param businessOrgCommunityInfo 隶属小区信息 封装
     * @throws DAOException DAO异常
     */
    @Override
    public void saveBusinessOrgCommunityInfo(Map businessOrgCommunityInfo) throws DAOException {
        businessOrgCommunityInfo.put("month", DateUtil.getCurrentMonth());
        // 查询business_user 数据是否已经存在
        logger.debug("保存隶属小区信息 入参 businessOrgCommunityInfo : {}", businessOrgCommunityInfo);
        int saveFlag = sqlSessionTemplate.insert("orgCommunityServiceDaoImpl.saveBusinessOrgCommunityInfo", businessOrgCommunityInfo);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存隶属小区数据失败：" + JSONObject.toJSONString(businessOrgCommunityInfo));
        }
    }


    /**
     * 查询隶属小区信息
     *
     * @param info bId 信息
     * @return 隶属小区信息
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getBusinessOrgCommunityInfo(Map info) throws DAOException {

        logger.debug("查询隶属小区信息 入参 info : {}", info);

        List<Map> businessOrgCommunityInfos = sqlSessionTemplate.selectList("orgCommunityServiceDaoImpl.getBusinessOrgCommunityInfo", info);

        return businessOrgCommunityInfos;
    }


    /**
     * 保存隶属小区信息 到 instance
     *
     * @param info bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveOrgCommunityInfoInstance(Map info) throws DAOException {
        logger.debug("保存隶属小区信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.insert("orgCommunityServiceDaoImpl.saveOrgCommunityInfoInstance", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存隶属小区信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询隶属小区信息（instance）
     *
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getOrgCommunityInfo(Map info) throws DAOException {
        logger.debug("查询隶属小区信息 入参 info : {}", info);

        List<Map> businessOrgCommunityInfos = sqlSessionTemplate.selectList("orgCommunityServiceDaoImpl.getOrgCommunityInfo", info);

        return businessOrgCommunityInfos;
    }


    /**
     * 修改隶属小区信息
     *
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateOrgCommunityInfoInstance(Map info) throws DAOException {
        logger.debug("修改隶属小区信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.update("orgCommunityServiceDaoImpl.updateOrgCommunityInfoInstance", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "修改隶属小区信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }

    /**
     * 查询隶属小区数量
     *
     * @param info 隶属小区信息
     * @return 隶属小区数量
     */
    @Override
    public int queryOrgCommunitysCount(Map info) {
        logger.debug("查询隶属小区数据 入参 info : {}", info);

        List<Map> businessOrgCommunityInfos = sqlSessionTemplate.selectList("orgCommunityServiceDaoImpl.queryOrgCommunitysCount", info);
        if (businessOrgCommunityInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessOrgCommunityInfos.get(0).get("count").toString());
    }


}
