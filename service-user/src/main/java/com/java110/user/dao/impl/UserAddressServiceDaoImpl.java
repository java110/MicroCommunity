package com.java110.user.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.utils.util.DateUtil;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.user.dao.IUserAddressServiceDao;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 用户联系地址服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("userAddressServiceDaoImpl")
//@Transactional
public class UserAddressServiceDaoImpl extends BaseServiceDao implements IUserAddressServiceDao {

    private static Logger logger = LoggerFactory.getLogger(UserAddressServiceDaoImpl.class);





    /**
     * 保存用户联系地址信息 到 instance
     * @param info   bId 信息
     * @throws DAOException DAO异常
     */
    @Override
    public void saveUserAddressInfo(Map info) throws DAOException {
        logger.debug("保存用户联系地址信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.insert("userAddressServiceDaoImpl.saveUserAddressInfo",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存用户联系地址信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }


    /**
     * 查询用户联系地址信息（instance）
     * @param info bId 信息
     * @return List<Map>
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getUserAddressInfo(Map info) throws DAOException {
        logger.debug("查询用户联系地址信息 入参 info : {}",info);

        List<Map> businessUserAddressInfos = sqlSessionTemplate.selectList("userAddressServiceDaoImpl.getUserAddressInfo",info);

        return businessUserAddressInfos;
    }


    /**
     * 修改用户联系地址信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    @Override
    public void updateUserAddressInfo(Map info) throws DAOException {
        logger.debug("修改用户联系地址信息Instance 入参 info : {}",info);

        int saveFlag = sqlSessionTemplate.update("userAddressServiceDaoImpl.updateUserAddressInfo",info);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"修改用户联系地址信息Instance数据失败："+ JSONObject.toJSONString(info));
        }
    }

     /**
     * 查询用户联系地址数量
     * @param info 用户联系地址信息
     * @return 用户联系地址数量
     */
    @Override
    public int queryUserAddresssCount(Map info) {
        logger.debug("查询用户联系地址数据 入参 info : {}",info);

        List<Map> businessUserAddressInfos = sqlSessionTemplate.selectList("userAddressServiceDaoImpl.queryUserAddresssCount", info);
        if (businessUserAddressInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessUserAddressInfos.get(0).get("count").toString());
    }


}
