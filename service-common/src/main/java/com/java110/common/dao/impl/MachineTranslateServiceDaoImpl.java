package com.java110.common.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.common.dao.IMachineTranslateServiceDao;
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
 * 设备同步服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("machineTranslateServiceDaoImpl")
//@Transactional
public class MachineTranslateServiceDaoImpl extends BaseServiceDao implements IMachineTranslateServiceDao {

    private static Logger logger = LoggerFactory.getLogger(MachineTranslateServiceDaoImpl.class);

    /**
     * 设备同步信息封装
     *
     * @param businessMachineTranslateInfo 设备同步信息 封装
     * @throws DAOException DAO异常
     */
    @Override
    public void saveBusinessMachineTranslateInfo(Map businessMachineTranslateInfo) throws DAOException {
        businessMachineTranslateInfo.put("month", DateUtil.getCurrentMonth());
        // 查询business_user 数据是否已经存在
        logger.debug("保存设备同步信息 入参 businessMachineTranslateInfo : {}", businessMachineTranslateInfo);
        int saveFlag = sqlSessionTemplate.insert("machineTranslateServiceDaoImpl.saveBusinessMachineTranslateInfo", businessMachineTranslateInfo);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存设备同步数据失败：" + JSONObject.toJSONString(businessMachineTranslateInfo));
        }
    }


    /**
     * 查询设备同步信息
     *
     * @param info bId 信息
     * @return 设备同步信息
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getBusinessMachineTranslateInfo(Map info) throws DAOException {

        logger.debug("查询设备同步信息 入参 info : {}", info);

        List<Map> businessMachineTranslateInfos = sqlSessionTemplate.selectList("machineTranslateServiceDaoImpl.getBusinessMachineTranslateInfo", info);

        return businessMachineTranslateInfos;
    }


    /**
     * 保存设备同步信息 到 instance
     *
     * @param info bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveMachineTranslateInfoInstance(Map info) throws DAOException {
        logger.debug("保存设备同步信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.insert("machineTranslateServiceDaoImpl.saveMachineTranslateInfoInstance", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存设备同步信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询设备同步信息（instance）
     *
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getMachineTranslateInfo(Map info) throws DAOException {
        logger.debug("查询设备同步信息 入参 info : {}", info);

        List<Map> businessMachineTranslateInfos = sqlSessionTemplate.selectList("machineTranslateServiceDaoImpl.getMachineTranslateInfo", info);

        return businessMachineTranslateInfos;
    }


    /**
     * 修改设备同步信息
     *
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateMachineTranslateInfoInstance(Map info) throws DAOException {
        logger.debug("修改设备同步信息Instance 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.update("machineTranslateServiceDaoImpl.updateMachineTranslateInfoInstance", info);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "修改设备同步信息Instance数据失败：" + JSONObject.toJSONString(info));
        }
    }

    /**
     * 查询设备同步数量
     *
     * @param info 设备同步信息
     * @return 设备同步数量
     */
    @Override
    public int queryMachineTranslatesCount(Map info) {
        logger.debug("查询设备同步数据 入参 info : {}", info);

        List<Map> businessMachineTranslateInfos = sqlSessionTemplate.selectList("machineTranslateServiceDaoImpl.queryMachineTranslatesCount", info);
        if (businessMachineTranslateInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessMachineTranslateInfos.get(0).get("count").toString());
    }

    @Override
    public int updateMachineTranslateState(Map info) {
        logger.debug("updateMachineTranslateState 入参 info : {}", info);

        int saveFlag = sqlSessionTemplate.update("machineTranslateServiceDaoImpl.updateMachineTranslateState", info);

        return saveFlag;
    }

    @Override
    public void saveMachineTranslate(Map info) throws DAOException {
        logger.debug("saveMachineTranslate 入参 info : {}", info);
        sqlSessionTemplate.update("machineTranslateServiceDaoImpl.saveMachineTranslate", info);
    }

    @Override
    public void updateMachineTranslate(Map info) throws DAOException {
        logger.debug("updateMachineTranslate 入参 info : {}", info);
        sqlSessionTemplate.update("machineTranslateServiceDaoImpl.updateMachineTranslate", info);
    }


}
