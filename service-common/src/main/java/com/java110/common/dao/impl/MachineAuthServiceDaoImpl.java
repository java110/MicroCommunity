package com.java110.common.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.utils.util.DateUtil;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.common.dao.IMachineAuthServiceDao;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 设备权限服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("machineAuthServiceDaoImpl")
//@Transactional
public class MachineAuthServiceDaoImpl extends BaseServiceDao implements IMachineAuthServiceDao {

    private static Logger logger = LoggerFactory.getLogger(MachineAuthServiceDaoImpl.class);

    /**
     * 设备权限信息封装
     *
     * @param businessMachineAuthInfo 设备权限信息 封装
     * @throws DAOException DAO异常
     */
    @Override
    public void saveBusinessMachineAuthInfo(Map businessMachineAuthInfo) throws DAOException {
        businessMachineAuthInfo.put("month", DateUtil.getCurrentMonth());
        // 查询business_user 数据是否已经存在
        logger.debug("保存设备权限信息 入参 businessMachineAuthInfo : {}", businessMachineAuthInfo);
        int saveFlag = sqlSessionTemplate.insert("machineAuthServiceDaoImpl.saveBusinessMachineAuthInfo", businessMachineAuthInfo);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存设备权限数据失败：" + JSONObject.toJSONString(businessMachineAuthInfo));
        }
    }


    /**
     * 查询设备权限信息
     *
     * @param info bId 信息
     * @return 设备权限信息
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getBusinessMachineAuthInfo(Map info) throws DAOException {

        logger.debug("查询设备权限信息 入参 info : {}", info);

        List<Map> businessMachineAuthInfos = sqlSessionTemplate.selectList("machineAuthServiceDaoImpl.getBusinessMachineAuthInfo", info);

        return businessMachineAuthInfos;
    }


    /**
     * 保存设备权限信息 到 instance
     *
     * @param info bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public int saveMachineAuthInfoInstance(Map info) throws DAOException {
        logger.debug("保存设备权限信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.insert("machineAuthServiceDaoImpl.saveMachineAuthInfoInstance", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存设备权限信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
        return saveFlag;
    }


    /**
     * 查询设备权限信息（instance）
     *
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getMachineAuthInfo(Map info) throws DAOException {
        logger.debug("查询设备权限信息 入参 info : {}", info);

        List<Map> businessMachineAuthInfos = sqlSessionTemplate.selectList("machineAuthServiceDaoImpl.getMachineAuthInfo", info);

        return businessMachineAuthInfos;
    }


    /**
     * 修改设备权限信息
     *
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public int updateMachineAuthInfoInstance(Map info) throws DAOException {
        logger.debug("修改设备权限信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.update("machineAuthServiceDaoImpl.updateMachineAuthInfoInstance", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "修改设备权限信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
        return saveFlag;
    }

    /**
     * 查询设备权限数量
     *
     * @param info 设备权限信息
     * @return 设备权限数量
     */
    @Override
    public int queryMachineAuthsCount(Map info) {
        logger.debug("查询设备权限数据 入参 info : {}", info);

        List<Map> businessMachineAuthInfos = sqlSessionTemplate.selectList("machineAuthServiceDaoImpl.queryMachineAuthsCount", info);
        if (businessMachineAuthInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessMachineAuthInfos.get(0).get("count").toString());
    }

    @Override
    public int saveMachineAuth(Map info) {
        logger.debug("保存员工门禁授权信息 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.insert("machineAuthServiceDaoImpl.saveMachineAuth", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存员工门禁授权数据失败：" + JSONObject.toJSONString(info));
        }
        return saveFlag;
    }

    @Override
    public int deleteMachineAuth(Map info) {
        logger.debug("保存员工门禁授权信息 入参 info : {}", info);
        int saveFlag = sqlSessionTemplate.delete("machineAuthServiceDaoImpl.deleteMachineAuth", info);
        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "删除员工门禁授权数据失败：" + JSONObject.toJSONString(info));
        }
        return saveFlag;
    }
}
