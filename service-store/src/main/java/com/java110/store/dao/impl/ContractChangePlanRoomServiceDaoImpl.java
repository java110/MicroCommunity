package com.java110.store.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.utils.util.DateUtil;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.store.dao.IContractChangePlanRoomServiceDao;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 合同房屋变更服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("contractChangePlanRoomServiceDaoImpl")
//@Transactional
public class ContractChangePlanRoomServiceDaoImpl extends BaseServiceDao implements IContractChangePlanRoomServiceDao {

    private static Logger logger = LoggerFactory.getLogger(ContractChangePlanRoomServiceDaoImpl.class);





    /**
     * 保存合同房屋变更信息 到 instance
     * @param info   bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveContractChangePlanRoomInfo(Map info) throws DAOException {
        logger.debug("保存合同房屋变更信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.insert("contractChangePlanRoomServiceDaoImpl.saveContractChangePlanRoomInfo",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存合同房屋变更信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询合同房屋变更信息（instance）
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getContractChangePlanRoomInfo(Map info) throws DAOException {
        logger.debug("查询合同房屋变更信息 入参 info : {}",info);

        List<Map> businessContractChangePlanRoomInfos = sqlSessionTemplate.selectList("contractChangePlanRoomServiceDaoImpl.getContractChangePlanRoomInfo",info);

        return businessContractChangePlanRoomInfos;
    }


    /**
     * 修改合同房屋变更信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateContractChangePlanRoomInfo(Map info) throws DAOException {
        logger.debug("修改合同房屋变更信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.update("contractChangePlanRoomServiceDaoImpl.updateContractChangePlanRoomInfo",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"修改合同房屋变更信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }

     /**
     * 查询合同房屋变更数量
     * @param info 合同房屋变更信息
     * @return 合同房屋变更数量
     */
    @Override
    public int queryContractChangePlanRoomsCount(Map info) {
        logger.debug("查询合同房屋变更数据 入参 info : {}",info);

        List<Map> businessContractChangePlanRoomInfos = sqlSessionTemplate.selectList("contractChangePlanRoomServiceDaoImpl.queryContractChangePlanRoomsCount", info);
        if (businessContractChangePlanRoomInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessContractChangePlanRoomInfos.get(0).get("count").toString());
    }


}
