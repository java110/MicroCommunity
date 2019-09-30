package com.java110.store.dao;


import com.java110.utils.exception.DAOException;


import java.util.List;
import java.util.Map;

/**
 * demo组件内部之间使用，没有给外围系统提供服务能力
 * demo服务接口类，要求全部以字符串传输，方便微服务化
 * 新建客户，修改客户，删除客户，查询客户等功能
 *
 * Created by wuxw on 2016/12/27.
 */
public interface IDemoServiceDao {

    /**
     * 保存 demo信息
     * @param businessDemoInfo demo信息 封装
     * @throws DAOException 操作数据库异常
     */
    void saveBusinessDemoInfo(Map businessDemoInfo) throws DAOException;



    /**
     * 查询demo信息（business过程）
     * 根据bId 查询demo信息
     * @param info bId 信息
     * @return demo信息
     * @throws DAOException DAO异常
     */
    List<Map> getBusinessDemoInfo(Map info) throws DAOException;




    /**
     * 保存 demo信息 Business数据到 Instance中
     * @param info
     * @throws DAOException DAO异常
     */
    void saveDemoInfoInstance(Map info) throws DAOException;




    /**
     * 查询demo信息（instance过程）
     * 根据bId 查询demo信息
     * @param info bId 信息
     * @return demo信息
     * @throws DAOException DAO异常
     */
    List<Map> getDemoInfo(Map info) throws DAOException;



    /**
     * 修改demo信息
     * @param info 修改信息
     * @throws DAOException DAO异常
     */
    void updateDemoInfoInstance(Map info) throws DAOException;


    /**
     * 查询demo总数
     *
     * @param info demo信息
     * @return demo数量
     */
    int queryDemosCount(Map info);

}
