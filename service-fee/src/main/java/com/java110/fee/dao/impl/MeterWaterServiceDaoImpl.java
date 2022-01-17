package com.java110.fee.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.fee.dao.IMeterWaterServiceDao;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.utils.util.DateUtil;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 水电费服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("meterWaterServiceDaoImpl")
//@Transactional
public class MeterWaterServiceDaoImpl extends BaseServiceDao implements IMeterWaterServiceDao {

    private static Logger logger = LoggerFactory.getLogger(MeterWaterServiceDaoImpl.class);

    /**
     * 水电费信息封装
     * @param businessMeterWaterInfo 水电费信息 封装
     * @throws DAOException DAO异常
     */
    @Override
    public void saveBusinessMeterWaterInfo(Map businessMeterWaterInfo) throws DAOException {
        businessMeterWaterInfo.put("month", DateUtil.getCurrentMonth());
        // 查询business_user 数据是否已经存在
        logger.debug("保存水电费信息 入参 businessMeterWaterInfo : {}",businessMeterWaterInfo);
        int saveFlag = sqlSessionTemplate.insert("meterWaterServiceDaoImpl.saveBusinessMeterWaterInfo",businessMeterWaterInfo);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存水电费数据失败："+ JSONObject.toJSONString(businessMeterWaterInfo));
        }
    }


    /**
     * 查询水电费信息
     * @param info bId 信息
     * @return 水电费信息
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getBusinessMeterWaterInfo(Map info) throws DAOException {

        logger.debug("查询水电费信息 入参 info : {}",info);

        List<Map> businessMeterWaterInfos = sqlSessionTemplate.selectList("meterWaterServiceDaoImpl.getBusinessMeterWaterInfo",info);

        return businessMeterWaterInfos;
    }



    /**
     * 保存水电费信息 到 instance
     * @param info   bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveMeterWaterInfoInstance(Map info) throws DAOException {
        logger.debug("保存水电费信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.insert("meterWaterServiceDaoImpl.saveMeterWaterInfoInstance",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存水电费信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询水电费信息（instance）
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getMeterWaterInfo(Map info) throws DAOException {
        logger.debug("查询水电费信息 入参 info : {}",info);

        List<Map> businessMeterWaterInfos = sqlSessionTemplate.selectList("meterWaterServiceDaoImpl.getMeterWaterInfo",info);

        return businessMeterWaterInfos;
    }


    /**
     * 修改水电费信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateMeterWaterInfoInstance(Map info) throws DAOException {
        logger.debug("修改水电费信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.update("meterWaterServiceDaoImpl.updateMeterWaterInfoInstance",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"修改水电费信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }

     /**
     * 查询水电费数量
     * @param info 水电费信息
     * @return 水电费数量
     */
    @Override
    public int queryMeterWatersCount(Map info) {
        logger.debug("查询水电费数据 入参 info : {}",info);

        List<Map> businessMeterWaterInfos = sqlSessionTemplate.selectList("meterWaterServiceDaoImpl.queryMeterWatersCount", info);
        if (businessMeterWaterInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessMeterWaterInfos.get(0).get("count").toString());
    }

    @Override
    public int insertMeterWaters(Map info) {

        int saveFlag = sqlSessionTemplate.insert("meterWaterServiceDaoImpl.insertMeterWaters", info);

        return saveFlag;
    }


}
