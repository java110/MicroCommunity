package com.java110.user.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.utils.util.DateUtil;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.user.dao.IRentingPoolFlowServiceDao;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 出租流程服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("rentingPoolFlowServiceDaoImpl")
//@Transactional
public class RentingPoolFlowServiceDaoImpl extends BaseServiceDao implements IRentingPoolFlowServiceDao {

    private static Logger logger = LoggerFactory.getLogger(RentingPoolFlowServiceDaoImpl.class);





    /**
     * 保存出租流程信息 到 instance
     * @param info   bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveRentingPoolFlowInfo(Map info) throws DAOException {
        logger.debug("保存出租流程信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.insert("rentingPoolFlowServiceDaoImpl.saveRentingPoolFlowInfo",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存出租流程信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询出租流程信息（instance）
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getRentingPoolFlowInfo(Map info) throws DAOException {
        logger.debug("查询出租流程信息 入参 info : {}",info);

        List<Map> businessRentingPoolFlowInfos = sqlSessionTemplate.selectList("rentingPoolFlowServiceDaoImpl.getRentingPoolFlowInfo",info);

        return businessRentingPoolFlowInfos;
    }


    /**
     * 修改出租流程信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateRentingPoolFlowInfo(Map info) throws DAOException {
        logger.debug("修改出租流程信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.update("rentingPoolFlowServiceDaoImpl.updateRentingPoolFlowInfo",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"修改出租流程信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }

     /**
     * 查询出租流程数量
     * @param info 出租流程信息
     * @return 出租流程数量
     */
    @Override
    public int queryRentingPoolFlowsCount(Map info) {
        logger.debug("查询出租流程数据 入参 info : {}",info);

        List<Map> businessRentingPoolFlowInfos = sqlSessionTemplate.selectList("rentingPoolFlowServiceDaoImpl.queryRentingPoolFlowsCount", info);
        if (businessRentingPoolFlowInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessRentingPoolFlowInfos.get(0).get("count").toString());
    }


}
