package com.java110.store.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.store.dao.ISmallWeChatServiceDao;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.utils.util.DateUtil;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 小程序管理服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("smallWeChatServiceDaoImpl")
//@Transactional
public class SmallWeChatServiceDaoImpl extends BaseServiceDao implements ISmallWeChatServiceDao {

    private static Logger logger = LoggerFactory.getLogger(SmallWeChatServiceDaoImpl.class);

    /**
     * 小程序管理信息封装
     * @param businessSmallWeChatInfo 小程序管理信息 封装
     * @throws DAOException DAO异常
     */
    @Override
    public void saveBusinessSmallWeChatInfo(Map businessSmallWeChatInfo) throws DAOException {
        businessSmallWeChatInfo.put("month", DateUtil.getCurrentMonth());
        // 查询business_user 数据是否已经存在
        logger.debug("保存小程序管理信息 入参 businessSmallWeChatInfo : {}",businessSmallWeChatInfo);
        int saveFlag = sqlSessionTemplate.insert("smallWeChatServiceDaoImpl.saveBusinessSmallWeChatInfo",businessSmallWeChatInfo);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存小程序管理数据失败："+ JSONObject.toJSONString(businessSmallWeChatInfo));
        }
    }


    /**
     * 查询小程序管理信息
     * @param info bId 信息
     * @return 小程序管理信息
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getBusinessSmallWeChatInfo(Map info) throws DAOException {

        logger.debug("查询小程序管理信息 入参 info : {}",info);

        List<Map> businessSmallWeChatInfos = sqlSessionTemplate.selectList("smallWeChatServiceDaoImpl.getBusinessSmallWeChatInfo",info);

        return businessSmallWeChatInfos;
    }



    /**
     * 保存小程序管理信息 到 instance
     * @param info   bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveSmallWeChatInfoInstance(Map info) throws DAOException {
        logger.debug("保存小程序管理信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.insert("smallWeChatServiceDaoImpl.saveSmallWeChatInfoInstance",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存小程序管理信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询小程序管理信息（instance）
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getSmallWeChatInfo(Map info) throws DAOException {
        logger.debug("查询小程序管理信息 入参 info : {}",info);

        List<Map> businessSmallWeChatInfos = sqlSessionTemplate.selectList("smallWeChatServiceDaoImpl.getSmallWeChatInfo",info);

        return businessSmallWeChatInfos;
    }


    /**
     * 修改小程序管理信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateSmallWeChatInfoInstance(Map info) throws DAOException {
        logger.debug("修改小程序管理信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.update("smallWeChatServiceDaoImpl.updateSmallWeChatInfoInstance",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"修改小程序管理信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }

     /**
     * 查询小程序管理数量
     * @param info 小程序管理信息
     * @return 小程序管理数量
     */
    @Override
    public int querySmallWeChatsCount(Map info) {
        logger.debug("查询小程序管理数据 入参 info : {}",info);

        List<Map> businessSmallWeChatInfos = sqlSessionTemplate.selectList("smallWeChatServiceDaoImpl.querySmallWeChatsCount", info);
        if (businessSmallWeChatInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessSmallWeChatInfos.get(0).get("count").toString());
    }


}
