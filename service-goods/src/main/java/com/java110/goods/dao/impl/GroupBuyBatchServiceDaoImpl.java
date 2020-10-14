package com.java110.goods.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.utils.util.DateUtil;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.goods.dao.IGroupBuyBatchServiceDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 拼团批次服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("groupBuyBatchServiceDaoImpl")
//@Transactional
public class GroupBuyBatchServiceDaoImpl extends BaseServiceDao implements IGroupBuyBatchServiceDao {

    private static Logger logger = LoggerFactory.getLogger(GroupBuyBatchServiceDaoImpl.class);





    /**
     * 保存拼团批次信息 到 instance
     * @param info   bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveGroupBuyBatchInfo(Map info) throws DAOException {
        logger.debug("保存拼团批次信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.insert("groupBuyBatchServiceDaoImpl.saveGroupBuyBatchInfo",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存拼团批次信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询拼团批次信息（instance）
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getGroupBuyBatchInfo(Map info) throws DAOException {
        logger.debug("查询拼团批次信息 入参 info : {}",info);

        List<Map> businessGroupBuyBatchInfos = sqlSessionTemplate.selectList("groupBuyBatchServiceDaoImpl.getGroupBuyBatchInfo",info);

        return businessGroupBuyBatchInfos;
    }


    /**
     * 修改拼团批次信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateGroupBuyBatchInfo(Map info) throws DAOException {
        logger.debug("修改拼团批次信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.update("groupBuyBatchServiceDaoImpl.updateGroupBuyBatchInfo",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"修改拼团批次信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }

     /**
     * 查询拼团批次数量
     * @param info 拼团批次信息
     * @return 拼团批次数量
     */
    @Override
    public int queryGroupBuyBatchsCount(Map info) {
        logger.debug("查询拼团批次数据 入参 info : {}",info);

        List<Map> businessGroupBuyBatchInfos = sqlSessionTemplate.selectList("groupBuyBatchServiceDaoImpl.queryGroupBuyBatchsCount", info);
        if (businessGroupBuyBatchInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessGroupBuyBatchInfos.get(0).get("count").toString());
    }


}
