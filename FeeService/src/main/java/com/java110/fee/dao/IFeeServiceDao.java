package com.java110.fee.dao;


import com.java110.common.exception.DAOException;

import java.util.List;
import java.util.Map;

/**
 * 小区组件内部之间使用，没有给外围系统提供服务能力
 * 小区服务接口类，要求全部以字符串传输，方便微服务化
 * 新建客户，修改客户，删除客户，查询客户等功能
 *
 * Created by wuxw on 2016/12/27.
 */
public interface IFeeServiceDao {

    /**
     * 保存 小区信息
     * @param businessFeeInfo 小区信息 封装
     * @throws DAOException 操作数据库异常
     */
     void saveBusinessFeeInfo(Map businessFeeInfo) throws DAOException;

    /**
     * 保存小区属性
     * @param businessFeeAttr 小区属性信息封装
     * @throws DAOException 操作数据库异常
     */
     void saveBusinessFeeAttr(Map businessFeeAttr) throws DAOException;


    /**
     * 保存小区照片信息
     * @param businessFeePhoto 小区照片
     * @throws DAOException 操作数据库异常
     */
     void saveBusinessFeePhoto(Map businessFeePhoto) throws DAOException;

    /**
     * 保存小区证件信息
     * @param businessFeeCerdentials 小区证件
     * @throws DAOException 操作数据库异常
     */
     void saveBusinessFeeCerdentials(Map businessFeeCerdentials) throws DAOException;

    /**
     * 查询小区信息（business过程）
     * 根据bId 查询小区信息
     * @param info bId 信息
     * @return 小区信息
     * @throws DAOException
     */
     Map getBusinessFeeInfo(Map info) throws DAOException;


    /**
     * 查询小区属性信息（business过程）
     * @param info bId 信息
     * @return 小区属性
     * @throws DAOException
     */
     List<Map> getBusinessFeeAttrs(Map info) throws DAOException;


    /**
     * 查询小区照片
     * @param info bId 信息
     * @return 小区照片
     * @throws DAOException
     */
     List<Map> getBusinessFeePhoto(Map info) throws DAOException;


    /**
     * 查询小区证件信息
     * @param info bId 信息
     * @return 小区照片
     * @throws DAOException
     */
     List<Map> getBusinessFeeCerdentials(Map info) throws DAOException;

    /**
     * 保存 小区信息 Business数据到 Instance中
     * @param info
     * @throws DAOException
     */
     void saveFeeInfoInstance(Map info) throws DAOException;


    /**
     * 保存 小区属性信息 Business数据到 Instance中
     * @param info
     * @throws DAOException
     */
     void saveFeeAttrsInstance(Map info) throws DAOException;

    /**
     * 保存 小区照片信息 Business数据到 Instance中
     * @param info
     * @throws DAOException
     */
     void saveFeePhotoInstance(Map info) throws DAOException;






    /**
     * 查询小区信息（instance过程）
     * 根据bId 查询小区信息
     * @param info bId 信息
     * @return 小区信息
     * @throws DAOException
     */
     Map getFeeInfo(Map info) throws DAOException;


    /**
     * 查询小区属性信息（instance过程）
     * @param info bId 信息
     * @return 小区属性
     * @throws DAOException
     */
     List<Map> getFeeAttrs(Map info) throws DAOException;


    /**
     * 查询小区照片（instance 过程）
     * @param info bId 信息
     * @return 小区照片
     * @throws DAOException
     */
     List<Map> getFeePhoto(Map info) throws DAOException;



    /**
     * 修改小区信息
     * @param info 修改信息
     * @throws DAOException
     */
     void updateFeeInfoInstance(Map info) throws DAOException;


    /**
     * 修改小区属性信息
     * @param info 修改信息
     * @throws DAOException
     */
     void updateFeeAttrInstance(Map info) throws DAOException;


    /**
     * 修改小区照片信息
     * @param info 修改信息
     * @throws DAOException
     */
     void updateFeePhotoInstance(Map info) throws DAOException;

    


    /**
     * 小区成员加入信息
     * @param businessFeeMember 小区成员信息 封装
     * @throws DAOException 操作数据库异常
     */
     void saveBusinessFeeMember(Map businessFeeMember) throws DAOException;

    /**
     * 成员加入 保存信息至instance
     * @param info
     * @throws DAOException
     */
     void saveFeeMemberInstance(Map info) throws DAOException;

    /**
     * 查询小区成员加入信息（business过程）
     * 根据bId 查询小区信息
     * @param info bId 信息
     * @return 小区信息
     * @throws DAOException
     */
     Map getBusinessFeeMember(Map info) throws DAOException;

    /**
     * 查询小区成员加入信息（instance过程）
     * 根据bId 查询小区信息
     * @param info bId 信息
     * @return 小区信息
     * @throws DAOException
     */
     Map getFeeMember(Map info) throws DAOException;

    /**
     * 修改小区成员加入信息
     * @param info 修改信息
     * @throws DAOException
     */
     void updateFeeMemberInstance(Map info) throws DAOException;

    /**
     * 查询小区成员加入信息（instance过程）
     * 根据bId 查询小区信息
     * @param info bId 信息
     * @return 小区信息
     * @throws DAOException
     */
     List<Map> getFeeMembers(Map info) throws DAOException;

    /**
     * 查询小区成员个数
     * 根据bId 查询小区信息
     * @param info bId 信息
     * @return 小区信息
     * @throws DAOException
     */
     int getFeeMemberCount(Map info);

}