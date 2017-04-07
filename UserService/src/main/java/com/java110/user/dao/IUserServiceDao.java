package com.java110.user.dao;

/**
 * 用户组件内部之间使用，没有给外围系统提供服务能力
 * 用户服务接口类，要求全部以字符串传输，方便微服务化
 * 新建客户，修改客户，删除客户，查询客户等功能
 *
 * Created by wuxw on 2016/12/27.
 */
public interface IUserServiceDao {

    /**
     * 保存用户基本信息(过程表)
     * @param boCust 用户基本信息
     * @return
     */
    public String saveDataToBoCust(String boCust) throws RuntimeException;

    /**
     * 保存用户属性(过程表)
     * @param boCustAttr 用户属性
     * @return
     * @throws RuntimeException
     */
    public String saveDataToBoCustAttr(String boCustAttr) throws RuntimeException;

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
    public String saveDataToCust(String cust) throws RuntimeException;

    /**
     *  保存用户属性
     * @param custAttr
     * @return
     * @throws RuntimeException
     */
    public String saveDataToCustAttr(String custAttr) throws RuntimeException;

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
    public String queryDataToCust(String cust) throws RuntimeException;

    /**
     *  查询用户属性
     * @param custAttr
     * @return
     * @throws RuntimeException
     */
    public String queryDataToCustAttr(String custAttr) throws RuntimeException;

    /**
     * 查询保存用户基本信息和属性
     * @param custInfo
     * @return
     * @throws RuntimeException
     */
    public String queryDataToCustAndCustAttr(String custInfo) throws RuntimeException;


}