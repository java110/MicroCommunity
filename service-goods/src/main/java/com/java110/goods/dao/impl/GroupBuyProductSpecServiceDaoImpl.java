package com.java110.goods.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.utils.util.DateUtil;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.goods.dao.IGroupBuyProductSpecServiceDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 拼团产品规格服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("groupBuyProductSpecServiceDaoImpl")
//@Transactional
public class GroupBuyProductSpecServiceDaoImpl extends BaseServiceDao implements IGroupBuyProductSpecServiceDao {

    private static Logger logger = LoggerFactory.getLogger(GroupBuyProductSpecServiceDaoImpl.class);





    /**
     * 保存拼团产品规格信息 到 instance
     * @param info   bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveGroupBuyProductSpecInfo(Map info) throws DAOException {
        logger.debug("保存拼团产品规格信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.insert("groupBuyProductSpecServiceDaoImpl.saveGroupBuyProductSpecInfo",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存拼团产品规格信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询拼团产品规格信息（instance）
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getGroupBuyProductSpecInfo(Map info) throws DAOException {
        logger.debug("查询拼团产品规格信息 入参 info : {}",info);

        List<Map> businessGroupBuyProductSpecInfos = sqlSessionTemplate.selectList("groupBuyProductSpecServiceDaoImpl.getGroupBuyProductSpecInfo",info);

        return businessGroupBuyProductSpecInfos;
    }


    /**
     * 修改拼团产品规格信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateGroupBuyProductSpecInfo(Map info) throws DAOException {
        logger.debug("修改拼团产品规格信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.update("groupBuyProductSpecServiceDaoImpl.updateGroupBuyProductSpecInfo",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"修改拼团产品规格信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }

     /**
     * 查询拼团产品规格数量
     * @param info 拼团产品规格信息
     * @return 拼团产品规格数量
     */
    @Override
    public int queryGroupBuyProductSpecsCount(Map info) {
        logger.debug("查询拼团产品规格数据 入参 info : {}",info);

        List<Map> businessGroupBuyProductSpecInfos = sqlSessionTemplate.selectList("groupBuyProductSpecServiceDaoImpl.queryGroupBuyProductSpecsCount", info);
        if (businessGroupBuyProductSpecInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessGroupBuyProductSpecInfos.get(0).get("count").toString());
    }


}
