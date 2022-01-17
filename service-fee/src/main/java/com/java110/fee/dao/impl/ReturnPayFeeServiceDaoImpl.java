package com.java110.fee.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.fee.dao.IReturnPayFeeServiceDao;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.utils.util.DateUtil;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 退费表服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("returnPayFeeServiceDaoImpl")
//@Transactional
public class ReturnPayFeeServiceDaoImpl extends BaseServiceDao implements IReturnPayFeeServiceDao {

    private static Logger logger = LoggerFactory.getLogger(ReturnPayFeeServiceDaoImpl.class);

    /**
     * 退费表信息封装
     *
     * @param businessReturnPayFeeInfo 退费表信息 封装
     * @throws DAOException DAO异常
     */
    @Override
    public void saveBusinessReturnPayFeeInfo(Map businessReturnPayFeeInfo) throws DAOException {
        businessReturnPayFeeInfo.put("month", DateUtil.getCurrentMonth());
        // 查询business_user 数据是否已经存在
        logger.debug("保存退费表信息 入参 businessReturnPayFeeInfo : {}", businessReturnPayFeeInfo);
        int saveFlag = sqlSessionTemplate.insert("returnPayFeeServiceDaoImpl.saveBusinessReturnPayFeeInfo", businessReturnPayFeeInfo);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存退费表数据失败：" + JSONObject.toJSONString(businessReturnPayFeeInfo));
        }
    }


    /**
     * 查询退费表信息
     *
     * @param info bId 信息
     * @return 退费表信息
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getBusinessReturnPayFeeInfo(Map info) throws DAOException {

        logger.debug("查询退费表信息 入参 info : {}", info);

        List<Map> businessReturnPayFeeInfos = sqlSessionTemplate.selectList("returnPayFeeServiceDaoImpl.getBusinessReturnPayFeeInfo", info);

        return businessReturnPayFeeInfos;
    }


    /**
     * 保存退费表信息 到 instance
     *
     * @param info bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveReturnPayFeeInfoInstance(Map info) throws DAOException {
        logger.debug("保存退费表信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.insert("returnPayFeeServiceDaoImpl.saveReturnPayFeeInfoInstance", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存退费表信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询退费表信息（instance）
     *
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getReturnPayFeeInfo(Map info) throws DAOException {
        logger.debug("查询退费表信息 入参 info : {}", info);

        List<Map> businessReturnPayFeeInfos = sqlSessionTemplate.selectList("returnPayFeeServiceDaoImpl.getReturnPayFeeInfo", info);

        return businessReturnPayFeeInfos;
    }

    /**
     * 查询退费表信息（instance）
     *
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getRoomReturnPayFeeInfo(Map info) throws DAOException {
        logger.debug("查询退费表信息 入参 info : {}", info);

        List<Map> businessReturnPayFeeInfos = sqlSessionTemplate.selectList("returnPayFeeServiceDaoImpl.getRoomReturnPayFeeInfo", info);

        return businessReturnPayFeeInfos;
    }

    /**
     * 查询退费表信息（instance）
     *
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getCarReturnPayFeeInfo(Map info) throws DAOException {
        logger.debug("查询退费表信息 入参 info : {}", info);

        List<Map> businessReturnPayFeeInfos = sqlSessionTemplate.selectList("returnPayFeeServiceDaoImpl.getCarReturnPayFeeInfo", info);

        return businessReturnPayFeeInfos;
    }


    /**
     * 修改退费表信息
     *
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateReturnPayFeeInfoInstance(Map info) throws DAOException {
        logger.debug("修改退费表信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.update("returnPayFeeServiceDaoImpl.updateReturnPayFeeInfoInstance", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "修改退费表信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }

    /**
     * 查询退费表数量
     *
     * @param info 退费表信息
     * @return 退费表数量
     */
    @Override
    public int queryReturnPayFeesCount(Map info) {
        logger.debug("查询退费表数据 入参 info : {}", info);

        List<Map> businessReturnPayFeeInfos = sqlSessionTemplate.selectList("returnPayFeeServiceDaoImpl.queryReturnPayFeesCount", info);
        if (businessReturnPayFeeInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessReturnPayFeeInfos.get(0).get("count").toString());
    }


}
