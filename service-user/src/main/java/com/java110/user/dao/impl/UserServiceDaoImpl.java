package com.java110.user.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.utils.constant.ResponseConstant;
import com.java110.utils.exception.DAOException;
import com.java110.utils.util.Assert;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.entity.user.BoCust;
import com.java110.entity.user.BoCustAttr;
import com.java110.entity.user.Cust;
import com.java110.entity.user.CustAttr;
import com.java110.user.dao.IUserServiceDao;
import org.slf4j.Logger;
import com.java110.core.log.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 用户服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */

/**
 * 用户信息实现工程
 * Created by wuxw on 2016/12/27.
 */
@Service("userServiceDaoImpl")
@Transactional
public class UserServiceDaoImpl extends BaseServiceDao implements IUserServiceDao {

    private final static Logger logger = LoggerFactory.getLogger(UserServiceDaoImpl.class);


    /**
     * 保存用户基本信息
     * 功能只用与保存用户处理
     *
     * @param boCust 用户基本信息
     * @return
     */
    @Override
    public int saveDataToBoCust(BoCust boCust) throws RuntimeException {

        logger.debug("----【userServiceDaoImpl.saveDataToBoCust】保存数据入参 : " + boCust);
        int saveFlag = 0;
        try {

            saveFlag = sqlSessionTemplate.insert("userServiceDaoImpl.saveDataToBoCust", boCust);

        } catch (RuntimeException e) {
            logger.error("----【userServiceDaoImpl.saveDataToBoCust】保存数据异常 : ", e);
            return saveFlag;
        } finally {
            logger.debug("----【userServiceDaoImpl.saveDataToBoCust】保存数据出参 : saveFlag:" + saveFlag);
        }

        return saveFlag;
    }

    /**
     * 保存用户属性（过程表）
     *
     * @param boCustAttr 用户属性
     * @return
     * @throws RuntimeException
     */
    @Override
    public int saveDataToBoCustAttr(BoCustAttr boCustAttr) throws RuntimeException {

        logger.debug("----【userServiceDaoImpl.saveDataToBoCustAttr】保存数据入参 : " + boCustAttr);
        //为了保险起见，再测检测reqList 是否有值
        if (boCustAttr == null) {
            logger.debug("----【userServiceDaoImpl.saveDataToBoCustAttr】保存数据出错 : " + boCustAttr);
            return 0;
        }
        int saveFlag = 0;

        saveFlag = sqlSessionTemplate.insert("userServiceDaoImpl.saveDataToBoCustAttr", boCustAttr);
        logger.debug("----【userServiceDaoImpl.saveDataToBoCustAttr】保存数据出参 :saveFlag " + saveFlag);

        return saveFlag;

    }

    /**
     * 保存实例数据 客户信息至Cust表中
     *
     * @param cust
     * @return
     * @throws RuntimeException
     */
    @Override
    public int saveDataToCust(Cust cust) throws RuntimeException {
        logger.debug("----【userServiceDaoImpl.saveDataToCust】保存数据入参 : " + cust);
        //为了保险起见，再测检测reqList 是否有值
        if (cust == null) {
            logger.debug("----【userServiceDaoImpl.saveDataToCust】保存数据出错 : " + cust);
            throw new IllegalArgumentException("请求参数错误，cust : " + cust);
        }
        int saveFlag = 0;

        saveFlag = sqlSessionTemplate.insert("userServiceDaoImpl.saveDataToCust", cust);
        logger.debug("----【userServiceDaoImpl.saveDataToCust】保存数据出参 :saveFlag " + saveFlag);

        return saveFlag;
    }

    /**
     * 保存实例数据 客户属性信息至CustAttr表中
     *
     * @param custAttr
     * @return
     * @throws RuntimeException
     */
    @Override
    public int saveDataToCustAttr(CustAttr custAttr) throws RuntimeException {
        logger.debug("----【userServiceDaoImpl.saveDataToCust】保存数据入参 : " + custAttr);
        //为了保险起见，再测检测reqList 是否有值
        if (custAttr == null) {
            logger.debug("----【userServiceDaoImpl.saveDataToCust】保存数据出错 : " + custAttr);
            throw new IllegalArgumentException("请求参数错误，custAttr : " + custAttr);
        }
        int saveFlag = 0;

        saveFlag = sqlSessionTemplate.insert("userServiceDaoImpl.saveDataToCustAttr", custAttr);
        logger.debug("----【userServiceDaoImpl.saveDataToCust】保存数据出参 :saveFlag " + saveFlag);

        return saveFlag;
    }

