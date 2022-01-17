package com.java110.report.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.utils.util.DateUtil;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.report.dao.IReportFeeYearCollectionDetailServiceDao;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 费用年收费明细服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("reportFeeYearCollectionDetailServiceDaoImpl")
//@Transactional
public class ReportFeeYearCollectionDetailServiceDaoImpl extends BaseServiceDao implements IReportFeeYearCollectionDetailServiceDao {

    private static Logger logger = LoggerFactory.getLogger(ReportFeeYearCollectionDetailServiceDaoImpl.class);





    /**
     * 保存费用年收费明细信息 到 instance
     * @param info   bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveReportFeeYearCollectionDetailInfo(Map info) throws DAOException {
        logger.debug("保存费用年收费明细信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.insert("reportFeeYearCollectionDetailServiceDaoImpl.saveReportFeeYearCollectionDetailInfo",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存费用年收费明细信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询费用年收费明细信息（instance）
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getReportFeeYearCollectionDetailInfo(Map info) throws DAOException {
        logger.debug("查询费用年收费明细信息 入参 info : {}",info);

        List<Map> businessReportFeeYearCollectionDetailInfos = sqlSessionTemplate.selectList("reportFeeYearCollectionDetailServiceDaoImpl.getReportFeeYearCollectionDetailInfo",info);

        return businessReportFeeYearCollectionDetailInfos;
    }


    /**
     * 修改费用年收费明细信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateReportFeeYearCollectionDetailInfo(Map info) throws DAOException {
        logger.debug("修改费用年收费明细信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.update("reportFeeYearCollectionDetailServiceDaoImpl.updateReportFeeYearCollectionDetailInfo",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"修改费用年收费明细信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }

     /**
     * 查询费用年收费明细数量
     * @param info 费用年收费明细信息
     * @return 费用年收费明细数量
     */
    @Override
    public int queryReportFeeYearCollectionDetailsCount(Map info) {
        logger.debug("查询费用年收费明细数据 入参 info : {}",info);

        List<Map> businessReportFeeYearCollectionDetailInfos = sqlSessionTemplate.selectList("reportFeeYearCollectionDetailServiceDaoImpl.queryReportFeeYearCollectionDetailsCount", info);
        if (businessReportFeeYearCollectionDetailInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessReportFeeYearCollectionDetailInfos.get(0).get("count").toString());
    }


}
