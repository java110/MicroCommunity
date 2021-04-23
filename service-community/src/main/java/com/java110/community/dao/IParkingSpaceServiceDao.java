package com.java110.community.dao;


import com.java110.utils.exception.DAOException;


import java.util.List;
import java.util.Map;

/**
 * 停车位组件内部之间使用，没有给外围系统提供服务能力
 * 停车位服务接口类，要求全部以字符串传输，方便微服务化
 * 新建客户，修改客户，删除客户，查询客户等功能
 * <p>
 * Created by wuxw on 2016/12/27.
 */
public interface IParkingSpaceServiceDao {

    /**
     * 保存 停车位信息
     *
     * @param businessParkingSpaceInfo 停车位信息 封装
     * @throws DAOException 操作数据库异常
     */
    void saveBusinessParkingSpaceInfo(Map businessParkingSpaceInfo) throws DAOException;


    /**
     * 查询停车位信息（business过程）
     * 根据bId 查询停车位信息
     *
     * @param info bId 信息
     * @return 停车位信息
     * @throws DAOException DAO异常
     */
    List<Map> getBusinessParkingSpaceInfo(Map info) throws DAOException;


    /**
     * 保存 停车位信息 Business数据到 Instance中
     *
     * @param info
     * @throws DAOException DAO异常
     */
    void saveParkingSpaceInfoInstance(Map info) throws DAOException;


    /**
     * 查询停车位信息（instance过程）
     * 根据bId 查询停车位信息
     *
     * @param info bId 信息
     * @return 停车位信息
     * @throws DAOException DAO异常
     */
    List<Map> getParkingSpaceInfo(Map info) throws DAOException;


    /**
     * 修改停车位信息
     *
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    void updateParkingSpaceInfoInstance(Map info) throws DAOException;


    /**
     * 查询停车位总数
     *
     * @param info 停车位信息
     * @return 停车位数量
     */
    int queryParkingSpacesCount(Map info);

    int saveParkingSpace(Map info);

}
