package com.java110.store.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.utils.util.DateUtil;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.store.dao.IWechatMenuServiceDao;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 公众号菜单服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("wechatMenuServiceDaoImpl")
//@Transactional
public class WechatMenuServiceDaoImpl extends BaseServiceDao implements IWechatMenuServiceDao {

    private static Logger logger = LoggerFactory.getLogger(WechatMenuServiceDaoImpl.class);

    /**
     * 公众号菜单信息封装
     * @param businessWechatMenuInfo 公众号菜单信息 封装
     * @throws DAOException DAO异常
     */
    @Override
    public void saveBusinessWechatMenuInfo(Map businessWechatMenuInfo) throws DAOException {
        businessWechatMenuInfo.put("month", DateUtil.getCurrentMonth());
        // 查询business_user 数据是否已经存在
        logger.debug("保存公众号菜单信息 入参 businessWechatMenuInfo : {}",businessWechatMenuInfo);
        int saveFlag = sqlSessionTemplate.insert("wechatMenuServiceDaoImpl.saveBusinessWechatMenuInfo",businessWechatMenuInfo);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存公众号菜单数据失败："+ JSONObject.toJSONString(businessWechatMenuInfo));
        }
    }


    /**
     * 查询公众号菜单信息
     * @param info bId 信息
     * @return 公众号菜单信息
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getBusinessWechatMenuInfo(Map info) throws DAOException {

        logger.debug("查询公众号菜单信息 入参 info : {}",info);

        List<Map> businessWechatMenuInfos = sqlSessionTemplate.selectList("wechatMenuServiceDaoImpl.getBusinessWechatMenuInfo",info);

        return businessWechatMenuInfos;
    }



    /**
     * 保存公众号菜单信息 到 instance
     * @param info   bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveWechatMenuInfoInstance(Map info) throws DAOException {
        logger.debug("保存公众号菜单信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.insert("wechatMenuServiceDaoImpl.saveWechatMenuInfoInstance",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存公众号菜单信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询公众号菜单信息（instance）
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getWechatMenuInfo(Map info) throws DAOException {
        logger.debug("查询公众号菜单信息 入参 info : {}",info);

        List<Map> businessWechatMenuInfos = sqlSessionTemplate.selectList("wechatMenuServiceDaoImpl.getWechatMenuInfo",info);

        return businessWechatMenuInfos;
    }


    /**
     * 修改公众号菜单信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateWechatMenuInfoInstance(Map info) throws DAOException {
        logger.debug("修改公众号菜单信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.update("wechatMenuServiceDaoImpl.updateWechatMenuInfoInstance",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"修改公众号菜单信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }

     /**
     * 查询公众号菜单数量
     * @param info 公众号菜单信息
     * @return 公众号菜单数量
     */
    @Override
    public int queryWechatMenusCount(Map info) {
        logger.debug("查询公众号菜单数据 入参 info : {}",info);

        List<Map> businessWechatMenuInfos = sqlSessionTemplate.selectList("wechatMenuServiceDaoImpl.queryWechatMenusCount", info);
        if (businessWechatMenuInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessWechatMenuInfos.get(0).get("count").toString());
    }


}
