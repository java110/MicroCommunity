package com.java110.user.dao.impl;

import com.alibaba.fastjson.JSONObject;
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

    @Override
    public List<CustAttr> queryDataToCustAttr(CustAttr custAttr) throws RuntimeException {
        return null;
    }

    @Override
    public String queryDataToCustAndCustAttr(String custInfo) throws RuntimeException {
        return null;
    }
}
