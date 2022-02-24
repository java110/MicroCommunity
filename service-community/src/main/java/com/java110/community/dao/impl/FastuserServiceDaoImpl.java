package com.java110.community.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.community.dao.IActivitiesServiceDao;
import com.java110.community.dao.IFastuserServiceDao;
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
@Service("fastuserServiceDaoImpl")
//@Transactional
public class FastuserServiceDaoImpl extends BaseServiceDao implements IFastuserServiceDao {

    private static Logger logger = LoggerFactory.getLogger(FastuserServiceDaoImpl.class);

    /**
     * 活动信息封装
     *
     * @param businessFastuserInfo 活动信息 封装
     * @throws DAOException DAO异常
     */
    @Override
    public void saveBusinessFastuserInfo(Map businessFastuserInfo) throws DAOException {
        businessFastuserInfo.put("month", DateUtil.getCurrentMonth());
        // 查询business_user 数据是否已经存在
        logger.debug("保存活动信息 入参 businessFastuserInfo : {}", businessFastuserInfo);
        int saveFlag = sqlSessionTemplate.insert("fastuserServiceDaoImpl.saveBusinessFastuserInfo", businessFastuserInfo);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存活动数据失败：" + JSONObject.toJSONString(businessFastuserInfo));
        }
    }



    /**
     * 保存活动信息 到 instance
     *
     * @param info bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveFastuserInfoInstance(Map info) throws DAOException {
        logger.debug("保存活动信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.insert("fastuserServiceDaoImpl.saveFastuserInfoInstance", info);

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
    public List<Map> getFastuserInfo(Map info) throws DAOException {
        logger.debug("查询活动信息 入参 info : {}", info);

        List<Map> businessActivitiesInfos = sqlSessionTemplate.selectList("fastuserServiceDaoImpl.getFastuserInfo", info);

        return businessActivitiesInfos;
    }

    /**
     * 查询活动信息
     *
     * @param info bId 信息
     * @return 活动信息
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getBusinessFastuserInfo(Map info) throws DAOException {

        logger.debug("查询活动信息 入参 info : {}", info);

        List<Map> businessActivitiesInfos = sqlSessionTemplate.selectList("fastuserServiceDaoImpl.getBusinessFastuserInfo", info);

        return businessActivitiesInfos;
    }

    /**
     * 查询活动数量
     *
     * @param info 活动信息
     * @return 活动数量
     */
    @Override
    public int queryFastuserCount(Map info) {
        logger.debug("查询活动数据 入参 info : {}", info);

        List<Map> businessActivitiesInfos = sqlSessionTemplate.selectList("fastuserServiceDaoImpl.queryFastuserCount", info);
        if (businessActivitiesInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessActivitiesInfos.get(0).get("count").toString());
    }
    /**
     * 修改活动信息
     *
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateFastuserInfoInstance(Map info) throws DAOException {
        logger.debug("修改活动信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.update("fastuserServiceDaoImpl.updateFastuserInfoInstance", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "修改活动信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }

}
