package com.java110.user.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.java110.common.exception.DAOException;
import com.java110.common.util.Assert;
import com.java110.entity.user.BoCust;
import com.java110.entity.user.BoCustAttr;
import com.java110.entity.user.Cust;
import com.java110.entity.user.CustAttr;
import com.java110.user.dao.IUserServiceDao;
import com.java110.common.log.LoggerEngine;
import com.java110.common.constant.CommonConstant;
import com.java110.common.constant.ResponseConstant;
import com.java110.common.util.ProtocolUtil;
import com.java110.core.base.dao.BaseServiceDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

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



    /**
     * 保存用户基本信息
     * 功能只用与保存用户处理
     * @param boCust 用户基本信息
     * @return
     */
    @Override
    public int saveDataToBoCust(BoCust boCust) throws RuntimeException{

        LoggerEngine.debug("----【UserServiceDAOImpl.saveDataToBoCust】保存数据入参 : " + boCust);
        int saveFlag = 0;
        try {

            saveFlag = sqlSessionTemplate.insert("userServiceDAOImpl.saveDataToBoCust",boCust);

        }catch(RuntimeException e){
            LoggerEngine.error("----【UserServiceDAOImpl.saveDataToBoCust】保存数据异常 : " ,e);
            return saveFlag;
        }finally {
            LoggerEngine.debug("----【UserServiceDAOImpl.saveDataToBoCust】保存数据出参 : saveFlag:" + saveFlag);
            return saveFlag;
        }

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

        LoggerEngine.debug("----【UserServiceDAOImpl.saveDataToBoCustAttr】保存数据入参 : " + boCustAttr);
        //为了保险起见，再测检测reqList 是否有值
        if(boCustAttr == null){
            LoggerEngine.debug("----【UserServiceDAOImpl.saveDataToBoCustAttr】保存数据出错 : " + boCustAttr);
            return 0;
        }
        int saveFlag = 0;

        saveFlag = sqlSessionTemplate.insert("userServiceDAOImpl.saveDataToBoCustAttr",boCustAttr);
        LoggerEngine.debug("----【UserServiceDAOImpl.saveDataToBoCustAttr】保存数据出参 :saveFlag " + saveFlag);

        return saveFlag;

    }

    /**
     * 保存实例数据 客户信息至Cust表中
     * @param cust
     * @return
     * @throws RuntimeException
     */
    @Override
    public int saveDataToCust(Cust cust) throws RuntimeException {
        LoggerEngine.debug("----【UserServiceDAOImpl.saveDataToCust】保存数据入参 : " + cust);
        //为了保险起见，再测检测reqList 是否有值
        if(cust == null){
            LoggerEngine.debug("----【UserServiceDAOImpl.saveDataToCust】保存数据出错 : " + cust);
            throw new IllegalArgumentException("请求参数错误，cust : " + cust);
        }
        int saveFlag = 0;

        saveFlag = sqlSessionTemplate.insert("userServiceDAOImpl.saveDataToCust",cust);
        LoggerEngine.debug("----【UserServiceDAOImpl.saveDataToCust】保存数据出参 :saveFlag " + saveFlag);

        return saveFlag;
    }

    /**
     * 保存实例数据 客户属性信息至CustAttr表中
     * @param custAttr
     * @return
     * @throws RuntimeException
     */
    @Override
    public int saveDataToCustAttr(CustAttr custAttr) throws RuntimeException {
        LoggerEngine.debug("----【UserServiceDAOImpl.saveDataToCust】保存数据入参 : " + custAttr);
        //为了保险起见，再测检测reqList 是否有值
        if(custAttr == null){
            LoggerEngine.debug("----【UserServiceDAOImpl.saveDataToCust】保存数据出错 : " + custAttr);
            throw new IllegalArgumentException("请求参数错误，custAttr : " + custAttr);
        }
        int saveFlag = 0;

        saveFlag = sqlSessionTemplate.insert("userServiceDAOImpl.saveDataToCustAttr",custAttr);
        LoggerEngine.debug("----【UserServiceDAOImpl.saveDataToCust】保存数据出参 :saveFlag " + saveFlag);

        return saveFlag;
    }

    /**
     * 删除用户基本信息（实例数据）
     * @param cust
     * @return
     * @throws RuntimeException
     */
    public int deleteDataToCust(Cust cust) throws RuntimeException{
        LoggerEngine.debug("----【UserServiceDAOImpl.deleteDataToCust】保存数据入参 : " + cust);
        //为了保险起见，再测检测reqList 是否有值
        if(cust == null){
            LoggerEngine.debug("----【UserServiceDAOImpl.deleteDataToCust】保存数据出错 : " + cust);
            throw new IllegalArgumentException("请求参数错误，cust : " + cust);
        }
        int saveFlag = 0;

        saveFlag = sqlSessionTemplate.update("userServiceDAOImpl.deleteDataToCust",cust);
        LoggerEngine.debug("----【UserServiceDAOImpl.deleteDataToCust】保存数据出参 :saveFlag " + saveFlag);

        return saveFlag;
    }

    /**
     *  删除用户属性（实例数据）
     * @param custAttr
     * @return
     * @throws RuntimeException
     */
    public int deleteDataToCustAttr(CustAttr custAttr) throws RuntimeException{
        LoggerEngine.debug("----【UserServiceDAOImpl.deleteDataToCustAttr】保存数据入参 : " + custAttr);
        //为了保险起见，再测检测reqList 是否有值
        if(custAttr == null){
            LoggerEngine.debug("----【UserServiceDAOImpl.deleteDataToCustAttr】保存数据出错 : " + custAttr);
            throw new IllegalArgumentException("请求参数错误，custAttr : " + custAttr);
        }
        int saveFlag = 0;

        saveFlag = sqlSessionTemplate.update("userServiceDAOImpl.deleteDataToCustAttr",custAttr);
        LoggerEngine.debug("----【UserServiceDAOImpl.deleteDataToCustAttr】保存数据出参 :saveFlag " + saveFlag);

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

        LoggerEngine.debug("----【UserServiceDAOImpl.saveDataToBoCustAndBoCustAttr】保存数据入参"+boCustInfo);

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
     * @param cust
     * @return
     * @throws RuntimeException
     */
    @Override
    public Cust queryDataToCust(Cust cust) throws RuntimeException {
        LoggerEngine.debug("----【UserServiceDAOImpl.queryDataToCust】保存数据入参 : " + cust);
        //为了保险起见，再测检测reqList 是否有值
        if(cust == null){
            LoggerEngine.debug("----【UserServiceDAOImpl.queryDataToCust】保存数据出错 : " + cust);
            throw new IllegalArgumentException("请求参数错误，cust : " + cust);
        }

        Cust newCust  = sqlSessionTemplate.selectOne("userServiceDAOImpl.queryDataToCust",cust);

        LoggerEngine.debug("----【UserServiceDAOImpl.queryDataToCust】保存数据出参 :newCust " + newCust);

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
        LoggerEngine.debug("----【UserServiceDAOImpl.queryDataToCustAttr】保存数据入参 : " + custAttr);
        //为了保险起见，再测检测reqList 是否有值
        if(custAttr == null){
            LoggerEngine.debug("----【UserServiceDAOImpl.queryDataToCust】保存数据出错 : " + custAttr);
            throw new IllegalArgumentException("请求参数错误，CustAttr : " + custAttr);
        }

        List<CustAttr> custAttrs  = sqlSessionTemplate.selectList("userServiceDAOImpl.queryDataToCustAttr",custAttr);

        LoggerEngine.debug("----【UserServiceDAOImpl.queryDataToCust】保存数据出参 :custAttrs " + custAttrs);

        return custAttrs;
    }

    @Override
    public String queryDataToCustAndCustAttr(String custInfo) throws RuntimeException {
        return null;
    }

    /**
     *
     * 查询 客户基本信息（过程表bo_cust）
     *
     * @param boCust
     * @return
     * @throws Exception
     */
    public List<BoCust> queryBoCust(BoCust boCust) throws Exception{
        LoggerEngine.debug("----【UserServiceDAOImpl.queryBoCustAttr】:"+boCust);

        Assert.isNull(boCust,"查询bo_cust 入参为空");

        return sqlSessionTemplate.selectList("userServiceDAOImpl.queryBoCust",boCust);
    }

    /**
     *
     * 查询 客户属性信息（过程表 bo_cust_attr）
     *
     * @param boCustAttr
     * @return
     * @throws Exception
     */
    public List<BoCustAttr> queryBoCustAttr(BoCustAttr boCustAttr) throws Exception{

        LoggerEngine.debug("【userServiceDAOImpl.queryBoCustAttr】:"+boCustAttr);

        Assert.isNull(boCustAttr,"查询bo_cust_attr 入参为空");

        return sqlSessionTemplate.selectList("userServiceDAOImpl.queryBoCustAttr",boCustAttr);

    }

    /**
     * 保存用户信息
     * @param userInfo
     * @throws DAOException
     */
    @Override
    public void saveBusinessUserInfo(Map userInfo) throws DAOException {
        LoggerEngine.debug("----【UserServiceDAOImpl.saveBusinessUserInfo】保存数据入参 : " + JSONObject.toJSONString(userInfo));

        // 查询business_user 数据是否已经存在

        int saveFlag = sqlSessionTemplate.insert("userServiceDAOImpl.saveBusinessUserInfo",userInfo);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存用户数据失败："+JSONObject.toJSONString(userInfo));
        }
    }
    /**
     * 保存用户属性
     * @param userAttr
     * @throws DAOException
     */
    @Override
    public void saveBusinessUserAttr(Map userAttr) throws DAOException {
        LoggerEngine.debug("----【UserServiceDAOImpl.saveBusinessUserAttr】保存数据入参 : " + JSONObject.toJSONString(userAttr));

        int saveFlag = sqlSessionTemplate.insert("userServiceDAOImpl.saveBusinessUserAttr",userAttr);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存用户属性数据失败："+JSONObject.toJSONString(userAttr));
        }
    }

    @Override
    public void saveUserInfoInstance(Map businessUser) {
        LoggerEngine.debug("----【UserServiceDAOImpl.saveUserInfoInstance】保存数据入参 : " + JSONObject.toJSONString(businessUser));
        int saveFlag = sqlSessionTemplate.insert("userServiceDAOImpl.saveUserInfoInstance",businessUser);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存用户Instance数据失败："+JSONObject.toJSONString(businessUser));
        }
    }

    @Override
    public void saveUserAttrInstance(Map attrInstance) {
        LoggerEngine.debug("----【UserServiceDAOImpl.saveUserAttrInstance】保存数据入参 : " + JSONObject.toJSONString(attrInstance));
        int saveFlag = sqlSessionTemplate.insert("userServiceDAOImpl.saveUserAttrInstance",attrInstance);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"保存用户Instance数据失败："+JSONObject.toJSONString(attrInstance));
        }
    }

    @Override
    public void updateUserInfoInstance(Map businessUser) {
        LoggerEngine.debug("----【UserServiceDAOImpl.updateUserInfoInstance】保存数据入参 : " + JSONObject.toJSONString(businessUser));
        int saveFlag = sqlSessionTemplate.insert("userServiceDAOImpl.updateUserInfoInstance",businessUser);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"修改用户Instance数据失败："+JSONObject.toJSONString(businessUser));
        }
    }

    @Override
    public void updateUserAttrInstance(Map attrInstance) {
        LoggerEngine.debug("----【UserServiceDAOImpl.updateUserAttrInstance】保存数据入参 : " + JSONObject.toJSONString(attrInstance));
        int saveFlag = sqlSessionTemplate.insert("userServiceDAOImpl.updateUserAttrInstance",attrInstance);

        if(saveFlag < 1){
            throw new DAOException(ResponseConstant.RESULT_PARAM_ERROR,"修改用户Instance数据失败："+JSONObject.toJSONString(attrInstance));
        }
    }

    /**
     * 查询用户信息
     * @param info
     * @return
     * @throws DAOException
     */
    public Map queryBusinessUserInfo(Map info) throws DAOException{
        Assert.notNull(info,"queryBusinessUserInfo 的参数不能为空");
        LoggerEngine.debug("----【UserServiceDAOImpl.queryBusinessUserInfo】保存数据入参 : " + JSONObject.toJSONString(info));
        List<Map> users = sqlSessionTemplate.selectList("userServiceDAOImpl.queryBusinessUserInfo",info);
        if(users == null || users.size() == 0){
            return null;
        }
        return users.get(0);
    }

    /**
     * 查询用户信息
     * @param info
     * @return
     * @throws DAOException
     */
    public List<Map> queryBusinessUserInfoAttrs(Map info) throws DAOException{
        Assert.notNull(info,"queryBusinessUserInfoAttrs 的参数不能为空");
        LoggerEngine.debug("----【UserServiceDAOImpl.queryBusinessUserInfoAttrs】保存数据入参 : " + JSONObject.toJSONString(info));
        List<Map> userAttrs = sqlSessionTemplate.selectList("userServiceDAOImpl.queryBusinessUserInfoAttrs",info);
        return userAttrs;
    }

    /**
     * 查询用户信息
     * @param info
     * @return
     * @throws DAOException
     */
    @Override
    public Map queryUserInfo(Map info) throws DAOException{
        Assert.notNull(info,"queryUserInfo 的参数不能为空");
        LoggerEngine.debug("----【UserServiceDAOImpl.queryUserInfo】保存数据入参 : " + JSONObject.toJSONString(info));
        List<Map> users = sqlSessionTemplate.selectList("userServiceDAOImpl.queryUserInfo",info);
        if(users == null || users.size() == 0){
            return null;
        }
        return users.get(0);
    }

    /**
     * 查询用户信息
     * @param info
     * @return
     * @throws DAOException
     */
    @Override
    public List<Map> queryUserInfoAttrs(Map info) throws DAOException{
        Assert.notNull(info,"queryUserInfo 的参数不能为空");
        LoggerEngine.debug("----【UserServiceDAOImpl.updateUserAttrInstance】保存数据入参 : " + JSONObject.toJSONString(info));
        List<Map> userAttrs = sqlSessionTemplate.selectList("userServiceDAOImpl.queryUserInfoAttrs",info);
        return userAttrs;
    }
}
