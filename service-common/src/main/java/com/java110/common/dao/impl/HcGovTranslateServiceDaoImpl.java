package com.java110.common.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.utils.util.DateUtil;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.common.dao.IHcGovTranslateServiceDao;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 社区政务同步服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("hcGovTranslateServiceDaoImpl")
//@Transactional
public class HcGovTranslateServiceDaoImpl extends BaseServiceDao implements IHcGovTranslateServiceDao {

    private static Logger logger = LoggerFactory.getLogger(HcGovTranslateServiceDaoImpl.class);





    /**
     * 保存社区政务同步信息 到 instance
     * @param info   bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveHcGovTranslateInfo(Map info) throws DAOException {
        logger.debug("保存社区政务同步信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.insert("hcGovTranslateServiceDaoImpl.saveHcGovTranslateInfo",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存社区政务同步信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询社区政务同步信息（instance）
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getHcGovTranslateInfo(Map info) throws DAOException {
        logger.debug("查询社区政务同步信息 入参 info : {}",info);

        List<Map> businessHcGovTranslateInfos = sqlSessionTemplate.selectList("hcGovTranslateServiceDaoImpl.getHcGovTranslateInfo",info);

        return businessHcGovTranslateInfos;
    }


    /**
     * 修改社区政务同步信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateHcGovTranslateInfo(Map info) throws DAOException {
        logger.debug("修改社区政务同步信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.update("hcGovTranslateServiceDaoImpl.updateHcGovTranslateInfo",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"修改社区政务同步信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }

     /**
     * 查询社区政务同步数量
     * @param info 社区政务同步信息
     * @return 社区政务同步数量
     */
    @Override
    public int queryHcGovTranslatesCount(Map info) {
        logger.debug("查询社区政务同步数据 入参 info : {}",info);

        List<Map> businessHcGovTranslateInfos = sqlSessionTemplate.selectList("hcGovTranslateServiceDaoImpl.queryHcGovTranslatesCount", info);
        if (businessHcGovTranslateInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessHcGovTranslateInfos.get(0).get("count").toString());
    }


}
