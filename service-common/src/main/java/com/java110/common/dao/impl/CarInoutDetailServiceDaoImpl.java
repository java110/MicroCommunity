package com.java110.common.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.common.dao.ICarInoutDetailServiceDao;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.utils.util.DateUtil;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 进出场详情服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("carInoutDetailServiceDaoImpl")
//@Transactional
public class CarInoutDetailServiceDaoImpl extends BaseServiceDao implements ICarInoutDetailServiceDao {

    private static Logger logger = LoggerFactory.getLogger(CarInoutDetailServiceDaoImpl.class);

    /**
     * 进出场详情信息封装
     *
     * @param businessCarInoutDetailInfo 进出场详情信息 封装
     * @throws DAOException DAO异常
     */
    @Override
    public void saveBusinessCarInoutDetailInfo(Map businessCarInoutDetailInfo) throws DAOException {
        businessCarInoutDetailInfo.put("month", DateUtil.getCurrentMonth());
        // 查询business_user 数据是否已经存在
        logger.debug("保存进出场详情信息 入参 businessCarInoutDetailInfo : {}", businessCarInoutDetailInfo);
        int saveFlag = sqlSessionTemplate.insert("carInoutDetailServiceDaoImpl.saveBusinessCarInoutDetailInfo", businessCarInoutDetailInfo);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存进出场详情数据失败：" + JSONObject.toJSONString(businessCarInoutDetailInfo));
        }
    }


    /**
     * 查询进出场详情信息
     *
     * @param info bId 信息
     * @return 进出场详情信息
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getBusinessCarInoutDetailInfo(Map info) throws DAOException {

        logger.debug("查询进出场详情信息 入参 info : {}", info);

        List<Map> businessCarInoutDetailInfos = sqlSessionTemplate.selectList("carInoutDetailServiceDaoImpl.getBusinessCarInoutDetailInfo", info);

        return businessCarInoutDetailInfos;
    }


    /**
     * 保存进出场详情信息 到 instance
     *
     * @param info bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveCarInoutDetailInfoInstance(Map info) throws DAOException {
        logger.debug("保存进出场详情信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.insert("carInoutDetailServiceDaoImpl.saveCarInoutDetailInfoInstance", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存进出场详情信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询进出场详情信息（instance）
     *
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getCarInoutDetailInfo(Map info) throws DAOException {
        logger.debug("查询进出场详情信息 入参 info : {}", info);

        List<Map> businessCarInoutDetailInfos = sqlSessionTemplate.selectList("carInoutDetailServiceDaoImpl.getCarInoutDetailInfo", info);

        return businessCarInoutDetailInfos;
    }


    /**
     * 修改进出场详情信息
     *
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateCarInoutDetailInfoInstance(Map info) throws DAOException {
        logger.debug("修改进出场详情信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.update("carInoutDetailServiceDaoImpl.updateCarInoutDetailInfoInstance", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "修改进出场详情信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }

    /**
     * 查询进出场详情数量
     *
     * @param info 进出场详情信息
     * @return 进出场详情数量
     */
    @Override
    public int queryCarInoutDetailsCount(Map info) {
        logger.debug("查询进出场详情数据 入参 info : {}", info);

        List<Map> businessCarInoutDetailInfos = sqlSessionTemplate.selectList("carInoutDetailServiceDaoImpl.queryCarInoutDetailsCount", info);
        if (businessCarInoutDetailInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessCarInoutDetailInfos.get(0).get("count").toString());
    }


}
