package com.java110.merchant.dao;

import com.java110.entity.product.BoProduct;
import com.java110.entity.product.BoProductAttr;
import com.java110.entity.product.Product;
import com.java110.entity.product.ProductAttr;

import java.util.List;

/**
 * 用户组件内部之间使用，没有给外围系统提供服务能力
 * 用户服务接口类，要求全部以字符串传输，方便微服务化
 * 新建客户，修改客户，删除客户，查询客户等功能
 *
 * Created by wuxw on 2016/12/27.
 */
public interface IProductServiceDao {

    /**
     * 保存用户基本信息(过程表)
     * @param boProduct 用户基本信息
     * @return
     */
    public int saveDataToBoProduct(BoProduct boProduct) throws RuntimeException;

    /**
     * 保存用户属性(过程表)
     * @param boProductAttr 用户属性
     * @return
     * @throws RuntimeException
     */
    public int saveDataToBoProductAttr(BoProductAttr boProductAttr) throws RuntimeException ;

    /**
     *  同事保存用户基本信息和属性(过程表)
     * @param boProductInfo 用户信息
     * @return
     * @throws RuntimeException
     */
    public String saveDataToBoProductAndBoProductAttr(String boProductInfo) throws RuntimeException;

    /**
     * 保存用户基本信息
     * @param product
     * @return
     * @throws RuntimeException
     */
    public int saveDataToProduct(Product product) throws RuntimeException;

    /**
     *  保存用户属性
     * @param productAttr
     * @return
     * @throws RuntimeException
     */
    public int saveDataToProductAttr(ProductAttr productAttr) throws RuntimeException;

    /**
     * 删除用户基本信息（实例数据）
     * @param product
     * @return
     * @throws RuntimeException
     */
    public int deleteDataToProduct(Product product) throws RuntimeException;

    /**
     *  删除用户属性（实例数据）
     * @param productAttr
     * @return
     * @throws RuntimeException
     */
    public int deleteDataToProductAttr(ProductAttr productAttr) throws RuntimeException;

    /**
     * 同事保存用户基本信息和属性
     * @param productInfo
     * @return
     * @throws RuntimeException
     */
    public String saveDataToProductAndProductAttr(String productInfo) throws RuntimeException;


    /**
     * 更新用户基本信息
     * @param product
     * @return
     * @throws RuntimeException
     */
    public String updateDataToProduct(String product) throws RuntimeException;

    /**
     *  更新用户属性
     * @param productAttr
     * @return
     * @throws RuntimeException
     */
    public String updateDataToProductAttr(String productAttr) throws RuntimeException;

    /**
     * 同事更新用户基本信息和属性
     * @param productInfo
     * @return
     * @throws RuntimeException
     */
    public String updateDataToProductAndProductAttr(String productInfo) throws RuntimeException;



    /**
     * 查询用户基本信息（一般没用，就算有用）
     * @param product
     * @return
     * @throws RuntimeException
     */
    public Product queryDataToProduct(Product product) throws RuntimeException ;



    /**
     *  查询用户属性
     * @param productAttr
     * @return
     * @throws RuntimeException
     */
    public List<ProductAttr> queryDataToProductAttr(ProductAttr productAttr) throws RuntimeException;

    /**
     * 查询保存用户基本信息和属性
     * @param productInfo
     * @return
     * @throws RuntimeException
     */
    public String queryDataToProductAndProductAttr(String productInfo) throws RuntimeException;

    /**
     *
     * 查询 客户基本信息（过程表bo_product）
     *
     * @param boProduct
     * @return
     * @throws Exception
     */
    public List<BoProduct> queryBoProduct(BoProduct boProduct) throws Exception;

    /**
     *
     * 查询 客户属性信息（过程表 bo_product_attr）
     *
     * @param boProductAttr
     * @return
     * @throws Exception
     */
    public List<BoProductAttr> queryBoProductAttr(BoProductAttr boProductAttr) throws Exception;


}