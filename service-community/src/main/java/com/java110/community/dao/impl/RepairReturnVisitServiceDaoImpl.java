package com.java110.community.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.utils.util.DateUtil;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.community.dao.IRepairReturnVisitServiceDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 报修回访服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("repairReturnVisitServiceDaoImpl")
//@Transactional
public class RepairReturnVisitServiceDaoImpl extends BaseServiceDao implements IRepairReturnVisitServiceDao {

    private static Logger logger = LoggerFactory.getLogger(RepairReturnVisitServiceDaoImpl.class);





    /**
     * 保存报修回访信息 到 instance
     * @param info   bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveRepairReturnVisitInfo(Map info) throws DAOException {
        logger.debug("保存报修回访信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.insert("repairReturnVisitServiceDaoImpl.saveRepairReturnVisitInfo",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存报修回访信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询报修回访信息（instance）
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getRepairReturnVisitInfo(Map info) throws DAOException {
        logger.debug("查询报修回访信息 入参 info : {}",info);

        List<Map> businessRepairReturnVisitInfos = sqlSessionTemplate.selectList("repairReturnVisitServiceDaoImpl.getRepairReturnVisitInfo",info);

        return businessRepairReturnVisitInfos;
    }


    /**
     * 修改报修回访信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateRepairReturnVisitInfo(Map info) throws DAOException {
        logger.debug("修改报修回访信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.update("repairReturnVisitServiceDaoImpl.updateRepairReturnVisitInfo",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"修改报修回访信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }

     /**
     * 查询报修回访数量
     * @param info 报修回访信息
     * @return 报修回访数量
     */
    @Override
    public int queryRepairReturnVisitsCount(Map info) {
        logger.debug("查询报修回访数据 入参 info : {}",info);

        List<Map> businessRepairReturnVisitInfos = sqlSessionTemplate.selectList("repairReturnVisitServiceDaoImpl.queryRepairReturnVisitsCount", info);
        if (businessRepairReturnVisitInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessRepairReturnVisitInfos.get(0).get("count").toString());
    }


}
