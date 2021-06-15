package com.java110.goods.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.utils.util.DateUtil;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.goods.dao.IGroupBuyServiceDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 拼团购买服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("groupBuyServiceDaoImpl")
//@Transactional
public class GroupBuyServiceDaoImpl extends BaseServiceDao implements IGroupBuyServiceDao {

    private static Logger logger = LoggerFactory.getLogger(GroupBuyServiceDaoImpl.class);





    /**
     * 保存拼团购买信息 到 instance
     * @param info   bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveGroupBuyInfo(Map info) throws DAOException {
        logger.debug("保存拼团购买信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.insert("groupBuyServiceDaoImpl.saveGroupBuyInfo",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存拼团购买信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询拼团购买信息（instance）
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getGroupBuyInfo(Map info) throws DAOException {
        logger.debug("查询拼团购买信息 入参 info : {}",info);

        List<Map> businessGroupBuyInfos = sqlSessionTemplate.selectList("groupBuyServiceDaoImpl.getGroupBuyInfo",info);

        return businessGroupBuyInfos;
    }


    /**
     * 修改拼团购买信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateGroupBuyInfo(Map info) throws DAOException {
        logger.debug("修改拼团购买信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.update("groupBuyServiceDaoImpl.updateGroupBuyInfo",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"修改拼团购买信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }

     /**
     * 查询拼团购买数量
     * @param info 拼团购买信息
     * @return 拼团购买数量
     */
    @Override
    public int queryGroupBuysCount(Map info) {
        logger.debug("查询拼团购买数据 入参 info : {}",info);

        List<Map> businessGroupBuyInfos = sqlSessionTemplate.selectList("groupBuyServiceDaoImpl.queryGroupBuysCount", info);
        if (businessGroupBuyInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessGroupBuyInfos.get(0).get("count").toString());
    }


}