    /**
     * 删除用户基本信息（实例数据）
     *
     * @param cust
     * @return
     * @throws RuntimeException
     */
    public int deleteDataToCust(Cust cust) throws RuntimeException {
        logger.debug("----【userServiceDaoImpl.deleteDataToCust】保存数据入参 : " + cust);
        //为了保险起见，再测检测reqList 是否有值
        if (cust == null) {
            logger.debug("----【userServiceDaoImpl.deleteDataToCust】保存数据出错 : " + cust);
            throw new IllegalArgumentException("请求参数错误，cust : " + cust);
        }
        int saveFlag = 0;

        saveFlag = sqlSessionTemplate.update("userServiceDaoImpl.deleteDataToCust", cust);
        logger.debug("----【userServiceDaoImpl.deleteDataToCust】保存数据出参 :saveFlag " + saveFlag);

        return saveFlag;
    }

    /**
     * 删除用户属性（实例数据）
     *
     * @param custAttr
     * @return
     * @throws RuntimeException
     */
    public int deleteDataToCustAttr(CustAttr custAttr) throws RuntimeException {
        logger.debug("----【userServiceDaoImpl.deleteDataToCustAttr】保存数据入参 : " + custAttr);
        //为了保险起见，再测检测reqList 是否有值
        if (custAttr == null) {
            logger.debug("----【userServiceDaoImpl.deleteDataToCustAttr】保存数据出错 : " + custAttr);
            throw new IllegalArgumentException("请求参数错误，custAttr : " + custAttr);
        }
        int saveFlag = 0;

        saveFlag = sqlSessionTemplate.update("userServiceDaoImpl.deleteDataToCustAttr", custAttr);
        logger.debug("----【userServiceDaoImpl.deleteDataToCustAttr】保存数据出参 :saveFlag " + saveFlag);

        return saveFlag;
    }

    /**
     * 同时保存客户基本信息和客户属性
     * 入参为：
     *
     * @param boCustInfo 用户信息
     * @return
     * @throws RuntimeException
     */
    @Override
    public String saveDataToBoCustAndBoCustAttr(String boCustInfo) throws RuntimeException {

        logger.debug("----【userServiceDaoImpl.saveDataToBoCustAndBoCustAttr】保存数据入参" + boCustInfo);

        return null;
    }

    @Override
    public String saveDataToCustAndCustAttr(String custInfo) throws RuntimeException {
        return null;
    }

    @Override
    public String updateDataToCust(String cust) throws RuntimeException {
        return null;
    }

    @Override
    public String updateDataToCustAttr(String custAttr) throws RuntimeException {
        return null;
    }

    @Override
    public String updateDataToCustAndCustAttr(String custInfo) throws RuntimeException {
        return null;
    }

    /**
     * 根据客户ID查询客户信息，包括基本信息和属性信息
     *
     * @param cust
     * @return
     * @throws RuntimeException
     */
    @Override
    public Cust queryDataToCust(Cust cust) throws RuntimeException {
        logger.debug("----【userServiceDaoImpl.queryDataToCust】保存数据入参 : " + cust);
        //为了保险起见，再测检测reqList 是否有值
        if (cust == null) {
            logger.debug("----【userServiceDaoImpl.queryDataToCust】保存数据出错 : " + cust);
            throw new IllegalArgumentException("请求参数错误，cust : " + cust);
        }

        Cust newCust = sqlSessionTemplate.selectOne("userServiceDaoImpl.queryDataToCust", cust);

        logger.debug("----【userServiceDaoImpl.queryDataToCust】保存数据出参 :newCust " + newCust);

        return newCust;
    }

