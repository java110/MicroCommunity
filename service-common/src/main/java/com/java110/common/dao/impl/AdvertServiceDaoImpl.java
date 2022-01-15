package com.java110.common.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.common.dao.IAdvertServiceDao;
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
 * 广告信息服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("advertServiceDaoImpl")
//@Transactional
public class AdvertServiceDaoImpl extends BaseServiceDao implements IAdvertServiceDao {

    private static Logger logger = LoggerFactory.getLogger(AdvertServiceDaoImpl.class);

    /**
     * 广告信息信息封装
     *
     * @param businessAdvertInfo 广告信息信息 封装
     * @throws DAOException DAO异常
     */
    @Override
    public void saveBusinessAdvertInfo(Map businessAdvertInfo) throws DAOException {
        businessAdvertInfo.put("month", DateUtil.getCurrentMonth());
        // 查询business_user 数据是否已经存在
        logger.debug("保存广告信息信息 入参 businessAdvertInfo : {}", businessAdvertInfo);
        int saveFlag = sqlSessionTemplate.insert("advertServiceDaoImpl.saveBusinessAdvertInfo", businessAdvertInfo);
        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存广告信息数据失败：" + JSONObject.toJSONString(businessAdvertInfo));
        }
    }

    /**
     * 查询广告信息信息
     *
     * @param info bId 信息
     * @return 广告信息信息
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getBusinessAdvertInfo(Map info) throws DAOException {
        logger.debug("查询广告信息信息 入参 info : {}", info);
        List<Map> businessAdvertInfos = sqlSessionTemplate.selectList("advertServiceDaoImpl.getBusinessAdvertInfo", info);
        return businessAdvertInfos;
    }

    /**
     * 保存广告信息信息 到 instance
     *
     * @param info bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveAdvertInfoInstance(Map info) throws DAOException {
        logger.debug("保存广告信息信息Instance 入参 info : {}", info);
        int saveFlag = sqlSessionTemplate.insert("advertServiceDaoImpl.saveAdvertInfoInstance", info);
        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存广告信息信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }

    /**
     * 查询广告信息信息（instance）
     *
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getAdvertInfo(Map info) throws DAOException {
        logger.debug("查询广告信息信息 入参 info : {}", info);
        List<Map> businessAdvertInfos = sqlSessionTemplate.selectList("advertServiceDaoImpl.getAdvertInfo", info);
        return businessAdvertInfos;
    }

    /**
     * 修改广告信息信息
     *
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateAdvertInfoInstance(Map info) throws DAOException {
        logger.debug("修改广告信息信息Instance 入参 info : {}", info);
        int saveFlag = sqlSessionTemplate.update("advertServiceDaoImpl.updateAdvertInfoInstance", info);
        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "修改广告信息信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }

    /**
     * 查询广告信息数量
     *
     * @param info 广告信息信息
     * @return 广告信息数量
     */
    @Override
    public int queryAdvertsCount(Map info) {
        logger.debug("查询广告信息数据 入参 info : {}", info);
        List<Map> businessAdvertInfos = sqlSessionTemplate.selectList("advertServiceDaoImpl.queryAdvertsCount", info);
        if (businessAdvertInfos.size() < 1) {
            return 0;
        }
        return Integer.parseInt(businessAdvertInfos.get(0).get("count").toString());
    }

    @Override
    public void updateAdverts(Map info) {
        logger.debug("修改广告信息Instance 入参 info : {}", info);
//        int saveFlag = sqlSessionTemplate.update("advertServiceDaoImpl.updateAdvertInfoInstance", info);
        int saveFlag = sqlSessionTemplate.update("advertServiceDaoImpl.updateAdvert", info);
        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "修改广告信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }
}
