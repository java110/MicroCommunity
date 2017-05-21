package com.java110.merchant.dao.impl;

import com.java110.common.log.LoggerEngine;
import com.java110.common.util.Assert;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.entity.merchant.BoMerchant;
import com.java110.entity.merchant.BoMerchantAttr;
import com.java110.entity.merchant.Merchant;
import com.java110.entity.merchant.MerchantAttr;
import com.java110.merchant.dao.IMerchantServiceDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 商户服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */
@Service("merchantServiceDaoImpl")
@Transactional
public class MerchantServiceDaoImpl extends BaseServiceDao implements IMerchantServiceDao {



    /**
     * 保存用户基本信息
     * 功能只用与保存用户处理
     * @param boMerchant 用户基本信息
     * @return
     */
    @Override
    public int saveDataToBoMerchant(BoMerchant boMerchant) throws RuntimeException{

        LoggerEngine.debug("----【UserServiceDAOImpl.saveDataToBoMerchant】保存数据入参 : " + boMerchant);
        int saveFlag = 0;
        try {

            saveFlag = sqlSessionTemplate.insert("userServiceDAOImpl.saveDataToBoMerchant",boMerchant);

        }catch(RuntimeException e){
            LoggerEngine.error("----【UserServiceDAOImpl.saveDataToBoMerchant】保存数据异常 : " ,e);
            return saveFlag;
        }finally {
            LoggerEngine.debug("----【UserServiceDAOImpl.saveDataToBoMerchant】保存数据出参 : saveFlag:" + saveFlag);
            return saveFlag;
        }

    }

    /**
     * 保存用户属性（过程表）
     *
     * @param boMerchantAttr 用户属性
     * @return
     * @throws RuntimeException
     */
    @Override
    public int saveDataToBoMerchantAttr(BoMerchantAttr boMerchantAttr) throws RuntimeException {

        LoggerEngine.debug("----【UserServiceDAOImpl.saveDataToBoMerchantAttr】保存数据入参 : " + boMerchantAttr);
        //为了保险起见，再测检测reqList 是否有值
        if(boMerchantAttr == null){
            LoggerEngine.debug("----【UserServiceDAOImpl.saveDataToBoMerchantAttr】保存数据出错 : " + boMerchantAttr);
            return 0;
        }
        int saveFlag = 0;

        saveFlag = sqlSessionTemplate.insert("userServiceDAOImpl.saveDataToBoMerchantAttr",boMerchantAttr);
        LoggerEngine.debug("----【UserServiceDAOImpl.saveDataToBoMerchantAttr】保存数据出参 :saveFlag " + saveFlag);

        return saveFlag;

    }

    /**
     * 保存实例数据 客户信息至Merchant表中
     * @param merchant
     * @return
     * @throws RuntimeException
     */
    @Override
    public int saveDataToMerchant(Merchant merchant) throws RuntimeException {
        LoggerEngine.debug("----【UserServiceDAOImpl.saveDataToMerchant】保存数据入参 : " + merchant);
        //为了保险起见，再测检测reqList 是否有值
        if(merchant == null){
            LoggerEngine.debug("----【UserServiceDAOImpl.saveDataToMerchant】保存数据出错 : " + merchant);
            throw new IllegalArgumentException("请求参数错误，merchant : " + merchant);
        }
        int saveFlag = 0;

        saveFlag = sqlSessionTemplate.insert("userServiceDAOImpl.saveDataToMerchant",merchant);
        LoggerEngine.debug("----【UserServiceDAOImpl.saveDataToMerchant】保存数据出参 :saveFlag " + saveFlag);

        return saveFlag;
    }

    /**
     * 保存实例数据 客户属性信息至MerchantAttr表中
     * @param merchantAttr
     * @return
     * @throws RuntimeException
     */
    @Override
    public int saveDataToMerchantAttr(MerchantAttr merchantAttr) throws RuntimeException {
        LoggerEngine.debug("----【UserServiceDAOImpl.saveDataToMerchant】保存数据入参 : " + merchantAttr);
        //为了保险起见，再测检测reqList 是否有值
        if(merchantAttr == null){
            LoggerEngine.debug("----【UserServiceDAOImpl.saveDataToMerchant】保存数据出错 : " + merchantAttr);
            throw new IllegalArgumentException("请求参数错误，merchantAttr : " + merchantAttr);
        }
        int saveFlag = 0;

        saveFlag = sqlSessionTemplate.insert("userServiceDAOImpl.saveDataToMerchantAttr",merchantAttr);
        LoggerEngine.debug("----【UserServiceDAOImpl.saveDataToMerchant】保存数据出参 :saveFlag " + saveFlag);

        return saveFlag;
    }

