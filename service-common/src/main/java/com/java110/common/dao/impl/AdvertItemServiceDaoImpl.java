package com.java110.common.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.common.dao.IAdvertItemServiceDao;
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
 * 广告项信息服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("advertItemServiceDaoImpl")
//@Transactional
public class AdvertItemServiceDaoImpl extends BaseServiceDao implements IAdvertItemServiceDao {

    private static Logger logger = LoggerFactory.getLogger(AdvertItemServiceDaoImpl.class);

    /**
     * 广告项信息信息封装
     *
     * @param businessAdvertItemInfo 广告项信息信息 封装
     * @throws DAOException DAO异常
     */
    @Override
    public void saveBusinessAdvertItemInfo(Map businessAdvertItemInfo) throws DAOException {
        businessAdvertItemInfo.put("month", DateUtil.getCurrentMonth());
        // 查询business_user 数据是否已经存在
        logger.debug("保存广告项信息信息 入参 businessAdvertItemInfo : {}", businessAdvertItemInfo);
        int saveFlag = sqlSessionTemplate.insert("advertItemServiceDaoImpl.saveBusinessAdvertItemInfo", businessAdvertItemInfo);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存广告项信息数据失败：" + JSONObject.toJSONString(businessAdvertItemInfo));
        }
    }


    /**
     * 查询广告项信息信息
     *
     * @param info bId 信息
     * @return 广告项信息信息
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getBusinessAdvertItemInfo(Map info) throws DAOException {

        logger.debug("查询广告项信息信息 入参 info : {}", info);

        List<Map> businessAdvertItemInfos = sqlSessionTemplate.selectList("advertItemServiceDaoImpl.getBusinessAdvertItemInfo", info);

        return businessAdvertItemInfos;
    }


    /**
     * 保存广告项信息信息 到 instance
     *
     * @param info bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveAdvertItemInfoInstance(Map info) throws DAOException {
        logger.debug("保存广告项信息信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.insert("advertItemServiceDaoImpl.saveAdvertItemInfoInstance", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存广告项信息信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询广告项信息信息（instance）
     *
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getAdvertItemInfo(Map info) throws DAOException {
        logger.debug("查询广告项信息信息 入参 info : {}", info);

        List<Map> businessAdvertItemInfos = sqlSessionTemplate.selectList("advertItemServiceDaoImpl.getAdvertItemInfo", info);

        return businessAdvertItemInfos;
    }


    /**
     * 修改广告项信息信息
     *
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateAdvertItemInfoInstance(Map info) throws DAOException {
        logger.debug("修改广告项信息信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.update("advertItemServiceDaoImpl.updateAdvertItemInfoInstance", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "修改广告项信息信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }

    /**
     * 查询广告项信息数量
     *
     * @param info 广告项信息信息
     * @return 广告项信息数量
     */
    @Override
    public int queryAdvertItemsCount(Map info) {
        logger.debug("查询广告项信息数据 入参 info : {}", info);

        List<Map> businessAdvertItemInfos = sqlSessionTemplate.selectList("advertItemServiceDaoImpl.queryAdvertItemsCount", info);
        if (businessAdvertItemInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessAdvertItemInfos.get(0).get("count").toString());
    }


}
