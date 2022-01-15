package com.java110.store.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.utils.util.DateUtil;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.store.dao.IWechatSmsTemplateServiceDao;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 微信消息模板服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("wechatSmsTemplateServiceDaoImpl")
//@Transactional
public class WechatSmsTemplateServiceDaoImpl extends BaseServiceDao implements IWechatSmsTemplateServiceDao {

    private static Logger logger = LoggerFactory.getLogger(WechatSmsTemplateServiceDaoImpl.class);





    /**
     * 保存微信消息模板信息 到 instance
     * @param info   bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveWechatSmsTemplateInfo(Map info) throws DAOException {
        logger.debug("保存微信消息模板信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.insert("wechatSmsTemplateServiceDaoImpl.saveWechatSmsTemplateInfo",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存微信消息模板信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询微信消息模板信息（instance）
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getWechatSmsTemplateInfo(Map info) throws DAOException {
        logger.debug("查询微信消息模板信息 入参 info : {}",info);

        List<Map> businessWechatSmsTemplateInfos = sqlSessionTemplate.selectList("wechatSmsTemplateServiceDaoImpl.getWechatSmsTemplateInfo",info);

        return businessWechatSmsTemplateInfos;
    }


    /**
     * 修改微信消息模板信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateWechatSmsTemplateInfo(Map info) throws DAOException {
        logger.debug("修改微信消息模板信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.update("wechatSmsTemplateServiceDaoImpl.updateWechatSmsTemplateInfo",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"修改微信消息模板信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }

     /**
     * 查询微信消息模板数量
     * @param info 微信消息模板信息
     * @return 微信消息模板数量
     */
    @Override
    public int queryWechatSmsTemplatesCount(Map info) {
        logger.debug("查询微信消息模板数据 入参 info : {}",info);

        List<Map> businessWechatSmsTemplateInfos = sqlSessionTemplate.selectList("wechatSmsTemplateServiceDaoImpl.queryWechatSmsTemplatesCount", info);
        if (businessWechatSmsTemplateInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessWechatSmsTemplateInfos.get(0).get("count").toString());
    }


}
