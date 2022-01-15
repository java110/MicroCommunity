package com.java110.store.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.utils.util.DateUtil;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.store.dao.IStoreMsgServiceDao;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 商户消息服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("storeMsgServiceDaoImpl")
//@Transactional
public class StoreMsgServiceDaoImpl extends BaseServiceDao implements IStoreMsgServiceDao {

    private static Logger logger = LoggerFactory.getLogger(StoreMsgServiceDaoImpl.class);





    /**
     * 保存商户消息信息 到 instance
     * @param info   bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveStoreMsgInfo(Map info) throws DAOException {
        logger.debug("保存商户消息信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.insert("storeMsgServiceDaoImpl.saveStoreMsgInfo",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存商户消息信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询商户消息信息（instance）
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getStoreMsgInfo(Map info) throws DAOException {
        logger.debug("查询商户消息信息 入参 info : {}",info);

        List<Map> businessStoreMsgInfos = sqlSessionTemplate.selectList("storeMsgServiceDaoImpl.getStoreMsgInfo",info);

        return businessStoreMsgInfos;
    }


    /**
     * 修改商户消息信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateStoreMsgInfo(Map info) throws DAOException {
        logger.debug("修改商户消息信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.update("storeMsgServiceDaoImpl.updateStoreMsgInfo",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"修改商户消息信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }

     /**
     * 查询商户消息数量
     * @param info 商户消息信息
     * @return 商户消息数量
     */
    @Override
    public int queryStoreMsgsCount(Map info) {
        logger.debug("查询商户消息数据 入参 info : {}",info);

        List<Map> businessStoreMsgInfos = sqlSessionTemplate.selectList("storeMsgServiceDaoImpl.queryStoreMsgsCount", info);
        if (businessStoreMsgInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessStoreMsgInfos.get(0).get("count").toString());
    }


}