    /**
     * 查询客户属性信息
     *
     * @param custAttr 对象
     * @return
     * @throws RuntimeException
     */
    @Override
    public List<CustAttr> queryDataToCustAttr(CustAttr custAttr) throws RuntimeException {
        logger.debug("----【userServiceDaoImpl.queryDataToCustAttr】保存数据入参 : " + custAttr);
        //为了保险起见，再测检测reqList 是否有值
        if (custAttr == null) {
            logger.debug("----【userServiceDaoImpl.queryDataToCust】保存数据出错 : " + custAttr);
            throw new IllegalArgumentException("请求参数错误，CustAttr : " + custAttr);
        }

        List<CustAttr> custAttrs = sqlSessionTemplate.selectList("userServiceDaoImpl.queryDataToCustAttr", custAttr);

        logger.debug("----【userServiceDaoImpl.queryDataToCust】保存数据出参 :custAttrs " + custAttrs);

        return custAttrs;
    }

    @Override
    public String queryDataToCustAndCustAttr(String custInfo) throws RuntimeException {
        return null;
    }

    /**
     * 查询 客户基本信息（过程表bo_cust）
     *
     * @param boCust
     * @return
     * @throws Exception
     */
    public List<BoCust> queryBoCust(BoCust boCust) throws Exception {
        logger.debug("----【userServiceDaoImpl.queryBoCustAttr】:" + boCust);

        Assert.isNull(boCust, "查询bo_cust 入参为空");

        return sqlSessionTemplate.selectList("userServiceDaoImpl.queryBoCust", boCust);
    }

    /**
     * 查询 客户属性信息（过程表 bo_cust_attr）
     *
     * @param boCustAttr
     * @return
     * @throws Exception
     */
    public List<BoCustAttr> queryBoCustAttr(BoCustAttr boCustAttr) throws Exception {

        logger.debug("【userServiceDaoImpl.queryBoCustAttr】:" + boCustAttr);

        Assert.isNull(boCustAttr, "查询bo_cust_attr 入参为空");

        return sqlSessionTemplate.selectList("userServiceDaoImpl.queryBoCustAttr", boCustAttr);

    }

