package com.java110.fee.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.core.log.LoggerFactory;
import com.java110.fee.dao.IFeeAccountDetailServiceDao;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("feeAccountDetailServiceDaoImpl")
public class FeeAccountDetailServiceDaoImpl extends BaseServiceDao implements IFeeAccountDetailServiceDao {

    private static Logger logger = LoggerFactory.getLogger(FeeAccountDetailServiceDaoImpl.class);

    @Override
    public List<Map> getFeeAccountDetailsInfo(Map info) {
        logger.debug("查询费用明细信息 入参 info : {}", info);

        List<Map> businessFeeAccountDetailInfos = sqlSessionTemplate.selectList("feeAccountDetailServiceDaoImpl.getFeeAccountDetailInfo", info);

        return businessFeeAccountDetailInfos;
    }

    @Override
    public int queryFeeAccountDetailsCount(Map info) {
        logger.debug("查询费用明细数据 入参 info : {}",info);

        List<Map> businessFeeAccountDetailInfos = sqlSessionTemplate.selectList("feeAccountDetailServiceDaoImpl.queryFeeAccountDetailsCount", info);
        if (businessFeeAccountDetailInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessFeeAccountDetailInfos.get(0).get("count").toString());
    }

    @Override
    public void saveFeeAccountDetail(Map info) {
        logger.debug("保存明细 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.update("feeAccountDetailServiceDaoImpl.saveFeeAccountDetail", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存明细 数据失败：" + JSONObject.toJSONString(info));
        }
    }

}
