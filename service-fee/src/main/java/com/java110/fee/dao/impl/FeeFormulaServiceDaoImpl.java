package com.java110.fee.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.utils.util.DateUtil;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.fee.dao.IFeeFormulaServiceDao;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 费用公式服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("feeFormulaServiceDaoImpl")
//@Transactional
public class FeeFormulaServiceDaoImpl extends BaseServiceDao implements IFeeFormulaServiceDao {

    private static Logger logger = LoggerFactory.getLogger(FeeFormulaServiceDaoImpl.class);





    /**
     * 保存费用公式信息 到 instance
     * @param info   bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveFeeFormulaInfo(Map info) throws DAOException {
        logger.debug("保存费用公式信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.insert("feeFormulaServiceDaoImpl.saveFeeFormulaInfo",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存费用公式信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询费用公式信息（instance）
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getFeeFormulaInfo(Map info) throws DAOException {
        logger.debug("查询费用公式信息 入参 info : {}",info);

        List<Map> businessFeeFormulaInfos = sqlSessionTemplate.selectList("feeFormulaServiceDaoImpl.getFeeFormulaInfo",info);

        return businessFeeFormulaInfos;
    }


    /**
     * 修改费用公式信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateFeeFormulaInfo(Map info) throws DAOException {
        logger.debug("修改费用公式信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.update("feeFormulaServiceDaoImpl.updateFeeFormulaInfo",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"修改费用公式信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }

     /**
     * 查询费用公式数量
     * @param info 费用公式信息
     * @return 费用公式数量
     */
    @Override
    public int queryFeeFormulasCount(Map info) {
        logger.debug("查询费用公式数据 入参 info : {}",info);

        List<Map> businessFeeFormulaInfos = sqlSessionTemplate.selectList("feeFormulaServiceDaoImpl.queryFeeFormulasCount", info);
        if (businessFeeFormulaInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessFeeFormulaInfos.get(0).get("count").toString());
    }


}
