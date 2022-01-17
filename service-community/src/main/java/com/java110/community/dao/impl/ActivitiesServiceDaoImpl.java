package com.java110.community.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.community.dao.IActivitiesServiceDao;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.utils.util.DateUtil;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 活动服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("activitiesServiceDaoImpl")
//@Transactional
public class ActivitiesServiceDaoImpl extends BaseServiceDao implements IActivitiesServiceDao {

    private static Logger logger = LoggerFactory.getLogger(ActivitiesServiceDaoImpl.class);

    /**
     * 活动信息封装
     *
     * @param businessActivitiesInfo 活动信息 封装
     * @throws DAOException DAO异常
     */
    @Override
    public void saveBusinessActivitiesInfo(Map businessActivitiesInfo) throws DAOException {
        businessActivitiesInfo.put("month", DateUtil.getCurrentMonth());
        // 查询business_user 数据是否已经存在
        logger.debug("保存活动信息 入参 businessActivitiesInfo : {}", businessActivitiesInfo);
        int saveFlag = sqlSessionTemplate.insert("activitiesServiceDaoImpl.saveBusinessActivitiesInfo", businessActivitiesInfo);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存活动数据失败：" + JSONObject.toJSONString(businessActivitiesInfo));
        }
    }


    /**
     * 查询活动信息
     *
     * @param info bId 信息
     * @return 活动信息
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getBusinessActivitiesInfo(Map info) throws DAOException {

        logger.debug("查询活动信息 入参 info : {}", info);

        List<Map> businessActivitiesInfos = sqlSessionTemplate.selectList("activitiesServiceDaoImpl.getBusinessActivitiesInfo", info);

        return businessActivitiesInfos;
    }


    /**
     * 保存活动信息 到 instance
     *
     * @param info bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveActivitiesInfoInstance(Map info) throws DAOException {
        logger.debug("保存活动信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.insert("activitiesServiceDaoImpl.saveActivitiesInfoInstance", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存活动信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询活动信息（instance）
     *
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getActivitiesInfo(Map info) throws DAOException {
        logger.debug("查询活动信息 入参 info : {}", info);

        List<Map> businessActivitiesInfos = sqlSessionTemplate.selectList("activitiesServiceDaoImpl.getActivitiesInfo", info);

        return businessActivitiesInfos;
    }


    /**
     * 修改活动信息
     *
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateActivitiesInfoInstance(Map info) throws DAOException {
        logger.debug("修改活动信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.update("activitiesServiceDaoImpl.updateActivitiesInfoInstance", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "修改活动信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }

    /**
     * 查询活动数量
     *
     * @param info 活动信息
     * @return 活动数量
     */
    @Override
    public int queryActivitiessCount(Map info) {
        logger.debug("查询活动数据 入参 info : {}", info);

        List<Map> businessActivitiesInfos = sqlSessionTemplate.selectList("activitiesServiceDaoImpl.queryActivitiessCount", info);
        if (businessActivitiesInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessActivitiesInfos.get(0).get("count").toString());
    }


}
