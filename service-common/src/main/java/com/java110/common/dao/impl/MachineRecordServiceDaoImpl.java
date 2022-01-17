package com.java110.common.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.common.dao.IMachineRecordServiceDao;
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
 * 设备上报服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("machineRecordServiceDaoImpl")
//@Transactional
public class MachineRecordServiceDaoImpl extends BaseServiceDao implements IMachineRecordServiceDao {

    private static Logger logger = LoggerFactory.getLogger(MachineRecordServiceDaoImpl.class);

    /**
     * 设备上报信息封装
     *
     * @param businessMachineRecordInfo 设备上报信息 封装
     * @throws DAOException DAO异常
     */
    @Override
    public void saveBusinessMachineRecordInfo(Map businessMachineRecordInfo) throws DAOException {
        businessMachineRecordInfo.put("month", DateUtil.getCurrentMonth());
        // 查询business_user 数据是否已经存在
        logger.debug("保存设备上报信息 入参 businessMachineRecordInfo : {}", businessMachineRecordInfo);
        int saveFlag = sqlSessionTemplate.insert("machineRecordServiceDaoImpl.saveBusinessMachineRecordInfo", businessMachineRecordInfo);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存设备上报数据失败：" + JSONObject.toJSONString(businessMachineRecordInfo));
        }
    }


    /**
     * 查询设备上报信息
     *
     * @param info bId 信息
     * @return 设备上报信息
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getBusinessMachineRecordInfo(Map info) throws DAOException {

        logger.debug("查询设备上报信息 入参 info : {}", info);

        List<Map> businessMachineRecordInfos = sqlSessionTemplate.selectList("machineRecordServiceDaoImpl.getBusinessMachineRecordInfo", info);

        return businessMachineRecordInfos;
    }


    /**
     * 保存设备上报信息 到 instance
     *
     * @param info bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveMachineRecordInfoInstance(Map info) throws DAOException {
        logger.debug("保存设备上报信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.insert("machineRecordServiceDaoImpl.saveMachineRecordInfoInstance", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存设备上报信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询设备上报信息（instance）
     *
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getMachineRecordInfo(Map info) throws DAOException {
        logger.debug("查询设备上报信息 入参 info : {}", info);

        List<Map> businessMachineRecordInfos = sqlSessionTemplate.selectList("machineRecordServiceDaoImpl.getMachineRecordInfo", info);

        return businessMachineRecordInfos;
    }


    /**
     * 修改设备上报信息
     *
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateMachineRecordInfoInstance(Map info) throws DAOException {
        logger.debug("修改设备上报信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.update("machineRecordServiceDaoImpl.updateMachineRecordInfoInstance", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "修改设备上报信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }

    /**
     * 查询设备上报数量
     *
     * @param info 设备上报信息
     * @return 设备上报数量
     */
    @Override
    public int queryMachineRecordsCount(Map info) {
        logger.debug("查询设备上报数据 入参 info : {}", info);

        List<Map> businessMachineRecordInfos = sqlSessionTemplate.selectList("machineRecordServiceDaoImpl.queryMachineRecordsCount", info);
        if (businessMachineRecordInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessMachineRecordInfos.get(0).get("count").toString());
    }

    @Override
    public List<Map> getAssetsMachineRecords(Map info) {
        List<Map> machineRecordInfos = sqlSessionTemplate.selectList("machineRecordServiceDaoImpl.getAssetsMachineRecords", info);
        return machineRecordInfos;
    }

    @Override
    public int saveMachineRecords(Map info) {
        int saveFlag = sqlSessionTemplate.update("machineRecordServiceDaoImpl.saveMachineRecords", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "添加数据失败：" + JSONObject.toJSONString(info));
        }

        return saveFlag;
    }


}
