package com.java110.common.dao;


import com.java110.utils.exception.DAOException;

import java.util.List;
import java.util.Map;

/**
 * 设备属性组件内部之间使用，没有给外围系统提供服务能力
 * 设备属性服务接口类，要求全部以字符串传输，方便微服务化
 * 新建客户，修改客户，删除客户，查询客户等功能
 *
 * Created by wuxw on 2016/12/27.
 */
public interface IMachineAttrServiceDao {

    /**
     * 保存 设备属性信息
     * @param businessMachineAttrInfo 设备属性信息 封装
     * @throws DAOException 操作数据库异常
     */
    void saveBusinessMachineAttrInfo(Map businessMachineAttrInfo) throws DAOException;



    /**
     * 查询设备属性信息（business过程）
     * 根据bId 查询设备属性信息
     * @param info bId 信息
     * @return 设备属性信息
     * @throws DAOException DAO异常
     */
    List<Map> getBusinessMachineAttrInfo(Map info) throws DAOException;




    /**
     * 保存 设备属性信息 Business数据到 Instance中
     * @param info
     * @throws DAOException DAO异常
     */
    void saveMachineAttrInfoInstance(Map info) throws DAOException;




    /**
     * 查询设备属性信息（instance过程）
     * 根据bId 查询设备属性信息
     * @param info bId 信息
     * @return 设备属性信息
     * @throws DAOException DAO异常
     */
    List<Map> getMachineAttrInfo(Map info) throws DAOException;



    /**
     * 修改设备属性信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    void updateMachineAttrInfoInstance(Map info) throws DAOException;


    /**
     * 查询设备属性总数
     *
     * @param info 设备属性信息
     * @return 设备属性数量
     */
    int queryMachineAttrsCount(Map info);

    int saveMachineAttrs(Map info);

    int updateMachineAttrs(Map info);
}
