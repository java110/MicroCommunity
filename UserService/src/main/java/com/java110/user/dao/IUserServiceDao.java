package com.java110.user.dao;

import com.alibaba.fastjson.JSONObject;
import com.java110.common.exception.DAOException;
import com.java110.common.log.LoggerEngine;
import com.java110.common.util.Assert;
import com.java110.entity.user.BoCust;
import com.java110.entity.user.BoCustAttr;
import com.java110.entity.user.Cust;
import com.java110.entity.user.CustAttr;

import java.util.List;
import java.util.Map;

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


    /**
     * 保存用户信息
     * @param userInfo
     * @throws DAOException
     */
    public void saveBusinessUserInfo(Map userInfo) throws DAOException;

    /**
     * 保存用户属性
     * @param userAttr
     * @throws DAOException
     */
    public void saveBusinessUserAttr(Map userAttr) throws DAOException;





    public void saveUserInfoInstance(Map businessUser);

    public void saveUserAttrInstance(Map attrInstance);

    public void updateUserInfoInstance(Map businessUser);

    public void updateUserAttrInstance(Map attrInstance);

    /**
     * 查询用户信息
     * @param info
     * @return
     * @throws DAOException
     */
    public Map queryBusinessUserInfo(Map info) throws DAOException;

    /**
     * 查询用户信息
     * @param info
     * @return
     * @throws DAOException
     */
    public List<Map> queryBusinessUserInfoAttrs(Map info) throws DAOException;

    /**
     * 查询用户信息
     * @param info
     * @return
     * @throws DAOException
     */
    public Map queryUserInfo(Map info) throws DAOException;

    /**
     * 查询用户信息
     * @param info
     * @return
     * @throws DAOException
     */
    public List<Map> queryUserInfoAttrs(Map info) throws DAOException;


    /**
     * 保存用户地址信息
     * Business 过程
     * @param userAddress 用户地址信息
     * @throws DAOException
     */
    public void saveBusinessUserAddress(Map userAddress) throws DAOException;


    /**
     * 查询用户地址信息
     * business 过程
     * @param info b_id
     * @return 查询到的用户地址信息
     * @throws DAOException
     */
    public Map queryBusinessUserAddress(Map info) throws DAOException;

    /**
     * 保存Business 数据到 Instance
     * @param businessUserAddress 从business 中查出的数据
     * @throws DAOException 数据处理异常
     */
    public void saveUserAddressInstance(Map businessUserAddress) throws DAOException;


    /**
     * 作废用户信息数据
     * @param businessUserAddress 用户地址信息 b_id
     * @throws DAOException 数据处理异常
     */
    public void updateUserAddressInstance(Map businessUserAddress) throws DAOException;


    /**
     * 保存用户打标信息
     * Business 过程
     * @param userTag 用户打标信息
     * @throws DAOException
     */
    public void saveBusinessUserTag(Map userTag) throws DAOException;


    /**
     * 查询用户打标信息
     * business 过程
     * @param info b_id
     * @return 查询到的用户打标信息
     * @throws DAOException
     */
    public Map queryBusinessUserTag(Map info) throws DAOException;

    /**
     * 保存Business 数据到 Instance
     * @param businessUserTag 从business 中查出的数据
     * @throws DAOException 数据处理异常
     */
    public void saveUserTagInstance(Map businessUserTag) throws DAOException;


    /**
     * 作废用户打标数据
     * @param businessUserTag 用户地址信息 b_id
     * @throws DAOException 数据处理异常
     */
    public void updateUserTagInstance(Map businessUserTag) throws DAOException;



    /**
     * 保存用户证件信息
     * Business 过程
     * @param userCredentials 用户证件信息
     * @throws DAOException
     */
    public void saveBusinessUserCredentials(Map userCredentials) throws DAOException;


    /**
     * 查询用户证件信息
     * business 过程
     * @param info b_id
     * @return 查询到的用户打标信息
     * @throws DAOException
     */
    public Map queryBusinessUserCredentials(Map info) throws DAOException;

    /**
     * 保存Business 数据到 Instance
     * @param businessUserCredentials 从business 中查出的数据
     * @throws DAOException 数据处理异常
     */
    public void saveUserCredentialsInstance(Map businessUserCredentials) throws DAOException;


    /**
     * 作废用户证件数据
     * @param businessUserCredentials 用户地址信息 b_id
     * @throws DAOException 数据处理异常
     */
    public void updateUserCredentialsInstance(Map businessUserCredentials) throws DAOException;
}