package com.java110.report.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.utils.util.DateUtil;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.report.dao.IReportInfoSettingTitleServiceDao;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 进出上报题目设置服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("reportInfoSettingTitleServiceDaoImpl")
//@Transactional
public class ReportInfoSettingTitleServiceDaoImpl extends BaseServiceDao implements IReportInfoSettingTitleServiceDao {

    private static Logger logger = LoggerFactory.getLogger(ReportInfoSettingTitleServiceDaoImpl.class);





    /**
     * 保存进出上报题目设置信息 到 instance
     * @param info   bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveReportInfoSettingTitleInfo(Map info) throws DAOException {
        logger.debug("保存进出上报题目设置信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.insert("reportInfoSettingTitleServiceDaoImpl.saveReportInfoSettingTitleInfo",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存进出上报题目设置信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询进出上报题目设置信息（instance）
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getReportInfoSettingTitleInfo(Map info) throws DAOException {
        logger.debug("查询进出上报题目设置信息 入参 info : {}",info);

        List<Map> businessReportInfoSettingTitleInfos = sqlSessionTemplate.selectList("reportInfoSettingTitleServiceDaoImpl.getReportInfoSettingTitleInfo",info);

        return businessReportInfoSettingTitleInfos;
    }


    /**
     * 修改进出上报题目设置信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateReportInfoSettingTitleInfo(Map info) throws DAOException {
        logger.debug("修改进出上报题目设置信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.update("reportInfoSettingTitleServiceDaoImpl.updateReportInfoSettingTitleInfo",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"修改进出上报题目设置信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }

     /**
     * 查询进出上报题目设置数量
     * @param info 进出上报题目设置信息
     * @return 进出上报题目设置数量
     */
    @Override
    public int queryReportInfoSettingTitlesCount(Map info) {
        logger.debug("查询进出上报题目设置数据 入参 info : {}",info);

        List<Map> businessReportInfoSettingTitleInfos = sqlSessionTemplate.selectList("reportInfoSettingTitleServiceDaoImpl.queryReportInfoSettingTitlesCount", info);
        if (businessReportInfoSettingTitleInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessReportInfoSettingTitleInfos.get(0).get("count").toString());
    }


}
