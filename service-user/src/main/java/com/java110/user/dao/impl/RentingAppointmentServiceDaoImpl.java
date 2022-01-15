package com.java110.user.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.utils.util.DateUtil;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.user.dao.IRentingAppointmentServiceDao;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 租赁预约服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("rentingAppointmentServiceDaoImpl")
//@Transactional
public class RentingAppointmentServiceDaoImpl extends BaseServiceDao implements IRentingAppointmentServiceDao {

    private static Logger logger = LoggerFactory.getLogger(RentingAppointmentServiceDaoImpl.class);





    /**
     * 保存租赁预约信息 到 instance
     * @param info   bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveRentingAppointmentInfo(Map info) throws DAOException {
        logger.debug("保存租赁预约信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.insert("rentingAppointmentServiceDaoImpl.saveRentingAppointmentInfo",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存租赁预约信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询租赁预约信息（instance）
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getRentingAppointmentInfo(Map info) throws DAOException {
        logger.debug("查询租赁预约信息 入参 info : {}",info);

        List<Map> businessRentingAppointmentInfos = sqlSessionTemplate.selectList("rentingAppointmentServiceDaoImpl.getRentingAppointmentInfo",info);

        return businessRentingAppointmentInfos;
    }


    /**
     * 修改租赁预约信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateRentingAppointmentInfo(Map info) throws DAOException {
        logger.debug("修改租赁预约信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.update("rentingAppointmentServiceDaoImpl.updateRentingAppointmentInfo",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"修改租赁预约信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }

     /**
     * 查询租赁预约数量
     * @param info 租赁预约信息
     * @return 租赁预约数量
     */
    @Override
    public int queryRentingAppointmentsCount(Map info) {
        logger.debug("查询租赁预约数据 入参 info : {}",info);

        List<Map> businessRentingAppointmentInfos = sqlSessionTemplate.selectList("rentingAppointmentServiceDaoImpl.queryRentingAppointmentsCount", info);
        if (businessRentingAppointmentInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessRentingAppointmentInfos.get(0).get("count").toString());
    }


}
