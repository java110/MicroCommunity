package com.java110.common.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.utils.util.DateUtil;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.common.dao.IAppraiseServiceDao;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 评价服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("appraiseServiceDaoImpl")
//@Transactional
public class AppraiseServiceDaoImpl extends BaseServiceDao implements IAppraiseServiceDao {

    private static Logger logger = LoggerFactory.getLogger(AppraiseServiceDaoImpl.class);


    /**
     * 保存评价信息 到 instance
     * @param info   bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveAppraiseInfo(Map info) throws DAOException {
        logger.debug("保存评价信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.insert("appraiseServiceDaoImpl.saveAppraiseInfo",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存评价信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询评价信息（instance）
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getAppraiseInfo(Map info) throws DAOException {
        logger.debug("查询评价信息 入参 info : {}",info);

        List<Map> businessAppraiseInfos = sqlSessionTemplate.selectList("appraiseServiceDaoImpl.getAppraiseInfo",info);

        return businessAppraiseInfos;
    }


    /**
     * 修改评价信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateAppraiseInfo(Map info) throws DAOException {
        logger.debug("修改评价信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.update("appraiseServiceDaoImpl.updateAppraiseInfo",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"修改评价信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }

     /**
     * 查询评价数量
     * @param info 评价信息
     * @return 评价数量
     */
    @Override
    public int queryAppraisesCount(Map info) {
        logger.debug("查询评价数据 入参 info : {}",info);

        List<Map> businessAppraiseInfos = sqlSessionTemplate.selectList("appraiseServiceDaoImpl.queryAppraisesCount", info);
        if (businessAppraiseInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessAppraiseInfos.get(0).get("count").toString());
    }


}
