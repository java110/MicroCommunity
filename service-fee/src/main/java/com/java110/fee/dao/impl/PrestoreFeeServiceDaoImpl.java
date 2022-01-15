package com.java110.fee.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.utils.util.DateUtil;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.fee.dao.IPrestoreFeeServiceDao;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 预存费用服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("prestoreFeeServiceDaoImpl")
//@Transactional
public class PrestoreFeeServiceDaoImpl extends BaseServiceDao implements IPrestoreFeeServiceDao {

    private static Logger logger = LoggerFactory.getLogger(PrestoreFeeServiceDaoImpl.class);





    /**
     * 保存预存费用信息 到 instance
     * @param info   bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void savePrestoreFeeInfo(Map info) throws DAOException {
        logger.debug("保存预存费用信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.insert("prestoreFeeServiceDaoImpl.savePrestoreFeeInfo",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存预存费用信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询预存费用信息（instance）
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getPrestoreFeeInfo(Map info) throws DAOException {
        logger.debug("查询预存费用信息 入参 info : {}",info);

        List<Map> businessPrestoreFeeInfos = sqlSessionTemplate.selectList("prestoreFeeServiceDaoImpl.getPrestoreFeeInfo",info);

        return businessPrestoreFeeInfos;
    }


    /**
     * 修改预存费用信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updatePrestoreFeeInfo(Map info) throws DAOException {
        logger.debug("修改预存费用信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.update("prestoreFeeServiceDaoImpl.updatePrestoreFeeInfo",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"修改预存费用信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }

     /**
     * 查询预存费用数量
     * @param info 预存费用信息
     * @return 预存费用数量
     */
    @Override
    public int queryPrestoreFeesCount(Map info) {
        logger.debug("查询预存费用数据 入参 info : {}",info);

        List<Map> businessPrestoreFeeInfos = sqlSessionTemplate.selectList("prestoreFeeServiceDaoImpl.queryPrestoreFeesCount", info);
        if (businessPrestoreFeeInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessPrestoreFeeInfos.get(0).get("count").toString());
    }


}
