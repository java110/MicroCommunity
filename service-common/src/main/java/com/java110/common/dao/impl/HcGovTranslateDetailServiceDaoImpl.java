package com.java110.common.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.utils.util.DateUtil;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.common.dao.IHcGovTranslateDetailServiceDao;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 信息分类服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("hcGovTranslateDetailServiceDaoImpl")
//@Transactional
public class HcGovTranslateDetailServiceDaoImpl extends BaseServiceDao implements IHcGovTranslateDetailServiceDao {

    private static Logger logger = LoggerFactory.getLogger(HcGovTranslateDetailServiceDaoImpl.class);





    /**
     * 保存信息分类信息 到 instance
     * @param info   bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveHcGovTranslateDetailInfo(Map info) throws DAOException {
        logger.debug("保存信息分类信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.insert("hcGovTranslateDetailServiceDaoImpl.saveHcGovTranslateDetailInfo",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存信息分类信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询信息分类信息（instance）
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getHcGovTranslateDetailInfo(Map info) throws DAOException {
        logger.debug("查询信息分类信息 入参 info : {}",info);

        List<Map> businessHcGovTranslateDetailInfos = sqlSessionTemplate.selectList("hcGovTranslateDetailServiceDaoImpl.getHcGovTranslateDetailInfo",info);

        return businessHcGovTranslateDetailInfos;
    }


    /**
     * 修改信息分类信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateHcGovTranslateDetailInfo(Map info) throws DAOException {
        logger.debug("修改信息分类信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.update("hcGovTranslateDetailServiceDaoImpl.updateHcGovTranslateDetailInfo",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"修改信息分类信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }

     /**
     * 查询信息分类数量
     * @param info 信息分类信息
     * @return 信息分类数量
     */
    @Override
    public int queryHcGovTranslateDetailsCount(Map info) {
        logger.debug("查询信息分类数据 入参 info : {}",info);

        List<Map> businessHcGovTranslateDetailInfos = sqlSessionTemplate.selectList("hcGovTranslateDetailServiceDaoImpl.queryHcGovTranslateDetailsCount", info);
        if (businessHcGovTranslateDetailInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessHcGovTranslateDetailInfos.get(0).get("count").toString());
    }


}
