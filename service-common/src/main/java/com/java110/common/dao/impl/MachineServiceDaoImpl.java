package com.java110.common.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.common.dao.IMachineServiceDao;
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
 * 设备服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("machineServiceDaoImpl")
//@Transactional
public class MachineServiceDaoImpl extends BaseServiceDao implements IMachineServiceDao {

    private static Logger logger = LoggerFactory.getLogger(MachineServiceDaoImpl.class);

    /**
     * 设备信息封装
     *
     * @param businessMachineInfo 设备信息 封装
     * @throws DAOException DAO异常
     */
    @Override
    public void saveBusinessMachineInfo(Map businessMachineInfo) throws DAOException {
        businessMachineInfo.put("month", DateUtil.getCurrentMonth());
        // 查询business_user 数据是否已经存在
        logger.debug("保存设备信息 入参 businessMachineInfo : {}", businessMachineInfo);
        int saveFlag = sqlSessionTemplate.insert("machineServiceDaoImpl.saveBusinessMachineInfo", businessMachineInfo);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存设备数据失败：" + JSONObject.toJSONString(businessMachineInfo));
        }
    }


    /**
     * 查询设备信息
     *
     * @param info bId 信息
     * @return 设备信息
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getBusinessMachineInfo(Map info) throws DAOException {

        logger.debug("查询设备信息 入参 info : {}", info);

        List<Map> businessMachineInfos = sqlSessionTemplate.selectList("machineServiceDaoImpl.getBusinessMachineInfo", info);

        return businessMachineInfos;
    }


    /**
     * 保存设备信息 到 instance
     *
     * @param info bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveMachineInfoInstance(Map info) throws DAOException {
        logger.debug("保存设备信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.insert("machineServiceDaoImpl.saveMachineInfoInstance", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存设备信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询设备信息（instance）
     *
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getMachineInfo(Map info) throws DAOException {
        logger.debug("查询设备信息 入参 info : {}", info);

        List<Map> businessMachineInfos = sqlSessionTemplate.selectList("machineServiceDaoImpl.getMachineInfo", info);

        return businessMachineInfos;
    }


    /**
     * 修改设备信息
     *
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateMachineInfoInstance(Map info) throws DAOException {
        logger.debug("修改设备信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.update("machineServiceDaoImpl.updateMachineInfoInstance", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "修改设备信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }

    /**
     * 查询设备数量
     *
     * @param info 设备信息
     * @return 设备数量
     */
    @Override
    public int queryMachinesCount(Map info) {
        logger.debug("查询设备数据 入参 info : {}", info);

        List<Map> businessMachineInfos = sqlSessionTemplate.selectList("machineServiceDaoImpl.queryMachinesCount", info);
        if (businessMachineInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessMachineInfos.get(0).get("count").toString());
    }


}
