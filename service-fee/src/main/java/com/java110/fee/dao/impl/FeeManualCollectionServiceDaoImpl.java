package com.java110.fee.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.utils.util.DateUtil;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.fee.dao.IFeeManualCollectionServiceDao;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 人工托收服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("feeManualCollectionServiceDaoImpl")
//@Transactional
public class FeeManualCollectionServiceDaoImpl extends BaseServiceDao implements IFeeManualCollectionServiceDao {

    private static Logger logger = LoggerFactory.getLogger(FeeManualCollectionServiceDaoImpl.class);





    /**
     * 保存人工托收信息 到 instance
     * @param info   bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveFeeManualCollectionInfo(Map info) throws DAOException {
        logger.debug("保存人工托收信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.insert("feeManualCollectionServiceDaoImpl.saveFeeManualCollectionInfo",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存人工托收信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询人工托收信息（instance）
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getFeeManualCollectionInfo(Map info) throws DAOException {
        logger.debug("查询人工托收信息 入参 info : {}",info);

        List<Map> businessFeeManualCollectionInfos = sqlSessionTemplate.selectList("feeManualCollectionServiceDaoImpl.getFeeManualCollectionInfo",info);

        return businessFeeManualCollectionInfos;
    }


    /**
     * 修改人工托收信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateFeeManualCollectionInfo(Map info) throws DAOException {
        logger.debug("修改人工托收信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.update("feeManualCollectionServiceDaoImpl.updateFeeManualCollectionInfo",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"修改人工托收信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }

     /**
     * 查询人工托收数量
     * @param info 人工托收信息
     * @return 人工托收数量
     */
    @Override
    public int queryFeeManualCollectionsCount(Map info) {
        logger.debug("查询人工托收数据 入参 info : {}",info);

        List<Map> businessFeeManualCollectionInfos = sqlSessionTemplate.selectList("feeManualCollectionServiceDaoImpl.queryFeeManualCollectionsCount", info);
        if (businessFeeManualCollectionInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessFeeManualCollectionInfos.get(0).get("count").toString());
    }


}
