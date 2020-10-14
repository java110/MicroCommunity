package com.java110.goods.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.utils.util.DateUtil;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.goods.dao.IGroupBuySettingServiceDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 拼团设置服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("groupBuySettingServiceDaoImpl")
//@Transactional
public class GroupBuySettingServiceDaoImpl extends BaseServiceDao implements IGroupBuySettingServiceDao {

    private static Logger logger = LoggerFactory.getLogger(GroupBuySettingServiceDaoImpl.class);





    /**
     * 保存拼团设置信息 到 instance
     * @param info   bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveGroupBuySettingInfo(Map info) throws DAOException {
        logger.debug("保存拼团设置信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.insert("groupBuySettingServiceDaoImpl.saveGroupBuySettingInfo",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存拼团设置信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询拼团设置信息（instance）
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getGroupBuySettingInfo(Map info) throws DAOException {
        logger.debug("查询拼团设置信息 入参 info : {}",info);

        List<Map> businessGroupBuySettingInfos = sqlSessionTemplate.selectList("groupBuySettingServiceDaoImpl.getGroupBuySettingInfo",info);

        return businessGroupBuySettingInfos;
    }


    /**
     * 修改拼团设置信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateGroupBuySettingInfo(Map info) throws DAOException {
        logger.debug("修改拼团设置信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.update("groupBuySettingServiceDaoImpl.updateGroupBuySettingInfo",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"修改拼团设置信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }

     /**
     * 查询拼团设置数量
     * @param info 拼团设置信息
     * @return 拼团设置数量
     */
    @Override
    public int queryGroupBuySettingsCount(Map info) {
        logger.debug("查询拼团设置数据 入参 info : {}",info);

        List<Map> businessGroupBuySettingInfos = sqlSessionTemplate.selectList("groupBuySettingServiceDaoImpl.queryGroupBuySettingsCount", info);
        if (businessGroupBuySettingInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessGroupBuySettingInfos.get(0).get("count").toString());
    }


}
