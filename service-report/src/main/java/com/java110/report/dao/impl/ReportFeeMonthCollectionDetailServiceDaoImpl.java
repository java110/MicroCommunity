package com.java110.report.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.utils.util.DateUtil;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.report.dao.IReportFeeMonthCollectionDetailServiceDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 月缴费表服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("reportFeeMonthCollectionDetailServiceDaoImpl")
//@Transactional
public class ReportFeeMonthCollectionDetailServiceDaoImpl extends BaseServiceDao implements IReportFeeMonthCollectionDetailServiceDao {

    private static Logger logger = LoggerFactory.getLogger(ReportFeeMonthCollectionDetailServiceDaoImpl.class);





    /**
     * 保存月缴费表信息 到 instance
     * @param info   bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveReportFeeMonthCollectionDetailInfo(Map info) throws DAOException {
        logger.debug("保存月缴费表信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.insert("reportFeeMonthCollectionDetailServiceDaoImpl.saveReportFeeMonthCollectionDetailInfo",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存月缴费表信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询月缴费表信息（instance）
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getReportFeeMonthCollectionDetailInfo(Map info) throws DAOException {
        logger.debug("查询月缴费表信息 入参 info : {}",info);

        List<Map> businessReportFeeMonthCollectionDetailInfos = sqlSessionTemplate.selectList("reportFeeMonthCollectionDetailServiceDaoImpl.getReportFeeMonthCollectionDetailInfo",info);

        return businessReportFeeMonthCollectionDetailInfos;
    }


    /**
     * 修改月缴费表信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateReportFeeMonthCollectionDetailInfo(Map info) throws DAOException {
        logger.debug("修改月缴费表信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.update("reportFeeMonthCollectionDetailServiceDaoImpl.updateReportFeeMonthCollectionDetailInfo",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"修改月缴费表信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }

     /**
     * 查询月缴费表数量
     * @param info 月缴费表信息
     * @return 月缴费表数量
     */
    @Override
    public int queryReportFeeMonthCollectionDetailsCount(Map info) {
        logger.debug("查询月缴费表数据 入参 info : {}",info);

        List<Map> businessReportFeeMonthCollectionDetailInfos = sqlSessionTemplate.selectList("reportFeeMonthCollectionDetailServiceDaoImpl.queryReportFeeMonthCollectionDetailsCount", info);
        if (businessReportFeeMonthCollectionDetailInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessReportFeeMonthCollectionDetailInfos.get(0).get("count").toString());
    }


}
