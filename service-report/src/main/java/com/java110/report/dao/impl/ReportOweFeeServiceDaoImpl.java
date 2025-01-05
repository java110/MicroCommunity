package com.java110.report.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.report.dao.IReportOweFeeServiceDao;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.stereotype.Service;

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
     *
     * @param info bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveReportOweFeeInfo(Map info) throws DAOException {
        logger.debug("保存欠费统计信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.insert("reportOweFeeServiceDaoImpl.saveReportOweFeeInfo", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存欠费统计信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询欠费统计信息（instance）
     *
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getReportOweFeeInfo(Map info) throws DAOException {
        logger.debug("查询欠费统计信息 入参 info : {}", info);

        List<Map> infos = sqlSessionTemplate.selectList("reportOweFeeServiceDaoImpl.getReportOweFeeInfo", info);

        return infos;
    }


    /**
     * 修改欠费统计信息
     *
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateReportOweFeeInfo(Map info) throws DAOException {
        logger.debug("修改欠费统计信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.update("reportOweFeeServiceDaoImpl.updateReportOweFeeInfo", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "修改欠费统计信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }

    /**
     * 修改欠费统计信息
     *
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public int deleteReportOweFeeInfo(Map info) throws DAOException {
        logger.debug("deleteReportOweFeeInfo : {}", info);

        int saveFlag = sqlSessionTemplate.update("reportOweFeeServiceDaoImpl.deleteReportOweFeeInfo", info);

        return saveFlag;
    }


    /**
     * 查询欠费统计数量
     *
     * @param info 欠费统计信息
     * @return 欠费统计数量
     */
    @Override
    public int queryReportOweFeesCount(Map info) {
        logger.debug("查询欠费统计数据 入参 info : {}", info);

        List<Map> infos = sqlSessionTemplate.selectList("reportOweFeeServiceDaoImpl.queryReportOweFeesCount", info);
        if (infos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(infos.get(0).get("count").toString());
    }

    @Override
    public List<Map> queryReportAllOweFees(Map info) {
        logger.debug("queryReportAllOweFees 入参 info : {}", info);

        List<Map> infos = sqlSessionTemplate.selectList("reportOweFeeServiceDaoImpl.queryReportAllOweFees", info);

        return infos;
    }

    @Override
    public List<Map> queryReportAllOweFeesByRoom(Map info) {
        logger.debug("queryReportAllOweFeesByRoom 入参 info : {}", info);

        List<Map> businessReportOweFeeByRoomInfos = sqlSessionTemplate.selectList("reportOweFeeServiceDaoImpl.queryReportAllOweFeesByRoom", info);

        return businessReportOweFeeByRoomInfos;
    }

    @Override
    public List<Map> queryReportAllOweFeesByCar(Map info) {
        logger.debug("queryReportAllOweFeesByCar 入参 info : {}", info);

        List<Map> businessReportOweFeeByCarInfos = sqlSessionTemplate.selectList("reportOweFeeServiceDaoImpl.queryReportAllOweFeesByCar", info);

        return businessReportOweFeeByCarInfos;
    }

    @Override
    public List<Map> queryReportAllOweFeesByContract(Map info) {
        logger.debug("queryReportAllOweFeesByContract 入参 info : {}", info);

        List<Map> businessReportOweFeeByCarInfos = sqlSessionTemplate.selectList("reportOweFeeServiceDaoImpl.queryReportAllOweFeesByContract", info);

        return businessReportOweFeeByCarInfos;
    }


    @Override
    public double computeReportOweFeeTotalAmount(Map info) {
        List<Map> infos = sqlSessionTemplate.selectList("reportOweFeeServiceDaoImpl.computeReportOweFeeTotalAmount", info);
        if (infos.size() < 1) {
            return 0;
        }
        return Double.parseDouble(infos.get(0).get("total").toString());
    }

    @Override
    public List<Map> computeReportOweFeeItemAmount(Map info) {
        List<Map> infos = sqlSessionTemplate.selectList("reportOweFeeServiceDaoImpl.computeReportOweFeeItemAmount", info);
        return infos;
    }

    @Override
    public int deleteInvalidFee(Map info) {
        logger.debug("保deleteInvalidFee 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.insert("reportOweFeeServiceDaoImpl.deleteInvalidFee", info);

        return saveFlag;
    }

    @Override
    public List<Map> queryInvalidOweFee(Map info) {
        List<Map> infos = sqlSessionTemplate.selectList("reportOweFeeServiceDaoImpl.queryInvalidOweFee", info);
        return infos;
    }

    @Override
    public List<Map> queryOweFeesByOwnerIds(Map info) {
        List<Map> infos = sqlSessionTemplate.selectList("reportOweFeeServiceDaoImpl.queryOweFeesByOwnerIds", info);
        return infos;
    }

    @Override
    public List<Map> queryOweFeesByRoomIds(Map info) {
        List<Map> infos = sqlSessionTemplate.selectList("reportOweFeeServiceDaoImpl.queryOweFeesByRoomIds", info);
        return infos;
    }

    @Override
    public List<Map> queryOwnerOweFee(Map info) {
        List<Map> infos = sqlSessionTemplate.selectList("reportOweFeeServiceDaoImpl.queryOwnerOweFee", info);
        return infos;
    }


}
