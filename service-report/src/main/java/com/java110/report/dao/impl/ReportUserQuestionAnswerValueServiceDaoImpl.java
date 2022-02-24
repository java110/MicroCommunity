package com.java110.report.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.report.dao.IReportOweFeeServiceDao;
import com.java110.report.dao.IReportUserQuestionAnswerValueServiceDao;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 欠费统计服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("reportUserQuestionAnswerValueServiceDaoImpl")
//@Transactional
public class ReportUserQuestionAnswerValueServiceDaoImpl extends BaseServiceDao implements IReportUserQuestionAnswerValueServiceDao {

    private static Logger logger = LoggerFactory.getLogger(ReportUserQuestionAnswerValueServiceDaoImpl.class);






    /**
     * 查询欠费统计信息（instance）
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getUserQuestionAnswerValueInfo(Map info) throws DAOException {
        logger.debug("查询欠费统计信息 入参 info : {}",info);

        List<Map> businessReportOweFeeInfos = sqlSessionTemplate.selectList("reportUserQuestionAnswerValueServiceDaoImpl.getUserQuestionAnswerValueInfo",info);

        return businessReportOweFeeInfos;
    }



     /**
     * 查询欠费统计数量
     * @param info 欠费统计信息
     * @return 欠费统计数量
     */
    @Override
    public int queryUserQuestionAnswerValuesCount(Map info) {
        logger.debug("查询欠费统计数据 入参 info : {}",info);

        List<Map> businessReportOweFeeInfos = sqlSessionTemplate.selectList("reportUserQuestionAnswerValueServiceDaoImpl.queryUserQuestionAnswerValuesCount", info);
        if (businessReportOweFeeInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessReportOweFeeInfos.get(0).get("count").toString());
    }




}
