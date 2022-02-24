package com.java110.common.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.common.dao.ICarInoutServiceDao;
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
 * 进出场服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("carInoutServiceDaoImpl")
//@Transactional
public class CarInoutServiceDaoImpl extends BaseServiceDao implements ICarInoutServiceDao {

    private static Logger logger = LoggerFactory.getLogger(CarInoutServiceDaoImpl.class);

    /**
     * 进出场信息封装
     *
     * @param businessCarInoutInfo 进出场信息 封装
     * @throws DAOException DAO异常
     */
    @Override
    public void saveBusinessCarInoutInfo(Map businessCarInoutInfo) throws DAOException {
        businessCarInoutInfo.put("month", DateUtil.getCurrentMonth());
        // 查询business_user 数据是否已经存在
        logger.debug("保存进出场信息 入参 businessCarInoutInfo : {}", businessCarInoutInfo);
        int saveFlag = sqlSessionTemplate.insert("carInoutServiceDaoImpl.saveBusinessCarInoutInfo", businessCarInoutInfo);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存进出场数据失败：" + JSONObject.toJSONString(businessCarInoutInfo));
        }
    }


    /**
     * 查询进出场信息
     *
     * @param info bId 信息
     * @return 进出场信息
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getBusinessCarInoutInfo(Map info) throws DAOException {

        logger.debug("查询进出场信息 入参 info : {}", info);

        List<Map> businessCarInoutInfos = sqlSessionTemplate.selectList("carInoutServiceDaoImpl.getBusinessCarInoutInfo", info);

        return businessCarInoutInfos;
    }


    /**
     * 保存进出场信息 到 instance
     *
     * @param info bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveCarInoutInfoInstance(Map info) throws DAOException {
        logger.debug("保存进出场信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.insert("carInoutServiceDaoImpl.saveCarInoutInfoInstance", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存进出场信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询进出场信息（instance）
     *
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getCarInoutInfo(Map info) throws DAOException {
        logger.debug("查询进出场信息 入参 info : {}", info);

        List<Map> businessCarInoutInfos = sqlSessionTemplate.selectList("carInoutServiceDaoImpl.getCarInoutInfo", info);

        return businessCarInoutInfos;
    }


    /**
     * 修改进出场信息
     *
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateCarInoutInfoInstance(Map info) throws DAOException {
        logger.debug("修改进出场信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.update("carInoutServiceDaoImpl.updateCarInoutInfoInstance", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "修改进出场信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }

    /**
     * 查询进出场数量
     *
     * @param info 进出场信息
     * @return 进出场数量
     */
    @Override
    public int queryCarInoutsCount(Map info) {
        logger.debug("查询进出场数据 入参 info : {}", info);

        List<Map> businessCarInoutInfos = sqlSessionTemplate.selectList("carInoutServiceDaoImpl.queryCarInoutsCount", info);
        if (businessCarInoutInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessCarInoutInfos.get(0).get("count").toString());
    }


}
