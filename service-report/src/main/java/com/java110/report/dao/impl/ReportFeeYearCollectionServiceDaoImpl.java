package com.java110.report.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.report.dao.IReportFeeYearCollectionServiceDao;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 费用年收费服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("reportFeeYearCollectionServiceDaoImpl")
//@Transactional
public class ReportFeeYearCollectionServiceDaoImpl extends BaseServiceDao implements IReportFeeYearCollectionServiceDao {

    private static Logger logger = LoggerFactory.getLogger(ReportFeeYearCollectionServiceDaoImpl.class);


    /**
     * 保存费用年收费信息 到 instance
     *
     * @param info bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveReportFeeYearCollectionInfo(Map info) throws DAOException {
        logger.debug("保存费用年收费信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.insert("reportFeeYearCollectionServiceDaoImpl.saveReportFeeYearCollectionInfo", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存费用年收费信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询费用年收费信息（instance）
     *
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getReportFeeYearCollectionInfo(Map info) throws DAOException {
        logger.debug("查询费用年收费信息 入参 info : {}", info);

        List<Map> businessReportFeeYearCollectionInfos = sqlSessionTemplate.selectList("reportFeeYearCollectionServiceDaoImpl.getReportFeeYearCollectionInfo", info);

        return businessReportFeeYearCollectionInfos;
    }

    /**
     * 查询费用年收费信息（instance）
     *
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getReportFeeYearCollectionInfos(Map info) throws DAOException {
        logger.debug("查询费用年收费信息 入参 info : {}", info);

        List<Map> businessReportFeeYearCollectionInfos = sqlSessionTemplate.selectList("reportFeeYearCollectionServiceDaoImpl.getReportFeeYearCollectionInfos", info);

        return businessReportFeeYearCollectionInfos;
    }


    /**
     * 修改费用年收费信息
     *
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateReportFeeYearCollectionInfo(Map info) throws DAOException {
        logger.debug("修改费用年收费信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.update("reportFeeYearCollectionServiceDaoImpl.updateReportFeeYearCollectionInfo", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "修改费用年收费信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }

    /**
     * 修改费用年收费信息
     *
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void deleteReportFeeYearCollectionInfo(Map info) throws DAOException {
        logger.debug("deleteReportFeeYearCollectionInfo 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.update("reportFeeYearCollectionServiceDaoImpl.deleteReportFeeYearCollectionInfo", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "修改费用年收费信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }

    /**
     * 查询费用年收费数量
     *
     * @param info 费用年收费信息
     * @return 费用年收费数量
     */
    @Override
    public int queryReportFeeYearCollectionsCount(Map info) {
        logger.debug("查询费用年收费数据 入参 info : {}", info);

        List<Map> businessReportFeeYearCollectionInfos = sqlSessionTemplate.selectList("reportFeeYearCollectionServiceDaoImpl.queryReportFeeYearCollectionsCount", info);
        if (businessReportFeeYearCollectionInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessReportFeeYearCollectionInfos.get(0).get("count").toString());
    }


}
