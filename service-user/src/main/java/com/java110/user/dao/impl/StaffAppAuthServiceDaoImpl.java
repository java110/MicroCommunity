package com.java110.user.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.utils.util.DateUtil;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.user.dao.IStaffAppAuthServiceDao;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 员工微信认证服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("staffAppAuthServiceDaoImpl")
//@Transactional
public class StaffAppAuthServiceDaoImpl extends BaseServiceDao implements IStaffAppAuthServiceDao {

    private static Logger logger = LoggerFactory.getLogger(StaffAppAuthServiceDaoImpl.class);





    /**
     * 保存员工微信认证信息 到 instance
     * @param info   bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveStaffAppAuthInfo(Map info) throws DAOException {
        logger.debug("保存员工微信认证信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.insert("staffAppAuthServiceDaoImpl.saveStaffAppAuthInfo",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存员工微信认证信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询员工微信认证信息（instance）
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getStaffAppAuthInfo(Map info) throws DAOException {
        logger.debug("查询员工微信认证信息 入参 info : {}",info);

        List<Map> businessStaffAppAuthInfos = sqlSessionTemplate.selectList("staffAppAuthServiceDaoImpl.getStaffAppAuthInfo",info);

        return businessStaffAppAuthInfos;
    }


    /**
     * 修改员工微信认证信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateStaffAppAuthInfo(Map info) throws DAOException {
        logger.debug("修改员工微信认证信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.update("staffAppAuthServiceDaoImpl.updateStaffAppAuthInfo",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"修改员工微信认证信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }

     /**
     * 查询员工微信认证数量
     * @param info 员工微信认证信息
     * @return 员工微信认证数量
     */
    @Override
    public int queryStaffAppAuthsCount(Map info) {
        logger.debug("查询员工微信认证数据 入参 info : {}",info);

        List<Map> businessStaffAppAuthInfos = sqlSessionTemplate.selectList("staffAppAuthServiceDaoImpl.queryStaffAppAuthsCount", info);
        if (businessStaffAppAuthInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessStaffAppAuthInfos.get(0).get("count").toString());
    }


}
