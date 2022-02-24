package com.java110.common.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.utils.util.DateUtil;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.common.dao.IMachineTranslateErrorServiceDao;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * IOT同步错误日志记录服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("machineTranslateErrorServiceDaoImpl")
//@Transactional
public class MachineTranslateErrorServiceDaoImpl extends BaseServiceDao implements IMachineTranslateErrorServiceDao {

    private static Logger logger = LoggerFactory.getLogger(MachineTranslateErrorServiceDaoImpl.class);





    /**
     * 保存IOT同步错误日志记录信息 到 instance
     * @param info   bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveMachineTranslateErrorInfo(Map info) throws DAOException {
        logger.debug("保存IOT同步错误日志记录信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.insert("machineTranslateErrorServiceDaoImpl.saveMachineTranslateErrorInfo",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存IOT同步错误日志记录信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询IOT同步错误日志记录信息（instance）
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getMachineTranslateErrorInfo(Map info) throws DAOException {
        logger.debug("查询IOT同步错误日志记录信息 入参 info : {}",info);

        List<Map> businessMachineTranslateErrorInfos = sqlSessionTemplate.selectList("machineTranslateErrorServiceDaoImpl.getMachineTranslateErrorInfo",info);

        return businessMachineTranslateErrorInfos;
    }


    /**
     * 修改IOT同步错误日志记录信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateMachineTranslateErrorInfo(Map info) throws DAOException {
        logger.debug("修改IOT同步错误日志记录信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.update("machineTranslateErrorServiceDaoImpl.updateMachineTranslateErrorInfo",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"修改IOT同步错误日志记录信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }

     /**
     * 查询IOT同步错误日志记录数量
     * @param info IOT同步错误日志记录信息
     * @return IOT同步错误日志记录数量
     */
    @Override
    public int queryMachineTranslateErrorsCount(Map info) {
        logger.debug("查询IOT同步错误日志记录数据 入参 info : {}",info);

        List<Map> businessMachineTranslateErrorInfos = sqlSessionTemplate.selectList("machineTranslateErrorServiceDaoImpl.queryMachineTranslateErrorsCount", info);
        if (businessMachineTranslateErrorInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessMachineTranslateErrorInfos.get(0).get("count").toString());
    }


}
