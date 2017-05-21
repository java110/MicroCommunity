package com.java110.merchant.dao.impl;

import com.java110.common.log.LoggerEngine;
import com.java110.common.util.Assert;
import com.java110.core.base.dao.BaseServiceDao;
import com.java110.entity.product.BoProduct;
import com.java110.entity.product.BoProductAttr;
import com.java110.entity.product.Product;
import com.java110.entity.product.ProductAttr;
import com.java110.merchant.dao.IMerchantServiceDao;
import com.java110.merchant.dao.IProductServiceDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 用户服务 与数据库交互
 * Created by wuxw on 2017/4/5.
 */

/**
 * 用户信息实现工程
 * Created by wuxw on 2016/12/27.
 */
@Service("productServiceDaoImpl")
@Transactional
public class ProductServiceDaoImpl extends BaseServiceDao implements IProductServiceDao {



    /**
     * 保存用户基本信息
     * 功能只用与保存用户处理
     * @param boProduct 用户基本信息
     * @return
     */
    @Override
    public int saveDataToBoProduct(BoProduct boProduct) throws RuntimeException{

        LoggerEngine.debug("----【ProductServiceDAOImpl.saveDataToBoProduct】保存数据入参 : " + boProduct);
        int saveFlag = 0;
        try {

            saveFlag = sqlSessionTemplate.insert("productServiceDAOImpl.saveDataToBoProduct",boProduct);

        }catch(RuntimeException e){
            LoggerEngine.error("----【ProductServiceDAOImpl.saveDataToBoProduct】保存数据异常 : " ,e);
            return saveFlag;
        }finally {
            LoggerEngine.debug("----【ProductServiceDAOImpl.saveDataToBoProduct】保存数据出参 : saveFlag:" + saveFlag);
            return saveFlag;
        }

    }

    /**
     * 保存用户属性（过程表）
     *
     * @param boProductAttr 用户属性
     * @return
     * @throws RuntimeException
     */
    @Override
    public int saveDataToBoProductAttr(BoProductAttr boProductAttr) throws RuntimeException {

        LoggerEngine.debug("----【ProductServiceDAOImpl.saveDataToBoProductAttr】保存数据入参 : " + boProductAttr);
        //为了保险起见，再测检测reqList 是否有值
        if(boProductAttr == null){
            LoggerEngine.debug("----【ProductServiceDAOImpl.saveDataToBoProductAttr】保存数据出错 : " + boProductAttr);
            return 0;
        }
        int saveFlag = 0;

        saveFlag = sqlSessionTemplate.insert("productServiceDAOImpl.saveDataToBoProductAttr",boProductAttr);
        LoggerEngine.debug("----【ProductServiceDAOImpl.saveDataToBoProductAttr】保存数据出参 :saveFlag " + saveFlag);

        return saveFlag;

    }

    /**
     * 保存实例数据 客户信息至Product表中
     * @param product
     * @return
     * @throws RuntimeException
     */
    @Override
    public int saveDataToProduct(Product product) throws RuntimeException {
        LoggerEngine.debug("----【ProductServiceDAOImpl.saveDataToProduct】保存数据入参 : " + product);
        //为了保险起见，再测检测reqList 是否有值
        if(product == null){
            LoggerEngine.debug("----【ProductServiceDAOImpl.saveDataToProduct】保存数据出错 : " + product);
            throw new IllegalArgumentException("请求参数错误，product : " + product);
        }
        int saveFlag = 0;

        saveFlag = sqlSessionTemplate.insert("productServiceDAOImpl.saveDataToProduct",product);
        LoggerEngine.debug("----【ProductServiceDAOImpl.saveDataToProduct】保存数据出参 :saveFlag " + saveFlag);

        return saveFlag;
    }

    /**
     * 保存实例数据 客户属性信息至ProductAttr表中
     * @param productAttr
     * @return
     * @throws RuntimeException
     */
    @Override
    public int saveDataToProductAttr(ProductAttr productAttr) throws RuntimeException {
        LoggerEngine.debug("----【ProductServiceDAOImpl.saveDataToProduct】保存数据入参 : " + productAttr);
        //为了保险起见，再测检测reqList 是否有值
        if(productAttr == null){
            LoggerEngine.debug("----【ProductServiceDAOImpl.saveDataToProduct】保存数据出错 : " + productAttr);
            throw new IllegalArgumentException("请求参数错误，productAttr : " + productAttr);
        }
        int saveFlag = 0;

        saveFlag = sqlSessionTemplate.insert("productServiceDAOImpl.saveDataToProductAttr",productAttr);
        LoggerEngine.debug("----【ProductServiceDAOImpl.saveDataToProduct】保存数据出参 :saveFlag " + saveFlag);

        return saveFlag;
    }

