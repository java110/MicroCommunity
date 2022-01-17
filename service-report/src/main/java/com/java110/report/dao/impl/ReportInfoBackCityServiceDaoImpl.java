package com.java110.report.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.utils.util.DateUtil;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.report.dao.IReportInfoBackCityServiceDao;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 返省上报服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("reportInfoBackCityServiceDaoImpl")
//@Transactional
public class ReportInfoBackCityServiceDaoImpl extends BaseServiceDao implements IReportInfoBackCityServiceDao {

    private static Logger logger = LoggerFactory.getLogger(ReportInfoBackCityServiceDaoImpl.class);





    /**
     * 保存返省上报信息 到 instance
     * @param info   bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveReportInfoBackCityInfo(Map info) throws DAOException {
        logger.debug("保存返省上报信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.insert("reportInfoBackCityServiceDaoImpl.saveReportInfoBackCityInfo",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存返省上报信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询返省上报信息（instance）
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getReportInfoBackCityInfo(Map info) throws DAOException {
        logger.debug("查询返省上报信息 入参 info : {}",info);

        List<Map> businessReportInfoBackCityInfos = sqlSessionTemplate.selectList("reportInfoBackCityServiceDaoImpl.getReportInfoBackCityInfo",info);

        return businessReportInfoBackCityInfos;
    }


    /**
     * 修改返省上报信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateReportInfoBackCityInfo(Map info) throws DAOException {
        logger.debug("修改返省上报信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.update("reportInfoBackCityServiceDaoImpl.updateReportInfoBackCityInfo",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"修改返省上报信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }

     /**
     * 查询返省上报数量
     * @param info 返省上报信息
     * @return 返省上报数量
     */
    @Override
    public int queryReportInfoBackCitysCount(Map info) {
        logger.debug("查询返省上报数据 入参 info : {}",info);

        List<Map> businessReportInfoBackCityInfos = sqlSessionTemplate.selectList("reportInfoBackCityServiceDaoImpl.queryReportInfoBackCitysCount", info);
        if (businessReportInfoBackCityInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessReportInfoBackCityInfos.get(0).get("count").toString());
    }


}
