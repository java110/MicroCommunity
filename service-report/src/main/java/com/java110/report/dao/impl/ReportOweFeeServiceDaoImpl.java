package com.java110.report.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.utils.util.DateUtil;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.report.dao.IReportOweFeeServiceDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 欠费统计服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("reportOweFeeServiceDaoImpl")
//@Transactional
public class ReportOweFeeServiceDaoImpl extends BaseServiceDao implements IReportOweFeeServiceDao {

    private static Logger logger = LoggerFactory.getLogger(ReportOweFeeServiceDaoImpl.class);





    /**
     * 保存欠费统计信息 到 instance
     * @param info   bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveReportOweFeeInfo(Map info) throws DAOException {
        logger.debug("保存欠费统计信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.insert("reportOweFeeServiceDaoImpl.saveReportOweFeeInfo",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存欠费统计信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询欠费统计信息（instance）
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getReportOweFeeInfo(Map info) throws DAOException {
        logger.debug("查询欠费统计信息 入参 info : {}",info);

        List<Map> businessReportOweFeeInfos = sqlSessionTemplate.selectList("reportOweFeeServiceDaoImpl.getReportOweFeeInfo",info);

        return businessReportOweFeeInfos;
    }


    /**
     * 修改欠费统计信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateReportOweFeeInfo(Map info) throws DAOException {
        logger.debug("修改欠费统计信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.update("reportOweFeeServiceDaoImpl.updateReportOweFeeInfo",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"修改欠费统计信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }

     /**
     * 查询欠费统计数量
     * @param info 欠费统计信息
     * @return 欠费统计数量
     */
    @Override
    public int queryReportOweFeesCount(Map info) {
        logger.debug("查询欠费统计数据 入参 info : {}",info);

        List<Map> businessReportOweFeeInfos = sqlSessionTemplate.selectList("reportOweFeeServiceDaoImpl.queryReportOweFeesCount", info);
        if (businessReportOweFeeInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessReportOweFeeInfos.get(0).get("count").toString());
    }


}
