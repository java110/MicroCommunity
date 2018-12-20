package com.java110.property.dao;


import com.java110.common.exception.DAOException;

import java.util.List;
import java.util.Map;

/**
 * 物业组件内部之间使用，没有给外围系统提供服务能力
 * 物业服务接口类，要求全部以字符串传输，方便微服务化
 * 新建客户，修改客户，删除客户，查询客户等功能
 *
 * Created by wuxw on 2016/12/27.
 */
public interface IPropertyServiceDao {

    /**
     * 保存 物业信息
     * @param businessPropertyInfo 物业信息 封装
     * @throws DAOException 操作数据库异常
     */
    public void saveBusinessPropertyInfo(Map businessPropertyInfo) throws DAOException;

    /**
     * 保存物业属性
     * @param businessPropertyAttr 物业属性信息封装
     * @throws DAOException 操作数据库异常
     */
    public void saveBusinessPropertyAttr(Map businessPropertyAttr) throws DAOException;


    /**
     * 保存物业照片信息
     * @param businessPropertyPhoto 物业照片
     * @throws DAOException 操作数据库异常
     */
    public void saveBusinessPropertyPhoto(Map businessPropertyPhoto) throws DAOException;

    /**
     * 保存物业证件信息
     * @param businessPropertyCerdentials 物业证件
     * @throws DAOException 操作数据库异常
     */
    public void saveBusinessPropertyCerdentials(Map businessPropertyCerdentials) throws DAOException;


    /**
     * 保存物业用户信息
     * @param info
     * @throws DAOException
     */
    public void saveBusinessPropertyUser(Map info) throws DAOException;

    /**
     * 保存物业费用信息
     * @param info
     * @throws DAOException
     */
    public void saveBusinessPropertyFee(Map info) throws DAOException;
    /**
     * 查询物业信息（business过程）
     * 根据bId 查询物业信息
     * @param info bId 信息
     * @return 物业信息
     * @throws DAOException
     */
    public Map getBusinessPropertyInfo(Map info) throws DAOException;


    /**
     * 查询物业属性信息（business过程）
     * @param info bId 信息
     * @return 物业属性
     * @throws DAOException
     */
    public List<Map> getBusinessPropertyAttrs(Map info) throws DAOException;


    /**
     * 查询物业照片
     * @param info bId 信息
     * @return 物业照片
     * @throws DAOException
     */
    public List<Map> getBusinessPropertyPhoto(Map info) throws DAOException;


    /**
     * 查询物业证件信息
     * @param info bId 信息
     * @return 物业照片
     * @throws DAOException
     */
    public List<Map> getBusinessPropertyCerdentials(Map info) throws DAOException;

    /**
     * 查询物业用户信息
     * @param info bId 信息
     * @return 物业照片
     * @throws DAOException
     */
    public List<Map> getBusinessPropertyUser(Map info) throws DAOException;

    /**
     * 查询物业费用信息
     * @param info bId 信息
     * @return 物业照片
     * @throws DAOException
     */
    public List<Map> getBusinessPropertyFee(Map info) throws DAOException;
    /**
     * 保存 物业信息 Business数据到 Instance中
     * @param info
     * @throws DAOException
     */
    public void savePropertyInfoInstance(Map info) throws DAOException;


    /**
     * 保存 物业属性信息 Business数据到 Instance中
     * @param info
     * @throws DAOException
     */
    public void savePropertyAttrsInstance(Map info) throws DAOException;

    /**
     * 保存 物业照片信息 Business数据到 Instance中
     * @param info
     * @throws DAOException
     */
    public void savePropertyPhotoInstance(Map info) throws DAOException;


    /**
     * 保存 物业证件信息 Business数据到 Instance中
     * @param info
     * @throws DAOException
     */
    public void savePropertyCerdentialsInstance(Map info) throws DAOException;


    /**
     * 保存 物业用户信息 Business数据到 Instance中
     * @param info
     * @throws DAOException
     */
    public void savePropertyUserInstance(Map info) throws DAOException;

    /**
     * 保存 物业费用信息 Business数据到 Instance中
     * @param info
     * @throws DAOException
     */
    public void savePropertyFeeInstance(Map info) throws DAOException;

    /**
     * 查询物业信息（instance过程）
     * 根据bId 查询物业信息
     * @param info bId 信息
     * @return 物业信息
     * @throws DAOException
     */
    public Map getPropertyInfo(Map info) throws DAOException;


    /**
     * 查询物业属性信息（instance过程）
     * @param info bId 信息
     * @return 物业属性
     * @throws DAOException
     */
    public List<Map> getPropertyAttrs(Map info) throws DAOException;


    /**
     * 查询物业照片（instance 过程）
     * @param info bId 信息
     * @return 物业照片
     * @throws DAOException
     */
    public List<Map> getPropertyPhoto(Map info) throws DAOException;

    /**
     * 查询物业证件信息（instance 过程）
     * @param info bId 信息
     * @return 物业照片
     * @throws DAOException
     */
    public List<Map> getPropertyCerdentials(Map info) throws DAOException;

    /**
     * 查询物业用户信息（instance 过程）
     * @param info bId 信息
     * @return 物业照片
     * @throws DAOException
     */
    public List<Map> getPropertyUser(Map info) throws DAOException;

    /**
     * 查询物业费用信息（instance 过程）
     * @param info bId 信息
     * @return 物业照片
     * @throws DAOException
     */
    public List<Map> getPropertyFee(Map info) throws DAOException;

    /**
     * 修改物业信息
     * @param info 修改信息
     * @throws DAOException
     */
    public void updatePropertyInfoInstance(Map info) throws DAOException;


    /**
     * 修改物业属性信息
     * @param info 修改信息
     * @throws DAOException
     */
    public void updatePropertyAttrInstance(Map info) throws DAOException;


    /**
     * 修改物业照片信息
     * @param info 修改信息
     * @throws DAOException
     */
    public void updatePropertyPhotoInstance(Map info) throws DAOException;

    /**
     * 修改物业证件信息
     * @param info 修改信息
     * @throws DAOException
     */
    public void updatePropertyCerdentailsInstance(Map info) throws DAOException;


    /**
     * 修改物业用户信息
     * @param info 修改信息
     * @throws DAOException
     */
    public void updatePropertyUserInstance(Map info) throws DAOException;



    /**
     * 修改物业费用信息
     * @param info 修改信息
     * @throws DAOException
     */
    public void updatePropertyFeeInstance(Map info) throws DAOException;



}