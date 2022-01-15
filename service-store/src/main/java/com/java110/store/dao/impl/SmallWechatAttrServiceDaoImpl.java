package com.java110.store.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.utils.util.DateUtil;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.store.dao.ISmallWechatAttrServiceDao;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 微信属性服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("smallWechatAttrServiceDaoImpl")
//@Transactional
public class SmallWechatAttrServiceDaoImpl extends BaseServiceDao implements ISmallWechatAttrServiceDao {

    private static Logger logger = LoggerFactory.getLogger(SmallWechatAttrServiceDaoImpl.class);

    /**
     * 微信属性信息封装
     * @param businessSmallWechatAttrInfo 微信属性信息 封装
     * @throws DAOException DAO异常
     */
    @Override
    public void saveBusinessSmallWechatAttrInfo(Map businessSmallWechatAttrInfo) throws DAOException {
        businessSmallWechatAttrInfo.put("month", DateUtil.getCurrentMonth());
        // 查询business_user 数据是否已经存在
        logger.debug("保存微信属性信息 入参 businessSmallWechatAttrInfo : {}",businessSmallWechatAttrInfo);
        int saveFlag = sqlSessionTemplate.insert("smallWechatAttrServiceDaoImpl.saveBusinessSmallWechatAttrInfo",businessSmallWechatAttrInfo);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存微信属性数据失败："+ JSONObject.toJSONString(businessSmallWechatAttrInfo));
        }
    }


    /**
     * 查询微信属性信息
     * @param info bId 信息
     * @return 微信属性信息
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getBusinessSmallWechatAttrInfo(Map info) throws DAOException {

        logger.debug("查询微信属性信息 入参 info : {}",info);

        List<Map> businessSmallWechatAttrInfos = sqlSessionTemplate.selectList("smallWechatAttrServiceDaoImpl.getBusinessSmallWechatAttrInfo",info);

        return businessSmallWechatAttrInfos;
    }



    /**
     * 保存微信属性信息 到 instance
     * @param info   bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveSmallWechatAttrInfoInstance(Map info) throws DAOException {
        logger.debug("保存微信属性信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.insert("smallWechatAttrServiceDaoImpl.saveSmallWechatAttrInfoInstance",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存微信属性信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询微信属性信息（instance）
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getSmallWechatAttrInfo(Map info) throws DAOException {
        logger.debug("查询微信属性信息 入参 info : {}",info);

        List<Map> businessSmallWechatAttrInfos = sqlSessionTemplate.selectList("smallWechatAttrServiceDaoImpl.getSmallWechatAttrInfo",info);

        return businessSmallWechatAttrInfos;
    }


    /**
     * 修改微信属性信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateSmallWechatAttrInfoInstance(Map info) throws DAOException {
        logger.debug("修改微信属性信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.update("smallWechatAttrServiceDaoImpl.updateSmallWechatAttrInfoInstance",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"修改微信属性信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }

     /**
     * 查询微信属性数量
     * @param info 微信属性信息
     * @return 微信属性数量
     */
    @Override
    public int querySmallWechatAttrsCount(Map info) {
        logger.debug("查询微信属性数据 入参 info : {}",info);

        List<Map> businessSmallWechatAttrInfos = sqlSessionTemplate.selectList("smallWechatAttrServiceDaoImpl.querySmallWechatAttrsCount", info);
        if (businessSmallWechatAttrInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessSmallWechatAttrInfos.get(0).get("count").toString());
    }


}
