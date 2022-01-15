package com.java110.common.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.utils.util.DateUtil;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.common.dao.ISmsConfigServiceDao;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 短信配置服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("smsConfigServiceDaoImpl")
//@Transactional
public class SmsConfigServiceDaoImpl extends BaseServiceDao implements ISmsConfigServiceDao {

    private static Logger logger = LoggerFactory.getLogger(SmsConfigServiceDaoImpl.class);





    /**
     * 保存短信配置信息 到 instance
     * @param info   bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveSmsConfigInfo(Map info) throws DAOException {
        logger.debug("保存短信配置信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.insert("smsConfigServiceDaoImpl.saveSmsConfigInfo",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存短信配置信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询短信配置信息（instance）
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getSmsConfigInfo(Map info) throws DAOException {
        logger.debug("查询短信配置信息 入参 info : {}",info);

        List<Map> businessSmsConfigInfos = sqlSessionTemplate.selectList("smsConfigServiceDaoImpl.getSmsConfigInfo",info);

        return businessSmsConfigInfos;
    }


    /**
     * 修改短信配置信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateSmsConfigInfo(Map info) throws DAOException {
        logger.debug("修改短信配置信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.update("smsConfigServiceDaoImpl.updateSmsConfigInfo",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"修改短信配置信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }

     /**
     * 查询短信配置数量
     * @param info 短信配置信息
     * @return 短信配置数量
     */
    @Override
    public int querySmsConfigsCount(Map info) {
        logger.debug("查询短信配置数据 入参 info : {}",info);

        List<Map> businessSmsConfigInfos = sqlSessionTemplate.selectList("smsConfigServiceDaoImpl.querySmsConfigsCount", info);
        if (businessSmsConfigInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessSmsConfigInfos.get(0).get("count").toString());
    }


}
