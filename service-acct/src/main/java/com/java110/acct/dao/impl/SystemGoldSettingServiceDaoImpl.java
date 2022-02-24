package com.java110.acct.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.utils.util.DateUtil;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.acct.dao.ISystemGoldSettingServiceDao;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 金币设置服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("systemGoldSettingServiceDaoImpl")
//@Transactional
public class SystemGoldSettingServiceDaoImpl extends BaseServiceDao implements ISystemGoldSettingServiceDao {

    private static Logger logger = LoggerFactory.getLogger(SystemGoldSettingServiceDaoImpl.class);





    /**
     * 保存金币设置信息 到 instance
     * @param info   bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveSystemGoldSettingInfo(Map info) throws DAOException {
        logger.debug("保存金币设置信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.insert("systemGoldSettingServiceDaoImpl.saveSystemGoldSettingInfo",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存金币设置信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询金币设置信息（instance）
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getSystemGoldSettingInfo(Map info) throws DAOException {
        logger.debug("查询金币设置信息 入参 info : {}",info);

        List<Map> businessSystemGoldSettingInfos = sqlSessionTemplate.selectList("systemGoldSettingServiceDaoImpl.getSystemGoldSettingInfo",info);

        return businessSystemGoldSettingInfos;
    }


    /**
     * 修改金币设置信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateSystemGoldSettingInfo(Map info) throws DAOException {
        logger.debug("修改金币设置信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.update("systemGoldSettingServiceDaoImpl.updateSystemGoldSettingInfo",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"修改金币设置信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }

     /**
     * 查询金币设置数量
     * @param info 金币设置信息
     * @return 金币设置数量
     */
    @Override
    public int querySystemGoldSettingsCount(Map info) {
        logger.debug("查询金币设置数据 入参 info : {}",info);

        List<Map> businessSystemGoldSettingInfos = sqlSessionTemplate.selectList("systemGoldSettingServiceDaoImpl.querySystemGoldSettingsCount", info);
        if (businessSystemGoldSettingInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessSystemGoldSettingInfos.get(0).get("count").toString());
    }


}