    /**
     * 删除用户基本信息（实例数据）
     * @param product
     * @return
     * @throws RuntimeException
     */
    public int deleteDataToProduct(Product product) throws RuntimeException{
        LoggerEngine.debug("----【ProductServiceDAOImpl.deleteDataToProduct】保存数据入参 : " + product);
        //为了保险起见，再测检测reqList 是否有值
        if(product == null){
            LoggerEngine.debug("----【ProductServiceDAOImpl.deleteDataToProduct】保存数据出错 : " + product);
            throw new IllegalArgumentException("请求参数错误，product : " + product);
        }
        int saveFlag = 0;

        saveFlag = sqlSessionTemplate.update("productServiceDAOImpl.deleteDataToProduct",product);
        LoggerEngine.debug("----【ProductServiceDAOImpl.deleteDataToProduct】保存数据出参 :saveFlag " + saveFlag);

        return saveFlag;
    }

    /**
     *  删除用户属性（实例数据）
     * @param productAttr
     * @return
     * @throws RuntimeException
     */
    public int deleteDataToProductAttr(ProductAttr productAttr) throws RuntimeException{
        LoggerEngine.debug("----【ProductServiceDAOImpl.deleteDataToProductAttr】保存数据入参 : " + productAttr);
        //为了保险起见，再测检测reqList 是否有值
        if(productAttr == null){
            LoggerEngine.debug("----【ProductServiceDAOImpl.deleteDataToProductAttr】保存数据出错 : " + productAttr);
            throw new IllegalArgumentException("请求参数错误，productAttr : " + productAttr);
        }
        int saveFlag = 0;

        saveFlag = sqlSessionTemplate.update("productServiceDAOImpl.deleteDataToProductAttr",productAttr);
        LoggerEngine.debug("----【ProductServiceDAOImpl.deleteDataToProductAttr】保存数据出参 :saveFlag " + saveFlag);

        return saveFlag;
    }

    /**
     * 同时保存客户基本信息和客户属性
     * 入参为：
     *
     * @param boProductInfo 用户信息
     * @return
     * @throws RuntimeException
     */
    @Override
    public String saveDataToBoProductAndBoProductAttr(String boProductInfo) throws RuntimeException {

        LoggerEngine.debug("----【ProductServiceDAOImpl.saveDataToBoProductAndBoProductAttr】保存数据入参"+boProductInfo);

        return null;
    }

    @Override
    public String saveDataToProductAndProductAttr(String productInfo) throws RuntimeException {
        return null;
    }

    @Override
    public String updateDataToProduct(String product) throws RuntimeException {
        return null;
    }

    @Override
    public String updateDataToProductAttr(String productAttr) throws RuntimeException {
        return null;
    }

    @Override
    public String updateDataToProductAndProductAttr(String productInfo) throws RuntimeException {
        return null;
    }

    /**
     * 根据客户ID查询客户信息，包括基本信息和属性信息
     * @param product
     * @return
     * @throws RuntimeException
     */
    @Override
    public Product queryDataToProduct(Product product) throws RuntimeException {
        LoggerEngine.debug("----【ProductServiceDAOImpl.queryDataToProduct】保存数据入参 : " + product);
        //为了保险起见，再测检测reqList 是否有值
        if(product == null){
            LoggerEngine.debug("----【ProductServiceDAOImpl.queryDataToProduct】保存数据出错 : " + product);
            throw new IllegalArgumentException("请求参数错误，product : " + product);
        }

        Product newProduct  = sqlSessionTemplate.selectOne("productServiceDAOImpl.queryDataToProduct",product);

        LoggerEngine.debug("----【ProductServiceDAOImpl.queryDataToProduct】保存数据出参 :newProduct " + newProduct);

        return newProduct;
    }

    @Override
    public List<ProductAttr> queryDataToProductAttr(ProductAttr productAttr) throws RuntimeException {
        return null;
    }

    @Override
    public String queryDataToProductAndProductAttr(String productInfo) throws RuntimeException {
        return null;
    }

    /**
     *
     * 查询 客户基本信息（过程表bo_product）
     *
     * @param boProduct
     * @return
     * @throws Exception
     */
    public List<BoProduct> queryBoProduct(BoProduct boProduct) throws Exception{
        LoggerEngine.debug("----【ProductServiceDAOImpl.queryBoProductAttr】:"+boProduct);

        Assert.isNull(boProduct,"查询bo_product 入参为空");

        return sqlSessionTemplate.selectList("productServiceDAOImpl.queryBoProduct",boProduct);
    }

    /**
     *
     * 查询 客户属性信息（过程表 bo_product_attr）
     *
     * @param boProductAttr
     * @return
     * @throws Exception
     */
    public List<BoProductAttr> queryBoProductAttr(BoProductAttr boProductAttr) throws Exception{

        LoggerEngine.debug("【productServiceDAOImpl.queryBoProductAttr】:"+boProductAttr);

        Assert.isNull(boProductAttr,"查询bo_product_attr 入参为空");

        return sqlSessionTemplate.selectList("productServiceDAOImpl.queryBoProductAttr",boProductAttr);

    }
}
