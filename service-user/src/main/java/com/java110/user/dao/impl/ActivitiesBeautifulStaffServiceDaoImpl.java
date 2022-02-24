package com.java110.user.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.utils.util.DateUtil;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.user.dao.IActivitiesBeautifulStaffServiceDao;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 活动规则服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("activitiesBeautifulStaffServiceDaoImpl")
//@Transactional
public class ActivitiesBeautifulStaffServiceDaoImpl extends BaseServiceDao implements IActivitiesBeautifulStaffServiceDao {

    private static Logger logger = LoggerFactory.getLogger(ActivitiesBeautifulStaffServiceDaoImpl.class);





    /**
     * 保存活动规则信息 到 instance
     * @param info   bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveActivitiesBeautifulStaffInfo(Map info) throws DAOException {
        logger.debug("保存活动规则信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.insert("activitiesBeautifulStaffServiceDaoImpl.saveActivitiesBeautifulStaffInfo",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存活动规则信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询活动规则信息（instance）
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getActivitiesBeautifulStaffInfo(Map info) throws DAOException {
        logger.debug("查询活动规则信息 入参 info : {}",info);

        List<Map> businessActivitiesBeautifulStaffInfos = sqlSessionTemplate.selectList("activitiesBeautifulStaffServiceDaoImpl.getActivitiesBeautifulStaffInfo",info);

        return businessActivitiesBeautifulStaffInfos;
    }


    /**
     * 修改活动规则信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateActivitiesBeautifulStaffInfo(Map info) throws DAOException {
        logger.debug("修改活动规则信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.update("activitiesBeautifulStaffServiceDaoImpl.updateActivitiesBeautifulStaffInfo",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"修改活动规则信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }

     /**
     * 查询活动规则数量
     * @param info 活动规则信息
     * @return 活动规则数量
     */
    @Override
    public int queryActivitiesBeautifulStaffsCount(Map info) {
        logger.debug("查询活动规则数据 入参 info : {}",info);

        List<Map> businessActivitiesBeautifulStaffInfos = sqlSessionTemplate.selectList("activitiesBeautifulStaffServiceDaoImpl.queryActivitiesBeautifulStaffsCount", info);
        if (businessActivitiesBeautifulStaffInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessActivitiesBeautifulStaffInfos.get(0).get("count").toString());
    }


}
