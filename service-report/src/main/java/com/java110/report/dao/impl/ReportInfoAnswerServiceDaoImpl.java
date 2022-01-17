package com.java110.report.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.utils.util.DateUtil;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.report.dao.IReportInfoAnswerServiceDao;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 批量操作日志详情服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("reportInfoAnswerServiceDaoImpl")
//@Transactional
public class ReportInfoAnswerServiceDaoImpl extends BaseServiceDao implements IReportInfoAnswerServiceDao {

    private static Logger logger = LoggerFactory.getLogger(ReportInfoAnswerServiceDaoImpl.class);





    /**
     * 保存批量操作日志详情信息 到 instance
     * @param info   bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveReportInfoAnswerInfo(Map info) throws DAOException {
        logger.debug("保存批量操作日志详情信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.insert("reportInfoAnswerServiceDaoImpl.saveReportInfoAnswerInfo",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存批量操作日志详情信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询批量操作日志详情信息（instance）
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getReportInfoAnswerInfo(Map info) throws DAOException {
        logger.debug("查询批量操作日志详情信息 入参 info : {}",info);

        List<Map> businessReportInfoAnswerInfos = sqlSessionTemplate.selectList("reportInfoAnswerServiceDaoImpl.getReportInfoAnswerInfo",info);

        return businessReportInfoAnswerInfos;
    }


    /**
     * 修改批量操作日志详情信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateReportInfoAnswerInfo(Map info) throws DAOException {
        logger.debug("修改批量操作日志详情信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.update("reportInfoAnswerServiceDaoImpl.updateReportInfoAnswerInfo",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"修改批量操作日志详情信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }

     /**
     * 查询批量操作日志详情数量
     * @param info 批量操作日志详情信息
     * @return 批量操作日志详情数量
     */
    @Override
    public int queryReportInfoAnswersCount(Map info) {
        logger.debug("查询批量操作日志详情数据 入参 info : {}",info);

        List<Map> businessReportInfoAnswerInfos = sqlSessionTemplate.selectList("reportInfoAnswerServiceDaoImpl.queryReportInfoAnswersCount", info);
        if (businessReportInfoAnswerInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessReportInfoAnswerInfos.get(0).get("count").toString());
    }


}
