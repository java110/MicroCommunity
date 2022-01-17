package com.java110.community.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.utils.util.DateUtil;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.community.dao.ICommunitySettingServiceDao;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 小区相关设置服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("communitySettingServiceDaoImpl")
//@Transactional
public class CommunitySettingServiceDaoImpl extends BaseServiceDao implements ICommunitySettingServiceDao {

    private static Logger logger = LoggerFactory.getLogger(CommunitySettingServiceDaoImpl.class);





    /**
     * 保存小区相关设置信息 到 instance
     * @param info   bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveCommunitySettingInfo(Map info) throws DAOException {
        logger.debug("保存小区相关设置信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.insert("communitySettingServiceDaoImpl.saveCommunitySettingInfo",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存小区相关设置信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询小区相关设置信息（instance）
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getCommunitySettingInfo(Map info) throws DAOException {
        logger.debug("查询小区相关设置信息 入参 info : {}",info);

        List<Map> businessCommunitySettingInfos = sqlSessionTemplate.selectList("communitySettingServiceDaoImpl.getCommunitySettingInfo",info);

        return businessCommunitySettingInfos;
    }


    /**
     * 修改小区相关设置信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateCommunitySettingInfo(Map info) throws DAOException {
        logger.debug("修改小区相关设置信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.update("communitySettingServiceDaoImpl.updateCommunitySettingInfo",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"修改小区相关设置信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }

     /**
     * 查询小区相关设置数量
     * @param info 小区相关设置信息
     * @return 小区相关设置数量
     */
    @Override
    public int queryCommunitySettingsCount(Map info) {
        logger.debug("查询小区相关设置数据 入参 info : {}",info);

        List<Map> businessCommunitySettingInfos = sqlSessionTemplate.selectList("communitySettingServiceDaoImpl.queryCommunitySettingsCount", info);
        if (businessCommunitySettingInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessCommunitySettingInfos.get(0).get("count").toString());
    }


}
