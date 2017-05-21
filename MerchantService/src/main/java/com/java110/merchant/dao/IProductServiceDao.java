package com.java110.merchant.dao;

import com.java110.entity.user.BoCust;
import com.java110.entity.user.BoCustAttr;
import com.java110.entity.user.Cust;
import com.java110.entity.user.CustAttr;

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
     * @param boCust 用户基本信息
     * @return
     */
    public int saveDataToBoCust(BoCust boCust) throws RuntimeException;

    /**
     * 保存用户属性(过程表)
     * @param boCustAttr 用户属性
     * @return
     * @throws RuntimeException
     */
    public int saveDataToBoCustAttr(BoCustAttr boCustAttr) throws RuntimeException ;

    /**
     *  同事保存用户基本信息和属性(过程表)
     * @param boCustInfo 用户信息
     * @return
     * @throws RuntimeException
     */
    public String saveDataToBoCustAndBoCustAttr(String boCustInfo) throws RuntimeException;

    /**
     * 保存用户基本信息
     * @param cust
     * @return
     * @throws RuntimeException
     */
    public int saveDataToCust(Cust cust) throws RuntimeException;

    /**
     *  保存用户属性
     * @param custAttr
     * @return
     * @throws RuntimeException
     */
    public int saveDataToCustAttr(CustAttr custAttr) throws RuntimeException;

    /**
     * 删除用户基本信息（实例数据）
     * @param cust
     * @return
     * @throws RuntimeException
     */
    public int deleteDataToCust(Cust cust) throws RuntimeException;

    /**
     *  删除用户属性（实例数据）
     * @param custAttr
     * @return
     * @throws RuntimeException
     */
    public int deleteDataToCustAttr(CustAttr custAttr) throws RuntimeException;

    /**
     * 同事保存用户基本信息和属性
     * @param custInfo
     * @return
     * @throws RuntimeException
     */
    public String saveDataToCustAndCustAttr(String custInfo) throws RuntimeException;


    /**
     * 更新用户基本信息
     * @param cust
     * @return
     * @throws RuntimeException
     */
    public String updateDataToCust(String cust) throws RuntimeException;

    /**
     *  更新用户属性
     * @param custAttr
     * @return
     * @throws RuntimeException
     */
    public String updateDataToCustAttr(String custAttr) throws RuntimeException;

    /**
     * 同事更新用户基本信息和属性
     * @param custInfo
     * @return
     * @throws RuntimeException
     */
    public String updateDataToCustAndCustAttr(String custInfo) throws RuntimeException;



    /**
     * 查询用户基本信息（一般没用，就算有用）
     * @param cust
     * @return
     * @throws RuntimeException
     */
    public Cust queryDataToCust(Cust cust) throws RuntimeException ;



    /**
     *  查询用户属性
     * @param custAttr
     * @return
     * @throws RuntimeException
     */
    public List<CustAttr> queryDataToCustAttr(CustAttr custAttr) throws RuntimeException;

    /**
     * 查询保存用户基本信息和属性
     * @param custInfo
     * @return
     * @throws RuntimeException
     */
    public String queryDataToCustAndCustAttr(String custInfo) throws RuntimeException;

    /**
     *
     * 查询 客户基本信息（过程表bo_cust）
     *
     * @param boCust
     * @return
     * @throws Exception
     */
    public List<BoCust> queryBoCust(BoCust boCust) throws Exception;

    /**
     *
     * 查询 客户属性信息（过程表 bo_cust_attr）
     *
     * @param boCustAttr
     * @return
     * @throws Exception
     */
    public List<BoCustAttr> queryBoCustAttr(BoCustAttr boCustAttr) throws Exception;


}