    /**
     * 保存用户信息
     *
     * @param userInfo
     * @throws DAOException
     */
    @Override
    public void saveBusinessUserInfo(Map userInfo) throws DAOException {
        logger.debug("----【userServiceDaoImpl.saveBusinessUserInfo】保存数据入参 : " + JSONObject.toJSONString(userInfo));

        // 查询business_user 数据是否已经存在

        int saveFlag = sqlSessionTemplate.insert("userServiceDaoImpl.saveBusinessUserInfo", userInfo);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存用户数据失败：" + JSONObject.toJSONString(userInfo));
        }
    }

    /**
     * 保存用户属性
     *
     * @param userAttr
     * @throws DAOException
     */
    @Override
    public void saveBusinessUserAttr(Map userAttr) throws DAOException {
        logger.debug("----【userServiceDaoImpl.saveBusinessUserAttr】保存数据入参 : " + JSONObject.toJSONString(userAttr));

        int saveFlag = sqlSessionTemplate.insert("userServiceDaoImpl.saveBusinessUserAttr", userAttr);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存用户属性数据失败：" + JSONObject.toJSONString(userAttr));
        }
    }

    @Override
    public void saveUserInfoInstance(Map businessUser) {
        logger.debug("----【userServiceDaoImpl.saveUserInfoInstance】保存数据入参 : " + JSONObject.toJSONString(businessUser));
        int saveFlag = sqlSessionTemplate.insert("userServiceDaoImpl.saveUserInfoInstance", businessUser);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存用户Instance数据失败：" + JSONObject.toJSONString(businessUser));
        }
    }

    @Override
    public void saveUserAttrInstance(Map attrInstance) {
        logger.debug("----【userServiceDaoImpl.saveUserAttrInstance】保存数据入参 : " + JSONObject.toJSONString(attrInstance));
        int saveFlag = sqlSessionTemplate.insert("userServiceDaoImpl.saveUserAttrInstance", attrInstance);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存用户Instance数据失败：" + JSONObject.toJSONString(attrInstance));
        }
    }

    @Override
    public void updateUserInfoInstance(Map businessUser) {
        logger.debug("----【userServiceDaoImpl.updateUserInfoInstance】保存数据入参 : " + JSONObject.toJSONString(businessUser));
        int saveFlag = sqlSessionTemplate.update("userServiceDaoImpl.updateUserInfoInstance", businessUser);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "修改用户Instance数据失败：" + JSONObject.toJSONString(businessUser));
        }
    }

    @Override
    public void updateUserAttrInstance(Map attrInstance) {
        logger.debug("----【userServiceDaoImpl.updateUserAttrInstance】保存数据入参 : " + JSONObject.toJSONString(attrInstance));
        int saveFlag = sqlSessionTemplate.update("userServiceDaoImpl.updateUserAttrInstance", attrInstance);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "修改用户Instance数据失败：" + JSONObject.toJSONString(attrInstance));
        }
    }


    /**
     * 查询用户信息
     *
     * @param info
     * @return
     * @throws DAOException
     */
    public Map queryBusinessUserInfo(Map info) throws DAOException {
        Assert.notNull(info, "queryBusinessUserInfo 的参数不能为空");
        logger.debug("----【userServiceDaoImpl.queryBusinessUserInfo】保存数据入参 : " + JSONObject.toJSONString(info));
        List<Map> users = sqlSessionTemplate.selectList("userServiceDaoImpl.queryBusinessUserInfo", info);
        if (users == null || users.size() == 0) {
            return null;
        }
        return users.get(0);
    }

    /**
     * 查询用户信息
     *
     * @param info
     * @return
     * @throws DAOException
     */
    public List<Map> queryBusinessUserInfoAttrs(Map info) throws DAOException {
        Assert.notNull(info, "queryBusinessUserInfoAttrs 的参数不能为空");
        logger.debug("----【userServiceDaoImpl.queryBusinessUserInfoAttrs】保存数据入参 : " + JSONObject.toJSONString(info));
        List<Map> userAttrs = sqlSessionTemplate.selectList("userServiceDaoImpl.queryBusinessUserInfoAttrs", info);
        return userAttrs;
    }

    /**
     * 查询用户信息
     *
     * @param info
     * @return
     * @throws DAOException
     */
    @Override
    public Map queryUserInfo(Map info) throws DAOException {
        Assert.notNull(info, "queryUserInfo 的参数不能为空");
        logger.debug("----【userServiceDaoImpl.queryUserInfo】保存数据入参 : " + JSONObject.toJSONString(info));
        List<Map> users = sqlSessionTemplate.selectList("userServiceDaoImpl.queryUserInfo", info);
        if (users == null || users.size() == 0) {
            return null;
        }
        return users.get(0);
    }

    /**
     * 查询用户信息
     *
     * @param info
     * @return
     * @throws DAOException
     */
    @Override
    public List<Map> queryUsersInfo(Map info) throws DAOException {
        Assert.notNull(info, "queryUserInfo 的参数不能为空");
        logger.debug("----【userServiceDaoImpl.queryUserInfo】保存数据入参 : " + JSONObject.toJSONString(info));
        List<Map> users = sqlSessionTemplate.selectList("userServiceDaoImpl.queryUserInfo", info);

        return users;
    }

    /**
     * 查询用户信息
     *
     * @param info
     * @return
     * @throws DAOException
     */
    @Override
    public List<Map> queryUserInfoAttrs(Map info) throws DAOException {
        Assert.notNull(info, "queryUserInfo 的参数不能为空");
        logger.debug("----【userServiceDaoImpl.queryUserInfoAttrs】查询用户属性 : " + JSONObject.toJSONString(info));
        List<Map> userAttrs = sqlSessionTemplate.selectList("userServiceDaoImpl.queryUserInfoAttrs", info);
        return userAttrs;
    }

    /**
     * 保存用户地址信息
     * Business 过程
     *
     * @param userAddress 用户地址信息
     * @throws DAOException
     */
    public void saveBusinessUserAddress(Map userAddress) throws DAOException {
        logger.debug("----【userServiceDaoImpl.saveBusinessUserAddress】保存数据入参 : " + JSONObject.toJSONString(userAddress));
        int saveFlag = sqlSessionTemplate.insert("userServiceDaoImpl.saveBusinessUserAddress", userAddress);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存用户地址数据失败：" + JSONObject.toJSONString(userAddress));
        }
    }

    /**
     * 查询用户地址信息
     * business 过程
     *
     * @param info b_id
     * @return 查询到的用户地址信息
     * @throws DAOException
     */
    public Map queryBusinessUserAddress(Map info) throws DAOException {
        Assert.notNull(info, "queryBusinessUserAddress 的参数不能为空");
        logger.debug("----【userServiceDaoImpl.queryBusinessUserAddress】保存数据入参 : " + JSONObject.toJSONString(info));
        List<Map> users = sqlSessionTemplate.selectList("userServiceDaoImpl.queryBusinessUserAddress", info);
        if (users == null || users.size() == 0) {
            return null;
        }
        return users.get(0);
    }

    /**
     * 保存用户地址 Business 数据到 Instance
     *
     * @param businessUserAddress 从business 中查出的数据
     * @throws DAOException 数据处理异常
     */
    public void saveUserAddressInstance(Map businessUserAddress) throws DAOException {
        logger.debug("----【userServiceDaoImpl.saveUserAddressInstance】保存数据入参 : " + JSONObject.toJSONString(businessUserAddress));
        int saveFlag = sqlSessionTemplate.insert("userServiceDaoImpl.saveUserAddressInstance", businessUserAddress);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存用户地址Instance数据失败：" + JSONObject.toJSONString(businessUserAddress));
        }
    }

    /**
     * 作废用户地址信息数据
     *
     * @param businessUserAddress 用户地址信息 b_id
     * @throws DAOException 数据处理异常
     */
    public void updateUserAddressInstance(Map businessUserAddress) throws DAOException {
        logger.debug("----【userServiceDaoImpl.updateUserAddressInstance】保存数据入参 : " + JSONObject.toJSONString(businessUserAddress));
        int saveFlag = sqlSessionTemplate.update("userServiceDaoImpl.updateUserAddressInstance", businessUserAddress);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "修改用户Instance数据失败：" + JSONObject.toJSONString(businessUserAddress));
        }
    }


    /**
     * 保存用户打标信息
     * Business 过程
     *
     * @param userTag 用户打标信息
     * @throws DAOException
     */
    public void saveBusinessUserTag(Map userTag) throws DAOException {
        logger.debug("----【userServiceDaoImpl.saveBusinessUserTag】保存数据入参 : " + JSONObject.toJSONString(userTag));
        int saveFlag = sqlSessionTemplate.insert("userServiceDaoImpl.saveBusinessUserTag", userTag);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存用户打标数据失败：" + JSONObject.toJSONString(userTag));
        }
    }


    /**
     * 查询用户打标信息
     * business 过程
     *
     * @param info b_id
     * @return 查询到的用户打标信息
     * @throws DAOException
     */
    public Map queryBusinessUserTag(Map info) throws DAOException {
        Assert.notNull(info, "queryBusinessUserTag 的参数不能为空");
        logger.debug("----【userServiceDaoImpl.queryBusinessUserTag】保存数据入参 : " + JSONObject.toJSONString(info));
        List<Map> users = sqlSessionTemplate.selectList("userServiceDaoImpl.queryBusinessUserTag", info);
        if (users == null || users.size() == 0) {
            return null;
        }
        return users.get(0);
    }

    /**
     * 保存Business 数据到 Instance
     *
     * @param businessUserTag 从business 中查出的数据
     * @throws DAOException 数据处理异常
     */
    public void saveUserTagInstance(Map businessUserTag) throws DAOException {
        logger.debug("----【userServiceDaoImpl.saveUserTagInstance】保存数据入参 : " + JSONObject.toJSONString(businessUserTag));
        int saveFlag = sqlSessionTemplate.insert("userServiceDaoImpl.saveUserTagInstance", businessUserTag);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存用户打标Instance数据失败：" + JSONObject.toJSONString(businessUserTag));
        }
    }


    /**
     * 作废用户打标数据
     *
     * @param businessUserTag 用户地址信息 b_id
     * @throws DAOException 数据处理异常
     */
    public void updateUserTagInstance(Map businessUserTag) throws DAOException {
        logger.debug("----【userServiceDaoImpl.updateUserTagInstance】保存数据入参 : " + JSONObject.toJSONString(businessUserTag));
        int saveFlag = sqlSessionTemplate.update("userServiceDaoImpl.updateUserTagInstance", businessUserTag);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "修改用户Instance数据失败：" + JSONObject.toJSONString(businessUserTag));
        }
    }


    /**
     * 保存用户证件信息
     * Business 过程
     *
     * @param userCredentials 用户证件信息
     * @throws DAOException
     */
    public void saveBusinessUserCredentials(Map userCredentials) throws DAOException {
        logger.debug("----【userServiceDaoImpl.saveBusinessUserCredentials】保存数据入参 : " + JSONObject.toJSONString(userCredentials));
        int saveFlag = sqlSessionTemplate.insert("userServiceDaoImpl.saveBusinessUserCredentials", userCredentials);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存用户打标数据失败：" + JSONObject.toJSONString(userCredentials));
        }
    }


    /**
     * 查询用户证件信息
     * business 过程
     *
     * @param info b_id
     * @return 查询到的用户打标信息
     * @throws DAOException
     */
    public Map queryBusinessUserCredentials(Map info) throws DAOException {
        Assert.notNull(info, "queryBusinessUserCredentials 的参数不能为空");
        logger.debug("----【userServiceDaoImpl.queryBusinessUserCredentials】保存数据入参 : " + JSONObject.toJSONString(info));
        List<Map> users = sqlSessionTemplate.selectList("userServiceDaoImpl.queryBusinessUserCredentials", info);
        if (users == null || users.size() == 0) {
            return null;
        }
        return users.get(0);
    }

    /**
     * 保存Business 数据到 Instance
     *
     * @param businessUserCredentials 从business 中查出的数据
     * @throws DAOException 数据处理异常
     */
    public void saveUserCredentialsInstance(Map businessUserCredentials) throws DAOException {
        logger.debug("----【userServiceDaoImpl.saveUserCredentialsInstance】保存数据入参 : " + JSONObject.toJSONString(businessUserCredentials));
        int saveFlag = sqlSessionTemplate.insert("userServiceDaoImpl.saveUserCredentialsInstance", businessUserCredentials);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "保存用户打标Instance数据失败：" + JSONObject.toJSONString(businessUserCredentials));
        }
    }


    /**
     * 作废用户证件数据
     *
     * @param businessUserCredentials 用户地址信息 b_id
     * @throws DAOException 数据处理异常
     */
    public void updateUserCredentialsInstance(Map businessUserCredentials) throws DAOException {
        logger.debug("----【userServiceDaoImpl.updateUserCredentialsInstance】保存数据入参 : " + JSONObject.toJSONString(businessUserCredentials));
        int saveFlag = sqlSessionTemplate.update("userServiceDaoImpl.updateUserCredentialsInstance", businessUserCredentials);

        if (saveFlag < 1) {
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR, "修改用户Instance数据失败：" + JSONObject.toJSONString(businessUserCredentials));
        }
    }

    @Override
    public int getStaffCount(Map businessUser) throws DAOException {
        logger.debug("查询组织数据 入参 info : {}",businessUser);

        List<Map> businessStaffInfos = sqlSessionTemplate.selectList("userServiceDaoImpl.getStaffCount", businessUser);
        if (businessStaffInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessStaffInfos.get(0).get("count").toString());
    }

    @Override
    public List<Map> getStaffs(Map info) throws DAOException {
        logger.debug("查询组织信息 入参 info : {}",info);

        List<Map> businessStaffs = sqlSessionTemplate.selectList("userServiceDaoImpl.getStaffs",info);

        return businessStaffs;
    }

    @Override
    public int getUserCount(Map businessUser) throws DAOException {
        logger.debug("查询组织数据 入参 info : {}",businessUser);

        List<Map> businessStaffInfos = sqlSessionTemplate.selectList("userServiceDaoImpl.getUserCount", businessUser);
        if (businessStaffInfos.size() < 1) {
            return 0;
        }

        return Integer.parseInt(businessStaffInfos.get(0).get("count").toString());
    }

    @Override
    public List<Map> getUsers(Map info) throws DAOException {
        logger.debug("查询组织信息 入参 info : {}",info);

        List<Map> businessStaffs = sqlSessionTemplate.selectList("userServiceDaoImpl.getUsers",info);

        return businessStaffs;
    }

    /**
     * 查询用户密码
     * 根据bId 查询组织信息
     * @param info bId 信息
     * @return 组织信息
     * @throws DAOException DAO异常
     */
    @Override
    public List<Map> getUserHasPwd(Map info) throws DAOException{
        logger.debug("查询用户密码 入参 info : {}",info);

        List<Map> businessStaffs = sqlSessionTemplate.selectList("userServiceDaoImpl.getUserHasPwd",info);
       return businessStaffs;
    }
}
