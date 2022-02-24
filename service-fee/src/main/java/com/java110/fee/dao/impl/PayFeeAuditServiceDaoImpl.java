package com.java110.fee.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.utils.util.DateUtil;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.fee.dao.IPayFeeAuditServiceDao;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 缴费审核服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("payFeeAuditServiceDaoImpl")
//@Transactional
public class PayFeeAuditServiceDaoImpl extends BaseServiceDao implements IPayFeeAuditServiceDao {

    private static Logger logger = LoggerFactory.getLogger(PayFeeAuditServiceDaoImpl.class);





    /**
     * 保存缴费审核信息 到 instance
     * @param info   bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void savePayFeeAuditInfo(Map info) throws DAOException {
        logger.debug("保存缴费审核信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.insert("payFeeAuditServiceDaoImpl.savePayFeeAuditInfo",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存缴费审核信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询缴费审核信息（instance）
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getPayFeeAuditInfo(Map info) throws DAOException {
        logger.debug("查询缴费审核信息 入参 info : {}",info);

        List<Map> businessPayFeeAuditInfos = sqlSessionTemplate.selectList("payFeeAuditServiceDaoImpl.getPayFeeAuditInfo",info);

        return businessPayFeeAuditInfos;
    }


    /**
     * 修改缴费审核信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updatePayFeeAuditInfo(Map info) throws DAOException {
        logger.debug("修改缴费审核信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.update("payFeeAuditServiceDaoImpl.updatePayFeeAuditInfo",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"修改缴费审核信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }

     /**
     * 查询缴费审核数量
     * @param info 缴费审核信息
     * @return 缴费审核数量
     */
    @Override
    public int queryPayFeeAuditsCount(Map info) {
        logger.debug("查询缴费审核数据 入参 info : {}",info);

        List<Map> businessPayFeeAuditInfos = sqlSessionTemplate.selectList("payFeeAuditServiceDaoImpl.queryPayFeeAuditsCount", info);
        if (businessPayFeeAuditInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessPayFeeAuditInfos.get(0).get("count").toString());
    }


}