    /**
     * 删除用户基本信息（实例数据）
     * @param merchant
     * @return
     * @throws RuntimeException
     */
    public int deleteDataToMerchant(Merchant merchant) throws RuntimeException{
        LoggerEngine.debug("----【UserServiceDAOImpl.deleteDataToMerchant】保存数据入参 : " + merchant);
        //为了保险起见，再测检测reqList 是否有值
        if(merchant == null){
            LoggerEngine.debug("----【UserServiceDAOImpl.deleteDataToMerchant】保存数据出错 : " + merchant);
            throw new IllegalArgumentException("请求参数错误，merchant : " + merchant);
        }
        int saveFlag = 0;

        saveFlag = sqlSessionTemplate.update("userServiceDAOImpl.deleteDataToMerchant",merchant);
        LoggerEngine.debug("----【UserServiceDAOImpl.deleteDataToMerchant】保存数据出参 :saveFlag " + saveFlag);

        return saveFlag;
    }

    /**
     *  删除用户属性（实例数据）
     * @param merchantAttr
     * @return
     * @throws RuntimeException
     */
    public int deleteDataToMerchantAttr(MerchantAttr merchantAttr) throws RuntimeException{
        LoggerEngine.debug("----【UserServiceDAOImpl.deleteDataToMerchantAttr】保存数据入参 : " + merchantAttr);
        //为了保险起见，再测检测reqList 是否有值
        if(merchantAttr == null){
            LoggerEngine.debug("----【UserServiceDAOImpl.deleteDataToMerchantAttr】保存数据出错 : " + merchantAttr);
            throw new IllegalArgumentException("请求参数错误，merchantAttr : " + merchantAttr);
        }
        int saveFlag = 0;

        saveFlag = sqlSessionTemplate.update("userServiceDAOImpl.deleteDataToMerchantAttr",merchantAttr);
        LoggerEngine.debug("----【UserServiceDAOImpl.deleteDataToMerchantAttr】保存数据出参 :saveFlag " + saveFlag);

        return saveFlag;
    }

    /**
     * 同时保存客户基本信息和客户属性
     * 入参为：
     *
     * @param boMerchantInfo 用户信息
     * @return
     * @throws RuntimeException
     */
    @Override
    public String saveDataToBoMerchantAndBoMerchantAttr(String boMerchantInfo) throws RuntimeException {

        LoggerEngine.debug("----【UserServiceDAOImpl.saveDataToBoMerchantAndBoMerchantAttr】保存数据入参"+boMerchantInfo);

        return null;
    }

    @Override
    public String saveDataToMerchantAndMerchantAttr(String merchantInfo) throws RuntimeException {
        return null;
    }

    @Override
    public String updateDataToMerchant(String merchant) throws RuntimeException {
        return null;
    }

    @Override
    public String updateDataToMerchantAttr(String merchantAttr) throws RuntimeException {
        return null;
    }

    @Override
    public String updateDataToMerchantAndMerchantAttr(String merchantInfo) throws RuntimeException {
        return null;
    }

    /**
     * 根据客户ID查询客户信息，包括基本信息和属性信息
     * @param merchant
     * @return
     * @throws RuntimeException
     */
    @Override
    public Merchant queryDataToMerchant(Merchant merchant) throws RuntimeException {
        LoggerEngine.debug("----【UserServiceDAOImpl.queryDataToMerchant】保存数据入参 : " + merchant);
        //为了保险起见，再测检测reqList 是否有值
        if(merchant == null){
            LoggerEngine.debug("----【UserServiceDAOImpl.queryDataToMerchant】保存数据出错 : " + merchant);
            throw new IllegalArgumentException("请求参数错误，merchant : " + merchant);
        }

        Merchant newMerchant  = sqlSessionTemplate.selectOne("userServiceDAOImpl.queryDataToMerchant",merchant);

        LoggerEngine.debug("----【UserServiceDAOImpl.queryDataToMerchant】保存数据出参 :newMerchant " + newMerchant);

        return newMerchant;
    }

    @Override
    public List<MerchantAttr> queryDataToMerchantAttr(MerchantAttr merchantAttr) throws RuntimeException {
        return null;
    }

    @Override
    public String queryDataToMerchantAndMerchantAttr(String merchantInfo) throws RuntimeException {
        return null;
    }

    /**
     *
     * 查询 客户基本信息（过程表bo_merchant）
     *
     * @param boMerchant
     * @return
     * @throws Exception
     */
    public List<BoMerchant> queryBoMerchant(BoMerchant boMerchant) throws Exception{
        LoggerEngine.debug("----【UserServiceDAOImpl.queryBoMerchantAttr】:"+boMerchant);

        Assert.isNull(boMerchant,"查询bo_merchant 入参为空");

        return sqlSessionTemplate.selectList("userServiceDAOImpl.queryBoMerchant",boMerchant);
    }

    /**
     *
     * 查询 客户属性信息（过程表 bo_merchant_attr）
     *
     * @param boMerchantAttr
     * @return
     * @throws Exception
     */
    public List<BoMerchantAttr> queryBoMerchantAttr(BoMerchantAttr boMerchantAttr) throws Exception{

        LoggerEngine.debug("【userServiceDAOImpl.queryBoMerchantAttr】:"+boMerchantAttr);

        Assert.isNull(boMerchantAttr,"查询bo_merchant_attr 入参为空");

        return sqlSessionTemplate.selectList("userServiceDAOImpl.queryBoMerchantAttr",boMerchantAttr);

    }
}
