package com.java110.community.dao;


import com.java110.dto.UnitDto;
import com.java110.utils.exception.DAOException;


import java.util.List;
import java.util.Map;

/**
 * 小区楼组件内部之间使用，没有给外围系统提供服务能力
 * 小区楼服务接口类，要求全部以字符串传输，方便微服务化
 * 新建客户，修改客户，删除客户，查询客户等功能
 * <p>
 * Created by wuxw on 2016/12/27.
 */
public interface IFloorServiceDao {

    /**
     * 保存 小区楼信息
     *
     * @param businessFloorInfo 小区楼信息 封装
     * @throws DAOException 操作数据库异常
     */
    void saveBusinessFloorInfo(Map businessFloorInfo) throws DAOException;


    /**
     * 查询小区楼信息（business过程）
     * 根据bId 查询小区楼信息
     *
     * @param info bId 信息
     * @return 小区楼信息
     * @throws DAOException 异常信息
     */
    List<Map> getBusinessFloorInfo(Map info) throws DAOException;


    /**
     * 保存 小区楼信息 Business数据到 Instance中
     *
     * @param info 信息
     * @throws DAOException 异常信息
     */
    void saveFloorInfoInstance(Map info) throws DAOException;


    /**
     * 查询小区楼信息（instance过程）
     * 根据bId 查询小区楼信息
     *
     * @param info bId 信息
     * @return 小区楼信息
     * @throws DAOException
     */
    List<Map> getFloorInfo(Map info) throws DAOException;


    /**
     * 修改小区楼信息
     *
     * @param info 修改信息
     * @throws DAOException
     */
    void updateFloorInfoInstance(Map info) throws DAOException;


    /**
     * 根据小区ID查询 小区楼数量
     *
     * @param communitId 小区ID
     * @return 小区楼数量
     * @throws DAOException 数据库异常信息
     */
    int queryFloorsCount(String communitId) throws DAOException;

    /**
     * 查询小区楼信息
     *
     * @param floorMap 查询条件
     * @return 小区楼列表集合
     * @throws DAOException 数据库操作异常
     */
    List<Map> queryFloors(Map floorMap) throws DAOException;

    /**
     * 根据小区ID查询 小区楼数量
     *
     * @param info 小区ID
     * @return 小区楼数量
     * @throws DAOException 数据库异常信息
     */
    int queryFloorsCount(Map info) throws DAOException;


    List<Map> queryFloorAndUnits(Map info);
}
