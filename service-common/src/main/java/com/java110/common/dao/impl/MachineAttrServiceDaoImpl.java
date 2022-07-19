package com.java110.common.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.common.dao.IMachineAttrServiceDao;
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
 * 设备属性服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("machineAttrServiceDaoImpl")
//@Transactional
public class MachineAttrServiceDaoImpl extends BaseServiceDao implements IMachineAttrServiceDao {

    private static Logger logger = LoggerFactory.getLogger(MachineAttrServiceDaoImpl.class);

    /**
     * 设备属性信息封装
     *
     * @param businessMachineAttrInfo 设备属性信息 封装
     * @throws DAOException DAO异常
     */
    @Override
    public void saveBusinessMachineAttrInfo(Map businessMachineAttrInfo) throws DAOException {
        businessMachineAttrInfo.put("month", DateUtil.getCurrentMonth());
        // 查询business_user 数据是否已经存在
        logger.debug("保存设备属性信息 入参 businessMachineAttrInfo : {}", businessMachineAttrInfo);
        int saveFlag = sqlSessionTemplate.insert("machineAttrServiceDaoImpl.saveBusinessMachineAttrInfo", businessMachineAttrInfo);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存设备属性数据失败：" + JSONObject.toJSONString(businessMachineAttrInfo));
        }
    }


    /**
     * 查询设备属性信息
     *
     * @param info bId 信息
     * @return 设备属性信息
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getBusinessMachineAttrInfo(Map info) throws DAOException {

        logger.debug("查询设备属性信息 入参 info : {}", info);

        List<Map> businessMachineAttrInfos = sqlSessionTemplate.selectList("machineAttrServiceDaoImpl.getBusinessMachineAttrInfo", info);

        return businessMachineAttrInfos;
    }


    /**
     * 保存设备属性信息 到 instance
     *
     * @param info bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveMachineAttrInfoInstance(Map info) throws DAOException {
        logger.debug("保存设备属性信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.insert("machineAttrServiceDaoImpl.saveMachineAttrInfoInstance", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存设备属性信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询设备属性信息（instance）
     *
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getMachineAttrInfo(Map info) throws DAOException {
        logger.debug("查询设备属性信息 入参 info : {}", info);

        List<Map> businessMachineAttrInfos = sqlSessionTemplate.selectList("machineAttrServiceDaoImpl.getMachineAttrInfo", info);

        return businessMachineAttrInfos;
    }


    /**
     * 修改设备属性信息
     *
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateMachineAttrInfoInstance(Map info) throws DAOException {
        logger.debug("修改设备属性信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.update("machineAttrServiceDaoImpl.updateMachineAttrInfoInstance", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "修改设备属性信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }

    /**
     * 查询设备属性数量
     *
     * @param info 设备属性信息
     * @return 设备属性数量
     */
    @Override
    public int queryMachineAttrsCount(Map info) {
        logger.debug("查询设备属性数据 入参 info : {}", info);

        List<Map> businessMachineAttrInfos = sqlSessionTemplate.selectList("machineAttrServiceDaoImpl.queryMachineAttrsCount", info);
        if (businessMachineAttrInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessMachineAttrInfos.get(0).get("count").toString());
    }

    @Override
    public int saveMachineAttrs(Map info) {
        int saveFlag = sqlSessionTemplate.update("machineAttrServiceDaoImpl.saveMachineAttrs", info);
        return saveFlag;
    }

    @Override
    public int updateMachineAttrs(Map info) {
        int saveFlag = sqlSessionTemplate.update("machineAttrServiceDaoImpl.updateMachineAttrInfoInstance", info);
        return saveFlag;
    }



}
